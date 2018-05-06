<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<div id="pbody" class="row">
	<div class="col-xs-12 col-gl-12">
	<c:if test="${not empty remap}">
		<c:forEach var="map" items="${remap}">
			<section class="panel">
			<header class="panel-heading" <c:if test="${empty requestScope.app || requestScope.app=='' }">style="padding-left:30px;"</c:if> >
          			${map.key}
      		</header>
      		<div class="list-group">
		         <c:forEach items="${map.value}" var="jy" varStatus="status">
		         	<a class="list-group-item " <c:if test="${empty requestScope.app || requestScope.app=='' }">style="padding-left:30px;"</c:if>  href="<c:url value="/water/guard/detail.html?gid=${jy.id}"/>">${ status.index + 1}、${jy.name }<span class="pull-right">>></span></a>
		         </c:forEach>
      		</div>
		</section>
		</c:forEach>
		
	</c:if>
</div>
</div>
<script>
$('#pictitle').html("监控项目：${zone}");
function goback(){
	javascript :history.back(-1);
}
</script>
