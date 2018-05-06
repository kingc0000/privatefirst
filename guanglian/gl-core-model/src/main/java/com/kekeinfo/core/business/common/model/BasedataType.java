package com.kekeinfo.core.business.common.model;


import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "BASEDATA_TYPE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class BasedataType extends KekeinfoEntity<Long, BasedataType> implements Auditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3546828765081794569L;

	@Id
	@Column(name = "BASEDATA_TYPE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_BASETYPE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
		
	@Column(name="NAME", length=255)
	private String name;
	
	@Column(name="SORT_ORDER")
	private Integer order = new Integer(0);
	
	@Column(name="TYPE", length=100)
	private String type; //定义基础数据类型，
	
	@Column(name="VALUE", length=255)
	private String value; //数值
	
	/**
	 * system =true 不允许删除
	 */
	@Column(name="SYS")
	private boolean sys =false; //数值
	
	@Embedded
	private AuditSection auditSection = new AuditSection();

	
	public BasedataType() {
		super();
	}

	public BasedataType( String name, Integer order, String type, String value) {
		super();
		this.name = name;
		this.order = order;
		this.type = type;
		this.value = value;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public boolean isSys() {
		return sys;
	}

	public void setSys(boolean sys) {
		this.sys = sys;
	}

}
