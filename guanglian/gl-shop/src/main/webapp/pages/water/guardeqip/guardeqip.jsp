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
<link
href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />'
rel="stylesheet">
<script type="text/javascript">
		$('#loading').show();
		var gid ="${gid}";
</script>
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
					commandName="guardeqip" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">设备</label>
						<div class="col-lg-10">
							<div class="input-group">
								<form:input cssClass="form-control required"
									path="equip.name" id="equip.name" readonly="true"/>
								<span class="input-group-btn phonehide">
									<button class="btn btn-theme tooltips" type="button"
										data-placement="top" data-container="body"
										data-original-title="请选择设备"
										onclick="getList('请选择设备','equip.name','<c:url value="/water/monitoreqip/equips.shtml"/>','modal')">
										<i class="fa fa-cog"></i>
									</button>
								</span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">进场日期</label>
						<div class="col-lg-10">
							<div class="date edate input-group">
								<form:input cssClass="form-control"
								path="entryDate" id="entryDate" />
							<span class="input-group-addon btn-theme"><i
								class="fa fa-calendar"></i></span> <span class="help-block"> <form:errors
									path="entryDate" cssClass="error" /></span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">离厂日期</label>
						<div class="col-lg-10">
							<div class="date edate input-group">
								<form:input cssClass="form-control"
								path="exitVaild" id="exitVaild" />
							<span class="input-group-addon btn-theme"><i
								class="fa fa-calendar"></i></span> <span class="help-block"> <form:errors
									path="exitVaild" cssClass="error" /></span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">备注</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control" path="memo"
								id="memo" />
							<span class="help-block"> <form:errors path="memo"
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
					<form:hidden path="guard.id" />
					<form:hidden path="equip.id" />
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
									<th>设备名称</th>
									<th>设备编号</th>
									<th>进场日期</th>
									<th>离厂日期</th>
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
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script>
$('#pictitle').html("${gentity.name }设备信息");
	$(function() {
$('#slist').dataTable({
												"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSortClasses" : false,
							"sDom" : "lf<c:if test="${hasRight==true}"><'#del_btn'></c:if>rtip",
							"sAjaxSource" : "server_processing.shtml?gid="+gid,
							"fnServerData" : retrieveData,
							"aoColumns" : [  {
								"mData" : "id"
							}, {
								"mData" : "equip.name"
							}, {
								"mData" : "equip.eNO"
							}, {
								"mData" : "entryDate"
							}, {
								"mData" : "exitVaild"
							}, {
								"mData" : "memo"
							}, {
								"mData" : "id"
							}],
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
										var s = '<a class="edit btn btn-primary btn-xs tooltips " data-placement="left" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';
										s += '&nbsp;<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-original-title="删除" onclick="deldata('
												+ data
												+ ',&quot;您真的要删除该对象吗？&quot; )"><i class="fa fa-trash-o "></i></a>';
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
	//加载日期控件
	$('.edate').datepicker({
		format : "yyyy-mm-dd",
		todayBtn : "linked",
		clearBtn : true,
		autoclose : true,
		todayHighlight : true
	});
	});
	function jssubmit() {
		$("#storeform").submit();
	}
	function setnew(){
		$("input[name='guard.id']").val(gid);
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>

