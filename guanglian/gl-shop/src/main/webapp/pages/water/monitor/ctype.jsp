<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<div  class="row">
<div id="pbody" class="col-xs-12 col-gl-12">
</div>
</div>
<script>

	jQuery(document).ready(function() {
		var zone ='${zone}';
		$('#pictitle').html(zone);
		if(zone!=''){
			$('#loading').show();
			$.ajax({
				type : "POST",
				url : 'actype.shtml',
				data : "zone=" + zone,
				success : function(result) {
					$('#loading').hide();
					if(result!=null && result!="" &&result.length>0) {
						var width = $(window).width();
						width=(width-40)/2;
						//pc版
						if(width>400){
							width=250;
						}
						for(var i=0;i<result.length;i++){
							var isfirst = false;
							if(result[i].sublist!=null && result[i].sublist!=""){
								isfirst =true;
							}
							drawbin(result[i],i,isfirst,width,zone);
							if(isfirst ==true){
								for(var j=0;j<result[i].sublist.length;j++){
									drawbin(result[i].sublist[j],i+"_"+j,false,width,zone);
								}
							}
						}
					}
				},
				error : function(xhr, textStatus, errorThrown) {
					$('#loading').hide();
					toastr.error('error失败！'+ errorThrown);	
					
				}
			});
		}
	});
	function goback(){
		javascript :history.back(-1);
	}
	
	function drawbin(result,index,fisrt,width,zone){
		var str ='<section class="panel"><div class="row">';
        str+='<div class="col-xs-12 text-center"><div><canvas id="pie-chart'+index+'" width="'+width+'" height="'+width+'" style="display: inline-block; vertical-align: top;"></canvas></div></div>';
        //str+='<div class="col-xs-12 text-center"><div><canvas id="pie-total'+index+'" width="'+width+'" height="'+width+'" style="display: inline-block; vertical-align: top;"></canvas></div></div>';
        var total=result.down+result.run;
        if(fisrt==false){
        	str+='<div class="col-xs-12 text-center" style="font-size:22px;padding:0px;"><a class="list-group-item " href="'+_context+'/water/monitor/mprojects.html?ctype='+result.val+'&zone='+zone+'">'+result.name+'('+total+')</a></div>';
        }else{
        	str+='<div class="col-xs-12 text-center" style="font-size:22px;padding:0px;color:#fff;"><spn style="background-color:#2196F3" class="list-group-item " >汇总：'+result.name+'('+total+')</span></div>';
        }
		str+='</div></section>';
		$('#pbody').append(str);
		
        var ctx = $("#pie-chart"+index);
    	var myChart = new Chart(ctx, {type: 'pie',
    		data: {
    	        labels: ["正常","结束"],
    	        datasets: [{
    	            data: [result.down, result.run, result.srun,result.over],
    	            backgroundColor: [
    	                              "#2894FF",
    	                              "#ADADAD"
    	                          ]              
    	        }]
    	    },
    	    options: {
    	    	responsive: false
    	    }
    	});
    	/**
    	var ctx2 = $("#pie-total"+index);
    	var myChart1 = new Chart(ctx2, {type: 'pie',
    		data: {
    	        labels: ["故障数", "正常数"],
    	        datasets: [{
    	            data: [result.erro, result.total],
    	            backgroundColor: [
    	                              "#F7464A",
    	                              "#00A600"
    	                          ]              
    	        }]
    	    },
    	    options: {
    	    	responsive: false
    	    }
    	});*/
	
	}
</script>
<script src='<c:url value="/resources/js/Chart.min.js"/>'></script>
