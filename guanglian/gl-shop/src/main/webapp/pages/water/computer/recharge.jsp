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
                <span id="edittile">承压含水层回灌井单井简易计算</span>
                <span ><button type="button"  class="btn btn-theme pull-right fa fa-refresh" onclick="clearf()">清空</button></span>
            </header>
            <div class="panel-body">
            	<form class="form-horizontal" id="storeform">
	         		<div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">回灌安全系数Fl</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input class="form-control required" id="fl" type="number"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">土层平均重度rs(kN/m3)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="rs"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">含水层顶板埋深hs(m)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="hs"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">单位设计回灌量qh(m3/h)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="qh"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">回灌井内水位P(MPa)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input class="form-control required" id="p" />
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">初始水位埋深h0(m)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="h0"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">导压系数a</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="a"/>
	                    </div>
	                </div>
	               <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">渗透系数k(m/d)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="k"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">含水层厚度M(m)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="m"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">时间系数(s)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="t"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">距离r</label>
	                    <div class="col-md-4 col-xs-6">
	                    	<input class="form-control required" id="rt"/>
	                    </div>
	                    <label class="col-md-4 col-xs-12 control-label text-left">允许输入多个深度h值，请用","分割</label>
	                </div>
	                <div class="form-group">
	                	<div class="row text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" style="font-weight:900;color:#000;">最大安全回灌水头:(rs/1.35/Fl/10-1）*hs = </label>
	                			<label class="control-label value" id="value1" style="font-weight:900;color:red;font-size:18px">?</label>
	                		</div>
	                	</div>
	                </div>
	                <div class="form-group">
	                	<div class="row text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" style="font-weight:900;color:#000;">最大可回灌量:qh（Hsaf+h0）= </label>
	                			<label class="control-label value" id="value2" style="font-weight:900;color:red;font-size:18px">?</label>
	                		</div>
	                	</div>
	                </div>
	                <div class="form-group">
	                	<div class="row text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" style="font-weight:900;color:#000;">回灌流量:qh（10*P+h0)= </label>
	                			<label class="control-label value" id="value3" style="font-weight:900;color:red;font-size:18px">?</label>
	                		</div>
	                	</div>
	                </div>
	                <div class="form-group">
	                	<div class="row text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" id="value4" style="font-weight:900;color:#000;word-wrap:break-word"><span style="font-weight:900;color:red;font-size:18px">?</span></label>
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
            			<div class="col-xs-12 col-sm-6 col-sm-offset-3" id="pchart">
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
var q=null;
var q0=null;
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
		var _fl = $("#fl").val();
		var _rs = $("#rs").val();
		var _qh = $("#qh").val();
		var _p = $("#p").val();
		var _h0 = $("#h0").val();
		var _a = $("#a").val();
		var _k = $("#k").val();
		var _m = $("#m").val();
		var _t = $("#t").val();
		var _hs = $("#hs").val();
		var _rt = $("#rt").val();
		
		var fl = Number(_fl);
		var rs = Number(_rs);
		var qh = Number(_qh);
		var p = Number(_p);
		var h0 = Number(_h0);
		var a = Number(_a);
		var k = Number(_k);
		var m = Number(_m);
		var t = Number(_t);
		var hs = Number(_hs);
		
		//灌水头
		if (_fl!=''&&_rs!=''&&_hs!='') {
			var t1=rs.div(1.35);
			t1=t1.div(fl);
			t1=t1.div(10);
			t1=Number(t1-1);
			t1=Number(t1*hs);
			$("#value1").html(t1);
			
		}
		//可回灌量
		if (qh!=''&&_h0!=''&&$("#value1").html()!='?') {
			var t1=Number($("#value1").html());
			t1=Number(t1+h0);
			t1=Number(qh*t1);
			$("#value2").html(t1);
			
		}
		//回灌流量Q 
		if (_qh!=''&&_p!=''&&_h0!='') {
			var t1 = Number(10*p);
			t1=Number(t1+h0);
			t1=Number(qh*t1);
			$("#value3").html(t1);
		}
		//水位抬升值
		if (_rt!=''&&_k!=''&&_m!=''&&_a!=''&&_t!=''&&$("#value3").html()!='?') {
			_rt = _rt.replace(/，/ig,",");
			var hes = _rt.split(",");
			var result = "不同距离(r1,r2,r3...,ri)水位抬升值<br>";
			$.each(hes, function(i, item){
				var h = Math.pow(item,2);
				var t1 = Number(4*Math.PI*k*m);
				var t2 =Number(2.25*a*t);
				t2=t2.div(h);
				t2=Math.log2(t2);
				t2=Number(t2*t1);
				var r=Number($("#value3").html());
				r=r.div(t2);
				result += r+",";
			});
			result = result.substring(0, result.length-1);
			var btn = ""; //按钮
			if(hes.length>1) {
				btn = "<br><button class='btn btn-theme btn-md' onclick='showChart()'><i class='fa fa-bar-chart-o ' >查看图标</i></button>";
			}
			$("#value4").html("<span class='result'>"+result+"</span>"+btn);
			
		}
		 		return;
	})
});

function showChart() {
	
	var hes = $("#rt").val().replace(/，/ig,",");
	var res = $(".result").html().substring($(".result").html().indexOf('<br>')+4);
	
	var titles = hes.split(",");
	var datas = res.split(",");
	
	$("#datamodal").modal('show');
	var pwidth=400;
	if($(window).width()<769){
		 pwidth=$("#datamodal .panel-body").width()-20;
	}
	
	$("#pchart").html('<canvas id="pointsChart" width="'+pwidth+'" height="'+pwidth+'"></canvas>');
	var ctx = $("#pointsChart");
	var myChart = new Chart(ctx, {type: 'line',
		data: {
			labels :titles,
	        datasets: [{
	            label: "水位抬升值",
	            data : datas,
	            backgroundColor: 'rgba(255, 99, 132, 1)',
	            borderColor: 'rgba(255,99,132,1)',
	            fill: false,
	            yAxisID: "y-axis-1"
	        }]
	    },
	    options: {
	    	responsive: false,
            tooltips: {
                mode: 'index',
                intersect: false
            },
	        scales: {
	        	xAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true
                    }
                }],
	            yAxes: [{
                    type: "linear", // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: '米'
                    },
                    position: "left",
                    id: "y-axis-1",
                    ticks: {
	                    beginAtZero:true
	                }
                }]
	        }
	    }
	});
	
}
	
</script>