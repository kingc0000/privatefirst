package com.kekeinfo.core.business.dreport.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Type;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 编制报告
 * @author sam
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "DESIGN_REPORT", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Dreport extends KekeinfoEntity<Long, Dreport> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6814315579101701224L;

	@Id
	@Column(name = "DREPORT_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DREPORT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	@Column(name="TITLE", length=255)
	private String title; //标题
	
	@Column(name="NOTE")
	@Type(type = "org.hibernate.type.StringClobType")
	private String note;
	
	//报告编制的状态：编制中0、待审核1、待审定2、修订（审核）3、修订（审定）4，结束5，其中待修正为审核或审定过程中退回后的状态
	@Column(name = "STATUS")
	private int status = 0;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="USER_ID", nullable=false)
	private User user;
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy = "dreport")
	@OrderBy("auditSection.dateCreated asc")
	private Set<Dopinion> opinions = new HashSet<Dopinion>();

	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy = "dreport")
	private Set<Dattach> attaches = new HashSet<Dattach>();
	
	//等级
	@Column(name="RANK",length=10)
	private String rank;
	
	public Dreport() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuditSection getAuditSection() {
		return auditSection;
	}

	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Set<Dopinion> getOpinions() {
		return opinions;
	}

	public void setOpinions(Set<Dopinion> opinions) {
		this.opinions = opinions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Set<Dattach> getAttaches() {
		return attaches;
	}

	public void setAttaches(Set<Dattach> attaches) {
		this.attaches = attaches;
	}
	
}
