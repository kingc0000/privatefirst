<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/guanglian-tags.tld" prefix="sm" %>  
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, user-scalable=no,initial-scale=1">
<meta name="renderer" content="webkit|ie-comp|ie-stand" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="description" content=""/>
<meta name="author" content="kekeinfo">
<meta name="keyword" content="kekeinfo">
<meta name="renderer" content="webkit" /> 
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="shortcut icon" href='<c:url value="/resources/img/favicon.ico"/>' type="image/x-icon" />

<title>上海广联环境岩土</title>
<!-- Bootstrap core CSS -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />'
	rel="stylesheet">
<link href='<c:url value="/resources/css/bootstrap-reset.css" />'
	rel="stylesheet">
<!--external css-->
<link
	href='<c:url value="/resources/assets/font-awesome/css/font-awesome.css" />' rel="stylesheet" />



<!-- Custom styles for this template -->
<link href='<c:url value="/resources/css/style.css" />' rel="stylesheet" />
<link href='<c:url value="/resources/css/style-responsive.css" />' rel="stylesheet" />




<!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
<!--[if lt IE 9]>
        <script href='<c:url value="/resources/js/html5shiv.js" />'></script>
        <script href='<c:url value="/resources/js/respond.min.js" />'></script>
        <script type="text/javascript">	
				alert('您的IE浏览器版本太低，请升级到高版本，谢谢！');
				window.location.href='<c:url value="/water/nonsupport.html"/>';
				</script>
        <![endif]-->
<script src='<c:url value="/resources/js/jquery.js" />'></script>
</head>




	<body>

		<div class="navbar-fixed-top text-center" style="background: #5b6e84;color: #fff;padding: 10px 10px;" >
