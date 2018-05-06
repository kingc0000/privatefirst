package com.kekeinfo.core.business.department.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;



@Entity
@Table(name = "PNODE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class DepartmentNode extends KekeinfoEntity<Long, DepartmentNode>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2948432837814401313L;
	@Id
	@Column(name = "PNODE_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "PNODE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	/**
	 * 权限组id,groupid 
	 */
	@Column(name = "GROUP_ID")
	private int groupid=-1;
	
	/**
	 * 部门id或者是项目id，如果权限组类型type为 csite，则对应项目id，如果权限组类型type为department或者features，对应部门id
	 */
	@Column(name = "DEPARTMENT_ID")
	private Long departmentid;
	
	/**
	 * 全选或者半选
	 */
	@Column(name = "isALL")
	private boolean isAll=false;
	
	//工程特性，在用户权限中作为二级分类，如果选择该工程特性，则工程特性下的项目都拥有权限
	@Column(name="FEATURES",length=10)
	private String features;
	
	//项目
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="USER_ID", nullable=false)
	private User user;
	/**
	 * 权限组类型
	 * csite 项目
	 * department 部门 
	 * features 工程特性
	 * all 则表示选中所有权限组
	 */
	@Column(name = "DEPARTMENT_TYPE")
	private String type;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(Long departmentid) {
		this.departmentid = departmentid;
	}
	public boolean isAll() {
		return isAll;
	}
	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	
}
