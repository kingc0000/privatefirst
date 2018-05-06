package com.kekeinfo.web.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Plan extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7135371418611337843L;
	private String name;
	private List<GJobEntity> jobs =new ArrayList<>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<GJobEntity> getJobs() {
		return jobs;
	}
	public void setJobs(List<GJobEntity> jobs) {
		this.jobs = jobs;
	}

}
