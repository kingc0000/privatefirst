var EditableTable = function () {

    return {

        //main function to initiate the module
        init: function () {
            function restoreRow(oTable1, nRow) {
                var aData = oTable1.fnGetData(nRow);
                var jqTds = $('>td', nRow);

                for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                    oTable1.fnUpdate(aData[i], nRow, i, false);
                }

                oTable1.fnDraw();
            }

            function editRow(oTable1, nRow) {
                var aData = oTable1.fnGetData(nRow);
                var jqTds = $('>td', nRow);
                jqTds[0].innerHTML = '<input type="text" class="form-control small" value="' + aData[1] + '"onkeyup="this.value=this.value.replace(/[^0-9.]/g,&quot;&quot;)">';
                jqTds[1].innerHTML = '<input type="text" class="form-control small" value="' + aData[2] + '"onkeyup="this.value=this.value.replace(/[^0-9.]/g,&quot;&quot;)">';
                jqTds[2].innerHTML = '<a class="save tooltips" data-placement="left" data-original-title="'+iconfirm+'" href="javascript:;"><i class="fa  fa-check"></i><span class="hidden-xs">'+iconfirm+'</span></a>';
                jqTds[3].innerHTML = '<a class="cancel tooltips" data-placement="left" data-original-title='+icancel+'  href="javascript:;"><i class="fa fa-times"></i><span class="hidden-xs">'+icancel+'</span></a>';
            }

            function saveRow(oTable1, nRow) {
                var jqInputs = $('input', nRow);
                var aData = oTable1.fnGetData(nRow);
                var data="gid="+aData[0]+"&did="+$("#sdis").val()+"&grad="+jqInputs[0].value+"&discount="+jqInputs[1].value;
                if($("#memo").val()!=null && $("#memo").val()!=""){
                	data +="&memo="+$("#memo").val();
                }
                $.ajax({
            		type : "POST",
            		url : "saveGrad.shtml",
            		data : data,
            		success : function(response) {
            			var status = response.response.status;
            			if(status==0){
            				toastr.success(issucccess);
            				oTable1.fnUpdate(jqInputs[0].value, nRow, 1, false);
                            oTable1.fnUpdate(jqInputs[1].value, nRow, 2, false);
                            oTable1.fnUpdate('<a class="edit tooltips" data-placement="left" data-original-title="'+iedit+'" href="javascript:;"><i class="fa fa-pencil"></i><span class="hidden-xs">'+iedit+'</span></a>', nRow, 3, false);
                            oTable1.fnUpdate('<a class="delete tooltips" data-placement="left" data-original-title="'+iedit+'" href="javascript:;"><i class="fa fa-trash-o "></i><span class="hidden-xs">'+iedit+'</span></a>', nRow, 4, false);
                            oTable1.fnDraw();
        					//$("#submitd").hide();
        					refreshTable();
            			}else{
            				toastr.error(isfailed);
            				restoreRow(oTable1, nEditing);
            				nEditing=null;
            			}
            			
            		},
            		error : function(xhr, textStatus, errorThrown) {
            			
            			toastr.error(isfailed);
            			restoreRow(oTable1, nEditing);
            			nEditing=null;
            		}
            	});
                
            }            

            var oTable1 = $('#editable-sample').dataTable({
               
                // set the initial value
                "bFilter": false,
                "bLengthChange": false,
                "bSort":false,
                "bPaginate": false,
                "bInfo":false,
                "aoColumnDefs": [
                                              {  "bVisible": false, "aTargets": [ 0 ] }
                                              ]
                
            });
            var nEditing = null;
            $('#editable-sample_new').click(function (e) {
            	if(nEditing!=null) return;
                e.preventDefault();
                var aiNew = oTable1.fnAddData(['', '<input type="text" class="form-control small" value=""onkeyup="this.value=this.value.replace(/[^0-9.]/g,&quot;&quot;)">', 
                                              '<input type="text" class="form-control small" value=""onkeyup="this.value=this.value.replace(/[^0-9.]/g,&quot;&quot;)">',
                        '<a class="save tooltips" data-placement="left" data-original-title="'+iconfirm+'" href="javascript:;"><i class="fa  fa-check"></i><span class="hidden-xs">'+iconfirm+'</span></a>', 
                        '<a class="cancel tooltips" data-placement="left" data-mode="new" data-original-title="'+icancel+'"  href="javascript:;"><i class="fa fa-times"></i><span class="hidden-xs">'+icancel+'</span></a>'
                ]);
                var nRow = oTable1.fnGetNodes(aiNew[0]);
                //editRow(oTable1, nRow);
                nEditing = nRow;
                //$("#submitd").hide();
            });

            $('#editable-sample').on("click","a.delete", function (e) {
                e.preventDefault();

                if (confirm(isure) == false) {
                    return;
                }

                var nRow = $(this).parents('tr')[0];
                var aData = oTable1.fnGetData(nRow);
            	$.ajax({
            		type : "POST",
            		url : "deleteGrad.shtml",
            		data : "gid="+aData[0]+"&sid="+$("#sdis").val(),
            		success : function(response) {
            			var status = response.response.status;
            			if(status==0){
            				toastr.success(issucccess);
            				oTable1.fnDeleteRow(nRow);
            			}//阶梯都删除了，设置默认为不打折
            			else if(status=9999){
            				toastr.success(issucccess);
            				oTable1.fnDeleteRow(nRow);
            				$("input[name='discount']").removeAttr('checked');
            				 $("input[name='discount']").eq(0).prop('checked',true);
 							hideAll();
            			}
            			else{
            				toastr.error(isfailed);
            			}
            			
            		},
            		error : function(xhr, textStatus, errorThrown) {
            			
            			toastr.error(isfailed);
            		}
            	});
                
                //alert("Deleted! Do not forget to do some ajax to sync with backend :)");
            });

            $('#editable-sample').on("click","a.cancel", function (e) {
                e.preventDefault();
                if ($(this).attr("data-mode") == "new") {
                    var nRow = $(this).parents('tr')[0];
                    oTable1.fnDeleteRow(nRow);
                } else {
                    restoreRow(oTable1, nEditing);
                }
                nEditing = null;
                $("#submitd").show();
            });
            
            $('#editable-sample').on("click","a.save",function(e){
            	e.preventDefault();
            	var nRow = $(this).parents('tr')[0];
            	saveRow(oTable1, nRow);
                nEditing = null;
            });

            $('#editable-sample').on("click","a.edit", function (e) {
                e.preventDefault();

                /* Get the row as a parent of the link that was clicked on */
                var nRow = $(this).parents('tr')[0];

                if (nEditing !== null && nEditing != nRow) {
                    /* Currently editing - but not this row - restore the old before continuing to edit mode */
                    restoreRow(oTable1, nEditing);
                    editRow(oTable1, nRow);
                    nEditing = nRow;
                } else {
                    /* No edit in progress - let's start one */
                    editRow(oTable1, nRow);
                    nEditing = nRow;
                }
            });
        }

    };

}();

