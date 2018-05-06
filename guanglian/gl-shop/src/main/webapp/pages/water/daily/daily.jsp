<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<link href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/jquery.steps.css" />' rel="stylesheet">
<style>
.wizard > .steps > ul > li
{
    display:none;
}
.wizard > .content {
	padding-right:0px;
	min-height: auto;
}
.f-textarea textarea {
	height:108px;
	max-height:108px;
}
#wellname .modal-header {
	background: #6699ff;
}
#wellname .modal-content {
	border:1px solid #6699ff;
}
</style>
<script>
	$("#pictitle").html("施工日志");
	var nowdate="${nowdate}";
</script>
				<form:form cssClass="form-horizontal" role="form"
					commandName="daily" id="storeform" enctype="multipart/form-data">
					<div id="wizard">
     					<h3>基本信息</h3>
     					<section>
					<div class="form-group">
						<label class="col-sm-2 control-label control-required">时间</label>
						<div class="col-sm-10 ">
							<input type="text" class="form-control" readonly="readonly" id="datec" name="datec" value="${nowdate}">
						</div>
					</div>
					<div class="form-group">
                        <label class="col-sm-2 control-label ">天气</label>
                        <div class="col-sm-10">
                            <form:input cssClass="form-control " path="weather" id="weather" />
                            <span class="help-block"> <form:errors path="weather" cssClass="error" /></span>
                        </div>
                    </div>
                    <div class="form-group">
		                <label class="col-sm-2 control-label ">每日信息</label>
		                <div class="col-sm-10">
		                	<form:textarea cssClass="ckeditor form-control" rows="6"  id="conclusion" path="conclusion"/>
		                </div>
		              </div>
		              </section>
		              <h3>成井</h3>
		              <section>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">成井工况</label>
                        <div class="col-lg-10" id="no-more-tables">
                        	<table class="table table-bordered">
	                        	<thead>
                        			<tr>
	                        			<td></td>
	                        			<td>当日完成数</td>
	                        			<td>当日井号</td>
	                        			<td>计划完成数</td>
	                        			<td>累积完成数</td>
	                        			<td>设计数量</td>
	                        			<td>完成率</td>
	                        			<td>破坏情况</td>
	                        			<td>备注</td>
	                        		</tr>
                        		</thead>
                        		<tbody>
                        		<c:set var="vs" value="${fn:split('疏干井,降水井,回灌井,观测井,监测点', ',')}" />
                        		<c:forEach items="${vs }" var="v" varStatus="status">
                        			<tr class="well_${status.index}">
	                        			<td data-title="类型">${v}<form:input type="hidden"  path="wType" name="wType"  class="wtype" title="${status.index}"/></td>
	                        			<td data-title="当日完成数"> <input class="form-control" id="dayCmp_${status.index}"  readonly="readonly" name="dayCmp" /></td>
	                        			<td data-title="当日井号">
	                        				<div class="input-group">
	                        					<input class="form-control input-min"  readonly="readonly" id="wellnames_${status.index}" />
	                        					<span class="input-group-btn " >
					                              <button class="btn btn-theme tooltips" type="button"  data-placement="left" data-container="body" data-original-title="选择当然完成井号" onclick="getWells('${status.index}')"><i class="fa fa-anchor"></i></button>
					                            </span> 
	                        				</div>
	                        				<input class="form-control " id="wellIds_${status.index}" type="hidden"  name="wellIds" />
	                        			</td>
	                        			<td data-title="计划完成数"> <input class="form-control" id="planCmp_${status.index}" name="planCmp" /></td>
	                        			<td data-title="累积完成数"> <input class="form-control" id="cumCmp_${status.index}" readonly="readonly" name="cumCmp" /></td>
	                        			<td data-title="设计数量"> <input class="form-control " readonly="readonly" id="designQua_${status.index}" name="designQua" /></td>
	                        			<td data-title="完成率"> <input class="form-control" id="crate_${status.index}" readonly="readonly" name="crate" /></td>
	                        			<td data-title="破坏情况"> <input class="form-control " id="dest_${status.index}" name="dest" /></td>
	                        			<td data-title="备注"> <input class="form-control " id="memo_${status.index}" name="memo" /></td>
                        			</tr>
                        		</c:forEach>
                        		</tbody>
                        	</table>
                        </div>
                    </div>
                    </section>
                    <h3>开挖</h3>
                    <section>
					<div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">开挖工况</label>
                        <div class="col-lg-10">
                        	<div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="excavation" path="excavation"/>
								<div class="textarea-ext">最多可以输入500字</div>
								<span class="help-block"> <form:errors path="excavation" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    </section>
					<h3>疏干</h3>
                    <section>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">疏干运行工况</label>
                        <div class="col-lg-10">
                           <div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="combDry" path="combDry"/>
								<div class="textarea-ext">最多可以输入500字</div>
								 <span class="help-block"> <form:errors path="combDry" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    </section>
                    <h3>降水</h3>
                    <section>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">降水运行工况</label>
                        <div class="col-lg-10">
                           <div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="stepDown" path="stepDown"/>
								<div class="textarea-ext">最多可以输入500字</div>
								 <span class="help-block"> <form:errors path="stepDown" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    </section>
                    <h3>回灌</h3>
                    <section>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">回灌运行工况</label>
                        <div class="col-lg-10">
                            <div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="recharge" path="recharge"/>
								<div class="textarea-ext">最多可以输入500字</div>
								<span class="help-block"> <form:errors path="recharge" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    </section>
                    <h3>风险问题</h3>
                    <section>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">主要风险点</label>
                        <div class="col-lg-10">
                             <div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="risk" path="risk"/>
								<div class="textarea-ext">最多可以输入500字</div>
								<span class="help-block"> <form:errors path="risk" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">安全问题</label>
                        <div class="col-lg-10">
                            <div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="safe" path="safe"/>
								<div class="textarea-ext">最多可以输入500字</div>
								<span class="help-block"> <form:errors path="safe" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">质量问题</label>
                        <div class="col-lg-10">
                            <div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="quality" path="quality"/>
								<div class="textarea-ext">最多可以输入500字</div>
								<span class="help-block"> <form:errors path="quality" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    </section>
                    <h3>主要工作</h3>
                    <section>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">今日主要工作</label>
                        <div class="col-lg-10">
                            <div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="todayWork" path="todayWork"/>
								<div class="textarea-ext">最多可以输入500字</div>
								 <span class="help-block"> <form:errors path="todayWork" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">本周主要工作</label>
                        <div class="col-lg-10">
                            <div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="thisWeek" path="thisWeek"/>
								<div class="textarea-ext">最多可以输入500字</div>
								  <span class="help-block"> <form:errors path="thisWeek" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">下周主要工作</label>
                        <div class="col-lg-10">
                            <div class="f-textarea">
								<form:textarea cssClass="form-control" rows="6"  id="nextWeek" path="nextWeek"/>
								<div class="textarea-ext">最多可以输入500字</div>
								  <span class="help-block"> <form:errors path="nextWeek" cssClass="error" /></span>
							</div>
                        </div>
                    </div>
                    </section>
                    <h3>照片</h3>
                    <section>
                    <div class="form-group">
                         <label class="col-lg-2 col-sm-2 control-label ">照片</label>
                         <div class="col-lg-9 col-sm-9">
                         	<div class="row" id="wimgs">
                         	</div>
                         </div>
                         <div class="col-sm-1 col-xs-6">
                         	<button type="button" class="addBtn btn btn-info fa fa-plus-square" onclick="addImage()"></button>
                         </div>
                     </div>
                     </section>
                    </div>
					<form:hidden path="id" id="did"/>
					 <input type="hidden" name="delids" id="delids"/>
					<input type="hidden" name="cSite.id" id="csiteId"/>
					<input type="hidden" name="auditSection.dateCreated" id="dateCreated"/>
				</form:form>
				<div class="form-group">
					<div class="col-sm-2"></div>
					<div class="col-sm-10 ">
						<button type="button" onclick="save()"
							class="btn btn-success">
							保存
						</button>
					</div>
				</div>
