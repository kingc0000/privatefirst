package com.kekeinfo.core.business.constructionsite.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

@Entity
@Table(name = "PROJECTBASE", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class ProjectBase extends  KekeinfoEntity<Long, ProjectBase> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1504710576382132978L;
	

	@Id
	@Column(name = "PBASE_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "PBASE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	 
	public ProjectBase() {
		// TODO Auto-generated constructor stub
		
	}	
	
	//建设单位
	@Column(name="UNIT", length=100)
	private String unit;
	
	//建设单位项目负责人
	@Column(name="UNITOWNER", length=100)
	private String unitOwner;
	
	//建设单位联系方式
	@Column(name="UNITINFO", length=100)
	private String unitInfo;
	
	//施工承包单位
	@Column(name ="CONTOR", length=100)
	private String contor;
	
	//施工承包单位项目负责人
	@Column(name ="CONTOROWNER", length=100)
	private String contorOwner;
	
	//施工承包单位联系方式
	@Column(name ="CONTORINFO", length=100)
	private String contorInfo;
	
	//设计单位
	@Column(name = "DESIGN",length=100)
	private String design;
	
	//设计单位项目负责人
	@Column(name = "DESIGNOWNER",length=100)
	private String designOwner;
	
	//设计单位联系方式
	@Column(name = "DESIGNINFO",length=100)
	private String designInfo;
	
	//监理单位
	@Column(name = "SUPERV",length=100)
	private String superv;
		
	//监理单位项目负责人
	@Column(name = "SUPERVOWNER",length=100)
	private String supervOwner;
	
	//监理单位联系方式
	@Column(name = "SUPERVINFO",length=100)
	private String supervInfo;
	
	//降水施工单位
	@Column(name = "PREUNIT",length=100)
	private String preUnit;
	
	//项管部负责人
	@Column(name = "PMDOWNER",length=100)
	private String pmdOwner;
	
	//项管部负责人联系方式
	@Column(name = "PMINFO",length=100)
	private String pmdInfo;
	
	//项目工程师
	@Column(name = "ENGINEER",length=100)
	private String engineer;
	
	//项目工程师联系方式
	@Column(name = "ENGINFO",length=100)
	private String engInfo;
	
	//安全负责人
	@Column(name = "SAFEOWNER",length=100)
	private String safeOwner;
	
	//安全负责人联系方式
	@Column(name = "SAFEINFO",length=100)
	private String safeInfo;
	
	//安全负责人
	@Column(name = "TECHOWNER",length=100)
	private String techOwner;
	
	//安全负责人联系方式
	@Column(name = "TECHINFO",length=100)
	private String techInfo;
	
	//等级
	@Column(name="RANK",length=10)
	private String rank;
	
	//环境等级
	@Column(name="ERANK",length=10)
	private String eRank;
	
	//基坑开挖深度
	@Column(name="PITDEPTH",length=10)
	private String pitDepth;
	
	//围护特征
	@Column(name="SURROUNDFEATURES",length=10)
	private String surroundFeatures;
	
	//围护形式
	@Column(name="SURROUNDSTYLE",length=10)
	private String surroundStyle;
	
	//布井方式
	@Column(name="PATTERN",length=10)
	private String pattern;
	
	//降水类型
	@Column(name="TYPE",length=10)
	private String type;
	
	//降压幅度
	@Column(name="PRANGE",length=10)
	private String prange;
	
	//降压幅度
	@Column(name="LAYER")
	private String layer;
	
	//承压水分类
	@Column(name="CONFINED")
	private String confined ;
	
	//降水目的层
	@Column(name="PRECIPITATION")
	private String precipitation ;
	
	//备注
	@Column(name="MEMO", length=1000)
	private String memo;
	
	//项目概述
	@Column(name="SUMMARY")
	@Type(type = "org.hibernate.type.StringClobType")
	private String summary;
	
	@JsonIgnore
	@OneToOne(mappedBy="pbase")
	private ConstructionSite csite;

	
	@Column(name="FEATURES",length=10)
	private String features;
	
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitOwner() {
		return unitOwner;
	}

	public void setUnitOwner(String unitOwner) {
		this.unitOwner = unitOwner;
	}

	public String getUnitInfo() {
		return unitInfo;
	}

	public void setUnitInfo(String unitInfo) {
		this.unitInfo = unitInfo;
	}

	public String getContor() {
		return contor;
	}

	public void setContor(String contor) {
		this.contor = contor;
	}

	public String getContorOwner() {
		return contorOwner;
	}

	public void setContorOwner(String contorOwner) {
		this.contorOwner = contorOwner;
	}

	public String getContorInfo() {
		return contorInfo;
	}

	public void setContorInfo(String contorInfo) {
		this.contorInfo = contorInfo;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getDesignOwner() {
		return designOwner;
	}

	public void setDesignOwner(String designOwner) {
		this.designOwner = designOwner;
	}

	public String getDesignInfo() {
		return designInfo;
	}

	public void setDesignInfo(String designInfo) {
		this.designInfo = designInfo;
	}

	public String getSuperv() {
		return superv;
	}

	public void setSuperv(String superv) {
		this.superv = superv;
	}

	public String getSupervOwner() {
		return supervOwner;
	}

	public void setSupervOwner(String supervOwner) {
		this.supervOwner = supervOwner;
	}

	public String getSupervInfo() {
		return supervInfo;
	}

	public void setSupervInfo(String supervInfo) {
		this.supervInfo = supervInfo;
	}

	public String getPreUnit() {
		return preUnit;
	}

	public void setPreUnit(String preUnit) {
		this.preUnit = preUnit;
	}

	public String getPmdOwner() {
		return pmdOwner;
	}

	public void setPmdOwner(String pmdOwner) {
		this.pmdOwner = pmdOwner;
	}

	public String getPmdInfo() {
		return pmdInfo;
	}

	public void setPmdInfo(String pmdInfo) {
		this.pmdInfo = pmdInfo;
	}

	public String getEngineer() {
		return engineer;
	}

	public void setEngineer(String engineer) {
		this.engineer = engineer;
	}

	public String getEngInfo() {
		return engInfo;
	}

	public void setEngInfo(String engInfo) {
		this.engInfo = engInfo;
	}

	public String getSafeOwner() {
		return safeOwner;
	}

	public void setSafeOwner(String safeOwner) {
		this.safeOwner = safeOwner;
	}

	public String getSafeInfo() {
		return safeInfo;
	}

	public void setSafeInfo(String safeInfo) {
		this.safeInfo = safeInfo;
	}

	public String getTechOwner() {
		return techOwner;
	}

	public void setTechOwner(String techOwner) {
		this.techOwner = techOwner;
	}

	public String getTechInfo() {
		return techInfo;
	}

	public void setTechInfo(String techInfo) {
		this.techInfo = techInfo;
	}

	public ConstructionSite getCsite() {
		return csite;
	}

	public void setCsite(ConstructionSite csite) {
		this.csite = csite;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String geteRank() {
		return eRank;
	}

	public void seteRank(String eRank) {
		this.eRank = eRank;
	}

	public String getPitDepth() {
		return pitDepth;
	}

	public void setPitDepth(String pitDepth) {
		this.pitDepth = pitDepth;
	}

	public String getSurroundFeatures() {
		return surroundFeatures;
	}

	public void setSurroundFeatures(String surroundFeatures) {
		this.surroundFeatures = surroundFeatures;
	}

	public String getSurroundStyle() {
		return surroundStyle;
	}

	public void setSurroundStyle(String surroundStyle) {
		this.surroundStyle = surroundStyle;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrange() {
		return prange;
	}

	public void setPrange(String prange) {
		this.prange = prange;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getConfined() {
		return confined;
	}

	public void setConfined(String confined) {
		this.confined = confined;
	}

	public String getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

}
