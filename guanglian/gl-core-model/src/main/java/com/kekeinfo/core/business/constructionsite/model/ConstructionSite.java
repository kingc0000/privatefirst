package com.kekeinfo.core.business.constructionsite.model;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.gateway.model.Gateway;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.observewell.model.Observewell;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "CONSTRUCTIONSITE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class ConstructionSite extends  KekeinfoEntity<Long, ConstructionSite>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1504710576382132978L;
	

	@Id
	@Column(name = "CSITE_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "CSITE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	 
	public ConstructionSite() {
		// TODO Auto-generated constructor stub
		
	}	
	
	//围栏
	@Column(name="RAIL",length=2000)
	private String rail;
	
	//断电监测频率
	@Column(name="MONITOR_POWER", length=2)
	private Integer monitorPower=10;
	
	//数据采集频率
	@Column(name="GATHER_DATA", length=2)
	private Integer gatherData=10;
	
	//是否显示给申通
	@Column(name="SHENGTONG")
	private Boolean shengTong=new Boolean(false);
	
	//是否显示给建工
	@Column(name="JIANGONG")
	private Boolean jianGong =new Boolean(false);
	
	//基本信息
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name="PBASE_ID", nullable=true)
	private ProjectBase pbase;
	
	//0:正常，大于1都错误数量
	@Column(name = "RUNSTATUS")
	private int runstatus=0;
	
	//zh:二维码
	@Column(name = "QCODE")
	private String qCode;
	
	@Transient
	private InputStream digital = null;
	
	@Transient
	private String name ;
	
	//项目信息
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name="PROJECT_ID", nullable=true)
	private Project project;
	
	@Column(name = "LONGITUDE", columnDefinition = "decimal(19,6)")
	private BigDecimal longitude = new BigDecimal(0); //经度
	
	@Column(name = "LATITUDE", columnDefinition = "decimal(19,6)")
	private BigDecimal latitude = new BigDecimal(0); //纬度
	
	@OneToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PROJECT_IMAGE", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "CSITE_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "IMAGE_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	@JsonIgnore
	private List<Images> images = new ArrayList<Images>();
	
	//井
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "cSite")
	private Set<Pumpwell> pwells = new LinkedHashSet<Pumpwell>();
	
	//井
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "cSite")
	private Set<Observewell> owells = new LinkedHashSet<Observewell>();
	
	public String getRail() {
		return rail;
	}

	public void setRail(String rail) {
		this.rail = rail;
	}

	public Integer getMonitorPower() {
		return monitorPower;
	}

	public void setMonitorPower(Integer monitorPower) {
		this.monitorPower = monitorPower;
	}

	public Integer getGatherData() {
		return gatherData;
	}

	public void setGatherData(Integer gatherData) {
		this.gatherData = gatherData;
	}

	public ProjectBase getPbase() {
		return pbase;
	}

	public void setPbase(ProjectBase pbase) {
		this.pbase = pbase;
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

	public List<Images> getImages() {
		return images;
	}

	public void setImages(List<Images> images) {
		this.images = images;
	}

	public Set<Pumpwell> getPwells() {
		return pwells;
	}

	public void setPwells(Set<Pumpwell> pwells) {
		this.pwells = pwells;
	}

	public Set<Observewell> getOwells() {
		return owells;
	}

	public void setOwells(Set<Observewell> owells) {
		this.owells = owells;
	}

	public Set<Invertedwell> getIwells() {
		return iwells;
	}

	public void setIwells(Set<Invertedwell> iwells) {
		this.iwells = iwells;
	}

	public Set<Dewatering> getDewells() {
		return dewells;
	}

	public void setDewells(Set<Dewatering> dewells) {
		this.dewells = dewells;
	}

	public Set<Deformmonitor> getEwells() {
		return ewells;
	}

	public void setEwells(Set<Deformmonitor> ewells) {
		this.ewells = ewells;
	}

	public Set<Gateway> getGateways() {
		return gateways;
	}

	public void setGateways(Set<Gateway> gateways) {
		this.gateways = gateways;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSstatus() {
		return sstatus;
	}

	public void setSstatus(int sstatus) {
		this.sstatus = sstatus;
	}

	//井
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "cSite")
	private Set<Invertedwell> iwells = new LinkedHashSet<Invertedwell>();
	
	//井
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "cSite")
	private Set<Dewatering> dewells = new LinkedHashSet<Dewatering>();
	
	//井
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "cSite")
	private Set<Deformmonitor> ewells = new LinkedHashSet<Deformmonitor>();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "cSite")
	private Set<Gateway> gateways = new LinkedHashSet<Gateway>();
	
	/**
	 * -1:结束
	 * 0：成井
	 * 1：疏干运行
	 * 2：降压运行
	 */
	@Column(name = "STATUS")
	private int status=0;
	
	/**
	 * -1: 未选择
	 * 0：半选中状态
	 * 1：选中
	 */
	@Transient
	private int sstatus=-1;

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Boolean getShengTong() {
		return shengTong;
	}

	public void setShengTong(Boolean shengTong) {
		this.shengTong = shengTong;
	}

	public int getRunstatus() {
		return runstatus;
	}

	public void setRunstatus(int runstatus) {
		this.runstatus = runstatus;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getJianGong() {
		return jianGong;
	}

	public void setJianGong(Boolean jianGong) {
		this.jianGong = jianGong;
	}
	
}
