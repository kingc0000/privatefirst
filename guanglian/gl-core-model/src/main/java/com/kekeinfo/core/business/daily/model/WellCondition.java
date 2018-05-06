package com.kekeinfo.core.business.daily.model;

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

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 成井统计
 * @author CHENYONG
 *
 */
@Entity
@Table(name = "WELLCONDITION", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class WellCondition extends KekeinfoEntity<Long, WellCondition>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1441422349179957870L;

	@Id
	@Column(name = "WC_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "WELLCONDITION_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	//当日井号,逗号分隔
	@Column(name="WELLIDS")
	private String wellIds; 
	
	@Transient
	private String wellnames; 
	//计划完成数
	@Column(name="PLANCMP")
	private int planCmp=0;
	
	//累积完成数
	@Column(name="CUMCMP")
	private int cumCmp=0;
	
	//设计数量
	@Column(name="DESIGNQUA")
	private int designQua=0;

	//破坏情况
	@Column(name="DESTRUCTION")
	private String dest;
	
	//备注
	@Column(name="MEMO")
	private String memo;
	
	/**
	 * 疏干井,降水井,回灌井,观测井,监测点
	 * 顺序从0开始
	 */
	
	//井的类型
	@Column(name="WTYPE")
	private int wType=0;
	
	public Daily getDaily() {
		return daily;
	}


	public void setDaily(Daily daily) {
		this.daily = daily;
	}

	@JsonIgnore
	@ManyToOne(targetEntity = Daily.class)
	@JoinColumn(name = "DAILY_ID", nullable = false)
	private Daily daily;
	
	public String getWellIds() {
		return wellIds;
	}


	public void setWellIds(String wellIds) {
		this.wellIds = wellIds;
	}


	public int getPlanCmp() {
		return planCmp;
	}


	public void setPlanCmp(int planCmp) {
		this.planCmp = planCmp;
	}


	public int getCumCmp() {
		return cumCmp;
	}


	public void setCumCmp(int cumCmp) {
		this.cumCmp = cumCmp;
	}


	public int getDesignQua() {
		return designQua;
	}


	public void setDesignQua(int designQua) {
		this.designQua = designQua;
	}

	public String getDest() {
		return dest;
	}


	public void setDest(String dest) {
		this.dest = dest;
	}


	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
	}


	public WellCondition() {
		super();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public int getwType() {
		return wType;
	}


	public void setwType(int wType) {
		this.wType = wType;
	}


	public String getWellnames() {
		return wellnames;
	}


	public void setWellnames(String wellnames) {
		this.wellnames = wellnames;
	}

}
