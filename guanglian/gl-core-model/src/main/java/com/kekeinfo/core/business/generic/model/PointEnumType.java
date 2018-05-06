package com.kekeinfo.core.business.generic.model;
/**
 * 1: 降水井，2：疏干井；3：回灌井；4：观测井；5：环境监测
 * @author sam
 *
 */
public enum PointEnumType {

	PUMP(1, "降水井"), DEWATERING(2, "疏干井"), INVERTED(3, "回灌井"), OBSERVE(4, "观测井"), DEFORM(5, "环境监测");
	
	private int code;
	private String name;
	
	private PointEnumType(int code, String name) {
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
	public String getType() {
		return this.toString();
	}
	public static PointEnumType getType(int index) {  
        for (PointEnumType c : PointEnumType.values()) {  
            if (c.getCode() == index) {  
                return c;  
            }  
        }  
        return null;  
    } 
	public static void main(String[] args) {
		System.out.println(PointEnumType.getType(1));
	}	
}
