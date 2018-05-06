package com.kekeinfo.web.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class DataTable<T>{

    //private String aaData;//数据
    private int iTotalDisplayRecords;//得到的记录数
    private int iTotalRecords;//数据库中记录数
    private long sEcho; //请求服务器端次数
    private boolean supportEnglish=false;
    private boolean hasRight =true;
    private List<T> aaData = new ArrayList<T>();
    //getter and setter
	/**
    public String getAaData() {
		return aaData;
	}
	public void setAaData(String aaData) {
		this.aaData = aaData;
	}*/
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public int getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public long getsEcho() {
		return sEcho;
	}
	public void setsEcho(long sEcho) {
		this.sEcho = sEcho;
	}
	/**
	public String toJSONString(){
		Gson gson = new Gson();
		String json = gson.toJson(jsonElement)
	}
	public String getAaDataJson(Object o,Boolean last) {
		StringBuffer json = new StringBuffer();
		json.append("{");
		Field[] fields=o.getClass().getDeclaredFields();
		for(int i=0;i<fields.length-1;i++){
			json.append("\"").append(fields[i].getName()).append("\":");
			json.append(getFieldValueByName(fields[i].getName(), o));
		}
		//最后一个没有逗号
		json.append("}");
		if(!last){
			json.append(",");
		}
       return null;
    }

	/**
	 * 根据属性名获取属性值
	 * */
	/**
    private Object getFieldValueByName(String fieldName, Object o) {
        try {  
            String firstLetter = fieldName.substring(0, 1).toUpperCase();  
            String getter = "get" + firstLetter + fieldName.substring(1);  
            Method method = o.getClass().getMethod(getter, new Class[] {});  
            Object value = method.invoke(o, new Object[] {});  
            return value;  
        } catch (Exception e) {  
            //log.error(e.getMessage(),e);  
            return null;  
        }  
    }*/
	public List<T> getAaData() {
		return aaData;
	}
	public void setAaData(List<T> aaData) {
		this.aaData = aaData;
	}
	public boolean isSupportEnglish() {
		return supportEnglish;
	}
	public void setSupportEnglish(boolean supportEnglish) {
		this.supportEnglish = supportEnglish;
	}
	public boolean isHasRight() {
		return hasRight;
	}
	public void setHasRight(boolean hasRight) {
		this.hasRight = hasRight;
	}
	
	/**
	 * 将DataTable对象转化为JSON格式字符串
	 * @return
	 */
	public String toJSONString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(this);
			return json;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
   
}
