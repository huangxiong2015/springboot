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
<title>广告位-新增</title>
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
	                                      <label class="col-sm-2 control-label"><span class="text-red">*</span>广告标题：</label>
	                                     <div class="col-sm-4" >
	                                          <input type="text" id="title" name="title" class="form-control ui-autocomplete-input" maxlength="50"  v-model="title">
	                                      </div>
	                                  </div>
	                              </div>
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
	                                      	<select :disabled="pageId==7002||pageId==7003" class="form-control" id="positionId" name="positionId"  v-model="positionId">
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
			                                         <input :disabled="pageId==7002||pageId==7003" type="text" name="startDate" id="startDate" class="form-control" v-model="startDate">
			                                         <span class="input-group-addon">至</span> 
			                                         <input :disabled="pageId==7002||pageId==7003" type="text" name="expiryDate" id="expiryDate" class="form-control" v-model="expiryDate">
		                                         </div>
	                                         </div>
	                                      </div>
	                                  </div>
						         </div>
						         <div class="form-group"  v-show="pageId==7001&&positionId==8001">
	                                   <div class="col-md-11">
	                                     <label for="createData" class="col-sm-2 control-label"><span class="text-red"></span>所属分销商：</label> 
	                                      <div class="col-sm-8 letter-select-wrap" >
	                                        <!-- 供应商 -->
											<span class="s-selected" v-for="sel in distributorDatasels1" @click="distributorDatadel1(sel.id)" ><em>{{sel.name}}</em><i class="icon-close-min"></i></span><a class="advertisement-edit" ><span @click="choosedistributor()">选择</span></a>
											<div id="distributorApp"  class="letter-select" style="display:none;">
												当前已选：
												<div class="form-group"  v-if="!distributorDatamultiple"> <div class="col-md-12">{{distributorDatasels.name}} </div></div>
											    <div class="form-group" v-if="distributorDatamultiple"> <div class="col-md-12 selected-list"><span class="s-selected" v-for="sel in distributorDatasels" @click="distributorDatadel(sel.id)" ><em>{{sel.name}}</em><i class="icon-close-min"></i></span></div></div>
											    <letter-select
											            :keyname="distributorDatakeyname"
											            :validate="distributorDatavalidate"
											            :options="distributorDataoptions"
											            :id="distributorDataid"
											            :name="distributorDataname"
											            :option-id="distributorDataoptionId"
											            :option-name="distributorDataoptionName"
											            @get-selecteds="distributorDatagetSelected"
											            :selected="distributorDataselected"
											            :multiple="distributorDatamultiple"
											            ref="distributorDataletter"
											            :placeholder="distributorDataplaceholder"
											    ></letter-select>
											</div>
											<!-- 供应商 -->
	                                      </div>
	                                  </div>
						         </div>
						         <div class="form-group"  v-show="pageId==7001&&positionId==8001">
	                                   <div class="col-md-11">
	                                     <label for="createData" class="col-sm-2 control-label"><span class="text-red"></span>所属制造商：</label> 
	                                      <div class="col-sm-8 letter-select-wrap" >
	                                       <!-- 制造商 -->
	                                       	<span class="s-selected" v-for="(sel,index) in manufacturerDatasels1" @click="manufacturerDatadel1(index)" ><em>{{sel.brandName}}</em><i class="icon-close-min"></i></span>
											<a class="advertisement-edit" ><span @click="chooseManufacturer()">选择</span></a>
											<div id="manufacturerApp"  class="letter-select" style="display:none;">
												当前已选：
												<div class="form-group"  v-if="!manufacturerDatamultiple"> <div class="col-md-12 selected-list">{{manufacturerDatasels.brandName}} </div></div>
											    <div class="form-group" v-if="manufacturerDatamultiple"> <div class="col-md-12 selected-list"><span class="s-selected" v-for="sel in manufacturerDatasels" @click="manufacturerDatadel(sel.id)" ><em>{{sel.brandName}}</em><i class="icon-close-min"></i></span></div></div>
												<letter-select
														v-if="showManufacturerSelect"
											            :keyname="manufacturerDatakeyname"
											            :validate="manufacturerDatavalidate"
											            :options="manufacturerDataoptions"
											            :id="manufacturerDataid"
											            :name="manufacturerDataname"
											            :option-id="manufacturerDataoptionId"
											            :option-name="manufacturerDataoptionName"
											            @get-selecteds="manufacturerDatagetSelected"
											            :selected="manufacturerDataselected"
											            :multiple="manufacturerDatamultiple"
											            ref="manufacturerDataletter"
											            :placeholder="manufacturerDataplaceholder"
											            :is-fuzzy-search="manufacturerIsFuzzySearch"
 														:reload-api="manufacturerReloadApi"
											    ></letter-select>
											</div>
	                                       <!-- 制造商 -->
	                                      </div>
	                                  </div>
						         </div>
	                             <div class="form-group " v-show="positionId!=8002||pageId!=7001">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label"><span class="text-red">*</span>曝光占比：</label>
	                                     <div class="col-sm-4" >
	                                          <input :disabled="pageId==7002||pageId==7003" type="text" id="proportion" name="proportion" class="form-control ui-autocomplete-input" placeholder="1~9的数字" maxlength="50" v-model="proportion">
	                                      </div>
	                                  </div>
	                              </div>
	                         	 <div class="form-group" >
				                    <div class="col-md-11">
				                        <label for="specurl" class="col-sm-2 control-label"><span class="text-red">*</span>广告效果图：</label>
				                        <div class="col-sm-7" > 
				                       	 <div class="logo-box">
				                        	<input id="uploadback" class="btn" type="button" value="上传文件"> 
			                            	<img class="background" :src="image ? image : '${ctx}/images/static/defaultImg.120.jpg'" />
			                       			请上传<span v-if="pageId==7001&&positionId==8002">116*235</span><span v-if="pageId==7001&&positionId==8001">1920*90</span><span v-if="pageId==7002">1920*540</span><span v-if="pageId==7003">265*105</span>尺寸图片, 支持jpg、jpeg、png、gif，文件大小不超过5MB
				                       	 </div> 
				                        </div> 
				                    </div>
               					 </div> 
               					 <!-- 首页7001的侧边栏8002不需要背景色，商品详情页7003不需要背景色 -->
               					 <div class="form-group " v-if="!((positionId==8002&&pageId==7001)||pageId==7003)">
	                               <div class="col-md-11">
	                                   <label class="col-sm-2 control-label"><span class="text-red">*</span>广告背景色：</label>
	                                   <div class="col-sm-4">
	                                     <input type="text" id="background" name="background" class="form-control ui-autocomplete-input" v-model="background">
	                                   </div>
	                              </div>
	                            </div>
               					<div class="form-group ">
	                               <div class="col-md-11">
	                                   <label class="col-sm-2 control-label"><span v-if="!(pageId==7002||(pageId==7001&&positionId==8002))" class="text-red">*</span>广告链接：</label>
	                                   <div class="col-sm-4">
	                                     <input type="text" id="url" name="url" class="form-control ui-autocomplete-input" v-model="linkUrl" @change="linkUrlFormat">
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
<script type="text/javascript" src="${ctx}/js/app/advertisementedit.js"></script>  
</body>
</html>