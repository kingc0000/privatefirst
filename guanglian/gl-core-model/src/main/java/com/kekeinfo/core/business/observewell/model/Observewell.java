package com.kekeinfo.core.business.observewell.model;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.pointinfo.model.ObserveInfo;
import com.kekeinfo.core.business.pointlink.model.ObserveLink;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "OBSERVEWELL", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Observewell extends Basepoint<ObserveLink, ObserveInfo> implements Auditable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8998768317385933666L;

	@Id
	@Column(name = "OBSERVEWELL_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "OBSERVEWELL_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	//所属工地
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CSITE_ID", nullable=true)
	private ConstructionSite cSite;
	
	@Column(name = "FLOW", columnDefinition = "decimal(19,3)")
	private BigDecimal flow = new BigDecimal(0); //流量
	
	@Column(name = "REALFLOW", columnDefinition = "decimal(19,3)")
	private BigDecimal rFlow = new BigDecimal(0); //实时流量
	
	//水位计量
	@Column(name="WATERMEASUREMENT", columnDefinition = "decimal(19,3)")
	private BigDecimal waterMeasurement = new BigDecimal(0);
	
	@Column(name = "WATERDOWN", columnDefinition = "decimal(19,3)")
	private BigDecimal waterDwon = new BigDecimal(0); //水位阈值下限
	
	//实时水位计量
	@Column(name="REALWATER", columnDefinition = "decimal(19,3)")
	private BigDecimal rWater = new BigDecimal(0);
	
	//水温
	@Column(name="WATERTEMPERATURE", columnDefinition = "decimal(19,3)")
	private BigDecimal waterTemperature = new BigDecimal(0);
	
	//实时水温
	@Column(name="REALTEMPERATURE", columnDefinition = "decimal(19,3)")
	private BigDecimal rTemperature = new BigDecimal(0);
	
	//流量采集转换公式
	@Column(name="FORMULA_1", length=100)
	private String formula1;
	
	//水位采集转换公式
	@Column(name="FORMULA_2", length=100)
	private String formula2;
	
	//水温采集转换公式
	@Column(name="FORMULA_3", length=100)
	private String formula3;
	
	//上次累计量
	@Column(name = "LASTACCU", columnDefinition = "decimal(19,3)")
	private BigDecimal lastAccu = new BigDecimal(0); 
	
	//本次累计值
	@Column(name = "THISACCU", columnDefinition = "decimal(19,3)")
	private BigDecimal thisAccu = new BigDecimal(0);
	
	//上次更新时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTDATE")
	private Date lastDate;
	
	
	public Observewell() {
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


	public ConstructionSite getcSite() {
		return cSite;
	}

	public void setcSite(ConstructionSite cSite) {
		this.cSite = cSite;
	}

	public BigDecimal getWaterMeasurement() {
		return waterMeasurement;
	}

	public void setWaterMeasurement(BigDecimal waterMeasurement) {
		this.waterMeasurement = waterMeasurement;
	}

	public BigDecimal getWaterTemperature() {
		return waterTemperature;
	}

	public void setWaterTemperature(BigDecimal waterTemperature) {
		this.waterTemperature = waterTemperature;
	}

	public BigDecimal getrWater() {
		return rWater;
	}

	public void setrWater(BigDecimal rWater) {
		this.rWater = rWater;
	}

	public BigDecimal getrTemperature() {
		return rTemperature;
	}

	public void setrTemperature(BigDecimal rTemperature) {
		this.rTemperature = rTemperature;
	}

	public BigDecimal getWaterDwon() {
		return waterDwon;
	}

	public void setWaterDwon(BigDecimal waterDwon) {
		this.waterDwon = waterDwon;
	}
	
	/**
	 * 测点根据新录入的最新测点数据值更新测点的实时状态
	 * 如果测点的状态为故障或关闭，则状态保持不变
	 */
	public void updateDataStatusByData(BigDecimal water, BigDecimal waterTemperature) {
		int status = 0;
		if(water!=null){
			if(water.compareTo(getWaterMeasurement())==1 || water.compareTo(getWaterDwon())==-1){
				status=1;
			}
		}
		if(waterTemperature!=null){
			if(waterTemperature.compareTo(getWaterTemperature())==1){
				status+=2;
			}
		}
		setDataStatus(status);
	}

	public String getFormula1() {
		return formula1;
	}

	public void setFormula1(String formula1) {
		this.formula1 = formula1;
	}

	public String getFormula2() {
		return formula2;
	}

	public void setFormula2(String formula2) {
		this.formula2 = formula2;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public BigDecimal getFlow() {
		return flow;
	}

	public void setFlow(BigDecimal flow) {
		this.flow = flow;
	}

	public BigDecimal getrFlow() {
		return rFlow;
	}

	public void setrFlow(BigDecimal rFlow) {
		this.rFlow = rFlow;
	}

	public String getFormula3() {
		return formula3;
	}

	public void setFormula3(String formula3) {
		this.formula3 = formula3;
	}
	
}
