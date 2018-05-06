/*---LEFT BAR ACCORDION----*/
$(function() {
    $('#nav-accordion').dcAccordion({
        eventType: 'click',
        autoClose: true,
        saveState: true,
        disableLink: true,
        speed: 'slow',
        showCount: false,
        autoExpand: true,
//        cookie: 'dcjq-accordion-1',
        classExpand: 'dcjq-current-parent'
    });
    if(isTouchDevice()===false) {
		 $('.tooltips').tooltip();
		}
    //关闭所有输入表单的自动完成功能 autocomplete
    $("form").attr("autocomplete", "off");
});
function logonout(){
	var rdel = confirm('您确定要退出吗');
	if (rdel==false){
		return ;
	}else {
		$.cookie("brmbUser", "false", {path: '/', expire: -1 }); 
		$.cookie("busername", "", { path: '/',expires: -1 }); 
		$.cookie("bpassword", "", { path: '/',expires: -1 }); 
		window.location.href=_context+'/water/j_spring_security_logout';
		
		
	}
}
//tool tips
function isTouchDevice(){
    return true == ("ontouchstart" in window || window.DocumentTouch && document instanceof DocumentTouch);
}

function addImage(fname,fjpeg,fid,ftype){
	var str='<div class="imgItem"><div class="fileupload fileupload-new col-lg-11 col-sm-11" data-provides="fileupload">'
       
        if(fname!=null){
        	if(fjpeg==null || fjpeg==""){
        		str +=' <span class="download default hidden-xs" id="'+fid+'"  style="margin-left:10px;" >';
           	    str +='<a class="tooltips hidden-xs" data-placement="left" data-original-title="下载" href="'+_context+'/files/downloads/'+fname+'?ftype='+ftype+'" ><i class="fa fa-download">'+fname+'</i></a>&nbsp;</span>';
        	}else{
        		str +='<span class="default" id="'+fid+'" style="display:none"/></span>';
            	str +='<span class="preview" style="margin-left:10px;" >';
            	str +='<a class="tooltips" data-placement="left" data-original-title="预览" href="'+_context+'/files/preview/'+fjpeg+'?ftype='+ftype+'" ';
            	if($(window).width()>767){
            		str += ' target="_blank"';
            	}
            	str +='><i class="fa fa-eye">'+fjpeg+'</i></a>&nbsp;</span>';
            	 str +=' <span class="download default hidden-xs" id="'+fname+'"  style="margin-left:10px;" >';
            	 str +='<a class="tooltips hidden-xs" data-placement="left" data-original-title="下载" href="'+_context+'/files/downloads/'+fname+'?ftype='+ftype+'" ><i class="fa fa-download">'+fname+'</i></a>&nbsp;</span>';
        	}
        }else{
        	 str += ' <span class="btn btn-white btn-file"><span class="fileupload-new"><i class="fa fa-paper-clip"></i> 选择文件</span>';
 	        str +='<span class="fileupload-exists"><i class="fa fa-undo"></i>修改</span>';
        	str +='<input type="file" class="default"  name="fileupload" /></span>';
        	str +='<span class="fileupload-preview" style="margin-left:5px;"></span>';
	        str +='<a href="#" class="close fileupload-exists" data-dismiss="fileupload" style="float: none;font-size: 14px; margin-left:5px;">重设</a>';
	        str +='  <span class="preview" style="margin-left:10px;" ></span>';
        	 str +=' <span class="download" style="margin-left:10px;" ></span>';
        }
        str +='</div><div class="col-sm-1 col-xs-6"><button type="button" class="addBtn btn btn-info fa fa-minus-square" onclick="removeImage()"></button></div></div>';
	//var clone = $(".imgItem").filter(":first").clone();
	$('#wimgs').append(str);
}
function removeImage(e){
	if($(".imgItem").length>0){
		e=e||event;
		if($(e.target).parents(".imgItem").find('.default').attr("id")!="" && $(e.target).parents(".imgItem").find('.default').attr("id")!=undefined){
			var rdel = confirm("您确定要删除这个图片吗？");
			if (rdel == false)
				return;
			if($('#delids').val()!=""){
				$('#delids').val($('#delids').val()+","+$(e.target).parents(".imgItem").find('.default').attr("id"));
			}else{
				$('#delids').val($(e.target).parents(".imgItem").find('.default').attr("id"));
			}
			
		}
		var o = $(e.target).parents(".imgItem");
		$(o).remove();
	}
}
// right slidebar
$(function(){
 $.slidebars();
});

