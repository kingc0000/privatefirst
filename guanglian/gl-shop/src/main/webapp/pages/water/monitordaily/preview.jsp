<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<link href='<c:url value="/resources/css/jquery.printarea.css" />' rel="stylesheet" media="all">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<script>
	$("#pictitle").html("项目日志");
</script>
<div class="row" >
	<section class="panel">
			<header class="panel-heading hidden-xs">
				<span id="edittile">项目日志查看</span>
                <button type="button"  class="btn btn-theme pull-right fa fa-print " onclick="print('${projectname}')">打印</button>
			</header>
			<div class="panel-body " style="outline: none;overflow-y:scroll;" id="no-more-tables" >
				<div class="printObj">
				<table class="table  table-bordered">
					<tr>
						<td class="text-center" colspan="3" style="padding-left:0px;text-align: center;font-weight: 700;">${projectname}项目日志</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">时间</td><td data-title="时间" colspan="2"> ${daily.datec }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">天气</td><td data-title="天气" colspan="2">${daily.weather }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">气温</td><td data-title="气温" colspan="2">${daily.temperature }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">风级</td><td data-title="风级" colspan="2">${daily.wind }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">工况描述</td><td data-title="工况描述" colspan="2">${daily.note }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">监测点情况</td><td data-title="监测点情况" colspan="2">${daily.pointDesc }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">总结</td><td data-title="总结" colspan="2">${daily.conclusion }</td>
					</tr>
						<c:if test="${not empty daily.monitorDailyImages}">
						  <tr >
					  		<td class="hidden-xs" style="width:20%">照片</td>
					  		<td data-title="照片" colspan="2">
					  			<c:forEach items="${daily.monitorDailyImages}" var="imgs">
                       				<div class="page-break" style="margin:0 auto;width:100%;height:100%">
                       					<c:choose>
                       							<c:when test="${not empty imgs.jpeg }">
                       								<img class="preimg" src="${imgs.jpeg }">
                       							</c:when>
                       							<c:otherwise>
                       								<img class="preimg" src="${imgs.dailyImage }">
                       							</c:otherwise>
                       					</c:choose>
                       				</div>	
                       			</c:forEach>	
					  		</td>
					 	 </tr>
					  </c:if>
					  <tr>
					  	<td rowspan="7" class="hidden-xs" style="width:20%;vertical-align:middle;" valign="middle">支护结构</td>
					  	<td class="hidden-xs" style="width:20%">围护结构外观形态</td>
					  	<td data-title="围护结构外观形态">${daily.weiHuW }</td>
					  </tr>
					  <tr>
					  	<td class="hidden-xs" style="width:20%">冠梁、支撑、围檩裂缝</td>
					  	<td data-title="冠梁、支撑、围檩裂缝">${daily.leiFeng }</td>
					  </tr>
					   <tr>
					  	<td class="hidden-xs" style="width:20%">支撑、立柱变形</td>
					  	<td data-title="支撑、立柱变形">${daily.bianXing }</td>
					  </tr>
					   <tr>
					  	<td class="hidden-xs" style="width:20%">止水帷幕开裂、渗漏</td>
					  	<td data-title="止水帷幕开裂、渗漏">${daily.senLou }</td>
					  </tr>
					  <tr>
					  	<td class="hidden-xs" style="width:20%">墙后土体沉陷、裂缝及滑移</td>
					  	<td data-title="墙后土体沉陷、裂缝及滑移">${daily.huaYi }</td>
					  </tr>
					  <tr>
					  	<td class="hidden-xs" style="width:20%">基坑涌土、流砂、管涌</td>
					  	<td data-title="基坑涌土、流砂、管涌">${daily.guanYong }</td>
					  </tr>
					   <tr>
					  	<td class="hidden-xs" style="width:20%">其他</td>
					  	<td data-title="支护结构其他">${daily.zhiHuOther }</td>
					  </tr>
					  <tr>
					  	<td rowspan="6" class="hidden-xs" style="width:20%;vertical-align:middle;" valign="middle">施工工况</td>
					  	<td class="hidden-xs" style="width:20%">开挖区域土质情况</td>
					  	<td data-title="开挖区域土质情况">${daily.tuZhi }</td>
					  </tr>
					  <tr>
					  	<td class="hidden-xs" style="width:20%">基坑开挖分段长度及分层厚度</td>
					  	<td data-title="基坑开挖分段长度及分层厚度">${daily.houDu }</td>
					  </tr>
					   <tr>
					  	<td class="hidden-xs" style="width:20%">地表水、地下水状况</td>
					  	<td data-title="地表水、地下水状况">${daily.shuiStatus }</td>
					  </tr>
					   <tr>
					  	<td class="hidden-xs" style="width:20%">基坑降水(回灌)设施运转情况</td>
					  	<td data-title="基坑降水(回灌)设施运转情况">${daily.huiGuan }</td>
					  </tr>
					  <tr>
					  	<td class="hidden-xs" style="width:20%">基坑周边地面堆载情况</td>
					  	<td data-title="基坑周边地面堆载情况">${daily.duiZhai }</td>
					  </tr>
					  <tr>
					  	<td class="hidden-xs" style="width:20%">其他</td>
					  	<td data-title="施工工况其他">${daily.shiGongOther }</td>
					  </tr>
					   <tr>
					  	<td rowspan="5" class="hidden-xs" style="width:20%;vertical-align:middle;" valign="middle">周边环境</td>
					  	<td class="hidden-xs" style="width:20%">管道破损、泄漏情况</td>
					  	<td data-title="管道破损、泄漏情况">${daily.xieLou }</td>
					  </tr>
					  <tr>
					  	<td class="hidden-xs" style="width:20%">周边建筑裂缝</td>
					  	<td data-title="周边建筑裂缝">${daily.jianLFENG }</td>
					  </tr>
					   <tr>
					  	<td class="hidden-xs" style="width:20%">周边道路（地面）裂缝、沉陷</td>
					  	<td data-title="周边道路（地面）裂缝、沉陷">${daily.chenXian }</td>
					  </tr>
					   <tr>
					  	<td class="hidden-xs" style="width:20%">邻近施工情况</td>
					  	<td data-title="邻近施工情况">${daily.neibor }</td>
					  </tr>
					  <tr>
					  	<td class="hidden-xs" style="width:20%">其他</td>
					  	<td data-title="周边环境其他">${daily.zbOther }</td>
					  </tr>
					   <tr>
					  	<td rowspan="4" class="hidden-xs" style="width:20%;vertical-align:middle;" valign="middle">监测设施</td>
					  	<td class="hidden-xs" style="width:20%">基准点完好状况</td>
					  	<td data-title="基准点完好状况">${daily.jiDian }</td>
					  </tr>
					  <tr>
					  	<td class="hidden-xs" style="width:20%">测点完好状况</td>
					  	<td data-title="测点完好状况">${daily.ceDian }</td>
					  </tr>
					   <tr>
					  	<td class="hidden-xs" style="width:20%">监测元件完好情况</td>
					  	<td data-title="监测元件完好情况">${daily.yuanJian }</td>
					  </tr>
					   <tr>
					  	<td class="hidden-xs" style="width:20%">观测工作条件</td>
					  	<td data-title="观测工作条件">${daily.tiaoJian }</td>
					  </tr>
				</table>
				</div>
			</div>
		</section>
	</div>
<script>
jQuery(document).ready(function() {
	if($(window).width()>767){
		$(".preimg").attr("target","_blank");
	}
	//设置img的高度不超过屏幕；
	$(".preimg").css("height",$(window).height()-30);
})
function print(name){
		var options = {
				extraHead: '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>',
				
	            retainAttr : ["id","class","style"], 
	            popTitle  : name+'每日工程日志打印'
			}
		$(".printObj").printArea(options);
	}
</script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
<script src='<c:url value="/resources/js/jquery.printarea.js" />'></script>
