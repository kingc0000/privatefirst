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
<link href='<c:url value="/resources/assets/bootstrap-datetimepicker/css/datetimepicker.css" />' rel="stylesheet">			
<script type="text/javascript">
		$('#loading').show();
		var pid ="${pid}";
		//设置title
		var pageTitle="环境监控${pName}";
		$('#pictitle').html(pageTitle);
		$('#panel-heading').html(pageTitle);
		var phonetype='${ptype}';
</script>
<div class="row" id="edittable" style="display:none;">	
	<div class="col-lg-12 col-sm-12">
				<section class="panel" >
                      <header class="panel-heading">
                          <span id="edittile"></span>
                          <span class="tools pull-right">
                            <a href="javascript:;" class="fa fa-chevron-down"></a>
                            <a href="javascript:;" class="fa fa-times"></a>
                          </span>
                      </header>
                      <div class="panel-body">
                      	<form:form cssClass="form-horizontal" role="form" commandName="pData" id="storeform">
							<form:errors path="*"
								cssClass="alert alert-block alert-danger fade in" element="div" />
							<div id="store.success" class="alert alert-success"
								style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">提交成功</div>
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label control-required">环境数据</label>
								<div class="col-lg-10">
                                    <form:input cssClass="form-control required"  type="number" path="data" id="data" />
                                       <span class="help-block"> <form:errors path="data" cssClass="error" /></span>
                               </div> 
							</div>
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label ">观测时间(为空则按系统默认时间)</label>
								<div class="col-lg-10">
								<div  class="input-group date datetimeControl">
                                       <input type="text" class="form-control"  id="dateCreated" name="auditSection.dateCreated">
                                       <div class="input-group-btn">
                                           <button type="button" class="btn btn-danger date-reset"><i class="fa fa-times"></i></button>
                                           <button type="button" class="btn btn-warning date-set"><i class="fa fa-calendar"></i></button>
                                       </div>
                                   </div>
								
                               </div> 
							</div>
							<c:if test="${hasRight==true }">
							<div class="form-group">
								<div class="col-lg-2 col-sm-2"></div>
								<div class="col-lg-10">
									<button type="button" onclick="jssubmit()" class="btn btn-success">保存</button>
								</div>
							</div>
							</c:if>
						<form:hidden path="id" id="id"/>
						<form:hidden path="emonitor.id" id="emonitor.id" />
						</form:form>
                  </div>
                  </section>				
</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading"> <span id="panel-heading"></span>列表 
		<c:if test="${hasRight==true }">
			<button type="button"  class="btn btn-theme pull-right fa fa-plus-square" onclick="changeEdittable(newObject)">新增</button>
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
             <table class="display table table-bordered table-striped" id="slist">
                 <thead>
                 <tr>
                     <th><input class="allCheckbox" type="checkbox"/></th>
                     <th>环境数据</th>
                     <th>观测时间</th>
                     <th>操作人</th>
                     <th>更新时间</th>
                     <th>阈值</th>
                     <th>是否超限</th>
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
 <jsp:include page="/common/getPointFromMap.jsp" />
 <jsp:include page="/pages/water/import/importexcel.jsp" />
 <jsp:include page="/common/exportModal.jsp" />
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
var permissionTree = new Array();
var groupid=-1;
function goback(){
	javascript :history.back(-1);
}
	jQuery(document).ready(function() {
		$('#slist').dataTable( {
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": false,
			"bSortClasses": false,"oLanguage": {
				"oPaginate":opaging
			},
			"sDom" : "lf<c:if test="${hasRight==true }"><'#import_btn'><'#export_btn'><'#del_btn'></c:if>rtip",
			"sAjaxSource": "server_processing.shtml?pid="+pid,
			"fnServerData": retrieveGoods,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "data"},
		                    { "mData": "dateCreated"},
		                    { "mData": "modifiedBy"},
		                    { "mData": "dateModified"},
		                    { "mData": "threshold"},
		                    { "mData": "status"},
		                    { "mData": "id" }
		                ],
                "aoColumnDefs": [
                             {"bVisible": true, "aTargets": [0],  "mRender":function(data,type,full){
                              	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                              },"bSortable": false, 
                             },
                             {"aTargets":[6],"mRender":function(data,type,full){
                            	 var reStr='否';
                            	 if(data>0){
                            		 reStr='是';
                            	 }
                            	 return reStr;
                             	}
                            },
                             {"aTargets":[7],"mRender":function(data,type,full){
                            	 var reStr='';
                            	 <c:if test="${hasRight==true }">
                            	 	reStr+='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata('+data+')"><i class="fa fa-trash-o "></i></a>'; 
             					</c:if>
                                	return reStr+"";
                             	},
                             	"sWidth":"120px","bSortable": false
                            },
                            {
                            	"aTargets":["_all"],"bSortable": false
                            }
                         ],
         "fnInitComplete": function(settings, json) {
        	 if(isTouchDevice()===false) {
        		 $('.tooltips').tooltip();
        		}
        	 definedSidCheckbox();
        	 $('#loading').hide();
         }
			
		} 
		);
		//加载日期控件
		$('.datetimeControl').datetimepicker({
			format: "yyyy-mm-dd hh:ii:ss",
		    autoclose: true,
		    todayBtn: true,
		    minuteStep:1,
		    pickerPosition: "bottom-left"
		});
		//增加导入按钮
		$("#export_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-download" style="color:red"></i>导出</button>');
		$("#export_btn").addClass("dt_btn pull-right");
		$("#export_btn").on('click',function(){
			exportdata(pid);
		});
		//增加导入按钮
		$("#import_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-upload" style="color:red"></i>导入</button>');
		$("#import_btn").addClass("dt_btn pull-right");
		$("#import_btn").on('click',function(){
			importdata(pid);
		});
		//需要显示编辑界面
		if(phonetype!=''){
			changeEdittable(newObject);
		}
	});

	function jssubmit(){
		 $("#storeform").submit();
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
			url : "server_processing.shtml?pid="+pid,
			dataType : "json",
			data : data,
			success : function(data) {
				fnCallback(data); // 服务器端返回的对象的returnObject部分是要求的格式
				$('#loading').hide();
			},
			error : function(data, xhr, textStatus, errorThrown) {
				console.log(data.responseText);
				fnCallback(data);
				$('#loading').hide();
			}
		});
	}
	function newObject(){
		$("input[id='emonitor.id']").val(pid);
	}
	// 回调函数，设置状态默认值
	function dolistafterfn(oTable, nRow){
		$("input[id='emonitor.id']").val(pid);
	}
	function importdata(iid){
		$('#iid').val(iid);
		$('#ititle').html('环境监测数据');
		$('.pwellimport').hide();
		$('.owellimport').hide();
		$('.iwellimport').hide();
		$('.emonitorimport').show();
		
		importexcel();
		
	}
	function exportdata(iid){
		$('#etitle').html('环境监测数据');
		$('#eid').val(iid);
		//$('#ename').val(pname);
		$('#export-modal').modal('show');
	}
</script>
<script src='<c:url value="/resources/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" />'></script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
