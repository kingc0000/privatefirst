<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>		
<div class="row">
	<div class="col-lg-12 col-sm-12 text-center">
		<div class="alert alert-info">
			同步摄像头，对于重复的摄像头目录不会更改，对于服务器新增目录，会同步增加摄像头数据，摄像头同部门项目的关联，需要在
			<a href='<c:url value="/water/camera/list.html"/>' class="btn btn-danger">摄像头管理列表</a>中维护
		</div>	
		<button class="btn btn-success" onclick="doSynchronize()">同步摄像头</button>
	</div>
</div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
	$(function() {
		$('#pictitle').html('摄像头同步');
	});
	
	
	function doSynchronize(event) {
		var url = _context + "/water/camera/doSynchronize.shtml";
		$('#loading').show();
		$.post(url, {}, function(response){
			$('#loading').hide();
			var status = response.response.status;
			var msg = response.response.statusMessage;
			if (status == 0 || status == 9999) {
				toastr.success("同步成功");
			} else if(status == -4) {
				toastr.error(msg);
			} else {
				toastr.error("系统错误，请联系管理员");
			}
		});
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
