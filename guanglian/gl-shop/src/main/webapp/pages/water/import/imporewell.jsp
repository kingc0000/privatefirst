<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<link href='<c:url value="/resources/css/jquery.steps.css" />' rel="stylesheet">
<link rel="stylesheet" type="text/css" href='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.css" />' />

<div class="modal fade" id="import-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
      <div class="modal-dialog mag-dialog" >
        <div class="modal-content" >
            <div class="modal-header">
              <button class="close " data-dismiss="modal" type="button">&times;</button>
              <h4 class="modal-title " >环境监测导入</h4>
            </div>
            <div class="modal-body" style="height:100%;width:100%" >
            	<section class="panel" >
	<header class="panel-heading"><span id="ititle"></span></header>
	<div class="panel-body">
                              <form id="importform" action="#">
                                  <div id="wizard">
                                      <h3>请选择导入文件</h3>
                                      <section>
                                      <div class="alert alert-block alert-danger fade in">
		                                  
		                                  <strong>注意</strong>当前版本只支持Excel格式的文件导入，如果没有选择观测时间和观测者则系统自动输入
		                              </div>
                                          <div class="form-group clearfix">
                                          <label class="control-label col-md-4 control-required">请选择导入文件</label>
                                          <div class="col-md-8">
                                              <div class="fileupload fileupload-new" data-provides="fileupload">
                                                <span class="btn btn-white btn-file">
                                                <span class="fileupload-new"><i class="fa fa-paper-clip"></i>请选择导入文件</span>
                                                <span class="fileupload-exists"><i class="fa fa-undo"></i>修改</span>
                                                <input type="file" name="uploadfile" id="uploadfile" class="default required" />
                                                </span>
                                                  <span class="fileupload-preview" style="margin-left:5px;"></span>
                                                  <a href="#" class="close fileupload-exists" data-dismiss="fileupload" style="float: none; margin-left:5px;"></a>
                                              </div>
                                          </div>
                                      </div>
                                	
                                      </section>
                                      <h3>请选择导入项目</h3>
                                      <section>
										<div class="alert alert-block alert-danger fade in">
		                                  
		                                  <strong>注意!</strong> 选择框的内容是从导入文件获取的，请选择对应的导入选项！！！
		                              </div>
                                          
                                          <div class="form-group clearfix" >
                                              <label class="col-lg-2 control-label control-required" >名称</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select required"></select>
                                              </div>
                                               <label class="col-lg-2 control-label control-required" >经度</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select required"></select>
                                              </div>
                                          </div>
                                          <div class="form-group clearfix" >
                                              <label class="col-lg-2 control-label control-required" >纬度</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select required"></select>
                                              </div>
                                               <label class="col-lg-2 control-label control-required" >环境数据告警阈值</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select required"></select>
                                              </div>
                                          </div>
                                          <div class="form-group clearfix" >
                                              <label class="col-lg-2 control-label " >状态(0:开启 Or 1：关闭 默认为0)</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select"></select>
                                              </div>
                                              <label class="col-lg-2 control-label" >备注</label>
                                              <div class="col-lg-4">
                                                  <select class="form-control select"></select>
                                              </div>
                                          </div>
                                          
                                      </section>
                                      <h3>确认导入</h3>
                                      <section>
                                          <div class="form-group clearfix">
                                              <div class="col-lg-12">
                                                  <div class="progress progress-striped active progress-sm">
                            				     	 <div id ="progress" class="progress-bar progress-bar-success" style="line-height:14px;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
                                					  	<span id="spanprogress">0%</span>
                                					  </div>
                             					 </div>
                                              </div>
                                              <div>
                                              </div>
                                          </div>
                                          <div id="result" class="alert alert-block alert-danger fade in" style="display:none;word-wrap: break-word; word-break: normal; ">
		                                  
		                                  <strong><s:message code="datacenter.note" text="Note "/>!</strong><s:message code="import.error.line" text="Error "/>：<span id="errocols"> </span>！！！
		                              </div>
                                      </section>
                                  </div>
                                  <input type="hidden" id="iid"/>
                              </form>

	</div>

</section>
            </div>
        </div>
      </div>
    </div>

<script src='<c:url value="/resources/js/jquery.steps.min.js" />'></script>
<script src='<c:url value="/resources/js/jquery.stepy.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script>
<script src='<c:url value="/resources/js/ajaxfileupload.js" />'></script>
<script src='<c:url value="/resources/js/project/importe.js" />'></script>

