package com.kekeinfo.web.admin.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.kekeinfo.core.business.appmessage.service.MessageUService;
import com.kekeinfo.core.business.dreport.service.DreportService;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.user.service.UserService;
import com.kekeinfo.web.constants.Constants;

public class UserAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationSuccessHandler.class);
	
	
	@Autowired
	private DreportService dreportService;
	@Autowired
	private MessageUService sessageUService;
	@Autowired UserService userService;
	
	  @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		  // last access timestamp
		  String userName = authentication.getName();
		  
		  try { 
			  //设定用户待办审批数量
			  Integer[] types = {1, 2}; 
			  int count = dreportService.countApproveDreports(userName, types);
			  int unread =sessageUService.read(userName);
			  //重新设置user
			  User user  = userService.getByUserName(userName);
			  request.getSession().setAttribute(Constants.ADMIN_USER, user);
			  request.getSession().setAttribute(Constants.USER_REPORT_DEAL, count);
			  request.getSession().setAttribute(Constants.USER_UN_READ, unread);
			 /**
			  if(StringUtils.isNotBlank(user.getuAgent())){
				  response.sendRedirect(request.getContextPath() + "/water/phome.html");
			  }else{*/
				  response.sendRedirect(request.getContextPath() + "/water/home.html");
			  //}
			 
		  } catch (Exception e) {
			  LOGGER.error("User authenticationSuccess",e);
		  }
	   }

}
