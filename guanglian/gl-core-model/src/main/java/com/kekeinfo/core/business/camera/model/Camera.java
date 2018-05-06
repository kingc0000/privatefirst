package com.kekeinfo.core.business.camera.model;

import java.math.BigDecimal;

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
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "CAMERA", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Camera extends KekeinfoEntity<Long, Camera> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6843077540284569929L;

	@Id
	@Column(name = "CAMERA_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CAMERA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	@Column(name="note", length=255)
	private String note; //备注
	
	@Column(name="path", length=255)
	private String path; //路径
	
	//所属工地
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID", nullable=true)
	private Project project;
	
	@Column(name = "LONGITUDE", columnDefinition = "decimal(19,6)")
	private BigDecimal longitude = new BigDecimal(0); //经度
	
	@Column(name = "LATITUDE", columnDefinition = "decimal(19,6)")
	private BigDecimal latitude = new BigDecimal(0); //纬度
	
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

	//状态
	@Column(name = "status")
	private boolean status = true;

	public Camera() {
		super();
	}

	public Camera(String note, String path) {
		super();
		this.note = note;
		this.path = path;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	
}
