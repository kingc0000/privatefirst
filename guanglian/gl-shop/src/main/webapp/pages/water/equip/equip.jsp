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
<link href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />' rel="stylesheet">
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
				<form:form cssClass="form-horizontal" role="form"
					commandName="equip" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">设备名称</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="name"
								id="name" />
							<span class="help-block"> <form:errors path="name"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">设备编号</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="eNO" id="eNO" />
							<span class="help-block"> <form:errors path="eNO"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">检定日期</label>
						<div class="col-lg-10 col-sm-10 ">
							<div class="date edate input-group">
								<form:input cssClass="form-control"
								path="testDate" id="testDate" />
							<span class="input-group-addon btn-theme"><i
								class="fa fa-calendar"></i></span> <span class="help-block"> <form:errors
									path="testDate" cssClass="error" /></span>
							</div>
							
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">检定有效期</label>
						<div class="col-lg-10 col-sm-10 ">
							<div class="date edate input-group">
								<form:input cssClass="form-control"
								path="testVaild" id="testVaild" />
							<span class="input-group-addon btn-theme"><i
								class="fa fa-calendar"></i></span> <span class="help-block"> <form:errors
									path="testVaild" cssClass="error" /></span>
							</div>
							
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">状态</label>
						<div class="col-lg-10">
							<form:select cssClass="form-control required" path="status" id="status" >
                               		<form:option value="1">正常</form:option>
                               		<form:option value="0">检修</form:option>
                               		<form:option value="-1">报废</form:option>
							</form:select>
							<span class="help-block"> <form:errors path="status"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">附件资料</label>
						<div class="col-lg-9 col-sm-9">
							<div class="row" id="attachs">
							</div>
						</div>
						<div class="col-lg-1 col-sm-1">
							<button type="button"
								class="addBtn btn btn-info fa fa-plus-square"
								onclick="addAttach()"></button>
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
					<input type="hidden" name="delids" id="delids"/>
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
									<th>设备名称</th>
									<th>设备编号</th>
									<th>检定日期</th>
									<th>检定有效期</th>
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
							},  {
								"mData" : "name"
							}, {
								"mData" : "eNO"
							}, {
								"mData" : "testDate"
							}, {
								"mData" : "testVaild"
							}, {
								"mData" : "status"
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
										"aTargets" : [ 5 ],
										"mRender" : function(data, type, full) {
											var s="正常";
											if(data==-1){
												s="报废";
											}else if(s==0){
												s="检修";
											}
											return s;
										},
										"bSortable" : false
									},
									{
										"aTargets" : [ 6 ],
										"mRender" : function(data, type, full) {
											var s = '<a class="edit btn btn-primary btn-xs tooltips hasfn" data-placement="left" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';
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
		fomdatesubmit();
	}
	function setpid(){
		$("input[id='status']").val(0);
		//$("select[id='rank']").val(1);
		$("select").prop("selectedIndex", 0);
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".attachItem").remove();
	}
	function dolistafterfn(oTable, nRow){
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".attachItem").remove();
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
				if(data!="-1"){
					if(data!="0"){
						var fname= data;
						if(fname!=null && fname.length >0){
							$.each(fname,function(index,img){
								addAttach(img);
							});
						}
					}
				}else{
					toastr.error("项目图纸加载失败，请重试");
				}
			},
			error : function(data, xhr, textStatus, errorThrown) {
				console.log(data.responseText);
				//fnCallback(data);
				toastr.error("项目图纸加载失败，请重试");
				$('#loading').hide();
			}
		});
		
	}
	function  addAttach(data){
		var str='<div class="attachItem"><div class="fileupload fileupload-new col-lg-11 col-sm-11" data-provides="fileupload">'
		       
	        if(data!=null){
	        	str +='<span class="default" id="'+data.id+'" style="display:none"/></span>';
	        	if(data.fileType==true){
		        	str +='<span class="preview" style="margin-left:10px;" >';
		        	str +='<a class="tooltips" data-placement="left" data-original-title="预览" href="'+_context+'/files/preview/'+data.fileName+'?ftype=EQUIPMENT" ';
		        	if($(window).width()>767){
		        		str += ' target="_blank"';
		        	}
		        	str +='><i class="fa fa-eye">'+data.fileName+'</i></a>&nbsp;</span>';
		        	 str +=' <span class="download default hidden-xs" id="'+data.fileName+'"  style="margin-left:10px;" >';
	        	}
	        	 str +='<a class="tooltips hidden-xs" data-placement="left" data-original-title="下载" href="'+_context+'/files/downloads/'+data.fileName+'?ftype=EQUIPMENT" ><i class="fa fa-download">'+data.fileName+'</i></a>&nbsp;</span>';
	        }else{
	        	 str += ' <span class="btn btn-white btn-file"><span class="fileupload-new"><i class="fa fa-paper-clip"></i> 选择文件</span>';
	 	        str +='<span class="fileupload-exists"><i class="fa fa-undo"></i>修改</span>';
	        	str +='<input type="file" class="default"  name="fileupload" /></span>';
	        	str +='<span class="fileupload-preview" style="margin-left:5px;"></span>';
		        str +='<a href="#" class="close fileupload-exists" data-dismiss="fileupload" style="float: none;font-size: 14px; margin-left:5px;">重设</a>';
		        str +='  <span class="preview" style="margin-left:10px;" ></span>';
	        	 str +=' <span class="download" style="margin-left:10px;" ></span>';
	        }
	        str +='</div><div class="col-lg-1 col-sm-1"><button type="button" class="addBtn btn btn-info fa fa-minus-square" onclick="removeAttach()"></button></div></div>';
		//var clone = $(".imgItem").filter(":first").clone();
		$('#attachs').append(str);
	}
	function removeAttach(e){
		if($(".attachItem").length>0){
			e=e||event;
			if($(e.target).parents(".attachItem").find('.default').attr("id")!="" && $(e.target).parents(".attachItem").find('.default').attr("id")!=undefined){
				var rdel = confirm("您确定要删除这个资料吗？");
				if (rdel == false)
					return;
				if($('#delids').val()!=""){
					$('#delids').val($('#delids').val()+","+$(e.target).parents(".attachItem").find('.default').attr("id"));
				}else{
					$('#delids').val($(e.target).parents(".attachItem").find('.default').attr("id"));
				}
				
			}
			var o = $(e.target).parents(".attachItem");
			$(o).remove();
		}
	}
	</script>

<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
