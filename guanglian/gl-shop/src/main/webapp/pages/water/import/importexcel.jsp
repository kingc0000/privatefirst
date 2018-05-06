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
<link href='<c:url value="/resources/css/jquery.steps.css" />' rel="stylesheet">
<link rel="stylesheet" type="text/css" href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' />

<div class="modal fade" id="import-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
      <div class="modal-dialog mag-dialog" >
        <div class="modal-content" >
            <div class="modal-header">
              <button class="close " data-dismiss="modal" type="button">&times;</button>
              <h4 class="modal-title " >数据导入</h4>
            </div>
            <div class="modal-body" style="height:100%;width:100%" >
            	<section class="panel" >
	<header class="panel-heading"><span id="ititle"></span>导入</header>
	<div class="panel-body">
                              <form id="importform" action="#">
                                  <div id="wizard">
                                      <h3>请选择导入文件</h3>
                                      <section>
                                      <div class="alert alert-block alert-danger fade in">
		                                  
		                                  <strong>注意</strong>当前版本只支持Excel格式的文件导入，如果没有选择观测时间和观测者则系统自动输入
		                              </div>
                                          <div class="form-group clearfix">
                                          <label class="control-label col-md-4 control-required">请选择导入文件</label>
                                          <div class="col-md-8">
                                              <div class="fileupload fileupload-new" data-provides="fileupload">
                                                <span class="btn btn-white btn-file">
                                                <span class="fileupload-new"><i class="fa fa-paper-clip"></i>请选择导入文件</span>
                                                <span class="fileupload-exists"><i class="fa fa-undo"></i>修改</span>
                                                <input type="file" name="uploadfile" id="uploadfile" class="default required" />
                                                </span>
                                                  <span class="fileupload-preview" style="margin-left:5px;"></span>
                                                  <a href="#" class="close fileupload-exists" data-dismiss="fileupload" style="float: none; margin-left:5px;"></a>
                                              </div>
                                          </div>
                                      </div>
                                     
                                      <div class="form-group clearfix">
	                                   <label class="col-lg-4 col-sm-4 ">观察者(当所有的观测者为同一人时选择)</label>
	                                   <div class="col-lg-8 col-sm-8">
	                                     <div class="input-group">
	                                   	   <input class="form-control" id="username" name="username"   />
	                                   	   <span class="input-group-btn " >
	                                   	   	<button class="btn btn-info" type="button" data-toggle="collapse" aria-haspopup="true" href="#collapseExample" aria-expanded="false"	aria-controls="collapseExample"><i class="fa  fa-user"></i></button>
	                                         <button class="btn btn-info" type="button" onclick="cancelUname()"><i class="fa  fa-undo"></i></button>
	                                       </span>
		                             	  </div>
	                                   </div>
	                                   <div class="collapse" id="collapseExample">
										<div class="well" id="selectNameList"></div>
									   </div>
                                	</div>
                                	
                                	
                                      </section>
                                      <h3>请选择导入项目</h3>
                                      <section>
										<div class="alert alert-block alert-danger fade in">
		                                  
		                                  <strong>注意!</strong> 选择框的内容是从导入文件获取的，请选择对应的导入选项！！！ 如有累计值的请按升序排序！！！
		                              </div>
                                          
                                          <div class="form-group clearfix pwellimport" >
                                              <label class="col-lg-2 control-label " >上次累计量</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select pwellimport"></select>
                                              </div>
                                               <label class="col-lg-2 control-label " >本次累计值</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select  pwellimport"></select>
                                              </div>
                                          </div>
                                          <div class="form-group clearfix pwellimport" >
                                              <label class="col-lg-2 control-label " >累计周期</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select pwellimport"></select>
                                              </div>
                                               <label class="col-lg-2 control-label " >水位</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select  pwellimport"></select>
                                              </div>
                                          </div>
                                          <div class="form-group clearfix owellimport" >
                                              <label class="col-lg-2 control-label " > 水位</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select owellimport"></select>
                                              </div>
                                               <label class="col-lg-2 control-label " >水温</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select owellimport"></select>
                                              </div>
                                          </div>
                                          <div class="form-group clearfix iwellimport" >
                                              <label class="col-lg-2 control-label " >上次累计量</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select iwellimport"></select>
                                              </div>
                                               <label class="col-lg-2 control-label " >本次累计值</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select  iwellimport"></select>
                                              </div>
                                          </div>
                                          <div class="form-group clearfix iwellimport" >
                                              <label class="col-lg-2 control-label " >累计周期</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select iwellimport"></select>
                                              </div>
                                               <label class="col-lg-2 control-label " >井内水位</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select iwellimport"></select>
                                              </div>
                                          </div>
                                          <div class="form-group clearfix emonitorimport" >
                                              <label class="col-lg-2 control-label " > 数据</label>
                                              <div class="col-lg-10">
                                                  <select class="form-control select emonitorimport"></select>
                                              </div>
                                          </div>
                                          <div class="form-group clearfix hasoimport" >
                                              <label class="col-lg-2 control-label control-required " >观测时间</label>
                                              <div class="col-lg-4 ">
                                                  <select class="form-control required select hasoimport"></select>
                                              </div>
                                               <label class="col-lg-2 control-label control-required " >观测者</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control required select hasoimport"></select>
                                              </div>
                                          </div>
                                          <div class="form-group clearfix nooimport" >
                                              <label class="col-lg-2 control-label control-required " >观测时间</label>
                                              <div class="col-lg-10">
                                                  <select class="form-control required select nooimport"></select>
                                              </div>
                                          </div>
                                      </section>
                                      <h3>确认导入</h3>
                                      <section>
                                          <div class="form-group clearfix">
                                              <div class="col-lg-12">
                                                  <div class="progress progress-striped active progress-sm">
                            				     	 <div id ="progress" class="progress-bar progress-bar-success" style="line-height:14px;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
                                					  	<span id="spanprogress">0%</span>
                                					  </div>
                             					 </div>
                                              </div>
                                              <div>
                                              </div>
                                          </div>
                                          <div id="result" class="alert alert-block alert-danger fade in" style="display:none;word-wrap: break-word; word-break: normal; ">
		                                  
		                                  <strong><s:message code="datacenter.note" text="Note "/>!</strong><s:message code="import.error.line" text="Error "/>：<span id="errocols"> </span>！！！
		                              </div>
                                      </section>
                                  </div>
                                  <input type="hidden" id="iid"/>
                              </form>

	</div>

