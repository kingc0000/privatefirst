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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kekeinfo.core.business.common.model.audit.AuditListener;
import com.kekeinfo.core.business.dewatering.model.Dewatering;
import com.kekeinfo.core.business.generic.model.KekeinfoEntity;
import com.kekeinfo.core.business.pumpwell.model.Pumpwell;
import com.kekeinfo.core.constants.SchemaConstant;

/**
 * Created by WangChong on 2018/5/9.
 */
@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "DEWATERINGMARKER", schema = SchemaConstant.KEKEINFO_SCHEMA)
public class DewateringMarker extends KekeinfoEntity<Long, DewateringMarker> {

    private static final long serialVersionUID = 4048050641859661550L;

    @Id
    @Column(name = "DEWATERINGMARKER_ID", unique=true, nullable=false)
    @TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName =
            "SEQ_COUNT", pkColumnValue = "DEWATERINGMARKER_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    // 标记X坐标
    @Column(name = "MARKERX", length = 20)
    private String markerX;

    // 标记Y坐标
    @Column(name = "MARKERY", length = 20)
    private String markerY;

    // 标记名称（保留）
    @Column(name = "NAME", length = 100)
    private String name;

    // 标记类型
    @Column(name = "MARKERTYPE", length = 20)
    private String markerType;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEWATERING_ID")
    private Dewatering well;

    @JsonIgnore
    @ManyToOne(targetEntity = ProjectImg.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECTIMG_ID", nullable = false)
    private ProjectImg projectImg;

    public ProjectImg getProjectImg() {
        return projectImg;
    }

    public void setProjectImg(ProjectImg projectImg) {
        this.projectImg = projectImg;
    }

    public Long getId() {
        return id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarkerType() {
        return markerType;
    }

    public void setMarkerType(String markerType) {
        this.markerType = markerType;
    }

    public Dewatering getWell() {
        return well;
    }

    public void setWell(Dewatering well) {
        this.well = well;
    }
}
