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
@Table(name = "ROWELL", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Rowell extends KekeinfoEntity<Long, Rowell> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6131993541879544824L;

	@Id
	@Column(name = "ROWELL_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ROWELL_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "NAME", length=100)
	private String name ;
	
	@Column(name = "WATER", columnDefinition = "decimal(19,3)")
	private BigDecimal water = new BigDecimal(0); //水位
	
	@Column(name = "WATERTHRESHOLD", columnDefinition = "decimal(19,3)")
	private BigDecimal waterThreshold = new BigDecimal(0); //阈值
	
	@Column(name = "WATERDOWN", columnDefinition = "decimal(19,3)")
	private BigDecimal waterDwon = new BigDecimal(0); //水位阈值下限
	
	@Column(name = "TEMPERATURE", columnDefinition = "decimal(19,3)")
	private BigDecimal temperature = new BigDecimal(0); //水温
	
	//降水目的层
	@Column(name="PRECIPITATION")
	private String precipitation ;
	
	@Column(name = "DATA_STATUS")
	private int dataStatus = 0;
	
	@Column(name = "POWER_STATUS")
	private int powerStatus = 0;
	
	//所属报表
	@JsonIgnore
	@ManyToOne(targetEntity = Report.class,fetch = FetchType.EAGER,cascade = {CascadeType.ALL})
	@JoinColumn(name="REPORT_ID", nullable=false)
	private Report report;
	

	public Rowell() {
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

	public BigDecimal getWater() {
		return water;
	}

	public void setWater(BigDecimal water) {
		this.water = water;
	}

	public String getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
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

	public BigDecimal getWaterThreshold() {
		return waterThreshold;
	}

	public void setWaterThreshold(BigDecimal waterThreshold) {
		this.waterThreshold = waterThreshold;
	}

	public BigDecimal getWaterDwon() {
		return waterDwon;
	}

	public void setWaterDwon(BigDecimal waterDwon) {
		this.waterDwon = waterDwon;
	}

	public BigDecimal getTemperature() {
		return temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}

	
}