${project.name }
		</div>
		<div class="row" style="padding-top:20px;margin-top:20px;">
			<div class="col-xs-12 col-gl-12">
				<section class="panel">
      					
      			<div class="list-group">
	         		<span class="list-group-item " >项目负责人:${project.projectOwner }</span>
	         		<span class="list-group-item " >联系电话:${project.phone }</span>
	         		<span class="list-group-item " >项目当前状态:
	         			<c:choose>
	         				<c:when test="${csite.status==0}">成井</c:when>
	         				<c:when test="${csite.status==1}">降压运行</c:when>
	         				<c:when test="${csite.status==2}">疏干运行</c:when>
	         				<c:otherwise>结束</c:otherwise>
	         			</c:choose>
	         		</span>
	         		<span class="list-group-item " id="conclud"></span>
	         		<span class="list-group-item " >项目概述:${project.summary }</span>
      			</div>
      			</section>
      			<section class="panel" id="pbody" style="display:none;">
      				<header class="panel-heading" id="phead">
      				</header>
      				<div class="panel-body" >
      					<div class="panel-body">
            		<div class="row">
            			<div class="col-xs-12">
		            		<canvas id="pointsChart_pie" class="chartmbole"  style="width:100%;height:100%;"></canvas>
            			</div>
            			<div class="col-xs-12 ">
		            		<canvas id="pointsChart_line" class="chartmbole" style="width:100%;height:100%;" ></canvas>
            			</div>
            			<!-- <div class="clearfix"></div> -->
            			<div class="col-xs-12">
		            		<canvas id="pointsChart_0" class="chartmbole" style="width:100%;height:100%;" ></canvas>
            			</div>
            			<div class="col-xs-12 ">
		            		<canvas id="pointsChart_1" class="chartmbole" style="width:100%;height:100%;" ></canvas>
            			</div>
            			<div class="col-xs-12 ">
		            		<canvas id="pointsChart_2" class="chartmbole" style="width:100%;height:100%;" ></canvas>
            			</div>
            		</div>
            	</div>
      				</div>
      			</section>
      			<section class="panel" id="imgs" style="display:none;">
      				<header class="panel-heading" id="ihead">
      				</header>
      				<div class="panel-body" id="ibody">
      				</div>
      			</section>
      			
  				
  
		</div>
		</div>
		<footer class=" navbar-fixed-bottom" style="background: #5b6e84;color: #fff;padding: 10px 0;">
			<div class="text-center">
				<span style="font-size:14px;">2016 © 上海<span>广联环境岩土</span>工程股份有限公司</span>
			</div>
		</footer>
	</body>
	<script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
	<script class="include"
		src='<c:url value="/resources/js/jquery.dcjqaccordion.2.7.js" />'></script>
	<script src='<c:url value="/resources/js/jquery.scrollTo.min.js" />'></script>
	<script src='<c:url value="/resources/js/jquery.nicescroll.js" />'></script>
	
	<script src='<c:url value="/resources/js/respond.min.js" />'></script>

	
	 <script src='<c:url value="/resources/js/link-hover.js"/>'></script>
	 <script src='/resources/js/Chart.min.js'></script>
	 <script>
	 var pid='${csite.id}';
	 jQuery(document).ready(function() {
		 var _w = $(window).width();
			var _t = _w-40;
			$(".chartmbole").attr("width", _t);
			$(".chartmbole").attr("height", _t);
			$(".chartmbole").css("width", _t);
			$(".chartmbole").css("height", _t);
			getConclusion();
			 getDrawing();
			showdata();
		
	 });
	//加载工地图纸
	 function getDrawing(){
	 	$.post("imgs.shtml?pid=" + pid, function(imgs) {
	 	    if (imgs.length>0) {
	 	    	var redata = eval(imgs);
		 	    $('#ihead').html('项目图纸');
		 	    var str1 = '';
		 	    for (var i = 0; i < redata.length; i++) {
		 	        str1 += '<div> <img width="100%" alt="person" src="'+redata[i].name+'"></div>';
		 	    }
		 	   $('#ibody').html(str1);
		 	   $("#imgs").show();
	 	    }
	 	});
	 }
	 function showdata(){
			//获取项目对应的观测点数据
			//console.log("当前项目id="+pid);
			var url = 'getCheckpoints.shtml'; 
			$.post(url,{"pid":pid}, function(response){
				var status = response.status;
				
				if (status == 0 || status == 9999) {
					var mheight=$(window).height()*0.5;
					
					var isshow =false;
					//$("canvas").css("height", mheight);
					//处理测点柱状数据
					var checkpoints = response.checkpoints;
					
					$.each(checkpoints, function(i, checkpoint) {
						var chkTitle = checkpoint.chkTitle; //测点类型标题
						var dataName1 = checkpoint.dataName1; //监测数据名称1
						var dataName2 = checkpoint.dataName2;//监测数据名称2
						var data = checkpoint.dataset; //测点监测数据集合
						if(data!=null&&data.length>0) {
							isshow=true;
							var labels=[], data1=[], data2=[];
							
							$.each(data, function(i, item){
								labels[i] = item.name; //测点名称
								data1[i] = item.data1; //测点数据1
								if(dataName2!=null){
									data2[i] = item.data2;//测点数据2
								}
							});
							var data= {
							        labels: labels,
							        datasets: [{
							            label: dataName1,
							            data: data1,
							            backgroundColor: 'rgba(255, 99, 132, 0.5)',
							            borderColor: 'rgba(255,99,132,1)',
							            borderWidth: 1,
							            yAxisID: "y-axis-1"
							        }]
							    };
							if(dataName2!=null){
								data={
								        labels: labels,
								        datasets: [{
								            label: dataName1,
								            data: data1,
								            backgroundColor: 'rgba(255, 99, 132, 0.5)',
								            borderColor: 'rgba(255,99,132,1)',
								            borderWidth: 1,
								            yAxisID: "y-axis-1"
								        },
								        {
								            label: dataName2,
								            data: data2,
								            backgroundColor: 'rgba(54, 162, 235, 0.5)',
								            borderColor: 'rgba(54, 162, 235, 1)',
								            borderWidth: 1,
								            yAxisID: "y-axis-2"
								        }]
								    };
							}
							$("#pointsChart_"+i).parent().show();
							
							var ctx = $("#pointsChart_"+i).get(0).getContext("2d");
							var myChart = new Chart(ctx, {
								type: 'bar',
								data: data,
							    options: {
				                    responsive: false,
							    	title:{
					                    display:true,
					                    text:chkTitle
					                },
					                tooltips: {
					                    mode: 'index',
					                    intersect: true
					                },
							        scales: {
							            yAxes: [{
					                        type: "linear", // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
					                        display: true,
					                        position: "left",
					                        id: "y-axis-1",
					                        ticks: {
							                    beginAtZero:true
							                },
							                scaleLabel: {
				                                display: true,
				                                labelString: dataName1
				                            }
					                    }, {
					                        type: "linear", // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
					                        display: true,
					                        position: "right",
					                        id: "y-axis-2",

					                        // grid line settings
					                        gridLines: {
					                            drawOnChartArea: false, // only want the grid lines for one axis to show up
					                        },
					                        ticks: {
							                    beginAtZero:true
							                },
							                scaleLabel: {
				                                display: true,
				                                labelString: dataName2
				                            }
					                    }]
							        }
							    }
							});
							if(i==0) {
								lineChart0 = myChart;
							} else if(i==1){
								lineChart1 = myChart;
							} else if(i==2) {
								lineChart2 = myChart;
							}
						} else {
							$("#pointsChart_"+i).parent().hide();
						}
					});
					
					//处理测点饼图数据
					if(response.pwellListFlows!=null&&response.pwellListFlows!=0
							||response.iwellListFlows!=null&&response.iwellListFlows!=0) {
						isshow=true;
						$("#pointsChart_pie").parent().show();
						var ctx = $("#pointsChart_pie").get(0).getContext("2d");
						var pwellListFlows = response.pwellListFlows;
						var iwellListFlows = response.iwellListFlows;
						pieChart = new Chart(ctx, {
							type: 'pie',
							data: {
								datasets: [{
									data: [
									       pwellListFlows,
									       iwellListFlows
									       ],
									       backgroundColor: [
									                         "rgba(255,99,132,1)", "rgba(54, 162, 235, 1)"
									                         ],
									                         label: '抽灌比'
								}],
								labels: [
								         "降水井(m³/h)",
								         "回灌井(m³/h)"
								         ]
							},
							options: {
								responsive: false
							}
						});
					} else {
						$("#pointsChart_pie").parent().hide();
					}
					
					//处理观测井曲线图
					var owelllines = response.owelllines;
					var chkTitle = owelllines.chkTitle; //测点类型标题
					var dataName1 = owelllines.dataName1; //监测数据名称1
					var dataName2 = owelllines.dataName2;//监测数据名称2
					var dataName3 = owelllines.dataName3;//监测数据名称3
					var data = owelllines.dataset; //测点监测数据集合
					if(data!=null&&data.length>0) {
						var labels=[], data1=[], data2=[], data3=[];
						isshow=true;
						$.each(data, function(i, item){
							labels[i] = item.name; //测点名称
							data1[i] = item.data1; //测点数据1
							data2[i] = item.data2;//测点数据2
							data3[i] = item.data3;//测点数据3
						});
						var owelldatas={
						        labels: labels,
						        datasets: [{
						            label: dataName1,
						            tension: 0,
						            data: data1,
						            backgroundColor: 'rgba(255, 159, 64, 0.5)',
						            borderColor: 'rgb(255, 159, 64)',
						            fill: false
						        },
						        {
						            label: dataName2,
						            tension: 0,
						            data: data2,
						            backgroundColor: 'rgba(54, 162, 235, 0.5)',
						            borderColor: 'rgb(54, 162, 235)',
						            fill: false
						        },
						        {
						            label: dataName3,
						            tension: 0,
						            data: data3,
						            backgroundColor: 'rgba(75, 192, 192, 0.5)',
						            borderColor: 'rgb(75, 192, 192)',
						            fill: false
						        }]
						    };
						$("#pointsChart_line").parent().show();
						var ctx = $("#pointsChart_line").get(0).getContext("2d");
						owellLines = new Chart(ctx, {
							type: 'line',
							data: owelldatas,
						    options: {
						    	responsive: false,
						    	title:{
				                    display:true,
				                    text:chkTitle
				                },
				                tooltips: {
				                    mode: 'index',
				                    intersect: true
				                },
						        scales: {
						            yAxes: [{
				                        type: "linear", // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
				                        display: true,
				                        position: "left",
				                        ticks: {
						                    beginAtZero:true
						                },
						                scaleLabel: {
			                                display: true,
			                                labelString: dataName1
			                            }
				                    }]
						        }
						    }
						});
					} else {
						$("#pointsChart_line").parent().hide();
					}
					var t=setTimeout(function(){
						if(isshow==true){
							$("#phead").html("图表数据");
							$("#pbody").show();
						}
						
					},100);
					
				} 
			}).error(function() {});
		}
	 
	 function getConclusion(){
			$.post("conclusion.shtml?pid=" + pid, function(response) {
				var status = response.status;
				if (status == 0) {
					$("#conclud").html(response.conclusion);
				} else{
					$("#conclud").html("每日信息：无");
				}
			});
		}
	 </script>
	
</html>
