<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>				
				
<section class="panel">
	<header class="panel-heading"> <s:message code="label.email" text="Email"/><s:message code="label.setting" text="Setting"/></header>
	<div class="panel-body">
		<div class="col-lg-8 col-sm-12">
			<c:url var="saveEmailConfiguration" value="/water/configuration/saveEmailConfiguration.html"/>
			<form:form method="POST" commandName="configuration" action="${saveEmailConfiguration}" id="emailForm" cssClass="form-horizontal">
				<form:errors path="*"
					cssClass="alert alert-block alert-danger fade in" element="div" />
				<div id="store.success" class="alert alert-success"
					style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="label.submit.success" text="Success"/></div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required"><s:message code="label.email" text="Email"/><s:message code="email.protocol" text="Protocol"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control required" path="protocol" />
						<span class="help-block"> <form:errors path="protocol" cssClass="error" /></span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required"><s:message code="email.host" text="Host"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control required  " path="Host" />
						<span class="help-block"> <form:errors path="Host" cssClass="error" /></span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required"><s:message code="email.port" text="Port"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control required" path="port" />
						<span class="help-block"> <form:errors path="port" cssClass="error" /></span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required">邮件地址</label>
					<div class="col-lg-10">
						<form:input cssClass="form-control required" path="username" />
						<span class="help-block"><form:errors path="username"
								cssClass="error" /></span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-lg-2 col-sm-2 control-label control-required"><s:message code="label.generic.password" text="Password"/></label>
					<div class="col-lg-10">
						<form:input cssClass="form-control required" path="password" type="password"/>
						<span class="help-block"><form:errors path="password"
								cssClass="error" /></span>
					</div>
				</div>
				<div class="form-group">
       				<label class="col-lg-2 col-sm-2 control-label"><s:message code="email.auth" text="Auth"/></label>
       				<div class="col-lg-10" style="padding-top:8px;">
							<form:checkbox cssClass="input-large" path="smtpAuth" />&nbsp;&nbsp;<s:message code="message.mail.auth" text="Auth"/>
       				</div>
       				<span class="help-block"><form:errors path="smtpAuth"
								cssClass="error" /></span>
        		</div> 
                  		
           		<div class="form-group">
          				<label class="col-lg-2 col-sm-2 control-label">STARTTLS</label>
          				<div class="col-lg-10" style="padding-top:8px;">
							<form:checkbox cssClass="input-large" path="starttls" />&nbsp;&nbsp;<s:message code="message.mail.starttls" text="Starttls"/>
          				</div>
          				<span class="help-block"><form:errors path="starttls"
								cssClass="error" /></span>
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
$(function(){
	$('#pictitle').html('邮箱配置');
});
</script>

