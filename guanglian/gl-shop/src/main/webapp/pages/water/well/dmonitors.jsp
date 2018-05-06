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
		var pageTitle="环境监测";
		$('#pictitle').html(pageTitle+'管理');
		$('#panel-heading').html(pageTitle);
</script>
<div class="row" id="edittable" style="display:none;">	
	<div class="col-lg-12 col-sm-12">
				<section class="panel" >
                      <header class="panel-heading">
                          <span id="edittile"></span>
                          <span class="tools pull-right">
                            <a href="javascript:;" class="fa fa-chevron-down"></a>
                            <a href="javascript:;" class="fa fa-times"></a>
                          </span>
                      </header>
                      <div class="panel-body">
                      	<form:form cssClass="form-horizontal" role="form" commandName="oWell" id="storeform">
							<form:errors path="*"
								cssClass="alert alert-block alert-danger fade in" element="div" />
							<div id="store.success" class="alert alert-success"
								style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">提交成功</div>
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label control-required">名称</label>
								<div class="col-lg-10">
								<div class="input-group">
                                    <form:input cssClass="form-control required" path="name" id="name"  />
                             	 	<span class="input-group-btn " >
                                    	 <button class="btn btn-theme tooltips" type="button" data-placement="left" data-container="body" data-original-title="从地图上获取经纬度"  onclick="getFromll(${csite.longitude},${csite.latitude},${csite.id})" ><i class="fa fa-map-marker"></i></button>
                                   </span>
                               </div>
                               </div> 
							</div>
							<div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label control-required">经度</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control required" type="number" readonly="true"   path="longitude" id="longitude" />
                                       <span class="help-block"> <form:errors path="longitude" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label control-required">纬度</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control  required" readonly="true"  type="number" path="latitude" id="latitude" />
                                       <span class="help-block"> <form:errors path="latitude" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label control-required">环境数据告警阈值</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control required" type="number" path="deformData" id="deformData" />
                                       <span class="help-block"> <form:errors path="deformData" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-sm-2 control-label">数据采集转换公式</label>
                                   <div class="col-sm-6">
                                       <form:input cssClass="form-control" path="formula1" id="formula1" />
                                   </div>
                                   <div class="col-sm-4 control-label">请用x代表采集数值</div>
                               </div>
	                               <div class="form-group">
	                                   <label class="col-lg-2 col-sm-2 control-label ">采集状态</label>
	                                   <div class="col-lg-10">
	                                       <form:select type="select" cssClass="form-control" path="dataStatus" id="dataStatus"  >
											 <form:option value="0" selected="selected" >正常</form:option>
												<form:option value="1">告警</form:option>
												<form:option value="4">封井</form:option>
	        		  					</form:select>
	                                   </div>
	                               </div>
	                               
	                               <div class="form-group">
										<label class="col-lg-2 col-sm-2 control-label">地图可见</label>
										<div class="col-lg-10">
											<span class="checkboxs-inline" onclick="setActive('visible')">
												<input type="checkbox" id="visible" name="visible" title="true" onclick="setActive('visible')"> &nbsp;可见
											</span>
										</div>
									</div>
	                               
							<div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">备注</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control "  path="pointInfo.note" />
                                       <span class="help-block"> <form:errors path="pointInfo.note" cssClass="error" /></span>
                                   </div>
                               </div>
							<c:if test="${hasRight==true }">
							<div class="form-group">
								<div class="col-lg-2 col-sm-2"></div>
								<div class="col-lg-10">
									<button type="button" onclick="jssubmit()" class="btn btn-success">保存</button>
								</div>
							</div>
							</c:if>
							<form:hidden path="powerStatus" id="powerStatus"/>
						<form:hidden path="id" id="id"/>
						<form:hidden path="pointInfo.id"/>
						<form:hidden path="pointLink.id"/>
						<form:hidden path="cSite.id" id="cSite.id" />
						<form:hidden path="rData" id="rData" />
						</form:form>
                  </div>
                  </section>				
</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading"> <span id="panel-heading"></span>列表 
		<c:if test="${hasRight==true }">
			<button type="button"  class="btn btn-theme pull-right fa fa-plus-square" onclick="changeEdittable(newObject)">新增</button>
		</c:if>
		<button id="nextbtn" type="button"
					class="btn btn-theme pull-right fa fa-arrow-down visible-xs"
					onclick="donext()">下一页</button>
					<button type="button" id="lastbtn"
					class="btn btn-theme pull-right fa fa-arrow-up visible-xs"
					onclick="dolast()">上一页</button>
	</header>
	<div class="panel-body">
		<div class="col-lg-12 col-sm-12">
			<section class="adv-table " id="no-more-tables">
             <table class="display table table-bordered table-striped" id="slist">
                 <thead>
                 <tr>
                     <th><input class="allCheckbox" type="checkbox"/></th>
                     <th>名称</th>
                     <th>监测数据阈值</th>
                     <th>实际值</th>
                     <th>设备状态</th>
                     <th>采集状态</th>
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
	            		<label class="col-sm-2 control-label">选择日期：</label>
	            		<div class="col-xs-10 col-sm-6">
			            	<div class="input-group input-medium date-picker input-daterange" data-date-format="yyyy-mm-dd">
							    <input name="begindt" class="form-control" style="font-size: 13px;" type="text" value="">
							    <span class="input-group-addon btn-info">到</span>
							    <input name="enddt" class="form-control" style="font-size: 13px;" type="text" value="">
							</div>
	            		</div>
	            		<div class="col-xs-2 col-sm-3 col-md-4 singlechart">
	            			<button onclick="getHistoryLines(3)" class="btn btn-theme"><i class="fa fa-search"></i></button>
	            		</div>
	            		<div class="col-xs-12 col-sm-3 col-md-4 comparechart">
	            			<button onclick="getHistoryLines('31')" class="btn btn-theme"><i class="fa fa-search"></i>数据比较</button>
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
 <jsp:include page="/common/getPointFromMap.jsp" />
