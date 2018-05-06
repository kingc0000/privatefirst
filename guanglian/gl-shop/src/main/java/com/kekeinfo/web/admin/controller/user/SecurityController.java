package com.kekeinfo.web.admin.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kekeinfo.core.business.user.service.GroupService;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.core.business.user.model.Group;

@Controller
public class SecurityController {
	
	@Autowired
	GroupService groupService;

	@PreAuthorize("hasRole('CONFIGURATION')")
	@RequestMapping(value="/water/user/permissions.html", method=RequestMethod.GET)
	public String displayPermissions(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		return "admin-user-permissions";
		
		
	}
	
	@PreAuthorize("hasRole('CONFIGURATION')")
	@RequestMapping(value="/water/user/groups.html", method=RequestMethod.GET)
	public String displayGroups(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		List<Group> groups = groupService.list();
		
		model.addAttribute("groups", groups);
		
		return "admin-user-groups";
		
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("profile", "profile");
		activeMenus.put("security", "security");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("profile");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
