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
		    <div class="span10">
		    	<div style="min-width:980px;position:relative;">
		    		<!-- LEFT -->
			     	<div class="mt50 tb_wrap" style="margin-left:50px;">
					  <table class="table table-striped">
					  	 <thead>
					  	 	<tr>
					  	 		<td>序号</td>
					  	 		<td>姓名</td>
					  	 		<td>地推券密码</td>
					  	 	</tr>
					  	 </thead>
						 <tbody>
						 	<tr ms-for="($i, item) in @memberList">
						 		<td>{{$i+1}}</td>
						 		<td>{{item.partyName}}</td>
						 		<td>{{item.couponCodeId}}</td>
						 	</tr>
						 </tbody>
					  </table>
					  <div class="action_bar">
							<p><a class="poi" href="${ctx}/coupon/couponHandleList.htm">返回列表</a></p>
					  </div>
					</div>
			     	<!-- LEFT -->
			     	<!--right content-->
				      <div class="member_c_r" style="top:0;">
							<div class="c_item" ms-controller="couponDetail">
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
<script type="text/javascript">
	var selectCatid = "242,250";
</script>
<script >

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
    data: {},
    memberList:[]
});

var couponItem = avalon.define({
	$id: "couponDetail",
	data:{}
	
});
var personListUrl  = ykyUrl.pay +"/v1/coupons/";
var couponDetailUrl = ykyUrl.pay+"/v1/coupons/detail/"; 
var initData = function(){
	var id = getQueryString("id");
	$.aAjax({
		url: personListUrl+id,
		type:"get",
		dataType: 'JSON',
		contentType:'application/json',
	    success: function(data) {
	    	if(data){
	    		showData.dataList = data;
	    		var dataArray = showData.dataList.couponCodeList;
	    		var tempArray = [];
	    		for(var index=0;index<dataArray.length;index++){
	    			if(dataArray[index].couponCodeId){
	    				tempArray.push(dataArray[index]);
	    			}
	    		}
	    		showData.memberList = tempArray;
	    	}
	    },
	    error:function(e){
	    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
	    }
	});
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

$(function() {
	initData();
});
</script>
</body>
</html>
