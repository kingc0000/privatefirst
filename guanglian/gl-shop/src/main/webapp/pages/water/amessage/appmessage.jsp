<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/css/table-response.css" />'
	rel="stylesheet">
   <div class="row" style="margin-bottom:10px">
   		 <select class="form-control " id="stype">
             	<c:forEach items="${mtypes }" var="mt">
             		<option value ="${mt.mtype}">${mt.name}</option>
             	</c:forEach>
             		<option value ="" selected = "selected">消息类别</option>
	</select>
   </div>
<div id=detail>
 </div>
 
  <div class="row" style="margin-bottom:50px">
  <div class="col-xs-6">
  <button type="button" id="lastbtn"
					class="btn btn-theme  pull-left fa fa-arrow-up "
					onclick="doalast()">上一页</button>
  </div>
  <div class="col-xs-6">
  <button id="nextbtn" type="button"
					class="btn btn-theme  pull-right fa fa-arrow-down "
					onclick="doanext()">下一页</button>
  </div>		
 </div>
 
 <script>
 var page=0;
 var total=-1;
 $('#pictitle').html("消息中心");
 function goback(){
 	javascript :history.back(-1);
 }
$(function() {
	$('#stype').change(function(){
		 getfiter();
	 });
	getdata(0);
});
function getfiter(){
	getdata(0);
}
	function writedata(data){
		var str='';
		$(data).each(function(index,ele){
			str+='<div class="row"><section class="panel"><header class="panel-heading">';
			str+='<a href="javascript:;"> <i class="fa fa-bell-o"></i> '+ele.message.typename+'<span class="label pull-right" style="color: #337ab7;">'+ele.message.dateCreated+'</span></a>';
			str+='</header><div class="panel-body" style="padding:10px 15px;">';
			if(ele.statu==0){
				str+='<a href="javascript:;" onclick="readdata('+ele.id+')">'+ ele.message.title+'<br>'+ele.message.message+'</a>';
			}else{
				str+=ele.message.title+'<br>'+ele.message.message;
			}
			str+='</div></section></div>';
		  });
		$("#detail").html(str);
	}
	function doanext(){
		//检查是否最后一页
		if(total!=-1){
			if(page*5>total){
				
				toastr.error('已加载全部数据...');
				return;
			}
		}
		page++;
		getdata(page);
	}
	function doalast(){
		//检查是否最后一页
		if(page==0){
			toastr.error('已经是第一页...');
				return;
		}
		page--;
		getdata(page);
	}
	function getdata(mpage){
		
		$.ajax({
            type: 'post',
            url: 'applist.shtml?page=' + (mpage)+'&mtype='+$("#stype").val() ,
            success: function (data) {
            	if(data!=null ){
            		if(data.data!=null && data.data.length>0){
            			writedata(data.data);
            			total=data.total;
            		}else{
            			$("#detail").html('当前数据为空');
                		
                		total=0;
            		}
            	}else{
					$("#detail").html('当前数据为空');
            		
            		total=0;
            	}
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	toastr.error('数据加载失败，请重试！');
            }
        });
	}
	
	function readdata(sid) {
		
		
		var url = "readed.shtml";
		
			$('#loading').show();
		$.ajax({
			type : "POST",
			url : url,
			data : "listId=" + sid,
			success : function(result) {
				$('#loading').hide();
			},
			
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				
			}
		});
		$('#edittable').hide();
	}
</script>