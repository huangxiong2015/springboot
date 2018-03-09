<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>活动管理-限时促销</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" href="${ctx}/css/app/activity.css"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="activityInformation">
	<section class="content-header">
	  <h1>
		 活动维护 
  		<small v-if="(activityId!=='' && from=='') || (activityId!=='' && from=='maintain')">编辑</small>
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
    	<input type="hidden" value="${activityId }" id="activityId"/>
        <section class="col-lg-12">
          <div class="box box-danger activity-info">
              <!-- Morris chart - Sales -->
                <div class="box-header with-border">
                  <h3 class="box-title">
                  	<span v-if="(activityId!=='' && from=='') || (activityId!=='' && from=='maintain')">编辑  -</span>
		    		<span v-else>添加  -</span>
                  	<span>活动维护</span>	 				
                  </h3>
                </div>
				<!-- <input type="hidden" id="newsId" name="newsId" :value="initData.newsId"> -->
                <div class="box-body ">
	                <div class="form-group ">
	                    <div class="col-md-11">
	                        <label for="title" class="col-sm-2 control-label">活动名称<span class="text-red">*</span></label>
	                        <div class="col-sm-4"> 
	                            <input type="text" class="form-control" name="name" id="name" v-model="initData.name" maxlength="50"/>
	                        </div>
	                        <span class="ipt-tips dn">50位中英文字母、符号或数字</span>
	                        <span class="ipt-vail dn">*活动名称不能为空</span>
	                    </div>
	                 </div>
					 <div class="form-group ">
	                    <div class="col-md-11">
	                        <label for="categoryTypeId" class="col-sm-2 control-label">活动类型<span class="text-red">*</span></label>
	                        <div class="col-sm-4">  
			                    <select class="form-control" id="type"  name="type" v-model="initData.type"> 
								    <option value="10001">限时促销</option>
								  </select>
	                        </div>
	                    </div>
	                 </div> 
	                 <div class="form-group">
		                <div class="col-md-11">
			                <label for="createData" class="col-sm-2 control-label">活动时间<span class="text-red">*</span></label>
			                <div class="col-sm-4">
				                <div id="createData">
					                <div id="createDataRange" class="input-daterange input-group">
					                	<div class="input-append date startDates form_datetime">
            								<input type="text" name="startDates" id="startDates" class="form-control" v-model="initData.startDates" readonly="readonly"> 					                	
					                		<span class="add-on"><i class="icon-th"></i></span>
					                	</div>
						                <span class="input-group-addon">至</span> 
						                <div class="input-append date endDates form_datetime">
						                	<input type="text" name="endDates" id="endDates" class="form-control" v-model="initData.endDates" readonly="readonly">
					                		<span class="add-on"><i class="icon-th"></i></span>
					                	</div>
					                </div>
				                </div>
			                </div>
		                 </div>
	                  </div>
	                  <div class="form-group">
		                <div class="col-md-11">
			                <label for="createData" class="col-sm-2 control-label">使用优惠券</label>
			                <div class="col-sm-1">				                
				                <select class="form-control" id="useCoupon"  name="type" v-model="status"> 
								    <option value="VALID">是</option>
								    <option value="NOT_VALID">否</option>									    
								  </select>								  			                
			                </div>
			                <div class="col-sm-2 coupon-par" v-if="status=='VALID'">
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
				                		<a v-if="couponList.length" @click="saveCouponList" class="coupon-save">保存</a>
				                		<a v-if="couponList.length" @click="cancelCouponList('cancel')" class="coupon-cancel">取消</a>
				                		<a v-if="!couponList.length"@click="cancelCouponList('know')" class="coupon-save">知道了</a>
				                	</p>
				                </div>	
			                </div>
		                 </div>
	                  </div>
	                  <div class="form-group" v-if="selectCouponList.length && status=='VALID'">
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
			                <label for="createData" class="col-sm-2 control-label">打折</label>
			                <div class="col-sm-1">				                
				                <select class="form-control pd5" name="type" v-model="initData.promoDiscountStatus"> 
								    <option value="Y">是</option>
								    <option value="N">否</option>									    
								</select>								  			                
			                </div>
			                <div v-if="initData.promoDiscountStatus=='Y'">
				                <div class="col-sm-2">
					                <input type="text" class="form-control" id="promoDiscount" @change="discountVali($event)" v-model="initData.promoDiscount">				                	
				                </div>
				                <span class="input-tips">请输入0~1之间数字，支持两位小数 ，例：0.9代表9折</span>				                
			                </div>
		                 </div>
	                  </div>
	                  <div class="form-group">
		                <div class="col-md-11">
			                <label for="createData" class="col-sm-2 control-label">库存读取方式</label>
			                <div class="col-sm-4 radio-wrap">	
			                	<span><input id="isY" type="radio" name="isSystemQty" value="N" v-model="initData.isSystemQty" disabled="disabled" ><label for="isY">上传库存</label></span>
			                	<span><input id="isN" type="radio" name="isSystemQty" value="Y" v-model="initData.isSystemQty" disabled="disabled"><label for="isN">系统库存</label></span>			                					                				                
			                </div>
		                 </div>
	                  </div>
	                  <div class="form-group">
		                <div class="col-md-11">
			                <label for="createData" class="col-sm-2 control-label">显示促销标志</label>
			                <div class="col-sm-4 radio-wrap">
			                	<span><input id="showIcon" type="radio" value="Y" v-model="initData.iconStatus"><label for="showIcon">是</label></span>
			                	<span><input id="notShow" type="radio" value="N" v-model="initData.iconStatus"><label for="notShow">否</label></span>
			                </div>
		                 </div>
	                  </div>
	                  <div class="form-group" v-show="initData.iconStatus=='Y'">
		                <div class="col-md-11">
			                <label class="col-sm-2"></label>			                
			                <div class="col-sm-5 upload-icon-wrap">
			                	<div class="line" v-for="(value,key,index) in iconUrlList">
				                	<span class="check-wrap"><input :id="key" type="checkbox" :value="key" v-model="scenesList"><label :for="key" v-html="scenesObj[key]"></label></span>
				                	<div class="icon-wrap">				                		
				                		<img width=100 height=100 :src="value" v-if="value">
				                		<span class="icon-plus add-icon" v-if="!value"></span>
				                		<span :id="'uploadIcon'+index" class="upload-btn show-btn">上传图片</span>
				                	</div>
				                	<a class="delete-icon" v-if="value" @click="deleteIcon(key)">删除图片</a>
			                	</div>
			                </div>
		                 </div>
	                  </div>
	                  <div class="form-group">
		                <div class="col-md-11">
			                <label for="createData" class="col-sm-2 control-label">限时时段<span class="text-red">*</span></label>
			                <div class="col-sm-4">
				                <table style="width:100%" class="time-range">
				                	<thead>
				                		<tr>
				                			<td>时段</td>
				                			<td style="text-align:center;">时段时间</td>
				                			<td>操作</td>
				                		</tr>
				                	</thead>
				                	<tbody>
				                		<tr v-for="(item,index) in initData.periodsList">
				                			<td v-html="'时段'+num[index]"></td>
				                			<td>
					                			<div class="input-daterange input-group">
						                			<div class="input-append date startTime form_datetime">
			            								<input type="text" class="form-control startTime" readonly="readonly" data-type ="startTime" v-bind:data-index ="index" v-bind:name="'startTime'+index" v-bind:id="'startTime'+index" v-model="item.startTime"> 					                	
								                		<span class="add-on"><i class="icon-th"></i></span>
								                	</div>
									                <span class="input-group-addon">至</span> 
									                <div class="input-append date endTime form_datetime">
			            								<input type="text" class="form-control endTime" readonly="readonly" data-type ="endTime" v-bind:data-index ="index" v-bind:name="'endTime'+index" v-bind:id="'endTime'+index "v-model="item.endTime">					                	
								                		<span class="add-on"><i class="icon-th"></i></span>
								                	</div>
							                	</div>
						                	</td>
				                			<td><a v-if="initData.periodsList.length>1" class="btn" v-on:click="deleteTime(index)">删除</a></td>
				                		</tr>
				                	</tbody>
				                </table>
			                </div>
		                 </div>
	                  </div>
	                  <div class="form-group" v-if="((activityId == '' && from=='') || (activityId !== '' && from=='upload')) && initData.periodsList.length<10">
		                  <div class="col-md-11">
			                  <div class="col-sm-4 col-sm-offset-2">
			                  	<a href="javascript:void(0);" id="addControl" class="pull-left" v-on:click="addTime"><i class="fa fa-plus"></i> 新增</a>
			                  </div>
		                  </div>
	                  </div>
                  </div>				
                <div class="box-footer text-center">
                    <button type="button" class="btn btn-danger c-btn" v-html="(activityId!=='' && from=='') || (activityId!=='' && from=='maintain')?'下一步：编辑商品':'下一步：添加商品'" v-on:click="saveData" >下一步：添加商品</button>                   
                    <button type="button" class="btn btn-concle c-btn" >取消</button>
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
<script type="text/javascript" src="${ctx}/js/app/timePromotionInformation.js"></script>

</body>
</html>