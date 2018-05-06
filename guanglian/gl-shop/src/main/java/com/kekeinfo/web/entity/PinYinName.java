package com.kekeinfo.web.entity;

import java.io.Serializable;

public class PinYinName  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4144630150593124238L;
	private String pinyin;
	private String name;
	private String id;
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
