/**
 * Created by yansha on 2017/9/21.
 */
var banTemplate='<div class="top_bg top-falg" id="module_{moduleId}" data-id="{moduleId}" data-promotion-id="{promotionId}" name="{name}" style="background: url({bannerImg}) no-repeat top;background-color: {backgroundColor}; width: {weight};">'
        +'{ruleHtml}'
        +'</div>';
var couponItemTemp= '<li>'+
		'<div class="coupon-detail-top">'+
		    '<div class="coupon-detail-left" >'+
		        '<p class="coupon-detail-num">{couponNum}</p>'+
		    '</div>'+
		    '<div class="coupon-detail-right">'+
		        '<p class="coupon-currency">{currency}</p>'+
		        '<p class="coupon-detail-title">优惠券</p>'+
		    '</div>'+
		'</div>'+
		' <div class="use-type">'+
		    ' <span>满{consumeLimitAmount}{unit}使用</span>'+
		'</div>'+
	'</li>';
var couponTemplate= '<div class="willas-coupons w top-falg" id="module_{moduleId}" data-id="{moduleId}"><div class="go-coupons">'+
			'<div class="go-coupon">'+
			    '<p class="coupon-title">GO <i class="arrow-icon">&gt;</i></p>'+
			    '<p class="coupon-text">先领再买 更省钱</p>'+
			    '<a href="javascript:;" class="btn-coupon disabled">一键领取<i class="icon-triangle-rightmin"></i></a>'+
			'</div>'+
			'<ul class="coupon-detail">' +
			'{couponItem}'+
			'</ul>'+
		'</div>'+
	'</div>';
var descTemplate='<div class="activity-description top-falg" id="module_{moduleId}" data-id="{moduleId}" data-promotion-id="{promotionId}">' +
		'<div class="activity-description-list w">' +
		    '<div class="product-list-top">' +
		        '<div class="list-top-title"><span class="title-text">{title}</span></div>' +
		    '</div>' +
		    '<div class="activity-description-box">{content}</div>'+
		'</div>'+
	'</div>';

var menuTemplate='<!-- 右侧导航栏 -->' +
	'<div class="right-nav-bar {dsn}">' +
		'<div class="right-nav-box">' +
			'<ul >{menuList}' +
			'</ul>' +
			'<div class="go-top">' +
				'<p class="icon-indicate_up">^</p>' +
				'<span>TOP</span>' +
			'</div>' +
			'<div>' +
	'</div>';

var listItemTemplate='<li class="ga_prod">' +
	'<span class="product-model">' +
	    '<a target="_blank" href="/product/903531338852728832.htm" title="LTV-816S-TA1-D3-TX" class="text-format ga_detail">{id}</a>' +
	'</span>' +
	'<span title="LITE-ON">{materialPartNo}</span>' +
	'<span>{brand}</span>' +
	'<span class="product-num">{number}</span>' +
	'<span class="new-price product-price">{currency}{price}</span>' +
	'<span>' +
	    '<a class="buy-button ga_detail" target="_blank" href="/product/903531338852728832.htm">立即购买</a>' +
	'</span>' +
'</li>';
var listTemplate='<div class="show-product-list top-falg" id="module_{moduleId}" data-id="{moduleId}" data-promotion-id="{promotionId}">'
		+'<div class="product-list w">'
			+'<div class="product-list-top">'
				+'<div class="list-top-title">'
				    +'<span class="title-text">{title}</span>'
				+'</div>'
				+'<!-- 搜索框 -->' 
				+'<div class="active_search {dsn}">'
					+'<div class="goods_search rel">'
						+'<input type="text" class="go_search" id="" maxlength="50" placeholder="{searchText}" data-id="49" data-item="prd-diode" />'
						+'<i class="search_btn"></i>' 
						+'<!-- 关联词显示 -->'
						+'<div class="predictive_box" style="display:none;">'
							+'<i class="show_up"></i>'
						+'</div>'
					+'</div>' 
				+'</div>' 
			+'</div>'
			+'<div class="product-list-title">'
			+'    <ul>'
			    +' {heardHtml}'
			+'    </ul>'
			+'</div>'
			+'<div class="product-list-box">'
			+'    <div class="loding-icon" style="display: none;"></div>'
			    +' <ul>{listItem}</ul>' +
			'</div>'
		    +'<!-- 临时分页 -->'
			+'<div class="item_page ml20 mr20">'
				+'<div id="pager">'
				    +'<div id="kkpager">'
				        +'<div>'
				            +'<span class="pageBtnWrap">'
				            	+'{pageHtml}'
				            +'</span>'
				        +'</div>'
					    +'<div style="clear:both;"></div>'
					+'</div>'
				+'</div>'
			+'</div>'
		+'</div>'
	+'</div>';
