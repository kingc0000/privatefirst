package com.kekeinfo.core.business.monitor.surface.model;

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
import com.kekeinfo.core.business.equipment.model.Equip;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MHORDISPLACE", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class HorDisplace extends MbasePoint<HorDisplace> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7240994678161288687L;

	@Id
	@Column(name = "HORDISPLACE_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MHORDISPLACE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	// zh:孔号
	@Column(name = "HOLENO")
	private String holeNO;

	// zh:总深度
	@Column(name = "SUMDEPTH")
	private BigDecimal sumDepth;

	// zh:间隔
	@Column(name = "MINTERVAL")
	private BigDecimal minterval;

	// zh:初始位移
	@Column(name = "INITDISPLACE")
	private BigDecimal initDisplace;

	// zh:告警阀值
	@Column(name = "THRESHOLD")
	private BigDecimal threshold;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	// zh:设备
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EQUIPMENT_ID", nullable = true)
	private Equip equip;

	@JsonIgnore
	@ManyToOne(targetEntity = Monitor.class)
	@JoinColumn(name = "MONITOR_ID", nullable = false)
	private Monitor monitor;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getHoleNO() {
		return holeNO;
	}

	public void setHoleNO(String holeNO) {
		this.holeNO = holeNO;
	}

	public BigDecimal getSumDepth() {
		return sumDepth;
	}

	public void setSumDepth(BigDecimal sumDepth) {
		this.sumDepth = sumDepth;
	}

	public BigDecimal getMinterval() {
		return minterval;
	}

	public void setMinterval(BigDecimal minterval) {
		this.minterval = minterval;
	}

	public BigDecimal getInitDisplace() {
		return initDisplace;
	}

	public void setInitDisplace(BigDecimal initDisplace) {
		this.initDisplace = initDisplace;
	}

	public BigDecimal getThreshold() {
		return threshold;
	}

	public void setThreshold(BigDecimal threshold) {
		this.threshold = threshold;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public Equip getEquip() {
		return equip;
	}

	public void setEquip(Equip equip) {
		this.equip = equip;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

}
