package com.kekeinfo.core.business.daily.model;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "DAILY_IMAGE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class DailyImage extends KekeinfoEntity<Long, DailyImage> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6412297464045666125L;

	@Id
	@Column(name = "DAILY_IMAGE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DAILY_IMG_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name = "DAILY_IMAGE")
	private String dailyImage;
	
	@Column(name = "IMAGE_TYPE")
	private int imageType;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Daily.class)
	@JoinColumn(name = "DAILY_ID", nullable = false)
	private Daily daily;
	
	@Column(name="JPEG", unique=true)
	private String jpeg;
	
	@Transient
	private InputStream jdigital = null;
	
	@Transient
	private InputStream image = null;
	
	//private MultiPartFile image

	public DailyImage(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDailyImage() {
		return dailyImage;
	}

	public void setDailyImage(String dailyImage) {
		this.dailyImage = dailyImage;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

	public Daily getDaily() {
		return daily;
	}

	public void setDaily(Daily daily) {
		this.daily = daily;
	}

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}

	public String getJpeg() {
		return jpeg;
	}

	public void setJpeg(String jpeg) {
		this.jpeg = jpeg;
	}

	public InputStream getJdigital() {
		return jdigital;
	}

	public void setJdigital(InputStream jdigital) {
		this.jdigital = jdigital;
	}


}
