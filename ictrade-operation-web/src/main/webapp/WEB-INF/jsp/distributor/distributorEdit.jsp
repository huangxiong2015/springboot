<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>分销商管理</title>    
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
<link rel="stylesheet" href="${ctx}/css/lemon/custom.css"/>
<link rel="stylesheet" href="${ctx}/css/app/distributor.css"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 

  <div class="content-wrapper" id="distributor-edit">
	<section class="content-header">
	  <h1>
	 分销商 
	  		<small v-if="vendorId==''">添加</small> 
		    <small v-else>编辑</small> 
	  </h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active"><a  :href="newsHome">分销商</a></li>
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
		    		<span v-if="vendorId==''">添加  -</span>
                  	<span v-else>编辑  -</span>
                  	<span>分销商</span>
                  </h3>
                </div>
				<input type="hidden" id="newsId" name="newsId" :value="initData.newsId">
                <div class="box-body ">
				 <div class="form-group">
                    <div class="col-md-11">
                        <label for="specurl" class="col-sm-2 control-label">选择分销商<span class="text-red">*</span>：</label>
                        <div class="col-sm-7" > 
                      		<a v-if="initData.newsId == ''" href="javascript:void(0);" class="file-btn" @click="showVendorModel">选择</a>
                        	<div v-show="initData.newsId != ''">
		                       	<div class="manu_logo">
	                            	 <div class="img-box">
	                            	 	<img :src="initData.attachUrl ? initData.attachUrl : '${ctx}/images/defaultImg01.jpg'"  class="d-logo"/>
		                        		<input class="file-btn" type="button" value="上传文件" id="uploadBtn" >  
	                            	 </div>
	                            	 <a href="javascript:void(0);" class="re-change"  @click="showVendorModel" v-if="!vendorId">重新选择</a>  
	                            </div> 
	                       		<div class="manu_text">建议上传120*44像素，支持jpg、jpeg、png、gif，文件大小不超过5MB</div>
                        		<input type="hidden" v-model="initData.attachUrl" id="attachUrl" name="attachUrl"> 
                        	</div>
                        </div>
                    </div>
                 </div>
                <div class="form-group" v-if="initData.newsId != ''">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">分销商名称<span class="text-red">*</span>：</label>
                        <div class="col-sm-5"> 
                            <input type="text" class="form-control" name="title" id="title" required v-model.trim="initData.title" maxlength="50"/>
                        </div>
                    </div>
                </div>
                 <div class="form-group">
                    <div class="col-md-11">
                       <label class="col-sm-2 control-label">活动图标<span class="text-red">*</span>：</label>
                       <div class="col-sm-5">
                       	<select name="content[labelContent]" class="form-control" id="labelContent" v-model="content.labelContent">
                       		<option v-for="(value,key) in labelType" :value="key" v-text="value"></option>
                       	</select>
                       </div>
                	</div>
                </div> 
                 <div class="form-group" v-if="content.labelContent == 'custom'">
                    <div class="col-md-11">
                       <label class="col-sm-2 control-label">自定义内容<span class="text-red">*</span>：</label>
                       <div class="col-sm-5">
						<input class="customContent form-control" type="text" name="content[customContent]" id="" placeholder="2-3个字符，支持中文、英文和数字及其组合" maxLength="3" v-model="content.customContent" required/>
                       </div>
                	</div>
                </div> 
                
                <div class="form-group" v-if="content.labelContent != 'nothing'">
	                <div class="col-md-11">
                        <label for="titleUrl" class="col-sm-2 control-label">标签链接：</label>
                        <div class="col-sm-5"> 
                            <input type="text" class="form-control" name="content[activityUrl]" id="titleUrl" v-model.trim="content.activityUrl" maxlength="100" />
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">排序位置<span class="text-red">*</span>：</label>
                        <div class="col-sm-5"> 
                            <input type="text" class="form-control number" name="orderSeq" id="orderSeq" required v-model.trim="initData.orderSeq" maxlength="9"  />
                        </div>
                    </div>
                </div>
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">分销商简介<span class="text-red">*</span>：</label>
                        <div class="col-sm-5">
                        	 <textarea class="form-control" name="content[profile]" id="profile" required v-model.trim="content.profile" maxlength="75" rows="3" ></textarea>
                            <div class="pull-right"><span class="in-num" v-text="content.profile?content.profile.length:'0'"></span>/75</div>
                        </div>
                    </div>
                </div>
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">分销商详情<span class="text-red">*</span>：</label>
                        <div class="col-sm-8">
                        	<div id="desc" class="ueditor_wrapper"> 
							    <iframe style="width:100%;border: none;min-height:546px;" src="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" scrolling="no" frameborder="0"  allowTransparency="true">
							    </iframe>
							</div>
                        </div>
                    </div>
                </div>
                 <div class="form-group ">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">优势代理线：</label>
                        <div class="col-sm-5">
                        	<a class="s-a-btn" href="javascript:;" @click="showAgent = true;showModal = true;modalTitle = '请选择代理线';">选择代理线</a>
                        	<span>（最多选择20项）</span>
                        	<div v-if="activeAgent.length">
                        		<p>已选中：</p>
                        		<div class="lemon-area">
	                        		<div class="lemon-tag" v-for="(ele,index) in activeAgent">
		                        		<span class="lemon-tag-text" v-text="ele.brandName"></span> 
		                        		<i class="fa fa-close" @click="deleteAgentSelect(index,ele.id)"></i>
	                        		</div>
                        		</div>
                        	</div>
                        	<input type="hidden" name="content[agents]">
                        </div>
                    </div>
                </div>
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">优势产品类别：</label>
                        <div class="col-sm-5">
                        	<a class="s-a-btn" href="javascript:;" @click="showCate = true;showModal = true;modalTitle = '请选择类别';">选择类别</a>
                        	<span>（最多选择10项大类）</span>
                        	<div v-if="activeCate.length">
                        		<p>已选中：</p>
                        		<div class="lemon-area">
	                        		<div class="lemon-tag" v-for="(ele,index) in activeCate">
		                        		<span class="lemon-tag-text" v-text="ele.name"></span> 
		                        		<i class="fa fa-close" @click="deleteCateSelect(index,ele.id)"></i>
	                        		</div>
                        		</div>
                        	</div>
                        	<input type="hidden" name="content[cate]">
                        </div>
                    </div>
                </div>
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">热销型号：</label>
                        <div class="col-sm-5 hot-sale-list">
                        	<div>
                        		<span><span class="text-red">*</span>支持添加5个型号</span>
                        		<a class="float-r poin" @click="showTopHotList" v-if="initData.newsId">选择热销型号</a>
                        	</div>
                        	<rowspan-table
                       		 	v-if="hotList.refresh"
                        		:columns="hotList.columns"
                        		:init-datas="hotList.datas"
                        		:default-tip="hotList.defaultTip"
                        	></rowspan-table>
                        	<div>
                        		<a class="poin" v-if="hotList.datas.length<5" @click="addProduct('hotList')">新增热销型号</a>
                        	</div>
                        </div>
                    </div>
                </div>
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">最新型号：</label>
                        <div class="col-sm-5 hot-sale-list">
                       		<div>
                        		<span><span class="text-red">*</span>支持添加5个型号</span>
                        		<a class="float-r poin" @click="showTopLatestList" v-if="initData.newsId">选择最新型号</a>
                        	</div>
                        	<rowspan-table
                        		v-if="newestList.refresh"
                        		:columns="newestList.columns"
                        		:init-datas="newestList.datas"
                        		:default-tip="newestList.defaultTip"
                        	></rowspan-table>
                        	<div>
	                       		<a class="poin" v-if="newestList.datas.length<5" @click="addProduct('newestList')">新增最新型号</a>
	                       	</div>
                        </div>
                       	
                    </div>
                </div>  
                
				<input type="hidden" id="categoryTypeId" name="categoryTypeId" v-model="initData.categoryTypeId"/>
                <div class="box-footer text-center">
                    <button type="button" class="btn btn-danger c-btn" onclick="requireSaveData()">保存</button>
                    <button type="button" class="btn btn-cancel c-btn" @click="cancel">取消</button>
                </div>
                <modal v-if="showModal"
               		   @close="toggleModal"
			           @click-ok="modalOk"
			           @click-cancel="toggleModal"
			           :modal-style="modalStyle"
			           :show-foot="false"
                >
                	<h3 slot="header" v-text="modalTitle">请选择分销商</h3>
                	<div v-if="showVendor" class="vendor-list">
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
						    	 :showSearch="vendorSelect.showSearch"
    							 :show-letter="vendorSelect.showLetter"				    		 
							     ref="vendorLetter"
						 	></letter-select>
                	</div>
                	<div v-if="showTopHot" class="top-hot-wrap">
                		<rowspan-table
                       		:columns="topHot.columns"
                       		:init-datas="topHot.datas"
                       		:default-tip="topHot.defaultTip"
                       		:checkflag="topHot.checkflag"
                       		:selected-item="topHot.selectedItem"
                       		ref="topHotList"
                       	></rowspan-table>
                       	<div class="box-footer text-center" v-if="topHot.datas.length">
	                       	<button type="button" class="btn btn-danger c-btn" @click="getActiveHotPrd">确认</button> 
	                       	<button type="button" class="btn btn-cancle c-btn" @click="showModal = false;showFoot = true;showTopHot = false;">取消</button>
                       	</div>
                	</div>
                	<!-- 选择最新型号 -->
                	<div v-if="showTopLatest" class="top-latest-wrap">
                		<rowspan-table
                       		:columns="topLatest.columns"
                       		:init-datas="topLatest.datas"
                       		:default-tip="topLatest.defaultTip"
                       		:checkflag="topLatest.checkflag"
                       		:selected-item="topLatest.selectedItem"
                       		ref="topLatestList"
                       	></rowspan-table>
                       	<div class="box-footer text-center" v-if="topLatest.datas.length">
	                       	<button type="button" class="btn btn-danger c-btn" @click="getActiveLatestPrd">确认</button> 
	                       	<button type="button" class="btn btn-cancle c-btn" @click="showModal = false;showFoot = true;showTopLatest = false;">取消</button>
                       	</div>
                	</div>
                	<!-- 选择最新型号 -->
                	<div v-if="showPrdAssociate">
                		<product-associate
                       		:api="productBasicApi"
                       		:request-type="productRequestType"
                       		:product-data="editPrdData"
                       		:is-edit="isEdit"
                       		:is-add="isAdd"
                       		:is-read-only="isReadOnly"
                       		:image-rec-text ="imageRecText"
                       		:prd-type="prdType"
                       		@cancel="prdAssociateCancel"
                       		@get-product-info="getProductInfo"
                       	></product-associate>
                	</div>
                	<div v-if="showAgent">
                		<agent-select
                			:agent-select="agentSelect"
                			:max-len="agentMaxLen"
                			 @get-selecteds="getAgentSelected"
                			 @delete-select="deleteAgentSelect"
                			 @cancel="agentSelectCancel"
                		></agent-select>
				 	</div>
				 	<div v-if="showCate">
						 	<lemmon-tab
						 		:api="cateSelect.api"
					 			:init-data="cateSelect.initData"
				 				:id="cateSelect.id"
			 					:name="cateSelect.name"
	 							:children="cateSelect.children"
	 							@get-data="getTempCateSelected"
		 						ref="cateSelect"
						 	></lemmon-tab>
						 	<div class="box-footer text-center">
		                       	<button type="button" class="btn btn-danger c-btn" @click="getCateSelected">确认</button> 
		                       	<button type="button" class="btn btn-cancle c-btn" @click="cateSelectCancel">取消</button>
	                       	</div>
					 	</div>
                </modal>
          </div>
          </section>
          </div>
	</form>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
 

</div>	 	  
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-checkbox.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-common.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-multi-select.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-modal.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-category-tab.js"></script>  
<script type="text/javascript" src="${ctx}/js/app/distrManufAssem.js"></script>
<script type="text/javascript" src="${ctx}/js/app/distributorEdit.js"></script>
<script type="text/javascript" src="${ctx}/js/common/add.validate.js"></script>
</body>
</html>