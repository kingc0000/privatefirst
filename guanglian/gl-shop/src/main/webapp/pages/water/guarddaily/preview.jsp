<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
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
						<td class="hidden-xs" style="width:20%">现场监护意见</td><td data-title="现场监护意见" colspan="2">${daily.pointDesc }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">地铁结构病害调查报表</td><td data-title="地铁结构病害调查报表" colspan="2">${daily.weiHuW }</td>
					</tr>
						<c:if test="${not empty daily.guardDailyImages}">
						  <tr >
					  		<td class="hidden-xs" style="width:20%">照片</td>
					  		<td data-title="照片" colspan="2">
					  			<c:forEach items="${daily.guardDailyImages}" var="imgs">
                       				<div class="page-break" style="margin:0 auto;width:100%;height:100%">
                       					<c:choose>
                       							<c:when test="${not empty imgs.jpeg }">
                       								<img class="preimg" src='<sm:contentImage imageName="${imgs.jpeg}" imageType="GUARD_DAILY"/>'/>
                       							</c:when>
                       							<c:otherwise>
                       								<img class="preimg" src='<sm:contentImage imageName="${imgs.dailyImage}" imageType="GUARD_DAILY"/>'/>
                       							</c:otherwise>
                       					</c:choose>
                       				</div>	
                       			</c:forEach>	
					  		</td>
					 	 </tr>
					  </c:if>
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
