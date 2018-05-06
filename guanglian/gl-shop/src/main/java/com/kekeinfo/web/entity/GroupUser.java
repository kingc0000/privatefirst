package com.kekeinfo.web.entity;

import java.io.Serializable;
import java.util.List;

public class GroupUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8697199047594837956L;
	
	private String memo;
	
	private List<UserGroup> froms;
	
	private List<UserGroup> tos;

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<UserGroup> getFroms() {
		return froms;
	}

	public void setFroms(List<UserGroup> froms) {
		this.froms = froms;
	}

	public List<UserGroup> getTos() {
		return tos;
	}

	public void setTos(List<UserGroup> tos) {
		this.tos = tos;
	}

	
}
