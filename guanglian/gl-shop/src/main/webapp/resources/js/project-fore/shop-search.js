document.write('<script src="'+_context+'/resources/js/project/toastr.js"></script>');
var conditionCateIds;
var manufacturers;
var isMuti=0; //是否多选，0为非多选，1为多选
var _more = new Array();
/**
 * 查询商品
 * @param url 请求参数
 * @param pname 提示
 */
function dosearch(pname, _context) { 
	var url = _context + "/services/public/search/products/list.shtml";
	loadProducts(url, getcriteria(), pname, _context);	
}
/**
 * 构造查询参数
 * @returns {String}
 */
function getcriteria(){
	var q =$("#searchField").val();
	var criteria = "1=1";
	if(q!=null && $.trim(q)!='') {
		criteria = "&q="+q;
	} 	
	//分类ID
	if (categoryIds != null  && categoryIds !=""){
		criteria += '&categoryId='+categoryIds;
	}
	//分类集合ID（过滤）
	if (conditionCateIds != null  && conditionCateIds !=""){
		criteria+='&conditionCateIds='+conditionCateIds;
	}
	
	if(manufacturers !=null && manufacturers.length>0){
		criteria += '&manufacturers=' + manufacturers ;	
	}
	return criteria;
}
/**
 * 向服务端发起商品集合查询请求
 * @param url 请求的url
 * @param data 传递的json参数
 * @param pname 提示信息
 * @param _context
 * @showntype 展示模式，showntype=1为图表模式，showntype=0为table模式
 */
function loadProducts(url,data,pname, _context) {
	//$("#pageContainer").showLoading();
	$.ajax({
  			cache: false,
  			type: 'POST',
			dataType: "json",
  			url: url,
  			data:data,
			success: function(productList) {
				var breadCrumb = null;
				if(productList != null && productList.products != null && productList.products.length>0){
					if(showntype==1){
						$(".filtered-list").show();
						buildSearchProductsList(productList.products, _context);
						writePtitle(productList.paginationData.offset, productList.paginationData.countByPage, productList.paginationData.totalCount, ["pageInfo"]);
						writePaging(productList.paginationData, 'common');
						writefilterPanel('manu', productList.manufacturerAggsList);
						writefilterPanel('cate', productList.categoryAggsList);
						writeSearchPrices(productList.products);
						writeGradiscount_table(productList.products);
						breadCrumb = productList.breadCrumb;
						$(".product-list").show();
					} else {
						$(".filtered-list").show();
						buildSearchProductsList_table(productList.products, _context);
						writePtitle(productList.paginationData.offset, productList.paginationData.countByPage, productList.paginationData.totalCount, ["pageInfo"]);
						writePaging(productList.paginationData, 'common');
						writefilterPanel('manu', productList.manufacturerAggsList);
						writefilterPanel('cate', productList.categoryAggsList);
						writeSearchPrices_table(productList.products);
						writeGradiscount_table(productList.products);
						breadCrumb = productList.breadCrumb;
						$(".product-list").show();
					}
				}else{
					$(".filtered-list").hide();
					$(".product-list").html('');
					$(".product-list").hide();
					writePtitle(0, 0, 0, ["pageInfo"]);
				}
				if (breadCrumb!=null) {
					writeCategoryBread(breadCrumb.breadCrumbs);
				} else {
					writeSearchBread(pname, _context);
				}
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#pageContainer").hideLoading();
			}
			
	});
}
//异步获取商品价格，针对热门商品展示模式
function writePrices(productList) {
	var url = _context + "/shop/product/displayPrices.shtml";
	if (productList != null && productList.length>0) {
		//构造商品id集合
		var pids = new Array();
		$.each(productList, function(i, p) {
			pids[i] = p.id;
		});
		$.ajax({
  			cache: false,
  			type: 'POST',
			dataType: "json",
  			url: url,
  			data:"pids="+pids,
			success: function(productList) {
				if (productList != null && productList.length>0) {
					$.each(productList, function(i, p){
						if (p.prices != null&&p.prices.length>0) {
							var _img_div = $("#pro-img-box-"+p.id);
							var _a_cart = $('<a href="#" class="adtocart" onclick="addCarts(\''+p.id+'\',\''+p.prices[0].id+'\',\'1\')"><i class="fa fa-shopping-cart"></i></a>');
							if(p.prices[0].title!=null &&p.prices[0].title!=""){
								var str='<span class="marketprice-del marketprice-12">￥' +p.prices[0].originalPrice+'</span>&nbsp;&nbsp;<span class="specialprice-12">￥'+p.prices[0].finalPrice+'</span>';
								$("#p_"+p.id).html(str);
								//添加购物车按钮
								_img_div.append(_a_cart);
							} else{
								var price_market = irequiredprice;
								if (p.prices[0].originalPrice!=null&&p.prices[0].originalPrice>0) {
									price_market = '￥'+p.prices[0].originalPrice;
									$("#p_"+p.id).html('<span class="marketprice-12">'+price_market+'</span>');
									//添加购物车按钮
									_img_div.append(_a_cart);
								}else{
									price_market='<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin='+_qq+'&site=qq&menu=yes">'+irequiredprice+'</a>';
									$("#p_"+p.id).html('<span class="marketprice-12">'+price_market+'</span>');
								}
								
								
							}
						}
					});
				}
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				toastr.error(textStatus);
			}
			
	});
	}
}
/**
 * 根据查询返回结果显示商品列表，针对热门商品的橱窗模式
 * @param productList
 * @returns
 */
