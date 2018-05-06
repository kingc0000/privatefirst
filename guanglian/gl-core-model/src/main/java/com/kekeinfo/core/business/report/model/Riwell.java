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
@Table(name = "RIWELL", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Riwell extends KekeinfoEntity<Long, Riwell> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6131993541879544824L;

	@Id
	@Column(name = "RIWELL_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "RIWELL_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "NAME", length=100)
	private String name ;
	
	//上次累计量
	@Column(name = "LASTACCU", columnDefinition = "decimal(19,3)")
	private BigDecimal lastAccu = new BigDecimal(0); 
	
	//本次累计值
	@Column(name = "THISACCU", columnDefinition = "decimal(19,3)")
	private BigDecimal thisAccu = new BigDecimal(0);

	@Column(name = "FLOW", columnDefinition = "decimal(19,3)")
	private BigDecimal flow = new BigDecimal(0); //流量
	
	@Column(name = "PRESSURE", columnDefinition = "decimal(19,3)")
	private BigDecimal pressure = new BigDecimal(0); //井内水位
	
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
	

	public Riwell() {
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

	public BigDecimal getLastAccu() {
		return lastAccu;
	}

	public void setLastAccu(BigDecimal lastAccu) {
		this.lastAccu = lastAccu;
	}

	public BigDecimal getThisAccu() {
		return thisAccu;
	}

	public void setThisAccu(BigDecimal thisAccu) {
		this.thisAccu = thisAccu;
	}

	public BigDecimal getFlow() {
		return flow;
	}

	public void setFlow(BigDecimal flow) {
		this.flow = flow;
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

	public BigDecimal getPressure() {
		return pressure;
	}

	public void setPressure(BigDecimal pressure) {
		this.pressure = pressure;
	}

	
}
