<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
	<style>
.wizard > .steps > ul > li
{
    width: 50% !important;
}
</style>
	<form:form cssClass="form-horizontal" role="form" commandName="csite" id="storeform">
      <div id="wizard">
     	<h3>基本信息</h3>
     	<section>
     		<div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label control-required">项目名称</label>
                <div class="col-lg-10">
                    <form:input cssClass="form-control required" path="project.name" id="name" />
                    <span class="help-block"> <form:errors path="project.name" cssClass="error" /></span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label control-required">项目等级</label>
                <div class="col-lg-10">
                		<form:select path="pbase.rank" class="required" items="${applicationScope.bd_proj_rank}" itemLabel="name" itemValue="value"></form:select> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label control-required">项目负责人</label>
                <div class="col-lg-10">
                 <div class="input-group">
              	 	<form:input cssClass="form-control required" path="project.projectOwner" id="projectOwner" readonly="true" />
             	 	<span class="input-group-btn phonehide" >
                     <button class="btn btn-theme tooltips" type="button"  data-placement="top" data-container="body" data-original-title="项目负责人在用户列表中选择，如果选择某个用户则该用户对此项目具有了编辑权限！！！" onclick="getList('请选择项目负责人','projectOwner','<c:url value="/water/project/users.shtml"/>','modal')"><i class="fa fa-user"></i></button>
                   </span>
             	  </div>
            	 </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label control-required">工程特性</label>
                <div class="col-lg-10">
                    <form:select path="project.features" items="${applicationScope.bd_project_type}" itemLabel="name" itemValue="value"></form:select> 
                </div>
            </div>
           <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label control-required">省份/直辖市</label>
                <div class="col-lg-10">
                	<form:input class="form-control required" path="project.zone.name" readonly="true" onclick="getList('请选择省份/直辖市','project.zone.name','zones.shtml')"/>
                </div>
            </div>
             <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label control-required">城市</label>
                <div class="col-lg-10">
                    <form:input cssClass="form-control required" path="project.city" id="city" />
                    <span class="help-block"> <form:errors path="project.city" cssClass="error" /></span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label control-required">详细地址</label>
                <div class="col-lg-10">
                 	<div class="input-group">
                    		<form:input cssClass="form-control required" path="project.address" id="address"  />
             	 	<span class="input-group-btn " >
                     <button class="btn btn-theme tooltips" type="button" data-placement="top" data-original-title="从地图上获取经纬度" data-container="body" onclick="getcFromPoint()" ><i class="fa fa-map-marker"></i></button>
                    <button class="btn btn-theme tooltips" type="button" data-placement="top" data-original-title="重新设置经纬度" data-container="body" onclick="setcpoint()" style="border-left:1px solid #e2e2e4;"><i class="fa fa-exchange"></i></button>
                   </span>
               </div>    
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label control-required">经度</label>
                <div class="col-lg-10">
                    <form:input cssClass="form-control required" type="number" readonly="true"   path="longitude" id="longitude" />
                    <span class="help-block"> <form:errors path="longitude" cssClass="error" /></span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label control-required">纬度</label>
                <div class="col-lg-10">
                    <form:input cssClass="form-control  required" readonly="true"  type="number" path="latitude" id="latitude" />
                    <span class="help-block"> <form:errors path="latitude" cssClass="error" /></span>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label">数据采集频率（分钟）</label>
                <div class="col-lg-10">
                    <form:input cssClass="form-control digits" path="gatherData"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label">断电监测频率（分钟）</label>
                <div class="col-lg-10">
                    <form:input cssClass="form-control digits" path="monitorPower"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">环境等级</label>
                <div class="col-lg-10">
                    <form:select path="pbase.eRank" items="${applicationScope.bd_evn_rank}" itemLabel="name" itemValue="value"></form:select> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">基坑开挖深度</label>
                <div class="col-lg-10">
                    <form:select path="pbase.pitDepth" items="${applicationScope.bd_depth_rank}" itemLabel="name" itemValue="value"></form:select> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">围护特征</label>
                <div class="col-lg-10">
                    <form:select path="pbase.surroundFeatures" items="${applicationScope.bd_surround_features}" itemLabel="name" itemValue="value"></form:select> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">围护形式</label>
                <div class="col-lg-10">
                    <form:select path="pbase.surroundStyle" items="${applicationScope.bd_surround_style}" itemLabel="name" itemValue="value"></form:select> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">布井方式</label>
                <div class="col-lg-10">
                    <form:select path="pbase.pattern" items="${applicationScope.bd_pattern}" itemLabel="name" itemValue="value"></form:select> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">降水类型</label>
                <div class="col-lg-10">
                    <form:select path="pbase.type" items="${applicationScope.bd_type}" itemLabel="name" itemValue="value"></form:select> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">降压幅度</label>
                <div class="col-lg-10">
                    <form:select path="pbase.prange" items="${applicationScope.bd_range}" itemLabel="name" itemValue="value"></form:select> 
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">承压水分类</label>
                <div class="col-lg-10">
                    <c:set value="${ fn:split(csite.pbase.confined, ',') }" var="cs" />
                    <c:forEach var="confined" items="${applicationScope.bd_confined}">
						<span class="checkboxs-inline" onclick="setCheckbox('pbase.confined','${confined.value}')">
						<input type="checkbox" id="confined" name="pbase.confined" title="${confined.value}" onclick="setCheckbox('pbase.confined','${confined.value}')" 
							<c:set var="isDoing" value="0"/>
							<c:forEach var="cselect" items="${cs}">
								<c:if test="${cselect==confined.value}">
									checked="checked"
									<c:set var="isDoing" value="1"/>
								</c:if>
							</c:forEach>
						> &nbsp;${confined.name}
						
						</span>
						</c:forEach>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">土层</label>
                <div class="col-lg-10">
                    <c:set value="${ fn:split(csite.pbase.layer, ',') }" var="cl" />
                    <c:forEach var="layer" items="${applicationScope.bd_layer}">
						<span class="checkboxs-inline" onclick="setCheckbox('pbase.layer','${layer.value}')">
						<input type="checkbox" id="layer" name="pbase.layer" title="${layer.value}" onclick="setCheckbox('pbase.layer','${layer.value}')" 
							<c:set var="isDoing" value="0"/>
							<c:forEach var="cselect" items="${cl}">
								<c:if test="${cselect==layer.value}">
									checked="checked"
									<c:set var="isDoing" value="1"/>
								</c:if>
							</c:forEach>
						> &nbsp;${layer.name}
						</span>
						</c:forEach>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">降水目的层</label>
                <div class="col-lg-9" id="pprecipitation">
                     <c:if test="${not empty csite.pbase.precipitation}">
                     <c:set value="${ fn:split(csite.pbase.precipitation, ',') }" var="cp" />
                     	<c:forEach var="precipitation" items="${cp}">
						<c:set var="isDoing" value="0"/>
								<span class="checkboxs-inline" onclick="setCheckbox('pbase.precipitation','${precipitation}')">
									<input type="checkbox" id="precipitation" name="pbase.precipitation" title="${precipitation}" onclick="setCheckbox('pbase.precipitation','${precipitation}')" checked="checked"> 
								&nbsp;${precipitation}
								<c:set var="isDoing" value="1"/>
								</span>
						</c:forEach>
						
                     </c:if>
                </div>
                <div class="col-lg-1 col-sm-1">
                	<button type="button" class="addBtn btn btn-info fa  fa-building-o" onclick="getMList('请选择降水目的层','pbase.precipitation','<c:url value="/water/project/precipitation.shtml"/>','modal','','pprecipitation')"></button>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">项目图纸</label>
                <div class="col-lg-9 col-sm-9">
                	<div class="row" id="wimgs">
                		<c:choose>
                			<c:when test="${not empty csite.images }">
                				<c:forEach items="${csite.images}" var="img"> 
                 				<div class="imgItem">
                 					<div class="fileupload fileupload-new col-lg-11 col-sm-11" data-provides="fileupload">
                            <span class="preview" style="margin-left:10px;" >
                            	<c:choose>
                            		<c:when test="${not empty img.jpeg }">
                            			<a class="tooltips" data-placement="left" data-container="body" data-original-title="预览" href="<c:url value="/files/preview/${img.jpeg } "/>" ><i class="fa fa-eye">${img.jpeg }</i></a>&nbsp;
                            		</c:when>
                            		<c:otherwise>
                            			<a class="tooltips" data-placement="left" data-container="body" data-original-title="预览" href="<c:url value="/files/preview/${img.name } "/>" ><i class="fa fa-eye">${img.name }</i></a>&nbsp;
                            		</c:otherwise>
                            	</c:choose>
                            </span>
                            <span class="download default hidden-xs" id="${img.name }" style="margin-left:10px;" >
                            	<a class="tooltips" data-placement="left"  data-container="body" data-original-title="下载" href="<c:url value="/files/downloads/${img.name }"/>" ><i class="fa fa-download">${img.name }</i></a>&nbsp;
                             </span>
                        </div>
                        <div class="col-lg-1 col-sm-1"><button type="button" class="addBtn btn btn-info fa fa-minus-square" onclick="removeImage()"></button></div>
                 				</div>
                				</c:forEach>
                			</c:when>
                		</c:choose>
                	</div>
                </div>
                <div class="col-lg-1 col-sm-1">
                	<button type="button" class="addBtn btn btn-info fa fa-plus-square" onclick="addImage()"></button>
                </div>
            </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">状态</label>
                <div class="col-lg-10">
                	<form:select path="status" cssClass="form-control">
                		<c:if test="${not empty applicationScope.project_status }">
                			<c:forEach items="${applicationScope.project_status }" var="news">
								<form:option value="${news.value}">${news.name}</form:option>
							</c:forEach>
                		</c:if>
                	</form:select>
                  </div>
              </div>
            <div class="form-group">
                <label class="col-sm-2 control-label ">概述</label>
                <div class="col-sm-10">
                	<form:textarea cssClass="ckeditor form-control" rows="6"  id="summary" path="project.summary"/>
                </div>
              </div>
            <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">备注</label>
                <div class="col-lg-10">
                    <form:input cssClass="form-control " path="project.memo" />
                    <span class="help-block"> <form:errors path="project.memo" cssClass="error" /></span>
                  </div>
              </div>
              <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">是否显示给申通地铁</label>
                <div class="col-lg-10">
                    <form:select path="shengTong" cssClass="form-control">
						<form:option value="false">否</form:option>
						<form:option value="true">是</form:option>
                	</form:select>
                 </div>
              </div>
              <div class="form-group">
                <label class="col-lg-2 col-sm-2 control-label ">界面是否共享</label>
                <div class="col-lg-10">
                    <form:select path="jianGong" cssClass="form-control">
						<form:option value="false">否</form:option>
						<form:option value="true">是</form:option>
                	</form:select>
                 </div>
              </div>
     	</section>
     	<h3>其他信息</h3>
              <section>
              	<div id="no-more-tables">
              		 <table class="table table-bordered ">
						<thead>
          					<tr class="text-center">
          						<th></th>
          						<th>单位名称</th>
          						<th>负责人</th>
          						<th>联系方式</th>
          					</tr>
          				</thead>	
        				<tbody>
        					<tr>
        						<td data-title="类型">建设单位</td>
        						<td data-title="单位名称"><form:input cssClass="form-control " path="pbase.unit" id="pbase.unit" /></td>
        						<td data-title="负责人"><form:input cssClass="form-control " path="pbase.unitOwner" id="pbase.unitOwner" /></td>
        						<td data-title="联系方式"><form:input cssClass="form-control " path="pbase.unitInfo" id="pbase.unitInfo" /></td>
        					</tr>
        					<tr>
        						<td data-title="类型">施工承包单位</td>
        						<td data-title="单位名称"><form:input cssClass="form-control " path="pbase.contor" id="pbase.contor" /></td>
        						<td data-title="负责人"><form:input cssClass="form-control " path="pbase.contorOwner" id="pbase.contorOwner" /></td>
        						<td data-title="联系方式"><form:input cssClass="form-control " path="pbase.contorInfo" id="pbase.contorInfo" /></td>
        					</tr>
        					<tr>
        						<td data-title="类型">设计单位</td>
        						<td data-title="单位名称"><form:input cssClass="form-control " path="pbase.design" id="pbase.design" /></td>
        						<td data-title="负责人"><form:input cssClass="form-control " path="pbase.designOwner" id="pbase.designOwner" /></td>
        						<td data-title="联系方式"><form:input cssClass="form-control " path="pbase.designInfo" id="pbase.designInfo" /></td>
        					</tr>
        					<tr>
        						<td data-title="类型">监理单位</td>
        						<td data-title="单位名称"><form:input cssClass="form-control " path="pbase.superv" id="pbase.superv" /></td>
        						<td data-title="负责人"><form:input cssClass="form-control " path="pbase.supervOwner" id="pbase.supervOwner" /></td>
        						<td data-title="联系方式"><form:input cssClass="form-control " path="pbase.supervInfo" id="pbase.supervInfo" /></td>
        					</tr>
        					<tr>
        						<td data-title="类型" rowspan="5">降水施工单位</td>
        						<td class="hidden-xs">单位名称</td>
        						<td data-title="单位名称" colspan="2"><form:input cssClass="form-control " path="pbase.preUnit" id="pbase.preUnit" /></td>
        					</tr>
        					<tr>
        						<td class="hidden-xs" data-title="类别">项管部负责人</td>
        						<td data-title="项管部负责人"><form:input cssClass="form-control " path="pbase.pmdOwner" id="pbase.pmdOwner" /></td>
        						<td data-title="联系方式"><form:input cssClass="form-control " path="pbase.pmdInfo" id="pbase.pmdInfo" /></td>
        					</tr>
        					<tr>
        						<td class="hidden-xs" data-title="类别">项目工程师</td>
        						<td data-title="项目工程师"><form:input cssClass="form-control " path="pbase.engineer" id="pbase.engineer" /></td>
        						<td data-title="联系方式"><form:input cssClass="form-control " path="pbase.engInfo" id="pbase.engInfo" /></td>
        					</tr>
        					<tr>
        						<td class="hidden-xs" data-title="类别">安全负责人</td>
        						<td data-title="安全负责人"><form:input cssClass="form-control " path="pbase.safeOwner" id="pbase.safeOwner" /></td>
        						<td data-title="联系方式"><form:input cssClass="form-control " path="pbase.safeInfo" id="pbase.safeInfo" /></td>
        					</tr>
        					<tr>
        						<td class="hidden-xs" data-title="类别">技术负责人</td>
        						<td data-title="技术负责人"><form:input cssClass="form-control " path="pbase.techOwner" id="pbase.techOwner" /></td>
        						<td data-title="联系方式"><form:input cssClass="form-control " path="pbase.techInfo" id="pbase.techInfo" /></td>
        					</tr>
        				</tbody>
        				
          				</table>
          				<div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">项目评论信息接收人</label>
			                <div class="col-lg-9 col-sm-9" id="cdiv">
			                	<c:if test="${not empty cusers}">
			                		<c:forEach items="${cusers }" var="cuser">
			                			<input name="cusers" checked="checked" type="checkbox" title="${cuser.id }" value="${cuser.firstName }"><a class="menulink" href="javascript:void(0);" onclick="setCheckbox('cusers',${cuser.id})">&nbsp;${cuser.firstName }</a>
			                		</c:forEach>
			                	</c:if>
			                </div>
			                <div class="col-lg-1 col-sm-1">
			                	<button type="button" class="addBtn btn btn-info fa fa-user" onclick="getMList('请选择项目评论信息接收人','cusers','<c:url value="/water/project/users.shtml"/>','modal','','cdiv')"></button>
			                </div>
			            </div>	
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">告警信息接收人</label>
			                <div class="col-lg-9 col-sm-9" id="wdiv">
			                	<c:if test="${not empty wusers}">
			                		<c:forEach items="${wusers }" var="wuser">
			                			<input name="wusers" checked="checked" type="checkbox" title="${wuser.id }" value="${wuser.firstName }"><a class="menulink" href="javascript:void(0);" onclick="setCheckbox('wusers',${wuser.id})">&nbsp;${wuser.firstName }</a>
			                		</c:forEach>
			                	</c:if>
			                </div>
			                <div class="col-lg-1 col-sm-1">
			                	<button type="button" class="addBtn btn btn-info fa fa-user" onclick="getMList('请选择告警信息接收人','wusers','<c:url value="/water/project/users.shtml"/>','modal','','wdiv')"></button>
			                </div>
			            </div>	
              	</div>	 
             </section>
      </div>
      <form:hidden path="id" />
      <form:hidden path="project.projectOwnerid" name="projectOwnerid" id="projectOwnerid" />
      <form:hidden path="project.zone.id" id="zone.id" />
      <form:hidden path="project.department.id" id="department.id" />
      <form:hidden path="pbase.id" id="pbase.id" />
      <form:hidden path="project.id" id="project.id" />
      <form:hidden path="rail" id="rail" />
      <input type="hidden" name="delids" id="delids"/>
  </form:form>
<script>
	jQuery(document).ready(function() {
		setAhref();
	});

	function setAhref(){
		if($(window).width()>767){
			if($('.preview').html()!=""){
				$('.preview a').attr("target", "_blank");
			}
		}else {
			if($('.preview').html()!=""){
				$('.preview a').removeAttr('target');
			}
		}
	}
	function getcFromPoint(){
		if($('#longitude').val() == "" || $('#latitude').val() == ""){
			getFromPoint($("#city").val(),$("#address").val(),$("input[id='zone.name']").val(),18);
		}else{
			getFromll($('#longitude').val(),$('#latitude').val());
		}
		
	}
	//自动添加经纬度
	function autoSetPoint() {
		//经纬度为空时自动赋值
		if ($('#longitude').val() == "" || $('#latitude').val() == "") {
			setPoint($('#city').val() + $('#address').val(), $(
					"input[id='zone.name']").val());
		}
	}

	function setcpoint() {
		setPoint($("#city").val() + $("#address").val(), $(
				"input[id='zone.name']").val());
	}
	
	$(window).resize(function() {
		setAhref();
		});
</script>
<script src='<c:url value="/resources/js/DrawingManager.js" />'></script>
 <script src='<c:url value="/resources/js/project/modalpagemuti.js" />'></script>