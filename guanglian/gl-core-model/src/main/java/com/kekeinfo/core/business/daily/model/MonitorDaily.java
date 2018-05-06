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
import org.hibernate.annotations.Type;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 监测日志管理
 * 
 * @author sam
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MONITOR_DAILY", schema = SchemaConstant.KEKEINFO_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = {
		"DATE_C", "MONITOR_ID" }))
public class MonitorDaily extends KekeinfoEntity<Long, MonitorDaily> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 931906270212892102L;

	@Id
	@Column(name = "DAILY_ID", nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MON_DAILY_SEQ_NEXT_VAL")
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
	private Long userId;

	//zh:工况描述
	@Column(name = "NOTE", length = 1000)
	private String note;
	
	//zh:监测点情况
	@Column(name = "POINT_DESC", length = 1000)
	private String pointDesc;

	//zh:天气
	@Column(name = "WEATHER", length = 20)
	private String weather;

	//zh:气温
	@Column(name = "TEMPERATURE", length = 20)
	private String temperature;
	
	//zh:风级
	@Column(name = "WIND", length = 20)
	private String wind;

	//zh:总结
	@Column(name = "CONCLUSION")
	@Type(type = "org.hibernate.type.StringClobType")
	private String conclusion;
	
	//zh:围护结构外观形态
	@Column(name = "WEIHWGXT", length = 255)
	private String weiHuW="/";
	
	//zh:冠梁、支撑、围檩裂缝
	@Column(name = "LIEFENG", length = 255)
	private String leiFeng="/";
	
	//zh:支撑、立柱变形
	@Column(name = "BIANXING", length = 255)
	private String bianXing="/";
	
	//zh:止水帷幕开裂、渗漏
	@Column(name = "SHENLOU", length = 255)
	private String senLou="/";
	
	//zh:墙后土体沉陷、裂缝及滑移
	@Column(name = "HUAYI", length = 255)
	private String huaYi="/";
	
	//zh:基坑涌土、流砂、管涌
	@Column(name = "GUANYONG", length = 255)
	private String guanYong="/";	
	
	//zh:支护结构其他
	@Column(name = "ZHIHUOTHER", length = 255)
	private String zhiHuOther="/";

	//zh:开挖区域土质情况
	@Column(name = "TUZHI", length = 255)
	private String tuZhi="/";
	
	//zh:基坑开挖分段长度及分层厚度
	@Column(name = "HOUDU", length = 255)
	private String houDu="/";
	
	//zh:地表水、地下水状况
	@Column(name = "SHUISTATUS", length = 255)
	private String shuiStatus="/";
	
	//zh:基坑降水(回灌)设施运转情况
	@Column(name = "HUIGUAN", length = 255)
	private String huiGuan="/";	
	
	//zh:基坑周边地面堆载情况
	@Column(name = "DUIZHAI", length = 255)
	private String duiZhai="/";	

	//zh:施工工况其他
	@Column(name = "SHIGONGTOHER", length = 255)
	private String shiGongOther="/";
	
	//zh:管道破损、泄漏情况
	@Column(name = "XIELOU", length = 255)
	private String xieLou="/";
	
	//zh:周边建筑裂缝
	@Column(name = "JIANLIEFENG", length = 255)
	private String jianLFENG="/";
	
	//zh:周边道路（地面）裂缝、沉陷
	@Column(name = "CHENGXIAN", length = 255)
	private String chenXian="/";
	
	//zh:邻近施工情况
	@Column(name = "NEIBER", length = 255)
	private String neibor="/";

	//zh:周边环境其他
	@Column(name = "ZHOUBIANOTHER", length = 255)
	private String zbOther="/";
	
	//zh:基准点完好状况
	@Column(name = "JIDIAN", length = 255)
	private String jiDian="/";
	
	//zh:测点完好状况
	@Column(name = "CEDIAN", length = 255)
	private String ceDian="/";
	
	//zh:监测元件完好情况
	@Column(name = "YUANJIAN", length = 255)
	private String yuanJian="/";
	
	//zh:观测工作条件
	@Column(name = "TIAOJIAN", length = 255)
	private String tiaoJian="/";
	
	//zh:所属项目
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MONITOR_ID", nullable = true)
	private Monitor monitor;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "monitorDaily")
	private Set<MonitorDailyImage> monitorDailyImages = new HashSet<MonitorDailyImage>();

	public MonitorDaily() {
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

	public void setDatec(Date datec) {
		this.datec = datec;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPointDesc() {
		return pointDesc;
	}

	public void setPointDesc(String pointDesc) {
		this.pointDesc = pointDesc;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public Set<MonitorDailyImage> getMonitorDailyImages() {
		return monitorDailyImages;
	}

	public void setMonitorDailyImages(Set<MonitorDailyImage> monitorDailyImages) {
		this.monitorDailyImages = monitorDailyImages;
	}

	public String getWeiHuW() {
		return weiHuW;
	}

	public void setWeiHuW(String weiHuW) {
		this.weiHuW = weiHuW;
	}

	public String getLeiFeng() {
		return leiFeng;
	}

	public void setLeiFeng(String leiFeng) {
		this.leiFeng = leiFeng;
	}

	public String getBianXing() {
		return bianXing;
	}

	public void setBianXing(String bianXing) {
		this.bianXing = bianXing;
	}

	public String getSenLou() {
		return senLou;
	}

	public void setSenLou(String senLou) {
		this.senLou = senLou;
	}

	public String getHuaYi() {
		return huaYi;
	}

	public void setHuaYi(String huaYi) {
		this.huaYi = huaYi;
	}

	public String getGuanYong() {
		return guanYong;
	}

	public void setGuanYong(String guanYong) {
		this.guanYong = guanYong;
	}

	public String getZhiHuOther() {
		return zhiHuOther;
	}

	public void setZhiHuOther(String zhiHuOther) {
		this.zhiHuOther = zhiHuOther;
	}

	public String getTuZhi() {
		return tuZhi;
	}

	public void setTuZhi(String tuZhi) {
		this.tuZhi = tuZhi;
	}

	public String getHouDu() {
		return houDu;
	}

	public void setHouDu(String houDu) {
		this.houDu = houDu;
	}

	public String getShuiStatus() {
		return shuiStatus;
	}

	public void setShuiStatus(String shuiStatus) {
		this.shuiStatus = shuiStatus;
	}

	public String getHuiGuan() {
		return huiGuan;
	}

	public void setHuiGuan(String huiGuan) {
		this.huiGuan = huiGuan;
	}

	public String getDuiZhai() {
		return duiZhai;
	}

	public void setDuiZhai(String duiZhai) {
		this.duiZhai = duiZhai;
	}

	public String getShiGongOther() {
		return shiGongOther;
	}

	public void setShiGongOther(String shiGongOther) {
		this.shiGongOther = shiGongOther;
	}

	public String getXieLou() {
		return xieLou;
	}

	public void setXieLou(String xieLou) {
		this.xieLou = xieLou;
	}

	public String getJianLFENG() {
		return jianLFENG;
	}

	public void setJianLFENG(String jianLFENG) {
		this.jianLFENG = jianLFENG;
	}

	public String getChenXian() {
		return chenXian;
	}

	public void setChenXian(String chenXian) {
		this.chenXian = chenXian;
	}

	public String getNeibor() {
		return neibor;
	}

	public void setNeibor(String neibor) {
		this.neibor = neibor;
	}

	public String getZbOther() {
		return zbOther;
	}

	public void setZbOther(String zbOther) {
		this.zbOther = zbOther;
	}

	public String getJiDian() {
		return jiDian;
	}

	public void setJiDian(String jiDian) {
		this.jiDian = jiDian;
	}

	public String getCeDian() {
		return ceDian;
	}

	public void setCeDian(String ceDian) {
		this.ceDian = ceDian;
	}

	public String getYuanJian() {
		return yuanJian;
	}

	public void setYuanJian(String yuanJian) {
		this.yuanJian = yuanJian;
	}

	public String getTiaoJian() {
		return tiaoJian;
	}

	public void setTiaoJian(String tiaoJian) {
		this.tiaoJian = tiaoJian;
	}

}
