package com.kekeinfo.web.entity;

import java.io.Serializable;

public class Entity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id = 0L;
	
	public Entity() {
		super();
	}
	public Entity(Long id) {
		super();
		this.id = id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}

}
