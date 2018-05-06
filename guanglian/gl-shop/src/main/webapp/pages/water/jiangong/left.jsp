<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<style>
@media (min-width: 769px){
	#sidebar {
	    /* bottom: 0px; */
	    padding-bottom: 100px;
	}
}
</style>
<script type="text/javascript">
var projectArray = new Array();
$('#loading').show();
</script>
<div id="sidebar" class="nav-collapse camera-sidebar" style="margin-top:-15px !important;margin-left:-15px !important;">
	<ul class="sidebar-menu" id="nav-accordion">
		<li id="paid_${project.id }" ><a class="needsclick<c:if test="${activeFun=='monitor'}"> active</c:if>"  href="<c:url value="/jiangong/csite/wlist.html?pid=${csite.id }"/>">实时监控</a></li>
		<li ><a class="needsclick<c:if test="${activeFun=='toEdit'}"> active</c:if>" href="<c:url value="/jiangong/csite/toEdit.html?cid=${csite.id }"/>">项目信息</a></li>
		<li class="sub-menu">
			<a class="m-win-title <c:if test="${(activeFun=='pwell'||activeFun=='dewell'||activeFun=='owell'||activeFun=='iwell'||activeFun=='dmonitor'||activeFun=='gateway'||activeFun=='dataconf'||activeFun=='warning'||activeFun=='camera')}"> active</c:if>" href="#"><span>测点管理</span></a>
			<ul class="sub">
				<li class="<c:if test="${activeFun=='pwell'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/jiangong/pwell/list.html?cid=${csite.id }"/>">降压井管理</a></li>
				<li class="<c:if test="${activeFun=='dewell'}"> active</c:if>"><a class="needsclick" href="<c:url value="/jiangong/dewell/list.html?cid=${csite.id }"/>">疏干井管理</a></li>
				<li class="<c:if test="${activeFun=='owell'}"> active</c:if>"><a class="needsclick" href="<c:url value="/jiangong/owell/list.html?cid=${csite.id }"/>">观测井管理</a></li>
				<li class="<c:if test="${activeFun=='iwell'}"> active</c:if>"><a class="needsclick" href="<c:url value="/jiangong/iwell/list.html?cid=${csite.id }"/>">回灌井管理</a></li>
				<li class="<c:if test="${activeFun=='dmonitor'}"> active</c:if>"><a class="needsclick" href="<c:url value="/jiangong/dmonitor/list.html?cid=${csite.id }"/>">环境监测</a></li>
			</ul>
		</li>
	</ul>
</div>