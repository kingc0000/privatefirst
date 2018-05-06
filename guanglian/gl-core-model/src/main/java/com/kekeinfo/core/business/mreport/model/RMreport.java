package com.kekeinfo.core.business.mreport.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.business.monitor.statistical.model.Mstatistical;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "RMREPORT", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class RMreport extends KekeinfoEntity<Long, RMreport> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5060088098376329018L;

	@Id
	@Column(name = "RMREPORT_ID", nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "RMREPORT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_LAST")
	private Date laster;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_THIS")
	private Date thiser;

	// zh:报表编号
	@Column(name = "RNO", length = 100)
	private String rNo;

	// zh:监测者
	@Column(name = "MONITER")
	private String rmonitor;

	// zh:计算者
	@Column(name = "COMPUTER")
	private String computer;

	// zh:校核者
	@Column(name = "VERIFIER")
	private String verifier;

	@JsonIgnore
	@ManyToOne(targetEntity = Monitor.class)
	@JoinColumn(name = "MONITOR_ID", nullable = false)
	private Monitor monitor;

	@JsonIgnore
	@ManyToOne(targetEntity = Mstatistical.class)
	@JoinColumn(name = "MSTATISTICAL_ID", nullable = false)
	private Mstatistical mstatistical;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
	private Set<RSurfaceData> sSurface = new HashSet<RSurfaceData>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
	private Set<RBuildingData> sBuilding = new HashSet<RBuildingData>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
	private Set<RWaterLineData> sWaterLine = new HashSet<RWaterLineData>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
	private Set<RRingBeamData> sRingBeam = new HashSet<RRingBeamData>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
	private Set<RSupAxialData> sSupAxial = new HashSet<RSupAxialData>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
	private Set<RUpRightData> sUpRight = new HashSet<RUpRightData>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
	private Set<RHiddenLineData> sHiddenLine = new HashSet<RHiddenLineData>();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
	private Set<RDisplacementData> sDisplacement = new HashSet<RDisplacementData>();
	
	// zh:报送
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH })
	@JoinTable(name = "REPORT_COMPANY", schema = SchemaConstant.KEKEINFO_SCHEMA, joinColumns = {
			@JoinColumn(name = "RMREPORT_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "BASEDATA_TYPE_ID", nullable = false, updatable = false) })
	@Cascade({ org.hibernate.annotations.CascadeType.DETACH, org.hibernate.annotations.CascadeType.LOCK,
			org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.REPLICATE

	})
	private List<BasedataType> rcompanys = new ArrayList<BasedataType>();

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

	public Date getLaster() {
		return laster;
	}

	public void setLaster(Date laster) {
		this.laster = laster;
	}

	public String getrNo() {
		return rNo;
	}

	public void setrNo(String rNo) {
		this.rNo = rNo;
	}

	public String getComputer() {
		return computer;
	}

	public void setComputer(String computer) {
		this.computer = computer;
	}

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	public String getRmonitor() {
		return rmonitor;
	}

	public void setRmonitor(String rmonitor) {
		this.rmonitor = rmonitor;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public Mstatistical getMstatistical() {
		return mstatistical;
	}

	public void setMstatistical(Mstatistical mstatistical) {
		this.mstatistical = mstatistical;
	}

	public Date getThiser() {
		return thiser;
	}

	public void setThiser(Date thiser) {
		this.thiser = thiser;
	}

	public Set<RSurfaceData> getsSurface() {
		return sSurface;
	}

	public void setsSurface(Set<RSurfaceData> sSurface) {
		this.sSurface = sSurface;
	}

	public Set<RBuildingData> getsBuilding() {
		return sBuilding;
	}

	public void setsBuilding(Set<RBuildingData> sBuilding) {
		this.sBuilding = sBuilding;
	}

	public Set<RWaterLineData> getsWaterLine() {
		return sWaterLine;
	}

	public void setsWaterLine(Set<RWaterLineData> sWaterLine) {
		this.sWaterLine = sWaterLine;
	}

	public Set<RRingBeamData> getsRingBeam() {
		return sRingBeam;
	}
	
	public void setsRingBeam(Set<RRingBeamData> sRingBeam) {
		this.sRingBeam = sRingBeam;
	}

	public Set<RSupAxialData> getsSupAxial() {
		return sSupAxial;
	}

	public void setsSupAxial(Set<RSupAxialData> sSupAxial) {
		this.sSupAxial = sSupAxial;
	}

	public Set<RUpRightData> getsUpRight() {
		return sUpRight;
	}

	public void setsUpRight(Set<RUpRightData> sUpRight) {
		this.sUpRight = sUpRight;
	}

	public Set<RHiddenLineData> getsHiddenLine() {
		return sHiddenLine;
	}

	public void setsHiddenLine(Set<RHiddenLineData> sHiddenLine) {
		this.sHiddenLine = sHiddenLine;
	}

	public List<BasedataType> getRcompanys() {
		return rcompanys;
	}

	public void setRcompanys(List<BasedataType> rcompanys) {
		this.rcompanys = rcompanys;
	}

	public Set<RDisplacementData> getsDisplacement() {
		return sDisplacement;
	}

	public void setsDisplacement(Set<RDisplacementData> sDisplacement) {
		this.sDisplacement = sDisplacement;
	}

}
