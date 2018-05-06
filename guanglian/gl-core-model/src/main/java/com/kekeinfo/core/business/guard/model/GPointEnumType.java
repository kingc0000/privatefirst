package com.kekeinfo.core.business.guard.model;
/**
 * 1: 降水井，2：疏干井；3：回灌井；4：观测井；5：环境监测
 * @author sam
 *
 */
public enum GPointEnumType {

	VerticalDis(1, "结构垂直位移"), DiameterConvert(2, "直径收敛"),Hshift(3, "水平位移");
	
	private int code;
	private String name;
	
	private GPointEnumType(int code, String name) {
		this.code = code;
		this.name = name;
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
	
	public static GPointEnumType getType(int index) {  
        for (GPointEnumType c : GPointEnumType.values()) {  
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
