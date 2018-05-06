package com.kekeinfo.core.business.point.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.generic.model.KekeinfoEntity;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BasepointInfo<E> extends KekeinfoEntity<Long, BasepointInfo<E>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@OneToOne(mappedBy="pointInfo")
	private E point;//关联测点
	
	//孔径/mm
	@Column(name="APERTURE")
	private BigDecimal aperture;
	
	//管井/mm
	@Column(name="TUBEWELL")
	private BigDecimal tubeWell;
	
	//井深/m
	@Column(name="DEEPWELL")
	private BigDecimal deepWell;
	
	//滤管长度
	@Column(name="FILTERTUBELENGTH")
	private BigDecimal fTubleLgn;
	
	//滤料回填量/t
	@Column(name="BACKFILLVOLUME")
	private BigDecimal backfillVol;
	
	//粘土球回填量/t
	@Column(name="CLAYBACKFILL")
	private BigDecimal clayBackfill;
	
	//井口是否回填
	@Column(name="BACKFILL")
	private int backFill=0;
	
	//洗井周期
	@Column(name="WASHWELLCYCLE")
	private String washWC;
	
	//单井涌水量/m3/h
	@Column(name="SINGLEWELLINFLOW")
	private BigDecimal sWellFlow;
	
	//动水位/m
	@Column(name="MOVINGWATER")
	private BigDecimal moveingWater;
	
	//初始水位/m
	@Column(name="INITIALWATER")
	private BigDecimal initialWater;
	
	//井号现场标识
	@Column(name="POUNDSITE")
	private String poundSite;

	//是否有异常
	@Column(name="EXCEPTION")
	private int exception=0;
	
	//成井时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="WELLTIME")
	private Date wellTime;
	
	//成井负责人
	@Column(name="CREATEPERSON")
	private String cPerson;
	
	//验收管理人员
	@Column(name="ACCEPTANCE")
	private String acceptance;
	
	//封井时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CLOSURETIME")
	private Date closure;
	
	//封井措施
	@Column(name="CLOSUREMEASURES")
	private String cmeasures;
	
	//封井验收人
	@Column(name="SEALACCEPTANCE")
	private String sAcceptance;
	
	
	@Column(name="note", length=255)
	private String note; //备注
	
	public E getPoint() {
		return point;
	}
	public void setPoint(E point) {
		this.point = point;
	}
	public BigDecimal getAperture() {
		return aperture;
	}
	public void setAperture(BigDecimal aperture) {
		this.aperture = aperture;
	}
	public BigDecimal getTubeWell() {
		return tubeWell;
	}
	public void setTubeWell(BigDecimal tubeWell) {
		this.tubeWell = tubeWell;
	}
	public BigDecimal getDeepWell() {
		return deepWell;
	}
	public void setDeepWell(BigDecimal deepWell) {
		this.deepWell = deepWell;
	}
	public BigDecimal getfTubleLgn() {
		return fTubleLgn;
	}
	public void setfTubleLgn(BigDecimal fTubleLgn) {
		this.fTubleLgn = fTubleLgn;
	}
	public BigDecimal getBackfillVol() {
		return backfillVol;
	}
	public void setBackfillVol(BigDecimal backfillVol) {
		this.backfillVol = backfillVol;
	}
	public BigDecimal getClayBackfill() {
		return clayBackfill;
	}
	public void setClayBackfill(BigDecimal clayBackfill) {
		this.clayBackfill = clayBackfill;
	}
	public int getBackFill() {
		return backFill;
	}
	public void setBackFill(int backFill) {
		this.backFill = backFill;
	}
	public String getWashWC() {
		return washWC;
	}
	public void setWashWC(String washWC) {
		this.washWC = washWC;
	}
	public BigDecimal getsWellFlow() {
		return sWellFlow;
	}
	public void setsWellFlow(BigDecimal sWellFlow) {
		this.sWellFlow = sWellFlow;
	}
	public BigDecimal getMoveingWater() {
		return moveingWater;
	}
	public void setMoveingWater(BigDecimal moveingWater) {
		this.moveingWater = moveingWater;
	}
	public BigDecimal getInitialWater() {
		return initialWater;
	}
	public void setInitialWater(BigDecimal initialWater) {
		this.initialWater = initialWater;
	}
	public String getPoundSite() {
		return poundSite;
	}
	public void setPoundSite(String poundSite) {
		this.poundSite = poundSite;
	}
	public int getException() {
		return exception;
	}
	public void setException(int exception) {
		this.exception = exception;
	}
	public Date getWellTime() {
		return wellTime;
	}
	public void setWellTime(Date wellTime) {
		this.wellTime = wellTime;
	}
	public String getcPerson() {
		return cPerson;
	}
	public void setcPerson(String cPerson) {
		this.cPerson = cPerson;
	}
	public String getAcceptance() {
		return acceptance;
	}
	public void setAcceptance(String acceptance) {
		this.acceptance = acceptance;
	}
	public Date getClosure() {
		return closure;
	}
	public void setClosure(Date closure) {
		this.closure = closure;
	}
	public String getCmeasures() {
		return cmeasures;
	}
	public void setCmeasures(String cmeasures) {
		this.cmeasures = cmeasures;
	}
	public String getsAcceptance() {
		return sAcceptance;
	}
	public void setsAcceptance(String sAcceptance) {
		this.sAcceptance = sAcceptance;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	
}
