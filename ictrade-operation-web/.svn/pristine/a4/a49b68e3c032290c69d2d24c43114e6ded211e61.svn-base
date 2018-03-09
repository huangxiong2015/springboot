<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>活动装修</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/activity/decoration.css">

</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="app">
  
  <section class="content-header-wrap">
  	<div class="content-header" v-bind:class="{ fixed : showFixed }">
	  	<h1>活动装修 - {{activityTitle}}</h1>
	  	<div class="pull-right activity-btn">
	  		<select  v-model="previewDisplaySidebar">
	  			<option  value="Y">显示导航</option>	
	  			<option value="N">不显示导航</option>	
	  		</select>
	  		<a class="btn btn-xs btn-default " :href="viewUrl" target="_blank">预览</a>
	  		<a class="btn btn-xs btn-warning " @click="publish">{{getPublishTxt()}}</a>
	  	</div>
  	</div>
  </section>
  <section class="content">
	<div  >
        <draggable v-model="list" :options="{draggable:'.item'}" class="module-box">
            <div v-for="(el, index) in  list" :key="el.promoModuleId" class="item box box-danger">
                <div class="box-header with-border" title="拖动模块可对模块进行排序">
                    <h3 class="box-title">{{getModleTitle(el)}} <span v-html="getSetTip(el)"></span></span>
                    </h3>
                    <div class="pull-right">
                    	<button title="配置模块" class="btn btn-xs btn-primary " @click="edit(index, el)">配置</button>
	                    <button title="删除模块" class="btn btn-xs btn-default " @click="del(index, el)">删除</button>
                    </div>
                </div>
                
                <!-- /.box-body -->
            </div>
        </draggable>
        <div>
            <button @click="clickShow" type="button" class="btn btn-block btn-primary btn-lg">添加模块</button>
        </div>

        <modal v-if="showModal" @close="showModal = false" @click-ok="modalOk" @click-cancel="toggleModal" :modal-style="modalStyle">
            <h3 slot="header">请选择模块类型</h3>
            <moudletype @get-type="getType"> </moudletype>
            <div v-html="content">
            </div>
        </modal>

        <modal v-if="settingModal.showConModal" @close="cancle" @click-ok="modalConOk" @click-cancel="toggleConModal" :modal-style="settingModal.modalStyle" :show-foot="settingModal.showFoot">
            <h3 slot="header">{{title}}</h3> 
            <banner-set v-if="editType=='BANNER'" @form-submit="formSubmit" @cancle="cancle" :api="ceatePromotionApi"   :banner-data="defaultData"></banner-set>
            <coupon-set v-else-if="editType == 'COUPON'" :api="couponApi" @cancle="cancle" @form-submit="formSubmit" :desc-data="defaultData"></coupon-set>
            <product-list-set  v-else-if="editType == 'PRODUCT_LIST'" @cancle="cancle" :api="editProductPromotionApi" :product-analysis-api="productAnalysisApi" :del-product-api="deleteProductApi" :derived-product-api="derivedProductApi" :product-data-api="productDataApi" :form-data="descFormData" :title-form-data="titleFormData" :product-list-data="defaultData" ref="productListSet"></product-list-set>            
            <description-set  v-else @form-submit="formSubmit" @cancle="cancle"  :api="ceatePromotionApi" :desc-data="defaultData"></description-set>
 
        </modal>

    </div>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" /> 
    <script src="${webres}/lib/Sortable/1.6.0/Sortable.min.js"></script>
    <script src="${webres}/lib/Vue.Draggable/2.14.1/vuedraggable.min.js"></script>
    <script src="${webres}/lib/plupload/2.1.2/plupload.full.min.js"></script>

	<script src="${ctx}/js/oss/oss_uploader.js"></script>
    <script src="${ctx}/js/common/config.js"></script>
    <script src="${ctx}/js/common/request.js"></script>
    <script src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
    <script src="${ctx}/assets/lemon/1.0.0/lemon-modal.js"></script>
    <script src="${ctx}/assets/lemon/1.0.0/lemon-common.js"></script>  
    <script src="${ctx}/assets/lemon/1.0.0/lemon-steps.js"></script>
    <script src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script>
    <script src="${ctx}/assets/lemon/1.0.0/lemon-category-tab.js"></script>  
    <script src="${ctx}/js/component/promotion/bannerSet.js"></script>
    <script src="${ctx}/js/component/promotion/moudleSelect.js"></script>
    <script src="${ctx}/js/component/promotion/couponSet.js"></script>
    <script src="${ctx}/js/component/promotion/descriptionSet.js"></script>
    <script src="${ctx}/js/component/promotion/titleSet.js"></script> 
    <script src="${ctx}/js/component/promotion/productSet.js"></script>
    <script src="${ctx}/js/component/promotion/productEdit.js"></script>
    <script src="${ctx}/js/component/promotion/productListSet.js"></script>
    <script src="${ctx}/js/app/activityDecorate.js"></script> 
</body>
</html>