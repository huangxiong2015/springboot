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
  <title>优惠券详情</title>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <link rel="stylesheet" href="${ctx}/css/app/coupon.css"/>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<!---index nav  end   ---->
	<div class="content-wrapper" id="editCoupon">
		<section class="content-header">
			<h1>
		 		优惠券详情
	  		</h1> 
	  	</section>
	  	<form name="createCouponForm" id="editCouponForm" onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
	  		<section class="content" style="color: rgb(102, 102, 102);">
		  		<div class="row">
		  			<section class="col-lg-12">
		  				<div class="box box-danger ">
		  					<div class="box-header with-border">
							  	<h3 class="box-title">
								  	<span>优惠券详情</span>
							  	</h3>
						  	</div>
						  	<div class="box-body ">
						  		<div class="box-body-tit">
						  			<span v-if="initData.couponCate == 'PLATFORM_PROMO'">[活动推广劵]</span>
									<span v-if="initData.couponCate == 'EXACT_PROMO'">[定向推广劵]</span>
									<span v-if="initData.couponCate == 'OFFLINE_PROMO'">[地推类型劵]</span>
									{{initData.name}}
						  		</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">有效时间：</label>
			  							<div class="col-sm-9">
											<p class="coupon-text">
										 		<span class="item_label st2"></span>
										 		<!-- 限时领取 -->
										 		<span style="display:inline-block;" v-show="initData.thruTypeId=='EXACT_DATE_ZONE'">
											 		<span class="rel">{{initData.fromDate}}</span>至<span>{{initData.thruDate}}</span>
								                </span>
								                <!-- 限时领取 -->
								                <span style="display:inline-block;" v-show="initData.thruTypeId=='FROM_GET_DATE'">
								                	<span>领取后<em>{{initData.limitMonth}}</em>天内有效</span>
								                </span>
										 	</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">优惠券形式：</label>
			  							<div class="col-sm-9">
										  	<p class="coupon-text">
										 		<span v-if="initData.couponType == 'DISCOUNT'">折扣劵</span>
												<span v-if="initData.couponType == 'DEDUCTION'">抵扣劵</span>
											</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group" v-if="initData.discountNumber">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">折扣：</label>
			  							<div class="col-sm-9">
										  	<p class="coupon-text">
												<span>{{initData.discountNumber}}</span>
											</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group" v-if="initData.unitAmount">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">单张面值：</label>
			  							<div class="col-sm-9">
										  	<p class="coupon-text">
												<span>{{initData.unitAmount}}</span>
										 		<span v-if="initData.couponCurrency == 'USD'"> USD</span>
												<span v-if="initData.couponCurrency == 'CNY'"> RMB</span>
											</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">总发行量：</label>
			  							<div class="col-sm-9">
										  	<p class="coupon-text">
												<span>{{initData.totalQty}}</span>
												<span>张</span>
											</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">立即使用链接：</label>
			  							<div class="col-sm-9">
										  	<p class="coupon-text">
												<span>{{initData.activityUrl?initData.activityUrl:toDefaultUrl()}}</span>
											</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">发放方式：</label>
			  							<div class="col-sm-9" id="couponMethodType">
										  	<p class="coupon-text">
										  		<span v-if="(initData.couponMethodType=='FREE_WGET')||(initData.couponMethodType=='free_wget')">自助领取</span>
										  		<span v-if="(initData.couponMethodType=='AUTO_SEND')||(initData.couponMethodType=='auto_send')">系统自动发放</span>
										  		<span v-if="(initData.couponMethodType=='EXACT_SEND')||(initData.couponMethodType=='exact_send')">定向派发</span>
									  		</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group" v-if="initData.couponMethodType === 'AUTO_SEND'">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">发放场景：</label>
			  							<div class="col-sm-9" id="businessType">
			  								<p class="coupon-text">
												<span v-if="(initData.businessType=='NO_LIMIT')">不限</span>
												<span v-if="(initData.businessType=='PERSON_REG')">个人注册</span>
												<span v-if="(initData.businessType=='COMPANY_REG')">企业注册</span>
												<span v-if="(initData.businessType=='UP_COMPANY')">升级企业</span>
												<span v-if="(initData.businessType=='APPROVE_COMPANY')">企业认证</span>
												<span v-if="(initData.businessType=='ACTIVITY_ORDER')">活动下单成功</span>
											</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">发放限制：</label>
			  							<div class="col-sm-9" >
			  								<p class="coupon-text">
			  									<span v-if="initData.useLimitPerCustomer == '0'">不限</span>
			  									<span v-if="initData.useLimitPerCustomer != '0'">每用户限领  {{initData.useLimitPerCustomer}}张</span>
			  								</p>
										  	
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">发放用户：</label>
			  							<div class="col-sm-9" id="couponPartyType">
										  	<p class="coupon-text">
												<span v-if="(initData.couponPartyType=='UNLIMIT')||(initData.couponPartyType=='unlimit')">不限</span>
												<span v-if="(initData.couponPartyType=='PARTY_GROUP')||(initData.couponPartyType=='party_group')">企业用户</span>
												<span v-if="(initData.couponPartyType=='PERSON')||(initData.couponPartyType=='person')">个人用户</span>
											</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">使用商品：</label>
			  							<div class="col-sm-9">
			  								<p class="coupon-text" v-if="initData.useProductType === 0">
			  									<span v-if="initData.vendorName === ''&&initData.brandName === ''&&initData.productTypeName === ''">
													<span >全平台通用 </span>
												</span>
												<span v-else>
													<span >供应商:</span>
										  			<span >{{initData.vendorListString}}</span>
										  			<br/><span >制造商:</span>
										  			<span >{{initData.brandListString}}</span>
										  			<br/><span >分类:</span>
										  			<span >{{initData.categoreListString}}</span>
												</span>	
											</p>
											<p v-else class="coupon-text">指定商品使用</p>
										  	<div class="coupon-upload coupon-upload-detail" v-show="initData.useProductType === 1">
												<div class="upload-product">		
													<div class="update-table" v-if="couponProductList.length > 0">
														<table class="table">
															<thead>
																<tr>
																	<th >商品信息</th>
																	<th width="150">供应商</th>
																	<th width="100">库存</th>
																	<th width="150">仓库</th>
																	<th width="100">阶梯</th>
																	<th width="100">价格</th>
																</tr>
															</thead>
															<tbody>
																<tr v-for="(item, $index) in couponProductList">
																	<td>
																		<div class="table-img"><img :src="item.spu.images.length > 0?item.spu.images[0].url:'${ctx}/images/defaultImg01.jpg'"/></div>
																		<p class="p-top" :class="{red: item.id === ''}">{{item.spu.manufacturerPartNumber !== ''?item.spu.manufacturerPartNumber:'--'}}</p>
																		<p :class="{red: item.id === ''}">类型：{{item.spu.categories && item.spu.categories.length > 0?item.spu.categories[2].cateName:'--'}}</p>
																		<p :class="{red: item.id === ''}">品牌：{{item.spu.manufacturer !== ''?item.spu.manufacturer:'--'}}</p>
																	</td>
																	<td :class="{red: item.id === ''}">{{item.vendorName?item.vendorName:'--'}}</td>
																	<td :class="{red: item.id === ''}">{{item.qty?item.qty:'--'}}</td>
																	<td :class="{red: item.id === ''}">{{item.sourceName?item.sourceName:'--'}}</td>
																	<td>
																		<div v-if="item.prices.length>0" :class="{red: item.id === ''}">
																			<p v-if="item.prices[0].priceLevels.length > 0" v-for="con in item.prices[0].priceLevels">{{con.breakQuantity !== ''?con.breakQuantity: '--'}}</p>
																			<p v-if="item.prices[0].priceLevels.length === 0">--</p>
																		</div>	
																		<p v-else :class="{red: item.id === ''}">--</p>
																	</td>
																	<td>
																		<div v-if="item.prices.length > 0" :class="{red: item.id === ''}">
																			<p v-if="item.prices[0].priceLevels.length > 0" v-for="con in item.prices[0].priceLevels">{{con.price?con.price:'--'}}</p>
																			<p v-if="item.prices[0].priceLevels.length === 0">--</p>
																		</div>	
																		<p v-else :class="{red: item.id === ''}">--</p>
																	</td>
																</tr>
															</tbody>
														</table>
													</div>
												</div>
											</div>
									  	</div>
									</div>
			  					</div>	
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">使用限额：</label>
			  							<div class="col-sm-9" >
										  	<p class="coupon-text">
												<span>单笔订单中此优惠券指定货品金额满</span>
												<span class="ml5"><em >{{initData.consumeLimitAmount}}</em></span>
												<span class="currency ml5 mr5"><em v-show="initData.couponCurrency=='CNY'">RMB</em><em v-show="initData.couponCurrency=='USD'">USD</em></span>
												<span>使用</span>
											</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label for="categoryTypeId" class="col-sm-2 control-label">优惠券申请凭证：</label>
			  							<div class="col-sm-9">
										  	<input type="hidden" name="proofUrl" id="proofUrl" />
										  	<div class="item_r">
										  		<p>
										  		<span class="proof_img"><img  id="proofImg" :src="profImg" alt="" @click="showBigImg();"/></span>
										  		<img class="originalImage" style="display:none;" :src="profImg"/>
										  		</p>
										  	</div>
									  	</div>
			  						</div>
			  					</div>	
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">优惠券备注：</label>
			  							<div class="col-sm-9">
										  	<p class="coupon-text">{{initData.couponText}}</p>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="box-body-tit" v-if="logListNull">操作日志</div>	
			  					<div class="form-group" v-if="logListNull">
			  						<div class="col-md-11">
			  							<div class="edit-table">
				  							<table class="table mt10">
										      <thead>
										        <tr>
										          <th>序号</th>
										          <th>操作人</th>
										          <th>时间</th>
										          <th>操作记录</th>
										        </tr>
										      </thead>
										      <tbody>
										        <tr v-for="(item, $index) in logList">
											 		<td>{{$index+1}}</td>
											 		<td>{{item.partyName}}</td>
											 		<td>{{item.createdDate}}</td>
											 		<td :title="item.comment">{{item.comment}}</td>
												</tr>
										      </tbody>
											 </table>
			  							</div>
			  						</div>
			  					</div>
			  				</div>
		  				</div>	
		  			</section>
		  		</div>
		 	</section>
		 </form>
	 </div>
	  	
	<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/app/couponDetail.js"></script>
</div>	    
</body>
</html>
