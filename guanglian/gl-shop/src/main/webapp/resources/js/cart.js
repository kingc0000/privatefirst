$(function(){
	$('.quantity').numeric();
    $("input[type='text']").keyup(function(e){
    	recuculc();
        if (e.which == 13){
        	e.preventDefault();	        	
        }
    });
    $("#selectall").click(function(){	
    	var isChecked = $(this).prop("checked");
    	 $(".everycheckbox").prop("checked", isChecked);
    	 $(".storecheckbox").prop("checked", isChecked);
        recuculc();
	});
    $(".storecheckbox").click(function(){	
    	var isChecked = $(this).prop("checked");
    	var storeid =$(this).parent().attr('id'); 
    	 $("input[name=check_"+storeid+"]").prop("checked", isChecked);
    	 doselectall(isChecked,storeid);
	});
    $(".everycheckbox").click(function(){
    	var storeid =$(this).attr('id');
    	doselectall($(this).prop("checked"),storeid);
    }); 

});


function doselectall(isChecked,storeid){
	var count=$(".everycheckbox").length;
	  if(isChecked == false){
		 $("#selectall").prop("checked", isChecked);
		 $("#store_"+storeid).prop("checked", isChecked);
	  }else{
	    	 var i =0;
	   	  $(".everycheckbox:checked").each(function(){
	   				  i++;
	   			});
	   	  if (count!=0 && i==count){
	   		  $("#selectall").prop("checked", isChecked);
	   		$("#store_"+storeid).prop("checked", isChecked);
	   	  }
	}
	 recuculc();
}


function dosubmit(){
	  var items=getSelect();
	  if(items !=null && items.length>0 ){
	  	document.getElementById("selecteditem").value=JSON.stringify(items);
	  	$("#checkout").submit();
	  }else {
		  alert(iselectonegoods);
	  }
	  
}

function getSelect(){
	var items = new Array();
	  var i=0;
	  $(":checkbox:checked").each(function(){
		  var item = new Object();
			  var tablerow = $(this).parent().parent("tr").find("[name='quantity']");
			  var qty = tablerow.val();
				if(qty>0) {
					var id = tablerow.attr("id");
					item.id = id;
					item.quantity = qty;
					items[i] = item;
					i++;
				}
			});
	  return items;
}
function doplus(id){
	var qu =$("#quantity_"+id).val();
	qu++;
	$("#quantity_"+id).val(qu);
	 recuculc();
}
function minus(id){
	var qu =$("#quantity_"+id).val();
	if(qu !='0'){
		qu--;
	}
	
	$("#quantity_"+id).val(qu);
	 recuculc();
}
function recuculc(){
	$('#loading').show();
	var items = getSelect();
	
	if(items !=null && items.length>0 ){
		items=JSON.stringify(items);
		$.ajax({
			type : "POST",
			url : "recuculc.shtml",
			dataType: 'json',
			data : "selected="+items,
			success : function(invoice) {
				$('#loading').hide();
				if(invoice !=null){
					$('#cdiscount').html(invoice.discount);
					$('#ctotal').html(invoice.total);
				}else{
					toastr.error(isfailed);
				}
			},
			error : function(xhr, textStatus, errorThrown) {
				$('#loading').hide();
				toastr.error(isfailed);
			}
		});
	}else{
		$('#loading').hide();
		$('#cdiscount').html(0);
		$('#ctotal').html(0);
	}
	
	
	/**
	var summeony=0.00;
	$(":checkbox:checked").each(function(){
		
		var item = new Object();
		  var tablerow = $(this).parent().parent("tr").find("[name='totalmeony']");
		  var tablequnity = $(this).parent().parent("tr").find("[name='quantity']");
		  var tableprice = $(this).parent().parent("tr").find("[name='price']");
		  var qty = tablerow.text();
		  if(null != qty && qty.length>0){
			  // Remove non-numeric chars (except decimal point/minus sign):
			    var priceVal = parseFloat(tableprice.text().replace(/[^0-9-.]/g, '')); // 12345.99
			    var everytotal = priceVal*tablequnity.val();
			    tablerow.text(everytotal.formatMoney());
			  summeony += everytotal;
			  //i++;
		  }	
		});
	var totalmoneys = document.getElementsByName("totalmoneyculc");
	
	if(totalmoneys !=null) {
		for(var j = 0; j< totalmoneys.length; j++) {
			totalmoneys[j].innerHTML='<strong >' + summeony.formatMoney() +'</strong >';
		} 
	}*/
};

function updateLineItem(lineItemId,actionURL){
	var rdel = confirm(icartremove);
	if (rdel == false)
		return;
	$("#shoppingCartLineitem_"+lineItemId).attr('action', actionURL);
	$( "#shoppingCartLineitem_"+lineItemId).submit();	
}

Number.prototype.formatMoney = function (places, symbol, thousand, decimal) {
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    symbol = symbol !== undefined ? symbol : "￥";
    thousand = thousand || ",";
    decimal = decimal || ".";
    var number = this,
        negative = number < 0 ? "-" : "",
        i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
    return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};