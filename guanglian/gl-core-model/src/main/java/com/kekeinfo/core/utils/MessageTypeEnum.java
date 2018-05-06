package com.kekeinfo.core.utils;



public enum MessageTypeEnum {
	EVerifyNotice(1, "设备检定"), GIssuesNotice(2, "项目问题"), MFeedback(4, "问题反馈"), GFeedback(5, "问题反馈"), 
	GJobSigning(6, "作业签到"), GComplete(7, "作业销点"), GSignFinish(8,"签到完成"), GComFinish(9,"销点完成"), WCommet(10,"评论"),
	WWarning(11,"数据采集告警"), WPower(12,"断电告警"),GJobA(13,"工作安排"),GAlarm(14,"工作提醒");
	
	private int code;
	private String name;
	
	@SuppressWarnings("unused")
	private String mtype;
	
	

	private MessageTypeEnum(int code, String name) {
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
	
	public static MessageTypeEnum getType(int index) {  
        for (MessageTypeEnum c : MessageTypeEnum.values()) {  
            if (c.getCode() == index) {  
                return c;  
            }  
        }  
        return null;  
    } 
	
	public String getType() {
		return this.toString();
	}

	public String getMtype() {
		return this.toString();
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

}
