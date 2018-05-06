package com.kekeinfo.core.business.equipment.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 设备仪器
 * @author YongChen
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "EQUIPMENT", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Equip extends KekeinfoEntity<Long, Equip> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6814315579101701224L;

	@Id
	@Column(name = "EQUIPMENT_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "EQUIPMENT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	//zh:设备名称
	@Column(name="NAME")
	private String name; 
	
	//zh:设备编号
	@Column(name="ENO")
	private String eNO; 
	
	//zh:检定日期
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_TEST")
	private Date testDate;

	//zh:检定有效期
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_VAILD")
	private Date testVaild;
	
	/**
	 * -1：报废
	 * 0:检修
	 * 1:正常
	 * 2:其他项目使用中
	 */
	//zh:状态
	@Column(name = "SSTATUS")
	private int status = 1;
		
	//zh:图片资料
	@OneToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "EQUIT_ATTACH", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "EQUIPMENT_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "ATTACH_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	@JsonIgnore
	private List<Attach> attach = new ArrayList<Attach>();

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String geteNO() {
		return eNO;
	}

	public void seteNO(String eNO) {
		this.eNO = eNO;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public Date getTestVaild() {
		return testVaild;
	}

	public void setTestVaild(Date testVaild) {
		this.testVaild = testVaild;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Attach> getAttach() {
		return attach;
	}

	public void setAttach(List<Attach> attach) {
		this.attach = attach;
	}

}
