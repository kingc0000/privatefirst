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
$("#pictitle").html("用户评论列表");
</script>
<style>
</style>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				用户评论列表-<span class="projectName"></span>
			</header>
			<div class="panel-body">
				<div class="col-lg-12 col-sm-12">
					<section class="adv-table " id="no-more-tables">
						<table class="table table-bordered table-striped"
							id="slist" style="table-layout: fixed;">
							<thead>
								<tr>
									<th>用户</th>
									<th>总分</th>
									<th>项目管理员</th>
									<th>项目工人</th>
									<th>安全管理</th>
									<th>项目质量</th>
									<th>评论内容</th>
									<th>评论时间</th>
									<th>图片</th>
									<th>处理人</th>
									<th>反馈内容</th>
									<th>反馈时间</th>
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
	var sScrollX = "100%";
	var sScrollXInner = "100%";
	var _target = "_self"
	if($(window).width()>768) {
		sScrollX = "150%";
		sScrollXInner = "160%";
		_target = "_blank";
	}
	$(function() {
		$('#slist').dataTable({
			"bPaginate" : true, //不显示分页
			"bProcessing" : true,
			"bServerSide" : true,
			"bLengthChange" : true,
			/* "sScrollX": sScrollX,
			"sScrollXInner": sScrollXInner, */
			"sScrollY": "auto",
			"bScrollCollapse": true,
			"bFilter" : true,
			"bSortClasses" : false,
			"sDom" : "lrtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource" : "detail_list.shtml?cid=${cid}",
			"fnServerData" : retrieveData,
			"aoColumns" : [ {
				"mData" : "userName"
			}, {
				"mData" : "id"
			}, {
				"mData" : "score1"
			}, {
				"mData" : "score2"
			}, {
				"mData" : "score3"
			}, {
				"mData" : "score4"
			}, {
				"mData" : "comment"
			}, {
				"mData" : "dateCreated"
			}, {
				"mData" : "id"
			}, {
				"mData" : "feedbackName"
			}, {
				"mData" : "feedback"
			}, {
				"mData" : "dateModified"
			}, {
				"mData" : "id"
			}
			],
			"aoColumnDefs" : [
					{
						"aTargets" : [1],
						"mRender" : function(data, type, full) {
							var s1 = Number(full.score1).toFixed(1);
							var s2 = Number(full.score2).toFixed(1);
							var s3 = Number(full.score3).toFixed(1);
							var s4 = Number(full.score4).toFixed(1);
							var s = s1*0.3+s2*0.1+s3*0.3+s4*0.3;
							return s.toFixed(1);
						},
						"sWidth" : "100px",
						"bSortable" : false
					}, 
					{
						"aTargets" : [2,3,4,5],
						"mRender" : function(data, type, full) {
							return Number(data).toFixed(1);
						},
						"sWidth" : "100px",
						"bSortable" : false
					},
					{
						"aTargets" : [8],
						"mRender" : function(data, type, full) {
							var s='<a target="'+_target+'" href="<c:url value="/water/preview/imgages.html?id='+data+'"/>">查看评论图片</a>';
							return s;
						},
						"bSortable" : false
					},
					{
						"aTargets" : [10],
						"mRender" : function(data, type, full) {
							var feedback = $("<textarea class='feedback' rows='5' style='width:100%;' id='feedback_"+full.id+"'>");
							feedback.html(data);
							return feedback.prop("outerHTML");
						},
						"bSortable" : false
					},
					{
						"aTargets" : [12],
						"mRender" : function(data, type, full) {
							var res = '<a class="btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="保存" href="javascript:;" onclick="savedata('+data+')"><i class="fa fa-save"></i></a>&nbsp;';
							res += '<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata(' + data + ')"><i class="fa fa-trash-o "></i></a>&nbsp;';
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
				$.each($(".feedback"), function(i, item){
					
					$(this).height($(this).parent().height());
				});
			}
		});
		
	});
	
	function savedata(id) {
		var url="feedback.shtml" ;
		var feedback = $("#feedback_"+id).val();
		if(feedback=='') {
			alert("请输入反馈信息");
			return false;
		}
		var dt = {
				"id": id,
				"feedback": feedback
		}
		$('#loading').show();
		$.ajax({
			type : "POST",
			cache: false, //cache设置为false，上传文件不需要缓存。
			url : url,
			data : dt,
			async : true,
			success: function(response, stat){  
				$('#loading').hide();
				var status = response.response.status;
				if (status == 0 || status == 9999) {
					toastr.success('操作成功！');
					refreshData();
				} else if(status==-1){
					toastr.error(response.response.statusMessage);
				} else {
					toastr.error('提交失败！');
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				alert('error ' + errorThrown);
			}
		});
		return false;
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
