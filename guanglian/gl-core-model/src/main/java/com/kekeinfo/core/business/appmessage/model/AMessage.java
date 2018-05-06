package com.kekeinfo.core.business.appmessage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;
import com.kekeinfo.core.utils.MessageTypeEnum;

@Entity
@Table(name = "AMESSAGE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class AMessage extends KekeinfoEntity<Long, AMessage>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4974414696713284094L;
	@Id
	@Column(name = "AMESSAGE_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "AMESSAGE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Column(name = "TIELT",length=1000)
	private String title ;
	
	@Column(name = "MESSAGE",length=1000)
	private String message ;
	
	//zh:类别
	@Column(name="MTYPE")
	@Enumerated(value = EnumType.STRING)
	private MessageTypeEnum mtype;
	
	@Transient
	private String typename ;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED")
	private Date dateCreated;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageTypeEnum getMtype() {
		return mtype;
	}

	public void setMtype(MessageTypeEnum mtype) {
		this.mtype = mtype;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
