<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<script>
	$("#pictitle").html("项目统计");
</script>
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<div class="row" >
	<div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                <span id="edittile">项目统计</span>
                <span class="tools pull-right">
                   <a href="javascript:;" class="fa fa-chevron-down"></a>
                 </span>
            </header>
            <div class="panel-body">
            	<form:form cssClass="form-horizontal" role="form" commandName="csite" id="csiteform">
	         		<div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">名称</label>
	                    <div class="col-md-10 ">
	                    	<form:input class="form-control" path="project.name" />
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">工程特性</label>
	                    <div class="col-md-10">
	                    	<form:select path="project.features" items="${applicationScope.bd_project_type}" itemLabel="name" itemValue="value"></form:select>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">设计单位</label>
	                    <div class="col-md-10 ">
	                    	<form:input class="form-control" path="pbase.design" />
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">施工单位</label>
	                    <div class="col-md-10 ">
	                    	<form:input class="form-control" path="pbase.contor" />
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">降水施工单位</label>
	                    <div class="col-md-10 ">
	                    	<form:input class="form-control" path="pbase.preUnit" />
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">建设单位</label>
	                    <div class="col-md-10 ">
	                    	<form:input class="form-control" path="pbase.unit" />
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">项管部负责人</label>
	                    <div class="col-md-10 ">
	                    	<form:input class="form-control" path="pbase.pmdOwner" />
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">项目负责人</label>
	                    <div class="col-md-10 ">
	                    	<form:input class="form-control" path="project.projectOwner" />
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">技术负责人</label>
	                    <div class="col-md-10 ">
	                    	<form:input class="form-control" path="pbase.techOwner" />
	                    </div>
	                </div>
	         		<div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">地区</label>
	                    <div class="col-md-10 ">
	                    	<form:input class="form-control" path="project.zone.name" readonly="true" onclick="getList('请选择省份/直辖市','project.zone.name','zones.shtml')"/>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">工程等级</label>
	                    <div class="col-md-10">
	                    	<form:select path="pbase.rank" class="required" items="${applicationScope.bd_proj_rank}" itemLabel="name" itemValue="value"></form:select> 
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">环境等级</label>
	                    <div class="col-md-10">
	                    	 <form:select path="pbase.eRank" items="${applicationScope.bd_evn_rank}" itemLabel="name" itemValue="value"></form:select>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label class="col-lg-2 col-sm-2 control-label ">围护特征</label>
	                    <div class="col-md-10">
	                    	<form:select path="pbase.surroundFeatures" items="${applicationScope.bd_surround_features}" itemLabel="name" itemValue="value"></form:select> 
	                    </div>
	                </div>
	                <div class="form-group">
                         <label class="col-lg-2 col-sm-2 control-label ">围护形式</label>
                         <div class="col-md-10">
                             <form:select path="pbase.surroundStyle" items="${applicationScope.bd_surround_style}" itemLabel="name" itemValue="value"></form:select> 
                         </div>
                     </div>
                     <div class="form-group">
                         <label class="col-lg-2 col-sm-2 control-label ">布井方式</label>
                         <div class="col-md-10">
                             <form:select path="pbase.pattern" items="${applicationScope.bd_pattern}" itemLabel="name" itemValue="value"></form:select> 
                         </div>
                     </div>
                     <div class="form-group">
                         <label class="col-lg-2 col-sm-2 control-label ">降水类型</label>
                         <div class="col-md-10">
                             <form:select path="pbase.type" items="${applicationScope.bd_type}" itemLabel="name" itemValue="value"></form:select> 
                         </div>
                     </div>
                     <div class="form-group">
                         <label class="col-lg-2 col-sm-2 control-label ">降压幅度</label>
                         <div class="col-md-10">
                             <form:select path="pbase.prange" items="${applicationScope.bd_range}" itemLabel="name" itemValue="value"></form:select> 
                         </div>
                     </div>
	                <div class="form-group">
                         <label class="col-lg-2 col-sm-2 control-label ">承压水分类</label>
                         <div class="col-lg-10">
                             <c:forEach var="confined" items="${applicationScope.bd_confined}">
								<span class="checkboxs-inline" onclick="setCheckbox('pbase.confined','${confined.value}')">
									<input type="checkbox"  name="pbase.confined" title="${confined.value}"> &nbsp;${confined.name}
								</span>
							</c:forEach>
                         </div>
                     </div>
                     <div class="form-group">
                         <label class="col-lg-2 col-sm-2 control-label ">土层</label>
                         <div class="col-lg-10">
                             <c:forEach var="layer" items="${applicationScope.bd_layer}">
								<span class="checkboxs-inline" onclick="setCheckbox('pbase.layer','${layer.value}')">
									<input type="checkbox"  name="pbase.layer" title="${layer.value}"> &nbsp;${layer.name}
								</span>
							</c:forEach>
                         </div>
                     </div>
                     <div class="form-group">
                         <label class="col-lg-2 col-sm-2 control-label ">降水目的层</label>
                         <div class="col-lg-10">
                              <c:forEach var="precipitation" items="${applicationScope.bd_precipitation}">
								<span class="checkboxs-inline" onclick="setCheckbox('pbase.precipitation','${precipitation.value}')">
									<input type="checkbox"  name="pbase.precipitation" title="${precipitation.value}"> &nbsp;${precipitation.name}
								</span>
							</c:forEach>
                         </div>
                     </div>
                     <div class="form-group">
						<div class="col-lg-2 col-sm-2"></div>
						<div class="col-lg-10">
							<button type="button" onclick="clearall()" class="btn btn-info">重置</button>
							<button type="button" onclick="find()" class="btn btn-success">提交</button>
						</div>
					</div>
					<input type="hidden" name="page" id="page" />
                </form:form>
        	</div>
        </section>
        <section class="panel">
        	<header class="panel-heading">查询结果</header>
        	<div class="panel-body" style="outline: none;overflow-y:scroll;" id="no-more-tables">
        		<table class="table table-hover table-bordered">
        			<thead>
                          <tr class="text-center">
                          	<th>编号</th>
                          	<th>项目名称</th>  
						    <th>降水目的层</th>  
						    <th>开启数</th>  
						    <th>关闭数</th>  
						    <th>警告数</th>  
						    <th>小计</th>
						    <th>测点类型</th>
                    	 </tr>
                     </thead> 
                     <tbody id="find">
                     </tbody>        
        		</table>
        		<div class="pull-right" id="pagination"></div>
        	</div>
        </section>
    </div>
