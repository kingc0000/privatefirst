<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,height=device-height,initial-scale=1,maximum-scale=1,user-scalable=no">     
<meta content="yes" name="apple-mobile-web-app-capable">     
<meta content="black" name="apple-mobile-web-app-status-bar-style">     
<meta content="telephone=no" name="format-detection">
<meta name="renderer" content="webkit|ie-comp|ie-stand" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="description" content="kekeinfo信息科技有限公司，专注于企业服务软件开发和IT服务">
<meta name="author" content="kekeinfo">
<meta name="keyword" content="kekeinfo，软件开发，IT服务">
<title>欢迎访问广联管理系统</title>
<link rel="shortcut icon" href='<c:url value="/resources/img/favicon.ico"/>' type="image/x-icon" />
<!-- Bootstrap core CSS -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/bootstrap-reset.css" />'
	rel="stylesheet">
<!--external css-->
<link
	href='<c:url value="/resources/assets/font-awesome/css/font-awesome.css" />'
	rel="stylesheet" />
<!--toastr-->
<link href='<c:url value="/resources/assets/toastr-master/toastr.css"/>' rel="stylesheet" type="text/css" />
<!--mask -->
<link href='<c:url value="/resources/css/mask.css"/>' rel="stylesheet" type="text/css" />
<!-- Custom styles for this template -->
<link href='<c:url value="/resources/css/style.css" />' rel="stylesheet" />
<link href='<c:url value="/resources/css/style-responsive.css" />'
	rel="stylesheet" />
<style type="text/css">
	@media (max-width: 768px){
		a.logo {
    font-size: 17px; 
    text-align: center;
     margin-top: 15px; 
    display: block;
    float: none;
	}
}
	</style>
<!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
<!--[if lt IE 9]>
        <script href='<c:url value="/resources/js/html5shiv.js" />'></script>
        <script href='<c:url value="/resources/js/respond.min.js" />'></script>
        <![endif]-->
</head>
<script type="text/javascript">
var devicetoken="";
function setDeviceID(did){
	devicetoken=did;
}

</script>
<body class="login-body">
	<div id="loading">
		<div id="loading-center">
			<div id="loading-center-absolute">
				<div class="object" id="object_four"></div>
				<div class="object" id="object_three"></div>
				<div class="object" id="object_two"></div>
				<div class="object" id="object_one"></div>
			</div>
		</div>
	</div>
	<div class="container">
		<!--header start-->
		<header class="header white-bg text-center" >
			<!--logo start-->
			<a href='#' class="logo " >
			上海<span>广联环境岩土</span>工程股份有限公司</a>
			<!--logo end-->
		</header>
		<!--header end-->
		<form method="post" id="logonForm" class="form-signin"
			action='<c:url value="/admin/j_spring_security_check"/>' onsubmit="jssubmit()">
			<c:if test="${not empty param.login_error}">
			<div class="alert alert-block alert-danger fade in" style="margin-bottom:0px;">
	            <strong>用户名或密码不正确!</strong>
	        </div>
	        </c:if>
			<h2 class="form-signin-heading">登录</h2>
			<div class="login-wrap">
				<input type="text" id="j_username" name="j_username"
					class="form-control" placeholder="用户ID" autofocus> <input
					type="password" id="j_password" name="j_password"
					class="form-control" placeholder="密码"> <label
					class="checkbox"> <input type="checkbox" id="remember">
					记住密码 <span class="pull-right"> <a data-toggle="modal"
						href="#myModal"> 忘记密码?</a>

				</span>
				</label>
				<button class="btn btn-lg btn-login btn-block" type="submit">登录</button>

				<a tabindex="0" style="color:#fff;" class="btn btn-theme hidden-xs" role="button" data-toggle="popover" data-trigger="focus" title="风险管控平台">手机APP安装</a>
			</div>
			

			<!-- Modal -->
			<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog"
				tabindex="-1" id="myModal" class="modal fade">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">&times;</button>
							<h4 class="modal-title">忘记密码 ?</h4>
						</div>
						<div class="modal-body">
							<p>请输入你的用户ID重置密码.</p>
							<input type="text" name="username" placeholder='<s:message code="label.generic.username" text="user name"/>'
								autocomplete="off" class="form-control placeholder-no-fix">

						</div>
						<div class="modal-footer">
							<button data-dismiss="modal" class="btn btn-default"
								type="button">取消</button>
							<button class="btn btn-success" type="button" onclick="forgetPassword()"><s:message code="label.submit" text="Submit"/></button>
						</div>
					</div>
				</div>
			</div>
			<!-- modal -->

		</form>

	</div>



	<!-- js placed at the end of the document so the pages load faster -->
	<script src='<c:url value="/resources/js/jquery.js"/>'
		type="text/javascript"></script>
	<script src='<c:url value="/resources/js/bootstrap.min.js" />'
		type="text/javascript"></script>
	<script src='<c:url value="/resources/js/jquery.cookie.js" />'></script>
	<!--toastr-->
	<script src='<c:url value="/resources/assets/toastr-master/toastr.js"/>'></script>
