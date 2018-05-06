package com.kekeinfo.core.business.sign.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.guard.model.Guard;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "SIGN", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Sign extends KekeinfoEntity<Long, Sign> implements Auditable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4974414696713284094L;
	@Id
	@Column(name = "SIGN_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SIGN_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	// zh:附件
	@OneToMany(fetch=FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinTable(name = "SIGN_ATTACH", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "SIGN_ID", nullable = false, updatable = false) }
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

	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_SHOULEDBE")
	private Date shouleBe;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Guard.class)
	@JoinColumn(name = "GUARD_ID", nullable = false)
	private Guard guard;
	
	@JsonIgnore
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "User_ID", nullable = false)
	private User user;
	
	//备注
	@Column(name="MEMO", length=1000)
	private String memo;
	/**
	 * 0:要点，1：笑点
	 */
	@Column(name = "STYPE")
	private int stype=0;
	
	/**
	 * 0:未签到，1：已签到
	 */
	@Column(name = "SATUSE")
	private int sattus=0;
	
	
	/**
	 * 项目名称
	 */
	@Column(name = "PNAME" ,length = 255)
	private String pName;
	
	/**
	 * 人员名称
	 */
	@Column(name = "UNAME" ,length = 20)
	private String uName;
	
	/**
	 * 签到车站
	 */
	@Column(name = "STATION" )
	private String station;
	
	/**
	 * 地址
	 */
	@Column(name = "ADDRESS" )
	private String address;
	
	/**
	 * 签到地址
	 */
	@Column(name = "SIGNADDRESS" )
	private String signaddress;
	
	@JsonIgnore
	@ManyToOne(targetEntity = GJob.class)
	@JoinColumn(name = "GJOB_ID", nullable = false)
	private GJob gjob;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Guard getGuard() {
		return guard;
	}

	public void setGuard(Guard guard) {
		this.guard = guard;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStype() {
		return stype;
	}

	public void setStype(int stype) {
		this.stype = stype;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public GJob getGjob() {
		return gjob;
	}

	public void setGjob(GJob gjob) {
		this.gjob = gjob;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<Images> getImages() {
		return images;
	}

	public void setImages(List<Images> images) {
		this.images = images;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSattus() {
		return sattus;
	}

	public void setSattus(int sattus) {
		this.sattus = sattus;
	}

	public Date getShouleBe() {
		return shouleBe;
	}

	public void setShouleBe(Date shouleBe) {
		this.shouleBe = shouleBe;
	}

	public String getSignaddress() {
		return signaddress;
	}

	public void setSignaddress(String signaddress) {
		this.signaddress = signaddress;
	}

}
