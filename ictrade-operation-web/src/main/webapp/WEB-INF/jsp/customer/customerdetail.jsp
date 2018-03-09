<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">
<head>
<title>个人账户详情</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/bootstrapValidator/css/bootstrapValidator.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/common/base.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/common/component.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseList.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseEdit.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/personUpEnterprise.css" />
<style>
.content-wrapper .content-header{
	padding-top:15px;
}
.main-header .sidebar-toggle:before{
	top: -6px;
}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper">
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper per-up-ent" id="edit">
	<section class="content-header">
		<h1>个人信息详情</h1> 
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li> 
			<li class="active">个人用户</li>
		</ol>
	</section>
	<section class="content">		
		<div id="detail" class="row form-wrap">
			<form class="form-horizontal infoFrom">							    
			    <div class="module-title">联系人信息</div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">个人编码：</label>
			      <div class="col-sm-6 col-lg-6 text" v-text="id"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">联系人姓名：</label>
			      <div class="col-sm-6 col-lg-6 text" v-text="info.name?info.name:'-'"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">手机号码：</label>
			      <div class="col-sm-6 col-lg-6 text" v-text="info.telNumber?info.telNumber:'-'"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">邮箱：</label>
			      <div class="col-sm-6 col-lg-6 text" v-text="info.mail?info.mail:'-'"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">固定电话：</label>
			      <div class="col-sm-6 col-lg-6 text" v-text="info.fixedTel?info.fixedTel:'-'"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">联系QQ：</label>
			      <div class="col-sm-6 col-lg-6 text" v-text="info.qq?info.qq:'-'"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">职位：</label>
			      <div class="col-sm-6 col-lg-6 text" v-text="info.personalTitle?positionList[info.personalTitle]:'-'"></div>
			    </div>			    
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">联系地址：</label>
			      <div class="col-sm-6 col-lg-6 text" v-text="showAddress()"></div>
			    </div>
			</form>
		</div>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/app/customerDetail.js"></script>
</body>
</html>