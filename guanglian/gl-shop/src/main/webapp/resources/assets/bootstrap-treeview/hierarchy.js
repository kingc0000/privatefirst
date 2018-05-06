$(function(){
	
	
	gettreedata();
	
});

function gettreedata(){
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: 'categorys.html',
		success: function(productList) {
			var treeData = new Array();
			var root = new Object();
			 root.title="ROOT";
			 root.code=-1;
			 root.href="";
			 var nodeData = new Array()
			for (var i = 0; i < productList.categorys.length; i++) {
				nodeData[i]= getNode(productList.categorys[i]);
			}
			 root.Link=nodeData;
			 treeData[0]=root;
			 showTreeData(treeData);		
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			//$(divProductsContainer).hideLoading();
			alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			
		}
	});
}

function changparent(cid,pid){
	$('#hierachy').showLoading();
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: 'moveCategory.html',
		data:"cid="+cid+"&pid="+pid,
		success: function(response) {
			if(response.response.status==9999) {
				  alertSuccess();				  
				 // setStatus(response.response.statusMessage)
			  }else {
				  activeFaild();
				  $("#treeDemo").empty();
				  gettreedata();
			  }
			  $('#hierachy').hideLoading();	
		},
		error: function(jqXHR,textStatus,errorThrown) { 
			//$(divProductsContainer).hideLoading();
			alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			$('#hierachy').hideLoading();
		}
	});
}

//实现树节点的隐藏与显示
function HideShowNode(switchNodeId) {
    //获取节点Id转换为对象
    thisObject = $("#" + switchNodeId);
    var currentDrageNodeId = switchNodeId.substring(0, switchNodeId.length - 7);
    var icoNodeId = "#" + currentDrageNodeId + "_ico"; 
	//文件夹图标打开与关闭
    var UlNodeId = "#" + currentDrageNodeId + "_ul";                

    //组合成ul为实现隐藏与显示 
    if ($(UlNodeId).is(":hidden")) {
        //切换图标
        InteractiveIcon(thisObject, "close", "open");
        if ($(icoNodeId).attr("class") == "glyphicon glyphicon-plus") {
            $(icoNodeId).attr("class", "glyphicon glyphicon-minus");
        }

        //滑入
        $(UlNodeId).slideDown("fast");
    } else {
        //切换图标
        InteractiveIcon(thisObject, "open", "close");
        if ($(icoNodeId).attr("class") == "glyphicon glyphicon-minus") {
        	$(icoNodeId).attr("class", "glyphicon glyphicon-plus");
        }

        //滑出
        $(UlNodeId).slideUp("fast");
    }
}

//触发器实现节点的隐藏与显示
function HideShowTrigger($switchId) {
    var strSwitchId = $switchId.substring(1, $switchId.length);
    $($switchId).bind("myEvent", function (event, messageObject) {
        HideShowNode(messageObject);
    });
    $($switchId).trigger("myEvent", [strSwitchId]);

    //避免事件被多次绑定
    $($switchId).unbind("myEvent");
}

//当前节点在移动后按其原来的样式显示【原来是展开就展开，收缩则收缩】
function CurrentNodeExpandContract($switchId,SwitchBool) {
    if (SwitchBool) {
        HideShowTrigger($switchId);
        SwitchBool = false;
    }
}



//拖拽至顶级节点判断
function DragingToRoot(rootUlId, event, sourceParentUlId) {
    var offset = $(rootUlId).offset();
    var ulWidth = $(rootUlId).width();
    var ulHeight = $(rootUlId).height();

    if ((((offset.left + ulWidth - 10) < event.pageX) && (event.pageX < (offset.left + ulWidth))) && ((offset.top < event.pageY) && (event.pageY < (offset.top + ulHeight))) && sourceParentUlId != "treeDemo") {
        //为顶级目录拖拽至顶级不显示样式,【即顶级不能再拖拽至顶级】
        $(rootUlId).css({ "background-color": "#D2E9FF" });
        isDrageToRoot = true;
    } else {
        $(rootUlId).css({ "background-color": "white" });
        isDrageToRoot = false;
    }
}


