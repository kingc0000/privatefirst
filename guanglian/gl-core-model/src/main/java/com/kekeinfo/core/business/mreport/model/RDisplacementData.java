package com.kekeinfo.core.business.mreport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "RDISPLACEMENTDATA", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class RDisplacementData extends RMBaseData<RDisplacementData> {

	private static final long serialVersionUID = -8231506062099735443L;
	@Id
	@Column(name = "RDISPLACEMENTDATA_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "RDISPLACEMENTDATA_ID_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
}
