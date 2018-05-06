package com.kekeinfo.core.business.report.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 工程日志管理
 * @author chenyong
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "REPORT", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Report extends KekeinfoEntity<Long, Report> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1441422349179957870L;

	@Id
	@Column(name = "REPORT_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "REPORT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	/**
	 * 报表日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "R_DATE")
	private Date rDate;
	
	@Column(name = "OWNER")
	private String owner;
	
	@Column(name = "NAME")
	private String name;
	
	/**
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="DAILY_ID", nullable=true)
	private Daily daily ;
	*/
	//井
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "report" ,cascade = {CascadeType.ALL},orphanRemoval=true)
	@OrderBy("name asc")
	private Set<Rdewell> dewells = new LinkedHashSet<Rdewell>();
	
	//井
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "report" ,cascade = {CascadeType.ALL} ,orphanRemoval=true)
	@OrderBy("name asc")
	private Set<Rewell> ewells = new LinkedHashSet<Rewell>();

	//井
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "report" ,cascade = {CascadeType.ALL} ,orphanRemoval=true)
	@OrderBy("name asc")
	private Set<Riwell> iwells = new LinkedHashSet<Riwell>();
	
	//井
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "report" ,cascade = {CascadeType.ALL} ,orphanRemoval=true)
	@OrderBy("name asc")
	private Set<Rowell> owells = new LinkedHashSet<Rowell>();
	
	//井
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "report" ,cascade = {CascadeType.ALL},orphanRemoval=true)
	@OrderBy("name asc")
	private Set<Rpwell> pwells = new LinkedHashSet<Rpwell>();	
	
	//所属工地
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CSITE_ID", nullable=true)
	private ConstructionSite cSite;
	
	public Report() {
		super();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public AuditSection getAuditSection() {
		return auditSection;
	}


	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}


	public Date getrDate() {
		return rDate;
	}


	public void setrDate(Date rDate) {
		this.rDate = rDate;
	}

	public Set<Rdewell> getDewells() {
		return dewells;
	}


	public void setDewells(Set<Rdewell> dewells) {
		this.dewells = dewells;
	}


	public Set<Rewell> getEwells() {
		return ewells;
	}


	public void setEwells(Set<Rewell> ewells) {
		this.ewells = ewells;
	}


	public Set<Riwell> getIwells() {
		return iwells;
	}


	public void setIwells(Set<Riwell> iwells) {
		this.iwells = iwells;
	}


	public Set<Rowell> getOwells() {
		return owells;
	}


	public void setOwells(Set<Rowell> owells) {
		this.owells = owells;
	}


	public Set<Rpwell> getPwells() {
		return pwells;
	}


	public void setPwells(Set<Rpwell> pwells) {
		this.pwells = pwells;
	}


	public ConstructionSite getcSite() {
		return cSite;
	}


	public void setcSite(ConstructionSite cSite) {
		this.cSite = cSite;
	}



	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	

}
