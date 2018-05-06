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
	position: absolute;
	z-index: 10;
	margin-top: 10px;
	margin-left: -8px;
}
</style>
<script>
	$('#loading').show();
</script>
<!--sidebar start-->
	<aside>
		<div id="sidebar" class="nav-collapse camera-sidebar" style="margin-top:-15px !important;margin-left:-15px !important">
		<div class="check-div hidden-xs">
			<a class="btn btn-default btn-sm" href='<c:url value="/water/camera.html?type=view"/>'>图片浏览</a>&nbsp;&nbsp;<a class="btn btn-theme btn-sm">图像监控</a>
		</div>
			<!-- sidebar menu start-->
			<ul class="sidebar-menu" id="nav-accordion">
				<c:forEach items="${requestScope.departments}" var="project"
					varStatus="status">
						<li <c:if test="${fn:length(project.cSites)>0 }"> class="sub-menu"</c:if>>
							<a href="#" title="${project.name }" class="m-win-title"> <i class="fa fa-group"></i> <span>${project.name }</span></a> <!-- first level menu -->
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
	<section id="middle-content" style="min-height:40px;">
		<button class="btn btn-xs btn-theme hidden-xs zoom" id="full-view" title="全屏"><i class="fa fa-arrows-alt"></i></button>
		<div class="row" id="edittable">
			
		</div>
	</section>
	<!--main content end-->
<!--right slidebar-->
<div id="hiddenData" class="display-none"></div>
<script src='<c:url value="/resources/js/slidebars.min.js" />'></script>
<script type="text/javascript">
var base_url = '<c:url value="/water/camera/view.html"/>';
var view_url = '<c:url value="/water/camera/showImg.html"/>';

jQuery(document).ready(function() {
	$('#loading').hide(); 
	//每一分钟刷新对象获取最新图片
	setInterval(getLatest, 60000);
	//设置title
	$('#pictitle').html('<a href="<c:url value="/water/camera.html?type=view"/>" >图片浏览|</a>图像监控</div>');
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
});
/**
 * 获取最新摄像头的监控图片
 */
function getLatest() {
	$.each($("#hiddenData").data(), function(key, value){
		console.log("loading camera.id="+key);
		loadCameraPictures(key, value);
	});
}
function getCameras(cid, title) {
	$('#loading').show(); 
	loadCameraPictures(cid, title);
	$("#hiddenData").data(cid, title); //存储
	refreshWins();
	$('#loading').hide(); 
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
/**
 根据cid，加载监控图片
 @param cid 摄像头id
 @param dt 所需要查询的日期
 */
function loadCameraPictures(cid, title) {
	createWin(cid, title);
	var url = '<c:url value="/water/camera/monitor.shtml"/>';
	$.post(url, {"cid":cid}, function(response){
		var status = response.response.status;
		var msg = response.response.statusMessage;
		var panel_id = "#panel-m-"+cid; 
		if (status == 0) {
			var p = response.response.abpath;
			var src = base_url + "?p="+p;
			$(panel_id+" .monitor-img").attr("src", src).addClass("display-block");
			//$(panel_id+" .monitor-img").wrap('<a href="'+view_url+'?p='+p+'" target="_blank"></a>');
			$(panel_id+" .monitor-img").parent("a").attr("href", view_url+'?p='+p);
			$(panel_id+" .monitor-tip").html("");
		} else if (status == -4) {
			$(panel_id+" .monitor-img").addClass("display-none");
			$(panel_id+" .monitor-tip").html(msg);
		} else {
			$(panel_id+" .monitor-img").addClass("display-none");
			$(panel_id+" .monitor-tip").html("系统错误，请联系管理员");
		}
	});
}
/*
 * 创建新的监控窗口
 * 如果已经存在，不做任何操作，否则新建一个窗口
 */
function createWin(cid, title) {
	var o = $("#hiddenData").data(cid);
	if(o==null||o=='undefined') {//不存在，新建
		var panel = $('<section class="panel"></section>');
		panel.attr("id", "panel-m-"+cid); //设定id
		var header = $('<header class="panel-heading"></header>');
		header.append('<span style="line-height: 34px; color:#ff6c60;font-weight:700;overflow: hidden;word-break: break-all; word-wrap:break-word;" title="'+title+'">'+title+'</span>&nbsp;'); //标题
		header.append('<span class="tools pull-right"><a href="javascript:closeMonitor('+cid+');" class="fa fa-times"></a></span>'); //close btn
		var panelBody = $('<div class="panel-body"></div>');
		var monitorBody = $('<div class="monitor-body"></div>');
		monitorBody.append('<a target="_blank"><img class="monitor-img" style="display:inline"/></a>').append('<div class="monitor-tip"></div>');
		panelBody.append(monitorBody);
		panel.append(header).append(panelBody);
		$("#edittable").append(panel);
	}
}
/**
 * 监控窗口更新排版
 * 如果只有一个，则col-sm-12，2个，则col-sm-6，大于2，则col-sm-4
 */
function refreshWins() {
	var data = $("#hiddenData").data();
	var size = 4, length=0;
	if(data==null||data=='undefined') {
		return;
	} else {
		for(var i in data) {
			length++;
		}
	}
	if (length==0) { //没有数据，不管
		return;
	} else if (length==1) {
		size=12;
	} else if(length==2) {
		size=6;
	}
	//对于只有一个监控窗口情况，需要调整其宽度比
	if (length==1) {
		var pratio = $(window).width()/$(window).height();
		if(pratio<=1){
			$(" .monitor-img").css("width","100%");
		}else{
			$(" .monitor-img").css("width", $(window).height());
		}
	} else {
		$(" .monitor-img").css("width","100%");
	}
	$.each(data, function(key, value){
		$('#panel-m-'+key).attr("class", "panel").addClass('col-sm-'+size);
	});
	//修正偏移
	$('div[class="clearfix visible-sm visible-md visible-lg"]').remove();
	$('[id^=panel-m-]').each(function(index){
		if((index%3==2)) {
			$(this).after('<div class="clearfix visible-sm visible-md visible-lg"></div>');
		}		
	});
	
}
function closeMonitor(cid) {
	$("#hiddenData").removeData(cid);
	$('#panel-m-'+cid).remove();
	refreshWins();
}
</script>
   