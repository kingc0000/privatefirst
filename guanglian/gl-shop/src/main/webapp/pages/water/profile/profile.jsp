<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<section class="panel">
	<header class="panel-heading"> <s:message code="label.basic.info" text="Info"/> </header>
	<div class="panel-body">
		<div class="col-lg-8 col-sm-12">
			<c:url var="userSave" value="/water/users/saveuser.html" />
			<form:form method="POST" commandName="user" action="${userSave}"
				cssClass="form-horizontal" id="storeform">
				<form:errors path="*"
					cssClass="alert alert-block alert-danger fade in" element="div" />
				<div id="store.success" class="alert alert-success"
					style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="label.submit.success" text="success "/></div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required"><s:message code="label.generic.username" text="username"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control required" path="adminName"
							remote="checkUserCode.shtml?id"  />
						<span class="help-block"> <form:errors path="adminName"
								cssClass="error" /></span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required"><s:message code="label.email" text="email"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control required email"  path="adminEmail" />
						<span class="help-block"><form:errors path="adminEmail"
								cssClass="error" /></span>
					</div>
				</div>
				

				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label"><s:message code="label.generic.name" text="name"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control" path="firstName" />
						<span class="help-block"><form:errors path="firstName"
								cssClass="error" /></span>
					</div>
				</div>

				

				<form:hidden path="id" id="id"/>
				<c:if test="${user.id!=null && user.id>0}">
					<form:hidden path="adminPassword" />
				</c:if>

				<div class="form-group">
					<div class="col-lg-2 col-sm-2"></div>
					<div class="col-lg-10">
						<button type="button" onclick="jssubmit()" class="btn btn-success"><s:message code="label.save" text="Save"/></button>
					</div>
				</div>

			</form:form>
		</div>
		<div class="col-lg-4"></div>
	</div>

</section>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script type="text/javascript">
$(function(){
	$("#storeform").validate();
	//设置title
	$('#pictitle').html('基本信息');
});

function jssubmit(){
	$("#storeform").submit();
	/**
	var groups=$( "form input:checked") ;
	var isSubit=false;
	 $(groups).each(function(){
		 if(this.name=="groups"){
			 isSubit=true;
			 return false; 
		 }
	 });
	 if(isSubit){
		 
	 }else{
		 alert('<s:message code="message.user.auth" text="Auth "/>');
			return;
	 }*/
	 
			
}
</script>