</div>
<!-- modal : 数据图 -->
<div class="modal fade " id="datamodal" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"><span class="projectname text-warning"></span></h4>
            </div>
            <div class="modal-body" id="modal-body">
            	<div class="panel-body" id="pchart">
            	</div>
            </div>
			<div class="modal-footer">
                <button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
            </div>
        </div>
    </div>
</div>

<script src='<c:url value="/resources/js/project-fore/pagingation.js"/>'></script>
<script>
jQuery(document).ready(function() {
	$('select').prepend("<option value=''>全部</option>");
	$("select").val("");
});
function setCheckbox(pname,gid){
	$.each($('input[name="'+pname+'"]'),function(index,data){
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
function find(){
	var url="list.shtml" ;
	
	/**有checkbox的*/
	var obj1 = $("#csiteform input:checked");
	$(obj1).each(function(i,o) {
		$(o).val($(o).attr('title'));
			});
	
	var data = new FormData($('#csiteform')[0]);
	
	

	$('#loading').show();
	$.ajax({
		type : "POST",
		cache: false, //cache设置为false，上传文件不需要缓存。
		url : url,
		data : data,
		async : true,
		processData: false, //processData设置为false。因为data值是FormData对象，不需要对数据做处理。
		contentType: false, //由<form>表单构造的FormData对象，且已经声明了属性enctype=multipart/form-data，所以这里设置为false
		success: function(response){  
			$('#loading').hide();
			$('#find').html(response.out);
			writePaging(response.page);
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			alert('error ' + errorThrown);
		}
	});
	return false;
}
function clearall(){
	$(':input', '#csiteform').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
}

function showbin(str,warning,open,close){
	$("#datamodal").modal('show');
	$(".projectname").html(str);
	var pwidth=400;
	if($(window).width()<769){
		 pwidth=$("#datamodal .panel-body").width()-20;
	}
	
	$("#pchart").html('<canvas id="pointsChart" width="'+pwidth+'" height="'+pwidth+'"></canvas>');
	
	
	var ctx = $("#pointsChart");
	var myChart = new Chart(ctx, {type: 'pie',
		data: {
	        labels: ["报警数", "开启数", "关闭数"],
	        datasets: [{
	            data: [warning, open, close],
	            backgroundColor: [
	                              "#F7464A",
	                              "#46BFBD",
	                              "#FDB45C"
	                          ],
                 hoverBackgroundColor: [
                                        "#FF5A5E",
                                        "#5AD3D1",
                                        "#FFC870"
                                    ]               
	        }]
	    },
	    options: {
	    	responsive: false
	    }
	});
	
	
}

function showline(obj,pid){
	$('#loading').show();
	$.ajax({
        cache: true,
        type: "POST",
        url:'lines.shtml',
        data:'pid='+pid,
        error: function(response) {
        	$('#loading').hide();
			toastr.error('操作失败');
        },
        success: function(response) {
			$('#loading').hide();
			if(response!=null && response!=""){
				$(".projectname").html(obj.parent().parent().children('td').eq(1).html()+'观测井区间图');
				var pwidth=400;
				if($(window).width()<769){
					 pwidth=$("#datamodal .panel-body").width()-20;
				}
				$("#pchart").html('<canvas id="pointsChart" width="'+pwidth+'" height="'+pwidth+'"></canvas>');
				
				var label = getarray(response.label);
				var data1 = getarray(response.data1);
				var data2 = getarray(response.data2);
				var data3 = getarray(response.data3);
				var ctx = $("#pointsChart");
				var myChart = new Chart(ctx, {type: 'line',
					data: {
						labels :label,
						datasets : [
							{
								label: '水位阈值上限',
								fill : false,
								backgroundColor:"#F7464A",
								borderColor : "#F7464A",
								borderDash : [5,5],
								data : data1
							},
							{
								label: '实时水位',
								fill : true,
								fillColor : "rgba(220,220,220,0.5)",
								strokeColor : "rgba(220,220,220,1)",
								pointColor : "rgba(220,220,220,1)",
								pointStrokeColor : "#fff",
								data : data2
							},
							{
								label: '水位阈值下限',
								fill : false,
								backgroundColor:"#5AD3D1",
								borderColor : "#5AD3D1",
								borderDash : [5,5],
								data : data3
							}
						]
					
					},
				    options: {
				    	responsive: false
				    }
				});
				
				$("#datamodal").modal('show');
			}else{
				toastr.error('观测井数据为空');
			}
			
        }
    });
	
	
}
function getarray(dataset){
	var data = new Array();
	$.each(dataset, function(i, item){
		data[i]=item;
	});
	return data;
}
function doAction(page){
	$("#page").val(page);
	find();
}
</script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>
<script src='<c:url value="/resources/js/Chart.min.js"/>'></script>