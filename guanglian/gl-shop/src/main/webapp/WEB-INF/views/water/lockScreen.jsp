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


<title>屏幕锁定</title>

<!-- Bootstrap core CSS -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/bootstrap-reset.css" />'
	rel="stylesheet">
<!--external css-->
<link
	href='<c:url value="/resources/assets/font-awesome/css/font-awesome.css" />'
	rel="stylesheet" />



<!-- Custom styles for this template -->
<link href='<c:url value="/resources/css/style.css" />' rel="stylesheet" />
<link href='<c:url value="/resources/css/style-responsive.css" />'
	rel="stylesheet" />




<!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
<!--[if lt IE 9]>
        <script href='<c:url value="/resources/js/html5shiv.js" />'></script>
        <script href='<c:url value="/resources/js/respond.min.js" />'></script>
        <![endif]-->

</head>

<body class="lock-screen" onload="startTime()">

    <div class="lock-wrapper">

        <div id="time"></div>


        <div class="lock-box text-center">
            <c:choose>
							<c:when test="${not empty requestScope.headimg }">
								<img alt="person" src='<sm:contentImage imageName="${requestScope.headimg}" imageType="LOGO"/>' width="47">
							</c:when>
							<c:otherwise>
								<img alt="person" src='<c:url value="/resources/img/avatar1_small.jpg"/>' >
							</c:otherwise>
						</c:choose>
            <h1><sec:authentication property="principal.username" /></h1>
            <span class="locked">屏幕锁定了！</span>
            <form method="post" role="form"  id="logonForm" class="form-inline" action='<c:url value="/admin/j_spring_security_check"/>'>
                <div class="form-group col-lg-12">
                    <input type="hidden" id="j_username" name="j_username" class="form-control" value="<sec:authentication property="principal.username" />">
                    <input id="j_password" name="j_password" type="password" placeholder="请输入密码" class="form-control lock-input">
                    <button class="btn btn-lock" type="submit">
                        <i class="fa fa-arrow-right"></i>
                    </button>
                </div>
                <br>
                <br>
            </form>
        </div>
    </div>
    <script>
        function startTime()
        {
            var today=new Date();
            var h=today.getHours();
            var m=today.getMinutes();
            var s=today.getSeconds();
            // add a zero in front of numbers<10
            m=checkTime(m);
            s=checkTime(s);
            document.getElementById('time').innerHTML=h+":"+m+":"+s;
            t=setTimeout(function(){startTime()},500);
        }

        function checkTime(i)
        {
            if (i<10)
            {
                i="0" + i;
            }
            return i;
        }
    </script>
</body>
</html>
