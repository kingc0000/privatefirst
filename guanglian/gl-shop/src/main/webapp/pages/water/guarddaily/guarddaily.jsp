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
	<link href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet">
<script>
	var nowdate = "${nowdate}";
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
					commandName="guarddaily" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">时间</label>
						<div class="col-lg-10">
							<input class="form-control required"  id="datec"  name="datec"  readonly="readonly">
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">现场监护意见</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control required" rows="6"  id="pointDesc" path="pointDesc"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">地铁结构病害调查报表</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" rows="6"  id="weiHuW" path="weiHuW"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">总结</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control required" rows="6"  id="conclusion" path="conclusion"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">照片</label>
						<div class="col-lg-9 col-sm-9">
							<div class="row" id="wimgs"></div>
						</div>
						<div class="col-sm-1 col-xs-6">
							<button type="button"
								class="addBtn btn btn-info fa fa-plus-square"
								onclick="addImage()"></button>
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
					<input type="hidden" name="delids" id="delids" />
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
					onclick="changeEdittable(afernew)">新增</button>
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
									<th>日志日期</th>
									<th>填报用户</th>
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
$('#pictitle').html("${gentity.name }项目日志信息");
var gid = '${gid}'; //项目id
var tar = ' target="_blank"';
if ($(window).width()<768){
tar='';
}
var now='${nowdate}';
	$(function() {
$('#slist').dataTable({
												"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSortClasses" : false,
							"sDom" : "lf<c:if test="${hasRight==true}"><'#del_btn'></c:if>rtip",
							"sAjaxSource" : "server_processing.shtml",
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							} , {
								"mData" : "datec"
							}, {
								"mData" : "auditSection.modifiedBy"
							}, {
								"mData" : "id"
							} ],
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
											var reStr = '';
											reStr += '<a class="preview btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="预览" href="<c:url value="/water/guarddaily/preview.html?gid='
													+ gid+ '&did='+ data+ '"/>" '+ tar+ '><i class="fa fa-eye"></i></a>&nbsp;';
											var nowDate = new Date(nowdate);
											var myDate = new Date(full.datec);
											<c:if test="${hasRight==true }">
											if (nowDate.getFullYear() == myDate
													.getFullYear()
													&& nowDate.getMonth() == myDate
															.getMonth()
													&& nowDate.getDate() == myDate
															.getDate()) {
												reStr += '<a class="btn btn-primary btn-xs tooltips edit hasfn" data-placement="left" data-container="body" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>'
											}
											</c:if>
											return reStr;
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
	});
	function jssubmit() {
		fomdatesubmit();
	}
	function dolistafterfn(oTable, nRow) {
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
		var aData = oTable.fnGetData(nRow);
		//异步加载图片
		$('#loading').show();
		$.ajax({
			type : "POST",
			url : "imges.shtml",
			dataType : "json",
			data : "guid="+aData["id"],
			success : function(data) {
				$('#loading').hide();
				if(data!="-1"){
					if(data!="0" && data!=null){
						//图片
						if(data!=null && data.length >0){
							for(var i=0;i<data.length;i++){
								addImage(data[i].dailyImage,data[i].jpeg,data[i].id,"GUARD_DAILY");
							}
						}
					}
				}else{
					toastr.error("图片加载失败，请重试");
				}
			},
			error : function(data, xhr, textStatus, errorThrown) {
				console.log(data.responseText);
				//fnCallback(data);
				toastr.error("图片加载失败，请重试");
				$('#loading').hide();
			}
		});
		//重新初始化图片控件
		var aData = oTable.fnGetData(nRow);
		var fname= aData["datec"];
		$("input[name='guard.id']").val(gid);
		$("#datec").val(fname);
	}
	function afernew(){
		$("#datec").val(now);
		var oTable = $('#slist').dataTable();
		var aData = oTable.fnGetData(0);
		if(aData!=null){
			var odate1=aData["datec"];
			if(now==odate1){
				editRow(oTable, 0,true);
			}
		}
		$("input[name='guard.id']").val(gid);
	}
	
</script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
<script src='<c:url value="/resources/js/project/list.js" />'></script>
