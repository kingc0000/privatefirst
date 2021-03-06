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
<script>
	var btype=new Array();
	<c:forEach items="${bt}" var="btl">
    var obj =new Object();
		obj.name="${btl.name }";
		obj.value="${btl.value}";
		btype[btype.length]=obj;
	</c:forEach>
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
					commandName="waterline" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">管线类型</label>
						<div class="col-lg-10">
							<select class=" form-control required" id="lbt" onchange="setNo(this.options[this.options.selectedIndex].value)">
								<c:forEach items="${bt}" var="content">
                                  		<option value="${content.value}">${content.name }</option>
                                  	</c:forEach>
							</select> 
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">开挖前累计量位移</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="frontDisplacement"
								id="frontDisplacement" />
							<span class="help-block"> <form:errors path="frontDisplacement"
									cssClass="error" /></span>
						</div>
					</div>					
					<jsp:include page="/common/mBase.jsp"></jsp:include>
					<c:if test="${hasRight==true}">
					<div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="jssubmit()"
								class="btn btn-success">提交</button>
						</div>
					</div>
					</c:if>
					<form:hidden path="id" />
					<form:hidden path="monitor.id" />
				</form:form>
			</div>
		</section>
	</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				管线列表
				<c:if test="${hasRight==true}">
				<button type="button"
					class="btn btn-theme pull-right fa fa-plus-square"
					onclick="changeEdittable(afternew)">新增</button>
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
									<th>点号</th>
									<th>初始高程</th>
									<th>开挖前累计量位移</th>
									<th>备注</th>
									<th>类型</th>
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
<jsp:include page="/pages/water/import/impordsface.jsp" />
<jsp:include page="/common/exportModal.jsp" />
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
$('#pictitle').html("${mentity.name }管线信息");
	var mid = '${mid}';
	$(function() {
		$('#slist')
				.dataTable(
						{
							"bPaginate" : true, //不显示分页
							"bProcessing" : true,
							"bServerSide" : true,
							"bLengthChange" : true,
							"bFilter" : true,
							"bSortClasses" : false,
							"sDom" : "lf<c:if test="${hasRight==true }"><'#import_btn'><'#export_btn'><'#del_btn'><'#importpoint_btn'></c:if><'#filter_btn'>rtip",
							"sAjaxSource" : "server_processing.shtml?mid="
									+ mid,
							"fnServerData" : retrieveData,
							"aoColumns" : [ {
								"mData" : "id"
							}, {
								"mData" : "markNO"
							}, {
								"mData" : "initHeight"
							},  {
								"mData" : "frontDisplacement"
							}, {
								"mData" : "memo"
							}, {
								"mData" : "markNO"
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
											var s='未定义类型';
											for(var i=0;i<btype.length;i++){
												var reg=new RegExp("^"+btype[i].value); 
												if(strstart(reg,data)){
													return btype[i].name;
												}
											}
											return s;
										},
										"sWidth" : "120px",
										"bSortable" : false
									},{
										"aTargets" : [ 6 ],
										"mRender" : function(data, type, full) {
											var s = '<a class="btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="数据列表" href="'
													+ _context
													+ '/water/waterlinedata/list.html?sid='
													+ full.id
													+ '&mid='
													+ mid
													+ '" ><i class="fa fa-list"></i></a>&nbsp;';
											s += '<a class="edit hasfn btn btn-primary btn-xs tooltips " data-placement="left" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';
											s += '&nbsp;<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-original-title="删除" onclick="deldata('
													+ data
													+ ',&quot;您真的要删除该对象吗？&quot; )"><i class="fa fa-trash-o "></i></a>';
											return s;
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
		//增加导入按钮
		$("#export_btn")
				.append(
						'<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-download" style="color:red"></i>导出</button>');
		$("#export_btn").addClass("dt_btn pull-right");
		$("#export_btn").on('click', function() {
			var ids = '';
			$('.sidCheckbox:checked').each(function(i, o) {
				ids += $(o).val() + ",";
			});
			if (ids == '') {
				alert("请至少选择一个需要导出的数据");
				return;
			}
			if (ids.indexOf(',') != -1) {
				ids = ids.substring(0, ids.length - 1);
			}
			$("#eid").val(ids);
			$('#etitle').html('管线数据');
			$('#export-modal').modal('show');
		});
		var dseelct ='<div style="padding: 15px 5px 15px 0;" class="pull-right"><select id="sclass" class="form-control">';
		for(var i=0;i<btype.length;i++){
			dseelct+='<option value ="'+btype[i].value+'">'+btype[i].name+'</option>'
		}
		dseelct+='<option value ="" selected = "selected">管线类型</option></select></div>'
		 $("#filter_btn").html(dseelct);
		 $('#sclass').change(function(){
			 getfiter();
			 oTable.fnReloadAjax();
		 });
		 //增加导入按钮
		$("#import_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-upload" style="color:red"></i>测点数据导入</button>');
		$("#import_btn").addClass("dt_btn pull-right");
		$("#import_btn").on('click',function(){
			importdata();
		});
		$("#importpoint_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-upload" style="color:red"></i>测点导入</button>');
		$("#importpoint_btn").addClass("dt_btn pull-right");
		$("#importpoint_btn").on('click',function(){
			importpoint();
		});
	});
	function importpoint(){
		$('#iid').val(mid);
		$('.monitordata').hide();
		$('.monitorpoint').show();
		$('.monitordatacomon').show();
		$('.monitorpointsupaxial').hide();
		importexcel('url');
	}
	function jssubmit() {
		$("#storeform").submit();
	}
	function getfiter(){
		filterjson= {"markNO":$("#sclass").val()};
	}
	function setNo(sno){
		
		var mno =$("#markNO").val();
		if(mno!=""){
			var reg=new RegExp("^"+sno); 
			if(!strstart(reg,mno)){
				//数字开头
				var reg1=/^\d+/;
				if(!strstart(reg1,mno)){
					$("#markNO").val(sno+mno.substring(2));
				}else{
					$("#markNO").val(sno+mno);
				}
			}
		}else{
			$("#markNO").val(sno);
		}
	}
	function strstart(reg,str){
		return reg.test(str);  
	}
	function dolistafterfn(oTable, nRow) {
		$("input[name='monitor.id']").val(mid);
	}
	function afternew() {
		$("input[name='monitor.id']").val(mid);
		//$('#csitesamediv').show();
	}
	function importdata(){
		$('#iid').val(mid);
		$('.monitordata').show();
		$('.monitorpoint').hide();
		$('.monitordatacomon').hide();
		$('.monitorpointsupaxial').hide();
		importexcel();
		
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>
