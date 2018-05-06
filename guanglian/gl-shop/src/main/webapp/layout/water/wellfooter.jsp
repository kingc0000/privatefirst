<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!--footer start-->
<script type="text/javascript">
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
<footer class="site-footer hidden-xs navbar-fixed-bottom">
	<div class="text-center ">
		<span style="font-size: 14px;">2016 &copy; 上海<span>广联环境岩土</span>工程股份有限公司
		</span>
	</div>
</footer>
<div id="footerb" class="navbar-fixed-bottom visible-xs footerb"
	style="padding: 7px 0px 3px 0px; ">
	<ul id="footerli" class="footerli" style="display: none">
		<li class="firstl"><a href='<c:url value="/"/>'><i class=" fa fa-home"></i>首页</a></li>
		
		<sec:authorize access="hasRole('EDIT-PROJECT') and fullyAuthenticated">
			<li class="dropup firstl"><a href='#' class="dropdown-toggle needsclick" data-toggle="dropdown"  aria-haspopup="true" aria-expanded="false"><i
				class=" fa fa-archive"></i>编制系统</a>
				<ul class="dropdown-menu bdropmenu" aria-labelledby="infomenu">
				    <li><a class="needsclick" href='<c:url value="/water/dreport/list.html"/>'><i class=" fa fa-list-ol"></i>报告列表</a></li>
				    <li><a class="needsclick" href='<c:url value="/water/dreport/listApprove.html"/>'><i class="fa fa-gavel"></i>报告审批</a></li>
				    <li class="log-arrow-down"></li>
				  </ul>
  			</li>
		</sec:authorize>
		<li class="dropup firstl"><a href='#' class="dropdown-toggle needsclick" data-toggle="dropdown"  aria-haspopup="true" aria-expanded="false"><i class=" fa fa-archive"></i>工具</a>
			<ul class="dropdown-menu bdropmenu" aria-labelledby="infomenu">
			    <li><a class="needsclick" href='<c:url value="/water/ddoc/list.html"/>'><i class="fa fa-book"></i>文献</a></li>
			    <li><a href='<c:url value="/water/amessage/app.html?type=water"/>'><i class="fa fa-bell-o"></i>消息中心<c:if test="${sessionScope.user_un_read!=0}">
				<span class="badge bg-important">${sessionScope.user_un_read }</span>
			</c:if></a></li>
			    <li role="separator" class="divider"></li>
			    <li><a class="needsclick" href='<c:url value="/water/computer/confined.html"/>'><i class="fa fa-superscript"></i>抗突涌计算</a></li>
			    <sec:authorize access="hasRole('EDIT-PROJECT') and fullyAuthenticated">
			    <li><a class="needsclick" href='<c:url value="/water/computer/aquifer.html"/>'><i class="fa fa-random"></i>涌水量计算</a></li>
			    <li><a class="needsclick" href='<c:url value="/water/computer/recharge.html"/>'><i class="fa fa-retweet"></i>回灌井计算</a></li>
			    <li role="separator" class="divider"></li>
			    <li><a class="needsclick" href='<c:url value="/water/statistics/statistics.html"/>'><i class="fa fa-rss"></i>项目统计</a></li>
			    <li><a class="needsclick" href='<c:url value="/water/preview/list.html"/>'><i class="fa fa-comments"></i>评论管理</a></li>
			    <li><a class="needsclick" href='<c:url value="/water/csite/list.html"/>'><i class="fa fa-tasks"></i>项目管理</a></li>
			    <li role="separator" class="divider"></li>
			    <li><a href="#" onclick="logonout()"><i class="fa fa-key"></i>退出</a></li>
			    <li class="log-arrow-down"></li>
			    </sec:authorize>
			</ul>
 		</li>
 		<li class="firstl"><a class="needsclick" href='#' onclick="sacn()"><i class="fa fa-qrcode"></i>扫描</a></li>
 		<li class="firstl "><a  class="needsclick" href='<c:url value="/water/csite/wlist.html"/>'><i
				class="fa fa-globe"></i>监控地图</a></li>	
	</ul>
</div>
<!--footer end-->
<script type="text/javascript">
	$(document).ready(function() {
		
		var nbutton=$('.firstl ').length;
		if(nbutton<5){
			var w =100/nbutton;
			w+='%';
			$('.footerli li:not(.divider)').css('width',w);
			//$('.bdropmenu').css('min-width',w);
		}
		$('.footerb').css('background','#5b6e84');
		$('.footerli ').show();
	})
	function sacn(){
		if(apptype=="android"){
			scanUtils.scan();
		}else if(apptype=="iOS"){
			window.webkit.messageHandlers.sacner.postMessage(null);
		}
	}
</script>