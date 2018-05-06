package com.kekeinfo.web.entity;

import java.io.Serializable;

public class UGJob  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2308484103837570887L;

	String title;
	long stime;
	int rstatus=0;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getStime() {
		return stime;
	}
	public void setStime(long stime) {
		this.stime = stime;
	}
	public int getRstatus() {
		return rstatus;
	}
	public void setRstatus(int rstatus) {
		this.rstatus = rstatus;
	}

}
