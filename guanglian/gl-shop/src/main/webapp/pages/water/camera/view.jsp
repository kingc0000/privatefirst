<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<link href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/jssor.slider-21.1.6.css" />' rel="stylesheet">
<style>
.zoom {
	filter:alpha(opacity=70); /*IE滤镜，透明度50%*/
	-moz-opacity:0.7; /*Firefox私有，透明度50%*/
	opacity:0.7;/*其他，透明度50%*/
	margin-top: -5px;
}

</style>
<script>
	$('#loading').show();
</script>
<!--sidebar start-->
	<aside>
		<div id="sidebar" class="nav-collapse camera-sidebar" style="margin-top:-15px !important;margin-left:-15px !important">
		<div class="check-div hidden-xs">
			<a class="btn btn-theme btn-sm">图片浏览</a>&nbsp;&nbsp;<a class="btn btn-default btn-sm" href='<c:url value="/water/camera.html?type=monitor"/>'>图像监控</a>
		</div>
			<!-- sidebar menu start-->
			<ul class="sidebar-menu" id="nav-accordion">
				<c:forEach items="${requestScope.departments}" var="project"
					varStatus="status">
						<li <c:if test="${fn:length(project.cSites)>0 }"> class="sub-menu"</c:if>>
							<a href="#" class="m-win-title" title="${project.name }"> <i class="fa fa-group"></i> <span>${project.name }</span></a> <!-- first level menu -->
							<c:if test="${fn:length(project.cSites)>0}">
								<ul class="sub">
									<c:forEach items="${project.cSites }" var="csite">
											<li <c:if test="${fn:length(csite.cameras)>0 }"> class="sub-menu"</c:if>>
												<a href="#" class="m-win-title" title="${csite.name }">${csite.name }</a>
												<!-- second level menu --> 
												<c:if test="${fn:length(csite.cameras)>0}">
													<ul class="sub">
														<c:forEach items="${csite.cameras }" var="camera">
																<li><a href="#" onclick="getCameras('${camera.id}', '${project.name }&gt;${csite.name }&gt;${camera.name }')"><i class="fa fa-video-camera"></i>${camera.name }</a></li>
																<!-- third level menu -->
														</c:forEach>
													</ul>
												</c:if>
											</li>
									</c:forEach>
								</ul>
							</c:if>
						</li>
				</c:forEach>
			</ul>
		</div>
	</aside>

	<!--sidebar end-->

	<!--main content start-->
	<section id="middle-content" >
		<div class="row" id="edittable">
			
				<section class="panel">
					<header class="panel-heading">
					<div class="row" style="padding:0px 5px;">
							<div class="pull-left headlan" >
								<button class="btn btn-theme btn-xs hidden-xs zoom" id="full-view" title="全屏"><i class="fa fa-arrows-alt"></i></button>
								<span id="edittile" style="line-height: 34px; color:#ff6c60;font-weight:700;overflow: hidden;word-break: break-all; word-wrap:break-word;"></span> 
							</div>
							<div class="pull-right headlan" style="height:37px;padding-top:3px">
								<select name="begintm" style="border:1px solid #e2e2e4;height: 34px;">  
						        </select>  
						        <select name="endtm" style="border:1px solid #e2e2e4;height: 34px;">  
						        </select>  
							</div>
							<div class="pull-right headlan" >
								<div class="input-group date dtControl" >
									<input type="text" class="form-control" id="begindt" name="begindt"><span class="input-group-addon btn-theme"><i class="fa fa-calendar"></i></span>
								</div>
							</div>
							<div class="pull-right headlan" >
								<input type="checkbox" class="autoplay hidden-xs" onclick="changeInterval()"><span class="hidden-xs">自动轮播</span>
								间隔<input type="text" class="refreshControl"  id="interval" value="1"/>帧
								<span class="btn btn-theme" onclick="changeInterval()"><i class="fa fa-refresh"></i></span>
							</div>
							
						</div>
							
					</header>
					<div class="panel-body">
						<!-- #Jssor Slider begin -->
						<div class="jssor-wrap">
							<div id="jssor_1" class="jssor-main-div">
						    </div>
					    </div>
					    <!-- #endregion Jssor Slider End -->
					</div>
				</section>
			</div>
	</section>
	<input type="hidden" name="cid" id="cid"/>
	<!--main content end-->
