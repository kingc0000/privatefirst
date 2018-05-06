package com.kekeinfo.core.business.last.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "MLAST", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class MonitLast extends  KekeinfoEntity<Long, MonitLast>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1504710576382132978L;
	

	@Id
	@Column(name = "MLAST_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "MLAST_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	 
	public MonitLast() {
		// TODO Auto-generated constructor stub
		
	}	
	
	
	@Column(name="CID")
	private long mid=10;
	
	@Column(name="UID")
	private long uid=10;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_MODIFIED")
	private Date dateModified;

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}
	
}
