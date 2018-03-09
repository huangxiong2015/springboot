﻿<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<html lang="zh-CN">
<head>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <title>订单详情</title>

  <!-- 引入样式 -->
    <link rel="stylesheet" href="${ctx}/css/lib/jquery.mCustomScrollbar.css" />
    <link rel="stylesheet" href="${ctx}/css/app/orderDetails.css" />
    <link rel="stylesheet" href="${ctx}/css/app/orderDetailFont.css" />
    <link rel="stylesheet" href="${ctx}/css/common/module.css" />
  <!-- 引入组件库 -->

</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>

	<div class="content-wrapper" id="order-list">
	    <section class="content-header">
            <h1>订单详情</h1>
        </section>
        <div id="root" style="display: none; margin: 10px;">
            <div class="row mt20 backgcolor">
                <div class="col-xs-3 order_left">
                    <div>
                        <span class="order_status">{{ orderStatusFormat }}</span>
                        <i class="icon-state_of_affairs coloG"></i>
                        <span class="sp mt20 f14">订单编号：{{ order.id }}</span>
                        <span class="sp mt20 f14">交易号：{{ order.orderPay.id }}</span>
                    </div>
                    <div class="mt20" style="margin-top: 20px;" v-if="order.innerNoteInfo">
                        <span class="poi f14 colblue" @click="showInnerNoteInfo = !showInnerNoteInfo">客服备注</span>
                        <span v-show="showInnerNoteInfo" class="remarks">
                            <span class="triangle"><em></em></span>
                            <i>{{ order.innerNoteInfo }}</i>
                        </span>
                    </div>
                    <div class="mt20" style="margin-top: 20px;" v-if="order.isInquiryTag">
                        <a
                            class="poi f14 colblue"
                            :href="inquiryHref"
                            target="_blank"
                        >
                            <span class="poi f14 colblue">关联报价单</span>
                        </a>
                    </div>
                </div>

                <div class="col-xs-9 pt58">
                    <div>
                        <template v-for="(item, index) in statusMap">
                            <div v-if="index > 0" class="dd2" :class="getDashClass(item, index)"></div>
                            <i
                                v-if="index > 0" style="float: left" class="icon-right_arrow l "
                                :class="index < orderStatusHistoriesLength ? ['coloG'] : ['coloC9']"
                            ></i>
                            <div class="dd1">
                                <i
                                    :class="index < orderStatusHistoriesLength ? ['coloG', item.className] : ['coloC9', item.className]"
                                ></i>
                                <label class="lb1">{{ item.label }}</label>
                                <span class="sp1">{{ index < (orderStatusHistoriesLength - 1) ? calculateOrderStatusHistories[index + 1].createdDate : '' }}</span>
                            </div>
                        </template>

                    </div>

                     <div v-if="order.orderStatus === 'REJECTED'" class="approveInfo_cal l">
                        <span class="gr">{{ order.approveInfo }}</span>
                    </div>
                </div>
            </div>

            <div class="row mt20 " style="background-color: #fff;border-top:10px solid #f8f8f8;border-bottom:10px solid #f8f8f8; margin-top: 20px;"
                v-if=" order.orderStatus === 'WAIT_RECEIVE' || order.orderStatus === 'COMPLETED' ">
                <div class="col-xs-3 " style="border-right: 1px solid #e2e2e2;">
                    <div class="dd3" style="border:none;">
                        <ul>
                            <div>
                                <span class="sp sp3">配送方式：</span>
                                <span>{{ isNotPay ? ' -- ' : isExpress ? '快递' : '自提' }}</span>
                            </div>

                            <div>
                                <span class="sp sp3">快递公司：</span>
                                <span>{{ expressCompany }}</span>
                            </div>
                            <div>
                                <span class="sp sp3">运费：</span>
                                <span>{{ currencyType }} {{ order.estimatedShipCost }}</span>
                            </div>
                            <div>
                                <span class="sp sp3">快递单号：</span>
                                <span>{{ expressId }}</span>
                            </div>
                            <div>
                                <span class="sp sp3">送货单号：</span>
                                <span>{{ deliveryId }}</span>
                            </div>
                            <div>
                                <span class="sp sp3 l">配送备注：</span>
                                <span  class="coms">{{ expressRemark }}</span>
                            </div>
                        </ul>

                    </div>
                </div>
                <div class="col-xs-9">
                    <!-- 快递信息-->
                    <div class="shipment_wrap">
                        <div class="shipment_cont" v-if="expressMessageArray.length > 0">
                            <div class="spmt_item clearfloat" v-for="item in expressMessageArray">
                                <i class="spmt_dots" :class="[(item.dateShowTag? '':'vdn')]"></i>
                                <div class="spmt_item_l">
                                    <p>
                                        <span class="f_date" style="margin-right:5px;margin-left:15px;" >
                                            <em  :class="[(item.dateShowTag? '':'vdn')]">{{item.formatDate}}</em>
                                        </span>
                                        <span class="f_week" style="margin-right:8px;" >
                                            <em :class="[(item.dateShowTag? '':'vdn')]">{{item.formatWeek}}</em>
                                        </span>
                                        <span class="f_time">{{ item.formatTime }}</span>
                                    </p>
                                </div>
                                <div class="spmt_item_r">
                                <p><span class="station_info">{{ item.acceptStation }}</span></p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 快递信息 -->

                </div>
            </div>


            <!-- 物流信息 -->
            <div class="row mt20 bd1" style="background-color: white; margin-top: 20px;">

                <!--取消原因-->
                <div v-if="order.orderStatus === 'CANCELLED'" class="col-xs-3 pr0">
                    <div class="dd3">
                        <div>
                            <label class="lb lb3">取消原因</label>
                            <div style="padding: 10px;" class="cancel-reason" :title="order.cancelReason || order.replenishmentRemark">
                                {{ order.cancelReason || order.replenishmentRemark }}
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xs-3 pr0">
                    <div class="dd3">
                        <div v-if="!isExpress">
                            <label class="lb lb3">自提人信息</label>
                            <div>
                                <span class="sp sp3">联系人：</span>
                                <span>{{ askForName }}</span>
                            </div>
                            <div>
                                <span class="sp sp3 l">联系电话：</span>
                                <span>{{ contactNumber }}</span>
                            </div>
                        </div>
                        <div v-if="isExpress">
                            <label class="lb lb3">收货人信息</label>
                            <div v-if="order.currency !== 'CNY'">
                                <span class="sp sp3">公司名称：</span>
                                <span>{{ companyName }}</span>
                            </div>
                            <div><span class="sp sp3">收货人：</span><span>{{ toName }}</span></div>
                            <div style="display: flex;"><span class="sp sp3 l">地址：</span><span style="display: inline-block;" class="wd57">{{ address }}</span></div>
                            <div><span class="sp sp3">手机：</span><span>{{ contactNumber2 }}</span></div>
                            <div><span class="sp sp3">邮箱：</span><span>{{ email }}</span></div>
                        </div>
                    </div>
                </div>

                <!--
                <div class="col-xs-3 pr0" v-if="isExpress && order.orderShipmentPreference">
                    <div class="dd3">
                        <label class="lb lb3">配送信息</label>
                        <ul>
                            <div>
                                <span class="sp sp3">配送方式：</span>
                                <span>{{ isNotPay ? ' -- ' : isExpress ? '快递' : '自提' }}</span>
                            </div>

                            <div>
                                <span class="sp sp3">快递公司：</span>
                                <span>{{ expressCompany }}</span>
                            </div>
                            <div>
                                <span class="sp sp3">运费：</span>
                                <span>{{ currencyType }} {{ order.estimatedShipCost }}</span>
                            </div>
                            <div>
                                <span class="sp sp3">快递单号：</span>
                                <span>{{ expressId }}</span>
                            </div>
                            <div>
                                <span class="sp sp3">送货单号：</span>
                                <span>{{ deliveryId }}</span>
                            </div>
                            <div>
                                <span class="sp sp3 l">配送备注：</span>
                                <span  class="coms">{{ expressRemark }}</span>
                            </div>
                        </ul>
                    </div>
                </div>
                -->

                <div class="col-xs-3 pr0">
                    <div class="dd3">
                        <label class="lb lb3">付款信息</label>
                        <div><span class="sp sp3">付款方式：</span>
                            <label v-if="isNotPay">待支付</label>
                            <span v-if="!isNotPay">{{ paymentType }}</span>
                        </div>
                        <div>
                            <span class="sp sp3" >付款时间：</span>
                            <span>{{ order.paymentDate || ' -- ' }}</span>
                        </div>
                        <div><span class="sp sp3">商品总额：</span><span>{{ currencyType }} {{ order.grandTotal }} </span></div>
                        <div><span class="sp sp3">运费金额：</span><span>{{ currencyType }} {{ order.estimatedShipCost }} </span></div>
                        <div v-if="order.discountAmount">
                            <span class="sp sp3">商品优惠：</span><span>{{ currencyType }} {{ order.discountAmount }} </span></div>
                        <div><span class="sp sp3">应支付总额：</span><span>{{ currencyType }} {{ order.orderPay.amount }} </span></div>
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="dd3" v-if="order.currency !== 'CNY'">
                        <label class="lb lb3">发票信息</label>
                        <div>
                            <div v-if="!order.orderInvoice"><span class="sp sp3 l">发票类型：</span><span>不需要发票</span></div>
                            <div v-if="order.orderInvoice"><span class="sp sp3 l">发票类型：</span><span>Invoice</span></div>
                            <div v-if="order.orderInvoice"><span class="sp sp3 l">单位名称：</span><span>{{order.orderInvoice.invoiceHeader}}</span></div>
                            <div v-if="order.orderInvoice"><span class="sp sp3 l">注册地址：</span><span class="wd57">{{order.orderInvoice.regAddress}}</span></div>
                        </div>
                    </div>
                </div>
            </div>


            <div class="row mt20" style="background-color: white; padding: 10px;">
                <span class="f14 mb10" style="display: inline-block; margin: 10px;">易库易自营</span>
                <table class="table">
                    <thead class="backgcolor">
                        <tr>
                            <td width="40%">商品</td>
                            <td width="10%">交货地</td>
                            <td>交期</td>
                            <td>单价</td>
                            <td width="10%">数量</td>
                        </tr>
                    </thead>
                    <tr v-for="item in order.orderItemList">
                        <td style="overflow: visible;">
                            <span class="content_img" style="position: relative">
                                <a target="_blank" :href="toLink(item.productId)" >
                                    <img :src="item.productImgUrl || '${ctx}/images/defaultImg01.jpg' " />
                                    <div v-if="isOrderItemDisable(item)" class="xxx">{{ item.approveReason || '无库存' }}</div>
                                </a>
                            </span>
                            <span class="lb4 mt20">
                                <a class="mpnCla" :href="toLink(item.productId)" target="_blank">{{item.mpn}}</a>
                                <div v-if="isRestrictMaterial(item.restrictMaterialType)" class="goods-hover" style="position: relative; display: inline-block;">
                                    <i class="icon-exclamark audit-icon" style="color: orange;"></i>
                                    <div class="goods-remarks">{{ restrictMaterialFormatter(item.restrictMaterialType) }}</div>
                                </div>
                            </span>
                            <span class="lb4 mt5">
                                <span>{{item.brandName}}</span>/<span>{{item.categoryName}}</span>
                                <span class="audit-hover" style="position: relative; float: right;" v-if="item.approveRemark">
                                    <i class="icon-exclamark audit-icon" style="color: orange;"></i>
                                    <div class="show-remarks" v-if="item.approveRemark" style="word-break: initial;">{{ item.approveRemark }}</div>
                                </span>
                            </span>
                            <span class="lb4 mt5"><span>供应商：{{item.vendorName}}</span></span>

                        </td>
                        <td>{{ order.currency === 'CNY' ? '国内' : '香港' }}</td>
                        <td>
                            <span v-if="!isOrderItemDisable(item)">{{ formatterLtTime(item) }}</span>
                            <span v-if="isOrderItemDisable(item)"> -- </span>
                        </td>
                        <td>
                            <span v-if="!isOrderItemDisable(item)">{{ item.price.unitPrice }}</span>
                            <span v-if="isOrderItemDisable(item)"> -- </span>
                        </td>
                        <td>
                            <span v-if="!isOrderItemDisable(item)">{{ item.quantity }}</span>
                            <span v-if="isOrderItemDisable(item)"> -- </span>
                        </td>
                    </tr>
                </table>

                <div class="user-note-info">
                    <span class="remark g6">用户备注：</span>
                    <span class="remark-msg g3 m-s-name">{{ order.userNoteInfo }}</span>
                </div>

                <div class="row pb20">
                    <div class="d5">
                        <div class="mt10"><span>商品总额：</span><span class="ml10">{{ currencyType }} {{ order.grandTotal }} </span></div>
                        <div class="mt10"><span>运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：</span><span class="ml10">{{ currencyType }} {{ order.estimatedShipCost }} </span></div>

                        <div class="mt10" v-if="order.discountAmount"><span>商品优惠：</span><span class="ml10">{{ currencyType }} {{ order.discountAmount }} </span></div>


                        <div class="mt10">
                            <span>补&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;价：</span>
                            <span class="ml10 audit-hover" style="position: relative;">{{ currencyType }} {{ order.replenishment || 0 }}
                                <i class="icon-exclamark audit-icon" v-if="order.replenishmentRemark" style="color: orange;"></i>
                                <div class="show-remarks" v-if="order.replenishmentRemark">{{ order.replenishmentRemark }}</div>
                            </span>
                        </div>

                       <!--
                        <div>
                            <span class="item-title">补价：</span>
                            <span class="r audit-hover item-content" style="position: relative;">{{ toCurrency }}{{ order.replenishment || 0 }}
                                <i class="icon-ask audit-icon" v-if="order.replenishmentRemark"></i>
                                <div class="show-remarks" v-if="order.replenishmentRemark">{{ order.replenishmentRemark }}</div>
                            </span>
                        </div>
                        -->

                        <div class="mt10 colorC"><span>应付总额：</span><span class="ml10">{{ currencyType }} {{ order.orderPay.amount }} </span></div>
                    </div>
                </div>
            </div>

        </div>
    </div>

	<jsp:directive.include file="../include/lemon/footer.jsp" />
</div>
<script type="text/javascript" src="${ctx}/js/app/orderDetails.js"></script>


</body>
</html>
