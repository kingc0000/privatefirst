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
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/fileicon.css" />' rel="stylesheet">
<script>
$("#pictitle").html("报告审批");
</script>
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
				<form:form cssClass="form-horizontal" role="form" commandName="dreport" id="storeform">
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
                        <div class="col-sm-10">
                        	<label id="note" class="report" style="border:1px #eee solid;width:100%;padding:5px;max-height:500px;min-height:34px;overflow: auto"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label ">附件</label>
                        <div class="col-sm-10">
                        	<div id="attachview"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label ">作者</label>
                        <div class="col-sm-10">
                        	<label id="firstName" class="form-control report" style="border:0px"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label ">创建时间</label>
                        <div class="col-sm-10">
                        	<label id="dateCreated" class="form-control report" style="border:0px"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label ">送审时间</label>
                        <div class="col-sm-10">
                        	<label id="dateCreated" class="form-control report" style="border:0px"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label ">审核意见</label>
                        <div class="col-sm-10">
                        	<textarea rows="5" id="auditNote" style="width:100%"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
						<div class="col-sm-2"></div>
						<div class="col-sm-10">
							<button type="button" onclick="doAudit(1)"
								class="btn btn-success">
								通过
							</button>
							<button type="button" onclick="doAudit(0)"
								class="btn btn-default">
								不通过
							</button>
						</div>
					</div>
					<input type="hidden" id="rid" name="rid"/> 
				</form:form>
				<hr/>
				<div class="form-group">
                    <label class="col-sm-12 control-label ">送审流程：</label>
                </div>
                <jsp:include page="/pages/water/dreport/opinionlist.jsp"></jsp:include>
			</div>
		</section>
	</div>
	
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
		<section class="panel">
			<header class="panel-heading">
				待审批报告列表
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
									<th>编制时间</th>
									<th>更新时间</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody role="alert" aria-live="polite" aria-relevant="all">
							</tbody>
						</table>
					</section>
				</div>
			</div>
		</section>
	</div>
</div>
<jsp:include page='/pages/water/dreport/auditUsers.jsp'/>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script
	src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
	var status; //报告状态
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
			"sAjaxSource" : "getApproves.shtml",
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
							return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';
						},
						"bSortable" : false
					},
					{
						"aTargets" : [ 6 ],
						"mRender" : function(data, type, full) {
							var msg = "未知";
							if (data==0) msg = "编制中";
							else if (data==1) return "<span style='color:#FF6C60'>待审核</span>";
							else if (data==2) return "<span style='color:#FF6C60'>待审定</span>";
							else if (data==3) return "<span style='color:#FF6C60'>修订(审核)</span>";
							else if (data==4) return "<span style='color:#FF6C60'>修订(审定)</span>";
							else if (data==5) return "<span style='color:#000'>结束</span>";
							return msg;
						},
						"sWidth" : "150px",
						"bSortable" : false
					}, {
						"aTargets" : [ 7 ],
						"mRender" : function(data, type, full) {
							var res = "";
							res = '<a class="reportView btn btn-primary btn-xs tooltips hasfn" data-placement="left" data-container="body" data-original-title="查看" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;';
							
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
		/**
		* 查看报告，并审批
		**/
		$('#slist').on('click', 'a.reportView', function(e) {
			e.preventDefault();
			var nRow = $(this).parents('tr')[0];
			var aData = oTable.fnGetData(nRow);
			$('#edittile').html("审批");
			$("#rid").val(aData["id"]); //报告id
			$.each($("label[class~=report]"), function(i, item) {
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
			status = aData["status"]; //获取报告状态
			$("#title").html(aData.title);
			$("#edittable").show("slow");
			//处理审批意见
			opinionlist(aData);
		});		
	});
	
	//提交审核
	function doAudit(result) {
		var rid = $("#rid").val();
		var note = $("#auditNote").val();
		if(result=="0") {
			//confirm("确认审核不通过？")
			sendFun("sendReject.shtml", rid, note);
			//sendReject(rid, note);
		} else { //审核通过，选择审核人
			if (status=="2") {//报告当前为审定状态，通过，则流程结束
				sendFun("sendComplete.shtml", rid, note);
			} else { //选择审批人
				selectAudit(rid, note);
			}
		}
	}
	
	//提交	
	function sendFun(url, rid, note) {
		$('#loading').show();
		$.post(url, {"rid":rid, "note":note}, function(response){
			var status = response.response.status;
			if (status == 0 || status == 9999) {
				toastr.success("操作成功");
				refreshData();
			} else {
				toastr.error(response.response.statusMessage);
			}
			$("#edittable").hide();
			$('#loading').hide();
		});
	}
	
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
