package com.kekeinfo.web.admin.controller.computer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.utils.PNodeUtils;
import com.kekeinfo.web.utils.WebApplicationCacheUtils;

@Controller
public class ComputerController {
	
	@Autowired
	private WebApplicationCacheUtils webCacheUtils;
	@Autowired private PNodeUtils pnodeUtils;
	/**
	 * 地下水控制设计简易计算分析的公式
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/computer/confined.html", method=RequestMethod.GET)
	public String displayComputer(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		setWaterMenu(model,request);
		return "csite-confined";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/computer/aquifer.html", method=RequestMethod.GET)
	public String displayComputer1(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		setWaterMenu(model,request);
		return "csite-aquifer";
	}
	
	@PreAuthorize("hasRole('VIEW-PROJECT')")
	@RequestMapping(value="/water/computer/recharge.html", method=RequestMethod.GET)
	public String display2Computer(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.isUserInRole("ADMIN") && !request.isUserInRole("SUPERADMIN")) {
			User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
			Department waterDept = (Department) webCacheUtils.getFromCache(Constants.DEPARTMENT_WATER); //地下水部门
			boolean find = pnodeUtils.hasProjectRight(user.getpNodes(), waterDept);
			if(!find){
				return "redirect:/water/home.html";
			}
		}
		setWaterMenu(model,request);
		return "csite-recharge";
	}
	
	
	
	private void setWaterMenu(Model model, HttpServletRequest request) throws Exception {
		
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("analysis", "analysis");
		model.addAttribute("activeMenus",activeMenus);
	}

}
