<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>		
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />' rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">	
<link href='<c:url value="/resources/css/fileicon.css" />' rel="stylesheet">	
<link href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />' rel="stylesheet">	
<style>
select{
	width: 100%;
}
</style>	
<script type="text/javascript">
$('#loading').show();
	var mid ="${mid}";
	function goback(){
		javascript :history.back(-1);
	}
</script>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
		<header class="panel-heading"> 测点类别列表 
		</header>
		<div class="panel-body">
	      	<div class="adv-table tab-content" style="padding-top:20px;">
				<div class="col-lg-12" id="no-more-tables">
					<table class="display table table-bordered table-striped" >
	                	<thead>
		                	<tr class="text-center">
			                     <th colspan="2">测点类别</th>
			                     <th>警戒值日变量</th>
			                     <th>警戒值累计值</th>
			                     <th>备注</th>
			                     <th>操作</th>
			                </tr>
	               		 </thead>
	               		 <c:forEach var="mpoint" items="${ms}" varStatus="status">	
							<c:choose>
								<c:when test="${mpoint.mtype.type!='WaterLine'}">
									<tr>
										<td colspan="2">${mpoint.mtype.name}</td>
										<td><span id="dspan${status.index}">${mpoint.dailyVar}</span><input class="form-control"  style="display: none" id="dailyVar${status.index}" value="${mpoint.dailyVar}" onKeyUp="clearNoNum(event,this)" onBlur="checkNum(this)"/></td>
										<td><span id="tspan${status.index}">${mpoint.totalValue}</span><input class="form-control"  style="display: none" id="totalValue${status.index}" value="${mpoint.totalValue}" onKeyUp="clearNoNum(event,this)" onBlur="checkNum(this)"/></td>
										<td><span id="mspan${status.index}">${mpoint.memo}</span><input class="form-control"  style="display: none" id="memo${status.index}" value="${mpoint.memo}"/></td>
										<td><c:if test="${hasRight==true}"><a class="edit" id="click${status.index}" href="javascript:;" onclick="domsave(${status.index},${mpoint.id})">编辑</a></c:if>
											<a class="edit" style="display: none" id="mcancel${status.index}" href="javascript:;" onclick="tcancal(${status.index})">取消</a>
										</td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td rowspan="${fn:length(btype)}" style="vertical-align:middle;">管线</td>
										<c:forEach var="bt" items="${btype}" varStatus="sstatus" begin="0" end="0">
											<td>${bt.baseType.name}</td>
											<td><span id="btdspan${sstatus.index}">${bt.dailyVar}</span><input class="form-control"  style="display: none" id="btdailyVar${sstatus.index}" value="${bt.dailyVar}" onKeyUp="clearNoNum(event,this)" onBlur="checkNum(this)"/></td>
											<td><span id="bttspan${sstatus.index}">${bt.totalValue}</span><input class="form-control"  style="display: none" id="bttotalValue${sstatus.index}" value="${bt.totalValue}" onKeyUp="clearNoNum(event,this)" onBlur="checkNum(this)"/></td>
											<td><span id="bmspan${sstatus.index}">${bt.memo}</span><input class="form-control"  style="display: none" id="bmemo${sstatus.index}" value="${bt.memo}"/></td>
											<td><c:if test="${hasRight==true}"><a class="edit" id="bclick${sstatus.index}" href="javascript:;" onclick="dobsave(${sstatus.index},${bt.id})">编辑</a></c:if>
												<a class="edit" style="display: none" id="bcancel${sstatus.index}" href="javascript:;" onclick="bcancal(${sstatus.index})">取消</a>
											</td>
										</c:forEach>
										
									</tr>
									<c:forEach var="bt" items="${btype}" varStatus="sstatus" begin="1">
										<tr>
											<td>${bt.baseType.name}</td>
											<td><span id="btdspan${sstatus.index}">${bt.dailyVar}</span><input class="form-control"  style="display: none" id="btdailyVar${sstatus.index}" value="${bt.dailyVar}" onKeyUp="clearNoNum(event,this)" onBlur="checkNum(this)"/></td>
											<td><span id="bttspan${sstatus.index}">${bt.totalValue}</span><input class="form-control"  style="display: none" id="bttotalValue${sstatus.index}" value="${bt.totalValue}" onKeyUp="clearNoNum(event,this)" onBlur="checkNum(this)"/></td>
											<td><span id="bmspan${sstatus.index}">${bt.memo}</span><input class="form-control"  style="display: none" id="bmemo${sstatus.index}" value="${bt.memo}"/></td>
											<td><c:if test="${hasRight==true}"><a class="edit" id="bclick${sstatus.index}" href="javascript:;" onclick="dobsave(${sstatus.index},${bt.id})">编辑</a></c:if>
												<a class="edit" style="display: none" id="bcancel${sstatus.index}" href="javascript:;" onclick="bcancal(${sstatus.index})">取消</a>
											</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</c:forEach>
	           		 </table>
	            </div>
			
				</div>
	    </div>
	</section>
