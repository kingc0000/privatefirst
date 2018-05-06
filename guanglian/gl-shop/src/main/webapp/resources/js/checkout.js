
function addinvoice(){
	$('#ititle').html(inew+iinvoice);
	cleariForm();
	setnomal();
	$('#iModal').modal('show');
}
function cleariForm(){
	var iid = $('#icontactid').val();
	$(':input', '#iform').not(':button, :submit, :reset').val('')
	.removeAttr('checked').removeAttr('selected');
	$('#icontactid').val(iid);
}
function addaddress(){
	$('#atitle').html(inew+iaddress);
	clearForm();
	$('#aModal').modal('show');
}

function clearForm(){
	var cid = $('#contactid').val();
	$(':input', '#aform').not(':button, :submit, :reset').val('')
	.removeAttr('checked').removeAttr('selected');
	$('#contactid').val(cid);
}

function editinvoice(iid){
	cleariForm();
	$('#loading').show();
	$('#atitle').html(imodify+iinvoice);
	$.ajax({
		type : "POST",
		url : "invoice.shtml",
		data : "iid="+iid,
		success : function(invoice) {
			$('#loading').hide();
			if(invoice !=null && invoice !=null){
				$("#icompany").val(invoice.billing.company);
				$("#billingtype").val(invoice.billing.type);
				$("#imemo").val(invoice.memo);
				$("#iid").val(invoice.id);
				if(invoice.billing.type==1){
					$("#companyAddress").val(invoice.billing.companyAddress);
					$("#companyTelephone").val(invoice.billing.companyTelephone);
					$("#bankName").val(invoice.billing.bankName);
					$("#bankAccount").val(invoice.billing.bankAccount);
					$("#taxpayerNumber").val(invoice.billing.taxpayerNumber);
					setspecial();
				}
				if(invoice.isdefault==true){
					$('#iisdefault').prop('checked',true);
				}else{
					$('#iisdefault').prop('checked',false);
				}
				$('#iModal').modal('show');
			}else{
				alert(isfailed);
			}
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			alert(isfailed);
		}
	});
	
}

function editaddress(aid){
	clearForm();
	$('#loading').show();
	$('#atitle').html(imodify+iaddress);
	$.ajax({
		type : "POST",
		url : "addres.shtml",
		data : "aid="+aid,
		success : function(address) {
			$('#loading').hide();
			if(address !=null && address !=""){
				$("#name").val(address.delivery.name);
				$("#zone").val(address.delivery.zone.description.name);
				$("#city").val(address.delivery.city);
				$("#address").val(address.delivery.address);
				$("#telephone").val(address.delivery.telephone);
				$("#company").val(address.delivery.company);
				$(".amemo").val(address.memo);
				$("#postal").val(address.delivery.postal);
				$("#aid").val(address.id);
				$(".aid").val(address.id);
				$("#zoneid").val(address.delivery.zone.id);
				if(address.isdefault==true){
					$('#isdefault').prop('checked',true);
				}else{
					$('#isdefault').prop('checked',false);
				}
				$('#aModal').modal('show');
			}else{
				alert(isfailed);
			}
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			alert(isfailed);
		}
	});
	
}
function choiseinvoice(iid){
	var castr='<a href="javascript:void(0);" onclick="editinvoice('+iid+')">'+$('#cinv_'+iid).html()+'</a>';
	$('#idefaultout').html(castr);
}
function choiseaddress(aid){
	var castr='<a href="javascript:void(0);" onclick="editinvoice('+aid+')">'+$('#cadd_'+aid).html()+'</a>';
	$('#adefaultout').html(castr);
}
function isubmit(url){
	$('#loading').show();
	$.ajax({
		type : "POST",
		url : url,
		data : $('#iform').serializeJson(),
		async : true,
		traditional : true,
		success : function(response) {
			$('#loading').hide();
			var status = response.response.status;
			if (status == 0 || status == 9999) {
				//设置开票信息
				var astr=$("#icompany").val()
				if($('#billingtype').val()==1){
					astr +='&nbsp;'+$("#companyAddress").val()+'&nbsp;'+$("#companyTelephone").val()+'&nbsp;'+$("#bankName").val();
				}
				astr+='&nbsp;'+$("#imemo").val();
				astr ='<input type="hidden" name="iselect" id="iselect" value="'+response.response.id+'"/><a href="javascript:void(0);" onclick="editinvoice('+response.response.id+')">' +astr + '</a>';
				$('#idefaultout').html(astr);
				getiaall();
			}
			$('#iModal').modal('hide');
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			alert(isfailed);
			$('#iModal').modal('hide');
		}
	});
}

