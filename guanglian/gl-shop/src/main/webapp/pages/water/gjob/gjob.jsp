<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/css/table-response.css" />'
	rel="stylesheet">
	<link href='<c:url value="/resources/assets/bootstrap-datepicker/css/datepicker.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-timepicker/compiled/timepicker.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-daterangepicker/daterangepicker-bs3.css" />' rel="stylesheet">
<div class="row" id="edittable" style="display: none;">
	<div class="col-md-12">
		<section class="panel">
			<header class="panel-heading">
				<span id="edittile">新增</span> <span class="tools pull-right"> <a
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
					<form:hidden path="guard.id"  id="guardid"/>
				</form:form>
			</div>
		</section>
	</div>
</div>

<div class="row" >
<div class="col-md-12">
<section class="panel">
	<div class="panel-body" style="outline: none;overflow-y:scroll;" id="no-more-tables">
        		<div class="pull-right">
        			 <button type="button" onclick="doSearch()" class="btn btn-success">本周</button> <button type="button" onclick="doSearch('next')" class="btn btn-info">下周</button>&nbsp;&nbsp;<span style="font-size: 14px;">选择日期：</span><input style="border: 1px solid #e2e2e4;height: 34px;padding: 6px 12px;border-radius: 4px;font-size: 14px;" class="edate"  readonly="readonly" id="edate" />
        		</div>
        		<br>
        		<hr>
        		<table class="table table-hover table-bordered">
        			<thead>
                          <tr class="text-center">
                          	<th rowspan="2" style="vertical-align:middle;font-size:15px;">项目名称</th>
                          	<th>星期六</th>
                          	<th>星期天</th>  
						    <th>星期一</th>  
						    <th>星期二</th>  
						    <th>星期三</th>  
						    <th>星期四</th>  
						    <th>星期五</th>
						    <th rowspan="2" style="vertical-align:middle;font-size:15px;text-align:center;">操作</th>
                    	 </tr>
                    	 <tr class="text-center" id="dates">
                    	 </tr>
                     </thead> 
                     <tbody id="find">
                     </tbody>        
        		</table>
        		<div class="pull-right" id="pagination"></div>
        	</div>
        	</section>
