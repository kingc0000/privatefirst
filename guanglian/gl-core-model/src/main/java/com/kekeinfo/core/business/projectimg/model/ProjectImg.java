package com.kekeinfo.core.business.projectimg.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "PROJECTIMG", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class ProjectImg extends KekeinfoEntity<Long, ProjectImg> {

    private static final long serialVersionUID = -7750422391607680027L;

    @Id
    @Column(name = "PROJECTIMG_ID", nullable = false)
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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "projectImg")
    private Set<ImgDewartWell> imgDewartWells;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "projectImg")
    private Set<ImgDredgWell> imgDredgWell;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "projectImg")
    private Set<ImgObservWell> imgObservWell;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "projectImg")
    private Set<ImgRechargeWell> imgRechargeWell;

    public Set<ImgDewartWell> getImgDewartWells() {
        return imgDewartWells;
    }

    public void setImgDewartWells(Set<ImgDewartWell> imgDewartWells) {
        this.imgDewartWells = imgDewartWells;
    }

    public Set<ImgDredgWell> getImgDredgWell() {
        return imgDredgWell;
    }

    public void setImgDredgWell(Set<ImgDredgWell> imgDredgWell) {
        this.imgDredgWell = imgDredgWell;
    }

    public Set<ImgObservWell> getImgObservWell() {
        return imgObservWell;
    }

    public void setImgObservWell(Set<ImgObservWell> imgObservWell) {
        this.imgObservWell = imgObservWell;
    }

    public Set<ImgRechargeWell> getImgRechargeWell() {
        return imgRechargeWell;
    }

    public void setImgRechargeWell(Set<ImgRechargeWell> imgRechargeWell) {
        this.imgRechargeWell = imgRechargeWell;
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
}
