package com.kekeinfo.core.business.content.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import com.kekeinfo.core.business.common.model.MetaDescription;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;


@Entity
@Table(name = "CONTENT", schema= SchemaConstant.KEKEINFO_SCHEMA)
public class Content extends KekeinfoEntity<Long, Content> implements Serializable {

	private static final long serialVersionUID = 1772757159185494620L;
	
	@Id
	@Column(name = "CONTENT_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CONTENT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Embedded
	private MetaDescription description = new MetaDescription();
	
	@NotEmpty
	@Column(name="CODE", length=100, nullable=false)
	private String code;
	
	@Column(name = "VISIBLE")
	private boolean visible;
	
	@Column(name = "CONTENT_POSITION", length=10, nullable=true)
	@Enumerated(value = EnumType.STRING)
	private ContentPosition contentPosition;
	
	//Used for grouping
	//BOX, SECTION, PAGE
	@Column(name = "CONTENT_TYPE", length=20, nullable=true)
	private String contentType; 
	
	@Column(name = "IMG")
	private String img;
	
	@Transient
	private String fullImageUrl; //完整的图片访问地址，便于页面处理
	
	@Transient
	private String typeName;
	
	@Column(name = "SORT_ORDER")
	private Integer sortOrder = 0;
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}



	

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setContentPosition(ContentPosition contentPosition) {
		this.contentPosition = contentPosition;
	}

	public ContentPosition getContentPosition() {
		return contentPosition;
	}

	public MetaDescription getDescription() {
		return description;
	}

	public void setDescription(MetaDescription description) {
		this.description = description;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getFullImageUrl() {
		return fullImageUrl;
	}

	public void setFullImageUrl(String fullImageUrl) {
		this.fullImageUrl = fullImageUrl;
	}


}
