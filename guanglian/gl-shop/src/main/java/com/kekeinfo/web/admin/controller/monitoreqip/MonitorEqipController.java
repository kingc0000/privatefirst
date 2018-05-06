package com.kekeinfo.web.admin.controller.monitoreqip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.model.MPoint;
import com.kekeinfo.core.business.monitor.service.MPointEquipService;
import com.kekeinfo.core.business.monitoreqip.model.MonitorEqip;
import com.kekeinfo.core.business.monitoreqip.model.MpointEquip;
import com.kekeinfo.core.business.monitoreqip.service.MonitorEqipService;
import com.kekeinfo.core.business.monitoreqip.service.MpointEquipService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.MonitorEntity;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class MonitorEqipController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorEqipController.class);
	@Autowired
	MonitorEqipService monitoreqipService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private MPointEquipService mPointEquipService;
	@Autowired private MpointEquipService mpointEquipService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private GroupService groupService;

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitoreqip/list.html", method = RequestMethod.GET)
	public String listmonitoreqips(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		setMenu(model, request);
		MonitorEqip monitoreqip = new MonitorEqip();
		model.addAttribute("monitoreqip", monitoreqip);
		String mid = request.getParameter("mid");
		//判断用户是否有该项目的编辑权限
		MonitorEntity me=null;
		if(StringUtils.isNotBlank(mid)){
			@SuppressWarnings("unchecked")
			List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
			for(MonitorEntity m:listen){
				if(m.getId().equals(Long.parseLong(mid))){
					model.addAttribute("mentity", m);
					me=m;
					break;
				}
			}
		}
		//判断用户是否有该项目的编辑权限
		boolean hasRight = false;
        if(me!=null){
			if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
	        	if(request.isUserInRole("EDIT-PROJECT")){
	        		List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
	        		hasRight=pnodeUtils.hasProjectRight(request, egroups, (BaseEntity)me);
	        	}
			}else{
				hasRight=true;
			}
		}
		request.setAttribute("activeCode", "equip"); //指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("mid", mid);
		List<MPoint> mpoints = mPointEquipService.list();
		model.addAttribute("ms", mpoints);
		return "admin-monitoreqip";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/meqips/list.html", method = RequestMethod.GET)
	public String meps(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		setMenu(model, request);
		String mid = request.getParameter("mid");
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
		if(StringUtils.isNotBlank(mid)){
			@SuppressWarnings("unchecked")
			List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
			for(MonitorEntity m:listen){
				if(m.getId().equals(Long.parseLong(mid))){
					model.addAttribute("mentity", m);
					break;
				}
			}
		}
		request.setAttribute("activeCode", "meps"); //指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("mid", mid);
		List<MPoint> mpoints = mPointEquipService.list();
		model.addAttribute("ms", mpoints);
		return "admin-meqips";
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitoreqip/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getMonitorEqip(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<MonitorEqip> dt = new DataTable<MonitorEqip>();
		String mid = request.getParameter("mid");
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("equip.name"); 
			attributes.add("equip.eNO"); 
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			List<String> where =new ArrayList<>();
			where.add("monitor.id");
			where.add(mid);
			Entitites<MonitorEqip> list = monitoreqipService.getPageListByAttributesLike(attributes,
					dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging monitoreqips", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/meqips/meqps_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getMonitorEqips(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<MpointEquip> dt = new DataTable<MpointEquip>();
		String mid = request.getParameter("mid");
		String type = request.getParameter("type");
		try { // 指定根据什么条件进行模糊查询
			
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			Map<String, String> fetches = new HashMap<String, String>();
			fetches.put("monitorEqip", "LEFT");
			List<Object[]> where =new ArrayList<>();
			where.add(new Object[]{"monitor.id", mid});
			where.add(new Object[]{"mpoint.id", type});
			Entitites<MpointEquip> list = mpointEquipService.getPageListByAttributesLike(null,dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, fetches, true);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging monitoreqips", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}
	

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitoreqip/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveMonitorEqip(@Valid @ModelAttribute("monitoreqip") MonitorEqip monitoreqip, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		String temp[] = request.getParameterValues("mps");
		//String mpString =request.getParameter("mps");
		try {
			monitoreqipService.saveOrUpdate(monitoreqip,temp);
		} catch (ServiceException e) {
			LOGGER.error("Error while save MonitorEqip", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/monitoreqip/getmpoint.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getMpoint(HttpServletRequest request, HttpServletResponse response) throws org.codehaus.jackson.JsonGenerationException, org.codehaus.jackson.map.JsonMappingException, IOException{
		
		String meid =request.getParameter("meid");
		List<String> resp =new ArrayList<>();
		try {
			List<MpointEquip> mps =mpointEquipService.getByMeid(Long.parseLong(meid));
			if(mps!=null && mps.size()>0){
				for(MpointEquip m:mps){
					resp.add(m.getMpoint().getId().toString());
				}
				
			}
		} catch (ServiceException e) {
		}
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(resp);
		return json;
	}
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitoreqip/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteMonitorEqip(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					MonitorEqip monitoreqip = monitoreqipService.getById(id);
					if (monitoreqip != null) {
						monitoreqipService.remove(monitoreqip);
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
		activeMenus.put("monitor", "monitor");
		activeMenus.put("monitor-list", "monitor-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("monitor");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