function showGrad(dtype){
	initMansAndCates(dtype);
	$("#editable-sample_new").show();
	$("#submitd").show();
	$("#distable").show();
	$("#gdiscount").hide();
	$("#submitno").hide();
	$("#submitg").hide();
	
}

function showGeneral(dtype){
	initMansAndCates(dtype);
	$("#gdiscount").show();
	$("#editable-sample_new").hide();
	$("#submitd").hide();
	$("#distable").hide();
	$("#submitno").hide();
	$("#submitg").show();
}

function hideAll(dtype){
	initMansAndCates(dtype);
	$("#editable-sample_new").hide();
	$("#submitd").hide();
	$("#distable").hide();
	$("#gdiscount").hide();
	$("#submitno").show();
	$("#submitg").hide();
}
//如果是通用折扣要初始化pinp和类别
function initMansAndCates(dtype){
	if(typeof(storediscount) != "undefined"){
		addgroup("mans.shtml",dtype,"","manmutiselect","manslect",igetbrand);
		addgroup("cates.shtml",dtype,"","catemutiselect","cateslect",igetcategory);
	}
}

function deleteGrad(row){
	var oTable1 = $('#editable-sample').dataTable();
	var aData = oTable1.fnGetData(nRow);
	var jqInputs = $('input', nRow);
	$.ajax({
		type : "POST",
		url : "deleteGrad.shtml",
		data : "gid="+aData[0],
		success : function(response) {
			var status = response.response.status;
			if(status==0){
				toastr.success(issucccess);
			}else{
				toastr.error(isfailed);
			}
			
		},
		error : function(xhr, textStatus, errorThrown) {
			
			toastr.error(isfailed);
		}
	});
}

function refreshTable(){
	var sTable = $('#slist');
	if (sTable.length >0){
		var sttbale = sTable.dataTable();
		var oSettings = sttbale.fnSettings();
		sttbale.fnReloadAjax(oSettings);
	}
}

function save(){
	var selectR = $('input:radio:checked');
	if(selectR.val()==1 ){
		if($("#gdiscount").val()=="" || $("#gdiscount").val()==null){
			alert(idiscountempty);
			return;
		}
	}else if(selectR.val()==2){
		var oTable1 = $('#editable-sample').dataTable();
		if(oTable1.fnSettings().fnRecordsTotal() ==0){
			alert(idiscountempty);
			return;
		}
	}
	var mids= new Array();
	
	$("#ms-manmutiselect .ms-selection .ms-selected" ).each(function(i,p){
//		var pid =p.id;
//		pid=pid.replace("-selection","");
//		mids[i]=pid;
		mids[i]=$(p).data("ms-value");
	});
	$("#mdis").val(mids);
	var cids= new Array();
	$("#ms-catemutiselect .ms-selection .ms-selected" ).each(function(i,p){
		cids[i]=$(p).data("ms-value");
	});
	$("#cdis").val(cids);
	 
	 $.ajax({
			type : "POST",
			url : "save.shtml",
			data : $('#disform').serializeJson(),
			async : true,
			traditional : true,
			success : function(response) {
				var status = response.response.status;
				if(status==0){
					toastr.success(issucccess);
					refreshTable();
				}else{
					toastr.error(isfailed);
				}
				
			},
			error : function(xhr, textStatus, errorThrown) {
				
				toastr.error(isfailed);
			}
		});
	
}