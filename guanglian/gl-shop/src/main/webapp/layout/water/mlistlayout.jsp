<!DOCTYPE html >
<html >
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no,initial-scale=1" >
<meta name="renderer" content="webkit|ie-comp|ie-stand" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="description" content="">
<meta name="author" content="kekeinfo" />
<meta name="keyword" content="kekeinfo" />
<link rel="shortcut icon" href='<c:url value="/resources/img/favicon.ico"/>' type="image/x-icon" />

<title><s:message code="label.backed.title" text="Title"/></title>
<script>
var _context = '${pageContext.request.contextPath}';
</script>
<!-- Bootstrap core CSS -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />' rel="stylesheet" media="all">
<link href='<c:url value="/resources/css/bootstrap-reset.css" />' rel="stylesheet" />
<!--external css-->
<link
	href='<c:url value="/resources/assets/font-awesome/css/font-awesome.css" />' rel="stylesheet" />
<!--toastr-->
<link href='<c:url value="/resources/assets/toastr-master/toastr.css"/>' rel="stylesheet" type="text/css" />

<!-- mask -->
<link href='<c:url value="/resources/css/mask.css"/>' rel="stylesheet" type="text/css" />


<!-- Custom styles for this template -->
<link href='<c:url value="/resources/css/style.css" />' rel="stylesheet" />
<link href='<c:url value="/resources/css/style-responsive.css" />' rel="stylesheet" />
<!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
<!--[if lt IE 9]>
        <script href='<c:url value="/resources/js/html5shiv.js" />'></script>
        <script href='<c:url value="/resources/js/respond.min.js" />'></script>
        <script type="text/javascript">	
				alert('您的IE浏览器版本太低，请升级到高版本，谢谢！');
				window.location.href='<c:url value="/water/nonsupport.html"/>';
				</script>
        <![endif]-->
<script src='<c:url value="/resources/js/jquery.js" />'></script>
<script src='<c:url value="/resources/js/slidebars.min.js" />'></script>
<script src='<c:url value="/resources/js/fastclick.js"/>'></script>
</head>
<script type="text/javascript">
$(function() {
    FastClick.attach(document.body);
});
</script>
<body>
<script>
	$('#loading').show();
</script>
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
	<section id="container" class="full-width">
		<!--header start-->
		<header class="header white-bg text-center">
			<!--logo start-->
			<button type="button" class="navbar-toggle"  style="margin-right:15px" id="camera-fa-bars">
                  <span class="fa fa-bars"></span>
              </button>
			<button type="button" onclick="goback()" class="goback" ><i class="theme-color fa fa-chevron-left"></i>&nbsp;&nbsp;</button>
			<a href='<c:url value="/water/home.html"/>' class="logo hidden-xs"><i class="theme-color fa fa-home"></i>&nbsp;&nbsp;</a>
			<div class="titlecenter" id="pictitle"></div>
			
			<!--logo end-->
			<div class="top-nav hidden-xs pull-right">
				<!--search & user info start-->
				
				<ul class="nav pull-right top-menu">
					
					<!-- user login dropdown start-->
					<li class="dropdown "><a data-toggle="dropdown"
						class="dropdown-toggle" href="#"> 
						<c:choose>
							<c:when test="${not empty requestScope.headimg }">
								<img alt="person" src='<sm:contentImage imageName="${requestScope.headimg}" imageType="LOGO"/>' width="30" height="30">
							</c:when>
							<c:otherwise>
								<img alt="person" src='<c:url value="/resources/img/avatar1_small.jpg"/>' >
							</c:otherwise>
						</c:choose>
						 <span
							class="username"><sec:authentication property="principal.username" /></span> <b class="caret"></b>
					</a>
						<ul class="dropdown-menu extended logout">
							<li class="log-arrow-up"></li>
							<li><a href='<c:url value="/water/users/displayUser.html"/>'><i class=" fa fa-user"></i><s:message code="label.basic.info" text="Basic Information"/></a></li>
							<li><a href='<c:url value="/water/users/password.html"/>'><i class="fa fa-chain"></i><s:message code="label.change.password" text="Change Password"/></a></li>
							<li><a href='<c:url value="/water/users/head.html"/>'><i class="fa fa-list"></i><s:message code="label.set.avatar" text="Set Avatar"/></a></li>
							<li><a href='<c:url value="/water/amessage/list.html"/>' class="hidden-xs"><i class="fa fa-bell-o"></i>消息中心<c:if test="${sessionScope.user_un_read!=0}">
																	<span class="badge bg-important">${sessionScope.user_un_read }</span>
																</c:if></a></li>
							<li><a href="#" onclick="logonout()"><i class="fa fa-key"></i><s:message code="label.logout" text="Log Out"/></a></li>
						</ul></li>
					<!-- user login dropdown end -->
				</ul>
				<!--search & user info end-->
			</div>
			<div class="horizontal-menu navbar-collapse collapse text-left pull-right font-size16">
                  <ul class="nav navbar-nav">
                      <c:forEach items="${requestScope.MONITORMENULIST}" var="menu" varStatus="status">
                      	<sec:authorize access="hasRole('${menu.role}') and fullyAuthenticated">
                      		<c:choose>
                      			<c:when test="${fn:length(menu.menus)>0 }">
                      				<li class="dropdown ">
                      					<a data-toggle="dropdown" data-hover="dropdown" class="dropdown-toggle needsclick <c:if test="${activeMenus[menu.code]!=null}"> active</c:if>" href="#"><i class="fa ${menu.icon }"></i><span>${menu.name}</span><b class=" fa fa-angle-down"></b></a>
                      					<ul class="dropdown-menu">
				                              <c:forEach items="${menu.menus }" var="submenu">
				                              	<li> 
				                              		<sec:authorize access="hasRole('${submenu.role}') and fullyAuthenticated">
														<li>
															<a href='<c:url value="${submenu.url}" />'>${submenu.name}
															<c:if test="${sessionScope.user_report_deal!=0&& submenu.code=='dreport-audit'}">
																<span class="badge bg-important">${sessionScope.user_report_deal }</span>
															</c:if>
															</a>
														</li>
																	<!-- third level menu -->
													</sec:authorize>
				                              	</li>
				                              </c:forEach>
                          				</ul>
                          			</li>
                      			</c:when>
                      			<c:otherwise>
									<li>
										<a class="<c:if test="${activeMenus[menu.code]!=null}">active</c:if>" href='<c:url value="${menu.url}" />'><i class="fa ${menu.icon }"></i>${menu.name}</a></li>
                      			</c:otherwise>
                      		</c:choose>
                      	</sec:authorize>
                      </c:forEach>
                  </ul>

              </div>
		</header>
		<!--header end-->
		<!--main content start-->
		<section id="main-content">
			<section class="wrapper">
				<!--sidebar start-->
				<aside>
					<jsp:include page="/pages/water/mlist/mtree.jsp" />
				</aside>
				<!--sidebar end-->
				<section id="middle-content">
					<tiles:insertAttribute name="body" />
				</section>
			</section>
		</section>
		<!--main content end-->

		<jsp:include page="/layout/water/monitorfooter.jsp" />
	</section>
