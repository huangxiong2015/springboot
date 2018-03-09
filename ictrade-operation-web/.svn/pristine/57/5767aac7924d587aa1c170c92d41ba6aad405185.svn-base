<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<!--[if IE 8 ]>    <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="zh-CN"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->

<head>
	<!-- jQuery 1.11.3 -->
<script src="${ctx}/js/lib/jquery-1.11.3.min.js"></script>
 <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <link rel="stylesheet" href="${ctx}/js/lib/zTree/zTreeStyle.css"/>
  <link rel="stylesheet" href="${ctx}/css/app/userList.css"/>
  <title>分类维护</title>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>

	  <!-- 右边内容显示 -->
	  <div class="content-wrapper">
	  <section class="content-header">
		  <h1>分类维护</h1>
		</section> 
		<section class="content s-help-maintain" id="maintainList">
			<div class="tree_area">
				<p class="module-tlt">产品分类视图</p>
				<div>
				   <ul id="treeDemo" class="ztree"></ul>
				</div>
			</div>
			<div class="h_content_area">
				<p class="module-tlt">维护产品分类</p>
				<div style="margin:20px;">
					<p class="mb20 f16">
						<span v-for="(ele,index) in pathList" v-text="index == 0 ? ele.cateName : '>' + ele.cateName"></span>
						<a href="javascript:;" class="btn btn-sm btn-danger pull-right" @click="addCategory"><i class="fa fa-plus"></i> 新增</a>
					</p>
					<div class="box-body">
						<div class="chart box-body" style="position: relative;" v-if="queryParams.id !== ''">
			              <!--表格组件-->
			               <rowspan-table
			                      :columns="gridColumns"
			                      :pageflag="pageflag"
			                      :query-params="queryParams"
			                      :api="url"
			                      :refresh="refresh"  
			              >
			              </rowspan-table>
			            </div>
		    			
		    			<div class="new_save" id="editCategory"  >
		    			 <p class="module-tlt"><span v-if="newContentBtn">编辑</span><span v-if="!newContentBtn">新增</span>分类</p>
		    			 <div class="form-group">
		    			 	<label for="menuName" class="col-sm-4 col-md-4 col-lg-2 control-label">分类名称：</label>
		    			 	<div class="col-sm-7 col-md-8 col-lg-9 checkVal">
		    			 		<input maxlength="50" type="text" id="cateName" style="width:200px;height:25px;line-height:25px;" v-model="categoryName">
		    			 		<label class="form-label"><input type="checkbox" value="red" name="isShowaColor" v-model="fontColor" />
									  			标红</label>
		    			 	</div>
		    			 </div>
			    			 
		    			 <file-upload
					            :keyname="keyname"
					            :file-id= "key"
					            :file-name= "name"
					            :button-id= "config.buttonId"
					            :upload-type= "config.uploadType"
					            :url= "config.url"
					            :types= "config.types"
					            :file-size= "config.fileSize"
					            :is-image= "isImage"
					            :validate="validate"
					            :default-pic="defaultPic"
					            :class="inputClass"
					            :attach-file="attachFile"
					            :manu-text="manuText"
					            v-bind="attr"
					    ></file-upload>
					    
					    <!-- 是否显示 --> 
					    <div class="form-group">
	  						<div class="col-md-11">
	  							<label class="col-sm-2 control-label">是否显示：<span class="text-red">*</span></label>
	  							<div class="col-sm-9" id="showStyleChoose">
								  	<label class="check-label"><input type="radio" name="showStyle" value="1" id="isShow" v-model="status" /> 显示</label>  <!-- :disabled="couponCate !=='PLATFORM_PROMO'" -->
								  	<label class="check-label"><input type="radio" name="showStyle" value="0" id="noShow" v-model="status" /> 不显示</label> <!-- :disabled="couponCate !=='PLATFORM_PROMO'" -->
							  	</div>
	  						</div>
	  					 </div>
					    
    					<input type="hidden" v-model="cateId"> 
		    			 <p>
		    			 	<a  class="btn btn-danger c-btn w110" @click="saveData">保存</a>
		    			 	<a class="btn btn-default c-btn w110" @click="cancelEdit">取消</a>
		    			 </p>
		    			</div>
					</div>					
				</div>
			</div>				   
		</div>
		</section>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ---->
</div>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/lib/jquery.base64.js"></script>
<script src="//misc.ykystatic.com/webres/lib/plupload/2.1.2/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-upload.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/zTree/jquery.ztree.all.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/app/productCategory.js"></script>

</html>
