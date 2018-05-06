package com.kekeinfo.core.business.pointlink.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import com.kekeinfo.core.business.invertedwell.model.Invertedwell;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "P_INVERTEDLINK", schema=SchemaConstant.KEKEINFO_SCHEMA,
uniqueConstraints={@UniqueConstraint(columnNames = {"GATEWAY_ID","NODE_1", "CHANNEL_1"}), @UniqueConstraint(columnNames = {"GATEWAY_ID","NODE_2", "CHANNEL_2"})
, @UniqueConstraint(columnNames = {"GATEWAY_ID","NODE_3", "CHANNEL_3"}), @UniqueConstraint(columnNames = {"GATEWAY_ID","NODE_4", "CHANNEL_4"})})
public class InvertedLink extends BasepointLink<Invertedwell> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3557992748565515314L;
	@Id
	@Column(name = "P_INVERTEDLINK_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "P_INVERTEDLINK_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
}
