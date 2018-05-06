package com.kekeinfo.web.admin.controller.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PinyinUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.csite.service.CSiteService;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.entity.PinYinName;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.filter.UserFilter;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.project.service.ProjectService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.business.zone.service.ZoneService;

import org.springframework.web.bind.annotation.ResponseBody;;

@Controller
public class ProjectController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired
	ProjectService projectService;
	@Autowired GroupService groupService;
	@Autowired
	ZoneService zoneService;
	@Autowired DepartmentService departmentService;
	@Autowired UserService userService;
	@Autowired CSiteService cSiteService;
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/project/list.html", method = RequestMethod.GET)
	public String listprojects(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setMenu(model, request);
		Project project = new Project();
		model.addAttribute("project", project);
		String departmentID = request.getParameter("departmentID");
		if(!StringUtils.isBlank(departmentID)){
			try{
				Department department = departmentService.getById(Long.parseLong(departmentID));
				model.addAttribute("pname", department.getName());
				model.addAttribute("pid", department.getId());
			}catch (Exception e){
				
			}
		}else{
			model.addAttribute("pid",-1);
			model.addAttribute("pname", "全部");
		}
		long mdid =-1l;
		//获取监控部门的id
		@SuppressWarnings("unchecked")
		List<Department> departments = (List<Department>) webCacheUtils.getFromCache(Constants.DEPARTMENTS); //所有部门
		for(Department d:departments){
			if(d.getCode().equalsIgnoreCase(Constants.DEPARTMENT_MONITOR)){
				mdid=d.getId();
				break;
			}
		}
		model.addAttribute("mdid", mdid);
		return "admin-project";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/project/departments.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin> project(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = departmentService.getPinYin();
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/project/users.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin> users(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = userService.getPinYin(null);
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/project/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getProject(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<Project> dt = new DataTable<Project>();
		try { // 指定根据什么条件进行模糊查询
			//指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("name"); 
			attributes.add("projectOwner"); 
			attributes.add("phone");
//			attributes.add("memo");
			attributes.add("address");
			attributes.add("zone.name");
			attributes.add("city");
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			String departmentID = request.getParameter("departmentID");
			Long did =null;
			if(StringUtils.isNotBlank(departmentID) && !departmentID.trim().equalsIgnoreCase("-1") ){
				did=Long.parseLong(departmentID);
			}
			
			List<String> where = null;
			if (did!=null) {
				where = pnodeUtils.getPids(request, did);
				//此次应该是ID,而不是项目ID
				if(where!=null && where.size()>0){
					where.set(0, "id");
				}else{
					where =new ArrayList<>();
				}
				//加入部门id
				where.add("department.id");
				where.add(did.toString());
			}
			List<String> join =new ArrayList<String>();
			join.add("zone");
			join.add("department");
			orderby.put("auditSection.dateModified", "desc");
			Entitites<Project> list = projectService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby,where,join);
			
			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
			
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(pnodeUtils.setEditProject(request, egroups, list.getEntites()));
		} catch (Exception e) {
			LOGGER.error("Error while paging projects", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(User.class, UserFilter.class);
		String json = mapper.writeValueAsString(dt);
		return json;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/project/zones.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin> zones(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = zoneService.getPinYin();
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/project/precipitation.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody List<PinYin> precipitation(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<BasedataType> precipitations = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_PRECIPITATION);
			List<PinYin> pinyin = null;
			Map<String,PinYin> map=new HashMap<>();
			for(BasedataType bt:precipitations){
				int index =bt.getValue().indexOf("_");
				String code="公共";
				if(index!=-1){
					 code=bt.getValue().substring(index+1);
				}
				PinYin pinYin =map.get(code);
				if(pinYin==null){
					pinYin=new PinYin();
					List<PinYinName> pNames= new ArrayList<>();
					pinYin.setLists(pNames);
					pinYin.setCode(code);
				}
				PinYinName pName =new PinYinName();
				pName.setId(bt.getName());
				pName.setName(bt.getName());
				pName.setPinyin(code);
				pinYin.getLists().add(pName);
				map.put(code, pinYin);
			}
			if(map.size()>0){
				pinyin=new ArrayList<>();
				for(PinYin p:map.values()){
					pinyin.add(p);
				}
			}
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value = "/water/project/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveProject(@ModelAttribute("project") Project project, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			User puser =null;
			//部门负责人从用户选择过来的
			Long uid = project.getProjectOwnerid();
			if(uid!=null){
				 puser = userService.getById(uid);
				if(puser!=null){
					if (StringUtils.isNotBlank(project.getProjectOwner())&&!project.getProjectOwner().equals(puser.getFirstName())) { //手动输入的用户姓名，而非系统用户名
						project.setProjectOwnerid(null);
					} else {
						project.setProjectOwner(puser.getFirstName());
						project.setPhone(puser.getTelephone());
					}
				}
			}
			int index=projectService.saveOrUpdate(project,puser);
			pnodeUtils.reloadAllDepartments();
			pnodeUtils.relaodAllGroupUser();
			if(index==1){
				pnodeUtils.reloadAllCsite(request);
			}else if(index==2){
				pnodeUtils.reloadAllMonitor();
				request.getSession().setAttribute(Constants.USER_ZONE_MONITOR,null);
			}else if(index==3){
				pnodeUtils.reloadAllGuard();
				request.getSession().setAttribute(Constants.USER_ZONE_GUARD,null);
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (ServiceException e) {
			LOGGER.error("Error while save Project", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/project/getTree.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json;charset=utf-8")
	public @ResponseBody String getTree(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			//判断是否权限状态
			boolean isSelectAll=false;
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			List<Department> departments = (List<Department>) webCacheUtils.getFromCache(Constants.DEPARTMENTS); //所有部门
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")){
				
				Set<DepartmentNode> departmentNodes = user.getpNodes();
				if(departmentNodes!=null && departmentNodes.size()>0){
					for(DepartmentNode pnode:departmentNodes){
						if(pnode.getDepartmentid().longValue()==-1 && pnode.isAll()==true){
							isSelectAll=true;
							break;
						}
					}
				}
				
			}else {
				isSelectAll=true;
			}
			
			if(isSelectAll){
				for(Department department:departments){
					Map<String, Object> entry_camera = new HashMap<String, Object>();
					entry_camera.put("id", department.getId());
					entry_camera.put("name", department.getName());
					resultList.add(entry_camera);
				}
			}else{
				Set<DepartmentNode> departmentNodes = user.getpNodes();
				if(departmentNodes!=null && departmentNodes.size()>0){
					for(Department department:departments){
						for(DepartmentNode pnode:departmentNodes){
							if(pnode.getDepartmentid().longValue()==department.getId().longValue() &&pnode.getType().equalsIgnoreCase("department")){
								Map<String, Object> entry_camera = new HashMap<String, Object>();
								entry_camera.put("id", department.getId());
								entry_camera.put("name", department.getName());
								resultList.add(entry_camera);
								break;
							}
						}
					}
				}
			}
			
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(resultList);
			return json;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value = "/water/project/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteProject(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					Project project = projectService.getById(id);
					if (project != null) {
						projectService.delete(project);
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						resplist.add(resp);
					}
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

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("department", "department");
		activeMenus.put("project-list", "project-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("project");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
