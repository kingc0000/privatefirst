<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>		
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />' rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />' rel="stylesheet">	
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
                      	<form:form cssClass="form-horizontal" role="form" commandName="group" id="storeform">
							<form:errors path="*"
								cssClass="alert alert-block alert-danger fade in" element="div" />
							<div id="store.success" class="alert alert-success"
								style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">操作成功</div>
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label control-required">权限组名称</label>
								<div class="col-lg-10">
									<form:input cssClass="form-control required" path="groupName"  remote="checkValidCode.shtml?id"/>
									<span class="help-block" id="checkCodeStatus" style="display:none;"> <form:errors path="groupName"
											cssClass="error" /></span>
								</div>
							</div>
							
								<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label control-required">中文标签</label>
								<div class="col-lg-10">
									<form:input cssClass="form-control " path="name" id="name" />
									<span class="help-block"><form:errors path="name"
											cssClass="error" /></span>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label ">权限分配</label>
								<div class="col-lg-10">
									<div>
										<c:forEach var="group" items="${rights}">
											<span class="checkboxs-inline ">
											
												<input type="checkbox" id="permissions" name="permissions" title="${group.id}" > &nbsp;${group.name}
											</span>
										</c:forEach>
										<span class="help-block"><form:errors path="permissions" cssClass="error" /></span>
										</div>	
								</div>
							</div>
							<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
							<div class="form-group">
								<div class="col-lg-2 col-sm-2"></div>
								<div class="col-lg-10">
									<button type="button" onclick="jssubmit()" class="btn btn-success">提交</button>
								</div>
							</div>
							</sec:authorize>
						<form:hidden path="id" />
						<form:hidden path="grouptype" />
						<form:hidden path="sGroup" />
						</form:form>
                  </div>
                  </section>				
</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading">权限管理列表
		<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated"><button type="button"  class="btn btn-theme pull-right fa fa-plus-square" onclick="changeEdittable(intiGroupType)">新增</button></sec:authorize>
		<button id="nextbtn" type="button"
					class="btn btn-theme pull-right fa fa-arrow-down visible-xs"
					onclick="donext()">下一页</button>
					<button type="button" id="lastbtn"
					class="btn btn-theme pull-right fa fa-arrow-up visible-xs"
					onclick="dolast()">上一页</button>
	</header>
	<div class="panel-body">
		<div class="col-lg-12 col-sm-12">
			<div class="adv-table table-responsive " tabindex="1" style="overflow:hidden;outlin:none">
             <table class="display table table-bordered table-striped" id="slist">
                 <thead>
                 <tr>
                 	<th>ID</th>
                     <th>名称</th>
                     <th>中文标题</th>
                     <th></th>
                     <th></th>
                     <th></th>
                     <th><s:message code="label.operator" text="Operator "/></th>
                 </tr>
                 </thead>
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
	var displayCustomGroup = '<c:url value="/admin/products/group/edit.html"/>';
	jQuery(document).ready(function() {
		//$('#edittable').hide();
		$('#pictitle').html('权限管理');
		$('#slist').dataTable( {
			"bPaginate": false, //不显示分页
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": false,
			"sAjaxSource": "server_processing.shtml",
			"fnServerData": retrieveData,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "groupName"},
		                    { "mData": "name"},
		                    { "mData": "permissions"},
		                    { "mData": "type"},
		                    { "mData": "sGroup"},
		                    { "mData": "id" }
		                ],
                "aoColumnDefs": [
                             {  "bVisible": false, "aTargets": [0,3,4,5] },
                             {"aTargets":[6],"mRender":function(data,type,full){
                            	 	var s = '';
										if(full.sGroup =="false"){
											s += '<a class="edit btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="修改" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;'; 
	                            	 		s += '<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata(\''+data+'\')"><i class="fa fa-trash-o "></i></a>';
										} 
                                	return  s;
                             	},
                             	"sWidth":"100px","bSortable": false
                            },
                            {
                            	"aTargets":["_all"],"bSortable": false
                            }
                         ],
         "fnInitComplete": function(settings, json) {
        	 if(isTouchDevice()===false) {
        		 $('.tooltips').tooltip();
        		}
         }
			
		} 
		);
	});
	
	function jssubmit(){
		$("#storeform").submit(); 		
	}
	
	function intiGroupType(){
		$('#grouptype').val(0);
		$('#sGroup').val(false);
	}
	
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
