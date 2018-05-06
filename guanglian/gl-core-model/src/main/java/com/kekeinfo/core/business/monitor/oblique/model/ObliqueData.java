package com.kekeinfo.core.business.monitor.oblique.model;

import java.math.BigDecimal;
import java.util.Date;

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
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "OBLIQUEDATA", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class ObliqueData extends KekeinfoEntity<Long, ObliqueData> implements Auditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9162768600042483613L;

	@Id
	@Column(name = "OBLIQUEDATA_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "OBLIQUEDATA_ID_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	// zh:上次累计
	private BigDecimal lastTotal;

	// zh:本次累计
	private BigDecimal curTotal;

	// 本次时间
	@Column(name = "CURDATE")
	private Date curDate;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DEPTH_ID", nullable = true)
	private Depth depth;

	public BigDecimal getLastTotal() {
		return lastTotal;
	}

	public void setLastTotal(BigDecimal lastTotal) {
		this.lastTotal = lastTotal;
	}

	public BigDecimal getCurTotal() {
		return curTotal;
	}

	public void setCurTotal(BigDecimal curTotal) {
		this.curTotal = curTotal;
	}


	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Date getCurDate() {
		return curDate;
	}

	public void setCurDate(Date curDate) {
		this.curDate = curDate;
	}

	public Depth getDepth() {
		return depth;
	}

	public void setDepth(Depth depth) {
		this.depth = depth;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

}
