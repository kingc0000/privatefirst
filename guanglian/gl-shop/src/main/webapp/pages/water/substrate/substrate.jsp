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
<link href='<c:url value="/resources/css/fileicon.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet">
<link
	href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />'
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
					commandName="substrate" id="storeform">
					
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">备注</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="remark"
								id="remark" />
							<span class="help-block"> <form:errors path="remark"
									cssClass="error" /></span>
						</div>
					</div>
					
					<div class="form-group">
		                <label class="col-lg-2 col-sm-2 control-label ">附件</label>
		                <div class="col-lg-9 col-sm-9">
		                	<div class="row" id="wimgs">
		                	</div>
		                </div>
		                <div class="col-lg-1 col-sm-1">
		                	<button type="button" class="addBtn btn btn-info fa fa-plus-square" onclick="addImage()"></button>
		                </div>
           			 </div>
					<c:if test="${hasRight==true}">
					<div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="jssubmit()"
								class="btn btn-success">提交</button>
						</div>
					</div></c:if>
					<form:hidden path="id" />
					<form:hidden path="monitor.id" />
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
					onclick="changeEdittable(setpid)">新增</button></c:if>
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
									<th>日期</th>
									<th>备注</th>
									<th>上传人员</th>
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
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
<script>
$('#pictitle').html("${mentity.name }项目交底物");
var mid = '${mid}';
var tar = ' target="_blank"';
if ($(window).width()<768){
tar='';
}
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
							"sDom" : "lf<c:if test="${hasRight==true}"><'#del_btn'></c:if>rtip",
							"sAjaxSource" : "server_processing.shtml?mid="+mid,
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							}, {
								"mData" : "auditSection.dateCreated"
							}, {
								"mData" : "remark"
							} ,{
								"mData" : "auditSection.modifiedBy"
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
											var s='<a class="preview btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="预览" href="<c:url value="/water/substrate/preview.html?mid='
												+ mid+ '&did='+ data+ '"/>" '+ tar+ '><i class="fa fa-eye"></i></a>';
											s += '&nbsp;<a class="edit btn btn-primary hasfn btn-xs hasfn tooltips " data-placement="left" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';
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
	});
	function jssubmit() {
		fomdatesubmit("save.shtml");
	}
	
	function setpid(){
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
		$("input[name='monitor.id']").val(mid);
	}
	
	function dolistafterfn(oTable, nRow){
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
		$("input[name='monitor.id']").val(mid);
		var aData = oTable.fnGetData(nRow);
		//异步加载图片
		$('#loading').show();
		$.ajax({
			type : "POST",
			url : "imges.shtml",
			dataType : "json",
			data : "muid="+aData["id"],
			success : function(data) {
				$('#loading').hide();
				if(data!="-1"){
					if(data!="0" && data!=null){
						//图片
						if(data!=null && data.length >0){
							for(var i=0;i<data.length;i++){
								if(data[i].fileType==true){
									addImage(data[i].fileName,data[i].fileName,data[i].id,"MONITOR_SUB");
								}else{
									addImage(data[i].fileName,"",data[i].id,"MONITOR_SUB");
								}
								
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
				toastr.error("附件加载失败，请重试");
				$('#loading').hide();
			}
		});
	}
	
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
 <jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>
