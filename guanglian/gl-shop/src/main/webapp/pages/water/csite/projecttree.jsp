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
		<li><input type="text" id="search-input" class="form-control search-tree search-input" autocomplete='off' placeholder="过滤"></li>
		<c:forEach items="${requestScope.projects}" var="zone" >
				<li class="sub-menu">
					<a class="m-win-title zone <c:if test="${activeZone==zone.key}"> active</c:if>" title="${zone.key }" href="#" id="zname_${zone.key}"> <i class="fa fa-flag"></i> <span>${zone.key }</span></a>
					<ul class="sub">
						<c:forEach items="${zone.value}" var="project" varStatus="status">
							<li  class="sub-menu">
								<a class="m-win-title zone-selected <c:if test="${activePid==project.id}"> active</c:if>" data-zone="${zone.key}" title="${project.name }" href="#" id="pid_${project.id }"> 
								 <span style="width:150px;display:-moz-inline-box;display:inline-block;overflow: hidden;text-overflow: ellipsis; white-space: nowrap;"><i class="fa fa-road"></i>&nbsp;${project.name }</span></a> 
								<script type="text/javascript">
									var pitem = new Object();
									pitem.id=${project.id };
									pitem.name="${project.name }";
									pitem.lng=${project.longitude };
									pitem.lat=${project.latitude };
									pitem.city="${project.city }";
									pitem.status="${project.status }";
									pitem.sstatus="${project.sstatus }";
									pitem.rail="${project.rail }";
									projectArray[projectArray.length]=pitem;
								</script>
								<ul class="sub">
									<li<c:if test="${activePid==project.id&&activeFun=='monitor'}"> class="active"</c:if> id="paid_${project.id }" ><a href="#" onclick="gotoProject(${project.id })">实时监控</a></li>
									<li class="needsclick <c:if test="${activePid==project.id&&activeFun=='toEdit'}"> active </c:if>"><a href="<c:url value="/water/csite/toEdit.html?cid=${project.id }"/>">项目信息</a></li>
									<li class="sub-menu">
										<a class="m-win-title <c:if test="${activePid==project.id&&(activeFun=='pwell'||activeFun=='dewell'||activeFun=='owell'||activeFun=='iwell'||activeFun=='dmonitor'||activeFun=='gateway'||activeFun=='dataconf'||activeFun=='warning'||activeFun=='camera'||activeFun=='projectimg')}"> active</c:if>" href="#"><span>测点管理</span></a>
										<ul class="sub">
											<li class="<c:if test="${activePid==project.id&&activeFun=='pwell'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/pwell/list.html?cid=${project.id }"/>">降水井管理</a></li>
											<li class="<c:if test="${activePid==project.id&&activeFun=='dewell'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/dewell/list.html?cid=${project.id }"/>">疏干井管理</a></li>
											<li class="<c:if test="${activePid==project.id&&activeFun=='owell'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/owell/list.html?cid=${project.id }"/>">观测井管理</a></li>
											<li class="<c:if test="${activePid==project.id&&activeFun=='iwell'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/iwell/list.html?cid=${project.id }"/>">回灌井管理</a></li>
											<li class="<c:if test="${activePid==project.id&&activeFun=='dmonitor'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/dmonitor/list.html?cid=${project.id }"/>">环境监测</a></li>
											<li class="<c:if test="${activePid==project.id&&activeFun=='gateway'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/gateway/list.html?cid=${project.id }"/>">设备网关</a></li>
											<li class="<c:if test="${activePid==project.id&&activeFun=='dataconf'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/dataconf/list.html?cid=${project.id }"/>">采集配置</a></li>
											<li class="<c:if test="${activePid==project.id&&activeFun=='warning'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/warning/list.html?cid=${project.id }"/>">告警汇总</a></li>
											<li class="<c:if test="${activePid==project.id&&activeFun=='camera'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/camera/plist.html?cid=${project.id }"/>">摄像头管理</a></li>
											<li class="<c:if test="${activePid==project.id&&activeFun=='projectimg'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/projectimg/list.html?cid=${project.id }"/>">图纸标记</a></li>
											
										</ul>
									</li>
									<li class="<c:if test="${activePid==project.id&&activeFun=='daily'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/daily/list.html?cid=${project.id }"/>">施工日志</a></li>
									<li class="<c:if test="${activePid==project.id&&activeFun=='welldaily'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/statistics/welldaily.html?cid=${project.id }"/>">成井日志</a></li>
									<li class="<c:if test="${activePid==project.id&&activeFun=='wellreport'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/report/wellreport.html?cid=${project.id }"/>">每日报表</a></li>
									<li class="<c:if test="${activePid==project.id&&activeFun=='preview'}"> active</c:if>"><a class="needsclick" href="<c:url value="/water/preview/review.html?cid=${project.id }"/>">我的评论</a></li>
								</ul>
							</li>
						</c:forEach>
					</ul>
				</li>
		</c:forEach>
	</ul>
</div>
<script src='<c:url value="/resources/js/quicksearch.js" />'></script>