package com.kekeinfo.core.business.point.model;

import java.io.InputStream;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.generic.model.PointEnumType;

/**
 * 项目各个测点基础抽象类
 * 抽水井：流量、水位，疏干井：流量、水位，观测井：水位、水温，回灌井：流量、井内水位，环境监测：监测值
 * @author sam
 *
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Basepoint<M extends BasepointLink<?>, N extends BasepointInfo<?>> extends KekeinfoEntity<Long, Basepoint<M, N>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4605985735093002944L;

	
	@Column(name="name", length=255)
	private String name; 
	
	//数据采集状态，流量1、水位2，疏干井：流量1、水位2，观测井：水位1、水温2，回灌井：流量1、井内水位2，环境监测：监测值1，全告警3    封井4
	@Column(name = "DATA_STATUS", length=1)
	private Integer dataStatus = 0; //默认正常
	
	//设备状态（正常0、关闭1，故障2（即断电））
	@Column(name = "POWER_STATUS", length=1)
	private Integer powerStatus = 0;
	
	//自动状态状（人工0、自动：根据深度1，根据时间2（即断电））
	@Column(name = "AUTO_STATUS", length=1)
	private Integer autoStatus = 0;
	
	/**
	 * 自动开启深度
	 */
	@Column(name = "OPENDEEP", columnDefinition = "decimal(19,3)")
	private BigDecimal openDepp ; 
	
	/**
	 * 自动关闭深度
	 */
	@Column(name = "CLOSEDEEP", columnDefinition = "decimal(19,3)")
	private BigDecimal closeDepp ; //经度
	
	/**
	 * 抽水持续时间
	 */
	@Column(name = "CONTIONUEMIN")
	private Long conMin ; //经度
	
	/**
	 * 抽水间隔时间
	 */
	@Column(name = "SPACEMIN")
	private Long spaceMin ; //经度
	
	/**
	 * 抽水持续时间
	 */
	@Transient
	private long continMin =0; //经度
	
	/**
	 * 自动开启对象ID
	 */
	@Column(name = "AUTOID")
	private Long autoID;
	
	//zh:类别
	@Column(name="ATYPE")
	@Enumerated(value = EnumType.STRING)
	private PointEnumType atype;
	
	@Column(name = "LONGITUDE", columnDefinition = "decimal(19,6)")
	private BigDecimal longitude = new BigDecimal(0); //经度
	
	@Column(name = "LATITUDE", columnDefinition = "decimal(19,6)")
	private BigDecimal latitude = new BigDecimal(0); //纬度
	
	//是否成井
	@Column(name="DONE")
	private boolean done=false;
	
	//该测点是否在地图可见
	@Column(name="VISIBLE")
	private boolean visible = false;
	
	//测点同设备网关节点映射定义
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name="POINTLINK_ID", nullable=true)
	private M pointLink;
	
	//zh:二维码
	@Column(name = "QCODE")
	private String qCode;
	
	@Transient
	private InputStream digital = null;
	
	//测点扩展信息
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name="POINTINFO_ID", nullable=true)
	private N pointInfo;

	public M getPointLink() {
		return pointLink;
	}

	public void setPointLink(M pointLink) {
		this.pointLink = pointLink;
	}

	public N getPointInfo() {
		return pointInfo;
	}

	public void setPointInfo(N pointInfo) {
		this.pointInfo = pointInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Integer getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
	}

	public Integer getPowerStatus() {
		return powerStatus;
	}

	public void setPowerStatus(Integer powerStatus) {
		this.powerStatus = powerStatus;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getqCode() {
		return qCode;
	}

	public void setqCode(String qCode) {
		this.qCode = qCode;
	}


	public InputStream getDigital() {
		return digital;
	}

	public void setDigital(InputStream digital) {
		this.digital = digital;
	}

	public Integer getAutoStatus() {
		return autoStatus;
	}

	public void setAutoStatus(Integer autoStatus) {
		this.autoStatus = autoStatus;
	}

	public BigDecimal getOpenDepp() {
		return openDepp;
	}

	public void setOpenDepp(BigDecimal openDepp) {
		this.openDepp = openDepp;
	}

	public BigDecimal getCloseDepp() {
		return closeDepp;
	}

	public void setCloseDepp(BigDecimal closeDepp) {
		this.closeDepp = closeDepp;
	}

	public Long getConMin() {
		return conMin;
	}

	public void setConMin(Long conMin) {
		this.conMin = conMin;
	}

	public Long getSpaceMin() {
		return spaceMin;
	}

	public void setSpaceMin(Long spaceMin) {
		this.spaceMin = spaceMin;
	}

	public long getContinMin() {
		return continMin;
	}

	public void setContinMin(long continMin) {
		this.continMin = continMin;
	}

	public Long getAutoID() {
		return autoID;
	}

	public void setAutoID(Long autoID) {
		this.autoID = autoID;
	}

	public PointEnumType getAtype() {
		return atype;
	}

	public void setAtype(PointEnumType atype) {
		this.atype = atype;
	}
	
}
