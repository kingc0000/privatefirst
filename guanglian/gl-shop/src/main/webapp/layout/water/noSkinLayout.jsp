<!DOCTYPE html>
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
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="description" content="">
<meta name="author" content="biosite">
<meta name="keyword" content="website">
<meta name="renderer" content="webkit" /> 
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link href='<c:url value="/resources/css/bootstrap.min.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/bootstrap-reset.css" />'
	rel="stylesheet">
    			<script src='<c:url value="/resources/js/jquery.js" />'></script>
    			
  
 	
 	
 	</head>
<script>
	var _context = '${pageContext.request.contextPath}';
</script> 
 	<body class="body">

     <p>&nbsp;</p>

<div class="sm">

	<!-- <div class="container">-->
	<div class="span6">
		<!--<div class="row">-->
				<tiles:insertAttribute name="body"/>
		<!--</div>-->
  
  
	</div> <!-- /container --> 
	

  
</div>
  <script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
    
 
 	</body>
 
 </html>
 
