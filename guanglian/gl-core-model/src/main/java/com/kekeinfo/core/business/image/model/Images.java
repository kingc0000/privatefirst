package com.kekeinfo.core.business.image.model;

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
@Table(name = "WATER_IMAGE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Images extends KekeinfoEntity<Long, Images>  {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8746362952922368636L;
	
	@Id
	@Column(name = "IMAGE_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "IMAGE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	public Images() {
		
	}
	
	@Transient
	private InputStream digital = null;
	
	@Transient
	private InputStream jdigital = null;
	
	@Column(name="NAME", unique=true)
	private String name;
	
	@Column(name="JPEG", unique=true)
	private String jpeg;
	
	public Images(String image) {
		this.name = image;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public InputStream getDigital() {
		return digital;
	}

	public void setDigital(InputStream digital) {
		this.digital = digital;
	}

	public InputStream getJdigital() {
		return jdigital;
	}

	public void setJdigital(InputStream jdigital) {
		this.jdigital = jdigital;
	}

	public String getJpeg() {
		return jpeg;
	}

	public void setJpeg(String jpeg) {
		this.jpeg = jpeg;
	}	

}
