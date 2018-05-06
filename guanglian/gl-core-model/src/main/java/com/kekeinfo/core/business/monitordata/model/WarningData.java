package com.kekeinfo.core.business.monitordata.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class WarningData<E> extends KekeinfoEntity<Long, WarningData<E>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	/**
	 * 告警类型
	 * 0：数据采集告警
	 * 1：断电告警
	 */
	@Column(name = "WARNING_TYPE", length=1)
	private int warningType = 0;
	
	//所属抽水井
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POINT_ID", nullable=true)
	private E point;
	
	//所属工地
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CSITE_ID", nullable=true)
	private ConstructionSite cSite;
	
	public E getPoint() {
		return point;
	}
	public void setPoint(E point) {
		this.point = point;
	}
	public int getWarningType() {
		return warningType;
	}
	public void setWarningType(int warningType) {
		this.warningType = warningType;
	}
	public ConstructionSite getcSite() {
		return cSite;
	}
	public void setcSite(ConstructionSite cSite) {
		this.cSite = cSite;
	}
	public AuditSection getAuditSection() {
		return auditSection;
	}
	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}
	
}
