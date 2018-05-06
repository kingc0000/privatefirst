<!DOCTYPE html >
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<head>
<meta name="viewport" content="width=device-width, user-scalable=no ,initial-scale=1">
<meta name="renderer" content="webkit|ie-comp|ie-stand" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="description" content="kekeinfo" />
<meta name="author" content="kekeinfo" />
<meta name="keyword" content="kekeinfo" />
<meta name="renderer" content="webkit" /> 
<link rel="shortcut icon" href='<c:url value="/resources/img/favicon.ico"/>' type="image/x-icon" />

<title><s:message code="label.backed.title" text="Title"/></title>

<!-- Bootstrap core CSS -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />' rel="stylesheet" media="all"/>
<link href='<c:url value="/resources/css/bootstrap-reset.css" />' rel="stylesheet"/>
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
<script  src='<c:url value="/resources/js/jquery.js" />'></script>
<!--right slidebar-->
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
	var _context = '${pageContext.request.contextPath}';

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
              <button type="button" onclick="goback()" class="goback" ><i class="theme-color fa fa-chevron-left"></i>&nbsp;&nbsp;</button>
              <c:choose>
              	<c:when test="${sessionScope.ADMIN_USER!=null &&  sessionScope.ADMIN_USER.uAgent!=null}">
              		<a href='<c:url value="/water/csite/phonefrom/chome.html"/>' class="logo pull-right" ><i class="theme-color fa fa-home"></i>&nbsp;&nbsp;</a>
              	</c:when>
              	<c:otherwise>
              		<a href='<c:url value="/water/csite/pcfrom/chome.html"/>' class="logo pull-right" ><i class="theme-color fa fa-home"></i>&nbsp;&nbsp;</a>
              	</c:otherwise>
              </c:choose>
			<!--logo start-->
			<div class="titlecenter" id="pictitle"></div>
			
			
		</header>
		<!--header end-->
		<!--main content start-->
		<section id="main-content">
			<section class="wrapper">
				<section >
					<tiles:insertAttribute name="body" />
				</section>
			</section>
		</section>
		<!--main content end-->

		<jsp:include page="/layout/water/wellfooter.jsp" />
	</section>
	<script src='<c:url value="/resources/js/jquery.cookie.js" />'></script>
<!-- Material button -->
    <button class="material-scrolltop" type="button"></button>
    <script type="text/javascript">
	$(document).ready(function() {
	    $('body').materialScrollTop({
	        revealElement: 'header',
	        revealPosition: 'bottom'
	    });
	    
	});
	$(window).load(function() {
		if(typeof(mapfine) == "undefined"){
			$('#loading').hide(); 
		}
		
	});
	
	
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

	
	<!--common script for all pages-->
	<script src='<c:url value="/resources/js/common-scripts.js" />'></script>
	<script src='<c:url value="/resources/js/form-component.js"/>'></script>
	<!--toastr-->
	<script src='<c:url value="/resources/assets/toastr-master/toastr.js"/>'></script>
	 <script src='<c:url value="/resources/js/link-hover.js"/>'></script>
</body>

</html>