function buildProductsList(productList, _context) {
	$(".product-list").html('');

	if (productList != null && productList.length > 0) {
		$.each(productList, function(i, p){
			var purl = _context + "/shop/product/" + p.id + ".html";
			var _div_wrap = $('<div class="col-md-3 col-sm-6"></div>');
			var _section = $('<section class="panel"></section>');
			
			var _img_div = $('<div class="pro-img-box" id="pro-img-box-'+p.id+'"></div>');
			var _img = $('<a href="'+purl+'" target="'+_popenMode+'"><div class="overflow-hidden"><img src="'+_context + p.imageSrc+'" alt="'+p.description.name+'"/><div></a>');
			//var _a_cart = $('<a href="#" class="adtocart" onclick="addCart(\''+p.id+'\')"><i class="fa fa-shopping-cart"></i></a>');
			//_img_div.append(_img).append(_a_cart);
			_img_div.append(_img);
			
			var _info_div = $('<div class="panel-body text-center"></div>');
			var _div_title = $('<div class="div-title"></div>');
			var _a_title = $('<a href="'+purl+'" class="pro-title" target="'+_popenMode+'" title="'+p.description.name+'"><span>'+p.description.name+'</span></a>');
			_div_title.append(_a_title);
			var _p_price = $('<p class="price" id="p_'+p.id+'"></p>');
			
			var manuName = p.manufacturer!=null?p.manufacturer.description.name:"";
			var _div_manufacture = $('<div class="pro-info pull-left">'+manuName+'</div>');
			var _div_code = $('<div class="pro-info pull-left">'+p.sku+'</div>');
			
			_info_div.append(_div_title).append(_p_price).append(_div_manufacture).append(_div_code);
			
			_div_wrap.append(_section.append(_img_div).append(_info_div));
			_div_wrap.appendTo($(".product-list"));
		});
		$(".product-list").show();
	}
}

