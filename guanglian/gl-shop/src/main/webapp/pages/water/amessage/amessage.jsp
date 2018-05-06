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
				消息列表
			</header>
			<div class="panel-body">
				<div class="col-lg-12 col-sm-12">
					<section class="adv-table " id="no-more-tables">
						<table class="display table table-bordered table-striped"
							id="slist">
							<thead>
								<tr>
									<th><input class="allCheckbox" type="checkbox" /></th>
									<th>标题</th>
									<th>类别</th>
									<th>状态</th>
									<th>时间</th>
								</tr>
							</thead>
						</table>
					</section>
				</div>
			</div>
		</section>
	</div>
</div>
<div class="modal fade" id="message-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
              <button class="close" data-dismiss="modal" type="button">&times;</button>
              <h4 class="modal-title" >消息详情</h4>
            </div>
            <div class="modal-body" id=mdetail>
              
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
	$(function() {
$('#slist').dataTable({
												"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : false,
							"bSortClasses" : false,
							"sDom" : "lf<'#read_btn'><'#filter_btn'><'#type_btn'>rtip",
							"sAjaxSource" : "server_processing.shtml",
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							},{
								"mData" : "message.title"
							},{
								"mData" : "message.typename"
							},{
								"mData" : "statu"
							}, {
								"mData" : "message.dateCreated"
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
										"aTargets" : [ 1 ],
										"mRender" : function(data, type, full) {
											if(data!=null && data.length>17){
												data=data.substring(0,14)+"...";
											}
											var str ='<a class="edit hasfn" href="javascript:;" >'+data+'</a>'
											return str;
										}
									},
									{
										"aTargets" : [ 3 ],
										"mRender" : function(data, type, full) {
											var s="未读";
											if(data==1){
												s="已读"
											}
											return s;
										}
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
				'<option value ="0">未阅读</option><option value ="1">已阅读</option>'+
				'<option value ="" selected = "selected">阅读状态</option></select></div>';
				 $("#filter_btn").html(dseelct);
				 $('#sclass').change(function(){
					 getfiter();
					 oTable.fnReloadAjax();
				 });
				 $("#read_btn").html('<button type="button" class="btn "><i class="fa fa-trash-o" style="color:red"></i>设置为已读</button>');
				
				$("#read_btn").addClass("dt_btn pull-right");
				$("#read_btn").on('click',function(){
					readdata("","flag");
				});
				var str='<div style="padding: 15px 5px 15px 0;" class="pull-right"><select id="stype" class="form-control">';
				<c:forEach items="${mtypes }" var="mt">
				str+='<option value ="${mt.mtype}">${mt.name}</option>';
			     </c:forEach>
			     str+='<option value ="" selected = "selected">消息类别</option></select></div>';
			     $("#type_btn").html(str);
				 $('#stype').change(function(){
					 getfiter();
					 oTable.fnReloadAjax();
				 });
	});
	function jssubmit() {
		$("#storeform").submit();
	}
	function getfiter(){
		filterjson= {"statu":$("#sclass").val(),"mtype":$("#stype").val()};
	}
	function dolistafterfn(oTable, nRow){
		var aData = oTable.fnGetData(nRow);
		$("#mdetail").html(aData.message.message);
		$("#message-modal").modal('show');
		if(aData.statu==0){
			readdata(aData.id);
		}
	}
	
	function readdata(sid,flag) {
		if(flag!=null){
			if (typeof(sid)=='undefined' || sid=="") {
				var ids = new Array();
				$('.sidCheckbox:checked').each(function(i,o){
					var nRow = $(o).parents('tr')[0];
					var aData = oTable.fnGetData(nRow);
					ids[i]=$(o).val();
				});
				if (ids.length<1) {
					alert("请至少选择一条消息");
					return;
				}
				sid=ids;
			}
			var ptitle='您确定要将这些消息设置为已读吗';
			
			var rdel = confirm(ptitle);
			if (rdel == false)
				return;
		}
		
		var url = "readed.shtml";
		if(flag!=null){
			$('#loading').show();
		}
		$.ajax({
			type : "POST",
			url : url,
			data : "listId=" + sid,
			success : function(result) {
				$('#loading').hide();
				if(result!=null&&result.length>0) {
					$(result).each(function(i, o){
						var status = o.status;
						var index = "["+(i+1)+"]:&nbsp;&nbsp;";
						var msg = o.statusMessage;
						if(status==0||status==9999) {
							refreshData();
							if(flag!=null){
								if(msg!=null&&msg!=''){
									toastr.success(index+msg);							
								} else {
									toastr.success(index+'操作成功！');
								}
							}
						}else{
							if(flag!=null){
								toastr.error('提交失败！');
							}
						} 
					});
				} else if(result!=null){
					var status = result.response.status;
					var msg = result.response.statusMessage;
					if(status==0||status==9999) {
						refreshData();
						if(flag!=null){
							if(msg!=null&&msg!=''){
								toastr.success(msg);							
							} else {
								toastr.success('操作成功！');
							}
						}
					} else {
						if(flag!=null){
							if(msg!=null&&msg!=''){
								toastr.error(msg);							
							} else {
								toastr.error('提交失败！');
							}
						}
					}
				}
			},
			
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				toastr.error('提交失败！');	
				
			}
		});
		$('#edittable').hide();
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
