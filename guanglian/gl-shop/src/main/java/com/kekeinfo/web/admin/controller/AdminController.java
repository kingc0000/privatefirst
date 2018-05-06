package com.kekeinfo.web.admin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.observewell.service.ObservewellService;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.business.pumpwell.service.PumpwellService;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.GuardEntity;
import com.kekeinfo.web.entity.UGJob;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PasswordReset;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class AdminController {
	
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	UserService userService;
	
	@Autowired 
	DepartmentService departmentService;
	
	@Autowired DepartmentNodeService departmentNodeService;

	@Autowired CSiteService csiteService;
	
	@Autowired PumpwellService pumpwellService;
	
	@Autowired ObservewellService observewellService;
	@Autowired private PNodeUtils pnodeUtils;
	/**
	 * 进入统一面板，判断用户是否有图片监控、地下水、系统管理的权限
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/water/home.html","/water/","/water","/"}, method=RequestMethod.GET)
	public String displayHome(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		//model.addAttribute("app", user.getuAgent());
		if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) { //admin 用户，用户图像监控、地下水、系统管理权限
			request.setAttribute("camera", "1");
			request.setAttribute("water", "1");
			request.setAttribute("system", "1");
			request.setAttribute("guard", "1");
			request.setAttribute("monitor", "1");
		} else {
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){ //拥有图像监控、地下水权限
				request.setAttribute("camera", "1");
				//List<Department> departments = (List<Department>) webCacheUtils.getFromCache(Constants.DEPARTMENTS);
				Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
				boolean find = pnodeUtils.hasProjectRight(departmentNodes, waterDept);
				if(find){
					request.setAttribute("water", "1");
				}else{
					request.setAttribute("water", "0");
				}
				
				find = pnodeUtils.hasProjectRightMoiniter(departmentNodes);
				if(find){
					request.setAttribute("monitor", "1");
				}else{
					request.setAttribute("monitor", "0");
				}
				find = pnodeUtils.hasProjectRightGuard(departmentNodes);
				if(find){
					request.setAttribute("guard", "1");
				}else{
					request.setAttribute("guard", "0");
				}
				request.setAttribute("system", "0");
			} else {
				request.setAttribute("camera", "0");
				request.setAttribute("water", "0");
				request.setAttribute("system", "0");
			}
		}
		if(user.getTemp()!=null && user.getTemp().equalsIgnoreCase("change")){
			pnodeUtils.reloadUserJobs(user.getId());
			pnodeUtils.reloadGroupUser(user);
			user.setTemp("");
			userService.update(user);
			request.getSession().setAttribute(Constants.ADMIN_USER, user);
		}
		return ControllerConstants.Tiles.dashboard;
	}
	
	/**
	 * 进入系统管理界面
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value={"/water/system.html"}, method=RequestMethod.GET)
	public String displaySystemDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return ControllerConstants.Tiles.systemDashboard;
	}
	
	@RequestMapping(value={"/water/locksreen.html"}, method=RequestMethod.GET)
	public String lockDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "water/lockScreen";
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value={"/water/water.html"}, method=RequestMethod.GET)
	public String displayWaterDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return ControllerConstants.Tiles.waterDashboard;
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value={"/water/app/message.html"}, method=RequestMethod.GET)
	public String app(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		//model.addAttribute("app", user.getuAgent());
		if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) { //admin 用户，用户图像监控、地下水、系统管理权限
			request.setAttribute("camera", "1");
			request.setAttribute("water", "1");
			request.setAttribute("system", "1");
			request.setAttribute("guard", "1");
			request.setAttribute("monitor", "1");
		} else {
			Set<DepartmentNode> departmentNodes = user.getpNodes();
			if(departmentNodes!=null && departmentNodes.size()>0){ //拥有图像监控、地下水权限
				request.setAttribute("camera", "1");
				Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
				boolean find = pnodeUtils.hasProjectRight(departmentNodes, waterDept);
				if(find){
					request.setAttribute("water", "1");
				}else{
					request.setAttribute("water", "0");
				}
				
				find = pnodeUtils.hasProjectRightMoiniter(departmentNodes);
				if(find){
					request.setAttribute("monitor", "1");
				}else{
					request.setAttribute("monitor", "0");
				}
				find = pnodeUtils.hasProjectRightGuard(departmentNodes);
				if(find){
					request.setAttribute("guard", "1");
				}else{
					request.setAttribute("guard", "0");
				}
				request.setAttribute("system", "0");
			} else {
				request.setAttribute("camera", "0");
				request.setAttribute("water", "0");
				request.setAttribute("system", "0");
			}
		}
		if(user.getTemp()!=null && user.getTemp().equalsIgnoreCase("change")){
			pnodeUtils.reloadUserJobs(user.getId());
			pnodeUtils.reloadGroupUser(user);
			user.setTemp("");
			userService.update(user);
			request.getSession().setAttribute(Constants.ADMIN_USER, user);
		}
		return ControllerConstants.Tiles.dashboard;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/water/jobs.shtml", method={RequestMethod.POST,RequestMethod.GET}, produces="application/json;charset=utf-8")
	public @ResponseBody String jobs(HttpServletRequest request, Locale locale) throws Exception {
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		//处理工作安排
				Map<Long,List<GJob>> ujmaps =  (Map<Long, List<GJob>>) webCacheUtils.getFromCache(Constants.USERGJOBS);
				if(ujmaps!=null && ujmaps.size()>0){
					List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
					List<GJob> jobs =ujmaps.get(user.getId());
					List<UGJob> gjobs=new ArrayList<>();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					 SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
					
					if(jobs!=null && jobs.size()>0){
						for(GJob job:jobs){
							Date date =new Date();
							Date baseDate=new Date();
							baseDate=dateFormat.parse(dateFormat.format(baseDate));
							if(!job.getEndDate().before(baseDate)){
								//处理第一天的
								UGJob gj=new UGJob();
								UGJob gje=new UGJob();
								String date1 =dateFormat.format(date);
								date1=date1+" "+job.getArriveDate();
								Date sDate =sdf.parse(date1);
								gj.setStime(sDate.getTime()-7200000);
								String date2 =dateFormat.format(date);
								date2=date2+" "+job.getLeaveDate();
								Date eDate =sdf.parse(date2);
								gje.setStime(eDate.getTime()-900000);
								for(GuardEntity ge:  listen){
									if(ge.getId().equals(job.getGuard().getId())){
										gj.setTitle(ge.getName()+"-2小时后要点");
										gje.setTitle(ge.getName()+"-15分钟后销点");
										gj.setRstatus(job.getRstatus());
										gje.setRstatus(job.getRstatus());
										break;
									}
								}
								if(gje.getStime()>date.getTime()){
									gjobs.add(gje);
								}
								if(gj.getStime()>date.getTime()){
									gjobs.add(gj);
								}
								Calendar c = Calendar.getInstance();
								//把date格式化成00000
								date =dateFormat.parse(dateFormat.format(date));
						        c.setTime(date);  
						        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
						        date =c.getTime();
						        
								while (!date.after(job.getEndDate())){
									UGJob gj1=new UGJob();
									UGJob gje1=new UGJob();
									String date11 =dateFormat.format(date);
									date11=date11+" "+job.getArriveDate();
									Date sDate1 =sdf.parse(date11);
									gj1.setStime(sDate1.getTime()-7200000);
									String date21 =dateFormat.format(date);
									date21=date21+" "+job.getLeaveDate();
									Date eDate1 =sdf.parse(date21);
									gje1.setStime(eDate1.getTime()-900000);
									for(GuardEntity ge:  listen){
										if(ge.getId().equals(job.getGuard().getId())){
											gj1.setTitle(ge.getName()+"-2小时后要点");
											gje1.setTitle(ge.getName()+"-15分钟后销点");
											gj1.setRstatus(job.getRstatus());
											gje1.setRstatus(job.getRstatus());
											break;
										}
									}
									
										gjobs.add(gj1);
										gjobs.add(gje1); 
							        c.setTime(date);  
							        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
							        date =c.getTime();
								}
							}
						}
					}
					 ObjectMapper mapper = new ObjectMapper();
						String json = mapper.writeValueAsString(gjobs);
						mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
						return json;
				}
				return "";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/water/rjobs.shtml", method={RequestMethod.POST,RequestMethod.GET}, produces="application/json;charset=utf-8")
	public @ResponseBody String removejob(HttpServletRequest request, Locale locale) throws Exception {
		AjaxResponse resp =new AjaxResponse();
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		//处理工作安排
		try{
			Map<Long,List<GJob>> ujmaps =  (Map<Long, List<GJob>>) webCacheUtils.getFromCache(Constants.USERGJOBS);
			if(ujmaps!=null && ujmaps.size()>0){
				ujmaps.remove(user.getId());
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);	
		}catch (Exception e){
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
				return resp.toJSONString();
	}
	
	@RequestMapping(value="/water/shmetro/projects.shtml", method={RequestMethod.POST,RequestMethod.GET}, produces="application/json;charset=utf-8")
	public @ResponseBody String project(HttpServletRequest request, Locale locale) throws Exception {
		String token = request.getParameter("token");
		String stoken = (String)request.getSession().getAttribute("shentongtoken");
		if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(stoken) && token.equals(stoken)){
			String lastdate = request.getParameter("lastdate");
			Date date  =null;
			if(StringUtils.isNotBlank(lastdate)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			    date = sdf.parse(lastdate);  
				
			}
			List<ConstructionSite> cs = csiteService.shengtong(date);
			if(cs!=null && !cs.isEmpty()){
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				for(ConstructionSite c:cs){
					Map<String, Object> entry = new HashMap<String, Object>();
					entry.put("id", c.getId());
					entry.put("name", c.getProject().getName());
					entry.put("address", c.getProject().getZone().getName()+c.getProject().getAddress());
					entry.put("freq", c.getGatherData());
					Map<String, Object> pmap = new HashMap<String, Object>();
					pmap.put("project", entry);
					resultList.add(pmap);
				}
				Map<String, Object> remap = new HashMap<String, Object>();
				remap.put("projects", resultList);
				 ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(remap);
					mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
					return json;
			}
		}else{
			return "logon error";
		}
		
		return "";
	}
	
	@RequestMapping(value="/water/shmetro/logon.shtml", method={RequestMethod.POST,RequestMethod.GET}, produces="application/json;charset=utf-8")
	public @ResponseBody String logon(HttpServletRequest request, Locale locale) throws Exception {
		String pname = request.getParameter("uname");
		@SuppressWarnings("unchecked")
		List<BasedataType> baselist = (List<BasedataType>) request.getSession().getServletContext().getAttribute("shentong");
		if(baselist!=null && baselist.size()>0){
			BasedataType bt = baselist.get(0);
			if(bt.getValue().equalsIgnoreCase(pname)){
				String token =PasswordReset.generateRandomString();
				request.getSession().setAttribute("shentongtoken", token);
				return token;
			}
		}
		return "token error";
	}
	
	@RequestMapping(value="/water/shmetro/data.shtml", method={RequestMethod.POST,RequestMethod.GET}, produces="application/json;charset=utf-8")
	public @ResponseBody String data(HttpServletRequest request, Locale locale) throws Exception {
		
		String token = request.getParameter("token");
		String stoken = (String)request.getSession().getAttribute("shentongtoken");
		if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(stoken) && token.equalsIgnoreCase(stoken)){
			String pid = request.getParameter("pid");
			if(StringUtils.isNotBlank(pid)){
				
				List<String> where = new ArrayList<>();
				where.add("cSite.id");
				where.add(pid);
				Entitites<Pumpwell> list  = pumpwellService.getPageListByAttributesLike(null, null,null, null, null,where,null);
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				if(list !=null && !list.getEntites().isEmpty()){
					
					for(Pumpwell p:list.getEntites()){
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", p.getId());
						entry.put("name", p.getName());
						entry.put("flow", p.getrFlow());
						entry.put("datetime", p.getLastDate());
						entry.put("flowThreshold", p.getFlow());
						entry.put("type", "flow");
						if(p.getrFlow()!=null && p.getFlow()!=null){
							if(p.getrFlow().compareTo(p.getFlow())==1){
								entry.put("alarm", true);
							}else{
								entry.put("alarm", false);
							}
						}else{
							entry.put("alarm", false);
						}
						
						Map<String, Object> well = new HashMap<String, Object>();
						well.put("well", entry);
						resultList.add(well);
					}
				}
				
				Entitites<Observewell> olist  = observewellService.getPageListByAttributesLike(null, null,null, null, null,where,null);
				if(olist !=null && !olist.getEntites().isEmpty()){
					for(Observewell o:olist.getEntites()){
						Map<String, Object> entry = new HashMap<String, Object>();
						entry.put("id", o.getId());
						entry.put("name", o.getName());
						entry.put("water",o.getrWater());
						entry.put("datetime", o.getLastDate());
						entry.put("waterMax", o.getWaterMeasurement());
						entry.put("waterMin", o.getWaterDwon());
						entry.put("type", "water");
						if(o.getrWater()!=null && o.getWaterMeasurement()!=null && o.getWaterDwon()!=null){
							if(o.getrWater().compareTo(o.getWaterMeasurement())==1 || o.getrWater().compareTo(o.getWaterDwon())==-1){
								entry.put("alarm", true);
							}else{
								entry.put("alarm", false);
							}
						}else{
							entry.put("alarm", false);
						}
						
						Map<String, Object> well = new HashMap<String, Object>();
						well.put("well", entry);
						resultList.add(well);
					}
				}
				Map<String, Object> rmap = new HashMap<String, Object>();
				rmap.put("wells", resultList);
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
				String json = mapper.writeValueAsString(rmap);
				
				return json;
			}
		}else{
			return "logon error";
		}
		
		
		return "";
	}
}
