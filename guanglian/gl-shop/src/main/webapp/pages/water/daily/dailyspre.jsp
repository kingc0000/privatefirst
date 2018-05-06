<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<link href='<c:url value="/resources/css/jquery.printarea.css" />' rel="stylesheet" media="all">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<script>
	$("#pictitle").html("施工日志");
</script>
<div class="row" >
	<section class="panel">
			<header class="panel-heading hidden-xs">
				<span id="edittile">项目施工日志查看</span>
                <button type="button"  class="btn btn-theme pull-right fa fa-print " onclick="print('${projectname}')">打印</button>
			</header>
			<div class="panel-body " style="outline: none;overflow-y:scroll;" id="no-more-tables" >
				<div class="printObj">
				<table class="table  table-bordered">
					<tr>
						<td class="text-center" colspan="10"style="padding-left:0px;text-align: center;font-weight: 700;">${projectname}项目每日施工日记</td>
					</tr>
					<tr>
						<td class="hidden-xs">时间</td><td data-title="时间" colspan="11"> ${daily.datec }</td>
					</tr>
					<tr>
						<td class="hidden-xs">天气</td><td data-title="天气" colspan="11">${daily.weather }</td>
					</tr>
					<tr>
					  	<td class="hidden-xs">每日信息</td>
					  	<td data-title="总结" colspan="10" class="conclusion">${daily.conclusion }</td>
					  </tr>
					<c:if test="${not empty daily.wellCon}">
						<tr>
							<td class="hidden-xs" rowspan="6">成井工况</td>
							<td class="hidden-xs"></td>
							<td class="hidden-xs">当日完成数</td>
				   			<td class="hidden-xs">当日井号</td>
				   			<td class="hidden-xs">计划完成数</td>
				   			<td class="hidden-xs">累积完成数</td>
				   			<td class="hidden-xs">设计数量</td>
				   			<td class="hidden-xs">完成率</td>
				   			<td class="hidden-xs">破坏情况</td>
				   			<td class="hidden-xs">备注</td>
						</tr>
						<c:forEach items="${daily.wellCon}" var="wc" varStatus="status">
							<tr>
								<td data-title="类型" class="wtype" title="${status.index}">
									<c:choose>
										<c:when test="${wc.wType==0}">
											疏干井
										</c:when>
										<c:when test="${wc.wType==1}">
											降水井
										</c:when>
										<c:when test="${wc.wType==2}">
											回灌井
										</c:when>
										<c:when test="${wc.wType==3}">
											观测井
										</c:when>
										<c:when test="${wc.wType==4}">
											监测点
										</c:when>
										
									</c:choose>
								</td>
								<td data-title="当日完成数" id="dayCmp_${status.index}"></td>
								<td data-title="当日井号" id="wellnames_${status.index}">${wc.wellnames}</td>
								<td data-title="计划完成数">${wc.planCmp}</td>
								<td data-title="累积完成数" id="cumCmp_${status.index}">${wc.cumCmp}</td>
								<td data-title="设计数量" id="designQua_${status.index}">${wc.designQua}</td>
								<td data-title="完成率" id="crate_${status.index}"></td>
								<td data-title="破坏情况">${wc.dest}</td>
								<td data-title="备注">${wc.memo}</td>
			        		</tr> 
						</c:forEach>
						<tr>
						  	<td class="hidden-xs">开挖工况</td>
						  	<td data-title="开挖工况" colspan="10">${daily.excavation }</td>
						  </tr> 
						  <tr>
						  	<td class="hidden-xs">疏干运行工况</td>
						  	<td data-title="疏干运行工况" colspan="10">${daily.combDry }</td>
						  </tr> 
						  <tr>
						  	<td class="hidden-xs">降水运行工况</td>
						  	<td data-title="降水运行工况" colspan="10">${daily.stepDown }</td>
						  </tr> 
						  <tr>
						  	<td class="hidden-xs">回灌井运行工况</td>
						  	<td data-title="回灌井运行工况" colspan="10">${daily.recharge }</td>
						  </tr> 
						   <tr>
						  	<td class="hidden-xs">主要风险点</td>
						  	<td data-title="主要风险点" colspan="10">${daily.risk }</td>
						  </tr>
						  <tr>
						  	<td class="hidden-xs">安全问题</td>
						  	<td data-title="安全问题" colspan="10">${daily.safe }</td>
						  </tr>
						  <tr>
						  	<td class="hidden-xs">质量问题</td>
						  	<td data-title="质量问题" colspan="10">${daily.quality }</td>
						  </tr>
						  <tr>
						  	<td class="hidden-xs">本周主要工作</td>
						  	<td data-title="本周主要工作" colspan="10">${daily.thisWeek }</td>
						  </tr>
						  <tr>
						  	<td class="hidden-xs">下周周主要工作</td>
						  	<td data-title="下周主要工作" colspan="10">${daily.nextWeek }</td>
						  </tr>
						  <c:if test="${not empty daily.dailyImages}">
						  <tr >
						  		<td class="hidden-xs">照片</td>
						  		<td data-title="照片" colspan="10">
						  			<c:forEach items="${daily.dailyImages}" var="imgs">
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
					</c:if>
				</table>
				</div>
			</div>
		</section>
	</div>
<script>
jQuery(document).ready(function() {
	var wtypes = $(".wtype");
	$.each(wtypes,function(i,m){
		var ind =$(this).attr("title");
		//设置当然完成量
		var ids =$("#wellnames_"+ind).html();
		if(ids !=""){
			var ida = ids.split(",");
			$("#dayCmp_"+ind).html(ida.length);
		}
		//设置完成率
		var cum=Number($("#cumCmp_"+ind).html());
		var plan=Number($("#designQua_"+ind).html());
		$("#crate_"+ind).html(cum.div(plan));
	})
	if($(window).width()>767){
		$(".preclass").attr("target","_blank");
	}
	//设置img的高度不超过屏幕；
	$(".preimg").css("height",$(window).height()-30);
})
function print(name){
		var options = {
				extraHead: '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>',
				
	            retainAttr : ["id","class","style"], 
	            popTitle  : name+'每日施工日志打印'
			}
		$(".printObj").printArea(options);
	}
</script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
<script src='<c:url value="/resources/js/jquery.printarea.js" />'></script>
