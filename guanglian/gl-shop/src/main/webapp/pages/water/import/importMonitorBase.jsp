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
                                          
        <div class="form-group clearfix monitordata" >
            <label class="col-lg-2 control-label control-required" >测点名称</label>
            <div class="col-lg-4">
                <select class="form-control select monitordata required"></select>
            </div>
             <label class="col-lg-2 control-label control-required" ><span id="dgaochen">本次高程</span></label>
            <div class="col-lg-4">
                <select class="form-control select monitordata required"></select>
            </div>
        </div>
        <div class="form-group clearfix monitordata" >
            <label class="col-lg-2 control-label " >导入时间</label>
            <div class="col-lg-4">
                <select class="form-control select monitordata"></select>
            </div>
        </div>
        
        <div class="form-group clearfix monitorpoint" >
            <label class="col-lg-2 control-label control-required" >测点名称</label>
            <div class="col-lg-4">
                <select class="form-control select monitorpoint required"></select>
            </div>
             <label class="col-lg-2 control-label control-required" ><span id="cgaochen">初始高程</span></label>
            <div class="col-lg-4">
                <select class="form-control select monitorpoint required"></select>
            </div> 
        </div>
        <div class="form-group clearfix monitordatacomon" >
            <label class="col-lg-2 control-label " ><span id="kwqwy">开挖前累计位移</span></label>
            <div class="col-lg-10">
                <select class="form-control select monitordatacomon"></select>
            </div>
        </div>
        <div class="form-group clearfix monitorpointsupaxial" >
            <label class="col-lg-2 control-label control-required" >告警值</label>
            <div class="col-lg-4">
                <select class="form-control select monitorpointsupaxial required"></select>
            </div>
            <label class="col-lg-2 control-label control-required" >支撑类型</label>
            <div class="col-lg-4">
                <select class="form-control select monitorpointsupaxial control-required"></select>
            </div>
        </div>
        <div class="form-group clearfix monitorpoint" >
            <label class="col-lg-2 control-label " >备注</label>
            <div class="col-lg-10">
                <select class="form-control select monitorpoint"></select>
            </div>
        </div>
                                          
                                     
