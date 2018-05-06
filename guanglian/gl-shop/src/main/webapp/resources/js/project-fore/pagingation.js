/**
 * 分页标签
 * @param paginationData
 * 
 */
function writePaging(paginationData){
	$("#pagination").html("");
	if(paginationData !=null){
		var pages = '<ul class="pagination pagination-sm">';
		 if(paginationData.startPages !=1){
			 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+1+')"><span class="glyphicon glyphicon-step-backward" style="line-height:17px" aria-hidden="true"></span></a></li>';
			 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+(paginationData.startPages-1)+')"><span class="glyphicon glyphicon-backward" style="line-height:17px" aria-hidden="true"></span></a></li>';
		 }
		 for(var i =paginationData.startPages;i<=paginationData.showPages;i++){
			 if(paginationData.currentPage == i){
				 pages += '<li class="active"><a href="javascript:void(0);" class="disabled">'+i+'</a></li>';
			 }else{
				 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+i+')">'+i+'</a></li>';
			 }
		 }
		 if(paginationData.startPages+4 <=paginationData.totalPages){
			 
			 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+(paginationData.startPages+5)+')"><span class="glyphicon glyphicon-forward" style="line-height:17px" aria-hidden="true"></a></li>';
			 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+paginationData.totalPages+')"><span class="glyphicon glyphicon-step-forward" style="line-height:17px" aria-hidden="true"></a></li>';
		 }
		 pages += '</ul>';
	}
	 $("#pagination").html(pages);
}