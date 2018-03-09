<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>活动维护</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" href="${ctx}/css/app/activity.css"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="commodityMaintain">
	<section class="content-header">
	  <h1>
		 活动维护 
  		<small>维护</small>
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
          <div class="box box-danger goods-maintain">
              <!-- Morris chart - Sales -->
                <div class="box-header with-border">
                  <h3 class="box-title">
                  	<span>维护商品</span>	 				
                  </h3>
                </div>
				<!-- <input type="hidden" id="newsId" name="newsId" :value="initData.newsId"> -->
                <div class="box-body ">
                	<ul class="time-tab">
	  					<li v-for="item in periodsList" v-if="item.status == 'ENABLE'" :class="{active:queryParams.periodsId==item.periodsId}"><a v-on:click="changePeriods(item.periodsId)" :id="item.periodsId" v-html="item.startTime +'-'+item.endTime "></a></li>
	  				</ul>
	  				<table class="prd-list mt30"  :class="{'over-head':prdList.length>30}">
	  					<thead>
	  						<tr>
	  							<td>商品信息</td>
	  							<td>供应商</td>
	  							<td>库存</td>
	  							<td>仓库</td>
	  							<td>阶梯</td>
	  							<td>限时价</td>
	  							<td>操作</td>
	  						</tr>
	  					</thead>
	  				</table>
	  				<div class="prd-area mt30">	  					  				
	  				<table class="prd-list">	  					
	  					<tbody>
	  						<template v-for='(item,index) in prdList'>
	  						<tr v-if="index!==0">
	  							<td colspan="7" style="height:20px;border:none;"></td>
	  						</tr>
	  						<tr>
	  							<td colspan="7" style="background:#f2f5fa;height:40px" v-html="item.title"></td>
	  						</tr>
	  						<tr>	  							
	  							<td>
									<div class="prd-info">
										<img :src="item.image1?item.image1:'${ctx}/images/static/defaultImg.120.jpg'">
										<div class="p_info">
											<p :title="item.manufacturerPartNumber" v-html="item.manufacturerPartNumber"></p>
											<p><span>类型：</span><span :title="item.category3Name?item.category3Name:''" v-html="item.category3Name?item.category3Name:'--'"></span></p>
											<p><span>品牌：</span><span :title="item.manufacturer?item.manufacturer:''" v-html="item.manufacturer?item.manufacturer:'--'"></span></p>											
										</div>
									</div>
								</td>
	  							<td v-html="item.vendorName">供应商</td>
	  							<td v-html="item.totalQty">库存</td>
	  							<td v-html="item.sourceName?item.sourceName:'--'">仓库</td>
	  							<td>
	  								<span class="db" v-for="ele in item.qtyBreak" v-html="ele + '+'"></span>
	  							</td>
	  							<td>
	  								<span class="db" v-for="ele in item.priceBreak" v-html="item.currencyUomId=='CNY'?'￥'+ele:'$'+ele"></span>
	  							</td>
	  							<td>
	  								<a href="javascript:;" v-on:click="editProduct(item.activityProductId)">编辑</a>
	  								<a href="javascript:;" v-on:click="deleteProduct(item.activityProductId,item.activityId,item.periodsId)" v-if="showDeleteBtn">删除</a>
	  							</td>
	  						</tr>
	  						</template>
	  						<tr v-if="!prdList.length">
	  							<td colspan="7" style="text-align:center;height:60px;" v-html="loadingData">加载中...</td>
	  						</tr>
	  					</tbody>
	  				</table>               	
                	</div>
                </div>
                <div class="modal-mask layer-box">
                	<div class="modal-wrapper">
                		<div class="layer-top">编辑商品<i @click="closeEdit" class="icon-close-min"></i></div>                		
	                	<div id="commodityEdit" class="commodity-edit">	                		
	                		<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="title" class="col-sm-2 control-label">型号：</label>
			                        <div class="col-sm-4">
			                            <span v-html="editInfo.manufacturerPartNumber?editInfo.manufacturerPartNumber:'--'"></span>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="title" class="col-sm-2 control-label">类型：</label>
			                        <div class="col-sm-4">
			                            <span v-html="editInfo.category3Name?editInfo.category3Name:'--'"></span>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="title" class="col-sm-2 control-label">品牌：</label>
			                        <div class="col-sm-4">
			                            <span v-html="editInfo.manufacturer?editInfo.manufacturer:'--'">ZTE</span>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="title" class="col-sm-2 control-label">供应商：</label>
			                        <div class="col-sm-4">
			                            <span v-html="editInfo.vendorName?editInfo.vendorName:'--'">Digi-Key</span>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="title" class="col-sm-2 control-label">仓库：</label>
			                        <div class="col-sm-4">
			                            <span v-html="editInfo.sourceName?editInfo.sourceName:'--'">北美仓</span>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="title" class="col-sm-2 control-label">主标题：</label>
			                        <div class="col-sm-4">
			                            <textarea v-model="editInfo.title" maxlength=100></textarea>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="title" class="col-sm-2 control-label">副标题：</label>
			                        <div class="col-sm-4">
			                            <textarea v-model="editInfo.subTitle" maxlength=100></textarea>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="title" class="col-sm-2 control-label"><span class="text-red">*</span>库存：</label>
			                        <div class="col-sm-4">
			                            <input type="text" name="qty" id="qty" style="width:450px;height:36px;" v-model="editInfo.totalQty" maxlength=9>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-11">
			                        <label for="title" class="col-sm-2 control-label">商品图片：</label>
			                        <div class="col-sm-9">
			                            <span>支持不大于2M的jpg、jpeg、png、gif格式文件，以正方形为佳；最多只能上传5张</span>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-12">
			                        <label for="title" class="col-sm-1 control-label"></label>
			                        <div class="col-sm-11">
			                            <ul class="img_upload">
			                            	<li>
			                            		<i class="icon-close-min i-delete" v-show="editInfo.image2" v-on:click="deleteImage('image1')"></i>
			                            		<img :src="editInfo.image1?editInfo.image1:'${ctx}/images/static/defaultImg.120.jpg'">
			                            		<span class="upload_btn" id="uploadBtn1">上传图片</span>
			                            	</li>
			                            	<li>
			                            		<i class="icon-close-min i-delete" v-show="editInfo.image2" v-on:click="deleteImage('image2')"></i>
			                            		<img :src="editInfo.image2?editInfo.image2:'${ctx}/images/static/defaultImg.120.jpg'">
			                            		<span class="upload_btn" id="uploadBtn2" v-show="editInfo.image1">上传图片</span>
			                            	</li>
			                            	<li>
			                            		<i class="icon-close-min i-delete" v-show="editInfo.image3" v-on:click="deleteImage('image3')"></i>
			                            		<img :src="editInfo.image3?editInfo.image3:'${ctx}/images/static/defaultImg.120.jpg'">
			                            		<span class="upload_btn" id="uploadBtn3" v-show="editInfo.image2">上传图片</span>
			                            	</li>
			                            	<li>
			                            		<i class="icon-close-min i-delete" v-show="editInfo.image4" v-on:click="deleteImage('image4')"></i>
			                            		<img :src="editInfo.image4?editInfo.image4:'${ctx}/images/static/defaultImg.120.jpg'">
			                            		<span class="upload_btn" id="uploadBtn4" v-show="editInfo.image3">上传图片</span>
			                            	</li>
			                            	<li>
			                            		<i class="icon-close-min i-delete" v-show="editInfo.image5" v-on:click="deleteImage('image5')"></i>
			                            		<img :src="editInfo.image5?editInfo.image5:'${ctx}/images/static/defaultImg.120.jpg'">
			                            		<span class="upload_btn" id="uploadBtn5" v-show="editInfo.image4">上传图片</span>
			                            	</li>
			                            </ul>
			                        </div>
			                    </div>
		                 	</div>
		                 	<div class="form-group ">
			                    <div class="col-md-11">
			                    	<label for="title" class="col-sm-2 control-label">商品描述：</label>
			                        <div class="col-sm-10" style="padding-left:0px;"> 		                        		
			                            <div id="desc" class="ueditor_wrapper"> 
										    <iframe name="coniframe" style="width:100%;border: none;min-height:546px;" src="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" scrolling="no" frameborder="0"  allowTransparency="true">
										    </iframe>
										</div>
			                        </div>
			                    </div>
		                 	</div>
	                	</div>
	                	<div class="layui-layer-btn"><a @click="saveEdit" class="layui-layer-btn0">确定</a></div>
                	</div>
                </div>
                	<input type="hidden" id="iframeUrl_Id" value="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" />			
                <div class="box-footer text-center">                   
                    <button type="button" class="btn btn-concle c-btn" >上一步</button>
                    <button type="button" class="btn btn-danger c-btn" v-on:click="saveData">保存	</button>
                </div>
          </div>
          </section>
          </div>
	</form>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/app/commodityMaintain.js"></script>
<script>
	$("#open").on("click",function(){
		var index = layer.open({
			  fix:true,
			  offset:"60px", 
		      type: 1,
		      title: '编辑商品',
		      shade:0.3,
		      btn:['确      定'],
		      area: ['1000px', '604px'],
		      content: $("#commodityEdit"),
		      skin:'commodity-edit',
		      yes:function(){
		    	  console.log('确定')
		    	  layer.close(index);
		      },
		      cancel:function(){
		    	  console.log('关闭')
		      }
		})
	})
</script>
</body>
</html>