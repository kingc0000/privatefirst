<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<script>
$("#pictitle").html("地下水项目管理");
</script>

<div class="row" id="edittable">
	<div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                <span id="edittile">项目信息</span>
            </header>
            <div class="panel-body">
         		<jsp:include page="/pages/water/csite/project.jsp" />
                 <c:if test="${hasRight==true }">
                     <div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="wellssae()" class="btn btn-success">提交</button>
						</div>
					</div>
				</c:if>
        </div>
        </section>
    </div>
</div>
<jsp:include page="/common/getPointFromMap.jsp" />
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
<script src='<c:url value="/resources/assets/ckeditor/ckeditor.js" />'></script>
<script>
function goback(){
	javascript :history.back(-1);
}
jQuery(document).ready(function() {
	
	//自动添加经纬度
	$('#address').blur(function(){
		autoSetPoint();
	});
	CKEDITOR.replace(  'summary',
		{
			filebrowserImageBrowseUrl :  '<c:url value="/admin/content/fileBrowser.html"/>',
			filebrowserImageUploadUrl :  '<c:url value="/admin/content/image/upload.shtml"/>'
		});
	
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
	});
	
</script>


