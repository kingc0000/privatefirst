package com.kekeinfo.core.business.monitordata.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 环境监测 告警记录
 * @author sam
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "WARNING_EMONITORDATA", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class WemonitorData extends WarningData<Deformmonitor> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8178977119686524338L;

	@Id
	@Column(name = "WARNING_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "WEMONITORDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name = "DATA", columnDefinition = "decimal(19,3)")
	private BigDecimal data = new BigDecimal(0); //数据
	
	@Column(name = "THRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal threshold = new BigDecimal(0); //阈值
	
	/**
	 * 0:未操过阈值
	 * 3:操过阈值
	 */
	@Column(name = "STATUS")
	private int status =0;

	public WemonitorData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getData() {
		return data;
	}

	public void setData(BigDecimal data) {
		this.data = data;
	}

	public BigDecimal getThreshold() {
		return threshold;
	}

	public void setThreshold(BigDecimal threshold) {
		this.threshold = threshold;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
