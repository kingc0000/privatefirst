package com.kekeinfo.core.business.department.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.kekeinfo.core.business.project.model.Project;

public class FeaturesType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4573500677425191154L;
	
	private String value;
	private String name;
	/**
	 * -1: 未选择
	 * 0：半选中状态
	 * 1：选中
	 * 1:也表示没有权限
	 */
	private int sstatus=-1;
	private Set<Project> cSites = new HashSet<Project>();

	
	public FeaturesType() {
		super();
	}
	public FeaturesType(String value, int sstatus) {
		super();
		this.value = value;
		this.sstatus = sstatus;
	}
	public Set<Project> getcSites() {
		return cSites;
	}
	public void setcSites(Set<Project> cSites) {
		this.cSites = cSites;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSstatus() {
		return sstatus;
	}
	public void setSstatus(int sstatus) {
		this.sstatus = sstatus;
	}
}
