package com.kekeinfo.core.business.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.department.model.DepartmentNode;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "USER", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class User extends KekeinfoEntity<Long, User> implements Auditable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -782107329756980608L;

	@Id
	@Column(name = "USER_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "USER_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	public User() {
		this.active=true;
		
	}
	
	public User(String userName,String password, String email) {
		
		this.adminName = userName;
		this.adminPassword = password;
		this.adminEmail = email;
	}
	
	@NotEmpty
	@Column(name="ADMIN_NAME", length=100, unique=true)
	private String adminName;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "USER_GROUP", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "USER_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "GROUP_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private List<Group> groups = new ArrayList<Group>();
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY, mappedBy = "user")
	private Set<DepartmentNode> pNodes = new LinkedHashSet<DepartmentNode>();
	
	@NotEmpty
	@Email
	@Column(name="ADMIN_EMAIL")
	private String adminEmail;
	
	
	@Column(name="ADMIN_PASSWORD", length=50)
	private String adminPassword;
	
	
	@Column(name="ADMIN_FIRST_NAME")
	private String firstName;
	
	@Column(name="TELEPHONE", length=50)
	private String telephone;
	
	@Column(name="ACTIVE")
	private boolean active = true;
	
	@Column(name="HEAD")
	private String head;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_ACCESS")
	private Date lastAccess;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGIN_ACCESS")
	private Date loginTime;
	
	/**
	 * 入职时间
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ENTER_TIME")
	private Date enterEnter;
	
	/**
	 * 保存临时信息
	 */
	@Column(name="TEMP")
	private String temp;
	
	@Column(name="UAGENT")
	private String uAgent = null;
	
	/**
	 * ios
	 * andord
	 */
	@Column(name="APPTYPE")
	private String atype = null;
	
	@Column(name="DEVICE_TOKEN")
	private String device_token;
	
	
	//部门评论负责人
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name="CPROJECT_ID", nullable=true)
	private Project cproject;
	
	//告警评论负责人
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name="WPROJECT_ID", nullable=true)
	private Project wproject;
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
		auditSection = audit;
		
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	public Date getLastAccess() {
		return lastAccess;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public Set<DepartmentNode> getpNodes() {
		return pNodes;
	}

	public void setpNodes(Set<DepartmentNode> pNodes) {
		this.pNodes = pNodes;
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	public String getuAgent() {
		return uAgent;
	}

	public void setuAgent(String uAgent) {
		this.uAgent = uAgent;
	}

	public Date getEnterEnter() {
		return enterEnter;
	}

	public void setEnterEnter(Date enterEnter) {
		this.enterEnter = enterEnter;
	}

	public Project getCproject() {
		return cproject;
	}

	public void setCproject(Project cproject) {
		this.cproject = cproject;
	}

	public Project getWproject() {
		return wproject;
	}

	public void setWproject(Project wproject) {
		this.wproject = wproject;
	}

	public String getAtype() {
		return atype;
	}

	public void setAtype(String atype) {
		this.atype = atype;
	}
}
