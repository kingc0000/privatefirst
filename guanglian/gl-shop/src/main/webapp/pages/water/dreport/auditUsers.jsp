<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page pageEncoding="UTF-8"%>

<div class="modal fade " id="groupmodel" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title">请选择审核人</h4>
            </div>
            <div class="modal-body row" id="modal-body">
            	<div class="col-md-12">
            	<div class="form-group last">
                	<div class="col-sm-6 col-sm-offset-3" id="resetselect">
                   	</div>
				</div>
               </div>
               <div class="col-md-12">
               	<label class="control-label col-sm-offset-3 col-sm-9">
	            	<br/>对于 windows：按住 Ctrl 按钮来选择多个选项；<br/>
	            	对于 Mac：按住 command 按钮来选择多个选项
            	</label>
               </div>
               
            </div>
            <div class="modal-footer">
            	<button data-dismiss="modal" class="btn btn-success" type="button" id="sendBtn">送审</button>
                <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            </div>
        </div>
    </div>
</div>
<script>
	/**
	* 调用送审modal内容
	* @param rid 报告id
	* @param note 审核意见
	**/
	function selectAudit(rid, note) {
		$('#loading').show();
		//
		$.ajax({
			type : "POST",
			url : "groups.shtml",
			data : {"rid":rid},
			dataType: 'json',
			success : function(response) {
				$('#loading').hide();
				if(response !=null){
					if(response.status == 0||response.status == 9999) {
						//ajax获取审核人员集合
						showSelectAuditModal(response, rid, note);
					} else {
						toastr.error(response.message);
					}
				}else{
					toastr.error('获取审核人员信息失败');
				}
				
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				toastr.error('获取审核人员信息失败');
			}
		});
	}
	/**
	* 显示select多选控件
	**/
	function showSelectAuditModal(response, rid, note) {
		$('#groupMemo').html(response.memo);
		$('#resetselect').html(' <select name="users" multiple="true" id="my_multi_select3" size="8" style="width:100%"></select>');
		//$("#my_multi_select3").empty();
		$(response.froms).each(function(i,p){
			$('#my_multi_select3').append('<option value="'+p.id+'">'+p.name+'-'+p.code+'</option>');
		});
		$("#sendBtn").off("click").on("click", function(event){
			sendAudit(rid, note, event);
		});
		$('#groupmodel').modal('show');
	}
	/**
	* 提交送审
	*/
	function sendAudit(rid, note, event) {
		var uids = $("#my_multi_select3").val(); 
		if (uids==null || uids=='') {
			alert("请选择审批人员");
			event.stopPropagation();
			return false;
		}
		$.post("sendAudit.shtml", {"rid":rid,"uids": ""+uids+"", "note":note}, function(response){
			var status = response.response.status;
			if (status == 0 || status == 9999) {
				toastr.success("操作成功");
				refreshData();
			} else {
				toastr.error(response.response.statusMessage);
			}
			$("#edittable").hide();
			$('#loading').hide();
		});
	}
</script>
