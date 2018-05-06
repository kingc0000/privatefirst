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
<script type="text/javascript">
		$("#pictitle").html("设备网关");
		var cid ="${csite.id}";
		function goback(){
			javascript :history.back(-1);
		}
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
                      	<form:form cssClass="form-horizontal" role="form" commandName="gateway" id="storeform">
							<div class="form-group">
								<label class="col-sm-2 control-label control-required">名称</label>
								<div class="col-sm-10">
                                    <form:input cssClass="form-control required" path="name" />
                               </div> 
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label control-required">设备序列号</label>
								<div class="col-sm-10">
                                    <form:input cssClass="form-control required" path="serialno" remote="checkValidCode.shtml?id"/>
                               </div> 
							</div>
							<c:if test="${hasRight==true }">
							<div class="form-group">
								<div class="col-sm-2"></div>
								<div class="col-sm-10">
									<button type="submit" class="btn btn-success">提交</button>
								</div>
							</div>
							</c:if>
						<form:hidden path="id" id="id"/>
						<form:hidden path="cSite.id" id="cid"/>
						</form:form>
                  </div>
                  </section>				
</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading"> <span id="panel-heading">网关</span>列表 
		<c:if test="${hasRight==true }">
			<button type="button"  class="btn btn-theme pull-right fa fa-plus-square" onclick="changeEdittable(setpid)">新增</button>
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
                     <th>名称</th>
                     <th>设备序列号</th>
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
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
	jQuery(document).ready(function() {
		//$('#edittable').hide();
		$('#slist').dataTable( {
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": false,
			"bSortClasses": false,
			"sDom" : "lf<c:if test="${hasRight==true }"><'#del_btn'></c:if>rtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource": "server_processing.shtml?cid="+cid,
			"fnServerData": retrieveData,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "name"},
		                    { "mData": "serialno"},
		                    { "mData": "id" }
		                ],
                "aoColumnDefs": [
                             {"bVisible": true, "aTargets": [0],  "mRender":function(data,type,full){
                              	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                              },"bSortable": false, 
                             },
                             {"aTargets":[3],"mRender":function(data,type,full){
                            	 var reStr='';
                            	 reStr+='<a class="edit hasfn btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="修改" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;';
                            	 <c:if test="${hasRight==true }">
                                	reStr+='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata('+data+')"><i class="fa fa-trash-o "></i></a>'; 
             					</c:if>
                                	return reStr+"";
                             	},
                             	"sWidth":"160px","bSortable": false
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
         }
		});
	});
	/**
	 * 新增方法回调函数
	 */
	function setpid(){
		if(cid!=null&&cid!=''){
			$("input[id='cid']").val(cid);
		}else{
			alert("参数有问题，请刷新页面");
		}
	}
	// 回调函数，设置状态默认值
	function dolistafterfn(oTable, nRow){
		setpid();
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>