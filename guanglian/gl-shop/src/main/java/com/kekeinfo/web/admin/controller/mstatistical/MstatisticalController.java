package com.kekeinfo.web.admin.controller.mstatistical;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;
import com.kekeinfo.core.business.daily.model.MonitorDaily;
import com.kekeinfo.core.business.daily.service.MonitorDailyService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.monitor.model.MPointEnumType;
import com.kekeinfo.core.business.monitor.statistical.model.Mstatistical;
import com.kekeinfo.core.business.monitor.statistical.service.MstatisticalService;
import com.kekeinfo.core.business.user.model.Group;
import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.BaseEntity;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.MonitorEntity;
import com.kekeinfo.web.event.SpringContextUtils;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.TransExcelView;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;;

@Controller
public class MstatisticalController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MstatisticalController.class);
	@Autowired
	MstatisticalService mstatisticalService;
	@Autowired  TransExcelView transExcelView;
	@Autowired MonitorDailyService monitorDailyService;
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private PNodeUtils pnodeUtils;
	@Autowired private GroupService groupService;
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/mstatistical/list.html", method = RequestMethod.GET)
	public String listmstatisticals(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		Mstatistical mstatistical = new Mstatistical();
		model.addAttribute("mstatistical", mstatistical);
		String mid = request.getParameter("mid");
		//判断用户是否有该项目的编辑权限
        boolean hasRight = false;
        MonitorEntity me=null;
		if(StringUtils.isNotBlank(mid)){
			List<MonitorEntity> listen = (List<MonitorEntity>) webCacheUtils.getFromCache(Constants.PROEJCT_MONITOR);
			for(MonitorEntity m:listen){
				if(m.getId().equals(Long.parseLong(mid))){
					me = m; 
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
		request.setAttribute("activeCode", "mstatistical"); //指定项目
		model.addAttribute("hasRight", hasRight);
		model.addAttribute("mid", mid);
		return "admin-mstatistical";
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/mstatistical/{msid}.html", method = RequestMethod.GET)
	public String detial(@PathVariable final long msid,Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Mstatistical mstatistical = mstatisticalService.getById(msid);
		List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_MONITOR_LINE);
		model.addAttribute("bts", blist);
		model.addAttribute("msr", mstatistical);
		model.addAttribute("mtypes", MPointEnumType.values());
		setMenu(model, request);
		return "admin-mdetail";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value = "/water/mstatistical/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getMstatistical(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<Mstatistical> dt = new DataTable<Mstatistical>();
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("auditSection.dateModified", "desc");
			Entitites<Mstatistical> list = mstatisticalService.getPageListByAttributesLike(attributes,
					dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(),
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, null, null);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
		} catch (Exception e) {
			LOGGER.error("Error while paging mstatisticals", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		String json = mapper.writeValueAsString(dt);
		return json;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/mstatistical/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveMstatistical(@ModelAttribute("mstatistical") Mstatistical mstatistical, Model model,
			HttpServletRequest request, HttpServletResponse response) { 
		AjaxResponse resp = new AjaxResponse();
		try {
			//mstatistical.setReportDate();
			MonitorDaily monitorDaily =monitorDailyService.getBydate(mstatistical.getmDaily().getDatec(), mstatistical.getMonitor().getId());
			if(monitorDaily ==null){
				resp.setStatus(-4);
				resp.setStatusMessage("该日期的项目日志还没有创建，请先创建项目日志！！！");
				return resp.toJSONString();
			}
			mstatistical.setmDaily(monitorDaily);
			//获取管线类型，避免查库
			List<BasedataType> blist = (List<BasedataType>) SpringContextUtils.getServletContext().getAttribute(Constants.BD_MONITOR_LINE);
			mstatisticalService.saveUpdate(mstatistical,blist);
		} catch (ServiceException e) {
			LOGGER.error("Error while save Mstatistical", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}
	
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/mstatistical/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteMstatistical(@RequestParam String[] listId,
			HttpServletRequest request, Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					Mstatistical mstatistical = mstatisticalService.getById(id);
					if (mstatistical != null) {
						mstatisticalService.delete(mstatistical);
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
		activeMenus.put("mstatistical", "mstatistical");
		activeMenus.put("mstatistical-list", "mstatistical-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("mstatistical");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