<!--right slidebar-->
<script src='<c:url value="/resources/js/slidebars.min.js" />'></script>
<script src='<c:url value="/resources/js/jssor.slider-21.1.6.mini.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script type="text/javascript">
var jssor_1_options;
var jssor_1_slider;
var base_url = '<c:url value="/water/camera/view.html"/>';
var view_url = '<c:url value="/water/camera/showImg.html"/>';
var jssor_bak;
var jssor_slide_bak;

/**
 * 时间选择控件
 */
function tmselect() {
	$("select[name='begintm']").append("<option value='-1'>全天</option>");
	for (var ti = 0; ti < 24; ti++) {
		$("select[name='begintm']").append("<option value='"+ti+"'>"+ti+"</option>");	
	}
	$("select[name='begintm']").change(function(){
		var selected_val = parseInt($(this).val());
		$("select[name='endtm']").html('');
		if (selected_val!='-1') {
			for (var tj = selected_val+1; tj <= 24; tj++) {
				var opt = $('<option>').val(tj).text(tj);
				$("select[name='endtm']").append(opt);		
			}
			$("select[name='endtm']").val(selected_val+1);
		}
		loadCameras();
	});
	$("select[name='endtm']").change(function(){
		loadCameras();
	});
}
jQuery(document).ready(function() {
	var wSize = $(window).width();
    if (wSize <= 768) {
        $("#jssor_1").width($(window).width()*0.9);
    }
	//处理时间选择select
	tmselect();
	//设置title
	$('#pictitle').html('图片浏览<a href="<c:url value="/water/camera.html?type=monitor"/>" >|图像监控</a></div>');
	$("#full-view").click(function(){
		if ($('.header').is(":visible") === true) {
			$('.header').hide("slow");
			$(".camera-sidebar").hide("slow");
			$("#middle-content").css({
				"margin-top":"-60px",
				"margin-left":"15px"
			});
		} else {
			$('.header').show("slow");
			$(".camera-sidebar").show("slow");
			$("#middle-content").css({
				"margin-top":"0px",
				"margin-left":"220px"
			});
		}
	});
	
	$('#loading').hide(); 
	
	jssor_1_options = {
      /* $AutoPlay: true,
      $AutoPlaySteps: 1, */
      $Loop:0,
      /* $SlideshowOptions: {
        $TransitionsOrder: 1
      }, */
      $ArrowNavigatorOptions: {
    	$Steps: 1,
        $Class: $JssorArrowNavigator$
      },
      $ThumbnailNavigatorOptions: {
        $Class: $JssorThumbnailNavigator$,
        $Cols: 10,
        $SpacingX: 8,
        $SpacingY: 8,
        $Align: 360
      }
    };
    
    $("#interval").keydown(function(e){
  	  	if(e.keyCode==13){ //回车事件
  	  		changeInterval();
		}
    });
      
    //加载日期控件
	$('.dtControl').datepicker({
	    format: "yyyy-mm-dd",
	    todayBtn: "linked",
	    //clearBtn: true,
	    autoclose: true,
	    todayHighlight: true
	});
	$('#begindt').change(function(){
		loadCameras();		 
	});
});
/*responsive code begin*/
/*you can remove responsive code if you don't want the slider scales while window resizing*/
function ScaleSlider() {
    var refSize = jssor_1_slider.$Elmt.parentNode.clientWidth;
    if (refSize) {
    	var pratio = $(window).width()/$(window).height();
    	if (pratio>1) {
    		refSize = Math.min(refSize, $(window).height());
    	} else {
    		refSize = Math.min(refSize, $(window).width());
    	}
        //refSize = Math.min(refSize, 800);
        jssor_1_slider.$ScaleWidth(refSize);
    }
    else {
        window.setTimeout(ScaleSlider, 30);
    }
}
//
/**
 * 动态创建jssor控件
 * type=0,通过oData（json格式）创建图片格式字符串，type=1，直接为原先保留的json数据
 */
