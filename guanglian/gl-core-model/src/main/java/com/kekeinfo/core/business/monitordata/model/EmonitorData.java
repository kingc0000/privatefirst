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
import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "EMONITORDATA", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class EmonitorData extends KekeinfoEntity<Long, EmonitorData> implements Auditable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6131993541879544824L;

	@Id
	@Column(name = "EMONITORDATA_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "EMONITORDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	
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
	
	//所属抽水井
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="EMONITOR_ID", nullable=true)
	private Deformmonitor emonitor;
	
	//历史表id
	@Column(name = "H_ID", nullable=false)
	private Long hid;
	
	public EmonitorData() {
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

	public BigDecimal getData() {
		return data;
	}

	public void setData(BigDecimal data) {
		this.data = data;
	}

	public Deformmonitor getEmonitor() {
		return emonitor;
	}

	public void setEmonitor(Deformmonitor emonitor) {
		this.emonitor = emonitor;
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

	public Long getHid() {
		return hid;
	}

	public void setHid(Long hid) {
		this.hid = hid;
	}
	
}
