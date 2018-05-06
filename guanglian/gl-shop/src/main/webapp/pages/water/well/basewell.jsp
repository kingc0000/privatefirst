<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>	
	<style>
.wizard > .steps > ul > li
{
    width: 25% !important;
}
</style>

						<h3>成井信息1</h3>
	                         <section>
	                         	<div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">孔径/mm</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " type="number" path="pointInfo.aperture" id="pointInfo.aperture" />
                                       <span class="help-block"> <form:errors path="pointInfo.aperture" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">管井/mm</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " type="number" path="pointInfo.tubeWell" id="pointInfo.tubeWell" />
                                       <span class="help-block"> <form:errors path="pointInfo.tubeWell" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">井深/m</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " type="number" path="pointInfo.deepWell" id="pointInfo.deepWell" />
                                       <span class="help-block"> <form:errors path="pointInfo.deepWell" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">滤管长度</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " type="number" path="pointInfo.fTubleLgn" id="pointInfo.fTubleLgn" />
                                       <span class="help-block"> <form:errors path="pointInfo.fTubleLgn" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">滤料回填量/t</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " type="number" path="pointInfo.backfillVol" id="pointInfo.backfillVol" />
                                       <span class="help-block"> <form:errors path="pointInfo.backfillVol" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">粘土球回填量/t</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " type="number" path="pointInfo.clayBackfill" id="pointInfo.clayBackfill" />
                                       <span class="help-block"> <form:errors path="pointInfo.clayBackfill" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">井口是否回填</label>
                                   <div class="col-lg-10">
                                       <form:select type="select" cssClass="form-control" path="pointInfo.backFill" id="pointInfo.backFill"  >
											 <form:option value="0" selected="selected" >否</form:option>
												<form:option value="1">是</form:option>
	        		  					</form:select>
                                       <span class="help-block"> <form:errors path="pointInfo.backFill" cssClass="error" /></span>
                                   </div>
                               </div>
                                <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">洗井周期</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control "  path="pointInfo.washWC" id="pointInfo.washWC" />
                                       <span class="help-block"> <form:errors path="pointInfo.washWC" cssClass="error" /></span>
                                   </div>
                               </div>
                               </section>
                                <h3>成井信息2</h3>
                                <section>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">单井涌水量/m3/h</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " type="number" path="pointInfo.sWellFlow" id="pointInfo.sWellFlow" />
                                       <span class="help-block"> <form:errors path="pointInfo.sWellFlow" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">动水位/m</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " type="number" path="pointInfo.moveingWater" id="pointInfo.moveingWater" />
                                       <span class="help-block"> <form:errors path="pointInfo.moveingWater" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">初始水位/m</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " type="number" path="pointInfo.initialWater" id="pointInfo.initialWater" />
                                       <span class="help-block"> <form:errors path="pointInfo.initialWater" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">井号现场标识</label>
                                   <div class="col-lg-10">
                                       <form:input cssClass="form-control " path="pointInfo.poundSite" id="pointInfo.poundSite" />
                                       <span class="help-block"> <form:errors path="pointInfo.poundSite" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
                                   <label class="col-lg-2 col-sm-2 control-label ">是否有异常</label>
                                   <div class="col-lg-10">
                                       <form:select type="select" cssClass="form-control" path="pointInfo.exception" id="pointInfo.exception"  >
											 <form:option value="0" selected="selected" >否</form:option>
												<form:option value="1">是</form:option>
	        		  					</form:select>
                                       <span class="help-block"> <form:errors path="pointInfo.exception" cssClass="error" /></span>
                                   </div>
                               </div>
                               <div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label ">成井时间</label>
								<div class="col-lg-10">
								<div  class="input-group date date-picker">
                                       <input type="text" class="form-control"  id="pointInfo.wellTime" name="pointInfo.wellTime">
                                       <div class="input-group-btn">
                                           <button type="button" class="btn btn-warning date-set"><i class="fa fa-calendar"></i></button>
                                       </div>
                                   </div>
                               </div> 
							</div>
							<div class="form-group">
	                         <label class="col-lg-2 col-sm-2 control-label ">成井负责人</label>
	                         <div class="col-lg-10">
	                          <div class="input-group">
	                       	 	<form:input cssClass="form-control " path="pointInfo.cPerson" id="pointInfo.cPerson"  />
	                      	 	<span class="input-group-btn " >
	                              <button class="btn btn-theme tooltips" type="button"  data-placement="left" data-container="body" data-original-title="成井负责人" onclick="getList('请选择成井负责人','pointInfo.cPerson','<c:url value="/water/csite/users.shtml"/>','modal', 'assignName')"><i class="fa fa-user"></i></button>
	                            </span>
	                      	  </div>
	                     	 </div>
	                     </div>
	                     <div class="form-group">
	                         <label class="col-lg-2 col-sm-2 control-label ">验收管理人员</label>
	                         <div class="col-lg-10">
	                          <div class="input-group">
	                       	 	<form:input cssClass="form-control " path="pointInfo.acceptance" id="pointInfo.acceptance"  />
	                      	 	<span class="input-group-btn " >
	                              <button class="btn btn-theme tooltips" type="button"  data-placement="left" data-container="body" data-original-title="验收管理人员" onclick="getList('请选择验收管理人员','pointInfo.acceptance','<c:url value="/water/csite/users.shtml"/>','modal', 'assignName')"><i class="fa fa-user"></i></button>
	                            </span>
	                      	  </div>
	                     	 </div>
	                     </div>
	                         </section> 
	                        <h3>封井信息</h3>
	                        <section>
	                        	<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label ">封井时间</label>
								<div class="col-lg-10">
								<div  class="input-group date date-picker">
                                       <input type="text" class="form-control"  id="pointInfo.closure" name="pointInfo.closure">
                                       <div class="input-group-btn">
                                           <button type="button" class="btn btn-warning date-set"><i class="fa fa-calendar"></i></button>
                                       </div>
                                   </div>
                               </div> 
							</div>
							<div class="form-group">
								<label class="col-lg-2 col-sm-2 control-label ">封井措施</label>
								<div class="col-lg-10">
                                       <form:input cssClass="form-control" path="pointInfo.cmeasures" id="pointInfo.cmeasures" />
                                       <span class="help-block"> <form:errors path="pointInfo.cmeasures" cssClass="error" /></span>
                                   </div>
                               </div> 
                               <div class="form-group">
	                         <label class="col-lg-2 col-sm-2 control-label">封井验收人</label>
	                         <div class="col-lg-10">
	                          <div class="input-group">
	                       	 	<form:input cssClass="form-control " path="pointInfo.sAcceptance" id="pointInfo.sAcceptance"  />
	                      	 	<span class="input-group-btn " >
	                              <button class="btn btn-theme tooltips" type="button"  data-placement="left" data-container="body" data-original-title="封井验收人" onclick="getList('请选择封井验收人','pointInfo.sAcceptance','<c:url value="/water/csite/users.shtml"/>','modal', 'assignName')"><i class="fa fa-user"></i></button>
	                             </span>
	                      	  </div>
	                     	 </div>
	                     </div>
	                        </section>
<script>
function setWellRunStatus(index,autoid,autotype){
	if(index==1){
		$(".deepclass").show();
		$(".dumpclass").hide();
		if(autoid!=null && autoid!=""){
			$('#loading').hide();
			$.ajax({
				type : "POST",
				url : _context+"/water/pwell/autoWell.shtml",
				dataType : "json",
				data : "wid="+autoid+"&wtype="+autotype,
				success : function(data) {
					if(data=="-1" ){
						toastr.error("获取根据水位自动开启关联测点失败，请重试");
					}else if(data!=""){
						$("#auto").val(data);
					}
					$('#loading').hide();
				},
				error : function(data, xhr, textStatus, errorThrown) {
					$('#loading').hide();
					toastr.error("获取根据水位自动开启关联测点失败，请重试");
				}
			});
		}
	}else if(index==2){
		$(".deepclass").hide();
		$(".dumpclass").show();
	}else{
		$(".deepclass").hide();
		$(".dumpclass").hide();
	}
}
</script>
	                        