function createJssor(type, oData) {
	//slide body
	var jssor_slide = $('<div data-u="slides" class="jssor-slide" id="img_content"></div>');
	//img item
	if (type==0) {
		jssor_slide_bak = oData; //保留json数据
	}
	if (oData!=null && oData.length>0) {
		for(var i=0;i<oData.length;i++) {
			var src = base_url + "?p="+oData[i].abpath;
			var img_item = $('<div data-p="144.50"></div>');
			img_item.append('<img data-u="image" src2="'+src+'" onclick="javascript:openImg(\''+oData[i].abpath+'\')"/>');
			jssor_slide.append(img_item);
		}
	}
	
	//console.log(jssor_slide_bak);
	//thumbnail navigator
	var jssor_thumb_navi = $('<div data-u="thumbnavigator" class="jssort01 jssor-thumbnavigator" data-autocenter="1"></div>');
	//Thumbnail Item Skin
	var jssor_thumb_item = $('<div data-u="slides" style="cursor: default;"><div data-u="prototype" class="p"><div class="w"><div data-u="thumbnailtemplate" class="t"></div></div><div class="c"></div></div></div>');
	jssor_thumb_navi.append(jssor_thumb_item);
	
	//arrow navigator
	/* var arrow_l = $('<span data-u="arrowleft" class="jssora05l jssor-arrow-l"></span>');
	var arrow_r = $('<span data-u="arrowright" class="jssora05r jssor-arrow-r"></span>'); */
	var arrow_l = $('<span u="arrowleft" class="jssora01l" style="top: 270px; left: 8px;"></span>');
	var arrow_r = $('<span u="arrowright" class="jssora01r" style="top: 270px; right: 8px;"></span>');
	
	//jssor_1置回初始值
	$("#jssor_1").removeAttr("jssor-slider").removeAttr("style").off();
	$("#jssor_1").html("");
	$("#jssor_1").append(jssor_slide).append(jssor_thumb_navi).append(arrow_l).append(arrow_r);
	//是否自动滚动
	if($(".autoplay").prop("checked")){
		jssor_1_options.$AutoPlay= true; 
		jssor_1_options.$Idle=2000;
		jssor_1_options.$AutoPlaySteps=$("#interval").val();
	} else {
		jssor_1_options.$AutoPlay= false;
	}
    jssor_1_slider = new $JssorSlider$("jssor_1", jssor_1_options);
    
    $(window).bind("load", ScaleSlider);
    $(window).bind("resize", ScaleSlider);
    $(window).bind("orientationchange", ScaleSlider); 
    ScaleSlider();
}
// 改变图片查看的跳跃数
function changeInterval(){
	var i = $("#interval").val();
	if(i==null || i==="" || i==0)return;
	i = parseInt(i);
	jssor_1_options.$ArrowNavigatorOptions.$Steps=i;
	createJssor(1, $(jssor_slide_bak));
	
	//jssor_1_options.$AutoPlaySteps=i;
	//重新构造jssor
	//createJssor();
}
function getCameras(cid, title) {
	$("#edittile").html(title);
	$("#cid").val(cid); //cid赋值
	var dt = $("#begindt").val();
	loadCameraPictures(cid, dt);
	//responsiveView();
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
function loadCameras() {
	var dt = $("#begindt").val();
	var cid = $("#cid").val();
	if(cid=='undefined'||cid=='')return false;
	loadCameraPictures(cid, dt);
}
/**
 根据cid和日期，加载监控图片
 @param cid 摄像头id
 @param dt 所需要查询的日期
 */
function loadCameraPictures(cid, dt) {
	$('#loading').show(); 
	//获取时间段
	var begintm = $("select[name='begintm']").val();
	var endtm = $("select[name='endtm']").val();
	var url = '<c:url value="/water/camera/loadImage.shtml"/>';
	$.post(url, {"cid":cid, "dt":dt, "begintm": begintm, "endtm": endtm}, function(response){
		$('#loading').hide(); 
		var status = response.response.status;
		var msg = response.response.statusMessage;
		var dt = response.response.dt; //日期
		if (dt!=null && dt!='undefined') {
			$("#begindt").val(dt);
		}
		if (status == 0) {
			var oData = response.response.data;
			$("#jssor_1").html("");
			if(oData!=null&&oData.length>0) {
				createJssor(0, oData);
			}
		} else if (status == -4) {
			toastr.error(msg);
			$("#jssor_1").html("");
		} else {
			toastr.error("系统错误，请联系管理员");
			$("#jssor_1").html("");
		}
	});
}
function openImg(path) {
	if($(window).width()<768){
		return;
	} else {
		window.open(view_url+"?p="+path, "_blank");
	}
}
</script>
   