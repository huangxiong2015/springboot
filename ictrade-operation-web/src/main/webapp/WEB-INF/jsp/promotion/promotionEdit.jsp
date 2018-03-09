<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>活动管理-活动促销</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" href="${ctx}/css/app/activity.css"/>
<style>
.has-error span.help-block{display:none !important;}
.check-wrap label{font-weight:normal;position: relative;top: -1px;}
.pt7{padding-top:7px;}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="activityInformation">
	<section class="content-header">
	  <h1>
		 活动维护 
  		<small v-if="!initData.promotionId">编辑</small>
	    <small v-else>添加</small> 
	  </h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active"><a  :href="newsHome">活动维护</a></li>
	  </ol>
	</section> 
    <section class="content" style=" color:#666;">
    <form class="form-horizontal" name="create" id="create" onsubmit="return false;">    
    <div class="row">
    	<input type="hidden" value="${activityId }" id="promotionId"/>
        <section class="col-lg-12">
          <div class="box box-danger activity-info">
              <!-- Morris chart - Sales -->
                <div class="box-header with-border">
                  <h3 class="box-title">
                  	<span v-if="!initData.promotionId">编辑  -</span>
		    		<span v-else>添加  -</span>
                  	<span>活动维护</span>	 				
                  </h3>
                </div>				
                <div class="box-body ">
	                <div class="form-group ">
	                    <div class="col-md-11">
	                        <label for="title" class="col-sm-2 control-label">活动名称<span class="text-red">*</span></label>
	                        <div class="col-sm-4"> 
	                            <input type="text" class="form-control" name="promotionName" id="promotionName"  required v-model="initData.promotionName" maxlength="50"/>
	                        </div>
	                        <span class="ipt-tips dn">50位中英文字母、符号或数字</span>
	                        <span class="ipt-vail dn">*活动名称不能为空</span>
	                    </div>
	                 </div> 
	                 <div class="form-group">
		                <div class="col-md-11">
			                <label for="createData" class="col-sm-2 control-label">活动时间<span class="text-red">*</span></label>
			                <div class="col-sm-4">
				                <div id="createData">
					                <div id="createDataRange" class="input-daterange input-group">
					                	<div class="input-append date startDates form_datetime">
            								<input type="text" name="startDate" id="startDate" class="form-control"  required v-model="initData.startDate" readonly="readonly"> 					                	
					                		<span class="add-on"><i class="icon-th"></i></span>
					                	</div>
						                <span class="input-group-addon">至</span> 
						                <div class="input-append date endDates form_datetime">
						                	<input type="text" name="endDate" id="endDate" class="form-control"  required v-model="initData.endDate" readonly="readonly">
					                		<span class="add-on"><i class="icon-th"></i></span>
					                	</div>
					                </div>
				                </div>
			                </div>
		                 </div>
	                  </div>
	                  <div class="form-group ">
	                    <div class="col-md-11">
	                        <label for="categoryTypeId" class="col-sm-2 control-label">活动类型<span class="text-red">*</span></label>
	                        <div class="col-sm-4">
			                    <select class="form-control" id="promotionType"  name="promotionType" v-model="initData.promotionType" v-bind="{disabled:initData.promotionId?true:false}"> 
								    <option value="ORDINARY">普通活动</option>
								    <option value="RECOMMEND">购物车推荐</option>
								</select>
	                        </div>
	                    </div>
                 	   </div>
                 	   <!-- 普通活动 -->
                 	   <div v-show="initData.promotionType == 'ORDINARY'">
							<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="categoryTypeId" class="col-sm-2 control-label">创建方式<span class="text-red">*</span></label>
			                        <div class="col-sm-4">
					                    <select class="form-control" id="createType"  name="createType" v-model="initData.createType" v-bind="{disabled:initData.promotionId?true:false}"> 
										    <option value="CUSTOM">定制开发</option>
										    <option value="TEMPLATE">模板创建</option>
										</select>
			                        </div>
			                    </div>
			                </div>
		                   <div class="form-group" v-show="initData.createType == 'TEMPLATE'">
			                <div class="col-md-11">
				                <label for="createData" class="col-sm-2 control-label">使用优惠券</label>
				                <div class="col-sm-1">				                
					                <select class="form-control" id="useCoupon" name="isUseCoupon" v-model="initData.isUseCoupon"> 
									    <option value="Y">是</option>
									    <option value="N">否</option>									    
									  </select>								  			                
				                </div>
				                <div class="col-sm-2 coupon-par" v-if="initData.isUseCoupon=='Y'">
					                <a class="select-coupon" @click="selectCoupon">
					                	<span>请选择优惠券</span>
					                	<i class="icon-down_arrow1 g9"></i>
					                </a>
					                <div class="coupon-wrap">
					                	<p v-html="couponList.length?'选择优惠券':'暂无优惠券，请先创建'"></p>
					                	<div class="coupon-list">
					                		<template v-for="(item,index) in couponList">
					                			<span>
						                			<input type="checkbox" :id="'coupon'+index" :value="item"  v-model="temporaryCouponList">
							                		<label class="s-coupon" :for="'coupon'+index">{{item.couponName}}</label>
						                		</span>
					                		</template>
					                	</div>
					                	<p>
					                		<a v-if="couponList.length" @click="closeCouponList('save')" class="coupon-save">保存</a>
					                		<a v-if="couponList.length" @click="closeCouponList('cancel')" class="coupon-cancel">取消</a>
					                		<a v-if="!couponList.length"@click="closeCouponList('know')" class="coupon-save close-box">知道了</a>
					                	</p>
					                </div>	
				                </div>
			                 </div>
		                  </div>
		                  <div class="form-group" v-show="selectCouponList.length&&initData.isUseCoupon=='Y'&&initData.createType == 'TEMPLATE'">
			               	<div class="col-md-11">
				                <label for="createData" class="col-sm-2 control-label"></label>
				                
				                <div class="col-sm-4 coupon-par">
					                <div class="selected-list">
					                	<label class="s-selected" v-for="item in selectCouponList">
					                		<span v-html="item.couponName" :id="item.couponId"></span>
					                		<i class="icon-close-min delete-btn" @click="deleteCoupon(item)"></i>
					                	</label>				                	
					                </div>	
				                </div>
			                 </div>
		                  </div>
		                  <div class="form-group">
			                <div class="col-md-11">
				                <label class="col-sm-2 control-label">活动封面<span class="text-red">*</span></label>
				                <div class="col-sm-7"  > 
		                       	 	<div class="manu_logo" style="position:relative;"> 
				                       	<input class="file-btn pos-abs" type="button" value="上传文件" id="uploadBtn">    
			                            <span>
			                            	<img alt="" :src="initData.faceImgUrl ? initData.faceImgUrl : '${ctx}/images/defaultImg01.jpg'" width="120" height ="120"  class="d-logo" />		                       			
			                       		</span>
			                        </div>
	                        	</div>
			                 </div>
		                  </div>
		                  <div class="form-group">
			                <div class="col-md-11">
				                <label for="createData" class="col-sm-2 control-label">活动链接<span class="text-red" v-if="initData.createType == 'CUSTOM'">*</span></label>
				                <div class="col-sm-4"> 
		                            <input type="text" class="form-control" name="promotionUrl" id="promotionUrl" required v-model="initData.promotionUrl" maxlength="50" :disabled="initData.createType == 'TEMPLATE'?true:false"/>
		                        </div>
			                 </div>
		                  </div>
		                  <div class="form-group" v-if="initData.createType == 'TEMPLATE'">
			                <div class="col-md-11">
				                <label for="createData" class="col-sm-2 control-label">SEO设置</label>
				                <div class="col-sm-4 seo-wrap"> 
				                	<div v-for="item in seoField">
				                		<label v-text="item.label">title</label>
				                		<textarea type="text" class="form-control seo-textarea" :placeholder="item.placeholder" :name="item.name" :id="item.label" :maxlength="item.maxLength" v-model.trim="initData[item.name]"></textarea>
				                		 <p class="text-r"><span v-text="initData[item.name].length ? initData[item.name].length : '0'"></span>/{{item.maxLength}}</p>
				                	</div>
		                        </div>
			                 </div>
		                  </div>
	                  </div>
	                  <!-- 购物车推荐 -->
	                  <div v-show="initData.promotionType == 'RECOMMEND'">
                  		 <div class="form-group">
			                <div class="col-md-11">
				                <label for="createData" class="col-sm-2 control-label">详情链接<span class="text-red">*</span></label>
				                <div class="col-sm-4"> 
		                            <input type="text" class="form-control" name="promotionContent[productUrl]" id="productUrl" required v-model="recommendContent.productUrl" @change="getProductData"/>
		                            <input type="hidden" name="promotionContent[productId]" v-model="recommendContent.productId">
		                        </div>
			                 </div>
		                  </div>
		                  <div class="form-group">
			                <div class="col-md-11">
				                <label for="createData" class="col-sm-2 control-label">数量<span class="text-red">*</span></label>
				                <div class="col-sm-4"> 
		                            <input type="text" class="form-control" name="promotionContent[productQuantity]" id="productQuantity" required maxlength="10" v-model="recommendContent.productQuantity" onkeyup='this.value=this.value.replace(/^[0]+|\D/gi,"")'/>
		                        </div>
		                        <div class="col-sm-2 pt7">
		                        	起订量：<span v-text="recommendContent.moq?recommendContent.moq:'-'"></span>
		                        	<input type="hidden" name="promotionContent[moq]" v-model="recommendContent.moq">
		                        </div>
			                 </div>
		                  </div>
		                  <div class="form-group">
			                <div class="col-md-11">
				                <label for="createData" class="col-sm-2 control-label">购物车类别<span class="text-red">*</span></label>
				                <div class="col-sm-4 pt7"> 
		                            <span class="check-wrap">
			                            <input type="checkbox" name="promotionContent[cartType][]" id="inland" value="CNY" v-model="recommendContent.cartType"/>
			                            <label for="inland">国内</label>
		                            </span class="check-wrap">
		                            <span class="check-wrap">
			                            <input type="checkbox" name="promotionContent[cartType][]" id="HK" value="USD" v-model="recommendContent.cartType"/>
			                            <label for="HK">香港</label>
		                            </span>
		                        </div>
			                 </div>
		                  </div>
		                  <div class="form-group">
			                <div class="col-md-11">
				                <label for="createData" class="col-sm-2 control-label">型号</label>
				                <div class="col-sm-4 pt7"> 
		                            <span v-text="recommendContent.productMpn ? recommendContent.productMpn : '-'"></span>
		                            <input type="hidden" name="promotionContent[productMpn]" v-model="recommendContent.productMpn">
		                        </div>
			                 </div>
		                  </div>
		                  <div class="form-group">
			                <div class="col-md-11">
				                <label for="createData" class="col-sm-2 control-label">分类</label>
				                <div class="col-sm-4 pt7"> 
		                            <span v-text="recommendContent.productCateName ? recommendContent.productCateName : '-'"></span>
		                            <input type="hidden" name="promotionContent[productCateName]" v-model="recommendContent.productCateName">
	                            </div>
			                 </div>
		                  </div>
	                  </div>
                  </div>
                 				
                <div class="box-footer text-center">
                	<button type="button" class="btn btn-danger btn-save" @click="saveData" >保存</button>
                    <button type="button" class="btn btn-concle" @click="cancelClick" >取消</button>
                </div>
          </div>
          </section>
          </div>
	</form>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/app/promotionEdit.js"></script>

</body>
</html>