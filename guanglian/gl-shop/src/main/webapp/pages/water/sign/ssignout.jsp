<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/css/table-response.css" />'
	rel="stylesheet">
<style type="text/css">
.sdiv {padding:5px 0px;}
</style>
<script type="text/javascript">
$("#pictitle").html("到站销点")
</script>
<div class="row">
	<c:if test="${not empty gjob }">
		<section class="panel">
			<header class="panel-heading">
				${gentity.name}
			</header>
			<div class="panel-body" style="padding:15px;">
				<div class="sdiv">
					省/市-城市：${gentity.zone}-${gentity.city}
				</div>
				<div class="sdiv">
					详细地址：${gentity.address}
				</div>
				<div class="sdiv">
					车站：${gentity.station}
				</div>
				<div class="sdiv">作业时间：${gjob.arriveDate}-${gjob.leaveDate}</div>
				<div class="sdiv" style="background:${color};color:#fff">要点时间：${sign.auditSection.dateCreated}
				</div>
				<hr>
				<div class="text-center">
					<a  class="btn-info" style="display:inline-block;height:70px;width:70px;line-height:70px;text-align:center;border-radius:50%;" href="javascript:;" onclick="dosubmit()">开始销点</a>
				</div>
				<br>
			</div>
		</section>
	</c:if>
</div>
<form  id="signForm" action="<c:url value='/water/sign/signout.html'/>" method="post">
	<input type="hidden"  name="gid" id="gid" value="${gentity.id}"/>
	<input type="hidden"  name="jid" id="jid" value="${gjob.id}"/>
</form>
<script>
	function dosubmit(){
		$("#signForm").submit();
	}
</script>
