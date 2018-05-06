<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>		
<link href='<c:url value="/resources/assets/image-cropper/cropper.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/image-cropper/main.css" />' rel="stylesheet">

<script type="text/javascript">
	
function removeImage(){
	//$("#store.error").show();
	$('#loading').show();
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/water/users/removeHead.shtml"/>',
	  dataType: 'json',
	  success: function(response){
		  $('#loading').hide();
		  var status =response.response.status;
			if(status==0 || status ==9999) {
				alert('提交成功');
				//设置
				//alert($("#storeLogo").val());
				$("#storeLogo").val('');
				//alert($("#storeLogo").val());
				window.location.reload();
			} else {
				alert('提交失败');
			}	
			
	  },
	  error: function(xhr, textStatus, errorThrown) {
		  $('#loading').hide();
		  alert('error ' + errorThrown);
	  }
	  
	});
}
	
	
	
</script>
		
<section class="panel">
	<header class="panel-heading">修改头像</header>
	<div class="panel-body">
		<div class="col-lg-8 col-sm-12">
			<form:form method="POST" enctype="multipart/form-data" cssClass="form-horizontal" onsubmit="return doSave()">
                <div class="form-group last">
                     <div class="col-lg-12">
                     	 <c:choose>
                     		<c:when test="${headimg==null||heading==''}">	                                   
                                 <div class="avatar-view">
							    	<img class="avatar-img" id="fullImageUrl" style="max-width:100%"/>
							    </div>
                             </c:when>
                             <c:otherwise>
                             	<div class="avatar-view has-preview">
							    	<img class="avatar-img" id="fullImageUrl" style="max-width:100%" src='<sm:contentImage imageName="${headimg}" imageType="LOGO"/>'/>
							    </div>
                             </c:otherwise>
                         </c:choose>
                     </div>
                 </div>   
	
				<div class="form-group">
					<div class="col-lg-2 col-sm-2"></div>
					<div class="col-lg-10">
						<button type="submit" class="btn btn-success">保存</button>
					</div>
				</div>

			</form:form>
		</div>
	</div>
	<jsp:include page="/common/cropperModal.jsp" />
</section>
<script src='<c:url value="/resources/js/project/toastr.js" />'></script>
<script src='<c:url value="/resources/assets/image-cropper/cropper.js" />'></script>
<c:choose>
	<c:when test="${sessionScope.app==null || sessionScope.app==''}">
		<script src='<c:url value="/resources/js/project/cropper-main.js" />'></script>
	</c:when>
	<c:otherwise>
		<script src='<c:url value="/resources/js/project/cropper-moblie.js" />'></script>	
	</c:otherwise>
</c:choose>
<script>
var avatarObj;
$(function(){
	avatarObj = $('#crop-avatar').cropAvatar({
		removeImgUrl: '<c:url value="/water/users/removeHead.shtml"/>',
        aspectRatio: 1,
        avatarView: ".avatar-view",
        avatar: ".avatar-img",
        avatarModal: "#avatar-modal"
	});
	$('#pictitle').html('修改头像');
});

function doSave(){
	$('#loading').show();
	/* $("#storeform").submit();  */
	var url = '<c:url value="/water/users/savehead.shtml"/>';
	var data = new FormData();
	
	var imgData = $(".avatar-img").attr("src");
	if(typeof(imgData)!='undefined'&&imgData!=""&&imgData.split(',').length>1) {
		// dataURL 的格式为 “data:image/png;base64,****”,逗号之前都是一些说明性的文字，我们只需要逗号之后的就行了
		imgData = imgData.split(',')[1];
		imgData = window.atob(imgData);
		var ia = new Uint8Array(imgData.length);
		for (var i = 0; i < imgData.length; i++) {
		    ia[i] = imgData.charCodeAt(i);
		};
		var blob = new Blob([ia], {type:"image/jpeg"});
		data.append("contentImages", blob, "upload.jpg");
	}

	$.ajax({
		type : "POST",
		cache: false, //cache设置为false，上传文件不需要缓存。
		url : url,
		data : data,
		async : true,
		processData: false, //processData设置为false。因为data值是FormData对象，不需要对数据做处理。
		contentType: false, //由<form>表单构造的FormData对象，且已经声明了属性enctype=multipart/form-data，所以这里设置为false
		success: function(response, stat){  
			$('#loading').hide();
			var status = response.response.status;
			if (status == 0 || status == 9999) {
				toastr.success('提交成功');
			} else {
				toastr.error('提交失败');
			}
			$('#edittable').hide();
			window.location.reload();
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			alert('error ' + errorThrown);
		}
	});
	return false;
}
</script>