package com.kekeinfo.core.business.monitor.statistical.model;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.daily.model.MonitorDaily;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MSTATISTICAL", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class Mstatistical extends KekeinfoEntity<Long, Mstatistical> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8464761132200274226L;

	@Id
	@Column(name = "MSTATISTICAL_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MSTATISTICAL_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	
	@OneToOne(cascade=CascadeType.REFRESH, fetch = FetchType.EAGER)
	private MonitorDaily mDaily;

	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER,mappedBy = "mstatistical")
	private Set<PointManager> pmanagers;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	
	
	@JsonIgnore
	@ManyToOne(targetEntity = Monitor.class)
	@JoinColumn(name = "MONITOR_ID", nullable = false)
	private Monitor monitor;

	

	public Set<PointManager> getPmanagers() {
		return pmanagers;
	}

	public void setPmanagers(Set<PointManager> pmanagers) {
		this.pmanagers = pmanagers;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
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

	public MonitorDaily getmDaily() {
		return mDaily;
	}

	public void setmDaily(MonitorDaily mDaily) {
		this.mDaily = mDaily;
	}

}
