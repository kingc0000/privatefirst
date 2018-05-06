<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/css/bootstrap-directional-buttons.min.css" />' rel="stylesheet">
<div class="row" id="edittable">
	<div class="col-xs-12 col-gl-12">
	<section class="panel">
      <header class="panel-heading" <c:if test="${empty app || app=='' }">style="padding-left:30px;"</c:if>>
          最近打开的项目
      </header>
      	<div class="list-group">
	         <c:forEach items="${last }" var="last" varStatus="status">
	         	<a class="list-group-item " <c:if test="${empty app || app=='' }">style="padding-left:30px;"</c:if> href="<c:url value="/water/guard/detail.html?gid=${last.id}"/>">${ status.index + 1}、${last.name }<span class="pull-right">>></span></a>
	         </c:forEach>
      	</div>
      
  </section>
  
  <section class="panel">
  	<div class="panel-body">
  		<c:forEach items="${projects}" var="project">
  			<div class="col-xs-6" style="font-size:20px;padding:10px 5px;text-align: center;">
  			<a class="btn btn-success btn-arrow-right " href='<c:url value="/water/guard/ctype.html?zone=${project.key}"/>'>${project.key}(<c:out value="${fn:length(project.value)}"></c:out>)</a>
  			</div>
  		</c:forEach>
  	</div>
  </section>
  <sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
  <section class="panel hidden-xs">
	<div class="panel-body" style="margin-bottom:30px;">
        <a href="<c:url value="/water/guard/list.html"/>" class="btn btn-info btn-lg btn-block"><i class="fa fa-sitemap"></i>全部项目</a>
     </div>
     </section>
     </sec:authorize>
</div>
</div>
<script>
$('#pictitle').html("监护项目");
function goback(){
	javascript :history.back(-1);
}
</script>