<div class="modal fade" id="wellname" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
      <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
              <button class="close" data-dismiss="modal" type="button">&times;</button>
              <h4 class="modal-title" id="avatar-modal-label">选择井号</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                  <div class="col-md-12" id="wname">
                   
                  </div>
                </div> 
            </div>
            <div class="modal-footer">
            	<button  class="btn btn-default" type="button" onclick="resetchk()">重置</button>
                <button  class="btn btn-theme" id="cbtn" type="button" onclick="setvale()">确认</button>
            </div>
        </div>
      </div>
    </div>

<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-treeview/bootstrap-treeview.js" />'></script>
<script>

function goback(){
	javascript :history.back(-1);
}
var activePid = '${activePid}'; //项目id
var indd="";
//当日井已经选择的数量
var todaychk=0;
jQuery(document).ready(function() {
	var form = $("#storeform");
	if(form.find('h3').length>0){
		$("#wizard").steps({
    	    headerTag: "h3",
    	    bodyTag: "section",
    	    transitionEffect: "slideLeft",
    	    enableFinishButton:false,
    	    onStepChanging: function (event, currentIndex, newIndex) {
    	    	$(window).scrollTop(0);
    	    	form.validate({
    	    		ignore: ":disabled,:hidden"
    	    	});
    	    	 var result= form.valid();
    	    	 if (result==false) return false;
    	    	
    	    	 return true;
    	    },
    	    onFinishing: function (event, currentIndex) {
    	    	form.validate({
    	    		ignore: ":disabled,:hidden"
    	    	});
    	    	
    	        return form.valid();
    	    },
    	    onFinished: function (event, currentIndex) {
    	    	if(hasright){
    	    		jssubmit();
    	    	}else{
    	    		alert("没有权限！！！")
    	    	}
    	    	
            }
    	});
	}
		//计算文本输入数量
	$(".f-textarea textarea").on("keyup", function(){
		var text1 = $(this).val();  
	    var len; //记录剩余字符串的长度   
	    if (text1.length >= 500) //textarea控件不能用maxlength属性，就通过这样显示输入字符数了   
	    {  
	        $(this).val(text1.substr(0, 500));  
	        len = 0;  
	    } else {  
	        len = 500 - text1.length;  
	    }  
	    var show = "还可输入" + len + "字";  
	    $(this).next().html(show); 
	});
	
	CKEDITOR.replace(  'conclusion',
	{
		filebrowserImageBrowseUrl :  '<c:url value="/admin/content/fileBrowser.html"/>',
		filebrowserImageUploadUrl :  '<c:url value="/admin/content/image/upload.shtml"/>'
	});
});

