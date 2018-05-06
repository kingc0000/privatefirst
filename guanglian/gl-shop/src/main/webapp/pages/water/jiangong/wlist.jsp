<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<meta name="viewport" content="width=device-width, initial-scale=1.0 user-scalable=yes" /> 
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<style type="text/css">
#projectmap {
	height: calc(100% - 15px);
	width: 100%;
	margin-top:5px;
}
#projectmap label{max-width:none;}
.style1{padding:1px 3px;
 font-size:11px;
 -moz-box-shadow:2px 3px 14px #333333; -webkit-box-shadow:2px 3px 14px #333333; box-shadow:2px 3px 14px #333333; 
}
 #showimg-overlay {
	display: none;
	opacity: 1;
	position: fixed;
	overflow: hidden;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: 1000000;
	background-color: #222;
	background-color: rgba(0, 0, 0, 0.8);
	transition: opacity 0.5s ease;
}
#showimg-overlay .full-image {
	display: inline-block;
	position: relative;
	width: 100%;
	height: 100%;
	text-align: center;
}
#showimg-overlay .full-image img {
	display: inline-block;
	width: auto;
	height: auto;
	max-height: 100%;
	max-width: 100%;
	vertical-align: middle;
	-moz-box-shadow: 0 0 8px rgba(0, 0, 0, 0.6);
	     box-shadow: 0 0 8px rgba(0, 0, 0, 0.6);
}

</style>
<link href='<c:url value="/resources/css/marquee.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/slide.css" />' rel="stylesheet">
<div id="showimg-overlay"></div>
<div id="dwarning" style="height:30px;margin-top:-20px"><p class="pwarn" ><marquee behavior="scroll" onclick="getWarning()"><ul class="marquee-content-items" id="mitems"></ul></marquee></p></div>

<div class="cmarquee">
	<div class="marquee-sibling" style="top:7px;">
		<button  class="btn btn-img btn-selected tooltips maptools cbtntool" style="display:none;" data-placement="top" data-container="body" data-original-title="图例"  onclick="showtools()"><i class="fa fa-anchor"></i></button>
		、<span style="display:none;" class="chartBtn">
			<button  class="btn btn-img btn-selected tooltips maptools" data-placement="top" data-container="body" data-original-title="项目图纸" onclick="getDrawing()" ><i class="fa fa-building-o"></i></button>
			<button  class="btn btn-img btn-selected tooltips maptools" data-placement="top" data-container="body" data-original-title="图表数据" onclick="showdata()"><i class="fa fa-bar-chart-o"></i></button>
			<button  class="btn btn-img btn-selected tooltips maptools" data-placement="top" data-container="body" data-original-title="每日信息" onclick="getConclusion()"><i class="fa fa-calendar"></i></button>
			
			<button  class="btn btn-img btn-selected tooltips pmaptool well-icon pwell " onclick="setlegend('pwell','pmark')"  data-container="body" data-placement="top" data-original-title="降水井图例"></button>
			<button  class="btn btn-img btn-selected tooltips pmaptool well-icon dewell" onclick="setlegend('dewell','demark')" data-container="body" data-placement="top" data-original-title="疏干井图例"></button>
			<button  class="btn btn-img btn-selected tooltips pmaptool well-icon owell" onclick="setlegend('owell','omark')" data-container="body" data-placement="top" data-original-title="观测井图例"></button>
			<button  class="btn btn-img btn-selected tooltips pmaptool well-icon iwell" onclick="setlegend('iwell','imark')" data-container="body" data-placement="top" data-original-title="回灌井图例"></button>
			<button  class="btn btn-img btn-selected tooltips pmaptool well-icon ewell" onclick="setlegend('ewell','emark')" data-container="body" data-placement="top" data-original-title="环境监测图例"></button>
			<button  class="btn btn-img btn-selected tooltips pmaptool well-icon camera" onclick="setlegend('camera','camera')" data-container="body" data-placement="top" data-original-title="摄像头图例"></button>
		</span>
	</div>
