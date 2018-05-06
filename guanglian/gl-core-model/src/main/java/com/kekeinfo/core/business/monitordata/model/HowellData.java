package com.kekeinfo.core.business.monitordata.model;

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
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "HOWELLDATA", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class HowellData extends KekeinfoEntity<Long, HowellData> implements Auditable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6131993541879544824L;

	@Id
	@Column(name = "OWELLDATA_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "HOWELLDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	@Column(name = "FLOW", columnDefinition = "decimal(19,3)")
	private BigDecimal flow = new BigDecimal(0); //流量
	
	@Column(name = "FLOWTHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal flowThreshold = new BigDecimal(0); //阈值
	
	@Column(name = "WATER", columnDefinition = "decimal(19,3)")
	private BigDecimal water = new BigDecimal(0); //水位
	
	@Column(name = "WATERTHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal waterThreshold = new BigDecimal(0); //阈值
	
	@Column(name = "WATERDOWN", columnDefinition = "decimal(19,3)")
	private BigDecimal waterDwon = new BigDecimal(0); //水位阈值下限
	
	@Column(name = "TEMPERATURE", columnDefinition = "decimal(19,3)")
	private BigDecimal temperature = new BigDecimal(0); //水温

	@Column(name = "TEMPERATURETHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal temperatureThreshold = new BigDecimal(0); //阈值
	
	//上次累计量
	@Column(name = "LASTACCU", columnDefinition = "decimal(19,3)")
	private BigDecimal lastAccu = new BigDecimal(0); 
	
	//本次累计值
	@Column(name = "THISACCU", columnDefinition = "decimal(19,3)")
	private BigDecimal thisAccu = new BigDecimal(0);
	
	//累计周期
	@Column(name = "ACCUPERIOD", columnDefinition = "decimal(19,3)")
	private BigDecimal accuPeriod = new BigDecimal(0);

	@Column(name = "AUTO")
	private Boolean mAuto = true;
	
	//所属观测井
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="OWELL_ID", nullable=true)
	private Observewell oWell;
	
	/**
	 * 0:未操过阈值
	 * 1:水位超过阈值
	 * 2:temperature超过阈值
	 * 3：均操过阈值
	 */
	@Column(name = "STATUS")
	private int status =0;
	
	public HowellData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public BigDecimal getTemperature() {
		return temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}

	public Observewell getoWell() {
		return oWell;
	}

	public void setoWell(Observewell oWell) {
		this.oWell = oWell;
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

	public BigDecimal getWaterDwon() {
		return waterDwon;
	}

	public void setWaterDwon(BigDecimal waterDwon) {
		this.waterDwon = waterDwon;
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

	public BigDecimal getLastAccu() {
		return lastAccu;
	}

	public void setLastAccu(BigDecimal lastAccu) {
		this.lastAccu = lastAccu;
	}

	public BigDecimal getThisAccu() {
		return thisAccu;
	}

	public void setThisAccu(BigDecimal thisAccu) {
		this.thisAccu = thisAccu;
	}

	public BigDecimal getAccuPeriod() {
		return accuPeriod;
	}

	public void setAccuPeriod(BigDecimal accuPeriod) {
		this.accuPeriod = accuPeriod;
	}

	public Boolean getmAuto() {
		return mAuto;
	}

	public void setmAuto(Boolean mAuto) {
		this.mAuto = mAuto;
	}

}
