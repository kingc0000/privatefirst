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
					commandName="gonproblem" id="storeform">
					<div class="form-group">
		                <label class="col-lg-2 col-sm-2 control-label control-required">问题紧急程度</label>
		                <div class="col-lg-10">
		                		<form:select path="monRank" class="required form-control" items="${applicationScope.problem_degree}" itemLabel="name" itemValue="name"></form:select> 
		                </div>
		            </div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">问题类型</label>
						<div class="col-lg-10">
						<form:select path="monType" class="required form-control" items="${applicationScope.bd_guard_ptype}" itemLabel="name" itemValue="name"></form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">问题详情</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" rows="6"  id="monDetail" path="monDetail"/>
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
				</div>
			</c:if>	
					<form:hidden path="id" />
					<form:hidden path="guard.id" />
					<form:hidden path="owner" />
					<form:hidden path="monStatus" />
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
									<th>紧急程度</th>
									<th>问题类型</th>
									<th>负责人</th>
									<th>提交日期</th>
									<th>问题状态</th>
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
<div class="modal fade" id="do-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
              <button class="close" data-dismiss="modal" type="button">&times;</button>
              <h4 class="modal-title" id="etitle">处理问题</h4>
            </div>
            <div class="modal-body">
              <form action="doman.shtml" id="export-form" class="form-horizontal" method="POST">
              	 <div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">处理意见</label>
						<div class="col-lg-10">
							<textarea class="form-control" rows="6"  id=suggest></textarea>
						</div>
					</div>
              	 <br>
	              	<hr  style="margin-top:10px">
              	<div class="form-group">
					<div class="col-lg-2 col-sm-2"></div>
					<div class="col-lg-10">
						<button type="button" onclick="domanaction()" class="btn btn-success pull-right">确定</button>
					</div>
				</div>
				<br/>
				<input type="hidden"  name="guid" id="guid"/>
              </form>
            </div>
        </div>
      </div>
    </div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
$('#pictitle').html("${gentity.name }项目问题");
var gid = '${gid}'; //项目id
var tar = ' target="_blank"';
if ($(window).width()<768){
tar='';
}
	$(function() {
$('#slist').dataTable({
												"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSortClasses" : false,
							"sDom" : "lf<c:if test="${hasRight==true }"><'#del_btn'></c:if><'#filter_btn'>rtip",
							"sAjaxSource" : "server_processing.shtml?gid="+gid,
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							}, {
								"mData" : "monRank"
							}, {
								"mData" : "monType"
							}, {
								"mData" : "owner"
							}, {
								"mData" : "auditSection.dateCreated"
							}, {
								"mData" : "monStatus"
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
											if(data==0){
												return "未处理";
											}else{
												return "已处理";
											}
										},
										"sWidth" : "120px",
										"bSortable" : false
									},
									{
										"aTargets" : [ 6 ],
										"mRender" : function(data, type, full) {
											var s='<a class="preview btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="预览" href="<c:url value="/water/gonproblem/preview.html?gid='
													+ gid+ '&did='+ data+ '"/>" '+ tar+ '><i class="fa fa-eye"></i></a>';
										<c:if test="${hasRight==true }">
												if(full.monStatus==0){
												 s+= '&nbsp;<a class="edit btn hasfn btn-primary btn-xs tooltips " data-placement="left" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';
												 s+= '&nbsp;<a class="btn btn-danger btn-xs tooltips " data-placement="left" data-original-title="处理" href="javascript:;" onclick="doman('+data+')"><i class="fa fa-eraser"></i></a>';
												}	
										</c:if>
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
				'<option value ="0">未处理</option><option value ="1">已处理</option>'+
				'<option value ="" selected = "selected">处理状态</option></select></div>';
				 $("#filter_btn").html(dseelct);
				 $('#sclass').change(function(){
					 getfiter();
					 oTable.fnReloadAjax();
				 });
	});
	
	function getfiter(){
		filterjson= {"monStatus":$("#sclass").val()};
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
								if(data[i].fileType==true){
									addImage(data[i].fileName,data[i].fileName,data[i].id,"GUARD_PRO");
								}else{
									addImage(data[i].fileName,"",data[i].id,"GUARD_PRO");
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
		//重新初始化图片控件
		var aData = oTable.fnGetData(nRow);
		$("input[name='guard.id']").val(gid);
	}
	function afernew(){
		$('#delids').val('');
		$(".imgItem").remove();
		$("#monStatus").val(0);
		$("input[name='guard.id']").val(gid);
	}
	function jssubmit() {
		fomdatesubmit();
	}
	function doman(guid){
		$("#suggest").val("");
		$("#guid").val(guid);
		$('#do-modal').modal('show');
	}
	function domanaction(){
		$('#do-modal').modal('hide');
		$('#loading').show();
		$.post('doman.shtml',{"guid":$("#guid").val(), "suggest":$("#suggest").val()}, function(response){
			var status = response.response.status;
			if (status == 0 || status == 9999) {
				toastr.success("处理问题成功");
				refreshData();
			}else{
				toastr.error('处理问题失败');
			}
			$('#loading').hide();
		}).error(function() {$('#loading').hide();toastr.error('处理问题失败'); });
	}
</script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
<script src='<c:url value="/resources/js/project/list.js" />'></script>