</div>
</div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
$('#pictitle').html("${mentity.name }告警值设置");
function domsave(index,pid){
	if ($("#dailyVar"+index).is(":hidden")){
			$("#dailyVar"+index).show();
			$("#totalValue"+index).show();
			$("#memo"+index).show();
			$("#mcancel"+index).show();
			$("#click"+index).text("保存");
			return ;
	}
	var dailyVar =$("#dailyVar"+index).val();
	var totalValue=$("#totalValue"+index).val();
	var memo =$("#memo"+index).val();
	if(dailyVar=="" || totalValue==""){
		toastr.error('日变量或者累计量的告警不能为空');
		return;
	}
	$('#loading').show();
	$.post('savempoint.shtml',{"dailyVar":dailyVar, "totalValue":totalValue,"pid":pid,"memo":memo}, function(response){
		var status = response.response.status;
		if(status==0){
			$("#dspan"+index).html(dailyVar);
			$("#tspan"+index).html(totalValue);
			$("#mspan"+index).html(totalValue);
			$("#dailyVar"+index).hide();
			$("#totalValue"+index).hide();
			$("#memo"+index).hide();
			$("#mcancel"+index).hide();
			$("#click"+index).text("编辑");
		}else{
			toastr.error('修改失败');
		}
		$('#loading').hide();
	}).error(function() {$('#loading').hide();toastr.error('获取数据失败'); });
}
function dobsave(index,bid){
	if ($("#btdailyVar"+index).is(":hidden")){
		$("#btdailyVar"+index).show();
		$("#bttotalValue"+index).show();
		$("#bmemo"+index).show();
		$("#bcancel"+index).show();
		$("#bclick"+index).text("保存");
		return ;
	}
	var dailyVar =$("#btdailyVar"+index).val();
	var totalValue=$("#bttotalValue"+index).val();
	var memo =$("#bmemo"+index).val();
	if(dailyVar=="" || totalValue==""){
		toastr.error('日变量或者累计量的告警不能为空');
		return;
	}
	$('#loading').show();
	$.post('savebtype.shtml',{"dailyVar":dailyVar, "totalValue":totalValue,"bid":bid,"memo":memo}, function(response){
		var status = response.response.status;
		if(status==0){
			$("#btdspan"+index).html(dailyVar);
			$("#bttspan"+index).html(totalValue);
			$("#bmspan"+index).html(totalValue);
			$("#btdailyVar"+index).hide();
			$("#bttotalValue"+index).hide();
			$("#bmemo"+index).hide();
			$("#bcancel"+index).hide();
			$("#bclick"+index).text("编辑");
		}else{
			toastr.error('修改失败');
		}
		$('#loading').hide();
		$('#loading').hide();
	}).error(function() {$('#loading').hide();toastr.error('修改失败');});
}

function tcancal(index){
	$("#dailyVar"+index).hide();
	$("#totalValue"+index).hide();
	$("#mcancel"+index).hide();
	$("#memo"+index).hide();
	$("#click"+index).text("编辑");
}

function bcancal(index){
	$("#btdailyVar"+index).hide();
	$("#bttotalValue"+index).hide();
	$("#bcancel"+index).hide();
	$("#bmemo"+index).hide();
	$("#bclick"+index).text("编辑");
}
function clearNoNum(event,obj){ 
    //响应鼠标事件，允许左右方向键移动 
    event = window.event||event; 
    if(event.keyCode == 37 | event.keyCode == 39){ 
        return; 
    } 
    //先把非数字的都替换掉，除了数字和. 
    obj.value = obj.value.replace(/[^\d.]/g,""); 
    //必须保证第一个为数字而不是. 
    obj.value = obj.value.replace(/^\./g,""); 
    //保证只有出现一个.而没有多个. 
    obj.value = obj.value.replace(/\.{2,}/g,"."); 
    //保证.只出现一次，而不能出现两次以上 
    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$","."); 
} 
function checkNum(obj){ 
    //为了去除最后一个. 
    obj.value = obj.value.replace(/\.$/g,""); 
} 
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>