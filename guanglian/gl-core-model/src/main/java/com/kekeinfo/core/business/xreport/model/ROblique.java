package com.kekeinfo.core.business.xreport.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "ROBLIQUE", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class ROblique extends KekeinfoEntity<Long, ROblique> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5044945689239571465L;

	@Id
	@Column(name = "ROBLIQUE_ID", unique = true, nullable = false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ROBLIQUE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	// zh:孔号
	@Column(name = "NAMBER")
	private String namber;

	// zh:评价
	@Column(name = "EVALUATE", length = 1000)
	private String evaluate;

	// zh:水平位移修正
	@Column(name = "HORCORRECT")
	private BigDecimal horcorrect =new BigDecimal(0);
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "XMREPORT_ID", nullable = true)
	private XMreport xmreport;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "rObliqu")
	private Set<RObliqueData> rDepth = new HashSet<RObliqueData>();

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getNamber() {
		return namber;
	}

	public void setNamber(String namber) {
		this.namber = namber;
	}

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	public BigDecimal getHorcorrect() {
		return horcorrect;
	}

	public void setHorcorrect(BigDecimal horcorrect) {
		this.horcorrect = horcorrect;
	}


	public XMreport getXmreport() {
		return xmreport;
	}

	public void setXmreport(XMreport xmreport) {
		this.xmreport = xmreport;
	}

	public Set<RObliqueData> getrDepth() {
		return rDepth;
	}

	public void setrDepth(Set<RObliqueData> rDepth) {
		this.rDepth = rDepth;
	}

}
