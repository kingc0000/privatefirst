package com.kekeinfo.web.admin.controller.obliquedata;

import java.io.IOException;
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
import com.kekeinfo.core.business.monitor.oblique.model.Oblique;
import com.kekeinfo.core.business.monitor.oblique.model.ObliqueData;
import com.kekeinfo.core.business.monitor.oblique.service.ObliqueDataService;
import com.kekeinfo.core.business.monitor.oblique.service.ObliqueService;
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
public class ObliqueDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ObliqueDataController.class);
	@Autowired
	ObliqueDataService obliquedataService;
	@Autowired ObliqueService  obliqueService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private GroupService groupService;
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/obliquedata/list.html", method = RequestMethod.GET)
	public String listobliquedatas(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		String tid = request.getParameter("tid");
		String sid = request.getParameter("sid");
		String mid= request.getParameter("mid");
		model.addAttribute("sid", sid);
		ObliqueData obliquedata = new ObliqueData();
		model.addAttribute("obliquedata", obliquedata);
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
		request.setAttribute("activeCode", "oblique"); //指定项目
		model.addAttribute("tid", tid);
		model.addAttribute("sid", sid);
		model.addAttribute("mid", mid);
		model.addAttribute("hasRight", hasRight);
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		if(user.getuAgent()!=null && user.getuAgent()!=""){
			return "admin-obliquedatanow";
		}else{
			return "admin-obliquedata";
		}
	}

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/obliquedata/oblique.shtml", method = { RequestMethod.GET,
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
			orderby.put("namber", "asc");
			List<Object[]> where =new ArrayList<>();
			where.add(new Object[]{"monitor.id", mid});
			if(StringUtils.isNotBlank(mname)){
				where.add(new Object[]{"namber", mname});
			}
			Entitites<Oblique> list = obliqueService.getPageListByAttributesLike(null, null, 1, 
					page, orderby, where, null, true);
			if(list!=null && list.getTotalCount()>0){
				Map<String, Object> entry = new HashMap<String, Object>();
				entry.put("total", list.getTotalCount());
				entry.put("mname", list.getEntites().get(0).getNamber());
				entry.put("tid", list.getEntites().get(0).getId());
				Date date =new Date();
				ObliqueData obd= obliquedataService.getLast(date, list.getEntites().get(0).getId());
				if(obd==null){
					entry.put("last", 0);
				}else{
					entry.put("last", obd.getCurTotal()==null ? 0 :obd.getCurTotal());
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
	@RequestMapping(value = "/water/obliquedata/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getObliqueData(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<ObliqueData> dt = new DataTable<ObliqueData>();
		String tid = request.getParameter("tid");
		if(StringUtils.isNotBlank(tid)){
			try { // 指定根据什么条件进行模糊查询
				List<String> attributes = new ArrayList<String>();
				attributes.add("lastTotal");
				HashMap<String, String> orderby = new HashMap<String, String>();
				orderby.put("auditSection.dateModified", "desc");
				List<Object[]> where =new ArrayList<>();
				where.add(new Object[]{"depth.id", tid});
				Entitites<ObliqueData> list = obliquedataService.getPageListByAttributesLike(attributes,
						dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
						Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, where, null,true);
				dt.setsEcho(dataTableParam.getsEcho() + 1);
				dt.setiTotalDisplayRecords(list.getTotalCount());
				dt.setiTotalRecords(list.getTotalCount());
				dt.setAaData(list.getEntites());
			} catch (Exception e) {
				LOGGER.error("Error while paging obliquedatas", e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/obliquedata/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveObliqueData(@ModelAttribute("obliquedata") ObliqueData obliquedata, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			if(obliquedata.getCurDate()==null){
				obliquedata.setCurDate(new Date());
			}
			obliquedataService.saveOrUpdate(obliquedata);
		} catch (ServiceException e) {
			LOGGER.error("Error while save ObliqueData", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/obliquedata/getByDate.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getByDate(HttpServletRequest request, HttpServletResponse response) throws org.codehaus.jackson.JsonGenerationException, org.codehaus.jackson.map.JsonMappingException, IOException {
		String date = request.getParameter("date");
		String sid = request.getParameter("sid");
		String tid = request.getParameter("tid");
		if(StringUtils.isNotBlank(tid)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//ObliqueData obliqueData = obliquedataService.getById(Long.parseLong(tid));
			Date fdate = null;
			try {
				fdate = sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ObliqueData buildingdata = null;
			try {
				buildingdata = obliquedataService.getLast(fdate, Long.parseLong(sid));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			Map<String, Object> entry = new HashMap<String, Object>();
			if(buildingdata==null) {
				entry.put("last", 0);
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(entry);
				return json;
			}
			entry.put("last", buildingdata.getCurTotal()==null ? 0 :buildingdata.getCurTotal());
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(entry);
			return json;
		}
		
		return "";
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/obliquedata/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteObliqueData(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					ObliqueData obliquedata = obliquedataService.getByIdWithPoint(id);
					if (obliquedata != null) {
						obliquedataService.deleteAndUpdate(obliquedata);
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
		activeMenus.put("obliquedata", "obliquedata");
		activeMenus.put("obliquedata-list", "obliquedata-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("obliquedata");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
