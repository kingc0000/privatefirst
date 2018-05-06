<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<link href='<c:url value="/resources/css/jquery.printarea.css" />' rel="stylesheet" media="all">
<style>
td
    {
        white-space: nowrap;
    }
    .font-18{
    	font-size:18px;
    }
</style>
<div class="row">
	<section class="panel">
	<header class="panel-heading hidden-xs">
        	<span id="edittile">每日报表查看</span>
                <button type="button"  class="btn btn-theme pull-right fa fa-print" onclick="print('${report.name}')">打印</button>
        	</header>
	<div class="panel-body printObj" style="outline: none;overflow-y:scroll;" >
	<script>
		var dea,deo,dec,pa,po,pc,oa,oo,oc,ia,io,ic,ea,eo,ec;
		dea=deo=dec=pa=po=pc=oa=oo=oc=ia=io=ic=ea=eo=ec=0;
		
	</script>
	<table class="table  table-bordered">
		<tr>
			<td class="text-center font-18" colspan="12">上海广联环境岩土工程股份有限公司地下水控制运行每日报表</td>
		</tr>
		<tr>
			<td class="font-18">时间</td><td class="font-18" colspan="11"> ${report.rDate }</td>
		</tr>
		
		<c:if test="${not empty daily}">
			<tr>
				<td class="font-18">天气</td><td class="font-18" colspan="11"><c:if test="${not empty daily.weather }">${daily.weather }</c:if> </td>
			</tr>
		<c:if test="${not empty daily.wellCon}">
			<tr>
				<td rowspan="6">成井工况</td>
				<td></td>
				<td>当日完成数</td>
	   			<td>当日井号</td>
	   			<td>计划完成数</td>
	   			<td>累积完成数</td>
	   			<td>设计数量</td>
	   			<td>完成率</td>
	   			<td colspan="2">破坏情况</td>
	   			<td colspan="2">备注</td>
			</tr>
			<c:forEach items="${daily.wellCon}" var="wc" varStatus="status">
				<tr>
				<td class="wtype" title="${status.index}">
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
				<td id="dayCmp_${status.index}"></td>
				<td id="wellnames_${status.index}">${wc.wellnames}</td>
				<td>${wc.planCmp}</td>
				<td id="cumCmp_${status.index}">${wc.cumCmp}</td>
				<td id="designQua_${status.index}">${wc.designQua}</td>
				<td id="crate_${status.index}"></td>
				<td colspan="2">${wc.dest}</td>
				<td colspan="2">${wc.memo}</td>
        	</tr> 
			</c:forEach>
		</c:if>
		  <tr>
		  	<td>开挖工况</td>
		  	<td colspan="11">${daily.excavation }</td>
		  </tr> 
		  <tr>
		  	<td>疏干运行工况</td>
		  	<td colspan="11">${daily.combDry }</td>
		  </tr> 
		  <tr>
		  	<td>降水运行工况</td>
		  	<td colspan="11">${daily.stepDown }</td>
		  </tr> 
		  <tr>
		  	<td>回灌井运行工况</td>
		  	<td colspan="11">${daily.recharge }</td>
		  </tr> 
		   <tr>
		  	<td>主要风险点</td>
		  	<td colspan="11">${daily.risk }</td>
		  </tr>
		  <tr>
		  	<td>本周主要工作</td>
		  	<td colspan="11">${daily.thisWeek }</td>
		  </tr>
		</c:if>
		  
		   <tr>
		  	<td class="text-center font-18" colspan="12">运行报表</td>
		  </tr> 
		  <c:if test="${not empty report.dewells }">
		  	<tr>
			  	<td>类型</td>
	           	<td>序号</td>  
	           	<td>井号</td>  
	           	<td>上次累积流量</td>  
	           	<td>本次累积流量</td> 
	           	<td>流量m3/h</td>
	           	<td>水位</td> 
	           	<td>状态</td>
	           	<td>目的层</td>
	           	<td colspan="2">状态风险</td> 
	           	<td>备注</td>
		 	 </tr> 
		 	 <c:set var="wellspan" value="${fn:length(report.dewells)+1}" />
		 	 <c:set var="tdspan" value="1" />
		 	 <c:if test="${fn:length(report.dewells)<3}">
		 	 	<c:set var="wellspan" value="4" />
		 	 	<c:set var="tdspan" value="${4-fn:length(report.dewells)}" />
		 	 </c:if>
		 	 <c:forEach items="${report.dewells}" var="dewell" varStatus="status">
		 	 	<tr>
		  			<c:if test="${status.first}"><td rowspan="${wellspan}">疏干井</td></c:if>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${status.index+1 }</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="den">${dewell.name}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${dewell.lastAccu}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${dewell.thisAccu}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="dev">${dewell.flow}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="dewater">${dewell.water}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  				<c:choose>
		  					<c:when test="${dewell.powerStatus==0}">
		  						开启
		  						<script>
		  						deo++;
		  						</script>
		  					</c:when>
		  					<c:when test="${dewell.powerStatus==1}">
		  						关闭
		  						<script>
		  						dec++;
		  						</script>
		  					</c:when>
		  					<c:otherwise>
		  						故障
		  					<script>
		  						dea++;
		  						</script>
		  					</c:otherwise>
		  				</c:choose>
		  			</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  			
                        		<c:forEach items="${fn:split(dewell.precipitation, ',')}" var="v" >
                        			<c:forEach var="precipitation" items="${applicationScope.bd_precipitation}" >
                        				<c:if test="${v== precipitation.value }">
                        				${precipitation.name}
                        				 <c:set var="isDone" value="1" scope="page"></c:set> 
                        				</c:if>
                        			</c:forEach>
                        		</c:forEach>
		  			</td>
		  			<c:choose>
		  				<c:when test="${status.index==0}">
		  					<td>故障数</td>
		  					<td id="tdea"></td>
		  				</c:when>
		  				<c:when test="${status.index==1}">
		  					<td>开启数</td>
		  					<td id="tdeo"></td>
		  				</c:when>
		  				<c:when test="${status.index==2}">
		  					<td>关闭数</td>
		  					<td id="tdec"></td>
		  				</c:when>
		  				<c:otherwise>
		  					<td></td>
		  					<td></td>
		  				</c:otherwise>
		  			</c:choose>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  				<c:choose>
		  					<c:when test="${not empty dewell.thisAccu}">
		  						人工监测
		  					</c:when>
		  					<c:otherwise>
		  						自动化
		  					</c:otherwise>
		  				</c:choose>
		  			</td>
		  		</tr> 
		 	 	
		 	 </c:forEach>
		 	 <c:if test="${fn:length(report.dewells)<3}">
		 	 	<c:forEach begin="1" end="${tdspan-1}" step="1" varStatus="status">
		 	 		<tr>
		 	 			<c:choose>
	 	 					<c:when test="${status.last}">
	 	 						<td>关闭数</td>
	  							<td id="tdec"></td>
	 	 					</c:when>
	 	 					<c:otherwise>
	 	 						<td>开启数</td>
		  						<td id="tdeo"></td>
	 	 					</c:otherwise>
	 	 				</c:choose>
		 	 		</tr>
		 	 	</c:forEach>
		 	 </c:if>
		  	<tr>
		  		<td colspan="11">
		  		<div class="row">
		  			<div class="col-sm-9 col-xs-9" ><canvas id="deChart"></canvas><div id="deChartimg"></div></div>
		  			<div class="col-sm-3 col-xs-3"><canvas id="deBin" ></canvas></div>
		  		</div>
		  		</td>
		  	</tr>
		  </c:if> 
		  <c:if test="${not empty report.iwells }">
		  	<tr>
			  	<td>类型</td>
	           	<td>序号</td>  
	           	<td>井号</td>  
	           	<td>上次累积回灌量</td>  
	           	<td>本次累积回灌量</td> 
	           	<td>回灌流量m3/h</td>
	           	<td>井内水位</td> 
	           	<td>状态</td>
	           	<td>目的层</td>
	           	<td colspan="2">状态风险</td> 
	           	<td>备注</td>
		 	 </tr> 
		 	 <c:set var="wellspan" value="${fn:length(report.iwells)+1}" />
		 	 <c:set var="tdspan" value="1" />
		 	 <c:if test="${fn:length(report.iwells)<3}">
		 	 	<c:set var="wellspan" value="4" />
		 	 	<c:set var="tdspan" value="${4-fn:length(report.iwells)}" />
		 	 </c:if>
		 	 <c:forEach items="${report.iwells}" var="dewell" varStatus="status">
		 	 	<tr>
		  			<c:if test="${status.first}"><td rowspan="${wellspan}">回灌井</td></c:if>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${status.index+1 }</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="in">${dewell.name}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${dewell.lastAccu}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${dewell.thisAccu}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="iv">${dewell.flow}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${dewell.pressure}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  				<c:choose>
		  					<c:when test="${dewell.powerStatus==0}">
		  						开启
		  						<script>
		  						io++;
		  						</script>
		  					</c:when>
		  					<c:when test="${dewell.powerStatus==1}">
		  						关闭
		  						<script>
		  						ic++;
		  						</script>
		  					</c:when>
		  					<c:otherwise>
		  					故障
		  					<script>
		  						ia++;
		  						</script>
		  					</c:otherwise>
		  				</c:choose>
		  			</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  			
                        		<c:forEach items="${fn:split(dewell.precipitation, ',')}" var="v" >
                        			<c:forEach var="precipitation" items="${applicationScope.bd_precipitation}" >
                        				<c:if test="${v== precipitation.value }">
                        				${precipitation.name}
                        				 <c:set var="isDone" value="1" scope="page"></c:set> 
                        				</c:if>
                        			</c:forEach>
                        		</c:forEach>
		  			</td>
		  			<c:choose>
		  				<c:when test="${status.index==0}">
		  					<td>故障数</td>
		  					<td id="tia"></td>
		  				</c:when>
		  				<c:when test="${status.index==1}">
		  					<td>开启数</td>
		  					<td id="tio"></td>
		  				</c:when>
		  				<c:when test="${status.index==2}">
		  					<td>关闭数</td>
		  					<td id="tic"></td>
		  				</c:when>
		  				<c:otherwise>
		  					<td></td>
		  					<td></td>
		  				</c:otherwise>
		  			</c:choose>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  				<c:choose>
		  					<c:when test="${not empty dewell.thisAccu}">
		  						人工监测
		  					</c:when>
		  					<c:otherwise>
		  						自动化
		  					</c:otherwise>
		  				</c:choose>
		  			</td>
		  		</tr> 
		 	 </c:forEach>
		 	 <c:if test="${fn:length(report.iwells)<3}">
		 	 	<c:forEach begin="1" end="${tdspan-1}" step="1" varStatus="status">
		 	 		<tr>
		 	 			<c:choose>
	 	 					<c:when test="${status.last}">
	 	 						<td>关闭数</td>
	  							<td id="tic"></td>
	 	 					</c:when>
	 	 					<c:otherwise>
	 	 						<td>开启数</td>
		  						<td id="tio"></td>
	 	 					</c:otherwise>
	 	 				</c:choose>
		 	 		</tr>
		 	 	</c:forEach>
		 	 </c:if>
		  	<tr>
		  		<td colspan="11">
		  			<div class="row">
		  			<div class="col-sm-9 col-xs-9"><canvas id="iChart"></canvas></div>
		  			<div class="col-sm-3 col-xs-3"><canvas id="iBin" ></canvas></div>
		  		</div>
		  		</td>
		  	</tr>
		  </c:if> 
		  <c:if test="${not empty report.pwells }">
		  	<tr>
			  	<td>类型</td>
	           	<td>序号</td>  
	           	<td>井号</td>  
	           	<td>上次累积流量</td>  
	           	<td>本次累积流量</td> 
	           	<td>流量m3/h</td>
	           	<td>水位</td> 
	           	<td>状态</td>
	           	<td>目的层</td>
	           	<td colspan="2">状态风险</td> 
	           	<td>备注</td>
		 	 </tr> 
		 	 <c:set var="wellspan" value="${fn:length(report.pwells)+1}" />
		 	 <c:set var="tdspan" value="1" />
		 	 <c:if test="${fn:length(report.pwells)<3}">
		 	 	<c:set var="wellspan" value="4" />
		 	 	<c:set var="tdspan" value="${4-fn:length(report.pwells)}" />
		 	 </c:if>
		 	 <c:forEach items="${report.pwells}" var="dewell" varStatus="status">
		 	 	<tr>
		  			<c:if test="${status.first}"><td rowspan="${wellspan}">降水井</td></c:if>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${status.index+1 }</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="pn">${dewell.name}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${dewell.lastAccu}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${dewell.thisAccu}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="pv">${dewell.flow}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="pwater">${dewell.water}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  				<c:choose>
		  					<c:when test="${dewell.powerStatus==0}">
		  					开启
		  						<script>
		  						po++;
		  						</script>
		  					</c:when>
		  					<c:when test="${dewell.powerStatus==1}">
		  					关闭
		  						<script>
		  						pc++;
		  						</script>
		  					</c:when>
		  					<c:otherwise>
		  					故障
		  					<script>
		  						pa++;
		  						</script>
		  					</c:otherwise>
		  				</c:choose>
		  			</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  			
                        		<c:forEach items="${fn:split(dewell.precipitation, ',')}" var="v" >
                        			<c:forEach var="precipitation" items="${applicationScope.bd_precipitation}" >
                        				<c:if test="${v== precipitation.value }">
                        				${precipitation.name}
                        				 <c:set var="isDone" value="1" scope="page"></c:set> 
                        				</c:if>
                        			</c:forEach>
                        		</c:forEach>
		  			</td>
		  			<c:choose>
		  				<c:when test="${status.index==0}">
		  					<td>故障数</td>
		  					<td id="tpa"></td>
		  				</c:when>
		  				<c:when test="${status.index==1}">
		  					<td>开启数</td>
		  					<td id="tpo"></td>
		  				</c:when>
		  				<c:when test="${status.index==2}">
		  					<td>关闭数</td>
		  					<td id="tpc"></td>
		  				</c:when>
		  				<c:otherwise>
		  					<td></td>
		  					<td></td>
		  				</c:otherwise>
		  			</c:choose>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  				<c:choose>
		  					<c:when test="${not empty dewell.thisAccu}">
		  						人工监测
		  					</c:when>
		  					<c:otherwise>
		  						自动化
		  					</c:otherwise>
		  				</c:choose>
		  			</td>
		  		</tr> 
		 	 </c:forEach>
		 	 <c:if test="${fn:length(report.pwells)<3}">
		 	 	<c:forEach begin="1" end="${tdspan-1}" step="1" varStatus="status">
		 	 		<tr>
		 	 			<c:choose>
	 	 					<c:when test="${status.last}">
	 	 						<td>关闭数</td>
	  							<td id="tpc"></td>
	 	 					</c:when>
	 	 					<c:otherwise>
	 	 						<td>开启数</td>
		  						<td id="tpo"></td>
	 	 					</c:otherwise>
	 	 				</c:choose>
		 	 		</tr>
		 	 	</c:forEach>
		 	 </c:if>
		  	<tr>
		  		<td colspan="11">
		  			<div class="row">
		  			<div class="col-sm-9 col-xs-9"><canvas id="pChart"></canvas></div>
		  			<div class="col-sm-3 col-xs-3"><canvas id="pBin" ></canvas></div>
		  		</div>
		  		</td>
		  	</tr>
		  </c:if> 
		  <c:if test="${not empty report.owells }">
		  	<tr>
			  	<td>类型</td>
	           	<td>序号</td>  
	           	<td>井号</td>  
	           	<td>水位</td>  
	           	<td>水位下限</td> 
	           	<td>水位上限</td>
	           	<td>水温</td> 
	           	<td>状态</td>
	           	<td>目的层</td>
	           	<td colspan="3">状态风险</td> 
		 	 </tr> 
		 	 <c:set var="wellspan" value="${fn:length(report.owells)+1}" />
		 	 <c:set var="tdspan" value="1" />
		 	 <c:if test="${fn:length(report.owells)<3}">
		 	 	<c:set var="wellspan" value="4" />
		 	 	<c:set var="tdspan" value="${4-fn:length(report.owells)}" />
		 	 </c:if>
		 	 <c:forEach items="${report.owells}" var="dewell" varStatus="status">
		 	 	<tr>
		  			<c:if test="${status.first}"><td rowspan="${wellspan}">观测井</td></c:if>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${status.index+1 }</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="on">${dewell.name}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="ov">${dewell.water}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="ovdown">${dewell.waterDwon}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="ovup">${dewell.waterThreshold}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${dewell.temperature}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  				<c:choose>
		  					<c:when test="${dewell.powerStatus==0}">
		  						开启
		  						<script>
		  						oo++;
		  						</script>
		  					</c:when>
		  					<c:when test="${dewell.powerStatus==1}">
		  					关闭
		  						<script>
		  						oc++;
		  						</script>
		  					</c:when>
		  					<c:otherwise>
		  					故障
		  					<script>
		  						oa++;
		  						</script>
		  					</c:otherwise>
		  				</c:choose>
		  			</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  			
                        		<c:forEach items="${fn:split(dewell.precipitation, ',')}" var="v" >
                        			<c:forEach var="precipitation" items="${applicationScope.bd_precipitation}" >
                        				<c:if test="${v== precipitation.value }">
                        				${precipitation.name}
                        				 <c:set var="isDone" value="1" scope="page"></c:set> 
                        				</c:if>
                        			</c:forEach>
                        		</c:forEach>
		  			</td>
		  			<c:choose>
		  				<c:when test="${status.index==0}">
		  					<td>故障数</td>
		  					<td id="toa" colspan="3"></td>
		  				</c:when>
		  				<c:when test="${status.index==1}">
		  					<td>开启数</td>
		  					<td id="too" colspan="3"></td>
		  				</c:when>
		  				<c:when test="${status.index==2}">
		  					<td>关闭数</td>
		  					<td id="toc" colspan="3"></td>
		  				</c:when>
		  				<c:otherwise>
		  					<td></td>
		  					<td colspan="3"></td>
		  				</c:otherwise>
		  			</c:choose>
		  		</tr> 
		 	 </c:forEach>
		 	 <c:if test="${fn:length(report.owells)<3}">
		 	 	<c:forEach begin="1" end="${tdspan-1}" step="1" varStatus="status">
		 	 		<tr>
		 	 			<c:choose>
	 	 					<c:when test="${status.last}">
	 	 						<td>关闭数</td>
	  							<td id="toc" colspan="3"></td>
	 	 					</c:when>
	 	 					<c:otherwise>
	 	 						<td>开启数</td>
		  						<td id="too" colspan="3"></td>
	 	 					</c:otherwise>
	 	 				</c:choose>
		 	 		</tr>
		 	 	</c:forEach>
		 	 </c:if>
		  	<tr>
		  		<td colspan="11">
		  			<div class="row">
		  			<div class="col-sm-9 col-xs-9"><canvas id="oChart"></canvas></div>
		  			<div class="col-sm-3 col-xs-3"><canvas id="oBin" ></canvas></div>
		  		</div>
		  		</td>
		  	</tr>
		  </c:if> 
		   <c:if test="${not empty report.ewells }">
		  	<tr>
			  	<td>类型</td>
	           	<td>序号</td>  
	           	<td>测点号</td>  
	           	<td>上次测量值</td>  
	           	<td>本次测量值</td> 
	           	<td>变形值</td>
	           	<td>报警值</td> 
	           	<td>状态</td>
	           	<td colspan="4">状态风险</td> 
		 	 </tr> 
		 	 <c:set var="wellspan" value="${fn:length(report.ewells)+1}" />
		 	 <c:set var="tdspan" value="1" />
		 	 <c:if test="${fn:length(report.ewells)<3}">
		 	 	<c:set var="wellspan" value="4" />
		 	 	<c:set var="tdspan" value="${4-fn:length(report.ewells)}" />
		 	 </c:if>
		 	 <c:forEach items="${report.ewells}" var="dewell" varStatus="status">
		 	 	<tr>
		  			<c:if test="${status.first}"><td rowspan="${wellspan}">环境监测点</td></c:if>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${status.index+1 }</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="en">${dewell.name}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>${dewell.lastData}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="ev">${dewell.data}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="evchange">${dewell.data-dewell.lastData}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if> class="evup">${dewell.threshold}</td>
		  			<td <c:if test="${status.last}">rowspan="${tdspan}"</c:if>>
		  				<c:choose>
		  					<c:when test="${dewell.powerStatus==0}">
		  						开启
		  						<script>
		  						eo++;
		  						</script>
		  					</c:when>
		  					<c:when test="${dewell.powerStatus==1}">
		  					关闭
		  						<script>
		  						ec++;
		  						</script>
		  					</c:when>
		  					<c:otherwise>
		  					故障
		  					<script>
		  						ea++;
		  						</script>
		  					</c:otherwise>
		  				</c:choose>
		  			</td>
		  			<c:choose>
		  				<c:when test="${status.index==0}">
		  					<td>故障数</td>
		  					<td id="tea" colspan="4"></td>
		  				</c:when>
		  				<c:when test="${status.index==1}">
		  					<td>开启数</td>
		  					<td id="teo" colspan="4"></td>
		  				</c:when>
		  				<c:when test="${status.index==2}">
		  					<td>关闭数</td>
		  					<td id="tec" colspan="4"></td>
		  				</c:when>
		  				<c:otherwise>
		  					<td></td>
		  					<td colspan="4"></td>
		  				</c:otherwise>
		  			</c:choose>
		  		</tr>
		 	 </c:forEach>
		 	 <c:if test="${fn:length(report.ewells)<3}">
		 	 	<c:forEach begin="1" end="${tdspan-1}" step="1" varStatus="status">
		 	 		<tr>
		 	 			<c:choose>
	 	 					<c:when test="${status.last}">
	 	 						<td>关闭数</td>
	  							<td id="tec" colspan="4"></td>
	 	 					</c:when>
	 	 					<c:otherwise>
	 	 						<td>开启数</td>
		  						<td id="teo" colspan="4"></td>
	 	 					</c:otherwise>
	 	 				</c:choose>
		 	 		</tr>
		 	 	</c:forEach>
		 	 </c:if>
		  	<tr>
		  		<td colspan="11">
		  		<div class="row">
		  			<div class="col-sm-9 col-xs-9"><canvas id="eChart"></canvas></div>
		  			<div class="col-sm-3 col-xs-3"><canvas id="eBin" ></canvas></div>
		  		</div>
		  		</td>
		  	</tr>
		  </c:if> 	
		</table>
	
		<div class="pull-left"> 项目名称&nbsp; &nbsp;${report.name}&nbsp; &nbsp; 项目负责人&nbsp; &nbsp;${report.owner} </div>
		<div class="pull-right">上海广联环境岩土工程股份有限公司&nbsp; &nbsp; <fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:MM:SS"/></div>
		</div>
		</section>
	
