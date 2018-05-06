package com.kekeinfo.web.admin.controller.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.last.model.MonitLast;
import com.kekeinfo.core.business.last.service.MonitLastService;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.business.monitor.service.MonitorService;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.business.zone.model.Zone;
import com.kekeinfo.core.business.zone.service.ZoneService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.admin.security.WebUserServices;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.MonitorEntity;
import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.entity.filter.ProjectFilter;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PinyinUtils;
import com.kekeinfo.web.utils.RadomSixNumber;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class MonitorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	MonitorService monitorService;
	@Autowired GroupService groupService;
	@Autowired ZoneService zoneService;
	@Autowired DepartmentService departmentService;
	@Autowired UserService userService;
	@Autowired MonitLastService monitLastService;
	@Autowired private WebUserServices userDetailsService;
	@Autowired DepartmentNodeService departmentNodeService;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitor.html", method = RequestMethod.GET)
	public String home(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		List<MonitLast> clast = monitLastService.getByUserID(user.getId());
		List<MonitorEntity> cs = (List<MonitorEntity> ) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
		
		if(clast!=null){
			List<MonitorEntity> last = new ArrayList<>();
			for(MonitLast c:clast){
				for(MonitorEntity ud:cs){
					if(ud.getId()==c.getMid()){
						last.add(ud);
						break;
					}
				}
			}
			model.addAttribute("last", last);
		}
		
		//加载项目集合
		HashMap<String, Set<MonitorEntity>> set  = pnodeUtils.getMonitorZone(request);
		request.setAttribute("projects", set);
		//model.addAttribute("app", user.getuAgent());
		setMenu(model,request);
		return "monitor-home";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitor/list.html", method = RequestMethod.GET)
	public String listmonitors(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		Monitor monitor = new Monitor();
		List<Zone> zones = zoneService.getAllZone();
		model.addAttribute("monitor", monitor);
		model.addAttribute("zones", zones);
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
        try {
        	if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
        		hasRight = true;
        	} else {
        		if (request.isUserInRole("EDIT-PROJECT")) {
        			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
        			hasRight = pnodeUtils.hasProjectRightMoiniter(user.getpNodes());
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        model.addAttribute("hasRight", hasRight);
		return "admin-monitor";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/monitor/mprojects.html", method=RequestMethod.GET)
	public String cprojrcts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ctype =request.getParameter("ctype");
		String zone =request.getParameter("zone");
		if(StringUtils.isNotBlank(zone) && StringUtils.isNotBlank(ctype)){
			//加载项目集合
			HashMap<String, Set<MonitorEntity>> set  = pnodeUtils.getMonitorZone(request);
			Set<MonitorEntity> uw = set.get(zone);
			if(uw!=null && uw.size()>0){
				List<MonitorEntity> yxlist = new ArrayList<>();
				List<MonitorEntity> gblist = new ArrayList<>();
				
				for(MonitorEntity u:uw){
					if(u.getCtype().equalsIgnoreCase(ctype.trim())){
						switch (u.getStatus()) {
						case 0:
							yxlist.add(u);
							break;
						case 1:
							gblist.add(u);
						}
					}	
				}
				model.addAttribute("yxlist", yxlist);
				model.addAttribute("gblist", gblist);
			}
		}
		//判断是否手机用户
		//User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		//model.addAttribute("app", user.getuAgent());
		model.addAttribute("zone", zone);
		return "monitor-mprojects";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitor/detail.html", method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mid = request.getParameter("mid");
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
		if(StringUtils.isNotBlank(mid)){
			Monitor monitor =monitorService.getByCid(Long.parseLong(mid));
			
			 MonitLast clast = new MonitLast();
				clast.setMid(monitor.getId());
				clast.setDateModified(new Date());
				User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
				clast.setUid(user.getId());
		       monitLastService.saveNew(clast);
			model.addAttribute("monitor", monitor);
			@SuppressWarnings("unchecked")
			List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
			for(MonitorEntity m:listen){
				if(m.getId().equals(monitor.getId())){
					model.addAttribute("mentity", m);
					break;
				}
			}
		}
		try {
        	if (request.isUserInRole("ADMIN") || request.isUserInRole("SUPERADMIN")) {
        		hasRight = true;
        	} else {
        		if (request.isUserInRole("EDIT-PROJECT")) {
        			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
        			hasRight = pnodeUtils.hasProjectRightMoiniter(user.getpNodes());
        		}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("activeCode", "project"); //指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("mid", mid);
		setMenu(model, request);
		return "monitor-detail";
	}
	
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/monitor/zones.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
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
	@RequestMapping(value = "/water/monitor/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getMonitor(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<Monitor> dt = new DataTable<Monitor>();
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("project.name"); 
			attributes.add("project.projectOwner"); 
			attributes.add("project.phone");
//			attributes.add("memo");
			attributes.add("project.address");
			attributes.add("project.zone.name");
			attributes.add("project.city");
			//HashMap<String, String> orderby = new HashMap<String, String>();
			//orderby.put("project.name", "desc");
			List<String> join =new ArrayList<String>();
			join.add("project");
			
			Department de=(Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_MONITOR);
			List<String> where = pnodeUtils.getCids(request, de.getId(),1);
			Entitites<Monitor> list = monitorService.getPageListByAttributesLike(attributes,
					dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), null, where, join);
			
			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(pnodeUtils.setEditMonitor(request, egroups, list.getEntites()));
		        if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") && !request.isUserInRole("EDIT-PROJECT")){
					dt.setHasRight(false);
				}else{
					dt.setHasRight(true);
				}
		} catch (Exception e) {
			LOGGER.error("Error while paging monitors", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		mapper.getSerializationConfig().addMixInAnnotations(Project.class, ProjectFilter.class);
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitor/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveMonitor(@ModelAttribute("monitor") Monitor monitor, Model model,@RequestParam(required=false, value="fileupload") List<CommonsMultipartFile> uploadfiles, 
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			
			String delids= request.getParameter("delids");
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			if(user!=null){
				monitor.getProject().getAuditSection().setModifiedBy(user.getId().toString());
				if(monitor.getId()==null){
					monitor.getProject().getAuditSection().setDateCreated(new Date());
				}else{
					monitor.getProject().getAuditSection().setDateModified(new Date());
				}
				//之前的用户如果有修改需要去掉权限
				//附件处理
				if(uploadfiles!=null && !uploadfiles.isEmpty()){
					List<Images> imgs= new ArrayList<>();
					for(CommonsMultipartFile uploadfile:uploadfiles){
						Images img = new Images();
						img.setName(RadomSixNumber.getImageName(uploadfile.getOriginalFilename())); 
						img.setDigital(uploadfile.getInputStream());
						//压缩
						img.setJpeg(RadomSixNumber.getImageName(img.getName()));
						InputStream inputStream = uploadfile.getInputStream();
						img.setJdigital(ImageUtils.ByteArrayOutputStream(inputStream));
						imgs.add(img);
					}
					monitor.setImages(imgs);
				}
				User puser =null;
				//部门负责人从用户选择过来的
				Long uid = monitor.getProject().getProjectOwnerid();
				if(uid!=null){
					 puser = userService.getById(uid);
					if(puser!=null){
						if (StringUtils.isNotBlank(monitor.getProject().getProjectOwner())&&!monitor.getProject().getProjectOwner().equals(puser.getFirstName())) { //手动输入的用户姓名，而非系统用户名
							monitor.getProject().setProjectOwnerid(null);
						} else {
							monitor.getProject().setProjectOwner(puser.getFirstName());
							monitor.getProject().setPhone(puser.getTelephone());
						}
					}
				}
				boolean isnew =false;
				if(monitor.getId()==null){
					monitor.getProject().setDepartment(pnodeUtils.getMonitorDepartment());
					monitor.getProject().setPtype(0);
					isnew=true;
				}
				/**
				 * 项目关闭
				 */
				List<DepartmentNode> pNodes=null;
				if(monitor.getStatus()==1){
					pNodes =departmentNodeService.getByTypePID(monitor.getProject().getId());
				}
				monitorService.saveOrUpdate(monitor,puser,delids,pNodes);
				if(isnew){
					pnodeUtils.reloadAllDepartments();
				}
				pnodeUtils.reloadAllMonitor();
				request.getSession().setAttribute(Constants.USER_ZONE_MONITOR,null);
				//设置成功后清除相应用户的session，让其重新登录
				if(pNodes!=null && pNodes.size()>0){
					for(DepartmentNode p:pNodes){
						userDetailsService.removeSession(p.getUser());
					}
				}
			}
			
		} catch (ServiceException | IOException e) {
			LOGGER.error("Error while save Monitor", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/monitor/ctype.html", method=RequestMethod.GET)
	public String ctype(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String zone = request.getParameter("zone");
		model.addAttribute("zone",zone);
		setMenu(model,request);
		return "monitor-water-ctype";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/monitor/actype.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String actype(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String zone = request.getParameter("zone");
		if(StringUtils.isNotBlank(zone)){
			@SuppressWarnings("unchecked")
			List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_PROJECT_TYPE);
			//List<Map<String, Object>> relist = new ArrayList<>();
			//加载项目集合
			HashMap<String, Set<MonitorEntity>> set  = pnodeUtils.getMonitorZone(request);
			Set<MonitorEntity> uw = set.get(zone);
			
			
			if(uw!=null){
				Map<String, List<MonitorEntity>> uws = new TreeMap<String, List<MonitorEntity>>();
				
				List<MonitorEntity> cs = new ArrayList<>();
				List<Long> ids =new ArrayList<>();
				for(MonitorEntity u:uw){
					List<MonitorEntity> ulist = uws.get(u.getCtype());
					if(ulist==null){
						ulist = new ArrayList<>();
						uws.put(u.getCtype(), ulist);
					}
					ulist.add(u);
					
					ids.add(u.getId());
					cs.add(u);
				}
				
				List<Map<String, Object>> relist =this.getRemap(uws, blist);
				 
				ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(relist);
					return json;
				
			}
		}
		
		return "";
	}
	
	@SuppressWarnings("unchecked")
	private  List<Map<String, Object>> getRemap(Map<String, List<MonitorEntity>> uws,List<BasedataType> blist){
		Iterator<String> iter = uws.keySet().iterator();
		List<Map<String, Object>> relist = new ArrayList<>();
		List<Map<String, Object>> templist = new ArrayList<>();
		while (iter.hasNext()) {
			Map<String, Object> entry_uws = new HashMap<String, Object>();
			Object key = iter.next();
			List<MonitorEntity> val = uws.get(key);
			int down =0;
			int run =0;
			for(MonitorEntity un:val){
				switch (un.getStatus()) {
				case 0:
					run++;
					break;
				case 1:
					down++;
					break;
				}
			}
			
			entry_uws.put("val", key.toString());
			entry_uws.put("down", down);
			entry_uws.put("run", run);
			entry_uws.put("total", down+run);
			String ctype = key.toString();
			int index = ctype.indexOf("_");
			for(BasedataType bt:blist){
				if(bt.getValue().equalsIgnoreCase(key.toString().trim())){
					entry_uws.put("name", bt.getName());
					break;
				}
			}
			
			if(index==-1){
				relist.add(entry_uws);
			}else{
				//将二级合入到一级
				String suName = ctype.substring(0,index);
				Map<String, Object> entry_ups=null;
				List<Map<String, Object>> submap =null;
				for(Map<String, Object> map:templist){
					if(map.get("name").toString().equalsIgnoreCase(suName)){
						entry_ups =map;
						submap = (List<Map<String, Object>>) entry_ups.get("sublist");
						break;
					}
				}
				if(entry_ups==null){
					entry_ups = new HashMap<String, Object>();
					submap =new ArrayList<>();
					entry_ups.put("down", down);
					entry_ups.put("run", run);
					entry_ups.put("total", down+run);
					templist.add(entry_ups);
				}else{
					entry_ups.put("down", Integer.parseInt(entry_ups.get("down").toString())+down);
					entry_ups.put("run", Integer.parseInt(entry_ups.get("run").toString())+run);
					entry_ups.put("total", Integer.parseInt(entry_ups.get("total").toString())+down+run);
				}
				entry_ups.put("name", suName);
				submap.add(entry_uws);
				entry_ups.put("sublist",submap);
				
			}
			
		}
		//将两者合并
		if(templist!=null){
			for(Map<String, Object> map:templist){
				relist.add(map);
			}
		}
		return relist;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/monitor/images.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String images(HttpServletRequest request, Locale locale) throws Exception {
		
		String sUserId = request.getParameter("cid");
		
		try {
			Monitor csite = monitorService.getByIdWithImg(Long.parseLong(sUserId));
			if(csite!=null){
				if(csite.getImages()!=null && csite.getImages().size()>0){
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(csite.getImages());
					return json;
				}else{
					return "0";
				}
			}
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			
		}
		
		return "-1";
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitor/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteMonitor(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					//Monitor monitor = monitorService.getById(id);
					//if (monitor != null) {
						monitorService.deleteWithPermission(id);
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						resplist.add(resp);
					//}
				} catch (Exception e) {
					e.printStackTrace();
					if (e.getMessage().startsWith("Cannot delete or update a parent row")) {
						resp.setStatus(9997);
					} else {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					}
					resplist.add(resp);
				}
			}
		}
		return resplist;
	}
	@RequestMapping(value = "/water/monitor/app.html", method = RequestMethod.GET)
	public String app(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return "monitor-appmessage";
	}
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("monitor", "monitor");
		activeMenus.put("monitor-list", "monitor-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		System.out.println(request.getAttribute("MENUMAP"));
		Menu currentMenu = (Menu) menus.get("monitor");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
