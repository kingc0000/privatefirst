<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />'
	rel="styleshee">
<link href='<c:url value="/resources/data-tables/DT_table.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet">
	<link href='<c:url value="/resources/css/jquery.steps.css" />' rel="stylesheet">
	<style>
.wizard > .steps > ul > li
{
    width: 20% !important;
}
</style>
<script>
	var nowdate = "${nowdate}";
</script>

<div class="row" id="edittable" style="display: none;">
	<div class="col-md-12">
		<section class="panel">
			<header class="panel-heading">
				<span id="edittile"></span> <span class="tools pull-right"> <a
					href="javascript:;" class="fa fa-chevron-down"></a> <a
					href="javascript:;" class="fa fa-times"></a>
				</span>
			</header>
			<div class="panel-body">
				<form:form cssClass="form-horizontal" role="form"
					commandName="monitordaily" id="storeform">
					 <div id="wizard">
                          <h3>基本信息</h3>
                         	 <section>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">时间</label>
						<div class="col-lg-10">
							<input class="form-control required"  id="datec"  name="datec" readonly="readonly"/>
							
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">天气</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control" path="weather" id="weather" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">气温</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control" path="temperature"
								id="temperature" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">风级</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control" path="wind" id="wind" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">工程状况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="note" id="note" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">监测点情况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="pointDesc"
								id="pointDesc" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">监测意见</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="conclusion"
								id="conclusion" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">照片</label>
						<div class="col-lg-9 col-sm-9">
							<div class="row" id="wimgs"></div>
						</div>
						<div class="col-sm-1 col-xs-6">
							<button type="button"
								class="addBtn btn btn-info fa fa-plus-square"
								onclick="addImage()"></button>
						</div>
					</div>
				</section>
			 <h3>支护结构</h3>
			 	<section>
			 		<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">围护结构外观形态</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="weiHuW"
								id="weiHuW" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">冠梁、支撑、围檩裂缝</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="leiFeng"
								id="leiFeng" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">支撑、立柱变形</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="bianXing"
								id="bianXing" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">止水帷幕开裂、渗漏</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="senLou"
								id="senLou" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">墙后土体沉陷、裂缝及滑移</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="huaYi"
								id="huaYi" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">基坑涌土、流砂、管涌</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="guanYong"
								id="guanYong" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">其他</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="zhiHuOther"
								id="zhiHuOther" />
						</div>
					</div>
			 	</section>
			 	<h3>施工工况</h3>
			 	<section>
			 	 <div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">开挖区域土质情况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="tuZhi"
								id="tuZhi" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">基坑开挖分段长度及分层厚度</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="houDu"
								id="houDu" />
						</div>
					</div>
			 	 <div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">地表水、地下水状况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="shuiStatus"
								id="shuiStatus" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">基坑降水(回灌)设施运转情况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="huiGuan"
								id="huiGuan" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">基坑周边地面堆载情况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="duiZhai"
								id="duiZhai" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">其他</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="shiGongOther"
								id="shiGongOther" />
						</div>
					</div>
			 	</section>
			 	 <h3>周边环境</h3>
			 	<section>
			 	 <div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">管道破损、泄漏情况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="xieLou"
								id="xieLou" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">周边建筑裂缝</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="jianLFENG"
								id="jianLFENG" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">周边道路（地面）裂缝、沉陷</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="chenXian"
								id="chenXian" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">邻近施工情况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="neibor"
								id="neibor" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">其他</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="zbOther"
								id="zbOther" />
						</div>
					</div>
			 	 </section>
			 	 <h3>监测设施</h3>
			 	 <section>
			 	 <div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">基准点完好状况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="jiDian"
								id="jiDian" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">测点完好状况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="ceDian"
								id="ceDian" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">监测元件完好情况</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="yuanJian"
								id="yuanJian" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">观测工作条件</label>
						<div class="col-lg-10">
							<form:textarea cssClass="form-control" path="tiaoJian"
								id="tiaoJian" />
						</div>
					</div>
			 	 </section>
			 	</div>
					<form:hidden path="id" id="did" />
					<input type="hidden" name="delids" id="delids" />
					<input type="hidden" name="monitor.id" id="mid" />
				</form:form>
				<c:if test="${hasRight==true}">
				<div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="fomdatesubmit()"
								class="btn btn-success">提交</button>
						</div>
					</div>
				</c:if>	
			</div>
		</section>
	</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				列表
				<c:if test="${hasRight==true}">
				<button type="button"
					class="btn btn-theme pull-right fa fa-plus-square"
					onclick="changeEdittable(afernew)">新增</button>
				</c:if>	
					<button id="nextbtn" type="button"
					class="btn btn-theme pull-right fa fa-arrow-down visible-xs"
					onclick="donext()">下一页</button>
					<button type="button" id="lastbtn"
					class="btn btn-theme pull-right fa fa-arrow-up visible-xs"
					onclick="dolast()">上一页</button>
			</header>
			<div class="panel-body">
				<div class="col-lg-12 col-sm-12">
					<section class="adv-table " id="no-more-tables">
						<table class="display table table-bordered table-striped"
							id="slist">
							<thead>
								<tr>
									<th><input class="allCheckbox" type="checkbox" /></th>
									<th>时间</th>
									<th>天气</th>
									<th>气温</th>
									<th>风级</th>
									<th>操作</th>
								</tr>
							</thead>
						</table>
					</section>
				</div>
			</div>
		</section>
	</div>
