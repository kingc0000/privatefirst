<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<link href='<c:url value="/resources/data-tables/DT_bootstrap.css" />' rel="stylesheet">
<link href='<c:url value="/resources/data-tables/DT_table.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/table-response.css" />' rel="stylesheet">
<link href='<c:url value="/resources/css/fileicon.css" />' rel="stylesheet">
<script type="text/javascript">
	
		$('#loading').show();
</script>
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
                   		<jsp:include page="/pages/water/csite/project.jsp" />
                           <c:if test="${hasRight==true}">
                           <div class="form-group">
								<div class="col-lg-2 col-sm-2"></div>
								<div class="col-lg-10">
									<button type="button" onclick="wellssae()" class="btn btn-success">提交</button>
								</div>
							</div>
						</c:if>	
                  </div>
                  </section>
              </div>
          </div>
<div class="row">
  <div class="col-md-12">
	<section class="panel">
	<header class="panel-heading">地下水项目列表
		<c:if test="${hasRight==true}">
			<button type="button"  class="btn btn-theme pull-right fa fa-plus-square hidden-xs" onclick="changeEdittable(setpid)">新增</button>
		</c:if>
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
	                     <th>项目名称</th>
	                     <th>项目负责人</th>
	                     <th>联系电话</th>
	                     <th>省份</th>
	                     <th>城市</th>
	                     <th>地址</th>
	                     <th>状态</th>
	                     <th>备注</th>
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
  <jsp:include page="/common/getPointFromMap.jsp" />
