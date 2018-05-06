<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/image-cropper/cropper.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/image-cropper/main.css" />' rel="stylesheet">
<script type="text/javascript">
var stype='${sign.stype}';
if(stype=='0'){
	$("#pictitle").html("到站开始要点")
}else{
	$("#pictitle").html("到站开始销点")
}
var apptype='';
var app = navigator.userAgent.indexOf("android");
if(app==-1){
	app = navigator.userAgent.indexOf("iOS");
	if(app!=-1){
		apptype='iOS';
	}
}else{
	apptype='android';
}
</script>
<div class="row">
	<section class="panel" style="padding:15px;">
		<form:form method="POST" enctype="multipart/form-data" cssClass="form-horizontal" role="form"
					commandName="sign" onsubmit="return doSave()" id="signform">
                <div>
					<label class="col-lg-2 col-sm-2 control-label">项目名称</label> <label class="col-lg-10 col-sm-10 control-label">${sign.pName}</label>
				</div>
				<div >
					<label class="col-lg-2 col-sm-2 control-label">地址</label> <label class="col-lg-10 col-sm-10 control-label">${sign.address}</label>
				</div>
				<div >
					<label class="col-lg-2 col-sm-2 control-label">车站</label> <label class="col-lg-10 col-sm-10 control-label">${sign.station}</label>
				</div>
				<div >
					<label class="col-lg-2 col-sm-2 control-label">签到时间</label> <label class="col-lg-10 col-sm-10 control-label">${sign.shouleBe}</label>
				</div>
				<div >
					<label class="col-lg-2 col-sm-2 control-label ">备注</label>
					<div class="col-lg-10">
						<textarea rows="3" id="memo" class="form-control"   name="memo">
										</textarea>
					</div>
				</div>
				<div class="col-lg-12 col-sm-12 control-label">上传附件（人员交底单、仪器设备清单及当前车站时间照片）</div>
                <div class="row" >
                	<div class="col-lg-12 col-sm-12 " id="attach">
	                	<div class="col-xs-4">
							 <div class="avatar-view">
						    	<img class="avatar-img" name="imageList" style="max-width:100%"/>
						    </div>
						</div>
                	</div>
                </div>
                <br>
                <div class="col-lg-12 col-sm-12 control-label pull-right " style="margin-bottom:50px;">
                	<button type="submit" class="btn btn-success">保存</button>
                </div>
                <form:hidden path="id"/>
                <form:hidden path="signaddress" id="signaddress"/>
			</form:form>
	</section>
</div>	
<jsp:include page="/common/cropperModal.jsp" />
<script src='<c:url value="/resources/assets/image-cropper/cropper.js" />'></script>
<script src='<c:url value="/resources/js/project/cropper-moblie.js" />'></script>
<script>
	$(function() {
		initava();
	});
	function initava(){
		avatarObj = $('#crop-avatar').cropAvatar({
			/* removeImgUrl: '<c:url value="/admin/products/images/remove.html"/>', */
			autoCropArea: 1,
			//aspectRatio: 1,
	        avatarView: ".avatar-view",
	        avatar: ".avatar-img",
	        avatarModal: "#avatar-modal",
	        asyncRemove: false,//是否异步删除图片
	        finishdo:"addimage",
	        deletedo:"removeimage"
		});
	}
	function doSave(){
		var url="save.shtml" ;
		var saddress='';
		if(apptype=="android"){
			saddress=scanUtils.address();
		}
		$('#signaddress').val(saddress);
		var data = new FormData($('#signform')[0]);
		
		var imgData = $(".avatar-img").attr("src");
		$(".avatar-img").each(function(i, item){
			var imgData = $(item).attr("src");
			var imgId = $(item).attr("imgId");
			if(imgData!=undefined&&imgData!=""&&imgData.split(',').length>1&&imgId=="") {
				// dataURL 的格式为 “data:image/png;base64,****”,逗号之前都是一些说明性的文字，我们只需要逗号之后的就行了
				var imagetype = "image/jpeg";
				if (imgData.indexOf("image/png")>0) {
					imagetype = "image/png";
				} else if (imgData.indexOf("image/gif")>0) {
					imagetype = "image/gif";
				}  
				imgData = imgData.split(',')[1];
				imgData = window.atob(imgData);
				
				var ia = new Uint8Array(imgData.length);
				for (var i = 0; i < imgData.length; i++) {
				    ia[i] = imgData.charCodeAt(i);
				};
				var blob = new Blob([ia], {type:imagetype});
				data.append("imageslist", blob);
			}
		});
	
		$('#loading').show();
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
					toastr.success('操作成功！');
				} else {
					toastr.error('提交失败！');
				}
				window.location.href='<c:url value="/water/amessage/app.html?type=guard"/>';
				$('#edittable').hide();
				
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				alert('error ' + errorThrown);
			}
		});
		return false;
	}
	function addimage(){
		var str='<div class="col-xs-4"><div class="avatar-view"><img class="avatar-img" name="imageList" style="max-width:100%"/></div></div>';
		$("#attach").append(str);
	}
	function delimage(obj){
		obj.remove();
	}
</script>