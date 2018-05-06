<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>	
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />' rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">			
<script type="text/javascript">
		$('#loading').show();
		var did='${did}';
</script>
		<div class="row" id="edittable" style="display:none;">
               <div class="col-md-12">
                  <section class="panel">
                      <header class="panel-heading">
                          <span id="edittile"></span>
                          <span class="tools pull-right">
                            <a href="javascript:;" class="fa fa-chevron-down"></a>
                            <a href="javascript:;" class="fa fa-times"></a>
                          </span>
                      </header>
                      <div class="panel-body">
                   		<form:form cssClass="form-horizontal" role="form" commandName="department" id="storeform">
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label control-required">部门名称</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control required" path="name" id="name"  remote="checkDepartmentCode.shtml?id"/>
                                       <span class="help-block"> <form:errors path="name" cssClass="error" /></span>
                                   </div>
                               </div>
                              <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label control-required">部门负责人</label>
                                   <div class="col-lg-10">
                                  	 <sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
	                                  	 <div class="input-group">
	                                  	 	<form:input cssClass="form-control required" path="departmentOwner" id="departmentOwner" readonly="true" onclick="getList('请选择部门负责人','departmentOwner','users.shtml','modal','setVisablePhone')" />
	                                  	 		<span class="input-group-btn " >
		                                         <button class="btn btn-theme tooltips" type="button"  data-placement="left" data-container="body" data-original-title="部门负责人在用户列表中选择，如果选择某个用户则该用户对此部门具有了编辑权限！！！" onclick="getList('请选择部门负责人','departmentOwner','users.shtml','modal','setVisablePhone')"><i class="fa fa-user"></i></button>
		                                       </span>
	                                  	 </div>
                                  	 </sec:authorize>
                                  	 <sec:authorize access="!hasRole('ADMIN')">
                                  	 	 <form:input cssClass="form-control required" path="departmentOwner" id="departmentOwner"  />
                                  	 </sec:authorize>
                                  	 <span class="help-block"> <form:errors path="departmentOwner" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group" id="phonediv">
                                   <label class="col-lg-2 col-sm-2 control-label control-required">联系电话</label>
                                   <div class="col-lg-10">
                                  	 <form:input cssClass="form-control required" path="phone" id="phone" />
                                  	 <span class="help-block"> <form:errors path="phone" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group" id="guardadmin">
                                    <label class="col-lg-2 col-sm-2 control-label control-required">监护部门管理员</label>
                                   <div class="col-lg-10">
                                  	  	 <div class="input-group">
	                                  	 	<input class="form-control required"  id="user" readonly="readonly" onclick="getList('请选择监护项目管理员','user','users.shtml','modal','')" />
	                                  	 		<span class="input-group-btn " >
		                                         <button class="btn btn-theme tooltips" type="button"  data-placement="left" data-container="body" data-original-title="请选择监护项目管理员" onclick="getList('请选择监护项目管理员','user','users.shtml','modal','')"><i class="fa fa-user"></i></button>
		                                       </span>
	                                  	 </div>
                                  	 <span class="help-block"> <form:errors path="user.id" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label">备注</label>
                                   <div class="col-lg-10">
                                  	 <form:input cssClass="form-control" path="memo" id="memo" />
                                  	 <span class="help-block"> <form:errors path="memo" cssClass="error" /></span>
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
							<form:hidden path="user.id" id="userid" />
							<form:hidden path="code" />
							<form:hidden path="departmentOwnerid" name="departmentOwnerid" id="departmentOwnerid" />
							<input type="hidden" name="zoneid" id="zoneid">
                           </form:form>
                  </div>
                  </section>
              </div>
          </div>
             
  <div class="row">
  <div class="col-md-12">
	<section class="panel">
	<header class="panel-heading">部门清单
	<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
		<button type="button"  class="btn btn-theme pull-right fa fa-plus-square hidden-xs" onclick="changeEdittable(afternew)">新增</button>
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
                     <th>部门名称</th>
                     <th>部门负责人</th>
                     <th>联系电话</th>
                     <th>备注</th>
                     <th></th>
                     <th></th>
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
		$('#pictitle').html('部门管理');
		$('#slist').dataTable( {
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": true,
			"bSortClasses": false,
			"sAjaxSource": "server_processing.shtml",
			"fnServerData": retrieveData,
			"sDom": "lf<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated"><'#del_btn'></sec:authorize>rtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "name"},
		                    { "mData": "departmentOwner"},
		                    { "mData": "phone"},
		                    { "mData": "memo" },
		                    { "mData": "departmentOwnerid" },
		                    { "mData": "sstatus" },
		                    { "mData": "id"}

		                ],
                "aoColumnDefs": [
							{  "bVisible": false, "aTargets": [5,6] },
                             {"aTargets": [0],  "mRender":function(data,type,full){
                             	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                             },"bSortable": false
                             },
                             {"aTargets":[7],"mRender":function(data,type,full){
                                if(full.sstatus==1)	{
                                	 var s = '<a class="edit hasfn btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>';

	                               	if(full.code!='water' && full.code!='monitor') {
	                                	 s += '&nbsp;<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除" onclick="deldata('+data+',&quot; 本次操作将会删除部门下所有的项目，您确认要删除吗？&quot; )"><i class="fa fa-trash-o "></i></a>';
                             	      }
                                	 s += '&nbsp;<a href="'+_context+'/water/project/list.html?departmentID='+full.id+'" class="btn btn-success btn-xs tooltips" data-placement="left" data-container="body" data-original-title="工地列表"><i  class="fa fa-list-ol"></i></a>';
                                	 return s; 
                                } else {
                                	return '';
                                }
                             	},
                             	"sWidth":"120px","bSortable": false
                            },
                            {
                            	"aTargets":["_all"],"bSortable": false
                            },
                            {"aTargets":[1],"mRender":function(data,type,full){
                            	return '<a class="showNext" href="javascript:;" >'+data+'</a>'; 
                         		}
                        	}
                         ]  ,
                         "fnInitComplete": function(settings, json) {
                        	 if(isTouchDevice()===false) {
                        		 $('.tooltips').tooltip();
                        		}
                        	 definedSidCheckbox();
                        	 this.fnSetColumnVis(7,settings.hasRight);
                        	
                        	 $('#loading').hide();
                         }
			
		} 
		);
	});
	function jssubmit(){
		$("#storeform").submit(); 		
	}
	function setVisablePhone(){
		$("#phonediv").hide();
	}
	function showPhone(){
		$("#phonediv").show();
		$('#departmentOwner').val('');
		$('#departmentOwner').removeAttr("readonly");
		
	}
	/**
	function taggleSame(){
		if($('#sameascsite').prop("checked")){
			$('#samediv').show();
		}else{
			$('#samediv').hide();
		}
	}*/
	function afternew(){
		//$('#samediv').hide();
		$('#departmentOwner').removeAttr("readonly");
		$("#commentid").hide();
		//$('#csitesamediv').show();
	}
	
	function dolistafterfn(oTable, nRow){
		var aData = oTable.fnGetData(nRow);
		var id=aData["id"];
		var code=aData["code"];
		if(code=='monitor'){
			var user=aData['user'];
			$("#user").val(user.firstName);
			$("#guardadmin").show();
		}else{
			$("#guardadmin").hide();
		}
		if(id==did){
			$("#commentid").show();
		}else{
			$("#commentid").hide();
		}
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>