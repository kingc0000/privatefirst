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
		$('#pictitle').html(pageTitle+'管理');
		$('#panel-heading').html(pageTitle);
		 var iid=null;
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
							<div id="wizard">
							<h3>基本信息</h3>
							 <section>
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label control-required">名称</label>
								<div class="col-lg-10">
								<div class="input-group">
                                    <form:input cssClass="form-control required" path="name" id="name"  />
                             	 	<span class="input-group-btn " >
                                    	 <button class="btn btn-theme tooltips" type="button" data-container="body" data-placement="left" data-original-title="从地图上获取经纬度"  onclick="getFromll(${csite.longitude},${csite.latitude},${csite.id})" ><i class="fa fa-map-marker"></i></button>
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
                                   <label class="col-lg-2 col-sm-2 control-label control-required">流量告警阈值</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control required" type="number" path="flow" id="flow" />
                                       <span class="help-block"> <form:errors path="flow" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-sm-2 control-label">流量采集转换公式</label>
                                   <div class="col-sm-6">
                                       <form:input cssClass="form-control" path="formula1" id="formula1" />
                                   </div>
                                   <div class="col-sm-4 control-label">请用x代表采集数值</div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label control-required">水位告警阈值上限</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control required" type="number" path="water" id="water" />
                                       <span class="help-block"> <form:errors path="water" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label control-required">水位告警阈值下限</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control required" type="number" path="waterDwon" id="waterDwon" />
                                       <span class="help-block"> <form:errors path="waterDwon" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-sm-2 control-label">水位采集转换公式</label>
                                   <div class="col-sm-6">
                                       <form:input cssClass="form-control" path="formula2" id="formula2" />
                                   </div>
                                   <div class="col-sm-4 control-label">请用x代表采集数值</div>
                               </div>
                               <div class="form-group">
	                                   <label class="col-lg-2 col-sm-2 control-label ">设备开启设置</label>
	                                   <div class="col-lg-10">
	                                       <form:select type="select" cssClass="form-control" path="autoStatus" id="autoStatus" onchange="setWellRunStatus(this.options[this.options.selectedIndex].value)" >
											 <form:option value="0" selected="selected" >人工开启</form:option>
												<form:option value="1">根据水位自动开启</form:option>
												<form:option value="2">定时自动开启</form:option>
	        		  					</form:select>
	                                   </div>
	                               </div>
	                               <div class="form-group deepclass" style="display: none;">
	                                   <label class="col-lg-2 col-sm-2 control-label control-required">水位埋深开启关闭关联测点</label>
	                                   <div class="col-lg-10">
						                 <div class="input-group">
						              	 	<input class="form-control required deepclass"  id="auto" style="display: none;" readonly="readonly" onclick="getautoList('请选择项目负责人','auto','<c:url value="/water/pwell/allwells.shtml?cid=${csite.id}"/>')" />
						             	 	<span class="input-group-btn" >
						                     <button class="btn btn-theme tooltips deepclass" type="button" style="display: none;" data-placement="top" data-container="body" data-original-title="选择关联测点" onclick="getautoList('请选择项目负责人','auto','<c:url value="/water/pwell/allwells.shtml?cid=${csite.id}"/>')"><i class="fa fa-plus-square-o"></i></button>
						                   </span>
						             	  </div>
						            	 </div>
	                               </div>
	                               <div class="form-group deepclass" style="display: none;">
	                                   <label class="col-lg-2 col-sm-2 control-label control-required">水位埋深开启阈值</label>
	                                   <div class="col-lg-10">
	                                        <form:input cssClass="form-control required deepclass"  style="display: none;" type="number" path="openDepp" id="openDepp" />
	                                   </div>
	                               </div>
	                               <div class="form-group deepclass" style="display: none;">
	                                   <label class="col-lg-2 col-sm-2 control-label control-required">水位埋深关闭阈值</label>
	                                   <div class="col-lg-10">
	                                        <form:input cssClass="form-control required deepclass" style="display: none;" type="number" path="closeDepp" id="closeDepp" />
	                                   </div>
	                               </div>
	                               
	                               <div class="form-group dumpclass " style="display: none;">
	                                   <label class="col-lg-2 col-sm-2 control-label control-required">抽水持续时间</label>
	                                   <div class="col-lg-10">
	                                        <form:input cssClass="form-control required dumpclass" style="display: none;" type="number" path="conMin" id="conMin" />
	                                   </div>
	                               </div>
	                               <div class="form-group dumpclass " style="display: none;">
	                                   <label class="col-lg-2 col-sm-2 control-label control-required">抽水间隔时间</label>
	                                   <div class="col-lg-10">
	                                        <form:input cssClass="form-control required dumpclass" style="display: none;" type="number" path="spaceMin" id="spaceMin" />
	                                   </div>
	                               </div>
	                               
	                               <div class="form-group">
	                                   <label class="col-lg-2 col-sm-2 control-label ">采集状态</label>
	                                   <div class="col-lg-10">
	                                       <form:select type="select" cssClass="form-control" path="dataStatus" id="dataStatus"  >
											 <form:option value="0" selected="selected" >正常</form:option>
												<form:option value="1">流量告警</form:option>
												<form:option value="2">水位告警</form:option>
												<form:option value="3">全告警</form:option>
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
		                         <label class="col-lg-2 col-sm-2 control-label ">降水目的层</label>
		                         <div class="col-lg-9" id="pprecipitation">
					                     <c:if test="${not empty oWell.pointInfo.precipitation}">
					                     <c:set value="${ fn:split(oWell.pointInfo.precipitation, ',') }" var="cp" />
					                     	<c:forEach var="precipitation" items="${cp}">
											<c:set var="isDoing" value="0"/>
													<span class="checkboxs-inline" onclick="setCheckbox('pointInfo.precipitation','${precipitation}')">
														<input type="checkbox" id="precipitation" name="pointInfo.precipitation" title="${precipitation}" onclick="setCheckbox('pointInfo.precipitation','${precipitation}')" checked="checked"> 
													&nbsp;${precipitation}
													<c:set var="isDoing" value="1"/>
													</span>
											</c:forEach>
											
					                     </c:if>
					                </div>
					                <div class="col-lg-1 col-sm-1">
					                	<button type="button" class="addBtn btn btn-info fa  fa-building-o" onclick="getMList('请选择降水目的层','pointInfo.precipitation','<c:url value="/water/project/precipitation.shtml"/>','modal','','pprecipitation')"></button>
					                </div>
		                     </div>
                               <div class="form-group">
			                         <label class="col-lg-2 col-sm-2 control-label ">井图纸</label>
			                         <div class="col-lg-9 col-sm-9">
			                         	<div class="row" id="wimgs">
			                         	</div>
			                         </div>
			                         <div class="col-lg-1 col-sm-1">
			                         	<button type="button" class="addBtn btn btn-info fa fa-plus-square" onclick="addImage()"></button>
			                         </div>
			                     </div>
							<div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">备注</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control "  path="pointInfo.note" />
                                       <span class="help-block"> <form:errors path="pointInfo.note" cssClass="error" /></span>
                                   </div>
                               </div>
                             </section>
	                          <jsp:include page="/pages/water/well/basewell.jsp" />  
						<form:hidden path="id" id="id"/>
						<form:hidden path="powerStatus" id="powerStatus"/>
						<form:hidden path="pointInfo.id"/>
						<form:hidden path="pointLink.id"/>
						<form:hidden path="cSite.id" id="cSite.id" />
						<form:hidden path="rFlow" id="rFlow" />
						<form:hidden path="rWater" id="rWater" />
						<form:hidden path="autoID" id="autoID" />
						<form:hidden path="atype" id="atype" />
						 <input type="hidden" name="delids" id="delids"/>
						</div> 
						</form:form>
						<c:if test="${hasRight==true }">
							<div class="form-group">
								<div class="col-lg-2 col-sm-2"></div>
								<div class="col-lg-10">
									<button type="button" onclick="jssubmit()" class="btn btn-success">保存</button>
								</div>
							</div>
							</c:if>
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
                     <th>流量阈值</th>
                     <th>实际流量</th>
                     <th>水位阈值上限</th>
                     <th>水位阈值下限</th>
                     <th>实际水位</th>
                     <th>设备状态</th>
                     <th>采集状态</th>
                     <th>经度</th>
                     <th>纬度</th>
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

 <jsp:include page="/common/getPointFromMap.jsp" />
 <jsp:include page="/pages/water/import/impordewell.jsp" />
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
                            	 var reStr='<a class="btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="数据列表" href="'+_context+'/water/dedata/list.html?pid='+full.id+'" ><i class="fa fa-list"></i></a>&nbsp;';
                            	 reStr+='<a class="btn btn-success btn-xs tooltips" data-placement="left" data-container="body" data-original-title="曲线图" href="javascript:;" onclick="loadChartModal('+full.id+')"><i class="fa fa-bar-chart-o"></i></a>&nbsp;';
                            	 reStr+='<a class="edit hasfn btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="修改" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;';
                            	 <c:if test="${hasRight==true }">
                            	 if(full.powerStatus=="0"||full.powerStatus!="1"){
                            		 reStr+='<a href="javascript:;" class="btn btn-theme btn-xs tooltips"  data-placement="left" data-container="body" data-original-title="关闭" onclick="doaction('+data+',&apos;active.shtml&apos;,&apos;关闭该'+pageTitle+'&apos;)" ><i class="fa fa-ban"></i></a>&nbsp;';
                            	 }else {
                            		 reStr+='<a href="javascript:;" class="btn btn-theme btn-xs tooltips"  data-placement="left" data-container="body" data-original-title="开启" onclick="doauto('+data+')" ><i class="fa fa-flash"></i></a>&nbsp;';
                            	 }
                                
                                	reStr+='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata('+data+')"><i class="fa fa-trash-o "></i></a>'; 
             					</c:if>
                                	return reStr+"";
                             	},
                             	"sWidth":"160px","bSortable": false
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
		window.open('<c:url value="/admin/dewell/print.html"/>'+'?ids='+ids, "_blank");		
	}
	function jssubmit(){
		if(parseFloat($('#waterDwon').val())> parseFloat($('#water').val())){
			toastr.error("阈值下限不能大于上限");
			return ;
		}
		var form = $("#storeform");
		form.validate({
    		ignore: ":disabled,:hidden"
    	});
    	 var result= form.valid();
    	 if(result){
    		 wellssae();
    	 }else{
    		 return false;
    	 }
		// $("#storeform").submit();
	}
	function exportdata(iid){
		$('#etitle').html('疏干井数据');
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
		$("select").prop("selectedIndex", 0);
		$("input[id='cSite.id']").val(cid);
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
	}
	// 回调函数，设置状态默认值
	function dolistafterfn(oTable, nRow){
		//$("[id='status']").val("0");
		$("input[id='cSite.id']").val(cid);
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
		var aData = oTable.fnGetData(nRow);
		var auto =aData["autoStatus"];
		var autoid =aData["autoID"];
		var autotype =aData["atype"];
		setWellRunStatus(auto,autoid,autotype);
		var pointInfo =aData["pointInfo"];
		var fname= pointInfo.images;
		if(fname!=null && fname.length >0){
			$.each(fname,function(index,img){
				addImage(img.name,img.jpeg,img.id,"DEWELL_DIGITAL");
			});
		}
		
		var jprecipitation=pointInfo.precipitation;
		$('#pprecipitation').html('');
		if(jprecipitation!=null && jprecipitation.length>0){
			var phtml='';
			jprecipitation=jprecipitation.split(",");
			$.each(jprecipitation,function(index,con){
				//$.each(data,function(index,pre){
					//if(pre.value==con){
						phtml+='<span class="checkboxs-inline" onclick="setCheckbox(&apos;pointInfo.precipitation&apos;,&apos;'+con+'&apos;)">'
						+'<input type="checkbox" id="precipitation" name="pointInfo.precipitation" title="'+con+'"'
						+ 'onclick="setCheckbox(&apos;pointInfo.precipitation&apos;,&apos;'+con+'&apos;)" checked="checked"> '
						+'&nbsp;'+con
						+'</span>';
					//}
					
				//})
			})
			$('#pprecipitation').html(phtml);
		}
	}
	function importdata(iid){
		$('#iid').val(iid);
		$('#ititle').html('疏干井导入');
		$('.emonitorimport').hide();
		
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
<script src='<c:url value="/resources/js/project/modalpagemuti.js" />'></script>