</div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
	var mid = '${mid}'; //项目id
	var tar = ' target="_blank"';
	if ($(window).width()<768){
	tar='';
}
	var now='${nowdate}';
	$('#pictitle').html("${mentity.name }项目日志");
$(function() {
$('#slist').dataTable({
												"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSortClasses" : false,
							"sDom" : "lf<c:if test="${hasRight==true}"><'#del_btn'></c:if>rtip",
							"sAjaxSource" : "server_processing.shtml?mid=${mid}",
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							}, {
								"mData" : "datec"
							}, {
								"mData" : "weather"
							}, {
								"mData" : "temperature"
							}, {
								"mData" : "wind"
							}, {
								"mData" : "id"
							} ],
							"aoColumnDefs" : [
{
										"bVisible" : true,
										"aTargets" : [ 0 ],
										"mRender" : function(data, type, full) {
											return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';
										},
										"bSortable" : false
									},
									{
										"aTargets" : [ 5 ],
										"mRender" : function(data, type, full) {
											var reStr = '';
											reStr += '<a class="preview btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="预览" href="<c:url value="/water/monitordaily/preview.html?mid='
													+ mid
													+ '&did='
													+ full.id
													+ '"/>" '
													+ tar
													+ '><i class="fa fa-eye"></i></a>&nbsp;';
											var nowDate = new Date(nowdate);
											var myDate = new Date(full.datec);
											<c:if test="${hasRight==true }">
											if (nowDate.getFullYear() == myDate
													.getFullYear()
													&& nowDate.getMonth() == myDate
															.getMonth()
													&& nowDate.getDate() == myDate
															.getDate()) {
												reStr += '<a class="btn btn-primary btn-xs tooltips edit hasfn" data-placement="left" data-container="body" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>'
											}
											</c:if>
											return reStr;
										},
										"sWidth" : "120px",
										"bSortable" : false
									}, {
										"aTargets" : [ "_all" ],
										"bSortable" : false
									} ],
							"fnInitComplete" : function(settings, json) {
								$('.tooltips').tooltip();
								definedSidCheckbox();
							}
						});
						
				
var form = $("#storeform");
if(form.find('h3').length>0){
	form.children("div").steps({
	    headerTag: "h3",
	    bodyTag: "section",
	    transitionEffect: "slideLeft",
	    enableFinishButton:false,
	    onStepChanging: function (event, currentIndex, newIndex) {
	    	
	    	form.validate({
	    		ignore: ":disabled,:hidden"
	    	});
	    	 var result= form.valid();
	    	 if (result==false) return false;
	    	
	    	 return true;
	    },
	    onFinishing: function (event, currentIndex) {
	    	form.validate({
	    		ignore: ":disabled,:hidden"
	    	});
	    	
	        return form.valid();
	    },
	    onFinished: function (event, currentIndex) {
	    	if(hasright){
	    		jssubmit();
	    	}else{
	    		alert("没有权限！！！")
	    	}
	    	
        }
	});
}
	});
	function jssubmit() {
		$("#storeform").submit();
	}
	function dolistafterfn(oTable, nRow) {
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
		var aData = oTable.fnGetData(nRow);
		//异步加载图片
		$('#loading').show();
		$.ajax({
			type : "POST",
			url : "imges.shtml",
			dataType : "json",
			data : "muid="+aData["id"],
			success : function(data) {
				$('#loading').hide();
				if(data!="-1"){
					if(data!="0" && data!=null){
						//图片
						if(data!=null && data.length >0){
							for(var i=0;i<data.length;i++){
								addImage(data[i].dailyImage,data[i].jpeg,data[i].id,"MONITOR_DAILY");
							}
						}
					}
				}else{
					toastr.error("图片加载失败，请重试");
				}
			},
			error : function(data, xhr, textStatus, errorThrown) {
				console.log(data.responseText);
				//fnCallback(data);
				toastr.error("图片加载失败，请重试");
				$('#loading').hide();
			}
		});
		//重新初始化图片控件
		var aData = oTable.fnGetData(nRow);
		var fname= aData["datec"];
		$("input[name='monitor.id']").val(mid);
		$("#datec").val(fname);
	}
	function afernew(){
		$("#datec").val(now);
		$("input[name='monitor.id']").val(mid);
		var oTable = $('#slist').dataTable();
		var aData = oTable.fnGetData(0);
		if(aData!=null){
			var odate1=aData["datec"];
			if(now==odate1){
				editRow(oTable, 0,true);
			}
		}
	}
	
</script>
	<script src='<c:url value="/resources/js/jquery.steps.min.js" />'></script>
<script src='<c:url value="/resources/js/jquery.stepy.js" />'></script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
