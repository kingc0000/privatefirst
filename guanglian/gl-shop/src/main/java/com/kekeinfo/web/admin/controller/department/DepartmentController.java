package com.kekeinfo.web.admin.controller.department;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.department.service.DepartmentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.business.zone.service.ZoneService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.controller.ControllerConstants;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.PinYin;
import com.kekeinfo.web.filter.UserFilter;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PinyinUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class DepartmentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private PNodeUtils pnodeUtils;	
	@Autowired DepartmentService departmentService;
	
	@Autowired DepartmentNodeService departmentNodeService;
	
	@Autowired UserService userService;
	
	@Autowired ZoneService zoneService;
	
	@Autowired GroupService groupService;
	
	//private final static String NEW_STORE_TMPL = "email_template_new_store.ftl";
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/department/list.html", method=RequestMethod.GET)
	public String displayStores(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		
		setMenu(model,request);
		Department department = new Department();
		model.addAttribute("department", department);
		Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER);
		model.addAttribute("did", waterDept.getId());
		return ControllerConstants.Tiles.Department.department;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/department/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
		String getDepartments(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		
		try {
			//指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("name"); 
			attributes.add("projectOwner"); 
			attributes.add("phone");
			attributes.add("memo");
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			List<String> where =pnodeUtils.getDids(request);
			
			Entitites<Department> list = departmentService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby,where,null);
			
			List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
			
			
			DataTable<Department> dt = new DataTable<Department>();
			dt.setsEcho(dataTableParam.getsEcho()+1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
	        dt.setiTotalRecords(list.getTotalCount());
	        dt.setAaData(pnodeUtils.setEditDepartment(request, egroups, list.getEntites()));
	        
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.getSerializationConfig().addMixInAnnotations(User.class, UserFilter.class);
			String json = mapper.writeValueAsString(dt);
			return json;
		} catch (Exception e) {
			LOGGER.error("Error while paging departments", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return null;
	}
	
	
	@PreAuthorize("hasRole('EDIT-PROJECT')") 
	@RequestMapping(value="/water/department/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String saveBanner(@ModelAttribute("department") Department department, Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		
		try {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			User puser=null;
			if(user!=null){
				department.getAuditSection().setModifiedBy(user.getId().toString());
				if(department.getId()==null){
					department.getAuditSection().setDateCreated(new Date());
				}else{
					department.getAuditSection().setDateModified(new Date());
				}
				//项目负责人从用户选择过来的
				Long uid = department.getDepartmentOwnerid();
				if(uid!=null){
					 puser = userService.getById(uid);
					if(puser!=null){
						department.setDepartmentOwner(puser.getFirstName());
						department.setPhone(puser.getTelephone());
						department.setDepartmentOwnerid(puser.getId());
					}
				}
				
				departmentService.saveOrUpdateWithUser(department,puser);
				pnodeUtils.reloadAllDepartments();
				/**
				if(puser!=null){
					//权限到缓存
					pnodeUtils.restids(request, puser.getId(), departmentNodeService);
				}*/
			}
			//
		} catch (ServiceException e) {
			LOGGER.error("Error while save banner ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/department/zones.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> zones(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = zoneService.getPinYin();
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/department/checkDepartmentCode.shtml", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody String checkValidCode(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
		String domain=request.getParameter("name");
		AjaxResponse resp = new AjaxResponse();
		if(StringUtils.isBlank(domain)) {
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			return resp.toJSONString();
		}
		try {
			Department department = departmentService.getByName(domain);
			if(!StringUtils.isBlank(id)) {
				try {
					Long lid = Long.parseLong(id);
					if(department == null || (department!=null && department.getName().equals(domain) && department.getId().longValue()==lid)) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						return resp.toJSONString();
					} 
				} catch (Exception e) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					return resp.toJSONString();
				}
			} else {
				if(department==null) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					return resp.toJSONString();
				}
			}
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			} catch (Exception e) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorMessage(e);
			}
			
			String returnString = resp.toJSONString();
			return returnString;
	} 
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/department/getCiste.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String getCsite(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			
			if(!StringUtils.isBlank(id)) {
				Long lid = Long.parseLong(id);
				Department department =departmentService.getByIDWithCsite(lid);
				Project csite=department.getcSites().iterator().next();
				StringBuffer json = new StringBuffer();
				json.append("{\"zone\":").append("\"").append(csite.getZone()==null?"":csite.getZone().getName()).append("\",");
				json.append("\"zoneid\":").append(csite.getZone()==null?"-1":csite.getZone().getId()).append(",");
				json.append("\"city\":").append("\"").append(csite.getCity()==null?"":csite.getCity()).append("\",");
				json.append("\"address\":").append("\"").append(csite.getAddress()==null?"":csite.getAddress()).append("\"}");
				return json.toString();
				//return csite;
				
			} 
			
			} catch (Exception e) {
				
			}
			return null;
	} 

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value="/water/department/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					Department department = departmentService.getByIDWithCsite(id);
					if (department!=null) {
						departmentService.deleteWithPermission(department);
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						resplist.add(resp);
					} 
				} catch (Exception e) {
					e.printStackTrace();
					if(e.getMessage().startsWith("Cannot delete or update a parent row")){
						resp.setStatus(9997);
					}else{
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					}
					resplist.add(resp);
				}
			}
			pnodeUtils.reloadAllDepartments();
		}
		return resplist;
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/water/department/getDepartmentSitesTree.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json; charset=utf-8")
	public @ResponseBody String getProjectSitesTree(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
//			Map<String, String> fetch = new HashMap<String, String>();
//			fetch.put("cSites", "LEFT");
//			Entitites<Department> result = departmentService.getPageListByAttributesLike(null, null, null, null, null, null, fetch, true);
			@SuppressWarnings("unchecked")
			List<Department> departments = (List<Department>) webCacheUtils.getFromCache(Constants.DEPARTMENTS); //所有部门
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (Department department : departments) {
				Map<String, Object> entry = new HashMap<String, Object>();
				entry.put("id", department.getId());
				entry.put("name", department.getName());
				Set<Project> csites = department.getcSites();
				if (csites!=null && csites.size()>0) {
					List<Map<String, Object>> csiteList = new ArrayList<Map<String, Object>>();
					Iterator<Project> iter = csites.iterator();
					while (iter.hasNext()) {
						Project csite = iter.next();
						Map<String, Object> entry_csite = new HashMap<String, Object>();
						entry_csite.put("id", csite.getId());
						entry_csite.put("name", csite.getName());
						csiteList.add(entry_csite);
					}
					entry.put("cSites", csiteList);
				}
				resultList.add(entry);
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(resultList);
			return json;
		}catch (Exception e){
			return null;
		}
		
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/department/users.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> project(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = userService.getPinYin(null);
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("department", "department");
		activeMenus.put("department-list", "department-list");

		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("department");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
