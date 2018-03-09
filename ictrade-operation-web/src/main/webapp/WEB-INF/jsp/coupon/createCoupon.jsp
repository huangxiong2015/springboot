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
  <title>创建优惠券</title>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <link rel="stylesheet" href="${ctx}/css/app/coupon.css"/>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<!---index nav  end   ---->
	<div class="content-wrapper" id="createCoupon">
		<section class="content-header">
			<h1>
		 		创建优惠券
	  		</h1> 
	  	</section>
	  	<form name="createCouponForm" id="createCouponForm" onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
	  		<section class="content" style="color: rgb(102, 102, 102);">
		  		<div class="row">
		  			<section class="col-lg-12">
		  				<div class="box box-danger ">
		  					<div class="box-header with-border">
							  	<h3 class="box-title">
								  	<span>创建优惠券</span>
							  	</h3>
						  	</div>
						  	<div class="box-body ">
						  		<div class="c-item-wrap">
						  			<div class="c-item-top" v-show="couponType=='DEDUCTION'">
						  				<p class="price"><span>{{unitAmount}}</span> <span v-if="couponCurrency === 'USD'">美元</span><span v-if="couponCurrency === 'CNY'">元</span></p>
						  				<p>单笔订单满 <span>{{consumeLimitAmount}}</span><span v-if="couponCurrency === 'USD'">美元</span><span v-if="couponCurrency === 'CNY'">元</span>使用</p>
						  			</div>
						  			<div class="c-item-top" v-show="couponType=='DISCOUNT'">
						  				<p class="price"><span ></span>{{discountNumberFormat}}折</p>
						  				<p>单笔订单满 <span>{{consumeLimitAmount}}</span><span v-if="couponCurrency === 'USD'">美元</span><span v-if="couponCurrency === 'CNY'">元</span>使用</p>
						  			</div>
						  			<div class="c-item-bottom">
						  				<div v-if="useProductType == 0">
							  				<p v-if="brandNames.length ===0 && vendorNames.length ===0 && categoryNames.length ===0">
												<span >[ 全平台通用 ]</span>
											</p>
							  				<p v-else>
												<span >供应商:</span>
									  			<span v-if="vendorNames.length > 0" class="c-item-bread" :title="vendorNameString">{{vendorNameString}}</span>
									  			<span v-if="vendorNames.length === 0">不限</span>
									  			<span ></span><span >； 制造商:</span>
									  			<span v-if="brandNames.length > 0" class="c-item-bread" :title="brandNameString">{{brandNameString}}</span>
									  			<span v-if="brandNames.length === 0">不限</span>
									  			<span ></span><span >；分类:</span>
									  			<span v-if="categoryNames.length > 0" class="c-item-bread" :title="categoryNameString">{{categoryNameString}}</span>
									  			<span v-if="categoryNames.length === 0">不限</span>
											</p>
										</div>
										<p v-else>[ 指定商品使用 ]</p>
										<p v-if="(thruTypeId =='FROM_GET_DATE')||(thruTypeId =='from_get_date')">有效期：<span v-show="limitMonth !==''">领取后<em>{{limitMonth}}</em>天内有效</span></p>
										<p v-if="(thruTypeId =='EXACT_DATE_ZONE')||(thruTypeId =='exact_date_zone')">有效期：<span>{{fromDate}}</span><span v-show="thruDate !==''">至{{thruDate}}</span></p>
						  				
						  			</div>
						  			<span class="c-item-type">
						  				<em v-show="couponType=='DEDUCTION'">抵扣劵</em>
						  				<em v-show="couponType=='DISCOUNT'">折扣劵</em>
						  			</span>
						  		</div>
						  		<div class="box-body-tit">优惠券基本属性</div>
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">优惠券类型：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<select class="form-control coupon_input" v-model="couponCate" @change="couponCateChange()">
									  			<option v-for="el in couponCateList" :value="el.value">{{el.text}}</option>
									  		</select>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">优惠券名称：<span class="text-red">*</span></label>
			  							<div class="col-sm-9">
										  	<input type="text" name="name" id="name" class="form-control inline-block coupon_input" maxlength="30" placeholder="不超过30个字" v-model="name"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">有效时间：<span class="text-red">*</span></label>
			  							<div class="col-sm-9">
										  	<div class="mb10">
										  		<select class="form-control coupon_input" v-model="thruTypeId">
										  			<option v-for="el in thruTypeIdList" :value="el.value">{{el.text}}</option>
										  		</select>
										  	</div>
										  	<div v-show="thruTypeId === 'EXACT_DATE_ZONE'" id="thruDateRange" class="input-daterange input-group coupon_input">
					                            <input type="text" name="thruDateStart" id="thruDateStart" class="form-control" v-model="fromDate"> 
					                            <span class="input-group-addon">-</span> 
					                            <input type="text" name="thruDateEnd" id="thruDateEnd" class="form-control" v-model="thruDate">
				                            </div>
				                            <div v-show="thruTypeId === 'FROM_GET_DATE'">领取后  <input type="text" name="limitMonth" class="form-control coupon_input_s inline-block digits" v-model="limitMonth" maxlength="3" min="1"/> 天内有效</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">优惠券形式：<span class="text-red">*</span></label>
			  							<div class="col-sm-9">
										  	<label>
										  		<select class="form-control coupon_input" v-model="couponType">
										  			<option v-for="el in couponTypeList" :value="el.value">{{el.text}}</option>
										  		</select>
										  	</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group" v-if="couponType === 'DEDUCTION'">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">单张面值：<span class="text-red">*</span></label>
			  							<div class="col-sm-9">
										  	<input maxlength='7' type="text" class="form-control coupon_input inline-block unitAmount" name="unitAmount" id="unitAmount" v-model="unitAmount"/> 
										  	<label>
										  		<select class="form-control" v-model="couponCurrency">
										  			<option value="CNY">RMB</option>
										  			<option value="USD">USD</option>
										  		</select>
										  	</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group" v-if="couponType === 'DISCOUNT'">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">折扣：<span class="text-red">*</span></label>
			  							
			  							<div class="col-sm-9">
										  	<input maxlength='7' type="text" class="form-control inline-block coupon_input " style="width:80px;" name="discountNumber" id="discountNumber" v-model="discountNumber"/>
										  	<select class="form-control" v-model="couponCurrency" style="width:77px;display:inline-block;">
									  			<option value="CNY">RMB</option>
									  			<option value="USD">USD</option>
									  		</select> 
									  		<span class="input-tips ml5">请输入0~1之间数字，支持两位小数 ，例：0.9代表9折</span>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">总发行量：<span class="text-red">*</span></label>
			  							<div class="col-sm-9">
										  	<input type="text" maxlength='8' class="form-control coupon_input inline-block" name="totalQty" id="totalQty" v-model="totalQty"/> <span>张</span>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">立即使用链接：</label>
			  							<div class="col-sm-9">
										  	<input type="text" class="form-control coupon_input inline-block" name="activityUrl" id="activityUrl" v-model="activityUrl" placeholder="不填写默认为库存列表页(请加上http://或https://)"/>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="box-body-tit">领取条件</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">发放方式：<span class="text-red">*</span></label>
			  							<div class="col-sm-9" id="couponMethodType">
										  	<label class="check-label"><input type="radio" name="getMethod" value="FREE_WGET" id="free-wget" v-model="couponMethodType" :disabled="couponCate !=='PLATFORM_PROMO'"/> 自助领取</label>
										  	<label class="check-label"><input type="radio" name="getMethod" value="AUTO_SEND" id="auto-send" v-model="couponMethodType" :disabled="couponCate !=='PLATFORM_PROMO'"/> 系统自动发放</label>
										  	<label class="check-label" v-show="couponCate !=='PLATFORM_PROMO' "><input type="radio" name="getMethod" value="EXACT_SEND" id="exact-send" v-model="couponMethodType"/> 定向派发</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group" v-show="couponMethodType === 'AUTO_SEND'">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">发放场景：<span class="text-red">*</span></label>
			  							<div class="col-sm-9" id="businessType">
										  <!-- 	<label class="check-label"><input type="radio" name="businessType" value="NO_LIMIT" v-model="businessType"/> 不限</label> -->
										  	<label class="check-label"><input type="radio" name="businessType" value="PERSON_REG" v-model="businessType"/> 个人注册</label>
										  	<label class="check-label"><input type="radio" name="businessType" value="COMPANY_REG" v-model="businessType"/> 企业注册</label>
										  	<label class="check-label"><input type="radio" name="businessType" value="UP_COMPANY" v-model="businessType"/> 升级企业</label>
										  	<label class="check-label"><input type="radio" name="businessType" value="APPROVE_COMPANY" v-model="businessType"/> 企业认证</label>
										  	<label class="check-label"><input type="radio" name="businessType" value="ACTIVITY_ORDER" v-model="businessType"/> 活动下单成功</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">发放限制：<span class="text-red">*</span></label>
			  							<div class="col-sm-9" id="useLimitPerCustomer">
										  	<label class="check-label"><input type="radio" name="useLimitPerCustomer" value="0"/> 不限</label>
										  	<label class="check-label"><input type="radio" name="useLimitPerCustomer"/> 每用户限领  <input type="text" maxlength="8" class="form-control coupon_input_s inline digits" name="useLimitPer" v-model="useLimitPerCustomer" min="1"/>张</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">发放用户：<span class="text-red">*</span></label>
			  							<div class="col-sm-9" id="couponPartyType">
										  	<label class="check-label"><input name="orientedTarget" type="checkbox" value="PERSON"/> 个人用户</label>
										  	<label class="check-label"><input name="orientedTarget" type="checkbox" value="PARTY_GROUP"/> 企业用户</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="box-body-tit">使用条件</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">使用限额：<span class="text-red">*</span></label>
			  							<div class="col-sm-9" id="deptId">
										  	单笔订单中此优惠券指定货品金额满  <input type="text" name="consumeLimitAmount" id="consumeLimitAmount" maxlength="8" class="form-control coupon_input_s inline" v-model="consumeLimitAmount"/><span v-if="couponCurrency === 'USD'">USD</span><span v-if="couponCurrency === 'CNY'">RMB</span>使用
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">使用商品：<span class="text-red">*</span></label>
			  							<div class="col-sm-9" id="deptId">
										  	<span @click="useProductType = 1" :class="['coupon-btn',{'active': useProductType === 1 }]">指定商品</span> <span :class="['coupon-btn',{'active': useProductType===0} ]" @click="useProductType = 0">指定商品组合</span>
									  	</div>
									</div>
									
			  					</div>	
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
														<th width="80">操作</th>
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
														<td class="table-btn"><a  @click="deleteProduct($index)">删除</a></td>
													</tr>
												</tbody>
											</table>
											<p class="red">红色标记为无此商品，请确定商品上架后再上传。最大只支持上传20个商品。</p>
										</div>
										<p><span class="coupon-btn" @click="uploadProducts()">上传文件</span> <a v-if="couponProductList.length === 0" href="${ctx}/resources/download/优惠券商品上传模板.xlsx">Excel模板下载</a></p>
										<p class="coupon-btn-tip" v-if="couponProductList.length === 0">*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现上传商品</p>
									</div>
								</div>
								
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
												<div class="form-group"  v-if="!brandDatamultiple"> <div class="col-md-12 selected-list">{{brandDatasels.brandName}} </div></div>
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
									  			<div id="vendorTab">
											        <lemmon-tab v-if="showCateData" :api="api" :init-data="initData" id="_id" name="cateName" children="children" ref="categoryLemonTab" @get-selecteds="getLemonTabCheckedList"></lemmon-tab>
											    </div>	
									  		</div>
										</li>
									</ul>
								</div>
								
			  					<div class="box-body-tit">其他</div>	
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label for="categoryTypeId" class="col-sm-2 control-label">优惠券申请凭证：<span class="text-red">*</span></label>
			  							<div class="col-sm-9">
										  	<span v-if="approvedProof" class="img-show" @click="showBigImg()">
										  		<img alt="" :src="approvedProof" style="width:35px;height:35px;border:0;"/>
										  	</span>  
										  	<span class="coupon-btn">选择文件<input type="file" id="uploadFile"/></span> 支持图片格式.jpg .jpeg .png .bmp，文件大小不能超过5MB！
									  	</div>
									  	<img alt="" :src="approvedProof" style="display: none;" class="originalImage"/>
			  						</div>
			  					</div>	
			  					<div class="form-group">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">优惠券备注：<span class="text-red">*</span></label>
			  							<div class="col-sm-9">
										  	<textarea class="form-control coupon_input coupon_textarea" v-model="couponText" maxlength="100" name="couponText" id="couponText" placeholder="可对优惠券进行描述，如：此优惠券为市场部推广申请创建的……"></textarea>
									  	</div>
			  						</div>
			  					</div>
			  				</div>
		  					<div class="box-footer text-center">
			  					<button type="button" @click="saveData()" :disabled="submiting" class="btn btn-danger c-btn">创建优惠券</button> 
			  					<button type="button" @click="cancel()" class="btn btn-concle c-btn">返回</button>
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
<script type="text/javascript" src="${ctx}/js/app/createcoupon.js"></script>
</div>	    
</body>
</html>
