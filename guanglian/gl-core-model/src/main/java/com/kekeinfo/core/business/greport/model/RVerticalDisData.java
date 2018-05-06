package com.kekeinfo.core.business.greport.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "RVERTICALDISDATA", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class RVerticalDisData extends GMBaseData<RVerticalDisData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3074841742357802348L;

	@Id
	@Column(name = "RVERTICALDISDATA_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "RVERTICALDISDATA_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
