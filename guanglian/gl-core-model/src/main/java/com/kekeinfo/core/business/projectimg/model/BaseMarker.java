package com.kekeinfo.core.business.projectimg.model;

/**
 * Created by WangChong on 2018/5/10.
 */
public class BaseMarker {

    private String markerX;

    private String markerY;

    private String name;

    private String markerType;

    private BaseWell well;

    private BaseProjecImg projectImg;

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
