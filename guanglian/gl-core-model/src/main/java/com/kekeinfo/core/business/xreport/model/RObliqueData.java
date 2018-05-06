package com.kekeinfo.core.business.xreport.model;

import java.math.BigDecimal;
import java.util.Date;

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
@Table(name = "ROBLIQUEDATA", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class RObliqueData extends KekeinfoEntity<Long, RObliqueData> {

	private static final long serialVersionUID = -8231506062099735443L;
	@Id
	@Column(name = "ROBLIQUEDATA_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ROBLIQUEDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	// zh:上次累计
	@Column(name = "DEPTH", length = 100)
	private BigDecimal depth;
	
	// zh:上次累计
	@Column(name = "LASTTOTAL", length = 100)
	private BigDecimal lastTotal;

	// zh:本次累计
	@Column(name = "CURTOTAL", length = 100)
	private BigDecimal curTotal;

	// 本次时间
	@Column(name = "CURDATE")
	private Date curDate;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROBLIQUE_ID", nullable = true)
	private ROblique rObliqu;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLastTotal() {
		return lastTotal;
	}

	public void setLastTotal(BigDecimal lastTotal) {
		this.lastTotal = lastTotal;
	}

	public BigDecimal getCurTotal() {
		return curTotal;
	}

	public void setCurTotal(BigDecimal curTotal) {
		this.curTotal = curTotal;
	}

	public BigDecimal getDepth() {
		return depth;
	}

	public void setDepth(BigDecimal depth) {
		this.depth = depth;
	}

	public ROblique getrObliqu() {
		return rObliqu;
	}

	public void setrObliqu(ROblique rObliqu) {
		this.rObliqu = rObliqu;
	}

	public Date getCurDate() {
		return curDate;
	}

	public void setCurDate(Date curDate) {
		this.curDate = curDate;
	}


}
