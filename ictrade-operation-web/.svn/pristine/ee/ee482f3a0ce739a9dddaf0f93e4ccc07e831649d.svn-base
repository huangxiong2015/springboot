<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!-- <!DOCTYPE> -->
<!DOCTYPE html>
<html lang="zh-CN">
<html>
<head>  
<title>推广位编辑</title>
<link rel="stylesheet" href="${ctx}/css/app/extensionEdit.css">
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<div class="content-wrapper" id="extension_edit">
		<section class="content-header">
		  <h1>
		    	推广位
		    <small class="edit_state" v-text="id?'编辑':'添加'"></small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
		    <li class="active">推广位编辑</li>
		  </ol>
		</section>
	    <section class="content" style=" color:#666;">
	      <form class="form-horizontal" name="manu-from" id="create" onsubmit="return false;" >    
	          <div class="row">
	              <section class="col-lg-12">
	                <div class="box box-danger ">
	                    <!-- Morris chart - Sales -->
	                      <div class="box-header with-border">
	                        <h3 class="box-title"><span class="edit_state" v-text="id?'编辑':'添加'"></span>&nbsp;-&nbsp;推广位</h3>
	                      </div>
	                      <input type="hidden" name="categoryTypeId" value="extension">
	                      <div class="box-body ">
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">类型：<span class="text-red">*</span></label>
	                                      <div class="col-sm-4" >
	                                      	  <select  class="form-control valid"  id="categoryId" name="categoryId" v-model="initData.categoryId">
	                                      	    <option value="4001">活动</option>
												<option value="4002">精选分销商</option>
												<option value="4003">制造商</option>
												
											</select>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group" v-show="initData.categoryId == '4002'">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">分销商：<span class="text-red">*</span></label>
	                                      <div class="col-sm-4 pdt7">
	                                          <a id="distributor" class="poi" v-text="content.currentId == '' ? '选择' : '重新选择'" @click="chooseVendor"></a>
	                                          <span v-if="content.currentId" v-text="content.facturerName"></span>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group" v-show="initData.categoryId == '4003'">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">制造商：<span class="text-red">*</span></label>
	                                      <div class="col-sm-4 pdt7">
	                                         <a id="selectBrand" class="poi" v-text="content.currentId == '' ? '选择' : '重新选择'" @click="chooseBrand">选择</a>
	                                         <span v-if="content.currentId" v-text="content.facturerName"></span>
	                                      </div>
	                                  </div>
	                              </div>
	                              <input type="hidden" name="content[currentId]" v-model="content.currentId">
	                              <input type="hidden" name="content[facturerName]" v-model="content.facturerName">
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">推广位置：<span class="text-red">*</span></label>
	                                      <div class="col-sm-4" >
	                                      	  <select class="form-control" id="orderSeq" name="orderSeq" v-model="initData.orderSeq">
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>
												<option value="6">6</option>
												<option value="7">7</option>
												<option value="8">8</option>
												<option value="9">9</option>									
											</select>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">推广描述：<span class="text-red">*</span></label>
	                                     <div class="col-sm-4" >
	                                          <input type="text" id="desc" name="desc" class="form-control" maxlength="50" v-model="initData.desc" required>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group" v-show="initData.categoryId == '4001'">
	                                   <div class="col-md-11">
	                                     <label for="createData" class="col-sm-2 control-label">投放时间：<span class="text-red">*</span></label> 
	                                      <div class="col-sm-4" >
	                                        <div id="createData" >
		                                        <div id="createDataRange" class="input-daterange input-group">
			                                         <input  type="text" name="startDate" id="startDate" class="form-control" v-model="initData.startDate">
			                                         <span class="input-group-addon">至</span> 
			                                         <input  type="text" name="expiryDate" id="expiryDate" class="form-control" v-model="initData.expiryDate">
		                                         </div>
	                                         </div>
	                                      </div>
	                                  </div>
						         </div>
	                         	 <div class="form-group">
				                    <div class="col-md-11">
				                       <label for="specurl" class="col-sm-2 control-label"><span v-text="initData.categoryId =='4001' ? '背景图：' : '推广logo：'"></span><span class="text-red">*</span></label>
				                        <div class="col-sm-7"  > 
				                       	 <div class="logo-box">
				                        	<input class="btn" type="button" value="上传文件" id="uploadback" > 
				                            	<img alt="" :src="content.imageUrl ? content.imageUrl : '${ctx}/images/static/defaultImg.120.jpg'" />
				                       			<i>请上传<span v-text="initData.categoryId =='4001' ? '290*160' : '190*60'"></span>尺寸图片, 支持jpg、jpeg、png、gif，文件大小不超过500KB </i>
				                       	 </div>
			                        		<input type="hidden" v-model="content.imageUrl" id="imageUrl" name="content[imageUrl]" required>
			                        		<input type="hidden" v-model="content.imageUrl" id="imageUrlLogo" name="content[imageUrlLogo]" required> 
				                        </div> 
				                    </div>
               					 </div>
               					  <div class="form-group" v-show="initData.categoryId !='4001'">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label"></label>
	                                     <div class="col-sm-4" >
	                                          <input type="input" id="character" placeholder="+添加描述" name="content[character]" class="form-control" maxlength="30" v-model="content.character" required> <span class="character_tip">*30个字符以内</span>
	                                      </div>
	                                  </div>
	                              </div>
               					 <div class="form-group" v-show="initData.categoryId !='4001'">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label " >详情链接：<span class="text-red">*</span></label>
	                                      <div class="col-sm-4" >
	                                          <input type="text" id="linkDetails" name="content[linkDetails]" class="form-control" v-model="content.linkDetails" required>
	                                      </div>
	                                  </div>
	                             </div>
	                               <div class="form-group" v-show="initData.categoryId =='4001'">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">活动链接</label>
	                                     <div class="col-sm-4" >
	                                          <input type="text" id="activityLink" name="content[activityLink]" class="form-control" maxlength="100" v-model="content.activityLink">
	                                      </div>
	                                  </div>
	                              </div>
	                           <!-- 精选分销商上传图片 -->
	                          	<div class="form-group" v-if="initData.categoryId == '4002'">
				                    <div class="col-md-11">
				                       <label class="col-sm-2 control-label">标签内容：<span class="text-red">*</span></label>
				                       <div class="col-sm-4">
				                       	<select name="content[labelContent]" class="form-control" v-model="content.labelContent">
				                       		<option value="none">无</option>
				                       		<option value="promotion">促销</option>
				                       		<option value="fullCut">满减</option>
				                       		<option value="fullDelivery">满送</option>
				                       		<option value="seckill">秒杀</option>
				                       		<option value="flashPurchase">闪购</option>
				                       		<option value="custom">自定义</option>
				                       	</select>
				                       </div>
				                	</div>
				                </div> 
				                 <div class="form-group" v-show="content.labelContent == 'custom' && initData.categoryId == '4002'">
				                    <div class="col-md-11">
				                       <label class="col-sm-2 control-label">自定义内容：<span class="text-red">*</span></label>
				                       <div class="col-sm-4">
										<input class="customContent form-control" type="text" name="content[customContent]" id="customContent" placeholder="2-3个字符，支持中文、英文和数字及其组合" maxLength="3" v-model="content.customContent" required/>
				                       </div>
				                	</div>
				                </div> 
				                
				                <div class="form-group" v-show="content.labelContent != 'none' && initData.categoryId == '4002'">
					                <div class="col-md-11">
				                        <label for="titleUrl" class="col-sm-2 control-label">标签链接：</label>
				                        <div class="col-sm-4"> 
				                            <input type="text" class="form-control" name="content[activityUrl]" id="activityUrl" v-model.trim="content.activityUrl" maxlength="100" />
				                        </div>
				                    </div>
				                </div>
	                            <!-- 精选分销商上传图片 -->
	                      </div>
	                      <div style="margin-left: 300px;margin-bottom: 30px;">
				              <a id="save_btn" class="condition_inquery" href="javascript:;" @click="saveData">保 存</a>
				              <a id="cancel_btn" class="return_btn" href="javascript:;">取 消</a>
				          </div>
	                </div>
	              </section>
	          </div>
	          <modal v-if="showModal"
               		   @close="toggleModal"
			           :modal-style="modalStyle"
			           :show-foot="false"
                	>
                	<h3 slot="header" v-text="modalTitle">请选择分销商</h3>
                	<div v-if="showVendor">
                		<letter-select
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
                		></letter-select>
				 	</div>
				 	<div v-if="showBrand">
                		<letter-select
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
                		></letter-select>
				 	</div>
                </modal>
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
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script> 
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-modal.js"></script> 
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>  
<script type="text/javascript" src="${ctx}/js/lib/pinyin.js"></script>  
<script type="text/javascript" src="${ctx}/js/app/extensionedit.js"></script>  
</body>
</html>