package com.kekeinfo.core.business.common.model;

public class Criteria {
	
	private long startIndex = 0;
	private long maxCount = 10;
	private String code;
	//翻页的页码
	private int page=1;
	private long total=0;
	private long showpage=0;
	private long startpage=1;
	//每次显示5个页码供选择
	private int fourpage=4;
	
	
	private CriteriaOrderBy orderBy = CriteriaOrderBy.DESC;
	
	public void init(){
		this.startIndex=(this.page - 1) * this.maxCount; 
	}
	
	public long getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}
	public long getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setOrderBy(CriteriaOrderBy orderBy) {
		this.orderBy = orderBy;
	}
	public CriteriaOrderBy getOrderBy() {
		return orderBy;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
		//设置最多显示页数
		this.showpage=(this.total-this.startIndex)/this.maxCount;
		if((this.total-this.startIndex)%this.maxCount>0){
			this.showpage++;
		}
		//换下一个5页
		if((this.startpage+fourpage)<this.page){
			/**
			long tpage = this.total/this.maxCount;
			if(this.total%this.maxCount>0){
				tpage++;
			}*/
			//取整数
			this.startpage=(this.page/(this.fourpage+1))*(this.fourpage+1);
			/**
			//最后一页
			if(this.page==tpage){
				
			}
			this.startpage=this.startpage+1l;*/
		}
		if((this.startpage+fourpage)<this.showpage){
			this.showpage=(this.page+4);
		}
		
		
	}

	public long getShowpage() {
		return showpage;
	}

	public void setShowpage(long showpage) {
		this.showpage = showpage;
	}

	public long getStartpage() {
		return startpage;
	}

	public void setStartpage(long startpage) {
		this.startpage = startpage;
	}
	

}