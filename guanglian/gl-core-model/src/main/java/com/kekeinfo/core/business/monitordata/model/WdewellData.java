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
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "WARNING_DEWELLDATA", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class WdewellData extends WarningData<Dewatering> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2922982509109269394L;

	@Id
	@Column(name = "WARNING_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "WDEWELLDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name = "FLOW", columnDefinition = "decimal(19,3)")
	private BigDecimal flow = new BigDecimal(0); //流量
	
	@Column(name = "FLOWTHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal flowThreshold = new BigDecimal(0); //阈值
	
	@Column(name = "WATER", columnDefinition = "decimal(19,3)")
	private BigDecimal water = new BigDecimal(0); //水位
	
	@Column(name = "WATERTHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal waterThreshold = new BigDecimal(0); //阈值
	
	@Column(name = "WATERTDOWN", columnDefinition = "decimal(19,3)")
	private BigDecimal waterDown = new BigDecimal(0); //阈值下限
	
	@Column(name = "TEMPERATURE", columnDefinition = "decimal(19,3)")
	private BigDecimal temperature = new BigDecimal(0); //水温

	@Column(name = "TEMPERATURETHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal temperatureThreshold = new BigDecimal(0); //阈值
	
	/**
	 * 0:未操过阈值
	 * 1:flow超过阈值
	 * 2:water超过阈值
	 * 3：均操过阈值
	 */
	@Column(name = "STATUS")
	private int status =0;

	public WdewellData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getFlow() {
		return flow;
	}

	public void setFlow(BigDecimal flow) {
		this.flow = flow;
	}

	public BigDecimal getFlowThreshold() {
		return flowThreshold;
	}

	public void setFlowThreshold(BigDecimal flowThreshold) {
		this.flowThreshold = flowThreshold;
	}

	public BigDecimal getWater() {
		return water;
	}

	public void setWater(BigDecimal water) {
		this.water = water;
	}

	public BigDecimal getWaterThreshold() {
		return waterThreshold;
	}

	public void setWaterThreshold(BigDecimal waterThreshold) {
		this.waterThreshold = waterThreshold;
	}

	public BigDecimal getWaterDown() {
		return waterDown;
	}

	public void setWaterDown(BigDecimal waterDown) {
		this.waterDown = waterDown;
	}

	public BigDecimal getTemperature() {
		return temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}

	public BigDecimal getTemperatureThreshold() {
		return temperatureThreshold;
	}

	public void setTemperatureThreshold(BigDecimal temperatureThreshold) {
		this.temperatureThreshold = temperatureThreshold;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