function save() {
	
	//$("#delids").val(dids);
	$(".wtype").each(function(){
		$(this).val($(this).attr("title"));
	  });
	fomdatesubmit();
}

//设定完成率等信息
function setraet(addnew){
	var wtypes = $(".wtype");
	$.each(wtypes,function(i,m){
		var ind =$(this).attr("title");
		if(addnew!=null){
			//设置当然完成量
			var ids =$("#wellIds_"+ind).val();
			if(ids !=""){
				var ida = ids.split(",");
				$("#dayCmp_"+ind).val(ida.length);
			}
		}else{
			$("#wellnames_"+ind).val('');
			$("#wellIds_"+ind).val('');
		}
		
		//设置完成率
		var cum=Number($("#cumCmp_"+ind).val());
		var plan=Number($("#designQua_"+ind).val());
		$("#crate_"+ind).val(cum.div(plan));
	})
}

function getWells(ind){
	indd=ind;
	$('#loading').show();
	var data={"pid":activePid,"wtype":ind,"chkd":$("#wellIds_"+ind).attr("title")};
	console.log(data);
	$.ajax({
		type : "POST",
		url : "<c:url value='/water/daily/wells.shtml'/>",
		dataType : "json",
		data:data,
		success : function(data) {
			if(data !=null){
				var cks="";
				//初始化当然选择的
				todaychk=0;
				var todaych= $("#")
				$.each(data,function(i,m){
					cks+='&nbsp; &nbsp; <label><input class="cks" type="checkbox" name="'+m.name+'" value="'+m.id+'"';
					if(m.checked!=null && m.checked!=""){
						todaychk++;
						cks+=' checked="checked"';
					}
					cks+=' />'+m.name+'</label>';
				})
				$("#wname").html(cks);
				$("#wellname").modal('show');
			}
			$('#loading').hide();
		},
		error : function(data, xhr, textStatus, errorThrown) {
			$('#loading').hide();
		}
	});
}

function setvale(){
	var names="";
	var ids="";
	var sum=0;
	$('input[type="checkbox"]:checked').each(function(i){
	       names+=$(this).attr('name')+",";
	       ids +=$(this).attr('value')+",";
	       sum++;
      });
	//去掉最后一个逗号
	names=names.substring(0,names.length-1);
	ids=ids.substring(0,ids.length-1);
	console.log(names);
	console.log(ids);
	$("#wellnames_"+indd).val(names);
	$("#wellIds_"+indd).val(ids);
	$("#dayCmp_"+indd).val(sum);
	/*
	var cum=Number(sum)+Number($("#cumCmp_"+indd).attr("title"));
	//当日已经选择过了，就不能从title获取值,如果小于要减去
	if(todaychk>0 && sum<=todaychk){
		cum=cum+(sum-todaychk);
	} */
	//累计完成
	var cum=Number(sum)+Number($("#cumCmp_"+indd).attr("title")); 
	if(todaychk>0){
		//如果当日已经选择过了，则不能再重复加减，需要减去之前所增加的
		cum=cum-todaychk;
	}
	
	$("#cumCmp_"+indd).val(cum);
	//完成率
	cum=cum.div(Number($("#designQua_"+indd).val()));
	$("#crate_"+indd).val(cum);
	
	$("#wellname").modal('hide');
}
function resetchk(){
	$(".cks").attr("checked",false);
}

</script>
<script src='<c:url value="/resources/js/jquery.steps.min.js" />'></script>
<script src='<c:url value="/resources/js/jquery.stepy.js" />'></script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
