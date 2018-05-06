<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />' rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">	
<link href='<c:url value="/resources/assets/bootstrap-datetimepicker/css/datetimepicker.css" />' rel="stylesheet">		
<script type="text/javascript">
		$('#loading').show();
		var cid ="${cid}";
		$("#pictitle").html("每日报表");
</script>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading"> <span id="panel-heading"></span>列表 
		<c:if test="${hasRight==true }">
			<button type="button"  class="btn btn-theme pull-right fa fa-plus-square" onclick="creport()">手动生成报表</button>
		</c:if>
	</header>
	<div class="panel-body">
		<div class="col-lg-12 col-sm-12">
			<section class="adv-table " id="no-more-tables">
             <table class="display table table-bordered table-striped" id="slist">
                 <thead>
                 <tr>
                     <th>报表日期</th>
                     <th>生成时间</th>
                     <th>操作</th>
                 </tr>
                 </thead>
             </table>
            </section>
		</div>
		</div>
</section>
</div>
</div>
<a id="openw" style="display: none" href="" target="_blank"></a>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
var tar=' target="_blank"';
if($(window).width()<768){
	tar='';
}
function goback(){
	javascript :history.back(-1);
}
	jQuery(document).ready(function() {
		$('#slist').dataTable( {
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": false,
			"bSortClasses": false,
			"sDom" : "lfrtip",
			"sAjaxSource": "server_processing.shtml?cid="+cid,
			"fnServerData": retrieveGoods,
			"oLanguage": {
				"oPaginate":opaging
				},
			"aoColumns": [  
		                    { "mData": "rDate"},
		                    { "mData": "dateCreated"},
		                    { "mData": "id" }
		                ],
                "aoColumnDefs": [
                             {"aTargets":[2],"mRender":function(data,type,full){
                            	 var reStr='';
                            	 <c:if test="${hasRight==true }">
                            	 	reStr+='<a href="<c:url value="/water/report/preport.html?rid='+data+'&cid='+cid+'"/>" '+tar+'" class="btn btn-danger btn-xs tooltips" data-container="body" data-placement="left" data-original-title="查看"  ><i class="fa fa-eye "></i></a>'; 
             					</c:if>
                                	return reStr;
                             	},
                             	"sWidth":"120px","bSortable": false
                            },
                            {
                            	"aTargets":["_all"],"bSortable": false
                            }
                         ],
         "fnInitComplete": function(settings, json) {
        	 if(isTouchDevice()===false) {
        		 $('.tooltips').tooltip();
        		}
        	 $('#loading').hide();
         }
			
		} 
		);
	});

	function jssubmit(){
		 $("#storeform").submit();
	}
	
	function retrieveGoods(sSource, aoData, fnCallback) {
		$('#loading').show();
		var data = {
			"aoData" : JSON.stringify(aoData)
		};
		if (!jQuery.isEmptyObject(filterjson)) {
			data = $.extend({}, {
				"aoData" : JSON.stringify(aoData)
			}, filterjson);
		}
		$.ajax({
			type : "POST",
			url : "server_processing.shtml?cid="+cid,
			dataType : "json",
			data : data,
			success : function(data) {
				fnCallback(data); // 服务器端返回的对象的returnObject部分是要求的格式
				$('#loading').hide();
			},
			error : function(data, xhr, textStatus, errorThrown) {
				console.log(data.responseText);
				fnCallback(data);
				$('#loading').hide();
			}
		});
	}
	function creport(){
		$.ajax({
			type : "POST",
			url : "creport.shtml?cid="+cid,
			dataType : "json",
			success : function(data) {
				$('#loading').hide();
				if(data!=null && data!=""){
					showr(data);
				}
				
			},
			error : function(data, xhr, textStatus, errorThrown) {
				console.log(data.responseText);
				$('#loading').hide();
			}
		});
	}
	
	function showr(rid){
		if($(window).width()<768){
			window.location.href=_context+"/water/report/preport.html?rid="+rid+"&cid="+cid;
		}else{
			refreshData();
			$("#openw").attr("href",_context+"/water/report/preport.html?rid="+rid+"&cid="+cid); 
			document.getElementById("openw").click();  
			//$("#openw").click();  
			//window.open(_context+"/water/report/preport.html?rid="+rid);
		}
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>