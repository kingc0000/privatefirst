package com.kekeinfo.web.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

public class I18nLocaleResolver extends AcceptHeaderLocaleResolver {

    private Locale defaultLocale;

    public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public Locale resolveLocale(HttpServletRequest request) {
        return defaultLocale;
    } 

    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    	if(defaultLocale.getLanguage().equalsIgnoreCase("zh")){
    		defaultLocale =  new Locale("en");
    	}else{
    		defaultLocale =  new Locale("zh");
    	}
    	
    }
  
}