$(function () {
	var activeApi=ykyUrl.product + '/v1/promotions/'+getProId()+'/module/draft';//'../../data/active.json';
    var $activeHtml= $('#activeHtml');
    var html='',data={},menuData={},menuHtml='';
    var totalPageNum=10,pageNum=1,perPage=10,api='',fieldArr=[],searchUrl='';
    function getProId(){
    	var linksArr = window.location.pathname.split("/"),id=0; 
    	if(linksArr.length>=2){
    		id= linksArr[linksArr.length-2];  
    	} 
    	return id;
    }
    function  _filter(data){
    	for(var i=data.length-1; i>=0; i--){
    		if(!data[i].promotionContent){
    			data.splice(i,1);
    		} 
    	}
    	return data;
    };
    syncData(activeApi, 'GET', null, function (res, err) {
    	if(res){ 
            data=_filter(res[0].list); 
            menuData.dsn=res[0].displaySidebar=='N'?'':'dsn';
    	}
    }, false)
    for( var i=0; i<data.length; i++){
    	var tit='';
        if(data[i].promoModuleType=='BANNER'){
            html+= returnBan(data[i]) ;
            tit='广告图';
        }else if( data[i].promoModuleType=='PRODUCT_LIST'){
            html+= returnList(data[i]);
            tit='产品';
        }else if( data[i].promoModuleType=='COUPON'){
            html+= returnCoupon(data[i]);
            tit='领券';
        }else{
            html+= returnDetail(data[i]);
        }
        tit=data[i].promotionContent.showSet.title?data[i].promotionContent.showSet.title:tit;
        
        menuHtml+='<li><a href="#module_'+data[i].promoModuleId+'" class="" title="'+tit+'">'+tit+'</a></li>';
    }
    menuData.menuList=menuHtml; 
    html+=applyTpl(menuTemplate, menuData);
    $activeHtml.html(html);

    function returnBan(data) {
        var html='';
        data.promotionContent.contentSet.moduleId=data.promoModuleId;
        if(data.promotionContent.contentSet&&data.promotionContent.contentSet.showDesc){
            data.ruleHtml='<div class="w rel"><a href="/topic/'+data.promoModuleId+'-Detail.htm" target="_blank" class="active-detail">活动细则</a></div>';
        }
        html=applyTpl(banTemplate, data.promotionContent.contentSet); 
        return html;
    }

    function returnDetail(data) {
        var html='';
        data.promotionContent.contentSet.moduleId=data.promoModuleId;
        data.promotionContent.contentSet.title=data.promotionContent.showSet.title;
        html=applyTpl(descTemplate, data.promotionContent.contentSet); 
        return html;
    }
    function returnList(data) {
        var html='',listItem='';
        fieldArr=data.promotionContent.contentSet.fields;
        totalPageNum=data.promotionContent.contentSet.page.pageNum;  //设置总页数
        perPage=data.promotionContent.contentSet.page.perPage;       //设置每页条数
        var dataSource=data.promotionContent.contentSet.dataSource;
        var params={
            vendorId:data.promotionContent.contentSet.promoModuleId,
            page:1,
            pageSize:perPage,
            dataSource:data.promotionContent.contentSet.dataSource,
            vendor:data.promotionContent.contentSet.condition.vendor,
            brand:data.promotionContent.contentSet.condition.brand,
            category:data.promotionContent.contentSet.condition.category,
            useStockQty:data.promotionContent.contentSet.uploadData.useStockQty,
            fileUrl:data.promotionContent.contentSet.uploadData.fileUrl
        }
         
        searchUrl=data.promotionContent.contentSet.search.url;	
        api=data.promotionContent.contentSet.search.api;		//获取数据接口
        var url=api+'&isPreview=Y&page=1'; 
        syncData(url, 'GET', params, function (res, err) {
        	if(res!=null){
	            var list=checkList(res.productInfo);
	            var vendorName=res.vendorName;
	            for(var i=0; i<list.length; i++){
	                if(list[i].currency=='RMB'&& list[i].price!='面议'){
	                    list[i].currency='￥';
	                }else{
	                    list[i].currency='';
	                }
	                listItem +='<li>'+retSpan(fieldArr, list[i])+'</li>';
	                //listItem+=applyTpl(listItemTemplate, list[i]);
	            }
	            res.pageHtml = createPage(res.total, res.pageSize);
	            res.listItem=listItem;
	            res.moduleId=data.promoModuleId;
	            res.dsn=data.promotionContent.contentSet.search.show?'':'dsn';
	            res.promotionId=data.promotionId; 
	            res.title=data.promotionContent.showSet.title;
	            res.searchText=data.promotionContent.contentSet.search.text;
	            res.heardHtml=retHead(fieldArr);
	            html=applyTpl(listTemplate, res);
        	}
        }, false)
        return html;
    }
    function retHead(fieldArr){
        var html='';
        for(var i=0; i<fieldArr.length; i++){
            if(fieldArr[i].value==='id'){
                html+='<li class="product-model">型号</li>';
            }else if(fieldArr[i].value==='price'){
                html+='<li class="product-price">价格</li>';
            }else if(fieldArr[i].value==='newPrice'){
                html+='<li class="product-price">促销价</li>';
            } else if(fieldArr[i].value==='qty'){
                html+='<li class="product-num">库存</li>';
            } else{
                html+='<li>'+fieldArr[i].textName+'</li>';
            }
        }
        return html;
    }
    function retSpan(fieldArr, data) {
       var html='';
        for(var i=0; i<fieldArr.length; i++){
           if(fieldArr[i].value==='id'){
               html+='<span class="product-model">' +
                   '<a target="_blank" href="/product/'+ data['id'] +'.htm" title="'+ data.spu.manufacturerPartNumber +'" class="text-format ga_detail">'+ data.spu.manufacturerPartNumber +'</a>' +
                   '</span>';
           }else if(fieldArr[i].value==='vendorName'){
               html+='<span >'+ data.vendorId +'</span>';
           }else if(fieldArr[i].value==='manufacturer'){
               html+='<span >'+ data.spu.manufacturer +'</span>';
           }else if(fieldArr[i].value==='price'){
               html+='<span class="old-price product-price">'+ data.priceObj.originalResalePrices.currency + data.priceObj.originalResalePrices.maxQuantityPrice +'</span>';
           }else if(fieldArr[i].value==='newPrice'){
               html+='<span class="new-price product-price">'+ data.priceObj.prices.currency + data.priceObj.prices.maxQuantityPrice +'</span>';
           }else if(fieldArr[i].value==='qty'){
               html+='<span class="product-num">'+ formatNum(data['qty']) +'</span>';
           }else if(fieldArr[i].value==='operator'){
               html+='<span > <a target="_blank" href="/product/'+ data['id'] +'.htm"  class="buy-button ga_detail" >立即购买</a>' +
                   '</span>';
           }
        }
        return html;
    }
    function createPage(len, size) {
        pageNum = Math.ceil(len / size)
        pageNum=pageNum>totalPageNum?totalPageNum:pageNum;
        var page = "";
        for (var i = 1; i <= pageNum; i++) {
            page += '<a href="javascript:;" title="第' + i + '页" data-page="' + i + '">' + i + "</a>";
            if (i == 1) {
                page = '<a href="javascript:;" class="curr" title="第' + i + '页" data-page="' + i + '">' + i + "</a>";
            }
        }
        var pageHtml = '<a class="disabled pre" href="javascript:;" title="上一页" data-page="pre">上一页</a>' + page + '<a class="next" href="javascript:;" title="下一页" data-page="next">下一页</a>';
        return pageHtml;
        /* $(".pageBtnWrap").append(pageHtml);
        if (pageNum == 1) {
            $(".pageBtnWrap .pre").addClass("disabled");
            $(".pageBtnWrap .next").addClass("disabled");
        }*/
    }
    $(".pageBtnWrap").on("click", "a", function() {
        var newPage = "",$curr=$(this).parent().find(".curr"),$page= $(this).parent().find("a"),$pre= $(this).parent().find(".pre"),$next= $(this).parent().find(".next");
        var dataPage = $(this).attr("data-page");
        var oldPage = $curr.attr("data-page");
        if (dataPage == "pre" && oldPage == 1) {
            return;
        }
        if (dataPage == "next" && oldPage == pageNum) {
            return;
        }
        if (dataPage == "pre") {
            newPage = oldPage - 1;
            for (var i = 0; i < $page.length; i++) {
                if ($page.eq(i).text() == newPage) {
                    $page.eq(i).addClass("curr");
                    $page.eq(i).siblings("a").removeClass("curr");
                }
            }
        } else {
            if (dataPage == "next") {
                newPage = Number(oldPage) + 1;
                for (var i = 0; i < $page.length; i++) {
                    if ($page.eq(i).text() == newPage) {
                        $page.eq(i).addClass("curr");
                        $page.eq(i).siblings("a").removeClass("curr");
                    }
                }
            } else {
                newPage = dataPage;
                for (var i = 0; i < $page.length; i++) {
                    if ($page.eq(i).text() == newPage) {
                        $page.eq(i).addClass("curr");
                        $page.eq(i).siblings("a").removeClass("curr");
                    }
                }
            }
        }
        if (newPage == 1) {
            $pre.addClass("disabled");
            $pre.siblings("a").removeClass("disabled");
        } else {
            if (newPage == pageNum) {
                $next.addClass("disabled");
                $next.siblings("a").removeClass("disabled");
            } else {
                $page.removeClass("disabled");
            }
        }

        showList($(this).parents('.show-product-list'), newPage);
    });

    function checkList(list) {
        var checkProdList=[];
        $.each(list, function(index, ele) {
            var prd = productDataFormat(ele);
            if (prd.qty != "" && prd.priceObj.maxQuantityPrice != "--") {
                checkProdList.push(prd);
            }
        });
        return checkProdList;
    }
    function showList(obj, page) {
        console.log(obj.data('id'));
        var listItem='';
        /*var params={
                vendorId:data.infoId,
                page:1,
                pageSize:perPage,
                dataSource:data.promotionContent.contentSet.dataSource,
                vendor:data.promotionContent.contentSet.condition.vendor,
                brand:data.promotionContent.contentSet.condition.brand,
                category:data.promotionContent.contentSet.condition.category,
                useStockQty:data.promotionContent.contentSet.uploadData.useStockQty,
                fileUrl:data.promotionContent.contentSet.uploadData.fileUrl
            }*/ 
        var url=api+'&isPreview=Y&page='+page ;
        syncData(url, 'GET', null, function (res, err) {
            var list=checkList(res.productInfo);
            var vendorName=res.vendorName;
            for(var i=0; i<list.length; i++){
                if(list[i].currency=='RMB'&& list[i].price!='面议'){
                    list[i].currency='￥';
                }else{
                    list[i].currency='';
                }
                listItem +='<li>'+retSpan(fieldArr, list[i])+'</li>';
                //listItem+=applyTpl(listItemTemplate, list[i]);
            } 
        }, false)
        obj.find('.product-list-box>ul').html(listItem);
       /* syncData('../../data/list.json', 'GET', {id: obj.data('infoId'), page: page}, function (res, err) {
            for(var i=0; i<res.list.length; i++){
                if(res.list[i].currency=='RMB'&& res.list[i].price!='面议'){
                    res.list[i].currency='￥';
                }else{
                    res.list[i].currency='';
                }
                listItem+=applyTpl(listItemTemplate, res.list[i]);
            }
            //res.pageHtml = createPage(res.total, res.perPage);
            //res.listItem=listItem;
            //html=applyTpl(listTemplate, res);
        }, false)
        obj.find('.product-list-box>ul').html(listItem);*/
    }
    function formatNum(num) {
        var num = String(num)
            , pos = num.indexOf(".")
            , numS = pos > 0 ? "" : num
            , numE = "";
        if (pos > 0) {
            numS = num.substring(0, pos);
            numE = num.substring(pos);
        }
        var re = /(-?\d+)(\d{3})/;
        while (re.test(numS)) {
            numS = numS.replace(re, "$1,$2");
            num = numS + numE;
        }
        return num;
    }
    function productDataFormat(list) {
        var prd = list;
        prd.priceObj = {
            "originalResalePrices": {
                "currencyCode": "",
                "currency": "",
                "maxQuantityPrice": "--"
            },
            "prices": {
                "currencyCode": "",
                "currency": "",
                "maxQuantityPrice": ""
            }
        };
        if (prd.prices && prd.prices.length > 0) {
            $.each(prd.prices, function(i, n) {
                if (n.currencyCode == "CNY") {
                    prd.priceObj.prices.currencyCode = n.currencyCode;
                    prd.priceObj.prices.currency = n.priceLevels ? (n.priceLevels.length ? "￥" : "") : "";
                    prd.priceObj.prices.maxQuantityPrice = n.priceLevels ? (n.priceLevels.length ? n.priceLevels[n.priceLevels.length - 1].price : "--") : "--";
                } else {
                    if (n.currencyCode == "USD") {
                        prd.priceObj.prices.currencyCode = n.currencyCode;
                        prd.priceObj.prices.currency = n.priceLevels ? (n.priceLevels.length ? "$" : "") : "";
                        prd.priceObj.prices.maxQuantityPrice = n.priceLevels ? (n.priceLevels.length ? n.priceLevels[n.priceLevels.length - 1].price : "--") : "--";
                    }
                }
                if (n.currencyCode == "CNY") {
                    return false;
                }
            });
        }
        if (prd.originalResalePrices && prd.originalResalePrices.length > 0) {
            $.each(prd.originalResalePrices, function(i, n) {
                if (prd.priceObj.prices.currencyCode == "CNY") {
                    if (n.currencyCode == "CNY") {
                        prd.priceObj.originalResalePrices.currency = n.unitPrice ? (n.unitPrice.length ? "￥" : "") : "";
                        prd.priceObj.originalResalePrices.maxQuantityPrice = n.unitPrice ? n.unitPrice : "--";
                    }
                    return;
                } else {
                    if (prd.priceObj.prices.currencyCode == "USD") {
                        if (n.currencyCode == "USD") {
                            prd.priceObj.originalResalePrices.currency = n.unitPrice ? (n.unitPrice.length ? "$" : "") : "";
                            prd.priceObj.originalResalePrices.maxQuantityPrice = n.unitPrice ? n.unitPrice : "--";
                        }
                    }
                }
            });
        }
        return prd;
    }
    function returnCoupon(data) {
        var html='',couponItem='';
        var api='../../data/activeCoupon.json';//'v1/coupons/infoList';
        syncData(api, 'POST', {couponIds: data.promotionContent.contentSet.coupon,couponMethodType:'FREE_WGET'}, function (res, err) {
            res.moduleId=data.promoModuleId;
            res.promotionId=data.promotionId;
            for(var i=0; i<res.list.length; i++){ 
            	var couponNum=0;
            	if(res.list[i].couponCurrency=='CNY'){
            		res.list[i].currency='元';
            		res.list[i].unit='元';
            	}else{
            		res.list[i].currency='美元';
            		res.list[i].unit='美元';
            	}
            	if(res.list[i].couponType=='DISCOUNT'){
            		couponNum=toDiscount(res.list[i].discountNumber);
            		res.list[i].currency='折';
            	}else{
            		couponNum=res.list[i].unitAmount;  
            	}
            	res.list[i].couponNum=couponNum;
                couponItem+=applyTpl(couponItemTemp, res.list[i]);
            }
            res.couponItem=couponItem;
            html=applyTpl(couponTemplate, res);
        }, false)
        return html;
    }
    $(".search_btn").on("click", function() { 
        var keyword = $.trim($(this).parent().find(".go_search").val());
        var id = $(this).parent().find(".go_search").attr("data-id");
        activeSearch(keyword);
    });
    function activeSearch(kw) {
        //setKeyWord(kw);
        var keywordVal = $.trim(kw);
        var keyword = keywordVal.replace(/\//g, "%2F");
        keyword = keyword.replace(/\\/g, "%5C");
        if (keyword != null) {
            if (keyword != "") {
                setTimeout(function() {
                    window.open(searchUrl+"&keyword=" + encodeURIComponent(keyword));
                }, 100);
            } else {
                setTimeout(function() {
                    window.open(searchUrl);
                }, 100);
            }
        }
    }

    getTop();
    showTop();
    checkBar();
    
    /*右侧菜单*/
    var scrollT;
    var scrollR;
    var time = 0;
    var heightVal = 0;
    $(window).scroll(function() {
        scrollT = showTop();
        scrollR = checkBar();
    });
    var heightVal = 0;
    function getTop() {
        heightVal = $("#activeHtml").offset().top;
    }
    function showTop() {
        var scrollTop = $(document).scrollTop();
        clearTimeout(scrollT);
        return setTimeout(function() {
            if (scrollTop < heightVal) {
                $(".right-nav-bar").removeClass("right-fix");
            } else {
                $(".right-nav-bar").addClass("right-fix");
            }
        });
    }
    
    function goToTop() {
        $("body,html").animate({
            scrollTop: 0
        }, 200);
    } 
    function checkBar() {
        clearTimeout(scrollR);
        var arr = [];
        var bodyTop = $("body").scrollTop();
        var len = $(".top-falg").length;
        return setTimeout(function() {
            for (var i = 0; i < len; i++) {
                var option = {
                    top: "",
                    item: ""
                };
                var top = $(".top-falg").eq(i).offset().top - 50;
                var item = $(".top-falg").eq(i).attr("id");
                option.top = top;
                option.item = item;
                arr.push(option);
            }
            $.each(arr, function(index, ele) {
                var nextTop = arr[index + 1] ? arr[index + 1].top : bodyTop;
                if (arr[index].top < bodyTop && nextTop >= bodyTop) {
                    $(".right-nav-box li").eq(index).find("a").addClass("active");
                    $(".right-nav-box li").eq(index).siblings("li").find("a").removeClass("active");
                }
            });
        }, 100);
    }
    $(".right-nav-box a").on("click", function() {
        $(this).addClass("active");
        $(this).parent().siblings("li").find("a").removeClass("active");
    }); 
    $(".go-top").on("click", function() {
        goToTop();
    });
    function toDiscount(data) {
        if (data == "undefined" || !data) {
            return "--";
        } else {
            return accMul(data, 10);
        }
    }
    function accMul(arg1, arg2) {
        var m = 0
          , s1 = arg1.toString()
          , s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length;
        } catch (e) {}
        try {
            m += s2.split(".")[1].length;
        } catch (e) {}
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
    }
})