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

<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				列表<button id="nextbtn" type="button"
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
									<th>要/销点时间</th>
									<th>项目</th>
									<th>人员</th>
									<th>车站</th>
									<th>类型</th>
									<th>签到时间</th>
									<th>签到地点</th>
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
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
$('#pictitle').html("${gentity.name }人员签到信息");
var gid='${gid}';
var uid='${uid}';
var utype='${utype}';
	$(function() {
$('#slist').dataTable({
							"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : false,
							"bSortClasses" : false,
							"sDom" : "lf<'#mytool'>rtip",
							"sAjaxSource" : "server_processing.shtml?gid="+gid+"&uid="+uid+"&utype"+utype,
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "shouleBe"
							}, {
								"mData" : "pName"
							}, {
								"mData" : "uName"
							}, {
								"mData" : "station"
							}, {
								"mData" : "stype"
							}, {
								"mData" : "auditSection.dateModified"
							}, {
								"mData" : "signaddress"
							}, {
								"mData" : "sattus"
							}, {
								"mData" : "id"
							}],
							"aoColumnDefs" : [
									{
										"aTargets" : [ 4 ],
										"mRender" : function(data, type, full) {
											if(data==0){
												return "要点";
											}else{
												return "销点";
											}
										},
										"sWidth" : "120px",
										"bSortable" : false
									},{
										"aTargets" : [ 5 ],
										"mRender" : function(data, type, full) {
											if(full.sattus==0){
												return "";
											}else{
												return data;
											}
										},
										"sWidth" : "120px",
										"bSortable" : false
									},{
										"aTargets" : [ 7 ],
										"mRender" : function(data, type, full) {
											if(data==0){
												return "未签到";
											}else{
												return "已签到";
											}
										},
										"sWidth" : "120px",
										"bSortable" : false
									},
									{
										"aTargets" : [ 8 ],
										"mRender" : function(data, type, full) {
											var s = '<a class="btn btn-primary btn-xs tooltips " data-placement="left" data-original-title="查看" href="'+_context+'/water/sign/detail.html?sid='+data+'" ><i class="fa fa-list"></i></a>';
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
				var dseelct ='<div style="padding: 15px 5px 15px 0;" class="pull-right"><select id="sclass" class="form-control">'+
				'<option value ="0">要点</option><option value ="1">销点</option>'+
				'<option value ="" selected = "selected">全部类型</option></select></div>';
				dseelct+='<div style="padding: 15px 5px 15px 0;" class="pull-right"><select id="stype" class="form-control">'+
				'<option value ="0">未签到</option><option value ="1">已签到</option>'+
				'<option value ="" selected = "selected">全部状态</option></select></div>';
				 $("#mytool").html(dseelct);
				 $('#sclass').change(function(){
					 getfiter();
					 oTable.fnReloadAjax();
				 });
				 $('#stype').change(function(){
					 getfiter();
					 oTable.fnReloadAjax();
				 });
	});
	function getfiter(){
		filterjson= {"stype":$("#sclass").val(),"status":$("#stype").val()};
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
