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
	$("#pictitle").html("项目问题");
</script>
<div class="row" >
	<section class="panel">
			<header class="panel-heading hidden-xs">
				<span id="edittile">项目问题查看</span>
                <button type="button"  class="btn btn-theme pull-right fa fa-print " onclick="print('${projectname}')">打印</button>
			</header>
			<div class="panel-body " style="outline: none;overflow-y:scroll;" id="no-more-tables" >
				<div class="printObj">
				<table class="table  table-bordered">
					<tr>
						<td class="text-center" colspan="3" style="padding-left:0px;text-align: center;font-weight: 700;">${projectname}项目问题</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">时间</td><td data-title="时间" colspan="2"> ${gon.auditSection.dateCreated }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">问题紧急程度</td><td data-title="问题紧急程度" colspan="2">${gon.monRank }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">问题类型</td><td data-title="问题类型" colspan="2">${gon.monType }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">负责人</td><td data-title="负责人" colspan="2">${gon.owner }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">问题处理情况</td><td data-title="问题处理情况" colspan="2">
							<c:choose>
								<c:when test="${gon.monStatus==0}">未处理</c:when>
								<c:otherwise>已处理</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">处理人</td><td data-title="处理人" colspan="2">${gon.didman }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">处理意见</td><td data-title="处理意见" colspan="2">${gon.suggest }</td>
					</tr>
						<c:if test="${not empty gon.attach}">
						  <tr >
					  		<td class="hidden-xs" style="width:20%">附件</td>
					  		<td data-title="附件" colspan="2">
					  			<c:forEach items="${gon.attach}" var="imgs">
                       				<div class="page-break" style="margin:0 auto;width:100%;height:100%">
                       					<c:choose>
                       							<c:when test="${imgs.fileType==true }">
                       								<img class="preimg" src='<sm:contentImage imageName="${imgs.fileName}" imageType="GUARD_PRO"/>'/>
                       							</c:when>
                       							<c:otherwise>
                       								<br>
                       								<a class="tooltips hidden-xs" data-placement="left" data-original-title="下载" href="<c:url value="/files/downloads/${imgs.fileName}?ftype=GUARD_PRO"/>"><i class="fa fa-download">${imgs.fileName}</i></a>
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
	            popTitle  : name+'项目问题打印'
			}
		$(".printObj").printArea(options);
	}
</script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
<script src='<c:url value="/resources/js/jquery.printarea.js" />'></script>
