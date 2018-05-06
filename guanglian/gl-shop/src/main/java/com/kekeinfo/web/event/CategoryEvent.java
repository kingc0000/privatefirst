package com.kekeinfo.web.event;

import org.springframework.context.ApplicationEvent;

public class CategoryEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	public CategoryEvent(Object source) {
		super(source);
	}
}
