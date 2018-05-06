package com.kekeinfo.core.business.monitoreqip.model;

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
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.monitor.model.MPoint;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MPOINT_EQUP", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class MpointEquip extends KekeinfoEntity<Long, MpointEquip> implements Auditable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6918217967025509766L;

	@Id
	@Column(name = "MPOINT_EQUP_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MPOINT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	//所属项目
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MONITOR_ID", nullable=false)
	private Monitor monitor;
	
	//所属项目
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MPOINT_ID", nullable=false)
	private MPoint mpoint;
	
	//所属项目
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MEQUIT_ID", nullable=false)
	private MonitorEqip monitorEqip;
	
	//zh:当前使用
	@Column(name = "USED")
	private boolean used =true;
	
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

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public MPoint getMpoint() {
		return mpoint;
	}

	public void setMpoint(MPoint mpoint) {
		this.mpoint = mpoint;
	}

	public MonitorEqip getMonitorEqip() {
		return monitorEqip;
	}

	public void setMonitorEqip(MonitorEqip monitorEqip) {
		this.monitorEqip = monitorEqip;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
}