</section>
            </div>
        </div>
      </div>
    </div>

<script src='<c:url value="/resources/js/jquery.steps.min.js" />'></script>
<script src='<c:url value="/resources/js/jquery.stepy.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script>
<script src='<c:url value="/resources/js/ajaxfileupload.js" />'></script>
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>
<script type="text/javascript">

jQuery(document).ready(function() {
	var selectfile="";
	var form = $("#importform");
	form.children("div").steps({
	    headerTag: "h3",
	    bodyTag: "section",
	    transitionEffect: "slideLeft",
	    onStepChanging: function (event, currentIndex, newIndex) {
	    	
	    	form.validate({
	    		ignore: ":disabled,:hidden"
	    	});
	    	 var result= form.valid();
	    	 if (result==false) return false;
	    	if(currentIndex==0 && newIndex==1){
	    		if(selectfile!=$("uploadfile").val()){
	    			selectfile=selectfile=$("uploadfile").val();
	    			getCols();
	    		}
	    		if($('#username').val()!=""){
    				$('.nooimport').show();
    				$('.hasoimport').hide();
    			}else{
    				$('.nooimport').hide();
    				$('.hasoimport').show();
    			}
	    		
	    		return true;
	    	}else {
	    		return true;
	    	}
	        
	    },
	    onFinishing: function (event, currentIndex) {
	    	form.validate({
	    		ignore: ":disabled,:hidden"
	    	});
	    	$("#result").hide();
			$("#errocols").html('');
	    	//$("ul[role='menu'] li").addClass("disabled");
	    	//$(".done").addClass("disabled");
	    	//$("ul[role='menu'] li a").attr("onclick","return false;");
	    	$("ul[role='menu'] li").hide();
	    	setCols();
	    	setTimeout(getProgress,1000);
	    	
	        return form.valid();
	    }
	});
	
	 //加载用户
    getList('请选择用户','username','<c:url value="/water/department/users.shtml" />', 'selectNameList');

});
function getCols(){
	$.ajaxFileUpload({
        url: '<c:url value="/water/pwell/getCols.shtml" />', 
        type: 'post',
        secureuri: false, //一般设置为false
        fileElementId: 'uploadfile', // 上传文件的id、name属性名
        dataType: 'json', //返回值类型，一般设置为json、application/json
        async: false,
        success: function(data, stat){  
        	 var status =data.response.status;
        	if(status==-1){
        		toastr.error('导入失败');
        		$("ul[role='menu'] li").removeClass("disabled");
    	    	$(".done").removeClass("disabled");
        	}else if(status==-2){
        		toastr.error('导入文件为空');
        		$("ul[role='menu'] li").removeClass("disabled");
    	    	$(".done").removeClass("disabled");
        	}else if(status==0){
        		var arr = data.response.cols.split(',');
        		var output = [];
        		for(var j in arr){
   					output.push('<option value="'+ j +'">'+ arr[j] +'</option>');
        		}
    			output.push('<option value=""></option>');
        		$('.select').each(function(i,p){
        			$(this).html(output.join(''));;
        			$(this).val(i);
        		});
        	}
        },
        error: function(data, status, e){ 
        	toastr.error('导入失败');
        	$("ul[role='menu'] li").removeClass("disabled");
	    	$(".done").removeClass("disabled");
        	//return false;
        }
    });
}

