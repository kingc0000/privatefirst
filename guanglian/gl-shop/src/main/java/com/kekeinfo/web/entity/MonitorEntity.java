package com.kekeinfo.web.entity;

import java.io.Serializable;

public class MonitorEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5056885152025887685L;
	/**
	 * 
	 */
	
	private Long id;
	private Long uid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
}
