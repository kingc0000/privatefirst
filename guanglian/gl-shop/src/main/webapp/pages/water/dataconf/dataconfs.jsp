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
<c:if test="${not empty gateways&&fn:length(gateways)>0 }">
	<script type="text/javascript">
		gateflag = true;
		gatewaystr = '<select name="gateway"><option value="">&nbsp;</option>';
		<c:forEach items="${gateways}" var="gateway">
			gatewaystr += '<option value="${gateway.id}">${gateway.name}</option>'; 
		</c:forEach>
		gatewaystr += '</select>';
		
		nodestr = '<select name="node"><option value="">&nbsp;</option>';
		for(var i=1; i<65; i++) {
			nodestr += '<option value="'+i+'">'+i+'</option>';
		}
		nodestr += '</select>';
		channelstr = '<select name="channel"><option value="">&nbsp;</option>';
		for(var i=0; i<4; i++) {
			channelstr += '<option value="'+i+'">'+(i+1)+'</option>';
		}
		channelstr += '</select>';
	</script>
</c:if>


<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
		<header class="panel-heading"> 数据配置列表 
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
				                    <th>测点名称</th>  
								    <th>网关</th>  
								    <th>节点(流量)</th>
								    <th>通道(流量)</th>
								    <th>节点(水位)</th>
								    <th>通道(水位)</th>
								    <th>节点(启停)</th>  
								    <th>通道(启停)</th>
								    <th>节点(断电)</th>  
								    <th>通道(断电)</th>
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
				                    <th>测点名称</th>  
								    <th>网关</th>  
								    <th>节点(流量)</th>
								    <th>通道(流量)</th>
								    <th>节点(水位)</th>
								    <th>通道(水位)</th>
								    <th>节点(启停)</th>  
								    <th>通道(启停)</th>
								    <th>节点(断电)</th>  
								    <th>通道(断电)</th>
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
				                    <th>测点名称</th>  
								    <th>网关</th>  
								    <th>节点(流量)</th>
								    <th>通道(流量)</th>
								    <th>节点(井内水位)</th>
								    <th>通道(井内水位)</th>
								    <th>节点(启停)</th>  
								    <th>通道(启停)</th>
								    <th>节点(断电)</th>  
								    <th>通道(断电)</th>
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
				                    <th>测点名称</th>  
								    <th>网关</th>  
								    <th>节点(水位)</th>
								    <th>通道(水位)</th>
								    <th>节点(水温)</th>
								    <th>通道(水温)</th>
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
				                    <th>测点名称</th>  
								    <th>网关</th>  
								    <th>节点(观察值)</th>
								    <th>通道(观察值)</th>
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
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
var plids = {}; //定义pointlink集合的id
jQuery(document).ready(function() {
	if(gateflag) { //项目已经有网关信息，加载项目下所有测点的网关、节点配置信息
		$('a[data-toggle="tab"]').on( 'shown.bs.tab', function (e) {
			//$(".dataTables_scrollBody").css("height", "auto");
			//$.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
			var datatype = $(e.target).attr("data-type");
			//$("#slist"+datatype).dataTable();
			var table = $.fn.dataTable.fnTables(true);
			if ( table.length > 0 ) {
				$(table).dataTable().fnAdjustColumnSizing();
			}
			//getDataconfs(datatype, "#slist"+datatype);
	    } );
		$('table.table').each(function(i){
			var index = i+1;
			getDataconfs(index, "#slist"+index);
		});
	} else {
		var gw_url = '<c:url value="/water/gateway/list.html?cid='+cid+'"/>';
		$("#find").html('<tr><td colspan="11" class="warning text-center">该项目没有网关数据，请先完成<a class="text-danger" href="'+gw_url+'">网关配置<i class="fa  fa-arrow-circle-right"></i></a></td></tr>')
	}
	//$('#edittable').hide();
	//获取测点集合信息
});

