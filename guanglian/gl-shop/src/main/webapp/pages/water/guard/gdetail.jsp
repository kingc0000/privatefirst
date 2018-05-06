<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page pageEncoding="UTF-8"%>
<link
	href='<c:url value="/resources/assets/bootstrap-datepicker/css/bootstrap-datepicker.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet">

				<form:form cssClass="form-horizontal" role="form"
					commandName="guard" id="storeform">
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">项目名称</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="project.name"
								id="project.name" />
							<span class="help-block"> <form:errors path="project.name"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">项目等级</label>
						<div class="col-lg-10">
							<form:select path="mBase.rank" class="required" name="mBase.rank"
								items="${applicationScope.mo_proj_rank}" itemLabel="name"
								itemValue="value"></form:select>
						</div>
					</div>
					<div class="form-group">
		                <label class="col-lg-2 col-sm-2 control-label control-required">工程特性</label>
		                <div class="col-lg-10">
		                    <form:select path="project.features" name="project.features" items="${applicationScope.bd_project_type}" itemLabel="name" itemValue="value"></form:select> 
		                </div>
		            </div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">项目负责人</label>
						<div class="col-lg-10">
							<div class="input-group">
								<form:input cssClass="form-control required"
									path="project.projectOwner" id="projectOwner" readonly="true"/>
								<span class="input-group-btn phonehide">
									<button class="btn btn-theme tooltips" type="button"
										data-placement="top" data-container="body"
										data-original-title="项目负责人在用户列表中选择，如果选择某个用户则该用户对此项目具有了编辑权限！！！"
										onclick="getList('请选择项目负责人','projectOwner','<c:url value="/water/project/users.shtml"/>','modal','setVisablePhone')">
										<i class="fa fa-user"></i>
									</button>
								</span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">技术负责人</label>
						<div class="col-lg-10">
							<div class="input-group">
								<form:input cssClass="form-control required"
									path="projectTechName" id="projectTechName" readonly="true"/>
								<span class="input-group-btn phonehide">
									<button class="btn btn-theme tooltips" type="button"
										data-placement="top" data-container="body"
										data-original-title="技术负责人"
										onclick="getList('请选择技术负责人','projectTechName','<c:url value="/water/project/users.shtml"/>','modal')">
										<i class="fa fa-user"></i>
									</button>
								</span>
							</div>
						</div>
					</div>
					<div class="form-group" id="phonediv">
						<label class="col-lg-2 col-sm-2 control-label control-required">联系电话</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="project.phone"
								id="phone" />
							<span class="help-block"> <form:errors
									path="project.phone" cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">省份/直辖市</label>
						<div class="col-lg-10">
							<form:input class="form-control required"
								path="project.zone.name" readonly="true"
								onclick="getList('请选择省份/直辖市','project.zone.name','zones.shtml')" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">城市</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required" path="project.city"
								id="city" />
							<span class="help-block"> <form:errors path="project.city"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">详细地址</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required"
									path="project.address" id="project.address" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label control-required">地铁车站</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control required"
									path="station" id="station" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">项目图纸</label>
						<div class="col-lg-9 col-sm-9">
							<div class="row" id="wimgs">
								<c:choose>
									<c:when test="${not empty guard.images }">
										<c:forEach items="${guard.images}" var="img">
											<div class="imgItem">
												<div class="fileupload fileupload-new col-lg-11 col-sm-11"
													data-provides="fileupload">
													<span class="preview" style="margin-left: 10px;">
															<c:if test="${img.fileType ==true }">
																<a class="tooltips" data-placement="left"
																	data-container="body" data-original-title="预览"
																	href="<c:url value="/files/preview/${img.fileName } "/>"><i
																	class="fa fa-eye">${img.fileName }</i></a>&nbsp;
                            		</c:if>
													</span> <span class="download default hidden-xs" id="${img.id }"
														style="margin-left: 10px;"> <a class="tooltips"
														data-placement="left" data-container="body"
														data-original-title="下载"
														href="<c:url value="/files/downloads/${img.fileName }"/>"><i
															class="fa fa-download">${img.fileName }</i></a>&nbsp;
													</span>
												</div>
												<div class="col-lg-1 col-sm-1">
													<button type="button"
														class="addBtn btn btn-info fa fa-minus-square"
														onclick="removeImage()"></button>
												</div>
											</div>
										</c:forEach>
									</c:when>
								</c:choose>
							</div>
						</div>
						<div class="col-lg-1 col-sm-1">
							<button type="button"
								class="addBtn btn btn-info fa fa-plus-square"
								onclick="addImage()"></button>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">结构</label>
						<div class="col-lg-10">
							<form:select path="structure" class="required" name="structure"
								items="${applicationScope.bd_guard_structure}" itemLabel="name"
								itemValue="value"></form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">状态</label>
						<div class="col-lg-10">
							<form:select path="status" class="required" name="status"
								items="${applicationScope.bd_guard_status}" itemLabel="name"
								itemValue="value"></form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label ">概述</label>
						<div class="col-sm-10">
							<form:textarea cssClass="ckeditor form-control" rows="6"
								id="summary" path="project.summary" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label ">备注</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control " path="project.memo" />
							<span class="help-block"> <form:errors path="project.memo"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">建设单位</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control " path="mBase.unit" />
							<span class="help-block"> <form:errors path="mBase.unit"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">施工承包单位</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control " path="mBase.contor" />
							<span class="help-block"> <form:errors path="mBase.contor"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">设计单位</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control " path="mBase.design" />
							<span class="help-block"> <form:errors path="mBase.design"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">监理单位</label>
						<div class="col-lg-10">
							<form:input cssClass="form-control " path="mBase.superv" />
							<span class="help-block"> <form:errors path="mBase.superv"
									cssClass="error" /></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">开工日期</label>
						<div class="col-lg-10 col-sm-10 ">
							<div class="date edate input-group">
								<form:input cssClass="form-control"
								path="mBase.pCreated" id="mBase.pCreated" />
							<span class="input-group-addon btn-theme"><i
								class="fa fa-calendar"></i></span> <span class="help-block"> <form:errors
									path="mBase.pCreated" cssClass="error" /></span>
							</div>
							
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 col-sm-2 control-label">计划竣工日期</label>
						<div class="col-lg-10 col-sm-10 ">
							<div class="date edate input-group">
								<form:input cssClass="form-control"
									path="mBase.pEnd" id="mBase.pEnd" />
								<span class="input-group-addon btn-theme"><i
									class="fa fa-calendar"></i></span> <span class="help-block"> <form:errors
										path="mBase.pEnd" cssClass="error" /></span>
							</div>
							
						</div>
					</div>
					<c:if test="${hasRight==true}">
					<div class="form-group" id="jssub">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="fubmit()"
								class="btn btn-success">提交</button>
						</div>
					</div>
					</c:if>
					<form:hidden path="project.projectOwnerid" name="project.projectOwnerid" id="projectOwnerid" />
					<form:hidden path="id" />
					<form:hidden path="project.zone.id" id="project.zone.id" name="project.zone.id"/>
					 <form:hidden path="project.department.id" id="project.department.id" name="project.department.id"/>
					 <input type="hidden" name="delids" id="delids"/>
					  <form:hidden path="project.id" id="project.id" />
					  <form:hidden path="projectTechNameid" id="projectTechNameid" />
				</form:form>
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
<script src='<c:url value="/resources/assets/ckeditor/ckeditor.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-datepicker/js/bootstrap-datepicker.js" />'></script>
<script>
$('#pictitle').html("${gentity.name }项目信息");
	$(function() {
		//加载日期控件
		$('.edate').datepicker({
			format : "yyyy-mm-dd",
			todayBtn : "linked",
			clearBtn : true,
			autoclose : true,
			todayHighlight : true
		});
	})
	function setVisablePhone(){
		$("#phonediv").hide();
	}
	function fubmit(durl,check){
		var url="save.shtml" ;
		if(durl !=null && durl !=""){
			url =durl;
		}
		/* $("#storeform").submit();  */
		if(typeof validator != "undefined"){
			if(!validator.form()){
				$("#storeform").validate();
			}
		}
		if(typeof(check)=="undefined"){
			/**有checkbox的*/
			var obj1 = $("#storeform input:checked");
			$(obj1).each(function(i,o) {
				$(o).val($(o).attr("title"));
					});
		}
		/**有select的*/
		/**有富文本框的*/
		if($("#storeform .ckeditor")!='undefined'&&$("#storeform .ckeditor").length>0) { //ck
			 
				$.each($("#storeform .ckeditor"),function(i,o){
					for ( instance in CKEDITOR.instances )  
					{  
						if(instance==$(o).attr("id")){
							$(o).val(CKEDITOR.instances[instance].getData());
						}
					} 
				});
		}
		var data = new FormData($('#storeform')[0]);
		
		var imgData = $(".avatar-img").attr("src");
		$(".avatar-img").each(function(i, item){
			var imgData = $(item).attr("src");
			var imgId = $(item).attr("imgId");
			if(imgData!=undefined&&imgData!=""&&imgData.split(',').length>1&&imgId=="") {
				// dataURL 的格式为 “data:image/png;base64,****”,逗号之前都是一些说明性的文字，我们只需要逗号之后的就行了
				var imagetype = "image/jpeg";
				if (imgData.indexOf("image/png")>0) {
					imagetype = "image/png";
				} else if (imgData.indexOf("image/gif")>0) {
					imagetype = "image/gif";
				}  
				imgData = imgData.split(',')[1];
				imgData = window.atob(imgData);
				
				var ia = new Uint8Array(imgData.length);
				for (var i = 0; i < imgData.length; i++) {
				    ia[i] = imgData.charCodeAt(i);
				};
				var blob = new Blob([ia], {type:imagetype});
				data.append("images", blob);
			}
		});
		$('#loading').show();
		$.ajax({
			type : "POST",
			cache: false, //cache设置为false，上传文件不需要缓存。
			url : url,
			data : data,
			async : true,
			processData: false, //processData设置为false。因为data值是FormData对象，不需要对数据做处理。
			contentType: false, //由<form>表单构造的FormData对象，且已经声明了属性enctype=multipart/form-data，所以这里设置为false
			success: function(response, stat){  
				$('#loading').hide();
				var status = response.response.status;
				if (status == 0 || status == 9999) {
					toastr.success('操作成功！');
				} else {
					toastr.error('提交失败！');
				}
				$('#edittable').hide();
				if($("#slist").length>0){
					refreshData();
				}else{
					location.reload();
				}
				
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				alert('error ' + errorThrown);
			}
		});
		return false;
	}
	function setCheckbox(gid){
		$.each($('input[name="status"]'),function(index,data){
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
</script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>
