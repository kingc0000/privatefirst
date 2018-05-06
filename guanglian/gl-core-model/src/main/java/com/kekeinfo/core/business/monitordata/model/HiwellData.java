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
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "HIWELLDATA", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class HiwellData extends KekeinfoEntity<Long, HiwellData> implements Auditable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6131993541879544824L;

	@Id
	@Column(name = "IWELLDATA_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "HIWELLDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@Column(name = "FLOW", columnDefinition = "decimal(19,3)")
	private BigDecimal flow = new BigDecimal(0); //流量
	
	@Column(name = "FLOWTHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal flowThreshold = new BigDecimal(0); //阈值
	
	@Column(name = "PRESSURE", columnDefinition = "decimal(19,3)")
	private BigDecimal pressure = new BigDecimal(0); //井内水位
	
	@Column(name = "PRESSURETHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal pressureThreshold = new BigDecimal(0); //阈值

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
	/**
	 * 0:未操过阈值
	 * 1:flow超过阈值
	 * 2:pressure超过阈值
	 * 3：均操过阈值
	 */
	@Column(name = "STATUS")
	private int status =0;
	
	//所属抽水井
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="IWELL_ID", nullable=true)
	private Invertedwell iWell;
	
	public HiwellData() {
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

	public BigDecimal getPressure() {
		return pressure;
	}

	public void setPressure(BigDecimal pressure) {
		this.pressure = pressure;
	}

	public Invertedwell getiWell() {
		return iWell;
	}

	public void setiWell(Invertedwell iWell) {
		this.iWell = iWell;
	}

	public BigDecimal getFlowThreshold() {
		return flowThreshold;
	}

	public void setFlowThreshold(BigDecimal flowThreshold) {
		this.flowThreshold = flowThreshold;
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
