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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 工程日志管理
 * 
 * @author sam
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "DAILY", schema = SchemaConstant.KEKEINFO_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = {
		"DATE_C", "CSITE_ID" }))
public class Daily extends KekeinfoEntity<Long, Daily> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1441422349179957870L;

	@Id
	@Column(name = "DAILY_ID", nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DAILY_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_C")
	private Date datec;

	// 天气
	@Column(name = "WEATHER", length = 255)
	private String weather;

	// 用户id
	@Column(name = "USER_ID")
	private Long userId;

	// 开挖工况
	@Column(name = "EXCAVATION", length = 1000)
	private String excavation;

	// 梳干运行工况
	@Column(name = "COMBDRY", length = 1000)
	private String combDry;

	// 降压运行工况
	@Column(name = "STEPDOWN", length = 1000)
	private String stepDown;

	// 回灌运行工况
	@Column(name = "RISK", length = 1000)
	private String risk;

	// 安全问题
	@Column(name = "SAFE", length = 1000)
	private String safe;

	// 回灌运行工况
	@Column(name = "RECHARGE", length = 1000)
	private String recharge;

	// 质量问题
	@Column(name = "QUALITY", length = 1000)
	private String quality;

	// 今日主要工作
	@Column(name = "TODAYWORK", length = 1000)
	private String todayWork;

	// 本周主要工作
	@Column(name = "THISWEEK", length = 1000)
	private String thisWeek;

	// 下周主要工作
	@Column(name = "NEXTWEEK", length = 1000)
	private String nextWeek;

	// 总结
	@Column(name = "CONCLUSION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String conclusion;

	@Transient
	private String[] wType; // 类型

	@Transient
	private String[] wellIds; // 类型

	@Transient
	private String[] planCmp; // 类型

	@Transient
	private String[] cumCmp; // 类型

	@Transient
	private String[] designQua; // 类型

	@Transient
	private String[] dest; // 类型

	@Transient
	private String[] memo; // 类型
	// 所属工地
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CSITE_ID", nullable = true)
	private ConstructionSite cSite;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "daily")
	private Set<DailyImage> dailyImages = new HashSet<DailyImage>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "daily")
	@OrderBy("wType asc")
	private Set<WellCondition> wellCon = new HashSet<WellCondition>();

	public Daily() {
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

	public ConstructionSite getcSite() {
		return cSite;
	}

	public void setcSite(ConstructionSite cSite) {
		this.cSite = cSite;
	}

	public Set<DailyImage> getDailyImages() {
		return dailyImages;
	}

	public void setDailyImages(Set<DailyImage> dailyImages) {
		this.dailyImages = dailyImages;
	}

	public Date getDatec() {
		return datec;
	}

	public void setDatec(Date datec) {
		this.datec = datec;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getExcavation() {
		return excavation;
	}

	public void setExcavation(String excavation) {
		this.excavation = excavation;
	}

	public String getCombDry() {
		return combDry;
	}

	public void setCombDry(String combDry) {
		this.combDry = combDry;
	}

	public String getStepDown() {
		return stepDown;
	}

	public void setStepDown(String stepDown) {
		this.stepDown = stepDown;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getSafe() {
		return safe;
	}

	public void setSafe(String safe) {
		this.safe = safe;
	}

	public String getRecharge() {
		return recharge;
	}

	public void setRecharge(String recharge) {
		this.recharge = recharge;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getTodayWork() {
		return todayWork;
	}

	public void setTodayWork(String todayWork) {
		this.todayWork = todayWork;
	}

	public String getThisWeek() {
		return thisWeek;
	}

	public void setThisWeek(String thisWeek) {
		this.thisWeek = thisWeek;
	}

	public String getNextWeek() {
		return nextWeek;
	}

	public void setNextWeek(String nextWeek) {
		this.nextWeek = nextWeek;
	}

	public Set<WellCondition> getWellCon() {
		return wellCon;
	}

	public void setWellCon(Set<WellCondition> wellCon) {
		this.wellCon = wellCon;
	}

	public String[] getwType() {
		return wType;
	}

	public void setwType(String[] wType) {
		this.wType = wType;
	}

	public String[] getWellIds() {
		return wellIds;
	}

	public void setWellIds(String[] wellIds) {
		this.wellIds = wellIds;
	}

	public String[] getPlanCmp() {
		return planCmp;
	}

	public void setPlanCmp(String[] planCmp) {
		this.planCmp = planCmp;
	}

	public String[] getCumCmp() {
		return cumCmp;
	}

	public void setCumCmp(String[] cumCmp) {
		this.cumCmp = cumCmp;
	}

	public String[] getDesignQua() {
		return designQua;
	}

	public void setDesignQua(String[] designQua) {
		this.designQua = designQua;
	}

	public String[] getDest() {
		return dest;
	}

	public void setDest(String[] dest) {
		this.dest = dest;
	}

	public String[] getMemo() {
		return memo;
	}

	public void setMemo(String[] memo) {
		this.memo = memo;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

}
