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
	href='<c:url value="/resources/assets/bootstrap-datetimepicker/css/datetimepicker.css" />'
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
					commandName="diameterconvertdata" id="storeform">
					<jsp:include page="/common/mdBase.jsp"></jsp:include>
					<c:if test="${hasRight==true}">
					<div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" id="subbtn" onclick="jssubmit()"
								class="btn btn-success">提交</button>
						</div>
					</div>
				</c:if>	
					<form:hidden path="id" />
					<form:hidden path="spoint.id" />
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
									<th>校准时间</th>
									<th>上次高程</th>
									<th>本次高程</th>
									<th>本次变化量</th>
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

<script>
$('#pictitle').html("${gentity.name }直径收敛信息");
	var sid = '${sid}';
	var sum = 0;
	var flag = false;
	$(function() {
		$('#slist')
				.dataTable(
						{
							"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : false,
							"bSortClasses" : false,
							"sDom" : "lf<c:if test="${hasRight==true}"><'#del_btn'></c:if>rtip",
							"sAjaxSource" : "server_processing.shtml?sid="
									+ sid,
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							}, {
								"mData" : "calibration"
							}, {
								"mData" : "initHeight"
							}, {
								"mData" : "curtHeight"
							}, {
								"mData" : "initHeight"
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
											var s = (full.curtHeight - data) ;
											return s;
										}
									},
									{
										"aTargets" : [ 5 ],
										"mRender" : function(data, type, full) {
											var s = '<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-original-title="删除" onclick="deldata('
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
		$('.datetimeControl').datetimepicker({
			format : "yyyy-mm-dd hh:ii:ss",
			autoclose : true,
			todayBtn : true,
			minuteStep : 1,
			pickerPosition : "bottom-left",
			callback : setHeight
		});
		$("input[name='curtHeight']")
				.on(
						"keyup",
						function(event) {
							var sch = (Number($("input[name='curtHeight']")
									.val()) - Number($(
									"input[name='initHeight']").val())) * 1000;
							$("#vDisplacement").val(sch);
							var initheight = '${initHeight}';
							var sum = ((Number($("input[name='curtHeight']")
									.val()) - initheight)) * 1000;
							$("#sumDisplacement").val(sum);
						});

	});
	function setHeight() {
		var date = $("input[name='calibration']").val();
		var id = sid;
		$.ajax({
			type : "POST",
			url : "getByDate.shtml",
			data : "date=" + date + "&sid=" + id,
			success : function(result) {
				if (result != null && result != "") {
					$("#subbtn").show();
					$("input[name='initHeight']").val(result.last);
				} else {
					$("#subbtn").hide();
					toastr.error('获取信息失败请重试！');
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				$("#subbtn").hide();
				toastr.error('获取信息失败请重试！');

			}
		});
	}
	function jssubmit() {
		$("#storeform").submit();
	}
	function dolistafterfn(oTable, nRow) {
		$("input[name='spoint.id']").val(sid);
		$("#cdate").show();
	}
	function setnew() {
		$('#loading').show();
		$("input[name='spoint.id']").val(sid);
		$("#cdate").show();
		var id = sid;
		$.ajax({
			type : "POST",
			url : "getMain.shtml",
			data : "listId=" + id,
			success : function(result) {
				$('#loading').hide();
				if (result != null && result != "") {
					$("#subbtn").show();
					$("input[name='initHeight']").val(result.init);
				} else {
					$("#subbtn").hide();
					toastr.error('获取信息失败请重试！');
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				$("#subbtn").hide();
				toastr.error('获取信息失败请重试！');
			}
		});
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script
	src='<c:url value="/resources/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" />'></script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
