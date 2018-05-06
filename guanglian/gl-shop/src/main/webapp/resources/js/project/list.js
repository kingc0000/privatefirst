var validator;
var oTable;
var filterjson;
document.write('<script src="'+_context+'/resources/js/project/toastr.js"></script>');
var touchpage=0;

//自定义按钮
//datatable删除按钮
var dt_del_btn = $('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-trash-o" style="color:red"></i>删除</button>');
//datatable的语言定义
var opaging= {"sFirst": "首页","sPrevious": "前一页","sNext": "后一页","sLast": "尾页"};
if($(window).width()<768){
	opaging= {"sFirst": "<i class='fa fa-step-backward'></i>","sPrevious": "<i class='fa fa-backward'></i>","sNext": "<i class='fa fa-forward'></i>","sLast": "<i class='fa fa-step-forward'></i>"};
}

$(function() {
	var form=$("#storeform");
	if(form.length>0){
		validator = $("#storeform").validate({
			invalidHandler : function() {
				return false;
			},
			//指明错误放置的位置
			errorPlacement : function errorPlacement(error, element) {
				element.after(error);
			},
			submitHandler : function() {
				// 表单的处理
				//alert(JSON.stringify($('#storeform').serializeJson()));
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
						refreshData();
						$('#loading').hide();
					},
					error : function(xhr, textStatus, errorThrown) {
						alert('error ' + errorThrown);
						$('#loading').hide();
					}
				});
				return false;// 阻止form提交
			}
		});
	}
	
	$('#slist').on('click', 'a.edit', function(e) {
		e.preventDefault();
		
		/* Get the row as a parent of the link that was clicked on */
		var nRow = $(this).parents('tr')[0];
		var dofn=false;
		if($(this).hasClass("hasfn")){
			dofn=true;
		}
		/* No edit in progress - let's start one */
		editRow(oTable, nRow,dofn);
		if (typeof(validator) != "undefined"){
			validator.resetForm();
			var form = $("#storeform");
			if (form.children("div[id='wizard']").length >0) {
				form.children("div[id='wizard']").steps('reinit', 1);
			}
		}
		
		document.documentElement.scrollTop = document.body.scrollTop =0;
	});
	oTable = $('#slist').dataTable();
	//加载删除按钮
	var del_btn_div = $("#del_btn");
	if (typeof(del_btn_div)!='undefined') {
		$(del_btn_div).addClass("dt_btn pull-right");
		$(del_btn_div).append(dt_del_btn);
		$(dt_del_btn).on('click',function(){
			deldata();
		});
	}
	var wSize = $(window).width();
	if(wSize>769){
		touchpage=-1;
	}
});
function retrieveData(sSource, aoData, fnCallback) {
	var data = {
		"aoData" : JSON.stringify(aoData)
	};
	if (!jQuery.isEmptyObject(filterjson)) {
		data = $.extend({}, {
			"aoData" : JSON.stringify(aoData)
		}, filterjson);
	}
	$.ajax({
		type : "POST",
		url : sSource,
		dataType : "json",
		data : data,
		success : function(data) {
			// $("#url_sortdata").val(data.aaData);
			if (typeof(data.response)=='object'&&data.response.status==-1) {
				console.log(data.response.statusMessage);
				$("#slist_processing").html("<span class='alert alert-danger'>"+data.response.statusMessage+"</span>");
				$("#slist_processing").hide(5000);
			} else {
				fnCallback(data); // 服务器端返回的对象的returnObject部分是要求的格式
			}
		},
		error : function(data, xhr, textStatus, errorThrown) {
			console.log(data.responseText);
			fnCallback(data);
		}
	});
}
function changeEdittable(aferfunction) {
	// if($("#edittable").is(":visible")){
	// $('#edittable').hide();
	// }else{
	$('#edittable').show();
	$('#edittile').html("新增");
	// }

	// 清空表单
	clearform();
	if (typeof(validator) != "undefined"){
		validator.resetForm();
		var form = $("#storeform");
		if (form.children("div[id='wizard']").length >0) {
			form.children("div[id='wizard']").steps('reinit', 1);
		}
	}
	// preset();
	//回调
	if(aferfunction !=null && aferfunction!=''){
		aferfunction();
	}
	document.documentElement.scrollTop = document.body.scrollTop =0;
}
var isclearform=false;
	function clearform() {
		isclearform=false;
	// 清空表单
	$(':input', '#storeform').not(':button, :submit, :reset').val('')
			.removeAttr('checked').removeAttr('selected');
	$("#storeform textarea").val("");
	
	/**有富文本框的*/
	if($("#storeform .ckeditor")!='undefined'&&$("#storeform .ckeditor").length>0) { //ck
		  
			$.each($("#storeform .ckeditor"),function(i,o){
				for ( instance in CKEDITOR.instances )  
				{  
					if(instance==$(o).attr("id")){
						CKEDITOR.instances[instance].setData("");
					}
				} 
			});
	}
	
	//清空dropify图片控件的内容
	if($("#storeform .dropify-event")!='undefined'&&$("#storeform .dropify-event").length>0) {
		$.each($("#storeform .dropify-event"),function(i,o){
			var drDestroy = $(o).data("dropify");
			if (typeof drDestroy == 'object') {
				drDestroy.destroy();
				drDestroy.settings.defaultFile = '';
				drDestroy.init();
			}
		});
	}
	isclearform=true;
}
	
