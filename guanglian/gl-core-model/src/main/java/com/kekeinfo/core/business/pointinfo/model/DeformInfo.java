package com.kekeinfo.core.business.pointinfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.business.deformmonitor.model.Deformmonitor;
import com.kekeinfo.core.business.point.model.BasepointInfo;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "P_DEFORMINFO", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class DeformInfo extends BasepointInfo<Deformmonitor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5951518118358563795L;

	@Id
	@Column(name = "P_DEFORMINFO_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "P_DEFORMINFO_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
