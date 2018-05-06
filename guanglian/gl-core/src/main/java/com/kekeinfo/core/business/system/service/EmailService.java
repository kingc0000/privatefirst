package com.kekeinfo.core.business.system.service;

import com.kekeinfo.core.modules.email.Email;
import com.kekeinfo.core.modules.email.EmailConfig;
import com.kekeinfo.core.business.generic.exception.ServiceException;


public interface EmailService {

	public void sendHtmlEmail(Email email) throws ServiceException, Exception;
	
	public EmailConfig getEmailConfiguration() throws ServiceException;
	
	public void saveEmailConfiguration(EmailConfig emailConfig) throws ServiceException;
	
}