</div>
</div>
<script src='<c:url value="/resources/js/project-fore/pagingation.js"/>'></script>
<script>
var pdatas =new Array();
var cpage=1;
var sdate="";
jQuery(document).ready(function() {
	doAction(1);
	var form=$("#storeform");
		validator = $("#storeform").validate({
			invalidHandler : function() {
				return false;
			},
			//指明错误放置的位置
			errorPlacement : function errorPlacement(error, element) {
				element.after(error);
			},
			submitHandler : function() {
				//蒙版处理
				$('#loading').show();
				var url = "save.shtml";
				$.ajax({
					type : "POST",
					url : url,
					data : $('#storeform').serializeJson(),					
					async : true,
					traditional : true,
					success : function(response) {

						var status = response.response.status;
						if (status == 0 || status == 9999) {
							toastr.success('操作成功！');
						} else if (status == 7) {
							toastr.info(response.response.statusMessage);
						} else if (status == 11) {
							toastr.success('操作成功！');
							afterSave(response); //自定义外部接口
						} else if (status == -4) {
							toastr.error(response.response.statusMessage);
						} else {
							toastr.error('提交失败！');
						}
						$('#edittable').hide();
						$('#loading').hide();
						doAction(1);
					},
					error : function(xhr, textStatus, errorThrown) {
						alert('error ' + errorThrown);
						$('#loading').hide();
					}
				});
				return false;// 阻止form提交
			}
		});
		$('.tooltips').tooltip();
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
        $('.edate').datepicker({
        	format: "yyyy-mm-dd",
		    todayBtn: "linked",
		    clearBtn: true,
		    autoclose: true,
		    todayHighlight: true
        });
        $("#edate").change(function(){
        	sdate=$(this).val();
        	doAction(1);
        	});
});
function formatDate(date){
	var year=date.getFullYear();//年
	var month=date.getMonth()+1;//月份（月份是从0~11，所以显示时要加1）
	var day=date.getDate();//日期
	var str=year+'-'+month+'-'+day;
	return str;
}
function doSearch(next){
	if(typeof(next)=="undefined"){
		sdate="";
	}else{
		var now = new Date();
		sdate = formatDate(new Date(now.getTime() + 7 * 24 * 3600 * 1000));
	}
	doAction(1);
}
function doAction(page){
	$('#loading').show();
	$.post('processing.shtml',{"page":page, "sdate":sdate}, function(response){
		if(response!=null && response!=""){
			writeData(response);
		}else{
			toastr.error('获取数据失败');
		}
		$('#loading').hide();
	}).error(function() {$('#loading').hide();toastr.error('获取数据失败'); });
}
function writeData(data){
	pdatas.splice(0,pdatas.length)
	//写日期
	var str='';
	for(var i=0;i<data.dates.length;i++){
		str+='<th>'+data.dates[i]+'</th>';
	}
	$("#dates").html(str);
	//写内容
	str='';
	for(var i=0;i<data.plans.length;i++){
		var plan=data.plans[i];
		str+='<tr><td ';
		var strrow='';
		var strplan='';
		if(plan.jobs.length>0){
			strrow='rowspan="'+plan.jobs.length+'"';
			//写已有的日程
			for(var j=0;j<plan.jobs.length;j++){
				var job=plan.jobs[j];
				//保存数据到内存
				var item = new Object();
				item.id=job.id;
				item.startDate=job.startDate;
				item.endDate=job.endDate;
				item.arriveDate=job.arriveDate;
				item.leaveDate=job.leaveDate;
				item.gid=plan.id;
				//开始结束日期
				var start=new Date(job.startDate.replace("-", "/").replace("-", "/"));
				var end=new Date(job.endDate.replace("-", "/").replace("-", "/"));
				//记录那一天开始有工作安排的
				var sday=-1;
				var eday=6;
				for(var k=0;k<data.dates.length;k++){
					var current = new Date(data.dates[k].replace("-", "/").replace("-", "/"));
					if(sday==-1){
						if(current>=start){
							sday=k;
							//要往前一天
							k--;
						}
					}else{
						//结束
						if(current>=end){
							eday=k;
							break;
						}
					}
				}
				if(sday!=-1){
					//写表空白的数据
					for(var n=0;n<sday;n++){
						strplan+='<td></td>';
					}
					//写具体内容数据
					strplan+='<td style="background:#1E90FF;word-wrap: break-word;" colspan="'+(eday-sday+1)+'">'+' <a href="javascript:;" style="color:#fff" onclick="doedit('+plan.id+')">';
					strplan+='签到时间:'+job.arriveDate+' 销点时间:'+job.leaveDate+' 责任人:';
					var users =new Array();
					for(var n=0;n<job.users.length;n++){
						var ju=job.users[n];
						var user =new Object();
						user=ju.id
						users[users.length]=user;
						strplan+=ju.name+' ';
					}
					item.users=users;
					pdatas[pdatas.length]=item;
					var pstatus ="未确认";
					if(job.cstatu==1){
						pstatus="已确认";
					}
					strplan+=' 工作状态：'+pstatus
					strplan+='</a></td>';
					//写表空白的数据
					for(var n=eday;n<6;n++){
						strplan+='<td></td>';
					}
					//结束的项目不能修改删除
					var now =new Date();
					strplan+='<td>';
					if(end>now){
						strplan+='<a class="edit btn btn-primary btn-xs tooltips " data-placement="left" data-original-title="编辑" href="javascript:;" onclick="doedit('+plan.id+')"><i class="fa fa-pencil"></i></a>'
						strplan+='&nbsp;<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-original-title="删除" onclick="deldata('+job.id+ ')"><i class="fa fa-trash-o "></i></a>&nbsp;';
					}
					//写操作
					strplan+='<a href="javascript:;" class="btn btn-success btn-xs tooltips" data-placement="left" data-original-title="新增" onclick="addnew('+plan.id+')"><i class="fa  fa-plus-square "></i></a></td></tr>'
					
				}else{
					//写表空白的数据
					for(var n=0;n<7;n++){
						strplan+='<td></td>';
					}
					strplan+='<td><a href="javascript:;" class="btn btn-success btn-xs tooltips" data-placement="left" data-original-title="新增" onclick="addnew('+plan.id+')"><i class="fa  fa-plus-square "></i></a></td></td></tr>';
				}
				
			}
		}else{
			for(var n=0;n<7;n++){
				strplan+='<td></td>';
			}
			strplan+='<td><a href="javascript:;" class="btn btn-success btn-xs tooltips" data-placement="left" data-original-title="新增" onclick="addnew('+plan.id+')"><i class="fa  fa-plus-square "></i></a></td></td></tr>';
		}
		str+=strrow+'>';
		str+=plan.name+'</td>';
		str+=strplan;
	}
	$("#find").html(str);
	//写分页
	writePaging(data.page);
	//获取当前页面的值，用于刷新页面
	cpage=data.page.currentPage;
}
function clear(){
	// 清空表单
	$(':input', '#storeform').not(':button, :submit, :reset').val('')
			.removeAttr('checked').removeAttr('selected');
}
function addnew(gid){
	$('#loading').show();
	clear();
	$.post('users.shtml',{"gid":gid}, function(response){
		if(response!=null){
			var str='';
			for(var i=0;i<response.length;i++){
				str+='<input name="cusers" ';
				
				str+=' type="checkbox" value="'+response[i].id+'"><a class="menulink" href="javascript:void(0);" onclick="setCheckbox(&quot;cusers&quot;,'+response[i].id+')">&nbsp;'+response[i].name+'</a>'
			}
			$("#cdiv").html(str);
			$('#edittable').show();
			$("#guardid").val(gid);
		}else{
			toastr.error('获取数据失败');
		}
		$('#loading').hide();
		
	}).error(function() {$('#loading').hide();toastr.error('获取数据失败'); });
	
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
function doedit(gid){
	$('#loading').show();
	clear();
	$.post('users.shtml',{"gid":gid}, function(response){
		if(response!=null){
			var str='';
			var item=null;
			
			for(var i=0;i<pdatas.length;i++){
				 if(pdatas[i].gid==gid){
					 item=pdatas[i];
					 break;
				 }
			}
			if(item!=null){
				var users=item.users;
				for(var i=0;i<response.length;i++){
					str+='<input name="cusers" ';
					var found=false;
					for(var n=0;n<users.length;n++){
						if(users[n]==response[i].id){
							found=true;
							break;
						}
					}
					if(found==true){
						str+='checked="checked"';
					}
					str+=' type="checkbox" value="'+response[i].id+'"><a class="menulink" href="javascript:void(0);" onclick="setCheckbox(&quot;cusers&quot;,'+response[i].id+')">&nbsp;'+response[i].name+'</a>'
				}
				$("#cdiv").html(str);
				$("#arriveDate").val(item.arriveDate);
				$("#leaveDate").val(item.leaveDate);
				$("#startDate").val(item.startDate);
				$("#endDate").val(item.endDate);
				$("#guardid").val(item.gid);
				$("#id").val(item.id);
				$('#edittable').show();
			}else{
				toastr.error('获取数据失败');
			}
		}else{
			toastr.error('获取数据失败');
		}
		$('#loading').hide();
		
	}).error(function() {$('#loading').hide();toastr.error('获取数据失败'); });
}
function deldata(gid){
	var rdel = confirm('您真的要删除该这项工作安排吗？');
	if (rdel == false)
		return;
	$('#loading').show();
	$.post('remove.shtml',{"gid":gid}, function(response){
		var status = response.response.status;
		if(status==0){
			doAction(cpage);
			toastr.success(index+'操作成功！');
		}else {
			toastr.error('处理问题失败,请联系管理员');
		}
		$('#loading').hide();
		
	}).error(function() {$('#loading').hide();toastr.error('获取数据失败'); });
}
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
</script>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-daterangepicker/daterangepicker.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-timepicker/js/bootstrap-timepicker.js" />'></script>