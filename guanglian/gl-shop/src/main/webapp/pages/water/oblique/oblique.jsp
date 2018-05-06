<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />'
	rel="styleshee">
<link href='<c:url value="/resources/data-tables/DT_table.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />'
	rel="stylesheet">
	


<div class="row" id="edittable" style="display: none;">
	<div class="col-md-12">
		<section class="panel">
			<header class="panel-heading">
				<span id="edittile"></span> <span class="tools pull-right"> <a
					href="javascript:;" class="fa fa-chevron-down"></a> <a
					href="javascript:;" class="fa fa-times"></a>
				</span>
			</header>
			<div class="panel-body">
				<form:form cssClass="form-horizontal" role="form"
					commandName="oblique" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">孔号</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="namber"
								id="namber" />
							<span class="help-block"> <form:errors path="namber"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">评价</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control " path="evaluate"
								id="evaluate" />
							<span class="help-block"> <form:errors path="evaluate"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">水平位移修正</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control " path="horcorrect"
								id="horcorrect" />
							<span class="help-block"> <form:errors path="horcorrect"
									cssClass="error" /></span>
						</div>
					</div>
					<c:if test="${hasRight==true}">
					<div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="jssubmit()"
								class="btn btn-success">提交</button>
						</div>
					</div>
					</c:if>
					<form:hidden path="id" />
					<form:hidden path="monitor.id" />
				</form:form>
			</div>
		</section>
	</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				列表
				<c:if test="${hasRight==true}">
				<button type="button"
					class="btn btn-theme pull-right fa fa-plus-square"
					onclick="changeEdittable(afternew)">新增</button>
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
						<table class="display table table-bordered table-striped"
							id="slist">
							<thead>
								<tr>
									<th><input class="allCheckbox" type="checkbox" /></th>
									<th>孔号</th>
									<th>评价</th>
									<th>水平位移修正</th>
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
<jsp:include page="/pages/water/import/importobli.jsp" />
<jsp:include page="/common/exportModal.jsp" />
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>

<script>
$('#pictitle').html("${mentity.name }测斜信息");
	var mid = '${mid}';
	$(function() {
		$('#slist')
				.dataTable(
						{
							"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSortClasses" : false,
							"sDom" : "lf<c:if test="${hasRight==true }"><'#import_btn'><'#del_btn'></c:if>rtip",
							"sAjaxSource" : "server_processing.shtml?mid="
									+ mid,
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							}, {
								"mData" : "namber"
							}, {
								"mData" : "evaluate"
							}, {
								"mData" : "horcorrect"
							}, {
								"mData" : "id"
							}, ],
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
										"aTargets" : [ 4 ],
										"mRender" : function(data, type, full) {
											var s = '<a class="btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="数据列表" href="'
													+ _context
													+ '/water/depth/list.html?sid='
													+ full.id
													+ '&mid='
													+ mid
													+ '" ><i class="fa fa-list"></i></a>&nbsp;';
											s += '<a class="edit hasfn btn btn-primary btn-xs tooltips " data-placement="left" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';
											s += '&nbsp;<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-original-title="删除" onclick="deldata('
													+ data
													+ ',&quot;您真的要删除该对象吗?&quot; )"><i class="fa fa-trash-o "></i></a>';
											return s;
										},
										"sWidth" : "120px",
										"bSortable" : false
									}, {
										"aTargets" : [ "_all" ],
										"bSortable" : false
									} ],
							"fnInitComplete" : function(settings, json) {
								$('.tooltips').tooltip();
								definedSidCheckbox();
							}
						});
		//增加导入按钮
		$("#import_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-upload" style="color:red"></i>导入</button>');
		$("#import_btn").addClass("dt_btn pull-right");
		$("#import_btn").on('click',function(){
			importdata();
		});
		
	});
	function jssubmit() {
		$("#storeform").submit();
	}
	function dolistafterfn(oTable, nRow) {
		$("input[name='monitor.id']").val(mid);
	}
	function afternew() {
		$("input[name='monitor.id']").val(mid);
	}
	function importdata(){
		$('#iid').val(mid);
		importexcel();
		
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>
