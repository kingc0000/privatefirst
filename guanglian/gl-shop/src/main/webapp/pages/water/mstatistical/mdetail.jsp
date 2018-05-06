<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<link href='<c:url value="/resources/css/jquery.printarea.css" />'
	rel="stylesheet" media="all">
<style>
td {
	white-space: nowrap;
}

.font-18 {
	font-size: 18px;
}
</style>
<div class="row">
	<section class="panel">
		<header class="panel-heading hidden-xs">
			<span id="edittile">统计报表查看</span>
			<button type="button" class="btn btn-theme pull-right fa fa-print"
				onclick="print('统计报表')">打印</button>
		</header>
		<div class="panel-body printObj"
			style="outline: none; overflow-y: scroll;">

			<table class="table  table-bordered">
				<tr>
					<td class="text-center font-18" colspan="12">统计报表查看</td>
				</tr>
				<tr>
					<td style="border-right-style: none">编号：</td>
					<td style="border-right-style: none; border-left-style: none">${msr.id}</td>
					<td colspan="2"
						style="border-right-style: none; border-left-style: none"></td>
					<td style="border-right-style: none; border-left-style: none">天气：</td>
					<td style="border-right-style: none; border-left-style: none">${msr.mDaily.weather}</td>
					<td colspan="2"
						style="border-right-style: none; border-left-style: none"></td>
					<td style="border-right-style: none; border-left-style: none">日期：</td>
					<td style="border-right-style: none; border-left-style: none">${msr.mDaily.datec}</td>
					<td style="border-left-style: none; border-right-style: none"></td>
					<td style="border-left-style: none"></td>
				</tr>
				<tr>
					<td style="text-align: center; line-height: 80px" rowspan="2"
						colspan="2">监测项目</td>
					<td colspan="2" style="text-align: center; line-height: 20px">本次最大变化量</td>
					<td colspan="2" style="text-align: center; line-height: 20px">总累计最大变化量</td>
					<td colspan="2" style="text-align: center; line-height: 20px">开挖前累计最大变化量</td>
					<td colspan="2" style="text-align: center; line-height: 20px">警戒值</td>
					<td rowspan="2" colspan="2"
						style="text-align: center; line-height: 80px">备注</td>
				</tr>
				<tr>
					<td style="text-align: center; line-height: 20px">点位</td>
					<td style="text-align: center; line-height: 20px">数值/mm</td>
					<td style="text-align: center; line-height: 20px">点位</td>
					<td>数值/mm</td>
					<td style="text-align: center; line-height: 20px">点位</td>
					<td style="text-align: center; line-height: 20px">数值/mm</td>
					<td style="text-align: center; line-height: 20px">日变量</td>
					<td style="text-align: center; line-height: 20px">累计值</td>
				</tr>


				<c:set var="isfirst" value="0"/> 
				<c:set var="linecount" value="0"/> 
				<c:forEach items="${msr.pmanagers}" var="wc">
					<c:if test="${wc.pType=='WaterLine'}">
						<c:set var="linecount" value="${linecount+1}"/> 
					</c:if>
				</c:forEach>
				<c:forEach items="${msr.pmanagers}" var="wc" varStatus="status">
					<c:choose>
						<c:when test="${wc.pType=='WaterLine'}">
							<c:forEach items="${bts}" var="bt" >
								<c:if test="${fn:startsWith(wc.pointNo,bt.value)}">
									<tr>
										<c:if test="${isfirst==0}">
											<c:set var="isfirst" value="1"/> 
											<td style="text-align: center; line-height: 100px" rowspan="${linecount}">周围管线竖向位移</td>
										</c:if>
										<td style="text-align: center">${bt.name}</td>
										<td style="text-align: center">${wc.pointNo}</td>
										<td style="text-align: center">${wc.curMaxVar eq null?"":wc.curMaxVar}</td>
										<td style="text-align: center">${wc.tpointNo}</td>
										<td style="text-align: center">${wc.totalMaxVar eq null?"":wc.totalMaxVar}</td>
										<td style="text-align: center">${wc.epointNo}</td>
										<td style="text-align: center">${wc.earlyMaxVar eq null?"":wc.earlyMaxVar}</td>
										<td style="text-align: center">${wc.dailyVar eq null?"":wc.dailyVar}</td>
										<td style="text-align: center">${wc.totalValue eq null?"": wc.totalValue}</td>
										<td colspan="2" style="text-align: center">${wc.memo}</td>
									</tr>
								</c:if>
							</c:forEach>
						</c:when>
					</c:choose>
				</c:forEach>

				<c:forEach items="${msr.pmanagers}" var="jz" varStatus="status">
					<c:if test="${jz.pType!='WaterLine' && jz.pType!='SupAxial'}">
						<tr>
							<td colspan="2" style="text-align: center; line-height: 20px">
							<c:forEach items="${mtypes}" var="mtype" >
								<c:if test="${mtype.mtype==jz.pType}">
									${mtype.name}竖向位移
								</c:if>
							</c:forEach>
							</td>
							<td style="text-align: center">${jz.pointNo}</td>
							<td style="text-align: center">${jz.curMaxVar eq null?"":jz.curMaxVar}</td>
							<td style="text-align: center">${jz.tpointNo}</td>
							<td style="text-align: center">${jz.totalMaxVar eq null?"":jz.totalMaxVar}</td>
							<td style="text-align: center">${jz.epointNo}</td>
							<td style="text-align: center">${jz.earlyMaxVar eq null?"":jz.earlyMaxVar}</td>
							<td style="text-align: center">${jz.dailyVar eq null?"":jz.dailyVar}</td>
							<td style="text-align: center">${jz.totalValue eq null?"":jz.totalValue}</td>
							<td colspan="2" style="text-align: center">${jz.memo}</td>
						</tr>
					</c:if>
				</c:forEach>
				<c:forEach items="${msr.pmanagers}" var="jz" varStatus="status">
					<c:if test="${jz.pType=='SupAxial' }">
						<tr>
							<td colspan="2" rowspan="2" style="text-align: center; line-height: 20px">支撑轴力</td>
							<td style="text-align: center" colspan="2"> 本次最大轴力值/KN</td>
							<td style="text-align: center" colspan="4"> 本次最大轴变化量/KN</td>
							<td style="text-align: center" colspan="4" rowspan="2"></td>
						</tr>
						<tr>
							<td>${jz.tpointNo}</td>
							<td>${jz.totalMaxVar eq null?"":jz.curMaxVar}</td>
							<td>${jz.pointNo}</td>
							<td>${jz.curMaxVar eq null?"":jz.curMaxVar}</td>
						</tr>
					</c:if>
				</c:forEach>
				<tr>
					<td style="text-align: center; line-height: 80px">工程状况</td>

					<td colspan="11">${msr.mDaily.note}</td>
				</tr>
				<tr>
					<td style="text-align: center; line-height: 80px">监测点情况</td>

					<td colspan="11">${msr.mDaily.pointDesc}</td>
				</tr>
				<tr>
					<td style="text-align: center; line-height: 80px">监测点意见</td>

					<td colspan="11">${msr.mDaily.conclusion}</td>
				</tr>
			</table>
		</div>
	</section>
</div>
<script src='<c:url value="/resources/js/jquery.printarea.js" />'></script>
<script>
	$("#pictitle").html("统计报表查看");
	function print(name){
		var cavas=new Array();
		var canvas =$("canvas");
		$.each(canvas,function(j,s){
			var obj = {
					id:$(this).attr("id") ,
					url:s.toDataURL()
			};
            cavas[cavas.length]=obj;
		  });
		var options = {
				extraHead: '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>',
				mode:"iframe",
	            retainAttr : ["id","class","style"], 
	            popTitle  : name+'统计报表打印'
			}
		$(".printObj").printArea(options,cavas);
	}
</script>
