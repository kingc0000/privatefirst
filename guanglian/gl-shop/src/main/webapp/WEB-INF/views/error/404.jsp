<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="Mosaddek">
    <meta name="keyword" content="FlatLab, Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">
    
    <title>404</title>

	<!-- Bootstrap core CSS -->
	<link href='<c:url value="/resources/css/bootstrap.min.css" />'
		rel="stylesheet">
	<link href='<c:url value="/resources/css/bootstrap-reset.css" />' 
		rel="stylesheet">
    <link href='<c:url value="/resources/assets/font-awesome/css/font-awesome.css" />' rel="stylesheet" />
   	<link href='<c:url value="/resources/css/style.css" />' rel="stylesheet" />
	<link href='<c:url value="/resources/css/style-responsive.css" />' rel="stylesheet" />
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
		<!--[if lt IE 9]>
        <script href='<c:url value="/resources/js/html5shiv.js" />'></script>
        <script href='<c:url value="/resources/js/respond.min.js" />'></script>
        <![endif]-->
	<script src='<c:url value="/resources/js/jquery.js" />'></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
</head>

  <body class="body-500">

    <div class="container">

      <section class="error-wrapper">
          <img  src='<c:url value="/resources/img/404.png"/>'>
          <h1>404</h1>
          <p class="page-500">访问的页面不存在 
          <a href="javascript :;" onClick="javascript :history.back(-1);">返回</p>
      </section>

    </div>


  </body>
</html>
