<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>		
<link href='<c:url value="/resources/assets/jquery-barrating/themes/fontawesome-stars.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet">	
<link href='<c:url value="/resources/assets/image-cropper/cropper.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/image-cropper/main.css" />' rel="stylesheet">	
<script type="text/javascript">
	$("#pictitle").html("我的评论");
	var cid ="${activePid}";
	function goback(){
		var from = "${from}";
		if(from=='map') { //pc端的map
			window.location.href="<c:url value='/water/csite/wlist.html?pid='/>"+cid;
		} else {
			javascript :history.back(-1);
		}
	}
</script>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading">
		项目评论
	</header>
	<div class="panel-body">
		<div class="container-fluid">
			<div class="review row vtitle" style="display:none">
				<div class="col-sm-4 col-md-2">
					项目总评分<br/>
					<span class="totalScore"></span>&nbsp;共<span class="totalComments">0</span>评论
				</div>
				<div class="col-sm-6 col-md-10">
					<div class="row">
						<div class="col-xs-4 col-md-2">项目管理员</div>
						<div class="col-xs-8 col-md-10">
							<span class="star" id="vscore1"></span>&nbsp;&nbsp;<span class="sstar" id="sscore1"></span>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">项目工人</div>
						<div class="col-xs-8 col-md-10">
							<span class="star" id="vscore2"></span>&nbsp;&nbsp;<span class="sstar" id="sscore2"></span>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">安全管理</div>
						<div class="col-xs-8 col-md-10">
							<span class="star" id="vscore3"></span>&nbsp;&nbsp;<span class="sstar" id="sscore3"></span>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">项目质量</div>
						<div class="col-xs-8 col-md-10">
							<span class="star" id="vscore4"></span>&nbsp;&nbsp;<span class="sstar" id="sscore4"></span>
						</div>
					</div>
				</div>
			</div>
			<hr/>
			<form:form role="form" commandName="preview" id="storeform" style="display:none">
			<div class="myreview row vtitle">
				<div class="col-sm-4 col-md-2">
					当前的评价
				</div>
				<div class="col-sm-8 col-md-10">
					<div class="row">
						<div class="col-xs-4 col-md-2">项目管理员</div>
						<div class="col-xs-8 col-md-10">
							<select class="star" name="score1">
							  <option value="1">1</option>
							  <option value="2">2</option>
							  <option value="3">3</option>
							  <option value="4">4</option>
							  <option value="5">5</option>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">项目工人</div>
						<div class="col-xs-8 col-md-10">
							<select class="star" name="score2">
							  <option value="1">1</option>
							  <option value="2">2</option>
							  <option value="3">3</option>
							  <option value="4">4</option>
							  <option value="5">5</option>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">安全管理</div>
						<div class="col-xs-8 col-md-10">
							<select class="star" name="score3">
							  <option value="1">1</option>
							  <option value="2">2</option>
							  <option value="3">3</option>
							  <option value="4">4</option>
							  <option value="5">5</option>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">项目质量</div>
						<div class="col-xs-8 col-md-10">
							<select class="star" name="score4">
							  <option value="1">1</option>
							  <option value="2">2</option>
							  <option value="3">3</option>
							  <option value="4">4</option>
							  <option value="5">5</option>
							</select>
						</div>		
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">评价内容</div>
						<div class="col-xs-8 col-md-10">
							<div class="f-textarea">
								<form:textarea name="comment" path="comment" placeholder="项目管理是否给力？快分享你的感受吧~"/>
								<div class="textarea-ext">还可输入500字</div>
							</div>						  
						</div>		
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">图片上传</div>
						<div class="col-xs-8 col-md-10">
							<div class="row">
		                        
		                         	<div class="col-lg-12 col-sm-12 " id="attach">
		                         		<c:if test="${not empty preview.previewImages}">
											<c:forEach items="${preview.previewImages}" var="img">
												<div class="col-sm-4"><a href="<c:url value="/files/preview/${img.reviewImage } "/>?ftype=PREVIEW"><img style="width:100%" src="${img.imageUrl}"/></a></div>
											</c:forEach>
										</c:if>
										<div class="col-xs-4">
										 <div class="avatar-view">		
						    				<img class="avatar-img" name="imageList" style="max-width:100%"/>
						   				 </div>
										</div>
		                         </div>
		                         
	                    	</div>
						</div>		
					</div>
					<div class="row">
						<div class="col-xs-offset-4 col-md-offset-2 col-xs-8 col-md-10">
							<button type="button" onclick="save()"
							class="btn btn-success">
							保存
							</button>
						</div>		
					</div>
				</div>
			</div>
			 <form:hidden path="id" />
			<input type="hidden" name="project.id" id="projectId" value="${projectId }"/>
			</form:form>
			<hr>
			<div id="myreview" class="myreview row vtitle">
				<div class="text-center" style="background: #5b6e84;color:white;font-size:15px;">
					历史评价
				</div>
				<c:forEach items="${previews}" var="preview"> 
					<div class="col-sm-12">
					<div class="row">
						<div class="col-xs-4 col-md-2">评论时间</div>
						<div class="col-xs-8 col-md-10">
							${preview.auditSection.dateCreated}
						</div>
						<div class="col-xs-4 col-md-2">项目管理员</div>
						<div class="col-xs-8 col-md-10">
							<c:forEach  begin="1" end="${preview.score1}" > 
								<i style="color: #ff606c;" class="fa fa-star"></i>
							</c:forEach>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">项目工人</div>
						<div class="col-xs-8 col-md-10">
							<c:forEach  begin="1" end="${preview.score2}" > 
								<i style="color: #ff606c;" class="fa fa-star"></i>
							</c:forEach>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">安全管理</div>
						<div class="col-xs-8 col-md-10">
							<c:forEach  begin="1" end="${preview.score3}" > 
								<i style="color: #ff606c;" class="fa fa-star"></i>
							</c:forEach>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">项目质量</div>
						<div class="col-xs-8 col-md-10">
							<c:forEach  begin="1" end="${preview.score4}" > 
								<i style="color: #ff606c;" class="fa fa-star"></i>
							</c:forEach>
						</div>		
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">评价内容</div>
						<div class="col-xs-8 col-md-10">
							<div class="f-textarea">
								<textarea name="comment" id="comment" readonly="readonly">${preview.comment}</textarea>
							</div>						  
						</div>		
					</div>
					<div class="row">
						<div class="col-xs-4 col-md-2">图片</div>
						<div class="col-xs-8 col-md-10">
							<div class="row" id="showimg">
								<c:if test="${not empty preview.previewImages}">
									<c:forEach items="${preview.previewImages}" var="img">
										<div class="col-sm-4"><a href="<c:url value="/files/preview/${img.reviewImage } "/>?ftype=PREVIEW"><img style="width:100%" src="${img.imageUrl}"/></a></div>
									</c:forEach>
								</c:if>
	                    	</div>
						</div>		
					</div>
					<div class="row" style="border-bottom: 1px solid #eee;padding-bottom: 10px;">
						<div class="col-xs-4 col-md-2">反馈内容</div>
						<div class="col-xs-8 col-md-10">
							<div class="f-textarea" style="margin-top:5px;">
								<textarea name="feedback" id="feedback" readonly="readonly">${preview.feedback}</textarea>
							</div>						  
						</div>		
					</div>
				</div>
				
				</c:forEach>
			</div>
		</div>
	</div>
