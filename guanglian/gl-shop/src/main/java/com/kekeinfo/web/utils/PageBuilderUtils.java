package com.kekeinfo.web.utils;

import com.kekeinfo.web.admin.entity.web.PaginationData;

public class PageBuilderUtils {
	
	

	public static PaginationData calculatePaginaionData( final PaginationData paginationData, final int maxshownum, final int resultCount){
        
    	int currentPage = paginationData.getCurrentPage();


    	int count = Math.min((currentPage * paginationData.getPageSize()), resultCount);  
    	paginationData.setCountByPage(count);
    	
    	int startPages = ((currentPage-1)/maxshownum)*maxshownum+1;
    	paginationData.setStartPages(startPages);
    	//一次显示5个页面导航
    	Integer totalPages= Integer.valueOf((int) (Math.ceil(Integer.valueOf(resultCount).doubleValue() / paginationData.getPageSize())));
    	if((startPages+maxshownum-1)>totalPages){
    		paginationData.setShowPages(totalPages);
    	}else{
    		paginationData.setShowPages(startPages+maxshownum-1);
    	}
    	paginationData.setTotalCount( resultCount );
        return paginationData;
    }
}
