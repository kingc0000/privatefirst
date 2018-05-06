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
<link href='<c:url value="/resources/css/fileicon.css" />' rel="stylesheet">	
<link href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />' rel="stylesheet">	
<style>
select{
	width: 100%;
}
</style>	
<script type="text/javascript">
$('#loading').show();
	var mid ="${mid}";
	function goback(){
		javascript :history.back(-1);
	}
</script>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
		<header class="panel-heading"> 台账列表 
		</header>
		<div class="panel-body">
	      	<div id="find">
				<ul class="nav nav-tabs" role="tablist">
					<c:forEach var="mpoint" items="${ms}" varStatus="status">
						<li <c:if test="${status.index==0}"> class="active"</c:if>>
							<a href="#tab-table${status.index+1}" id="taba${status.index+1}" data-toggle="tab" aria-expanded="true" data-index="${status.index+1}" data-type="${mpoint.id}">${mpoint.pName}</a>
						</li>
					</c:forEach>
				</ul>
				<div class="adv-table tab-content" style="padding-top:20px;">
					<c:forEach var="mpoint" items="${ms}" varStatus="status">
						<div class="tab-pane <c:if test="${status.index==0} "> active</c:if>" id="tab-table${status.index+1}">
						<div class="col-lg-12" id="no-more-tables">
						<table class="display table table-bordered table-striped" id="slist${status.index+1}" data-type="${mpoint.id}">
			                <thead>
			                	<tr class="text-center">
				                     <th>设备名称</th>
				                     <th>设备编号</th>
				                     <th>进场日期</th>
				                     <th>离厂日期</th>
				                     <th>使用时间</th>
				                     <th>当前状态</th>
				                     <th>备注</th>
				                </tr>
			                </thead>
			            </table>
			            </div>
						</div>
					</c:forEach>
				</div>
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
$('#pictitle').html("${mentity.name }台账信息");
jQuery(document).ready(function() {
	$('a[data-toggle="tab"]').on( 'shown.bs.tab', function (e) {
		var datatype = $(e.target).attr("data-type");
		var index= $(e.target).attr("data-index");
		var table = $.fn.dataTable.fnTables(true);
		if ( table.length > 0 ) {
			$(table).dataTable().fnAdjustColumnSizing();
			$(table).dataTable().fnReloadAjax()
		}else{
			getWarningdatas(datatype,"#slist"+index);
		}
    } );
	//初始化第一个
	var inf=$('#taba1').attr("data-index");
	var tyf=$("#taba1").attr("data-type");
	if(typeof(inf)!="undefined"){
		getWarningdatas(tyf,"#slist"+inf);
		$("#tab-table1").addClass("active");
	}
	
});

function getWarningdatas(datatype,tableId) {
	var params = {
			"bPaginate" : true, //不显示分页
			"bProcessing" : true,
			"bServerSide" : true,
			"bLengthChange" : true,
			"bFilter" : false,
			"bSortClasses" : false,
			"destroy":true,
			"retrieve":true,
			"sDom" : "lfrtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource": "meqps_processing.shtml?mid="+mid+"&type="+datatype,
			"fnServerData": retrieveData
			};
	$(tableId).dataTable( $.extend({}, params, {
		"sDom" : "lfrtip",
		"aoColumns": [
	                    { "mData": "monitorEqip.equip.name"},
	                    { "mData": "monitorEqip.equip.eNO"},
	                    { "mData": "monitorEqip.entryDate"},
	                    { "mData": "monitorEqip.exitVaild"},
	                    { "mData": "auditSection.dateCreated"},
	                    { "mData": "used"},
	                    { "mData": "monitorEqip.memo"}
	                ],
        "aoColumnDefs": [
			 
			{"aTargets":[5],"mRender":function(data,type,full){
				if(full.used==true) {
					return "正在使用";
				} else {
					return "没有使用";
				}}
			},
			 {
				"aTargets" : [ "_all" ],
				"bSortable" : false
			} ],
         "fnInitComplete": function(settings, json) {
        	 if(isTouchDevice()===false) {
        		 $('.tooltips').tooltip();
        		}
        	 $('#loading').hide();
         }
	}));
	
}
function afterRemove(response, params) {
	var index = params.indexOf("type");
	var type = params.substr(index+5);
	var tab = $("#slist"+type).dataTable();
	var oSettings = tab.fnSettings();
	tab.fnReloadAjax(oSettings);
}

</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>