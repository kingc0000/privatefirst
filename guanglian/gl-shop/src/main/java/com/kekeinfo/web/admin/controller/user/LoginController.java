package com.kekeinfo.web.admin.controller.user;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.web.constants.Constants;

@Controller
public class LoginController {
	

	@RequestMapping(value="/water/logon.html", method=RequestMethod.GET)
	public String displayLogon(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return "water/logon";
		
		
	}
	
	@RequestMapping(value="/water/login.html", method=RequestMethod.GET)
	public String displayLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return "water/login";
		
		
	}

	@RequestMapping(value="/water/denied.html", method=RequestMethod.GET)
	public String displayDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
		//logoff the user
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){  
	    	try{
	    		 new SecurityContextLogoutHandler().logout(request, response, auth);
		         //new PersistentTokenBasedRememberMeServices().logout(request, response, auth);
	    	}catch (Exception e){
	    		
	    	}
	        
	    }
	   // User user = (User)request.getSession().getAttribute(Constants.ADMIN_USER);
	    if(StringUtils.isNotBlank(user.getuAgent())){
	    	return "water/login";
	    }
		return "water/logon";
		
		
	}
	
	@RequestMapping(value="/water/unauthorized.html", method=RequestMethod.GET)
	public String unauthorized(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "water/unauthorized";
	}
	@RequestMapping(value="/water/infomation.html", method=RequestMethod.GET)
	public String infomation(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "water/infomation";
	}
	@RequestMapping(value="/water/info.html", method=RequestMethod.GET)
	public String info(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "water/info";
	}

	@RequestMapping(value={"/water/nonsupport.html"}, method=RequestMethod.GET)
	public String nospoort(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return "error/nonspport";
	}
}