</div>
<div class="cmarquee">
	<div class="marquee-sibling" style="top:39px;">
		<div class="cmaptool" style="display:none;text-align: left;line-height: 12px;background-color: #6699ff;width: 27px; z-index: 100;">
			<button  class="btn btn-img btn-selected tooltips well-icon pwell " onclick="setlegend('pwell','pmark')"  data-container="body" data-placement="top" data-original-title="降水井图例"></button>
			<br>
			<button  class="btn btn-img btn-selected tooltips well-icon dewell" onclick="setlegend('dewell','demark')" data-container="body" data-placement="top" data-original-title="疏干井图例"></button>
			<br>
			<button  class="btn btn-img btn-selected tooltips well-icon owell" onclick="setlegend('owell','omark')" data-container="body" data-placement="top" data-original-title="观测井图例"></button>
			<br>
			<button  class="btn btn-img btn-selected tooltips well-icon iwell" onclick="setlegend('iwell','imark')" data-container="body" data-placement="top" data-original-title="回灌井图例"></button>
			<br>
			<button  class="btn btn-img btn-selected tooltips well-icon ewell" onclick="setlegend('ewell','emark')" data-container="body" data-placement="top" data-original-title="环境监测图例"></button>
			<br>
			<button  class="btn btn-img btn-selected tooltips well-icon camera" onclick="setlegend('camera','camera')" data-container="body" data-placement="top" data-original-title="摄像头图例"></button>
		</div>
	</div>
</div>	

<div  id="projectmap">
 </div>
<!-- modal begin : 测点曲线图 -->
<div class="modal fade" id="commonmodal" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" ><span id="modal-title" class="projectname text-warning"></span></h4>
            </div>
            <div class="modal-body" id="modal-body">
            	<div class="panel-body">
            	</div>
            </div>
			<div class="modal-footer">
                <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            </div>
        </div>
    </div>
</div>
<!-- modal end : 测点曲线图 -->
<!-- modal begin: 数据图 -->
<div class="modal fade" id="datamodal" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" ><span class="projectname text-warning"></span>数据图</h4>
            </div>
            <div class="modal-body" >
            	<div class="panel-body">
            		<div class="row">
            			<div class="col-xs-12 col-sm-8">
		            		<canvas id="pointsChart_pie" style="width:100%;height:100%"></canvas>
            			</div>
            			<div class="col-xs-12 col-sm-4">
		            		<canvas id="pointsChart_line"></canvas>
            			</div>
            			<!-- <div class="clearfix"></div> -->
            			<div class="col-xs-12 col-sm-4">
		            		<canvas id="pointsChart_0" style="width:100%;height:100%"></canvas>
            			</div>
            			<div class="col-xs-12 col-sm-4">
		            		<canvas id="pointsChart_1" style="width:100%;height:100%"></canvas>
            			</div>
            			<div class="col-xs-12 col-sm-4">
		            		<canvas id="pointsChart_2" style="width:100%;height:100%"></canvas>
            			</div>
            		</div>
            	</div>
            </div>
			<div class="modal-footer">
                <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            </div>
        </div>
    </div>
</div>

<!-- modal end : 工程日志 -->
<script src='<c:url value="/resources/js/project/map.js" />'></script>
<script src='<c:url value="/resources/js/moment.min.js"/>'></script> 
<script src='<c:url value="/resources/js/Chart.min.js"/>'></script>
<script src='<c:url value="/resources/assets/ckeditor/ckeditor.js" />'></script>