function editsubrow(sProp,data){
	for ( var subProp in data) {
		if (data[subProp] != null && typeof data[subProp] == 'object') {
			editsubrow(sProp+"."+subProp,data[subProp]);
		}else{
			var obj=getobj( sProp + '.' + subProp);
			if(obj.length>0){
				editvalue(obj,data,subProp);
			}
		}
	}
}
function editcheckbox(data,sProp){
	if ($.isArray(data)) {
		$(data).each(function(i, p) {
			$("input[name='" + sProp + "']").each(function() {
				if (this.title == p || this.title == '"' + p + '"') {
					this.checked = true;
					// this.prop("checked",true);
					return true;
				}
			});
		});
	} else {
		var checkvalue = false;
		if (data == true || data == "true") {
			checkvalue = true;
		}
		$('#' + sProp).prop("checked", checkvalue);
	}
}

function editvalue(obj,data,sProp){
	if (obj.hasClass("dropify-event")) { //增加dropify图片控件的处理
		var drDestroy = obj.data("dropify");
		if (typeof drDestroy == 'object') {
			drDestroy.destroy();
			if((data[sProp]!=null||data[sProp]!='')){
				drDestroy.settings.defaultFile = aData[sProp];
			}
			drDestroy.init();
		}
	} else if (obj.hasClass("avatar-img")) { //增加cropper图片控件的处理
		if (data[sProp]==''||data[sProp]==null||data[sProp]=='null') {
			obj.parent().removeClass("has-preview");
			obj.attr("src", "");
			//obj.removeAttr("src");
			obj.attr("imgId", ""); //图片ID
			obj.hide();
		} else {
			obj.attr("src", data[sProp]);
			obj.attr("imgId", data["id"]); //图片ID
			obj.parent().addClass("has-preview");
			obj.show();
		}
	} else if(obj.hasClass("ckeditor")){
		for ( instance in CKEDITOR.instances )  
		{  
			if(instance==obj.attr("id")){
				CKEDITOR.instances[instance].setData(data[sProp]);
				break;
			}
			
		} 
	}
	else {
		obj.val(data[sProp]);
	}
}

