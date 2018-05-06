<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<link href='<c:url value="/resources/css/jquery.printarea.css" />' rel="stylesheet" media="all">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<script>
	$("#pictitle").html("评论图片");
</script>
<div class="row" >
	<section class="panel">
			<header class="panel-heading hidden-xs">
				<span id="edittile">评论图片查看</span>
			</header>
			<div class="panel-body " style="outline: none;overflow-y:scroll;"  >
				<c:choose>
					<c:when test="${not empty preview }">
						<c:forEach items="${preview}" var="img">
							<div><img  src="${img.imageUrl }" style="width:100%;width:100%"/></div>
							
						</c:forEach>
					</c:when>
					<c:otherwise>该评论没有图片</c:otherwise>
				</c:choose>
			</div>
		</section>
	</div>

