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
					<li><a class="m-win-title zone <c:if test="${activeCode=='project'}"> active</c:if>" title="" href="<c:url value="/water/guard/detail.html?gid=${gid}"/>"> <i class="fa fa-info"></i> <span>项目信息</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='equip'}"> active</c:if>" title="" href="<c:url value="/water/guardeqip/list.html?gid=${gid}"/>"> <i class="fa fa-gear"></i> <span>设备台账</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='daily'}"> active</c:if>" title="" href="<c:url value="/water/guarddaily/list.html?gid=${gid}"/>"> <i class="fa fa-pencil-square-o"></i> <span>项目日志</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='greport'}"> active</c:if>" title="" href="<c:url value="/water/greport/list.html?gid=${gid}"/>"> <i class="fa fa-bullseye"></i> <span>监护日报</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='gnoproblem'}"> active</c:if>" title="" href="<c:url value="/water/gonproblem/list.html?gid=${gid}"/>"> <i class="fa fa-bookmark"></i> <span>项目问题</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='gsign'}"> active</c:if>" title="" href="<c:url value="/water/sign/list.html?gid=${gid}"/>"> <i class="fa fa-edit"></i> <span>人员签到</span></a></li>
						<li><a class="m-win-title zone <c:if test="${activeCode=='verticaldis'||activeCode=='diameterconvert' ||activeCode=='hshift'}"> active</c:if>" title="" href="#"> <i class="fa fa-list-ul"></i> <span>测点管理</span></a>
						      <ul class="sub">
									<c:choose>
										<c:when test="${requestScope.app==null || requestScope.app==''}">
											<li class="<c:if test="${activeCode=='verticaldis'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/verticaldis/list.html?gid=${gid}"/>">结构垂直位移</a></li>
											<li class="<c:if test="${activeCode=='diameterconvert'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/diameterconvert/list.html?gid=${gid}"/>">直径收敛</a></li>
											<li class="<c:if test="${activeCode=='hshift'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/hshift/list.html?gid=${gid}"/>">水平位移</a></li>
										</c:when>
										<c:otherwise>
											<li class="<c:if test="${activeCode=='verticaldis'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/verticaldisdata/list.html?gid=${gid}"/>">结构垂直位移</a></li>
											<li class="<c:if test="${activeCode=='diameterconvert'}"> active</c:if>"><a  class="needsclick" href="<c:url value="/water/diameterconvertdata/list.html?gid=${gid}"/>">直径收敛</a></li>
											<li class="<c:if test="${activeCode=='hshift'}"> active</c:if>"><a style="padding-bottom: 50px;margin-bottom:100px;" class="needsclick" href="<c:url value="/water/hshiftdata/list.html?gid=${gid}"/>">水平位移</a></li>
										</c:otherwise>
									</c:choose>
								</ul>
						</li>
	</ul>
</div>