function getobj(sProp){
	var obj = $("[id='" + sProp + "']");
	if(sProp.indexOf(".")!=-1){
		obj =$('input[name="'+sProp+'"]');
	}
	//有可能是select
	if(obj.length==0){
		obj = $('select[name="' + sProp + '"]');
	}
	if(obj.length==0){
		obj = $('textarea[name="' + sProp + '"]');
	}
	return obj
}
function editRow(oTable, nRow,dofn) {
	// 清空表单
	clearform();
	var interval = setInterval(function(){
		if(isclearform==true){
			clearInterval(interval);
			
			var aData = oTable.fnGetData(nRow);
			for ( var sProp in aData) {
				var obj =getobj(sProp);
				if (obj.attr('type') == "checkbox") {
					editcheckbox(aData[sProp],sProp);
				} else {
					// 增加处理存在子对象的情况，例如description.name的赋值处理
					if (aData[sProp] != null && typeof aData[sProp] == 'object') {
						editsubrow(sProp,aData[sProp]);
					} else {
						if(obj.length>0){
							editvalue(obj,aData,sProp);
						}
					}
				}
			}
			$('#edittile').html("修改");
			$("#edittable").show("slow");
			//是否需要回调函数
			if(dofn==true){
				dolistafterfn(oTable, nRow); //将oTable参数也传递给回调函数
			}
			return;
		}
	}, 500);
	

}
function deldata(sid,ptitle) {
	//是否有权限操作
	var hasright=false;
	if (typeof(sid)=='undefined') {
		var ids = new Array();
		$('.sidCheckbox:checked').each(function(i,o){
			var nRow = $(o).parents('tr')[0];
			var aData = oTable.fnGetData(nRow);
			//判断是否有权限字段
			
			if(typeof(aData["sstatus"])=="sstatus"){
				if(aData["sstatus"]==1){
					ids[i]=$(o).val();
				}
				hasright=true;		
				
			}else{
				ids[i]=$(o).val();
			}
			
			
		});
		if (ids.length<1) {
			if(hasright==true){
				alert("请至少选择一个您有删除权限的数据");
			}else{
				alert("请至少选择一个需要删除的数据");
			}
			
			return;
		}
		sid=ids;
	}
	if(ptitle==null){
		if(hasright==true){
			ptitle='一些您没有操作权限的数据已经过滤掉了，您确定剩余您有操作权限的数据吗';
		}else{
			ptitle='您确定要删除吗';
		}
	}
	var rdel = confirm(ptitle);
	if (rdel == false)
		return;
	var url = "remove.shtml";
	$('#loading').show();
	$.ajax({
		type : "POST",
		url : url,
		data : "listId=" + sid,
		success : function(result) {
			$('#loading').hide();
			if(result!=null&&result.length>0) {
				$(result).each(function(i, o){
					var status = o.status;
					var index = "["+(i+1)+"]:&nbsp;&nbsp;";
					var msg = o.statusMessage;
					if(status==0||status==9999) {
						refreshData();
						if(msg!=null&&msg!=''){
							toastr.success(index+msg);							
						} else {
							toastr.success(index+'操作成功！');
						}
					} else if (status == 9998) {
						if(msg!=null&&msg!=''){
							toastr.error(index+msg);							
						} else {
							toastr.error(index+'提交失败！'+',编码重复');
						}
					}else if (status == 9997) {
						if(msg!=null&&msg!=''){
							toastr.error(index+msg);							
						} else {
							toastr.error(index+'无法删除！ 下级数据没有清空');
						}
					}else if (status == 11) {
						toastr.success('操作成功！');
						afterRemove(result, sid); //自定义外部接口
					} else {
						if(msg!=null&&msg!=''){
							toastr.error(index+msg);							
						} else {
							toastr.error(index+'提交失败！');
						}
					}
				});
			} else if(result!=null){
				var status = result.response.status;
				var msg = result.response.statusMessage;
				if(status==0||status==9999) {
					refreshData();
					if(msg!=null&&msg!=''){
						toastr.success(msg);							
					} else {
						toastr.success('操作成功！');
					}
				} else if (status == 9998) {
					if(msg!=null&&msg!=''){
						toastr.error(msg);							
					} else {
						toastr.error('提交失败！'+',编码重复');
					}
				} else {
					if(msg!=null&&msg!=''){
						toastr.error(msg);							
					} else {
						toastr.error('提交失败！');
					}
				}
			}
		},
		
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			toastr.error('提交失败！');	
			
		}
	});
	$('#edittable').hide();
}

function refreshData(sid) {
	oTable = $('#slist').dataTable();
	var oSettings = oTable.fnSettings();
	if (touchpage!=-1) {
		var sleng=oSettings._iDisplayLength;
		var stotal=oSettings._iRecordsTotal;
		if(touchpage*sleng<stotal){
			oSettings._iDisplayStart=touchpage*sleng;
		}else{
			if(touchpage!=0){
				touchpage--;
				toastr.error('已经是第最后一页了');
				return;	
			}
		}
	}
	//重设sid,就一个参数用法
	if(typeof(sid)!="undefined"){ 
		var str =oSettings.sAjaxSource;
		str =str.substring(0,str.indexOf("=")+1)+sid;
		oSettings.sAjaxSource=str;
	}
	oTable.fnReloadAjax(oSettings);
}

function doaction(uid, url,con) {
	console.log(url);
	if(con!=null){
		var rdel = confirm('您确定要'+con+'吗？');
		if (rdel == false)
			return;
	}
	$('#loading').show();  
	$.ajax({
		type : "POST",
		url : url,
		data : "listId=" + uid,
		success : function(response) {

			var status = response.response.status;
			$('#loading').hide();
			if (status == 0 || status == 9999) {
				refreshData();
				toastr.success(response.response.statusMessage);
			} else {
				toastr.error(response.response.statusMessage);
			}
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			toastr.error(con+'失败');
		}
	});
}

function valueReplace(v) {
	v=v.toString().replace(new RegExp('(["\"])', 'g'),"\&quot;"); 
	return v; 
}
/**
 * 采用FormData方式提交
 * @param durl
 * @param check 有值的时候不再是true，false的形式
 * @returns {Boolean}
 * 
 */
