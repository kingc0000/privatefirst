package com.kekeinfo.core.business.preview.model;

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
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.business.user.model.User;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * 工程评价管理
 * @author sam
 *
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "P_REVIEW", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class Preview extends KekeinfoEntity<Long, Preview> implements Auditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7038175879832153439L;

	@Id
	@Column(name = "REVIEW_ID", nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PREVIEW_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();
	
	//评价内容
	@Column(name="COMMENT", length=1000)
	private String comment; 
	
	//内容
	@Column(name="FEEDBACK", length=1000)
	private String feedback; 
	
	//评分维度1
	@Column(name="SCORE1", length=1)
	private Integer score1; 
	
	//评分维度2
	@Column(name="SCORE2", length=1)
	private Integer score2;
	
	//评分维度3
	@Column(name="SCORE3", length=1)
	private Integer score3; 
	
	//评分维度4
	@Column(name="SCORE4", length=1)
	private Integer score4;
	
	//所属项目
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CSITE_ID", nullable=false)
	private Project project;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy = "preview")
	private Set<PreviewImage> previewImages = new HashSet<PreviewImage>();
	
	//评论用户
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="USER_ID", nullable=false)
	private User user;
	
	//反馈用户
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FEEDBACK_USER_ID", nullable=true)
	private User feedbackUser;
	
	public Preview() {
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Integer getScore1() {
		return score1;
	}

	public void setScore1(Integer score1) {
		this.score1 = score1;
	}

	public Integer getScore2() {
		return score2;
	}

	public void setScore2(Integer score2) {
		this.score2 = score2;
	}

	public Integer getScore3() {
		return score3;
	}

	public void setScore3(Integer score3) {
		this.score3 = score3;
	}

	public Integer getScore4() {
		return score4;
	}

	public void setScore4(Integer score4) {
		this.score4 = score4;
	}

	public Set<PreviewImage> getPreviewImages() {
		return previewImages;
	}

	public void setPreviewImages(Set<PreviewImage> previewImages) {
		this.previewImages = previewImages;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getFeedbackUser() {
		return feedbackUser;
	}

	public void setFeedbackUser(User feedbackUser) {
		this.feedbackUser = feedbackUser;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}


}
