package com.kekeinfo.core.business.job.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "GJOB", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class GJob extends KekeinfoEntity<Long, GJob> implements Auditable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4974414696713284094L;
	@Id
	@Column(name = "GJOB_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "GJOB_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	// zh:签到时间
	@Column(name = "DATE_ARRIVED",length=8)
	private String arriveDate;
	
	@Column(name = "DATE_LEAVE",length=8)
	private String leaveDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_END")
	private Date endDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_START")
	private Date startDate;
	
	/**
	 * 0:正常状态
	 * 1：已删除
	 */
	@Column(name = "RSTATUS")
	private int rstatus = 0 ;
	
	/**
	 * 0:对方未确认
	 * 1：已确认
	 */
	@Column(name = "CSTATUS")
	private Integer cstatu = new Integer(0) ;
	
	@ManyToOne(targetEntity = Guard.class)
	@JoinColumn(name = "GUARD_ID", nullable = false)
	private Guard guard;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "USER_JOB", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "GJOB_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "USER_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<User> users = new HashSet<User>();
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Guard getGuard() {
		return guard;
	}

	public void setGuard(Guard guard) {
		this.guard = guard;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getArriveDate() {
		return arriveDate;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public int getRstatus() {
		return rstatus;
	}

	public void setRstatus(int rstatus) {
		this.rstatus = rstatus;
	}

	public Integer getCstatu() {
		return cstatu;
	}

	public void setCstatu(Integer cstatu) {
		this.cstatu = cstatu;
	}


}
