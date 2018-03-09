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
  <title>查看优惠券</title>
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
		 		查看优惠券
	  		</h1> 
	  	</section>
	  	<form name="createCouponForm" id="editCouponForm" onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
	  		<section class="content" style="color: rgb(102, 102, 102);">
		  		<div class="row">
		  			<section class="col-lg-12">
		  				<div class="box box-danger ">
		  					<div class="box-header with-border">
							  	<h3 class="box-title">
								  	<span>查看优惠券</span>
							  	</h3>
						  	</div>
						  	<div class="box-body ">
						  		<div class="c-item-wrap">
						  			<div class="c-item-top" v-if="initData.couponType=='DEDUCTION'">
						  				<p class="price"><span>{{initData.unitAmount}}</span> <span v-if="initData.couponCurrency === 'USD'">美元</span><span v-if="initData.couponCurrency === 'CNY'">元</span></p>
						  				<p>单笔订单满 <span>{{initData.consumeLimitAmount}}</span><span v-if="initData.couponCurrency === 'USD'">美元</span><span v-if="initData.couponCurrency === 'CNY'">元</span>使用</p>
						  			</div>
						  			<div class="c-item-top" v-if="initData.couponType=='DISCOUNT'">
						  				<p class="price"><span>{{discountNumberFormated}}</span>折</p>
						  				<p>单笔订单满 <span>{{initData.consumeLimitAmount}}</span><span v-if="initData.couponCurrency === 'USD'">美元</span><span v-if="initData.couponCurrency === 'CNY'">元</span>使用</p>
						  			</div>
						  			<div class="c-item-bottom">
						  				<div v-if="useProductType == 0">
						  					<div v-if="action === 'examine'">
						  						<p v-if="brandNames.length ===0 && vendorNames.length ===0 && categoryNames.length === 0">
													<span >[ 全平台通用 ]</span>
												</p>
								  				<p v-else>
													<span >供应商:</span>
										  			<span v-if="vendorNames.length > 0" class="c-item-bread" :title="vendorNameString">{{vendorNameString}}</span>
										  			<span v-if="vendorNames.length === 0">不限</span>
										  			<span ></span><span >； 制造商:</span>
										  			<span v-if="brandNames.length > 0" class="c-item-bread" :title="brandNameString">{{brandNameString}}</span>
										  			<span v-if="brandNames.length === 0">不限</span>
										  			<span ></span><span >； 分类:</span>
										  			<span v-if="categoryNames.length > 0" class="c-item-bread" :title="categoryNameString">{{categoryNameString}}</span>
										  			<span v-if="categoryNames.length === 0">不限</span>
												</p>
						  					</div>
						  					<div v-else>
						  						<p v-if="brandNames.length ===0 && vendorNames.length ===0 && cateNames.length === 0">
													<span >[ 全平台通用 ]</span>
												</p>
								  				<p v-else>
													<span >供应商:</span>
										  			<span v-if="vendorNames.length > 0" class="c-item-bread" :title="vendorNameString">{{vendorNameString}}</span>
										  			<span v-if="vendorNames.length === 0">不限</span>
										  			<span ></span><span >； 制造商:</span>
										  			<span v-if="brandNames.length > 0" class="c-item-bread" :title="brandNameString">{{brandNameString}}</span>
										  			<span v-if="brandNames.length === 0">不限</span>
										  			<span ></span><span >； 分类:</span>
										  			<span v-if="categoryNames.length > 0" class="c-item-bread" :title="categoryNameString">{{categoryNameString}}</span>
										  			<span v-if="categoryNames.length === 0">不限</span>
												</p>
						  					</div>
										</div>
										<p v-else>[ 指定商品使用 ]</p>
										<p v-if="(initData.thruTypeId =='FROM_GET_DATE')||(initData.thruTypeId =='from_get_date')">有效期：领取后<em>{{initData.limitMonth}}</em>天内有效</p>
										<p v-if="(initData.thruTypeId =='EXACT_DATE_ZONE')||(initData.thruTypeId =='exact_date_zone')">有效期：<span>{{initData.fromDateFormat}}</span>至<span >{{initData.thruDateFormat}}</span></p>
						  				
						  			</div>
						  			<span class="c-item-type">
						  				<em v-show="initData.couponType=='DEDUCTION'">抵扣劵</em>
						  				<em v-show="initData.couponType=='DISCOUNT'">折扣劵</em>
						  			</span>
						  		</div>
						  		<div class="box-body-tit">优惠券基本属性</div>
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">优惠券类型：</label>
									  	<div class="col-sm-9">
									  		<p class="coupon-text">
									  			<span v-if="initData.couponCate == 'PLATFORM_PROMO'">活动推广劵</span>
												<span v-if="initData.couponCate == 'EXACT_PROMO'">定向推广劵</span>
												<span v-if="initData.couponCate == 'OFFLINE_PROMO'">地推类型劵</span>
									  		</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">优惠券名称：</label>
			  							<div class="col-sm-9">
										  	<p class="coupon-text">{{initData.name}}</p>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">有效时间：</label>
			  							<div class="col-sm-9">
										  	<p class="coupon-text">
												<span v-if="(initData.thruTypeId == 'EXACT_DATE_ZONE')||(initData.thruTypeId == 'exact_date_zone')">限时领取</span>
												<span v-if="(initData.thruTypeId == 'FROM_GET_DATE')||(initData.thruTypeId == 'from_get_date')">限时使用</span>
										 	</p>
											<p class="mt10">
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
			  					<div class="form-group" v-if="initData. discountNumber">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">折扣：</label>
			  							<div class="col-sm-9">
										  	<p class="coupon-text">
												<span>{{initData. discountNumber}}</span>
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
												<span v-if="action=='examine'">{{initData.totalQty}}</span>
												<input v-if="action=='edit'" type="text" class="form-control inline coupon_input_s" maxlength="8" v-model="initData.totalQty" />
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
												<span v-if="action=='examine'">{{initData.activityUrl?initData.activityUrl:toDefaultUrl()}}</span>
												<input v-if="action=='edit'" type="text" class="form-control coupon_input inline-block" v-model="initData.activityUrl" placeholder="不填写默认为库存列表页(请加上http://或https://)"/>
											</p>
										  	
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="box-body-tit">领取条件</div>
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
			  					<div class="box-body-tit">使用条件</div>
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
			  							<label class="col-sm-2 control-label">使用商品：<span class="text-red">*</span></label>
			  							<div class="col-sm-9">
			  								<div v-if="action === 'edit'">
										  		<span @click="useProductType = 1" :class="['coupon-btn',{'active': useProductType === 1 }]">指定商品</span> <span :class="['coupon-btn',{'active': useProductType===0} ]" @click="useProductType = 0">指定商品组合</span>
									  		</div>
									  		<div v-if="action === 'examine'" class="coupon-text">
									  			<span v-if="useProductType === 1">指定商品使用</span>
									  			<div v-if="useProductType === 0">
									  				<p v-if="brandNames.length ===0 && vendorNames.length ===0 && categoryNames.length === 0">
														<span >[ 全平台通用 ]</span>
													</p>
									  				<p v-else>
														<span >供应商:</span>
											  			<span v-if="vendorNames.length > 0">{{vendorNameString}}</span>
											  			<span v-if="vendorNames.length === 0">不限</span>
											  			<span ></span><br/><span >制造商:</span>
											  			<span v-if="brandNames.length > 0">{{brandNameString}}</span>
											  			<span v-if="brandNames.length === 0">不限</span>
											  			<span ></span><br/><span >分类:</span>
											  			<span v-if="categoryNames.length > 0">{{categoryNameString}}</span>
											  			<span v-if="categoryNames.length === 0">不限</span>
													</p>
									  			</div>
									  		</div>
									  	</div>
									</div>
									
			  					</div>	
			  					<div>
				  					<div class="coupon-upload" v-show="useProductType === 1">
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
															<th width="80" v-if="action === 'edit'">操作</th>
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
															<td class="table-btn"  v-if="action === 'edit'"><a  @click="deleteProduct($index)">删除</a></td>
														</tr>
													</tbody>
												</table>
												<p class="red"  v-if="action === 'edit'">红色标记为无此商品，请确定商品上架后再上传。最大只支持上传20个商品。</p>
											</div>
											<p  v-if="action === 'edit'"><span class="coupon-btn" @click="uploadProducts()">上传文件</span> <a v-if="couponProductList.length === 0" href="${ctx}/resources/download/优惠券商品上传模板.xlsx">Excel模板下载</a></p>
											<p class="coupon-btn-tip" v-if="couponProductList.length === 0 &&  action === 'edit'">*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现上传商品</p>
										</div>
									</div>
								</div>
								<div v-if="action === 'edit'">
									<div class="coupon-upload" v-show="useProductType === 0">
										<ul>
											<!-- 供应商 -->
											<li>选择供应商：
												<span class="coupon-bread" v-if="vendorNames.length === 0" >不限</span>
												<span class="coupon-bread" v-if="vendorNames.length > 0" v-for="name in vendorNames">{{name}}</span>
												<a class="coupon-edit" ><span @click="chooseVendor()">选择</span></a>
												<div id="vendorApp"  class="vendor-wrap" style="display:none;">
													当前已选：
													<div class="form-group"  v-if="!vendorDatamultiple"> <div class="col-md-12">{{vendorDatasels.name}} </div></div>
												    <div class="form-group" v-if="vendorDatamultiple"> <div class="col-md-12 selected-list"><span class="s-selected" v-for="sel in vendorDatasels" @click="vendorDatadel(sel.id)" ><em>{{sel.name}}</em><i class="icon-close-min"></i></span></div></div>
												    <letter-select
												            :keyname="vendorDatakeyname"
												            :validate="vendorDatavalidate"
												            :options="vendorDataoptions"
												            :id="vendorDataid"
												            :name="vendorDataname"
												            :option-id="vendorDataoptionId"
												            :option-name="vendorDataoptionName"
												            @get-selecteds="vendorDatagetSelected"
												            :selected="vendorDataselected"
												            :multiple="vendorDatamultiple"
												            ref="vendorDataletter"
												            :placeholder="vendorDataplaceholder"
												    ></letter-select>
												</div>
											</li>
											<!-- 供应商 -->
											<li>选择制造商：
												<span class="coupon-bread" v-if="brandNames.length === 0" >不限</span>
												<span class="coupon-bread" v-if="brandNames.length > 0" v-for="name in brandNames">{{name}}</span>
												<a class="coupon-edit" ><span @click="chooseBrand()">选择</span></a>
												<div id="brandApp"  class="vendor-wrap" style="display:none;">
													当前已选：
													<div class="form-group"  v-if="!brandDatamultiple"> <div class="col-md-12">{{brandDatasels.brandName}} </div></div>
												    <div class="form-group" v-if="brandDatamultiple"> <div class="col-md-12 selected-list"><span class="s-selected" v-for="sel in brandDatasels" @click="brandDatadel(sel.id)" ><em>{{sel.brandName}}</em><i class="icon-close-min"></i></span></div></div>
													<letter-select
															v-if="showBrandData"
												            :keyname="brandDatakeyname"
												            :validate="brandDatavalidate"
												            :options="brandDataoptions"
												            :id="brandDataid"
												            :name="brandDataname"
												            :option-id="brandDataoptionId"
												            :option-name="brandDataoptionName"
												            @get-selecteds="brandDatagetSelected"
												            :selected="brandDataselected"
												            :multiple="brandDatamultiple"
												            ref="brandDataletter"
												            :placeholder="brandDataplaceholder"
												            :is-fuzzy-search="brandDataFuzzySearch"
															:reload-api="brandDataReloadApi"
												    ></letter-select>
												</div>
											</li>
											
											<li>选择分类：
												<span class="coupon-bread" v-if="categoryNames.length === 0" >不限</span>
												<span class="coupon-bread" v-if="categoryNames.length > 0" v-for="name in categoryNames">{{name}}</span>
												<a class="coupon-edit" @click="categorySelect()"><span>选择</span></a>
												<div class="category-wrap"  id="categoryWrap" style="display:none;">
										  			<div id="vendorTab" v-if="isCategoryBoolean">
												        <lemmon-tab v-if="showCateData" :api="api" :init-data="VendorInitData" :id="vendorCompomentid" :name="vendorCompomentname" :children="vendorCompomentchildren" @get-selecteds="getLemonTabCheckedList"></lemmon-tab>
												    </div>	
										  		</div>
											</li>
										</ul>
									</div>
								</div>
								
			  					<div class="box-body-tit">其他</div>	
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
		  					<div class="box-footer text-center">
			  					<button type="button" @click="saveData()" class="btn btn-danger c-btn" v-if="action === 'edit'">修改优惠券</button> 
			  					<a href="${ctx}/coupon.htm" type="button"  class="btn btn-concle c-btn">返回</a>
		  					</div>
		  				</div>	
		  			</section>
		  		</div>
		 	</section>
		 </form>
		 <section id="file-upload" style="display:none;">
			<form>
				<div class="form-group">
					<label class="form-label">添加商品：</label>
					<div class="form-control">
						<span class="btn btn-primary">
							上传文件
							<input type="file" id="uploadProductFile"/>
						</span>
						<span v-if="fileName !== ''">{{fileName}}<i class="label-success">上传成功</i></span>
						 <a href="${ctx}/resources/download/优惠券商品上传模板.xlsx">Excel模板下载</a>	
						<div class="tip">*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现上传商品</div>
					</div>
				</div>
			</form>
		</section>
	 </div>
	  	
	<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script> 
<script src="${ctx}/assets/lemon/1.0.0/lemon-category-tab.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/app/editcoupon.js"></script>
</div>	    
</body>
</html>
