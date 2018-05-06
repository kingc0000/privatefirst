package com.kekeinfo.core.business.monitor.data.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MDbasePoint<E> extends KekeinfoEntity<Long, MDbasePoint<E>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6599778725229354644L;

	// zh:上次高程
	@Column(name = "INITHEIGHT")
	private BigDecimal initHeight=new BigDecimal(0.00);

	// zh:本次高程
	@Column(name = "CURTHEIGHT")
	private BigDecimal curtHeight;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CALIBRATION")
	private Date calibration;
	
	// zh:设备名称
	@Column(name = "EQIPNAME")
	private String eName;
	
	// zh:设备编号
	@Column(name = "EQIPNO")
	private String eNo;
	
	public BigDecimal getInitHeight() {
		return initHeight;
	}

	public void setInitHeight(BigDecimal initHeight) {
		this.initHeight = initHeight;
	}

	public BigDecimal getCurtHeight() {
		return curtHeight;
	}

	public void setCurtHeight(BigDecimal curtHeight) {
		this.curtHeight = curtHeight;
	}

	public Date getCalibration() {
		return calibration;
	}

	public void setCalibration(Date calibration) {
		this.calibration = calibration;
	}

}
