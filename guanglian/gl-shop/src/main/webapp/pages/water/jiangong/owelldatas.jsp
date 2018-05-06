<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>		
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />' rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">		
<link href='<c:url value="/resources/assets/bootstrap-datetimepicker/css/datetimepicker.css" />' rel="stylesheet">		
<script type="text/javascript">
		$('#loading').show();
		var pid ="${pid}";
		//设置title
		var pageTitle="观测井${pName}";
		$('#pictitle').html(pageTitle);
		$('#panel-heading').html(pageTitle);
		var phonetype='${ptype}';
</script>

<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading"> <span id="panel-heading"></span>列表 
		
	</header>
	<div class="panel-body">
		<div class="col-lg-12 col-sm-12">
			<section class="adv-table " id="no-more-tables">
             <table class="display table table-bordered table-striped" id="slist">
                 <thead>
                 <tr>
                     <th><input class="allCheckbox" type="checkbox"/></th>
                     <th>水位</th>
                     <th>水温</th>
                     <th>观测时间</th>
                     <th>操作人</th>
                     <th>更新时间</th>
                     <th>水位阈值上限</th>
                     <th>水位阈值下限</th>
                     <th>水温阈值</th>
                     <th>是否超限</th>
                 </tr>
                 </thead>
             </table>
            </section>
		</div>
		</div>
</section>
</div>
</div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
var permissionTree = new Array();
var groupid=-1;
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
			"oLanguage": {
				"oPaginate":opaging
				},
			"sDom" : "lfrtip",
			"sAjaxSource": "server_processing.shtml?pid="+pid,
			"fnServerData": retrieveGoods,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "water"},
		                    { "mData": "temperature"},
		                    { "mData": "dateCreated"},
		                    { "mData": "modifiedBy"},
		                    { "mData": "dateModified"},
		                    { "mData": "waterThreshold"},
		                    { "mData": "waterDown"},
		                    { "mData": "temperatureThreshold"},
		                    { "mData": "status"}
		                ],
                "aoColumnDefs": [
                             {"bVisible": true, "aTargets": [0],  "mRender":function(data,type,full){
                              	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                              },"bSortable": false, 
                             },
                             {"aTargets":[9],"mRender":function(data,type,full){
                            	 var reStr='否';
                            	 if(data>0){
                            		 reStr='是';
                            	 }
                            	 return reStr;
                             	}
                            },
                            {
                            	"aTargets":["_all"],"bSortable": false
                            }
                         ],
         "fnInitComplete": function(settings, json) {
        	 if(isTouchDevice()===false) {
        		 $('.tooltips').tooltip();
        		}
        	 definedSidCheckbox();
        	 $('#loading').hide();
         }
			
		} 
		);
		//加载日期控件
		$('.datetimeControl').datetimepicker({
			format: "yyyy-mm-dd hh:ii:ss",
		    autoclose: true,
		    todayBtn: true,
		    minuteStep:1,
		    pickerPosition: "bottom-left"
		});
		
	});

	
	
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
			url : "server_processing.shtml?pid="+pid,
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
	
</script>
<script src='<c:url value="/resources/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" />'></script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
