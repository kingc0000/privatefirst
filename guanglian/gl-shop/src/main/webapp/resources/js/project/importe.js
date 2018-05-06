var url='importCols.shtml';
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
	    		if(selectfile!=$("#uploadfile").val()){
	    			selectfile=$("#uploadfile").val();
	    			getCols();
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
	    	$("#importform ul[role='menu'] li").hide();
	    	setCols();
	    	setTimeout(getProgress,1000);
	    	
	        return form.valid();
	    }
	});
	
});
function getCols(){
	$.ajaxFileUpload({
        url: _context+'/water/pwell/getCols.shtml', 
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
        		$('select').each(function(i,p){
        			$(this).html(output.join(''));;
        			$(this).val(i);
        		});
        	}
        	
        },
        error: function(data, status, e){ 
        	toastr.error('导入失败');
        	$("#importform ul[role='menu'] li").removeClass("disabled");
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
        url: url, 
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

function getProgress(){
	$.ajax({
		type : "POST",
		url : _context+'/water/pwell/getProgress.shtml',
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

function importexcel(rl){
	if(typeof(rl)=="undefined"){ 
		url='importCols.shtml';
	}else{
		url='importPoints.shtml';
	}
	$("#result").hide();
	$("#errocols").html('');
	$('#spanprogress').width('0%');
	$('#progress').width('0%');
	$('#uploadfile').val('');
	$('#uploadfile').trigger('reset.fileupload');
	var form = $("#importform");
	if (form.children("div[id='wizard']").length >0) {
		form.children("div").steps('reinit', 1);
		$("ul[role='menu'] li").removeClass("disabled");
		$("ul[role='menu'] li").show();
	}

	$('#import-modal').modal('show');
}