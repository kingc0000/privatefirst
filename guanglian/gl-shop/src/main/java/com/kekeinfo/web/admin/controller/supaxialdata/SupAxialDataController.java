package com.kekeinfo.web.admin.controller.supaxialdata;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.data.model.MDbasePoint;
import com.kekeinfo.core.business.monitor.data.model.SupAxialData;
import com.kekeinfo.core.business.monitor.data.service.SupAxialDataService;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.model.SupAxial;
import com.kekeinfo.core.business.monitor.surface.service.SupAxialService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
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
public class SupAxialDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SupAxialDataController.class);
	@Autowired
	SupAxialDataService supaxialdataService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired SupAxialService supAxialService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private GroupService groupService;

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/supaxialdata/list.html", method = RequestMethod.GET)
	public String listsupaxialdatas(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		String sid = request.getParameter("sid");
		String mid = request.getParameter("mid");
		if(StringUtils.isNotBlank(sid)){
			model.addAttribute("sid", sid);
			MbasePoint<SupAxial> supAxial = supAxialService.getById(Long.parseLong(sid));
			BigDecimal intiHeight = supAxial.getInitHeight();
			model.addAttribute("initHeight", intiHeight);
			
		}
		MonitorEntity me=null;
		SupAxialData supaxialdata = new SupAxialData();
		model.addAttribute("supaxialdata", supaxialdata);
		
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
        model.addAttribute("hasRight", hasRight);
		request.setAttribute("activeCode", "supaxial"); //指定项目
		model.addAttribute("mid", mid);
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		if(user.getuAgent()!=null && user.getuAgent()!=""){
			return "admin-supaxialnow";
		}else{
			return "admin-supaxialdata";
		}
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/supaxialdata/oblique.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getOblique(HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		String mid = request.getParameter("mid");
		String mname=request.getParameter("mname");
		int page=0;
		String ppage=request.getParameter("page");
		if(StringUtils.isNotBlank(ppage)){
			try {
				page =Integer.parseInt(ppage);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		try { // 指定根据什么条件进行模糊查询
			
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("markNO", "asc");
			List<Object[]> where =new ArrayList<>();
			where.add(new Object[]{"monitor.id", mid});
			if(StringUtils.isNotBlank(mname)){
				where.add(new Object[]{"markNO", mname});
			}
			Entitites<MbasePoint<SupAxial>> list = supAxialService.getPageListByAttributesLike(null, null, 1, 
					page, orderby, where, null, true);
			if(list!=null && list.getTotalCount()>0){
				Map<String, Object> entry = new HashMap<String, Object>();
				entry.put("total", list.getTotalCount());
				entry.put("mname", list.getEntites().get(0).getMarkNO());
				entry.put("tid", list.getEntites().get(0).getId());
				Date date =new Date();
				MDbasePoint<SupAxialData> obd= supaxialdataService.getLast(date,MPointEnumType.SupAxial, list.getEntites().get(0).getId());
				if(obd==null){
					entry.put("last", list.getEntites().get(0).getInitHeight());
				}else{
					entry.put("last", obd.getCurtHeight()==null ? list.getEntites().get(0).getInitHeight() :obd.getCurtHeight());
				}
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(entry);
				return json;
			}
			
		} catch (Exception e) {
			LOGGER.error("Error while paging obliques", e);
		}
		return null;
		
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/supaxialdata/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getSupAxialData(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<MDbasePoint<SupAxialData>> dt = new DataTable<MDbasePoint<SupAxialData>>();
		String sid = request.getParameter("sid");
		if(StringUtils.isNotBlank(sid)){
			try { // 指定根据什么条件进行模糊查询
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<Object[]> where =new ArrayList<>();
				where.add(new Object[]{"spoint.id", sid});
				Entitites<MDbasePoint<SupAxialData>> list = supaxialdataService.getPageListByAttributesLike(null,
						dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null,true);
				dt.setsEcho(dataTableParam.getsEcho() + 1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
				dt.setiTotalRecords(list.getTotalCount());
				dt.setAaData(list.getEntites());
			} catch (Exception e) {
				LOGGER.error("Error while paging supaxialdatas", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/supaxialdata/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveSupAxialData(@ModelAttribute("supaxialdata") SupAxialData supaxialdata, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			if(supaxialdata.getCalibration()==null){
				supaxialdata.setCalibration(new Date());
			}
			supaxialdataService.saveOrUpdate(supaxialdata);
		} catch (ServiceException e) {
			LOGGER.error("Error while save SupAxialData", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/supaxialdata/getMain.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getsurface(HttpServletRequest request, HttpServletResponse response) throws org.codehaus.jackson.JsonGenerationException, org.codehaus.jackson.map.JsonMappingException, IOException {
		String sid = request.getParameter("listId");
		if(StringUtils.isNotBlank(sid)){
			MbasePoint<SupAxial> supAxial = supAxialService.getById(Long.parseLong(sid));
			Map<String, Object> entry = new HashMap<String, Object>();
			entry.put("init", supAxial.getInitHeight()==null ? "" :supAxial.getInitHeight());
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(entry);
			return json;
		}
		
		return "";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/supaxialdata/getByDate.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getByDate(HttpServletRequest request, HttpServletResponse response) throws org.codehaus.jackson.JsonGenerationException, org.codehaus.jackson.map.JsonMappingException, IOException {
		String date = request.getParameter("date");
		String sid = request.getParameter("sid");
		if(StringUtils.isNotBlank(sid)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date fdate = null;
			try {
				fdate = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			MDbasePoint<SupAxialData> supAxialData = null;
			try {
				supAxialData = supaxialdataService.getLast(fdate, MPointEnumType.SupAxial,Long.parseLong(sid));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			Map<String, Object> entry = new HashMap<String, Object>();
			if(supAxialData==null) {
				MbasePoint<SupAxial> supAxial = supAxialService.getById(Long.parseLong(sid));
				entry.put("last", supAxial.getInitHeight());
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(entry);
				return json;
			}
			entry.put("last", supAxialData.getCurtHeight()==null ? "" :supAxialData.getCurtHeight());
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(entry);
			return json;
		}
		
		return "";
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/supaxialdata/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteSupAxialData(@RequestParam String[] listId,
			HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					MDbasePoint<SupAxialData> supaxialdata = supaxialdataService.getByIdWithPoint(id, MPointEnumType.SupAxial);
					if (supaxialdata != null) {
						supaxialdataService.deleteAndUpdate(supaxialdata);
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
		activeMenus.put("supaxialdata", "supaxialdata");
		activeMenus.put("supaxialdata-list", "supaxialdata-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("supaxialdata");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
