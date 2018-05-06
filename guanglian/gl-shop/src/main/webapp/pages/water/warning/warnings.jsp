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
	var cid ="${csite.id}";
	var gateflag = false; //该项目下是否有网关
	var gatewaystr='', nodestr='', channelstr='';
	function goback(){
		javascript :history.back(-1);
	}
</script>

<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
		<header class="panel-heading"> 项目数据/断电信息告警列表 
		</header>
		<div class="panel-body">
	      	<div id="find">
				<ul class="nav nav-tabs" role="tablist">
					<li class="active">
						<a href="#tab-table1" data-toggle="tab" aria-expanded="true" data-type="1">降水井</a>
					</li>
					<li class="">
						<a href="#tab-table2" data-toggle="tab" aria-expanded="false" data-type="2">疏干井</a>
					</li>
					<li class="">
						<a href="#tab-table3" data-toggle="tab" aria-expanded="false" data-type="3">回灌井</a>
					</li>
					<li class="">
						<a href="#tab-table4" data-toggle="tab" aria-expanded="false" data-type="4">观测井</a>
					</li>
					<li class="">
						<a href="#tab-table5" data-toggle="tab" aria-expanded="false" data-type="5">环境监测</a>
					</li>
				</ul>
				<div class="adv-table tab-content" style="padding-top:20px;">
					<div class="tab-pane active" id="tab-table1">
						<div class="col-lg-12" id="no-more-tables">
						<table class="display table table-bordered table-striped" id="slist1" data-type="1">
			                <thead>
			                	<tr class="text-center">
				                     <th>名称</th>
				                     <th>告警类型</th>
				                     <th>流量</th>
				                     <th>水位</th>
				                     <th>初始告警时间</th>
				                     <th>最后告警时间</th>
				                     <th>流量阈值</th>
				                     <th>水位阈值上限</th>
				                     <th>水位阈值下限</th>
				                     <th>是否超限</th>
				                     <th>操作</th>
				                </tr>
			                </thead>
			            </table>
			            </div>
					</div>
					<div class="tab-pane" id="tab-table2">
						<div class="col-lg-12" id="no-more-tables">
						<table class="display table table-bordered table-striped" id="slist2" data-type="2">
			                <thead>
			                	<tr class="text-center">
			                	 <th>名称</th>
			                     <th>告警类型</th>
			                     <th>流量</th>
			                     <th>水位</th>
			                     <th>初始告警时间</th>
			                     <th>最后告警时间</th>
			                     <th>流量阈值</th>
			                     <th>水位阈值上限</th>
			                     <th>水位阈值下限</th>
			                     <th>是否超限</th>
			                     <th>操作</th>
				                </tr>
			                </thead>
			            </table>
			            </div>
					</div>
					<div class="tab-pane" id="tab-table3">
						<div class="col-lg-12" id="no-more-tables">
						<table class="display table table-bordered table-striped" id="slist3" data-type="3">
			                <thead>
			                	<tr class="text-center">
			                	 <th>名称</th>
			                     <th>告警类型</th>
			                     <th>流量</th>
			                     <th>井内水位</th>
			                     <th>初始告警时间</th>
			                     <th>最后告警时间</th>
			                     <th>流量阈值</th>
			                     <th>井内水位阈值</th>
			                     <th>是否超限</th>
			                     <th>操作</th>
				                </tr>
			                </thead>
			            </table>
			            </div>
					</div>
					<div class="tab-pane" id="tab-table4">
						<div class="col-lg-12" id="no-more-tables">
						<table class="display table table-bordered table-striped" id="slist4">
			                <thead>
			                	<tr class="text-center">
			                		<th>名称</th>
			                     	<th>告警类型</th>
					                <th>水位</th>
				                    <th>水温</th>
				                    <th>初始告警时间</th>
			                     	<th>最后告警时间</th>
				                    <th>水位阈值上限</th>
				                    <th>水位阈值下限</th>
				                    <th>水温阈值</th>
				                    <th>是否超限</th>
				                    <th>操作</th>
				                </tr>
			                </thead>
			            </table>
			            </div>
					</div>
					<div class="tab-pane" id="tab-table5">
						<div class="col-lg-12" id="no-more-tables">
						<table class="display table table-bordered table-striped" id="slist5" data-type="5">
			                <thead>
			                	<tr class="text-center">
				                 <th>名称</th>
			                     <th>告警类型</th>
				                 <th>环境数据</th>
			                     <th>初始告警时间</th>
			                     <th>最后告警时间</th>
			                     <th>阈值</th>
			                     <th>是否超限</th>
			                     <th>操作</th>
				                </tr>
			                </thead>
			            </table>
			            </div>
					</div>
				</div>
			</div>
	    </div>
	</section>
	</div>
