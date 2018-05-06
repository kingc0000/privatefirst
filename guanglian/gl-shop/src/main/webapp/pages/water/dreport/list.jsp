<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>

<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet"/>
<link href='<c:url value="/resources/css/fileicon.css" />' rel="stylesheet">
<script>
$("#pictitle").html("报告列表");
</script>
<style>
.printnote table {
	width: 100%!important;
}
</style>
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
				<form:form cssClass="form-horizontal" role="form" commandName="dreport" id="storeform" enctype="multipart/form-data">
					<div class="form-group">
						<label class="col-sm-2 control-label control-required">标题</label>
						<div class="col-sm-10">
							<form:input cssClass="form-control required" path="title" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label control-required">项目等级</label>
						<div class="col-sm-10">
							<form:select path="rank" items="${applicationScope.bd_proj_rank}" itemLabel="name" itemValue="value"></form:select>
						</div>
					</div>
					<div class="form-group" id="rmodel" style="display:none">
						<label class="col-sm-2 control-label">模板选择</label>
						<div class="col-sm-10" id="tempDiv">
						</div>
					</div>
					<div class="form-group">
                         <label class="col-sm-2 control-label ">内容</label>
                         <div class="col-sm-10">
                         	<form:textarea cssClass="ckeditor form-control" rows="6"  id="note" path="note"/>
                         </div>
                       </div>
                    <div class="form-group">
                    	<label class="col-sm-2 control-label ">附件</label>
                      	<div class="col-sm-10 attachdiv">
                      		<div class="row">
								<div class="col-xs-10">
									<div class="alert alert-info" style="margin-bottom:0px">请选择附件上传</div>
									<div id="attachments"></div>
								</div>
								<div class="col-xs-2">
									<button type="button" class="addBtn btn btn-theme fa fa-plus-square" onclick="addAttach()"></button>
								</div> 
							</div>
							<div class="row attachItem">
								<div class="col-xs-12">
									<input type="file" name="attachments" class="pull-left form-control"/>
									<input type="text" name="attachNotes" class="pull-left form-control" placeholder='附件说明'/>
									<button type="button" class="delBtn btn btn-default pull-left fa fa-minus-square" onclick="removeAttach()"></button>
								</div>
							</div>
                      	</div>
                    </div>
					<div class="form-group">
						<div class="col-sm-2"></div>
						<div class="col-sm-10">
							<button type="button" onclick="fomdatesubmit()"
								class="btn btn-success">
								保存
							</button>
						</div>
					</div>
					<form:hidden path="id" />
					<form:hidden path="status" />
					<form:hidden path="user.id" id="userId"/>
					<input type="hidden" name="auditSection.dateCreated" id="dateCreated"/>
					<input type="hidden" name="delDigitalIds" id="delDigitalIds"/>
				</form:form>
			</div>
		</section>
	</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				报告编制列表
				<button type="button"
					class="btn btn-theme pull-right fa fa-plus-square"
					onclick="changeEdittable(toInsert)">
					<s:message code="label.generic.new" text="New" />
				</button>
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
									<th>作者</th>
									<th>项目等级</th>
									<th>创建时间</th>
									<th>更新时间</th>
									<th>状态</th>
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
<jsp:include page='/pages/water/dreport/auditUsers.jsp'/>
<div class="modal fade " id="opinionlist" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title">送审流程</h4>
            </div>
            <div class="modal-body row" id="modal-body">
               <jsp:include page="/pages/water/dreport/opinionlist.jsp"></jsp:include>
            </div>
        </div>
    </div>
</div>
<div class="modal fade " id="previewmodal" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title">设计报告</h4>
            </div>
            <div class="modal-body row" id="modal-body">
            	<div class="panel-body">
               <form:form cssClass="form-horizontal" role="form" commandName="dreport" id="previewform">
					<div class="form-group">
						<label class="col-sm-2 control-label">标题</label>
						<div class="col-sm-10">
							<label id="title" class="form-control report" style="border:0px"></label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">项目等级</label>
						<div class="col-sm-10">
							<label id="rank" class="form-control report" style="border:0px"></label>
						</div>
					</div>
					<div class="form-group">
                        <label class="col-sm-2 control-label ">内容</label>
                        <div class="col-sm-10 printObj">
                        	<div id="note" class="report printnote"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label ">附件</label>
                        <div class="col-sm-10">
                        	<div id="attachview"></div>
                        </div>
                    </div>
            	</form:form>
            	</div>
            </div>
			<div class="modal-footer">
				<button class="btn btn-info" type="button" onclick="printInfo(0)">打印草稿</button>
           		<button class="btn btn-success" id="waterBtn" type="button" onclick="printInfo(1)">打印(水印)</button>
                <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            </div>
        </div>
    </div>
