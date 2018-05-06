package com.kekeinfo.core.business.preview.model;

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

/**
 * 评论图片
 * @author sam
 *
 */
@Entity
@Table(name = "P_REVIEW_IMAGE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class PreviewImage extends KekeinfoEntity<Long, PreviewImage> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6261949245612665698L;

	@Id
	@Column(name = "REVIEW_IMAGE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "REVIEW_IMG_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name = "REVIEW_IMAGE")
	private String reviewImage;
	
	@Column(name = "REVIEW_TYPE")
	private int imageType;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Preview.class)
	@JoinColumn(name = "REVIEW_ID", nullable = false)
	private Preview preview;
	
	@Transient
	private InputStream image = null;
	
	@Transient
	private String imageUrl;
	
	//private MultiPartFile image

	public PreviewImage(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReviewImage() {
		return reviewImage;
	}

	public void setReviewImage(String reviewImage) {
		this.reviewImage = reviewImage;
	}

	public int getImageType() {
		return imageType;
	}

	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}

	public Preview getPreview() {
		return preview;
	}

	public void setPreview(Preview preview) {
		this.preview = preview;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


}
