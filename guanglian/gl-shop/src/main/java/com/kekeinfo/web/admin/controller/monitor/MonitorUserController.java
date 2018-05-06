package com.kekeinfo.web.admin.controller.monitor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.kekeinfo.core.business.monitor.model.MonitorUser;
import com.kekeinfo.core.business.monitor.service.MonitorUserService;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.entity.DataTable;
import com.kekeinfo.web.entity.PinYinUserName;
import com.kekeinfo.web.filter.UserFilter;
import com.kekeinfo.web.utils.DataTableParameter;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.PinyinUtils;

@Controller
public class MonitorUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorController.class);
	@Autowired MonitorUserService monitorUserService;
	@Autowired UserService userService;
	@Autowired private PNodeUtils pnodeUtils;
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitoruser/list.html", method = RequestMethod.GET)
	public String listmonitoreqips(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		setMenu(model, request);
		MonitorUser monitorUser = new MonitorUser();
		model.addAttribute("monitorUser", monitorUser);
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
		return "admin-monitoruser";
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitoruser/server_processing.shtml", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = " application/json; charset=utf-8")
	public @ResponseBody String getMonitorEqip(@RequestParam String aoData, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		AjaxResponse resp = new AjaxResponse();
		DataTableParameter dataTableParam = new DataTableParameter(aoData);
		DataTable<MonitorUser> dt = new DataTable<MonitorUser>();
		try { // 指定根据什么条件进行模糊查询
			List<String> attributes = new ArrayList<String>();
			attributes.add("user.firstName"); 
			attributes.add("user.telephone"); 
			attributes.add("memo");
			HashMap<String, String> orderby = new HashMap<String, String>();
			orderby.put("memo", "desc");
			Map<String, String> fetches = new HashMap<String, String>();
			fetches.put("user", "LEFT");
			Entitites<MonitorUser> list = monitorUserService.getPageListByAttributesLike(attributes, dataTableParam.getsSearch(), Long.valueOf(dataTableParam.getiDisplayLength()).intValue(), 
					Long.valueOf(dataTableParam.getiDisplayStart()).intValue(), orderby, null, fetches, true);
			dt.setsEcho(dataTableParam.getsEcho() + 1);
			dt.setiTotalDisplayRecords(list.getTotalCount());
			dt.setiTotalRecords(list.getTotalCount());
			dt.setAaData(list.getEntites());
			ObjectMapper mapper = new ObjectMapper();
			 mapper.getSerializationConfig().addMixInAnnotations(User.class, UserFilter.class);
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
			String json = mapper.writeValueAsString(dt);
			return json;
		} catch (Exception e) {
			LOGGER.error("Error while paging monitoreqips", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return "";
	}
	
	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitoruser/save.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String saveMonitorEqip(@ModelAttribute("monitoreqip") MonitorUser monitoreqip, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		try {
			User user =userService.getById(monitoreqip.getUser().getId());
			monitoreqip.setUser(user);
			monitorUserService.saveOrUpdate(monitoreqip);
		} catch (ServiceException e) {
			LOGGER.error("Error while save MonitorEqip", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return returnString;
	}

	@PreAuthorize("hasRole('EDIT-PROJECT')")
	@RequestMapping(value = "/water/monitoruser/remove.shtml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AjaxResponse> deleteMonitorEqip(@RequestParam String[] listId, HttpServletRequest request,
			Locale locale) throws Exception {
		List<AjaxResponse> resplist = new ArrayList<AjaxResponse>();
		if (listId != null && listId.length > 0) {
			for (int i = 0; i < listId.length; i++) {
				AjaxResponse resp = new AjaxResponse();
				Long id = Long.parseLong(listId[i]);
				try {
					MonitorUser monitoreqip = monitorUserService.getById(id);
					if (monitoreqip != null) {
						monitorUserService.delete(monitoreqip);
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

	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/monitoruser/users.shtml", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYinUserName> project(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = userService.getPinYinUser();
			List<PinYinUserName> pinyin = PinyinUtils.getPinyinUserList(stores);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("muser", "muser");
		activeMenus.put("muser-list", "muser-list");
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
		Menu currentMenu = (Menu) menus.get("muser");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}

}
