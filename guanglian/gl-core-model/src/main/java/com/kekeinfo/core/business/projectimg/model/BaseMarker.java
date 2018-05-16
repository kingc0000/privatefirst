package com.kekeinfo.core.business.projectimg.model;

import java.math.BigDecimal;

/**
 * Created by WangChong on 2018/5/10.
 */
public class BaseMarker {

    private Long id;

    private Long wellid;

    private BigDecimal markerX;

    private BigDecimal markerY;

    private BigDecimal imgX;

    private BigDecimal imgY;

    private String name;

    private String markerType;

    private BaseWell well;

    private BaseProjecImg projectImg;

    public BigDecimal getImgX() {
        return imgX;
    }

    public void setImgX(BigDecimal imgX) {
        this.imgX = imgX;
    }

    public BigDecimal getImgY() {
        return imgY;
    }

    public void setImgY(BigDecimal imgY) {
        this.imgY = imgY;
    }

    public Long getWellid() {
        return wellid;
    }

    public void setWellid(Long wellid) {
        this.wellid = wellid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMarkerX() {
        return markerX;
    }

    public void setMarkerX(BigDecimal markerX) {
        this.markerX = markerX;
    }

    public BigDecimal getMarkerY() {
        return markerY;
    }

    public void setMarkerY(BigDecimal markerY) {
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

    public BaseWell getWell() {
        return well;
    }

    public void setWell(BaseWell well) {
        this.well = well;
    }

    public BaseProjecImg getProjectImg() {
        return projectImg;
    }

    public void setProjectImg(BaseProjecImg projectImg) {
        this.projectImg = projectImg;
    }
}
