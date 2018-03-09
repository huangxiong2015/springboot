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
  <title>创建供应商</title>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <link rel="stylesheet" href="${ctx}/css/app/vendor.css"/>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<!---index nav  end   ---->
	<div class="content-wrapper" id="createVendor">
		<section class="content-header">
			<h1>
		 		创建供应商
	  		</h1> 
	  	</section>
	  	<section section class="content-header vendor-header">
	  		<a :class="{header_tab:true,active: tabValue === 'baseData'}" @click="tabValue = 'baseData'">基本信息</a>
	  		<a :class="{header_tab:true,active: tabValue === 'product'}" @click="saveBaseData()" v-show="baseDataGone || tabValue !== 'baseData'">产品线</a>
	  		<a :class="{header_tab:true,active: tabValue === 'credit'}" @click="saveProductData()" v-show="productGone || (tabValue !== 'baseData' && tabValue !== 'product')">信用</a>
	  		<!-- <a :class="{active: tabValue === 'sales'}" @click="saveCreditData()" v-show="creditGone || (tabValue !== 'baseData' && tabValue !== 'product' && tabValue !== 'credit')">销售信息</a> -->
	  	</section>
  		<section class="content" style="color: rgb(102, 102, 102);">
	  		<div class="row">
	  			<section class="col-lg-12">
	  				<div class="box box-danger ">
	  					<div class="box-header">
	  						<div>供应商名称：{{baseData.groupNameFull}}</div>
	  					</div>
					  	<div class="box-body ">
					  		<!-- 基本信息表单 -->
					  		<form name="createVendorBase" autocomplete="off" id="createVendorBase" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'baseData'">
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商全称：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<input class="form-control vendor-input" type="text" name="groupNameFull" v-model="baseData.groupNameFull" maxlength="100"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商简称：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<input class="form-control vendor-input" type="text" name="groupName" v-model="baseData.groupName" maxlength="60"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商编码：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<input class="form-control vendor-input" type="text" name="partyCode" v-model="baseData.partyCode" maxlength="12"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">所属分类：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<select class="form-control vendor-input" name="category" v-model="baseData.category">
									  			<option v-for="item in categoryList" :value="item.categoryId">{{item.categoryName}}</option>
									  		</select>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">核心供应商：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="radio" value="Y" name="vendorCore" v-model="baseData.isCore"/> 是</label>
									  		<label class="form-label"><input type="radio" value="N" name="vendorCore" v-model="baseData.isCore"/> 否</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商logo：</label>
									  	<div class="col-sm-9">
									  		<span class="upload-logo">
									  			<img alt="" :src="vendorLogo!==''?vendorLogo: '../images/default_logo.png'" width="100" height="100"/>
									  			<input type="file" id="upload-logo"/>
									  			<i>上传logo</i>
									  		</span>
									  		<label class="upload-label">仅支持png、bmp、jpeg、jpg格式，文件小于10M</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商官网：</label>
									  	<div class="col-sm-9">
									  		<input class="form-control vendor-input" type="text" name="website" v-model="vendorMap.VENDOR_INFO_WEBSITE" maxlength="50"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">所属地区：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<select class="form-control vendor-input" name="region" v-model="baseData.region">
									  			<option v-for="item in regionList" :value="item.categoryId">{{item.categoryName}}</option>
									  		</select>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商总公司：</label>
									  	<div class="col-sm-9">
									  		<input class="form-control vendor-input" type="text" name="generalHeadquarters" v-model="baseData.generalHeadquarters" maxlength="50"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">公司类型：<span class="text-red">*</span></label>
									  	<div class="col-sm-9 pdt7">
									  		<label class="form-label" v-for="item in companyTypeList"><input type="radio" :value="item.categoryId" name="genre" v-model="baseData.genre"/><span class="ml3">{{item.categoryName}}</span></label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否上市：<span class="text-red">*</span></label>
									  	<div class="col-sm-9 pdt7">
									  		<label class="form-label"><input type="radio" value="Y" name="listed" v-model="baseData.listed"/> 是</label>
									  		<label class="form-label"><input type="radio" value="N" name="listed" v-model="baseData.listed"/> 否</label>
									  		<input type="text" class="form-control vendor-input-s inline" placeholder="请输入股票代码" name="stockCode" required maxlength="30" v-model="stockCode" v-if="baseData.listed === 'Y'"/>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">公司法人：</label>
									  	<div class="col-sm-9">
									  		<input class="form-control vendor-input" type="text" name="legalPerson" v-model="vendorMap.VENDOR_INFO_LEGALPERSON" maxlength="30"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">注册资金：</label>
									  	<div class="col-sm-9">
									  		<input class="form-control vendor-input" type="text" maxlength="30" name="regPrice" v-model="vendorMap.VENDOR_INFO_REGPRICE"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">注册地址：</label>
									  	<div class="col-sm-9">
									  		<input class="form-control vendor-input" type="text" name="regAddress" maxlength="100" v-model="vendorMap.VENDOR_INFO_REGRADDRESS"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">员工人数：</label>
									  	<div class="col-sm-9">
									  		<input class="form-control vendor-input" type="text" name="employeeNum" maxlength="100" v-model="vendorMap.VENDOR_INFO_EMPLOYEENUM"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">分管部门：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<select class="form-control vendor-input" name="department" v-model="baseData.deptId">
									  			<option v-for="item in deptList" :value="item.id">{{item.name}}</option>
									  		</select>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">负责人：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<select class="form-control vendor-input" name="principalId" v-model="baseData.principalId">
									  			<option v-for="item in userList" :value="item.partyId">{{item.partyAccount}}</option>
									  		</select>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">询价员：<span class="text-red">*</span></label>
									  	<div class="col-sm-9 pdt7">
									  		<label class="form-label"><input type='checkbox' class='input-checkbox' v-model='enquiryNameChecked' v-on:click='enquiryNameCheckedAll'><span class="ml3">全选</span></label>
									  		<label class="form-label" v-for='checkb in offerList'>
										         <input type='checkbox' name='checkboxinput' class='input-checkbox' v-model='enquiryNameCheckboxModel' :value='checkb.partyId'>
										         <span class="ml3">{{checkb.partyAccount}}</span>
										    </label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">报价员：<span class="text-red">*</span></label>
									  	<div class="col-sm-9 pdt7">
									  		<label class="form-label"><input type='checkbox' class='input-checkbox' v-model='offerNameChecked' v-on:click='offerNameCheckedAll'><span class="ml3">全选</span></label>
									  		<label class="form-label" v-for='checkb in offerList'>
										         <input type='checkbox' name='checkboxinput' class='input-checkbox' v-model='offerNameCheckboxModel' :value='checkb.partyId'>
										         <span class="ml3">{{checkb.partyAccount}}</span>
										    </label>
									  	</div>
			  						</div>
			  					</div>
		  					</form>
		  					<!-- 基本信息表单 -->
		  					<!-- 产品线表单 -->
		  					<form name="createVendorProduct" autocomplete="off" id="createVendorProduct" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'product' && prodctType === 'PROXY'">
		  						<div class="product-label">
		  							<span class="product-tab product-active" @click="productLineChange('PROXY')">代理产品线</span>
		  							<span class="product-tab" @click="productLineChange('NOT_PROXY')">不代理择产品线</span>
	  							</div>
		  						
		  						<div class="product-table" v-if="partyProductLineList.length > 0">
		  							<table class="table table-head">
		  								<thead>
		  									<tr>
		  										<th>品牌</th>
		  										<th>大类</th>
		  										<th>小类</th>
		  										<th>次小类</th>
		  										<th width="120">操作</th>
		  									</tr>
		  								</thead>
		  							</table>
		  							<div class="table-body">
		  								<table class="table">
		  									<tbody>
		  										<tr v-for="(item, $index) in partyProductLineList">
		  											<td :class="{'red-tip': (item.brandSign && item.brandSign === 'Y')}">{{item.brandName}}</td>
		  											<td :class="{'red-tip': (item.brandSign && item.category1Sign === 'Y')}">{{item.category1Name?item.category1Name: '不限'}}</td>
		  											<td :class="{'red-tip': (item.brandSign && item.category2Sign === 'Y')}"><span v-if="item.category1Name">{{item.category2Name?item.category2Name: '不限'}}</span><span v-else>*</span></td>
		  											<td :class="{'red-tip': (item.brandSign && item.category3Sign === 'Y')}"><span v-if="item.category2Name">{{item.category3Name?item.category3Name: '不限'}}</span><span v-else>*</span></td>
		  											<td width="120"><a href="javascript:void(0);" @click="editProductItem($index)">编辑</a><a href="javascript:void(0);" @click="deleteProductItem($index)">删除</a></td>
		  										</tr>
		  									</tbody>
		  								</table>
		  								<div class="product-table-tip">红色字体，系统中无匹配字段，请检查后重新上传</div>
		  							</div>
		  						</div>
		  						
		  						<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-1 control-label">上传</label>
									  	<div class="col-sm-10">
									  		<div class="upload-file" @click="uploadProductLine()">
									  			<span>选择文件</span>
									  		</div>
									  		<a href="${ctx}/resources/download/授权代理线.xlsx" class="download-temp">Excel模板下载</a>
									  		
									  		<span>*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现所选类别</span>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-1 control-label">或</label>
									  	<div class="col-sm-10">
									  		<div class="vendor-text blue" ><a href="javascript:void(0);" @click="showProductSelect()">在线选择产品线</a></div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="product-select" v-if="productSelectFlag">
			  						<div v-if="!editProduct">
			  						<letter-select
								            :keyname="keyname"
								            :id="id"
								            :api="api"
								            :name="name"
								            :option-id="optionId"
								            :option-name="optionName"
								            @get-selecteds="getBrandSelected"
								            :multiple="multiple"
								            :placeholder="placeholder"
								            :selected="selected"
								            :is-fuzzy-search="isFuzzySearch"
								            :reload-api="reloadApi"
								            ref="letter"    
								    ></letter-select>
								    </div>
								    <div class="product-cate-tit" v-if="multBrand">
								    	选择分类：
								    </div>
								    
								    <div class="product-cate" v-if="multBrand">
								    	<select-province
											@onchange="change"
								            :input-class="inputClass"
								            :style-object="styleObject"
								            :api="category"
								            :option-id="cateId"
								            :option-name="cateName"
								            :set-province="setProvince"
								            :set-city="setCity"
								            :set-district="setDistrict"
								            :parent-id="parentId"
								            :query-param="queryParam"	
								            :placeholder="catePlaceholder"	
								            ref="refs"    
									    >
									    </select-province>
								    </div>
								    
								    <div class="product-cate-tit">
								    	已选择：
								    	<div class="data-selected" v-for="(item, $index) in productItems">
									    	<span>{{item.brandName}}</span>
									    	<span v-if="item.category1Name">- {{item.category1Name?item.category1Name: '所有大类'}}</span>
									    	<span v-if="item.category1Name">- {{item.category2Name?item.category2Name: '所有小类'}}</span>
									    	<span v-if="item.category2Name">- {{item.category3Name?item.category3Name: '所有次小类'}}</span>
								    		<i v-if="!editProduct" @click="removeSelected($index)">x</i>
								    	</div>
								    </div>
  									<div class="bank-info-create center">
				  						<button type="button" @click="saveProduct()" class="btn btn-danger c-btn">确定</button>
				  						<button type="button" @click="productSelectFlag = false"  class="btn btn-cancel c-btn">取消</button>
				  					</div>
				  					<div style="display:none;">{{productFlagNum}}</div>
			  					</div>
		  					</form>
		  					<!-- 代理产品线表单 -->
		  					<!-- 不代理产品线表单 -->
		  					<form name="notProxyVendorProduct" autocomplete="off" id="notProxyVendorProduct" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'product' && prodctType === 'NOT_PROXY'">
		  						<div class="product-label">
		  							<span class="product-tab" @click="productLineChange('PROXY')">代理产品线</span>
		  							<span class="product-tab product-active" @click="productLineChange('NOT_PROXY')">不代理择产品线</span>
	  							</div>
		  						
		  						<div class="product-table" v-if="productNotProxyLines.length > 0">
		  							<table class="table table-head">
		  								<thead>
		  									<tr>
		  										<th>品牌</th>
		  										<th>大类</th>
		  										<th>小类</th>
		  										<th>次小类</th>
		  										<th width="120">操作</th>
		  									</tr>
		  								</thead>
		  							</table>
		  							<div class="table-body">
		  								<table class="table">
		  									<tbody>
		  										<tr v-for="(item, $index) in productNotProxyLines">
		  											<td :class="{'red-tip': (item.brandSign && item.brandSign === 'Y')}">{{item.brandName}}</td>
		  											<td :class="{'red-tip': (item.brandSign && item.category1Sign === 'Y')}">{{item.category1Name?item.category1Name: '不限'}}</td>
		  											<td :class="{'red-tip': (item.brandSign && item.category2Sign === 'Y')}"><span v-if="item.category1Name">{{item.category2Name?item.category2Name: '不限'}}</span><span v-else>*</span></td>
		  											<td :class="{'red-tip': (item.brandSign && item.category3Sign === 'Y')}"><span v-if="item.category2Name">{{item.category3Name?item.category3Name: '不限'}}</span><span v-else>*</span></td>
		  											<td width="120"><a href="javascript:void(0);" @click="editProductItem($index)">编辑</a><a href="javascript:void(0);" @click="deleteProductItem($index)">删除</a></td>
		  										</tr>
		  									</tbody>
		  								</table>
		  								<div class="product-table-tip">红色字体，系统中无匹配字段，请检查后重新上传</div>
		  							</div>
		  						</div>
		  						
		  						<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-1 control-label">上传</label>
									  	<div class="col-sm-10">
									  		<div class="upload-file" @click="uploadProductLine()">
									  			<span>选择文件</span>
									  		</div>
									  		<a href="${ctx}/resources/download/授权代理线.xlsx" class="download-temp">Excel模板下载</a>
									  		
									  		<span>*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现所选类别</span>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-1 control-label">或</label>
									  	<div class="col-sm-10">
									  		<div class="vendor-text blue" ><a href="javascript:void(0);" @click="showProductSelect()">在线选择产品线</a></div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="product-select" v-if="productSelectFlag">
			  						<div v-if="!editProduct">
			  						<letter-select
								            :keyname="keyname"
								            :id="id"
								            :api="api"
								            :name="name"
								            :option-id="optionId"
								            :option-name="optionName"
								            @get-selecteds="getBrandSelected"
								            :multiple="multiple"
								            :placeholder="placeholder"
								            :selected="selected"
								            :is-fuzzy-search="isFuzzySearch"
								            :reload-api="reloadApi"
								            ref="letterNot"    
								    ></letter-select>
								    </div>
								    <div class="product-cate-tit" v-if="multBrand">
								    	选择分类：
								    </div>
								    
								    <div class="product-cate" v-if="multBrand">
								    	<select-province
											@onchange="change"
								            :input-class="inputClass"
								            :style-object="styleObject"
								            :api="category"
								            :option-id="cateId"
								            :option-name="cateName"
								            :set-province="setProvince"
								            :set-city="setCity"
								            :set-district="setDistrict"
								            :parent-id="parentId"
								            :query-param="queryParam"	
								            :placeholder="catePlaceholder"	
								            ref="refsNot"    
									    >
									    </select-province>
								    </div>
								    
								    <div class="product-cate-tit">
								    	已选择：
								    	<div class="data-selected" v-for="(item, $index) in productNotProxyItems">
									    	<span>{{item.brandName}}</span>
									    	<span v-if="item.category1Name">- {{item.category1Name?item.category1Name: '所有大类'}}</span>
									    	<span v-if="item.category1Name">- {{item.category2Name?item.category2Name: '所有小类'}}</span>
									    	<span v-if="item.category2Name">- {{item.category3Name?item.category3Name: '所有次小类'}}</span>
								    		<i v-if="!editProduct" @click="removeSelected($index)">x</i>
								    	</div>
								    </div>
  									<div class="bank-info-create center">
				  						<button type="button" @click="saveProduct()" class="btn btn-danger c-btn">确定</button>
				  						<button type="button" @click="productSelectFlag = false"  class="btn btn-cancel c-btn">取消</button>
				  					</div>
				  					<div style="display:none;">{{productFlagNum}}</div>
			  					</div>
		  					</form>
		  					<!-- 不代理产品线表单 -->

		  					<!-- 信用表单 -->
		  					<form name="createVendorCredit" autocomplete="off" id="createVendorCredit" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'credit'">
		  						<!-- 账期信息 -->
		  						<div class="product-label">账期信息</div>
		  						<div class="form-group ">
		  							<label for="title" class="col-sm-2 control-label">信用额度：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input-p inline" type="text" name="creditQuota" v-model="vendorCreditVo.creditQuota" maxlength="10"/>
								  		<select class="form-control vendor-hlaf-s inline" name="currency" v-model="vendorCreditVo.currency">
								  			<option value="CNY">RMB</option>
								  			<option value="USD">USD</option>
								  		</select>
								  	</div>
								</div>
							  	<div class="form-group ">
		  							<label for="title" class="col-sm-2 control-label">授信期限：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input-d inline" type="text" name="creditDeadline" v-model="vendorCreditVo.creditDeadline" maxlength="50"/>
								  	</div>
							  	</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">对账日期：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input-l inline" type="text" name="checkDate" v-model="vendorCreditVo.checkDate" maxlength="2"/>天
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">付款日期：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input-l inline" type="text" name="payDate" v-model="vendorCreditVo.payDate" maxlength="2"/>日
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">付款条件：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input" type="text" name="paymentTerms" v-model="vendorCreditVo.paymentTerms" maxlength="20"/>
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">结算方式：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input" type="text" name="settlementMethod" v-model="vendorCreditVo.settlementMethod" maxlength="10"/>
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
			  					<ul class="bank-info-list" v-if="vendorCreditVo.partyBankAccount.length > 0">
			  						<li v-for="(bank, $index) in vendorCreditVo.partyBankAccount">
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
			  								<div class="bank-info-btn"><a href="javascript:void(0);" @click="addBankInfo('edit', $index)">编辑</a><a href="javascript:void(0);" @click="deleteBankInfo($index)">删除</a></div>
			  							</div>
			  						</li>
			  					</ul>
			  					<div class="bank-info-create">
			  						<button type="button" @click="addBankInfo('add')" class="btn btn-danger c-btn">新增银行账号</button>
			  					</div>
			  					<!-- 银行资料 -->
			  					<!-- 协议 -->
			  					<div class="product-label">协议</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">采购协议：<span class="text-red">*</span></label>
									  	<div class="col-sm-8">
									  		<select class="form-control vendor-hlaf inline" name="purchaseDeal" v-model="vendorMap.VENDOR_CREDIT_PURCHASEDEAL">
									  			<option value="ALREADY_SIGN">已签</option>
									  			<option value="NOT_SIGN">未签</option>
									  			<option value="ALREADY_SPE_SIGN">已特批</option>
									  		</select>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">协议有效期：</label>
									  	<div class="col-sm-8" id="dealTime">
									  		 <div class="input-daterange">
									  			<input class="form-control vendor-hlaf inline" type="text" name="purchaseDealDate" v-model="vendorMap.VENDOR_CREDIT_PURCHASEDEALDATE"/>
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">保密协议：<span class="text-red">*</span></label>
									  	<div class="col-sm-8">
									  		<select class="form-control vendor-hlaf inline" name="secrecyProtocol" v-model="vendorMap.VENDOR_CREDIT_SECRECYPROTOCOL">
									  			<option value="ALREADY_SIGN">已签</option>
									  			<option value="NOT_SIGN">未签</option>
									  			<option value="ALREADY_SPE_SIGN">已特批</option>
									  		</select>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">协议有效期：</label>
									  	<div class="col-sm-8" id="secrecyTime">
									  		 <div class="input-daterange">
									  			<input class="form-control vendor-hlaf inline" type="text" name="secrecyProtocolDate" v-model="vendorMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE"/>
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">附件：</label>
									  	<div class="col-sm-8">
									  		 <span class="file-btn">添加文件 <input type="file" id="upload-deal"/></span> <span>支持格式：Excel、PDF、压缩文件</span>
									  		 <div>
									  		 	<div class="lemon-tag inline tag" v-for="(item, $index) in vendorCreditVo.creditAttachmentList"><span class="lemon-tag-text">{{item.attachmentName}}</span> <i class="fa fa-close" @click="removeAttachment($index)"></i></div>
									  		 </div>
									  	</div>
			  						</div>
			  					</div>
			  					<!-- 协议 -->
		  					</form>
		  					<!-- 信用表单 -->
		  					<!-- 销售信息 -->
		  					<!-- <form name="createVendorSales" autocomplete="off" id="createVendorSales" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'sales'">
		  						
		  						<div class="product-label">销售基本信息</div>
		  						<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否审核订单：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="radio" value="Y" name="orderVerify" v-model="vendorSaleInfoVo.orderVerify"/> 是</label>
									  		<label class="form-label"><input type="radio" value="N" name="orderVerify" v-model="vendorSaleInfoVo.orderVerify"/> 否</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否显示名称：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="radio" value="Y" name="isShowName" v-model="vendorSaleInfoVo.isShowName"/> 是</label>
									  		<label class="form-label"><input type="radio" value="N" name="isShowName" v-model="vendorSaleInfoVo.isShowName"/> 否</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">Mov策略：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="checkbox" value="Y" name="vendorMovStatus"/> 供应商MOV</label>
									  		<label class="form-label"><input type="checkbox" value="Y" name="skuMovStatus"/> 商品MOV</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商仓库：</label>
									  	<div class="col-sm-9">
									  		<span class="mb" v-for="(item, $index) in vendorSaleInfoVo.facilityList">
									  			<input class="form-control vendor-input" type="text" maxlength="20" name="facility" v-model="vendorSaleInfoVo.facilityList[$index].facilityName"/>
									  		</span>
									  		<a href="javascript:void(0)" @click="addFacility()">新增仓库</a>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">支持币种：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="checkbox" value="RMB" name="currencyRmb"/> RMB</label>
									  		<label class="form-label"><input type="checkbox" value="USD" name="currencyUsd"/> USD</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">最小订单金额：</label>
									  	<div class="col-sm-9">
									  		<span v-if="showRmb" class="mb"><input type="text" class="form-control vendor-input-t inline" name="minOrderPriceCNY" v-model="vendorSaleInfoVo.minOrderPriceCNY" maxlength="10"/> <span>RMB</span></span>
									  		<span v-if="showUsd" class="mb"><input type="text" class="form-control vendor-input-t inline" name="minOrderPriceUSD" v-model="vendorSaleInfoVo.minOrderPriceUSD" maxlength="10"/> <span>USD</span></span>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">装运条款：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-input-watch">
									  			<textarea class="form-control vendor-input vendor-textarea" name="description" v-model="vendorSaleInfoVo.description" maxlength="20"></textarea>
									  			<span>{{vendorSaleInfoVo.description.length}}/100</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">应付余额：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">{{vendorSaleInfoVo.balanceDue?vendorSaleInfoVo.balanceDue:'--'}}</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">剩余余额：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">{{vendorSaleInfoVo.balanceDue?vendorSaleInfoVo.balanceDue:'--'}}</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">最后交易日期：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">{{vendorSaleInfoVo.lastTransactionDate?vendorSaleInfoVo.lastTransactionDate:'--'}}</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">最后交易金额：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">{{vendorSaleInfoVo.lastTransactionPrice?vendorSaleInfoVo.lastTransactionPrice:'--'}}</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">最后付款日期：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">{{vendorSaleInfoVo.lastPayDate?vendorSaleInfoVo.lastPayDate:'--'}}</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6">
			  							<label for="title" class="col-sm-3 control-label">最后付款金额：</label>
									  	<div class="col-sm-8">
									  		<div class="vendor-text">{{vendorSaleInfoVo.lastPaymPrice?vendorSaleInfoVo.lastPaymPrice:'--'}}</div>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<enter-input :keyname="focusFieldsname" :id="focusFieldsId" :name="focusFieldsName" :input-class="focusFieldsClass" :placeholder="focusFieldsPlaceholder"></enter-input>
			  				
			  					<enter-input :keyname="productCategorysname" :id="productCategorysId" :name="productCategorysName" :input-class="productCategorysClass" :placeholder="productCategorysPlaceholder"></enter-input>
			  				
			  					<enter-input :keyname="majorClientsname" :id="majorClientsId" :name="majorClientsName" :input-class="majorClientsClass" :placeholder="majorClientsPlaceholder"></enter-input>
			  					
			  					
			  					<div class="product-label">联系人信息</div>
			  					<div class="bank-info-create">
			  						<button type="button" @click="addContact('add')" class="btn btn-danger c-btn">新增联系人</button>
			  					</div>
			  					<ul class="bank-info-list" v-if="vendorSaleInfoVo.contactPersonInfoList.length > 0">
			  						<li v-for="(item, $index) in vendorSaleInfoVo.contactPersonInfoList">
			  							<div class="bank-info">
			  								<div class="back-info-h">{{item.lastNameLocal}} <div class="bank-info-btn"><a href="javascript:void(0);" @click="addContact('edit', $index)">编辑</a><a href="javascript:void(0);" @click="deleteContactInfo($index)">删除</a></div></div>
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
			  							</div>
			  						</li>
			  					</ul>
		  					</form> -->
		  					<!-- 销售信息 -->
		  				</div>
	  					<div class="box-footer text-center">
	  						<button type="button" @click="saveBaseData();" class="btn btn-danger c-btn" v-show="tabValue === 'baseData'">下一步</button>  
		  					<!-- 产品线 -->
		  					<button type="button" @click="tabValue = 'baseData'" class="btn btn-danger c-btn" v-show="tabValue === 'product'">上一步</button> 
		  					<button type="button" @click="saveProductData();" class="btn btn-danger c-btn" v-show="tabValue === 'product'">下一步</button>  
		  					<!-- 产品线 -->
		  					<!-- 信用 -->
		  					<button type="button" @click="tabValue = 'product'" class="btn btn-danger c-btn" v-show="tabValue === 'credit'">上一步</button> 
		  					<button type="button" @click="saveData();" class="btn btn-danger c-btn" v-show="tabValue === 'credit'">保存</button>  
		  					<!-- 信用  -->
		  					<!-- 销售信息 -->
		  					<!-- <button type="button" @click="tabValue = 'credit'" class="btn btn-danger c-btn" v-show="tabValue === 'sales'">上一步</button>
		  					<button type="button" @click="saveData()" class="btn btn-danger c-btn" v-show="tabValue === 'sales'">保存</button>  -->
		  					<!-- 销售信息  -->
	  					</div>	
	  				</div>	
	  			</section>
	  		</div>
	 	</section>
		 
		 <section id="file-upload" style="display:none;">
			<form autocomplete="off">
				<div class="form-group">
					<label class="form-label">添加产品线：</label>
					<div class="form-control">
						<span class="btn btn-primary">
							上传文件
							<input type="file" id="uploadProductFile"/>
						</span>
						<span v-if="fileName !== ''">{{fileName}}<i class="label-success">上传成功</i></span>
						 <a href="${ctx}/resources/download/授权代理线.xlsx">Excel模板下载</a>	
						<div class="tip">*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现上传商品</div>
					</div>
				</div>
			</form>
		</section>
		
		<div class="bank_layer" style="display: none;">
			<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate" autocomplete="off" id="bankLayer">
				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">币种：<span class="text-red">*</span></label>
					  	<div class="col-sm-9">
					  		<select class="form-control vendor-input" v-model="partyBankAccount.currency">
					  			<option value="CNY">RMB</option>
					  			<option value="USD">USD</option>
					  		</select>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">账户名称：<span class="text-red">*</span></label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="accountName" v-model="partyBankAccount.accountName" maxlength="60"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">开户银行：<span class="text-red">*</span></label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="bankName" v-model="partyBankAccount.bankName" maxlength="50"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">银行账号：<span class="text-red">*</span></label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="bankAccount" v-model="partyBankAccount.bankAccount" maxlength="50"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group" v-if="partyBankAccount.currency === 'CNY'">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">税号：<span class="text-red">*</span></label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="taxNumber" v-model="partyBankAccount.taxNumber" maxlength="50"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">地址：<span class="text-red">*</span></label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="address" v-model="partyBankAccount.address" maxlength="255"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group" v-if="partyBankAccount.currency === 'CNY'">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">电话：<span class="text-red">*</span></label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="contactNumber" v-model="partyBankAccount.contactNumber" maxlength="100"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group" v-if="partyBankAccount.currency === 'USD'">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">SWIFT CODE：<span class="text-red">*</span></label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="swiftCode" v-model="partyBankAccount.swiftCode" maxlength="100"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label"></label>
					  	<div class="col-sm-9">
					  		<label class="form-label"><input type="checkbox" value="Y" name="isDefault" v-model="partyBankAccount.isDefault"/> 设置为默认银行资料</label>
					  	</div>
 					</div>
 				</div>
 				
			</form>
  		</div>
  		
  	<!-- 	<div class="contact_layer" style="display: none;">
			<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate" autocomplete="off" id="contactLayer">
				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">联系人：</label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="lastNameLocal" v-model="contactPersonInfo.lastNameLocal" maxlength="40"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">联系人职位：</label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="occupation" v-model="contactPersonInfo.occupation" maxlength="20"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">联系人职能：</label>
					  	<div class="col-sm-9">
					  		<label class="form-label"><input type="checkbox" value="ENQUIRY" name="enquiry"/> 询价</label>
							<label class="form-label"><input type="checkbox" value="ORDER" name="order"/> 下单</label>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">邮箱：</label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="mail" v-model="contactPersonInfo.mail" maxlength="100"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">电话：</label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="fixedtel" v-model="contactPersonInfo.fixedtel" maxlength="100"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">手机：</label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="tel" v-model="contactPersonInfo.tel" maxlength="100"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label">联系人地址：</label>
					  	<div class="col-sm-9">
					  		<input type="text" class="form-control vendor-input inline" name="address" v-model="contactPersonInfo.address" maxlength="100"/>
					  	</div>
 					</div>
 				</div>
 				<div class="form-group">
 					<div class="col-md-11">
 						<label for="title" class="col-sm-2 control-label"></label>
					  	<div class="col-sm-9">
					  		<label class="form-label"><input type="checkbox" value="Y" name="isDefault" v-model="contactPersonInfo.isDefault"/> 设置为默认联系人</label>
					  	</div>
 					</div>
 				</div>
 				
			</form>
  		</div>
  		 -->
	 	<div class="save-describe describe-layer" style="display: none;">
  			<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate" id="saveDescribeLayer">
				<div class="form-group">
					<div>请填写说明：</div>
					<div class="discribe-textarea">
						<textarea class="form-control vendor-input inline" v-model="saveDescribe" maxlength="100"></textarea>
						<span>{{saveDescribe.length}}/100</span>
					</div>
 				</div>
 			</form>
  		</div>
	 </div>
	  	
	<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-city.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-enter-input.js"></script>
<script type="text/javascript" src="${ctx}/js/common/add.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/app/vendorCreate.js"></script>
</div>	    
</body>
</html>
