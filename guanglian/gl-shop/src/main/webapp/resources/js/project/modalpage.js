/**
 * 
 * @param title
 * @param sfiled 赋值指定的属性，调用页面出来定义显示的属性name，对于id，需要定义type=hidden，id=sfiledid
 * @param url
 * @param type，指定显示的方式，type=modal，以modal模态方式显示，type=select，以select方式显示
 * @param exfun , 回调方法，如果回调方法名为“assginName",则将_assignName=true，只进行名称赋值操作
 */
var _assignName = false; //只进行名称赋值，不做id值赋值操作
function getList(title,sfiled,url,type,exfun) {
		$.ajax({
			type: 'GET',
			dataType: 'json',
			url: url,
			contentType:"application/x-www-form-urlencoded",
			success: function(storemenu) {
				$("#modal-title").html(title);
				$("#okbtn").hide();
				$("#cancelbtn").hide();
				if(exfun=='assignName')_assignName=true;
				var body = '<ul class="nav nav-tabs" role="tablist">';
				var detail = ' <div class="tab-content">';
				if (storemenu != null && storemenu.length > 0) {
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
																
																	detail += '</div><div class="col-sm-11"><a class="menulink" href="javascript:void(0);" onclick="setListValue(&quot;';
																	
																//执行函数
																	if(exfun){
																		detail +=k.id+'&quot;,&quot;'+k.name+'&quot;,&quot;'+sfiled+'&quot;,&quot;'+type+'&quot;,'+exfun+')">';
																	}else{
																		detail +=k.id+'&quot;,&quot;'+k.name+'&quot;,&quot;'+sfiled+'&quot;,&quot;'+type+'&quot;)">';
																	}
																detail += k.name
																		+ '</a>';
																lett = k.pinyin;
															} else {
																detail += '<a class="menulink" href="javascript:void(0);" onclick="setListValue(&quot;';
																if(exfun){
																	detail +=k.id+'&quot;,&quot;'+k.name+'&quot;,&quot;'+sfiled+'&quot;,&quot;'+type+'&quot;,'+exfun+')">';
																}else{
																	detail +=k.id+'&quot;,&quot;'+k.name+'&quot;,&quot;'+sfiled+'&quot;,&quot;'+type+'&quot;)">';
																}
																
																detail += k.name
																		+ '</a>';
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
					$("#okbtn").hide();
					$("#cancelbtn").hide();
				}
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#modalList .modal-body").html("");
			}
		})
	}

	/**
	 * @param sid
	 * @param sname
	 * @param sfiled
	 * @param type
	 * @param exfun 回调函数，但是如果exfun==='assignName'
	 */
	function setListValue(sid,sname,sfiled,type,exfun){
		if(_assignName) {
			assignName(sname,sfiled,type);
		} else {
			//将对象设置为只读
			$("#"+sfiled).attr("readonly","readonly");
			$("input[id='"+sfiled+"']").val(sname);
			var index = sfiled.lastIndexOf(".");
			var n = (sfiled.split('.')).length-1;
			if(n==1){
				var nfiled = sfiled.substring(0,index);
				$("input[id='"+nfiled+".id']").val(sid);
			}else if(n==2){
				var nfiled = sfiled.substring(0,index);
				$("input[name='"+nfiled+".id']").val(sid);
			}else{
				$("input[id='"+sfiled+"id']").val(sid);
			}
			
			if(type!=null && type !="undefined" && type!="modal") {
				$('#'+type).parent().collapse('hide');
			} else {
				$('#modalList').modal('hide');
			}
			if(exfun){
				eval(exfun());
			}
		}
	}
	
	function setfildValue(sid,sname,sfiled,type){
		$("#"+sfiled).val(sname);
		$("#autoID").val(sid);
		$("#atype").val(type);
		$('#modalList').modal('hide');
		
	}
	/**
	 * 只进行名称赋值，不做id传递
	 */
	function assignName(sname,sfiled,type) {
		//将对象设置为只读
		$("#"+sfiled).attr("readonly","readonly");
		$("input[id='"+sfiled+"']").val(sname);
		if(type!=null && type !="undefined" && type!="modal") {
			$('#'+type).parent().collapse('hide');
		} else {
			$('#modalList').modal('hide');
		}
	}
	
	function getTrees(title,sfiled,url) {
		$("#modal-title").html(title);
		var body = '<ul class="nav nav-tabs" role="tablist">';
		var detail = ' <div class="tab-content">';
		
		
		body += '</ul>' + detail;
		$("#modalList .modal-body").html(body);
		$('#modalList').modal('show');
	}
	/**
	 * 自定义按钮，让控件不在只读，情况对象id
	 * @param oid
	 */
	function defineSelf(oid){
		$('#'+oid).val('');
		$('#'+oid).removeAttr("readonly");
		$("#"+oid+"id").val('-1');
		$('#'+oid).focus(); 
	}
	/**
	 * 取消选择
	 */
	function cancelSelect(oid){
		$('#'+oid).val('');
		$("#"+oid+"id").val('');
	}
	
	function getautoList(title,sfiled,url) {
		$.ajax({
			type: 'GET',
			dataType: 'json',
			url: url,
			contentType:"application/x-www-form-urlencoded",
			success: function(storemenu) {
				$("#modal-title").html(title);
				$("#okbtn").hide();
				$("#cancelbtn").hide();
				var body = '<ul class="nav nav-tabs" role="tablist">';
				var detail = ' <div class="tab-content">';
				if (storemenu != null && storemenu.length > 0) {
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
																detail += '<div class="row" style="padding-top:10px;"><div class="col-sm-12"><a class="menulink" href="javascript:void(0);" onclick="setfildValue(&quot;';
																	
																	detail +=k.id+'&quot;,&quot;'+k.name+'&quot;,&quot;'+sfiled+'&quot;,&quot;'+m.type+'&quot;)">';
																detail += k.name
																		+ '</a>';
																lett = k.pinyin;
															} else {
																detail += '<a class="menulink" href="javascript:void(0);" onclick="setfildValue(&quot;';
																detail +=k.id+'&quot;,&quot;'+k.name+'&quot;,&quot;'+sfiled+'&quot;,&quot;'+m.type+'&quot;)">';
																
																detail += k.name
																		+ '</a>';
															}

														});
										detail += '</div></div></div>';
									});
					
				}else{
					detail ='';
				}
				
				body += '</ul>' + detail;
				$("#modalList .modal-body").html(body);
				$('#modalList').modal('show');
				$("#okbtn").hide();
				$("#cancelbtn").hide();
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#modalList .modal-body").html("");
			}
		})
	}
	