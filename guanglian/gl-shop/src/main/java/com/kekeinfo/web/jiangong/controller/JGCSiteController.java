package com.kekeinfo.web.jiangong.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.camera.model.Camera;
import com.kekeinfo.core.business.camera.service.CameraService;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.daily.model.Daily;
import com.kekeinfo.core.business.daily.service.DailyService;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.last.service.CsiteLastService;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.service.ProjectService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.business.zone.service.ZoneService;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.entity.UnderWater;
import com.kekeinfo.web.entity.filter.PointInfoFilter;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.DataUtils;
import com.kekeinfo.web.utils.ImageFilePathUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PinyinUtils;
import com.kekeinfo.web.utils.QCodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class JGCSiteController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JGCSiteController.class);
	
	@Autowired
	ZoneService zoneService;
	
	@Autowired CSiteService cSiteService;
	
	@Autowired DepartmentService departmentService;
	
	@Autowired UserService userService;
	
	@Autowired DepartmentNodeService departmentNodeService;
	
	@Autowired GroupService groupService;
	
	@Autowired DataUtils dataUtils;
	
	@Autowired ObservewellService owellService;
	
	@Autowired CameraService cameraService;
	
	@Autowired CsiteLastService csiteLastService;
	
	@Autowired ProjectService projectService;
	
	@Autowired QCodeUtils qCodeUtils;
	
	@Autowired
	private DailyService dailyService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	
	
	/**
	 * 地下水部门项目查看
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/jiangong/csite/wlist.html", method=RequestMethod.GET)
	public String displayWProjects(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		setWaterMenu(model,request);
		//加载工程日志内容
		request.setAttribute("daily", new Daily());
		request.setAttribute("nowdate", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));

		//是否某个具体的项目
		String pid = request.getParameter("pid");
		if(!StringUtils.isBlank(pid)){
			
			//ConstructionSite csite =cSiteService.getById(Long.parseLong(pid));
			List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
			UnderWater csite=null;
			for(UnderWater c:cs){
				if(c.getId().equals(Long.parseLong(pid))){
					csite=c;
					break;
				}
			}
			if(csite==null){
				csite=pnodeUtils.getByCid(Long.parseLong(pid));
			}
			if(csite.isJiangong()){
				request.setAttribute("activePid", pid); //指定项目
				request.setAttribute("activeZone", csite.getZone()); //指定项目
				request.setAttribute("activeFun", "monitor");  //指定当前操作功能
				model.addAttribute("pid", pid);
				model.addAttribute("csite", csite);
			}else{
				return "water/404";
			}
			
		}else{
			return "water/404";
		}
		//加载刷新时间
		List<BasedataType> rlist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.FRESHTIME);
		if(rlist.isEmpty()){
			model.addAttribute("rfreshtime", "60");
		}else {
			model.addAttribute("rfreshtime", rlist.get(0).getValue());
		}
		return "jg-map";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/jiangong/csite/cpdetail.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String cpdetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cid = request.getParameter("cid");
		if(StringUtils.isNotBlank(cid) ){
			List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
			UnderWater csite=null;
			for(UnderWater c:cs){
				if(c.getId().equals(Long.parseLong(cid))){
					csite=c;
					break;
				}
			}
			if(csite==null){
				csite=pnodeUtils.getByCid(Long.parseLong(cid));
			}
			
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(csite);
			return json;
		}
		return "";	
	}
	
	/**
	 * 编辑项目
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/jiangong/csite/toEdit.html", method=RequestMethod.GET)
	public String toEdit(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setWaterMenu(model,request);
		String cid = request.getParameter("cid");
		
		ConstructionSite csite =cSiteService.getByCid(Long.parseLong(cid));
		request.setAttribute("activeFun", "toEdit");  //指定当前操作功能
		
		model.addAttribute("csite", csite);
		
		Project project =projectService.withUserGroup(csite.getProject().getId());
		model.addAttribute("cusers", project.getcUsers());
		model.addAttribute("wusers", project.getwUsers());
		return "jg-project";
	}
	
	
	
	
	@RequestMapping(value="/jiangong/csite/images.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String images(HttpServletRequest request, Locale locale) throws Exception {
		
		String sUserId = request.getParameter("cid");

		//AjaxResponse resp = new AjaxResponse();
		//resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		
		try {
			ConstructionSite csite = cSiteService.getByIdWithImg(Long.parseLong(sUserId));
			Map<String, Object> result = new HashMap<>();
			if(csite!=null){
				if(csite.getImages()!=null && csite.getImages().size()>0){
					result.put("img", csite.getImages());
					
				}
				Project project =projectService.withUserGroup(csite.getProject().getId());
				if(project.getcUsers()!=null && project.getcUsers().size()>0){
					List<com.kekeinfo.web.entity.User> cuser = new ArrayList<>();
					for(User cu:project.getcUsers()){
						com.kekeinfo.web.entity.User fuser = new com.kekeinfo.web.entity.User();
						fuser.setId(cu.getId());
						fuser.setName(cu.getFirstName());
						cuser.add(fuser);
					}
					result.put("cusers", cuser);
				}
				if(project.getwUsers()!=null && project.getwUsers().size()>0){
					List<com.kekeinfo.web.entity.User> wuser = new ArrayList<>();
					for(User cu:project.getwUsers()){
						com.kekeinfo.web.entity.User fuser = new com.kekeinfo.web.entity.User();
						fuser.setId(cu.getId());
						fuser.setName(cu.getFirstName());
						wuser.add(fuser);
					}
					result.put("wusers", wuser);
				}
				if(result.size()>0){
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(result);
					return json;
				}	
			}
			
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			return "-1";
		}
		
		return "0";
		
	}
	
	
	@RequestMapping(value="/jiangong/csite/getWellData.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String WellData(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String cid = request.getParameter("cid");
		if(StringUtils.isNotBlank(cid)){
			try{
				Map<String, Object> dataResult = dataUtils.getWellSitesByCid(Long.parseLong(cid));
				ConstructionSite csite = cSiteService.getById(Long.parseLong(cid));
				dataResult.put("rail", csite.getRail());
				//dataResult.put("csite", csite);
//				var point = new BMap.Point(data.longitude,data.latitude);
				
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dataResult);
				return json;
			}catch (Exception e){
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/jiangong/csite/getCamera.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String cameraData(HttpServletRequest request, HttpServletResponse response, Locale locale) throws ServiceException {
		String cid = request.getParameter("pid");
		if(StringUtils.isNotBlank(cid)){
			List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
			UnderWater csite=null;
			for(UnderWater c:cs){
				if(c.getId().equals(Long.parseLong(cid))){
					csite=c;
					break;
				}
			}
			if(csite==null){
				try {
					csite = pnodeUtils.getByCid(Long.parseLong(cid));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			List<Object[]> where =new ArrayList<>();
			where.add(new Object[]{"project.id", csite.getPid()});
			Entitites<Camera> list = cameraService.getPageListByAttributesLike(null,null, null, null,null, where, null, true);
			if(list!=null && !list.getEntites().isEmpty()){
				List<Camera> cameras = list.getEntites();
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				for (Camera camera : cameras) {
					Map<String, Object> entry = new HashMap<String, Object>();
					entry.put("id", camera.getId());
					entry.put("name", camera.getNote());
					entry.put("status", camera.isStatus());
					entry.put("longitude", camera.getLongitude());
					entry.put("latitude", camera.getLatitude());
					resultList.add(entry);
				}
				ObjectMapper mapper = new ObjectMapper();
				String json;
				try {
					json = mapper.writeValueAsString(resultList);
					return json;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LOGGER.error(e.getMessage());
					e.printStackTrace();
				} 
			}
		}
		return "";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/jiangong/csite/getWarning.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String WarningData(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String cid = request.getParameter("pid");
		
		try{
			Map<String, Object> dataResult = null;
			if(StringUtils.isNotBlank(cid)){
				dataResult = dataUtils.getWellDataByCid(Long.parseLong(cid), true);
			}
			List<Long> ids =new ArrayList<>();
			ids.add(Long.parseLong(cid));
			List<UnderWater> cs = (List<UnderWater> ) webCacheUtils.getFromCache(Constants.WATER_CSITE);
			dataResult =dataUtils.getWarningData(dataResult,ids,cs);
			//加载实时水位
			//dataResult =dataUtils.getStardingInfo(dataResult,ids);
			
			//ConstructionSite csite = cSiteService.getByIdWithDepartment(Long.parseLong(cid));
			//dataResult.put("hasright", pnodeUtils.hasProjectRight(request,egroups,csite));
			ObjectMapper mapper = new ObjectMapper();
			mapper.getSerializationConfig().addMixInAnnotations(Basepoint.class, PointInfoFilter.class);
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			String json = mapper.writeValueAsString(dataResult);
			return json;
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		
		
		return null;
	}
	
	@RequestMapping(value="/jiangong/csite/zones.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin> zones(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = zoneService.getPinYin();
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/jiangong/csite/imgs.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<Images> imags(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			String pid = request.getParameter("pid");
			if(StringUtils.isNotBlank(pid)){
				ConstructionSite csite = cSiteService.getByIdWithImg(Long.parseLong(pid));
				List<Images> imgs =csite.getImages();
				if(imgs!=null){
					for(Images img:imgs){
						img.setName(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getName(), FileContentType.PRODUCT_DIGITAL));
						if(StringUtils.isNotBlank(img.getJpeg())){
							img.setJpeg(request.getContextPath() + ImageFilePathUtils.buildFilePathByConentType(img.getJpeg(), FileContentType.PRODUCT_DIGITAL));
						}
					}
				}
				return imgs;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取项目每日信息
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@RequestMapping(value="/jiangong/csite/conclusion.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody Map<String, String> conclusion(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		Map<String, String> resp = new HashMap<String, String>();
		try{
			String pid = request.getParameter("pid");
			
			Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("datec", "desc");
			Entitites<Daily> list  = dailyService.getListByAttributes(new String[]{"cSite.id", "datec"}, new Object[]{Long.valueOf(pid), now.getTime()}, orderby);
			//第一次创建的情况
			if(list.getTotalCount()>0){
				Daily daily = list.getEntites().get(0);
				resp.put("conclusion", daily.getConclusion());
				resp.put("status", "0");
			}else{
				resp.put("status", "-1");
			}
			
		}catch (Exception e){
			e.printStackTrace();
			resp.put("status", "-1");
		}
		return resp;
	}
	
	/**
	 * 获取当前项目抽水井和回灌井的各个测点的当前状态，显示柱状图，读取当前表
	 * 获取当前项目抽水量和回灌量的平均值，显示饼图。（获取当天所有抽水井的流量总和，即将单井的平均流量求和，回灌井采用同样方式处理）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/jiangong/csite/getCheckpoints.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String getCheckpoints(@RequestParam(required=true, value="pid") String pid, HttpServletRequest request, HttpServletResponse response) {
		
		//Entitites<Invertedwell> iwellList = invertedwellService.getListByAttributes(new String[]{"cSite.id"}, new Object[]{Long.valueOf(pid)}, null);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> result = dataUtils.getCheckpoints(Long.parseLong(pid));
			String json = mapper.writeValueAsString(result);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{status:-1}";
	}
	
	/**
	 * 获取当天测点的数据记录，提供前台页面展示曲线图
	 * @param cid
	 * @param type 测点类型，0抽水井，1观测井，2回灌井，3环境监测
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/jiangong/csite/getLines.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String getLines(@RequestParam(required=true, value="cid") String cid,@RequestParam(required=true, value="type") String type, HttpServletRequest request, HttpServletResponse response) {
		/**
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		*/
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> result = dataUtils.getLines60(Long.valueOf(cid), type);
			String json = mapper.writeValueAsString(result);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{status:-1, message:'系统报错'}";
	}
	
	/**
	 * 读取历史表，获取当天测点的数据记录，提供前台页面展示曲线图
	 * 如果数据超过300条记录，则页面显示会间隔读取记录点
	 * @param cid
	 * @param type 测点类型，0抽水井，1观测井，2回灌井，3环境监测
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/jiangong/csite/getHistoryLines.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String getHistoryLines(@RequestParam(required=true, value="cid") String cid,@RequestParam(required=true, value="type") String type, HttpServletRequest request, HttpServletResponse response) {
		String begindt = request.getParameter("begindt");
		String enddt = request.getParameter("enddt");
		String[] cids = cid.split(",");
		Date begin = null;
		Date end = null;
		try {
			if (StringUtils.isNotBlank(begindt)) {
					begin = DateUtils.parseDateStrictly(begindt, "yyyy-MM-dd");
			} 
			if (StringUtils.isNotBlank(enddt)) {
				end = DateUtils.parseDateStrictly(enddt, "yyyy-MM-dd");
				end = DateUtils.addDays(end, 1); //截止时间+1，查询包含了截止时间当天
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = null;
		try {
			int i = checkTime(request, begin);
			if (cids.length==1) { //单个测点历史曲线
				if(i<0) { //读取时间范围不在当前表保留范围之内，读取历史表
					result = dataUtils.getHistoryLines(Long.valueOf(cids[0]), type, begin, end);
				} else { //读取当前表
					result = dataUtils.getLines(Long.valueOf(cids[0]), type, begin, end);
				}
			} else { //多测点历史曲线比较
				if(i<0) { //读取时间范围不在当前表保留范围之内，读取历史表
					result = dataUtils.getHistoryLines(cids, type, begin, end);
				} else { //读取当前表
					result = dataUtils.getLines(cids, type, begin, end);
				}
			}
			String json = mapper.writeValueAsString(result);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{status:-1}";
	}

	/**
	 * 判断读取测点数据，比较是否在当前表保存时间范围之内，如果是，则读取当前表，不是，则读取历史表
	 * @param request
	 * @param begin，读取起始时间
	 * @return <0，读取历史表，>=0，读取当前表
	 */
	private int checkTime(HttpServletRequest request, Date begin) {
		@SuppressWarnings("unchecked")
		List<BasedataType> keeptimeList = (List<BasedataType>) request.getSession().getServletContext().getAttribute(Constants.KEEPTIME); //数据保留天数
		//比较是否在当前表保存时间范围之内，如果是，则读取当前表，不是，则读取历史表
		Integer keeptime = Integer.valueOf(keeptimeList.get(0).getValue());
		Date tmp = DateUtils.addDays(begin, keeptime+1);
		Date now = new Date();
		int i = tmp.compareTo(now);
		return i;
	}
	
	
	
	private void setWaterMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("csite", "csite");

		
//		@SuppressWarnings("unchecked")
//		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("WATERMENUMAP");
		
//		Menu currentMenu = (Menu)menus.get("csite");
//		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