<script>
$("#pictitle").html("地下水项目管理");
var pid = '${pid}';
var rtime = ${rfreshtime};
//保存测点的markerid
var pmark =  null;
var demark =  null;
var omark =  null;
var imark =  null;
var emark =  null;
var camera = null;
var pointArray= new Array();
function goback(){
	javascript :history.back(-1);
}
function setlegend(btn,datas){
	///alert(this.html());
	if($("."+btn).hasClass('grey')){
		$("."+btn).removeClass('grey');
		$.each(eval(datas), function(i, data) {
			map.addOverlay(data);
		})
	}else {
		$("."+btn).addClass('grey');
		removeMaekers(datas);
	}
}
function removeMaekers(datas){
	$.each(eval(datas), function(i, data) {
		map.removeOverlay(data);
	})
}
//加载工地图纸
function getDrawing(){
	$.post("imgs.shtml?pid=" + pid, function(imgs) {
	    if (!imgs) {
	        toastr.error("当前项目没有图纸");
	        return;
	    }
	    var redata = eval(imgs);
	    $('#modal-title').html($("#pictitle").html() + '项目图纸');
	    var str1 = '<div class="gallery">';
	    for (var i = 0; i < redata.length; i++) {
	        str1 += ' <a href="' + redata[i].name + '" "><img style="width:100px;height:100px" src="' + redata[i].name + '"></a>';
	    }
	    str1 += '</div>';
	    $.getScript('<c:url value="/resources/js/slide.js" />', function() {
	        baguetteBox.run('.gallery', {
	            async: true,
	            fullScreen: true,
	            afterHide: function() {
	                console.log($(window).width());
	            }
	        });
	    });
	    $('#modal-body .panel-body').html(str1);
	    $("#commonmodal").modal('show');
	});
}
//modal关闭时清空
$('#commonmodal').on('hidden.bs.modal', function () {
	$('#modal-body .panel-body').html('');
})	
//加载工地每日信息，读取施工日志的总结
function getConclusion(){
	$.post("conclusion.shtml?pid=" + pid, function(response) {
		var status = response.status;
		if (status == 0) {
			$('#modal-title').html('每日信息');
			var str1 = '<div class="conclusion">';
			str1 += response.conclusion;
			str1 += '</div>';

			$('#modal-body .panel-body').html(str1);
			$("#commonmodal").modal('show');
		} else {
			toastr.error("当前项目暂无信息");
			return;
		}

	});
}
//加载告警列表
function getWarning(){
	$('#modal-title').html('项目告警列表');
	$('#modal-body .panel-body').html('');
	$('#modal-body .panel-body').append("<ul class='warning-ul'></ul>");
	var strli='';
	$('#mitems li').each(function(){
		if($(this).attr('id')){
			strli +='<li><a href="<c:url value="/jiangong/csite/wlist.html?pid='+$(this).attr('id')+'"/>">';
			strli +=$(this).html()+'</a></li>';
		}else if($(this).html()!=""){
			strli +='<li>'+$(this).html()+'</li>';
		}
	});
	$('#modal-body .panel-body .warning-ul').append(strli);
	//$('#modal-body .panel-body').append($('#mitems').html());
	$("#commonmodal").modal('show');
}

//手机版显示工具
function showtools(){
	if($(".cmaptool").is(":hidden")){
		$(".cmaptool").show();
		$(".cbtntool").css("border-radius","4px 4px 0 0");
	}else{
		$(".cmaptool").hide();
		$(".cbtntool").css("border-radius","4px");
	}
}
//加载项目的经纬度，在init地图加载之后执行
function setProjectMarker(){
	mapfine=true;
	map.removeEventListener("tilesloaded",setProjectMarker);
	 	
	if(pid!=-1){
		toProject(pid);
	}else{
		//quick search
		$('#search-input').quicksearch($('.zone-selected'));
		setProject();
		//让所有点在视野范围内
		map.setViewport(pointArray);
		$(".chartBtn").hide();
		$(".cbtntool").hide();
		$(".cmaptool").hide();
		setMarquee();
	}
	
	$('#loading').hide();

	window.setInterval(setMarquee, rtime*1000);
	if(isTouchDevice()===false) {
		 $('.tooltips').tooltip();
		}
	
}

function initDiag(){
	//重新初始化图例
	$('.well-icon').removeClass('grey');
	$('.dewell').addClass('grey'); //默认疏干井不可见
}
function initWell(){
	
	if(pmark!=null && pmark.length>0){
		removeMaekers("pmark");
	}
	pmark =[];
	if(demark!=null &&  demark.length>0){
		removeMaekers("demark");
	}
	demark = [];
	if(omark!=null && omark.length>0){
		removeMaekers("omark");
	}
	omark =[];
	if(imark!=null && imark.length>0){
		removeMaekers("imark");
	}
	imark =[];
	if(emark!=null &&  emark.length>0){
		removeMaekers("emark");
	}
	emark =[];
	
}

