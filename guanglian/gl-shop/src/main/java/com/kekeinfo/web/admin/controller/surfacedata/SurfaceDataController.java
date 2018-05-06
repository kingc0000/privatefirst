package com.kekeinfo.web.admin.controller.surfacedata;

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
import com.kekeinfo.core.business.monitor.data.model.SurfaceData;
import com.kekeinfo.core.business.monitor.data.service.SurfaceDataService;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.surface.model.MbasePoint;
import com.kekeinfo.core.business.monitor.surface.model.Surface;
import com.kekeinfo.core.business.monitor.surface.service.SurfaceService;
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
public class SurfaceDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SurfaceDataController.class);
	@Autowired
	SurfaceDataService surfacedataService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired SurfaceService surfaceService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private GroupService groupService;
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/surfacedata/list.html", method = RequestMethod.GET)
	public String listsurfacedatas(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		String sid = request.getParameter("sid");
		String mid = request.getParameter("mid");
		SurfaceData surfacedata = new SurfaceData();
		model.addAttribute("surfacedata", surfacedata);
		if(StringUtils.isNotBlank(sid)){
			MbasePoint<Surface> surface = surfaceService.getById(Long.parseLong(sid));
			BigDecimal intiHeight = surface.getInitHeight();
			model.addAttribute("initHeight", intiHeight);
			model.addAttribute("sid", sid);
		}
		
		boolean hasRight = false;
        MonitorEntity me=null;
		if(StringUtils.isNotBlank(mid)){
			@SuppressWarnings("unchecked")
			List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
			for(MonitorEntity m:listen){
				if(m.getId().equals(Long.parseLong(mid))){
					me=m;
					model.addAttribute("mentity", m);
					break;
				}
			}
		}
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
		request.setAttribute("activeCode", "surface"); //指定项目
		model.addAttribute("mid", mid);
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		if(user.getuAgent()!=null && user.getuAgent()!=""){
			return "admin-surfacenow";
		}else{
			return "admin-surfacedata";
		}
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/surfacedata/oblique.shtml", method = { RequestMethod.GET,
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
			Entitites<MbasePoint<Surface>> list = surfaceService.getPageListByAttributesLike(null, null, 1, 
					page, orderby, where, null, true);
			if(list!=null && list.getTotalCount()>0){
				Map<String, Object> entry = new HashMap<String, Object>();
				entry.put("total", list.getTotalCount());
				entry.put("mname", list.getEntites().get(0).getMarkNO());
				entry.put("tid", list.getEntites().get(0).getId());
				Date date =new Date();
				MDbasePoint<SurfaceData> obd= surfacedataService.getLast(date,MPointEnumType.Surface, list.getEntites().get(0).getId());
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
	@RequestMapping(value = "/water/surfacedata/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getSurfaceData(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<MDbasePoint<SurfaceData>> dt = new DataTable<MDbasePoint<SurfaceData>>();
		String sid = request.getParameter("sid");
		if(StringUtils.isNotBlank(sid)){
			try { 
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<Object[]> where =new ArrayList<>();
				where.add(new Object[]{"spoint.id", sid});
				Entitites<MDbasePoint<SurfaceData>> list = surfacedataService.getPageListByAttributesLike(null, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null, true);
				dt.setsEcho(dataTableParam.getsEcho() + 1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
				dt.setiTotalRecords(list.getTotalCount());
				dt.setAaData(list.getEntites());
			} catch (Exception e) {
				LOGGER.error("Error while paging surfacedatas", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/surfacedata/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveSurfaceData(@ModelAttribute("surfacedata") SurfaceData surfacedata, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			if(surfacedata.getCalibration()==null){
				surfacedata.setCalibration(new Date());
			};
			surfacedataService.saveOrUpdate(surfacedata);
		} catch (ServiceException e) {
			LOGGER.error("Error while save SurfaceData", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/surfacedata/getMain.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getsurface(HttpServletRequest request, HttpServletResponse response) throws org.codehaus.jackson.JsonGenerationException, org.codehaus.jackson.map.JsonMappingException, IOException {
		String sid = request.getParameter("listId");
		if(StringUtils.isNotBlank(sid)){
			MbasePoint<Surface> surface = surfaceService.getById(Long.parseLong(sid));
			Map<String, Object> entry = new HashMap<String, Object>();
			entry.put("init", surface.getInitHeight()==null ? "" :surface.getInitHeight());
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(entry);
			return json;
		}
		
		return "";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/surfacedata/getByDate.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
			MDbasePoint<SurfaceData> surfaceData = null;
			try {
				surfaceData = surfacedataService.getLast(fdate, MPointEnumType.Surface,Long.parseLong(sid));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			Map<String, Object> entry = new HashMap<String, Object>();
			if(surfaceData==null) {
				MbasePoint<Surface> surface = surfaceService.getById(Long.parseLong(sid));
				entry.put("last", surface.getInitHeight());
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(entry);
				return json;
			}
			entry.put("last", surfaceData.getCurtHeight()==null ? "" :surfaceData.getCurtHeight());
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(entry);
			return json;
		}
		
		return "";
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/surfacedata/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteSurfaceData(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					MDbasePoint<SurfaceData> surfacedata = surfacedataService.getByIdWithPoint(id, MPointEnumType.Surface);
					if (surfacedata != null) {
						surfacedataService.deleteAndUpdate(surfacedata);
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