//点击搜索操作下的面包屑导航展示
function writeSearchBread(pname, _context){
	var tbBody='<li><a href="'+_context+'">'+ihome+'</a></li>';	
	var sSearch = $("#searchField").val()===''?'':'<span style="color:#777;padding:0px 5px;">["'+$("#searchField").val()+'"]</span>';
	
	tbBody+='<li class="active">'+iall+sSearch+iresults+'</li>'; 
	if(pname != null && pname !='' && pname!=(iall+iresults)){
		tbBody+='<li class="active"><a href="javascript:void(0);" ">'+pname+'</a></li>'; 
	}
	$("#breadcrumb").html(tbBody);
}
//点击分类操作下的面包屑导航展示
function writeCategoryBread(bread){
	var tbBody='';
	if(bread != null){
		$.each(bread, function(i, p) {
			tbBody+='<li><a href="'+p.url+'">'+p.label+'</a></li>';
		});
	}
	$("#breadcrumb").html(tbBody);
}
//输出分页描述信息
function writePtitle(offset,countByPage,totalCount, target){
	$.each(target, function(i, pagetitle){
		if (totalCount==null || totalCount==0) {
			$("#page-panel-body").hide();
			$("#no-result-info").show();
		} else {
			$("#page-panel-body").show();
			$("#no-result-info").hide();
			var info = iview+"&nbsp;&nbsp;"+offset+"&nbsp;&nbsp;"+ito+"&nbsp;&nbsp;"+ countByPage+"&nbsp;&nbsp;"+idatas+"，"+itotal+"&nbsp;&nbsp;"+totalCount+"&nbsp;&nbsp;"+idatas;
			$("[id='"+pagetitle+"']").html(info);
		}
	});
}
//翻页操作
function doAction(pagenum, type){
	if (type=='hots') {
		loadProductHots('page=' + pagenum);
	} else {
		page = pagenum;
		var criteria = getcriteria() ;
		criteria += '&page=' + pagenum ;
		var url = _context + "/services/public/search/products/list.shtml";
		loadProducts(url, criteria, "", _context);
	}
}
//输出分类聚合信息
function writeCategoryPanel(url, categoryList) {
	$("#cateContent").html("");
	if(categoryList != null) {
		$.each(categoryList, function(i, m){
			var s = $("<a class='btn btn-sm'></a>");
			s.html(m.name+"&nbsp;("+m.count+")").attr("cate_id", m.key);
			s.bind("click", function(event) {
				if (categoryIds != null  && categoryIds !=""){
					categoryIds = $(this).attr("cate_id");
				} else {
					conditionCateIds[0] = $(this).attr("cate_id");
					//添加选中的品牌过滤的显示
					$("#categoryFilter").html('<div id="choosedCate_'+m.key+'" class="choosedFilter">'+icategory+'：<em>'+m.name+'&nbsp;&nbsp;<span class="glyphicon glyphicon-remove ico-style" aria-hidden="true"/></em></div>');
					//定义选中品牌的删除事件
					$("#choosedCate_"+m.key).bind("click", function(){
						$(this).remove();	
						conditionCateIds = new Array();
						loadProducts(url, getcriteria(), "", _context);
					});
				}
				loadProducts(url, getcriteria(), "", _context); //重新获取商品
				//$("#breadcrumb:nth-child(1)").append("");
			});
			$("#cateContent").append(s);
		});
	}
	//处理是否显示更多按钮
	if($("#cateContent").height()>34) {
		$("#catediv").css("display", "block");
	} else {
		$("#catediv").css("display", "none");
	}
}
//输出品牌聚合信息
function writeManufacturerPanel(url, manufacturerList) {
	$("#manuContent").html("");
	if(manufacturerList != null) {
		$.each(manufacturerList, function(i, m){
			var s = $("<a class='btn btn-sm'></a>");
			s.html(m.name+"&nbsp;("+m.count+")").attr("manu_id", m.key);
			s.bind("click", function(event) {
				manufacturers[0] = $(this).attr("manu_id"); //添加品牌ID过滤条件 
				var criteria = getcriteria() ;
				loadProducts(url, criteria, "", _context); //重新获取商品
				//添加选中的品牌过滤的显示
				$("#manufactureFilter").html('<div id="choosedManu_'+m.key+'" class="choosedFilter">'+ibrand+'：<em>'+m.name+'&nbsp;&nbsp;<span class="glyphicon glyphicon-remove ico-style" aria-hidden="true"/></em></div>');
				//定义选中品牌的删除事件
				$("#choosedManu_"+m.key).bind("click", function(){
					$(this).remove();	
					manufacturers = new Array();
					loadProducts(url, getcriteria(), "", _context);
				});
			});
			$("#manuContent").append(s);
		});
	}
	//处理是否显示更多按钮
	if($("#manuContent").height()>=34) {
		$("#manudiv").css("display", "block");
	} else {
		$("#manudiv").css("display", "none");
	}
}
function showMore(panel){
	$("#"+panel).toggleClass("search_auto_height");
}
/**
 * Function used for adding a product to the Shopping Cart
 */
