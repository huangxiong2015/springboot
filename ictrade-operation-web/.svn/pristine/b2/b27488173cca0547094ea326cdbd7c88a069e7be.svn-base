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
	<link rel="stylesheet" type="text/css" href="${ctx}/css/lib/jquery.mCustomScrollbar.css" />
	<link rel="stylesheet" href="${ctx}/css/app/orderDetails.css"></link>
	<link rel="stylesheet" href="${ctx}/css/app/orderDetailFont.css"></link>

	<title>订单详情</title>

 <style type="text/css">
 	.condition_inquery{background-color:#c11f2e;color:#fff;padding:10px 10px;font-size:14px;border-radius:3px;}
	.col_bt_time input{width:37%;}
  	.col_bt label,.col_bt_time label{text-align:right;}
  	.col_input_2{width:69%;}
  	.col_input_3{width:59%;}
  	.col_input_4{width:49%;}
  	.mt25{margin-top:25px;}
  	.content-wrapper .content-header .pitch_tab {border-top: 0px;}

  	.ms-controller{visibility: hidden;}
 </style>
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
	<div class="content-wrapper ms-controller" ms-controller=data >
		<section class="content-header">
			<a href="#" class="header_tab pitch_tab">订单详情</a>
		</section>
		<section class="content container-fluid" >
			<div class="row mt20 backgcolor">
				<div class="col-xs-3 order_left">
					<div>
						<span class="order_status" :if="@info.notPaid">待支付</span>
						<span class="order_status" :if="@info.orderStatus == 'WAIT_APPROVE'">待审核</span>
						<span class="order_status" :if="@info.orderStatus == 'REJECTED'">审核不通过</span>
						<span class="order_status" :if="@info.orderStatus == 'WAIT_SHIP'">待发货</span>
						<span class="order_status" :if="@info.orderStatus == 'WAIT_RECEIVE'">待收货</span>
						<span class="order_status" :if="@info.orderStatus == 'COMPLETED'">交易完成</span>
						<span class="order_status" :if="@info.orderStatus == 'CANCELLED'">交易关闭</span>
						<span class="order_status" :if="@info.orderStatus == 'REFUNDED'">已退款</span>
						<i class="icon-state_of_affairs coloG"></i>
						<span class="sp mt20 f14">订单编号：{{@info.id}}</span>
					</div>
					<div class="mt20" :if="@info.innerNoteInfo">
						<span class="poi f14 colblue" ms-click="@showRemarks();">客服备注</span>
						<span class="remarks dn">
							<span class="triangle"><em></em></span>
							<i>{{@toHtml(@info.innerNoteInfo)}}</i>
						</span>
					</div>
					<div class="mt20" :if="@info.isInquiryTag">
						<a class="poi f14 colblue"  ms-attr="{href:ykyUrl.quotation+'/inquiries.htm?action=itemListFromOrder&ids='+@info.inquiryItemIds}"  target="_blank"><span class="poi f14 colblue" >关联报价单</span></a>
					</div>
				</div>
				<div :if="@info.orderStatus != 'CANCELLED'" class="col-xs-9 pt58">
					<div>
						<div :if="@info.inquiryId"><!--  -->
							<div class="dd1 ml27">
								<i class="icon-pending_payment coloC9"></i>
								<label class="lb1">待审核</label>
								<span class="sp1"></span>
							</div>
							<div class="dd2"></div><i class="icon-right_arrow l coloC9"></i>
						</div>
						<div class="dd1 ml27">
							<i class="icon-pending_payment coloC9"></i>
							<label class="lb1">{{(@info.notPaid ? '待支付' : '已支付')}}</label>
							<span class="sp1"></span>
						</div>
						<div class="dd2"></div><i class="icon-right_arrow l coloC9"></i>
						<div class="dd1">
							<i class="icon-to_be_shipped coloC9"></i>
							<label class="lb1">待发货</label>
							<span class="sp1"></span>
						</div>
						<div class="dd2"></div><i class="icon-right_arrow l coloC9"></i>
						<div class="dd1" :if="@info.orderStatus == 'REFUNDED'">
                            <i class="icon-refund_money coloC9"></i>
                            <label class="lb1">已退款</label>
                            <span class="sp1"></span>
                        </div>
						<div class="dd1" :if="@info.orderStatus != 'REFUNDED'">
							<i class="icon-already_shipped coloC9"></i>
							<label class="lb1">已发货</label>
							<span class="sp1"></span>
						</div>
						<div class="dd2" :if="@info.orderStatus != 'REFUNDED'"></div>
						<i class="icon-right_arrow l coloC9" :if="@info.orderStatus != 'REFUNDED'"></i>
						<div class="dd1" :if="@info.orderStatus != 'REFUNDED'">
							<i class="icon-complete_ok coloC9"></i>
							<label class="lb1">交易完成</label>
							<span class="sp1"></span>
						</div>
					</div>
					 <div :if="@info.orderStatus == 'REJECTED'" class="approveInfo_cal l">
		            	<span class="gr">{{@info.approveInfo}}</span>
		            </div>
				</div>
				<!-- 交易关闭   -->
				<div :if="@info.orderStatus == 'CANCELLED'" class="col-xs-9 pt58">
					<div class="dd1 ml27">
						<i class="icon-pending_payment coloG"></i>
						<label class="lb1 colo5E">待支付</label>
						<span class="sp1"></span>
					</div>
					<div class="dd2" style="border-color: #7abd54;"></div>
					<i class="icon-right_arrow l coloG"></i>
					<div class="dd1">
						<i class="icon-canc coloG"></i>
						<label class="lb1 colo5E">交易关闭</label>
						<span class="sp1"></span>
					</div>
				</div>
			</div>
			<!-- 物流信息 -->
			<div class="row mt20 " style="background-color: #fff;border-top:10px solid #f8f8f8;border-bottom:10px solid #f8f8f8;" :if="(@info.orderStatus == 'WAIT_RECEIVE')||(@info.orderStatus == 'COMPLETED')">
					<div class="col-xs-3 " style="border-right: 1px solid #e2e2e2;">
						<div class="dd3" style="border:none;">
							<ul :if="!@info.notPaid">
								<div><span class="sp sp3">配送方式：</span>
									<label :if="@info.shipFlag">快递</label>
									<label :if="!@info.shipFlag">自提</label>
								</div>
								<li :if="@info.orderShipmentPreference">
									<div :if="@info.shipFlag">
										<span class="sp sp3">快递公司：</span>
										<label>{{@toHtml(@info.orderShipmentPreference.carrierPartyId)}}</label>
									</div>
									<div :if="@info.orderShipmentPreference.carrierPartyId != '其他'"><span class="sp sp3">快递单号：</span>
										<label>{{@toHtml(@info.orderShipmentPreference.trackingNumber)}}</label>
									</div>
									<div><span class="sp sp3">送货单号：</span>
										<label>{{@toHtml(@info.orderShipmentPreference.shipmentNumber)}}</label>
									</div>
									<div :if="@info.orderShipmentPreference.carrierPartyId == '其他'">
										<span class="sp sp3 l">配送备注：</span>
										<label  class="coms">{{@toHtml(@info.orderShipmentPreference.comments)}}</label>
									</div>
								</li>
								<li :if="@info.shipFlag && !@info.orderShipmentPreference">
									<div>
										<span class="sp sp3">快递公司：</span>
										<label>--</label>
									</div>
									<div><span class="sp sp3">运费：</span>
										<label>{{@currency}} {{@info.estimatedShipCost|number(2)}}</label>
									</div>
									<div><span class="sp sp3">快递单号：</span>
										<label>--</label>
									</div>
									<div><span class="sp sp3">送货单号：</span>
										<label>--</label>
									</div>
								</li>
							</ul>
							<ul :if="@info.notPaid"><!-- 未付款时   -->
								<div><span class="sp sp3">配送方式：</span>
									<label>--</label></div>
								<div><span class="sp sp3">运费：</span>
									<label>{{@currency}} {{@info.estimatedShipCost|number(2)}}</label>
								</div>
							</ul>
						</div>
					</div>
					<div class="col-xs-9">
						<!-- 快递信息-->
		                <div ms-controller="shipData" class="shipment_wrap">
		                	<div class="shipment_cont" :if="@orderShipment.traces.length>0">
			                	<div class="spmt_item clearfloat" ms-for="($j, item) in @orderShipment.traces">
			                		<i class="spmt_dots" :class="[(item.dateShowTag? '':'vdn')]"></i>
			                		<div class="spmt_item_l"><p><span class="f_date" style="margin-right:5px;margin-left:15px;" ><em  :class="[(item.dateShowTag? '':'vdn')]">{{item.formatDate}}</em></span><span class="f_week" style="margin-right:8px;" ><em :class="[(item.dateShowTag? '':'vdn')]">{{item.formatWeek}}</em></span><span class="f_time">{{item.formatTime}}</span></p></div>
			                		<div class="spmt_item_r">
			                		<p><span class="station_info">{{item.acceptStation}}</span></p>
			                		</div>
			                	</div>

			                </div>
		                </div>
		                <!-- 快递信息 -->
					</div>
			</div>
			<!-- 物流信息 -->
			<div class="row mt20 bd1">
				<div class="col-xs-3 pr0">
					<div class="dd3">
						<div :if="!@info.shipFlag">
							<label class="lb lb3">自提人信息</label>
							<div><span class="sp sp3">联系人：</span>
								<label>{{@toHtml(@info.selfContactMech.contactMech.telecomNumber.mobileTelecomNumber.askForName)}}</label>
							</div>
							<div><span class="sp sp3 l">联系电话：</span>
								<label>{{@toHtml(@info.selfContactMech.contactMech.telecomNumber.mobileTelecomNumber.contactNumber)}}</label>
							</div>
						</div>
						<div :if="@info.shipFlag">
							<label class="lb lb3">收货人信息</label>
							<div :if="@info.shippingContactMech.contactMech.contactMechAttributes.usdCompany">
							    <span class="sp sp3">公司名称：</span>
                                <label>{{@toHtml(@info.shippingContactMech.contactMech.contactMechAttributes.usdCompany.value)}}</label>
                            </div>
							<div><span class="sp sp3">收货人：</span><label>{{@toHtml(@info.shippingContactMech.contactMech.postalAddress.toName)}}</label></div>
							<div><span class="sp sp3 l">地址：</span><label class="wd57">{{@toHtml(@info.shippingContactMech.contactMech.postalAddress.address1)}}</label></div>
							<div><span class="sp sp3">手机：</span><label>{{@toHtml(@info.shippingContactMech.contactMech.telecomNumber.mobileTelecomNumber.contactNumber)}}</label></div>
							<div><span class="sp sp3">邮箱：</span><label>{{@toHtml(@info.shippingContactMech.contactMech.email)}}</label></div>
						</div>
					</div>
				</div>
				<div class="col-xs-3 pr0" :if="@info.shipFlag && @info.orderShipmentPreference">
					<div class="dd3">
						<label class="lb lb3">配送信息</label>
						<ul :if="!@info.notPaid">
							<div><span class="sp sp3">配送方式：</span>
								<label :if="@info.shipFlag">快递</label>
								<label :if="!@info.shipFlag">自提</label>
							</div>
							<li :if="@info.shipFlag && @info.orderShipmentPreference">
								<div>
									<span class="sp sp3">快递公司：</span>
									<label>{{@toHtml(@info.orderShipmentPreference.carrierPartyId)}}</label>
								</div>
								<div><span class="sp sp3">运费：</span>
									<label>{{@currency}} {{@info.estimatedShipCost|number(2)}} </label>
								</div>
								<div :if="@info.orderShipmentPreference.carrierPartyId != '其他'"><span class="sp sp3">快递单号：</span>
									<label>{{@toHtml(@info.orderShipmentPreference.trackingNumber)}}</label>
								</div>
								<div><span class="sp sp3">送货单号：</span>
									<label>{{@toHtml(@info.orderShipmentPreference.shipmentNumber)}}</label>
								</div>
								<div :if="@info.orderShipmentPreference.carrierPartyId == '其他'">
									<span class="sp sp3 l">配送备注：</span>
									<label  class="coms">{{@toHtml(@info.orderShipmentPreference.comments)}}</label>
								</div>
							</li>
							<li :if="@info.shipFlag && !@info.orderShipmentPreference">
								<div>
									<span class="sp sp3">快递公司：</span>
									<label>--</label>
								</div>
								<div><span class="sp sp3">运费：</span>
									<label>{{@currency}} {{@info.estimatedShipCost|number(2)}}</label>
								</div>
								<div><span class="sp sp3">快递单号：</span>
									<label>--</label>
								</div>
								<div><span class="sp sp3">送货单号：</span>
									<label>--</label>
								</div>
							</li>
						</ul>
						<ul :if="@info.notPaid"><!-- 未付款时   -->
							<div><span class="sp sp3">配送方式：</span>
								<label>--</label></div>
							<div><span class="sp sp3">运费：</span>
								<label>{{@currency}} {{@info.estimatedShipCost|number(2)}}</label>
							</div>
						</ul>
					</div>
				</div>
				<div class="col-xs-3 pr0">
					<div class="dd3">
						<label class="lb lb3">付款信息</label>
						<div><span class="sp sp3">付款方式：</span>
							<label :if="@info.notPaid">待支付</label>
							<i :if="@info.orderPay.paymentStatus == 'PMNT_RECEIVED'">
								<label :if="@info.orderPay.paymentMethodId == 'ALIPAY'">支付宝支付</label>
								<label :if="@info.orderPay.paymentMethodId == 'EXT_OFFLINE'">银行汇款</label>
								<label :if="@info.orderPay.paymentMethodId == 'UNIONPAY_B2B'">企业网银支付</label>
								<label :if="@info.orderPay.paymentMethodId == 'UNIONPAY_B2C'">银联支付</label>
								<label :if="@info.orderPay.paymentMethodId == 'WECHATPAY'">微信支付</label>
								<label :if="@info.orderPay.paymentMethodId == 'TEST'">测试支付</label>
							</i>
						</div>
						<div>
							<span class="sp sp3" >付款时间：</span>
							<label :if="@info.orderPay.paymentStatus != 'PMNT_RECEIVED'">--</label>
							<label :if="@info.orderPay.paymentStatus == 'PMNT_RECEIVED'">{{@toHtml(@info.orderPay.createdDate)}}</label>
						</div>
						<div><span class="sp sp3">商品总额：</span><label>{{@currency}} {{@info.grandTotal|number(2)}} </label></div>
						<div><span class="sp sp3">运费金额：</span><label>{{@currency}} {{@info.estimatedShipCost|number(2)}} </label></div>
						<div :if="@info.discountAmount">
							<span class="sp sp3">商品优惠：</span><label>-{{@currency}} {{@info.discountAmount|number(2)}} </label></div>
						<div><span class="sp sp3">应支付总额：</span><label>{{@currency}} {{@info.orderPay.amount|number(2)}} </label></div>
					</div>
				</div>
				<div class="col-xs-3">
					<div class="dd3" :if="@currency == '$'">
						<label class="lb lb3">发票信息</label>
						<div class="dn" :if="@currency == '￥'">
							<div><span class="sp sp3">发票类型：</span>
								<label :if="@info.orderInvoice.invoiceTypeId == 'COMMON'">增值税普通发票</label>
								<label :if="@info.orderInvoice.invoiceTypeId == 'PROFESSIONAL'">增值税专用发票</label>
								<label :if="!@info.orderInvoice">不需要发票</label>
							</div>
							<ul :if="@info.orderInvoice">
								<div>
									<span class="sp sp3 l">单位名称：</span>
									<label class="wd57">{{@toHtml(@info.orderInvoice.invoiceHeader)}}</label>
								</div>
								<li :if="@info.orderInvoice.invoiceTypeId == 'PROFESSIONAL'">
									<div><span class="sp sp3 l">纳税人识别码：</span><label class="wd57">{{@toHtml(@info.orderInvoice.taxCode)}}</label></div>
									<div><span class="sp sp3 l">注册地址：</span><label class="wd57">{{@toHtml(@info.orderInvoice.regAddress)}}</label></div>
									<div><span class="sp sp3">注册电话：</span><label>{{@toHtml(@info.orderInvoice.regPhone)}}</label></div>
									<div><span class="sp sp3">开户银行：</span><label>{{@toHtml(@info.orderInvoice.bankName)}}</label></div>
								</li>
								<div :if="@info.orderInvoice.invoiceTypeId == 'COMMON'">
									<span class="sp sp3">发票内容：</span><label>明细</label>
								</div>
								<div><span class="sp sp3">收票人姓名：</span><label>{{@toHtml(@info.billingContactMech.contactMech.postalAddress.toName)}}</label></div>
								<div><span class="sp sp3 l">地址：</span><label class="wd57">{{@toHtml(@info.billingContactMech.contactMech.postalAddress.address1)}}</label></div>
								<div><span class="sp sp3">手机号码：</span><label>{{@toHtml(@info.billingContactMech.contactMech.telecomNumber.mobileTelecomNumber.contactNumber)}}</label></div>
							</ul>
						</div>
						<div :if="@currency == '$'">
							<div :if="!@info.orderInvoice"><span class="sp sp3 l">发票类型：</span><label>不需要发票</label></div>
							<div :if="@info.orderInvoice"><span class="sp sp3 l">发票类型：</span><label>Invoice</label></div>
			                <div :if="@info.orderInvoice"><span class="sp sp3 l">单位名称：</span><label>{{@toHtml(@info.orderInvoice.invoiceHeader)}}</label></div>
							<div :if="@info.orderInvoice"><span class="sp sp3 l">注册地址：</span><label class="wd57">{{@toHtml(@info.orderInvoice.regAddress)}}</label></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row mt20">
				<label class="f14 mb10">易库易自营</label>
				<table class="table">
					<thead class="backgcolor">
						<tr>
							<td width="40%">商品</td>
							<td width="10%">交货地</td>
							<td>交期</td><td>单价</td>
							<td width="10%">数量</td>
						</tr>
					</thead>
					<tr ms-for="($j, item) in @info.orderItemList">
						<td>
							<span class="content_img">
								<a target="_blank" ms-attr="{href:@toLink(item.productId)}" ><img ms-attr="{src:@toSrc(item.productImgUrl)}" /></a>
			       			</span>
			       			<label class="lb4 mt20"><a class="mpnCla" ms-attr="{href:@toLink(item.productId)}" target="_blank">{{item.mpn}}</a></label>
			       			<label class="lb4 mt5"><span>{{item.brandName}}</span>/<span>{{item.categoryName}}</span></label>
			       			<label class="lb4 mt5"><span>供应商：{{item.vendorName}}</span></label>
				       	</td>
						<td :if="(@currency == '￥' || @currency == 'CNY')">国内</td>
						<td :if="(@currency == '$' || @currency == 'USD')">香港</td>
						<td :if="item.ltTime">{{item.ltTime}}个工作日</td>
						<td :if="!item.ltTime">--</td>
						<td>{{item.price.unitPrice}}</td>
						<td>{{item.quantity}}</td>
					</tr>
				</table>
			</div>
			<div class="user-note-info">
				<span class="remark g6">用户备注：</span>
				<span class="remark-msg g3 m-s-name">{{ @info.userNoteInfo }}</span>
			</div>
			<div class="row mb20">
				<div class="d5">
					<div class="mt10"><label>商品总额：</label><label class="ml10">{{@currency}} {{@info.grandTotal|number(2)}} </label></div>
					<div class="mt10"><label>运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：</label><label class="ml10">{{@currency}} {{@info.estimatedShipCost|number(2)}} </label></div>
					<div class="mt10" :if="@info.discountAmount"><label>商品优惠：</label><label class="ml10">-{{@currency}} {{@info.discountAmount|number(2)}} </label></div>
					<div class="mt10 colorC"><label>应付总额：</label><label class="ml10">{{@currency}} {{@info.orderPay.amount|number(2)}} </label></div>
				</div>
			</div>
		</section>
	</div>
	<script type="text/javascript">
		var selectCatid = "237,238";
	</script>
	<!-- 右边内容显示 -->
	<jsp:directive.include file="../include/main/footer.jsp" />

</div>
<script src="${ctx}/js/lib/jquery.mCustomScrollbar.js"></script>
<script src="${ctx}/js/lib/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript">
	//对Date的扩展，将 Date 转化为指定格式的String
	//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
	//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
	//例子：
	//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
	//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
	//日期格式化
	Date.prototype.Format = function(fmt){
		  var o = {
		    "M+" : this.getMonth()+1,                 //月份
		    "d+" : this.getDate(),                    //日
		    "h+" : this.getHours(),                   //小时
		    "m+" : this.getMinutes(),                 //分
		    "s+" : this.getSeconds(),                 //秒
		    "q+" : Math.floor((this.getMonth()+3)/3), //季度
		    "S"  : this.getMilliseconds()             //毫秒
		  };
		  if(/(y+)/.test(fmt))
		    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
		  for(var k in o)
		    if(new RegExp("("+ k +")").test(fmt))
		  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
		  return fmt;
	}
	Date.prototype.getWeek = function(){
		var a = new Array("日", "一", "二", "三", "四", "五", "六");
		var week = this.getDay();
		return "周"+ a[week];
	}
	//单个数字配零
	 var getDouble  =function(number){
	  var numbers=["0","1","2","3","4","5","6","7","8","9"];
	  for(var i=0;i<numbers.length;i++){
	   if(numbers[i]==number){
	    return "0"+numbers[i];
	   }else if(i==9){
	    return number;
	   }

	  }
	 }

	 var shipData = avalon.define({
		    $id: "shipData",
		    orderShipment: {
		    	"success":false,
		    	"traces":[]
			}});
	 var shipmentUrl = ykyUrl.shipment+'/v1/shipment';
	 /* var shipmentUrl = 'http://localhost:27086'+'/v1/shipment';   */

	 var partyIdToCode ={
		'SF_CARRIER':'SF',   //顺丰
		'KY_CARRIER':'KYSY',  //跨越速运
		'DB_CARRIER': 'DBL' //德邦
	 }
</script>
<script type="text/javascript">

	var showData = avalon.define({
	    $id: "data",
	    info: {
	    	orderItemList:[],
	    	shipFlag:false,
	    	notPaid:false,  //未支付状态
	    	isInquiryTag:false,
	    	inquiryItemIds:'',
		},
		currency:"￥",
		toHtml: function(data){

			if(data=='undefined' || !data){
				return "--";
			}else{
				return data;
			}
		},
		showRemarks:function(){
			var remarks = $(".remarks");
			if(remarks.hasClass("dn")){
				remarks.removeClass("dn");
			}else{
				remarks.addClass("dn");
			}
		},
		toLink:function(id){
			return ykyUrl.portal + "/product/detail.htm?id=" + id;
			/* $(".content a")[i].href = ykyUrl.portal + "/product/detail.htm?id=" + node.productId;
			$(".mpnCla")[i].href = ykyUrl.portal + "/product/detail.htm?id=" + node.productId;
			if(node.productImgUrl){
	    		$(".content_img img")[i].src = node.productImgUrl;
	    	} */
		},
		toSrc:function(src){
			var s = "";
			if(src){
				s = src ;
			}else{
				s = "${ctx}/images/defaultImg01.jpg";
			}
			return s;
		}
	})

	$(function(){
		/* 请求数据 */
		var url = ykyUrl.transaction+"/v1/orders/"+window.location.search.split("id=")[1];
		//var url = "http://localhost:8080/ictrade-operation-web/html/aa.json";
		$.aAjax({
			url: url,
		    success: function(data) {
		    	if(data != ""){
					showData.info.userNoteInfo = data.userNoteInfo
		    		if(data.currency == "CNY"){
		    			showData.currency = "￥";
		    		}else{
		    			showData.currency = "$"
		    		}
		    		if(data.discountAmount){
			    		data.discountAmount = data.discountAmount * -1;
		    		}

					showData.info = $.extend({},showData.info,data);

					//如果满足以下四种情况，为未支付状态
					if(data.orderStatus == 'WAIT_PAID' || data.orderStatus == 'WAIT_CON_PSENT'
						|| data.orderStatus == 'PSENT_RT_FAIL' || data.orderStatus == 'PAY_FAILED'){

						showData.info.notPaid = true;
					}

					//拼接收货人信息：地址
					var shippContactMech = data.shippingContactMech;

					if(shippContactMech){
						showData.info.shipFlag = true;
						var address = shippContactMech.contactMech.postalAddress;
						var newAddress = (address.countryGeoName || '') + address.provinceGeoName + address.cityGeoName + address.countyGeoName + address.address1;
						showData.info.shippingContactMech.contactMech.postalAddress.address1 = newAddress;
					}


					if(data.orderShipmentPreference){
						var carrierPartyId = data.orderShipmentPreference.carrierPartyId;

						var expressMap = {
							"SF_CARRIER": "顺丰快递",
							"KY_CARRIER": "跨越快递",
							"DHL_CARRIER": "DHL快递",
							"OTHER": "其他",
							"DB_CARRIER": "德邦快递"
						}
						var express = expressMap[carrierPartyId] || "";

						//if(carrierPartyId == "SF_CARRIER"){
						//	express = "顺丰快递";
						//}else if(carrierPartyId == "KY_CARRIER"){
						//	express = "跨越快递";
						//}else if(carrierPartyId == "DHL_CARRIER"){
						//	express = "DHL快递";
						//}else if(carrierPartyId == "OTHER"){
						//	express = "其他";
						//}
						showData.info.orderShipmentPreference.carrierPartyId = express;
					}

					//处理邮箱显示格式
					var reg = /(.{2}).+(.{2}@.+)/g;

					/* var bilContactMech = data.billingContactMech;
					if(bilContactMech){
						//拼接发票信息：地址
						var address = bilContactMech.contactMech.postalAddress;
						var newAddress = address.countryGeoName + address.provinceGeoName + address.cityGeoName + address.countyGeoName + address.address1;
						showData.info.billingContactMech.contactMech.postalAddress.address1 = newAddress;
					} */

					//组装流程数据
					var historiesList = data.orderStatusHistories;
					var idx = historiesList.length;
					if(idx < 5){
						if(data.orderStatus != "REJECTED"){
							//$(".step1 li .i1")[idx].className += " gf";
						}
						$.each(historiesList,function(i,node){

							var sp = $(".dd1 .sp1");
							if(node.orderStatus != "REJECTED"){
								$(".dd1 i")[i].className += " coloG";
								if(i > 0){
									$(".dd2")[i-1].style.borderColor = "#7ABD54";
									$(".icon-right_arrow")[i-1].className="icon-right_arrow l coloG";
								}
							}
							sp[i].innerHTML = node.createdDate;
						});
					}
					if(!data.inquiryId){
						$(".dd2").addClass("w22");
					}
					//快递信息
					if(data.orderShipmentPreference&&data.orderShipmentPreference.trackingNumber&&data.orderShipmentPreference.carrierPartyId){
						var code = partyIdToCode[data.orderShipmentPreference.carrierPartyId];
						if(code){
							var shipmentObj = {
								    "logisticCode": data.orderShipmentPreference.trackingNumber,
								    "shipperCode": code
								}
							$.aAjax({
								url: shipmentUrl,
								type:"POST",
								data: JSON.stringify(shipmentObj),
							    success: function(data) {
							    	var tempData = data;
							    	var formatedTraces = [];
							    	$.each(tempData.traces,function(i,n){
							    		var day = new Date(n.acceptTime.replace(/-/g,"/"));//部分IE需要先将2017-02-08格式化为2017/02/08才能进行转化为时间，不然NaN
							    		var formatedDate = day.Format("yyyy-MM-dd");
							    		var formatedTime = getDouble(day.getHours())+":"+getDouble(day.getMinutes())+":"+getDouble(day.getSeconds());;
							    		var formatedWeek = day.getWeek();
							    		formatedTraces.push({
							    			formatDate:formatedDate,
							    			formatWeek:formatedWeek,
							    			formatTime:formatedTime,
							    			acceptStation:n.acceptStation
							    		});
							    	});
							    	if(formatedTraces.length>0){
							    		formatedTraces = formatedTraces.reverse();
							    		formatedTraces[0].dateShowTag = true;
							    		if(formatedTraces.length>1){
							    			for(var index=1;index<formatedTraces.length;index++){
									    		if(formatedTraces[index].formatDate==formatedTraces[index-1].formatDate){
									    			formatedTraces[index].dateShowTag = false;
									    		}else{
									    			formatedTraces[index].dateShowTag = true;
									    		}
									    	}
							    		}
								    	tempData.traces = formatedTraces;
								    	shipData.orderShipment.traces = tempData.traces; //快递信息
								    	$(".shipment_cont").mCustomScrollbar();  //滚动条插件初始化
							    	}
							    },
							    error:function(e){
							    	console.log("系统错误，请刷新重试："+e.responseText);
							    }
							})
						}
					}
					//快递信息
					//是否为询报价订单
					var tempInquiryItemIdString = '';
					var tempInquiryItemIdArray = [];
					$.each(data.orderItemList,function(i,item){
						if(item.inquiryItemId){
							tempInquiryItemIdArray.push(item.inquiryItemId);
						}
					})
					if(tempInquiryItemIdArray.length>0){
						 showData.info.isInquiryTag = true;
						 showData.info.inquiryItemIds = tempInquiryItemIdArray.join(",");
					}

					//
					//组装流程数据
					/*var historiesList = data.orderStatusHistories;
					$.each(historiesList,function(index,node){

						var i = $(".dd1 i");
						var lb = $(".dd1 .lb1");

						if(data.orderStatus == "CANCELLED"){

						}else{
							if(showData.info.notPaid){  //未支付


							}else if(data.orderStatus == "WAIT_SHIP"){  //待发货

								$(".dd2")[0].style.borderColor="#7ABD54";
								$(".icon-right_arrow")[0].className="icon-right_arrow l coloG";

							}else if(data.orderStatus == "WAIT_RECEIVE"){  //已发货

								$(".dd2")[0].style.borderColor="#7ABD54";
								$(".dd2")[1].style.borderColor="#7ABD54";
								$(".icon-right_arrow")[0].className="icon-right_arrow l coloG";
								$(".icon-right_arrow")[1].className="icon-right_arrow l coloG";

							}else if(data.orderStatus == "COMPLETED"){  //已完成

								$(".dd2")[0].style.borderColor="#7ABD54";
								$(".icon-right_arrow")[0].className="icon-right_arrow l coloG";
								$(".dd2")[1].style.borderColor="#7ABD54";
								$(".icon-right_arrow")[1].className="icon-right_arrow l coloG";
								$(".dd2")[2].style.borderColor="#7ABD54";
								$(".icon-right_arrow")[2].className="icon-right_arrow l coloG";

							}
						}
						i[index].className = "icon-pending_payment coloG";
						lb[index].className = "lb1 colo5E";
						$(".dd1 .sp1")[index].innerHTML = node.createdDate;

					})*/

					/* $.each(data.orderItemList,function(i,node){
						//交货地
						if(data.currency == "USD"){
							showData.currency = "$";
							$("#address"+i)[0].innerHTML = "香港";
						}else{
							showData.currency = "￥";
							$("#address"+i)[0].innerHTML = "国内";
						}
					}) */
		    	}
		    }
		})
	})
</script>

</body>
</html>
