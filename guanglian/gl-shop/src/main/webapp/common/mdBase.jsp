<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<div class="form-group" id="cdate" style="display: none">
	<label class="col-lg-2 col-sm-2 control-label control-required">校准日期</label>
	<div class="col-lg-10">
		<div class="input-group date datetimeControl">
			<form:input cssClass="form-control required"
			path="calibration" id="calibration" />
			<div class="input-group-btn">
				<button type="button" class="btn btn-danger date-reset">
					<i class="fa fa-times"></i>
				</button>
				<button type="button" class="btn btn-warning date-set">
					<i class="fa fa-calendar"></i>
				</button>
			</div>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-lg-2 col-sm-2 control-label control-required">上次高程</label>
	<div class="col-lg-10">
		<form:input cssClass="form-control required" readonly="true"
			path="initHeight" id="initHeight" />
		<span class="help-block"> <form:errors path="initHeight"
				cssClass="error" /></span>
	</div>
</div>
<div class="form-group">
	<label class="col-lg-2 col-sm-2 control-label control-required">本次高程</label>
	<div class="col-lg-10">
		<form:input cssClass="form-control required" path="curtHeight"
			id="curtHeight" />
		<span class="help-block"> <form:errors path="curtHeight"
				cssClass="error" /></span>
	</div>
</div>
<div class="form-group">
	<label class="col-lg-2 col-sm-2 control-label">本次变化量</label>
	<div class="col-lg-10">
		<input class="form-control" 
			id="vDisplacement" readonly="readonly" />
	</div>
</div>
<div class="form-group">
	<label class="col-lg-2 col-sm-2 control-label">总累积量</label>
	<div class="col-lg-10">
		<input class="form-control" 
			id="sumDisplacement" readonly="readonly" />
	</div>
</div>