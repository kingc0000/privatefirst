package com.kekeinfo.web.entity;

import java.io.Serializable;

public class CommonEntity extends Entity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private String name;
	private String code;
	private String description;
	private Long relatedId; //相关联对象id
	
	public CommonEntity() {
		super();
	}
	public CommonEntity(String name, String code, String description) {
		super();
		this.name = name;
		this.code = code;
		this.description = description;
	}
	
	public CommonEntity(Long id, String name, String code, String description, Long relatedId) {
		super(id);
		this.name = name;
		this.code = code;
		this.description = description;
		this.relatedId = relatedId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}
	

}
