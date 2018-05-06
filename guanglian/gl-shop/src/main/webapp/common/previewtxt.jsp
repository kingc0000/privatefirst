<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<script>
	$('#pictitle').html('文件查看');
</script>
<iframe id="tframe" frameborder="0" style="border:1px solid #ccc;" src="<c:url value='/files/previewbyext/${aname}'/>"></iframe>
<script>
	$("#tframe").height($(window).height()-140);
	$("#tframe").width($(window).width()*0.9);
</script>