//拖拽优化整合
function DragingIconIntegrate(nodeId,nodeType) {
    var $nodeSwitchId = "#" + nodeId + "_switch";
    var $nodeUlId = "#" + nodeId + "_ul";
    if (nodeType == "sourceParentNode") {
        //$nodeUlId = "";
         $($nodeSwitchId).hide();
    }    
    if(nodeType == "targetCurrentParentNode"){
    	
    	$($nodeSwitchId).show();
    	$($nodeSwitchId).removeClass("glyphicon"); $($nodeSwitchId).removeClass("glyphicon-minus");
    	$($nodeSwitchId).removeClass("glyphicon-plus");$($nodeSwitchId).removeClass("glyphicon");
    	$($nodeSwitchId).addClass("class", "glyphicon");
    	$($nodeSwitchId).addClass("class"," glyphicon-minus");
    }
    //var nodeClass = $($nodeSwitchId).attr("class");
    //DragingInteractiveIcon(nodeClass, $nodeSwitchId, $nodeUlId, nodeType);
}

function getNode(nodes){
	
	var item = new Object();
		item.title=nodes.categoryName;
		item.code=nodes.categoryID;
		item.href="";
	if(nodes.categorys !=null && nodes.categorys.length>0 ){
		var sonNode = new Array();
		for(var i=0; i<nodes.categorys.length;i++){
			sonNode[i]=getNode(nodes.categorys[i]);
		}		
		item.Link=sonNode;
	}
	
	return item;
}
//切换图标[beforeIcon:以前图标,thisIcon:当前图标]
var InteractiveIcon = function ($this, beforeIcon, thisIcon) {
    if ($this.attr("class") == "switch_roots_" + beforeIcon) {
        $this.attr("class", "switch_roots_" + thisIcon);
    } else if ($this.attr("class") == "switch_bottom_" + beforeIcon) {
        $this.attr("class", "switch_bottom_" + thisIcon);
    } else if ($this.attr("class") == "switch_root_" + beforeIcon) {
        $this.attr("class", "switch_root_" + thisIcon);
    } else if ($this.attr("class") == "switch_center_" + beforeIcon) {
        $this.attr("class", "switch_center_" + thisIcon);
    }
};

