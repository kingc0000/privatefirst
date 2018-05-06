<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />'
	rel="styleshee">
<link href='<c:url value="/resources/data-tables/DT_table.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />' rel="stylesheet">

<div class="row" id="edittable" style="display: none;">
	<div class="col-md-12">
		<section class="panel">
			<header class="panel-heading">
				<span id="edittile"></span> <span class="tools pull-right"> <a
					href="javascript:;" class="fa fa-chevron-down"></a> <a
					href="javascript:;" class="fa fa-times"></a>
				</span>
			</header>
			<div class="panel-body">
				<form:form cssClass="form-horizontal" role="form" commandName="rmreport" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">报表日期</label>
						<div class="col-lg-10">
							<div class="date edate input-group">
								<form:input cssClass="form-control"
								path="thiser" id="thiser" />
							<span class="input-group-addon btn-theme"><i
								class="fa fa-calendar"></i></span> <span class="help-block"> <form:errors
									path="thiser" cssClass="error" /></span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">报表编号</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="rNo" id="rNo" />
							<span class="help-block"> <form:errors path="rNo"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">监测者</label>
						<div class="col-lg-10">
							<div class="input-group">
			              	 	<form:input cssClass="form-control " path="rmonitor" id="rmonitor" readonly="true" />
			             	 	<span class="input-group-btn phonehide" >
			                     <button class="btn btn-theme tooltips" type="button"  data-placement="top" data-container="body" data-original-title="监测者" onclick="getList('请选择监测者','rmonitor','<c:url value="/water/gjob/musers.shtml"/>','modal')"><i class="fa fa-user"></i></button>
			                   </span>
			             	  </div>	
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">计算者</label>
						<div class="col-lg-10">
							<div class="input-group">
			              	 	<form:input cssClass="form-control " path="computer" id="computer" readonly="true" />
			             	 	<span class="input-group-btn phonehide" >
			                     <button class="btn btn-theme tooltips" type="button"  data-placement="top" data-container="body" data-original-title="计算者" onclick="getList('请选择计算者','computer','<c:url value="/water/gjob/musers.shtml"/>','modal')"><i class="fa fa-user"></i></button>
			                   </span>
			             	  </div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">校核者</label>
						<div class="col-lg-10">
							<div class="input-group">
			              	 	<form:input cssClass="form-control" path="verifier" id="verifier" readonly="true" />
			             	 	<span class="input-group-btn phonehide" >
			                     <button class="btn btn-theme tooltips" type="button"  data-placement="top" data-container="body" data-original-title="校核者" onclick="getList('请选择校核者','verifier','<c:url value="/water/gjob/musers.shtml"/>','modal')"><i class="fa fa-user"></i></button>
			                   </span>
			             	  </div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">报送公司</label>
						<div class="col-lg-10">
							<c:forEach var="bcm" items="${bc}">
								<span class="checkboxs-inline" onclick="setCheckbox('rcompany','${bcm.id}')">
								<input type="checkbox" id="rcompany" name="rcompany" title="${bcm.id}" onclick="setCheckbox('rcompany','${bcm.id}')"> &nbsp;${bcm.name}
								</span>
							</c:forEach>
						</div>
					</div>
					<c:if test="${hasRight==true}"><div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="jssubmit()"
								class="btn btn-success">提交</button>
						</div>
					</div></c:if>
					<form:hidden path="id" />
					<form:hidden path="monitor.id"/>
				</form:form>
			</div>
		</section>
	</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				列表
				<c:if test="${hasRight==true}">
				<button type="button" id="lastbtn"
					class="btn btn-theme pull-right fa fa-plus-square"
					onclick="changeEdittable(afternew)">新增</button></c:if>
					<button id="nextbtn" type="button"
					class="btn btn-theme pull-right fa fa-arrow-down visible-xs"
					onclick="donext()">下一页</button>
					<button type="button"
					class="btn btn-theme pull-right fa fa-arrow-up visible-xs"
					onclick="dolast()">上一页</button>
			</header>
			<div class="panel-body">
				<div class="col-lg-12 col-sm-12">
					<section class="adv-table " id="no-more-tables">
						<table class="display table table-bordered table-striped"
							id="slist">
							<thead>
								<tr>
									<th><input class="allCheckbox" type="checkbox" /></th>
									<th>报表编号</th>
									<th>监测者</th>
									<th>计算者</th>
									<th>校核者</th>
									<th>生成时间</th>
									<th>操作</th>
								</tr>
							</thead>
						</table>
					</section>
				</div>
			</div>
		</section>
	</div>
</div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script>
$('#pictitle').html("${mentity.name }沉降日报");
	var mid = '${mid}';
	$(function() {
$('#slist').dataTable({
												"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSortClasses" : false,
							"sDom" : "lf<c:if test="${hasRight==true}"><'#del_btn'></c:if>rtip",
							"sAjaxSource" : "server_processing.shtml?mid="+mid,
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							},{
								"mData" : "rNo"
							},  {
								"mData" : "rmonitor"
							}, {
								"mData" : "computer"
							}, {
								"mData" : "verifier"
							},  {
								"mData" : "auditSection.dateCreated"
							}, {
								"mData" : "id"
							}, ],
							"aoColumnDefs" : [
{
										"bVisible" : true,
										"aTargets" : [ 0 ],
										"mRender" : function(data, type, full) {
											return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';
										},
										"bSortable" : false
									},
									{
										"aTargets" : [ 6 ],
										"mRender" : function(data, type, full) {
											var s = '&nbsp;<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-original-title="删除" onclick="deldata('
													+ data
													+ ',&quot;您真的要删除该对象吗？&quot; )"><i class="fa fa-trash-o "></i></a>';
													s+='&nbsp;<a class="btn btn-info btn-xs tooltips hidden-xs" data-placement="left" data-container="body" data-original-title="下载" href="<c:url value="/water/rmreport/downloads/'+data+'.html?mid='+mid+'"/>" ><i class="fa fa-download"></i></a>&nbsp;';
													return s;
										},
										"sWidth" : "120px",
										"bSortable" : false
									}, {
										"aTargets" : [ "_all" ],
										"bSortable" : false
									} ],
							"fnInitComplete" : function(settings, json) {
								$('.tooltips').tooltip();
								definedSidCheckbox();
							}
						});
//加载日期控件
$('.edate').datepicker({
	format : "yyyy-mm-dd",
	todayBtn : "linked",
	clearBtn : true,
	autoclose : true,
	todayHighlight : true
});
	});
	function jssubmit() {
		$("#storeform").submit();
	}
	function afternew() {
		$("input[name='monitor.id']").val(mid);
	}
	function setCheckbox(pname,gid){
		$.each($('input[name="'+pname+'"]'),function(index,data){
			if($(this).attr("title")==gid){
				if($(this).prop("checked")){
					$(this).prop("checked",false);
				}else{
					$(this).prop("checked",true);
				}
				
				return false;
			}
			
		});
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>
