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
  <title>供应商详情</title>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <link rel="stylesheet" href="${ctx}/css/app/vendor.css"/>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
<input type="hidden" id="userId" value="${userId}"/>
	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<!---index nav  end   ---->
	<div class="content-wrapper" id="detailVendor">
		<section class="content-header">
			<h1>
		 		创建供应商
	  		</h1> 
	  	</section>
	  	<div class="content-header vendor-header" >
	  		<a :class="{header_tab:true,active: tabValue === 'baseData'}" @click="tabValue = 'baseData'">基本信息</a>
	  		<a :class="{header_tab:true,active: tabValue === 'product'}" @click="tabValue = 'product'">产品线</a>
	  		<a :class="{header_tab:true,active: tabValue === 'credit'}" @click="tabValue = 'credit'">信用</a>
	  		<a :class="{header_tab:true,active: tabValue === 'sales'}" @click="tabValue = 'sales'" v-if="!noSales">销售信息</a>
	  		<!-- <a :class="{header_tab:true,active: tabValue === 'vendorAlias'}" @click="tabValue = 'vendorAlias'">供应商别名</a> -->
	  	</div>
  		<section class="content" style="color: rgb(102, 102, 102);">
	  		<div class="row">
	  			<section class="col-lg-12">
	  				<div class="box box-danger ">
	  					<div class="box-header">
	  						<div>供应商名称：{{baseData.groupNameFull}}</div>
	  					</div>
					  	<div class="box-body ">
					  		<!-- 基本信息表单 -->
					  		<form name="createVendorBase" id="createVendorBase" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'baseData'">
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商全称：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.groupNameFull}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商简称：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.groupName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商编码：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.partyCode}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">所属分类：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.categoryName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">核心供应商：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span v-if="baseData.isCore === 'Y'">是</span>
									  			<span v-if="baseData.isCore === 'N'">否</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商logo：</label>
									  	<div class="col-sm-9">
									  		<span class="upload-logo">
									  			<img alt="" :src="vendorLogo?vendorLogo: '../images/default_logo.png'" width="100" height="100"/>
									  		</span>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商官网：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.vendorInfoAttributeMap && baseData.vendorInfoAttributeMap.VENDOR_INFO_WEBSITE?baseData.vendorInfoAttributeMap.VENDOR_INFO_WEBSITE:'-'}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">所属地区：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.regionName}}
									  		</div>
									  		<!-- <select class="form-control vendor-input" name="region" v-model="baseData.region">
									  			<option v-for="item in regionList" :value="item.categoryId">{{item.categoryName}}</option>
									  		</select> -->
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商总公司：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.generalHeadquarters}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">公司类型：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.genreName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否上市：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span class="mb" v-if="baseData.listed === 'Y'">是</span>
									  			<span class="mb"  v-if="baseData.listed === 'Y'">股票代码：{{baseData.stockCode}}</span>
									  			<span v-if="baseData.listed === 'N'">否</span>
									  		</div>
									  		
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">公司法人：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.vendorInfoAttributeMap && baseData.vendorInfoAttributeMap.VENDOR_INFO_LEGALPERSON?baseData.vendorInfoAttributeMap.VENDOR_INFO_LEGALPERSON:'-'}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">注册资金：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.vendorInfoAttributeMap && baseData.vendorInfoAttributeMap.VENDOR_INFO_REGPRICE?baseData.vendorInfoAttributeMap.VENDOR_INFO_REGPRICE:'-'}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">注册地址：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.vendorInfoAttributeMap && baseData.vendorInfoAttributeMap.VENDOR_INFO_REGRADDRESS?baseData.vendorInfoAttributeMap.VENDOR_INFO_REGRADDRESS:'-'}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">员工人数：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.vendorInfoAttributeMap && baseData.vendorInfoAttributeMap.VENDOR_INFO_EMPLOYEENUM?baseData.vendorInfoAttributeMap.VENDOR_INFO_EMPLOYEENUM:'-'}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">分管部门：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.deptName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">负责人：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.principalName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">询价员：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.enquiryName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">报价员：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.offerName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
		  					</form>
		  					<!-- 基本信息表单 -->
		  					<!-- 代理产品线表单 -->
		  					<form name="createVendorProduct" id="createVendorProduct" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'product'">
		  						<div class="product-label">
		  							<span :class="{'product-tab':true,'product-active': prodctType === 'PROXY'}" @click="productLineChange('PROXY')">代理产品线</span>
		  							<span :class="{'product-tab':true,'product-active': prodctType === 'NOT_PROXY'}" @click="productLineChange('NOT_PROXY')">不代理择产品线</span>
	  							</div>
		  								  						
		  						<div class="pull-right" style="position: relative;top: -60px;">
					                   <input type="button" class="btn btn-light" @click="exportProductList" value="导出">
					            </div>
		  						
		  						<div class="product-table">
		  							<table class="table table-head">
		  								<thead>
		  									<tr>
		  										<th>品牌</th>
		  										<th>大类</th>
		  										<th>小类</th>
		  										<th>次小类</th>
		  									</tr>
		  								</thead>
		  							</table>
		  							<div class="table-body">
		  								<table class="table">
		  									<tbody>
		  										<tr v-for="(item, $index) in productLines">
		  											<td>{{item.brandName}}</td>
		  											<td>{{item.category1Name?item.category1Name: '不限'}}</td>
		  											<td><span v-if="item.category1Name">{{item.category2Name?item.category2Name: '不限'}}</span><span v-else>*</span></td>
		  											<td><span v-if="item.category2Name">{{item.category3Name?item.category3Name: '不限'}}</span><span v-else>*</span></td>
		  										</tr>
		  									</tbody>
		  								</table>
		  							</div>
		  						</div>
		  					</form>
		  					<!-- 代理产品线表单 -->
		  					
		  					<!-- 信用表单 -->
		  					<form name="createVendorCredit" id="createVendorCredit" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'credit'">
		  						<!-- 账期信息 -->
		  						<div class="product-label">账期信息</div>
		  						<div class="form-group ">
		  							<label for="title" class="col-sm-2 control-label">信用额度：</label>
								  	<div class="col-sm-9">
								  		<div class="vendor-text">
								  			{{creditInfo.creditQuota}}
								  			<span v-if="creditInfo.currency === 'CNY'">RMB</span>
								  			<span v-if="creditInfo.currency === 'USD'">USD</span>
								  		</div>
								  	</div>
								</div>
							  	<div class="form-group ">
		  							<label for="title" class="col-sm-2 control-label">授信期限：</label>
								  	<div class="col-sm-9">
								  		<div class="vendor-text">
								  			{{creditInfo.creditDeadline}}
								  		</div>
								  	</div>
							  	</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">对账日期：</label>
								  	<div class="col-sm-9">
								  		<div class="vendor-text">
								  			{{creditInfo.checkDate}}天
								  		</div>
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">付款日期：</label>
								  	<div class="col-sm-9">
								  		<div class="vendor-text">
								  			{{creditInfo.payDate}}日
								  		</div>
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">付款条件：</label>
								  	<div class="col-sm-9">
								  		<div class="vendor-text">
								  			{{creditInfo.paymentTerms}}日
								  		</div>
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">结算方式：</label>
								  	<div class="col-sm-9">
								  		<div class="vendor-text">
								  			{{creditInfo.settlementMethod}}日
								  		</div>
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">应付余额：</label>
								  	<div class="col-sm-8">
								  		<div class="vendor-text">--</div>
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">剩余额度：</label>
								  	<div class="col-sm-9">
								  		<div class="vendor-text">--</div>
								  	</div>
			  						
			  					</div>
			  					<!-- 账期信息 -->
			  					<!-- 银行资料 -->
			  					<div class="product-label">银行资料</div>
			  					<ul class="bank-info-list">
			  						<li v-for="(bank, $index) in creditInfo.partyBankAccount">
			  							<div class="bank-info bank-h">
			  								<div class="back-info-h">{{bank.currency==='CNY'?'RMB':bank.currency}}账号 <span v-if="bank.isDefault === 'Y'">默认</span></div>
			  								<div class="back-info-item">
			  									账户名称 : <span :title="bank.accountName">{{bank.accountName}}</span>
			  								</div>
			  								<div class="back-info-item">
			  									开户银行 : <span :title="bank.bankName">{{bank.bankName}}</span>
			  								</div>
			  								<div class="back-info-item">
			  									银行账号 : <span :title="bank.bankAccount">{{bank.bankAccount}}</span>
			  								</div>
			  								<div class="back-info-item" v-if="bank.currency === 'CNY'">
			  									税号 : <span :title="bank.taxNumber">{{bank.taxNumber}}</span>
			  								</div>
			  								<div class="back-info-item">
			  									地址 : <span :title="bank.address">{{bank.address}}</span>
			  								</div>
			  								<div class="back-info-item" v-if="bank.currency === 'CNY'">
			  									电话 : <span :title="bank.contactNumber">{{bank.contactNumber}}</span>
			  								</div>
			  								<div class="back-info-item" v-if="bank.currency === 'USD'">
			  									SWIFT CODE : <span :title="bank.swiftCode">{{bank.swiftCode}}</span>
			  								</div>
			  								<!-- <div class="bank-info-btn"><a href="javascript:void(0);" @click="addBankInfo('edit', $index)">编辑</a><a href="javascript:void(0);" @click="deleteBankInfo($index)">删除</a></div> -->
			  							</div>
			  						</li>
			  					</ul>
			  					<!-- <div class="bank-info-create">
			  						<button type="button" @click="addBankInfo('add')" class="btn btn-danger c-btn">新增银行账号</button>
			  					</div> -->
			  					<!-- 银行资料 -->
			  					<!-- 协议 -->
			  					<div class="product-label">协议</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">采购协议：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">
									  			<span v-if="creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEAL === 'ALREADY_SIGN'">已签</span>
									  			<span v-if="creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEAL === 'NOT_SIGN'">未签</span>
									  			<span v-if="creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEAL === 'ALREADY_SPE_SIGN'">已特批</span>
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">协议有效期：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">
									  			{{creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEALDATE}}
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">保密协议：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">
									  			<span v-if="creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOL === 'ALREADY_SIGN'">已签</span>
									  			<span v-if="creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOL === 'NOT_SIGN'">未签</span>
									  			<span v-if="creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOL === 'ALREADY_SPE_SIGN'">已特批</span>
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">协议有效期：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">
									  			{{creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE}}
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">附件：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text" v-for="(item,index) in creditInfo.creditAttachmentList">
									  			<span><a v-bind="{'href':downLoadUrl[index]}">{{item.attachmentName}}</a></span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<!-- 协议 -->
		  					</form>
		  					<!-- 信用表单 -->
		  					<!-- 销售信息 -->
		  					<div v-if="!noSales">
		  					<form name="createVendorSales" id="createVendorSales" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'sales'">
		  						
		  						<div class="product-label">
			  					   <span  class="nav_tab "><a  class="tab"  @click="oninfoChange">销售基本信息</a></span>
			  					   <span  class="nav_tab ml20"><a  @click="onmovChange">MOV设置</a></span>
			  					   <span  class="nav_tab ml20"><a  @click="onSpecialOfferChange">专属特价设置</a></span>
		  					    </div>
		  					    <div v-show ="subTabValue === 'nextbaseData'">
		  					            <div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">负责人：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.principalName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">询价员：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.enquiryName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">报价员：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.offerName}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">支持币种：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.supportCurrency}}
									  		</div>
									  		<!-- <label class="form-label"><input type="checkbox" value="RMB" name="currencyRmb"/> RMB</label>
									  		<label class="form-label"><input type="checkbox" value="USD" name="currencyUsd"/> USD</label> -->
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商最小接单金额：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span class="mb" v-if="salesInfo.minOrderPriceCNY && salesInfo.minOrderPriceCNY !== '' && salesInfo.minOrderPriceCNY !== 0">{{salesInfo.minOrderPriceCNY}} RMB</span>
									  			<span class="mb" v-if="salesInfo.minOrderPriceUSD && salesInfo.minOrderPriceUSD !== '' && salesInfo.minOrderPriceUSD !== 0">{{salesInfo.minOrderPriceUSD}} USD</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
		  						<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否审核订单：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span v-if="salesInfo.orderVerify==='Y'">是</span>
									  			<span v-if="salesInfo.orderVerify==='N'">否</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否显示名称：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span v-if="salesInfo.isShowName==='Y'">是</span>
									  			<span v-if="salesInfo.isShowName==='N'">否</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否启用MOV：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span v-if="salesInfo.vendorMovStatus==='Y'">供应商MOV</span>
									  			<span v-if="salesInfo.skuMovStatus==='Y'">商品MOV</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商仓库：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span class="mb" v-for="(item, $index) in salesInfo.facilityList">{{item.facilityName}}<span style="margin-left:20px;" v-if="item.isShowaFacility == 'Y'">显示仓库</span></span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<!-- <div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">商品库存最大有效天数：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  		{{salesInfo.productInvalidDays}}天
									  		</div>
									  	</div>
			  						</div>
			  					</div> -->
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否自动集成库存：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span v-if="salesInfo.isAutoIntegrateQty==='Y'">是</span>
									  			<span v-if="salesInfo.isAutoIntegrateQty==='N'">否</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">装运条款：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.description}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">应付余额：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">--</div>
									  		<!-- <div class="vendor-text">{{vendorSaleInfoVo.balanceDue?vendorSaleInfoVo.balanceDue:'--'}}</div> -->
									  	</div>
			  						</div>
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">剩余余额：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">--</div>
									  		<!-- <div class="vendor-text">{{vendorSaleInfoVo.balanceDue?vendorSaleInfoVo.balanceDue:'--'}}</div> -->
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">最后交易日期：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">--</div>
									  		<!-- <div class="vendor-text">{{vendorSaleInfoVo.lastTransactionDate?vendorSaleInfoVo.lastTransactionDate:'--'}}</div> -->
									  	</div>
			  						</div>
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">最后交易金额：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">--</div>
									  		<!-- <div class="vendor-text">{{vendorSaleInfoVo.lastTransactionPrice?vendorSaleInfoVo.lastTransactionPrice:'--'}}</div> -->
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">最后付款日期：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">--</div>
									  		<!-- <div class="vendor-text">{{vendorSaleInfoVo.lastPayDate?vendorSaleInfoVo.lastPayDate:'--'}}</div> -->
									  	</div>
			  						</div>
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">最后付款金额：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">--</div>
									  		<!-- <div class="vendor-text">{{vendorSaleInfoVo.lastPaymPrice?vendorSaleInfoVo.lastPaymPrice:'--'}}</div> -->
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">关注领域：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_FOCUSFIELDS}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">优势产品类别：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_PRODUCTCATEGORYS}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">主要客户：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_MAJORCLIENTS}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  						
			  					<div class="product-label" v-if="!noContactInfo">联系人信息</div>
			  					<!-- <div class="bank-info-create">
			  						<button type="button" @click="addContact('add')" class="btn btn-danger c-btn">新增联系人</button>
			  					</div> -->
			  					<ul class="bank-info-list" v-if="!noContactInfo">
			  						<li v-for="(item, $index) in salesInfo.contactPersonInfoList">
			  							<div class="bank-info">
			  								<div class="back-info-h"><div class="contact-name" :title="item.lastNameLocal">{{item.lastNameLocal}}</div></div>
			  								<div class="back-info-item">
			  									职位: {{item.occupation}}
			  								</div>
			  								<div class="back-info-item">
			  									职能: <span v-if="item.personalTitle === 'ENQUIRY'">询价</span><span v-if="item.personalTitle === 'ORDER'">下单</span><span v-if="item.personalTitle === 'NOT_LIMIT'">询价、下单</span>
			  								</div>
			  								<div class="back-info-item">
			  									邮箱: {{item.mail}}
			  								</div>
			  								<div class="back-info-item">
			  									电话: {{item.fixedtel}}
			  								</div>
			  								<div class="back-info-item">
			  									手机: {{item.tel}}
			  								</div>
			  								<div class="back-info-item" >
			  									联系人地址: <span :title="item.address">{{item.address}}</span>
			  								</div>
			  								<div class="back-info-item">
			  									产品线: 
			  									<div class="contact-productline back-productline">
			  										<div v-if="item.partyProductLineList && item.partyProductLineList.length > 0" v-for="productline in item.partyProductLineList">
			  											<p :title="(productline.brandName)+'-'+(!productline.category1Name?'不限':productline.category1Name)+'-'+(!productline.category2Name?'不限':productline.category2Name)+'-'+(!productline.category3Name?'不限':productline.category3Name)">
			  												<span>{{productline.brandName}}</span>-
			  												<span>{{!productline.category1Name?'不限':productline.category1Name}}</span>-
			  												<span>{{!productline.category2Name?'不限':productline.category2Name}}</span>-
			  												<span>{{!productline.category3Name?'不限':productline.category3Name}}</span>
			  											</p>
			  										</div>
			  									</div>
			  								</div>	
			  							</div>
			  						</li>
			  					</ul>
			  					<div class="bank-info-create" v-if="!noContactInfo"><i class="red-tip">*</i> 为当前默认联系人</div>
		  					    
		  					     </div>
		  					     <div v-show ="subTabValue === 'nextmovData'">
		  					       			<!-- 		<div class="cont_nav g3" >
								    			<span class="f14" style="line-height:30px;margin-left: 20px;">已保存的MOV规则</span>
								    			<a class="r but_cl"  @click="addRules()"><i class="icon-plus mr10 f12" style="font-weight:bold;"></i>新增MOV规则</a>
								    		</div> -->
								    		<div class="date_list r_edit mt30" >
								    			<div class="tem_content"  v-for="el in movList.list" >
									    				<p class="tem_tlt bb0"  >
									    					<span class="dib g3 b over_tw l" style="width:30%"><i v-html="el.ruleType == '0' ?'供应商MOV规则':'商品MOV规则'"></i></span>
									    					<span class="dib g3 tr" style="width:40%">创建时间：<i v-html="el.createdDate?el.createdDate:'--'"></i></span>
									    					 <span class="dib tr mov-operate" style="width:29%;float:right;" >
									    					     <span class="blue" href="javascript:;" v-if="el.status == 'DISABLED'" >已停用</span>
									 							 <span class="blue" href="javascript:;" v-if="el.status=='ENABLED'" >已启用</span> 
									    					</span> 
									    				</p>	    					    				
									    				<table class="freight_table" cellspacing="0" cellpadding="0">
									    					<tr>
									    						<th style="width:10%" class="pl30">供应商</th>
									    						<th style="width:15%;">商品仓库</th>
									    						<th style="width:25%;">制造商</th>
									    						<th style="width:20%;">交货地</th>
									    						<th style="width:30%;" v-html="el.ruleType == '0' ?'供应商MOV':'商品MOV'"></th>
									    					</tr>
									    					<tr>
									    					    <td rowspan="2" class="pl30" >{{baseData.groupNameFull}}</td>
									    						<td rowspan="2" class="pl30" v-html="el.sourceName?el.sourceName:'不限'"></td>
									    						<td rowspan="2" class="pl30" v-html="el.manufacturerName?el.manufacturerName:'不限'"></td>
									    						<td>香港</td>
									    						<td v-html="el.usdMovAmount?el.usdMovAmount:'--'"></td>	
									    						
									    					</tr>
									    					<tr>
									    						<td>国内</td>
									    						<td v-html="el.cnyMovAmount?el.cnyMovAmount:'--'"></td>	
									    					</tr>
									    					<tr>
									    						<td colspan="5" class="pl30 rel tr_last_child">
									    							<em class="abs" style="top:11px;">备注：</em><span class="dib ml50 mr20 lh25">{{el.description}}</span>
									    						</td>
									    					</tr>
									    				</table>	    					    				
									    		</div>   			
								    		</div>
		  					     </div>
		  					     <!-- 专属特价设置 -->
		  					     <div class="special-set" v-show ="subTabValue === 'nextSpecialData'">
		  					     	<div class="col-md-11">
			  							<label class="col-sm-2 control-label">供应商专属特价显示：</label>
									  	<div class="col-sm-4">
									  		<div >
									  			<span class="radio-wrap" v-for="(val,key) in specialRuleStatusList">
										  			<input type="radio" name="specialType" :disabled="true" :id="key" :value="key" v-model="specialRuleStatus">
										  			<label :for="key" v-text="val"></label>
										  		</span>
									  		</div>
								  		</div>
								  		<label class="col-sm-2 control-label">显示文案：</label>
								  		<div class="col-sm-3">
								  			<div class="vendor-text">
								  				<span v-text="specialRulesText"></span>
								  			</div>
								  		</div>
					  				</div>
					  				<div v-if="!showSpecialDetail" >
						  				<section class="col-md-12" style="width:99%; margin-top:20px;">
											<div class="box box-solid ">
												<div class="box-body customCol">
									              <form class="form-horizontal" method="get" name="speListSeachForm" id="speListSeachForm" >
											   		 <div class="row">
								                  		<div class="col-sm-12 col-md-11 col-lg-10 bor-rig-light">
												              <div class="col-sm-7 col-md-4 col-lg-6 margin10"> 
												                  <div class="form-group-sm">
												                      <label for="payData" class="col-sm-4 col-md-4 col-lg-3 control-label"> 创建时间 </label>
												                        <div id="createData" class="col-sm-7 col-md-8 col-lg-7 ">
												                          <div id="createDataRange" class="input-daterange input-group">
														                      <input type="text" name="speListStartDate" id="speListStartDate" class="input-sm form-control"> 
														                      <span class="input-group-addon">至</span> 
														                      <input type="text" name="speListEndDate" id="speListEndDate" class="input-sm form-control">
													                      </div>
												                      </div>
												                  </div>
												              </div>
												          
												          	  <div class="col-sm-6 col-md-4 col-lg-6 margin10">
													               <label for="status" class="col-sm-2 control-label">规则类型</label>
										                           <div class="col-sm-4" >
											                          <select class="form-control" id="status" name="status" v-model="speListRuleType">
											                            <option value="">全部</option>
											                            <option value="RULE">产品线</option>
											                            <option value="MPN">型号</option>
											                          </select>
										                           </div>
												               </div>
												          </div>
												          
												          <div class="col-lg-2">
										                    <button class="btn btn-sm btn-danger sendData margin10" id="search_all" @click="onSearchSpeListClick()">
										                      <i class="fa fa-search"></i>查询
										                    </button>
										                  </div>
									                  </div>
											       	</form>
												</div>
											 </div>
								        </section>
			  					     	<div class="col-sm-12" style="margin-top:20px;">
			  					     	    <div class="box ">
								                <div class="chart box-body " style="position: relative;">
								                <rowspan-table
								                      :api="speListUrl"
	    											  :columns="speListColumns"
												      :pageflag="speListPageflag"
												      :query-params="speListQueryParams"
												      :refresh="speListRefresh" 
								                >
								                </rowspan-table>
								            </div>
								          </div>
								        </div>
							        </div>
							        <div v-if="showSpecialDetail">
			  				   			<div class="form-group ">
					  						<div class="col-md-11">
					  							<label class="col-sm-2 control-label">规则类型：</label>
											  	<div class="col-sm-9 pdt7" v-text="specialRuleType == 'RULE'?'产品线':'型号'"></div>
					  						</div>
					  					</div>
					  					<div v-if="specialRuleType == 'MPN'">
						  					<div class="form-group ">
						  						<div class="col-md-11">
						  							<label class="col-sm-2 control-label">应用型号：</label>
												  	<div class="col-sm-9">
												  		<rowspan-table
														        :api="uploadPrdStandardApi"
														        :columns="uploadPrdColumns"
														        :pageflag="uploadPrdPageflag"
														        :query-params="uploadPrdQueryParams"
														></rowspan-table>
											  		</div>
						  						</div>
						  					</div>
					  					</div>
					  					<div v-if="specialRuleType == 'RULE'">
					  						<div class="form-group ">
						  						<div class="col-md-11">
						  							<label class="col-sm-2 control-label">应用条件：</label>
						  							<div class="col-sm-9 bd" style="margin-left:15px;">
						  								<div class="apply-rule-wrapper" >
						  									<div class="row">
						  										<label for="specialRuleManufacturer" class="col-sm-2 s-prod-label"><span class="text-red"></span>制造商：</label>
						  										<div class="col-sm-10">
						  											<div class="lemon-area" v-text="speaiclRuleProductLine.mfrName?speaiclRuleProductLine.mfrName:'-'">
										                            </div>
						  										</div>
						  									</div>
						  									<div class="row">
						  										<label for="specialRuleWharehouse" class="col-sm-2 s-prod-label"><span class="text-red"></span>仓库：</label>
						  										<div class="col-sm-9">
							  										<div class="ware-house-box" v-text="speaiclRuleProductLine.sourceName?speaiclRuleProductLine.sourceName:'-'"> 
					                                                 </div>
				                                                 </div>
						  									</div>
					  										<div class="row pdt7">
					  											<label for="specialRuleCategory" class="col-sm-2 s-prod-label"><span class="text-red"></span>商品分类：</label>
					  											<div class="col-sm-9" v-text="speaiclRuleProductLine.catName?speaiclRuleProductLine.catName:'-'">
					  											</div>
						  									</div>
						  								</div>
						  							</div>
						  						</div>
						  					</div>
					  					</div>
					  					<div class="form-group ">
					  						<div class="col-md-11">
					  							<label class="col-sm-2 control-label">规则备注：</label>
											  	<div class="col-sm-9 special-remark" v-text="specialRemark?specialRemark:'-'"></div>
					  						</div>
					  					</div>
					  					<div class="box-footer text-center">
						  					<button type="button" class="btn btn-concle" @click="specialCancelEvent">返回</button>
					  					</div>
			  				   		</div>
			  					 </div>
		  					     <!-- 专属特价设置 -->
		  						
		  					</form>
		  					</div>
		  					<!-- 销售信息 -->
		  					<!-- 供应商别名 -->
					  		<form id="vendorAlias" v-show="tabValue === 'vendorAlias'" class="form-horizontal">
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商别名：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text" v-for="(el,index) in aliasInfo.aliasList" v-if="aliasInfo.aliasList.length>0">
									  			{{el.aliasName}}
									  		</div>
									  		<div class="vendor-text" v-if=" aliasInfo.aliasList.length==0">
									  			--
									  		</div>
									  	</div>
			  						</div>
			  					</div>
		  					</form>
		  					<!-- 供应商别名 -->
		  				</div>
	  					
	  				</div>	
	  				<div class="box box-danger">
	  					<div class="box-header">
	  						<div>修改记录</div>
	  					</div>
	  					<div class="box-body ">
	  						<table class="table">
	  							<thead>
	  								<tr>
	  									<th>修改时间</th>
	  									<th>修改人</th>
	  									<th>描述</th>
	  								</tr>
	  							</thead>
	  							<tbody>
	  								<tr v-for="item in auditList">
	  									<td>{{item.date}}</td>
	  									<td>{{item.userName}}</td>
	  									<td>{{item.actionDesc}}</td>
	  								</tr>
	  							</tbody>
	  						</table>
	  					</div>
	  				</div>
	  			</section>
	  		</div>
	 	</section>

	 </div>
	  	
	<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/distrManufAssem.js"></script>
<script type="text/javascript" src="${ctx}/js/app/vendorDetail.js"></script>
</div>	    
</body>
</html>
