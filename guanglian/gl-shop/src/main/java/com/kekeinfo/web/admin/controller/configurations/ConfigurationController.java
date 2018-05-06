package com.kekeinfo.web.admin.controller.configurations;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.kekeinfo.core.business.content.model.FileContentType;
import com.kekeinfo.core.business.content.model.InputContentFile;
import com.kekeinfo.core.business.content.service.ContentService;
import com.kekeinfo.core.business.generic.exception.ServiceException;
import com.kekeinfo.core.business.system.model.SystemConfiguration;
import com.kekeinfo.core.business.system.service.EmailService;
import com.kekeinfo.core.business.system.service.SystemConfigurationService;
import com.kekeinfo.core.business.zone.service.ZoneService;
import com.kekeinfo.core.modules.email.EmailConfig;
import com.kekeinfo.core.modules.utils.DreportConfig;
import com.kekeinfo.core.utils.ajax.AjaxResponse;
import com.kekeinfo.web.admin.controller.ControllerConstants;
import com.kekeinfo.web.admin.entity.web.Menu;
import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.utils.ImageUtils;
import com.kekeinfo.web.utils.RadomSixNumber;


@Controller
public class ConfigurationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationController.class);
	
	@Autowired
	private EmailService emailService;

	@Autowired
	Environment env;
	
	@Autowired 
	ContentService contentService;
	
	@Autowired private SystemConfigurationService sysconfigService;
	
	@Autowired
	ZoneService zoneService;
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/configuration/logo.html", method=RequestMethod.GET)
	public String displayAccountsConfguration(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setConfigurationMenu(model, request);
		SystemConfiguration configuration = sysconfigService.getByKey(Constants.LOGO_CONFIG);
		if(configuration!=null){
			model.addAttribute("logo", configuration.getValue());
		}
		
		return com.kekeinfo.web.admin.controller.ControllerConstants.Tiles.Configuration.logo;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/configuration/savelogo.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public  @ResponseBody String saveLogo(@RequestParam(value="contentImages", required=false) @Valid final CommonsMultipartFile contentImages, HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxResponse resp = new AjaxResponse();
		try{
			if(contentImages != null) {

				String imageName = contentImages.getOriginalFilename();
				//图片没有改动不用保存
				if(imageName !=""){
					SystemConfiguration configuration = sysconfigService.getByKey(Constants.LOGO_CONFIG);
					if(configuration ==null){
		            	configuration = new SystemConfiguration();
		            	configuration.setKey(Constants.LOGO_CONFIG);
		            }
					if( configuration.getValue()!=null){
						contentService.removeFile(FileContentType.LOGO,configuration.getValue());
					};
					InputStream inputStream = contentImages.getInputStream();
					BufferedImage imBuff = ImageIO.read(inputStream);
					int type = imBuff.getType() == 0? BufferedImage.TYPE_INT_ARGB : imBuff.getType();
					imBuff =ImageUtils.resizeImage(imBuff, 45, 45, type);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ImageIO.write(imBuff, "jpeg", os);
					inputStream = new ByteArrayInputStream(os.toByteArray());
		            String in = RadomSixNumber.getImageName(imageName, Constants.IMAGE_PNG);
		            InputContentFile cmsContentImage = new InputContentFile();
		            cmsContentImage.setFileName(in);
		            cmsContentImage.setMimeType( contentImages.getContentType());
		            cmsContentImage.setFile( inputStream );
		            cmsContentImage.setFileContentType(FileContentType.LOGO);
		            contentService.addLogo(cmsContentImage);
		            
		            
		            configuration.setValue(in);
		            sysconfigService.saveOrUpdate(configuration);
		            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
				}
				
			}
		}catch(Exception e){
			LOGGER.error("Error while save logo ", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		
		return resp.toJSONString();
				
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/configuration/removeLogo.shtml", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String removeImage( HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		SystemConfiguration configuration;
		try {
			configuration = sysconfigService.getByKey(Constants.LOGO_CONFIG);
			if(configuration!=null && configuration.getValue()!=null){
				contentService.removeFile(FileContentType.LOGO,configuration.getValue());
				configuration.setValue(null);
				sysconfigService.saveOrUpdate(configuration);
			}
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/configuration/email.html", method=RequestMethod.GET)
	public String displayEmailSettings(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setEmailConfigurationMenu(model, request);
		//String domainUrl = request.getServerName()+":"+request.getServerPort();
		//SpringContextUtils.getServletContext().setAttribute("domainUrl", domainUrl);//MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		EmailConfig emailConfig = emailService.getEmailConfiguration();
		if(emailConfig == null){
			emailConfig = new EmailConfig();
			//TODO: Need to check below properties. When there are no record available in MerchantConfguration table with EMAIL_CONFIG key, 
			// instead of showing blank fields in setup screen, show default configured values from email.properties
			emailConfig.setProtocol(env.getProperty("mailSender.protocol"));
			emailConfig.setHost(env.getProperty("mailSender.host"));
			emailConfig.setPort(env.getProperty("mailSender.port}"));
			emailConfig.setUsername(env.getProperty("mailSender.username"));
			emailConfig.setPassword(env.getProperty("mailSender.password"));
			emailConfig.setSmtpAuth(Boolean.parseBoolean(env.getProperty("mailSender.mail.smtp.auth")));
			emailConfig.setStarttls(Boolean.parseBoolean(env.getProperty("mail.smtp.starttls.enable")));
		}
		
		model.addAttribute("configuration", emailConfig);
		return ControllerConstants.Tiles.Configuration.email;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/configuration/saveEmailConfiguration.html", method=RequestMethod.POST)
	public String saveEmailSettings(@ModelAttribute("configuration") EmailConfig config, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		setEmailConfigurationMenu(model, request);
		EmailConfig emailConfig = emailService.getEmailConfiguration();
		if(emailConfig == null){
			emailConfig = new EmailConfig();
		}
		
		// populte EmailConfig model from UI values
		emailConfig.setProtocol(config.getProtocol());
		emailConfig.setHost(config.getHost());
		emailConfig.setPort(config.getPort());
		emailConfig.setUsername(config.getUsername());
		emailConfig.setPassword(config.getPassword());
		emailConfig.setSmtpAuth(config.isSmtpAuth());
		emailConfig.setStarttls(config.isStarttls());
		
		emailService.saveEmailConfiguration(emailConfig);
		
		model.addAttribute("configuration", emailConfig);
		model.addAttribute("success","success");
		return ControllerConstants.Tiles.Configuration.email;
	}
	
	/**
	 * 显示设计报告的审核和审定人配置
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/water/configuration/dreport.html", method=RequestMethod.GET)
	public String displayDreportSettings(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setDreportConfigurationMenu(model, request);
		DreportConfig dreportConfig = sysconfigService.getDreportConfiguration();
		
		if (dreportConfig == null) {
			dreportConfig= new DreportConfig();
		}
		
		model.addAttribute("configuration", dreportConfig);
		return ControllerConstants.Tiles.Configuration.dreport;
	}
	
	private void setConfigurationMenu(Model model, HttpServletRequest request) throws Exception {
		
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("logo", "logo");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("configuration");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
	}
	
	private void setEmailConfigurationMenu(Model model, HttpServletRequest request) throws Exception {
		
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("email", "email");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("configuration");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
	}
	
	private void setDreportConfigurationMenu(Model model, HttpServletRequest request) throws Exception {
		
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("dreport", "dreport");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("configuration");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
	}
}
