package com.kekeinfo.core.business.projectimg.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import com.kekeinfo.core.business.common.model.audit.Auditable;
import com.kekeinfo.core.business.constructionsite.model.ConstructionSite;
import com.kekeinfo.core.business.daily.model.GuardDailyImage;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.point.model.Basepoint;
import com.kekeinfo.core.business.point.model.BasepointLink;
import com.kekeinfo.core.business.project.model.Project;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * Created by WangChong on 2018/5/6.
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "PROJECTIMG", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class ProjectImg extends KekeinfoEntity<Long, ProjectImg> {

    private static final long serialVersionUID = -7750422391607680027L;

    @Id
    @Column(name = "PROJECTIMG_ID", unique=true, nullable=false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName =
            "SEQ_COUNT", pkColumnValue = "PROJECTIMG_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    // 图片地址
    @Column(name = "URL", length = 200)
    private String url;

    // 图片名称（保留）
    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "MEMO", length = 1000)
    private String memo;

    // 保留字段
    @Column(name = "PICTYPE", length = 200)
    private String picType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSITE_ID", nullable = true)
    private ConstructionSite csite;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "projectImg")
    private Set<DeformmonitorMarker> dmMarkers = new HashSet<DeformmonitorMarker>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "projectImg")
    private Set<DewateringMarker> dwMarkers = new HashSet<DewateringMarker>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "projectImg")
    private Set<InvertedWellMarker> iMarkers = new HashSet<InvertedWellMarker>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "projectImg")
    private Set<ObserveWellMarker> oMarkers = new HashSet<ObserveWellMarker>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "projectImg")
    private Set<PumpWellMarker> pMarkers = new HashSet<PumpWellMarker>();

    public Set<DeformmonitorMarker> getDmMarkers() {
        return dmMarkers;
    }

    public void setDmMarkers(Set<DeformmonitorMarker> dmMarkers) {
        this.dmMarkers = dmMarkers;
    }

    public Set<DewateringMarker> getDwMarkers() {
        return dwMarkers;
    }

    public void setDwMarkers(Set<DewateringMarker> dwMarkers) {
        this.dwMarkers = dwMarkers;
    }

    public Set<InvertedWellMarker> getiMarkers() {
        return iMarkers;
    }

    public void setiMarkers(Set<InvertedWellMarker> iMarkers) {
        this.iMarkers = iMarkers;
    }

    public Set<ObserveWellMarker> getoMarkers() {
        return oMarkers;
    }

    public void setoMarkers(Set<ObserveWellMarker> oMarkers) {
        this.oMarkers = oMarkers;
    }

    public Set<PumpWellMarker> getpMarkers() {
        return pMarkers;
    }

    public void setpMarkers(Set<PumpWellMarker> pMarkers) {
        this.pMarkers = pMarkers;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    public ConstructionSite getCsite() {
        return csite;
    }

    public void setCsite(ConstructionSite csite) {
        this.csite = csite;
    }
}
