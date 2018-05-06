package com.kekeinfo.web.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.kekeinfo.web.constants.Constants;

public class EmailUtils {
	
	private final static String EMAIL_STORE_NAME = "EMAIL_STORE_NAME";
	private final static String EMAIL_FOOTER_COPYRIGHT = "EMAIL_FOOTER_COPYRIGHT";
	private final static String EMAIL_DISCLAIMER = "EMAIL_DISCLAIMER";
	private final static String EMAIL_SPAM_DISCLAIMER = "EMAIL_SPAM_DISCLAIMER";
	private final static String EMAIL_ADMIN_LABEL = "EMAIL_ADMIN_LABEL";
	private final static String LOGOPATH = "LOGOPATH";
	//private final static String EMAIL_PHONE_LABEL = "EMAIL_PHONE_LABEL";
	//private final static String EMAIL_PHONE = "EMAIL_PHONE";
	
	private final static String LOGO = "";
	private final static String Email="";
	/**
	 * Builds generic html email information
	 * @param store
	 * @param messages
	 * @param locale
	 * @return
	 */
	/*public static Map<String, String> createEmailObjectsMap(BaseStore baseStore){
		
		//商铺名称在代码里面写死
		Map<String, String> templateTokens = new HashMap<String, String>();
		String storeName=baseStore.getStorename();
		String email=baseStore.getStoreEmailAddress();
		
		templateTokens.put(EMAIL_ADMIN_LABEL, "来自于"+storeName+"的邮件");
		templateTokens.put(EMAIL_STORE_NAME, storeName);
		templateTokens.put(EMAIL_FOOTER_COPYRIGHT, "版权 @ "+storeName+DateUtil.getPresentYear()+"所有");
		templateTokens.put(EMAIL_SPAM_DISCLAIMER, "如果你不想收到该邮件，请发邮件给我们，谢谢！");
		templateTokens.put(EMAIL_DISCLAIMER, "这个邮件地址是由您或我们的客户之一给我们的，如果您觉得收到此邮件是个错误，请发送电子邮件至"+email+"以取消。");
		
			templateTokens.put(LOGOPATH, storeName);
	
		return templateTokens;
	}*/
	

	/**
	 * Builds generic html email information
	 * @param store
	 * @param messages
	 * @param locale
	 * @return
	 **/
	public static Map<String, String> createEmailObjectsMap(HttpServletRequest request,  LabelUtils messages, Locale locale){
		Map<String, String> templateTokens = new HashMap<String, String>();
		String[] adminNameArg = {"上海广联环境岩土工程股份有限公司"};
		String[] adminEmailArg = {Email};
		String[] copyArg = {"上海广联环境岩土工程股份有限公司", DateUtil.getPresentYear()};
		
		templateTokens.put(EMAIL_ADMIN_LABEL, messages.getMessage("email.message.from", adminNameArg, locale));
		templateTokens.put(EMAIL_STORE_NAME, "上海广联环境岩土工程股份有限公司");
		templateTokens.put(EMAIL_FOOTER_COPYRIGHT, messages.getMessage("email.copyright", copyArg, locale));
		templateTokens.put(EMAIL_DISCLAIMER, messages.getMessage("email.disclaimer", adminEmailArg, locale));
		templateTokens.put(EMAIL_SPAM_DISCLAIMER, messages.getMessage("email.spam.disclaimer", locale));
		//templateTokens.put(EMAIL_PHONE_LABEL, messages.getMessage("label.hot.phone", locale));
		//templateTokens.put(EMAIL_PHONE, StringUtils.trimToEmpty("021-6149-1081"));
		
		
		if(StringUtils.isNotBlank(LOGO)) {
			StringBuilder logoPath = new StringBuilder();
			StringBuilder resourcePath = new StringBuilder();
			String scheme = Constants.HTTP_SCHEME;
			String serverName = request.getServerName();
			int port = request.getServerPort();
			resourcePath.append(scheme).append("://").append(serverName).append(":").append(port)
			.append(request.getContextPath());
			
			logoPath.append("<img src='").append(resourcePath).append(ImageFilePathUtils.buildStoreLogoFilePath(LOGO)).append("' style='max-width:50px;'>");
			templateTokens.put(LOGOPATH, logoPath.toString());
		} else {
			templateTokens.put(LOGOPATH, "上海广联环境岩土工程股份有限公司");
		}
		return templateTokens;
	}


	

}
