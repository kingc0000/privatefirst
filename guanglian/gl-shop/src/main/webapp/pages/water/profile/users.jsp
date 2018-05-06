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
		$('#loading').show();
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
                      	<form:form cssClass="form-horizontal" role="form" commandName="user" id="storeform">
							<form:errors path="*"
								cssClass="alert alert-block alert-danger fade in" element="div" />
							<div id="store.success" class="alert alert-success"
								style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>">提交成功</div>
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label control-required">登录名</label>
								<div class="col-lg-10">
									<form:input cssClass="form-control required" path="adminName"  remote="checkUserCode.shtml?id"/>
									<span class="help-block"  id="checkCodeStatus" style="display:none;"> <form:errors path="adminName"
											cssClass="error" /></span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label control-required">电子邮件</label>
								<div class="col-lg-10">
									<form:input cssClass="form-control email required" path="adminEmail" />
									<span class="help-block"><form:errors path="adminEmail"
											cssClass="error" /></span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label control-required">姓名</label>
								<div class="col-lg-10">
									<form:input cssClass="form-control required" path="firstName" />
									<span class="help-block"><form:errors path="firstName"
											cssClass="error" /></span>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label control-required">电话</label>
								<div class="col-lg-10">
									<form:input cssClass="form-control required" path="telephone" />
									<span class="help-block"><form:errors path="telephone"
											cssClass="error" /></span>
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label ">入职时间</label>
								<div class="col-lg-10">
									<div  class="input-group date date-picker">
                                       <input type="text" class="form-control"  id="enterEnter" name="enterEnter">
                                       <div class="input-group-btn">
                                           <button type="button" class="btn btn-danger date-reset"><i class="fa fa-times"></i></button>
                                           <button type="button" class="btn btn-warning date-set"><i class="fa fa-calendar"></i></button>
                                       </div>
                                   </div>
								</div>
							</div>
			
							<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
								<div class="form-group">
									<label class="col-lg-2 col-sm-2 control-label control-required">用户组</label>
									<div class="col-lg-10">
										<div>
										<c:forEach var="group" items="${groups}">
											
											<c:choose>
												<c:when test="${group.grouptype==1 }">
													<span class="checkboxs-inline" onclick="allocateRight(${group.id})">
													<input type="checkbox" id="groups" name="groups" title="${group.id}"> &nbsp;${group.name}
													<span style="color:red">(请关联项目)</span>
													</span>
												</c:when>
												<c:otherwise>
													<span class="checkboxs-inline" onclick="setCheckbox(${group.id})">
													<input type="checkbox" id="groups" name="groups" title="${group.id}" onclick="setCheckbox(${group.id})"> &nbsp;${group.name}
													</span>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										<span class="help-block"><form:errors path="groups" cssClass="error" /></span>
										</div>		
									</div>
								</div>
							</sec:authorize>
			
			
							<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
								<div class="form-group">
									<label class="col-lg-2 col-sm-2 control-label">激活状态</label>
									<div class="col-lg-10">
										<span class="checkboxs-inline" onclick="setActive('active')">
											<input type="checkbox" id="active" name="active" title="true" onclick="setActive('active')"> &nbsp;激活
										</span>
									</div>
								</div>
							</sec:authorize>
							<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
							<div class="form-group">
								<div class="col-lg-2 col-sm-2"></div>
								<div class="col-lg-10">
									<button type="button" onclick="jssubmit()" class="btn btn-success">保存</button>
								</div>
							</div></sec:authorize>
						<form:hidden path="id" id="id"/>
						<input type="hidden" name="pright" id="pright"> 
						</form:form>
                  </div>
                  </section>				
