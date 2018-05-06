<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<!--footer start-->
<footer class="site-footer hidden-xs navbar-fixed-bottom">
	<div class="text-center ">
		<span style="font-size: 14px;">2016 &copy; 上海<span>广联环境岩土</span>工程股份有限公司
		</span>
	</div>
</footer>
<div class="navbar-fixed-bottom visible-xs footerb"
	style="padding: 7px 0px 3px 0px;">
	<ul class="footerli">
		<li class="firstls"><a href='<c:url value="/"/>'><i class=" fa fa-home"></i>首页</a></li>
		<li class="firstls"><a href='<c:url value="/water/amessage/app.html?type=guard"/>'><i class="fa fa-bell-o"></i>消息中心<c:if test="${sessionScope.user_un_read!=0}">
					<span class="badge bg-important">${sessionScope.user_un_read }</span>
			</c:if></a></li>
		<li class="firstls"><a class="needsclick" href='<c:url value="/water/guard.html"/>'><i class=" fa fa-sitemap"></i>项目</a></li>
		<li class="dropup needsclick firstls"><a href='#' class="dropdown-toggle needsclick" data-toggle="dropdown"  aria-haspopup="true" aria-expanded="false"><i
				class=" fa fa-camera"></i>要点||销点</a>
			<ul class="dropdown-menu bdropmenu">
			    <li><a class="needsclick" href='<c:url value="/water/gjob/signin.html"/>'><i class=" fa fa-sign-in"></i>要点||销点</a></li>
			    <li><a class="needsclick" href='<c:url value="/water/sign/slist.html?utype=utype"/>'><i class="fa fa-sign-out"></i>签到记录</a></li>
			    <li class="log-arrow-down"></li>
			 </ul>
		</li>
		<li class="firstls"><a class="needsclick" href="#" onclick="logonout()"><i class="fa  fa-sign-out"></i>退出</a></li>
		
	</ul>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		
		var nbutton=$('.firstls ').length;
		if(nbutton<5){
			var w =100/nbutton;
			w+='%';
			$('.footerli li').css('width',w);
			//$('.bdropmenu').css('min-width',w);
		}
		$('.footerb').css('background','#5b6e84');
		$('.footerli ').show();
	})
</script>
<!--footer end-->
