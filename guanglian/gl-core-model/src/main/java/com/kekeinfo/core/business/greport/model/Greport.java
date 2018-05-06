package com.kekeinfo.core.business.greport.model;

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
import com.kekeinfo.core.business.daily.model.GuardDaily;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "GREPORT", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class Greport extends KekeinfoEntity<Long, Greport> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5060088098376329018L;

	@Id
	@Column(name = "GREPORT_ID", nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "GREPORT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	// zh:校核者
	@Column(name = "VERIFIER")
	private String verifier;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_THIS")
	private Date thiser;

	// zh:报表编号
	@Column(name = "RNO", length = 100)
	private String rNo;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Guard.class)
	@JoinColumn(name = "GUARD_ID", nullable = false)
	private Guard guard;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "greport")
	private Set<RDiameterConvertData> rDiameterConvert = new HashSet<RDiameterConvertData>();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "greport")
	private Set<RVerticalDisData> rVerticalDis = new HashSet<RVerticalDisData>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "greport")
	private Set<RHshiftData> rHshift = new HashSet<RHshiftData>();

	@JsonIgnore
	@ManyToOne(targetEntity = GuardDaily.class)
	@JoinColumn(name = "GDIALY_ID", nullable = false)
	private GuardDaily dialy;
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

	public Guard getGuard() {
		return guard;
	}

	public void setGuard(Guard guard) {
		this.guard = guard;
	}

	public Set<RDiameterConvertData> getrDiameterConvert() {
		return rDiameterConvert;
	}

	public void setrDiameterConvert(Set<RDiameterConvertData> rDiameterConvert) {
		this.rDiameterConvert = rDiameterConvert;
	}

	public Set<RVerticalDisData> getrVerticalDis() {
		return rVerticalDis;
	}

	public void setrVerticalDis(Set<RVerticalDisData> rVerticalDis) {
		this.rVerticalDis = rVerticalDis;
	}

	public Set<RHshiftData> getrHshift() {
		return rHshift;
	}

	public void setrHshift(Set<RHshiftData> rHshift) {
		this.rHshift = rHshift;
	}

	public GuardDaily getDialy() {
		return dialy;
	}

	public void setDialy(GuardDaily dialy) {
		this.dialy = dialy;
	}

}