<!-- Material button -->
    <button class="material-scrolltop" type="button"></button>
    <script src='<c:url value="/resources/js/jquery.cookie.js" />'></script>
    <script type="text/javascript">
	$(document).ready(function() {
	    $('body').materialScrollTop({
	        revealElement: 'header',
	        revealPosition: 'bottom'
	    });
	    $.slidebars();
		jQuery('#sidebar .sub-menu > a').click(function () {
	        var o = ($(this).offset());
	        diff = 250 - o.top;
	        if(diff>0)
	            $("#sidebar").scrollTo("-="+Math.abs(diff),500);
	        else
	            $("#sidebar").scrollTo("+="+Math.abs(diff),500);
	    });
	    $(window).on('load', responsiveView);
	    $(window).on('resize', responsiveView);
	    
	});
	$(window).load(function() {
		$('#loading').hide(); 
	});
	function goback(){
		javascript :history.back(-1);
	}
	//sidebar toggle
	function responsiveView() {
	    var wSize = $(window).width();
	    if (wSize <= 768) {
	        $('#container').addClass('sidebar-close');
	    }

	    if (wSize > 768) {
	        $('#container').removeClass('sidebar-close');
	    }
	}
	
</script>
	<!-- js placed at the end of the document so the pages load faster -->
	<script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
	<script class="include"
		src='<c:url value="/resources/js/jquery.dcjqaccordion.2.7.js" />'></script>
	<script src='<c:url value="/resources/js/jquery.scrollTo.min.js" />'></script>
	<script src='<c:url value="/resources/js/jquery.nicescroll.js" />'></script>
	
	<script src='<c:url value="/resources/js/respond.min.js" />'></script>

	<!--custom switch-->
	<script src='<c:url value="/resources/js/bootstrap-switch.js"/>'></script>
	<!--custom checkbox & radio-->
	
	<!--custom tagsinput-->
	<script src='<c:url value="/resources/js/jquery.tagsinput.js" />'></script>

	<script src='<c:url value="/resources/js/slidebars.min.js" />'></script>
	<!--common script for all pages-->
	<script src='<c:url value="/resources/js/common-scripts.js" />'></script>
	<script src='<c:url value="/resources/js/form-component.js"/>'></script>
	<!--toastr-->
	<script src='<c:url value="/resources/assets/toastr-master/toastr.js"/>'></script>
	 <script src='<c:url value="/resources/js/link-hover.js"/>'></script>
</body>

</html>