function addCarts(productIds, priceIds, quantities){
	$.ajax({  
		 type: 'POST',  
		 url: _context + '/shop/cart/addShoppingCartItems.shtml',  
		 data: "productIds="+productIds+"&priceIds="+priceIds+"&quantities="+quantities, 
		 dataType: "json",
		 error: function(xhr, textStatus, errorThrown) { 
			 toastr.error(isfailed);
		 },
		 success: function(response) {
			 var status = response.response.status;
			 if(status==0){
				 var murl=_context +'/shop/cart/cartinfo.shtml';
				 var curl=_context +'/shop/cart/shoppingCartByCode.html';
				 toastr.success(icartadd);
				 getMiniInfo(murl,curl);
			 }else if(status==-1){
				 toastr.error(isfailed);
			 //未登录，跳转登陆页面
			 }else{
				 toastr.error(iloginfirst);
				 window.location.href=_context + '/shop/customer/logon.html';
			 }
		 } 
	});
}
/**
 * 加载热门商品
 * @param data
 */
function loadProductHots(data) {
	var url = _context + "/shop/products/displayHots.shtml";
	$.ajax({
			cache: false,
			type: 'POST',
			dataType: "json",
			url: url,
			data:data,
			success: function(productList) {
				if(productList != null && productList.products != null && productList.products.length>0){
					buildProductsList(productList.products, _context);
					writePtitle(productList.paginationData.offset, productList.paginationData.countByPage, productList.paginationData.totalCount, ["pageInfo"]);
					writePaging(productList.paginationData, 'hots');
					writePrices(productList.products);
				}else{
					writePtitle(0, 0, 0, ["pageInfo"]);
					$(".product-list").hide();
				}
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#pageContainer").hideLoading();
			}
		
	});
}

