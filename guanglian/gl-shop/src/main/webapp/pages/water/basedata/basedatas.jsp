<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
                      	<form:form cssClass="form-horizontal" role="form" commandName="basedata" id="storeform" >
							<div class="form-group">
								<label class="col-sm-2 control-label control-required">名称</label>
								<div class="col-sm-10 ">
									<form:input cssClass="form-control required" path="name"/>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-2 control-label control-required">值</label>
								<div class="col-sm-10 ">
									<form:input cssClass="form-control required" path="value" />
								</div>
							</div>
							<div class="form-group" id="type_div">
								<label class="col-sm-2 control-label control-required">类型</label>
								<div class="col-sm-10 ">
                                    <form:select cssClass="form-control required" path="type" id="type" style="display:inline;width:50%">
                                    	<c:forEach items="${btype }" var="content">
                                    		<form:option value="${content}">${content }</form:option>
                                    	</c:forEach>
    								</form:select>
                                </div>
							</div>
							<div class="form-group">
								<div class="col-sm-2"></div>
								<div class="col-sm-10 ">
									<button type="button" onclick="fomdatesubmit()" class="btn btn-success"><s:message code="label.save" text="Save"/></button>
								</div>
							</div>
						<form:hidden path="id" />
						<form:hidden path="sys" id="sys"/>
						</form:form>
                  </div>
                  </section>				
</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading">
		基础数据列表 
		<button type="button"  class="btn btn-theme pull-right fa fa-plus-square" onclick="changeEdittable2()"><s:message code="label.generic.new" text="New"/></button>
	</header>
	<div class="panel-body">
		<div class="col-lg-12 col-sm-12">
			<div class="adv-table table-responsive " tabindex="1" style="overflow:hidden;outlin:none">
             <table class="display table table-bordered table-striped" id="slist">
                 <thead>
                 <tr>
                 	<th><input class="allCheckbox" type="checkbox"/></th>
                     <th>名称</th>
                     <th>值</th>
                     <th>类型</th>
                     <th>操作</th>
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
	
	$(function() {
		//$('#edittable').hide();
		$('#pictitle').html('基础数据');
		$('#slist').dataTable( {
			"bPaginate": true, //不显示分页
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": true,
			"bSortClasses": false,
			"sDom": "lf<'#del_btn'>rtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource": "server_processing.shtml",
			"fnServerData": retrieveData,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "name"},
		                    { "mData": "value"},
		                    { "mData": "type"},
		                    { "mData": "id" }

		                ],
                "aoColumnDefs": [
                             {"bVisible": true, "aTargets": [0],  "mRender":function(data,type,full){
                               	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                               },"bSortable": false
                              },
                             {"aTargets":[4],"mRender":function(data,type,full){
                                var rStr=	'<a class="edit btn btn-primary btn-xs tooltips" data-placement="left" data-container="body"  data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;'
                            	  
                                	if(full.sys==false){
                                		rStr +='<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata('+data+')"><i class="fa fa-trash-o "></i></a>';
                                	}
                                return rStr;
                             	},
                             	"sWidth":"150px","bSortable": false
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
			
		} 
		);
	});
	
	
	function changeEdittable2(event) {
		changeEdittable();
		$("#sys").val(false);
	}
	
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
