<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<script>
	$('#pictitle').html('文件查看');
	var pheight=$(window).height()-140;
</script>
<div id="pdf"></div> 
<iframe id="docframe"></iframe>
<%-- <script src='<c:url value="/resources/assets/pdf/pdfobject.min.js" />'></script> --%>

<script>
	//PDFObject.embed("<c:url value='/files/previewbyext/${aname}'/>", "#pdf");
	//$(".pdfobject-container").height($(window).height()-140);
	
	var url = "<c:url value='/resources/assets/pdfjs/web/viewer.html?file='/>"+"<c:url value='/files/previewbyext/${aname}'/>";
	$("#docframe").attr("width", "100%").attr("height", $(window).height()-120).attr("src", url);
</script>
