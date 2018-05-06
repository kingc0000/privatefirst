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
@Table(name = "RSUPAXIALDATA", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class RSupAxialData extends RMBaseData<RSupAxialData> {

	private static final long serialVersionUID = -8231506062099735443L;
	@Id
	@Column(name = "RSUPAXIALDATA_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SUPAXIALDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	// zh:报警值
	@Column(name = "ALARM")
	private BigDecimal alarmV;
	
	// zh:支撑类型
	@Column(name = "ZHICHENG", length = 20)
	private String zhiCheng;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAlarmV() {
		return alarmV;
	}

	public void setAlarmV(BigDecimal alarmV) {
		this.alarmV = alarmV;
	}

	public String getZhiCheng() {
		return zhiCheng;
	}

	public void setZhiCheng(String zhiCheng) {
		this.zhiCheng = zhiCheng;
	}

	
}
