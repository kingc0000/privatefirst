package com.kekeinfo.web.event;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

@Service
public class SpringContextUtils implements ApplicationContextAware, ServletContextAware{

	private static ApplicationContext applicationContext;
	private static ServletContext servletContext;

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;
	}
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}

	public final static Object getBean(String beanName){
		return applicationContext.getBean(beanName);
	}

	public final static Object getBean(String beanName, Class<?> requiredType) {
		return applicationContext.getBean(beanName, requiredType);
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		servletContext = arg0;
	}
	
	public static ServletContext getServletContext() {
		return servletContext;
	}
}
