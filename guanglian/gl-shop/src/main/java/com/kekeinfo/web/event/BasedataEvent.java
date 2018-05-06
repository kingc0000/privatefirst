package com.kekeinfo.web.event;

import org.springframework.context.ApplicationEvent;

public class BasedataEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	public BasedataEvent(Object source) {
		super(source);
	}
}