function setPwell(datas,hasright){
	
	if(datas!=null){
		//console.log(datas);
		$.each(datas, function(i, data) {
			var point = new BMap.Point(data.longitude,data.latitude);
			var sts = data.powerStatus; //0正常，1关闭，2故障
			var dataSts = data.dataStatus; //0正常，1，2，3告警
			if (sts==0 && dataSts>0) {
				sts=3;
			}
			var myIcon = new BMap.Icon('<c:url value="/resources/img/water/checks.png"/>', new BMap.Size(30,23), {imageOffset:new BMap.Size(-30*sts,0)});
			var marker = new BMap.Marker(point,{icon:myIcon}); 
			if(!$('.pwell').hasClass('grey')){
				map.addOverlay(marker);
			}
			marker.id=data.id;
			marker.status=data.status;
			pmark[pmark.length]=marker;
			var rflow="无";
			var rwater="无";
			var lastwater="0";
			if(data.thisAccu!=null){
				lastwater=data.thisAccu
			}
			if(data.rWater!=null){
				rwater=data.rWater;
			}
			if(data.rFlow!=null){
				rflow=data.rFlow;
			}
			var content='<form id="pform" action="'+_context+'/water/pdata/savemap.shtml" autocomplete="off">名称：'+data.name+'<br>运行/采集状态：'+getCstatus(data.powerStatus, data.dataStatus);
			content+='<span class="mapedit" style="display:none;"><br><span class="mapedit" >上次累积流量:</span> <input class="mapedit lastAccu" onkeyup="autoc(&apos;pform&apos;)" style="width:77px;border: 1px solid #e2e2e4;" type="text" name="lastAccu" value="'+lastwater+'"/>';
			content+='<br><span class="mapedit" >本次累积流量:</span> <input class="mapedit thisAccu" style="width:77px;border: 1px solid #e2e2e4;" type="text" onkeyup="autoc(&apos;pform&apos;)" name="thisAccu" />';
			content+='<br><span class="mapedit" >周期(小时):</span> <input class="mapedit accuPeriod" id="paccuPeriod" style="width:77px;border: 1px solid #e2e2e4;" onkeyup="autoc(&apos;pform&apos;)" type="text" name="accuPeriod"/></span>';
			content+='<br>流量值：'+rflow+'  <input class="mapedit flow" readonly="readonly" style="display:none;width:77px;color:#ff6c60;border: 0px solid #e2e2e4;" type="text" name="flow"/><br>'+'流量阈值：'+data.flow+'<br>水位值：'+rwater
			content+='  <input class="mapedit" style="display:none;width:77px;border: 1px solid #e2e2e4;" type="text" name="water"/><br>'+'水位阈值上限：'+data.water;
			content+=' <br>'+'水位阈值下限：'+data.waterDwon;
			content += '<input type="hidden" name="pid" value="'+data.id+'" /></form><br/><button class="btn btn-xs btn-theme" onclick="showLines(\'\', 0, '+data.id+', true)"><i class="fa fa-bar-chart-o"></i></button>';
			
			addClickHandler(content,marker,data.name);
		});
	}
}

