<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>

<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<div class="row" id="edittable" style="display: none;">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				<span id="edittile"></span> <span class="tools pull-right"> <a
					href="javascript:;" class="fa fa-chevron-down"></a> <a
					href="javascript:;" class="fa fa-times"></a>
				</span>
			</header>
			<div class="panel-body">
				<form:form cssClass="form-horizontal" role="form" commandName="camera" id="storeform">
					<div class="form-group">
						<label class="col-sm-2 control-label">名称</label>
						<div class="col-sm-10">
							<div class="input-group">
							<form:input cssClass="form-control" path="note" />
							<c:if test="${not empty  csite}">
								<span class="input-group-btn " >
                              	 <button class="btn btn-theme tooltips" type="button" data-container="body" data-placement="top" data-original-title="从地图上获取经纬度"  onclick="getFromll(${csite.longitude},${csite.latitude},${csite.id})" ><i class="fa fa-map-marker"></i></button>
                             </span>
							</c:if>
							
                             </div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label control-required">路径</label>
						<div class="col-sm-6">
							<form:input cssClass="form-control required" path="path"/>
						</div>
						<div class="col-sm-4 control-label">
							路径采用“/地区/项目/摄像头”结构定义
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">所属部门</label>
						<div class="col-sm-10">
							<input class="form-control" id="departmentName" readonly="readonly" style="border:0px; background:#fff"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">所属项目</label>
						<div class="col-sm-10">
							<input class="form-control" name="csiteName" id="csiteName" readonly="readonly" />
									<div id="editTree" class="col-sm-12"
										style="position: relative; padding: 0; top: 0px; z-index: 100; display: none"></div>
						</div>
					</div>
					<c:if test="${not empty  csite}">
						<div class="form-group">
                          <label class="col-lg-2 col-sm-2 control-label ">经度</label>
                          <div class="col-lg-10">
                              <form:input cssClass="form-control " type="number" readonly="true"   path="longitude" id="longitude" />
                              <span class="help-block"> <form:errors path="longitude" cssClass="error" /></span>
                          </div>
                      </div>
                      <div class="form-group">
                          <label class="col-lg-2 col-sm-2 control-label ">纬度</label>
                          <div class="col-lg-10">
                              <form:input cssClass="form-control " readonly="true"  type="number" path="latitude" id="latitude" />
                              <span class="help-block"> <form:errors path="latitude" cssClass="error" /></span>
                          </div>
                      </div>
					</c:if>
					<div class="form-group">
						<label class="col-sm-2 control-label">状态</label>
						<div class="col-sm-10">
							<span class="checkboxs-inline"> 
								<input type="checkbox" id="status" name="status" title="true" value=""> &nbsp;是否可用
							</span>
						</div>
					</div>
					<c:if test="${hasRight==true }">
						<div class="form-group">
							<div class="col-sm-2"></div>
							<div class="col-sm-10">
								<button type="submit" class="btn btn-success">提交</button>
							</div>
						</div>
					</c:if>
					
					<form:hidden path="id" />
					<input type="hidden" name="project.id" id="csiteId"/>
				</form:form>
			</div>
		</section>
	</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				摄像头列表
				<c:if test="${hasRight==true && empty  csite}">
					<button type="button"
					class="btn btn-theme pull-right fa fa-plus-square"
					onclick="changeEdittable2()">
					新增
				</button>
				</c:if>
				<button id="nextbtn" type="button"
					class="btn btn-theme pull-right fa fa-arrow-down visible-xs"
					onclick="donext()">下一页</button>
					<button type="button"
					class="btn btn-theme pull-right fa fa-arrow-up visible-xs"
					onclick="dolast()">上一页</button>
			</header>
			<div class="panel-body">
				<div class="col-lg-12 col-sm-12">
					<section class="adv-table " id="no-more-tables">
						<table class="display table table-bordered table-striped"
							id="slist">
							<thead>
								<tr>
									<th><input class="allCheckbox" type="checkbox" /></th>
									<th>名称</th>
									<th>路径</th>
									<th>所属部门</th>
									<th>所属项目</th>
									<th>状态</th>
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
<c:if test="${not empty  csite}">
	<jsp:include page="/common/getPointFromMap.jsp" />
</c:if>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-treeview/bootstrap-treeview.js" />'></script>
<script>
var cid ="";
<c:if test="${not empty  csite}">
	cid ="${csite.id}";
</c:if>
function goback(){
	javascript :history.back(-1);
}
	$(function() {
		$('#pictitle').html('摄像头配置');
		$('#slist').dataTable({
			"bPaginate" : true, //不显示分页
			"bProcessing" : true,
			"bServerSide" : true,
			"bLengthChange" : true,
			"bFilter" : true,
			"bSortClasses" : false,
			"sDom" : "lf<c:if test="${hasRight==true && empty csite}"><'#del_btn'></c:if>rtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource" : "server_processing.shtml?cid="+cid,
			"fnServerData" : retrieveData,
			"aoColumns" : [ {
				"mData" : "id"
			}, {
				"mData" : "note"
			}, {
				"mData" : "path"
			}, {
				"mData" : "departmentName"
			}, {
				"mData" : "csiteName"
			}, {
				"mData" : "status"
			}, {
				"mData" : "id"
			}

			],
			"aoColumnDefs" : [
					{
						"bVisible" : true,
						"aTargets" : [ 0 ],
						"mRender" : function(data, type, full) {
							return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';
						},
						"bSortable" : false
					},
					{
						"aTargets" : [ 6 ],
						"mRender" : function(data, type, full) {
							var str="";
							 <c:if test="${hasRight==true }">
							 str+= '<a class="edit btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;';
							 </c:if>
							 <c:if test="${hasRight==true && empty  csite}">
							 	str += '<a href="javascript:;" class="btn btn-danger btn-xs tooltips"  data-container="body" data-placement="left" data-original-title="删除"  onclick="deldata(' + data + ')"><i class="fa fa-trash-o "></i></a>'; 
							 </c:if>
							 return str;
							
						},
						"sWidth" : "150px",
						"bSortable" : false
					}, {
						"aTargets" : [ "_all" ],
						"bSortable" : false
					} ],
			"fnInitComplete" : function(settings, json) {
				if(isTouchDevice()===false) {
           		 $('.tooltips').tooltip();
           		}
				definedSidCheckbox();
			}

		});
		
		//加载项目/工地的集合
		getProjectSitesList('<c:url value="/water/department/getDepartmentSitesTree.shtml" />', $("#editTree"), $("[id='csiteId']"), $("#csiteName"), $("#departmentName"));
		$("#csiteName").bind('click', function(){
			$("#editTree").toggle();
		});
	});
	function changeEdittable2(event) {
		changeEdittable();
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script src='<c:url value="/resources/js/project/tree.js" />'></script>
