
function getList(title,url,exfun) {
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
					var nfield="udf";
					
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
																	detail +=k.id+'&quot;,&quot;'+k.name+'&quot;,&quot;'+k.phone+'&quot;,'+exfun+')">';
																detail += k.name
																		+ '</a>';
																lett = k.pinyin;
															} else {
																detail += '<a class="menulink" href="javascript:void(0);" onclick="setListValue(&quot;';
																detail +=k.id+'&quot;,&quot;'+k.name+'&quot;,&quot;'+k.phone+'&quot;,'+exfun+')">';
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
	function setListValue(sid,sname,sphone,exfun){
		$("input[name='user.id']").val(sid);
		$("input[name='user.firstName']").val(sname);
		$("input[name='user.telephone']").val(sphone);
		if(exfun){
			eval(exfun());
		}
		$('#modalList').modal('hide');
	}
	/**
	 * 取消选择
	 */
	function cancelSelect(oid){
		$('#'+oid).val('');
		$("#"+oid+"id").val('');
	}
	function ok(){
		
	}
	