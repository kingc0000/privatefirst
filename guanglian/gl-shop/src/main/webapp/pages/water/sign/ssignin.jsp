<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/css/table-response.css" />'
	rel="stylesheet">
<style type="text/css">
.sdiv {padding:5px 0px;}
</style>
<script type="text/javascript">
$("#pictitle").html("到站要点");
var apptype='';
var app = navigator.userAgent.indexOf("android");
if(app==-1){
	app = navigator.userAgent.indexOf("iOS");
	if(app!=-1){
		apptype='iOS';
	}
}else{
	apptype='android';
}
</script>
<div class="row">
	<c:if test="${not empty signs }">
		<c:forEach items="${signs}" var="sign">
			<section class="panel">
			<header class="panel-heading">
				${sign.pName}
			</header>
			<div class="panel-body" style="padding:15px;">
				<div class="sdiv">
					详细地址：${sign.address}
				</div>
				<div class="sdiv">
					车站：${sign.station}
				</div>
				<div class="sdiv">签到时间：${sign.shouleBe}</div>
				<hr>
				<div class="text-center">
					<c:choose>
						<c:when test="${sign.sattus==0}">
								<c:choose>
									<c:when test="${sign.stype==0}">
										<a  class="btn-info" style="display:inline-block;height:70px;width:70px;line-height:70px;text-align:center;border-radius:50%;" href="javascript:;" onclick="dosubmit(${sign.id})">
										开始要点</a>
									</c:when>
									<c:otherwise>
										<a  class="btn-primary" style="display:inline-block;height:70px;width:70px;line-height:70px;text-align:center;border-radius:50%;" href="javascript:;" onclick="dosubmit(${sign.id})">
										开始销点</a>
									</c:otherwise>
								</c:choose>
							
						</c:when>
						<c:otherwise>
							<span class="btn-success">已签到,签到时间： <fmt:formatDate value="${sign.auditSection.dateModified}" pattern="yyyy/MM/dd  HH:mm:ss" /></span>
						</c:otherwise>
					</c:choose>
					
				</div>
				<br>
			</div>
		</section>
		</c:forEach>
		
	</c:if>
</div>
<form  id="signForm" action="<c:url value='/water/sign/startsign.html'/>" method="post">
	<input type="hidden"  name="sid" id="sid" />
</form>
<script>
	function dosubmit(sid){
		if(apptype=="android"){
			scanUtils.location();
		}
		$("#sid").val(sid);
		$("#signForm").submit();
	}
</script>
