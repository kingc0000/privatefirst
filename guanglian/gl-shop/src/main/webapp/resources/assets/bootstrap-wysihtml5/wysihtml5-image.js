
/*
 * 定义图片上传需要传递的参数,页面调用时，可以传递参数至后台
 * <meta name="csrf-token" content="csrf-token"/>
 * <meta name="csrf-param" content="csrf-param"/>
 * 自定义图片上传路径
 * <meta name="csrf-image-url" content="csrf-image-url"/> 
 * */
var csrf_token = $('meta[name=csrf-token]').attr('content');
var csrf_param = $('meta[name=csrf-param]').attr('content');
var imageCustomTemplates = {
		image : function(locale, options) {
//      var locale = context.locale;
//      var options = context.options;
			var size = (options && options.size) ? ' btn-'+options.size : '';
			return "<li>" +
			"<div class='bootstrap-wysihtml5-insert-image-modal modal fade'>" +
			"<div class='modal-dialog'>" +
			"<div class='modal-content'>" +
			"<div class='modal-header'>" +
			"<a class='close' data-dismiss='modal'>×</a>" +
			"<h4 class='modal-title'>" + locale.image.insert + "</h4>" +
			"</div>" +
			"<div class='modal-body'>" +
			"<div class='upload-picture'>" +
			"<form accept-charset='UTF-8' class='form-horizontal' id='wysiwyg_image_upload_form' method='post' enctype='multipart/form-data'>"+
			"<div style='display:none'>"+
			"<input name='charset' value='utf8' type='hidden'></input>"+
			"<input name='"+ csrf_param +"' value='"+ csrf_token +"' type='hidden'></input>" +
			"</div>" +
			"<div class='form-group'>" +
			"<div class='col-xs-9 col-md-10'>"+
			"<input value='' accept='image/jpeg,image/gif,image/png' class='form-control' id='wysiwyg_image_picture' name='pict' type='file' required='required'></input>"+
			"</div>" +
			"<div class='col-xs-3 col-md-2'>"+
			"<input class='btn btn-primary' id='wysiwyg_image_submit' name='commit' type='submit' value='上传'></input>"+
			"</div>" +
			"</div>" +
			"</form>"+
			"</div>"+
			"<div class='form-group'>" +
			"<input type='text' value='http://' id='bootstrap-wysihtml5-picture-src' class='bootstrap-wysihtml5-insert-image-url form-control large'>"+
			"</div>" +
			"<div id='wysihtml5_upload_notice'>"+
			"</div>"+
			"</div>" +
			"<div class='modal-footer'>" +
			"<a href='#' class='btn btn-default' data-dismiss='modal'>" + locale.image.cancel + "</a>" +
			"<a class='btn btn-primary' data-dismiss='modal' href='#'>" + locale.image.insert + "</a>"+
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"<a class='btn btn-default" + size + "' data-wysihtml5-command='insertImage' title='" + locale.image.insert + "' tabindex='-1'><i class='fa fa-picture-o'></i></a>" +
			"</li>";
		}
};
jQuery(document).ready(function() {

  //提交图片动作
  var csrf_image_url = $('meta[name=csrf-image-url]').attr('content');
  csrf_image_url = csrf_image_url != null ? csrf_image_url : "/images/upload.shtml";
  $('#wysiwyg_image_upload_form').on('submit',function(event){
	  event.stopPropagation();
	  event.preventDefault();
	  $('#wysiwyg_image_submit').val('Uploading');
	  var wysiwyg_file = $('#wysiwyg_image_picture')[0].files[0];
	  var wysiwyg_formData = new FormData();
	  //wysiwyg_formData.append('utf8', "✓");
	  wysiwyg_formData.append(csrf_param, csrf_token);
	  wysiwyg_formData.append('pict', wysiwyg_file, wysiwyg_file.name);
	  $.ajax({
		  url: csrf_image_url,
		  type: 'POST',
		  data: wysiwyg_formData,
		  dataType: 'json',
		  /**   
           * 必须false才会避开jQuery对 formdata 的默认处理   
           * XMLHttpRequest会对 formdata 进行正确的处理   
           */  
          processData : false,  
          /**   
           *必须false才会自动加上正确的Content-Type   
           */  
          contentType : false,  
		  success: function(response)
		  {
			  
			  $('#wysiwyg_image_submit').val('上传');
			  $('#wysiwyg_image_picture').val('');
			  var status = response.response.status;
			  if (status == 0 || status == 9999) {
				  $('#bootstrap-wysihtml5-picture-src').val(response.response.src_0);
			  } else {
				  alert(response.response.statusMessage);
			  }
		  },
		  error: function(jqXHR, textStatus, errorThrown)
		  {
			  alert('error ' + jqXHR.responseText);
		  }
	  });
  });  
});