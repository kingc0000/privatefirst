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
<link href='<c:url value="/resources/assets/bootstrap-datepicker/css/datepicker.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-timepicker/compiled/timepicker.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-daterangepicker/daterangepicker-bs3.css" />' rel="stylesheet">
<script type="text/javascript">
		$('#loading').show();
		var pid = '${gid}';
		var pname='${pname}';
		$('#pname').html(pname);
</script>
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
				<form:form cssClass="form-horizontal" role="form" commandName="gjob"
					id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">签到时间</label>
						<div class="col-lg-10">
							<div class="input-group bootstrap-timepicker">
                                 <form:input cssClass="form-control timepicker-24 required" path="arriveDate" readonly="true" id="arriveDate" />
                                   <span class="input-group-btn">
                                   <button class="btn btn-default" type="button"><i class="fa fa-clock-o"></i></button>
                                   </span>
                             </div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">销点时间</label>
						<div class="col-lg-10">
							<div class="input-group bootstrap-timepicker">
                                 <form:input cssClass="form-control timepicker-24 required" path="leaveDate" readonly="true" id="leaveDate" />
                                   <span class="input-group-btn">
                                   <button class="btn btn-default" type="button"><i class="fa fa-clock-o"></i></button>
                                   </span>
                             </div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">时间范围</label>
						<div class="col-lg-10">
							<div class="input-group input-large"  data-date-format="yyyy-mm-dd">
                                  <form:input cssClass="form-control dpd1 required" path="startDate" readonly="true" id="startDate" />
                                  <span class="input-group-addon">To</span>
                                  <form:input cssClass="form-control dpd2 required" path="endDate" readonly="true" id="endDate" />
                              </div>
						</div>
					</div>
					<div class="form-group" id="project_div">
						<label class="col-sm-2 control-label control-required">项目</label>
						<div class="col-sm-10 ">
                                  <select class="form-control required"  onchange="getUser(this.options[this.options.selectedIndex].value)" id="guard" style="display:inline;width:50%">
                                  	<c:forEach items="${gprojects }" var="content">
                                  		<option value="${content.id}">${content.name }</option>
                                  	</c:forEach>
  								</select>
                              </div>
					</div>
					<div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label control-required">负责人</label>
			                <div class="col-lg-10 col-sm-10" id="cdiv">
			                </div>
			            </div>	
					<div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="jssubmit()"
								class="btn btn-success">提交</button>
						</div>
					</div>
					<form:hidden path="id" />
					<form:hidden path="guard.id" />
				</form:form>
			</div>
		</section>
	</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				<span id="pname">全部</span>的监护作业列表列表
				<button type="button"
					class="btn btn-theme pull-right fa fa-plus-square"
					onclick="changeEdittable(setnew)">新增</button>
			</header>
			<div class="panel-body">
				<div class="col-lg-12 col-sm-12">
					<section class="adv-table " id="no-more-tables">
						<table class="display table table-bordered table-striped"
							id="slist">
							<thead>
								<tr>
									<th><input class="allCheckbox" type="checkbox" /></th>
									<th>项目名称</th>
									<th>周天</th>
									<th>周一</th>
									<th>周二</th>
									<th>周三</th>
									<th>周四</th>
									<th>周五</th>
									<th>周六</th>
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
<script src='<c:url value="/resources/assets/bootstrap-treeview/bootstrap-treeview.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
	$(function() {
		$('#pictitle').html('监护项目作业计划管理');
		//getProjectTreeForCsite('getTree.shtml',$(".projectTree"), goodslist);
$('#slist').dataTable({
												"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : false,
							"bSortClasses" : false,
							"sDom" : "lf<'#del_btn'>rtip",
							"sAjaxSource" : "server_processing.shtml?gid="+pid,
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							},  {
								"mData" : "sun"
							}, {
								"mData" : "mon"
							}, {
								"mData" : "tue"
							}, {
								"mData" : "wed"
							}, {
								"mData" : "thu"
							}, {
								"mData" : "fri"
							}, {
								"mData" : "sat"
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
										"aTargets" : [ 9 ],
										"mRender" : function(data, type, full) {
											var s = '<a class="edit btn btn-primary hasfn btn-xs tooltips " data-placement="left" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';
											s += '&nbsp;<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-original-title="删除" onclick="deldata('
													+ data
													+ ',&quot;您真的要删除该对象吗？&quot; )"><i class="fa fa-trash-o "></i></a>';
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
								$('#loading').hide();
							}
						});
			var checkin = $('.dpd1').datepicker({
				format: 'yyyy-mm-dd',
	            onRender: function(date) {
	                return date.valueOf() < now.valueOf() ? 'disabled' : '';
	            }
	        }).on('changeDate', function(ev) {
	                if ((typeof(checkout.date)=="undefined") || (ev.date.valueOf() > checkout.date.valueOf())) {
	                    var newDate = new Date(ev.date)
	                    newDate.setDate(newDate.getDate() + 1);
	                    checkout.setValue(newDate);
	                }
	                checkin.hide();
	                $('.dpd2')[0].focus();
	            }).data('datepicker');
	        var checkout = $('.dpd2').datepicker({
	        	format: 'yyyy-mm-dd',
	            onRender: function(date) {
	                return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
	            }
	        }).on('changeDate', function(ev) {
	                checkout.hide();
	            }).data('datepicker');
	        $('.timepicker-24').timepicker({
	            autoclose: true,
	            minuteStep: 1,
	            showSeconds: true,
	            showMeridian: false
	        });
	});
	function jssubmit() {
		if($("#cdiv").html()=="" ){
			alert("请至少选择一个负责人！");
			return;
		}else{
			var hadvalue=false;
			$.each($('input[name="cusers"]:checked'),function(index,data){
				hadvalue=true;
				return false;
			});
			if(hadvalue==false){
				alert("请至少选择一个负责人！");
				return;
			}
		}
		//time 转日期
		//$("#arriveDate").val("2017-07-17 "+$("#arriveDate").val());
		//$("#leaveDate").val("2017-07-17 "+$("#leaveDate").val());
		$("#storeform").submit();
		//fomdatesubmit("save.shtml","ckeck");
	}
	function setCheckbox(pname,gid){
		$.each($('input[name="'+pname+'"]'),function(index,data){
			if($(this).val()==gid){
				if($(this).prop("checked")){
					$(this).prop("checked",false);
				}else{
					$(this).prop("checked",true);
				}
				
				return false;
			}
			
		});
	}
	function setnew(){
		if(pid!=-1 && pid!=""){
			$("#project_div").hide();
			$('input[name="guard.id"]').val(pid);
			$("#guard").val(pid);
			getUser(pid);
		}else{
			$("#project_div").show();
			$("#guard']").val('');
			$('input[name="guard.id"]').val('');
			
		}
	}
	/**
	//点击树节点触发的点击事件，刷新右边的商品列表集合
	function goodslist(data) {
		pid = data.id;
		setnew();
		$('#pname').html(data.text);
		
		pname=data.text;
		//恢复otable初始设置
		oTable = $('#slist').dataTable();
		var oSettings = oTable.fnSettings();
		oSettings._iDisplayStart = 0; //数据访问置0
		refreshData();
		$("#edittable").hide();
	}
	function treeDisplay() {
		$(".sm-side").toggle("normal");
		$("#distag").toggleClass("fa-chevron-right");
	}
	*/
	function dolistafterfn(oTable, nRow){
		var aData = oTable.fnGetData(nRow);
		var gid=aData["gid"];
		$("#project_div").hide();
		$("#guard").val(pid);
		$('input[name="guard.id"]').val(pid);
		getUser(gid,aData["id"]);
	}
	function getUser(pid,jid){
		$('#loading').show();
		$("#cdiv").html('');
		if(typeof(jid)=="undefined"){
			jid="";
		}
		$('input[name="guard.id"]').val(pid);
		var url = "users.shtml";
		$.ajax({
			type : "POST",
			url : url,
			data : "gid="+pid+"&jid="+jid,
			success : function(response) {
				
				if(response!=null){
					var str='';
					for(var i=0;i<response.length;i++){
						str+='<input name="cusers" ';
						if(response[i].uAgent!=null && response[i].uAgent=="checked"){
							str+='checked="checked"';
						}
						str+=' type="checkbox" value="'+response[i].id+'"><a class="menulink" href="javascript:void(0);" onclick="setCheckbox(&quot;cusers&quot;,'+response[i].id+')">&nbsp;'+response[i].name+'</a>'
					}
					$("#cdiv").html(str);
				}
				$('#loading').hide();
			},
			error : function(xhr, textStatus, errorThrown) {
				alert('error ' + errorThrown);
				$('#loading').hide();
			}
		});
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-daterangepicker/daterangepicker.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-timepicker/js/bootstrap-timepicker.js" />'></script>
