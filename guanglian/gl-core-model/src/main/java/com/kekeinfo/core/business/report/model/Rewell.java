package com.kekeinfo.core.business.report.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
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
@Table(name = "REWELL", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Rewell extends KekeinfoEntity<Long, Rewell> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6131993541879544824L;

	@Id
	@Column(name = "REWELL_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "REWELL_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "NAME", length=100)
	private String name ;
	
	@Column(name = "DATA", columnDefinition = "decimal(19,3)")
	private BigDecimal data = new BigDecimal(0); //数据
	
	@Column(name = "LASTDATA", columnDefinition = "decimal(19,3)")
	private BigDecimal lastData = new BigDecimal(0); //数据
	
	@Column(name = "THRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal threshold = new BigDecimal(0); //阈值
	
	@Column(name = "DATA_STATUS")
	private int dataStatus = 0;
	
	@Column(name = "POWER_STATUS")
	private int powerStatus = 0;
	
	//所属报表
	@JsonIgnore
	@ManyToOne(targetEntity = Report.class,fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	@JoinColumn(name="REPORT_ID", nullable=false)
	private Report report;
	

	public Rewell() {
		super();
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

	public int getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(int dataStatus) {
		this.dataStatus = dataStatus;
	}

	public int getPowerStatus() {
		return powerStatus;
	}

	public void setPowerStatus(int powerStatus) {
		this.powerStatus = powerStatus;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public BigDecimal getData() {
		return data;
	}

	public void setData(BigDecimal data) {
		this.data = data;
	}

	public BigDecimal getLastData() {
		return lastData;
	}

	public void setLastData(BigDecimal lastData) {
		this.lastData = lastData;
	}

	public BigDecimal getThreshold() {
		return threshold;
	}

	public void setThreshold(BigDecimal threshold) {
		this.threshold = threshold;
	}

	
}