<jsp:include page="/pages/water/import/imporewell.jsp" />
 <jsp:include page="/common/exportModal.jsp" />
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
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
			"sDom" : "lf<c:if test="${hasRight==true }"><'#printerer'><'#import_btn'><'#export_btn'><'#showchart_btn'><'#del_btn'></c:if>rtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource": "server_processing.shtml?cid="+cid,
			"fnServerData": retrieveGoods,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "name"},
		                    { "mData": "deformData"},
		                    { "mData": "rData"},
		                    { "mData": "powerStatus"},
		                    { "mData": "dataStatus"},
		                    { "mData": "pointInfo.note"},
		                    { "mData": "id" }
		                ],
                "aoColumnDefs": [
                             {"bVisible": true, "aTargets": [0],  "mRender":function(data,type,full){
                              	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                              },"bSortable": false, 
                             },
                            {"aTargets":[4],"mRender":function(data,type,full){
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
                        {"aTargets":[5],"mRender":function(data,type,full){
                    		switch (data) {
							case 0:
								return "正常";
							default:
								return "告警";
							}
                     	}
                    },
                             {"aTargets":[7],"mRender":function(data,type,full){
                            	 var reStr='<a class="btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="数据列表" href="'+_context+'/water/edata/list.html?pid='+full.id+'" ><i class="fa fa-list"></i></a>&nbsp;';
                            	 reStr+='<a class="btn btn-success btn-xs tooltips" data-placement="left" data-container="body" data-original-title="曲线图" href="javascript:;" onclick="loadChartModal('+full.id+')"><i class="fa fa-bar-chart-o"></i></a>&nbsp;';
                            	 reStr+='<a class="edit hasfn btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="修改" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;';
                            	 <c:if test="${hasRight==true }">
                            	 	if(full.powerStatus=="0"||full.powerStatus!="1"){
	                            		 reStr+='<a href="javascript:;" class="btn btn-theme btn-xs tooltips"  data-placement="left" data-container="body" data-original-title="关闭" onclick="doaction('+data+',&apos;active.shtml&apos;,&apos;关闭该'+pageTitle+'&apos;)" ><i class="fa fa-ban"></i></a>&nbsp;';
	                            	 }else {
	                            		 reStr+='<a href="javascript:;" class="btn btn-theme btn-xs tooltips"  data-placement="left" data-container="body" data-original-title="开启" onclick="doaction('+data+',&apos;active.shtml&apos;,&apos;打开该'+pageTitle+'&apos;)" ><i class="fa fa-flash"></i></a>&nbsp;';
	                            	 }
	                                
	                                	reStr+='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata('+data+')"><i class="fa fa-trash-o "></i></a>'; 
	                            	 </c:if>
	                                	return reStr;
                             	},
                             	"sWidth":"150px","bSortable": false
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
		//增加导入按钮
		$("#import_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-upload" style="color:red"></i>导入测点</button>');
		$("#import_btn").addClass("dt_btn pull-right");
		$("#import_btn").on('click',function(){
			importdata(cid);
		});
		//增加导入按钮
		$("#export_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-download" style="color:red"></i>导出所有观测数据</button>');
		$("#export_btn").addClass("dt_btn pull-right");
		$("#export_btn").on('click',function(){
			exportdata(cid);
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
		//加载日期控件
		$(".date-picker").datepicker({
		    language: "zh-CN",
		    format: "yyyy-mm-dd",
		    autoclose: true,
		    todayBtn: "linked",
		    //clearBtn: true,
		    todayHighlight: true
		});
		//增加打印按钮
		$("#printerer").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-barcode" style="color:red"></i>打印二维码</button>');
		$("#printerer").addClass("dt_btn pull-right");
		$("#printerer").on('click',function(){
			erprint();
		});
	});
	function erprint(){
		var ids = new Array();
		$('.sidCheckbox:checked').each(function(i,o){
			var nRow = $(o).parents('tr')[0];
			var aData = oTable.fnGetData(nRow);
			ids[i]=$(o).val();
			
		});
		if (ids.length<1) {
			alert("请至少选择一个需要打印二维码的数据");
			return;
		}
		window.open('<c:url value="/admin/dmonitor/print.html"/>'+'?ids='+ids, "_blank");		
	}

	function jssubmit(){	
		 $("#storeform").submit();
	}
	function exportdata(iid){
		$('#etitle').html('环境监测数据');
		$('#eid').val(iid);
		$('#ename').val(pname);
		$('#export-modal').modal('show');
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
	function newObject(){
		$("#powerStatus").val("0");
		$('#visible').prop("checked",true);
		$("select").prop("selectedIndex",0);
		$("input[id='cSite.id']").val(cid);
	}
	// 回调函数，设置状态默认值
	function dolistafterfn(oTable, nRow){
		$("input[id='cSite.id']").val(cid);
	}
	function importdata(iid){
		$('#iid').val(iid);
		$('#ititle').html('环境监测导入');
		
		importexcel();
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