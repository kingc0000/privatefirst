package com.kekeinfo.core.business.monitor.substrate.model;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MSUBSTRATE", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class SubStrate extends KekeinfoEntity<Long, SubStrate> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 105795516508179222L;

	@Id
	@Column(name = "MSUBSTRATE_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MSUBSTRATE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	// zh:备注
	@Column(name = "REMARK")
	private String remark;

	// zh:附件
		@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
		@JoinTable(name = "MSUB_ATTACH", schema = SchemaConstant.KEKEINFO_SCHEMA, joinColumns = {
				@JoinColumn(name = "MSUBSTRATE_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
						@JoinColumn(name = "ATTACH_ID", nullable = false, updatable = false) })
		@Cascade({ org.hibernate.annotations.CascadeType.DETACH, org.hibernate.annotations.CascadeType.LOCK,
				org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.REPLICATE

		})
		@JsonIgnore
		private List<Attach> attach = new ArrayList<Attach>();
	

	@Embedded
	private AuditSection auditSection  = new AuditSection();

	// 所属项目
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MONITOR_ID", nullable = true)
	private Monitor monitor;

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public List<Attach> getAttach() {
		return attach;
	}

	public void setAttach(List<Attach> attach) {
		this.attach = attach;
	}

}
