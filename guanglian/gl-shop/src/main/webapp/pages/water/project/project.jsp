<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />'
	rel="styleshee">
<link href='<c:url value="/resources/data-tables/DT_table.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />'
	rel="stylesheet">


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
					commandName="project" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">项目名称</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="name"
								id="name" />
							<span class="help-block"> <form:errors path="name"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
		                <label class="col-lg-2 col-sm-2 control-label control-required">项目负责人</label>
		                <div class="col-lg-10">
		                 <div class="input-group">
		              	 	<form:input cssClass="form-control required" path="projectOwner" readonly="true" id="projectOwner"  onclick="getList('请选择项目负责人','projectOwner','users.shtml','modal')"/>
		             	 	<span class="input-group-btn phonehide" >
		                     <button class="btn btn-theme tooltips" type="button"  data-placement="top" data-container="body" data-original-title="项目负责人在用户列表中选择，如果选择某个用户则该用户对此项目具有了编辑权限！！！" onclick="getList('请选择项目负责人','projectOwner','users.shtml','modal')"><i class="fa fa-user"></i></button>
		                   </span>
		             	  </div>
		            	 </div>
		            </div>
					<div class="form-group">
		                <label class="col-lg-2 col-sm-2 control-label control-required">工程特性</label>
		                <div class="col-lg-10">
		                    <form:select path="features" items="${applicationScope.bd_project_type}" itemLabel="name" itemValue="value"></form:select> 
		                </div>
		            </div>
					<div class="form-group">
		                <label class="col-lg-2 col-sm-2 control-label control-required">省份/直辖市</label>
		                <div class="col-lg-10">
		                	<form:input class="form-control required" path="zone.name" readonly="true" onclick="getList('请选择省份/直辖市','zone.name','zones.shtml')"/>
		                </div>
		            </div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">城市</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="city"
								id="city" />
							<span class="help-block"> <form:errors path="city"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">地址</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="address"
								id="address" />
							<span class="help-block"> <form:errors path="address"
									cssClass="error" /></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">备注</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control " path="memo"
								id="memo" />
							<span class="help-block"> <form:errors path="memo"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">项目简述</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control " path="summary"
								id="summary" />
							<span class="help-block"> <form:errors path="summary"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group" id="belongdepart">
						<label class="col-lg-2 col-sm-2 control-label control-required">所属部门</label>
						<div class="col-lg-10">
                			<input class="form-control required" id="department.name"  name="department.name" readonly="readonly" onclick="getList('请项目','department.name','departments.shtml','modal','domonitor')"/>
                		</div>
					</div>
					<div class="form-group" id="projecttype">
						<label class="col-lg-2 col-sm-2 control-label control-required">项目类型</label>
						<div class="col-lg-10">
							<form:select path="ptype" class="required form-control" name="ptype">
								<form:option value="0">监控</form:option>
								<form:option value="1">监护</form:option>
							</form:select>
						</div>
					</div>
					<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
					<div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="fomdatesubmit()"
								class="btn btn-success">提交</button>
						</div>
					</div>
					</sec:authorize>
					<form:hidden path="id" />
					<form:hidden path="projectOwnerid" name="projectOwnerid" id="projectOwnerid" />
     				 <form:hidden path="zone.id" id="zone.id" />
      				<form:hidden path="department.id" id="department.id"  name="department.id"/>
				</form:form>
			</div>
		</section>
	</div>
</div>
<div class="mail-box">
  	<aside class="sm-side hidden-xs">
		<div class="user-head">
			<div class="user-name">
				<a href="#" style="font-size: 14px; color: #fff;font-weight: bold">部门列表</a>
			</div>
		</div>
		<div class="inbox-body"></div>
		
		<div class=projectTree></div>
		<ul class="inbox-nav inbox-divider">
		</ul>
		<div>&nbsp;</div>
	</aside>
	<aside class="lg-side">

		<div class="inbox-head hidden-xs">
			<a style="font-size: 14px; color: #fff"
				href='javascript:treeDisplay()'><i id="distag"
				class="fa fa-chevron-left"></i></a>
		</div>
		<div class="inbox-body">
			<div class="row">
  <div class="col-md-12">
	<section class="panel">
	<header class="panel-heading"><span id="pname">全部</span>的项目列表
		<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
			<button type="button"  class="btn btn-theme pull-right fa fa-plus-square hidden-xs" onclick="changeEdittable(setpid)">新增</button>
		</sec:authorize>
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
	             <table class="display table table-bordered table-striped" id="slist">
	                 <thead>
	                 <tr>
	                	 <th><input class="allCheckbox" type="checkbox"/></th>
	                     <th>项目名称</th>
	                     <th>项目负责人</th>
	                     <th>联系电话</th>
	                     <th>省份</th>
	                     <th>城市</th>
	                     <th>地址</th>
	                     <th>部门</th>
	                     <th>备注</th>
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
		</div>
	</aside>	
  </div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-treeview/bootstrap-treeview.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/ckeditor/ckeditor.js" />'></script>
<script>
var pid = ${pid};
var pname='${pname}';
$('#pname').html(pname);
var mdid=${mdid};