</div>
<div>
	<p class="alert alert-warning">告警类型分为采集数据告警和断电告警，如果为断电告警，则对应的采集数据和数据阀值均没有值；<br/>初始告警时间为该测点本次起始告警时间，最后告警时间为该测点本次最后的告警时间。</p>
</div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
jQuery(document).ready(function() {
	$('a[data-toggle="tab"]').on( 'shown.bs.tab', function (e) {
		var datatype = $(e.target).attr("data-type");
		filterjson= {};  //过滤条件清空
		$("#wclass"+datatype+" option:last").prop("selected", "selected");
		var table = $.fn.dataTable.fnTables(true);
		if ( table.length > 0 ) {
			$(table).dataTable().fnAdjustColumnSizing();
		}
    } );
	$('table.table').each(function(i){
		var index = i+1;
		getWarningdatas(index, "#slist"+index);
	});
});

function getWarningdatas(datatype, tableId) {
	var params = {
			"bPaginate" : true, //不显示分页
			"bProcessing" : true,
			"bServerSide" : true,
			"bLengthChange" : true,
			"bFilter" : true,
			"bSortClasses" : false,
			"sDom" : "lfrtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource": "server_processing.shtml?cid="+cid+"&type="+datatype,
			"fnServerData": retrieveData
			};
	if(datatype=="1"||datatype=="2") { //降水井、疏干井 
		$(tableId).dataTable( $.extend({}, params, {
			"sDom" : "lf<'#mytool"+datatype+"'>rtip",
			"aoColumns": [
		                    { "mData": "point.name"},
		                    { "mData": "warningType"},
		                    { "mData": "flow"},
		                    { "mData": "water"},
		                    { "mData": "auditSection.dateCreated"},
		                    { "mData": "auditSection.dateModified"},
		                    { "mData": "flowThreshold"},
		                    { "mData": "waterThreshold"},
		                    { "mData": "waterDown"},
		                    { "mData": "status"},
		                    { "mData": "id" }
		                ],
            "aoColumnDefs": [
				 {"aTargets":[1],"mRender":function(data,type,full){
					 if(data==0){
						 return "<span class='text-warning'>数据告警</span>";
					 } else return "<span class='text-danger'>断电告警</span>";
				 }
				},
				{"aTargets":[9],"mRender":function(data,type,full){
					if(full.warningType==1) {
						return "";
					} else {
						switch (data) {
						case 0:
							return "正常";
						case 1:
							return "流量告警";
						case 2:
							return "水位告警";
						default:
							return "全告警";
						}
					}}
				},
				{"aTargets":[2,3,6,7,8],"mRender":function(data,type,full){
					if(full.warningType==1){ //断电告警数据不显示信息
						 return "";
					 } else return data;
					}
				},
				 {"aTargets":[10],"mRender":function(data,type,full){
					 var reStr='';
					 <c:if test="${hasRight==true }">
					 	reStr+='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-container="body" data-placement="left" data-original-title="删除"  onclick="deldata(\''+data+'&type='+datatype+'\')"><i class="fa fa-trash-o "></i></a>'; 
						</c:if>
				    	return reStr;
				 	},
				 	"sWidth":"120px","bSortable": false
				}, {
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
		var dseelct ='<div style="padding: 15px 5px 15px 0;" class="pull-right"><select id="wclass'+datatype+'" class="form-control">'+
		'<option value ="0">数据告警</option><option value ="1">断电告警</option>'+
		'<option value ="" selected = "selected">告警类型</option></select></div>';
		 $("#mytool"+datatype).html(dseelct);
		 $("#wclass"+datatype).change(function(){
			 getfiter(datatype);
			 var tab = $("#slist"+datatype).dataTable();
			var oSettings = tab.fnSettings();
			tab.fnReloadAjax(oSettings);
		 });
	} else if (datatype=="3") { //回灌井 
		$(tableId).dataTable( $.extend({}, params, {
			"sDom" : "lf<'#mytool"+datatype+"'>rtip",
			"aoColumns": [
		                    { "mData": "point.name"},
		                    { "mData": "warningType"},
		                    { "mData": "flow"},
		                    { "mData": "pressure"},
		                    { "mData": "auditSection.dateCreated"},
		                    { "mData": "auditSection.dateModified"},
		                    { "mData": "flowThreshold"},
		                    { "mData": "pressureThreshold"},
		                    { "mData": "status"},
		                    { "mData": "id" }
		                ],
            "aoColumnDefs": [
				 {"aTargets":[1],"mRender":function(data,type,full){
					 if(data==0){
						 return "<span class='text-warning'>数据告警</span>";
					 } else return "<span class='text-danger'>断电告警</span>";
				 }
				},
				{"aTargets":[8],"mRender":function(data,type,full){
					if(full.warningType==1) {
						return "";
					} else {
						switch (data) {
						case 0:
							return "正常";
						case 1:
							return "流量告警";
						case 2:
							return "井内水位告警";
						default:
							return "全告警";
						}
					}}
				},
				{"aTargets":[2,3,6,7],"mRender":function(data,type,full){
					if(full.warningType==1){ //断电告警数据不显示信息
						 return "";
					 } else return data;
					}
				},
				 {"aTargets":[9],"mRender":function(data,type,full){
					 var reStr='';
					 <c:if test="${hasRight==true }">
					 reStr+='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-container="body" data-placement="left" data-original-title="删除"  onclick="deldata(\''+data+'&type='+datatype+'\')"><i class="fa fa-trash-o "></i></a>'; 
						</c:if>
				    	return reStr;
				 	},
				 	"sWidth":"120px","bSortable": false
				}, {
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
		var dseelct ='<div style="padding: 15px 5px 15px 0;" class="pull-right"><select id="wclass'+datatype+'" class="form-control">'+
		'<option value ="0">数据告警</option><option value ="1">断电告警</option>'+
		'<option value ="" selected = "selected">告警类型</option></select></div>';
		 $("#mytool"+datatype).html(dseelct);
		 $("#wclass"+datatype).change(function(){
			 getfiter(datatype);
			 var tab = $("#slist"+datatype).dataTable();
			var oSettings = tab.fnSettings();
			tab.fnReloadAjax(oSettings);
		 });
	} else if (datatype=="4") { //观测井 
		$(tableId).dataTable( $.extend({}, params, {
			"aoColumns": [
		                    { "mData": "point.name"},
		                    { "mData": "warningType"},
		                    { "mData": "water"},
		                    { "mData": "temperature"},
		                    { "mData": "auditSection.dateCreated"},
		                    { "mData": "auditSection.dateModified"},
		                    { "mData": "waterThreshold"},
		                    { "mData": "waterDwon"},
		                    { "mData": "temperatureThreshold"},
		                    { "mData": "status"},
		                    { "mData": "id" }
		                ],
            "aoColumnDefs": [
				 {"aTargets":[1],"mRender":function(data,type,full){
					 return "<span class='text-warning'>数据告警</span>";
				 }
				},
				{"aTargets":[9],"mRender":function(data,type,full){
					switch (data) {
					case 0:
						return "正常";
					case 1:
						return "水位告警";
					case 2:
						return "水温告警";
					default:
						return "全告警";
					}
					}
				},
				 {"aTargets":[10],"mRender":function(data,type,full){
					 var reStr='';
					 <c:if test="${hasRight==true }">
					 reStr+='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-container="body" data-placement="left" data-original-title="删除"  onclick="deldata(\''+data+'&type='+datatype+'\')"><i class="fa fa-trash-o "></i></a>'; 
						</c:if>
				    	return reStr;
				 	},
				 	"sWidth":"120px","bSortable": false
				}, {
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
	} else { //环境监控
		$(tableId).dataTable( $.extend({}, params, {
			"aoColumns": [
		                    { "mData": "point.name"},
		                    { "mData": "warningType"},
		                    { "mData": "data"},
		                    { "mData": "auditSection.dateCreated"},
		                    { "mData": "auditSection.dateModified"},
		                    { "mData": "threshold"},
		                    { "mData": "status"},
		                    { "mData": "id" }
		                ],
            "aoColumnDefs": [
				 {"aTargets":[1],"mRender":function(data,type,full){
					 return "<span class='text-warning'>数据告警</span>";
				 }
				},
				{"aTargets":[6],"mRender":function(data,type,full){
					switch (data) {
					case 0:
						return "正常";
					default:
						return "告警";
					}
					}
				},
				 {"aTargets":[7],"mRender":function(data,type,full){
					 var reStr='';
					 <c:if test="${hasRight==true }">
					 reStr+='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-container="body" data-placement="left" data-original-title="删除"  onclick="deldata(\''+data+'&type='+datatype+'\')"><i class="fa fa-trash-o "></i></a>'; 
						</c:if>
				    	return reStr;
				 	},
				 	"sWidth":"120px","bSortable": false
				}, {
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
}
function afterRemove(response, params) {
	var index = params.indexOf("type");
	var type = params.substr(index+5);
	var tab = $("#slist"+type).dataTable();
	var oSettings = tab.fnSettings();
	tab.fnReloadAjax(oSettings);
}
function getfiter(datatype){
	filterjson= {"warningtype":$("#wclass"+datatype).val()} 
}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>