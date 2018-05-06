package com.kekeinfo.core.business.appmessage.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.job.model.GJob;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "UMESSAGE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class MessageU extends KekeinfoEntity<Long, MessageU>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4974414696713284094L;
	@Id
	@Column(name = "MESSAGE_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "UMESSAGE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	/**
	 * 0:未阅读
	 * 1:已阅读
	 */
	@Column(name = "STATUS")
	private int statu = 0 ;
	
	
	/**
	 * 项目id,逗号分隔
	 */
	@Column(name = "PROJECTID")
	private String pids;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="USER_ID", nullable=true)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinColumn(name="AMESSAGE_ID", nullable=true)
	private AMessage message;
	
	@JsonIgnore
	@ManyToOne(targetEntity = GJob.class)
	@JoinColumn(name = "GJOB_ID", nullable =true)
	private GJob gjob;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AMessage getMessage() {
		return message;
	}

	public void setMessage(AMessage message) {
		this.message = message;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	public GJob getGjob() {
		return gjob;
	}

	public void setGjob(GJob gjob) {
		this.gjob = gjob;
	}
	

}