<script src='<c:url value="/resources/js/jquery.validate.min.js" />'></script>
<script src='<c:url value="/resources/data-tables/jquery.dataTables.js" />'></script>
<script src='<c:url value="/resources/data-tables/DT_bootstrap.js" />'></script>
<script src='<c:url value="/resources/data-tables/fnReloadAjax.js" />'></script>
<script src='<c:url value="/resources/assets/bootstrap-fileupload/bootstrap-fileupload.js" />'></script> 
<script src='<c:url value="/resources/assets/ckeditor/ckeditor.js" />'></script>
<script>
	var sDom = "lf<c:if test="${hasRight==true}"><'#del_btn'><'#printerer'></c:if>rtip";
	jQuery(document).ready(function() {
		$('#pictitle').html('项目管理');
		
		//$('#edittable').hide();
		$('#slist').dataTable( {
			"bProcessing": true,
			"bServerSide": true,
			"bLengthChange": true,
			"bFilter": true,
			"bSortClasses": false,
			"sAjaxSource": "server_processing.shtml",
			"fnServerData" : retrieveGoods,
			"sDom": sDom,
			"oLanguage": {
				"oPaginate":opaging
				},
			"aoColumns": [
		                    { "mData": "id"},
		                    { "mData": "project.name"},
		                    { "mData": "project.projectOwner"},
		                    { "mData": "project.phone"},
		                    { "mData": "project.zone.name"},
		                    { "mData": "project.city"},
		                    { "mData": "project.address" },
		                    { "mData": "status" },
		                    { "mData": "project.memo"},
		                    { "mData": "id" }

		                ],
                "aoColumnDefs": [
                             {"aTargets": [0],  "mRender":function(data,type,full){
                              	return '<input type="checkbox" value="'+data+'" class="sidCheckbox"/>';	
                              },"bSortable": false
                              },
                              {"aTargets": [1],  "mRender":function(data,type,full){
                            	  if(full.sstatus==1) {
                            		  var href = '<c:url value="/water/csite/wlist.html?pid='+full.id+'"/>';
                            		  return '<a href="'+href+'">'+data+'</a>';
                            	  } else {
                            		  return data;
                            	  }
                                },"bSortable": false
                                },
                              {"aTargets": [7],  "mRender":function(data,type,full){
                                	if(data==0){
                                		return "成井";
                                	}else if(data==1){
                                		return "降压运行";
                                	}else if(data==2){
                                		return "疏干运行";
                                	} else if(data==-1){
                                		return "结束";
                                	}
                                }
                               },
                             {"aTargets":[9],"mRender":function(data,type,full){
                            	 var reStr="";
                            	 if(full.sstatus==1)	{
                            		 reStr +='<a class="edit btn hasfn btn-primary btn-xs tooltips" title="修改" data-placement="left" data-container="body" data-original-title="修改"  href="javascript:;" ><i  class="fa fa-pencil"></i></a>&nbsp;'
                         	 			+'<a href="javascript:;" class="btn btn-danger btn-xs tooltips" title="删除" data-placement="left" data-container="body" data-original-title="删除" onclick="deldata('+data+')"><i  class="fa fa-trash-o"></i></a>&nbsp;';
                         	 			//项目状态
                         	 			if(full.status==-1){
                         	 				reStr +='<a href="javascript:;" class="btn btn-success btn-xs tooltips" title="项目开工" data-placement="left" data-container="body" data-original-title="项目开工" onclick="doaction('+data+',&apos;active.shtml&apos;,&apos;修改该项目状态为开工&apos;)"><i  class="fa fa-play"></i></a>';
                         	 			}else if(full.status==0){
                         	 				reStr +='<a href="javascript:;" class="btn btn-success btn-xs tooltips" title="项目结束" data-placement="left" data-container="body" data-original-title="项目结束" onclick="doaction('+data+',&apos;active.shtml&apos;,&apos;修改该项目状态为结束&apos;)"><i  class="fa fa-stop"></i></a>';
                         	 			}
                                } 	
                            	 	return reStr;
                             	},
                             	"sWidth":"110px","bSortable": false
                            },
                            {
                            	"aTargets":["_all"],"bSortable": false
                            }
                         ] ,
                         "fnInitComplete": function(settings, json) {
                        	 if(isTouchDevice()===false) {
                        		 $('.tooltips').tooltip();
                        		}
                        	 definedSidCheckbox();
                        	 this.fnSetColumnVis(9,settings.hasRight);
                        	 if(settings.hasRight) {
	                        	 $("#del_btn").hide();
                        	 }
                        	 $('#loading').hide();
                         }
			
		} 
		);
		//增加打印按钮
		$("#printerer").append('<button type="button" class="btn delete_btn hidden-xs"><i class="fa fa-barcode" style="color:red"></i>打印二维码</button>');
		$("#printerer").addClass("dt_btn pull-right");
		$("#printerer").on('click',function(){
			erprint();
		});
		//设置地图宽度
		$("#mag-dialog").height($(window).height()*0.8);
		$("#mag-dialog").width($(window).width()*0.8);
		
		//自动添加经纬度
		$('#address').blur(function(){
			autoSetPoint();
		});
		
		CKEDITOR.replace(  'summary',
		{
			filebrowserImageBrowseUrl :  '<c:url value="/admin/content/fileBrowser.html"/>',
			filebrowserImageUploadUrl :  '<c:url value="/admin/content/image/upload.shtml"/>'
		});
		
	});
	
	function erprint(){
		var ids = new Array();
		$('.sidCheckbox:checked').each(function(i,o){
			var nRow = $(o).parents('tr')[0];
			var aData = oTable.fnGetData(nRow);
			ids[i]=$(o).val();
			
		});
		if (ids.length<1) {
			alert("请至少选择一个需要打印二维码的数据");
			return;
		}
		window.open('<c:url value="/water/csite/print.html"/>'+'?ids='+ids, "_blank");		
	}
	
	
	function setpid(){
		$("input[id='status']").val(0);
		//$("select[id='rank']").val(1);
		$("select").prop("selectedIndex", 0);
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
	}
	
	function retrieveGoods(sSource, aoData, fnCallback) {
		$('#loading').show();
		var data = {
			"aoData" : JSON.stringify(aoData)
		};
		if (!jQuery.isEmptyObject(filterjson)) {
			data = $.extend({}, {
				"aoData" : JSON.stringify(aoData)
			}, filterjson);
		}
		$.ajax({
			type : "POST",
			url : "server_processing.shtml",
			dataType : "json",
			data : data,
			success : function(data) {
				fnCallback(data); // 服务器端返回的对象的returnObject部分是要求的格式
				oTable.fnSetColumnVis(9,data.hasRight);
				$('#loading').hide();
			},
			error : function(data, xhr, textStatus, errorThrown) {
				console.log(data.responseText);
				fnCallback(data);
				$('#loading').hide();
			}
		});
	}
	function setUsers(did,filed,users){
		var selec='';
		for(var i=0;i<users.length;i++){
				selec+='<input name="'+filed+'" checked="checked" type="checkbox" title="'+users[i].id+'" value="'+users[i].id+'"><a class="menulink" href="javascript:void(0);" onclick="setCheckbox(&quot;'+filed+'&quot;,'+users[i].id+')">&nbsp;'+users[i].name+'</a>'
			
		}
		
		$("#"+did).html(selec);
	}
	function dolistafterfn(oTable, nRow){
		//清空图像id,删除之前添加的图像
		$('#delids').val('');
		$(".imgItem").remove();
		var aData = oTable.fnGetData(nRow);
		//异步加载图片
		$('#loading').show();
		$.ajax({
			type : "POST",
			url : "images.shtml",
			dataType : "json",
			data : "cid="+aData["id"],
			success : function(data) {
				$('#loading').hide();
				if(data!="-1"){
					if(data!="0" && data!=null){
						//图片
						if(data.img!=null){
							var fname= data.img;
							if(fname!=null && fname.length >0){
								$.each(fname,function(index,img){
									addImage(img.name,img.jpeg,img.id,"PRODUCT_DIGITAL");
								});
							}
						}
						//cuser
						if(data.cusers!=null){
							setUsers("cdiv","cusers",data.cusers);
						}
					}
				}else{
					toastr.error("项目图纸或者评论负责人或者告警负责人加载失败，请重试");
				}
			},
			error : function(data, xhr, textStatus, errorThrown) {
				console.log(data.responseText);
				//fnCallback(data);
				toastr.error("项目图纸加载失败，请重试");
				$('#loading').hide();
			}
		});
		
		var pbase=aData["pbase"];
		var jconfined=pbase.confined;
		if(jconfined!=null && jconfined.length>0){
			jconfined=jconfined.split(",");
			$.each(jconfined,function(index,con){
				setCheckbox('pbase.confined',con);
			});
		}
		
		var jlayer=pbase.layer;
		if(jlayer!=null && jlayer.length>0){
			jlayer=jlayer.split(",");
			$.each(jlayer,function(index,con){
				setCheckbox('pbase.layer',con);
			});
		}
		var jprecipitation=pbase.precipitation;
		$('#pprecipitation').html('');
		if(jprecipitation!=null && jprecipitation.length>0){
			var phtml='';
			jprecipitation=jprecipitation.split(",");
			$.each(jprecipitation,function(index,con){
				//$.each(data,function(index,pre){
					//if(pre.value==con){
						phtml+='<span class="checkboxs-inline" onclick="setCheckbox(&apos;pbase.precipitation&apos;,&apos;'+con+'&apos;)">'
						+'<input type="checkbox" id="precipitation" name="pbase.precipitation" title="'+con+'"'
						+ 'onclick="setCheckbox(&apos;pbase.precipitation&apos;,&apos;'+con+'&apos;)" checked="checked"> '
						+'&nbsp;'+con
						+'</span>';
					//}
					
				//})
			})
			$('#pprecipitation').html(phtml);
		}
		var summry =pbase.summary;
		CKEDITOR.instances['summary'].setData(summry);
		
	}

	</script>
<script src='<c:url value="/resources/js/project/list.js" />'></script>

 