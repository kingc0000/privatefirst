package com.kekeinfo.core.business.monitor.data.model;

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
import com.kekeinfo.core.business.monitor.surface.model.SupAxial;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "SUPAXIALDATA", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class SupAxialData extends MDbasePoint<SupAxialData> implements Auditable {

	private static final long serialVersionUID = -8231506062099735443L;
	@Id
	@Column(name = "SUPAXIALDATA_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SUPAXIALDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Embedded
	private AuditSection auditSection = new AuditSection();

	// zh:Surface
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUPAXIAL_ID", nullable = true)
	private SupAxial spoint;

	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
		this.auditSection = audit;
	}

	public SupAxial getSpoint() {
		return spoint;
	}

	public void setSpoint(SupAxial spoint) {
		this.spoint = spoint;
	}

}
