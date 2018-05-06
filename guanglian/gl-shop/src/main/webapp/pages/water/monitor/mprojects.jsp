<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<div id="pbody" class="row">
	<div class="col-xs-12 col-gl-12">
	<c:if test="${not empty yxlist}">
		<section class="panel">
			<header class="panel-heading" <c:if test="${empty app || app=='' }">style="padding-left:30px;"</c:if> >
          			正在运行项目
      		</header>
      		<div class="list-group">
		         <c:forEach items="${yxlist }" var="jy" varStatus="status">
		         	<a class="list-group-item " <c:if test="${empty app || app=='' }">style="padding-left:30px;"</c:if>  href="<c:url value="/water/monitor/detail.html?mid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         </c:forEach>
      		</div>
		</section>
	</c:if>
	<c:if test="${not empty gblist}">
		<section class="panel">
			<header class="panel-heading" <c:if test="${empty app || app=='' }">style="padding-left:30px;" </c:if>>
          			已结束项目
      		</header>
      		<div class="list-group">
		         <c:forEach items="${gblist }" var="jy" varStatus="status">
		         	<a class="list-group-item " style="padding-left:30px;"  href="<c:url value="/water/monitor/detail.html?mid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         </c:forEach>
      		</div>
		</section>
	</c:if>
</div>
</div>
<script>
$('#pictitle').html("监控项目：${zone}");
function goback(){
	javascript :history.back(-1);
}
</script>
