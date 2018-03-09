<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>诚聘英才管理</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="recuit-edit">
	<section class="content-header">
	  <h1>诚聘英才<small>添加</small></h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active">诚聘英才</li>
	  </ol>
	</section> 
    <section class="content" style=" color:#666;">
    <form class="form-horizontal" name="create" id="create" onsubmit="return false;">    
    <div class="row">
        <section class="col-lg-12">
          <div class="box box-danger ">
              <!-- Morris chart - Sales -->
                <div class="box-header with-border">
                  <h3 class="box-title">诚聘英才</h3>
                </div>
				<input type="hidden" id="newsId" name="newsId" :value="initData.newsId">
                <div class="box-body "> 
				 <div class="form-group ">
                    <div class="col-md-11">
                        <label for="categoryTypeId" class="col-sm-2 control-label">类型<span class="text-red">*</span></label>
                        <div class="col-sm-4">  
		                    <select-control
						        :id="typeId"
						        :name="typeName"
						        :api="selectUrl"
						        :option-id = "typeId"
						        :option-name = "typeOptionName"
						        :selected-value = "initData.categoryId"
						        :required="required"
						    ></select-control>
                        </div>
                    </div>
                </div>  
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">职位<span class="text-red">*</span></label>
                        <div class="col-sm-4"> 
                            <input type="text" class="form-control" name="title" id="title" required v-model="initData.title" maxlength="60" />
                        </div>
                    </div>
                </div>
                
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="regionCategoryId" class="col-sm-2 control-label">工作地点<span class="text-red">*</span></label>
                       <div class="col-sm-4">  
		                    <select-control
						        :id="cityId"
						        :name="cityName"
						        :api="selectRegionUrl"
						        :option-id = "placeId"
						        :option-name = "placeOptionName"
						        :selected-value = "initData.regionCategoryId"
						        :required="required"
						    ></select-control>
                        </div>
                    </div>
                </div>  
                
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="extend" class="col-sm-2 control-label">招聘人数</label>
                        <div class="col-sm-4"> 
                            <input type="text" class="form-control" name="extend" id="extend" v-model="initData.extend" maxlength="5"  onkeyup='this.value=this.value.replace(/^[0]+|\D/gi,"")'/>
                        </div>
                    </div>
                </div>
                
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="desc" class="col-sm-2 control-label">职责</label>
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
                    <button type="button" class="btn btn-danger c-btn" onclick="goBack();">取消</button>
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
<script type="text/javascript" src="${ctx}/js/app/recuitEdit.js"></script>

</body>
</html>