//查询商品列表异步获取商品价格，针对图文模式
function writeSearchPrices(productList) {
	var url = _context + "/shop/product/displayPrices.shtml";
	if (productList != null && productList.length>0) {
		//构造商品id集合
		var pids = new Array();
		$.each(productList, function(i, p) {
			pids[i] = p.id;
		});
		$.ajax({
  			cache: false,
  			type: 'POST',
			dataType: "json",
  			url: url,
  			data:"pids="+pids,
			success: function(productPriceList) {
				if (productPriceList != null && productPriceList.length>0) {
					$.each(productPriceList, function(i, p){
						var _a_cart = $('<li><a href="#" class="btn btn-default btn-xs btn-active" onclick="addCarts(\''+p.id+'\',\''+p.prices[0].id+'\',\'1\')"><i class="fa fa-shopping-cart"></i></a></li>');
						if(p.prices[0]!=null){
							var price_name = $('<li>'+ipricename+'：'+p.prices[0].name+'</li>'); //规格
							$("#priceset_"+p.id).append(price_name);
							if (p.prices[0].period!=null&&p.prices[0].period!="") {
								var price_period = $('<li>'+ipriceperiod+'：' + p.prices[0].period+'</li>');//供货周期
								$("#priceset_"+p.id).append(price_period);
							}
							if(p.prices[0].title!=null &&p.prices[0].title!=""){
								var price_market = '<li><span class="marketprice-12 marketprice-del">'+'￥'+p.prices[0].originalPrice+'</span>&nbsp;&nbsp;';
								var price_special = '<span class="specialprice-12">'+'￥'+p.prices[0].finalPrice+'</span></li>';
								//添加购物车按钮
								$("#priceset_"+p.id).append(price_market+price_special).append(_a_cart);
							} else {
								var price_market ;
								if (p.prices[0].originalPrice!=null&&p.prices[0].originalPrice>0) {
									price_market = "￥"+ p.prices[0].originalPrice;
									var price_market_li = $('<li>'+imarketprice+'：'+'<span class="marketprice-12">'+price_market+'</span>'+'</li>');
									$("#priceset_"+p.id).append(price_market_li);
									$("#priceset_"+p.id).append(_a_cart);
								}else{
									price_market='<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin='+_qq+'&site=qq&menu=yes">'+irequiredprice+'</a>';
									var price_market_li = $('<li>'+imarketprice+'：'+'<span class="marketprice-12">'+price_market+'</span>'+'</li>');
									$("#priceset_"+p.id).append(price_market_li);
								}
							}
							if(p.prices.length>1) {
								var purl = _context + "/shop/product/" + p.id + ".html";
								$("#priceset_"+p.id).append('<li><a href="'+purl+'" target="'+_popenMode+'" title="'+imore+'">'+imore+'<span class="fa fa-ellipsis-h"></span></a></li>');
							}
						}
						
					});
				}
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				toastr.error(textStatus);
			}
			
	});
	}
}
//查询商品列表异步获取商品价格，针对table模式
function writeSearchPrices_table(productList) {
	var url = _context + "/shop/product/displayPrices.shtml";
	if (productList != null && productList.length>0) {
		//构造商品id集合
		var pids = new Array();
		$.each(productList, function(i, p) {
			pids[i] = p.id;
		});
		$.ajax({
  			cache: false,
  			type: 'POST',
			dataType: "json",
  			url: url,
  			data:"pids="+pids,
			success: function(productPriceList) {
				if (productPriceList != null && productPriceList.length>0) {
					$.each(productPriceList, function(i, p){ 
						if(p.prices[0]!=null){
							var _a_cart = $('<a href="#" class="btn btn-default btn-xs btn-active" onclick="addCarts(\''+p.id+'\',\''+p.prices[0].id+'\',\'1\')"><i class="fa fa-shopping-cart"></i></a>');
							$("#spec_"+p.id).append(p.prices[0].name);
							if(p.prices[0].title!=null &&p.prices[0].title!=""){
								var price_market = '<li><span class="marketprice-12 marketprice-del">'+'￥'+p.prices[0].originalPrice+'</span>&nbsp;&nbsp;';
								var price_special = '<span class="specialprice-12">'+'￥'+p.prices[0].finalPrice+'</span></li>';
								$("#priceset_"+p.id).append(price_market+price_special);
								//添加购物车按钮
								$("#cart_"+p.id).append(_a_cart);
							} else {
								var price_market ;
								if (p.prices[0].originalPrice!=null&&p.prices[0].originalPrice>0) {
									price_market = "￥"+ p.prices[0].originalPrice;
									var price_market_li = $('<span class="marketprice-12">'+price_market+'</span>');
									$("#priceset_"+p.id).append(price_market_li);
									$("#cart_"+p.id).append(_a_cart);
								}else{
									price_market='<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin='+_qq+'&site=qq&menu=yes">'+irequiredprice+'</a>';
									var price_market_li = $('<span class="marketprice-12">'+price_market+'</span>');
									$("#priceset_"+p.id).append(price_market_li);
								}
								
							}
							if(p.prices.length>1) {
								var purl = _context + "/shop/product/" + p.id + ".html";
								$("#priceset_"+p.id).append('<li><a href="'+purl+'" target="'+_popenMode+'" title="'+imore+'">'+imore+'<span class="fa fa-ellipsis-h"></span></a></li>');
							}
						}
					});
				}
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				toastr.error(textStatus);
			}
			
	});
	}
}
/**
 * 查询商品梯度折扣优惠信息
 * @param productList
 */
