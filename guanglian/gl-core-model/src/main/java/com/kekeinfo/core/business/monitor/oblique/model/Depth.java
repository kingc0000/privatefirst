package com.kekeinfo.core.business.monitor.oblique.model;

import java.math.BigDecimal;

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
@Table(name = "DEPTH", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class Depth extends KekeinfoEntity<Long, Depth> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5044945689239571465L;

	@Id
	@Column(name = "DEPTH_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DEPTH_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "DEEP")
	private BigDecimal deep;
	
	@Column(name = "AVEV")
	private BigDecimal av =new BigDecimal(0);

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OBLIQUE_ID", nullable = true)
	private Oblique oblique;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	

	public Oblique getOblique() {
		return oblique;
	}

	public void setOblique(Oblique oblique) {
		this.oblique = oblique;
	}

	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}

	@Override
	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAv() {
		return av;
	}

	public void setAv(BigDecimal av) {
		this.av = av;
	}

	public BigDecimal getDeep() {
		return deep;
	}

	public void setDeep(BigDecimal deep) {
		this.deep = deep;
	}

}
