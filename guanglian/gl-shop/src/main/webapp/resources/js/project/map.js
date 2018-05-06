//地图相关
var map;
var marker;
//加载地图控件
$(window).load(function(){
	$('#loading').show();
	loadMap();
})
	
var mapfine=false;
function loadMap(){
	var script = document.createElement("script");
	script.type = "text/javascript";
	script.src = "http://api.map.baidu.com/api?v=2.0&ak=UF2AIPvxyB3OMgat4izp88A4DWUjiuuD&callback=init";
	document.body.appendChild(script);
	setTimeout(function () { 
		if(mapfine==false){
			//$('#loading').hide();
			toastr.error("网络信号不太好，努力加载中，请稍后");
			//$('#loading').show();
		}
    }, 3000);
	setTimeout(function () { 
		if(mapfine==false){
			$('#loading').hide();
			toastr.error("地图资源加载失败请重试");
		}
    }, 10000);
}	

function init() {
	//设置地图宽度，头尾都减掉
	$("#middle-content").height($(window).height()-120);
	// 创建Map实例
	 map = new BMap.Map("projectmap", {enableMapClick:false});
	 var spoint = new BMap.Point(121.475063,31.233094);
	 var mapType1 = new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});
		//var mapType2 = new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_LEFT});
	map.centerAndZoom(spoint, 15); 
	map.enableScrollWheelZoom(true);
	map.disableDoubleClickZoom(true);
	map.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]}));          //左上角，默认地图控件
	//map.addControl(mapType2);
	//map.addControl(overViewOpen = new BMap.OverviewMapControl({isOpen:true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT}));          //添加默认缩略地图控件
	map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT, type: BMAP_NAVIGATION_CONTROL_LARGE}));
	//map.removeControl(mapType1)
	//$("#main-content").width($(window).width()-220);
	//手机版支持全屏
	if($(window).width()<768){
//		$("#projectmap").css("height","100%");
	       $("#middle-content").height($(window).height()-120);
		map.addEventListener("click",function(e){
			if(e.overlay!=null){
				return;
			}
			if($("header").is(":hidden")){
			       $("header").show();  
			       $("#footerb").css("padding-bottom","3px");
			       $("#footerb").css("padding-top","7px");
			       $("#footerli").show();//
			       $("#middle-content").height($(window).height()-120);
			       $(".wrapper").css("margin-top","60px");
			       $(".wrapper").css("padding","15px");
			       $("#dwarning").css("margin-top","-20px");
			       
			}else{
			      $("header").hide();     //全屏
			      $("#footerli").hide();
			      $("#footerb").css("padding","0");
			      $("#middle-content").height($(window).height());
			      $(".wrapper").css("margin-top","0");
			      $(".wrapper").css("padding","0");
			      $("#dwarning").css("margin-top","-10px");
			}
		});
	}
	//地图加载完后在初始
	map.addEventListener("tilesloaded",setProjectMarker);
	
} 
//重新设置modal大小
$(window).resize(function() {
	if($(window).width()>767) {
		$("#middle-content").height($(window).height()-120);
	}
	//$("#main-content").width($(window).width()-220);
});

