<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>类别广告-新增</title>
<link rel="stylesheet" href="${ctx}/css/app/extensionEdit.css">
</head>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<div class="content-wrapper" id="advertise_edit">
			<section class="content-header">
			  <h1>
			    	广告位
			    <small class="edit_state">{{ pageTitle }}</small>
			  </h1>
			  <ol class="breadcrumb">
			    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
			    <li class="active">广告位新增</li>
			  </ol>
			</section>   
		    
	    <section class="content" style=" color:#666;">
	      <form class="form-horizontal" name="manu-from" id="manu-from" onsubmit="return false;" >    
	          <div class="row">
	              <section class="col-lg-12">
	                <div class="box box-danger ">
	                    <!-- Morris chart - Sales -->
	                      <div class="box-header with-border">
	                        <h3 class="box-title"><span v-if=""></span><span class="edit_state">{{ pageTitle }}</span>&nbsp;-&nbsp;广告位</h3>
	                      </div>
	                      <div class="box-body ">
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label"><span class="text-red">*</span>广告类型：</label>
	                                      <div class="col-sm-4" >
	                                      	<select  class="form-control valid"  id="categoryId" name="categoryId"  v-model="categoryId">
	                                      	    <option v-for="item in initData.category" :value="item.categoryId">{{ item.categoryName }}</option>
											</select>
	                                      </div>
	                                  </div>
	                              </div>
	                   			 <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label"><span class="text-red">*</span>广告页面：</label>
	                                      <div class="col-sm-4" >
	                                      	  <select  class="form-control valid" id="pageId" name="pageId" v-model="pageId">
	                                      	    <option v-for="item in initData.page" :value="item.pageId">{{ item.pageName }}</option>
											</select>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group " >
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label"><span class="text-red">*</span>广告位置：</label>
	                                      <div class="col-sm-4" >
	                                      	<select class="form-control" id="positionId" name="positionId"  v-model="positionId">
												<option v-for="item in initData.position" :value="item.positionId">{{ item.positionName }}</option>
											</select>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group">
	                                   <div class="col-md-11">
	                                     <label for="createData" class="col-sm-2 control-label"><span class="text-red">*</span>投放时间：</label> 
	                                      <div class="col-sm-4" >
	                                        <div id="createData" >
		                                        <div id="createDataRange" class="input-daterange input-group">
			                                         <input :disabled="pageId==7002" type="text" name="startDate" id="startDate" class="form-control" v-model="startDate">
			                                         <span class="input-group-addon">至</span> 
			                                         <input :disabled="pageId==7002" type="text" name="expiryDate" id="expiryDate" class="form-control" v-model="expiryDate">
		                                         </div>
	                                         </div>
	                                      </div>
	                                  </div>
						         </div>
	                         	 <div class="form-group" >
				                    <div class="col-md-11">
				                        <label for="specurl" class="col-sm-2 control-label">广告小图：</label>
				                        <div class="col-sm-7" >
				                        <p class="img-desc"><span style="color:#B2191B;">*</span> 支持单张大小不超过2M的jpg、jepg、png格式图片，图片尺寸为95*45为佳，最多上传9张</p> 
				                       	 <div class="row">
				                       	 	<ul class="small-ad-list">
				                       	 		<li  v-for="(item,$index) in smallImagesAdList">
				                       	 			<div class="logo-box">
				                       	 				<i class="icon-close-min i-delete" v-show="item.image!=''&&item.image" v-on:click="deleteImg({type:'small',index:$index})"></i>
							                        	<input v-bind:id="'uploadbackSmall'+$index" class="btn uploadback-btn uploadback-btn-small" data-imagetype="small" type="button" value="上传文件"> 
						                            	<img class="background" :src="item.image ? item.image : '${ctx}/images/static/defaultImg.120.jpg'" />
							                       	 </div>
				                       	 		</li>
				                       	 	</ul>
				                       	 </div>
				                       	 <div class="row">
				                       	 	<ul class="small-ad-input-list">
				                       	 		<li  v-for="(item,$index) in smallImagesAdList">
				                       	 			<div class="col-sm-8" >
							                       	 	<input type="text" v-model="item.url" class="image-url-input form-control ui-autocomplete-input" :placeholder="'位置'+numToChinsese[$index+1]+'链接：'"/>
							                       	 </div>
				                       	 		</li>
				                       	 	</ul>
				                       	 </div>  
				                        </div> 
				                    </div>
               					 </div>
               					 <div class="form-group" >
				                    <div class="col-md-11">
				                        <label for="specurl" class="col-sm-2 control-label">广告大图：</label>
				                        <div class="col-sm-7" >
				                        <p class="img-desc"><span style="color:#B2191B;">*</span> 支持单张大小不超过2M的jpg、jepg、png格式图片，图片尺寸为290*135为佳，最多上传2张</p>
				                        <div class="row mt20" v-for="(item,$index) in bigImagesAdList" >
				                        	<div class="col-sm-5 big-ad-list" >  
						                       	 <div class="logo-box">
						                       	 	<i class="icon-close-min i-delete" v-show="item.image!=''&&item.image" v-on:click="deleteImg({type:'big',index:$index})"></i>
						                        	<input v-bind:id="'uploadbackBig'+$index" class="btn uploadback-btn uploadback-btn-big" data-imagetype="big" type="button" value="上传文件"> 
					                            	<img class="background" :src="item.image ? item.image : '${ctx}/images/static/defaultImg.120.jpg'" />
						                       	 </div>
					                       	  </div>
					                       	 <div class="col-sm-7" >
					                       	 	<input type="text" v-model="item.url" class="image-url-input form-control ui-autocomplete-input" :placeholder="'+添加图片链接'"/>
					                       	 </div>
				                        </div>
				                        </div> 
				                    </div>
               					 </div>  
	                             <div class="ovh mt20 ml30 pl30 btn-wrap" style="margin-left: 268px;margin-bottom: 30px; padding-bottom: 60px; padding-top: 20px;">
						              <a id="save_btn" class="l mr30 btnStyle01 dib f16 tc active ml30 condition_inquery" href="javascript:;" @click="save">保 存</a>
						              <a id="cancel_btn" class="l mr20 btnStyle01 dib f16 tc return_btn" @click="cancel">取 消</a>
				         	     </div>
	                           </div>
	                             
	                      </div>
	                      
	              </section>
	          </div>
	      </form>
	    </section>
	    	<!-- 阿里云OSS图片地址前缀 -->
			<input type="hidden" value="<spring:eval expression="@appProps.getProperty('item.ImgUrl')"/>photos/" id="imagePrefix">
			<input type="hidden" value="${recommendationId}" id="recommendationId">
			
	
	</div>
	
</div> 

<!-- Main content end -->
<script type="text/javascript">
	 var selectCatid = "242,244"; 
</script>
<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<!---footer  end   ---->
<script type="text/javascript" src="${ctx}/js/oss/common.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script> 
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>  
<script type="text/javascript" src="${ctx}/js/lib/pinyin.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/moment.min.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script> 
<script type="text/javascript" src="${ctx}/js/app/classifiedAdsEdit.js"></script>  
</body>
</html>