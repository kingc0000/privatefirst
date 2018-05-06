package com.kekeinfo.core.business.mreport.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Embeddable
@DynamicInsert(true)
@DynamicUpdate(true)
public class PointInfo implements Serializable {

	private static final long serialVersionUID = -1934446958975060889L;

	// zh:初始高程
	@Column(name = "FHEIGHT")
	private BigDecimal fHeight;
	
	// zh:开挖前累计
	@Column(name = "SHEIGHT")
	private BigDecimal sHeight;
	
	// zh:点号
	@Column(name = "MARKNO")
	private String markNO;
	
	// zh:备注
	@Column(name = "MEMO" , length = 1000)
	private String memo;
	
	// zh:仪器名称
	@Column(name = "ENAME", length=100)
	private String eName ;

	public BigDecimal getfHeight() {
		return fHeight;
	}

	public void setfHeight(BigDecimal fHeight) {
		this.fHeight = fHeight;
	}

	public String getMarkNO() {
		return markNO;
	}

	public void setMarkNO(String markNO) {
		this.markNO = markNO;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String geteNameid() {
		return eNameid;
	}

	public void seteNameid(String eNameid) {
		this.eNameid = eNameid;
	}

	// zh:仪器编号
	@Column(name = "ENO", length=100)
	private String eNameid ;

	public BigDecimal getsHeight() {
		return sHeight;
	}

	public void setsHeight(BigDecimal sHeight) {
		this.sHeight = sHeight;
	}
}