jQuery(document).ready(function() {
	
	$('#pictitle').html('项目管理');
	getProjectTreeForCsite(
			'getTree.shtml',
			$(".projectTree"), goodslist);
	//$('#edittable').hide();
	$('#slist').dataTable( {
		"bProcessing": true,
		"bServerSide": true,
		"bLengthChange": true,
		"bFilter": true,
		"bSortClasses": false,
		"sAjaxSource": "server_processing.shtml?departmentID="+pid,
		"fnServerData" : retrieveGoods,
		"sDom": "lf<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated"><'#del_btn'></sec:authorize>rtip",
		"oLanguage": {
			"oPaginate":opaging
			},
		"aoColumns": [
	                    { "mData": "id"},
	                    { "mData": "name"},
	                    { "mData": "projectOwner"},
	                    { "mData": "phone"},
	                    { "mData": "zone.name"},
	                    { "mData": "city"},
	                    { "mData": "address" },
	                    { "mData": "department.name"},
	                    { "mData": "memo"},
	                    { "mData": "id" }

	                ],
            "aoColumnDefs": [
                         {"aTargets": [0],  "mRender":function(data,type,full){
                          	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                          },"bSortable": false
                          },
                         {"aTargets":[9],"mRender":function(data,type,full){
                        	 var reStr="";
                        	 if(full.sstatus==1)	{
                        		 reStr +='<a class="edit btn hasfn btn-primary btn-xs tooltips" title="修改" data-placement="left" data-container="body" data-original-title="修改"  href="javascript:;" ><i  class="fa fa-pencil"></i></a>&nbsp;'
                     	 			+'<a href="javascript:;" class="btn btn-danger btn-xs tooltips" title="删除" data-placement="left" data-container="body" data-original-title="删除" onclick="deldata('+data+')"><i  class="fa fa-trash-o"></i></a>&nbsp;';
                            } 	
                        	 	return reStr;
                         	},
                         	"sWidth":"110px","bSortable": false
                        },
                        {
                        	"aTargets":["_all"],"bSortable": false
                        }
                     ] ,
                     "fnInitComplete": function(settings, json) {
                    	 if(isTouchDevice()===false) {
                    		 $('.tooltips').tooltip();
                    		}
                    	 definedSidCheckbox();
                    	 this.fnSetColumnVis(9,settings.hasRight);
                    	 $('#loading').hide();
                     }
		
	} 
	);
	
	
	
	CKEDITOR.replace(  'summary',
	{
		filebrowserImageBrowseUrl :  '<c:url value="/admin/content/fileBrowser.html"/>',
		filebrowserImageUploadUrl :  '<c:url value="/admin/content/image/upload.shtml"/>'
	});
	
});

function setpid(){
	if(pid!=-1){
		$("input[id='department.id']").val(pid);
		$("input[id='department.name']").val(pname);
	}else{
		$("input[id='department.id']").val('');
		$("input[id='department.name']").val('');
	}
	$("#features").val(0);
	$("input[id='status']").val(0);
	//$("select[id='rank']").val(1);
	$("select").prop("selectedIndex", 0);
	//清空图像id,删除之前添加的图像
	$('#delids').val('');
	$(".imgItem").remove();
	$("#belongdepart").show();
	domonitor();
}
//点击树节点触发的点击事件，刷新右边的商品列表集合
function goodslist(data) {
	pid = data.id;
	/**
	if(pid==mdid){
		$("#projecttype").show();
	}else{
		$("#projecttype").hide();
	}*/
	$('#pname').html(data.text);
	pname=data.text;
	setpid();
	//恢复otable初始设置
	oTable = $('#slist').dataTable();
	var oSettings = oTable.fnSettings();
	oSettings._iDisplayStart = 0; //数据访问置0
	refreshData();
	$("#edittable").hide();
}
function treeDisplay() {
	$(".sm-side").toggle("normal");
	$("#distag").toggleClass("fa-chevron-right");
}
function retrieveGoods(sSource, aoData, fnCallback) {
	$('#loading').show();
	var data = {
		"aoData" : JSON.stringify(aoData)
	};
	if (!jQuery.isEmptyObject(filterjson)) {
		data = $.extend({}, {
			"aoData" : JSON.stringify(aoData)
		}, filterjson);
	}
	$.ajax({
		type : "POST",
		url : "server_processing.shtml?departmentID="+pid,
		dataType : "json",
		data : data,
		success : function(data) {
			fnCallback(data); // 服务器端返回的对象的returnObject部分是要求的格式
			oTable.fnSetColumnVis(9,data.hasRight);
			$('#loading').hide();
		},
		error : function(data, xhr, textStatus, errorThrown) {
			console.log(data.responseText);
			fnCallback(data);
			$('#loading').hide();
		}
	});
}
function dolistafterfn(oTable, nRow){
	var aData = oTable.fnGetData(nRow);
	var summry=aData["summary"];
	
	CKEDITOR.instances['summary'].setData(summry);
	domonitor();
	$("#belongdepart").hide();
	$("#projecttype").hide();
}

function domonitor(){
	$("#projecttype").hide();
	if(mdid!=-1){
		if($('input[name="department.id"]').val()==mdid){
			$("#projecttype").show();
		}
	}
}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script src='<c:url value="/resources/js/project/tree.js" />'></script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>
