package com.kekeinfo.core.business.xreport.model;

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "XMREPORT", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class XMreport extends KekeinfoEntity<Long, XMreport> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5060088098376329018L;

	@Id
	@Column(name = "XMREPORT_ID", nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "XMREPORT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	// zh:校核者
	@Column(name = "VERIFIER")
	private String verifier;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_THIS")
	private Date thiser;

	// zh:报表编号
	@Column(name = "RNO", length = 100)
	private String rNo;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Monitor.class)
	@JoinColumn(name = "MONITOR_ID", nullable = false)
	private Monitor monitor;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "xmreport")
	private Set<ROblique> rDepth = new HashSet<ROblique>();

	

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

	

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public Set<ROblique> getrDepth() {
		return rDepth;
	}

	public void setrDepth(Set<ROblique> rDepth) {
		this.rDepth = rDepth;
	}

	public Date getThiser() {
		return thiser;
	}

	public void setThiser(Date thiser) {
		this.thiser = thiser;
	}

	public String getrNo() {
		return rNo;
	}

	public void setrNo(String rNo) {
		this.rNo = rNo;
	}

}
