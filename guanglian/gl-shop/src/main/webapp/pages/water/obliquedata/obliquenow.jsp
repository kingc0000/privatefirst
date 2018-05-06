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
<div class="row" >
	<div class="col-md-12">
		<section class="panel">
			<header class="panel-heading">
				深度：<span id="nowdepth"> </span>
				<button id="nextbtn" type="button"
					class="btn btn-theme pull-right fa fa-arrow-down visible-xs"
					onclick="nextdepth()">下一个</button>
					<button type="button" id="lastbtn"
					class="btn btn-theme pull-right fa fa-arrow-up visible-xs"
					onclick="lastdepth()">上一个</button>
			</header>
			<div class="panel-body">
				<div class="col-xs-12" style="padding:10px;">
					<div class="input-group">
					      <input type="text" class="form-control" id="mname" placeholder="请输入深度，精确匹配">
					      <span class="input-group-btn">
					        <button class="btn btn-default fa fa-search" style="height: 34px;" onclick="getdata()"></button>
					      </span>
					    </div>
				</div>
				<form:form cssClass="form-horizontal" role="form"
					commandName="obliquedata" id="storeform">
					<div class="form-group" id="cdate" style="display: none">
						<label class="col-lg-2 col-sm-2 control-label control-required">本次日期</label>
						<div class="col-lg-10">
							<div class="input-group date datetimeControl">
								<form:input cssClass="form-control required" path="curDate"
									id="curDate" />
								<div class="input-group-btn">
									<button type="button" class="btn btn-danger date-reset">
										<i class="fa fa-times"></i>
									</button>
									<button type="button" class="btn btn-warning date-set">
										<i class="fa fa-calendar"></i>
									</button>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">上次累计</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="lastTotal"
								id="lastTotal" readonly="true"/>
							<span class="help-block"> <form:errors path="lastTotal"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">本次累计</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="curTotal"
								id="curTotal" />
							<span class="help-block"> <form:errors path="curTotal"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">位移增量</label>
						<div class="col-lg-10">
							<input class="form-control" id="disIncrement"
								readonly="readonly" />
						</div>
					</div>
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
					<form:hidden path="depth.id" />
				</form:form>
			</div>
		</section>
	</div>
</div>

<div class="row" style="display: none;" id="datalist">
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
									<th>上次累计</th>
									<th>本次累计</th>
									<th>位移增量</th>
									<th>本次时间</th>
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
<script src='<c:url value="/resources/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" />'></script>
<script>
$('#pictitle').html("${mentity.name }测斜信息");
	var sid = '${sid}';
	var tid = '${tid}';
	var mid = '${mid}';
	var total=0;
	var page=0;
	$(function() {
		getdata();
		//加载日期控件
		$('.datetimeControl').datetimepicker({
			format : "yyyy-mm-dd hh:ii:ss",
			autoclose : true,
			todayBtn : true,
			minuteStep : 1,
			pickerPosition : "bottom-left",
			callback : setHeight
		});
		getdatalist();
		$("input[name='curTotal']")
				.on(
						"keyup",
						function(event) {
							var sch = (Number($("input[name='curTotal']").val()) - Number($(
									"input[name='lastTotal']").val()));
							$("#disIncrement").val(sch);
						});

	});

	function getdatalist(){
		$('#slist').dataTable(
				{
					"bPaginate" : true, //不显示分页
					"bProcessing" : true,
					"bServerSide" : true,
					"bLengthChange" : true,
					"bFilter" : false,
					"bSortClasses" : false,
					"sDom" : "lf<c:if test="${hasRight==true}"><'#del_btn'></c:if>rtip",
					"sAjaxSource" : "server_processing.shtml?tid="+tid,
					"fnServerData" : retrieveData,
					"aoColumns" : [ {
						"mData" : "id"
					}, {
						"mData" : "lastTotal"
					}, {
						"mData" : "curTotal"
					}, {
						"mData" : "lastTotal"
					}, {
						"mData" : "curDate"
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
								"bVisible" : true,
								"aTargets" : [ 3 ],
								"mRender" : function(data, type, full) {
									var s = (full.curTotal - data);
									return s;
								},
								"bSortable" : false
							},
							{
								"aTargets" : [ 5 ],
								"mRender" : function(data, type, full) {
									var s = '<a class="edit hasfn btn btn-primary btn-xs tooltips " data-placement="left" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';
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
	}
	
	function setHeight() {
		var date = $("input[name='curDate']").val();
		var id = sid;
		var ids = tid;
		$.ajax({
			type : "POST",
			url : "getByDate.shtml",
			data : "date=" + date + "&sid=" + sid + "&tid=" + ids,
			success : function(result) {
				if (result != null && result != "") {
					$("#subbtn").show();
					$("input[name='lastTotal']").val(result.last);
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
		$("input[name='depth.id']").val(tid);
		$("#cdate").show();
	}
	function setnew() {
		$("input[name='depth.id']").val(tid);
		$("input[name='lastTotal']").val(0);
		$("#cdate").show();
	}
	function getdata(){
		$('#loading').show();
		$.post('oblique.shtml',{"page":page, "mname":$("#mname").val(),"mid":mid}, function(response){
			if(response!=null && response!=""){
				$("#nowdepth").html(response.mname);
				$("#lastTotal").val(response.last);
				total=response.total;
				tid=response.tid;
				refreshData(tid);
				$("#curTotal").val('');
				$("input[name='depth.id']").val(tid);
				$("#datalist").show();$("#subbtn").show();
			}else{
				toastr.error('获取数据失败');$("#subbtn").hide();
			}
			$('#loading').hide();
		}).error(function() {$('#loading').hide();toastr.error('获取数据失败'); $("#subbtn").hide();});
	}
	function nextdepth(){
		$("#cdate").hide();
		$("#mname").val('');
		if(page<total-1){
			page++;
			getdata();
		}else{
			toastr.error('已经到末尾');
		}
	}
	function lastdepth(){
		$("#cdate").hide();
		$("#mname").val('');
		if(page<=0){
			page=0;
			toastr.error('已经到第一页');
		}else{
			page--;
			getdata();
		}
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
