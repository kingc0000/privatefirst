package com.kekeinfo.web.admin.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kekeinfo.core.business.user.model.User;

public interface WebUserServices extends UserDetailsService{
	
	void createDefaultAdmin() throws Exception;
	
	void removeSession(User user);
}
