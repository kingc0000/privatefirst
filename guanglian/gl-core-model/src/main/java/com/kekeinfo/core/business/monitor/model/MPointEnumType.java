package com.kekeinfo.core.business.monitor.model;
/**
 *
 *
 */
public enum MPointEnumType {

	Surface(3, "周边地表"), Building(4, "建筑物"), WaterLine(2, "管线"), RingBeam(6, "圈梁变形"), SupAxial(8, "支撑轴力"), 
	UpRight(5, "立柱变形"), HiddenLine(7, "潜层水位"),Displacement(1,"水平位移");
	
	private int code;
	private String name;
	private String mtype;
	
	public String getMtype() {
		return mtype;
	}

	

	private MPointEnumType(int code, String name) {
		this.code = code;
		this.name = name;
		this.mtype=this.toString();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static MPointEnumType getType(int index) {  
        for (MPointEnumType c : MPointEnumType.values()) {  
            if (c.getCode() == index) {  
                return c;  
            }  
        }  
        return null;  
    } 
	
	public String getType() {
		return this.toString();
	}
}
