package com.kekeinfo.core.business.monitor.statistical.model;

import java.math.BigDecimal;

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

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "POINTMANAGER", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class PointManager extends KekeinfoEntity<Long, PointManager> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2401994790003157091L;

	@Id
	@Column(name = "POINTMANAGER_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "POINTMANAGER_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	// zh:本次最大变化量点号
	@Column(name = "POINTNO")
	private String pointNo;

	// zh:总累计最大变化量点号
	@Column(name = "TPOINTNO")
	private String tpointNo;

	// zh:开挖前累计最大变化量点号
	@Column(name = "EPOINTNO")
	private String epointNo;

	// zh:日变量
	@Column(name = "DAILYVAR")
	private BigDecimal dailyVar;

	@Column(name = "TOTALVAlUE")
	private BigDecimal totalValue;

	// zh:本次最大变化量
	@Column(name = "CURMAXVAR")
	private BigDecimal curMaxVar;

	// zh:总累计最大变化量
	@Column(name = "TOTALMAXVAR")
	private BigDecimal totalMaxVar;

	// zh:开挖前累计最大变化量
	@Column(name = "EARLYMAXVAR")
	private BigDecimal earlyMaxVar;

	// zh:测点类型
	@Column(name = "PTYPE")
	private String pType;
	
	// zh:测点类型
	@Column(name = "MEMO")
	private String memo;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MSTATISTICAL_ID", nullable = true)
	private Mstatistical mstatistical;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getPointNo() {
		return pointNo;
	}

	public void setPointNo(String pointNo) {
		this.pointNo = pointNo;
	}

	public String getTpointNo() {
		return tpointNo;
	}

	public void setTpointNo(String tpointNo) {
		this.tpointNo = tpointNo;
	}

	public String getEpointNo() {
		return epointNo;
	}

	public void setEpointNo(String epointNo) {
		this.epointNo = epointNo;
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

	public BigDecimal getTotalMaxVar() {
		return totalMaxVar;
	}

	public void setTotalMaxVar(BigDecimal totalMaxVar) {
		this.totalMaxVar = totalMaxVar;
	}

	public BigDecimal getEarlyMaxVar() {
		return earlyMaxVar;
	}

	public void setEarlyMaxVar(BigDecimal earlyMaxVar) {
		this.earlyMaxVar = earlyMaxVar;
	}

	public String getpType() {
		return pType;
	}

	public void setpType(String pType) {
		this.pType = pType;
	}

	public BigDecimal getCurMaxVar() {
		return curMaxVar;
	}

	public void setCurMaxVar(BigDecimal curMaxVar) {
		this.curMaxVar = curMaxVar;
	}

	public Mstatistical getMstatistical() {
		return mstatistical;
	}

	public void setMstatistical(Mstatistical mstatistical) {
		this.mstatistical = mstatistical;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
