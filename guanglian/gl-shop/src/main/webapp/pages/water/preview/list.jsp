<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>

<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet"/>
<script>
$("#pictitle").html("项目评论汇总");
</script>
<style>
.printnote table {
	width: 100%!important;
}
</style>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				项目评论汇总
			</header>
			<div class="panel-body">
				<div class="col-lg-12 col-sm-12">
					<section class="adv-table " id="no-more-tables">
						<table class="display table table-bordered table-striped"
							id="slist">
							<thead>
								<tr>
									<th>项目</th>
									<th>评论数</th>
									<th>总分</th>
									<th>项目管理员</th>
									<th>项目工人</th>
									<th>安全管理</th>
									<th>项目质量</th>
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
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
	var uid = '${sessionScope.ADMIN_USER.id}';
	$(function() {
		$('#slist').dataTable({
			"bPaginate" : true, //不显示分页
			"bProcessing" : true,
			"bServerSide" : true,
			"bLengthChange" : true,
			"bFilter" : true,
			"bSortClasses" : false,
			"sDom" : "lrtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource" : "server_processing.shtml",
			"fnServerData" : retrieveData,
			"aoColumns" : [ {
				"mData" : "name"
			}, {
				"mData" : "total"
			}, {
				"mData" : "total"
			}, {
				"mData" : "score1"
			}, {
				"mData" : "score2"
			}, {
				"mData" : "score3"
			}, {
				"mData" : "score4"
			}, {
				"mData" : "id"
			}

			],
			"aoColumnDefs" : [
					{
						"aTargets" : [2],
						"mRender" : function(data, type, full) {
							var s1 = Number(full.score1).toFixed(1);
							var s2 = Number(full.score2).toFixed(1);
							var s3 = Number(full.score3).toFixed(1);
							var s4 = Number(full.score4).toFixed(1);
							var s = s1*0.3+s2*0.1+s3*0.3+s4*0.3;
							return s.toFixed(1);
						},
						"bSortable" : false
					}, 
					{
						"aTargets" : [3,4,5,6],
						"mRender" : function(data, type, full) {
							return Number(data).toFixed(1);
						},
						"bSortable" : false
					}, 
					{
						"aTargets" : [7],
						"mRender" : function(data, type, full) {
							var res = '<a class="btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="用户评论" href="detaillist.html?cid='+full.id+'" ><i class="fa fa-comments"></i></a>&nbsp;';
							return res;
						},
						"bSortable" : false
					}, {
						"aTargets" : [ "_all" ],
						"bSortable" : false
					} ],
			"fnInitComplete" : function(settings, json) {
				if(isTouchDevice()===false) {
           		 $('.tooltips').tooltip();
           		}
			}
		});
		
	});
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
