<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>

<script>
$("#pictitle").html("地下水计算分析");
</script>

<div class="row" id="edittable">
	<div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                <span id="edittile">承压水抗突涌计算</span>
                <span ><button type="button"  class="btn btn-theme pull-right fa fa-refresh" onclick="clearf()">清空</button></span>
            </header>
            <div class="panel-body">
            	<form class="form-horizontal" id="storeform">
	         		<div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">初始水位(埋深)h0</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input class="form-control required" id="h0" type="number"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">含水层顶板埋深hs</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="hs"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">平均重度rs</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="rs"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">基坑开挖深度h</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input class="form-control required" id="h"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">安全系数Fs</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="fs"/>
	                    </div>
	                </div>
	                
	                <div class="form-group">
	                	<div class="text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" style="font-weight:900;color:#000;word-break: break-all; word-wrap:break-word;">临界开挖深度H（m）:hs-10*(hs-h0)*Fs/rs = </label>
	                			<label class="control-label" id="value1" style="font-weight:900;color:red;font-size:18px;word-break: break-all; word-wrap:break-word;">?</label>
	                		</div>
	                	</div>
	                </div>
	                <div class="form-group">
	                	<div class="text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" style="font-weight:900;color:#000;word-break: break-all; word-wrap:break-word;">抗突涌安全系数Fs0:(hs-h)rs/10/(hs-h0) = </label>
	                			<label class="control-label" id="value3" style="font-weight:900;color:red;font-size:18px;word-break: break-all; word-wrap:break-word;">?</label>
	                		</div>
	                	</div>
	                </div>
	                <div class="form-group">
	                	<div class="text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" id="value2" style="font-weight:900;color:#000;"><span  style="font-weight:900;color:red;font-size:18px;word-break: break-all; word-wrap:break-word;">?</span></label>
	                		</div>
	                	</div>
	                </div>
	                
                </form>
        	</div>
        </section>
    </div>
</div>
<!-- modal : 数据图 -->
<div class="modal fade " id="datamodal" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"><span class="projectname text-warning"></span>数据图</h4>
            </div>
            <div class="modal-body" id="modal-body">
            	<div class="panel-body">
            		<div class="row">
            			<div class="col-xs-12 col-sm-10 col-sm-offset-2" id="pchart">
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
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
<script src='<c:url value="/resources/js/Chart.min.js"/>'></script> 
<script>
var arrayObj = new Array([6]); 
jQuery(document).ready(function() {
	validator = $("#storeform").validate({
		invalidHandler : function() {
			return false;
		},
		//指明错误放置的位置
		errorPlacement : function errorPlacement(error, element) {
			element.after(error);
		},
		submitHandler : function() {
			return false;// 阻止form提交
		}
	});
	
	$("input[class~='form-control']").on("keyup", function(event) {
		var _h0 = $("#h0").val();
		var _hs = $("#hs").val();
		var _rs = $("#rs").val();
		var _fs = $("#fs").val();
		var _h = $("#h").val();
		
		var h0 = Number(_h0);
		var hs = Number(_hs);
		var rs = Number(_rs);
		var fs = Number(_fs);
		var h = Number(_h);
		//var h = Number(_h);
		
		//计算临界深度
		if (_h0!=''&&_hs!=''&&_rs!=''&&_fs!='') {
			//var r = hs.sub(h0).mul(fs).mul(10).div(rs);
			var t1 = hs-h0;
			var t2 = Number(t1*fs*10);
			var r = t2.div(rs);
			r=hs-r;
			//console.info(r);
			$("#value1").html(r);
		}
		//基坑开挖深度(m)
		if (_h0!=''&&_hs!=''&&_rs!=''&&_fs!=''&&_h!='') {
			var tmp = 0;
			var resutl='基坑开挖深度(m)<br>基坑开挖深度H0:';
			var hh0=Number(10*(hs-h0)*fs);
			hh0 = hh0.div(rs);
			hh0=hs-hh0-3;
			resutl+=hh0;
			resutl+=' 安全水位埋深:'+h0;
			arrayObj[0] = {x:hh0, y: h0};
			
			var hh=Number(10*(hs-h0)*fs);
			hh = hh.div(rs);
			hh =hs-hh;
			tmp = hh;
			resutl+='<br>基坑开挖深度H:'+hh;
			hh=Number(10*hs*fs-(hs-hh)*rs);
			hh=hh.div(10);
			hh=hh.div(fs);
			resutl+=' 安全水位埋深:'+hh;
			arrayObj[1] = {x:tmp, y: hh};
			
			resutl+='<br>基坑开挖深度H1:'+h;
			var hh1=Number(10*hs*fs-(hs-h)*rs);
			hh1=hh1.div(10);
			hh1=hh1.div(fs);
			resutl+=' 安全水位埋深:'+hh1;
			arrayObj[2] = {x:h, y: hh1};
			
			resutl+='<br>基坑开挖深度H2:'+Number(hs-1);
			var hh2=Number(10*hs*fs-rs);
			hh2=hh2.div(10);
			hh2=hh2.div(fs);
			resutl+=' 安全水位埋深:'+hh2;
			arrayObj[3] = {x:Number(hs-1), y: hh2};
			
			resutl+='<br>基坑开挖深度H3:'+Number(hs-1);
			resutl+=' 安全水位埋深:'+Number(hs);
			arrayObj[4] = {x:Number(hs-1), y: hs};
			
			resutl+='<br>基坑开挖深度H4:'+Number(hs+5);
			resutl+=' 安全水位埋深:'+Number(hs+4);
			arrayObj[5] = {x:Number(hs+5), y: Number(hs+4)};
			
			resutl+='<br><button class="btn btn-theme btn-md" onclick="showChart()"><i class="fa fa-bar-chart-o" ></i></button>';
			
			$("#value2").html(resutl);
		} 
		//计算抗突涌安全系数
		if(_h0!=''&&_hs!=''&&_h!=''&&_rs!=''){
			var t1=Number((hs-h)*rs);
			t1=t1.div(10);
			t1=t1.div(Number(hs-h0));
			$("#value3").html(t1);
		}
		return;
	})
});

function showChart() {
	
	$("#datamodal").modal('show');
	var pwidth=400;
	if($(window).width()<769){
		 pwidth=$("#datamodal .panel-body").width()-20;
	}
	
	$("#pchart").html('<canvas id="pointsChart" width="'+pwidth+'" height="'+pwidth+'"></canvas>');
	var ctx = $("#pointsChart");
	console.log(arrayObj);
	var myChart = Chart.Scatter(ctx, {
		data: {
	        datasets: [{
	            label: "安全水位埋深(m)",
	            data: arrayObj,
	            backgroundColor: 'rgba(255, 99, 132, 1)',
	            borderColor: 'rgba(255,99,132,1)',
            	fill:false
	        }]
	    },
	    options: {
	    	responsive: false,
            tooltips: {
                mode: 'index',
                intersect: false
            },
            title: {
                display: true,
                text: '承压水抗突涌计算'
            },
	        scales: {
	        	xAxes: [{
            		position: 'bottom',
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: '基坑开挖深度(m)'
                    }
                }],
	            yAxes: [{
                    type: "linear", // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: '安全水位埋深(m)'
                    },
                    position: "left",
                    id: "y-axis-1",
                    ticks: {
	                    beginAtZero:true,
	                    reverse: true
	                }
                }]
	        }
	    }
	});
	
}
	
</script>