function getDataconfs(datatype, tableId) {
	var params = {
			"bPaginate" : false, //不显示分页
			"bProcessing" : true,
			"bServerSide" : true,
			"bLengthChange" : true,
			/* "sScrollX" : "100%", */
			"bFilter" : true,
			"bSortClasses" : false,
			"sDom" : "lrti",
			"sAjaxSource": "server_processing.shtml?cid="+cid+"&type="+datatype,
			"fnServerData" : retrieveData};
	if(datatype=="1"||datatype=="2"||datatype=="3") {
		$(tableId).dataTable( $.extend({}, params, {
			"aoColumns": [
		                    { "mData": "name"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "id" }
		                ],
            "aoColumnDefs": [
				{ 
					"aTargets":[1],
					"mRender":function(data,type,full){
						//网关
						var _gate = $(gatewaystr);
						_gate.attr("id", "gate_"+datatype+"_"+full["id"]);
						if(full["pointLink"]!=null) {
							//网关初始值
							if (full["pointLink"]["gateway"]!=null&&full["pointLink"]["gateway"]["id"]!=null) {
								var gtid = full["pointLink"]["gateway"]["id"];
								var _selected = _gate.children('option[value="'+gtid+'"]');
								if (_selected!=null) { 
									_selected.attr("selected", true);
								}
							}
							//设置默认的pointLink的id，定义隐藏域 type_id_plid
							plids[datatype+"_"+full["id"]] = full["pointLink"]["id"]; 
						}
				    	return _gate.prop("outerHTML");
				 	},
				},
				{ 
					"aTargets":[2],
					"mRender":function(data,type,full){
						//节点
						return getNode(full, datatype, 1);
				 	},
				},
				{ 
					"aTargets":[4],
					"mRender":function(data,type,full){
						//节点
						return getNode(full, datatype, 2);
				 	},
				},
				{ 
					"aTargets":[6],
					"mRender":function(data,type,full){
						//节点
						return getNode(full, datatype, 3);
				 	},
				},
				{ 
					"aTargets":[8],
					"mRender":function(data,type,full){
						//节点
						return getNode(full, datatype, 4);
				 	},
				},
				{ 
					"aTargets":[3],
					"mRender":function(data,type,full){
						//通道
						return getChannel(full, datatype, 1);
				 	},
				},
				{ 
					"aTargets":[5],
					"mRender":function(data,type,full){
						//通道
						return getChannel(full, datatype, 2);
				 	},
				},
				{ 
					"aTargets":[7],
					"mRender":function(data,type,full){
						//通道
						return getChannel(full, datatype, 3);
				 	},
				},
				{ 
					"aTargets":[9],
					"mRender":function(data,type,full){
						//通道
						return getChannel(full, datatype, 4);
				 	},
				},
				{ 
					"aTargets":[10],
					"mRender":function(data,type,full){
				    	var rStr= '<a class="btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="保存" href="javascript:;" onclick="saveDataconf(this, '+full.id+','+datatype+')"><i class="fa fa-save"></i></a>&nbsp;'
				    	return rStr;
				 	},
				},
				{
                	"aTargets":["_all"],"bSortable": false
                }],
             "fnInitComplete": function(settings, json) {
            	 if(isTouchDevice()===false) {
            		 $('.tooltips').tooltip();
            		}
            	 this.fnSetColumnVis(10,settings.hasRight);
            	 $('#loading').hide();
             }
			
		}));
	} else if (datatype=="4") { //观测井
		$(tableId).dataTable( $.extend({}, params, {
			"aoColumns": [
		                    { "mData": "name"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "id" }
		                ],
            "aoColumnDefs": [
				{ 
					"aTargets":[1],
					"mRender":function(data,type,full){
						//网关
						var _gate = $(gatewaystr);
						_gate.attr("id", "gate_"+datatype+"_"+full["id"]);
						if(full["pointLink"]!=null) {
							//网关初始值
							if (full["pointLink"]["gateway"]!=null&&full["pointLink"]["gateway"]["id"]!=null) {
								var gtid = full["pointLink"]["gateway"]["id"];
								var _selected = _gate.children('option[value="'+gtid+'"]');
								if (_selected!=null) { 
									_selected.attr("selected", true);
								}
							}
							//设置默认的pointLink的id，定义隐藏域 type_id_plid
							plids[datatype+"_"+full["id"]] = full["pointLink"]["id"];
						}
				    	return _gate.prop("outerHTML");
				 	},
				},
				{ 
					"aTargets":[2],
					"mRender":function(data,type,full){
						//节点
						return getNode(full, datatype, 1);
				 	},
				},
				{ 
					"aTargets":[4],
					"mRender":function(data,type,full){
						//节点
						return getNode(full, datatype, 2);
				 	},
				},
				{ 
					"aTargets":[3],
					"mRender":function(data,type,full){
						//通道
						return getChannel(full, datatype, 1);
				 	},
				},
				{ 
					"aTargets":[5],
					"mRender":function(data,type,full){
						//通道
						return getChannel(full, datatype, 2);
				 	},
				},
				{ 
					"aTargets":[6],
					"mRender":function(data,type,full){
						var plid = -1; //pointlink的id
						if(full["pointLink"]!=null) {
							plid = full["pointLink"]["id"];
						}
				    	var rStr= '<a class="btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="保存" href="javascript:;" onclick="saveDataconf(this, '+full.id+','+datatype+')"><i class="fa fa-save"></i></a>&nbsp;'
				    	return rStr;
				 	},
				},
				{
                	"aTargets":["_all"],"bSortable": false
                }],
             "fnInitComplete": function(settings, json) {
            	 if(isTouchDevice()===false) {
            		 $('.tooltips').tooltip();
            		}
            	 this.fnSetColumnVis(6,settings.hasRight);
            	 $('#loading').hide();
             }
		}));
	} else { //环境监测
		$(tableId).dataTable( $.extend({}, params, {
			"aoColumns": [
		                    { "mData": "name"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "pointLink"},
		                    { "mData": "id" }
		                ],
            "aoColumnDefs": [
				{ 
					"aTargets":[1],
					"mRender":function(data,type,full){
						//网关
						var _gate = $(gatewaystr);
						_gate.attr("id", "gate_"+datatype+"_"+full["id"]);
						if(full["pointLink"]!=null) {
							//网关初始值
							if (full["pointLink"]["gateway"]!=null&&full["pointLink"]["gateway"]["id"]!=null) {
								var gtid = full["pointLink"]["gateway"]["id"];
								var _selected = _gate.children('option[value="'+gtid+'"]');
								if (_selected!=null) { 
									_selected.attr("selected", true);
								}
							}
							//设置默认的pointLink的id，定义隐藏域 type_id_plid
							plids[datatype+"_"+full["id"]] = full["pointLink"]["id"];
						}
				    	return _gate.prop("outerHTML");
				 	},
				},
				{ 
					"aTargets":[2],
					"mRender":function(data,type,full){
						//节点
						return getNode(full, datatype, 1);
				 	},
				},
				{ 
					"aTargets":[3],
					"mRender":function(data,type,full){
						//通道
						return getChannel(full, datatype, 1);
				 	},
				},
				{ 
					"aTargets":[4],
					"mRender":function(data,type,full){
				    	var rStr= '<a class="btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="保存" href="javascript:;" onclick="saveDataconf(this, '+full.id+','+datatype+')"><i class="fa fa-save"></i></a>&nbsp;'
				    	return rStr;
				 	},
				},
				{
                	"aTargets":["_all"],"bSortable": false
                }],
             "fnInitComplete": function(settings, json) {
            	 if(isTouchDevice()===false) {
            		 $('.tooltips').tooltip();
            		}
            	 this.fnSetColumnVis(4,settings.hasRight);
            	 $('#loading').hide();
             }
		}));
	}
}

