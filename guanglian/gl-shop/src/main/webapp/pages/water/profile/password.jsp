<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page session="false" %>				
<%@page pageEncoding="UTF-8"%>
<section class="panel">
	<header class="panel-heading"> <s:message code="label.change.password" text="password "/></header>
	<div class="panel-body">
		<div class="col-lg-8 col-sm-12">
			<c:url var="savePassword" value="/water/users/savePassword.html"/>
			<form:form method="POST" commandName="password"  id="passwordForm" cssClass="form-horizontal" action="${savePassword}">
				<form:errors path="*"
					cssClass="alert alert-block alert-danger fade in" element="div" />
				<div id="store.success" class="alert alert-success"
					style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="label.submit.success" text="success "/></div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required"><s:message code="label.old.password" text="Old"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control required  " type="password" path="password" minlength="6" remote="checkoldpwd.shtml"/>
	                    <span class="help-block"><form:errors path="password" cssClass="error" /></span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required"><s:message code="label.new.password" text="new"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control required  " type="password" path="newPassword" minlength="6" id="newPassword" />
						<span class="help-block"> <form:errors path="password" cssClass="error" /></span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required"><s:message code="label.repeate.password" text="Repeat"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control " path="repeatPassword" type="password" minlength="6" equalTo="#newPassword" id="repeatPassword"/>
						<span class="help-block"> <form:errors path="repeatPassword" cssClass="error" /></span>
					</div>
				</div>
				<div class="form-group">
					<div class="col-lg-2 col-sm-2"></div>
					<div class="col-lg-10">
						<button type="submit" class="btn btn-success"><s:message code="label.save" text="Save"/></button>
					</div>
				</div>

			</form:form>
		</div>
		<div class="col-lg-4"></div>
	</div>

</section>
<script type="text/javascript">
$().ready(function() {
    $("#passwordForm").validate(); 
    $('#pictitle').html('修改密码');
});
</script>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
