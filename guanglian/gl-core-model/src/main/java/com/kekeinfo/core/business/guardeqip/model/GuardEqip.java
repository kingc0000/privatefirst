package com.kekeinfo.core.business.guardeqip.model;

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
import com.kekeinfo.core.business.equipment.model.Equip;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 设备仪器
 * @author YongChen
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "GUARDQUIP", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class GuardEqip extends KekeinfoEntity<Long, GuardEqip> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6814315579101701224L;

	@Id
	@Column(name = "GEQUIP_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "GEQUIP_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	//zh:进场日期
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_TEST")
	private Date entryDate;

	//zh:离厂日期
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_VAILD")
	private Date exitVaild;
	
	//所属项目
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="GUARD_ID", nullable=true)
	private Guard guard;
	
	//所属设备
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="EQUIPMENT_ID", nullable=true)
	private Equip equip;
	
	//zh:备注
	@Column(name = "MEMO")
	private String memo;
		

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

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getExitVaild() {
		return exitVaild;
	}

	public void setExitVaild(Date exitVaild) {
		this.exitVaild = exitVaild;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Equip getEquip() {
		return equip;
	}

	public void setEquip(Equip equip) {
		this.equip = equip;
	}

	public Guard getGuard() {
		return guard;
	}

	public void setGuard(Guard guard) {
		this.guard = guard;
	}

}
