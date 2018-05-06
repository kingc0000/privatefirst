package com.kekeinfo.web.admin.controller.diameterconvertdata;

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
import com.kekeinfo.core.business.guard.data.model.DiameterConvertData;
import com.kekeinfo.core.business.guard.data.model.GDbasePoint;
import com.kekeinfo.core.business.guard.data.service.DiameterConvertDataService;
import com.kekeinfo.core.business.guard.model.GPointEnumType;
import com.kekeinfo.core.business.guard.point.model.DiameterConvert;
import com.kekeinfo.core.business.guard.point.model.GbasePoint;
import com.kekeinfo.core.business.guard.point.service.DiameterConvertService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.GuardEntity;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class DiameterConvertDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DiameterConvertDataController.class);
	@Autowired
	DiameterConvertDataService diameterconvertdataService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired DiameterConvertService diameterconvertService;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private GroupService groupService;

	@RequestMapping(value = "/water/diameterconvertdata/list.html", method = RequestMethod.GET)
	public String listdiameterconvertdatas(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		String sid = request.getParameter("sid");
		String gid = request.getParameter("gid");
		if(StringUtils.isNotBlank(sid)){
			model.addAttribute("sid", sid);
			GbasePoint<DiameterConvert> building = diameterconvertService.getById(Long.parseLong(sid));
			BigDecimal intiHeight = building.getInitHeight();
			model.addAttribute("initHeight", intiHeight);
		}
		
		DiameterConvertData diameterconvertdata = new DiameterConvertData();
		model.addAttribute("diameterconvertdata", diameterconvertdata);
		
		GuardEntity ge=null;
		if(StringUtils.isNotBlank(gid)){
			@SuppressWarnings("unchecked")
			List<GuardEntity> listen = (List<GuardEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_GUARD);
			for(GuardEntity m:listen){
				if(m.getId().equals(Long.parseLong(gid))){
					model.addAttribute("gentity", m);
					ge=m;
					break;
				}
			}
		}
		boolean hasRight = false;
		if(ge!=null){
			if(!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN") ){
	        	if(request.isUserInRole("EDIT-PROJECT")){
	        		List<Group> egroups = groupService.listGroupByPermissionName("EDIT-PROJECT",1);
	        		hasRight=pnodeUtils.hasProjectRight(request, egroups, (BaseEntity)ge);
	        	}
			}else{
				hasRight=true;
			}
		}
		model.addAttribute("hasRight", hasRight);
		request.setAttribute("activeCode", "diameterconvert"); //指定项目
		model.addAttribute("gid", gid);
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		if(user.getuAgent()!=null && user.getuAgent()!=""){
			return "admin-diameterconvertnow";
		}else{
			return "admin-diameterconvertdata";
		}
	}

	@RequestMapping(value = "/water/diameterconvertdata/oblique.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getOblique(HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		String gid = request.getParameter("gid");
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
			where.add(new Object[]{"guard.id", gid});
			if(StringUtils.isNotBlank(mname)){
				where.add(new Object[]{"markNO", mname});
			}
			Entitites<GbasePoint<DiameterConvert>> list = diameterconvertService.getPageListByAttributesLike(null, null, 1, 
					page, orderby, where, null, true);
			if(list!=null && list.getTotalCount()>0){
				Map<String, Object> entry = new HashMap<String, Object>();
				entry.put("total", list.getTotalCount());
				entry.put("mname", list.getEntites().get(0).getMarkNO());
				entry.put("tid", list.getEntites().get(0).getId());
				Date date =new Date();
				GDbasePoint<DiameterConvertData> obd= diameterconvertdataService.getLast(date,GPointEnumType.DiameterConvert, list.getEntites().get(0).getId());
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
	
	@RequestMapping(value = "/water/diameterconvertdata/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getDiameterConvertData(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<GDbasePoint<DiameterConvertData>> dt = new DataTable<GDbasePoint<DiameterConvertData>>();
		String sid = request.getParameter("sid");
		if(StringUtils.isNotBlank(sid)){
			try { // 指定根据什么条件进行模糊查询
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<Object[]> where =new ArrayList<>();
				where.add(new Object[]{"spoint.id", sid});
				Entitites<GDbasePoint<DiameterConvertData>> list = diameterconvertdataService.getPageListByAttributesLike(null, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null, true);
				dt.setsEcho(dataTableParam.getsEcho() + 1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
				dt.setiTotalRecords(list.getTotalCount());
				dt.setAaData(list.getEntites());
			} catch (Exception e) {
				LOGGER.error("Error while paging diameterconvertdatas", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@RequestMapping(value = "/water/diameterconvertdata/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveDiameterConvertData(
			@ModelAttribute("diameterconvertdata") DiameterConvertData diameterconvertdata, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			if(diameterconvertdata.getCalibration()==null){
				diameterconvertdata.setCalibration(new Date());
			}
			diameterconvertdataService.saveOrUpdate(diameterconvertdata);
		} catch (ServiceException e) {
			LOGGER.error("Error while save DiameterConvertData", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@RequestMapping(value = "/water/diameterconvertdata/getMain.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getsurface(Model model,HttpServletRequest request, HttpServletResponse response) throws org.codehaus.jackson.JsonGenerationException, org.codehaus.jackson.map.JsonMappingException, IOException {
		String sid = request.getParameter("listId");
		if(StringUtils.isNotBlank(sid)){
			GbasePoint<DiameterConvert> building = diameterconvertService.getById(Long.parseLong(sid));
			Map<String, Object> entry = new HashMap<String, Object>();
			entry.put("init", building.getInitHeight()==null ? "" :building.getInitHeight());
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(entry);
			return json;
		}
		
		return "";
	}
	
	@RequestMapping(value = "/water/diameterconvertdata/getByDate.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
			GDbasePoint<DiameterConvertData> buildingdata = null;
			try {
				buildingdata = diameterconvertdataService.getLast(fdate, GPointEnumType.DiameterConvert,Long.parseLong(sid));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			Map<String, Object> entry = new HashMap<String, Object>();
			if(buildingdata==null) {
				GbasePoint<DiameterConvert> building = diameterconvertService.getById(Long.parseLong(sid));
				entry.put("last", building.getInitHeight());
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(entry);
				return json;
			}
			entry.put("last", buildingdata.getCurtHeight()==null ? "" :buildingdata.getCurtHeight());
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(entry);
			return json;
		}
		
		return "";
	}

	@RequestMapping(value = "/water/diameterconvertdata/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteDiameterConvertData(@RequestParam String[] listId,
			HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					GDbasePoint<DiameterConvertData> diameterconvertdata = diameterconvertdataService.getById(id);
					if (diameterconvertdata != null) {
						diameterconvertdataService.delete(diameterconvertdata);
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
		activeMenus.put("diameterconvertdata", "diameterconvertdata");
		activeMenus.put("diameterconvertdata-list", "diameterconvertdata-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("diameterconvertdata");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
