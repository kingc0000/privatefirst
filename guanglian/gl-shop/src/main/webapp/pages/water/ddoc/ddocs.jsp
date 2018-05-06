<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet">
<div class="row" id="edittable" style="display: none;">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				<span id="edittile"></span> <span class="tools pull-right"> <a
					href="javascript:;" class="fa fa-chevron-down"></a> <a
					href="javascript:;" class="fa fa-times"></a>
				</span>
			</header>
			<div class="panel-body">
				<form:form cssClass="form-horizontal" role="form"
					commandName="ddoc" id="storeform" enctype="multipart/form-data">
					<div class="form-group">
						<label class="col-sm-2 control-label control-required">标题</label>
						<div class="col-sm-10 ">
							<form:input cssClass="form-control required" path="tite" />
						</div>
					</div>
					<div class="form-group">
                        <label class="col-lg-2 col-sm-2 control-label ">类别</label>
                        <div class="col-sm-10">
                            <form:select path="dtype" items="${applicationScope.dd_type}" id="dtype" itemLabel="name" itemValue="value"></form:select> 
                        </div>
                    </div>
					<div class="form-group">
						<label class="col-sm-2 control-label">说明</label>
						<div class="col-sm-10 ">
							<form:textarea cssClass="form-control" rows="6"  id="content" path="content"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">附件</label>
						<div class="controls col-md-10">
                            <div class="fileupload fileupload-new" data-provides="fileupload">
                              <span class="btn btn-white btn-file">
                              <span class="fileupload-new"><i class="fa fa-paper-clip"></i> 选择文件</span>
                              <span class="fileupload-exists"><i class="fa fa-undo"></i>修改</span>
                              <input type="file" class="default" id="fileupload" name="fileupload" value="${ddoc.fileName }"/>
                              </span>
                                <span class="fileupload-preview" style="margin-left:5px;"></span>
                                <a href="#" class="close fileupload-exists" data-dismiss="fileupload" style="float: none;font-size: 14px; margin-left:5px;">重设</a>
                                <span id="preview" style="margin-left:10px;" > </span>
                                <span id="download" style="margin-left:10px;" > </span>
                            </div>
                            
                        </div>
					</div>
					<sec:authorize access="hasRole('EDIT-PROJECT') and fullyAuthenticated">
					<div class="form-group">
						<div class="col-sm-2"></div>
						<div class="col-sm-10 ">
							<button type="button" onclick="jssubmit()"
								class="btn btn-success">
								保存
							</button>
						</div>
					</div>
					</sec:authorize>
					<form:hidden path="id" />
				</form:form>
			</div>
		</section>
	</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				文献库
				<sec:authorize access="hasRole('EDIT-PROJECT') and fullyAuthenticated">
				<button type="button"
					class="btn btn-theme pull-right fa fa-plus-square hidden-xs"
					onclick="changeEdittable(cleardownload)">
					新增
				</button>
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
						<table class="display table table-bordered table-striped"
							id="slist">
							<thead>
								<tr>
									<th><input class="allCheckbox" type="checkbox" /></th>
									<th>标题</th>
									<th>说明</th>
									<th>隶属人</th>
									<th>创建时间</th>
									<th>更新时间</th>
									<th>类别</th>
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
<jsp:include page="/common/cropperModal.jsp" />
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script>

<script>

