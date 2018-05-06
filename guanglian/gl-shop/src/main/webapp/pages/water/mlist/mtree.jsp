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
$('#loading').show();

</script>
<div id="sidebar" class="nav-collapse camera-sidebar" style="margin-top:-15px !important;margin-left:-15px !important;">
	<!-- sidebar menu start-->
	
	<ul class="sidebar-menu" id="nav-accordion">
					<li><a class="m-win-title zone <c:if test="${activeCode=='project'}"> active</c:if>" title="" href="<c:url value="/water/monitor/detail.html?mid=${mid}"/>"> <i class="fa fa-info"></i> <span>项目信息</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='equip'||activeCode=='meps'}"> active</c:if>" href="#"> <i class="fa fa-gear"></i> <span>设备台账</span></a>
							<ul class="sub">
								<li class="<c:if test="${activeCode=='equip'}"> active</c:if>"><a class="needsclick"  href="<c:url value="/water/monitoreqip/list.html?mid=${mid}"/>">设备信息</a></li>
								<li class="<c:if test="${activeCode=='meps'}"> active</c:if>"><a class="needsclick"  href="<c:url value="/water/meqips/list.html?mid=${mid}"/>">台账</a></li>
							</ul>
						</li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='mpoint'}"> active</c:if>" title="" href="<c:url value="/warter/mpoint/list.html?mid=${mid}"/>"> <i class="fa fa-exclamation-triangle"></i> <span>警戒值设置</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='daily'}"> active</c:if>" title="" href="<c:url value="/water/monitordaily/list.html?mid=${mid}"/>"> <i class="fa fa-pencil-square-o"></i> <span>项目日志</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='mstatistical'}"> active</c:if>" title="" href='<c:url value="/water/mstatistical/list.html?mid=${mid}"></c:url>'> <i class="fa fa-th"></i> <span>统计报表</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='mreport'||activeCode=='xreport'}"> active</c:if>" href="#"> <i class="fa fa-calendar-o"></i> <span>监控日报</span></a>
							<ul class="sub">
								<li class="<c:if test="${activeCode=='mreport'}"> active</c:if>"><a class="needsclick"  href="<c:url value="/water/rmreport/list.html?mid=${mid}"/>">沉降日报</a></li>
								<li class="<c:if test="${activeCode=='xreport'}"> active</c:if>"><a class="needsclick"  href="<c:url value="/water/xmreport/list.html?mid=${mid}"/>">测斜日报</a></li>
							</ul>
						</li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='monproblem'}"> active</c:if>" title="" href="<c:url value="/warter/monproblem/list.html?mid=${mid}"/>"> <i class="fa fa-bookmark"></i> <span>项目问题</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='substrate'}"> active</c:if>" title="" href="<c:url value="/water/substrate/list.html?mid=${mid}"/>"> <i class="fa fa-cloud-upload"></i> <span>项目交底物</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='surface'||activeCode=='waterline' ||activeCode=='building'||activeCode=='upright'||activeCode=='ringbeam'||activeCode=='hiddenline' || activeCode=='supaxial' || activeCode=='displacement' || activeCode=='oblique'}"> active</c:if>" href="#"> <i class="fa fa-list-ul"></i> <span>测点管理</span></a>
								<ul class="sub">
									<c:choose>
										<c:when test="${requestScope.app ==null || requestScope.app==''}">
											<li class="<c:if test="${activeCode=='surface'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/surface/list.html?mid=${mid}"/>">周边地表</a></li>
											<li class="<c:if test="${activeCode=='waterline'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/waterline/list.html?mid=${mid}"/>">管线</a></li>
											<li class="<c:if test="${activeCode=='building'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/building/list.html?mid=${mid}"/>">建筑物</a></li>
											<li class="<c:if test="${activeCode=='upright'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/upright/list.html?mid=${mid}"/>">立柱变形</a></li>
											<li class="<c:if test="${activeCode=='ringbeam'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/ringbeam/list.html?mid=${mid}"/>">圈梁变形</a></li>
											<li class="<c:if test="${activeCode=='hiddenline'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/hiddenline/list.html?mid=${mid}"/>">潜层水位</a></li>
											<li class="<c:if test="${activeCode=='supaxial'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/supaxial/list.html?mid=${mid}"/>">支撑轴力</a></li>
											<li class="<c:if test="${activeCode=='displacement'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/displacement/list.html?mid=${mid}"/>">水平位移</a></li>
											<li class="<c:if test="${activeCode=='oblique'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/oblique/list.html?mid=${mid}"/>">测斜</a></li>
										</c:when>
										<c:otherwise>
											<li class="<c:if test="${activeCode=='surface'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/surfacedata/list.html?mid=${mid}"/>">周边地表</a></li>
											<li class="<c:if test="${activeCode=='waterline'}"> active</c:if>"><a  class="needsclick" href="<c:url value="water/waterlinedata/list.html?mid=${mid}"/>">管线</a></li>
											<li class="<c:if test="${activeCode=='building'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/buildingdata/list.html?mid=${mid}"/>">建筑物</a></li>
											<li class="<c:if test="${activeCode=='upright'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/uprightdata/list.html?mid=${mid}"/>">立柱变形</a></li>
											<li class="<c:if test="${activeCode=='ringbeam'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/ringbeamdata/list.html?mid=${mid}"/>">圈梁变形</a></li>
											<li class="<c:if test="${activeCode=='hiddenline'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/hiddenlinedata/list.html?mid=${mid}"/>">潜层水位</a></li>
											<li class="<c:if test="${activeCode=='supaxial'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/supaxialdata/list.html?mid=${mid}"/>">支撑轴力</a></li>
											<li class="<c:if test="${activeCode=='displacement'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/displacementdata/list.html?mid=${mid}"/>">水平位移</a></li>
											<li style="margin-bottom: 50px;"class="<c:if test="${activeCode=='oblique'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/obliquedata/list.html?mid=${mid}"/>">测斜</a></li>
										</c:otherwise>
									</c:choose>
								</ul>
						</li>
	</ul>
</div>