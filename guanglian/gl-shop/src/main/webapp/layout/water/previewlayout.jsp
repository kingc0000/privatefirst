<!DOCTYPE html>
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
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta name="description" content="">
<meta name="author" content="kekeinfo">
<meta name="keyword" content="kekeinfo">
<link rel="shortcut icon" href='<c:url value="/resources/img/favicon.ico"/>' type="image/x-icon" />

<title><s:message code="label.backed.title" text="Title"/></title>
<script>
var _context = '${pageContext.request.contextPath}';
</script>
<!-- Bootstrap core CSS -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/bootstrap-reset.css" />'
	rel="stylesheet">
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
		 <button type="button" onclick="goback()" class="goback visible-xs" ><i class="theme-color fa fa-chevron-left"></i>&nbsp;&nbsp;</button>
			
			<div class="titlecenter" id="pictitle"></div>
		</header>
		<!--header end-->
		<!--main content start-->
		<section id="main-content">
			<section class="wrapper">
				<tiles:insertAttribute name="body" />
			</section>
		</section>
		<!--main content end-->

		<jsp:include page="/layout/water/footer.jsp" />
	</section>
<!-- Material button -->
    <button class="material-scrolltop" type="button"></button>
    <script type="text/javascript">
	$(document).ready(function() {
	    $('body').materialScrollTop({
	        revealElement: 'header',
	        revealPosition: 'bottom'
	    });
	    //$('#loading').hide(); 
	});
	$(window).load(function() {
		$('#loading').hide(); 
	});
	function goback(){
    	javascript :history.back(-1);
    }
	function logonout(){
		var rdel = confirm('<s:message code="message.logout.confirm" text="Confirm"/>');
		if (rdel==false){
			return ;
		}else {
			$.cookie("brmbUser", "false", { expire: -1 }); 
			$.cookie("busername", "", { expires: -1 }); 
			$.cookie("bpassword", "", { expires: -1 }); 
			window.location.href='<c:url value="/water/j_spring_security_logout"/>';
		}
	}
</script>
	<!-- js placed at the end of the document so the pages load faster -->
	<script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
	
	<script src='<c:url value="/resources/assets/toastr-master/toastr.js"/>'></script>
	 <script src='<c:url value="/resources/js/link-hover.js"/>'></script>
	 <script src='<c:url value="/resources/js/jquery.cookie.js" />'></script>
</body>

</html>
