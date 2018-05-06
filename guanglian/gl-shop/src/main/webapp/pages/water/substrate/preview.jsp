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
	$("#pictitle").html("项目交底物");
</script>
<div class="row" >
	<section class="panel">
			<header class="panel-heading hidden-xs">
				<span id="edittile">项目交底物查看</span>
                <button type="button"  class="btn btn-theme pull-right fa fa-print " onclick="print('${projectname}')">打印</button>
			</header>
			<div class="panel-body " style="outline: none;overflow-y:scroll;" id="no-more-tables" >
				<div class="printObj">
				<table class="table  table-bordered">
					<tr>
						<td class="text-center" colspan="3" style="padding-left:0px;text-align: center;font-weight: 700;">${projectname}项目交底物</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">时间</td><td data-title="时间" colspan="2"> ${sub.auditSection.dateCreated }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">备注</td><td data-title="备注" colspan="2">${sub.remark }</td>
					</tr>
					<tr>
						<td class="hidden-xs" style="width:20%">提交人</td><td data-title="负责人" colspan="2">${sub.auditSection.modifiedBy }</td>
					</tr>
					
						<c:if test="${not empty sub.attach}">
						  <tr >
					  		<td class="hidden-xs" style="width:20%">附件</td>
					  		<td data-title="附件" colspan="2">
					  			<c:forEach items="${sub.attach}" var="imgs">
                       				<div class="page-break" style="margin:0 auto;width:100%;height:100%">
                       					<c:choose>
                       							<c:when test="${imgs.fileType==true }">
                       								<img class="preimg" src='<sm:contentImage imageName="${imgs.fileName}" imageType="MONITOR_SUB"/>'/>
                       							</c:when>
                       							<c:otherwise>
                       								<br>
                       								<a class="tooltips hidden-xs" data-placement="left" data-original-title="下载" href="<c:url value="/files/downloads/${imgs.fileName}?ftype=MONITOR_SUB"/>"><i class="fa fa-download">${imgs.fileName}</i></a>
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
	            popTitle  : name+'项目交底物打印'
			}
		$(".printObj").printArea(options);
	}
</script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
<script src='<c:url value="/resources/js/jquery.printarea.js" />'></script>
