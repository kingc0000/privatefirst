<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />' rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<link rel="stylesheet" type="text/css" href='<c:url value="/resources/assets/jquery-multi-select/css/multi-select.css" />' />	
	

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
            	<form:form cssClass="form-horizontal" role="form" commandName="auditConfig" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">规则</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="name" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">项目等级</label>
						<div class="col-lg-10">
							<form:select path="rank" items="${applicationScope.bd_proj_rank}" itemLabel="name" itemValue="value"></form:select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">送审节点</label>
						<div class="col-lg-10">
							<select name="auditType" id="auditType">
								<option value="1">审核</option>
								<option value="2">审定</option>
							</select>
						</div>
					</div>
	
					<div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="submit" class="btn btn-success">保存</button>
						</div>
					</div>
				<form:hidden path="id" />
				</form:form>
        	</div>
        </section>
    </div>
</div>
<div class="row">
	<div class="col-md-12">
		<section class="panel">
		<header class="panel-heading">送审规则配置
			<button type="button"  class="btn btn-info pull-right fa fa-plus-square" onclick="changeEdittable(toInsert)"><s:message code="label.generic.new" text="New "/></button>
		</header>
		<div class="panel-body">
			<div class="col-lg-12 col-sm-12">
				<section class="adv-table " id="no-more-tables">
	             <table class="display table table-bordered table-striped" id="slist">
	                 <thead>
	                 <tr>
	                 	<th>ID</th>
	                    <th>规则</th>
	                    <th>项目等级</th>
	                    <th>送审节点</th>
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
<div class="modal fade " id="groupmodel" tabindex="-1" role="dialog" aria-labelledby="modalList" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title">送审规则人员配置</h4>
            </div>
            <div class="modal-body row" id="modal-body">
            	<div class="col-md-12">
            	<div class="form-group last">
                         <label class="control-label col-md-3" id="groupMemo"></label>
                         <div class="col-md-9" id="resetselect">
                         <select name="country" class="multi-select" multiple="" id="my_multi_select3" >
                         </select>
                   </div>
				</div>
               </div>
            </div>
            <div class="modal-footer">
            	<button data-dismiss="modal" class="btn btn-success" type="button" id="groupsubmit" onclick="addtogroup()">保存</button>
                <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            </div>
        </div>
    </div>
</div>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/jquery-multi-select/js/jquery.multi-select.js" />'></script>
<script src='<c:url value="/resources/assets/jquery-multi-select/js/jquery.quicksearch.js" />'></script>
<script>
	var rule_id; //送审规则组id
	var multiselect;
	jQuery(document).ready(function() {
		//$('#edittable').hide();
		$('#slist').dataTable( {
			"bPaginate" : false, //不显示分页
			"bProcessing" : true,
			"bServerSide" : true,
			"bLengthChange" : true,
			"bFilter" : false,
			"bSortClasses" : false,
			"sDom" : "lrtp",
			"sAjaxSource": "server_processing.shtml",
			"fnServerData": retrieveData,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "name" },
		                    { "mData": "rank" },
		                    { "mData": "auditType" },
		                    { "mData": "id" }
		                ],
                "aoColumnDefs": [
                             {  "bVisible": false, "aTargets": [ 0] },
                             {"aTargets":[4],"mRender":function(data,type,full){
                            	 return '<a class="edit btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="编辑" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;'
                            				 +'<a href="javascript:;" class="btn btn-info btn-xs tooltips" data-placement="left" data-container="body" data-original-title="配置规则" onclick="addgroup('+data+')"><i  class="fa fa-group  "></i></a>&nbsp;'
                            	 			+'<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除" onclick="deldata('+data+')"><i  class="fa fa-trash-o "></i></a>';
                                	
                             	},
                             	"sWidth":"120px","bSortable": false
                            },
                            {"aTargets":[3],"mRender":function(data,type,full){
                            		if(data=="1") return "审核";
                            		else return "审定";
                            	}, "bSortable": false
                           },
                            {
                            	"aTargets":["_all"],"bSortable": false
                            }
                         ] ,
                 "fnInitComplete": function(settings, json) {
                	 if(isTouchDevice()===false) {
                		 $('.tooltips').tooltip();
                		}
                 }
			
		});
	});
	
	/**
	** ajax用户添加至送审规则组
	**/
	function addtogroup(){
		$('#loading').show();
		var ids= new Array();
		var obj = multiselect.data('multiselect');
		$("#ms-my_multi_select3 .ms-selection .ms-selected" ).each(function(i,p){
			ids[i]=$(p).data("ms-value"); //商品id
		});
		$.ajax({
			type : "POST",
			url : "setGroup.shtml",
			data : "rid="+rule_id+"&ids="+ids,
			dataType: 'json',
			success : function(response) {
				$('#loading').hide();
				var status = response.response.status;
				if(status ==0){
					toastr.success('操作成功');
				}else{
					toastr.error('操作失败');
				}
					
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				toastr.error('操作失败');
			}
		});
	}
	
	//展示送审规则配置modal控件
	function addgroup(id){
		rule_id = id;
		$('#loading').show();
		$.ajax({
			type : "POST",
			url : "groups.shtml",
			data : {"rid":id},
			dataType: 'json',
			success : function(response) {
				$('#loading').hide();
				if(response !=null){
					if(response.status == 0||response.status == 9999) {
						//ajax获取送审规则分组数据
						showGroupModal(response);
					} else {
						toastr.error(response.message);
					}
				}else{
					toastr.error('获取送审规则配置失败');
				}
				
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				toastr.error('送审规则人员配置失败');
			}
		});
	}
	
	//处理送审规则人员配置控件的分组数据
	function showGroupModal(response) {
		$('#groupMemo').html(response.memo);
		$('#resetselect').html(' <select name="country" class="multi-select" multiple="" id="my_multi_select3" ></select>');
		//$("#my_multi_select3").empty();
		$(response.froms).each(function(i,p){
			$('#my_multi_select3').append('<option value="'+p.id+'">'+p.name+'-'+p.code+'</option>');
		});
		multiselect = $('#my_multi_select3').multiSelect({
	        selectableHeader: "<input type='text' class='form-control search-input' autocomplete='off' placeholder='<s:message code='front.search' text='Search '/>'>",
	        selectionHeader: "<input type='text' class='form-control search-input' autocomplete='off' placeholder='<s:message code='front.search' text='Search '/>'>",
	        afterInit: function (ms) {
	            var that = this,
                $selectableSearch = that.$selectableUl.prev(),
                $selectionSearch = that.$selectionUl.prev(),
                selectableSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selectable:not(.ms-selected)',
                selectionSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selection.ms-selected';

	            that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
	                .on('keydown', function (e) {
	                    if (e.which === 40) {
	                        that.$selectableUl.focus();
	                        return false;
	                    }
	                });

	            that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
	                .on('keydown', function (e) {
	                    if (e.which == 40) {
	                        that.$selectionUl.focus();
	                        return false;
	                    }
	                });
	            
	        },
	        afterSelect: function () {
	            this.qs1.cache();
	            this.qs2.cache();
	        },
	        afterDeselect: function () {
	            this.qs1.cache();
	            this.qs2.cache();
	        }
	    });
		//setTimeout(function(){ $('#my_multi_select3').multiSelect('select', "1");}, 1000);
		$(response.tos).each(function(i,p){
			var pid=''+p.id;
			$('#my_multi_select3').multiSelect('select', pid);
		});
		
		$('#groupmodel').modal('show');
	}
	function toInsert() {
		$("select").prop("selectedIndex", 0);
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
 