package com.kekeinfo.core.business.dreport.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "AUDIT_RULE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class AuditRule extends KekeinfoEntity<Long, AuditRule> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9044916752505410663L;

	@Id
	@Column(name = "AUDIT_RULE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "AUDIT_RULE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	//意见类型，即编制流程节点，审核节点1，审定节点2
	@Column(name = "AUDIT_TYPE")
	private int auditType=1;
	
	
	@Column(name="PROJECT_RANK", length=10)
	private String rank; //项目等级
	
	@Column(name="NAME", length=50)
	private String name; //规则名称
	
	@ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "AUDIT_USER", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "AUDIT_RULE_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "USER_ID", 
					nullable = false, updatable = false) }
	)
	private Set<User> users = new HashSet<User>();

	public AuditRule(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAuditType() {
		return auditType;
	}

	public void setAuditType(int auditType) {
		this.auditType = auditType;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
}
