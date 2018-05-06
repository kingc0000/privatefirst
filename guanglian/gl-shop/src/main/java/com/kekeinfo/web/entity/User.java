package com.kekeinfo.web.entity;

import java.io.Serializable;

public class User extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4903612785320907153L;
	
	private String name;
	private String phone;
	private String device_token;
	private String uAgent;
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
	

}
