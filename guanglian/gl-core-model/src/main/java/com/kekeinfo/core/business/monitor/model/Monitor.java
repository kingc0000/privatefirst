package com.kekeinfo.core.business.monitor.model;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.image.model.Images;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "MONITOR", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Monitor extends  KekeinfoEntity<Long, Monitor>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3527205924052041414L;

	/**
	 * 
	 */
	
	

	@Id
	@Column(name = "MONITOR_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "MONITOR_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	 
	public Monitor() {
		// TODO Auto-generated constructor stub
		
	}	
	
	//项目信息
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name="PROJECT_ID", nullable=true)
	private Project project;
	
	
	@OneToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "MONITOR_IMAGE", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "MONITOR_ID", nullable = false, updatable = false) }
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
	
	/**
	 * 
	 * 0：成井
	 * 1：关闭	 */
	@Column(name = "STATUS")
	private int status=0;
	
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

	public List<Images> getImages() {
		return images;
	}

	public void setImages(List<Images> images) {
		this.images = images;
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

	public Mbade getmBase() {
		return mBase;
	}

	public void setmBase(Mbade mBase) {
		this.mBase = mBase;
	}
		
}
