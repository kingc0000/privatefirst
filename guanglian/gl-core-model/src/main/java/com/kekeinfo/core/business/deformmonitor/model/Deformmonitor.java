package com.kekeinfo.core.business.deformmonitor.model;

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
import com.kekeinfo.core.business.pointinfo.model.DeformInfo;
import com.kekeinfo.core.business.pointlink.model.DeformLink;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "DEFORMMONITOR", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Deformmonitor extends Basepoint<DeformLink, DeformInfo> implements Auditable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6131993541879544824L;

	@Id
	@Column(name = "DEFORMMONITOR_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DEFORMMONITOR_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	//数据阈值
	@Column(name="DEFORMMONITOR", columnDefinition = "decimal(19,3)")
	private BigDecimal deformData = new BigDecimal(0);
	
	//实时数据
	@Column(name="REALDATA", columnDefinition = "decimal(19,3)")
	private BigDecimal rData = new BigDecimal(0);
	
	//前一次的值
	@Column(name="LASTDATA", columnDefinition = "decimal(19,3)")
	private BigDecimal lData = new BigDecimal(0);	
	
	//数据采集转换公式
	@Column(name="FORMULA_1", length=100)
	private String formula1;
	
	//所属工地
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CSITE_ID", nullable=true)
	private ConstructionSite cSite;

	//上次更新时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTDATE")
	private Date lastDate;
	
	public Deformmonitor() {
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

	public BigDecimal getDeformData() {
		return deformData;
	}

	public void setDeformData(BigDecimal deformData) {
		this.deformData = deformData;
	}

	public BigDecimal getrData() {
		return rData;
	}

	public void setrData(BigDecimal rData) {
		this.rData = rData;
	}
	
	/**
	 * 测点根据新录入的最新测点数据值更新测点的实时状态
	 * 如果测点的状态为故障或关闭，则状态保持不变
	 */
	public void updateDataStatusByData(BigDecimal deformData) {
		int status = 0;
		if(deformData!=null){
			if(deformData.compareTo(getDeformData())==1){
				status=1;
			}
		}
		setDataStatus(status);
	}
	
	public BigDecimal getlData() {
		return lData;
	}

	public void setlData(BigDecimal lData) {
		this.lData = lData;
	}

	public String getFormula1() {
		return formula1;
	}

	public void setFormula1(String formula1) {
		this.formula1 = formula1;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

}
