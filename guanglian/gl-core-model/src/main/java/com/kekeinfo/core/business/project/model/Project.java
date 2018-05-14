package com.kekeinfo.core.business.project.model;

import java.util.LinkedHashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;

import com.kekeinfo.core.business.camera.model.Camera;
import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.business.zone.model.Zone;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PROJECT", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Project extends  KekeinfoEntity<Long, Project> implements Auditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1504710576382132978L;
	

	@Id
	@Column(name = "PROJECT_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "PROJECT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	 
	public Project() {
		// TODO Auto-generated constructor stub
		
	}	
	
	//项目名称
	@Column(name="NAME", length=100)
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(String projectOwner) {
		this.projectOwner = projectOwner;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getProjectOwnerid() {
		return projectOwnerid;
	}

	public void setProjectOwnerid(Long projectOwnerid) {
		this.projectOwnerid = projectOwnerid;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	//项目负责人
	@Column(name ="OWNER")
	private String projectOwner;
	//联系电话
	@Column(name = "PHONE")
	private String phone;
	
	//住址
	@Column(name="ADDRESS",length=1000)
	private String address;
	
	//负责人
	@Column(name="OWNERID")
	private Long projectOwnerid;
	
	//省
	@OneToOne(fetch = FetchType.EAGER, targetEntity = Zone.class)
	@JoinColumn(name="ZONE_ID", nullable=true, updatable=true)
	private Zone zone;
	
	//城市
	@Column(name="CITY",length=20)
	private String city;
	
	//备注
	@Column(name="MEMO", length=1000)
	private String memo;
	
	//项目类型
	/**
	 * -1:其他
	 * 0：监控
	 * 1：监护
	 */
	@Column(name="PTYPE", length=1000)
	private int ptype=-1;

	//工程特性，在用户权限中作为二级分类，如果选择该工程特性，则工程特性下的项目都拥有权限
	@Column(name="FEATURES",length=10)
	private String features;
	
	//项目概述
	@Column(name="SUMMARY")
	@Type(type = "org.hibernate.type.StringClobType")
	private String summary;
	
	//部门
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="DEPARTMENT_ID", nullable=false)
	private Department department;
	
	@JsonIgnore
	@OneToMany(mappedBy = "project")
	private Set<Camera> camera = new LinkedHashSet<Camera>();
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "cproject")
	private Set<User> cUsers = new LinkedHashSet<User>();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "wproject")
	private Set<User> wUsers = new LinkedHashSet<User>();
	/**
	 * -1: 未选择
	 * 0：半选中状态
	 * 1：选中
	 */
	@Transient
	private int sstatus=-1;
	
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}

	@Override
	public AuditSection getAuditSection() {
		// TODO Auto-generated method stub
		return auditSection;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
		// TODO Auto-generated method stub
		this.auditSection=audit;
		
	}

	public Set<Camera> getCamera() {
		return camera;
	}

	public void setCamera(Set<Camera> camera) {
		this.camera = camera;
	}

	public int getSstatus() {
		return sstatus;
	}

	public void setSstatus(int sstatus) {
		this.sstatus = sstatus;
	}

	public int getPtype() {
		return ptype;
	}

	public void setPtype(int ptype) {
		this.ptype = ptype;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public Set<User> getcUsers() {
		return cUsers;
	}

	public void setcUsers(Set<User> cUsers) {
		this.cUsers = cUsers;
	}

	public Set<User> getwUsers() {
		return wUsers;
	}

	public void setwUsers(Set<User> wUsers) {
		this.wUsers = wUsers;
	}
	
}
