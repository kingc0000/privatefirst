package com.kekeinfo.core.business.user.model;

import java.io.Serializable;

public class AppUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6330524442309403693L;
	
	private Long id = 0L;
	private String name;
	private String phone;
	private String device_token;
	private String uAgent;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDevice_token() {
		return device_token;
	}
	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}
	public String getuAgent() {
		return uAgent;
	}
	public void setuAgent(String uAgent) {
		this.uAgent = uAgent;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
