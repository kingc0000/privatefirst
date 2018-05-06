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
<link href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet">

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
				<jsp:include page="/pages/water/guard/gdetail.jsp" />
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
					onclick="changeEdittable(setpid)">新增</button>
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
									<th>项目名称</th>
									<th>项目负责人</th>
									<th>技术负责人</th>
									<th>联系电话</th>
									<th>省份</th>
									<th>城市</th>
									<th>地址</th>
									<th>状态</th>
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
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
<script src='<c:url value="/resources/assets/ckeditor/ckeditor.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script>
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
							"sAjaxSource" : "server_processing.shtml",
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							}, {
								"mData" : "project.name"
							}, {
								"mData" : "project.projectOwner"
							}, {
								"mData" : "projectTechName"
							}, {
								"mData" : "project.phone"
							}, {
								"mData" : "project.zone.name"
							}, {
								"mData" : "project.city"
							}, {
								"mData" : "project.address"
							}, {
								"mData" : "status"
							}, {
								"mData" : "project.memo"
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
										"aTargets" : [ 1 ],
										"mRender" : function(data, type, full) {
											return '<a class="tooltips" data-placement="left" data-original-title="详情" href="'+_context+'/water/guard/detail.html?gid='+full.id+'" >'+data+'</a>';
										},
										"bSortable" : false
									},
									{	
										"aTargets" : [ 8 ],
										"mRender" : function(data, type, full) {
											var re=data;
											$("#status option").each(function () {
											       var txt = $(this).text(); //获取单个text
											       var val = $(this).val(); //获取单个value
											       if(val==data){
											    	   re= txt;
											    	   return re;
											       }
											  });
											return re;
										},
										"bSortable" : false
									},
									{
										"aTargets" : [ 10 ],
										"mRender" : function(data, type, full) {
											 var s="";
											 if(full.sstatus==1){
												 var s = '<a class="edit btn btn-primary btn-xs tooltips hasfn" data-placement="left" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';
													s += '&nbsp;<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-original-title="删除" onclick="deldata('
															+ data
															+ ',&quot;您真的要删除该对象吗？&quot; )"><i class="fa fa-trash-o "></i></a>';
													$("#jssub").show();
											 }else{
												 $("#jssub").hide();
											 }
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
	function setpid(){
		$("input[id='status']").val(0);
		$("input[name='project.features']").val(0);
		//$("select[id='rank']").val(1);
		$("select").prop("selectedIndex", 0);
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
	}
	function jssubmit() {
		fomdatesubmit();
	}
	function setVisablePhone(){
		$("#phonediv").hide();
	}
	function dolistafterfn(oTable, nRow){
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
		var aData = oTable.fnGetData(nRow);
		//异步加载图片
		$('#loading').show();
		$.ajax({
			type : "POST",
			url : "images.shtml",
			dataType : "json",
			data : "cid="+aData["id"],
			success : function(data) {
				$('#loading').hide();
				if(data=="-1"){
					toastr.error("项目图纸加载失败，请重试");
				}else if(data!="0"){
					var fname= data;
					if(fname!=null && fname.length >0){
						$.each(fname,function(index,img){
							addImage(img.name,img.jpeg,img.id,"PRODUCT_DIGITAL");
						});
					}
				}
			},
			error : function(data, xhr, textStatus, errorThrown) {
				console.log(data.responseText);
				//fnCallback(data);
				toastr.error("项目图纸加载失败，请重试");
				$('#loading').hide();
			}
		});
		var pbase=aData["project"];
		var summry =pbase.summary;
		CKEDITOR.instances['summary'].setData(summry);
		
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
