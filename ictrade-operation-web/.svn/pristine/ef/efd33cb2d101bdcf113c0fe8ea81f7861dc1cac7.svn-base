<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="zh-CN">
<head> 
<title>分类</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head> 
<style>
.btn-default{
	position: relative;
	width: 120px;
	margin: 0 10px;
}

.btn-default input{
	width: 120px;
	opacity: 0;
	position: absolute;
	top: 0;
	left: 0;
}
.col-sm-5 img{
	width: 120px;
}
</style>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="categoryEdit">
		<section class="content-header">
		  <h1>
		    分类
		    <small v-if="categoryId!=''">编辑</small>
		    <small v-else>添加</small> 
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
		    <li class="active">category</li>
		  </ol>
		</section>   
	    
    <section class="content" style=" color:#666;">
      <form class="form-horizontal" name="manu-from" id="manu-from" onsubmit="return false;">    
          <div class="row">
              <section class="col-lg-12">
                <div class="box box-danger ">
                    <!-- Morris chart - Sales -->
                      <div class="box-header with-border">
                        <h3 class="box-title"><span v-if="categoryId!=''">编辑</span><span v-else>添加</span>分类</h3>
                      </div>
    
                      <div class="box-body ">  
                              <div class="form-group ">
                                  <div class="col-md-12">
                                      <label for="sequenceNum" class="col-sm-3 control-label">序号：<span class="text-red">*</span></label>
                                      <div class="col-sm-5" > 
                                          <input type="text" class="form-control" name="sequenceNum" id="sequenceNum"  v-model.trim="sequenceNum" required >
                                      </div>
                                  </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-12">
                                      <label for="categoryId" class="col-sm-3 control-label">分类ID：<span class="text-red">*</span></label>
                                      <div class="col-sm-5" > 
                                          <input type="text" class="form-control" disabled name="categoryId" id="categoryId"  v-model.trim="categoryId" required >
                                      </div>
                                  </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-12">
                                      <label for="categoryTypeId" class="col-sm-3 control-label">分类类型ID：<span class="text-red">*</span></label>
                                      <div class="col-sm-5">
                                          <input type="text" class="form-control" name="categoryTypeId" id="categoryTypeId"  v-model.trim="categoryTypeId" required >
                                      </div>
                                  </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-12">
                                      <label for="categoryName" class="col-sm-3 control-label">分类名称：<span class="text-red">*</span></label>
                                      <div class="col-sm-5">
                                          <input type="text" class="form-control" name="categoryName" id="categoryName"  v-model.trim="categoryName" required >
                                      </div>
                                  </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-12">
                                      <label class="col-sm-3 control-label">大图：<span class="text-red">*</span></label>
                                      <div class="col-sm-5">
										  <img alt="" :src="categoryImageUrlBig !== ''?categoryImageUrlBig:'../images/defaultImg01.jpg'">
                                          <span class="btn btn-default">
												上传文件
											 <input type="file" id="uploadBtnBig" name="categoryImageUrlBig"/>
										  </span>
                                      </div>
                                  </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-12">
                                      <label class="col-sm-3 control-label">小图：<span class="text-red">*</span></label>
                                      <div class="col-sm-5">
                                       	  <img alt="" :src="categoryImageUrlSmall !== ''? categoryImageUrlSmall:'../images/defaultImg01.jpg'">
                                       	  
                                		  <span class="btn btn-default">
												上传文件
											 <input type="file" id="uploadBtnSmall" name="categoryImageUrlSmall"/>
										  </span>
                                      </div>
                                  </div>
                              </div>
                      </div>
    
                      <div class="box-footer text-center">
                          <button type="submit" class="btn btn-danger c-btn">保存</button>
                          <button type="button" class="btn btn-concle c-btn" >取消</button>
                      </div>
                </div>
              </section>
          </div>
      </form>
    </section>
    
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" /> 
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/app/categoryEdit.js"></script>
</body>
</html>