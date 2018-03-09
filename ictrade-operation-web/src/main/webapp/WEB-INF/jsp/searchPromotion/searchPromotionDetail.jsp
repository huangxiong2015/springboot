<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>搜索推广详情</title> 
<link rel="stylesheet" href="${ctx}/css/app/extensionEdit.css">
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<style>
.pt7{padding-top:7px;}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="recuit-edit">
	<section class="content-header">
	  <h1>搜索推广<small>详情</small></h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active">搜索推广</li>
	  </ol>
	</section> 
    <section class="content" style=" color:#666;">
    <form class="form-horizontal" name="searchPromotionDetail" id="searchPromotionDetail" onsubmit="return false;">    
    <div class="row">
        <section class="col-lg-12">
          <div class="box box-danger ">
              <!-- Morris chart - Sales -->
                <div class="box-header with-border">
                  <h3 class="box-title">搜索推广</h3>
                </div>
                <div class="box-body ">
	                <div class="form-group">
	                    <div class="col-md-11">
	                        <label for="desc" class="col-sm-2 control-label">推广描述</label>
	                         <div class="col-sm-4 pt7" v-text="searchPromotionData.desc"></div>
	                    </div>
	                </div>
					<!-- 分销商  -->
	                <div class="form-group">
	                	<div class="col-md-11">
	                    	<label class="col-sm-2 control-label">分销商</label>
	                        <div class="col-sm-4 pt7" v-text="searchPromotionData.distributorName===''?'不限':searchPromotionData.distributorName"></div>
	                	</div>
	                </div>
	                <!-- 原厂  -->
	                 <div class="form-group">
	                 	<div class="col-md-11">
	                    	<label class="col-sm-2 control-label">原厂</label>
	                       	<div class="col-sm-4 pt7" v-text="searchPromotionData.sourceName===''?'不限':searchPromotionData.sourceName"></div>
	                   	</div>
	               	</div>
	                <!-- 分类 -->
	                <div class="form-group ">
	                	<div class="col-md-11">
	                   		<label class="col-sm-2 control-label">分类</label>
	                   		<div class="col-sm-4 pt7">
		                   		<span v-text="searchPromotionData.categoryTypeName1===''?'不限':searchPromotionData.categoryTypeName1"></span>
		                   		<span v-text="searchPromotionData.categoryTypeName2?'>'+searchPromotionData.categoryTypeName2:''"></span>
		                   		<span v-text="searchPromotionData.categoryTypeName3?'>'+searchPromotionData.categoryTypeName3:''"></span>
	                   		</div>
	                 	</div>
	                 </div>
	                 <div class="form-group ">
	                    <div class="col-md-11">
	                        <label for="extend" class="col-sm-2 control-label">推广截止日</label>
	                        <div class="col-sm-4 pt7" v-text="searchPromotionData.expiryDate?searchPromotionData.expiryDate:'-'"></div>
	                    </div>
	                 </div>
	                 <div class="form-group ">
	                    <div class="col-md-11">
	                        <label for="extend" class="col-sm-2 control-label">推广商品数</label>
	                        <div class="col-sm-4 pt7" v-text="searchPromotionData.goodsQuantity"></div>
	                    </div>
	                 </div>
	                 <div class="form-group ">
	                	<div class="col-md-11">
	                   		<label class="col-sm-2 control-label">排序</label>
	                        <div class="col-sm-4 pt7" v-text="searchPromotionData.orderSeq"></div>
	                 	</div>
	                 </div>
                </div>
                <div class="box-footer text-center">
                    <button type="button" class="btn btn-default c-btn" onclick="goBack();">返回</button>
                </div>
                <input type="hidden" value="RECRUITMENT" name="categoryTypeId">
          </div>
		</section>
		</div>
	</form>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script src="${ctx}/assets/lemon/1.0.0/lemon-select.js"></script> 
<script src="${ctx}/assets/lemon/1.0.0/lemon-city.js"></script>

</body>
<script type="text/javascript" >
$(function(){ 
	  var vm = new Vue({
	        el: '#searchPromotionDetail',
	        data: { 
	        	searchPromotionData:{
	        		newsContent:{}
	        	}  
	        }
	   });
	  var recommendationId = getQueryString('recommendationId');
	  if(recommendationId){

		  syncData(ykyUrl.info + "/v1/recommendations/searchpromotion/"+ recommendationId , "get", null, function (data, err) {
	          if (data) {
	        	  vm.searchPromotionData = data;   
	        	  console.log(data);
	          }  
	      }); 
	  }
});
function goBack(){
	location.href = ykyUrl._this + "/searchPromotion.htm"
}
</script>
</html>