</div>
<script src='<c:url value="/resources/js/Chart.min.js"/>'></script> 
<script src='<c:url value="/resources/js/jquery.printarea.js" />'></script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
<script>
$("#pictitle").html("每日报表查看");
var welln = ["de","i", "p","o","e"];
var wells = ["a","o","c"];
	jQuery(document).ready(function() {
		
		
		$.each(welln,function(i,n){
			$.each(wells,function(j,s){
				var v =n+s;
			    $("#t"+n+s).html(eval(v));
			  });
		  });
		$.each(welln,function(i,n){
			showChar(n);
		});
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
	});
	
	/**
	* 画出数据图
	* 对于 疏干井de、回灌井i、降水井p，采用柱状图表示
	* 对于 观测井o、环境监测e采用曲线图表示
	**/
	function showChar(nam){
		console.log(nam);
		var ctx = $("#"+nam+"Chart");
		if(ctx.length<1)return;
		var labels = new Array();
		var datas = new Array();
		$("."+nam+"n").each(function(){
			labels[labels.length]=$(this).html();
		  });
		$("."+nam+"v").each(function(){
			datas[datas.length]=$(this).html();
		  }); 
		var data, datatype;
		if (nam=='o') {
			datatype = "line";
			var datas_up = new Array(); //水位上限
			var datas_down = new Array(); //水位下限
			
			$("."+nam+"vup").each(function(){
				datas_up[datas_up.length]=$(this).html();
			  }); 
			$("."+nam+"vdown").each(function(){
				datas_down[datas_down.length]=$(this).html();
			  }); 
			
			data = {
				    labels: labels,
				    datasets: [
				        {
				            label: "当前水位/m",
				            tension: 0,
				            data:datas,
				            backgroundColor: 'rgba(255, 159, 64, 0.5)',
				            borderColor: 'rgb(255, 159, 64)',
				            fill: false
				        },
				        {
				            label: "水位上限/m",
				            tension: 0,
				            data: datas_up,
				            backgroundColor: 'rgba(54, 162, 235, 0.5)',
				            borderColor: 'rgb(54, 162, 235)',
				            fill: false
				        },
				        {
				            label: "水位下限/m",
				            tension: 0,
				            data: datas_down,
				            backgroundColor: 'rgba(75, 192, 192, 0.5)',
				            borderColor: 'rgb(75, 192, 192)',
				            fill: false
				        }
				    ]
				};
		} else if(nam=='e') { //环境监测
			datatype = "line";
			var datas_up = new Array(); //报警值
			var datas_change = new Array(); //变形值
			
			$("."+nam+"vup").each(function(){
				datas_up[datas_up.length]=$(this).html();
			  }); 
			$("."+nam+"vchange").each(function(){
				datas_change[datas_change.length]=$(this).html();
			  }); 
			data = {
				    labels: labels,
				    datasets: [
				        {
				            label: "本次测量值",
				            tension: 0,
				            data:datas,
				            backgroundColor: 'rgba(255, 159, 64, 0.5)',
				            borderColor: 'rgb(255, 159, 64)',
				            fill: false
				        },
				        {
				            label: "报警值",
				            tension: 0,
				            data: datas_up,
				            backgroundColor: 'rgba(54, 162, 235, 0.5)',
				            borderColor: 'rgb(54, 162, 235)',
				            fill: false
				        },
				        {
				            label: "变形值",
				            tension: 0,
				            data: datas_change,
				            backgroundColor: 'rgba(75, 192, 192, 0.5)',
				            borderColor: 'rgb(75, 192, 192)',
				            fill: false
				        }
				    ]
				};
		} else {
			datatype = "bar";
			
			data = {
				    labels: labels,
				    datasets: [
				        {
				            label: "流量m3/h",
				            borderWidth: 1,
				            data:datas,
				            backgroundColor: 'rgba(255, 159, 64, 0.5)',
				            borderColor: 'rgb(255, 159, 64)'
				        }				        
				    ]
				};
			//如果有水位采集数据
			if($("."+nam+"water").length>0) {
				var waterdatas = new Array(); //水位
				$("."+nam+"water").each(function(){
					waterdatas[waterdatas.length]=$(this).html();
				  });	
				var _data = {
		            label: "水位m",
		            borderWidth: 1,
		            data:waterdatas,
		            backgroundColor: 'rgba(54, 162, 235, 0.5)',
		            borderColor: 'rgb(54, 162, 235)'
		        }
				data.datasets.push(_data);
			}	
		}
		var myBarChart = new Chart(ctx, {
		    type: datatype,
		    data: data,
		    options: {
		    	tooltips: {
                    mode: 'index',
                    intersect: true
                },
		        scales: {
		            xAxes: [{
		                stacked: true
		            }],
		            falsegridLines: {
	                    display: false,
	                    drawBorder: false
	                },
	                responsive: false,
		            yAxes: [{
		            	type: "linear", // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
	                    display: true,
	                    ticks: {
		                    beginAtZero:true
		                },
	                    position: "left"}]
		        }
		    }
		});
		var bin = new Array();
		$.each(wells,function(j,s){
			bin[bin.length]=$("#t"+nam+s).html();
		  });
		var ctx1 = $("#"+nam+"Bin");;
		var myChart1 = new Chart(ctx1, {type: 'pie',
			data: {
		        labels: ["故障数", "开启数", "关闭数"],
		        datasets: [{
		            data: bin,
		            backgroundColor: [
		                              "#F7464A",
		                              "#46BFBD",
		                              "#FDB45C"
		                          ],
	                 hoverBackgroundColor: [
	                                        "#FF5A5E",
	                                        "#5AD3D1",
	                                        "#FFC870"
	                                    ]               
		        }]
		    },
		    options: {
		    	responsive: true
		    }
		});
		ctx.height=ctx1.height();
	}
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
	            popTitle  : name+'每日报表打印'
			}
		$(".printObj").printArea(options,cavas);
	}
</script>	
