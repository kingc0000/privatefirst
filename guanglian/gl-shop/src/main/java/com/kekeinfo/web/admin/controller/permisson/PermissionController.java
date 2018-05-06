package com.kekeinfo.web.admin.controller.permisson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.kekeinfo.core.business.department.service.DepartmentNodeService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.Permission;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.business.user.service.PermissionService;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.event.BeanEvent;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.LabelUtils;

@Controller
public class PermissionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired PermissionService permissionService;
	
	@Autowired DepartmentNodeService departmentNodeService;
	
	@Autowired UserService userService;
	
	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value="/water/permission/list.html", method=RequestMethod.GET)
	public String listBanners(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Group group = new Group();
		//group.setGrouptype(0);
		//group.setAuditSection(new AuditSection());
		model.addAttribute("group",group);
		//BaseStore store = (BaseStore)request.getAttribute(Constants.ADMIN_STORE);
		//model.addAttribute("supportEn",store.isSupportEnglish());
		List<Permission> rights= permissionService.listAdminPermission();
		List<Permission> r = new ArrayList<Permission>();
		//基本权限
		for(Permission p : rights){
			if( !p.getPermissionName().equalsIgnoreCase("AUTH")){
				r.add(p);
			}
		}
		
		model.addAttribute("rights",r);
		setMenu(model,request);
		return "water-permission";
	}

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value = "/water/permission/server_processing.shtml", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getBanner(@RequestParam String aoData,HttpServletRequest request,
			HttpServletResponse response,Locale locale) {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		
		
		try {
			
			List<Group> groups = groupService.listGroupWithPermission();
			
			DataTable<Group> dt = new DataTable<Group>();
			dt.setsEcho(dataTableParam.getsEcho()+1);
			dt.setiTotalDisplayRecords(groups.size());
	        dt.setiTotalRecords(groups.size());
	        //dt.setAaData(groups);
	        Gson gson = new Gson();
	        String jsondata= gson.toJson(dt);
	        if(groups !=null && groups.size()>0){
	        	
		        StringBuffer json = new StringBuffer();
		        json.append("[");
		        //获取基础数据
		      
		        for(Group ms:groups){
		        	json.append("{\"id\":").append(ms.getId()).append(",");
		        	json.append("\"groupName\":").append("\"").append(ms.getGroupName()).append("\",");
		        	if(ms.getPermissions() !=null && ms.getPermissions().size()>0){
		        		json.append("\"permissions\":[");
		        		for(Permission g:ms.getPermissions()){
		        			json.append(g.getId()).append(",");
		        		}
		        		//去掉最后一个逗号
		 		       json.deleteCharAt(json.lastIndexOf(","));
		 		       json.append("],");
		        	}else{
		        		json.append("\"permissions\":").append("\"\",");
		        	}
		        	json.append("\"name\":").append("\"").append(ms.getName()==null ?"":ms.getName()).append("\",");
		        	json.append("\"type\":").append("\"").append(ms.getGrouptype()).append("\",");
		        	json.append("\"sGroup\":").append("\"").append(ms.issGroup()).append("\",");
		        	json.append("\"grouptype\":").append("\"").append(ms.getGrouptype()).append("\",");
		        	json.append("\"operate\":").append(ms.getId()).append("},");
		        	
		        }
		        json.append("]");
		        //去掉最后一个逗号
		       json.deleteCharAt(json.lastIndexOf(","));
		      
		       jsondata = jsondata.substring(0, jsondata.length()-1)+",\"aaData\":"+json.toString()+"}";
	        }else{
	        	jsondata = jsondata.substring(0, jsondata.length()-1)+",\"aaData\":[]"+"}";
	        }
	        
	        return jsondata;
		} catch (Exception e) {
			LOGGER.error("Error while paging banners", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return null;
	}

	@PreAuthorize("hasRole('SUPERADMIN')") 
	@RequestMapping(value="/water/permission/save.shtml", method=RequestMethod.POST, produces="application/json; charset=utf-8")
	public @ResponseBody String saveBanner(@ModelAttribute("group") Group banner, Model model, HttpServletRequest request, HttpServletResponse response)  {
		AjaxResponse resp = new AjaxResponse();
		try {
			
			
			banner.getAuditSection().setModifiedBy(request.getRemoteUser());
			System.out.println("-----------"+1);
			groupService.saveOrUpdate(banner);
			
		} catch (ServiceException e ) {
			LOGGER.error("Error while save basedata ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@RequestMapping(value="/water/permission/checkValidCode.shtml", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
	public @ResponseBody String checkValidCode(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
		String domain=request.getParameter("groupName");
		AjaxResponse resp = new AjaxResponse();
		if(StringUtils.isBlank(domain)) {
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			return resp.toJSONString();
		}
		try {
			Group content = groupService.getByName(domain);
			if(!StringUtils.isBlank(id)) {
				try {
					Long lid = Long.parseLong(id);
					if(content == null || (content!=null && content.getGroupName().equals(domain) && content.getId().longValue()==lid)) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						return resp.toJSONString();
					} 
				} catch (Exception e) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					return resp.toJSONString();
				}
			} else {
				if(content==null) {
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					return resp.toJSONString();
				}
			}
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			} catch (Exception e) {
				if(e.getMessage().equalsIgnoreCase("No entity found for query; nested exception is javax.persistence.NoResultException: No entity found for query")){
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
					return resp.toJSONString();
				}
				//LOGGER.error("Error while getting category", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorMessage(e);
			}
			
			String returnString = resp.toJSONString();
			return returnString;
	} 

	@PreAuthorize("hasRole('SUPERADMIN')")
	@RequestMapping(value="/water/permission/remove.shtml", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody List<AjaxResponse> remove(@RequestParam String[] listId,HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId!=null&&listId.length>0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Integer id = Integer.valueOf(listId[i]);
				try {
					groupService.deleteById(id);
					//PNodeUtils.reloadAllUser(SpringContextUtils.getServletContext(), departmentNodeService, userService);
					resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
					resplist.add(resp);
				} catch (Exception e) {
					e.printStackTrace();
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					resplist.add(resp);
				}
			}
			ApplicationContext context = SpringContextUtils.getApplicationContext();
			context.publishEvent(new BeanEvent(context, BeanEvent.BASEDATA_SRC));
		}
		return resplist;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("base-right", "base-right");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("base-right");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
	}
}
