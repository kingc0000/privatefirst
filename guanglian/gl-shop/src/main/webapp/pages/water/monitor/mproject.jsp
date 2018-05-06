<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<div class="row">
	<div class="col-md-12">
		<section class="panel">
			<header class="panel-heading">
				
			</header>
			<div class="panel-body">
				<jsp:include page="/pages/water/monitor/mdetail.jsp" />
			</div>
		</section>
	</div>
</div>
<script>
$(function() {
	var hasright=${hasRight};
	if(hasright){
		$("#jssub").show();
	}else{
		$("#jssub").hide();
	}
});
</script>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/js/project/vaildform.js" />'></script>
