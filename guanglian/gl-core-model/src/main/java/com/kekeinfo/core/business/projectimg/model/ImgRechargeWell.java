package com.kekeinfo.core.business.projectimg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * Created by WangChong on 2018/5/6.
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "IMGRECHARGEWELL", schema=SchemaConstant.KEKEINFO_SCHEMA)
public class ImgRechargeWell extends KekeinfoEntity<Long, ImgRechargeWell> {

    private static final long serialVersionUID = 1695826342009139776L;

    @Id
    @Column(name = "RECHARGEWELL_ID", nullable=false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "RECHARGEWELL_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "MARKERX", length = 20)
    private String markerX;

    @Column(name = "MARKERY", length = 20)
    private String markerY;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProjectImg.class)
    @JoinColumn(name="PROJECTIMG_ID", nullable=true)
    private ProjectImg projectImg;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getMarkerX() {
        return markerX;
    }

    public void setMarkerX(String markerX) {
        this.markerX = markerX;
    }

    public String getMarkerY() {
        return markerY;
    }

    public void setMarkerY(String markerY) {
        this.markerY = markerY;
    }

    public ProjectImg getProjectImg() {
        return projectImg;
    }

    public void setProjectImg(ProjectImg projectImg) {
        this.projectImg = projectImg;
    }
}
