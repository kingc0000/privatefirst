package com.kekeinfo.core.business.monitor.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class Mbade implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8454501021694155981L;

	/**
	 * 
	 */
	//建设单位
	@Column(name="UNIT", length=100)
	private String unit;
	
	//施工承包单位
	@Column(name ="CONTOR", length=100)
	private String contor;
	
	//设计单位
	@Column(name = "DESIGN",length=100)
	private String design;
	
	//等级
	@Column(name="RANK",length=10)
	private String rank;
	
	//监理单位
	@Column(name = "SUPERV",length=100)
	private String superv;
	
	//开工日期
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_CREATED")
	private Date pCreated;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_MODIFIED")
	private Date pEnd;
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getContor() {
		return contor;
	}

	public void setContor(String contor) {
		this.contor = contor;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getSuperv() {
		return superv;
	}

	public void setSuperv(String superv) {
		this.superv = superv;
	}

	public Date getpCreated() {
		return pCreated;
	}

	public void setpCreated(Date pCreated) {
		this.pCreated = pCreated;
	}

	public Date getpEnd() {
		return pEnd;
	}

	public void setpEnd(Date pEnd) {
		this.pEnd = pEnd;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
}