function setDewell(datas,hasright){
	
	if(datas!=null){
		//console.log(datas);
		$.each(datas, function(i, data) {
			var point = new BMap.Point(data.longitude,data.latitude);
			var sts = data.powerStatus; //0正常，1关闭，2故障
			var dataSts = data.dataStatus; //0正常，1，2，3告警
			if (sts==0 && dataSts>0) {
				sts=3;
			}
			
			var myIcon = new BMap.Icon('<c:url value="/resources/img/water/checks.png"/>', new BMap.Size(30,23), {imageOffset:new BMap.Size(-30*sts,-92)});
			var marker = new BMap.Marker(point,{icon:myIcon}); 
/* 			if(!$('.dewell').hasClass('grey')){
				map.addOverlay(marker);
			}
 */			marker.id=data.id;
			marker.status=data.status;
			demark[demark.length]=marker;
			var rflow="无";
			var rwater="无";
			var lastwater="0";
			if(data.thisAccu!=null){
				lastwater=data.thisAccu
			}
			if(data.rWater!=null){
				rwater=data.rWater;
			}
			if(data.rFlow!=null){
				rflow=data.rFlow;
			}
			var content='<form id="deform" action="'+_context+'/water/dedata/savemap.shtml" autocomplete="off">名称：'+data.name+'<br>运行/采集状态：'+getCstatus(data.powerStatus, data.dataStatus);
			content+='<span class="mapedit" style="display:none;"><br><span class="mapedit" >上次累积流量:</span> <input class="mapedit lastAccu" onkeyup="autoc(&apos;deform&apos;)" style="width:77px;border: 1px solid #e2e2e4;" type="text" name="lastAccu" value="'+lastwater+'"/>';
			content+='<br><span class="mapedit" >本次累积流量:</span> <input class="mapedit thisAccu" style="width:77px;border: 1px solid #e2e2e4;" type="text" onkeyup="autoc(&apos;deform&apos;)" name="thisAccu" />';
			content+='<br><span class="mapedit" >周期(小时):</span> <input class="mapedit accuPeriod" id="deaccuPeriod" style="width:77px;border: 1px solid #e2e2e4;" onkeyup="autoc(&apos;deform&apos;)" type="text" name="accuPeriod" /></span>';
			content+='<br>流量值：'+rflow+'  <input class="mapedit flow" readonly="readonly" style="display:none;width:77px;color:#ff6c60;border: 0px solid #e2e2e4;" type="text" name="flow"/><br>'+'流量阈值：'+data.flow+'<br>水位值：'+rwater
			content+='  <input class="mapedit" style="display:none;width:77px;border: 1px solid #e2e2e4;" type="text" name="water"/><br>'+'水位阈值上限：'+data.water;
			content+='<br>'+'水位阈值下限：'+data.waterDwon;
			content += '<input type="hidden" name="pid" value="'+data.id+'" /></form><br/><button class="btn btn-xs btn-theme" onclick="showLines(\'\', 4, '+data.id+', true)"><i class="fa fa-bar-chart-o"></i></button>';
			
			addClickHandler(content,marker,data.name);
		});
		//默认初始疏干井不在地图上显示
		if($('.dewell').hasClass('grey')){
			removeMaekers(datas);
		}else{
			$.each(eval(datas), function(i, data) {
				map.addOverlay(demark);
			})
		}
		
	}
}
function setOwell(datas,hasright){
	
	if(datas!=null){
		
		$.each(datas, function(i, data) {
			var point = new BMap.Point(data.longitude,data.latitude);
			var sts = data.powerStatus; //0正常，1关闭，2故障
			var dataSts = data.dataStatus; //0正常，1，2，3告警
			if (sts==0 && dataSts>0) {
				sts=3;
			}
			var myIcon = new BMap.Icon('<c:url value="/resources/img/water/checks.png"/>', new BMap.Size(30,23), {imageOffset:new BMap.Size(-30*sts,-23)});
			var marker = new BMap.Marker(point,{icon:myIcon}); 
			marker.id=data.id;
			marker.status=data.status;
			omark[omark.length]=marker;
			if(!$('.owell').hasClass('grey')){
				map.addOverlay(marker);
			}
			var rflow="无";
			var rwater="无";
			if(data.rTemperature!=null){
				rflow=data.rTemperature;
			}
			if(data.rWater!=null){
				rwater=data.rWater;
			}
			var content='<form id="oform" action="'+_context+'/water/odata/savemap.shtml" autocomplete="off">名称：'+data.name+'<br>运行/采集状态：'+getCstatus(data.powerStatus, data.dataStatus)+'<br>水位值：'+rwater+'  <input type="text" class="mapedit" style="display:none;width:77px;border: 1px solid #e2e2e4;" name="water"/><br>'+'水位阈值上限：'+data.waterMeasurement;
			content+='<br>'+'水位阈值下限：'+data.waterDwon+'<br>水温值：'+rflow+'  <input type="text" class="mapedit" style="display:none;width:77px;border: 1px solid #e2e2e4;" name="temperature"/><br>'+'水温阈值：'+data.waterTemperature;
			content += '  <input type="hidden" name="pid" value="'+data.id+'" /></form><br/><button class="btn btn-xs btn-theme" onclick="showLines(\'\', 1, '+data.id+', true)"><i class="fa fa-bar-chart-o"></i></button>';
			
			addClickHandler(content,marker,data.name);
		});
	}
}
function setIwell(datas,hasright){
	
	if(datas!=null){
		
		$.each(datas, function(i, data) {
			var point = new BMap.Point(data.longitude,data.latitude);
			var sts = data.powerStatus; //0正常，1关闭，2故障
			var dataSts = data.dataStatus; //0正常，1，2，3告警
			if (sts==0 && dataSts>0) {
				sts=3;
			}
			var myIcon = new BMap.Icon('<c:url value="/resources/img/water/checks.png"/>', new BMap.Size(30,23), {imageOffset:new BMap.Size(-30*sts,-46)});
			var marker = new BMap.Marker(point,{icon:myIcon}); 
			marker.id=data.id;
			marker.status=data.status;
			imark[imark.length]=marker;
			if(!$('.iwell').hasClass('grey')){
				map.addOverlay(marker);
			}
			var rflow="无";
			var rwater="无";
			var lastwater="0";
			if(data.thisAccu!=null){
				lastwater=data.thisAccu
			}
			if(data.rFlow!=null){
				rflow=data.rFlow;
			}
			if(data.rPressure!=null){
				rwater=data.rPressure;
			}
			var content='<form id="iform" action="'+_context+'/water/idata/savemap.shtml" autocomplete="off">名称：'+data.name+'<br>运行/采集状态：'+getCstatus(data.powerStatus, data.dataStatus);  
			content+='<span class="mapedit" style="display:none;"><br><span class="mapedit" >上次累积流量:</span> <input class="mapedit lastAccu" onkeyup="autoc(&apos;iform&apos;)" style="width:77px;border: 1px solid #e2e2e4;" type="text" name="lastAccu" value="'+lastwater+'"/>';
			content+='<br><span class="mapedit" >本次累积流量:</span> <input class="mapedit thisAccu" style="width:77px;border: 1px solid #e2e2e4;" type="text" onkeyup="autoc(&apos;iform&apos;)" name="thisAccu" />';
			content+='<br><span class="mapedit" >周期(小时):</span> <input class="mapedit accuPeriod" id="iaccuPeriod" style="width:77px;border: 1px solid #e2e2e4;" onkeyup="autoc(&apos;iform&apos;)" type="text" name="accuPeriod" /></span>';
			content+='<br>流量值：'+rflow+' <input type="text" class="mapedit flow" readonly="readonly" style="display:none;width:77px;color:#ff6c60;border: 0px solid #e2e2e4;" name="flow"/><br>'+'流量阈值：'+data.flow+'<br>井内水位值：'+rwater;
			content += '   <input type="text"  style="display:none;width:77px;border: 1px solid #e2e2e4;" class="mapedit" name="pressure"/><input type="hidden" name="pid" value="'+data.id+'" /><br>'+'井内水位阈值：'+data.pressure+'</form><br/><button class="btn btn-xs btn-theme" onclick="showLines(\'\', 2, '+data.id+', true)"><i class="fa fa-bar-chart-o"></i></button>';
			
			addClickHandler(content,marker,data.name);
		});
	}
}
function getCstatus(powerStatus, dataStatus){
	var message = "";
	switch (powerStatus) {
	case 0:
		message = "正常";
		break;
	case 1:
		message = "<span class='text-danger'>关闭</span>";
		break;
	case 2:
		message = "<span class='text-danger'>故障</span>";
	}
	switch (dataStatus) {
	case 0:
		message += "/正常";
		break;
	default:
		message += "/<span class='text-danger'>告警</span>";
	}
	return message;
}
function setEwell(datas,hasright){
	if(datas!=null){
		
		$.each(datas, function(i, data) {
			var point = new BMap.Point(data.longitude,data.latitude);
			var sts = data.powerStatus; //0正常，1关闭，2故障
			var dataSts = data.dataStatus; //0正常，1告警
			if (sts==0 && dataSts>0) {
				sts=3;
			}
			var myIcon = new BMap.Icon('<c:url value="/resources/img/water/checks.png"/>', new BMap.Size(30,23), {imageOffset:new BMap.Size(-30*sts,-69)});
			var marker = new BMap.Marker(point,{icon:myIcon}); 
			emark[emark.length]=marker;
			if(!$('.ewell').hasClass('grey')){
				map.addOverlay(marker);
			}
			var rflow="无";
			if(data.rData!=null){
				rflow=data.rData;
			}
			var content='<form id="eform" action="'+_context+'/water/edata/savemap.shtml" autocomplete="off">名称：'+data.name+'<br>运行/采集状态：'+getCstatus(data.powerStatus, data.dataStatus)+'<br>监测值：'+rflow+'   <input type="text" style="display:none;width:77px;border: 1px solid #e2e2e4;" class="mapedit" name="data"/><br>'+'监测阈值：'+data.deformData;
			content+='<input type="hidden" name="pid" value="'+data.id+'" /></form>';
			content += '<button class="btn btn-xs btn-theme" onclick="showLines(\'\', 3, '+data.id+', true)"><i class="fa fa-bar-chart-o"></i></button>';
			
			addClickHandler(content,marker,data.name);
		});
	}
}

