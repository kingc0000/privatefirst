package com.kekeinfo.web.entity;

import java.io.Serializable;
import java.util.List;

public class PinYinUserName implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3100114333058010489L;
	String code;
	List<PinYinUser> lists;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<PinYinUser> getLists() {
		return lists;
	}
	public void setLists(List<PinYinUser> lists) {
		this.lists = lists;
	}

}
