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
<script type="text/javascript">
		$('#loading').show();
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
                   		<form:form cssClass="form-horizontal" role="form" commandName="monitorUser" id="storeform">
                              <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label control-required">项目人员</label>
                                   <div class="col-lg-10">
	                                  	 <div class="input-group">
	                                  	 	<form:input cssClass="form-control required" path="user.id" id="user.id" name="user.id" readonly="true"/>
	                                  	 		<span class="input-group-btn " >
		                                         <button class="btn btn-theme tooltips" type="button"  data-placement="left" data-container="body" data-original-title="请选择项目人员" onclick="getList('请选择项目人员','users.shtml','setVisablePhone')"><i class="fa fa-user"></i></button>
											</span>
	                                  	 </div>
                                   </div>
                               </div>
                               <div id="phonediv">
                               <div class="form-group" >
                                   <label class="col-lg-2 col-sm-2 control-label control-required">姓名</label>
                                   <div class="col-lg-10">
                                  	 <input class="form-control " id="name" name="user.firstName" readonly="readonly"/>
                                   </div>
                               </div>
                               <div class="form-group" >
                                   <label class="col-lg-2 col-sm-2 control-label control-required">联系电话</label>
                                   <div class="col-lg-10">
                                  	 <input class="form-control " id="phone" name="user.telephone" readonly="readonly"/>
                                   </div>
                               </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label">备注</label>
                                   <div class="col-lg-10">
                                  	 <form:input cssClass="form-control" path="memo" id="memo" />
                                  	 <span class="help-block"> <form:errors path="memo" cssClass="error" /></span>
                                   </div>
                               </div>
                               <c:if test="${hasRight==true}">
                               <div class="form-group">
								<div class="col-lg-2 col-sm-2"></div>
								<div class="col-lg-10">
									<button type="button" onclick="jssubmit()" class="btn btn-success">提交</button>
								</div>
							</div>
							</c:if>
							<form:hidden path="id" />
                           </form:form>
                  </div>
                  </section>
              </div>
          </div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading"> 用户列表 
		<c:if test="${hasRight==true}">
		<button type="button"  class="btn btn-theme pull-right fa fa-plus-square" onclick="changeEdittable(afternew)">新增</button>
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
                     <th>姓名</th>
                     <th>电话</th>
                     <th>入职时间</th>
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
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script>
//设置title
$('#pictitle').html('项目人员');
	jQuery(document).ready(function() {
		
		$('#slist').dataTable( {
			"bPaginate": true, //显示分页
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": true,
			"bSortClasses": false,
			"sDom": "lf<c:if test="${hasRight==true}"><'#del_btn'></c:if>rtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource": "server_processing.shtml",
			"fnServerData": retrieveData,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "user.firstName"},
		                    { "mData": "user.telephone" },
		                    { "mData": "user.enterEnter"},
		                    { "mData": "memo"},
		                    { "mData": "id" }

		                ],
                "aoColumnDefs": [
                             {"bVisible": true, "aTargets": [0],  "mRender":function(data,type,full){
                              	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                              },"bSortable": false, 
                             },
                             {"aTargets":[5],"mRender":function(data,type,full){
                            	 var reStr='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata('+data+')"><i class="fa fa-trash-o "></i></a>'; 
                                	return reStr;
                             	},
                             	"bSortable": false
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
	});
	
	function setVisablePhone(){
		$("#phonediv").show();
	}
	
	function afternew(){
		
		$("#phonediv").hide();
		//$('#csitesamediv').show();
	}

	function jssubmit(){
		$("#storeform").submit();
			
	}
	
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/userModalselect.js" />'></script>