</div>
</div>
<div class="row">
	<div class="col-lg-12 col-sm-12">
	<section class="panel">
	<header class="panel-heading"> 用户列表 
		<sec:authorize access="hasRole('ADMIN') and fullyAuthenticated">
		<button type="button"  class="btn btn-theme pull-right fa fa-plus-square" onclick="changeEdittable(dolistafterfn)">新增</button></sec:authorize>
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
                     <th>登陆名</th>
                     <th>姓名</th>
                     <th>邮箱</th>
                     <th>激活</th>
                     <th>电话</th>
                     <th>创建时间</th>
                     <th>上次登陆时间</th>
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
<script src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script>
var permissionTree = new Array();
var groupid=-1;
	jQuery(document).ready(function() {
		//$('#edittable').hide();
		//设置title
		$('#pictitle').html('用户列表');
		$('#slist').dataTable( {
			"bPaginate": true, //显示分页
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": true,
			"bSortClasses": false,
			"sDom": "lf<'#del_btn'><sec:authorize access="hasRole('SUPERADMIN') and fullyAuthenticated"><'#password_btn'></sec:authorize>rtip",
			"oLanguage": {
				"oPaginate":opaging
				},
			"sAjaxSource": "server_processing.shtml",
			"fnServerData": retrieveData,
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "adminName"},
		                    { "mData": "firstName"},
		                    { "mData": "adminEmail"},
		                    { "mData": "active"},
		                    { "mData": "telephone"},
		                    { "mData": "dateCreated" },
		                    { "mData": "loginTime"},
		                    { "mData": "operate" }

		                ],
                "aoColumnDefs": [
                             {"bVisible": true, "aTargets": [0],  "mRender":function(data,type,full){
                              	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                              },"bSortable": false, 
                             },
                             {"aTargets":[8],"mRender":function(data,type,full){
                            	 var reStr='<a class="edit hasfn btn btn-primary btn-xs tooltips" data-placement="left" data-container="body" data-original-title="修改" href="javascript:;" ><i class="fa fa-pencil"></i></a>&nbsp;';
                            	 if(full.active=="true"){
                            		 reStr+='<a href="javascript:;" class="btn btn-theme btn-xs tooltips"  data-placement="left" data-container="body" data-original-title="冻结" onclick="doaction('+data+',&apos;active.shtml&apos;,&apos;冻结&apos;)" ><i class="fa fa-ban"></i></a>&nbsp;';
                            	 }else{
                            		 reStr+='<a href="javascript:;" class="btn btn-theme btn-xs tooltips"  data-placement="left" data-container="body" data-original-title="激活" onclick="doaction('+data+',&apos;active.shtml&apos;,&apos;激活&apos;)" ><i class="fa fa-flash"></i></a>&nbsp;';
                            	 }
                                
                                	reStr+=	'<a href="javascript:;" class="btn btn-warning btn-xs tooltips" data-placement="left" data-container="body" data-original-title="重设密码" onclick="doaction('+data+',&apos;repassword.shtml&apos;,&apos;重设密码&apos;)" ><i  class="fa  fa-refresh"></i></a>&nbsp;';
                                	<sec:authorize access="hasRole('SUPERADMIN') and fullyAuthenticated">
                                	reStr+=	'<a href="javascript:;" class="btn btn-warning btn-xs tooltips" data-placement="left" data-container="body" data-original-title="设置密码" onclick="setpassword('+data+')" ><i  class="fa  fa-key"></i></a>&nbsp;';
                                	</sec:authorize>
                                	reStr+= '<a href="javascript:;" class="btn btn-danger btn-xs tooltips" data-placement="left" data-container="body" data-original-title="删除"  onclick="deldata('+data+')"><i class="fa fa-trash-o "></i></a>'; 
                                	return reStr;
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
        	 $('#loading').hide();
         }
			
		} 
		);
		
		//增加修改按钮
		$("#password_btn").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-key" style="color:red"></i>设置密码</button>');
		$("#password_btn").addClass("dt_btn pull-right");
		$("#password_btn").on('click',function(){
			setpassword();
		});
		//加载日期控件
		$(".date-picker").datepicker({
		    language: "zh-CN",
		    format: "yyyy-mm-dd",
		    autoclose: true,
		    todayBtn: "linked",
		    //clearBtn: true,
		    todayHighlight: true
		});
	});

	function jssubmit(){
		
		var groups=$( "form input:checked") ;
		var isSubit=false;
		 $(groups).each(function(){
			 if(this.name=="groups"){
				 isSubit=true;
				 return false; 
			 }
		 });
		 if(isSubit){
			 $("#storeform").submit();
			 //$('#loading').show();
		 }else{
			 alert('请至少分配一个权限给用户！');
			 return;
		 }
	}
	/**
	* 获取对应权限组的权限树选中集合（全选和部分选节点信息）
	* permissionTree 权限组数组
	*/
	function getPermissionTreeBygroup(gid){
		var rvalue=-1;
		for (var i=0;i<permissionTree.length;i++){
			if(permissionTree[i].groupid==gid){
				rvalue=i
				return rvalue;
			}
		}
		return rvalue;
	}
	// 回调函数，初始化数
	function dolistafterfn(oTable, nRow){
		permissionTree = new Array();
	} 
	function allocateRight(gid){
		$('#loading').show();
		groupid=gid; //权限组id
		var index=-1;
		if(permissionTree.length>0){
			index=getPermissionTreeBygroup(gid)
		}
		if(index==-1){
			//获取用户对应项目组的权限树情况（全选，部分选，未选）
			getProjectSitesTree('getDepartmentNode.shtml',$(".rightTree"),$('#id').val(),gid,showmodel);
		}else{
			
			$(".rightTree").treeview('setArrayCheckedState',[permissionTree[index].item,{"silent":false}]);
			
			$('#rightList').modal('show');
			$('#loading').hide();
		}
		//提交选择结果
		$('#submitRight').one("click",function(event){
			//获取全选、部分选的结果集合
			var seletecd=$('.rightTree').treeview('getCheckedWithoutSon');
			var index=-1;
			if(permissionTree.length>0){
				index=getPermissionTreeBygroup(groupid)
			}
			if(index==-1){
				index=permissionTree.length;
			}
			
			var item = new Object();
			item.groupid=groupid;
			var selectedArray =  new Array();
			for(var i=0;i<seletecd.length;i++){
				var sitem = new Object();
				sitem.nodeId=seletecd[i].nodeId;
				sitem.checked=seletecd[i].state.checked; //选中
				sitem.seletecd=seletecd[i].state.selected; //全选
				sitem.checkedMiddle=seletecd[i].state.checkedMiddle; //部分选
				sitem.id=seletecd[i].id;
				sitem.type=seletecd[i].type; //节点类型：all,department,features,csite
				selectedArray[i]=sitem;
			}
			item.item=selectedArray;
			permissionTree[index]=item;
			if(seletecd.length>0 ){
				$.each($('input[name="groups"]'),function(index,data){
					if($(this).attr("title")==groupid){
						$(this).prop("checked",true);
						return false;
					}
				});				
			}else{ //没有选择，则清空权限组check
				//removeSelected(groupid,false);
			
				$.each($('input[name="groups"]'),function(index,data){
					if($(this).attr("title")==groupid){
						$(this).prop("checked",false);
						return false;
					}
				});
			}
			$('#rightList').modal('hide');
			var pright = JSON.stringify(permissionTree);
			$('#pright').val(pright);
		});
		
		$('#resetRight').on("click",function(event){
			$('#loading').show();
			$('#pright').val('');
			getProjectSitesTree('getDepartmentNode.shtml',$(".rightTree"),$('#id').val(),gid,resetremove);
			
		});
		
	}
	
	function getCheckedArrar(node){
		var selecte =false;
		for(var i=0;i<node.length;i++){
			if(node[i].state.checked==true && node[i].state.checkedMiddle==false){
				return true;
			}else if(node[i].nodes!=null && node[i].nodes.length>0){
				selecte= getCheckedArrar(node[i].nodes);
			}
		}
		
		return selecte;
	}
	
	function showmodel(){
		$('#rightList').modal('show');
	}
	
	function resetremove(gid){
		var seletecd=$('.rightTree').treeview('getCheckedWithoutSon');
		var checked=false;
		if(seletecd!=null && seletecd.length>0){
			checked=true;
		}
		removeSelected(gid,checked);
	}
	var loadinggif= '<img src="<c:url value="/resources/img/tree-style.css" />"/>';
	function removeSelected(gid ,check){
		//去掉之前的选择
		var index=-1;
		if(permissionTree.length>0){
			index=getPermissionTreeBygroup(gid)
		}else {
			index=0;
		}
		permissionTree.splice(index,1);
		$.each($('input[name="groups"]'),function(index,data){
			if($(this).attr("title")==gid){
				$(this).prop("checked",check);
				return false;
			}
			
		});
	}
	
	function setCheckbox(gid){
		$.each($('input[name="groups"]'),function(index,data){
			if($(this).attr("title")==gid){
				if($(this).prop("checked")){
					$(this).prop("checked",false);
				}else{
					$(this).prop("checked",true);
				}
				
				return false;
			}
			
		});
	}
	function setpassword(uid){
		if(uid!=null){
			$("#uid").val(uid);
		}else{
			var ids = new Array();
			$('.sidCheckbox:checked').each(function(i,o){
				var nRow = $(o).parents('tr')[0];
				var aData = oTable.fnGetData(nRow);
				ids[i]=$(o).val();
			});
			if(ids.length>0){
				$("#uid").val(ids);
			}else{
				alert('请至少选择一个用户！');
				return ;
			}
			
		}
		$('#passmodal').modal('show');
	}
	function setpass(){
		if($("#newpassword").val()==null || $("#newpassword").val()==""){
			alert('密码不能为空！');
			return;
		}
		$('#loading').show();
		$.ajax({
			type : "POST",
			url : "setpassword.shtml",
			data : "listId=" + $("#uid").val()+"&newpass="+$("#newpassword").val(),
			success : function(result) {
				$('#passmodal').modal('hide');
				$('#loading').hide();
				if(result!=null&&result.length>0) {
					$(result).each(function(i, o){
						var status = o.status;
						var index = "["+(i+1)+"]:&nbsp;&nbsp;";
						var msg = o.statusMessage;
						if(status==0||status==9999) {
							refreshData();
							if(msg!=null&&msg!=''){
								toastr.success(index+msg);							
							} else {
								toastr.success(index+'操作成功！');
							}
						} else {
							if(msg!=null&&msg!=''){
								toastr.error(index+msg);							
							} else {
								toastr.error(index+'提交失败！');
							}
						}
					});
				} else if(result!=null){
					var status = result.response.status;
					var msg = result.response.statusMessage;
					if(status==0||status==9999) {
						refreshData();
						if(msg!=null&&msg!=''){
							toastr.success(msg);							
						} else {
							toastr.success('操作成功！');
						}
					} else {
						if(msg!=null&&msg!=''){
							toastr.error(msg);							
						} else {
							toastr.error('提交失败！');
						}
					}
				}
			
			},
			
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				toastr.error('设置失败！');	
				
			}
		});
	}
</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>
<script src='<c:url value="/resources/js/project/tree.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-treeview/bootstrap-treeview.js" />'></script>

<div class="modal fade " id="rightList" tabindex="-1" role="dialog" aria-labelledby="modalList" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title">请关联项目</h4>
            </div>
            <div class="modal-body" >
            	
				<div class="inbox-body"></div>
				
				<div class="rightTree"></div>
				
            </div>
            <div class="modal-footer">
            	<button id="submitRight" class="btn btn-success">提交 </button>
            	<button id="resetRight" class="btn btn-theme">重置 </button>
                <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade " id="passmodal" tabindex="-1" role="dialog" aria-labelledby="modalList" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" >请设置密码</h4>
            </div>
            <div class="modal-body" >
            	<form class="form-horizontal" id="passform">
            		 <input class="form-control" name="newpassword" id="newpassword"  />
            		 <input type="hidden" name="uid" id="uid" />
            	</form>
            </div>
            <div class="modal-footer">
            	<button  class="btn btn-success" onclick="setpass()">提交 </button>
                <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            </div>
        </div>
    </div>
</div>