function writeGradiscount_table(productList) {
	if (productList != null && productList.length>0) {
		//构造商品id集合
		var pids = new Array();
		$.each(productList, function(i, p) {
			pids[i] = p.id;
		});
		$.post(_context + "/shop/product/gradientDiscount.shtml", "pids="+pids, function(result){
			if(result!=null&&result!=""&&result.length>0) {
				$.each(result, function(i, item){
					console.info(item);
					$("#mj_"+item.id).addClass("display-inline");
					$("#mj_"+item.id).attr("title", item.description);
				});
			}
		});
	}
}
/**
 * 查询商品列表，根据查询返回结果显示商品列表，为图文模式
 * @param productList
 * @returns
 */
function buildSearchProductsList(productList, _context) {
	$(".product-list").html('');
	
	if (productList != null && productList.length > 0) {
		$.each(productList, function(i, p){
			var purl = _context + "/shop/product/" + p.id + ".html";
			var _product_href = '<a href="'+purl+'" target="'+_popenMode+'">'+p.description.name+'</a>'
			var _product_item = $('<div class="product-item"></div>');
			var _section = $('<section class="panel"></section>');
			var _name = $('<h3 class="product-name"></h3>');
			_name.append(_product_href+'<em class="mj_red_bg display-none mj_list" id="mj_'+p.id+'">'+ifulldiscount+'</em>'); //增加满减折扣标志
			_product_item.append(_name);
			
			var _row = $('<div class="row"></div>');
			var _img_url = p.imageSrc;
			if(_img_url==null||_img_url=="") _img_url = "/resources/img/product.jpg";
			//处理商品图片div
			var _image_a = '<div class="product-pic"><a href="'+purl+'" target="'+_popenMode+'"><img src="'+_context + _img_url+'"/></a></div>';
			var _image_div = $('<div class="col-xs-4 col-md-2">'+_image_a+'<div class="product-pic"></div></div>');
			_row.append(_image_div); //图片div
			
			//处理商品信息div
			var _basic_div = $('<div class="product-basic"></div>');
			var _row_1 = $('<div class="row"></div>');
			var _basic_1_ul = $('<ul></ul>');
			
			if (p.sku!=null) { //货号
				_basic_1_ul.append('<li>'+isku+'：'+p.sku+'</li>');
			}
			if (p.cas!=null) { //cas
				_basic_1_ul.append('<li>'+icas+'：'+p.cas+'</li>');
			}
			var manuName = p.manufacturer!=null?p.manufacturer.description.name:"";
			if (manuName!="") { //品牌
				_basic_1_ul.append('<li>'+ibrand+'：'+manuName+'</li>');
			}
			_row_1.append($('<div class="col-sm-6"></div>').append(_basic_1_ul));
			
			//处理商品价格ul，价格采用异步加载
			var _basic_2_ul = $('<ul id="priceset_'+p.id+'"></ul>');
			
			_row_1.append($('<div class="col-sm-6"></div>').append(_basic_2_ul));
			_basic_div.append(_row_1);
			//处理商品简单描述
			if (p.summary!=null) {
				var _row_2 = $('<div class="row"></div>');
				_row_2.append('<div class="col-sm-12 product-info">'+p.summary+'</div>');
				_basic_div.append(_row_2);
			}

			_row.append($('<div class="col-xs-8 col-md-10">').append(_basic_div));
			_product_item.append(_row);
			_product_item.appendTo($(".product-list"));
		});
	}
}
/**
 * 查询商品列表，根据查询返回结果显示商品列表，为table模式
 * @param productList
 * @param _context
 */
