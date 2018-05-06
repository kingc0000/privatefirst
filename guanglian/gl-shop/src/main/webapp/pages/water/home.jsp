<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<div class="row main-background"   >
	
</div>
<!--right slidebar-->
<script src='<c:url value="/resources/js/slidebars.min.js" />'></script>
<script type="text/javascript">
$(function(){
	resizeLayout();
	//设置title
	$('#pictitle').html('系统管理');
});
function resizeLayout(){
	var hei=$(window).height();
	var wid=$(window).width()
	$(".main-background").css("height",hei-40);
	$(".main-background").css("width",wid);
	$(".wrapper").css("margin-top",20);
	$(".wrapper").css("padding",0);
}
$(window).resize(function(){
	resizeLayout();
	})
</script>
   