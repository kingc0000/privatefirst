package com.kekeinfo.web.admin.controller.gjob;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.job.service.GJobService;
import com.kekeinfo.core.business.monitor.model.MonitorUser;
import com.kekeinfo.core.business.monitor.service.MonitorUserService;
import com.kekeinfo.core.business.sign.model.Sign;
import com.kekeinfo.core.business.sign.service.SignService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.admin.entity.web.PaginationData;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.GJobEntity;
import com.kekeinfo.web.entity.GuardEntity;
import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.entity.Plan;
import com.kekeinfo.web.entity.User;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.DateUtil;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PageBuilderUtils;
import com.kekeinfo.web.utils.PinyinUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class GJobController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GJobController.class);
	@Autowired
	GJobService gjobService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired MonitorUserService monitorUserService;
	@Autowired SignService signService;
	@Autowired UserService userService;
	@Autowired private PNodeUtils pnodeUtils;

	@PreAuthorize("hasRole('GJOB')")
	@RequestMapping(value = "/water/gjob/list.html", method = RequestMethod.GET)
	public String listgjobs(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		GJob gjob = new GJob();
		model.addAttribute("gjob", gjob);
		return "admin-gjob";
	}
	
	@RequestMapping(value = "/water/gjob/signin.html", method = RequestMethod.GET)
	public String signin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		com.kekeinfo.core.business.user.model.User user = (com.kekeinfo.core.business.user.model.User)request.getSession().getAttribute(Constants.ADMIN_USER);
		List<Sign> signs =signService.getByUidToday(user.getId());
		model.addAttribute("signs", signs);
		return "admin-gjob-signin";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/water/gjob/signout.html", method = RequestMethod.GET)
	public String signout(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		com.kekeinfo.core.business.user.model.User user = (com.kekeinfo.core.business.user.model.User)request.getSession().getAttribute(Constants.ADMIN_USER);
		//先查是否已经销点
		List<GJob> gjobs = gjobService.getToday(new Date(), user.getId());
		if(gjobs!=null){
			List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
			for(GJob gjob:gjobs){
				List<Sign> signs =signService.getByJIdToday(gjob.getId());
				//有要点
				if(signs !=null && signs.size()>0){
					Sign signout =null;
					Sign signin =null;
					for(Sign sign:signs){
						if(sign.getStype()==0){
							signin=sign;
						}else{
							signout=sign;
						}
					}
					if(signout==null){
						Date date2=DateUtil.timeDate(gjob.getArriveDate());
						if(date2.getTime()>=signin.getAuditSection().getDateCreated().getTime()){
							model.addAttribute("color", "#048E13");
						}else{
							model.addAttribute("color", "#EA1D06");
						}
						model.addAttribute("gjob", gjob);
						model.addAttribute("sign", signin);
						for(GuardEntity m:listen){
							if(m.getId().equals(gjob.getGuard().getId())){
								model.addAttribute("gentity", m);
								break;
							}
						}
						break;
					}
					
				}
			}
		}
		return "admin-gjob-signout";
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('GJOB')")
	@RequestMapping(value = "/water/gjob/processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String processing(HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException, ParseException, ServiceException {
		Map<String, Object> entry_camera = new HashMap<String, Object>();
		String sdate =request.getParameter("sdate");
		Date date=null;
		//起始项目
		int start=0;
		int page=1;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
		if(StringUtils.isNotBlank(sdate)){
			date=sdf.parse(sdate);
		}else {
			date=sdf.parse(sdf.format(new Date()));
		}
		//当前页
		String pages =request.getParameter("page");
		if(StringUtils.isNotBlank(pages)){
			start=(Integer.parseInt(pages)-1)*7;
			page=Integer.parseInt(pages);
		}
		List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
		List<Plan> plans =new ArrayList<>();
		//找到未结束的项目
		List<GuardEntity> gps =new ArrayList<>();
		List<Long> ids =new ArrayList<>();
		for(GuardEntity gn:listen){
			if(gn.getStatus()!=-1){
				gps.add(gn);
			}
		}
		if(gps.size()>0){
			int end = gps.size()<start+7?gps.size():start+7;
			for(;start<end;start++){
				ids.add(gps.get(start).getId());
				Plan plan =new Plan();
				plan.setId(gps.get(start).getId());
				plan.setName(gps.get(start).getName());
				plans.add(plan);
			}
			//获取周天
			Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if(w>=0){
	        	cal.add(Calendar.DATE, -w);
	        	//后来改成周六到周五，所以加一天
	        	cal.add(Calendar.DATE, -1);
	        	date=sdf.parse(sdf.format(cal.getTime()));
	        }
	        //获取下周
	      //获取下周天
			 cal.setTime(date);
			 cal.add(Calendar.DATE, +7);
			 Date edate =sdf.parse(sdf.format(cal.getTime()));
			List<GJob> list = gjobService.getByGidsAndDate(ids, date,edate);
			if(list!=null && list.size()>0){
				for(GJob job:list){
					GJobEntity gje =new GJobEntity();
					gje.setId(job.getId());
					gje.setArriveDate(job.getArriveDate());
					gje.setEndDate(job.getEndDate());
					gje.setLeaveDate(job.getLeaveDate());
					gje.setStartDate(job.getStartDate());
					gje.setCstatu(job.getCstatu());
					if(job.getUsers()!=null && job.getUsers().size()>0){
						List<User> users =new ArrayList<>();
						for(com.kekeinfo.core.business.user.model.User um:job.getUsers()){
							User user =new User();
							user.setId(um.getId());
							user.setName(um.getFirstName());
							users.add(user);
						}
						gje.setUsers(users);
					}
					for(Plan plan:plans){
						if(plan.getId().equals(job.getGuard().getId())){
							plan.getJobs().add(gje);
							break;
						}
					}
				}
			}
		}
		
		//每页显示7个
		//int page = gps.size()%7==0?gps.size()/7:gps.size()/7+1;
		PaginationData paginationData = new PaginationData(7, page);
		paginationData = PageBuilderUtils.calculatePaginaionData(paginationData, Constants.MAX_ORDERS_PAGE, gps.size());
		entry_camera.put("page",paginationData);
		
		//获取日期,周天到周六
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        List<Date> dates =new ArrayList<>();
        dates.add(date);
        for(int i=0;i<6;i++){
        	cal.add(Calendar.DATE, 1);
        	date=sdf.parse(sdf.format(cal.getTime()));
        	dates.add(date);
        }
        entry_camera.put("dates", dates);
        
		//entry_camera.put("pages", page);
		entry_camera.put("plans", plans);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		String json = mapper.writeValueAsString(entry_camera);
		return json;
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('GJOB')")
	@RequestMapping(value = "/water/gjob/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getGJob(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<GJob> dt = new DataTable<GJob>();
		String mid = request.getParameter("gid");
		try { // 指定根据什么条件进行模糊查询
			//List<String> attributes = new ArrayList<String>();
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			List<Object[]> where =null;
			if(StringUtils.isNotBlank(mid)){
				where =new ArrayList<>();
				where.add(new Object[]{"guard.id", mid});
				where.add(new Object[]{"guard.status", 1,4});
			}
			
			Map<String, String> fetches = new HashMap<String, String>();
			fetches.put("guard", "LEFT");
			Entitites<GJob> list = gjobService.getPageListByAttributesLike(null, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null, true);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			 Gson gson = new Gson();
		        String jsondata= gson.toJson(dt);
		        if(list.getEntites() !=null && list.getEntites().size()>0){
		        	List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
			        StringBuffer json = new StringBuffer();
			        json.append("[");
			        //获取基础数据
			      
			        for(GJob gJob:list.getEntites()){
			        	json.append("{\"id\":").append(gJob.getId()).append(",");
			        	json.append("\"arriveDate\":").append("\"").append(gJob.getArriveDate()).append("\",");
			        	json.append("\"leaveDate\":").append("\"").append(gJob.getLeaveDate()).append("\",");
			        	json.append("\"startDate\":").append("\"").append(DateUtil.formatDate(gJob.getStartDate(), "yyyy-MM-dd")).append("\",");
			        	json.append("\"endDate\":").append("\"").append(DateUtil.formatDate(gJob.getEndDate(), "yyyy-MM-dd")).append("\",");
			        	json.append("\"dateModified\":").append("\"").append(DateUtil.formatDate(gJob.getAuditSection().getDateModified(), "yyyy-MM-dd hh:mm:ss")).append("\",");
			        	json.append("\"gid\":").append(gJob.getGuard().getId()).append(",");
			        	for(GuardEntity gn:listen){
			        		if(gn.getId().equals(gJob.getGuard().getId())){
			        			json.append("\"project\":").append("\"").append(gn.getName()).append("\"");
			        			break;
			        		}
			        	}
			        	json.append("},");
			        	
			        }
			        json.append("]");
			        //去掉最后一个逗号
			       json.deleteCharAt(json.lastIndexOf(","));
			      
			       jsondata = jsondata.substring(0, jsondata.length()-1)+",\"aaData\":"+json.toString()+"}";
		        }else{
		        	jsondata = jsondata.substring(0, jsondata.length()-1)+",\"aaData\":[]"+"}";
		        }
		        return jsondata;
			//dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging gjobs", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		return "";
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('GJOB')")
	@RequestMapping(value = "/water/gjob/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveGJob(@ModelAttribute("gjob") GJob gjob, Model model, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		AjaxResponse resp = new AjaxResponse();
		try {
			String[] urses = request.getParameterValues("cusers");
			if(urses!=null && urses.length>0){
				Set<com.kekeinfo.core.business.user.model.User> us =new HashSet<>();
				for(String s:urses){
					com.kekeinfo.core.business.user.model.User user =userService.getById(Long.parseLong(s));
					us.add(user);
				}
				gjob.setUsers(us);
				List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
				for(GuardEntity m:listen){
					if(m.getId().equals(gjob.getGuard().getId())){
						gjob.getGuard().setName(m.getName());
						gjob.getGuard().setStation(m.getStation());
						gjob.getGuard().setAddress(m.getAddress());
						break;
					}
				}
				String re=gjobService.saveUpdate(gjob);
				if(re=="0"){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				}else if(re=="-1"){
					resp.setStatus(-4);
					resp.setStatusMessage("有负责人在该时段有其他安排");
				}else{
					resp.setStatus(7);
					resp.setStatusMessage("以下人员没有登录过app，信息无法推送："+re);
				}
				//保存到内存中去
				if(re!="-1"){
					Map<Long,List<GJob>> ujmaps =  (Map<Long, List<GJob>>) webCacheUtils.getFromCache(Constants.USERGJOBS);
					for(com.kekeinfo.core.business.user.model.User u:us){
						List<GJob> glist=ujmaps.get(u.getId());
						if(glist==null){
							glist =new ArrayList<>();
						}
						glist.add(gjob);
						ujmaps.put(u.getId(), glist);
					}
					webCacheUtils.putInCache(Constants.USERGJOBS, ujmaps);
				}
			}else{
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setStatusMessage("没有选择责任人");
			}
			
		} catch (ServiceException e) {
			LOGGER.error("Error while save GJob", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setStatusMessage(e.getMessage());
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('GJOB')")
	@RequestMapping(value = "/water/gjob/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String deleteGJob(@RequestParam String gid, HttpServletRequest request,
			Locale locale) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		if (StringUtils.isNotBlank(gid)) {
			try {
				GJob gjob = gjobService.getById(Long.parseLong(gid));
				if (gjob != null) {
					gjobService.changestatus(gjob);
					pnodeUtils.reloadAllUserJobs();
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (e.getMessage().startsWith("Cannot delete or update a parent row")) {
					resp.setStatus(9997);
				} else {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				}
			}
		}
		return resp.toJSONString();
	}
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('GJOB')")
	@RequestMapping(value="/water/gjob/getTree.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String getTree(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
			for(GuardEntity ge:listen){
				Map<String, Object> entry_camera = new HashMap<String, Object>();
				entry_camera.put("id", ge.getId());
				entry_camera.put("name", ge.getName());
				resultList.add(entry_camera);
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(resultList);
			return json;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@PreAuthorize("hasRole('GJOB')")
	@RequestMapping(value="/water/gjob/users.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String users(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			//String gid =request.getParameter("gid");
			//Set<com.kekeinfo.core.business.user.model.User> jobs=null;
			//if(StringUtils.isNotBlank(gid)){
				
				Map<String, String> fetches = new HashMap<String, String>();
				fetches.put("user", "LEFT");
				Entitites<MonitorUser> list = monitorUserService.getPageListByAttributesLike(null, null,null, 
						null, null, null, fetches, true);
				if(list!=null && list.getEntites().size()>0){
					List<User> users =new ArrayList<>();
					for(MonitorUser gu:list.getEntites()){
						User u=new User();
						u.setId(gu.getUser().getId());
						u.setName(gu.getUser().getFirstName());
						users.add(u);
				/**
				String jid =request.getParameter("jid");
				//先查已经选择的
				if(StringUtils.isNotBlank(jid) ){
					List<Object[]> where =new ArrayList<>();;
					where.add(new Object[]{"id", jid});
					
					Map<String, String> fetches = new HashMap<String, String>();
					fetches.put("users", "LEFT");
					Entitites<GJob> list = gjobService.getPageListByAttributesLike(null, null, null,null, null, where, fetches, true);
					if(list!=null && list.getTotalCount()>0){
						jobs=list.getEntites().get(0).getUsers();
					}
				}
				
				List<Object[]> where =new ArrayList<>();
				where.add(new Object[]{"guard.id", gid});
				Map<String, String> fetches = new HashMap<String, String>();
				fetches.put("user", "LEFT");
				Entitites<GuardUser> list = guardUserService.getPageListByAttributesLike(null, null,null, 
						null, null, where, fetches, true);
				if(list!=null && list.getEntites().size()>0){
					List<User> users =new ArrayList<>();
					for(GuardUser gu:list.getEntites()){
						User u=new User();
						//查找已经被选择中的
						if(jobs!=null){
							for(com.kekeinfo.core.business.user.model.User ue:jobs){
								if(ue.getId().equals(gu.getUser().getId())){
									u.setuAgent("checked");
									break;
								}
							}
						}
						
						u.setId(gu.getUser().getId());
						u.setName(gu.getUser().getFirstName());
						users.add(u);*/
					}
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(users);
					return json;
				}
			//}
			
			
			/**
			if(StringUtils.isNotBlank(gid)){
				String where=" LEFT JOIN GUARDUSER ON USER.USER_ID=GUARDUSER.USER_ID WHERE GUARDUSER.GUARD_ID="+gid;
				List<Object[]> stores = userService.getPinYin(where);
				List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
				return pinyin;
			}*/
			
		}catch (Exception e){
			return null;
		}
		return null;
	}
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/gjob/musers.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin> getusers(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = monitorUserService.getPinYin();
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("gjob", "gjob");
		activeMenus.put("gjob-list", "gjob-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("gjob");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
