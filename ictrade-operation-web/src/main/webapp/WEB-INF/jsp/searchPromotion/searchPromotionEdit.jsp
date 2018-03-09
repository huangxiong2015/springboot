<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>搜索推广管理</title> 
<link rel="stylesheet" href="${ctx}/css/app/extensionEdit.css">
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<style>
span.help-block{display:none !important;}
.col-lg-9{width: 92%;}
.poi{cursor: pointer;}
.b_red{border: solid 1px #eb0038 !important;}
.datetimepicker-minutes{max-height:250px;overflow-y:auto;}
.form-control[readonly]{background:#fff;}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="recuit-edit">
	<section class="content-header">
	  <h1>搜索推广<small>编辑</small></h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active">搜索推广</li>
	  </ol>
	</section> 
    <section class="content" style=" color:#666;">
    <form class="form-horizontal" name="searchPromotion" id="searchPromotion" onsubmit="return false;">    
    <div class="row">
        <section class="col-lg-12">
          <div class="box box-danger">
              <!-- Morris chart - Sales -->
              <input type="hidden" name="status" :value="initData.status"/>
              <input type="hidden" name="recommendationId" :value="initData.recommendationId"/>
                <div class="box-header with-border">
                  <h3 class="box-title">搜索推广</h3>
                </div>
                <div class="box-body "> 
                	<!-- 推广描述 -->
	                <div class="form-group ">
	                    <div class="col-md-11">
	                        <label for="desc" class="col-sm-2 control-label">推广描述：<span class="text-red">*</span></label>
	                         <div class="col-sm-4" >
								<div id="desc"> 
									<input type="text" class="form-control" name="desc" required v-model="initData.desc" maxlength="50" placeholder="不超过50个字符" />
								</div>
							</div>
	                    </div>
	                </div>
					<!-- 分销商  -->
	                <div class="form-group">
	                	<div class="col-md-11">
	                    	<label class="col-sm-2 control-label">分销商</label>
	                        <div class="col-sm-4" >
	                       		<span id="_distributor" style="padding-right:15px;" v-text="activeVendorName===''?'不限':activeVendorName"></span>
	                            <a id="distributor" class="lh32 poi" @click="chooseVendor">选择</a>
	                            <input type="hidden" :value="activeVendor" name="distributor"/>   
	                        </div>
	                	</div>
	                </div>
	                
	                <!-- 原厂  -->
	                 <div class="form-group">
	                 	<div class="col-md-11">
	                    	<label class="col-sm-2 control-label">原厂</label>
	                       	<div class="col-sm-4" >
	                           	<span id="_selectBrand" style="padding-right:15px;" v-text="activeBrandName===''?'不限':activeBrandName"></span>
	                       		<a id="selectBrand" class="lh32 poi" @click="chooseBrand">选择</a>
	                       		<input type="hidden" :value="activeBrand" name="source"/>   
	                        </div>
	                   	</div>
	               	</div>
	                <!-- 分类 -->
	                <div class="form-group ">
	                	<div class="col-md-11">
	                   		<label class="col-sm-2 control-label">分类</label>
	                   		<div id="app-select" class="col-md-4">
							    <select-province
							            :api="api"
							            @onchange="change"
							            :input-class="inputClass"
							            :style-object="styleObject"
							            :name-province="nameFirst" 
							            :name-city="nameSecond" 
							            :name-district="nameThird"  
							            :id-province="idFirst" 
	    								:id-city="idSecond" 
	    								:id-district="idThird"
	    								:parent-id="parentId"
	    								:option-id="optionId"
	            						:option-name="optionName"
	            						:set-province="initData.categoryType1"
	            						:set-city="initData.categoryType2"
	            						:set-district="initData.categoryType3"
							    ></select-province>
							</div>
	                 	</div>
	                 </div>
	                 <!-- 推广截止日 -->
	                <div class="form-group ">
	                    <div class="col-md-11">
	                        <label for="extend" class="col-sm-2 control-label">推广截止日：</label>
	                        <div class="col-sm-4"> 
	                            <input type="text" class="form-control form_datetime" readonly="readonly" name="expiryTime"/>
	                        </div>
	                        <a class="col-sm-1" style="padding-top:10px;cursor: pointer;" @click="emptyExpiryTime">清空</a>
	                    </div>
	                </div>
	                <!-- 商品数 -->
	                <div class="form-group ">
	                    <div class="col-md-11">
	                        <label for="extend" class="col-sm-2 control-label">推广商品数：<span class="text-red">*</span></label>
	                        <div class="col-sm-4"> 
	                            <input type="text" class="form-control" name="goodsQuantity" id="extend" required v-model="initData.goodsQuantity" maxlength="5" placeholder="不超过10条" onkeyup='this.value=this.value.replace(/^[0]+|\D/gi,"")' @blur="checkGoodsQuantity"/>
	                        </div>
	                    </div>
	                </div>
	                <!-- 排序 -->
	                <div class="form-group ">
	                	<div class="col-md-11">
	                   		<label class="col-sm-2 control-label">排序：<span class="text-red">*</span></label>
	                        <div class="col-sm-4" >
	                           	<select class="form-control" id="orderSeq" name="orderSeq" v-model="initData.orderSeq">
									<option v-for="i in 10" :value="i" v-text="i"></option>
								</select>
	                    	</div>
	                 	</div>
	                 </div>
                
                </div>
                <div class="box-footer text-center">
                    <button type="button" class="btn btn-danger c-btn" @click="submitData">保存</button>
                    <button type="button" class="btn btn-danger c-btn" @click="goBack">取消</button>
                </div>
                <input type="hidden" value="RECRUITMENT" name="categoryTypeId">
                <input type="hidden" value="" id="categoryType" name="categoryType">
                <modal
                	v-if="showModal"
            		@close="toggleModal"
		            @click-ok="modalOk"
		            @click-cancel="toggleModal"
		            :modal-style="modalStyle"
                >
                	<h3 slot="header" v-text="modalTitle">请选择分销商</h3>
                	<letter-select
                		 v-if="showVendor"
					     :keyname="vendorSelect.keyname"
					     :validate="vendorSelect.validate"
					     :options="vendorSelect.options"
					     :id="vendorSelect.id"
					     :option-id="vendorSelect.optionId"
					     :option-name="vendorSelect.optionName"		 
					     @get-selecteds="getVendorSelected"
					     :placeholder="vendorSelect.placeholder"
					     :selected="vendorSelect.selected"
					     :searchText="vendorSelect.searchText"
					     :char="vendorSelect.char"
				    	 :multiple="vendorSelect.multiple"
					     ref="vendorLetter"
				 	></letter-select>
				 	<letter-select
                		 v-if="showBrand"
					     :keyname="brandSelect.keyname"
					     :validate="brandSelect.validate"
					     :options="brandSelect.options"
					     :id="brandSelect.id"
					     :name="brandSelect.name"
					     :option-id="brandSelect.optionId"
					     :option-name="brandSelect.optionName"		 
					     @get-selecteds="getBrandSelected"
					     :placeholder="brandSelect.placeholder"
					     :selected="brandSelect.selected"
					     :searchText="brandSelect.searchText"
					     :char="brandSelect.char"
				    	 :multiple="brandSelect.multiple"
						 :is-fuzzy-search="brandSelect.isFuzzySearch"
						 :reload-api="brandSelect.reloadApi"				    		 
					     ref="brandLetter"
				 	></letter-select>
                </modal>
          </div>
		</section>
		</div>
	</form>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script src="${ctx}/assets/lemon/1.0.0/lemon-select.js"></script> 
<script src="${ctx}/assets/lemon/1.0.0/lemon-city.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-modal.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/pinyin.js"></script> 
<script type="text/javascript" src="${ctx}/js/app/searchPromotionEdit.js"></script>

</body>
</html>