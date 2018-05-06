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
<script type="text/javascript">
		$('#loading').show();
		var zone="${csite.zone}";
		var cid ="${csite.id}";
		var pname="${csite.name}";
		//设置title
		var pageTitle="疏干井";
		$('#pictitle').html(pageTitle);
		$('#panel-heading').html(pageTitle);
		 var iid=null;
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
                     <th>名称</th>
                     <th>流量阈值</th>
                     <th>实际流量</th>
                     <th>水位阈值上限</th>
                     <th>水位阈值下限</th>
                     <th>实际水位</th>
                     <th>设备状态</th>
                     <th>采集状态</th>
                     <th>精度</th>
                     <th>维度</th>
                     <th>备注</th>
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
<!-- modal begin : 测点曲线图 -->
<div class="modal fade" id="commonmodal" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" ><span id="modal-title" class="text-warning"></span></h4>
            </div>
            <div class="modal-body" id="modal-body">
            	<div class="container-fluid form-horizontal dataselect">
	            	<div class="row">
	            		<label class="col-xs-12 col-sm-3 col-md-2 control-label">选择日期：</label>
	            		<div class="col-xs-10 col-sm-6">
			            	<div class="input-group input-medium date-picker input-daterange" data-date-format="yyyy-mm-dd">
							    <input name="begindt" class="form-control" style="font-size: 13px;" type="text" value="">
							    <span class="input-group-addon btn-info">到</span>
							    <input name="enddt" class="form-control" style="font-size: 13px;" type="text" value="">
							</div>
	            		</div>
	            		<div class="col-xs-2 col-sm-3 col-md-4 singlechart">
	            			<button onclick="getHistoryLines(4)" class="btn btn-theme"><i class="fa fa-search"></i></button>
	            		</div>
	            		<div class="col-xs-12 col-sm-3 col-md-4 comparechart">
	            			<button onclick="getHistoryLines('41')" class="btn btn-theme"><i class="fa fa-search"></i>流量比较</button>
		            		<button onclick="getHistoryLines('42')" class="btn btn-warning"><i class="fa fa-search"></i>水位比较</button>
	            		</div>
	            	</div>
            	</div>
            	<div class="panel-body">
            	</div>
            </div>
			<div class="modal-footer">
                <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            </div>
        </div>
    </div>
</div>
<!-- modal end : 测点曲线图 -->
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script>
var permissionTree = new Array();
var cid; //测点id
var groupid=-1;
function goback(){
	javascript :history.back(-1);
}
	jQuery(document).ready(function() {
		//$('#edittable').hide();
		
		$('#slist').dataTable( {
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": false,
			"bSortClasses": false,
			"sDom" : "lf<'#showchart_btn'>rtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource": "server_processing.shtml?cid="+cid,
			"fnServerData": retrieveGoods,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "name"},
		                    { "mData": "flow"},
		                    { "mData": "rFlow"},
		                    { "mData": "water"},
		                    { "mData": "waterDwon"},
		                    { "mData": "rWater"},
		                    { "mData": "powerStatus"},
		                    { "mData": "dataStatus"},
		                    { "mData": "longitude"},
		                    { "mData": "latitude" },
		                    { "mData": "pointInfo.note"},
		                    { "mData": "id" }
		                ],
                "aoColumnDefs": [
                             {"bVisible": true, "aTargets": [0],  "mRender":function(data,type,full){
                              	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                              },"bSortable": false, 
                             },
                             {"aTargets":[7],"mRender":function(data,type,full){
                         		switch (data) {
									case 0:
										return "开启";
									case 1:
										return "关闭";
									case 2:
										return "故障";
									}
                          	}
                         },
                         {"aTargets":[8],"mRender":function(data,type,full){
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
                      	}
                     },
                             {"aTargets":[12],"mRender":function(data,type,full){
                            	 var reStr='<a class="btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="数据列表" href="'+_context+'/jiangong/dedata/list.html?pid='+full.id+'" ><i class="fa fa-list"></i></a>&nbsp;';
                            	 reStr+='<a class="btn btn-success btn-xs tooltips" data-placement="left" data-container="body" data-original-title="曲线图" href="javascript:;" onclick="loadChartModal('+full.id+')"><i class="fa fa-bar-chart-o"></i></a>&nbsp;';
                                	return reStr+"";
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
		$(".date-picker").datepicker({
		    language: "zh-CN",
		    format: "yyyy-mm-dd",
		    autoclose: true,
		    todayBtn: "linked",
		    //clearBtn: true,
		    todayHighlight: true
		});
		//增加曲线比较按钮
		$("#showchart_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-exchange" style="color:red"></i>数据比较</button>');
		$("#showchart_btn").addClass("dt_btn pull-right");
		$("#showchart_btn").on('click',function(){
			var ids = new Array();
			$('.sidCheckbox:checked').each(function(i,o){
				var nRow = $(o).parents('tr')[0];
				var aData = oTable.fnGetData(nRow);
				//判断是否有权限字段
				ids[i]=$(o).val();
			});
			if (ids.length<2) {
				alert("请至少选择两条需要比较的数据");
				return;
			}
			$("#commonmodal").modal("show");
			$("#commonmodal .panel-body").html("");
			$(".comparechart").show();
			$(".singlechart").hide();
			
			cid = ids;
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
	
	function loadChartModal(_cid) {
		$("#commonmodal").modal("show");
		$("#commonmodal .panel-body").html("");
		$(".comparechart").hide();
		$(".singlechart").show();
		cid = _cid;
}
	
</script>
<script src='<c:url value="/resources/js/moment.min.js"/>'></script> 
<script src='<c:url value="/resources/js/Chart.min.js"/>'></script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script src='<c:url value="/resources/js/project/chart.js" />'></script>