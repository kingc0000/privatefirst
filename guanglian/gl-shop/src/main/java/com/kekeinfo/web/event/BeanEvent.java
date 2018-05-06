package com.kekeinfo.web.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;

import com.kekeinfo.core.business.user.service.UserService;

public class BeanEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	private String text;
	public static final String BASEDATA_SRC = "basedata";
	public static final String CATEGORY_SRC = "category";
	public static final String MANUFACTURER_SRC = "manufacture";
	public static final String PRODUCT_RELATIONSHIP_SRC = "product_relationship";
	
	public static final String CONTENT_FEATURE = "content_feature";
	public static final String CONTENT_WORD_SALES = "content_word_sales";
	public static final String CONTENT_SERVICE = "content_service";
	public static final String CONTENT_COMPANY = "content_company";
	public static final String CONTENT_DOCMENT = "cont_docment"; //文献管理
	
	
	public static final String PRODUCT_FEATURE = "product_feature";
	
	@Autowired UserService userService;	
	public BeanEvent(Object source) {
		super(source);
	}
	//text，定义事件具体属于哪个事件，以便监听者触发相应动作
	public BeanEvent(Object source, String text) {
		super(source);
		this.text = text;
	
			}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