function getNode(full, datatype, index) {
	var _node = $(nodestr);
	var _nindex = "node"+index;
	_node.attr("id", _nindex+"_"+datatype+"_"+full["id"]);
	if(full["pointLink"]!=null) {
		if (full["pointLink"]!=null&&full["pointLink"][_nindex]!=null) {
			var nval = full["pointLink"][_nindex];
			var _selected = _node.children('option[value="'+nval+'"]');
			if (_selected!=null) { 
				_selected.attr("selected", true);
			}
		}
	}
	return _node.prop("outerHTML");
}
function getChannel(full, datatype, index) {
	var _channel = $(channelstr);
	var _cindex = "channel"+index;
	_channel.attr("id", _cindex+"_"+datatype+"_"+full["id"]);
	if(full["pointLink"]!=null) {
		if (full["pointLink"]!=null&&full["pointLink"][_cindex]!=null) {
			var nval = full["pointLink"][_cindex];
			var _selected = _channel.children('option[value="'+nval+'"]');
			if (_selected!=null) { 
				_selected.attr("selected", true);
			}
		}
	}
	return _channel.prop("outerHTML");
}
/**
 * 保存配置数据
 @param id 测点id
 @param type 测点类型，1: 降水井，2：疏干井；3：回灌井；4：观测井；5：环境监测
 */
function saveDataconf(o, id, type) {
	//console.log(_tr.html());
	$('#loading').show();
	var data = {
		type: type,
		cid: id,
		plid: plids[type+"_"+id],
		gateway: $("#gate_"+type+"_"+id).val(),
		node1: $("#node1_"+type+"_"+id).val(),
		node2: $("#node2_"+type+"_"+id).val(),
		node3: $("#node3_"+type+"_"+id).val(),
		node4: $("#node4_"+type+"_"+id).val(),
		channel1: $("#channel1_"+type+"_"+id).val(),
		channel2: $("#channel2_"+type+"_"+id).val(),
		channel3: $("#channel3_"+type+"_"+id).val(),
		channel4: $("#channel4_"+type+"_"+id).val()
	}
	//console.log(data);
	$.post("save.shtml", data, function(response){
		var status = response.response.status;
		if (status == 0 || status == 9999) {
			plids[type+"_"+id] = response.response.plid; //新增操作，则需要将新的pointlink的id赋值
			toastr.success('操作成功！');
		} else if (status == -1) {
			toastr.error(response.response.statusMessage);
		} else {
			toastr.error('提交失败！');
		}
		$('#loading').hide();		
	});
}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>