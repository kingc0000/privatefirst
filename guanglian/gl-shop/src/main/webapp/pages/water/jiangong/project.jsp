<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/css/jquery.steps.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
	<style>
.wizard > .steps > ul > li
{
    width: 50% !important;
}
</style>
<script>
$("#pictitle").html("地下水项目管理");
</script>
<section class="panel">
            <header class="panel-heading">
                <span id="edittile">项目信息</span>
            </header>
            <div class="panel-body">
			<form:form cssClass="form-horizontal" role="form" commandName="csite" id="storeform">
				  <div id="wizard">
			     	<h3>基本信息</h3>
			     	<section >
			     		<div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label">项目名称</label>
			                <div class="col-lg-10 ">
			                	<div class=" form-control">${csite.project.name}</div>
			                    
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label control-required">项目等级</label>
			                <div class="col-lg-10">
			                		<c:forEach items="${applicationScope.bd_proj_rank}" var="bg">
			                			<c:if test="${bg.value== csite.pbase.rank}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                		</c:forEach>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label control-required">工程特性</label>
			                <div class="col-lg-10">
			                		<c:forEach items="${applicationScope.bd_project_type}" var="bg">
			                			<c:if test="${bg.value== csite.project.features}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                		</c:forEach>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label control-required">项目负责人</label>
			                <div class="col-lg-10">
			                	<div class=" form-control">${csite.project.projectOwner}</div>
			            	 </div>
			            </div>
			            
			           <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label control-required">省份/直辖市</label>
			                	<div class="col-lg-10">
			                		<div class=" form-control">${csite.project.zone.name }</div>
			                	</div>
			            </div>
			             <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label control-required">城市</label>
			                	<div class="col-lg-10">
			                		<div class=" form-control">${csite.project.city }</div>
			                	</div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label control-required">详细地址</label>
			                <div class="col-lg-10">
			                		<div class=" form-control">${csite.project.address }</div>
			                	</div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label control-required">经度</label>
			                <div class="col-lg-10">
			                		<div class=" form-control">${csite.longitude }</div>
			                	</div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label control-required">纬度</label>
			                 <div class="col-lg-10">
			                		<div class=" form-control">${csite.latitude }</div>
			                	</div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">环境等级</label>
			                <div class="col-lg-10">
			               	 <c:forEach items="${applicationScope.bd_evn_rank}" var="bg">
			                				<c:if test="${bg.value== csite.pbase.eRank}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                </c:forEach>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">基坑开挖深度</label>
			                <div class="col-lg-10">
			               	 <c:forEach items="${applicationScope.bd_depth_rank}" var="bg">
			                				<c:if test="${bg.value== csite.pbase.pitDepth}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                </c:forEach>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">围护特征</label>
			                <div class="col-lg-10">
			               	 <c:forEach items="${applicationScope.bd_surround_features}" var="bg">
			                				<c:if test="${bg.value== csite.pbase.surroundFeatures}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                </c:forEach>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">围护形式</label>
			                <div class="col-lg-10">
			               	 <c:forEach items="${applicationScope.bd_surround_style}" var="bg">
			                				<c:if test="${bg.value== csite.pbase.surroundStyle}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                </c:forEach>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">布井方式</label>
			                <div class="col-lg-10">
			               	 <c:forEach items="${applicationScope.bd_pattern}" var="bg">
			                				<c:if test="${bg.value== csite.pbase.pattern}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                </c:forEach>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">降水类型</label>
			                <div class="col-lg-10">
			               	 <c:forEach items="${applicationScope.bd_type}" var="bg">
			                				<c:if test="${bg.value== csite.pbase.type}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                </c:forEach>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">降压幅度</label>
			                <div class="col-lg-10">
			               	 <c:forEach items="${applicationScope.bd_range}" var="bg">
			                				<c:if test="${bg.value== csite.pbase.prange}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                </c:forEach>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">承压水分类</label>
			                <div class="col-lg-10">
			                    <div class=" form-control">
			                    <c:set value="${ fn:split(csite.pbase.confined, ',') }" var="cs" />
			                    <c:forEach var="confined" items="${applicationScope.bd_confined}">
									<c:set var="isDoing" value="0"/>
										<c:forEach var="cselect" items="${cs}">
											<c:if test="${cselect==confined.value}">
												&nbsp;${confined.name}
												<c:set var="isDoing" value="1"/>
											</c:if>
										</c:forEach>
									</c:forEach>
									</div>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">土层</label>
			                <div class="col-lg-10">
			                    <div class=" form-control">
			                    <c:set value="${ fn:split(csite.pbase.layer, ',') }" var="cl" />
			                    <c:forEach var="layer" items="${applicationScope.bd_layer}">
									<c:set var="isDoing" value="0"/>
										<c:forEach var="cselect" items="${cl}">
											<c:if test="${cselect==layer.value}">
												&nbsp;${layer.name}
												<c:set var="isDoing" value="1"/>
											</c:if>
										</c:forEach>
									</c:forEach>
									</div>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">降水目的层</label>
			                <div class="col-lg-10">
			                    <div class=" form-control">
			                     <c:set value="${ fn:split(csite.pbase.precipitation, ',') }" var="cp" />
			                     <c:forEach var="precipitation" items="${applicationScope.bd_precipitation}">
										<c:set var="isDoing" value="0"/>
											<c:forEach var="cselect" items="${cp}">
												<c:if test="${cselect==precipitation.value}">
													&nbsp;${precipitation.name}
													<c:set var="isDoing" value="1"/>
												</c:if>
											</c:forEach>
											</c:forEach>
											</div>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">项目图纸</label>
			                <div class="col-lg-10 col-sm-10">
			                	<div class="row" id="wimgs">
			                		<c:choose>
			                			<c:when test="${not empty csite.images }">
			                				<c:forEach items="${csite.images}" var="img"> 
			                 				<div class="imgItem">
			                 					<div class="fileupload fileupload-new col-lg-12 col-sm-12" data-provides="fileupload">
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
			                 				</div>
			                				</c:forEach>
			                			</c:when>
			                		</c:choose>
			                	</div>
			                </div>
			            </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">状态</label>
			                <label class="col-lg-10">
			               	 <c:forEach items="${applicationScope.project_status}" var="bg">
			                				<c:if test="${bg.value== csite.status}">
			                				<div class=" form-control">${bg.name}</div>
			                			</c:if>
			                </c:forEach>
			                </label>
			              </div>
			            <div class="form-group">
			                <label class="col-sm-2 control-label ">概述</label>
			                <label class="col-lg-10">
			                	<div class="form-control"> ${csite.project.summary }</div>
			                </label>
			              </div>
			            <div class="form-group">
			                <label class="col-lg-2 col-sm-2 control-label ">备注</label>
			                <label class="col-lg-10">
			                	<div class="form-control">${csite.project.memo }</div>
			                </label>
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
			        						<td data-title="单位名称"><label class="form col-lg-10">${csite.pbase.unit }</label></td>
			        						<td data-title="负责人"><label class="form col-lg-10">${csite.pbase.unit }</label></td>
			        						<td data-title="联系方式"><label class="form col-lg-10">${csite.pbase.unitInfo }</label></td>
			        					</tr>
			        					<tr>
			        						<td data-title="类型">施工承包单位</td>
			        						<td data-title="单位名称"><label class="form col-lg-10">${csite.pbase.contor }</label></td>
			        						<td data-title="负责人"><label class="form col-lg-10">${csite.pbase.contorOwner }</label></td>
			        						<td data-title="联系方式"><label class="form col-lg-10">${csite.pbase.contorInfo }</label>></td>
			        					</tr>
			        					<tr>
			        						<td data-title="类型">设计单位</td>
			        						<td data-title="单位名称"><label class="form col-lg-10">${csite.pbase.design }</label></td>
			        						<td data-title="负责人"><label class="form col-lg-10">${csite.pbase.designOwner }</label></td>
			        						<td data-title="联系方式"><label class="form col-lg-10">${csite.pbase.designInfo }</label></td>
			        					</tr>
			        					<tr>
			        						<td data-title="类型">监理单位</td>
			        						<td data-title="单位名称"><label class="form col-lg-10">${csite.pbase.superv }</label></td>
			        						<td data-title="负责人"><label class="form col-lg-10">${csite.pbase.supervOwner }</label></td>
			        						<td data-title="联系方式"><label class="form col-lg-10">${csite.pbase.supervInfo }</label></td>
			        					</tr>
			        					<tr>
			        						<td data-title="类型" rowspan="5">降水施工单位</td>
			        						<td class="hidden-xs">单位名称</td>
			        						<td data-title="单位名称" colspan="2"><label class="form col-lg-10">${csite.pbase.preUnit }</label></td>
			        					</tr>
			        					<tr>
			        						<td class="hidden-xs" data-title="类别">项管部负责人</td>
			        						<td data-title="项管部负责人"><label class="form col-lg-10">${csite.pbase.pmdOwner }</label></td>
			        						<td data-title="联系方式"><label class="form col-lg-10">${csite.pbase.pmdInfo }</label></td>
			        					</tr>
			        					<tr>
			        						<td class="hidden-xs" data-title="类别">项目工程师</td>
			        						<td data-title="项目工程师"><label class="form col-lg-10">${csite.pbase.engineer }</label></td>
			        						<td data-title="联系方式"><label class="form col-lg-10">${csite.pbase.engInfo }</label></td>
			        					</tr>
			        					<tr>
			        						<td class="hidden-xs" data-title="类别">安全负责人</td>
			        						<td data-title="安全负责人"><label class="form col-lg-10">${csite.pbase.safeOwner }</label></td>
			        						<td data-title="联系方式"><label class="form col-lg-10">${csite.pbase.safeInfo }</label></td>
			        					</tr>
			        					<tr>
			        						<td class="hidden-xs" data-title="类别">技术负责人</td>
			        						<td data-title="技术负责人"><label class="form col-lg-10">${csite.pbase.techOwner }</label></td>
			        						<td data-title="联系方式"><label class="form col-lg-10">${csite.pbase.techInfo }</label></td>
			        					</tr>
			        				</tbody>
			        				
			          				</table>
			              	</div>	 
			             </section>
			      </div>
			     </form:form> 
			   </div>  
		     
        </section>
<script>
	
jQuery(document).ready(function() {
    	
    	var form = $("#storeform");
    	if(form.find('h3').length>0){
    		form.children("div").steps({
        	    headerTag: "h3",
        	    bodyTag: "section",
        	    transitionEffect: "slideLeft",
        	    enableFinishButton:false
        	    
        	});
    	}
    	
    	

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
	
	$(window).resize(function() {
		setAhref();
		});
</script>
<script src='<c:url value="/resources/js/jquery.steps.min.js" />'></script>
<script src='<c:url value="/resources/js/jquery.stepy.js" />'></script>