<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<script>
	$("#pictitle").html("成井日志");
</script>
<style>
td
    {
        white-space: nowrap;
    }
</style>
<link href='<c:url value="/resources/css/jquery.printarea.css" />' rel="stylesheet" media="all">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<div class="row" >
	<div class="col-md-12">
        <section class="panel">
        	<header class="panel-heading hidden-xs">
        	<span id="edittile">成井日志</span>
                <button type="button"  class="btn btn-theme pull-right fa fa-print" onclick="print('${cname}')">打印</button>
                <button type="button"  class="btn btn-theme pull-right fa fa-download" onclick="exportexcel()">导出</button>
        	</header>
        	<div class="panel-body printObj" style="outline: none;overflow-y:scroll;" id="no-more-tables" >
        		<table class="table table-bordered ">
        			<thead>
                          <tr class="text-center">
                          	<td rowspan="2">类型</td>
                          	<td colspan="${fn:length(pwells)}">降水井</td>  
                          	<td colspan="${fn:length(dewells)}">疏干井</td>  
                          	<td colspan="${fn:length(owells)}">观测井</td>  
                          	<td colspan="${fn:length(iwells)}">回灌井</td>  
                    	 </tr>
                    	 <tr>
                    	 	<c:forEach items="${pwells}" var="pwell">
                    	 		<td>${pwell.name}</td>
                    	 	</c:forEach>
                    	 	<c:forEach items="${dewells}" var="dewell">
                    	 		<td>${dewell.name}</td>
                    	 	</c:forEach>
                    	 	<c:forEach items="${owells}" var="oewell">
                    	 		<td>${oewell.name}</td>
                    	 	</c:forEach>
                    	 	<c:forEach items="${iwells}" var="iewell">
                    	 		<td>${iewell.name}</td>
                    	 	</c:forEach>
                    	 </tr>
                     </thead> 
                     <tbody >
                     <tr>
                     	<td data-title="类型">降水目的层</td>
                     	<c:forEach items="${pwells}" var="pwell">
                     		<td data-title="${pwell.name}">
                     			<c:set value="${ fn:split(pwell.pointInfo.precipitation, ',') }" var="ps" />
                     				<c:forEach items="${ps}" var="p">
                     					<c:forEach	items="${pres}" var="pre">
                     						<c:if test="${pre.value==p }">
                     							${pre.name}
                     						</c:if>
                     					</c:forEach>
                     				</c:forEach>
                     		</td>
                     	</c:forEach>
                     	<c:forEach items="${dewells}" var="dewell">
                     		<td data-title="${dewell.name}">
                     			<c:set value="${ fn:split(dewell.pointInfo.precipitation, ',') }" var="ps" />
                     				<c:forEach items="${ps}" var="p">
                     					<c:forEach	items="${pres}" var="pre">
                     						<c:if test="${pre.value==p }">
                     							${pre.name}
                     						</c:if>
                     					</c:forEach>
                     				</c:forEach>
                     		</td>
                     	</c:forEach>
                     	<c:forEach items="${owells}" var="oewell">
                     		<td data-title="${oewell.name}">
                     			<c:set value="${ fn:split(oewell.pointInfo.precipitation, ',') }" var="ps" />
                     				<c:forEach items="${ps}" var="p">
                     					<c:forEach	items="${pres}" var="pre">
                     						<c:if test="${pre.value==p }">
                     							${pre.name}
                     						</c:if>
                     					</c:forEach>
                     				</c:forEach>
                     		</td>
                     	</c:forEach>
                     	<c:forEach items="${iwells}" var="iewell">
                     		<td data-title="${iewell.name}">
                     			<c:set value="${ fn:split(iewell.pointInfo.precipitation, ',') }" var="ps" />
                     				<c:forEach items="${ps}" var="p">
                     					<c:forEach	items="${pres}" var="pre">
                     						<c:if test="${pre.value==p }">
                     							${pre.name}
                     						</c:if>
                     					</c:forEach>
                     				</c:forEach>
                     		</td>
                     	</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">孔径/mm</td>
                   		<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.aperture}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.aperture}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.aperture}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.aperture}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">管井/mm</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.tubeWell}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.tubeWell}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.tubeWell}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.tubeWell}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">井深/m</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.deepWell}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.deepWell}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.deepWell}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.deepWell}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">滤管长度</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.fTubleLgn}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.fTubleLgn}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.fTubleLgn}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.fTubleLgn}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">滤料回填量/t</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.backfillVol}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.backfillVol}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.backfillVol}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.backfillVol}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">粘土球回填量/t</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.clayBackfill}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.clayBackfill}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.clayBackfill}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.clayBackfill}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">井口是否回填</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">
	                   			<c:choose>
	                   				<c:when test="${pwell.pointInfo.backFill==1}">
	                   					是
	                   				</c:when>
	                   				<c:otherwise>
	                   					否
	                   				</c:otherwise>
	                   			</c:choose>
                   			</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">
	                   			<c:choose>
	                   				<c:when test="${dewell.pointInfo.backFill==1}">
	                   					是
	                   				</c:when>
	                   				<c:otherwise>
	                   					否
	                   				</c:otherwise>
	                   			</c:choose>
                   			</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">
	                   			<c:choose>
	                   				<c:when test="${oewell.pointInfo.backFill==1}">
	                   					是
	                   				</c:when>
	                   				<c:otherwise>
	                   					否
	                   				</c:otherwise>
	                   			</c:choose>
                   			</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">
	                   			<c:choose>
	                   				<c:when test="${iewell.pointInfo.backFill==1}">
	                   					是
	                   				</c:when>
	                   				<c:otherwise>
	                   					否
	                   				</c:otherwise>
	                   			</c:choose>
                   			</td>
                   		</c:forEach>
                     </tr>
                      <tr>
                     	<td data-title="类型">洗井周期</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.washWC}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.washWC}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.washWC}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.washWC}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">单井涌水量/m3/h</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.sWellFlow}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.sWellFlow}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.sWellFlow}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.sWellFlow}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">动水位/m</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.moveingWater}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.moveingWater}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.moveingWater}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.moveingWater}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">初始水位/m</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.initialWater}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.initialWater}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.initialWater}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.initialWater}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">井号现场标识</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.poundSite}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.poundSite}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.poundSite}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.poundSite}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">是否有异常</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">
	                   			<c:choose>
	                   				<c:when test="${pwell.pointInfo.exception==1}">
	                   					是
	                   				</c:when>
	                   				<c:otherwise>
	                   					否
	                   				</c:otherwise>
	                   			</c:choose>
                   			</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">
	                   			<c:choose>
	                   				<c:when test="${dewell.pointInfo.exception==1}">
	                   					是
	                   				</c:when>
	                   				<c:otherwise>
	                   					否
	                   				</c:otherwise>
	                   			</c:choose>
                   			</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">
	                   			<c:choose>
	                   				<c:when test="${oewell.pointInfo.exception==1}">
	                   					是
	                   				</c:when>
	                   				<c:otherwise>
	                   					否
	                   				</c:otherwise>
	                   			</c:choose>
                   			</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">
	                   			<c:choose>
	                   				<c:when test="${iewell.pointInfo.exception==1}">
	                   					是
	                   				</c:when>
	                   				<c:otherwise>
	                   					否
	                   				</c:otherwise>
	                   			</c:choose>
                   			</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">成井时间</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}"><fmt:formatDate value="${pwell.pointInfo.wellTime}" /></td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}"><fmt:formatDate value="${dewell.pointInfo.wellTime}" /></td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}"><fmt:formatDate value="${oewell.pointInfo.wellTime}" /></td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}"><fmt:formatDate value="${iewell.pointInfo.wellTime}" /></td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">成井负责人</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.cPerson}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.cPerson}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.cPerson}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.cPerson}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">验收管理人员</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.acceptance}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.acceptance}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.acceptance}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.acceptance}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">封井时间</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}"><fmt:formatDate value="${pwell.pointInfo.closure}" /></td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}"><fmt:formatDate value="${dewell.pointInfo.closure}" /></td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}"><fmt:formatDate value="${oewell.pointInfo.closure}" /></td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}"><fmt:formatDate value="${iewell.pointInfo.closure}" /></td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">封井措施</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.cmeasures}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.cmeasures}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.cmeasures}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.cmeasures}</td>
                   		</c:forEach>
                     </tr>
                     <tr>
                     	<td data-title="类型">封井验收人</td>
                     	<c:forEach items="${pwells}" var="pwell">
                   			<td data-title="${pwell.name}">${pwell.pointInfo.sAcceptance}</td>
                   		</c:forEach>
                   		<c:forEach items="${dewells}" var="dewell">
                   			<td data-title="${dewell.name}">${dewell.pointInfo.sAcceptance}</td>
                   		</c:forEach>
                   		<c:forEach items="${owells}" var="oewell">
                   			<td data-title="${oewell.name}">${oewell.pointInfo.sAcceptance}</td>
                   		</c:forEach>
                   		<c:forEach items="${iwells}" var="iewell">
                   			<td data-title="${iewell.name}">${iewell.pointInfo.sAcceptance}</td>
                   		</c:forEach>
                     </tr>
                     </tbody>        
        		</table>
        	</div>
        </section>
    </div>
</div>
<form action="exportCols.shtml" id="export-form" class="form-horizontal" method="POST">
	<input type="hidden"  name="cid" id="cid" value="${cid}"/>
</form>
<script src='<c:url value="/resources/js/jquery.printarea.js" />'></script>
<script>
function exportexcel(){
	 //$('#loading').show();
	$("#export-form").submit();
	//$('#loading').hide();
	
}
function goback(){
	javascript :history.back(-1);
}
function print(title){
	var options = {
			extraHead: '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>',
			
            retainAttr : ["id","class","style"], 
            popTitle  : title+'成井日志'
		}
	$(".printObj").printArea(options);
	//$(".printObj").printArea({popTitle  : title+'成井日志'});
}
</script>