function setCols(){
	var dataa=new Array()
	
	$('.select').each(function(i,p){
		if($(this).css('display')!="none"){
			if($(this).val()==null || $(this).val()==""){
				dataa[dataa.length]=-1;
			}else{
				dataa[dataa.length]=$(this).val();
			}
		}
		
	});
	
	$.ajaxFileUpload({
        url: 'importCols.shtml', 
        type: 'post',
        secureuri: false, //一般设置为false
        fileElementId: 'uploadfile', // 上传文件的id、name属性名
        dataType: 'json', //返回值类型，一般设置为json、application/json
        data: {"cols":dataa,
        		"uname":$('#username').val(),
        		"pid":$('#iid').val()
        	},
        success: function(data, stat){  
        	 var status =data.response.status;
        	if(status==-1){
        		toastr.error('导入失败');
        	}else if(status==-2){
        		toastr.error('导入文件为空');
        	}else if(status==0){
        		$('#progress').width('100%');
        		$('#spanprogress').html('100%');
        		toastr.success('导入完成');
        	}else if(status==9999){
        		$('#progress').width('100%');
        		$('#spanprogress').html('100%');
        		toastr.success('导入完成');
        		var arr = data.response.cols;
        		if(arr !=null && arr !=""){
        			$("#result").show();
        			$("#errocols").html(arr);
        			
        		}
        	}
        	refreshData();
        },
        error: function(data, status, e){ 
        	toastr.error('导入失败');
        	//return false;
        }
    });
}

function cancelUname(){
	$('#username').val('');
	$('#username').removeAttr("readonly");
}

function getProgress(){
	$.ajax({
		type : "POST",
		url : '<c:url value="/water/pwell/getProgress.shtml" />',
		data:"pro="+$('#progress').width(),
		dataType: 'json',
		success : function(response) {
			var status =response.response.status;
			if(status==0){
				var arr = response.response.progress;
				$('#progress').width(arr*100+'%');
				$('#spanprogress').html(arr*100+'%');
				setTimeout(getProgress,1000);
			}else if(status==9999){
				$('#progress').width('100%');
			}
		}
	});
}

function importexcel(){
	$("#result").hide();
	$("#errocols").html('');
	$('#spanprogress').width('0%');
	$('#progress').width('0%');
	$('#uploadfile').val('');
	$('#uploadfile').trigger('reset.fileupload');
	//取消用户选择
	cancelUname();
	var form = $("#importform");
	if (form.children("div[id='wizard']").length >0) {
		form.children("div").steps('reinit', 1);
		$("ul[role='menu'] li").show();
		$("ul[role='menu'] li").removeClass("disabled");
	}

	$('#import-modal').modal('show');
}

</script>