<script type="text/javascript">
$(function(){
	var apptype='';
	var app = navigator.userAgent.indexOf("android");
	if(app==-1){
		app = navigator.userAgent.indexOf("iOS");
		if(app!=-1){
			apptype='iOS';
		}
	}else{
		apptype='android';
		//alert(apptype);
		devicetoken=scanUtils.deviceid();
		//alert(devicetoken);
		var version=scanUtils.version();
		if(version==null || version=="" ||version=='' || version!=1){
			alert("APP已经有新的版本，请及时更新，谢谢！");
		}
	}
	//确认是否提交过
	var isSumbit="${param.login_error}";
	if (isSumbit =="" && $.cookie("brmbUser") == "true") { 
		$('#loading').show();
		if(apptype!=''){
			$("#j_username").val($.cookie("busername")+","+devicetoken+","+apptype); 
		}else{
			$("#j_username").val($.cookie("busername")); 
		}
		$("#remember").prop("checked", true); 
		$("#j_password").val($.cookie("bpassword"));  
		$("#logonForm").submit(); 
	} 
	
	//加载显示app二维码
	if($(window).width()>768) {
		var url = '<c:url value="/resources/img/ercode.png"/>';
		$('[data-toggle="popover"]').popover({
			placement: 'top',
			html: true,
			content: '<img style="width:100%" src="'+url+'"/>'
		})
	}
});
function jssubmit(){
	if ($("#remember").prop("checked")) { 
		var username = $("#j_username").val(); 
		var upassword =$("#j_password").val();
		$.cookie("brmbUser", "true", { path: '/',expires: 7 }); //存储一个带7天期限的cookie 
		$.cookie("busername", username, {path: '/', expires: 7 });
		$.cookie("bpassword", upassword, {path: '/', expires: 7 });
	}else{ 
		$.cookie("brmbUser", "false", {path: '/', expire: -1 }); 
		$.cookie("busername", "", { path: '/',expires: -1 }); 
		$.cookie("bpassword", "", { path: '/',expires: -1 }); 
	} 
	$('#loading').show();
	if(apptype!=''){
		$("#j_username").val($("#j_username").val()+","+devicetoken+","+apptype); 
	}
	
}
function forgetPassword(){
	$('#loading').show();
	//根据用户名找回密码
	$.post('<c:url value="/water/users/resetPassword.shtml"/>', {"usercode": $('input[name=username]').val()})
			.success( function(msg) { 
				$('#loading').hide();
				var status = msg.response.status;
				if(status==-1){
					toastr.error("提交失败");
				}else if(status==-4){
					toastr.error(msg.response.statusMessage);
				}else if(status==0||status==9999){
					toastr.success("提交成功");
					alert("您的修改密码链接已经发往您的邮箱，请查收，谢谢！");
					$('#myModal').modal('hide');
				}
			})
		    .error( function(xhr, textStatus, errorThrown) {
		    	$('#loading').hide();
		    	toastr.error("提交失败");
		    });
}

$("body").on("touchmmove",function(event){//移动端禁用滚动条
	event.preventDefault();
	},false);
</script>
</body>
</html>
