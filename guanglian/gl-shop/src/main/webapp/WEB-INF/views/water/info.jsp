<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no,initial-scale=1">
<meta name="renderer" content="webkit|ie-comp|ie-stand" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="description" content=""/>
<meta name="author" content="kekeinfo">
<meta name="keyword" content="kekeinfo">
<meta name="renderer" content="webkit" /> 
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="shortcut icon" href='<c:url value="/resources/img/favicon.ico"/>' type="image/x-icon" />

<title>上海广联环境岩土</title>
<!-- Bootstrap core CSS -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/bootstrap-reset.css" />'
	rel="stylesheet">
<!--external css-->
<link
	href='<c:url value="/resources/assets/font-awesome/css/font-awesome.css" />' rel="stylesheet" />



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
、
</head>

<script>
var _context = '${pageContext.request.contextPath}';
</script>


	<body>

		<div class="navbar-fixed-top text-center" style="background: #5b6e84;color: #fff;padding: 10px 10px;" >
${project.name }
		</div>
		<div class="row" style="padding-top:20px">
			<div class="col-xs-12 col-gl-12">
				<section class="panel">
      					
      			<div class="list-group">
      				<span class="list-group-item " >项目当前状态:
	         			<c:choose>
	         				<c:when test="${csite.status==0}">成井</c:when>
	         				<c:when test="${csite.status==1}">降压运行</c:when>
	         				<c:when test="${csite.status==2}">疏干运行</c:when>
	         				<c:otherwise>结束</c:otherwise>
	         			</c:choose>
	         		</span>
      				<span class="list-group-item " >井类型:${welltype }</span>
      				<span class="list-group-item " >井号:${wellname }</span>
	         		${detail}
	         		<c:if test="${not empty images}">
	         			<span class="list-group-item ">井图纸</span>
	         			<c:forEach items="${images}" var="img" >
	         			<span class="list-group-item ">
	         				<img width="100%"  src="${img}">
	         			</span>
	         			</c:forEach>
	         		</c:if>
	         		<span class="list-group-item " id="conclud"></span>
	         		<span class="list-group-item " >项目负责人:${project.projectOwner }</span>
	         		<span class="list-group-item " >联系电话:${project.phone }</span>
      			</div>
      
  				</section>
  
		</div>
		</div>
		<footer class=" navbar-fixed-bottom" style="background: #5b6e84;color: #fff;padding: 10px 0;">
			<div class="text-center">
				<span style="font-size:14px;">2016 © 上海<span>广联环境岩土</span>工程股份有限公司</span>
			</div>
		</footer>
	</body>
	<script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
	<script class="include"
		src='<c:url value="/resources/js/jquery.dcjqaccordion.2.7.js" />'></script>
	<script src='<c:url value="/resources/js/jquery.scrollTo.min.js" />'></script>
	<script src='<c:url value="/resources/js/jquery.nicescroll.js" />'></script>
	
	<script src='<c:url value="/resources/js/respond.min.js" />'></script>

	
	 <script src='<c:url value="/resources/js/link-hover.js"/>'></script>
	<script>
	 var pid='${csite.id}';
	 jQuery(document).ready(function() {
			getConclusion();
			 getDrawing();
		
	 });
	 function getConclusion(){
			$.post(_context+"/water/csite/conclusion.shtml?pid=" + pid, function(response) {
				var status = response.status;
				if (status == 0) {
					$("#conclud").html(response.conclusion);
				} else{
					$("#conclud").html("每日信息：无");
				}
			});
		}
	 </script>
	
	
</html>
