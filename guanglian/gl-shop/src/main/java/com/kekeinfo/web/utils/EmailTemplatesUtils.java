package com.kekeinfo.web.utils;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kekeinfo.core.business.system.service.EmailService;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.modules.email.Email;
import com.kekeinfo.core.modules.email.EmailConfig;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.constants.EmailConstants;




@Component
public class EmailTemplatesUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTemplatesUtils.class);

	@Autowired
	private EmailService emailService;

	@Autowired
	private LabelUtils messages;
	
	
/*
	private final static String LINE_BREAK = "<br/>";
	private final static String TABLE = "<table>";
	private final static String CLOSING_TABLE = "</table>";
	private final static String TR = "<tr>";
	private final static String CLOSING_TR = "</tr>";
	private final static String TD = "<td>";
	private final static String CLOSING_TD = "</td>";
	private final static String SPACE = "&nbsp;";
*/
	
	/**
	 * 后台管理用户创建邮件通知
	 * @param baseStore
	 * @param locale
	 * @param request
	 * @param user
	 * @param password
	 * @throws Exception 
	 */
	public void sendUserRegistrationEmail(Locale locale, HttpServletRequest request, User user, String password) throws Exception {
		String userName = StringUtils.isNotBlank(user.getFirstName())?user.getFirstName():user.getAdminName();
		LOGGER.info("Sending welcome email to admin user for " + userName);
		try {
			String[] userNameArg = {userName};
			Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request, messages, locale); //邮件基本变量信息
			templateTokens.put(EmailConstants.EMAIL_NEW_USER_TEXT, messages.getMessage("email.greeting", userNameArg, locale));
			//templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, contact.getName());
			templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL, messages.getMessage("label.generic.username",locale));
			templateTokens.put(EmailConstants.EMAIL_ADMIN_NAME, user.getAdminName());
			templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_CREATED, messages.getMessage("email.newuser.text",locale));
			templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, messages.getMessage("label.generic.password",locale));
			templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD, password);
			templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));
			templateTokens.put(EmailConstants.EMAIL_ADMIN_URL, buildStoreUri(request)+"/water");

			Email email = new Email();
			email.setFrom("上海广联环境岩土工程股份有限公司");
			email.setSubject(messages.getMessage("email.createuser.title",locale));
			email.setTo(user.getAdminEmail());
			email.setTemplateName("email_template_new_user.ftl");
			email.setTemplateTokens(templateTokens);
			EmailConfig emailConfig = emailService.getEmailConfiguration();
			email.setFromEmail(emailConfig.getUsername());
			
			emailService.sendHtmlEmail(email);
		} catch (Exception e) {
			LOGGER.error("Error occured while sending welcome email ", e);
			throw e;
		}
	}
	
	/**
	 * 后台用户重置密码激活链接邮件
	 * @param basestore
	 * @param contact
	 * @param request
	 * @param locale
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public int sendRetPasswordUserEmail(User user, HttpServletRequest request, Locale locale)
			throws Exception {
		Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request, messages, locale);
		templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", locale));
		templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, user.getAdminName());
		EmailConfig emailConfig = emailService.getEmailConfiguration();
		String activeUrl = buildStoreUri(request);

		activeUrl += "/water/users/verify.html?account=" + user.getAdminName() + "&code=" + user.getTemp();
		templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, "<a href='" + activeUrl + "'>"
				+ messages.getMessage("contact.reset.password.url", locale) + "</a>" + ":" + activeUrl);

		Email email = new Email();
		email.setFrom("上海广联环境岩土工程股份有限公司");
		email.setSubject(messages.getMessage("contact.reset.password", locale));
		email.setTo(user.getAdminEmail());
		email.setTemplateName("email_template_notification.ftl");
		email.setTemplateTokens(templateTokens);
		email.setFromEmail(emailConfig.getUsername());
		emailService.sendHtmlEmail(email);
		return 0;
	}
	
	/**
	 * 密码重置，发送新的密码到客户邮箱
	 * @param store
	 * @param contact
	 * @param request
	 * @param locale
	 * @param password
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public int sendRetPassword(User user,HttpServletRequest request, Locale locale,
			String password) throws Exception {
		EmailConfig emailConfig = emailService.getEmailConfiguration();
		String[] storeEmail = new String[] { emailConfig.getUsername() };

		Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request, messages, locale);
		templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", locale));
		templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, user.getFirstName());
		templateTokens.put(EmailConstants.EMAIL_RESET_PASSWORD_TXT,
				messages.getMessage("email.customer.resetpassword.text", locale));
		templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER,
				messages.getMessage("email.contactowner", storeEmail, locale));
		templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password", locale));
		templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, password);

		Email email = new Email();
		email.setFrom("上海广联环境岩土工程股份有限公司");
		email.setFromEmail(emailConfig.getUsername());
		email.setSubject(messages.getMessage("label.generic.changepassword", locale));
		email.setTo(user.getAdminEmail());
		email.setTemplateName("email_template_password_reset_customer.ftl");
		email.setTemplateTokens(templateTokens);
		emailService.sendHtmlEmail(email);
		//
		return 0;

	}
	
	/**
	 * 密码重置邮件，发送新的密码至后台用户的邮箱
	 * @param store
	 * @param user
	 * @param request
	 * @param locale
	 * @param password
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public int sendRetPasswordUser(User user, HttpServletRequest request, Locale locale,
			String password) throws Exception {
		// active of a user, send an email
		EmailConfig emailConfig = emailService.getEmailConfiguration();
		String[] storeEmail = new String[] { emailConfig.getUsername() };

		Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(request,  messages, locale);
		templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", locale));
		templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, user.getAdminName());
		templateTokens.put(EmailConstants.EMAIL_RESET_PASSWORD_TXT,
				messages.getMessage("email.customer.resetpassword.text", locale));
		templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER,
				messages.getMessage("email.contactowner", storeEmail, locale));
		templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password", locale));
		templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, password);

		Email email = new Email();
		email.setFrom("上海广联环境岩土工程股份有限公司");
		email.setSubject(messages.getMessage("label.generic.changepassword", locale));
		email.setTo(user.getAdminEmail());
		email.setTemplateName("email_template_password_reset_customer.ftl");
		email.setTemplateTokens(templateTokens);
		email.setFromEmail(emailConfig.getUsername());
		emailService.sendHtmlEmail(email);
		//
		return 0;

	}
	
	private String buildStoreUri(HttpServletRequest request) {
		StringBuilder resourcePath = new StringBuilder();
		String scheme = Constants.HTTP_SCHEME;
		String serverName = request.getServerName();
		int port = request.getServerPort();
		resourcePath.append(scheme).append("://").append(serverName).append(":").append(port)
		.append(request.getContextPath());
		return resourcePath.toString();
		
	}
	
}
