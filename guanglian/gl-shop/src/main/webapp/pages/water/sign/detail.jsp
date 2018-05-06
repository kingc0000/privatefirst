<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %> 
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">

<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading">签到详情
	</header>
	<div class="panel-body">
		<div class="col-lg-12 col-sm-12">
			<section class="adv-table " id="no-more-tables">
             <table class="display table table-bordered table-striped" id="slist">
               
                 <tr>
                     <td>姓名</td><td>${sign.uName}</td>
                     </tr>
                     <tr>
                     <td>项目名称</td><td>${sign.pName}</td>
                     </tr>
                     <tr>
                     <td>工作地点</td><td>${sign.address}</td>
                     </tr>
                     <tr>
                     <td>登记车站</td><td>${sign.station }</td>
                     </tr>
                     <tr>
                     <td>作业时间</td><td>${sign.gjob.startDate }-${sign.gjob.endDate }</td>
                     </tr>
                     <tr>
                     <td>${title}计划时间</td>
                     <td>
                     	<c:choose>
                     		<c:when test="${sign.stype==0}">
                     			${sign.gjob.arriveDate }
                     		</c:when>
                     		<c:otherwise>
                     			${sign.gjob.leaveDate }
                     		</c:otherwise>
                     	</c:choose>
                     </td>
                     </tr>
                     <tr>
                     <td>实际时间</td><td>${sign.auditSection.dateCreated }</td>
                     </tr>
                     <tr>
                     <td>备注</td><td>${sign.memo}</td>
                     </tr>
                     <tr>
                     <td>附件</td>
                     <td><c:forEach items="${sign.images}" var="imgs">
                       				<div class="page-break" style="margin:0 auto;width:100%;height:100%">
                       					<img class="preimg" style="width:100%" src='<sm:contentImage imageName="${imgs.name}" imageType="SIGN"/>'/>
                       				</div>	
                       			</c:forEach>	
                       		</td>
                     </tr>
             </table>
            </section>
		</div>
		</div>
</section>
</div>
</div>


