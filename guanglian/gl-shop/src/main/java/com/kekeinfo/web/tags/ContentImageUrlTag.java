package com.kekeinfo.web.tags;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kekeinfo.web.constants.Constants;
import com.kekeinfo.web.utils.ImageFilePathUtils;

public class ContentImageUrlTag extends TagSupport {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6319855234657139862L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentImageUrlTag.class);

	private String imageName;
	private String imageType;


	public int doStartTag() throws JspException {
		try {



			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			
			//BaseStore baseStore = (BaseStore)request.getAttribute(Constants.ADMIN_STORE);
			
			
			HttpSession session = request.getSession();

			StringBuilder imagePath = new StringBuilder();
			
			//TODO domain from merchant, else from global config, else from property (localhost)
			
			//http://domain/static/merchantid/imageType/imageName
			
			//@SuppressWarnings("unchecked")
			//Map<String,String> configurations = (Map<String, String>)session.getAttribute("STORECONFIGURATION");
			//String scheme = (String)configurations.get("scheme");
			
			//if(StringUtils.isBlank(scheme)) {
			//	scheme = "http";
			//}
			


			@SuppressWarnings("unchecked")
			Map<String,String> configurations = (Map<String, String>)session.getAttribute(Constants.STORE_CONFIGURATION);
			String scheme = Constants.HTTP_SCHEME;
			if(configurations!=null) {
				scheme = (String)configurations.get("scheme");
			}else{
				scheme = request.getScheme()+"://"+request.getServerName();
				if(request.getServerPort()!=80){
					scheme+=":"+request.getServerPort();
				}
				
			}
			
			
			
			imagePath.append(scheme).append(request.getContextPath());
			
			//只有basestore才有logo,路径默认为DEFAULT
			if(this.getImageType()=="LOGO"){
				imagePath.append(Constants.STATIC_URI).append("/").append("DEFAULT").append("/").append("LOGO")
				.append("/").append(this.getImageName());
			}else{
				imagePath.append(ImageFilePathUtils.buildStaticDefaultFilePath(this.getImageName(), this.getImageType()));
			}
			
			
			

			pageContext.getOut().print(imagePath.toString());


			
		} catch (Exception ex) {
			LOGGER.error("Error while getting content url", ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageType() {
		return imageType;
	}




	

}
