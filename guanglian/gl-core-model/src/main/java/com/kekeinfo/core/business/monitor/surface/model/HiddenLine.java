package com.kekeinfo.core.business.monitor.surface.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MHIDDENLINE", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class HiddenLine extends MbasePoint<HiddenLine> implements Auditable {
	/**
	 * 潜层水位
	 */
	private static final long serialVersionUID = -8231506062099735443L;
	@Id
	@Column(name = "HIDDENLINE_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MHiddenLine_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	// zh:管口高程
	@Column(name = "LINEGAOCHEN")
	private BigDecimal lineGaoChen;

	// zh:开挖前累计位移
	@Column(name = "FRONTDISPLACEMENT")
	private BigDecimal frontDisplacement;
	
	public BigDecimal getFrontDisplacement() {
		return frontDisplacement;
	}

	public void setFrontDisplacement(BigDecimal frontDisplacement) {
		this.frontDisplacement = frontDisplacement;
	}
	@Embedded
	private AuditSection auditSection = new AuditSection();

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public BigDecimal getLineGaoChen() {
		return lineGaoChen;
	}

	public void setLineGaoChen(BigDecimal lineGaoChen) {
		this.lineGaoChen = lineGaoChen;
	}

}
