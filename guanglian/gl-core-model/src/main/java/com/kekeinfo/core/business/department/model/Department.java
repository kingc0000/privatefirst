package com.kekeinfo.core.business.department.model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "DEPARTMENT", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Department extends KekeinfoEntity<Long, Department>  implements Auditable{
	private static final long serialVersionUID = 7671103335743647655L;
	
	
	@Id
	@Column(name = "DEPARTMENT_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "DEPARTMENT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@NotEmpty
	@Column(name = "DEPARTMENT_NAME", nullable=false, length=100)
	private String name;
	
	@Column(name="CODE", length=10)
	private String code; //部门编码
	
	//项目负责人
	@Column(name ="OWNER")
	private String departmentOwner;
	
	//项目负责人ID
	@Column(name ="OWNERID")
	private Long departmentOwnerid;
	//联系电话
	@Column(name = "Phone")
	private String phone;
	
	//监护项目信息接收人
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="USER_ID", nullable=true)
	private User user;
	
	@Column(name = "MEMO")
	private String memo;
	
	/**
	@Column(name = "SAMEWITHCSITE")
	private boolean sameascsite=false;
	*/
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY,mappedBy = "department")
	private Set<Project> cSites = new LinkedHashSet<Project>();
	
	/**
	 * -1: 未选择
	 * 0：半选中状态
	 * 1：选中
	 * 1:也表示用户是否对该部门拥有编辑权限（前台页面逻辑处理使用）
	 */
	@Transient
	private int sstatus=-1;
	
	/**
	 * 以工程特性为key的项目集合
	 */
	@Transient
	private Map<String, FeaturesType> featuresMap = new HashMap<String, FeaturesType>(); 
	
	public Department() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}

	@Override
	public void setAuditSection(AuditSection audit) {
		auditSection = audit;
		
	}

	public int getSstatus() {
		return sstatus;
	}

	public void setSstatus(int sstatus) {
		this.sstatus = sstatus;
	}

	public String getDepartmentOwner() {
		return departmentOwner;
	}

	public void setDepartmentOwner(String departmentOwner) {
		this.departmentOwner = departmentOwner;
	}

	public Long getDepartmentOwnerid() {
		return departmentOwnerid;
	}

	public void setDepartmentOwnerid(Long departmentOwnerid) {
		this.departmentOwnerid = departmentOwnerid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<Project> getcSites() {
		return cSites;
	}

	public void setcSites(Set<Project> cSites) {
		this.cSites = cSites;
	}

	public Map<String, FeaturesType> getFeaturesMap() {
		return featuresMap;
	}

	public void setFeaturesMap(Map<String, FeaturesType> featuresMap) {
		this.featuresMap = featuresMap;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
