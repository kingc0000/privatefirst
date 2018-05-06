package com.kekeinfo.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class BioSiteExceptionHandler implements HandlerExceptionResolver{
	private static final Logger LOGGER = LoggerFactory.getLogger(BioSiteExceptionHandler.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3) {
		// TODO Auto-generated method stub
		LOGGER.error("Catch Exception,mess: ..... " + arg3.getMessage());
		arg3.printStackTrace();
		//如果是访问拒绝则跳转到登录页面
		//arg3.getMessage().equalsIgnoreCase("Access is denied")
		if("Access is denied".equalsIgnoreCase(arg3.getMessage())){
			//BaseStore store = getSessionAttribute(Constants.MERCHANT_STORE, arg0);
			//StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerLogon).append(".").append(store.getStoreTemplate());
			//return template.toString();
			return null;
		}
		String path=arg0.getServletPath();
		
		ModelAndView model = new ModelAndView("error/error");
		//服务器错误
		if(path.indexOf("/water")!=-1){
			model.addObject("homeurl","/water/home.html");
		}else{
			model.addObject("homeurl","/shop/");
		}
		return model;
	}

}
