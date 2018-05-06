<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<script>
	$('#pictitle').html('预览');
	
</script>
<img style="max-width:100%;height:auto;" class="text-center" src="<c:url value='/files/previewbyext/${aname}?ftype=${ftype}'/>"/>