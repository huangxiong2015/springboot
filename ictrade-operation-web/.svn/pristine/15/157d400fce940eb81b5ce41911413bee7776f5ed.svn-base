<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<!--[if IE 8 ]>    <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="zh-CN"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->

<head>
	<!-- jQuery 1.11.3 -->
  <c:import url ="/WEB-INF/jsp/include/main/constants.jsp"/>
  <title>地推发放页</title>
  <meta HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Expires" CONTENT="0"> 
  <link rel="stylesheet" href="${ctx}/css/app/groundCouponHandleOut.css" />
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

<!---index header  start   ---->
<jsp:directive.include file="../include/main/header.jsp" />
<!---index header  end   ---->
<!---index nav  start   ---->
<jsp:directive.include file="../include/main/menu.jsp" />
<!---index nav  end   ---->
	  
 <!-- 右边内容显示 -->
<div class="content-wrapper"  >
	<div class="content_headder">
	</div>
	<div class="content_main" ms-controller="data">
		<!-- 栅格系统 -->
		<div class="container-fluid">
		  <div class="row-fluid">
		  	<div class="span2"></div>
		    <div class="span10">
		     	<!-- LEFT -->
		     	<div class="panel panel-default mt50" style="min-width:980px;position:relative;">
				  <div class="panel-heading">
				    <h3 class="panel-title">发放地推券</h3>
				  </div>
				  <div class="panel-body">
				  	<p class="choose_txt" ><span class="c_label">请选择推广员工：</span>已选 <span class="number">{{@selectData.count}}</span>个</p>
			    	<div class="member_list">
						<li ms-for="($i, item) in @dataList.couponCodeList">
						<span ms-if="item.couponCodeId!=undefined"><input type="checkbox" checked="checked" disabled="disabled" /></span>
						<span ms-if="item.couponCodeId==undefined"><input type="checkbox" ms-duplex-checked="@data[$i].checked" ms-attr="{'data-partyid':item.partyId}" data-duplex-changed="@checkOne" /></span>
						<span>{{item.partyName}}</span>
						</li>
						<div style="clear:both;"><span><input type="checkbox" ms-duplex-checked="@allchecked" data-duplex-changed="@checkAll" /><span>全选</span></span></div>
						</div>
						<div class="action_bar">
							<p><span class="yky_btn_y" ms-click="@sub">发放</span><span class="yky_btn_r ml20" ms-click="@returnList">返回</span></p>
						</div>
					</div>
					<!--right content-->
			     	 <div class="member_c_r mt50" ms-controller="couponDetail">
						<!-- <div class="c_item" ms-controller="couponDetail">
							<div class="c_item_top" ms-if="@data.couponType =='DEDUCTION'">
								<p class="price">{{@data.unitAmount}}<span ms-if="@data.couponCurrency=='CNY'">元</span><span ms-if="@data.couponCurrency=='USD'">美元</span></p>
								<p>单笔订单满 <span>{{@data.consumeLimitAmount}}</span><span ms-if="@data.couponCurrency=='CNY'">元</span><span ms-if="@data.couponCurrency=='USD'">美元</span>使用</p>
							</div>
							<div class="c_item_top" ms-if="@data.couponType =='DISCOUNT'">
								<p class="price"><span>{{@data.discountNumber}}</span>折</p>
								<p>单笔订单满 <span>{{@data.consumeLimitAmount}}</span><span ms-if="@data.couponCurrency=='CNY'">元</span><span ms-if="@data.couponCurrency=='USD'">美元</span>使用</p>
							</div>
							<div class="c_item_bottom">
								<p>
								<span>[ 地推券]</span>
								</p>
								<p ms-if="@data.thruTypeId =='FROM_GET_DATE'">领取后<em>{{@data.limitMonth}}</em>个月内有效</p>
								<p ms-if="@data.thruTypeId =='EXACT_DATE_ZONE'">有效期：<span>{{@data.fromDateFormat}}</span>至<span>{{@data.thruDateFormat}}</span></p>
							</div>
						</div> -->
						<div class="c_item">
				  			<div class="c_item_top" ms-if="@data.couponType=='DEDUCTION'">
				  				<p class="price"><span>{{@data.unitAmount}}</span> <span ms-if="@data.couponCurrency === 'USD'">美元</span><span ms-if="@data.couponCurrency === 'CNY'">元</span></p>
				  				<p>单笔订单满 <span>{{@data.consumeLimitAmount}}</span><span ms-if="@data.couponCurrency === 'USD'">美元</span><span ms-if="@data.couponCurrency === 'CNY'">元</span>使用</p>
				  			</div>
				  			<div class="c_item_top" ms-if="@data.couponType=='DISCOUNT'">
				  				<p class="price"><span ></span>{{@data.discountNumberFormated}}折</p>
				  				<p>单笔订单满 <span>{{@data.consumeLimitAmount}}</span><span ms-if="@data.couponCurrency === 'USD'">美元</span><span ms-if="@data.couponCurrency === 'CNY'">元</span>使用</p>
				  			</div>
				  			<div class="c_item_bottom">
				  				<div ms-if="@data.useProductType == 0">
					  				<p ms-if="@data.brandList.length ===0 && @data.vendorList.length ===0 && @data.categoryList.length ===0">
										<span >[ 全平台通用 ]</span>
									</p>
					  				<p ms-if="@data.brandList.length !=0 || @data.vendorList.length ===0 || @data.categoryList.length ===0">
										<span >供应商:</span>
							  			<span class="c_item_bread" ms-attr="{title:@data.vendorListString}">{{@data.vendorListString}}</span>
							  			<span ></span><span >； 制造商:</span>
							  			<span class="c_item_bread" ms-attr="{title:@data.brandListString}">{{@data.brandListString}}</span>
							  			<span ></span><span >；分类:</span>
							  			<span class="c_item_bread" ms-attr="{title:@data.categoreListString}">{{@data.categoreListString}}</span>
									</p>
								</div>
								<p ms-if="@data.useProductType == 1">[ 指定商品使用 ]</p>
								<p ms-if="(@data.thruTypeId =='FROM_GET_DATE')||(@data.thruTypeId =='from_get_date')">有效期：<span :visible="@data.limitMonth !==''">领取后<em>{{@data.limitMonth}}</em>天内有效</span></p>
								<p ms-if="(@data.thruTypeId =='EXACT_DATE_ZONE')||(@data.thruTypeId =='exact_date_zone')">有效期：<span>{{@data.fromDate}}</span><span :visible="@data.thruDate !==''">至{{@data.thruDate}}</span></p>
				  				
				  			</div> 
				  			<span class="c_item_type">
				  				<em :visible="@data.couponType=='DEDUCTION'">抵扣劵</em>
				  				<em :visible="@data.couponType=='DISCOUNT'">折扣劵</em>
				  			</span>
				  		</div>
					 </div>	
			        <!-- right content -->
				  </div>
		     	<!-- LEFT -->
		    </div>
		  </div>
		</div>
		<!-- 栅格系统结束 -->
	</div>
