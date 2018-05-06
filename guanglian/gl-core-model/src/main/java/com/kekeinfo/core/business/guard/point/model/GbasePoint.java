package com.kekeinfo.core.business.guard.point.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.guard.model.Guard;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class GbasePoint<E> extends KekeinfoEntity<Long, GbasePoint<E>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1907333826765386078L;

	// zh:点号
	@Column(name = "MARKNO")
	private String markNO;

	// zh:初始高程
	@Column(name = "INITHEIGHT")
	private BigDecimal initHeight;

	// zh:备注
	@Column(name = "MEMO", length = 1000)
	private String memo;
// zh:总累计位移
	@Transient
	private BigDecimal sumDisplacement;

	@JsonIgnore
	@ManyToOne(targetEntity = Guard.class)
	@JoinColumn(name = "GUARD_ID", nullable = false)
	private Guard guard;

	public String getMarkNO() {
		return markNO;
	}

	public void setMarkNO(String markNO) {
		this.markNO = markNO;
	}

	public BigDecimal getInitHeight() {
		return initHeight;
	}

	public void setInitHeight(BigDecimal initHeight) {
		this.initHeight = initHeight;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}


	public BigDecimal getSumDisplacement() {
		return sumDisplacement;
	}

	public void setSumDisplacement(BigDecimal sumDisplacement) {
		this.sumDisplacement = sumDisplacement;
	}

	public Guard getGuard() {
		return guard;
	}

	public void setGuard(Guard guard) {
		this.guard = guard;
	}

}