function buildSearchProductsList_table(productList, _context) {
	$(".product-list").html('');
	
	if (productList != null && productList.length > 0) {
		var _title = $('<div class="row title hidden-xs"></div>');
		_title.append('<div class="col-sm-2">'+isku+'/'+icas+'</div>').append('<div class="col-sm-3">'+iname+'</div>').append('<div class="col-sm-2">'+ibrand+'</div>')
		.append('<div class="col-sm-2">'+ipricename+'</div>').append('<div class="col-sm-2">'+iprice+'</div>').append('<div class="col-sm-1">'+'</div>');
		_title.appendTo($(".product-list"));
		
		$.each(productList, function(i, p){
			var purl = _context + "/shop/product/" + p.id + ".html";
			var _product_href = '<a href="'+purl+'" target="'+_popenMode+'">'+p.description.name+'</a>'
			//编码/CAS+名称+品牌+规格+价格+购买
			var _product_item = $('<div class="product-item-table row"></div>');
			
			//code
			var _code = $('<div class="col-sm-2"></div>');
			var codeVal = p.sku;
			if (p.cas!=undefined&&p.cas!='') {
				codeVal += "&nbsp;&nbsp;/&nbsp;&nbsp;" + p.cas;
			}
			_code.html(codeVal);
			//name
			var _name = $('<div class="col-sm-3"></div>');
			_name.append(_product_href+'<em class="mj_red_bg display-none mj_list" id="mj_'+p.id+'">'+ifulldiscount+'</em>'); //增加满减折扣标志
			//manufacturer
			var _manuName = $('<div class="col-sm-2"></div>');
			var manuName = p.manufacturer!=null?p.manufacturer.description.name:"";
			_manuName.html(manuName);
			//specification
			var _spec = $('<div class="col-sm-2"></div>');
			var specName = $('<span id="spec_'+p.id+'"></span>');
			_spec.append(specName);
			
			//处理商品价格ul，价格采用异步加载
			var _prices = $('<div class="col-sm-2"></div>');
			var _basic_2_ul = $('<ul id="priceset_'+p.id+'"></ul>');
			_prices.append(_basic_2_ul);
			
			var _cart = $('<div class="col-sm-1"></div>');
			_cart.append('<span id="cart_'+p.id+'"></span>');
			
			_product_item.append(_code).append(_name).append(_manuName).append(_spec).append(_prices).append(_cart);
			_product_item.appendTo($(".product-list"));
		});
	}
}
/**
 * 输出聚合信息
 * @panel 表示对那个聚合进行处理，目前是对品牌manu，分类cate进行聚合
 */
function writefilterPanel(panel, aggsList) {
	var l = $("#filtered-"+panel);
	l.html("");
	if(aggsList != null) {
		$.each(aggsList, function(i, m){
			var s = $('<a id="'+m.key+'" onclick="addFilter(\''+m.key+'\',\''+m.name+'\',\''+panel+'\')" class="btn-white btn-xs"></a>');
			s.html(m.name+"&nbsp;("+m.count+")").attr("manu_id", m.key);
			l.append(s);
		});
	}
	//恢复各个按钮初始状态
	$("#filtered-"+panel).removeClass("search_fixed_height").addClass("search_150_height");
	$("#mutiButton-"+panel).addClass("display-inline"); //多选按钮
	$("#mutiPanel-"+panel).removeClass("display-block").addClass("display-none"); //多选面板
	$("#moreButton-"+panel+" span").removeClass("fa-chevron-up").addClass("fa-chevron-down"); //更多按钮
	isMuti=0;
	//处理是否显示更多按钮
	if($("#filtered-"+panel).height()>60){
		$("#filtered-"+panel).addClass("search_fixed_height");
		$("#moreButton-"+panel).removeClass("display-none").addClass("display-inline");
		_more[panel] = 1;
	} else {
		$("#moreButton-"+panel).removeClass("display-inline").addClass("display-none");
		_more[panel] = 0;
	}
	
	//处理多选按钮，目前只针对品牌进行多选
	if (panel == 'manu') {
		$("#mutiPanel-"+panel).addClass("display-none");
	}
}
//更多按钮的事件切换
function switchMore(panel) {
	$("#filtered-"+panel).toggleClass("search_fixed_height");
	$("#moreButton-"+panel+" span").toggleClass("fa-chevron-down").toggleClass("fa-chevron-up");
}