</div>
<script type="text/javascript">
	var selectCatid = "242,250";
</script>
<!---footer  start   ---->
<jsp:directive.include file="../include/main/footer.jsp" />
<!---footer  end   ---->   

<script >
//在array原型上定义删除指定值的函数
Array.prototype.removeByValue = Array.prototype.removeByValue||function(val) {
	  for(var i=0; i<this.length; i++) {
	    if(this[i] == val) {
	      this.splice(i, 1);
	      break;
	    }
	  }
}

function toName(list){
	if(list==undefined||list.length==0||list==""){
		return "不限";
	}else if(list.length>0){
		var cateList = [];
		$.each(list,function(i,item){
			var cateName = item.firstClassName+(item.secondClassId?"-"+item.secondClassName:"");
			cateList.push(cateName);
		})
		var categoryString = cateList.join();
		return categoryString;
	}
}
var showData = avalon.define({
    $id: "data",
    dataList: {
    	couponCodeList:[]
    },
    unpushList:[],
	selectData:{
		invoiceIdArray:[],
		count:0,
		initCount:0,
		invoiceString:''
	},
	selectFlag:false,
	data: [],
    allchecked: false,
    checkAll: function (e) {
         var checked = e.target.checked
         showData.data.forEach(function (el) {
             el.checked = checked;
         })
         showData.selectData.invoiceIdArray=[];
         showData.selectData.invoiceString = "";
         if(checked === false){
        	 showData.selectData.count= showData.selectData.initCount;
        	 showData.selectFlag = false;
         }else{
        	 showData.selectData.count= showData.data.length;
             showData.unpushList.$model.forEach(function (el) {      //蒋将dataList中id拼接起来
            	 showData.selectData.invoiceIdArray.push(el.partyId);
             })
             showData.selectFlag = true;
             showData.selectData.invoiceString = showData.selectData.invoiceIdArray.join();
         }
    },
    checkOne: function (e) {
         var checked = e.target.checked;
         var invoiceId = $(e.target).data("partyid");
         if (checked === false) {
        	 showData.allchecked = false
        	 if(showData.selectData.count>0){
        		 showData.selectData.count-= 1;
        		 showData.selectData.invoiceIdArray.removeByValue(invoiceId);
        	 }
         } else {//avalon已经为数组添加了ecma262v5的一些新方法
        	 showData.allchecked = showData.data.every(function (el) {
                 return el.checked;
             })
             showData.selectData.count+= 1;
        	 showData.selectData.invoiceIdArray.push(invoiceId);
         }
         
         if(showData.selectData.count>0){
        	 showData.selectFlag = true;
         }else{
        	 showData.selectFlag = false;
         }
         showData.selectData.invoiceString = showData.selectData.invoiceIdArray.join();
    },
    sub:function(){
		if(showData.selectData.count>showData.selectData.initCount){
			gPush();
		}else{
			var id = getQueryString("id");
			window.location.href= "${ctx}/coupon.htm?action=groundhandleSucc&&id=" + id;
		}
	},
	returnList:function(){
		window.location.href = "${ctx}/coupon/couponHandleList.htm";
	}
});

