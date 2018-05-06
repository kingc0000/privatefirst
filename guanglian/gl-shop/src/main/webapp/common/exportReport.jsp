<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link
	href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />'
	rel="stylesheet">
<div class="container">

	<!-- Cropping modal -->
	<div class="modal fade" id="export-modal" aria-hidden="true"
		aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button class="close" data-dismiss="modal" type="button">&times;</button>
					<h4 class="modal-title" id="etitle">导出</h4>
				</div>
				<div class="modal-body">
					<form action="exportCols.shtml" id="export-form"
						class="form-horizontal" method="POST">
						<div>请选择统计日期</div>
						<br>

						<div class="col-md-6 col-xl-12">
							<div class="row">
								<label class="col-lg-3 col-sm-3 control-label control-required">统计时间</label>
								<div class="input-group date edate col-lg-9 col-sm-9">
									<input type="text" class="form-control" id="ebegindt"
										name="ebegindt" /><span class="input-group-addon btn-theme"><i
										class="fa fa-calendar"></i></span>
								</div>
							</div>
						</div>
						<br>
						<hr style="margin-top: 10px">
						<div class="form-group">
							<div class="col-lg-2 col-sm-2"></div>
							<div class="col-lg-10">
								<button type="button" onclick="exportexcel()"
									class="btn btn-success pull-right">导出</button>
							</div>
						</div>
						<br />
					</form>
				</div>
			</div>
		</div>
	</div>

</div>

<script
	src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		//加载日期控件
		$('.edate').datepicker({
		    format: "yyyy-mm-dd",
		    todayBtn: "linked",
		    clearBtn: true,
		    autoclose: true,
		    todayHighlight: true
		});
	});
	
	function exportexcel(){
		
		var begindt = $("input[name='ebegindt']").val();
		 if (begindt=='') {
		  toastr.error('请选择查询时间范围');
		  return false;
		 }
		 $('#loading').show();
		$("#export-form").submit();
		$('#export-modal').modal('hide');
		$('#loading').hide();
	}
	
	function autoc(flow){
		if($("#lastAccu").val()!="" && $("#thisAccu").val()!="" && $("#accuPeriod").val()!=""){
			var low=(Number($("#thisAccu").val())-Number($("#lastAccu").val()));
			low =low.div(Number($("#accuPeriod").val()));
			$("#"+flow).val(low);
		}
	}
	function getperid(date1,data2){
		if(data2==null){
			data2=new Date();
		}else{
			data2 = new Date(data2);
		}
		var date = new Date(Date.parse(date1.replace(/-/g, "/")));
		 
		var diff =(data2-date);
		var diff =diff.div(1000*60*60);
		if(isNaN(diff)){
			diff=0;
		}
		return diff;
	}
</script>