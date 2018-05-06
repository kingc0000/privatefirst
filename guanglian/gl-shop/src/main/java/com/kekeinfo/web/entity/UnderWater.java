package com.kekeinfo.web.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class UnderWater extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6299331093895167762L;
	
	private Long id; //地下水项目ID，即ConstructionSite
	private BigDecimal longitude;
	private BigDecimal latitude;
	private int sstatus;
	private String rail;
	private int runstatus;
	private Integer monitorPower=10;
	private Integer gatherData=10;
	private boolean jiangong=false;

	public UnderWater() {
		super();
	}
	public UnderWater(Long id) {
		this.id = id;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public int getSstatus() {
		return sstatus;
	}
	public void setSstatus(int sstatus) {
		this.sstatus = sstatus;
	}
	public String getRail() {
		return rail;
	}
	public void setRail(String rail) {
		this.rail = rail;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getRunstatus() {
		return runstatus;
	}
	public void setRunstatus(int runstatus) {
		this.runstatus = runstatus;
	}
	public Integer getMonitorPower() {
		return monitorPower;
	}
	public void setMonitorPower(Integer monitorPower) {
		this.monitorPower = monitorPower;
	}
	public Integer getGatherData() {
		return gatherData;
	}
	public void setGatherData(Integer gatherData) {
		this.gatherData = gatherData;
	}
	public boolean isJiangong() {
		return jiangong;
	}
	public void setJiangong(boolean jiangong) {
		this.jiangong = jiangong;
	}

}