</section>
</div>
</div>
<script src='<c:url value="/resources/assets/jquery-barrating/jquery.barrating.min.js" />'></script>
<jsp:include page="/common/cropperModal.jsp" />
<script src='<c:url value="/resources/assets/image-cropper/cropper.js" />'></script>
<script src='<c:url value="/resources/js/project/cropper-moblie.js" />'></script>
<script>
$('#loading').hide();
var projectId = '${projectId}';
	jQuery(document).ready(function() {
		$("#storeform").show();
		initava();
		<c:choose>
		<c:when test="${not empty preview }">
		
		$("#storeform select[name='score1']").val('${preview.score1}');
		$("#storeform select[name='score2']").val('${preview.score2}');
		$("#storeform select[name='score3']").val('${preview.score3}');
		$("#storeform select[name='score4']").val('${preview.score4}');
		$('.myreview .star').barrating({
	        theme: 'fontawesome-stars',
	        readonly: false
	      });
		
		</c:when>
		<c:otherwise>
		$("select.star").val(5);
		$('#storeform .myreview .star').barrating({
	        theme: 'fontawesome-stars',
	        //showValues: true,
	        initialRating: 5,
	        onSelect:function(value, text, event){
	        	var name = $(this)[0].$elem[0]['name']
	        	$("select[name='"+name+"']").val(value);
	        }
	      });

		</c:otherwise>
		</c:choose>
		
		
		//获取项目评分情况
		$.post('getTotal.shtml', {projectId:projectId}, function(response){
			var status = response.response.status;
			if (status == 0 || status == 9999) {
				var total = response.response.total;
				if(total>0) {
					$(".totalComments").html(response.response.total)
					var s1 = Number(response.response.score1).toFixed(1);
					var s2 = Number(response.response.score2).toFixed(1);
					var s3 = Number(response.response.score3).toFixed(1);
					var s4 = Number(response.response.score4).toFixed(1);
					$.each([s1, s2, s3, s4], function(j, item){
						var _item = Math.ceil(item);
						var _s = Math.floor(item);
						for(var i=0; i<_s; i++) {
							$("#vscore"+(j+1)).append('<i class="fa fa-star"></i>');
						}
						if(_item>_s) {
							$("#vscore"+(j+1)).append('<i class="fa fa-star-half"></i>');
						}
						$("#sscore"+(j+1)).html(item+"分");
					});
					var s = s1*0.3+s2*0.1+s3*0.3+s4*0.3;
					$(".totalScore").html(s.toFixed(1)+"分");
					
					$(".review").show();
				} else {
					$(".review").show();
					$(".review").html("暂无评论");
				}
				
			} else if(status == -1) {
				toastr.error(response.response.statusMessage);
			}
		});
		
		//计算文本输入数量
		$(".f-textarea textarea").on("keyup", function(){
			var text1 = $(this).val();  
		    var len; //记录剩余字符串的长度   
		    if (text1.length >= 500) //textarea控件不能用maxlength属性，就通过这样显示输入字符数了   
		    {  
		        $(this).val(text1.substr(0, 500));  
		        len = 0;  
		    } else {  
		        len = 500 - text1.length;  
		    }  
		    var show = "还可输入" + len + "字";  
		    $(".f-textarea .textarea-ext").html(show); 
		});
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
	function save() {
		var url="save.shtml" ;
		var files = $("input[name='fileupload']");
		var imgarry =new Array();
		if(app!=-1){
			if(files!=null && files.length>0){
				for(var i=0;i<files.length;i++){
					imgarry[imgarry.length]=files[i].src;
					files[i].value="";
					files[i].files[0]="";
				}
			}
		}
		
		var data = new FormData($('#storeform')[0]);
		if(app!=-1){
			if(imgarry.length>0){
				for(var i=0;i<imgarry.length;i++){
					var imgData = imgarry[i];
					if(imgData!=undefined&&imgData!=""&&imgData.split(',').length>1) {
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
						for (var j = 0; j < imgData.length; j++) {
						    ia[j] = imgData.charCodeAt(j);
						};
						var blob = new Blob([ia], {type:imagetype});
						data.append("fileupload", blob);
					}
				}
			}
		}
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
				var status = response.response.status;
				if (status == 0 || status == 9999) {
					toastr.success('操作成功！');
					setTimeout(function(){window.location.reload();}, 500);
				} else if(status==-1){
					toastr.error(response.response.statusMessage);
				} else {
					toastr.error('提交失败！');
				}
				$('#loading').hide();
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				alert('error ' + errorThrown);
			}
		});
		return false;
	}
</script>