package com.kekeinfo.web.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataTableParameter {
	
	
	private long sEcho; //请求服务器端次数
    private long iDisplayStart;//其实记录，第一条为0
    private long iDisplayLength;
    private long iColumns;
    private List<String> mDataProps; //列的Name列表
    private List<Boolean> bSortables;//列对应是否能排序
    private long iSortingCols;
    private List<Integer> iSortCols;    //排序列的编号
    private List<String> iSortColsName; //排序列的名称
    private List<String> sSortDirs;     //排布列排序形式 Asc/Desc
    private Map<String,Object> paraMap; //参数数组
    private String sSearch; //查询字段
    
    public DataTableParameter(String jsonParam){
    	 this.paraMap = covertJsonStringToHashMap(jsonParam);
    	 this.sEcho = (long) paraMap.get("sEcho"); 
         this.iDisplayStart = (long) paraMap.get("iDisplayStart");
         this.iDisplayLength = (long) paraMap.get("iDisplayLength");
         this.iColumns = (long)paraMap.get("iColumns");
         this.iSortingCols = (long)paraMap.get("iSortingCols");
         //如果前台没有查询条件，则sSearch为空
         this.sSearch = (String) paraMap.get("sSearch");
         /**
    	 this.mDataProps = new ArrayList<>();
         this.bSortables = new ArrayList<>();
         for(int i=0;i<iColumns;i++){
             String dataProp = (String) paraMap.get("mDataProp_"+i);
             Boolean sortable = (Boolean) paraMap.get("bSortable_"+i);
             mDataProps.add(dataProp);
             bSortables.add(sortable);
         }

         this.iSortCols = new ArrayList<>();
         this.sSortDirs = new ArrayList<>();
         this.iSortColsName = new ArrayList<>();
         for(int i=0;i<iSortingCols;i++){
             Integer sortCol = (Integer) paraMap.get("iSortCol_"+i);
             String sortColName = mDataProps.get(sortCol);
             String sortDir = (String) paraMap.get("sSortDir_"+i);
             iSortCols.add(sortCol);
             sSortDirs.add(sortDir);
             iSortColsName.add(sortColName);
         }*/
    }
    
	public long getsEcho() {
		return sEcho;
	}
	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}
	public long getiDisplayStart() {
		return iDisplayStart;
	}
	public void setiDisplayStart(long iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	public long getiDisplayLength() {
		return iDisplayLength;
	}
	public void setiDisplayLength(long iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}
	public long getiColumns() {
		return iColumns;
	}
	public void setiColumns(long iColumns) {
		this.iColumns = iColumns;
	}
	public List<String> getmDataProps() {
		return mDataProps;
	}
	public void setmDataProps(List<String> mDataProps) {
		this.mDataProps = mDataProps;
	}
	public List<Boolean> getbSortables() {
		return bSortables;
	}
	public void setbSortables(List<Boolean> bSortables) {
		this.bSortables = bSortables;
	}
	public long getiSortingCols() {
		return iSortingCols;
	}
	public void setiSortingCols(int iSortingCols) {
		this.iSortingCols = iSortingCols;
	}
	public List<Integer> getiSortCols() {
		return iSortCols;
	}
	public void setiSortCols(List<Integer> iSortCols) {
		this.iSortCols = iSortCols;
	}
	public List<String> getiSortColsName() {
		return iSortColsName;
	}
	public void setiSortColsName(List<String> iSortColsName) {
		this.iSortColsName = iSortColsName;
	}
	public List<String> getsSortDirs() {
		return sSortDirs;
	}
	public void setsSortDirs(List<String> sSortDirs) {
		this.sSortDirs = sSortDirs;
	}
	
	protected Map<String,Object> covertJsonStringToHashMap(String jsonParam){
		JSONParser parser = new JSONParser();
        JSONArray jsonArray;
		try {
			jsonArray = (JSONArray) parser.parse(jsonParam);
			Map<String,Object> map = new HashMap<>();
	        for(int i=0;i<jsonArray.size();i++){
	        	JSONObject obj = (JSONObject) jsonArray.get(i);
	            map.put(obj.get("name").toString(), obj.get("value"));
	        }
	        return map;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

	public Map<String, Object> getParaMap() {
		return paraMap;
	}

	public void setParaMap(Map<String, Object> paraMap) {
		this.paraMap = paraMap;
	}

	public String getsSearch() {
		return sSearch;
	}

	public void setsSearch(String sSearch) {
		this.sSearch = sSearch;
	}

	public void setsEcho(long sEcho) {
		this.sEcho = sEcho;
	}

	public void setiSortingCols(long iSortingCols) {
		this.iSortingCols = iSortingCols;
	}

	
}