var couponItem = avalon.define({
	$id: "couponDetail",
	data:{},
	toDiscount: function(data){
		if(data=='undefined' || !data){
			return "--";
		}else{
			return accMul(data,10);
		}
	}
});
var personListUrl  = ykyUrl.pay +"/v1/coupons/";
var couponDetailUrl = ykyUrl.pay+"/v1/coupons/detail/"; 
var gPushlUrl = ykyUrl.pay+"/v1/coupons/push";
var initData = function(){
	var id = getQueryString("id");
	if(id!=undefined){
		$.aAjax({
			url: personListUrl+id,
			type:"get",
			dataType: 'JSON',
			contentType:'application/json',
		    success: function(data) {
		    	if(data){
		    		showData.dataList = jQuery.extend({},showData.dataList.$model,data);
		    		
		    		var dataArray = data.couponCodeList;
		    		var unpushListTemp = [];
		    		for(var index=0;index<dataArray.length;index++){
		    			if(dataArray[index].couponCodeId){
		    				var temp = {checked: true}
		    			}else{
		    				var temp = {checked: false}
		    				unpushListTemp.push(dataArray[index]); //未发放的地推员列表
		    			}
		    			showData.data.push(temp);
		    		}
		    		showData.unpushList = unpushListTemp;
		    		$.each(data.couponCodeList,function(index,ele){
		    			if(ele.couponCodeId){
		    				showData.selectData.count+= 1;
		    				showData.selectData.initCount+= 1;
		    			}
		    		})
		    	}
		    },
		    error:function(e){
		    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
		    }
		});
	}
	if(id!=undefined){
		$.aAjax({
			url: couponDetailUrl+id,
			type:"get",
			dataType: 'JSON',
			contentType:'application/json',
		    success: function(data) {
		    	var tempInitData = data;
	    		tempInitData.brandListString = toName(data.brandList);
	    		tempInitData.vendorListString = toName(data.vendorList);
	    		tempInitData.categoreListString = toName(data.categoreList);
	    		if(data.discountNumber){
	    			tempInitData.discountNumberFormated = numMulti(data.discountNumber,10);
	    		}
	    		couponItem.data = tempInitData;
		    },
		    error:function(e){
		    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
		    }
		});
	} 
}

var gPush = function(){
	var id = getQueryString("id");
	var param = {
		"couponId": id,
		/* "salesList": showData.selectData.invoiceIdArray */
		"salesList":  showData.selectData.invoiceIdArray
	}
	$.aAjax({
		url: gPushlUrl,
		type:"POST",
		data:JSON.stringify(param),
		contentType:'application/json',
	    success: function(data) {
	    	window.location.href= "${ctx}/coupon.htm?action=groundhandleSucc&&id=" + id;
	    },
	    error:function(e){
	    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
	    }
	});
}
$(function() {
	initData();
});
</script>
</body>
</html>