function domapedit(sid,date1){
	$('.mappost').show();
	$('.mapedit').show();
	$(".mapedit").keyup(function(){    
	    $(this).val($(this).val().replace(/[^\- \d.]/g,''));    
	}).bind("paste",function(){  
	    $(this).val($(this).val().replace(/[^\- \d.]/g,'')); //粘贴的不是数字，则替换为''    
	//或 return false; 禁用粘贴功能
	}).css("ime-mode", "disabled");
	var infow =map.getInfoWindow();
	if(infow){
		infow.redraw();
		//var ipt=infow.getPosition();
		//map.panTo(ipt);
		map.panBy(0,77);
	}
	if(sid!=null && date1!=null){
		$("#"+sid+"accuPeriod").val(getperid(date1));
	}
}

var opts = {
		width : 0,     // 信息窗口宽度
		height: 0,     // 信息窗口高度
		enableAutoPan: true,
		enableMessage:false//设置允许信息窗发送短息
	   };
function addClickHandler(content,marker,name,point){
	var infoWindow = new BMap.InfoWindow(content, opts);  // 创建信息窗口对象 
	marker.addEventListener("click", function(e){          
		map.openInfoWindow(infoWindow, point?point:e.target.point); //开启信息窗口
	});
	//增加hover事件
	marker.addEventListener("mouseover",function(e){
		var label = new BMap.Label(name,{offset:new BMap.Size(18,-18)});
		label.setStyle({
			 fontSize : "14px",
			 width: "auto",
			 paddingLeft: "10px",
			 paddingRight: "10px",
			 color: '#fff',
			 background: '#ff8355',
			 border: '1px solid "#ff8355"',
			 borderRadius: "5px",
			 textAlign: "center",
			 height: "26px",
			 lineHeight: "26px",
			 zIndex:9999
		 });
		marker.setLabel(label);
		marker.setTop(true);
	});
	marker.addEventListener("mouseout",function(e){  
		var label = this.getLabel();
		label.setContent("");//设置标签内容为空
		label.setStyle({border:"none",width:"0px",padding:"0px"});//设置标签边框宽度为0
		marker.setTop(false);
	});
}



