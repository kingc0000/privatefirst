package com.kekeinfo.core.business.daily.model;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 监测日志管理
 * 
 * @author yongchen
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "GUARD_DAILY", schema = SchemaConstant.KEKEINFO_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = {
		"DATE_C", "GUARD_ID" }))
public class GuardDaily extends KekeinfoEntity<Long, GuardDaily> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 931906270212892102L;

	@Id
	@Column(name = "GUARD_DAILY_ID", nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "GUARD_DAILY_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	//zh:日志日期
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_C")
	private Date datec;

	//zh:用户id
	@Column(name = "USER_ID")
	private Long userid;
	
	//zh:现场监护意见
	@Column(name = "POINT_DESC", length = 1000)
	private String pointDesc;
	
	//zh:地铁结构病害调查报表
	@Column(name = "WEIHWGXT", length = 1000)
	private String weiHuW="/";

	//zh:总结
	@Column(name = "CONCLUSION", length = 1000)
	private String conclusion;
	
	//zh:所属项目
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GUARD_ID", nullable = true)
	private Guard guard;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "guardDaily")
	private Set<GuardDailyImage> guardDailyImages = new HashSet<GuardDailyImage>();

	public GuardDaily() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public Date getDatec() {
		return datec;
	}

	public String getPointDesc() {
		return pointDesc;
	}

	public void setPointDesc(String pointDesc) {
		this.pointDesc = pointDesc;
	}

	public String getWeiHuW() {
		return weiHuW;
	}

	public void setWeiHuW(String weiHuW) {
		this.weiHuW = weiHuW;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public Guard getGuard() {
		return guard;
	}

	public void setGuard(Guard guard) {
		this.guard = guard;
	}

	public Set<GuardDailyImage> getGuardDailyImages() {
		return guardDailyImages;
	}

	public void setGuardDailyImages(Set<GuardDailyImage> guardDailyImages) {
		this.guardDailyImages = guardDailyImages;
	}

	public void setDatec(Date datec) {
		this.datec = datec;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	

}