function asubmit(url){
	$('#loading').show();
	$.ajax({
		type : "POST",
		url : url,
		data : $('#aform').serializeJson(),
		async : true,
		traditional : true,
		success : function(response) {
			$('#loading').hide();
			var status = response.response.status;
			if (status == 0 || status == 9999) {
				//设置邮寄地址
				var astr=$("#name").val()+'&nbsp;'+$("#zone").val()+'&nbsp;'+$("#city").val()+'&nbsp;'+$("#address").val()+'&nbsp;'+$("#telephone").val()+'&nbsp;'+$("#company").val()+'&nbsp;'+$(".amemo").val();
				astr ='<input type="hidden" name="aselect" id="aselect" value="'+response.response.id+'"/><a href="javascript:void(0);" onclick="editaddress('+response.response.id+')">' +astr + '</a>';
				$('#adefaultout').html(astr);
				getaall();
			}
			$('#aModal').modal('hide');
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			alert(isfailed);
			$('#aModal').modal('hide');
		}
	});
}
function iisdefault(aid){
	$('#loading').show();
	$.ajax({
		type : "POST",
		url : _context+'/shop/customer/invoice/setdrfault.shtml',
		data : 'listId='+aid,
		async : true,
		traditional : true,
		success : function(response) {
			$('#loading').hide();
			var status = response.response.status;
			if(status==9999){
				 toastr.success(issucccess);
				 getiaall();
			 }else{
				 toastr.success(isfailed);
			 }
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			toastr.success(isfailed);
		}
	});
}

function aisdefault(aid){
	$('#loading').show();
	$.ajax({
		type : "POST",
		url : _context+'/shop/customer/address/setdrfault.shtml',
		data : 'listId='+aid,
		async : true,
		traditional : true,
		success : function(response) {
			$('#loading').hide();
			var status = response.response.status;
			if(status==9999){
				 toastr.success(issucccess);
				 getaall();
			 }else{
				 toastr.success(isfailed);
			 }
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			toastr.success(isfailed);
		}
	});
}

function getaall(){
	$('#loading').show();
	$.ajax({
		type : "POST",
		url : _context+'/shop/customer/address/alladdress.shtml',
		success : function(response) {
			$('#loading').hide();
			if(response !=null){
				var alist = '';
				$.each(response, function( i, m ){
					alist +='<li><div class="task-title"><span class="task-title-sp"><a href="javascript:void(0);" onclick="choiseaddress('+m.id+')">';
					alist +='<span id="cadd_'+m.id+'">'+m.name+'&nbsp;'+m.zoneName+'&nbsp;'+m.city+'&nbsp;'+m.address+'&nbsp;';
					alist +=m.telephone+'&nbsp;'+m.company+'</span></a></span>';
					if(m.isdefault==false){
						alist +='<div class="pull-right hidden-phone"><button class="btn btn-success btn-xs" onclick="aisdefault('+m.id+')"><i class=" fa fa-dot-circle-o"></i>'+isetdefault+'</button></div>';
					}
					alist +='</div></li>';
				});
				$('#alist').html(alist);
			}
			
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			$('#alist').html('');
		}
	});
}
function getiaall(){
	$('#loading').show();
	$.ajax({
		type : "POST",
		url : _context+'/shop/customer/invoice/allinvoice.shtml',
		success : function(response) {
			$('#loading').hide();
			if(response !=null){
				var alist = '';
				$.each(response, function( i, m ){
					alist +='<li><div class="task-title"><span class="task-title-sp"><a href="javascript:void(0);" onclick="choiseinvoice('+m.id+')">';
					alist +='<span id="cinv_'+m.id+'">'+m.company;
					if(m.type==1){
						alist +='&nbsp;'+m.companyAddress+'&nbsp;'+m.city+'&nbsp;'+m.companyTelephone+'&nbsp;'+m.bankName;
					}
					alist +='</span></a></span>';
					if(m.isdefault==false){
						alist +='<div class="pull-right hidden-phone"><button class="btn btn-success btn-xs" onclick="iisdefault('+m.id+')"><i class=" fa fa-dot-circle-o"></i>'+isetdefault+'</button></div>';
					}
					alist +='</div></li>';
				});
				$('#ilist').html(alist);
			}
			
		},
		error : function(xhr, textStatus, errorThrown) {
			$('#loading').hide();
			$('#ilist').html('');
		}
	});
}
function ishowmore(){
	if($('#ishow').hasClass("fa-chevron-down")){
		$('#ishow').removeClass("fa-chevron-down");
		$('#ishowlist').show();
		$('#ishow').addClass("fa-chevron-up");
		
	}else {
		$('#ishow').removeClass("fa-chevron-up");
		$('#ishowlist').hide();
		$('#ishow').addClass("fa-chevron-down");
	}
}
function ashowmore(){
	if($('#ashow').hasClass("fa-chevron-down")){
		$('#ashow').removeClass("fa-chevron-down");
		$('#ashowlist').show();
		$('#ashow').addClass("fa-chevron-up");
		
	}else {
		$('#ashow').removeClass("fa-chevron-up");
		$('#ashowlist').hide();
		$('#ashow').addClass("fa-chevron-down");
	}
}
function setnomal(){
	$("#invoicetype").hide();
	$("#companytitle").html(iinvoicetitle);
	$("#company").attr('placeholder',iinvoicetitle);
	$("#billingtype").val(0);
}

function setspecial(){
	$("#invoicetype").show();
	$("#companytitle").html(icompany);
	$("#company").attr('placeholder',icompany);
	$("#billingtype").val(1);
}
function sumitorder(){
	var rdel=confirm(iordersubmit);
	if(rdel == false) return;
	if($('#aselect').length<0 ){
		alert(iaddressselect);
		return ;
	}
	if($('#iselect').length<0){
		alert(iinvoiceselect);
		return;
	}
	$('#loading').show();
	$("#checkoutForm").submit();
}