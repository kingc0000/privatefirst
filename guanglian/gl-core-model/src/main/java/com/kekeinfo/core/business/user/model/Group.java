package com.kekeinfo.core.business.user.model;

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
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.NotEmpty;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "SM_GROUP", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Group extends KekeinfoEntity<Integer, Group> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3786127652573709701L;
	@Id
	@Column(name = "GROUP_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "GROUP_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Integer id;
	
	public Group() {
		
	}
	
	
	@NotEmpty
	@Column(name="GROUP_NAME", unique=true)
	private String groupName;
	
	@Column(name="NAME", unique=true)
	private String name;
	
	@Column(name="ISAYSTEM")
	private boolean sGroup =false;
	
	public Group(String groupName) {
		this.groupName = groupName;
	}
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PERMISSION_GROUP", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "GROUP_ID", nullable = false, updatable = true) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "PERMISSION_ID", 
					nullable = false, updatable = true) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<Permission> permissions =  new HashSet<Permission>();
	
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	/**
	 * 0:不需要关联项目
	 * 1:需要关联项目
	 */
	@Column(name="GROUP_TYPE")
	private int grouptype=0;
	
	
	@Override
	public AuditSection getAuditSection() {
		return this.auditSection;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
			this.auditSection = audit;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGrouptype() {
		return grouptype;
	}

	public void setGrouptype(int grouptype) {
		this.grouptype = grouptype;
	}

	public boolean issGroup() {
		return sGroup;
	}

	public void setsGroup(boolean sGroup) {
		this.sGroup = sGroup;
	}	

}
