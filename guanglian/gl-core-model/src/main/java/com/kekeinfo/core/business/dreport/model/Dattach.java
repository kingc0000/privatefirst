package com.kekeinfo.core.business.dreport.model;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 报告附件
 * @author sam
 *
 */
@Entity
@Table(name = "DREPORT_ATTACH", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Dattach extends KekeinfoEntity<Long, Dattach> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1213282320480294410L;


	@Id
	@Column(name = "DREPORT_ATTACH_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DREPORT_ATTACH_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	@ManyToOne(targetEntity = Dreport.class)
	@JoinColumn(name = "DREPORT_ID", nullable = false)
	private Dreport dreport;


	@Column(name="FILE_NAME",nullable=false)
	private String fileName;
	
	@Column(name = "FILE_TYPE", length=20)
	private String fileType; //文件类型
	
	@Column(name = "FILE_NOTE", length=200)
	private String fileNote; //文件说明
	
	@Transient
	private InputStream digital = null;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileNote() {
		return fileNote;
	}

	public void setFileNote(String fileNote) {
		this.fileNote = fileNote;
	}

	public Dreport getDreport() {
		return dreport;
	}

	public void setDreport(Dreport dreport) {
		this.dreport = dreport;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public InputStream getDigital() {
		return digital;
	}

	public void setDigital(InputStream digital) {
		this.digital = digital;
	}

	
}
