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
					commandName="depth" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">深度</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required number" path="deep"
								id="deep" remote="checkDeepCode.shtml?id,obliqueid"/>
							<span class="help-block"> <form:errors path="deep"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">初始值</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required number" path="av" id="deep" />
							<span class="help-block"> <form:errors path="av"
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
					<form:hidden path="oblique.id" id="obliqueid"/>
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
					onclick="changeEdittable(setnew)">新增</button>
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
									<th>深度</th>
									<th>初始</th>
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
<jsp:include page="/pages/water/import/importdepth.jsp" />
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
$('#pictitle').html("${mentity.name }测斜深度信息");
	var sid = '${sid}';
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
							"sDom" : "lf<c:if test="${hasRight==true}"><'#import_btn'><'#del_btn'><'#importpoint_btn'></c:if>rtip",
							"sAjaxSource" : "server_processing.shtml?sid="+sid,
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							}, {
								"mData" : "deep"
							},{
								"mData" : "av"
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
										"aTargets" : [ 3 ],
										"mRender" : function(data, type, full) {
											var s = '<a class="btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="数据列表" href="'
													+ _context
													+ '/water/obliquedata/list.html?tid='
													+ full.id
													+ '&mid='
													+ mid
													+ '&sid='
													+ sid
													+ '" ><i class="fa fa-list"></i></a>&nbsp;';
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
		$("#import_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-upload" style="color:red"></i>深度数据导入</button>');
		$("#import_btn").addClass("dt_btn pull-right");
		$("#import_btn").on('click',function(){
			importdata();
		});
		$("#importpoint_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-upload" style="color:red"></i>深度导入</button>');
		$("#importpoint_btn").addClass("dt_btn pull-right");
		$("#importpoint_btn").on('click',function(){
			importpoint();
		});
		
	});
	function importpoint(){
		$('#iid').val(sid);
		$('.monitordata').hide();
		$('.monitorpoint').show();
		importexcel('url');
	}
	function importdata(){
		$('#iid').val(sid);
		$('.monitordata').show();
		$('.monitorpoint').hide();
		importexcel();
		
	}
	function jssubmit() {
		$("#storeform").submit();
	}
	function dolistafterfn(oTable, nRow) {
		$("input[name='oblique.id']").val(sid);
	}
	function setnew() {
		$("input[name='oblique.id']").val(sid);
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
