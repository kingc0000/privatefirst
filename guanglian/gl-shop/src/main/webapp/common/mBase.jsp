<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>

<div class="form-group">
	<label class="col-lg-2 col-sm-2 control-label control-required">点号</label>
	<div class="col-lg-10">
		<form:input cssClass="form-control required" path="markNO" id="markNO" />
		<span class="help-block"> <form:errors path="markNO"
				cssClass="error" /></span>
	</div>
</div>
<div class="form-group">
	<label class="col-lg-2 col-sm-2 control-label control-required"><span id="mbaseinit">初始高程</span></label>
	<div class="col-lg-10">
		<form:input cssClass="form-control required" path="initHeight"
			id="initHeight" />
		<span class="help-block"> <form:errors path="initHeight"
				cssClass="error" /></span>
	</div>
</div>
<div class="form-group">
	<label class="col-lg-2 col-sm-2 control-label">备注</label>
	<div class="col-lg-10">
		<form:input cssClass="form-control" path="memo" id="memo" />
		<span class="help-block"> <form:errors path="memo"
				cssClass="error" /></span>
	</div>
</div>