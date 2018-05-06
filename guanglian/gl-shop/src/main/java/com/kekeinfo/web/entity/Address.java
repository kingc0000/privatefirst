package com.kekeinfo.web.entity;

import java.io.Serializable;

public class Address  extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3819977951203623016L;
	
	private String name;
	
	private String zoneName;
	
	private String city;
	
	private String address;
	
	private String telephone;
	
	private String company;
	
	private boolean isdefault;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public boolean isIsdefault() {
		return isdefault;
	}

	public void setIsdefault(boolean isdefault) {
		this.isdefault = isdefault;
	}
	

}
