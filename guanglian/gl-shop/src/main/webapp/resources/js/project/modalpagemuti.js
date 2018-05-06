/**
 * 
 * @param title
 * @param sfiled 赋值指定的属性，调用页面出来定义显示的属性name，对于id，需要定义type=hidden，id=sfiledid
 * @param url
 * @param type，指定显示的方式，type=modal，以modal模态方式显示，type=select，以select方式显示
 * @param exfun , 回调方法，如果回调方法名为“assginName",则将_assignName=true，只进行名称赋值操作
 */
var filed=null;
var did =null;
function getMList(title,sfiled,url,type,uses,divid,exfun) {
		$.ajax({
			type: 'GET',
			dataType: 'json',
			url: url,
			contentType:"application/x-www-form-urlencoded",
			success: function(storemenu) {
				$("#modal-title").html(title);
				if(exfun=='assignName')_assignName=true;
				var body = '<div>已选用户：<span id="aselected"></span></div><ul class="nav nav-tabs" role="tablist">';
				var detail = ' <div class="tab-content">';
				if (storemenu != null && storemenu.length > 0) {
					filed=sfiled;
					did=divid
					var nfield=sfiled;
					var index = sfiled.indexOf(".");
					if(index!=-1){
						nfield=sfiled.substring(0,index);
					}
					$.each(storemenu,function(i, m) {
										body += '<li role="presentation"';
										detail += ' <div role="tabpanel" class="tab-pane ';
										if (i == 0) {
											body += ' class="active"';
											detail += 'active';
										}
										body += '><a href="#'+nfield+'_'+m.code +'" aria-controls="'+nfield+'_'+m.code+'" role="tab" data-toggle="tab">'
												+ m.code + '</a></li>';
										detail += '" id="'+nfield+'_' + m.code
												+ '">';
										var lett = '';
										$.each(
														m.lists,
														function(j, k) {
															
															if (k.pinyin != lett) {
																if (j != 0) {
																	detail += '</div></div>';
																}
																detail += '<div class="row" style="padding-top:10px;"><div class="col-sm-1">';
																detail += k.pinyin;
																
																	detail += '</div><div class="col-sm-11"><div class="menulink"><input name="uselect" onclick="setValue(&quot;'+k.id+'&quot;,&quot;chek&quot;)" type="checkbox" title="'+k.name+'" value="'+k.id+'"><a href="javascript:void(0);" onclick="setValue(&quot;'+k.id+'&quot;)">&nbsp;'+k.name+'</a></div>';
																lett = k.pinyin;
															} else {
																detail += '<div class="menulink"><input type="checkbox" name="uselect" title="'+k.name+'" onclick="setValue(&quot;'+k.id+'&quot;,&quot;chek&quot;)" value="'+k.id+'"><a href="javascript:void(0);" onclick="setValue(&quot;'+k.id+'&quot;)">&nbsp;'+k.name+'</a></div>';
															}

														});
										detail += '</div></div></div>';
									});
					
				}else{
					detail ='';
				}
				
				body += '</ul>' + detail;
				if(type!=null && type!="modal") {
					$("#"+type).html(body);
				} else {
					$("#modalList .modal-body").html(body);
					$('#modalList').modal('show');
					$("#okbtn").show();
					$("#cancelbtn").show();
					setinit(sfiled);
				}
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#modalList .modal-body").html("");
			}
		})
	}	
	function setinit(sfiled){
		var selected='';
		$.each($('input[name="'+sfiled+'"]'),function(index,data){
			var that =data;
			$.each($('input[name="uselect"]'),function(index1,data1){
				if($(this).attr("value")==$(that).attr("title")){
					$(this).prop("checked",true);
					selected+=$(this).attr("title")+'&nbsp;';
					return false;
				}
			});
		});
		$("#aselected").html(selected);
	}
	function setValue(uid,chek){
		var selected='';
		$("#aselected").html('');
		$.each($('input[name="uselect"]'),function(index,data){
			if($(this).attr("value")==uid){
				//checkbox传过啦的
				if(chek==null){
					if($(this).prop("checked")){
						$(this).prop("checked",false);
					}else{
						$(this).prop("checked",true);
					}	
				}
			}
			if($(this).prop("checked")){
				selected+=$(this).attr("title")+'&nbsp;';
			}
			
		});
		$("#aselected").html(selected);
	}
	function cancelall(){
		$("#aselected").html('');
		$.each($('input[name="uselect"]'),function(index,data){
			$(this).prop("checked",false);
		});
	}
	function ok(){
		var selec='';
		$.each($('input[name="uselect"]'),function(index,data){
			if($(this).prop("checked")){
				selec+='<input name="'+filed+'" checked="checked" type="checkbox" title="'+$(this).attr("value")+'" value="'+$(this).attr("value")+'"><a class="menulink" href="javascript:void(0);" onclick="setCheckbox(&quot;'+filed+'&quot;,'+$(this).attr("value")+')">&nbsp;'+$(this).attr("title")+'</a>'
			}
			
		});
		$("#"+did).html(selec);
		$('#modalList').modal('hide');
	}