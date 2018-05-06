package com.kekeinfo.core.business.mreport.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "RHIDDENLINEDATA", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class RHiddenLineData extends RMBaseData<RHiddenLineData>{

	private static final long serialVersionUID = -8231506062099735443L;
	@Id
	@Column(name = "RHIDDENLINEDATA_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "HIDDENLINEDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	// zh:管口高程
	@Column(name = "LINEGAOCHEN")
	private BigDecimal lineGaoChen;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLineGaoChen() {
		return lineGaoChen;
	}

	public void setLineGaoChen(BigDecimal lineGaoChen) {
		this.lineGaoChen = lineGaoChen;
	}

	
}
