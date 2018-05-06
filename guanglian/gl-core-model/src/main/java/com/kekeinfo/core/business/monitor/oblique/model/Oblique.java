package com.kekeinfo.core.business.monitor.oblique.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MOBLIQUE", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class Oblique extends KekeinfoEntity<Long, Oblique> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 638706199546896741L;

	@Id
	@Column(name = "OBLIQUE_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "OBLIQUE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	// zh:孔号
	@Column(name = "NAMBER")
	private String namber;

	// zh:评价
	@Column(name = "EVALUATE", length = 1000)
	private String evaluate;

	// zh:水平位移修正
	@Column(name = "HORCORRECT")
	private BigDecimal horcorrect =new BigDecimal(0);

	@JsonIgnore
	@ManyToOne(targetEntity = Monitor.class)
	@JoinColumn(name = "MONITOR_ID", nullable = false)
	private Monitor monitor;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	public String getNamber() {
		return namber;
	}

	public void setNamber(String namber) {
		this.namber = namber;
	}

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	public BigDecimal getHorcorrect() {
		return horcorrect;
	}

	public void setHorcorrect(BigDecimal horcorrect) {
		this.horcorrect = horcorrect;
	}

	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}

	@Override
	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
