package com.kekeinfo.core.business.designdoc.model;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.department.model.Department;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "DESIGNDOC", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Designdoc extends KekeinfoEntity<Long, Department>  implements Auditable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4963171195128111933L;

	@Id
	@Column(name = "DESIGNDOC_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "DESIGNDOC_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="TITILE", length=200)
	private String tite; //标题
	
	@Column(name="CONTENT")
	private String content; //说明
	
	//用于保存到cms
	@Column(name="FILE_NAME")
	private String fileName;
	
	//文件类型是否可以预览图片格式，pdf，txt
	@Column(name = "FILE_TYPE")
	private boolean fileType=false; 
	
	@JsonIgnore
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;
	
	//文献类别
	@Column(name="DTYPE",length=10)
	private String dtype;
	
	@Transient
	private InputStream digital = null;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	/**
	 * 
	 * 0：没有权限
	 * 1：有权限
	 */
	@Transient
	private int sstatus=0;

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTite() {
		return tite;
	}

	public void setTite(String tite) {
		this.tite = tite;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public InputStream getDigital() {
		return digital;
	}

	public void setDigital(InputStream digital) {
		this.digital = digital;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getSstatus() {
		return sstatus;
	}

	public void setSstatus(int sstatus) {
		this.sstatus = sstatus;
	}

	public boolean isFileType() {
		return fileType;
	}

	public void setFileType(boolean fileType) {
		this.fileType = fileType;
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
}
