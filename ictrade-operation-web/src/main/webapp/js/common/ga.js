(function($){
     window.gaObj = {}
     
     //搜索页采集keyword
     function gaKeyWord(){
 		var kw = $("#_condition").val(),	//关键字
 			page = getQueryString("page");	//是否有 
 		//根据KW和当前分页来判断是否需要发送搜索采集
 		if(kw !="" && !page){
 			ga('set', 'dimension2', kw);
 			ga('send', 'pageview', '/search_results.htm?q='+kw); 
 		}
 		
 	 }
     //场景一：衡量产品展示
     function onProductShow(par) {//par为存放被展示产品集合
    	  for(var i = 0; i < par.length; i++) {
    	    var product = par[i]; 
    	   // {                 // t0为跟踪器名称，ec:addImpression 命令来衡量产品展示
  	        //   'id': '838704077918437376',                   //产品 id(string类型).  必填
  	        // 'name': 'AD8617ARZ-REEL7',                  //产品型号 (string类型).
  	    	// 'category': '通用放大器',                     // 产品次小类 (string).
  	    	//  'brand': 'Analog Devices Inc',                // 品牌 (string).
  	    	//  'list': 'Search Results',               // 列表所在位置，此处为搜索结果列表(string).
  	    	//  'position':43,                        // 产品在列表或集合中的位置（分页数*条目数+当前索引）(number).
  	        // 'dimension1': 'digikey'               // 供应商 (string).
  	    	//}
    	    ga('t0.ec:addImpression', product);
    	    ga('set', 'dimension1', product.dimension1);        //供应商（维度索引1）
    	 }
    	  ga('send', 'pageview');
    	  
    	  
    }
       //场景二：点击型号和图片查看详情( 当点击产品的链接时调用。)
     function onProductDetail(par,url) {
    	// {                      // ec:addProduct 命令发送产品信息
          //   'id': '12345',                          //产品 id(string类型).  必填
          //   'name': 'Android Warhol T-Shirt',      //产品型号 (string类型).
           //  'category': 'Apparel',                  // 产品次小类 (string). 
            // 'brand': 'Google',                     // 品牌 (string).
            // 'position': 1                      // 产品在列表或集合中的位置（分页数*条目数+当前索引）。
         //  }
       ga('t0.ec:addProduct', par);
       ga('t0.ec:setAction', 'click', {list: 'Search Results'});     //发送当前所在列表为搜索页
       ga('send', 'event', '衡量产品点击', '点击产品型号或图片', '搜索结果页', {
         hitCallback: function() {
        	 if(url){
        		 document.location = url;   //详情链接
        	 }
         }
       });
     }
    // 场景三：详情页点击加入购物车（搜索结果页购物车弹窗，加入购物车属于详情页加入购物车）
     function  addToCart(par,prod){
    	// {       // ec:addImpression 命令来衡量产品展示      
   	    //  'id': '12775677345',          //产品 id(string类型).  必填                 
   	    //  'name': 'ZY200R340',           //产品型号 (string类型).
   	     // 'category': '分立元件配件',      // 产品次小类 (string).
   	      //'brand': 'YKY'                  // 品牌 (string).
   	      //'list': 'Detail Results',       //发送当前所在列表为详情页
   	      //'position': 1 
   	    // }
    	 //{           // ec:addProduct 命令发送产品信息          
   	    //  'id': '5645667890',                 //产品 id(string类型).  必填  
   	     // 'name': 'YouTube Organic T-Shirt',   //产品型号 (string类型).
   	     // 'category': 'RFID，RF 接入，监控 IC',      // 产品次小类 (string).
   	    //  'brand': 'YKY'
   	    // }
    	 ga('t0.ec:addImpression', par);
    	    ga('t0.ec:addProduct', prod);
    	    ga('t0.ec:setAction', 'detail');       //行为
    	    ga('t0.send', 'pageview');   //发送页面信息  
    	    return !ga.loaded; 
     }
     
    
   //场景三：商品详情页被查看
    function productDetailShow(par) {
//    	{                      // ec:addProduct 命令发送产品信息
//    	    'id': '12345',                          //产品 id(string类型).  必填
//    	    'name': 'Android Warhol T-Shirt',      //产品型号 (string类型). 
//    	    'category': 'Apparel',                  // 产品次小类 (string). 
//    	    'brand': 'Google',                     // 品牌 (string).
//    	    'position': 1                      // 产品在列表或集合中的位置（分页数*条目数+当前索引）。
//    	  }
    	 ga('t0.ec:addProduct', par);
    	 ga('t0.ec:setAction', 'detail');      //查看的页面为详情页（默认值）
    	 ga('t0.send', 'pageview');           //发送页面信息（默认值）
     }
    
   //场景四，活动信息与活动
    function onProductPromoClick(par,prom){   
//    	{            //发送活动商品信息 
//  		  'id': 'PROMO_1234',            //活动 id(string类型).  必填 
//  		  'name': 'Summer Sale',         //活动名称（string类型）
//  		  'creative': 'summer_banner2',   //与促销活动关联的广告素材（例如 summer_banner2）
//  		  'position': 'banner_1'          //广告位首页位置 （string类型）  
//  	}
    	ga('t0.ec:addPromo',par);
		ga('t0.ec:setAction', 'promo_click');   //促销点击
		//ga('send', 'event', '促销', '点击促销商品', '活动名称');  //发送事件
		ga('send', 'event', '促销', '点击促销商品', prom.activityName);
    }
    
    //场景五：移除购物车
    function delProductClick(par) {
    	//[{                  
    	//  'id': '23323'    //商品id (string类型).  必填
    	//}]
    	for(var i = 0; i < par.length; i++) {
    		ga('t0.ec:addProduct', par[i]);  //发送产品信息
    	}
    	ga('t0.ec:setAction', 'remove');  //t0跟踪器名称，action为行为
    	ga('t0.send', 'event', '购物车移除', '从购物车移除商品', '购物车页面');   //发送匹配类型为事件（默认值）
    }
    
    //场景六：结算
    function settleClick(par, option) {
    	//[{   
        //	'id': '12345',        //产品 id(string类型).  必填
        //	'price':'123.5',        //价格（总价）(string类型).
        //	'quantity': '46',       //商品数量
        //	'dimension3': 'RMB'     //币种RMB或者USD，自定义的维度
    	//}]
    	
    	// option: 'Alipay'
    	
    	for(var i = 0; i < par.length; i++) {
    		ga('t0.ec:addProduct', par[i]);  //发送产品信息
    		ga('set', 'dimension3', par[i].dimension3);   //币种 （索引3）
    	}

        ga('t0.ec:setAction','checkout', {
          'step': 1,         //确认订单    
          'option': option     //支付方式  Alipay（支付宝） Wechat（微信） Individual  online payment（在线支付）
                               //Business online payment（企业在线支付） Bank transfer（银行在线付款）
        });
        
        ga('send', 'event', '衡量结算', '点击提交订单按钮', '结算页');
    }
    
    //场景七： 供应商中心，整单退款
    function  refundClick(par, orderId) {
    	//[{
	    // 	  'id': product.id,                //产品 id(string类型).  必填
	    // 	  'name': product.name,              //产品型号 (string类型).
	    // 	  'category': product.category,      // 产品次小类 (string). 
	    //	  'brand': product.brand,             // 品牌 (string).
	    //    'price': product.price,            //价格（string类型）
	    //    'quantity': product.qty              //数量（number类型）
	    // }]
    	
    	//orderId: '343234'
    	
		for(var i = 0; i < par.length; i++) {
		    ga('t0.ec:addProduct', par[i]);
		}
		ga('t0.ec:setAction', 'refund', {
			 'id': orderId   // 订单号 (string类型) 
		});
		ga('t0.ec:setAction','checkout', {
	          'step': 3,         //确认订单    
	          'option':'退款'       //发送固定退款信息
	        });
		ga('send', 'event', '衡量退款', '点击退款按钮', '订单列表页');
    }
    
    //场景八：支付步骤，支付成功（是个页面）
    function purchaseClick(prodList,orderInfo){
//    	 {                 //发送产品信息         
//  		  'id': '12345',                        //产品 id(string类型).  必填    
//  		  'name': 'Android Warhol T-Shirt',       //产品型号 (string类型).
//  		  'category': 'Apparel',             //产品次小类 (string类型).
 // 		  'brand': 'Google',                 // 品牌 (string).
//  		  'price': '29.20',                
 // 		  'coupon': 'APPARELSALE',           //订单优惠券码 (string).      
 // 		  'quantity': 1                    
//  		}
    	for(var i = 0; i < prodList.length; i++) {
		    ga('t0.ec:addProduct', prodList[i]);
		}
    	
//    	{     //操作类型设置为购买     
//    		  'id': 'T12345',                      //订单号(string类型).  必填  
//    		  'revenue': '37.39',                 //价格（总价）(string类型).     
//    		  'tax': '2.85',                         //税收（string类型）
//    		  'shipping': '5.34',                   //运费（string类型）  
//    		  'coupon': 'SUMMER2013'         //订单优惠券码            
//    		}
  		ga('t0.ec:setAction', 'purchase',orderInfo); 
  	    ga('t0.ec:setAction','checkout', {
	          'step': 2,         //确认订单    
	          'option':'支付'       //发送固定信息
	        });
  		ga('t0.send', 'pageview');     //发送匹配类型（默认值）
    }
    
    //场景九：结账行为细分（流失率）  
    function onShippingComplete(cart,stepNumber, shippingOption) {
    	//参数说明    提交订单   stepNumber:1,shippingOption为支付方式
    	//支付成功    stepNumber:2,shippingOption为支付方式
    	//退款   stepNumber:3,shippingOption为订单号
    	for(var i = 0; i < cart.length; i++) {
    	    var product = cart[i];
    	   // {
      	   //   'id': product.id,
      	    //  'name': product.name,
      	    //  'category': product.category,
      	    //  'brand': product.brand,
      	    //  'price': product.price,
      	    //  'quantity': product.qty
      	   // }
    	    ga('ec:addProduct', product);
    	  }
    	  ga('ec:setAction', 'checkout_option', {
    	    'step': stepNumber,
    	    'option': shippingOption
    	  });

    	  ga('send', 'event', 'Checkout', 'Option', 'shippingOption');
    	  
    	}
    //场景十：加入购物车(列表页加入购物车，快速加入购物车)
    function addlistTocart(pro){
    	 //{               
      		//  'id': '869915804865396736',                 //产品ID（string类型）   
      		//  'name': 'Series Voltage Reference IC'      //产品型号
      		//}
    	 ga('t0.ec:addProduct',pro);             //发送产品信息
   	     ga('t0.ec:setAction', 'add');   //默认值(加入购物车)
   	     ga('send', 'event', 'add to cart', 'click', 'add');    //发送匹配类型， 一个ip只记录一次，点击多次也是 记录一次 
   	
    }
   
     window.gaObj = {
    		 gaKeyWord:gaKeyWord,
    		 addToCart:addToCart,
    		 productDetailShow:productDetailShow,         //场景三：商品详情页被查看
    		 onProductShow:onProductShow,           //场景一：衡量产品展示
    		 onProductDetail:onProductDetail,       //场景二：点击型号和图片查看详情( 当点击产品的链接时调用。)
    		 onProductPromoClick:onProductPromoClick,    //场景四，活动信息与活动
    		 delProductClick:delProductClick,             //场景五：移除购物车
    		 settleClick:settleClick,              //场景六：结算
    		 refundClick:refundClick,               //场景七： 供应商中心，整单退款
    		 purchaseClick:purchaseClick,            //场景八：支付步骤，支付
    		 onShippingComplete:onShippingComplete,    //结账行为细分
    		 addlistTocart:addlistTocart               //加入购物车
     }
     
})(jQuery)