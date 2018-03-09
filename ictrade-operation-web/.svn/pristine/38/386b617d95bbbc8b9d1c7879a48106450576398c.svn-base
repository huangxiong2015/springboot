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
  <title>供应商建档审核</title>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <link rel="stylesheet" href="${ctx}/css/app/vendor.css"/>
  <style>
  	.apply-status{
  		margin-bottom:10px;
  	}
  	.vendor-name-label{
  		height:32px;
  		line-height:32px;
  	}
  	.reason-wrap{
		display:none;
	}
	.reason-box{
		position:relative;
	}
	.reason-box textarea{
		width:350px;
		height:120px;
	}
	.reason-box .num-wrap{
		position:absolute;
		bottom:10px;
		right:20px;
	}
	.ml5{margin-left:5px;}
	.audit-log .vendor-text{padding-top:2px;}
	.audit-log p{margin-bottom:5px;}
  </style>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

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
	  		<a v-if="isSalesInfoNotNull" :class="{header_tab:true,active: tabValue === 'sales'}" @click="tabValue = 'sales'">销售信息</a>
	  	</div>
  		<section class="content" style="color: rgb(102, 102, 102);">
	  		<div class="row">
	  			<section class="col-lg-12">
	  				<!-- 审核人信息 -->
	  				<input type="hidden" id="userName" value="${userName }">
					<input type="hidden" id="userId" value="${userId }">
	  				<!-- 审核人信息 -->
	  				<div class="box box-danger ">
	  					<div class="box-header">
	  						<div class="col-md-9 vendor-name-label">供应商名称：<a v-bind:href="thisUrl+'/vendor/detail.htm?partyId='+auditInfo.applyOrgId" target="_blank">{{baseData.groupNameFull}}</a></div>
	  						<div class="col-md-3 text-right" v-show="auditInfo.status == 'WAIT_APPROVE'" v-if="isNotMyVendor">
					  			<button class="btn-danger btn" @click="examineEvent('yes')">通过</button>
					  			<button class="btn-danger btn" @click="examineEvent('no')">不通过</button>
					  		</div>
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
									  			{{infoMap.VENDOR_INFO_WEBSITE?infoMap.VENDOR_INFO_WEBSITE:"--"}}
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
									  			{{baseData.generalHeadquarters?baseData.generalHeadquarters:"--"}}
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
									  			{{infoMap.VENDOR_INFO_LEGALPERSON?infoMap.VENDOR_INFO_LEGALPERSON:"--"}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">注册资金：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{infoMap.VENDOR_INFO_REGPRICE?infoMap.VENDOR_INFO_REGPRICE:"--"}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">注册地址：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{infoMap.VENDOR_INFO_REGRADDRESS?infoMap.VENDOR_INFO_REGRADDRESS:"--"}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">员工人数：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{infoMap.VENDOR_INFO_EMPLOYEENUM?infoMap.VENDOR_INFO_EMPLOYEENUM:"--"}}
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
									  			{{baseData.principalName?baseData.principalName:"--"}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">询价员：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.enquiryName?baseData.enquiryName:"--"}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">报价员：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{baseData.offerName?baseData.offerName:"--"}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
		  					</form>
		  					<!-- 基本信息表单 -->
		  					<!-- 代理产品线表单 -->
		  					<form name="createVendorProduct" id="createVendorProduct" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'product' && prodctType === 'PROXY'">
		  						<div class="product-label">
		  							<span class="product-tab product-active" @click="productLineChange('PROXY')">代理产品线</span>
		  							<span class="product-tab" @click="productLineChange('NOT_PROXY')">不代理择产品线</span>
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
		  					<!-- 不代理产品线表单 -->
		  					<form name="notProxyVendorProduct" id="notProxyVendorProduct" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'product' && prodctType === 'NOT_PROXY'">
		  						<div class="product-label">
		  							<span class="product-tab" @click="productLineChange('PROXY')">代理产品线</span>
		  							<span class="product-tab product-active" @click="productLineChange('NOT_PROXY')">不代理择产品线</span>
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
		  										<tr v-for="(item, $index) in productLinesNot">
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
		  					<!-- 不代理产品线表单 -->
		  					
		  					<!-- 信用表单 -->
		  					<form name="createVendorCredit" id="createVendorCredit" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'credit'">
		  						<!-- 账期信息 -->
		  						<div class="product-label">账期信息</div>
		  						<div class="form-group ">
		  							<label for="title" class="col-sm-2 control-label">信用额度：</label>
								  	<div class="col-sm-9">
								  		<div class="vendor-text">
								  			{{creditInfo.creditQuota}} {{creditInfo.currency=='CNY'?'RMB':'USD'}}
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
									  			<span v-if="infoMap.VENDOR_CREDIT_PURCHASEDEAL === 'ALREADY_SIGN'">已签</span>
									  			<span v-if="infoMap.VENDOR_CREDIT_PURCHASEDEAL === 'NOT_SIGN'">未签</span>
									  			<span v-if="infoMap.VENDOR_CREDIT_PURCHASEDEAL === 'ALREADY_SPE_SIGN'">已特批</span>
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">协议有效期：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">
									  			{{infoMap.VENDOR_CREDIT_PURCHASEDEALDATE?infoMap.VENDOR_CREDIT_PURCHASEDEALDATE:"--"}}
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">保密协议：</label>
									  	<div class="col-sm-6">
									  		<div class="vendor-text">
									  			<span v-if="infoMap.VENDOR_CREDIT_SECRECYPROTOCOL === 'ALREADY_SIGN'">已签</span>
									  			<span v-if="infoMap.VENDOR_CREDIT_SECRECYPROTOCOL === 'NOT_SIGN'">未签</span>
									  			<span v-if="infoMap.VENDOR_CREDIT_SECRECYPROTOCOL === 'ALREADY_SPE_SIGN'">已特批</span>
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">协议有效期：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">
									  			{{infoMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE?infoMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE:"--"}}
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">附件：</label>
									  	<div class="col-sm-8" v-if="creditAttachListLength>0">
									  		<div class="vendor-text" v-for="(item,index) in creditInfo.creditAttachmentList">
									  			<span><a v-bind="{'href':downLoadUrl[index]}">{{item.attachmentName}}</a></span>
									  		</div>
									  	</div>
									  	<div class="col-sm-8" v-if="creditAttachListLength ==0">
									  		<div class="vendor-text" >
									  			<span>--</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<!-- 协议 -->
		  					</form>
		  					<!-- 信用表单 -->
		  					<!-- 销售信息 -->
		  					<div v-if="isSalesInfoNotNull">
			  					<form name="createVendorSales" id="createVendorSales" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'sales'" >
			  						
			  						<div class="product-label">销售基本信息</div>
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
				  							<label for="title" class="col-sm-2 control-label">Mov策略：</label>
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
										  			<span class="mb" v-for="(item, $index) in salesInfo.facilityList">{{item.facilityName}}</span>
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
				  							<label for="title" class="col-sm-2 control-label">最小订单金额：</label>
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
										  			{{infoMap.VENDOR_SALE_INFOVO_FOCUSFIELDS}}
										  		</div>
										  	</div>
				  						</div>
				  					</div>
				  					
				  					<div class="form-group ">
				  						<div class="col-md-11">
				  							<label for="title" class="col-sm-2 control-label">优势产品类别：</label>
										  	<div class="col-sm-9">
										  		<div class="vendor-text">
										  			{{infoMap.VENDOR_SALE_INFOVO_PRODUCTCATEGORYS}}
										  		</div>
										  	</div>
				  						</div>
				  					</div>
				  					<div class="form-group ">
				  						<div class="col-md-11">
				  							<label for="title" class="col-sm-2 control-label">主要客户：</label>
										  	<div class="col-sm-9">
										  		<div class="vendor-text">
										  			{{infoMap.VENDOR_SALE_INFOVO_MAJORCLIENTS}}
										  		</div>
										  	</div>
				  						</div>
				  					</div>
				  						
				  					<div class="product-label">联系人信息</div>
				  					<!-- <div class="bank-info-create">
				  						<button type="button" @click="addContact('add')" class="btn btn-danger c-btn">新增联系人</button>
				  					</div> -->
				  					<ul class="bank-info-list">
				  						<li v-for="(item, $index) in salesInfo.contactPersonInfoList">
				  							<div class="bank-info">
				  								<div class="back-info-h">{{item.lastNameLocal}}</div>
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
				  								<div class="back-info-item">
				  									联系人地址: <span :title="item.address">{{item.address}}</span>
				  								</div>
				  								<div class="back-info-item" v-if="item.partyProductLineList && item.partyProductLineList.length > 0 ">
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
			  					</form>
		  					</div>
		  					<!-- 销售信息 -->
		  				</div>
	  					
	  				</div>	
	  			</section>
	  		</div>
	 	</section>
		
		
		<!-- 审核信息 -->
		<section class="content" style="color: rgb(102, 102, 102);">
	  		<div class="row">
	  			<section class="col-lg-12">
	  				<div class="box box-danger ">
	  					<div class="box-header">
	  						<div>审核信息</div>
	  					</div>
					  	<div class="box-body ">
					  		<!-- 基本信息表单 -->
					  		<form name="auditInfo" id="auditInfo" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" >
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">申请类型：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span v-if="auditInfo.processId == 'ORG_SUPPLIER_ARCHIVES_REVIEW'">建档申请</span>
									  			<span v-if="auditInfo.processId == 'ORG_SUPPLIER_INFO_CHANGE_REVIEW'">基本信息变更</span>
				                            	<span v-if="auditInfo.processId == 'ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW'">产品线变更</span>
				                            	<span v-if="auditInfo.processId == 'ORG_SUPPLIER_INVALID_REVIEW'">失效申请</span>
				                            	<span v-if="auditInfo.processId == 'ORG_SUPPLIER_ENABLED_REVIEW'">启用申请</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">申请人：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{applyContentJsonObj.applyMail?applyContentJsonObj.applyMail:'--'}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">申请时间：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text" >
									  			<span class="vendor-text" v-html="auditInfo.createdDate?auditInfo.createdDate:'--'">
									  			</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<!-- <div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">申请说明：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text" v-html="applyContentJsonObj.describe?applyContentJsonObj.describe:'--'">
									  		</div>
									  	</div>
			  						</div>
			  					</div> -->
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">审核状态：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span v-if="auditInfo.status == 'APPROVED'">通过</span>
					                            <span v-if="auditInfo.status == 'REJECT'">不通过</span>
					                            <span v-if="auditInfo.status == 'WAIT_APPROVE'">待审核</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">审核记录：</label>
									  	<div class="col-sm-9 audit-log">
									  		<div class="vendor-text" v-if="approveLogListLength>0">
								  				<p v-for="(item,$index) in approveLogList">
									  				<span>{{item.date}}</span>
									  				<span class="ml5" >{{item.userName}}</span>
									  				<span class="ml5" v-if="item.actionDesc">{{item.actionDesc}}</span>
								  				</p>
									  		</div>
									  		<div class="vendor-text" v-if="approveLogListLength==0">
									  			<p>--</p>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<!-- 不通过原因 -->
			  					<div class="reason-wrap">
			                      	<p>请输入审核不通过的原因：</p>
			                      	<div class='reason-box'>
			                      		<textarea id='remark' onkeyup="setLetterNum(this)" maxlength="100"></textarea>
			                      		<span class="num-wrap"><span class="letter-num">0</span>/100</span>
			                      	</div>
			                     </div>
			  					<!-- 不通过原因 -->
		  					</form>
		  					<!-- 基本信息表单 -->
		  				</div>
	  				</div>	
	  			</section>
	  		</div>
	 	</section>
	 	<!-- 审核信息 -->
		
	 </div>
	  	
	<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/app/archivingAudit.js"></script>
</div>	    
</body>
</html>
