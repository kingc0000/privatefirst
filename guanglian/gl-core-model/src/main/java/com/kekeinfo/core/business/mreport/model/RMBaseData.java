package com.kekeinfo.core.business.mreport.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class RMBaseData<E> extends KekeinfoEntity<Long, RMBaseData<E>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5301346555797166739L;

	/**
	 * 
	 */
		@Embedded
		PointInfo pointInfo;

		// zh:上次高程
		@Column(name = "INITHEIGHT")
		private BigDecimal initHeight;

		// zh:本次高程
		@Column(name = "CURTHEIGHT")
		private BigDecimal curtHeight;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CALIBRATION")
		private Date calibration;
		
		@JsonIgnore
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "REPORT_ID", nullable = false)
		private RMreport report;
		
		public PointInfo getPointInfo() {
			return pointInfo;
		}

		public void setPointInfo(PointInfo pointInfo) {
			this.pointInfo = pointInfo;
		}

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

		public RMreport getReport() {
			return report;
		}

		public void setReport(RMreport report) {
			this.report = report;
		}

}
