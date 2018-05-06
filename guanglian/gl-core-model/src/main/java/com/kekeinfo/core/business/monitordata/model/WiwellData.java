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
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "WARNING_IWELLDATA", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class WiwellData extends WarningData<Invertedwell> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4478719760749740659L;

	@Id
	@Column(name = "WARNING_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "WIWELLDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "FLOW", columnDefinition = "decimal(19,3)")
	private BigDecimal flow = new BigDecimal(0); //流量
	
	@Column(name = "FLOWTHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal flowThreshold = new BigDecimal(0); //阈值
	
	@Column(name = "PRESSURE", columnDefinition = "decimal(19,3)")
	private BigDecimal pressure = new BigDecimal(0); //井内水位
	
	@Column(name = "PRESSURETHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal pressureThreshold = new BigDecimal(0); //阈值

	/**
	 * 0:未操过阈值
	 * 1:flow超过阈值
	 * 2:pressure超过阈值
	 * 3：均操过阈值
	 */
	@Column(name = "STATUS")
	private int status =0;
	public WiwellData() {
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

	public BigDecimal getPressure() {
		return pressure;
	}

	public void setPressure(BigDecimal pressure) {
		this.pressure = pressure;
	}

	public BigDecimal getPressureThreshold() {
		return pressureThreshold;
	}

	public void setPressureThreshold(BigDecimal pressureThreshold) {
		this.pressureThreshold = pressureThreshold;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
