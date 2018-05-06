<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/css/jquery.printarea.css" />' rel="stylesheet" media="all">
<div class="row">
	<section class="panel">
		<header class="panel-heading">
		<button type="button" onclick="javascript :history.back(-1);" class="goback hidden" ><i class="theme-color fa fa-chevron-left"></i>&nbsp;&nbsp;</button>
		<button type="button" class="btn delete_btn" id="sw_btn" onclick="swithcNo()">隐藏名称</button>
		<select  id="stype" class="btn delete_btn" style="height:33px;marin-left:20px;">
			<option value ="col-lg-12 col-md-12 col-sm-12 col-xs-12">一排显示一个</option>
			<option value ="col-lg-6 col-md-6 col-sm-6 col-xs-6" selected = "selected">一排显示两个</option>
			<option value ="col-lg-4 col-md-4 col-sm-4 col-xs-4" >一排显示三个</option>
			<option value ="col-lg-3 col-md-3 col-sm-3 col-xs-3">一排显示四个</option>
			<option value ="col-lg-2 col-md-2 col-sm-2 col-xs-2">一排显示六个</option>
		</select>
		<span class="pull-right"><button type="button" class="btn delete_btn" onclick="print()">打印</button></span>
		<span class="pull-right" style="margin-right:5px;"><button type="button" class="btn delete_btn" onclick="download()">下载</button></span>
		</header>
		<div class="panel-body printObj" style="outline: none;overflow-y:scroll;">
			<c:forEach items="${qcodes}" var="qcode">
				<div class="col-xs-6 qcode">
					<div class="qcodeNo">${qcode.name }</div>
					<div><img src="${qcode.qCode }" width="100%" data-no="${qcode.name }"></div>
					
				</div>
			</c:forEach>
		</div>
	</section>
	
</div>
<script src='<c:url value="/resources/js/jquery.printarea.js" />'></script>
<script>
	$(function() {
		var app = navigator.userAgent.indexOf("fanandroid");
		if(app!=-1){
			$(".goback").removeClass("hidden");
		}
		//添加促发事件
		 $('#stype').change(function(){
			 $(".qcode").each(function(){
				 $(this).removeClass();
				 $(this).addClass("qcode");
				 $(this).addClass($('#stype').val());
			 });
		 });
	});
	function swithcNo(){
		if($("#sw_btn").html()=="隐藏名称"){
			$(".qcodeNo").hide();
			$("#sw_btn").html('显示名称');
		}else{
			$(".qcodeNo").show();
			$("#sw_btn").html('隐藏名称');
		}
	}
	function print(){
		var options = {
				extraHead: '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>',
				
	            retainAttr : ["id","class","style"], 
	            popTitle  : '二维码打印'
			}
		$(".printObj").printArea(options);
	}
	
	function download() {
		$(".printObj img").each(function(index) {
			var src = $(this).attr("src");
			var no = $(this).attr("data-no");
			console.log(src);
			downloadImage(src, no);
		})
	}
	
	function downloadImage(src, no) {
	    var canvas = document.createElement('canvas');
	    var img = document.createElement('img');
	    img.onload = function(e) {
	        canvas.width = img.width;
	        canvas.height = img.height;
	        var context = canvas.getContext('2d');
	        context.drawImage(img, 0, 0, img.width, img.height);
	        var imgsrc = canvas.toDataURL("image/jpg");
        	savaFile(imgsrc, 'image_'+no+'.jpg');
	        /* var navigatorName = "Microsoft Internet Explorer"; 
	        if( navigator.appName == navigatorName ){ 
	        	console.debug("ie");
	        	window.navigator.msSaveBlob(canvas.msToBlob(),'image_'+no+'.jpg');
	        } else {
	        	console.debug("not ie");
	        	var imgsrc = canvas.toDataURL("image/jpg");
	        	savaFile(imgsrc, 'image_'+no+'.jpg');
	        } */
	    }
	    img.src = src;
	}
	
	var savaFile=function(data,filename)
    {
        var save_link=document.createElementNS('http://www.w3.org/1999/xhtml', 'a');
        save_link.href=data;
        save_link.download=filename;
        var event=document.createEvent('MouseEvents');
        event.initMouseEvent('click',true,false,window,0,0,0,0,0,false,false,false,false,0,null);
        save_link.dispatchEvent(event);
    };
</script>

