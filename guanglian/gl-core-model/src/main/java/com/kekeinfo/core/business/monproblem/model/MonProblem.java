package com.kekeinfo.core.business.monproblem.model;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;

import com.kekeinfo.core.business.attach.model.Attach;
import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.common.model.audit.AuditSection;
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.monitor.model.Monitor;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "MONITORPROBLEM", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class MonProblem extends KekeinfoEntity<Long, MonProblem> implements Auditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MON_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MONPRO_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	// zh:问题紧急程度
		@Column(name = "MONRANK", length = 20)
		private String monRank;

		// zh:问题类型
		@Column(name = "MONTYPE", length = 20)
		private String monType;
		
		// zh:负责人
		@Column(name = "OWNER", length = 20)
		private String owner;

		// zh:问题详情
		@Column(name = "MONDETAIL", length = 1000)
		private String monDetail;

		// zh:附件
		@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
		@JoinTable(name = "MONPRO_ATTACH", schema = SchemaConstant.KEKEINFO_SCHEMA, joinColumns = {
				@JoinColumn(name = "MON_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
						@JoinColumn(name = "ATTACH_ID", nullable = false, updatable = false) })
		@Cascade({ org.hibernate.annotations.CascadeType.DETACH, org.hibernate.annotations.CascadeType.LOCK,
				org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.REPLICATE

		})
		@JsonIgnore
		private List<Attach> attach = new ArrayList<Attach>();

		// zh:0：未处理 , 1：已反馈
		@Column(name = "MONSTATUS")
		private int monStatus = 0;

		// zh:处理人
		@Column(name = "DIDMAN", length = 20)
		private String didman;
			
		// zh:处理意见
		@Column(name = "SUGGEST", length = 1000)
		private String suggest;
		
		// zh:所属项目
		@JsonIgnore
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "MONITOR_ID", nullable = true)
		private Monitor monitor;

		@Embedded
		private AuditSection auditSection = new AuditSection();

		public String getMonRank() {
			return monRank;
		}

		public void setMonRank(String monRank) {
			this.monRank = monRank;
		}

		public String getMonType() {
			return monType;
		}

		public void setMonType(String monType) {
			this.monType = monType;
		}

		public String getMonDetail() {
			return monDetail;
		}

		public void setMonDetail(String monDetail) {
			this.monDetail = monDetail;
		}

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public void setId(Long id) {
			this.id = id;
		}

		public AuditSection getAuditSection() {
			return auditSection;
		}

		public void setAuditSection(AuditSection auditSection) {
			this.auditSection = auditSection;
		}

		public List<Attach> getAttach() {
			return attach;
		}

		public void setAttach(List<Attach> attach) {
			this.attach = attach;
		}

		public int getMonStatus() {
			return monStatus;
		}

		public void setMonStatus(int monStatus) {
			this.monStatus = monStatus;
		}

		public String getSuggest() {
			return suggest;
		}

		public void setSuggest(String suggest) {
			this.suggest = suggest;
		}

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}

		public String getDidman() {
			return didman;
		}

		public void setDidman(String didman) {
			this.didman = didman;
		}

		public Monitor getMonitor() {
			return monitor;
		}

		public void setMonitor(Monitor monitor) {
			this.monitor = monitor;
		}

}
