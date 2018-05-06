
var oPolygon =null,drawPoint=null,isBinded=false;
	/**
	 * 开始画图
	 */
	var startAction = function (e) {
	    if (!isBinded) {
	        isBinded = true;
	        points.splice(0,points.length);
	        map.removeOverlay(oPolygon);
	      //移动事件
		    map.addEventListener('mousemove', mousemoveAction);
	    }
	    points.push(e.point);
	    drawPoint = points.concat(points[points.length - 1]);
	    if (points.length == 1) {
	            oPolygon = new BMap.Polygon(drawPoint, styleOptions);
	        map.addOverlay(oPolygon);
	    } else {
	        oPolygon.setPath(drawPoint);
	    }
	   
	}	
	
	var mousemoveAction = function(e) {
		oPolygon.setPositionAt(drawPoint.length - 1, e.point);
    }
	/**
     * 鼠标移动事件
     */
    var mousemoveAction = function(e) {
    	if(drawPoint!=null && drawPoint.length>1){
    		 oPolygon.setPositionAt(drawPoint.length - 1, e.point);
    	}
    }
    
    function stopDraw(e){
    	 stopBubble(e);
         isBinded = false;
         map.removeEventListener('mousemove', mousemoveAction);
    }
    /**
     * 双击停止
     */
    var dblclickAction = function (e) {
    	stopDraw(e);
    }
    
    function stopBubble(e){
		// 如果传入了事件对象，那么就是非ie浏览器
		if(e&&e.stopPropagation){
			//因此它支持W3C的stopPropagation()方法
			e.stopPropagation();
		}else{
			//否则我们使用ie的方法来取消事件冒泡
			window.event.cancelBubble = true;
		}
	}

       

        

        

        



