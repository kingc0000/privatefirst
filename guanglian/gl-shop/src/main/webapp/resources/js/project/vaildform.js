$(function() {
	var form=$("#storeform");
	if(form.length>0){
		validator = $("#storeform").validate({
			invalidHandler : function() {
				return false;
			},
			//指明错误放置的位置
			errorPlacement : function errorPlacement(error, element) {
				element.after(error);
			},
			submitHandler : function() {
				// 表单的处理
				//alert(JSON.stringify($('#storeform').serializeJson()));
				//蒙版处理
				$('#loading').show();
				var url = "save.shtml";
				$.ajax({
					type : "POST",
					url : url,
					data : $('#storeform').serializeJson(),
					async : true,
					traditional : true,
					success : function(response) {

						var status = response.response.status;
						if (status == 0 || status == 9999) {
							toastr.success('操作成功！');
						} else if (status == 7) {
							toastr.info(response.response.statusMessage);
						} else if (status == 11) {
							toastr.success('操作成功！');
							afterSave(response); //自定义外部接口
						} else if (status == -4) {
							toastr.error(response.response.statusMessage);
						} else {
							toastr.error('提交失败！');
						}
						$('#edittable').hide();
						refreshData();
						$('#loading').hide();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert('error ' + errorThrown);
						$('#loading').hide();
					}
				});
				return false;// 阻止form提交
			}
		});
	}
});