$(function() {
	$('#pictitle').html('文献库');
	$('#slist').dataTable({
		"bPaginate" : true, //不显示分页
		"bProcessing" : true,
		"bServerSide" : true,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSortClasses" : false,
		"sDom": "lf<'#filter_btn'><sec:authorize access="hasRole('EDIT-PROJECT') and fullyAuthenticated"><'#del_btn'></sec:authorize>rtip",
		"oLanguage": {
			"oPaginate":opaging
			},
		"sAjaxSource" : "server_processing.shtml",
		"fnServerData" : retrieveData,
		"aoColumns" : [ {
			"mData" : "id"
		}, {
			"mData" : "tite"
		}, {
			"mData" : "content"
		}, {
			"mData" : "uname"
		}, {
			"mData" : "dateCreated"
		}, {
			"mData" : "dateModified"
		}, {
			"mData" : "dtype"
		}, {
			"mData" : "id"
		} ],
		"aoColumnDefs" : [
			{
				"bVisible" : true,
				"aTargets" : [ 0 ],
				"mRender" : function(data, type, full) {
					 if(full.sstatus==1){
						 return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';
					 }else{
						 return "";
					 }
				},
				"bSortable" : false
			},
			{
				"aTargets" : [ 6 ],
				"mRender" : function(data, type, full) {
					var rstr="";
					$("#dtype option").each(function () {
						if($(this).val()==data){
							rstr=$(this).text();
							return false;
						}
					});	
					return rstr;
				}
			},
			{
				"aTargets" : [ 7 ],
				"mRender" : function(data, type, full) {
					var reStr = '';
					if (full.fileName!=""&&full.fileName!=null) {
						if(full.fileType==true){
							reStr='<a class="btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="预览" href="<c:url value="/files/preview/'+full.fileName+'"/>"';
							if($(window).width()>767){
								reStr+='target="_blank"';
							}
							reStr+='><i class="fa fa-eye"></i></a>&nbsp;';
						}
						reStr+='<a class="btn btn-info btn-xs tooltips hidden-xs" data-placement="left" data-container="body" data-original-title="下载" href="<c:url value="/files/downloads/'+full.fileName+'"/>" ><i class="fa fa-download"></i></a>&nbsp;';
					}
					 if(full.sstatus==1){
						 reStr +='<a class="edit hasfn btn btn-primary btn-xs tooltips " data-placement="left" data-container="body" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;';
						 <sec:authorize access="hasRole('EDIT-PROJECT') and fullyAuthenticated">
						 reStr += '<a href="javascript:;" class="btn btn-danger btn-xs tooltips btn-del" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata('+ data+ ')"><i class="fa fa-trash-o "></i></a>';
						 </sec:authorize>
					 }	
					
					return reStr;
				},
				"sWidth" : "150px",
				"bSortable" : false
			}, {
				"aTargets" : [ "_all" ],
				"bSortable" : false
			} ],
		"fnInitComplete" : function(settings, json) {
			if(isTouchDevice()===false) {
       		 $('.tooltips').tooltip();
       		}
			definedSidCheckbox();
		}

	});
	var clone = $("#dtype").clone();
	clone.append('<option value="-1" selected="selected">类别</option>');
	clone.attr('id','ftype');
	clone.css("padding","15px");
	clone.height("34px");
	var filter_btn_div = $("#filter_btn");
	if (typeof(filter_btn_div)!='undefined') {
		$(filter_btn_div).addClass("dt_btn pull-right");
		$(filter_btn_div).append(clone);
		$(clone).on('change',function(){
			 $('#loading').show();
			 getfiter();
			 oTable.fnReloadAjax();
			 $('#loading').hide();
		});
	}
});

function getfiter(){
	filterjson= {"ftype":$("#ftype").val()};
}

function dolistafterfn(oTable, nRow){
	//$('.fileupload').clear();
	$('#fileupload').trigger('change', [ 'change' ]);
	$('#preview').html('');
	$('#download').html('');
	var aData = oTable.fnGetData(nRow);
	var fname= aData["fileName"];
	if(fname!=null && fname.replace(/(^s*)|(s*$)/g, "").length !=0){
		if(aData["fileType"]==true){
			var pstr ='<a class="tooltips" data-placement="left" data-container="body" data-original-title="预览" href="<c:url value="/files/preview/'+fname+'"/>"';
			if($(window).width()>767){
				pstr+=' target="_blank"';
			}
			pstr+='><i class="fa fa-eye">'+fname+'</i></a>&nbsp;';	
			$('#preview').html(pstr);	
		}
		$('#download').html('<a class="tooltips hidden-xs" data-placement="left" data-container="body" data-original-title="下载" href="<c:url value="/files/downloads/'+fname+'"/>" ><i class="fa fa-download">'+fname+'</i></a>&nbsp;');
	}
	
}

function cleardownload(){
	$('#fileupload').trigger('change', [ 'change' ]);
	$('#preview').html('');
	$('#download').html('');
	$("select").prop("selectedIndex", 0);
}
function jssubmit(){
	fomdatesubmit();
	 //$("#storeform").submit();
}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