//定位到某个具体的项目
function toProject(spid){
	$(".chartBtn").show();
	if($(window).width()<768){
		$(".cbtntool").show();
		$(".pmaptool").hide();
	}else{
		$(".pmaptool").show();
		$(".cbtntool").hide();
	}
	$('#loading').show();
	$.ajax({
		type : "POST",
		url : 'cpdetail.shtml',
		data :"cid="+pid,
		success : function(result) {
			$('#loading').hide();
			if(result!=null && result!="" ) {
				$("#pictitle").css("max-width", $(window).width()-960);
				$("#pictitle").html(result.name);
				$("#pictitle").attr("title", result.name);
				//初始化
				map.clearOverlays();
				camera = [];
								
				var zoom = map.getMapType().getMaxZoom();
				var point = new BMap.Point(result.longitude, result.latitude);
				map.centerAndZoom(point, zoom); 
				//map.panTo(point); //将地图的中心点更改为给定的点。如果该点在当前的地图视图中已经可见，则会以平滑动画的方式移动到中心点位置。
				map.setZoom(zoom); //先定点，再设置zoom大小
				//加载已有的围栏
				var rails = result.rail;
				var styleOptions = {
					strokeColor:"#0099cc",    //边线颜色。
    	            fillColor:"#ccccff",      //填充颜色。当参数为空时，圆形将没有填充效果。
					strokeWeight : 1, //边线的宽度，以像素为单位。
					strokeOpacity : 0.8, //边线透明度，取值范围0 - 1。
					fillOpacity : 0.6, //填充的透明度，取值范围0 - 1。
					strokeStyle : 'solid' //边线的样式，solid或dashed。
				}
				if (rails != null && rails != "") {
					var arails = rails.split(",");
					var points = [];
					$.each(arails, function(index, mpoints) {
						var latlnts = mpoints.split(":");
						var point = new BMap.Point(latlnts[0], latlnts[1]);
						points.push(point);
					});

					var oPolygon = new BMap.Polygon(points, styleOptions);
					map.addOverlay(oPolygon);

				}
				initDiag();
				setMarquee("showloading");
				$("#commonmodal").modal('hide');
			}
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			toastr.error('error失败！'+ errorThrown);	
			
		}
	});
}

