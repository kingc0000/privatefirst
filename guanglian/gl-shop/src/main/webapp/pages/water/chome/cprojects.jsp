<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<div id="pbody" class="row">
	<div class="col-xs-12 col-gl-12">
	<c:if test="${not empty jylist}">
		<section class="panel">
			<header class="panel-heading" <c:if test="${empty app || app=='' }">style="padding-left:30px;" </c:if>>
          			降压运行
      		</header>
      		<div class="list-group">
		         <c:forEach items="${jylist }" var="jy" varStatus="status">
		         	<c:choose>
		         		<c:when test="${empty app || app=='' }">
		         			<a class="list-group-item " style="padding-left:30px; <c:if test="${jy.runstatus>0}"> color: #FF6C60; </c:if>"  href="<c:url value="/water/csite/wlist.html?pid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         		</c:when>
		         		<c:otherwise>
		         			<a class="list-group-item " <c:if test="${jy.runstatus>0}">style="color: #FF6C60;" </c:if> href="<c:url value="/water/csite/cproject.html?cid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         		</c:otherwise>
		         	</c:choose>
		         </c:forEach>
      		</div>
		</section>
	</c:if>
	<c:if test="${not empty sglist}">
		<section class="panel">
			<header class="panel-heading" <c:if test="${empty app || app=='' }">style="padding-left:30px;" </c:if>>
          			疏干运行
      		</header>
      		<div class="list-group">
		         <c:forEach items="${sglist }" var="jy" varStatus="status">
		         	<c:choose>
		         		<c:when test="${empty app || app=='' }">
		         			<a class="list-group-item " style="padding-left:30px; <c:if test="${jy.runstatus>0}"> color: #FF6C60; </c:if>" href="<c:url value="/water/csite/wlist.html?pid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         		</c:when>
		         		<c:otherwise>
		         			<a class="list-group-item " <c:if test="${jy.runstatus>0}">style="color: #FF6C60;" </c:if> href="<c:url value="/water/csite/cproject.html?cid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         		</c:otherwise>
		         	</c:choose>
		         </c:forEach>
      		</div>
		</section>
	</c:if>
	<c:if test="${not empty cjlist}">
		<section class="panel">
			<header class="panel-heading" <c:if test="${empty app || app=='' }">style="padding-left:30px;" </c:if>>
          			成井
      		</header>
      		<div class="list-group">
		         <c:forEach items="${cjlist }" var="jy" varStatus="status">
		         	<c:choose>
		         		<c:when test="${empty app || app=='' }">
		         			<a class="list-group-item " style="padding-left:30px; <c:if test="${jy.runstatus>0}"> color: #FF6C60; </c:if>"  href="<c:url value="/water/csite/wlist.html?pid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         		</c:when>
		         		<c:otherwise>
		         			<a class="list-group-item " <c:if test="${jy.runstatus>0}">style="color: #FF6C60;" </c:if> href="<c:url value="/water/csite/cproject.html?cid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         		</c:otherwise>
		         	</c:choose>
		         </c:forEach>
      		</div>
		</section>
	</c:if>
	<c:if test="${not empty wclist}">
		<section class="panel">
			<header class="panel-heading" <c:if test="${empty app || app=='' }">style="padding-left:30px;" </c:if>>
          			结束
      		</header>
      		<div class="list-group">
		         <c:forEach items="${wclist }" var="jy" varStatus="status">
		         	<c:choose>
		         		<c:when test="${empty app || app=='' }">
		         			<a class="list-group-item " style="padding-left:30px;<c:if test="${jy.runstatus>0}"> color: #FF6C60; </c:if>"  href="<c:url value="/water/csite/wlist.html?pid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         		</c:when>
		         		<c:otherwise>
		         			<a class="list-group-item " <c:if test="${jy.runstatus>0}">style="color: #FF6C60;" </c:if> href="<c:url value="/water/csite/cproject.html?cid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         		</c:otherwise>
		         	</c:choose>
		         </c:forEach>
      		</div>
		</section>
	</c:if>
</div>
</div>
<script>
$('#pictitle').html("${ctype}");
function goback(){
	javascript :history.back(-1);
}
</script>
