package com.kekeinfo.core.business.dewatering.model;

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
import com.kekeinfo.core.business.pointinfo.model.DewateringInfo;
import com.kekeinfo.core.business.pointlink.model.DewateringLink;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "DEWATERING", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Dewatering extends Basepoint<DewateringLink, DewateringInfo> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7163715387234423733L;

	@Id
	@Column(name = "DEWATERING_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DEWATERING_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@Column(name = "FLOW", columnDefinition = "decimal(19,3)")
	private BigDecimal flow = new BigDecimal(0); //流量
	
	@Column(name = "REALFLOW", columnDefinition = "decimal(19,3)")
	private BigDecimal rFlow = new BigDecimal(0); //实时流量
	
	@Column(name = "WATER", columnDefinition = "decimal(19,3)")
	private BigDecimal water = new BigDecimal(0); //水位
	
	@Column(name = "WATERDOWN", columnDefinition = "decimal(19,3)")
	private BigDecimal waterDwon = new BigDecimal(0); //水位阈值下限
	
	@Column(name = "REALWATER", columnDefinition = "decimal(19,3)")
	private BigDecimal rWater = new BigDecimal(0); //实时水位
		
	//流量采集转换公式
	@Column(name="FORMULA_1", length=100)
	private String formula1;
	
	//水位采集转换公式
	@Column(name="FORMULA_2", length=100)
	private String formula2;
	
	//上次累计量
	@Column(name = "LASTACCU", columnDefinition = "decimal(19,3)")
	private BigDecimal lastAccu = new BigDecimal(0); 
	
	//本次累计值
	@Column(name = "THISACCU", columnDefinition = "decimal(19,3)")
	private BigDecimal thisAccu = new BigDecimal(0);
	
	//所属工地
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CSITE_ID", nullable=true)
	private ConstructionSite cSite;
	
	//上次更新时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTDATE")
	private Date lastDate;

	public Dewatering() {
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



	public BigDecimal getFlow() {
		return flow;
	}

	public void setFlow(BigDecimal flow) {
		this.flow = flow;
	}


	public ConstructionSite getcSite() {
		return cSite;
	}

	public void setcSite(ConstructionSite cSite) {
		this.cSite = cSite;
	}


	public BigDecimal getWater() {
		return water;
	}

	public void setWater(BigDecimal water) {
		this.water = water;
	}

	public BigDecimal getrFlow() {
		return rFlow;
	}

	public void setrFlow(BigDecimal rFlow) {
		this.rFlow = rFlow;
	}

	public BigDecimal getrWater() {
		return rWater;
	}

	public void setrWater(BigDecimal rWater) {
		this.rWater = rWater;
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
	public void updateDataStatusByData(BigDecimal flow, BigDecimal water) {
		int status = 0;
		if(flow!=null){
			if(flow.compareTo(getFlow())==1){
				status=1;
			}
		}
		if(water!=null){
			if(water.compareTo(getWater())==1 || water.compareTo(getWaterDwon())==-1){
				status+=2;
			}
		}
		setDataStatus(status);
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

	
}
