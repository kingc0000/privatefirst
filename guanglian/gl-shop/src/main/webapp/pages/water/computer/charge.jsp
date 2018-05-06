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
                <span id="edittile">均质含水层承压水基坑涌水量计算</span>
                <span ><button type="button"  class="btn btn-theme pull-right fa fa-refresh" onclick="clearf()">清空</button></span>
            </header>
            <div class="panel-body">
            	<form class="form-horizontal" id="storeform">
	         		<div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">渗透系数k(m/d)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input class="form-control required" id="k" type="number"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">含水层厚度M(m)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="m"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">坑水位降深S(m)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="s"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">影响半径R(m)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input class="form-control required" id="r" readonly/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">过滤器长度l(m)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="l"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">等效半径r0(m)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="r0"/>
	                    </div>
	                </div>
	               <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">过滤器半径rss(m)</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="rss"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-md-offset-2 col-md-2 col-xs-6 control-label control-required">安全系数K</label>
	                    <div class="col-md-6 col-xs-6">
	                    	<input type="number" class="form-control required" id="ks"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                	<div class="text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" style="font-weight:900;color:#000;word-break: break-all; word-wrap:break-word;">基坑涌水量Q:2.73k*((M*S)/(lg(1+R/r0)+((M-l)/l)*lg(1+0.2*(M/r0))) = </label>
	                			<label class="control-label value" id="value1" style="font-weight:900;color:red;font-size:18px;word-break: break-all; word-wrap:break-word;">?</label>
	                		</div>
	                	</div>
	                </div>
	                <div class="form-group">
	                	<div class="text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" style="font-weight:900;color:#000;word-break: break-all; word-wrap:break-word;">单井涌水量qo:120*&pi;*rss*l*&sup3;&radic;k = </label>
	                			<label class="control-label value" id="value2" style="font-weight:900;color:red;font-size:18px;word-break: break-all; word-wrap:break-word;">?</label>
	                		</div>
	                	</div>
	                </div>
	                <div class="form-group">
	                	<div class="text-center">
	                		<div class="col-sm-12">
	                			<label class="control-label" style="font-weight:900;color:#000;word-break: break-all; word-wrap:break-word;">降水井预估n:K*Q/q0 = </label>
	                			<label class="control-label value" id="value3" style="font-weight:900;color:red;font-size:18px;word-break: break-all; word-wrap:break-word;">?</label>
	                		</div>
	                	</div>
	                </div>
	                
                </form>
        	</div>
        </section>
    </div>
</div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/js/computer.js" />'></script>
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
		var _k = $("#k").val();
		var _m = $("#m").val();
		var _s = $("#s").val();
		var _r = $("#r").val();
		var _l = $("#l").val();
		var _r0 = $("#r0").val();
		var _ks = $("#ks").val();
		var _rss = $("#rss").val();
		
		var k = Number(_k);
		var m = Number(_m);
		var s = Number(_s);
		var r = Number(_r);
		var l = Number(_l);
		var r0 = Number(_r0);
		var ks = Number(_ks);
		var rss = Number(_rss);
		
		//影响半径R
		if (_k!=''&&_s!='') {
			var t1=Number(10*s);
			var t2 = Math.pow(k,1/2);
			t2=Number(t2*t1);
			$("#r").val(t2);
			
		}
		//坑涌水(m)
		if (_k!=''&&_r!=''&&_m!=''&&_s!=''&&_l!=''&&_r0!='') {
			var t1=Number(m*s);
			var t2=Number(r.div(r0));
			t2=Number(t2+1);
			t2=Math.log10(t2);
			var t3=Number(m.div(r0));
			t3=Number(0.2*t3+1);
			t3=Math.log10(t3);
			var t4=Number(m-l);
			t4=Number(t4.div(l));
			t4=Number(t4*t3);
			t4=Number(t4+t2);
			t1=Number(t1.div(t4));
			t1=Number(2,73*k*t1);
			q=t1;
			$("#value1").html(t1);
			
		}
		//单井涌水量
		if (_rss!=''&&_l!=''&&_ks!='') {
			//var r = hs.sub(h0).mul(fs).mul(10).div(rs);
			var t1 = Number(120*Math.PI*rss*l);
			var t2 = Math.pow(k,1/3);
			var t1 = Number(t1*t2);
			q0=t1;
			//console.info(r);
			$("#value2").html(t1);
		}
		 
		//估计
		if(q!=null&&q0!=null&&_ks!=''){
			var t1=q.div(q0);
			t1=Number(t1*ks);
			$("#value3").html(t1);
		}
		return;
	})
});
	
</script>