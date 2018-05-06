package com.kekeinfo.core.business.attach.model;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "ATTACH", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Attach extends KekeinfoEntity<Long, Attach> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4963171195128111933L;

	@Id
	@Column(name = "ATTACH_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "ATTACH_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	
	//用于保存到cms
	@Column(name="FILE_NAME")
	private String fileName;
	
	//文件类型是否可以预览图片格式，pdf，txt
	@Column(name = "FILE_TYPE")
	private boolean fileType=false; 
	
	
	@Transient
	private InputStream digital = null;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public boolean isFileType() {
		return fileType;
	}


	public void setFileType(boolean fileType) {
		this.fileType = fileType;
	}


	public InputStream getDigital() {
		return digital;
	}


	public void setDigital(InputStream digital) {
		this.digital = digital;
	}
	
	
}
