var lineChart;
var pieChart;
var lineChart0, lineChart1, lineChart2, owellLines;
$(function(){
	//加载对时间曲线图自适应处理
	$("#commonmodal").on('shown.bs.modal', function(){
//		if(lineChart!=null&&lineChart.chart.canvas!=null) {
//			lineChart.resize();
//		}
	});
	$("#commonmodal, #datamodal").on('hidden.bs.modal', function(){
		if(lineChart!=null) {
			lineChart.destroy();
		}
		
		if(pieChart!=null) {
			pieChart.destroy();
		}
		if(lineChart0!=null) {
			lineChart0.destroy();
		}
		if(lineChart1!=null) {
			lineChart1.destroy();
		}
		if(lineChart2!=null) {
			lineChart2.destroy();
		}
		if(owellLines!=null) {
			owellLines.destroy();
		}
		
	});
});
/**
 * 实时监控页面，显示降水井和回灌井的流量比饼图、当前数据柱状图
 */
function showdata(url){
	//获取项目对应的观测点数据
	//console.log("当前项目id="+pid);
	if(url==null || url == '') url = 'getCheckpoints.shtml'; 
	$('#loading').show();
	$.post(url,{"pid":pid}, function(response){
		var status = response.status;
		
		if (status == 0 || status == 9999) {
			var mheight=$(window).height()*0.5;
			//出来model内容
			if ($(window).width()>768) {
				$("#datamodal .modal-dialog").css("width", "90%");
			}
			
			//$("canvas").css("height", mheight);
			//处理测点柱状数据
			var checkpoints = response.checkpoints;
			
			$.each(checkpoints, function(i, checkpoint) {
				var chkTitle = checkpoint.chkTitle; //测点类型标题
				var dataName1 = checkpoint.dataName1; //监测数据名称1
				var dataName2 = checkpoint.dataName2;//监测数据名称2
				var data = checkpoint.dataset; //测点监测数据集合
				if(data!=null&&data.length>0) {
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
					var _w = $(window).width();
					var _t = _w;
					if(_w<768){
						_t = _w-40;
//						_responsive = false;
					} else {
						_t = _w/3;
					}
					$("#pointsChart_"+i).parent().show();
					$("#pointsChart_"+i).attr("width", _t);
					$("#pointsChart_"+i).attr("height", _t);
					$("#pointsChart_"+i).css("width", _t);
					$("#pointsChart_"+i).css("height", _t);
					
					var ctx = $("#pointsChart_"+i).get(0).getContext("2d");
					var myChart = new Chart(ctx, {
						type: 'bar',
						data: data,
					    options: {
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
						responsive: true
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
				
//				var _p_w=$("#pointsChart_line").parent().width();
				var _w = $(window).width();
				var _t = _w;
				if(_w<768){
					_t = _w-40;
				}
				$("#pointsChart_line").parent().show();
				$("#pointsChart_line").attr("width", _t);
				$("#pointsChart_line").attr("height", _t);
				$("#pointsChart_line").css("width", _t);
				$("#pointsChart_line").css("height", _t);
				var ctx = $("#pointsChart_line").get(0).getContext("2d");
				owellLines = new Chart(ctx, {
					type: 'line',
					data: owelldatas,
				    options: {
				    	responsive: true,
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
				$("#datamodal").modal('show');
			},100);
			//$("#datamodal").modal('show');
			//toastr.success("生成数据图表成功！");
			//alert($("#datamodal .modal-dialog").width());
		} else {
			toastr.error("获取测点数据出错");
		}
		$('#loading').hide();
	}).error(function() {$('#loading').hide(); });
}
/**
 * 显示测点当天的曲线图
 * @param type 测点类型，0降水井，1观测井，2回灌井，3环境监测
 * @param cid 测点id
 * @param modal 是否调用modal
 * @param begindt 开始时间
 * @param enddt 截止时间
 */
function showLines(url, type, cid, modal, begindt, enddt) {
	if(url==null || url == '') url = 'getLines.shtml'; 
	var timeout = 0;
	if(modal) {
		//先加载modal，通过setTimeout方式将modal先宽度获取，以便chart 的canvas宽度可以
		$("#commonmodal").modal('show');
		timeout = 500;
	}
	$('#loading').show();
	setTimeout(function(){$.post(url,{"type":type, "cid":cid, "begindt": begindt, "enddt": enddt}, function(response){
		var status = response.status;
		if (status == 0 || status == 9999) {
			//出来model内容
			var linesMap = response.linesMap;
			var chkTitle = response.chkTitle; 
			var dataName1 = linesMap.dataName1;
			var dataName2 = linesMap.dataName2;
			var dataset = linesMap.dataset;
			if(linesMap==null||dataName1==null||dataName1=='undefined'||dataset.length<1) {
				$('#loading').hide();
				$("#modal-body .panel-body").html("当前测点没有监测数据");
				$("#modal-title").html(chkTitle);
				return;
			}
			
			var data1=new Array(), data2=new Array(), titles=new Array();
			$.each(dataset, function(i, item){
				var o1 = {x:item.xTime, y:item.data1};
				var o2 = {x:item.xTime, y:item.data2};
				
				data1[i] = o1;
				data2[i] = o2;
			}); 
			//var _size = 'width="500" height="400"';
			var _w = $("#commonmodal .modal-body").width()-30;
			var _h=_w*0.7;
			if($(window).width()<768){
				_h=$(window).height()-250;
			}
			var _size = 'width="'+_w+'" height="'+_h+'"'; 
			var canvas = $('<canvas id="lineCanvas" '+_size+'></canvas>');
			var row = $('<div class="row"></div>');
			var col = $('<div class="col-sm-12"></div>');
			col.append(canvas);
			row.append(col);
			$("#modal-body .panel-body").html("").append(row); 
			$("#modal-title").html(chkTitle);
			var ctx = $("#lineCanvas").get(0).getContext("2d");
			
			var config = {
					type: 'line',
					data: {
				        datasets: [{
				            label: dataName1,
				            data: data1,
				            backgroundColor: 'rgba(255, 99, 132, 0.5)',
				            borderColor: 'rgba(255,99,132,1)',
				            fill: false,
				            yAxisID: "y-axis-1"
				        }]
			        },
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
							xAxes: [{
								type: "time",
								display: true,
								scaleLabel: {
									display: true,
									labelString: '录入时间'
								},
								time: {
									displayFormats: {
										second: 'HH:mm:ss',
										minute: 'HH:mm:ss',
					                	hour: 'MM-DD, HH'
					                }
								}
							}],
							yAxes: [{
		                        display: true,
		                        position: "left",
		                        id: "y-axis-1",
		                        scaleLabel: {
	                                display: true,
	                                labelString: dataName1
	                            }
		                    }]
						}
					}
			}
			//config.data.datasets.push(); 
			if(type!="3") {
				var _data = {
		            label: dataName2,
		            data: data2,
		            backgroundColor: 'rgba(54, 162, 235, 0.5)',
		            borderColor: 'rgba(54, 162, 235, 1)',
		            fill: false,
		            yAxisID: "y-axis-2"
		        };
				var _yAxe = {
                    type: "linear", // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
                    display: true,
                    position: "right",
                    id: "y-axis-2",
                    scaleLabel: {
                        display: true,
                        labelString: dataName2
                    }
                }
				config.data.datasets.push(_data);
				config.options.scales.yAxes.push(_yAxe);
			}
			lineChart = new Chart(ctx, config);
			
		} else {
			toastr.error("获取测点数据出错");
		}
		$('#loading').hide();
	}).error(function() {$('#loading').hide(); })}, timeout);
}
/**
 * 
 * @param type 测点类型 测点类型，0降水井，1观测井，2回灌井，3环境监测，01降水井流量，02降水井水位
 * @returns {Boolean}
 */
function getHistoryLines(type) {
	var begindt = $("input[name='begindt']").val();
	var enddt = $("input[name='enddt']").val();
	if (begindt=='' || enddt=='') {
		toastr.error('请选择查询时间范围');
		return false;
	}
	if (cid==undefined||cid=='') {
		toastr.error('请选择需要查询的测点数据');
		return false;
	}
	if (cid.length>1) {
		var cids="";
		for (var i = 0; i < cid.length; i++) {
			cids += cid[i]+",";
		}
		showCompareLines(_context+'/water/csite/getHistoryLines.shtml', type, cids, false, begindt, enddt);
	} else {
		showLines(_context+'/water/csite/getHistoryLines.shtml', type, cid, false, begindt, enddt);
	}
}

function showCompareLines(url, type, cid, modal, begindt, enddt) {
	if(url==null || url == '') url = 'getLines.shtml'; 
	$('#loading').show();
	$.post(url,{"type":type, "cid":cid, "begindt": begindt, "enddt": enddt}, function(response){
		var status = response.status;
		if (status == 0 || status == 9999) {
			if(modal) {
				$("#commonmodal").modal('show');
			}
			//出来model内容
			var comparedataset = response.comparedataset;
			var chkTitle = response.chkTitle; 
			
			var _w = $("#commonmodal .modal-body").width()-30;
			var _h=_w*0.7;
			if($(window).width()<768){
				_h=$(window).height()-250;
			}
			var _size = 'width="'+_w+'" height="'+_h+'"'; 
			var canvas = $('<canvas id="lineCanvas" '+_size+'></canvas>');
			var row = $('<div class="row"></div>');
			var col = $('<div class="col-sm-12"></div>');
			col.append(canvas);
			row.append(col);
			$("#modal-body .panel-body").html("").append(row); 
			$("#modal-title").html(chkTitle);
			var ctx = $("#lineCanvas").get(0).getContext("2d");
			var config = {
				type: 'line',
				data: {
			        datasets: []
		        },
		        options: {
		        	responsive: true,
	                title:{
	                    display:true,
	                    text:chkTitle
	                },
	                tooltips: {
	                    mode: 'index',
	                    intersect: true
	                },
					scales: {
						xAxes: [{
							type: "time",
							display: true,
							scaleLabel: {
								display: true,
								labelString: '录入时间'
							},
							time: {
								displayFormats: {
									second: 'HH:mm:ss',
									minute: 'HH:mm:ss',
				                	hour: 'MM-DD, HH'
				                }
							}
						}],
						yAxes: [{
	                        display: true,
	                        position: "left"
	                    }]
					}
				}
			}
			var size = comparedataset.length;
			console.log(size);
			$.each(comparedataset, function(i, item){
				var dataName = item.dataName;
				var dataset = item.dataset;
				var data = new Array();
				$.each(dataset, function(j, obj){
					var o = {x:obj.xTime, y:obj.data};
					data[j] = o;
				});
				var index = i%size;
				var _data = {
		            label: dataName,
		            data: data,
		            backgroundColor: chartColors[index],
		            borderColor: chartColorsborder[index],
		            fill: false
		        };
				config.data.datasets.push(_data);
			});
			lineChart = new Chart(ctx, config);
		} else {
			toastr.error("获取测点数据出错");
		}
		$('#loading').hide();
	}).error(function() {$('#loading').hide(); });
}

var chartColorsborder = ['rgb(255, 99, 132)', 'rgb(255, 159, 64)','rgb(255, 205, 86)', 'rgb(75, 192, 192)', 'rgb(54, 162, 235)', 'rgb(153, 102, 255)', 'rgb(231,233,237)'];
var chartColors = ['rgba(255, 99, 132,0.5)', 'rgba(255, 159, 64, 0.5)','rgba(255, 205, 86, 0.5)', 'rgba(75, 192, 192, 0.5)', 'rgba(54, 162, 235, 0.5)', 'rgba(153, 102, 255, 0.5)', 'rgba(231,233,237, 0.5)'];