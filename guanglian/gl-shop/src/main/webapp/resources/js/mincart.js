function getMiniInfo(url,cartUrl){
	$.ajax({
		type:"POST",
		url:url,
		  success: function(response){
			  writeItems(response,cartUrl);
		  }  
	});
}
function removeItems(cid){
	console.log(cartRemoveUrl);
	$.ajax({
		type:"POST",
		url:cartRemoveUrl,
		data:"cid="+cid,
		  success: function(response){
			  writeItems(response);
		  }  
	});
}

function getDiscount(discountUrl){
	$.ajax({
		type:'POST',
		url:discountUrl,
		success:function(response){
			writeDiscount(response);
		}
	});
}
function writeItems(response,cartUrl){
	if(response !=null && response.quantity >0){
		  var carthtml='<a  data-toggle="dropdown" class="dropdown-toggle  login-a" href="#" onclick="javascript:window.location.href=&quot;'+cartUrl+'&quot;"><span class="hidden-xs"></span><i class="fa  fa-shopping-cart  "></i><span class="badge bg-success">';
		  carthtml +=response.quantity+'</span></a> <ul class="dropdown-menu seft-css-menu"><li><div class="yamm-content minicart-div">';
		  carthtml +='<table class="table table-hover minicart">';
		  var gradiscountId = -1; //初始化梯度折扣id
		  $.each(response.shoppingCartItemSet,function(i, m) {
			  
			  //如果为梯度折扣
			  var discount = m.discount;
			  if (discount!=null&&(discount.type==2||discount.type==13)&&gradiscountId!=discount.id) {
				  var info = igradientDiscount;
				  var gradDiscounts = discount.gradDiscounts;
				  if (gradDiscounts!=null && gradDiscounts.length>0) {
					  var msg = '<em class="mj_green_bg">'+ifulldiscount+'</em>';
					  $.each(gradDiscounts, function(i,item){
						  msg += info.replace("{0}", item.grad).replace("{1}", Number(item.discount)*10);
						  msg += "，";
					  });
					  msg = msg.substring(0, msg.length-1);
					  carthtml += '<tr class="gradiscount"><td colspan="4">'+msg+'</td></tr>';
					  gradiscountId = discount.id; //赋值比较折扣id，如果下一条折扣记录同上一条折扣记录id一致，则不再显示说明信息
				  }
			  }
			  //carthtml +='<li><a href="#"> ';
			  var _a_url = _context + '/shop/product/' + m.productId + '.html';  
			  carthtml +='<tr>';
			  if(m.image !=null){
				  carthtml += '<td><span class="photo"><a href="'+_a_url+'"><img width="50" src="' +_context+m.image+'"></a></span></td>';
			  } else {
				  carthtml += '<td><span class="photo">&nbsp;</span></td>';
			  }
			  carthtml +='<td class="pname">'
			  carthtml +='<a href="'+_a_url+'">'+m.name+'<a/>';
			  if(m.productCode!=null) {
				  carthtml +='<br><span class="message">';
	              carthtml +=isku+ '：'+m.productCode+'</span>';
			  }
			  if(m.manuName!=null) {
				  carthtml +='<br><span class="message">';
	              carthtml +=ibrand+'：'+m.manuName+'</span>';
			  }
			  carthtml +='</td><td ><div class="pull-right marketprice-12">￥'+m.productPrice +'x' +m.quantity;
			  carthtml +='</td><td><a href="javascript:void(0);" class="extendedli" onclick="removeItems('+m.id+')">'+iremove+'</span></a></div></td><tr>';
		  });
		  carthtml += '</table></div>';
		  var bottomInfo = '<div class="cartBottom"><span class="pull-left">'+itotal+'：<span class="marketprice-12">￥'+response.total+'</span></span>&nbsp;&nbsp;&nbsp;&nbsp;<span class="pull-left">'
		  +ipay+'：<span class="marketprice-12">￥'+response.discount+'</span></span><buttom onclick="javascript:window.location.href=\''+_context+'/shop/cart/shoppingCartByCode.html\'" class="btn btn-sm theme-btn pull-right">'+igocart+'</button></div>';
		  carthtml += bottomInfo + '</li>';
		  carthtml +='</ul>';
		  $("#top_menu").html(carthtml);
		  $('#phonecart').html('<a href="'+cartUrl+'"><span class="hidden-xs"></span><i class="fa  fa-shopping-cart "></i><span class="badge bg-success">'+response.quantity+'</span></a>');
	  }else{
		  $("#top_menu").html('<a class=" login-a" href="#" onclick="javascript:window.location.href=&quot;'+cartUrl+'&quot;"><i class="fa  fa-shopping-cart forecartsize"></i><span class="badge bg-success">0</span></a>');
		  $('#phonecart').html('<a href="'+cartUrl+'"><span class="hidden-xs"></span><i class="fa  fa-shopping-cart "></i><span class="badge bg-success">0</span></a>');
		 
	  }

}

function writeDiscount(response){
	if(response !=null  ){
		  var hasDiscount=true;
		  var carthtml='<ul class="nav top-menu"> <li  class="dropdown foreadmindropmenu"><a data-toggle="dropdown" class="dropdown-toggle" href="#"> <i class="fa fa-magic"></i></a>';
		  carthtml +=' <ul class="dropdown-menu extended notification"><div class="notify-arrow notify-arrow-yellow"></div><li> <p class="yellow ">'+idiscountcurrent+'</p>';
		  carthtml +='</li>';
		  //统一折扣
		  if(response.type==1 || response.type==12){
			  carthtml +='<li><p> <a href="#">'+iuniformdiscount+'：'+response.disc+'</p></li>';
		  }else if(response.type==2 || response.type==13){
			  if(response.gradDiscounts ==null || response.gradDiscounts.length==0){
				  hasDiscount=false;
			  }else{
				  $.each(response.gradDiscounts,function(i,m){
					  carthtml+='<li><p><a href="#">'+ito+m.grad+idiscount+' : '+m.discount+'</p></li>';
				  });
			  }
		  }else{
			  hasDiscount=false;
		  }
		  if(hasDiscount==true){
			  carthtml +='</ul></li> </ul>';
			  $("#discount_menu").html(carthtml);
		  }else{
			  $("#discount_menu").html("");
		  }
		  
	  }else{
		  $("#discount_menu").html("");
	  }
}
function changeLang(){
	$.get(_context+'/shop?locale=en', function(result){
		window.location.reload();
	});
}