function showTreeData(Lists){
	 //用Json的方式设置定义参数
    var setting = {
        //组合而成的html代码
        treeString: "",
        //是否为首节点
        isRootNode: true,
        //+、-交互按钮
        switchClass: "",
        //顶级目录的竖虚线
        ulClass: "",
        //文件、文件夹图标
        fileClass: "",
        //定义是否显示子目录
        isDisplay: "display:block",
        //开始坐标
        startAxisX: 0,
        startAxisY: 0,
        //移动坐标
        moveAxisX: 0,
        moveAxisY: 0,
        //最小移动距离
        MinMoveSize: 5
    };
    var SwitchBool = false;     //拖拽时判断当前节点是否展开状态
    var isDrageToRoot = false;  //是否拖拽至顶级节点


    //1.树Html初始化
    var InitTreeHtml = function (treeId, treeTitle, treeHref, treeImg, switchClass, fileClass) {
        var TreeHtml = '<li class="list-group-item" id="treeDemo_' + treeId + '_li"><button type="button" title="switch" class="' + switchClass + '" id="treeDemo_' + treeId + '_switch"><span id="treeDemo_' + treeId + '_ico" class="glyphicon glyphicon-plus" aria-hidden="true"></span></button><a id="treeDemo_' + treeId + '_a" onclick="" target="_blank" ><span id="treeDemo_' + treeId + '_span">' + treeTitle + '</span></a>';                
        return TreeHtml;
    };
    
    var InitNoButtonTree = function (treeId, treeTitle,treeHref, treeImg, switchClass, fileClass) {
        var TreeHtml = '<li class="list-group-item" id="treeDemo_' + treeId + '_li"><button type="button" style="display:none" title="switch" class="' + switchClass + '" id="treeDemo_' + treeId + '_switch"><span id="treeDemo_' + treeId + '_ico" class="glyphicon glyphicon-minus" aria-hidden="true"></span></button><a id="treeDemo_' + treeId + '_a" onclick="" target="_blank" ><span id="treeDemo_' + treeId + '_span">' + treeTitle + '</span></a>';                
        return TreeHtml;
    };

    //虚线和展开、收缩图标[初始化,jsonList:json数据,index:索引,initClass:初始化图标]
    var InitIcon = function (jsonList, index, initClass, isFirstNode) {
        if (index + 1 == jsonList.length) {
            if (jsonList.length == 1 && isFirstNode == true) {
                //整个树只有一个节点的情况【特殊】
                setting.switchClass = "";
            } else {
                //同级最后一个元素图标
                setting.switchClass = "switch_bottom_" + initClass;
            }
            setting.ulClass = "";
        } else {
            //同级中间元素图标
            setting.switchClass = "switch_center_" + initClass;
            setting.ulClass = "line";
        }
    }

    //根节点【isRootNode】首次加载判断
    var InitFirstIcon = function (jsonList, index) {
        //同级第一个元素图标
        if (index == 0) {
            setting.switchClass = "switch_roots_close";
            setting.ulClass = "line";
        }

        //本级目录只有一项显示图标【jsonList为一个值时】
        if (jsonList.length == 1) {
            setting.switchClass = "switch_root_close";
            setting.ulClass = "";
        }
    };

    
    
   


    var InitTreeView = function (jsonList, isFirstNode) {
        $.each(jsonList, function (index, term) {

            if (!jsonList) return;

            if (term.Link) {
                ///图标加载
                //1.当有子节点时图标关闭状态
                InitIcon(jsonList, index, "close", isFirstNode);
                //2.首节点
                if (isFirstNode == true) {
                    //加载同级首节点的判断
                    InitFirstIcon(jsonList, index);
                }
                //3.有子节点为文件夹图标
                setting.fileClass = "ico_close";


                setting.treeString += InitTreeHtml(term.code, term.title, term.href, term.img, setting.switchClass, setting.fileClass);


                isFirstNode = false;
                setting.isDisplay = "display:none;";
                setting.treeString += '<ul class="' + setting.ulClass + '" id="treeDemo_' + term.code + '_ul" style="' + setting.isDisplay + '">';

                //递归寻找子目录
                InitTreeView(term.Link, isFirstNode);

                setting.treeString += '</ul>';
            } else {
                ///图标加载
                //1.无子节点为文件图标
                //setting.fileClass = "ico_docu";
                //2.最后子节点时显示文件图标
                //InitIcon(jsonList, index, "docu", isFirstNode);
                setting.switchClass="glyphicon glyphicon-minus";
                //setting.isDisplay = "display:none;";

                setting.treeString += InitNoButtonTree(term.code, term.title, term.href, term.img, setting.switchClass, setting.fileClass);
            }

            setting.treeString += '</li>';
        });
        return setting.treeString;
    };

    //2.初始化Tree目录【Main】
    var TreeView = InitTreeView(Lists, setting.isRootNode);
    $("#treeDemo").append(TreeView);

    //3.事件模块【Event】
    //单击隐藏与显示列表
    $('button[title="switch"]').click(function () {
        var $this = $(this);
        //获取单击button中的Id
        var SwitchNodeId = $this.attr("id");

        HideShowNode(SwitchNodeId);
    });            

    //单击a标签Dragging
    //实现思想：1.单击<a>标签时将<li>追加至<div> 2.<div>实现移动  3.释放时remove<div>
    //实现方法:1.mousedown 2.mousemove 3.mouseover 4.mouseup
    var currentAId="";
    var ZTreeMask = "";
    var $currentAId = "";
    var curentParentAId = "";
    var currentDrageNodeId = "";
    var currentDrageLiId = "";
    var currentDrageSwitchId = "";            

    $("a").mousedown(function (event) {
        currentAId = $(this).attr("id");
        $currentAId = "#" + currentAId;
        curentParentAId = $($currentAId).parent().parent().prev().attr("id");  //获取当前节点的父节点
        currentDrageNodeId = currentAId.substring(0, currentAId.length - 2);
        currentDrageLiId = "#" + currentDrageNodeId + "_li";
        currentDrageSwitchId = "#" + currentDrageNodeId + "_switch";

        //当前拖拽节点存在返回,可不要以防程序出现bug
        if ($("#zTreeMask_" + currentDrageNodeId).length > 0) return;  

        setting.startAxisX = event.clientX;
        setting.startAxisY = event.clientY;                

        //追加拖拽div
        ZTreeMask = "<div id='zTreeMask_" + currentDrageNodeId + "' class='dragingNode' style='top:" + setting.startAxisY + "px; left:" + setting.startAxisX + "px; width:" + $(this).width() + "px; height:" + $(this).height() + "px;'></div>";

        //单击树节点选中
        $("a").removeClass("curSelectedNode");
        $(this).attr("class", "curSelectedNode");
                        
    }).mouseover(function (e) {
        if ($(this).attr("class") != "curSelectedNode") {
            $(this).attr("class", "tmpTargetTree");
        } 
    }).mouseout(function (e) {
        $(this).removeClass("tmpTargetTree");
    });
    

    var Bool = false;
    var tmpDragingNode = "";
    //拖拽时判断当前节点是否展开状态
    $(document).mousemove(function (event) {
    	//1,2,3,4不能移动
    	if(currentAId.substring(9,currentAId.length-2)<5){
    		return;
    	}
        //除掉默认事件，防止文本被选择
        window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();

        //节点拖拽移动的距离
        setting.moveAxisX = event.pageX - setting.startAxisX;
        setting.moveAxisY = event.pageY - setting.startAxisY;

        //避免鼠标误操作，对于第一次移动小于MinMoveSize时，不开启拖拽功能
        if (setting.moveAxisX < setting.MinMoveSize && setting.moveAxisY < setting.MinMoveSize) {
            return;
        }

        //判断鼠标是否按下event.which==1在ie中不兼容 右键不能拖动NOT DO【更改为Bool判断】
        if (Bool) {                   
            if ($("#zTreeMask_" + currentDrageNodeId).length == 0 && currentDrageNodeId != "") {
                $("body").append(ZTreeMask);
                $("#zTreeMask_" + currentDrageNodeId).append($($currentAId).clone());

                //判断当前拖拽的节点为展开文件夹则先把文件夹收缩再拖拽【触发器实现】
                var currentDrageIcoId = "#" + currentDrageNodeId + "_ico";
                currentDrageSwitchId = currentDrageNodeId + "_switch";
                if ($(currentDrageIcoId).attr("class") == "glyphicon glyphicon-minus") {
                    HideShowTrigger("#" + currentDrageSwitchId);

                    SwitchBool = true;
                }

            }

            //拖拽移动的距离
            $("#zTreeMask_" + currentDrageNodeId).css({ "left": setting.startAxisX + setting.moveAxisX + "px", "top": setting.startAxisY + setting.moveAxisY + "px" });

            //拖拽的目标节点 
            if ($("#zTreeMask_" + currentDrageNodeId).length > 0) {
                //绑定mouseover事件，鼠标键mouseup时要unbind（mouseover）
                $("a").mouseover(function (e) {
                                                
                    tmpDragingNode = $(this).attr("id");
                    //判断当移动目标父节点为本身源节点则不显示样式,目标父节点置为空
                    $(this).addClass("tmpTargetNode");                        

                }).mouseout(function (e) {
                    $(this).removeClass("tmpTargetNode");
                });
            }                    
            

            //目标拖动至顶级节点【顶级拖拽至顶级不显示】显示样式,判断下级拖动至上级成为顶级节点【******】
            var currentParentUlId = $(currentDrageLiId).parent().attr("id");
            DragingToRoot("#treeDemo", event, currentParentUlId);                    

        };                               

    }).mousedown(function (e) {
        Bool = true;                
    }).mouseup(function (e) {
        Bool = false;
        if ($("#zTreeMask_" + currentDrageNodeId).length > 0) {
            //释放移除临时拖动的节点
            $("#zTreeMask_" + currentDrageNodeId).fadeOut().remove();

            //源节点拖拽至目标节点代码  
            //移上的节点加子节点
            var tmpDragingNodeString = tmpDragingNode.substring(0, tmpDragingNode.length - 2);
            var tmpDragingNodeSwitchId = tmpDragingNodeString + "_switch";
            var tmpDragingNodeIcoId = tmpDragingNodeString + "_ico";
            var tmpDragingNodeAId = tmpDragingNodeString + "_a";
            var tmpDragingNodeUlId = tmpDragingNodeString + "_ul";

            //获取当前拖拽的Li的父目录Ul
            var currentParentUlId = $(currentDrageLiId).parent().attr("id");

            if ((tmpDragingNode == currentAId || tmpDragingNode == "") && isDrageToRoot==false ) {
                //临时移动目标节点为自己Id或为空不移动,【释放后原来是展开的仍展开，收缩的仍收缩】
                CurrentNodeExpandContract("#" + tmpDragingNodeSwitchId,SwitchBool);

            } else {                                                                                              
                //移动前：同级->在源节点当前拖拽时的前个元素下的switch图标更改                        
                var currentParentNodeId = currentParentUlId.substring(0, currentParentUlId.length - 3);                        
                var currentDrageSwitchClass1 = $("#" + currentDrageSwitchId).attr("class");

                //在此同级上增加属性，以供后面判断，以免last会把其下的所有相同元素都会算上
                $(currentDrageLiId).siblings("li").attr("title", "sibling");

                var prevSourceLiId = $(currentDrageLiId).prev().attr("id");
                var nextSourceLiId = $(currentDrageLiId).next().attr("id");                        
                
                if (isDrageToRoot) {
                    //子节点移至根节点实现
                    tmpDragingNodeUlId = "treeDemo";
                    $("#" + tmpDragingNodeUlId).append($(currentDrageLiId));

                    //移动前：同级->在源节点当前拖拽时的前个元素变为最后元素图标切换
                    var currentMoveLastLiId = "";
                    if (currentParentUlId) {
                        //获取同级最后一个元素 【没有最后元素的话父节点图标变空】要节点为treeDemo
                        currentMoveLastLiId = $('#' + currentParentUlId + ' li[title="sibling"]:last').attr("id"); 
                    }
                    
                } else {
                    //判断不存在则UL追加
                    if ($("#" + tmpDragingNodeUlId).length == 0) {
                        var tmpDragingNodeUl = '<ul id="' + tmpDragingNodeUlId + '"></ul>';
                        $("body").append(tmpDragingNodeUl);
                    }

                    //追加移动节点至ul并追加至要移上的节点
                    $("#" + tmpDragingNodeUlId).append($(currentDrageLiId)).insertAfter($("#" + tmpDragingNodeAId));


                    //移动前：同级->在源节点当前拖拽时的前个元素变为最后元素图标切换
                    var currentMoveLastLiId = "";
                    if (currentParentUlId) {
                        //获取同级最后一个元素 【没有最后元素的话父节点图标变空】要节点为treeDemo
                        currentMoveLastLiId = $('#' + currentParentUlId + ' li[title="sibling"]:last').attr("id");

                        //根顶部图标移动后，下个元素变为顶部元素
                        if (currentParentUlId == "treeDemo" && (currentDrageSwitchClass1 == "switch_roots_open" || currentDrageSwitchClass1 == "switch_roots_close" || currentDrageSwitchClass1 == "switch_roots_docu") && nextSourceLiId) {
                            var nextSourceNodeId = nextSourceLiId.substring(0, nextSourceLiId.length - 3);
                            var $nextSourceLiId = "#" + nextSourceLiId;
                            DragingIconIntegrate(nextSourceNodeId, "sourceRootNextNode");
                        }
                    }

                    //移动后:目标父节点切换图标、文件夹图标【不能移入自身的子节点Bug,移动时收起】                            
                    var $tmpDragingNodeIcoId = "#" + tmpDragingNodeIcoId;                            
                    var dragNodeAInput = $("#" + tmpDragingNodeIcoId).attr("class");
                    DragingIconIntegrate(tmpDragingNodeString, "targetCurrentParentNode");
                    if (dragNodeAInput == "ico_close" || dragNodeAInput == "ico_docu") {
                        $($tmpDragingNodeIcoId).attr("class", "ico_open");
                    }
                    

                    //3.[原来]

                }

                //3.[原来]     
                //同：移动后前个元素为最后一个元素才判断
                if (prevSourceLiId && prevSourceLiId == currentMoveLastLiId) {
                    var prevSourceNodeId = prevSourceLiId.substring(0, prevSourceLiId.length - 3);
                    var $prevSourceLiId = "#" + prevSourceLiId;                           
                    DragingIconIntegrate(prevSourceNodeId, "sourcePrevNode");
                   

                } else {
                    //当前拖拽节点后，判断当前目录下是否有子节点，没有则父节点变为文件图标【拖至本身父节点下不改变图标】
                    if (currentParentNodeId != tmpDragingNodeString) {
                        if (!currentMoveLastLiId) {
                        	
                            var currentParentIcoId = "#" + currentParentNodeId + "_ico";
                            var currentParentIcoClass = $(currentParentIcoId).attr("class");
                            DragingIconIntegrate(currentParentNodeId, "sourceParentNode");

                            if (currentParentIcoClass == "ico_open" || currentParentIcoClass == "ico_close") {
                                $(currentParentIcoId).attr("class", "");
                            }

                        }
                    }                            

                }

                //同：移动后：同级->移动目标节点后前个元素下的switch图标更改
                var prevTargetLiId = $(currentDrageLiId).prev().attr("id");
                if (prevTargetLiId) {
                    var prevTargetNodeId = prevTargetLiId.substring(0, prevTargetLiId.length - 3);
                    var $prevTargetLiId = "#" + prevTargetLiId;      
                   
                    DragingIconIntegrate(prevTargetNodeId, "targetPrevNode");
                }

                //同：2.判断当前节点为打开状态移入时也为打开状态 
                //当前的节点为文件最后切换图标【判断为文件夹要添加收缩展开图标 DONE】
                DragingIconIntegrate(currentDrageNodeId, "targetCurrentNode");
                changparent(currentAId.substring(9,currentAId.length-2),tmpDragingNode.substring(9,tmpDragingNode.length-2));

            }                    

            //移除事件，不然鼠标移上节点又显示原来拖拽时的样式 【移动后(移除)再移上鼠标不能显示样式bug NOTDO】
            $("a").unbind("mouseover").unbind("mouseout");

            //更新XML文档  【判断当没有移动时不更新】
            var sourceParentId = currentParentNodeId.substring(9);
            var targetParentId = tmpDragingNodeString.substring(9);
            var currentDrageId = currentDrageNodeId.substring(9);
        }


        //清除空白处拖拽再次显示
          currentDrageNodeId = "";
       
          $("a").removeClass();                //$("a").removeClass("tmpTargetNode");
          $("li").removeAttr("title");         //清空判断更改图标时的属性
          

    });
}