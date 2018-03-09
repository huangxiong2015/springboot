<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>物料上传</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" href="${ctx }/js/lib/jquery-ui-1.20.0/jquery-ui.css">
<link rel="stylesheet" href="${ctx}/css/app/material.css">
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="help-edit">
	<section class="content-header">
	  <h1>添加物料-上传文件</h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active"><a href="${ctx}/basicMaterial/materialist.htm">物料管理</a></li>
	  </ol>
	</section> 
    <section id="upload" class="content" style=" color:#666;">
	    <div class="row">
	        <section class="col-lg-12 s-upload">
	          <div class="box box-danger ">
	                <div class="upload_area form-group">
						<span>上传文件：</span>					
						<div class="upload_btn" v-show="!isUploading && !isFinish">
							<span href="javascript:;" id="file">选择文件</span>
						</div>
						<span v-show="!isUploading && !isFinish">支持扩展名为xls、xlsx、csv的Excel文件 ，大小不能超过10MB</span>
						<div v-show="isUploading || isFinish" style="display:inline-block;">
							<span id="fileName"></span>
							<div class="progressBar" id="progressBar"></div>
							<span id="showPercent"></span>
						</div>
					</div>
					<div class="uploading-tip">
						<a href="${ctx}/template/BasicMaterial_template.xlsx" class="init_tips">点击下载标准Excel文件范本</a>					
					</div>
					<div class="uploading-tip" v-show="isUploading">正在上传文件，请勿刷新或关闭页面...</div>
					<div class="uploading-tip" v-show="isFinish">文件上传成功</div>
					<div>
						<a href="javascript:;" class="btn btn-sm btn-danger" id="saveBtn" @click="saveData" :disabled="!isFinish || isSave">确认保存</a>
					</div>
					<div class="operation_explain">
						<p>操作说明：</p>
						<p>1.当物料型号、制造商这2列内容完全相同时，此类物料默认为相同物料；</p>
						<p>2.在此上传库存文件将采用程序自动处理，文件内容严格按照标准格式，否则转换时可能产生错误；</p>
						<p>3.如果您已经上传了物料文件，请点击<a class="g_blue" href="${ctx }/basicMaterial/history.htm">查看上传文件的处理状态</a></p>
					</div>							  	  			   
	          </div>
	          </section>
	    </div>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/lib/jquery-ui-1.20.0/jquery-ui.js"></script>
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/app/upload.js"></script>
</body>
</html>