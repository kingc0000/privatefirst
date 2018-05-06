<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<link href='<c:url value="/resources/css/jquery.steps.css" />' rel="stylesheet">
	<div class="modal fade" id="point-map-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
      <div class="modal-dialog mag-dialog"  >
        <div class="modal-content" style="height:100%;width:100%">
            <div class="modal-header">
              <h4 class="modal-title hidden-xs" style="display : inline">获取经纬度</h4>
              <button class="close " data-dismiss="modal" type="button">&times;</button>
              <button class="pull-right btn btn-success" id="btn_copy" style="margin:0px 5px;padding:2px;" type="button" >确定</button>
              <button class="pull-right btn btn-success" id="btn_latlnt" style="margin:0px 5px;padding:2px;" type="button" onclick="setlatlnt()">经纬度</button>
              <button class="pull-right btn btn-success" id="btn_polygon" style="margin:0px 5px;padding:2px;" type="button" onclick="drawPolygon()">绘图</button>
              <span class="pull-right">当前经度:<label id="clng"></label>当前维度<label id="clat" style="margin-left:10px;"></label></span>
              <span class="pull-right">当前经度:<label id="clng"></label>当前维度<label id="clat" style="margin-left:10px;"></label></span>
              <button class="pull-right btn btn-success" id="btn_search" style="margin:0px 5px;padding:2px;display:none" type="button" onclick="searchl()">搜索</button>
              <span class="pull-right" id="span_search" style="display:none"><input type="text" style="color:#000000" id="wellname" placeholder="请输入井号"></span>
            </div>
            <div class="modal-body"  id="allmap" style="height:100%;width:100%" >
            </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="auto-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
              <button class="close" data-dismiss="modal" type="button">&times;</button>
              <h4 class="modal-title" id="etitle">开启</h4>
            </div>
            <div class="modal-body">
              <form  id="auto-form" class="form-horizontal" method="POST">
              	<div>请选择开启模式</div>
              	<div class="radio">
                      <label>
                          <input type="radio" name="autoStatus" id="autoStatus" value="0" checked="checked" >
                          人工开启
                      </label>
                       <label>
                          <input type="radio" name="autoStatus" id="autoStatus" value="1"  >
                          根据水位自动开启
                      </label>
                       <label>
                          <input type="radio" name="autoStatus" id="autoStatus" value="2"  >
                          定时自动开启
                      </label>
                  </div>
				<br/>
				<input type="hidden"  name="autolistId" id="autolistId"/>
                    
              </form>
              	<div class="form-group">
					<div class="col-lg-2 col-sm-2"></div>
					<div class="col-lg-10">
						<button type="button" onclick="doautoopen()" class="btn btn-success pull-right">确定</button>
					</div>
				</div>			
              <br>
	              	<hr  style="margin-top:10px">
            </div>
        </div>
      </div>
    </div>
    <script>
    var isLatLng=true;
    var markers=[];
    var points=[];
    var styleOptions = {
    		strokeColor:"#0099cc",    //边线颜色。
            fillColor:"#ccccff",      //填充颜色。当参数为空时，圆形将没有填充效果。
            strokeWeight: 1,       //边线的宽度，以像素为单位。
            strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
            fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
            strokeStyle: 'solid' //边线的样式，solid或dashed。
        }
    jQuery(document).ready(function() {
    	
    	var form = $("#storeform");
    	if(form.find('h3').length>0){
    		form.children("div").steps({
        	    headerTag: "h3",
        	    bodyTag: "section",
        	    transitionEffect: "slideLeft",
        	    enableFinishButton:false,
        	    onStepChanging: function (event, currentIndex, newIndex) {
        	    	
        	    	form.validate({
        	    		ignore: ":disabled,:hidden"
        	    	});
        	    	 var result= form.valid();
        	    	 if (result==false) return false;
        	    	
        	    	 return true;
        	    },
        	    onFinishing: function (event, currentIndex) {
        	    	form.validate({
        	    		ignore: ":disabled,:hidden"
        	    	});
        	    	
        	        return form.valid();
        	    },
        	    onFinished: function (event, currentIndex) {
        	    	if(hasright){
        	    		jssubmit();
        	    	}else{
        	    		alert("没有权限！！！")
        	    	}
        	    	
                }
        	});
    	}
    	
    	

    });
  //地图相关
	var map;
	var marker;
	var scrotop;
	var mapfined=false;
	var mapinit=false;
    
	var myGeo;
	var mpoint =null;
    	function copypoit(tp){
    		if($("#clng").html()=="" || $("#clat").html()==""){
    			alert("经纬度为空不能复制！");
    			return;
    		}
    		if(tp!=null){
    			if(points!=null && points.length>0){
    				var rail="";
    				$.each(points,function(index,point){
    					rail+=point.lng+":";
    					rail+=point.lat+",";
    				});
    				$("#rail").val(rail);
    			}
    			if(!isLatLng){
    				map.removeEventListener('dblclick', dblclickAction);
                    map.removeEventListener('mousedown', startAction);
                    if(isBinded){
                    	isBinded = false;
                    	map.removeEventListener('mousemove', mousemoveAction);
                    }
                    isLatLng=true;
        		}
    		}
    		map.removeEventListener("click",setlnglat);
    		
    		
    		$('#longitude').val($("#clng").html());
			$('#latitude').val($("#clat").html());
			$('#point-map-modal').modal('hide');
    	}
    	
    	
    	function searchl(){
    		var wname =$("#wellname").val();
    		if(wname=="" ||wname==null){
    			toastr.error("请输入井号，谢谢！");
    			return;
    		}else{
    			if(markers!=null && markers.length>0){
    				var found =false;
    				for(var i=0;i<markers.length;i++){
    					if(markers[i].name==wname){
    						markers[i].setAnimation(BMAP_ANIMATION_BOUNCE);
    						found=true;
    						map.panTo(markers[i].point); 
    					}else {
    						markers[i].setAnimation(null);
    					}
    				}
    				if(!found){
    					toastr.error("没有查找到该井");
    				}
    				
    			}
    		}
    	}
    		
    	
    	function loadMap(){
    		if(!mapinit){
    			var script = document.createElement("script");
        		script.type = "text/javascript";
        		script.src = "http://api.map.baidu.com/api?v=2.0&ak=UF2AIPvxyB3OMgat4izp88A4DWUjiuuD&callback=init";
        		document.body.appendChild(script);
    		}
    		
    	}	
    	function drawPolygon(){
    		if(isLatLng){
    			isBinded=false;
        		map.removeEventListener("click", setlnglat);
        		//绘图
    			map.addEventListener('mousedown', startAction);

    		    //双击停止
    		    map.addEventListener('dblclick', dblclickAction);
    		    isLatLng=false;
    		}
    		
		    
		    
    	}
    	
    	function setlatlnt(){
    		if(!isLatLng){
    			map.addEventListener("click", setlnglat);
                map.removeEventListener('dblclick', dblclickAction);
                map.removeEventListener('mousedown', startAction);
                if(isBinded){
                	isBinded = false; 
                	map.removeEventListener('mousemove', mousemoveAction);
                }
                isLatLng=true;
    		}
    		
    	}
    	function init() {
    		mapinit=true;
    		// 创建Map实例
    			myGeo = new BMap.Geocoder();
    			map = new BMap.Map("allmap", {enableMapClick:false}); 
    			map.centerAndZoom("上海",12); 
    			map.enableScrollWheelZoom(true);
    			map.disableDoubleClickZoom(true);
    			map.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]}));          //2D图，卫星图
    			//map.addControl(new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_LEFT}));          //左上角，默认地图控件
    			//map.addControl(new BMap.OverviewMapControl({isOpen:true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT}));
    			map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT, type: BMAP_NAVIGATION_CONTROL_LARGE}));
    			map.addEventListener("tilesloaded",function(){mapfined=true
    				});
    			
    		}  
    	$('#point-map-modal').on('shown.bs.modal', function () {
    			if(mpoint!=null){
    				map.panTo(mpoint);
    			}else{
    				var zname="" ;
    				if($("input[id='zone.name']").length>0){
    					zname=$("input[id='zone.name']").val();
    				}else{
    					zname=zone;
    				}
    				
    				map.setCenter(zname);
    			}
    		})
    	$('#point-map-modal').on('hidden.bs.modal', function () {
    		//清空坐标
    		mpoint=null;
    		if(scrotop!=null){
    			$(window).scrollTop(scrotop);
    			scrotop=null;
    		}
    		if(isLatLng){
    			map.removeEventListener("click",setlnglat);
    		}else{
    			map.removeEventListener('dblclick', dblclickAction);
                map.removeEventListener('mousedown', startAction);
                if(isBinded){
                	isBinded = false;
                	map.removeEventListener('mousemove', mousemoveAction);
                }
                isLatLng=true;
    		}
    		
    		
    	})	
    	//重新设置modal大小
    	$(window).resize(function() {
    		resetmodal();
    	});
    	function resetmodal(){
    		if($(window).width()<768){
    			$(".mag-dialog").width($(window).width()-20);
    			$(".mag-dialog").height($(window).height()-140);
    		}else{
    			$(".mag-dialog").width($(window).width()*0.8);
    			$(".mag-dialog").height($(window).height()*0.8);
    		}
    	}
    	function setPointmap(address,zonename){
    		myGeo.getPoint(address, function(point){
    			if (point) {
    				$('#longitude').val(point.lng);
    				$('#latitude').val(point.lat);
    			}else{
    				toastr.error("获取经纬度失败，请通过地图获取，谢谢！");
    			}
    			$('#loading').hide();
    		}, zonename);
    		$('#loading').hide();
    	}
    	//手工设置
    	function setPoint(address,zonename){
    		$('#loading').show();
    		if(mapfined==false){
    			loadMap();
    			setTimeout(function () { 
    				if(mapfined==false){
    	    			toastr.error("获取地图资源失败！请稍后重新加载！！！");
    	    			return;
    	    		}
    				setPointmap(address,zonename);
    		    }, 2000);
    		}else{
    			setPointmap(address,zonename)
    		}
    		
    		
    	}
    	function setwells(datas,index){
    		if(datas!=null){
    			//console.log(datas);
    			$.each(datas, function(i, data) {
    				var point = new BMap.Point(data.longitude,data.latitude);
    				var myIcon = new BMap.Icon('<c:url value="/resources/img/water/checks.png"/>', new BMap.Size(30,23), {imageOffset:new BMap.Size(0,index)});
    				var marker = new BMap.Marker(point,{icon:myIcon}); 
    				marker.name=data.name;
    				markers[markers.length]=marker;
    				map.addOverlay(marker);
    			});
    	}
    	}
    	function getFromllmap(lng,lat,cid){
    		scrotop=$(window).scrollTop();
    		$('#loading').show();
    		$(window).scrollTop(0);
    		var point = new BMap.Point(lng,lat);
    		if(point || !typeof(point) == "undefined"){
    			//清除所有的标注
        		map.clearOverlays();
    			$("#clng").html(point.lng);
    			$("#clat").html(point.lat);
    			mpoint = point;
    			var pmarker = new BMap.Marker(point);  // 创建标注
    			map.addOverlay(pmarker);  
    			pmarker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
        		map.setZoom(18);
    			if(cid!=null){
    				$.ajax({
            			type : "POST",
            			url : _context+"/water/csite/getWellData.shtml",
            			data : "cid=" + cid,
            			success : function(datas) {
            				if(datas!=null && datas!=""){
            					markers.splice(0,markers.length);
            					scrotop=$(window).scrollTop();
            					$(window).scrollTop(0);
            					setwells(datas.pwell,0);
            					setwells(datas.dewell,-92);
            					setwells(datas.owell,-23);
            					setwells(datas.iwell,-46);
            					setwells(datas.ewell,-69);
            					drawraill(datas.rail);
            					$("#btn_latlnt").hide();
            	    			$("#btn_polygon").hide();
            	    			$('#point-map-modal').modal('show');
            	        		//单击获取点击的经纬度
            	        		map.addEventListener("click",function(e){
            	        			$("#clng").html(e.point.lng);
            	        			$("#clat").html(e.point.lat);
            	        			map.removeOverlay(marker);
            	        			marker = new BMap.Marker(e.point);  // 创建标注
            	        			map.addOverlay(marker);               // 将标注添加到地图中
            	        			//marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
            	        		});
            	        		$("#btn_copy").attr("onclick","copypoit()");
            	        		//显示输入框
            	        		$("#span_search").show();
            	        		$("#btn_search").show();
            	        		searchid=datas;
            				}else{
            					toastr.error('获取项目经纬度失败！');
            				}
            				$('#loading').hide();
            				
            			},
            			error : function(xhr, textStatus, errorThrown) {
            				$('#loading').hide();
            				toastr.error('获取项目经纬度失败！');
            			}
            		});
    			}else{
    				//加载已有的围栏
    	    		var rails = $("#rail").val();
    	    		drawraill(rails);
    				if($(window).width()>767){
    					$("#btn_latlnt").show();
    	    			$("#btn_polygon").show();
    				}else{
    					$("#btn_latlnt").hide();
    	    			$("#btn_polygon").hide();
    				}
    				$("#btn_copy").attr("onclick","copypoit(1)");
    			}
    			
    			
    			//单击获取点击的经纬度
        		map.addEventListener("click",setlnglat);
    			$('#loading').hide();
    			$('#point-map-modal').modal('show');
    		}else {
    			toastr.error('获取项目经纬度失败！');
    		}
    	}
    	
    	function drawraill(rails){
    		if(rails!=null && rails!=""){
    			var arails=rails.split(",");
    			 if(points.length>0){
    				points.splice(0,points.length);
    			}
    			
    			$.each(arails,function(index,mpoints){
					var latlnts=mpoints.split(":");
					var point=	new BMap.Point(latlnts[0],latlnts[1]);
					points.push(point);
				});
    			
    			oPolygon = new BMap.Polygon(points, styleOptions);
    			map.addOverlay(oPolygon);
    			
    		}
    	}
    	function getFromll(lng,lat,cid){
    		$('#loading').show();
    		if(mapfined==false){
    			loadMap();
    			setTimeout(function () { 
    				if(mapfined==false){
    	    			toastr.error("获取地图资源失败！请稍后重新加载！！！");
    	    			$('#loading').hide();
    	    			return;
    	    		}
    				getFromllmap(lng,lat,cid);
    		    }, 2000);
    		}else{
    			getFromllmap(lng,lat,cid);
    		}
    		
    		
    	}
    	function getFromPointmap(city,address,zonename,zoomsize){
    		scrotop=$(window).scrollTop();
    		$('#loading').show();
    		$(window).scrollTop(0);
    		//清除所有的标注
    		map.clearOverlays();
    		myGeo.getPoint(city+address, function(point){
    			if(typeof(zoomsize)!="undefined" ||zoomsize!=null){
        			map.setZoom(zoomsize);
        		}else{
        			map.setZoom(12);
        		}
    		if (point) {
    			$("#clng").html(point.lng);
    			$("#clat").html(point.lat);
    			mpoint = point;
    			var pmarker = new BMap.Marker(point);  // 创建标注
    			map.addOverlay(pmarker);               // 将标注添加到地图中
    			//map.setCurrentCity(city);
    			pmarker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
    		}else{
    			mpoint=null;
    		}
    		//加载已有的围栏
    		var rails = $("#rail").val();
    		drawraill(rails);
    		if($(window).width()>767){
        		$('#btn_polygon').show();
        		$("#btn_latlnt").show();
    		}else{
    			$("#btn_latlnt").hide();
    			$("#btn_polygon").hide();
    		}
    		$("#btn_copy").attr("onclick","copypoit(1)");
    		$('#point-map-modal').modal('show');
    		//单击获取点击的经纬度
    		map.addEventListener("click",setlnglat);
    			$('#loading').hide();
    		}, zonename);
    	}
    	function getFromPoint(city,address,zonename,zoomsize){
    		if(city=="" || city==null){
    			toastr.error('城市不能为空！');
    			return ;
    		}
    		
    		if(mapfined==false){
    			loadMap();
    			setTimeout(function () { 
    				if(mapfined==false){
    	    			toastr.error("获取地图资源失败！请稍后重新加载！！！");
    	    			$('#loading').hide();
    	    			return;
    	    		}
    				getFromPointmap(city,address,zonename,zoomsize);
    		    }, 2000);
    		}else{
    			getFromPointmap(city,address,zonename,zoomsize);
    		}
    	}
    	var setlnglat = function (e) {
    		$("#clng").html(e.point.lng);
			$("#clat").html(e.point.lat);
			//清除所有的标注
			map.removeOverlay(marker);
			marker = new BMap.Marker(e.point);  // 创建标注
			map.addOverlay(marker);               // 将标注添加到地图中
    	}
    	function wellssae(){
    		var url="save.shtml" ;
    		
    		/**有checkbox的*/
    		var obj1 = $("#storeform input:checked");
    		$(obj1).each(function(i,o) {
    			$(o).val($(o).attr('title'));
    				});
    		
    		/**有富文本框的*/
    		//$("#summary").val(CKEDITOR.instances['summary'].getData());
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
    				} else if(status==-4){
    					toastr.error(response.response.statusMessage);
    				}else{
    					toastr.error('提交失败！');
    				}
    				window.location.reload();
    			},
    			error : function(xhr, textStatus, errorThrown) {
    				$('#loading').hide();
    				alert('error ' + errorThrown);
    			}
    		});
    		return false;
    	}
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
    	//加载地图控件
    	$(window).load(function(){
    		//loadMap();
    		resetmodal();
    	})
    
   function doauto(pid){
    		$("#autolistId").val(pid);
    		$("#auto-modal").modal('show');
    	} 	
  function doautoopen(){
	$('#loading').show();  
	$.ajax({
		type : "POST",
		url : "active.shtml",
		data : "listId=" + $("#autolistId").val()+"&astatus="+$('input:radio:checked').val(),
		success : function(response) {

			var status = response.response.status;
			$('#loading').hide();
			if (status == 0 || status == 9999) {
				$("#auto-modal").modal('hide');
				refreshData();
				toastr.success(response.response.statusMessage);
			} else {
				toastr.error(response.response.statusMessage);
			}
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			toastr.error(con+'失败');
		}
	});
}
    </script>
    <script src='<c:url value="/resources/js/jquery.steps.min.js" />'></script>
<script src='<c:url value="/resources/js/jquery.stepy.js" />'></script>
<jsp:include page="/resources/js/project/modalpage.jsp" />
<script src='<c:url value="/resources/js/project/modalpage.js" />'></script>