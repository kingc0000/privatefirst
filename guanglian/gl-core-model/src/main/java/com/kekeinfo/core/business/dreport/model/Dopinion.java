package com.kekeinfo.core.business.dreport.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "DREPORT_OPINION", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Dopinion extends KekeinfoEntity<Long, Dopinion> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2040199256458575682L;

	@Id
	@Column(name = "DREPORT_OPINION_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DREPORT_OPINION_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	//意见类型，即编制流程节点，审核节点1，审定节点2
	@Column(name = "AUDIT_TYPE")
	private int auditType=1;
	
	//处理结果，不通过0，通过1，待处理-1
	@Column(name = "RESULT")
	private int result=-1;
	
	@ManyToOne(targetEntity = Dreport.class)
	@JoinColumn(name = "DREPORT_ID", nullable = false)
	private Dreport dreport;
	
	@Column(name="NOTE", length=1024)
	private String note; //意见内容
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name="DEALER_ID", nullable=true, updatable=true)
	private User dealer;//结果处理人
	
	@ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "DREPORT_OPINION_USER", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "DREPORT_OPINION_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "USER_ID", 
					nullable = false, updatable = false) }
	)
	private Set<User> users = new HashSet<User>(); //分配的审批人集合
	
	//private MultiPartFile image

	public Dopinion(){
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

	public int getAuditType() {
		return auditType;
	}

	public void setAuditType(int auditType) {
		this.auditType = auditType;
	}

	public Dreport getDreport() {
		return dreport;
	}

	public void setDreport(Dreport dreport) {
		this.dreport = dreport;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public User getDealer() {
		return dealer;
	}

	public void setDealer(User dealer) {
		this.dealer = dealer;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	
}
