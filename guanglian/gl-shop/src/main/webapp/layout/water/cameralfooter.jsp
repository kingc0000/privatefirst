<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!--footer start-->
<footer class="site-footer hidden-xs navbar-fixed-bottom">
	<div class="text-center ">
		<span style="font-size: 14px;">2016 &copy; 上海<span>广联环境岩土</span>工程股份有限公司
		</span>
	</div>
</footer>
<div class="navbar-fixed-bottom visible-xs footerb"
	style="padding: 7px 0px 3px 0px; ">
	<ul class="footerli" style="display: none">
		<li class="firstl"><a class="needsclick" href='<c:url value="/"/>'><i class=" fa fa-home"></i>首页</a></li>
		<li class="dropup firstl"><a href='#' class="dropdown-toggle needsclick" data-toggle="dropdown" id="infomenu" aria-haspopup="true" aria-expanded="false"><i
				class=" fa fa-user"></i>用户</a>
				<ul class="dropdown-menu bdropmenu" aria-labelledby="infomenu">
				    <li><a class="needsclick" href='<c:url value="/water/users/displayUser.html"/>'><i class=" fa fa-user"></i>基本信息</a></li>
				    <li><a class="needsclick" href='<c:url value="/water/users/password.html"/>'><i class="fa fa-chain"></i>修改密码</a></li>
				     <li><a class="needsclick" href='<c:url value="/water/amessage/list.html"/>'><i class="fa fa-bell-o"></i>消息中心<c:if test="${sessionScope.user_un_read!=0}">
																	<span class="badge bg-important">${sessionScope.user_un_read }</span>
																</c:if></a></li>
				    <li role="separator" class="divider"></li>
				    <li><a href="#" onclick="logonout()"><i class="fa fa-key"></i>退出</a></li>
				    <li class="log-arrow-down"></li>
				  </ul>
  			</li>
		<li class="firstl"><a class="needsclick"  href='<c:url value="/water/camera.html?type=monitor"/>'><i class="fa fa-camera"></i>图像监控</a></li>
		<li class="firstl"><a class="needsclick" href='<c:url value="/water/camera.html?type=view"/>'><i class="fa fa-camera-retro"></i>图片浏览</a></li>
	</ul>
</div>
<!--footer end-->
<script type="text/javascript">
	$(document).ready(function() {
		
		var nbutton=$('.firstl ').length;
		if(nbutton<5){
			var w =100/nbutton;
			w+='%';
			//$('.footerli li').css('width',w);
			$('.footerli li:not(.divider)').css('width',w);
			//$('.bdropmenu').css('min-width',w);
		}
		$('.footerb').css('background','#5b6e84');
		$('.footerli ').show();
	})
</script>