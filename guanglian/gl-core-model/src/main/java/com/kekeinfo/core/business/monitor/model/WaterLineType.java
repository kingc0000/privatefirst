package com.kekeinfo.core.business.monitor.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "WATERLINETYPE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class WaterLineType extends  KekeinfoEntity<Long, WaterLineType>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3527205924052041414L;

	/**
	 * 
	 */
	
	

	@Id
	@Column(name = "WATERLINETYPE_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "WATERLINETYPE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	 
	public WaterLineType() {
		// TODO Auto-generated constructor stub
		
	}	
	
	// zh:日变量
	@Column(name = "DAILYVAR")
	private BigDecimal dailyVar;
    
	//累计值
	@Column(name = "TOTALVALUE")
	private BigDecimal totalValue;
	
	//类别
	@JsonIgnore
	@ManyToOne(targetEntity = BasedataType.class)
	@JoinColumn(name="MTYPE_ID", nullable=false)
	private BasedataType baseType;
	
	@Transient
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//zh:类别
	@Column(name="MEMO")
	private String memo;
		
	@JsonIgnore
	@ManyToOne(targetEntity = Monitor.class)
	@JoinColumn(name = "MONITOR_ID", nullable = false)
	private Monitor monitor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getDailyVar() {
		return dailyVar;
	}

	public void setDailyVar(BigDecimal dailyVar) {
		this.dailyVar = dailyVar;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public BasedataType getBaseType() {
		return baseType;
	}

	public void setBaseType(BasedataType baseType) {
		this.baseType = baseType;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
