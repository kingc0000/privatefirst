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
function gotoProject(pid){
	window.location.href='<c:url value="/water/csite/wlist.html?pid='+pid+'"/>';
}
</script>
<div id="sidebar" class="nav-collapse camera-sidebar" style="margin-top:-15px !important;margin-left:-15px !important;">
	<!-- sidebar menu start-->
	
	<ul class="sidebar-menu" id="nav-accordion">
				<li id="paid_${project.id }" ><a class="needsclick<c:if test="${activeFun=='monitor'}"> active</c:if>"  href="#" onclick="gotoProject(${csite.id })">实时监控</a></li>
				<li ><a class="needsclick<c:if test="${activeFun=='toEdit'}"> active</c:if>" href="<c:url value="/water/csite/toEdit.html?cid=${csite.id }"/>">项目信息</a></li>
				<li class="sub-menu">
					<a class="m-win-title <c:if
					test="${(activeFun=='pwell'||activeFun=='dewell'||activeFun=='owell'||activeFun=='iwell'||activeFun=='dmonitor'||activeFun=='gateway'||activeFun=='dataconf'||activeFun=='warning'||activeFun=='camera'|| activeFun=='projectimg')}"> active</c:if>" href="#"><span>测点管理</span></a>
					<ul class="sub">
						<li class="<c:if test="${activeFun=='pwell'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/pwell/list.html?cid=${csite.id }"/>">降水井管理</a></li>
						<li class="<c:if test="${activeFun=='dewell'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/dewell/list.html?cid=${csite.id }"/>">疏干井管理</a></li>
						<li class="<c:if test="${activeFun=='owell'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/owell/list.html?cid=${csite.id }"/>">观测井管理</a></li>
						<li class="<c:if test="${activeFun=='iwell'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/iwell/list.html?cid=${csite.id }"/>">回灌井管理</a></li>
						<li class="<c:if test="${activeFun=='dmonitor'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/dmonitor/list.html?cid=${csite.id }"/>">环境监测</a></li>
						<c:if test="${hasRight}">
							<li class="<c:if test="${activeFun=='gateway'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/gateway/list.html?cid=${csite.id }"/>">设备网关</a></li>
							<li class="<c:if test="${activeFun=='dataconf'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/dataconf/list.html?cid=${csite.id }"/>">采集配置</a></li>
						</c:if>
						<li class="<c:if test="${activeFun=='warning'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/warning/list.html?cid=${csite.id }"/>">告警汇总</a></li>
						<li class="<c:if test="${activeFun=='camera'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/camera/plist.html?cid=${csite.id }"/>">摄像头管理</a></li>
						<li class="<c:if test="${activeFun=='projectimg'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/projectimg/list.html?cid=${csite.id }"/>">图纸标记</a></li>
					</ul>
				</li>
				<c:if test="${hasRight}">
					<li><a class="needsclick<c:if test="${activeFun=='daily'}"> active</c:if>" href="<c:url value="/water/daily/list.html?cid=${csite.id }"/>">施工日志</a></li>
				</c:if>
				<li><a class="needsclick<c:if test="${activeFun=='welldaily'}"> active</c:if>" href="<c:url value="/water/statistics/welldaily.html?cid=${csite.id }"/>">成井日志</a></li>
				<li><a class="needsclick<c:if test="${activeFun=='wellreport'}"> active</c:if>" href="<c:url value="/water/report/wellreport.html?cid=${csite.id }"/>">每日报表</a></li>
				<li><a class="needsclick<c:if test="${activeFun=='preview'}"> active</c:if>" href="<c:url value="/water/preview/review.html?cid=${csite.id }"/>">我的评论</a></li>
			
	</ul>
</div>
<script src='<c:url value="/resources/js/quicksearch.js" />'></script>