//多选按钮的事件切换
function startMuti(panel) {
	isMuti=1;
	$("#filtered-"+panel).removeClass("search_fixed_height");
	$("#moreButton-"+panel+" span").addClass("fa-chevron-up");
	$("#moreButton-"+panel).removeClass("display-inline").addClass("display-none");
	$("#mutiButton-"+panel).removeClass("display-inline").addClass("display-none");
	$("#mutiPanel-"+panel).removeClass("display-none").addClass("display-block");
}
//取消多选
function closeMuti(panel) {
	isMuti=0;
	$("#filtered-"+panel).addClass("search_fixed_height");
	if(_more[panel]==1) {
		$("#moreButton-"+panel+" span").removeClass("fa-chevron-up").addClass("fa-chevron-down");
		$("#moreButton-"+panel).addClass("display-inline");
	}
	$("#mutiButton-"+panel).addClass("display-inline");
	$("#mutiPanel-"+panel).removeClass("display-block").addClass("display-none");
	//取消之前所选中的元素
	$("#filtered-"+panel + " a.selected").removeClass("selected");
}
/**
 * 进行多选的查询，目前针对的是品牌进行多选操作
 * @param panel
 */
function doFilter(panel) {
	var _array = new Array();
	var showNames = "";
	var objs = $("#filtered-"+panel + " a.selected");
	if(objs!=null&&objs.length>0){
		$.each(objs, function(i, item){
			_array[i] = $(item).attr("id");
			var txt = $(item).text();
			txt = txt.indexOf("(")>0?txt.substring(0, txt.indexOf("(")):txt;
			showNames += txt + "&nbsp;&nbsp;";
		});
	} else {
		toastr.info(iselectedneed);
		return;
	}
	if (panel == 'manu') {
		manufacturers = _array;
	} else if(panel == 'cate') {
		conditionCateIds = _array;
	}
	getSelectedFilter(panel, showNames);
	dosearch("", _context);
	$("#row-"+panel).addClass("display-none");
}
/**
 * 
 * @param id
 * @param name
 * @param panel 指定类型，manu和cate两种
 */
function addFilter(id, name, panel) {
	//如果是多选，则选中不查询，统一点击查询
	if(isMuti==1) {
		$(event.target).toggleClass("selected"); //选中
		return;
	}
	if(panel=='manu') {
		manufacturers = id;
	} else if(panel == 'cate') {
		categoryIds = 0; //清空条件分类
		conditionCateIds = id; //设定过滤分类
	}
	getSelectedFilter(panel, name);
	dosearch("", _context);
	$("#row-"+panel).addClass("display-none");
}
/**
 * 定义选中分类/品牌的标签显示和点击事件
 * @param panel
 * @param showNames
 */
function getSelectedFilter(panel, showNames) {
	var _remove = $('<a class="btn btn-white manu-selected"></a>');
	_remove.click(function(){
		if (panel == 'manu') {
			manufacturers = null;
		} else if(panel == 'cate') {
			conditionCateIds = null;
		}
		dosearch("", _context);
		$("#Filter-"+panel).html('');
		$("#row-"+panel).removeClass("display-none");
	});
	if (panel == 'manu') {
		$("#Filter-"+panel).append(_remove.append(ibrand+'：'+showNames+'<span class="fa fa-times"></span>'));
	} else if(panel == 'cate') {
		$("#Filter-"+panel).append(_remove.append(icategory+'：'+showNames+'<span class="fa fa-times"></span>'));
	}
}