//告警信息
function setMarquee(showloading){
	if(showloading!=null){
		$('#loading').show();
	}
	$.post('getWarning.shtml',{"pid":pid}, function(datas){
		if(datas!=null && datas!=""){
			if(pid!=-1){
				initWell();
				setPwell(datas.pwell,datas.hasright);
				setDewell(datas.dewell,datas.hasright);
				setOwell(datas.owell,datas.hasright);
				setIwell(datas.iwell,datas.hasright);
				setEwell(datas.ewell,datas.hasright);
			}
			//else{
				//setProject();
			//}
			var str="";
			$('#mitems').html('');
			warningTitle(datas.wpwell,"降水井");
			warningTitle(datas.wdewell,"疏干井");
			warningTitle(datas.wowell,"观测井");
			warningTitle(datas.wiwell,"回灌井");
			warningTitle(datas.wewell,"环境监测");
		}else{
			$('#mitems').html('');
		}
		$('#loading').hide();
	}).error(function() { 
		if(showloading!=null){
			$('#loading').hide();
		}
		});
}
function autoc(pform){
	var lastAccu=$("#"+pform +" .lastAccu").val();
	var thisAccu=$("#"+pform +" .thisAccu").val();
	var accuPeriod=$("#"+pform +" .accuPeriod").val();
	if(lastAccu!="" && thisAccu!="" && accuPeriod!=""){
		var low=(Number(thisAccu)-Number(lastAccu));
		low =low.div(Number(accuPeriod));
		$("#"+pform +" .flow").val(low);
	}
	
	
}
function getperid(date1){
	var	data2=new Date();
	var date = new Date(Date.parse(date1.replace(/-/g, "/")));
	var diff =(data2-date);
	var diff =diff.div(1000*60*60);
	if(isNaN(diff)){
		diff=0;
	}
	return diff;
}
function warningTitle(datas,title){
	
	if(datas!=null && datas.length>0){
		$('#mitems').append('<li>'+'<i class="fa fa-warning">&nbsp;</i>以下'+title+'有运行故障或者采集告警：'+'</li>');
		//str="以下"+title+"有故障或者告警信息：";
		$.each(datas, function(i, data) {
			var str=data.csite+"-"+data.name+"：";
			
			var powerStatus = data.powerStatus; //0正常，1关闭，2故障
			var dataStatus = data.dataStatus; //0正常，1，2，3告警
			var tmp = "";
			if(powerStatus==2){
				str+="运行故障";
				tmp = "&&";
			}
			if(dataStatus>0){
				str+=tmp+"采集告警";
			}
			$('#mitems').append('<li id="'+data.cid+'">'+str+'</li>');
		});
	}
	if($('#mitems').html()==''){
		$('.pwarn').hide();
	}else{
		$('.pwarn').show();
	}
}
function doauto(pid,url){
	$("#autolistId").val(pid);
	$("#auto-modal").modal('show');
	$("#autoos-form").attr("action", url);
} 	


</script>
<script src='<c:url value="/resources/js/project/chart.js?v=0.01" />'></script>
<script src='<c:url value="/resources/js/computer.js" />'></script>