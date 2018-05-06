package com.kekeinfo.core.business.pointinfo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.image.model.Wellimages;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "P_PUMPINFO", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class PumpInfo extends BasepointInfo<Pumpwell> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1358728366425339554L;

	@Id
	@Column(name = "P_PUMPINFO_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "P_PUMPINFO_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "PWELL_IMAGE", schema=SchemaConstant.KEKEINFO_SCHEMA, joinColumns = { 
			@JoinColumn(name = "PUMPWELL_ID", nullable = false, updatable = false) }
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
	private List<Wellimages> images = new ArrayList<Wellimages>();
	
	//降水目的层
	@Column(name="PRECIPITATION")
	private String precipitation ;

	public List<Wellimages> getImages() {
		return images;
	}

	public void setImages(List<Wellimages> images) {
		this.images = images;
	}

	public String getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}

}
