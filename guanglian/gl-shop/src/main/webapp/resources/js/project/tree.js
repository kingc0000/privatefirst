
/**
 * 解析项目
 * @param url
 * @param target
 * @param exFunction
 */

function getProjectTreeForCsite(url, target, exFunction){
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: url,
		success: function(cateList) {
			//alert(pid);
			var root = new Object();
			root.text = "全部";
			root.id = -1;
			root.icon = "fa fa-home";
			var nodeData = new Array();
			if (cateList.length<1) return;
			for (var i = 0; i < cateList.length; i++) {
				//nodeData[i]= getCateNode(cateList.cSites[i]);
				var item = new Object();
				item.text=cateList[i].name;
				item.id=cateList[i].id;
				item.onclick="";
				//设置选中
				if(item.id==pid){
					var rstate = new Object();
					rstate.selected=true;
					item.state=rstate;
				}
				nodeData[i]=item;
			}
			if(pid==-1){
				//设置全选
				var rstate = new Object();
				rstate.selected=true;
				root.state=rstate;
			}
			root.nodes = nodeData;
			target.treeview({
				color: "#222",
				backColor: "#e5e8ef",
		        showBorder: false,
		        onhoverColor: '#53bee6',
		        data: [root],
		        levels: 3,
		        ignoreChildren:true,
		        enableLinks:false
			});
			target.on("nodeSelected",function(event, data){
				 //target.toggle();
				//点击触发右边商品的查询
				eval(exFunction(data));
			 });
			 
		},
		error: function(data, jqXHR,textStatus,errorThrown) { 
			alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
	});	
}

/**
 * 项目工地结构List
 * @param url 获取项目工地集合
 * @param target 触发对象
 * @param csiteId 工地ID，赋值的hidden
 * @param csiteName 工地名称，赋值的text
 * @param projectName 项目名称
 */
function getProjectSitesList(url, target, csiteId, csiteName, projectName) {
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: url,
		success: function(projects) {
			var nodeData = new Array();
			if(projects==null || projects.length<1)return;
			for (var i = 0; i < projects.length; i++) { //项目
				var item = new Object();
				item.text = projects[i].name;
				item.id = projects[i].id;
				item.type = "department"; //项目类型
				item.onclick="";
				//工地
				var csites = projects[i].cSites;
				if (csites!=null && csites.length>0) {
					var csitesNode = new Array();
					for (var j = 0; j < csites.length; j++) {
						var csite = new Object();
						csite.id = csites[j].id;
						csite.text = csites[j].name;
						csite.type = "csite"; //工地类型
						csite.projectName = item.text;
						csite.onclick="";
						csitesNode[j] = csite;
					}
					item.nodes = csitesNode;
				}
				nodeData[i]=item;
			}
			
			target.treeview({
				color: "#000",
				backColor: "#e2e2e4",
		        showBorder: true,
		        onhoverColor: '#c2c2c2',
		        data: nodeData,
		        levels: 1,
		        enableLinks:false
			});
			 target.on("nodeSelected",function(event, data){
				 if (data.type == "csite") {
					 csiteId.val(data.id);
					 csiteName.val(data.text);
					 projectName.val(data.projectName);
					 target.toggle();
				}
			 });
			 
		},
		error: function(data, jqXHR,textStatus,errorThrown) { 
			console.log(data.responseText);
			alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
	});	
}

/**
 * 构造权限树
 * @param url 获取部门、工程特性、项目三级树集合
 * @param target 触发对象
 * @param uid 用户ID
 * @param gid 权限组ID
 * @param fn 回调方法
 */
function getProjectSitesTree(url, target,uid,gid,fn) {
	
	$.ajax({
		type: 'POST',
		dataType: "json",
		url: url,
		data:'uid='+uid+'&gid='+gid,
		success: function(projects) {
			if(projects==null || projects.length<1){
				$('#loading').hide();
				$('#rightList').modal('hide');
				alert("获取数据失败!!!!");
				return;
			}
			var root = new Object();
			root.text = "全部";
			root.id = -1;
			root.icon = "fa fa-home";
			root.type ="all";
			//
			var rstate = new Object();
			if(projects.status==0){ //部分选
				rstate.checkedMiddle=true;
			}else if(projects.status==1){//全选
				rstate.checkedMiddle=false;
				rstate.selected=true;
				rstate.checked=true;
			}else{//未选
				rstate.checkedMiddle=false;
				rstate.selected=false;
				rstate.checked=false;
			}
			root.state=rstate;
			
			var nodeData = new Array();
			//item.type 节点类型：all,department,features,csite
			for (var i = 0; i < projects.data.length; i++) {
				var item = new Object();
				item.text = projects.data[i].name;
				item.id = projects.data[i].id;
				item.type = "department"; //项目类型
				var pstate = new Object();
				
				item.state=setSatues(pstate,projects.data[i].status);
				
				//工程特性
				var featureses = projects.data[i].featureses;
				if (featureses!=null && featureses.length>0) {
					var featuresNode = new Array();
					for (var j = 0; j < featureses.length; j++) {
						var features = new Object();
						features.id = featureses[j].id;
						features.text = featureses[j].name;
						features.type = "features"; //工地类型
						var fstate = new Object();
						features.state = setSatues(fstate, featureses[j].status);
						featuresNode[j] = features;
						//项目
						var csites = featureses[j].cSites;
						if (csites!=null && csites.length>0) {
							var citesNode = new Array();
							for (var k = 0; k < csites.length; k++) {
								var csite = new Object();
								csite.id = csites[k].id;
								csite.text = csites[k].name;
								csite.type = "csite"; //工地类型
								csite.onclick="";
								var cstate = new Object();
								csite.state = setSatues(cstate, csites[k].status);
								citesNode[k] = csite;
							}
							features.nodes = citesNode;
						}
						featuresNode[j]=features;		
					}
					item.nodes = featuresNode;
				}
				nodeData[i]=item;
			}
			
			root.nodes = nodeData;
			target.treeview({
				color: "#000",
				backColor: "#e2e2e4",
		        showBorder: true,
		        onhoverColor: '#c2c2c2',
		        data: [root],
		        levels: 3,
		        selectedEqChecked:true,
		        showCheckbox:true, 
		        multiSelect:true,
		        enableLinks:false
			}); 
			$('#loading').hide();
			if(fn!=null){
				fn(gid);
			}
			
		},
		error: function(data, jqXHR,textStatus,errorThrown) { 
			$('#loading').hide();
			alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
		}
	});	
}

function setSatues(state,sindex){
	if(sindex==1){
		state.selected=true;
		state.checked=true;
		state.checkedMiddle=false;
	}else if(sindex==0){
		state.selected=false;
		state.checked=false;
		state.checkedMiddle=true;
	}else{
		state.selected=false;
		state.checked=false;
		state.checkedMiddle=false;
		
	}
	return state;
}