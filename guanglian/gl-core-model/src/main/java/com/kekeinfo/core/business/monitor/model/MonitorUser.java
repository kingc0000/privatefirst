package com.kekeinfo.core.business.monitor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "MONITORUSER", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class MonitorUser extends  KekeinfoEntity<Long, MonitorUser>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 715132611587632192L;
	
	@Id
	@Column(name = "MONITORUSER_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "MONITORUSER_ID_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	//项目人员
	@OneToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name="USER_ID", nullable=true, updatable=true)
	private User user;
	
	//备注
	@Column(name="MEMO", length=1000)
	private String memo;

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