</div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/ckeditor/ckeditor.js" />'></script>
<script>
	var templates = [];
	var delDigitalIds = ""; //需要删除的附件id字符串，用#作为分隔符
	var uid = '${sessionScope.ADMIN_USER.id}';
	$(function() {
		$('#slist').dataTable({
			"bPaginate" : true, //不显示分页
			"bProcessing" : true,
			"bServerSide" : true,
			"bLengthChange" : true,
			"bFilter" : true,
			"bSortClasses" : false,
			"sDom" : "lf<c:if test="${hasRight==true}"><'#del_btn'></c:if>rtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource" : "server_processing.shtml",
			"fnServerData" : retrieveData,
			"aoColumns" : [ {
				"mData" : "id"
			}, {
				"mData" : "title"
			}, {
				"mData" : "firstName"
			}, {
				"mData" : "rank"
			}, {
				"mData" : "dateCreated"
			}, {
				"mData" : "dateModified"
			}, {
				"mData" : "status"
			}, {
				"mData" : "id"
			}

			],
			"aoColumnDefs" : [
					{
						"bVisible" : true,
						"aTargets" : [ 0 ],
						"mRender" : function(data, type, full) {
							if (full.userId==uid) {
								return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';
							} else return '';
						},
						"bSortable" : false
					},
					{
						"aTargets" : [ 6 ],
						"mRender" : function(data, type, full) {
							var msg = "未知";
							var _a = '<a class="tooltips aopinion" style="color:#FF6C60" data-placement="right" href="javascript:;" data-container="body" data-original-title="查看审批流程">';
							if (data==0) msg = "编制中";
							else if (data==1) return _a+"待审核</a>";
							else if (data==2) return _a+"待审定</span>";
							else if (data==3) return _a+"修订(审核)</span>";
							else if (data==4) return _a+"修订(审定)</span>";
							else if (data==5) return _a+"<span style='color:#000'>结束</span></a>";
							return msg;
						},
						"sWidth" : "150px",
						"bSortable" : false
					}, {
						"aTargets" : [ 7 ],
						"mRender" : function(data, type, full) {
							var res = "";
							if ((full.status==0 || full.status==3 || full.status==4)&&full.userId==uid) {//编制中和待修正,并且是当前用户本人所创建，才可以编辑和送审
								res = '<a class="edit btn btn-primary btn-xs tooltips hasfn" data-placement="left" data-container="body" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;';
								res += '<a href="javascript:;" class="btn btn-success btn-xs tooltips" data-placement="left" data-container="body" data-original-title="送审"  onclick=\'selectAudit("' + data + '","")\'><i class="fa fa-briefcase"></i></a>&nbsp;';
							}
							if (full.status==0&&full.userId==uid) { 
								res += '<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata(' + data + ')"><i class="fa fa-trash-o "></i></a>&nbsp;';
							}
							if (full.status==5&&full.userId==uid) { 
								res = '<a class="edit btn btn-primary btn-xs tooltips hasfn" data-placement="left" data-container="body" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;';
								res += '<a href="javascript:;" class="btn btn-success btn-xs tooltips" data-placement="left" data-container="body" data-original-title="送审"  onclick=\'selectAudit("' + data + '","")\'><i class="fa fa-briefcase"></i></a>&nbsp;';
							}
							res += '<a href="javascript:;" class="btn btn-warning btn-xs tooltips preview" data-placement="left" data-container="body" data-original-title="预览"><i class="fa fa-print "></i></a>&nbsp;';
							//}	
							return res;
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
		//调用显示送审流程
		$('#slist').on('click', 'a.aopinion', function(e) {
			$("#opinionlist").modal('show');
			
			var nRow = $(this).parents('tr')[0];
			var aData = oTable.fnGetData(nRow);
			opinionlist(aData);
		});
		
		//预览，打印
		$('#slist').on('click', 'a.preview', function(e) {
			$("#previewmodal").modal('show');
			
			var nRow = $(this).parents('tr')[0];
			var aData = oTable.fnGetData(nRow);
			$.each($("#previewform [class~=report]"), function(i, item) {
				var id = $(item).attr("id");
				$(item).html(aData[id]);
			});
			//加载附件
			$("#attachview").html("");
			var attachments = JSON.parse(aData["attachments"]);
			if (typeof (attachments) == "object"
					&& attachments.length > 0) {
				for (var i = 0; i < attachments.length; i++) {
					var id = attachments[i]["id"];
					var note = attachments[i]["note"];
					var attachName = attachments[i]["attachName"];
					var type = attachments[i]["type"];
					var item = $('<div class="attachitem"></div>'); 
					var typeImg = $('<span class="file-icon file-icon-sm"></span>');
					var download = _context+"/files/downloads//DREPORT/"+attachName;
					$(typeImg).attr("data-type", type);
					$(item).append(typeImg).append('<span class="note"><a href="'+download+'" target="_blank">'+note+'</a></span>');
					$("#attachview").append(item);
				}
			}
			if(aData["status"]==5) {
				$("#waterBtn").show();
			} else {
				$("#waterBtn").hide();
			}
		});
		
		CKEDITOR.replace('note', {
			filebrowserImageBrowseUrl : '<c:url value="/admin/content/fileBrowser.html"/>',
			filebrowserImageUploadUrl : '<c:url value="/admin/content/image/upload.shtml"/>',
			height: '500px'
		});
		
		//获取模板数据
		$.post("templates.shtml", function(response){
			var status = response.response.status;
			if (status == 0 || status == 9999) {
				//处理模板数据
				var data = response.response.data;
				$.each(data, function(i, o) {
					templates["tid"+i] = o.description;
					var _input = $('<input type="radio" name="temp"/>');
					
					_input.attr("id", "tid"+i);
					_input.on("click", function(){
						var desc = templates[$(this).attr("id")];
						CKEDITOR.instances["note"].setData(desc);
					});
					$("#tempDiv").append(_input).append("&nbsp;&nbsp;").append(o.name).append("&nbsp;&nbsp;&nbsp;&nbsp;");
				});
			} else {
				toastr.error("获取模板数据出错");
			}
		});
	});
	//新增按钮前回调方法
	function toInsert() {
		$("#rmodel").show();
		$("#status").val(0);
		//$("#rank").val("1");
		$("select").prop("selectedIndex", 0);
		initAttach();
	}
	/**
	 * datatable点击编辑完的回调函数
	 * 编辑状态下，隐藏模板
	 */
	function dolistafterfn(oTable, nRow) {
		$("#rmodel").hide();
		initAttach();
		var aData = oTable.fnGetData(nRow);
		 //附件集合处理
		var attachments = JSON.parse(aData["attachments"]);
		if (typeof (attachments) == "object"
				&& attachments.length > 0) {
			for (var i = 0; i < attachments.length; i++) {
				var id = attachments[i]["id"];
				var note = attachments[i]["note"];
				var attachName = attachments[i]["attachName"];
				var type = attachments[i]["type"];
				var item = $('<div class="attachitem" id="attach_'+id+'"></div>'); 
				var typeImg = $('<span class="file-icon file-icon-sm"></span>');
				var download = _context+"/files/downloads//DREPORT/"+attachName;
				$(typeImg).attr("data-type", type);
				$(item).append(typeImg).append('<span class="note"><a href="'+download+'" target="_blank">'+note+'</a></span>').append('<span class="fa fa-times" onclick="delAttach(\''+id+'\')"></span>');
				$("#attachments").append(item);
			}
		}
	}
	
	/**
	* 打印
	**/
	function printInfo(type) {
		/* var newstr = $("#previewform #note").html(); 
		var headstr = "<html><head><title>上海广联</title></head><body>";
		var footstr = "</body>";
		
		var oldstr = document.body.innerHTML; 
		document.body.innerHTML = headstr+newstr+footstr; 
		window.print(); 
		document.body.innerHTML = oldstr; 
		return false; */
		if (type==0) {
			$("div.printObj").printArea({popTitle  : '上海广联', bodyClass : 'draftReport',extraCss   : '<c:url value="/resources/css/jquery.printarea.css" />'});	
		} else  {
			var options = {
				/* mode: "popup",  */
				extraHead: '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>',
				/* popClose   : false, */
	            extraCss   : '<c:url value="/resources/css/jquery.printarea.css" />',
	            retainAttr : ["id","class","style"], 
	            bodyClass : 'printReport',
	            popTitle  : '上海广联'
			}
			$("div.printObj").printArea(options); 
		}
	}
	
	//新增附件上传块
	function addAttach() {
		var clone = $(".attachItem").filter(":first").clone();
		$(".attachdiv").append(clone);
	}
	//删除附件删除块
	function removeAttach(e){
		if($(".attachItem").length>1){
			e=e||event;
			var o = $(e.target).parents(".attachItem");
			$(o).remove();
		}
	}
	//删除指定附件
	function delAttach(attachId) {
		var item = $("#attach_"+attachId);
		delDigitalIds += attachId + "#";
		$("#delDigitalIds").val(delDigitalIds);
		$(item).remove();
	}
	//初始化附件控件
	function initAttach() {
		delDigitalIds = ""; //清空附件id集合
		$("#delDigitalIds").val('');
		//附件信息
		$(".attachItem:gt(0)").remove();
		$("#attachments").html(""); //清空附件
	}
	
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script src='<c:url value="/resources/js/jquery.printarea.js" />'></script>