var Script = function () {

//    sidebar dropdown menu auto scrolling

    jQuery('#sidebar .sub-menu > a').click(function () {
        var o = ($(this).offset());
        diff = 250 - o.top;
        if(diff>0)
            $("#sidebar").scrollTo("-="+Math.abs(diff),500);
        else
            $("#sidebar").scrollTo("+="+Math.abs(diff),500);
    });

//    sidebar toggle

    $(function() {
        function responsiveView() {
            var wSize = $(window).width();
            if (wSize <= 768) {
                $('#container').addClass('sidebar-close');
                $('#sidebar > ul').hide();
            }

            if (wSize > 768) {
                $('#container').removeClass('sidebar-close');
                $('#sidebar > ul').show();
            }
        }
        $(window).on('load', responsiveView);
        $(window).on('resize', responsiveView);
    });
    
    $('#camera-fa-bars').click(function () {
        if ($('.camera-sidebar > ul').is(":visible") === true) {
//            $('#main-content').css({
//                'margin-left': '0px'
//            });
//            $('.camera-sidebar').css({
//                'margin-left': '-210px'
//            });
            $('.camera-sidebar > ul').hide();
            //$("#container").addClass("sidebar-closed");
        } else {
//            $('#main-content').css({
//                'margin-left': '210px'
//            });
            $('.camera-sidebar > ul').show();
//            $('.camera-sidebar').css({
//                'margin-left': '0'
//            });
            //$("#container").removeClass("sidebar-closed");
        }
       
    });

// custom scrollbar
    $("#sidebar").niceScroll({styler:"fb",cursorcolor:"#e8403f", cursorwidth: '3', cursorborderradius: '10px', background: '#404040', spacebarenabled:false, cursorborder: '', scrollspeed: 60});

    //$("html").niceScroll({styler:"fb",cursorcolor:"#e8403f", cursorwidth: '6', cursorborderradius: '10px', background: '#404040', spacebarenabled:false,  cursorborder: '', zindex: '1000', scrollspeed: 100, mousescrollstep: 60});

    $(".table-responsive").niceScroll({styler:"fb",cursorcolor:"#e8403f", cursorwidth: '6', cursorborderradius: '10px', background: '#404040', spacebarenabled:false,  cursorborder: '', zindex: '1000', horizrailenabled: true });



// widget tools

    jQuery('.panel .tools .fa-chevron-down').click(function () {
        var el = jQuery(this).parents(".panel").children(".panel-body");
        if (jQuery(this).hasClass("fa-chevron-down")) {
            jQuery(this).removeClass("fa-chevron-down").addClass("fa-chevron-up");
            el.slideUp(200);
        } else {
            jQuery(this).removeClass("fa-chevron-up").addClass("fa-chevron-down");
            el.slideDown(200);
        }
    });

// by default collapse widget

//    $('.panel .tools .fa').click(function () {
//        var el = $(this).parents(".panel").children(".panel-body");
//        if ($(this).hasClass("fa-chevron-down")) {
//            $(this).removeClass("fa-chevron-down").addClass("fa-chevron-up");
//            el.slideUp(200);
//        } else {
//            $(this).removeClass("fa-chevron-up").addClass("fa-chevron-down");
//            el.slideDown(200); }
//    });

    jQuery('.panel .tools .fa-times').click(function () {
        //jQuery(this).parents(".panel").parent().remove();
    	//改为隐藏
    	jQuery(this).parents(".panel").parent().parent().hide();
    });



//    popovers

    $('.popovers').popover();



// custom bar chart

    if ($(".custom-bar-chart")) {
        $(".bar").each(function () {
            var i = $(this).find(".value").html();
            $(this).find(".value").html("");
            $(this).find(".value").animate({
                height: i
            }, 2000)
        })
    }
}();
(function($) {
	$.fn.serializeJson = function() {
		// var obj=$( "form input:checkbox") ;
		var obj1 = $("form input:checked");
		var chks = $(this).find(":checkbox:not(:checked)"); // 取得所有未选中的checkbox
		var serializeObj = {};
		var array = this.serializeArray();
		var str = this.serialize();
		$(array).each(
				function() {
					if (serializeObj[this.name]) {
						if ($.isArray(serializeObj[this.name])) {
							serializeObj[this.name].push(this.value);
						} else {
							serializeObj[this.name] = [
									serializeObj[this.name], this.value ];
						}
					} else {
						serializeObj[this.name] = this.value;
					}
				});
		$(obj1).each(
				function() {
					if(this.title!=null && this.title!=""){
						if (serializeObj[this.name]) {
							if ($.isArray(serializeObj[this.name])) {
								serializeObj[this.name].push(this.title);
							} else {
								serializeObj[this.name] = [
										serializeObj[this.name], this.title ];
							}
						} else {
							serializeObj[this.name] = this.title;
						}
					}
				});
		// 没有选中的赋值false
		
		$(chks).each(function() {
			if(this.title!=null && this.title!=""){
				var lengths=$('input[id="'+this.name+'"]').length;
				// = document.getElementsByName(this.name);
				//只使用于单个checkbox对象
				if (lengths == 1) {
					serializeObj[this.name] = false;
				}
			}
		})
		return serializeObj;
	};
})(jQuery);
function doajax(url,data,title,exFunction){
	$('#loading').show();
	$.ajax({
		type:"POST",
		url:url,	
		data:data ,
		  success: function(response){
			  $('#loading').hide();  
			  var status =response.response.status;
				if(status==0 || status ==9999) {
					toastr.success(title+issucccess);
					if (exFunction!=null||exFunction!=undefined) {
						eval(exFunction());
					}
				} else {
					toastr.error(title+isfailed);
				}
		  },
		  error: function(xhr, textStatus, errorThrown) {
			  $('#loading').hide();
			  alert('error ' + errorThrown);
		  }
	});
	
}