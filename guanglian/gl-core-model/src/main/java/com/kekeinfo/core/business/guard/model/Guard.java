package com.kekeinfo.core.business.guard.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.common.model.BasedataType;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.monitor.model.Mbade;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.constants.SchemaConstant;

@SuppressWarnings("unused")
@Entity
@Table(name = "GUARD", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Guard extends  KekeinfoEntity<Long, Guard>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3527205924052041414L;

	/**
	 * 
	 */
	
	

	@Id
	@Column(name = "GUARD_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "GUARD_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	 
	public Guard() {
		
	}	
	
	//项目信息
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID", nullable=true)
	private Project project;
	
	//项目名称
	@Column(name="STATION", length=100)
	private String station;
	
	@Transient
	private String address ;
	
	
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinTable(name = "GUARD_IMAGE", schema = SchemaConstant.KEKEINFO_SCHEMA, joinColumns = {
			@JoinColumn(name = "GUARD_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "ATTACH_ID", nullable = false, updatable = false) })
	@Cascade({ org.hibernate.annotations.CascadeType.DETACH, org.hibernate.annotations.CascadeType.LOCK,
			org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.REPLICATE

	})
	@JsonIgnore
	private List<Attach> images = new ArrayList<Attach>();
	
	//负责人
	@Column(name="TECHID")
	private Long projectTechNameid;
	
	//负责人名称
	@Column(name="TECHNAME")
	private String projectTechName;
	
	@Transient
	private String name ;
	
	//项目状态
	@Column(name = "STATUS")
	private int status=0;
	
	//项目状态
	@Column(name = "STRUCTURE")
	private int structure=0;
	/**
	@ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "GUARD_STATUSS", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "GUARD_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "BASEDATA_TYPE_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private Set<BasedataType> statuss = new HashSet<BasedataType>();
	*/
	
	/**
	 * -1: 未选择
	 * 0：半选中状态
	 * 1：选中
	 */
	@Transient
	private int sstatus=-1;

	//外围信息
	@Embedded
	private Mbade mBase;
	
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

	

	public int getSstatus() {
		return sstatus;
	}

	public void setSstatus(int sstatus) {
		this.sstatus = sstatus;
	}

	public Mbade getmBase() {
		return mBase;
	}

	public void setmBase(Mbade mBase) {
		this.mBase = mBase;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStructure() {
		return structure;
	}

	public void setStructure(int structure) {
		this.structure = structure;
	}

	public List<Attach> getImages() {
		return images;
	}

	public void setImages(List<Attach> images) {
		this.images = images;
	}	

	public String getProjectTechName() {
		return projectTechName;
	}

	public void setProjectTechName(String projectTechName) {
		this.projectTechName = projectTechName;
	}

	public Long getProjectTechNameid() {
		return projectTechNameid;
	}

	public void setProjectTechNameid(Long projectTechNameid) {
		this.projectTechNameid = projectTechNameid;
	}

}
