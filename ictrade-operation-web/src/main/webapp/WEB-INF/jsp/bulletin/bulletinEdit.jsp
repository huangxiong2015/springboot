<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>公告资讯管理</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" href="${ctx}/css/app/bulletin.css">
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="bulletin-edit">
	<section class="content-header">
	  <h1>
	 公告/资讯 
	  		<small v-if="newsId!=''">编辑</small>
		    <small v-else>添加</small> 
	  </h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active"><a  :href="newsHome">公告资讯</a></li>
	  </ol>
	</section> 
    <section class="content" style=" color:#666;">
    <form class="form-horizontal" name="create" id="create" onsubmit="return false;">    
    <div class="row">
        <section class="col-lg-12">
          <div class="box box-danger ">
              <!-- Morris chart - Sales -->
                <div class="box-header with-border">
                  <h3 class="box-title">
                  	<span v-if="newsId!=''">编辑  -</span>
		    		<span v-else>添加  -</span>
                  	<span v-if="initData.categoryTypeId=='INFORMATION'">新闻资讯</span>
                  	<span v-if="initData.categoryTypeId=='MEDIAREPORT'">媒体报道</span>
	 				<span v-if="initData.categoryTypeId=='NOTICE'">官方公告</span>
	 				<!-- <span v-if="initData.categoryTypeId=='VENDORAFFAIRS'">供应商动态</span>   -->
                  </h3>
                </div>
				<input type="hidden" id="newsId" name="newsId" :value="initData.newsId">
                <div class="box-body "> 
				 <div class="form-group ">
                    <div class="col-md-11">
                        <label for="categoryTypeId" class="col-sm-2 control-label">类型<span class="text-red">*</span></label>
                        <div class="col-sm-4">  
		                    <select class="form-control" id="categoryTypeId"  name="categoryTypeId" v-model="initData.categoryTypeId"> 
							    <option value="NOTICE">官方公告</option>
							    <option value="INFORMATION">新闻资讯</option>
							    <option value="MEDIAREPORT">媒体报道</option>  
							    <option value="VENDORAFFAIRS">供应商动态</option>
							  </select>
                        </div>
                    </div>
                </div>  
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">标题<span class="text-red">*</span></label>
                        <div class="col-sm-4"> 
                            <input type="text" class="form-control" name="title" id="title" required v-model="initData.title" maxlength="60" onkeyup='this.value=this.value.replace(/^\s$/gi,"")' />
                        </div>
                    </div>
                </div>
                <!-- 4月13日不上线内容 -->
                <!-- <div class="form-group ">
                    <div class="col-md-11">
                        <label for="author" class="col-sm-2 control-label">发布人<span class="text-red">*</span></label>
                        <div class="col-sm-4"> 
                            <input type="text" class="form-control" name="author" id="author" required v-model="initData.author" maxlength="60" />
                        </div>
                    </div>
                </div> -->

                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="publishOrg" class="col-sm-2 control-label">发布机构<span class="text-red">*</span></label>
                        <div class="col-sm-4"> 
                            <input type="text" class="form-control" name="publishOrg" id="publishOrg" v-model="initData.publishOrg" required maxlength="30" onkeyup='this.value=this.value.replace(/^\s$/gi,"")' />
                        </div>
                    </div>
                </div>

                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="properties" class="col-sm-2 control-label">属性设置 </label>
                        <div class="col-sm-6 checkVal" >
                            <div class="checkbox inline">
                                <span class="check-span">
                                    <input type="checkbox" id="isTop" name="isTop"  :value="initData.isTop" v-model="checkModel.isTop" class="input_check" > <label for="isTop"></label> 文章置顶
                                </span>
                            </div>
                            <div class="checkbox inline" v-if="initData.categoryTypeId=='NOTICE' || initData.categoryTypeId=='MEDIAREPORT'">
                                <span class="check-span">{{initData.isTitleRed}}
                                    <input type="checkbox" id="isTitleRed" name="isTitleRed" :value="initData.isTitleRed" v-model="checkModel.isTitleRed" class="input_check"> <label for="isTitleRed"></label> 顶部栏展示
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
				<div class="form-group">
                    <div class="col-md-11">
                        <label for="specurl" class="col-sm-2 control-label">封面：<span v-if="initData.categoryTypeId != 'NOTICE' && initData.categoryTypeId != 'VENDORAFFAIRS'" class="text-red">*</span></label>
                        <div class="col-sm-7" > 
	                       	<div class="manu_logo"> 
		                       	<input class="file-btn pos-abs" type="button" value="上传文件" id="uploadBtn">    
	                            <span>
	                            	<img alt="" :src="initData.attachUrl ? initData.attachUrl : '${ctx}/images/defaultImg01.jpg'" width="120" height ="120"  class="d-logo" /> 
	                       			<a v-show="initData.attachUrl !=''" href="javascript:void(0);" class="del_logo" > 删除 </a> 
	                       		</span>
	                        </div>
	                       	<div class="manu_text">支持jpg、jpeg、png、gif，文件大小不超过5MB</div>
                        	<input v-if="initData.categoryTypeId != 'NOTICE' && initData.categoryTypeId != 'VENDORAFFAIRS'" type="text" v-model="initData.attachUrl" id="attachUrl" name="attachUrl"  style="visibility: hidden;height:0;" required>
                        	<input v-if="initData.categoryTypeId == 'NOTICE' || initData.categoryTypeId == 'VENDORAFFAIRS'" type="text" v-model="initData.attachUrl" id="attachUrl" name="attachUrl"  style="visibility: hidden;height:0;" >
                        </div> 
                    </div>
                </div>
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="publishOrg" class="col-sm-2 control-label">所属分销商</label>
                        <div class="col-sm-4 letter-select-wrap">
                        	<a class="s-a-btn" href="javascript:;" @click="choosedistributor">选择</a>
                        	<!-- 供应商 -->
							<div class="lemon-area">
	                        		<div class="lemon-tag" v-for="(ele,index) in activeVendor">
		                        		<span class="lemon-tag-text" v-text="ele.name"></span> 
		                        		<i class="fa fa-close" @click="deleteVendorSelect(index,ele.id)"></i>
	                        		</div>
                        		</div>
							
							<!-- 供应商 -->                          
                        </div>
                    </div>
                </div>
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="publishOrg" class="col-sm-2 control-label">所属制造商</label>
                        <div class="col-sm-4 letter-select-wrap">
                        	<!-- 制造商 -->
                            <a class="s-a-btn" href="javascript:;" @click="chooseManufacturer">选择</a>
                        	<!-- 供应商 -->
							<div class="lemon-area">
	                        		<div class="lemon-tag" v-for="(ele,index) in activeBrand">
		                        		<span class="lemon-tag-text" v-text="ele.brandName"></span> 
		                        		<i class="fa fa-close" @click="deleteBrandSelect(index,ele.id)"></i>
	                        		</div>
                        		</div>
                            <!-- 制造商 --> 
                        </div>
                    </div>
                </div>
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="desc" class="col-sm-2 control-label">详情</label>
                         <div class="col-sm-10" >
		          			<!-- 使用的时，将这个部分引入到页面中start -->
							<div id="desc" class="ueditor_wrapper"> 
							    <iframe style="width:100%;border: none;min-height:546px;" src="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" scrolling="no" frameborder="0"  allowTransparency="true">
							    </iframe>
							</div>
							<!-- 使用的时，将这个部分引入到页面中end --> 
						</div>
                    </div>
                </div>
                
                </div>
				<input type="hidden" id="iframeUrl_Id" value="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" />
                <div class="box-footer text-center">
                    <button type="button" class="btn btn-danger c-btn" onclick="requireSaveData();">保存</button>
                    <button type="button" class="btn btn-concle c-btn" >取消</button>
                </div>
          </div>
          <modal v-if="showModal"
               		   @close="toggleModal"
			           :modal-style="modalStyle"
			           :show-foot="false"
                >
                	<h3 slot="header" v-text="modalTitle">请选择分销商</h3>
                	<div v-if="showVendor">
                		<agent-select
                			:agent-select="vendorSelect"
                			 @get-selecteds="getVendorSelected"
                			 @delete-select="deleteVendorSelect"
                			 @cancel="toggleModal"
                		></agent-select>
				 	</div>
				 	<div v-if="showBrand">
                		<agent-select
                			:agent-select="brandSelect"
                			 @get-selecteds="getBrandSelected"
                			 @delete-select="deleteBrandSelect"
                			 @cancel="toggleModal"
                		></agent-select>
				 	</div>
                </modal>
          </section>
          </div>
	</form>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />

<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script> 
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-modal.js"></script>
<script type="text/javascript" src="${ctx}/js/app/distrManufAssem.js"></script>
<script type="text/javascript" src="${ctx}/js/app/bulletinEdit.js"></script>

</body>
</html>