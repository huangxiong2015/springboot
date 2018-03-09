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
  <title>编辑供应商</title>
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
		 		编辑供应商
	  		</h1> 
	  	</section>
	  	<section class="content-header vendor-header" v-if="status !== 'applyNo'">
	  		<a :class="{header_tab:true,active: tabValue === 'baseData'}" @click="tabValue = 'baseData'">基本信息</a>
	  		<a :class="{header_tab:true,active: tabValue === 'product'}" @click="tabValue = 'product'">产品线</a>
	  		<a :class="{header_tab:true,active: tabValue === 'credit'}" @click="tabValue = 'credit'">信用</a>
	  		<a :class="{header_tab:true,active: tabValue === 'sales'}" @click="tabValue = 'sales'" v-if="!noSales">销售信息</a>
	  		<!-- <a :class="{header_tab:true,active: tabValue === 'vendorAlias'}" @click="tabValue = 'vendorAlias'">供应商别名</a> -->
	  	</section>
	  	<section class="content-header vendor-header" v-if="status === 'applyNo'">
	  		<a :class="{header_tab:true,active: tabValue === 'baseData'}" @click="tabValue = 'baseData'">基本信息</a>
	  		<a :class="{header_tab:true,active: tabValue === 'product'}" @click="tabValue = 'product'">产品线</a>
	  		<a :class="{header_tab:true,active: tabValue === 'credit'}" @click="tabValue = 'credit'">信用</a>
	  		<a :class="{header_tab:true,active: tabValue === 'sales'}" @click="tabValue = 'sales'" v-if="!noSales">销售信息</a>
	  		<!-- <a :class="{header_tab:true,active: tabValue === 'vendorAlias'}" @click="tabValue = 'vendorAlias'">供应商别名</a> -->
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
									  		<input class="form-control vendor-input" type="text" :disabled="noEdit" name="groupNameFull" v-model="baseData.groupNameFull" maxlength="100"/>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商简称：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<input class="form-control vendor-input" type="text" :disabled="noEdit" name="groupName" v-model="baseData.groupName" maxlength="60"/>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商编码：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<input class="form-control vendor-input" type="text" :disabled="noEdit" name="partyCode" v-model="baseData.partyCode" maxlength="12"/>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">所属分类：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<select class="form-control vendor-input" :disabled="noEdit" name="category" v-model="baseData.category">
										  			<option v-for="item in categoryList" :value="item.categoryId">{{item.categoryName}}</option>
										  		</select>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">核心供应商：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<label class="form-label"><input type="radio" :disabled="noEdit" value="Y" name="vendorCore" v-model="baseData.isCore"/> 是</label>
									  			<label class="form-label"><input type="radio" :disabled="noEdit" value="N" name="vendorCore" v-model="baseData.isCore"/> 否</label>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商logo：</label>
									  	<div class="col-sm-9">
									  		<span class="upload-logo">
									  			<span class="upload-logo">
										  			<img alt="" :src="vendorLogo!==''?vendorLogo: '../images/default_logo.png'" width="100" height="100"/>
										  			<input type="file" id="upload-logo" :disabled="noEdit"/>
										  			<i v-show="!noEdit">上传logo</i>
										  		</span>
										  		<label class="upload-label">仅支持png、bmp、jpeg、jpg格式，文件小于10M</label>
									  		</span>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商官网：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<input class="form-control vendor-input" type="text" name="website" :disabled="noEdit" v-model="baseData.vendorInfoAttributeMap.VENDOR_INFO_WEBSITE" maxlength="50"/>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">所属地区：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<select class="form-control vendor-input" name="region" :disabled="noEdit" v-model="baseData.region">
									  			<option v-for="item in regionList" :value="item.categoryId">{{item.categoryName}}</option>
									  		</select>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商总公司：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<input class="form-control vendor-input" type="text" name="generalHeadquarters" :disabled="noEdit" v-model="baseData.generalHeadquarters" maxlength="50"/>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">公司类型：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<label class="form-label" v-for="item in companyTypeList">
									  				<input type="radio" :value="item.categoryId" name="genre" :disabled="noEdit" v-model="baseData.genre"/>{{item.categoryName}}
									  			</label>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否上市：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<label class="form-label"><input type="radio" value="Y" name="listed" :disabled="noEdit" v-model="baseData.listed"/> 是</label>
									  			<label class="form-label"><input type="radio" value="N" name="listed" :disabled="noEdit" v-model="baseData.listed"/> 否</label>
									  			<input type="text" class="form-control vendor-input-s inline" placeholder="请输入股票代码" name="stockCode" :disabled="noEdit" required maxlength="30" v-model="baseData.stockCode" v-if="baseData.listed === 'Y'"/>
									  		</div>
									  		
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">公司法人：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<input class="form-control vendor-input" type="text" name="legalPerson" maxlength="30" :disabled="noEdit" v-model="baseData.vendorInfoAttributeMap.VENDOR_INFO_LEGALPERSON"/>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">注册资金：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<input class="form-control vendor-input" type="text" name="regPrice" maxlength="30" :disabled="noEdit" v-model="baseData.vendorInfoAttributeMap.VENDOR_INFO_REGPRICE"/>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">注册地址：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<input class="form-control vendor-input" type="text" name="regAddress" maxlength="100" :disabled="noEdit" v-model="baseData.vendorInfoAttributeMap.VENDOR_INFO_REGRADDRESS"/>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">员工人数：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<input class="form-control vendor-input" type="text" name="employeeNum" maxlength="100" :disabled="noEdit" v-model="baseData.vendorInfoAttributeMap.VENDOR_INFO_EMPLOYEENUM"/>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">分管部门：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<select class="form-control vendor-input" name="deptId" :disabled="noEdit" v-model="departmentId">
										  			<option v-for="item in deptList" :value="item.id">{{item.name}}</option>
										  		</select>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">负责人：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<select class="form-control vendor-input" name="principalId" :disabled="noEdit" v-model="principalId">
									  			<option v-for="item in userList" :value="item.partyId">{{item.partyAccount}}</option>
									  		</select>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">询价员：<span class="text-red">*</span></label>
									  	<div class="col-sm-9  pdt7">
									  		<label class="form-label"><input type='checkbox' :disabled="noEdit" class='input-checkbox' v-model='enquiryNameChecked' v-on:click='enquiryNameCheckedAll'><span class="ml5">全选</span></label>
									  		<label class="form-label" v-for='checkb in offerList'>
										         <input type='checkbox' name='checkboxinput' :disabled="noEdit" class='input-checkbox' v-model='enquiryNameCheckboxModel' :value='checkb.partyId'>
										         <span class="ml3">{{checkb.partyAccount}}</span>
										    </label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">报价员：<span class="text-red">*</span></label>
									  	<div class="col-sm-9 pdt7">
									  		<label class="form-label"><input type='checkbox' :disabled="noEdit" class='input-checkbox' v-model='offerNameChecked' v-on:click='offerNameCheckedAll'><span class="ml5">全选</span></label>
									  		<label class="form-label" v-for='checkb in offerList'>
										         <input type='checkbox' name='checkboxinput' :disabled="noEdit" class='input-checkbox' v-model='offerNameCheckboxModel' :value='checkb.partyId'>
										         <span class="ml3">{{checkb.partyAccount}}</span>
										    </label>
									  	</div>
			  						</div>
			  					</div>
		  					</form>
		  					<!-- 基本信息表单 -->
		  					<!-- 代理产品线表单 -->
		  					<form name="createVendorProduct" autocomplete="off" id="createVendorProduct" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'product' && prodctType === 'PROXY'">
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
		  										<th width="120" v-if="!noEdit">操作</th>
		  									</tr>
		  								</thead>
		  							</table>
		  							<div class="table-body">
		  								<table class="table">
		  									<tbody>
		  										<tr v-for="(item, $index) in productLines">
		  											<td :class="{'red-tip': (item.brandSign && item.brandSign === 'Y')}">{{item.brandName}}</td>
		  											<td :class="{'red-tip': (item.brandSign && item.category1Sign === 'Y')}">{{item.category1Name?item.category1Name: '不限'}}</td>
		  											<td :class="{'red-tip': (item.brandSign && item.category2Sign === 'Y')}"><span v-if="item.category1Name">{{item.category2Name?item.category2Name: '不限'}}</span><span v-else>*</span></td>
		  											<td :class="{'red-tip': (item.brandSign && item.category3Sign === 'Y')}"><span v-if="item.category2Name">{{item.category3Name?item.category3Name: '不限'}}</span><span v-else>*</span></td>
		  											<td width="120" v-if="!noEdit"><a href="javascript:void(0);" @click="editProductItem($index)">编辑</a><a href="javascript:void(0);" @click="deleteProductItem($index)">删除</a></td>
		  										</tr>
		  									</tbody>
		  								</table>
		  								<div class="product-table-tip">红色字体，系统中无匹配字段，请检查后重新上传</div>
		  							</div>
		  						</div>
		  						
		  						<div class="form-group " v-if="!noEdit">
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
			  					<div class="form-group " v-if="!noEdit">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-1 control-label">或</label>
									  	<div class="col-sm-10">
									  		<div class="vendor-text blue" ><a href="javascript:void(0);" @click="showProductSelect()">在线选择产品线</a></div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="product-select" v-if="productSelectFlag && !noEdit">
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
			  					</div>
		  					</form>
		  					<!-- 代理产品线表单 -->
		  					
		  					<!-- 不代理产品线表单 -->
		  					<form name="notProxyVendorProduct" autocomplete="off" id="notProxyVendorProduct" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'product' && prodctType === 'NOT_PROXY'">
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
		  										<th width="120" v-if="!noEdit">操作</th>
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
		  											<td width="120" v-if="!noEdit"><a href="javascript:void(0);" @click="editProductItem($index)">编辑</a><a href="javascript:void(0);" @click="deleteProductItem($index)">删除</a></td>
		  										</tr>
		  									</tbody>
		  								</table>
		  								<div class="product-table-tip">红色字体，系统中无匹配字段，请检查后重新上传</div>
		  							</div>
		  						</div>
		  						
		  						<div class="form-group " v-if="!noEdit">
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
			  					<div class="form-group " v-if="!noEdit">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-1 control-label">或</label>
									  	<div class="col-sm-10">
									  		<div class="vendor-text blue" ><a href="javascript:void(0);" @click="showProductSelect()">在线选择产品线</a></div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="product-select" v-if="productSelectFlag && !noEdit">
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
								  		<input class="form-control vendor-input-p inline" type="text" name="creditQuota" :disabled="noEdit" v-model="creditInfo.creditQuota" maxlength="10"/>
								  		<select class="form-control vendor-hlaf-s inline" name="currency" :disabled="noEdit" v-model="creditInfo.currency">
								  			<option value="CNY">RMB</option>
								  			<option value="USD">USD</option>
								  		</select>
								  	</div>
								</div>
							  	<div class="form-group ">
		  							<label for="title" class="col-sm-2 control-label">授信期限：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input inline" type="text" name="creditDeadline" :disabled="noEdit" v-model="creditInfo.creditDeadline" maxlength="50"/>
								  	</div>
							  	</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">对账日期：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input-l inline" type="text" name="checkDate" :disabled="noEdit" v-model="creditInfo.checkDate" maxlength="2"/>天
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">付款日期：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input-l inline" type="text" name="payDate" :disabled="noEdit" v-model="creditInfo.payDate" maxlength="2"/>日
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">付款条件：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input" type="text" name="paymentTerms" :disabled="noEdit" v-model="creditInfo.paymentTerms" maxlength="20"/>
								  	</div>
		  						</div>
		  						<div class="form-group">
		  							<label for="title" class="col-sm-2 control-label">结算方式：</label>
								  	<div class="col-sm-9">
								  		<input class="form-control vendor-input" type="text" name="settlementMethod" :disabled="noEdit" v-model="creditInfo.settlementMethod" maxlength="10"/>
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
			  								<div class="bank-info-btn" v-show="!noEdit"><a href="javascript:void(0);" @click="addBankInfo('edit', $index)">编辑</a><a href="javascript:void(0);" @click="deleteBankInfo($index)">删除</a></div>
			  							</div>
			  						</li>
			  					</ul>
			  					<div class="bank-info-create" v-show="!noEdit">
			  						<button type="button" @click="addBankInfo('add')" class="btn btn-danger c-btn">新增银行账号</button>
			  					</div>
			  					<!-- 银行资料 -->
			  					<!-- 协议 -->
			  					<div class="product-label">协议</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">采购协议：<span class="text-red">*</span></label>
									  	<div class="col-sm-8">
									  		<select class="form-control vendor-hlaf inline" name="purchaseDeal" :disabled="noEdit" v-model="creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEAL">
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
									  			<input class="form-control vendor-hlaf inline" type="text" name="purchaseDealDate" :disabled="noEdit" :value="creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEALDATE" v-model="creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEALDATE"/>
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">保密协议：<span class="text-red">*</span></label>
									  	<div class="col-sm-8">
									  		<select class="form-control vendor-hlaf inline" name="secrecyProtocol" :disabled="noEdit" v-model="creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOL">
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
									  			<input class="form-control vendor-hlaf inline" type="text" name="secrecyProtocolDate" :disabled="noEdit" :value="creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE" v-model="creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE"/>
									  		</div>
									  	</div>
			  						</div>
			  						<div class="col-md-6 form-group">
			  							<label for="title" class="col-sm-3 control-label">附件：</label>
									  	<div class="col-sm-8">
									  		<span class="file-btn">添加文件 <input type="file" id="upload-deal" :disabled="noEdit"/></span> <span>支持格式：Excel、PDF、压缩文件</span>
									  		<div>
									  			<div class="lemon-tag inline tag" v-for="(item, $index) in creditInfo.creditAttachmentList"><span class="lemon-tag-text">{{item.attachmentName}}</span> <i class="fa fa-close" @click="removeAttachment($index)" v-if="!noEdit"></i></div>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<!-- 协议 -->
		  					</form>
		  					<!-- 信用表单 -->
		  					<!-- 销售信息 -->
		  					<div v-if="!noSales">
		  					<form name="createVendorSales" autocomplete="off" id="createVendorSales" onsubmit="return false;" class="form-horizontal cc-kt" novalidate="novalidate" v-show="tabValue === 'sales'">
		  					   <div class="product-label">
		  					   <span  class="nav_tab"><a :class="{tab:subTabValue == 'nextbaseData'}"  @click="oninfoChange">销售基本信息</a></span>
		  					   <span  class="nav_tab ml20"><a :class="{tab:subTabValue == 'nextmovData'}" @click="onmovChange">MOV设置</a></span>
		  					   <span class="nav_tab ml20"><a :class="{tab:subTabValue == 'nextSpecialData'}" @click="onSpecialChange">专属特价</a></span>
		  					   </div>
		  					  
		  					  <div  v-show="subTabValue === 'nextbaseData'">
		  						
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">负责人：</label>
									  	<div class="col-sm-9">
									  		<select class="form-control vendor-input" name="salePrincipalId" disabled v-model="salePrincipalId">
									  			<option v-for="item in userList" :value="item.partyId">{{item.partyAccount}}</option>
									  		</select>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">询价员：</label>
									  	<!-- <div class="col-sm-9">
									  		<select class="form-control vendor-input" name="saleEnquiryId" :disabled="salesEdit" v-model="saleEnquiryId">
									  			<option v-for="item in offerList" :value="item.partyId">{{item.partyAccount}}</option>
									  		</select>
									  	</div> -->
									  	<div class="col-sm-9  pdt7">
									  		<label class="form-label"><input type='checkbox' :disabled="salesEdit" class='input-checkbox' v-model='salesInfoEnquiryNameChecked' v-on:click='salesInfoEnquiryNameCheckedAll'><span class="ml5">全选</span></label>
									  		<label class="form-label" v-for='checkb in offerList'>
										         <input type='checkbox' name='checkboxinput' :disabled="salesEdit" class='input-checkbox' v-model='salesInfoEnquiryNameCheckboxModel' :value='checkb.partyId'>
										         <span class="ml3">{{checkb.partyAccount}}</span>
										    </label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">报价员：</label>
									  	<!-- <div class="col-sm-9">
									  		<select class="form-control vendor-input" name="saleOfferId" :disabled="salesEdit"  v-model="saleOfferId">
									  			<option v-for="item in offerList" :value="item.partyId">{{item.partyAccount}}</option>
									  		</select>
									  	</div> -->
									  	<div class="col-sm-9 pdt7">
									  		<label class="form-label"><input type='checkbox' :disabled="salesEdit" class='input-checkbox' v-model='salesInfoOfferNameChecked' v-on:click='salesInfoOfferNameCheckedAll'><span class="ml5">全选</span></label>
									  		<label class="form-label" v-for='checkb in offerList'>
										         <input type='checkbox' name='checkboxinput' :disabled="salesEdit" class='input-checkbox' v-model='salesInfoOfferNameCheckboxModel' :value='checkb.partyId'>
										         <span class="ml3">{{checkb.partyAccount}}</span>
										    </label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">支持币种：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="checkbox" value="RMB" name="currencyRmb" :disabled="salesEdit"/> RMB</label>
									  		<label class="form-label"><input type="checkbox" value="USD" name="currencyUsd" :disabled="salesEdit"/> USD</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商最小接单金额：</label>
									  	<div class="col-sm-9">
									  		<span v-if="showRmb" class="mb"><input type="text" class="form-control vendor-input-t inline" :disabled="salesEdit" name="minOrderPriceCNY" v-model="salesInfo.minOrderPriceCNY" maxlength="10"/> <span>RMB</span></span>
									  		<span v-if="showUsd" class="mb"><input type="text" class="form-control vendor-input-t inline" :disabled="salesEdit" name="minOrderPriceUSD" v-model="salesInfo.minOrderPriceUSD" maxlength="10"/> <span>USD</span></span>
									  	</div>
			  						</div>
			  					</div>
		  						<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否审核订单：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="radio" value="Y" :disabled="salesEdit" name="orderVerify" v-model="salesInfo.orderVerify"/> 是</label>
									  		<label class="form-label"><input type="radio" value="N" :disabled="salesEdit" name="orderVerify" v-model="salesInfo.orderVerify"/> 否</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否显示名称：<span class="text-red">*</span></label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="radio" value="Y" :disabled="salesEdit" name="isShowName" v-model="salesInfo.isShowName"/> 是</label>
									  		<label class="form-label"><input type="radio" value="N" :disabled="salesEdit" name="isShowName" v-model="salesInfo.isShowName"/> 否</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否启用MOV：</label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="checkbox" value="Y" name="vendorMovStatus" :disabled="salesEdit"/> 供应商MOV</label>
									  		<label class="form-label"><input type="checkbox" value="Y" name="skuMovStatus" :disabled="salesEdit"/> 商品MOV</label>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商仓库：</label>
									  	<div class="col-sm-9 cc-show">
									  		<span class="mb" v-for="(item, $index) in salesInfo.facilityList">
									  			<input style="display: inline-block;" class="form-control vendor-input" type="text" maxlength="20" :disabled="salesEdit" name="facility" v-model="salesInfo.facilityList[$index].facilityName"/>
									  			<label class="form-label"><input type="checkbox" value="Y" name="isShowaFacility" :disabled="salesEdit" @click="showaFacility($event,$index)" />
									  			显示仓库</label>
									  		</span>
									  		<a href="javascript:void(0)" @click="addFacility()" v-if="!salesEdit">新增仓库</a>
									  	</div>
			  						</div>
			  					</div>
			  					<!-- <div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">商品库存最大有效天数：</label>
									  	<div class="col-sm-9">
									  		<input type="text" class="form-control vendor-input-t inline" name="productInvalidDays" v-model="salesInfo.productInvalidDays" :disabled="salesEdit"/>天
									  	</div>
			  						</div>
			  					</div> -->
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">是否自动集成库存：</label>
									  	<div class="col-sm-9">
									  		<label class="form-label"><input type="radio" value="Y" name="isAutoIntegrateQty" :disabled="salesEdit" v-model="salesInfo.isAutoIntegrateQty"/> 是</label>
									  		<label class="form-label"><input type="radio" value="N" name="isAutoIntegrateQty" :disabled="salesEdit" v-model="salesInfo.isAutoIntegrateQty"/> 否</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">装运条款：</label>
									  	<div class="col-sm-9">
									  		<textarea class="form-control vendor-input vendor-textarea" name="description" :disabled="salesEdit" v-model="salesInfo.description" maxlength="20"></textarea>
									  		<!-- <span>{{discriptLength(salesInfo.description)}}/100</span> -->
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
			  					<div class="form-group " v-if="salesEdit">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">关注领域：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_FOCUSFIELDS}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group " v-if="salesEdit">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">优势产品类别：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			{{salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_PRODUCTCATEGORYS}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group " v-if="salesEdit">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">主要客户：</label>
									  	<div class="col-sm-9">
											<div class="vendor-text">
									  			{{salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_MAJORCLIENTS}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					
			  					<enter-input v-if="!salesEdit" :options="focusFieldsOptions" :keyname="focusFieldsname" :id="focusFieldsId" :name="focusFieldsName" :input-class="focusFieldsClass" :placeholder="focusFieldsPlaceholder"></enter-input>
			  				
			  					<enter-input v-if="!salesEdit" :options="productCategorysOptions" :keyname="productCategorysname" :id="productCategorysId" :name="productCategorysName" :input-class="productCategorysClass" :placeholder="productCategorysPlaceholder"></enter-input>
			  				
			  					<enter-input v-if="!salesEdit" :options="majorClientsOptions" :keyname="majorClientsname" :id="majorClientsId" :name="majorClientsName" :input-class="majorClientsClass" :placeholder="majorClientsPlaceholder"></enter-input>
			  					
			  						
			  					<div class="product-label" v-if="!noContactInfo">联系人信息</div>
			  					<div class="bank-info-create">
			  						<button type="button" @click="addContact('add')" class="btn btn-danger c-btn" v-if="!noContactInfo && status === 'salesEdit'">新增联系人</button>
			  					</div>
			  					<ul class="bank-info-list" v-if="!noContactInfo">
			  						<li v-for="(item, $index) in salesInfo.contactPersonInfoList">
			  							<div class="bank-info contact-info">
			  								<div class="back-info-h"><div class="contact-name" :title="item.lastNameLocal"><i class="red-tip" v-if="item.isDefault === 'Y'">*</i> {{item.lastNameLocal}}</div> <div class="bank-info-btn" v-if="!salesEdit"><a href="javascript:void(0);" @click="addContact('edit', $index)">编辑</a><a href="javascript:void(0);" @click="deleteContactInfo($index)">删除</a></div></div>
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
			  					 <div class="bank-info-create" v-if="!noContactInfo"><i class="red-tip">*</i> 为当前默认联系人</div>
			  				  </div>
			  				  <div  v-show ="subTabValue === 'nextmovData'">
			  				       
									<div class="r_edit"  v-show ="thereTabValue === 'movList'">
											<div class="cont_nav g3" >
								    			<span class="f14" style="line-height:30px;margin-left: 20px;">已保存的MOV规则</span>
								    			<a class="r but_cl"  @click="addRules()"><i class="icon-plus mr10 f12" style="font-weight:bold;"></i>新增MOV规则</a>
								    		</div>
								    		<div class="date_list mt30" >
								    			<div class="tem_content"  v-for="el in movList.list" >
									    				<p class="tem_tlt bb0"  >
									    					<span class="dib g3 b over_tw l" style="width:30%"><i v-html="el.ruleType == '0' ?'供应商MOV规则':'商品MOV规则'"></i></span>
									    					<span class="dib g3 tr" style="width:40%">创建时间：<i v-html="el.createdDate?el.createdDate:'--'"></i></span>
									    					<span class="dib tr mov-operate" style="width:29%;float:right;" >
									 							 <a href="javascript:;" v-if="el.status=='DISABLED'" @click="changeStatus('ENABLED',el.ruleId,$event)">启用</a>
									 							 <a href="javascript:;" v-if="el.status=='ENABLED'" @click="changeStatus('DISABLED',el.ruleId,$event)">停用</a> 
									 							 <a href="javascript:;" :class="el.status=='DISABLED'?'blue':'disable'" @click="el.status=='DISABLED'?reviseRules(el.ruleId):''">修改</a> 
									 							 <a href="javascript:;" :class="el.status=='DISABLED'?'blue':'disable'" @click="el.status=='DISABLED'?deleteRule(el.ruleId,$event):''">删除</a> 
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
			  				  
			  				      <div  class="xiegexinzengguize"  v-show ="thereTabValue === 'movEdit'">
				  				       <div class="row mt30">
											<div class="col-xs-2 tr">
												<label>规则类型：</label>
											</div>
											 <div class="col-xs-10 c5e">
												   <span>
													<input id="isrohs" type="radio" name="isrohs"  value="0"  v-model="ruleType" >
													<label for="isrohs">供应商MOV</label></span>
													<span  class="ml20">
													<input id="isnorohs" type="radio" name="isrohs" value="1" v-model="ruleType" >
													<label for="isnorohs">商品MOV</label>
												</span>
											</div>
										</div>
										 <div class="row mt30">
											<div class="col-xs-2 tr">
												<label>应用条件：</label>
											</div>
											<div class="col-xs-10 c5e bd">
											    <label class="l" >仓库：</label>
												<div class="warehouse" >

												       <input type="checkbox" id="none" v-model="isChecked" name="none" class="input_checkall" value="none" @change="onCheckboxChange">

													   <label  for="none" > </label>不限

												      <span  v-for ="(item, index) in wareList"  >
                                                          <input type="checkbox" :id="item.id"  v-model="item.isChecked" :value="item.id" name="item.id" class="input_check"  >
                                                          <label  :for="item.id" ></label>
                                                           {{item.facilityName}}
                                                      </span>

												</div>
												<div  class="manufacturer" >
												    <label  class="l"  title ="未选择时默认为不限">制造商：</label>
												    <!-- <span>未选择时默认为不限</span> -->
													  	<div   v-if="!showManSelectFlag">
													  		<div class="vendor-text" ><a href="javascript:void(0);" @click="showManufacturerSelect()"  v-html="manufacturerIds.length > 0?'重新选择':'请选择'"></a></div>
													  	     <div class="data-selected" v-for="(item, $index) in manufacturerIds"> 
														    	<span>{{item.brandName}}</span>
													    		<i   @click="delshowSelect($index)">x</i>
													    	</div>
													  	</div>
			  				                    </div>
			  				                    
			  				                    <div class="product-cate-tit"    v-if="!showManSelectFlag   &&  mdefalut "  >
													    	<div class="data-selected" v-for="(item, $index) in manufacturerIds"> 
														    	<span>{{item.brandName}}</span>
													    		<i   @click="delshowSelect($index)">x</i>
													    	</div>
													    	<div class="vendor-text" ><a href="javascript:void(0);" @click="showManufacturerSelect()">重新选择</a></div>
												</div>
												<div   v-if="showManSelectFlag"  >
							  							<letter-select
													            :keyname="mkeyname"
													            :id="mid"
													            :options="moptions"
													            :name="mname"
													            :option-id="moptionId"
													            :option-name="moptionName"
													            @get-selecteds="getSelected"
													            :multiple="multiple"
													            :placeholder="mplaceholder"
													            :selected="mselected"
													            ref="letter"    
													    ></letter-select>
													    <div class="product-cate-tit"   >
													    	已选择：
													    	<div class="data-selected" v-for="(item, $index) in tempManufacturerIds"> 
														    	<span>{{item.brandName}}</span>
													    		<i   @click="delmanfacture($index)">x</i>
													    	</div>
												         </div> 
												         <div class="product-cate-tit  mt30"  >
													    	<span  class='m_sure'   @click="m_sure(tempManufacturerIds)">确认</span><span  @click="m_cancel()" class="m_cancel ml20">取消</span>
												         </div>  
			  						              </div>
			  						                 
												  
											</div>
											
										</div>
										<div  class="row  mt30">
 											<div class="col-xs-2 tr">
												<label>设置MOV:</label>
											 </div>
											 <div  class="movSet col-xs-10">
											     <div  class="l">
											     <span class="check-span">
									            		<input class="check_select" type="checkbox" name="HK" id="HK">
									            		<label for="HK" class="checkbox_radio"><em class="tick"></em></label>
				             	                 </span>	 
												    <span>MOV（香港）</span>
												    <input type="text" v-model="usdMovAmount" @change="movAmountVail($event,'usd')" name="usdMovAmount" id="usdMovAmount"  maxlength ="10" >
												    <span>USD</span>
												 </div>
											 	<div class="l  ml20">
											 	    <span class="rel">
									            		<input class="check_select" type="checkbox" name="CH" id="CH">
									            		<label for="CH" class="checkbox_radio"><em class="tick"></em></label>
				            	                    </span>	
												    <span>MOV（国内）</span>
												   <input type="text" v-model="cnyMovAmount"   @change="movAmountVail($event,'cny')" name="cnyMovAmount" id="cnyMovAmount"  maxlength ="10">
												    <span>RMB</span>
											    </div>
											</div>
											
										</div>
										<div  class="row  mt30">
 											<div class="col-xs-2 tr">
												<label>规则备注:</label>
											 </div>
											 <div  class="col-xs-10">
											   <textarea id="todescription" name="description"  v-model="movdescription"  maxlength="100"   class="valid"  placeholder="可对模板进行描述，如Digi-Key的MOV规则"></textarea>
											   <span>{{movdescription.length}}/100</span>
											
											</div>
											
										</div>
			  				   
			  				    </div> 
			  				    
			  				   </div>
			  				   
			  				   <div class="special-set" v-show="subTabValue === 'nextSpecialData'">
			  				   		
			  				   		<!-- 专属特价列表 -->
			  				   		<div class="init" v-show="!showSpecialEdit && !showSpecialDetail">
			  				   			<div class="form-group">
					  						<div class="col-md-11">
					  							<label class="col-sm-2 control-label">供应商专属特价显示：</label>
											  	<div class="col-sm-4">
											  		<span class="radio-wrap" v-for="(val,key) in specialRuleStatusList">
											  			<input type="radio" name="specialType" :id="key" :value="key" v-model="specialRuleStatus" v-on:click="updateRuleStatus(key)">
											  			<label :for="key" v-text="val"></label>
											  		</span>
										  		</div>
										  		<label class="col-sm-2 control-label">显示文案：</label>
										  		<div class="col-sm-3">
										  			<input class="form-control" :disabled="specialRuleStatus=='INOPERATIVE'" name="specialRulesText" v-model.trim="specialRulesText" v-on:change="updateRulesText" maxlength="5">
										  		</div>
										  		<span :class="[specialRuleStatus!= 'APPOINT'?'disabled':'','add-rule-btn']" @click="addSpecialRule"><i class="icon-plus"></i>新增细则</span>
					  						</div>
					  						
				  							<div class="row">
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
																                      <input type="text" name="speListStartDate" id="speListStartDate" class="input-sm form-control" :disabled="specialRuleStatus=='ALL' || specialRuleStatus=='INOPERATIVE'"> 
																                      <span class="input-group-addon">至</span> 
																                      <input type="text" name="speListEndDate" id="speListEndDate" class="input-sm form-control" :disabled="specialRuleStatus=='ALL' || specialRuleStatus=='INOPERATIVE'">
															                      </div>
														                      </div>
														                  </div>
														              </div>
														          
														          	  <div class="col-sm-6 col-md-4 col-lg-6 margin10">
															               <label for="status" class="col-sm-2 control-label">规则类型</label>
												                           <div class="col-sm-4" >
													                          <select class="form-control" id="status" name="status" v-model="speListRuleType" :disabled="specialRuleStatus=='ALL' || specialRuleStatus=='INOPERATIVE'">
													                            <option value="">全部</option>
													                            <option value="RULE">产品线</option>
													                            <option value="MPN">型号</option>
													                          </select>
												                           </div>
														               </div>
														          </div>
														          
														          <div class="col-lg-2">
												                    <button :class="[specialRuleStatus!= 'APPOINT'?'btn-cancel':'btn-danger','btn-sm','btn','sendData','margin10']" id="search_all" :disabled="specialRuleStatus=='ALL' || specialRuleStatus=='INOPERATIVE'" @click="onSearchSpeListClick()">
												                      <i class="fa fa-search"></i>查询
												                    </button>
												                  </div>
											                  </div>
													       	</form>
														</div>
													 </div>
										          </section>
										      </div>
										      
										      <div class="row" v-if="!showSpecialEdit">
										        <section class="col-sm-12">
										          <div class="box ">
										            <div :class="[specialRuleStatus!= 'APPOINT'?'disabled':'','chart','box-body','special-rule-list']" style="position: relative;">
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
										        </section>
										      </div>
					  						
					  					</div>
					  					
			  				   		</div>
			  				   		<div class="set-rule" v-show="showSpecialEdit">
			  				   			<div class="form-group">
					  						<div class="col-md-11">
					  							<label class="col-sm-2 control-label">规则类型：</label>
											  	<div class="col-sm-3">
											  		<span class="radio-wrap" v-for="(val,key) in specialRuleTypeList">
											  			<input type="radio" name="specialRuleType" :id="key" :value="key" v-model="specialRuleType" :disabled="!isAddSpecialRule?true:false">
											  			<label :for="key" v-text="val"></label>
											  		</span>
										  		</div>
					  						</div>
					  					</div>
					  					<!-- 专属特价规则类型为产品线 -->
					  					<div class="form-group " v-if="specialRuleType == 'RULE'">
					  						<div class="col-md-11">
					  							<label class="col-sm-2 control-label">应用条件：</label>
					  							<div class="col-sm-9">
					  								<div class="apply-rule-wrapper bd" >
					  									<div class="row">
					  										<label for="specialRuleManufacturer" class="col-sm-2 s-prod-label"><span class="text-red"></span>制造商：</label>
					  										<div class="col-sm-10">
					  											<div class="lemon-area">
										                        		<div class="lemon-tag" v-for="(sel,index) in speaiclRuleProductLine.chosenManufacturer">
											                        		<span class="lemon-tag-text" v-text="sel.brandName"></span> 
											                        		<i class="fa fa-close" @click="deleteManufacturerSelect(index,sel.id)"></i>
										                        		</div>
										                        		<a href="javascript:void(0);" @click="choseSpecialRuleManufacturer">请选择</a>
									                            </div>
					  										</div>
					  									</div>
					  									<div class="row">
					  										<label for="specialRuleWharehouse" class="col-sm-2 s-prod-label"><span class="text-red"></span>仓库：</label>
					  										<div class="col-sm-9">
						  										<div class="ware-house-box"> 
				                                                      <input type="checkbox"  v-model="speaiclRuleProductLine.isAllChecked" class="input_allware_check" value="*" @change="isAllWarechecked">

																	   <label  for="none" > </label>不限
				
																      <span  v-for ="(item, index) in speaiclRuleProductLine.wareList"  >
				                                                          <input type="checkbox" :id="item.id"  v-model="item.isChecked" :value="item.id" name="item.id" class="input_ware_check"  >
				                                                          <label  :for="item.id" ></label>
				                                                           {{item.facilityName}}
				                                                      </span>
				                                                 </div>
			                                                 </div>
					  									</div>
				  										<div class="row pdt7">
				  											<label for="specialRuleCategory" class="col-sm-2 s-prod-label"><span class="text-red"></span>商品分类：</label>
				  											<div class="col-sm-9">
				  												<lemon-tabcate v-if="showTabcate" :api="speaiclRuleProductLine.cateApi" :init-data="speaiclRuleProductLine.cateInitData" :id="speaiclRuleProductLine.id" :name="speaiclRuleProductLine.cateName" :children="speaiclRuleProductLine.children" @get-checkeds="getCheckedCategories"></lemon-tabcate>
				  											</div>
					  									</div>
					  								</div>
					  							</div>
						  					</div>
						  					<!-- 介于同一页面两人开发，先modal组件各用各的 ，专属特价规则类型为产品线modal-->
						  					<modal v-if="specialRuleShowModal" @close="specialRuleShowModal = false" @click-ok="modalOk" @click-cancel="toggleModal" :modal-style="specialRuleModalStyle" :modal-text="specialRuleModalText">
						  						<div class="form-group" v-if="speaiclRuleProductLine.multiple">
						  							<span>已选择：</span>
										            <div class="col-md-12">
										            	<div class="lemon-area">
											            	<div class="lemon-tag" v-for="(sel,index) in speaiclRuleProductLine.sels">
								                        		<span class="lemon-tag-text" v-text="sel.brandName"></span> 
								                        		<i class="fa fa-close" @click="deleteManufacturer(index,sel.id)"></i>
							                        		</div>
						                        		</div>
										            </div>
										        </div>  
									            <div>
									            	<letter-select v-if="speaiclRuleShowManufacturer" :keyname="speaiclRuleProductLine.keyname" :validate="speaiclRuleProductLine.validate"
								                       :options="speaiclRuleProductLine.options" :id="speaiclRuleProductLine.id" :name="speaiclRuleProductLine.name" :option-id="speaiclRuleProductLine.optionId" :option-name="speaiclRuleProductLine.optionName" @get-selecteds="specialRuleGetSelected" :selected="speaiclRuleProductLine.selected"
								                       :multiple="speaiclRuleProductLine.multiple" ref="speaiclRuleProductLineLetter" :placeholder="speaiclRuleProductLine.placeholder" :show-search="speaiclRuleProductLine.showSearch" :show-letter="speaiclRuleProductLine.showLetter"></letter-select>
									            </div>
									        </modal>
						  					<!-- 介于同一页面两人开发，先modal组件各用各的 ，专属特价规则类型为产品线modal-->
					  					</div>
					  					<!-- 专属特价规则类型为型号 -->
					  					<div v-show="specialRuleType == 'MPN'">
					  						<div class="form-group">
						  						<div class="col-md-11">
						  							<label class="col-sm-2 control-label">应用型号：</label>
												  	<!-- <div class="col-sm-9">
												  		<span class="radio-wrap" v-for="(val,key) in specialMpnTypeList">
												  			<input type="radio" name="specialMpnType" :id="key" :value="key" v-model="specialMpnType"  :disabled="!isAddSpecialRule?true:false">
												  			<label :for="key" v-text="val"></label>
												  		</span>
											  		</div> -->
						  						</div>
						  					</div>
						  					<!-- 专属特价应用型号为EXCEL上传 -->
						  					<div class="form-group">
						  						<div class="col-md-11">
						  							<label class="col-sm-2 control-label"></label>
												  	<div class="col-sm-9">
												  		<span class="btn btn-primary" style="margin-bottom:10px;float:left;" @click="editAssociatePrd">单条添加型号</span>
												  		<div style="float:left;margin-left:30px;">
													  		<span class="btn btn-primary" id="uploadSpecialExcel">excel上传</span>
												  			<a class="template-download" href="${ctx}/template/Special_price_templet.xlsx">Excel模板下载</a>
												  			<span class="upload-tip">*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现所选类别</span>
											  			</div>
											  			<div class="uploadPrdList" v-if="showSpecialEdit && specialRuleType == 'MPN' && tabValue == 'sales' && subTabValue == 'nextSpecialData'">
												  			<rowspan-table
															        :api="uploadPrdApi"
															        :columns="uploadPrdColumns"
															        :pageflag="uploadPrdPageflag"
															        :checkflag = "uploadPrdCheckflag"
															        :query-params="uploadPrdQueryParams"
															></rowspan-table>
															<div class="upload-btn-wrap">
																<button class="btn btn-success" @click="uploadPrdmultDel('')">批量删除</button>
																<button class="btn btn-success" @click="exportSpeaiclPrd('')" ><i class="fa fa-download"></i>导出</button>
																<button class="btn btn-success" @click="exportSpeaiclPrd('error')"><i class="fa fa-download"></i>导出异常商品</button>
															</div>
														</div>
											  		</div>
						  						</div>
						  					</div>
					  					</div>
					  					<div class="form-group ">
					  						<div class="col-md-11">
					  							<label for="specialdescription" class="col-sm-2 control-label">规则备注：</label>
											  	<div class="col-sm-9">
											  		<textarea class="form-control" name="specialdescription" v-model.trim="specialRemark" maxlength="200"></textarea>
										  		</div>
					  						</div>
					  					</div>
					  					<div class="box-footer text-center">
						  					<button type="button" class="btn btn-danger btn-save" @click="specialSaveEvent">保存</button> 
						  					<button type="button" class="btn btn-concle" @click="specialCancelEvent">取消</button>
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
			  				 		<modal
			  				 			v-if="showModal"
			  				 			:modal-style="modalStyle"
		           						:show-foot="false"
		           						@close="prdAssociateCancel"
			  				 		>
			  				 			<h3 slot="header" v-text="modalTitle">添加型号</h3>
			  				 			<product-associate
			  				 				v-if="showPrdAssociate"
				                       		:api="productAssociate.api"
				                       		:request-type="productAssociate.requestType"
				                       		:product-data="productAssociate.productData"				                       		
				                       		:is-add="productAssociate.isAdd"
				                       		:is-read-only="true"
				                       		:show-image="productAssociate.showImage"
				                       		@cancel="prdAssociateCancel"
				                       		@get-product-info="getProductInfo"
				                       	></product-associate>
			  				 		</modal>
			  				   </div>
		  					</form>
		  					
	  						<form id="exportForm" action="" method="GET" target="_blank" style="display: none;">
				            	<input type="hidden" name="ids">
				            	<input type="hidden" name="ruleId">
				            	<input type="hidden" name="status">
				            </form>
		  					</div>
		  					<!-- 销售信息 -->
		  					<!-- 供应商别名 -->
					  		<form name="createVendorAlias" autocomplete="off" id="createVendorAlias" onsubmit="return false;" class="form-horizontal form-alias" novalidate="novalidate" v-show="tabValue === 'vendorAlias'">
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">供应商别名：</label>
									  	<div class="col-sm-9">
									  		<div class="mb" v-for="(el,index) in aliasInfo.aliasList">
									  			<span class="alias-item"><input class="form-control vendor-input" v-bind:data-indexnum="index" type="text" v-bind:name="'vendorAlias'+index" v-model="el.aliasName" :placeholder="'请输入供应商别名'" maxlength="50"/></span>
									  			<a class="ml5" href="javascript:void(0)" @click="deleteSingleAlias(el,index);">删除</a>
									  		</div>
									  		<div class="mb" v-if="aliasInfo.hasAddVendorAliasBtn"><a href="javascript:void(0);" @click="addVendorAlias">新增别名</a></div>
									  	</div>
			  						</div>
			  					</div>
		  					</form>
		  					<!-- 供应商别名 -->
		  				</div>
	  					<div class="box-footer text-center" v-if="status !== 'applyNo'">
	  						<button type="button" @click="saveBaseData();" class="btn btn-danger c-btn" v-show="tabValue === 'baseData' && !noEdit">保存</button>  
		  					<!-- 产品线 -->
		  					<button type="button" @click="saveProductData();" class="btn btn-danger c-btn" v-show="tabValue === 'product' && !noEdit">保存</button>  
		  					<!-- 产品线 -->
		  					<!-- 信用 -->
		  					<button type="button" @click="saveCreditData();" class="btn btn-danger c-btn" v-show="tabValue === 'credit' && !noEdit">保存</button>  
		  					<!-- 信用  -->
		  					<!-- 销售信息 -->
		  					 <button type="button" @click="saveSalesData()" class="btn btn-danger c-btn" v-show="tabValue === 'sales' && !salesEdit && subTabValue === 'nextbaseData'">保存</button> 
		  					<!-- 销售信息  -->
		  					<!-- 销售信息-设置mov -->
		  					<button type="button" @click="saveMovData($event)" id = "MovData" class="btn btn-danger  c-btn" v-show="tabValue === 'sales' && subTabValue === 'nextmovData' && thereTabValue === 'movEdit' ">保存</button> 
		  					<button type="button" @click="cancelData()" class="btn " v-show="tabValue === 'sales' && subTabValue === 'nextmovData' && thereTabValue === 'movEdit'">取消</button>  
		  					<!-- 销售信息  -设置mov-->
		  					
		  					<!-- 供应商别名 -->
		  					<button type="button" @click="saveAliasData();" class="btn btn-danger c-btn" v-show="tabValue === 'vendorAlias'">保存</button>  
		  					<!-- 供应商别名 -->
	  					</div>	
	  					<div class="box-footer text-center" v-if="status === 'applyNo'">
	  						<button type="button" @click="saveCreateBaseData();" class="btn btn-danger c-btn" v-show="tabValue === 'baseData'">下一步</button>  
		  					<!-- 产品线 -->
		  					<button type="button" @click="saveCreateProductData();" class="btn btn-danger c-btn" v-show="tabValue === 'product'">下一步</button>  
		  					<!-- 产品线 -->
		  					<!-- 信用 -->
		  					<button type="button" @click="saveData();" class="btn btn-danger c-btn" v-show="tabValue === 'credit'">保存</button>  
		  					<!-- 信用  -->
	  					</div>
	  				</div>	
	  				
	  				<div class="box box-danger" v-if="status !== 'applyNo'">
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
			<form onsubmit="return false;" class="form-horizontal" autocomplete="off" novalidate="novalidate" id="bankLayer">
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
  		
  		<div class="contact_layer" style="display: none;">
			<form onsubmit="return false;" class="form-horizontal" autocomplete="off" novalidate="novalidate" id="contactLayer">
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
 						<label for="title" class="col-sm-2 control-label">联系人负责的产品线：</label>
					  	<div class="col-sm-9">
					  		<div class="vendor-text">
					  			<a href="javascript:void(0);" @click="showProductLine()">选择</a>
					  			<div class="contact-productline">
					  				<div v-for="item in contactPersonInfo.partyProductLineList">
					  					<p :title="(item.brandName)+'-'+(item.category1Name?item.category1Name:'不限')+'-'+(item.category2Name?item.category2Name:'不限')+'-'+(item.category3Name?item.category3Name:'不限')">
					  						<span>{{item.brandName}}</span>-
					  						<span>{{item.category1Name?item.category1Name:'不限'}}</span>-
					  						<span>{{item.category2Name?item.category2Name:'不限'}}</span>-
					  						<span>{{item.category3Name?item.category3Name:'不限'}}</span>
					  					<p>
					  				</div>
					  			</div>
					  		</div>
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
 				<div style="display: none;">{{productFlagNum}}</div>
			</form>
  		</div>
  		
  		<div class="productline_layer" style="display: none;">
  			<div class="product-table">
  				<table class="table table-head"><thead><tr><th width="75"><label><input type="checkbox" v-model='checked' v-on:click='checkedAll'/> 选择</label></th><th>品牌</th> <th>大类</th> <th>小类</th> <th>次小类</th></tr></thead></table>
  				<div class="table-body">
  					<table class="table">
  						<tbody>
  							<tr v-for="item in salesInfo.partyProductLineList">
  								<td width="75"><input type="checkbox" v-model='checkboxModel' :value="item.partyProductLineId"/></td>
								<td>{{item.brandName}}</td>
								<td>{{item.category1Name?item.category1Name: '不限'}}</td>
								<td><span v-if="item.category1Name">{{item.category2Name?item.category2Name: '不限'}}</span><span v-else>*</span></td>
								<td><span v-if="item.category2Name">{{item.category3Name?item.category3Name: '不限'}}</span><span v-else>*</span></td>
							</tr>
  						</tbody>
  					</table>
  				</div>
  			</div>
  		</div>
  		
  		<div class="basedata-describe describe-layer" style="display: none;">
  			<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate" id="basedataDescribeLayer">
				<div class="form-group">
					<div>请填写修改说明：</div>
					<div class="discribe-textarea">
						<textarea class="form-control vendor-input inline" v-model="baseDescribe" maxlength="100"></textarea>
						<span>{{baseDescribe.length}}/100</span>
					</div>
 				</div>
 			</form>
  		</div>
  		
  		<div class="product-describe describe-layer" style="display: none;">
  			<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate" id="productDescribeLayer">
				<div class="form-group">
					<div>请填写修改说明：</div>
					<div class="discribe-textarea">
						<textarea class="form-control vendor-input inline" v-model="prodctDescribe" maxlength="100"></textarea>
						<span>{{prodctDescribe.length}}/100</span>
					</div>
 				</div>
 			</form>
  		</div>
  		
  		<div class="credit-describe describe-layer" style="display: none;">
  			<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate" id="creditDescribeLayer">
				<div class="form-group">
					<div>请填写修改说明：</div>
					<div class="discribe-textarea">
						<textarea class="form-control vendor-input inline" v-model="creditDescribe" maxlength="100"></textarea>
						<span>{{creditDescribe.length}}/100</span>
					</div>
 				</div>
 			</form>
  		</div>
  		
  		<div class="sales-describe describe-layer" style="display: none;">
  			<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate" id="salesDescribeLayer">
				<div class="form-group">
					<div>请填写修改说明：</div>
					<div class="discribe-textarea">
						<textarea class="form-control vendor-input inline" v-model="salesDescribe" maxlength="100"></textarea>
						<span>{{salesDescribe.length}}/100</span>
					</div>
 				</div>
 			</form>
  		</div>
		
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
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-modal.js"></script>
<script type="text/javascript" src="${ctx}/js/common/add.validate.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-tabcate.js"></script>
<script type="text/javascript" src="${ctx}/js/app/distrManufAssem.js"></script>
<script type="text/javascript" src="${ctx}/js/app/vendorEdit.js"></script>
</div>	    
</body>
</html>
