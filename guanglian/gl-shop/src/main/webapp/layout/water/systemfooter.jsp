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
	style="padding: 7px 0px 3px 0px;">
	<ul class="footerli">
		<li><a href='<c:url value="/"/>'><i class=" fa fa-home"></i>首页</a></li>
		<li class="dropup"><a href='#' class="dropdown-toggle needsclick" data-toggle="dropdown" id="infomenu" aria-haspopup="true" aria-expanded="false"><i
				class=" fa fa-user"></i>用户</a>
				<ul class="dropdown-menu bdropmenu" aria-labelledby="infomenu">
				    <li><a class="needsclick" href='<c:url value="/water/users/displayUser.html"/>'><i class=" fa fa-user"></i>基本信息</a></li>
				    <li><a  class="needsclick" href='<c:url value="/water/users/password.html"/>'><i class="fa fa-chain"></i>修改密码</a></li>
				     <li><a class="needsclick" href='<c:url value="/water/amessage/list.html"/>'><i class="fa fa-bell-o"></i>消息中心<c:if test="${sessionScope.user_un_read!=0}">
																	<span class="badge bg-important">${sessionScope.user_un_read }</span>
																</c:if></a></li>
				    <sec:authorize access="hasRole('USER') and fullyAuthenticated">
				    	<li role="separator" class="divider"></li>
				    	<li><a class="needsclick" href="<c:url value="/water/users/list.html"/>"><i class="fa fa-group"></i>用户列表</a></li>
				    </sec:authorize>
				    <li class="log-arrow-down"></li>
				  </ul>
  			</li>
		<sec:authorize access="hasRole('VIEW-PROJECT') and fullyAuthenticated">
			<li class="dropup"><a href='#' class="dropdown-toggle needsclick" data-toggle="dropdown" id="infomenu" aria-haspopup="true" aria-expanded="false"><i
				class=" fa fa-anchor"></i>部门管理</a>
				<ul class="dropdown-menu bdropmenu" aria-labelledby="infomenu">
				    <li><a class="needsclick" href='<c:url value="/water/department/list.html"/>'><i class=" fa fa-bars"></i>部门列表</a></li>
				    <li><a class="needsclick" href='<c:url value="/water/project/list.html"/>'><i class="fa fa-list-ol"></i>项目列表</a></li>
				    <li class="log-arrow-down"></li>
				  </ul>
  			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
			<li class="dropup"><a href='#' class="dropdown-toggle needsclick" data-toggle="dropdown" id="infomenu" aria-haspopup="true" aria-expanded="false"><i
				class=" fa fa-cog"></i>系统管理</a>
				<ul class="dropdown-menu bdropmenu" aria-labelledby="infomenu">
				    <li><a class="needsclick" href='<c:url value="/water/configuration/logo.html"/>'><i class=" fa fa-puzzle-piece"></i>公司LOGO</a></li>
				    <li><a class="needsclick" href='<c:url value="/water/configuration/email.html"/>'><i class="fa fa-envelope-o"></i>邮箱配置</a></li>
				    <li><a  class="needsclick" href='<c:url value="/water/auditconfig/list.html"/>'><i class="fa fa fa-gavel"></i>送审规则</a></li>
				    <li role="separator" class="divider"></li>
				    <li><a class="needsclick" href='<c:url value="/water/camera/list.html"/>'><i class="fa fa-camera"></i>摄像头管理</a></li>
				    <li><a class="needsclick" href='<c:url value="/water/camera/init.html"/>'><i class="fa fa-camera-retro"></i>摄像头同步</a></li>
				    <sec:authorize access="hasRole('SUPERADMIN') and fullyAuthenticated">
				    	<li role="separator" class="divider"></li>
				    	<li><a class="needsclick" href='<c:url value="/water/basedata/list.html"/>'><i class="fa fa-asterisk"></i>基础数据</a></li>
				   		 <li><a class="needsclick" href='<c:url value="/water/permission/list.html"/>'><i class="fa fa-legal"></i>权限管理</a></li>
				    </sec:authorize>
				    <li class="log-arrow-down"></li>
				  </ul>
  			</li>
		</sec:authorize>
		<li><a href="#" onclick="logonout()"><i class="fa fa-key"></i>退出</a></li>
		
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
<!--footer end-->
