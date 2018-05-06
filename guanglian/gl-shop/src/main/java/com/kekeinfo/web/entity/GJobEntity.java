package com.kekeinfo.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GJobEntity extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8285818330590837303L;

	private String arriveDate;
	private String leaveDate;
	private Date startDate;
	private Date endDate;
	private Integer cstatu = new Integer(0) ;
	private List<User> users;
	public String getArriveDate() {
		return arriveDate;
	}
	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}
	public String getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public Integer getCstatu() {
		return cstatu;
	}
	public void setCstatu(Integer cstatu) {
		this.cstatu = cstatu;
	}
}
