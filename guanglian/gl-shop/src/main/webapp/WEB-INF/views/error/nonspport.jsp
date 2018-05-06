<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
 request.setCharacterEncoding("UTF-8");
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm"%>


<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta charset="utf-8">



<style type="text/css">
.icoStyle {
	color: #fafafa;
	font-family: 微软雅黑;
	padding: 30px 40px;
}

.neikuang {
	border-radius: 6px;
	border: 2px solid #FAFAFA;
	line-height: 30px;
	font-size: 22px;
	display: inline-block;
}

.browseName {
	margin-top: 8.6%;
	font-size: 20px;
	color: #fafafa;
}


.download {
	margin-top: 10%
}


    
    html,body{
    height:100%;
    margin:0px; 
   
}

</style>




<div style="height:100%">
	<div style="position:absolute;font-size: 30px;margin-top: 5%;width:100%;z-index:999;text-align: center;">
		<span style="color:#fafafa;">您没有开启推送服务，该APP需要支持推送功能，请打开，谢谢！</span>
			
	</div>
			

</div>
