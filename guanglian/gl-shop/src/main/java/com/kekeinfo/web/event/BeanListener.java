package com.kekeinfo.web.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.kekeinfo.core.business.basedata.service.BaseDataService;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.Entitites;

@Service
public class BeanListener implements ApplicationListener<BeanEvent> {

	private static final Logger logger = LoggerFactory.getLogger(BeanListener.class);
	@Autowired
	private BaseDataService baseDataService;	
	//@Autowired
	//private WebApplicationCacheUtils webApplicationCache;
	

	@Override
	public void onApplicationEvent(BeanEvent event) {
		String src = (String) event.getText();
		ServletContext servletContext = SpringContextUtils.getServletContext();
		try {
			if (src.equals(BeanEvent.BASEDATA_SRC)) {//判断是否为基础数据事件触发监听
				List<String> attributes = new ArrayList<String>();
				attributes.add("distinct(TYPE)");
				List<Object[]> bbtype= baseDataService.getBySql(attributes, "BASEDATA_TYPE", " ");
				for (Object objects : bbtype) {
					HashMap<String, String> orders = new HashMap<String, String>();
					orders.put("order", "asc");
					Entitites<BasedataType> entities = baseDataService.getListByAttributes(new String[]{"type"}, new String[]{objects.toString()}, orders);
					servletContext.setAttribute(objects.toString(), entities.getEntites());
				}
				logger.info("update basedata list successfully");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
