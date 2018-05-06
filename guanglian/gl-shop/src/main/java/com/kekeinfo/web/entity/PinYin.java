package com.kekeinfo.web.entity;

import java.io.Serializable;
import java.util.List;

public class PinYin implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3100114333058010489L;
	String code;
	String type;
	List<PinYinName> lists;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<PinYinName> getLists() {
		return lists;
	}
	public void setLists(List<PinYinName> lists) {
		this.lists = lists;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
