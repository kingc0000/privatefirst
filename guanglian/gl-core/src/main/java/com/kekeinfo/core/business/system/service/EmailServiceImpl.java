package com.kekeinfo.core.business.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.system.model.SystemConfiguration;
import com.kekeinfo.core.constants.Constants;
import com.kekeinfo.core.modules.email.Email;
import com.kekeinfo.core.modules.email.EmailConfig;
import com.kekeinfo.core.modules.email.HtmlEmailSender;
@Service("emailService")
public class EmailServiceImpl implements EmailService {

	
	
	@Autowired
	private HtmlEmailSender sender;
	
	@Autowired private SystemConfigurationService sysconfigService;
	
	@Override
	public void sendHtmlEmail(Email email) throws ServiceException, Exception {

		EmailConfig emailConfig = getEmailConfiguration();
		
		sender.setEmailConfig(emailConfig);
		sender.send(email);
	}

	@Override
	public EmailConfig getEmailConfiguration() throws ServiceException {
		
		SystemConfiguration configuration = sysconfigService.getByKey(Constants.EMAIL_CONFIG);
		EmailConfig emailConfig = null;
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				emailConfig = mapper.readValue(value, EmailConfig.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return emailConfig;
	}
	
	
	@Override
	public void saveEmailConfiguration(EmailConfig emailConfig) throws ServiceException {
		
		SystemConfiguration configuration = sysconfigService.getByKey(Constants.EMAIL_CONFIG);
		if(configuration==null) {
			configuration = new SystemConfiguration();
			configuration.setKey(Constants.EMAIL_CONFIG);
		}
		
		String value = emailConfig.toJSONString();
		configuration.setValue(value);
		sysconfigService.saveOrUpdate(configuration);
		
	}

}