function fomdatesubmit(durl,check){
	var url="save.shtml" ;
	if(durl !=null && durl !=""){
		url =durl;
	}
	/* $("#storeform").submit();  */
	if(typeof validator != "undefined"){
		if(!validator.form())return false;
	}
	if(typeof(check)=="undefined"){
		/**有checkbox的*/
		var obj1 = $("#storeform input:checked");
		$(obj1).each(function(i,o) {
			$(o).val("true");
				});
	}
	/**有select的*/
	/**有富文本框的*/
	if($("#storeform .ckeditor")!='undefined'&&$("#storeform .ckeditor").length>0) { //ck
		 
			$.each($("#storeform .ckeditor"),function(i,o){
				for ( instance in CKEDITOR.instances )  
				{  
					if(instance==$(o).attr("id")){
						$(o).val(CKEDITOR.instances[instance].getData());
					}
				} 
			});
	}
	var data = new FormData($('#storeform')[0]);
	
	var imgData = $(".avatar-img").attr("src");
	$(".avatar-img").each(function(i, item){
		var imgData = $(item).attr("src");
		var imgId = $(item).attr("imgId");
		if(imgData!=undefined&&imgData!=""&&imgData.split(',').length>1&&imgId=="") {
			// dataURL 的格式为 “data:image/png;base64,****”,逗号之前都是一些说明性的文字，我们只需要逗号之后的就行了
			var imagetype = "image/jpeg";
			if (imgData.indexOf("image/png")>0) {
				imagetype = "image/png";
			} else if (imgData.indexOf("image/gif")>0) {
				imagetype = "image/gif";
			}  
			imgData = imgData.split(',')[1];
			imgData = window.atob(imgData);
			
			var ia = new Uint8Array(imgData.length);
			for (var i = 0; i < imgData.length; i++) {
			    ia[i] = imgData.charCodeAt(i);
			};
			var blob = new Blob([ia], {type:imagetype});
			data.append("images", blob);
		}
	});
/*	if(imgData!="" && imgData!=undefined && imgData.split(',').length>1) {
		// dataURL 的格式为 “data:image/png;base64,****”,逗号之前都是一些说明性的文字，我们只需要逗号之后的就行了
		imgData = imgData.split(',')[1];
		imgData = window.atob(imgData);
		var ia = new Uint8Array(imgData.length);
		for (var i = 0; i < imgData.length; i++) {
		    ia[i] = imgData.charCodeAt(i);
		};
		var blob = new Blob([ia], {type:"image/jpeg"});
		data.append("images", blob, "upload.jpg");
	}*/
	$('#loading').show();
	$.ajax({
		type : "POST",
		cache: false, //cache设置为false，上传文件不需要缓存。
		url : url,
		data : data,
		async : true,
		processData: false, //processData设置为false。因为data值是FormData对象，不需要对数据做处理。
		contentType: false, //由<form>表单构造的FormData对象，且已经声明了属性enctype=multipart/form-data，所以这里设置为false
		success: function(response, stat){  
			$('#loading').hide();
			var status = response.response.status;
			if (status == 0 || status == 9999) {
				toastr.success('操作成功！');
			} else {
				toastr.error('提交失败！');
			}
			$('#edittable').hide();
			refreshData();
			
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			alert('error ' + errorThrown);
		}
	});
	return false;
}
/**
 * checkbox的转换
 * @param aid
 */
function setActive(aid,exfun){
	if($('#'+aid).prop("checked")){
		$('#'+aid).prop("checked",false);
	}else{
		$('#'+aid).prop("checked",true);
	}
	if(exfun){
		exfun();
	}
}
function dolast(){
	if(touchpage>0){
		touchpage--;
		refreshData();
	}else{
		touchpage=0;
		toastr.error('已经是第一页了');
	}
}
function donext(){
	touchpage++;
	refreshData();
}
//定义datatables列表页面checkbox的选择事件
function definedSidCheckbox() {
	$('.sidCheckbox').on('click', function(){
		var o = $(this);
		var nTr = $(o).parents('tr')[0];
		$(nTr).toggleClass('selected');

	});
	$('.allCheckbox').on('click', function(){
		var checked = $(this).prop("checked");
		if(checked) {
			$('.sidCheckbox').prop("checked",true);
			$('.sidCheckbox').parents('tr').addClass('selected');
		} else {
			$('.sidCheckbox').prop("checked",false);
			$('.sidCheckbox').parents('tr').removeClass('selected');
		}
	});
	var width = $(document.body).width();
	if(width<=768){
		oTable.fnSetColumnVis(0,false);
	} else {
		oTable.fnSetColumnVis(0,true);
	}
	//屏幕大小变化监听，当小于768时，隐藏checkbox
	$(window).resize(function(){
	   //process here
		var width = $(document.body).width();
		if(width<=768){
			oTable.fnSetColumnVis(0,false);
		} else {
			oTable.fnSetColumnVis(0,true);
		}
	});
}
