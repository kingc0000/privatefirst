package com.kekeinfo.web.admin.entity.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Menu implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String url;
	private String icon;
	private String role;
	private String name;
	private int order;
	private List<Menu> menus = new ArrayList<Menu>();
	public String getCode() {
		return code;
	}
	@JsonProperty("code")  
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	@JsonProperty("url")  
	public void setUrl(String url) {
		this.url = url;
	}

	 

	public int getOrder() {
		return order;
	}
	@JsonProperty("order")  
	public void setOrder(int order) {
		this.order = order;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	@JsonProperty("menus")  
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIcon() {
		return icon;
	}
	public String getRole() {
		return role;
	}
	@JsonProperty("role") 
	public void setRole(String role) {
		this.role = role;
	}

}
