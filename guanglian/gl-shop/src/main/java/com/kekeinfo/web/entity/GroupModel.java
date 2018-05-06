package com.kekeinfo.web.entity;

import java.util.List;

public class GroupModel<T>{
	
	public final static int RESPONSE_STATUS_SUCCESS=0;
	public final static int RESPONSE_STATUS_FAIURE=-1;
	public final static int RESPONSE_OPERATION_COMPLETED=9999;
	
	private String memo;
	//源数据对象集合
	private List<T> froms;
	//目的数据对象集合
	private List<T> tos;
	private int status; //结果状态
	private String message;

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<T> getFroms() {
		return froms;
	}

	public void setFroms(List<T> froms) {
		this.froms = froms;
	}

	public List<T> getTos() {
		return tos;
	}

	public void setTos(List<T> tos) {
		this.tos = tos;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
