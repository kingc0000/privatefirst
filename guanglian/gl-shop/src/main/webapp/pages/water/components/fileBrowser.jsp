<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
 request.setCharacterEncoding("UTF-8");
%>
<%@ page session="false" %>
<link href='<c:url value="/resources/css/gallery.css" />' rel="stylesheet">
 <!--[if IE]>
		<script href='<c:url value="/resources/js/html5shiv.js" />'></script>	
	<![endif]-->


<script>
	//CKEditor=option0&CKEditorFuncNum=409&langCode=zh-cn
	var cur_editor = '${CKEditor}';
	var funcNum = '${CKEditorFuncNum}';
	var psize=12;
	var pages=0;
	var images=null;
	$(document).ready(function(){
		getImages();
		
		 
	});
	
	function getImages(){
		$('#loading').show();
		$.ajax({  
			type: 'POST',
			  url: _context+"/admin/content/images/paging.shtml",			 
			  success: function(is) {
				  images=is;
				  pages = parseInt(images.length/psize);
				  if(images.length%psize>0) {
						pages++;
					}
					writeImage(pages);
					$('#loading').hide();
				 }
			 	});
	}
	function selectImage(img) {
		var image =_context+ '/static/DEFAULT/IMAGE/' + img;
		/* for ( instance in CKEDITOR.instances )  
		{  
			console.log(instance);
			if(instance==target){
				window.opener.CKEDITOR.tools.callFunction(2, image);
				break;
			}
		}  */
		//
		window.opener.CKEDITOR.tools.callFunction(funcNum, image);
		window.close();
	}

	function saveImage(file,filename){
		$.ajaxFileUpload({
			type: 'POST',
			  url: _context+'/admin/content/image/upload.shtml',				  
			  fileElementId:file,
	            dataType: 'json', 
	            secureuri : false,
	            data:{pid:file,fname:filename},
			  success: function(is) {
				  if(is.response.status == -2){
					  alert('<s:message code="category.picture.type" text="Type"/>'+" jpg|jpeg|bmp|gif|png");
				  }else if(is.response.status == -1){
					  alert('<s:message code="message.picture.savefailed" text="Failed"/>');
				  }
				 },
	    		error : function(xhr, textStatus, errorThrown) {
	    			$('#loading').hide();
	    			//toastr.error(con+"失败！");
	    		}
			 	});
		}
	function removeImage(filename,page)	{
		var con= confirm('确定删除" ' + filename +' "? ');
		if(con==false) return;
		$('#loading').show();
		$.ajax({
			type: 'POST',
			  url: _context+'/admin/content/removeImage.shtml',	
	            dataType: 'json', 	           
	            data:"name="+filename,
			  success: function(is) {
				  $('#loading').hide();
				  if(is.response.status < 0){
					  alert('图片删除失败');
				  }else{
					  pages=page;
					  getImages();
				  }
				  
				 },error : function(xhr, textStatus, errorThrown) {
						$('#loading').hide();
						//toastr.error(con+"失败！");
					}
			 	});
	}
	
	function getImageName(filename){
		var index = filename.lastIndexOf(".");
		var myDate=new Date().getTime();
		return Math.random().toString(6).substr(2)+myDate.toString().substr(2)+filename.substr(index);
	}
	function writeImage(page){
		 $('#showImage').html('');
		//var body='<div class="col-sm-3" style="height:300px;"><input type="file" name="upload_-1" id="upload_-1"  class="dropify-event"></div>';
		var body='<ul class="grid cs-style-3">';
		var lastsize=images.length;
		//
		if(page*psize>lastsize){
			page--;
		}
		if((page*psize+psize)<images.length){
			lastsize=page*psize+psize;
		}
		//for(var i=lastsize-1;i>=page*psize;i--){
		for(var i=page*psize; i<lastsize;i++){			
			body+=' <li style="border:1px solid #ccc;padding:3px;text-align:center"><figure><a  href="#" class="" onclick="selectImage(&quot;'+images[i]+'&quot;)">  <img style="max-height:300px;display:inline;width:auto;" src="'+_context+'/static/DEFAULT/IMAGE/'+images[i]+'"/></a><figcaption><h3>'+images[i];
			body+='</h3><a  href="#" onclick="removeImage(&quot;'+images[i]+'&quot;,'+page+')"><s:message code="category.delete" text="Delete"/></a> </figcaption> </figure></li>';
		}
		if(pages>0){
			var pagenum='<nav><ul class="pagination">';
			for(var j=0;j<pages;j++){
				pagenum +='<li';
				if(j==page){
					pagenum +=' class="active"'
				}
				pagenum +='><a href="#" onclick="writeImage('+j+')">'+(j+1)+'</a></li>';
			}
			pagenum+='</ul></nav>';
			$('#paging').html(pagenum);
			
		}
		 $('#showImage').html(body);
	}

</script>
<div class="container-fluid">
<div id="paging" class="col-sm-12"></div>
<div class="row">
	<div id="showImage" >
</div>

</div>
</div>
 
<script src='<c:url value="/resources/assets/ckeditor/ckeditor.js" />'></script>
<script src='<c:url value="/resources/js/ajaxfileupload.js" />'></script>
<script src='<c:url value="/resources/js/modernizr.custom.js"/>'></script>     

			      			     
			      			     
