<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">
<head>
<title>账期管理</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/bootstrapValidator/css/bootstrapValidator.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/common/base.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseEdit.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseEditSon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseAccountPeriod.css" />
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper">
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="son">
	<section class="content-header">
	  <a href="javascript:;" class="header_tab" @click="clickBasicInfo">基本信息</a>
	  <a href="javascript:;" class="header_tab" @click="clickSonAccount">子账号管理</a>
	  <a href="javascript:;" class="header_tab pitch_tab">账期管理</a>
	</section>
	<input type="hidden" value="${userId}" id="applyUserId">
	<input type="hidden" value="${userName}" id="applyUserName">
	<section class="content">
		<h5 class="title">{{name}}<em>{{partyCode ? partyCode : '-'}}</em></h5>
		<div id="edit" class="row form-wrap">
			<form class="form-horizontal infoFrom">
				<label :class="{'verify-status':true,danger:status !== 'APPROVED',success: status === 'APPROVED'}">
					<template v-if="status=='WAIT_APPROVE'">待审核</template>
					<template v-else-if="status=='APPROVED'">审核通过</template>
					<template v-else-if="status=='REJECT'">驳回</template>
					<template v-else>未申请</template>
				</label>
				<div class="col-sm-11">
					<div class="form-group">
				      <label class="col-sm-2 col-lg-2 control-label text"><span class="cRed">*</span>结算币种：</label>
				      <div class="col-sm-10 col-lg-6 text top6">
				      	  <span v-if="currency == 'USD'">美金</span>	
				      	  <span v-if="currency == 'CNY'">人民币（含17%增值税）</span>	
				      </div>
				    </div>
				    <div class="clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>授信额度：</label>
					      <div class="col-sm-8 col-lg-8 text"><span>{{creditQuota | showDefault}}</span> <span v-if="currency=='CNY'">RMB</span><span v-if="currency=='USD'">USD</span></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>授信期限：</label>
					      <div class="col-sm-8 col-lg-8 text"><span class="mr5">月结</span>{{creditDeadline | showDefault}} 天</div>
					    </div>
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>对账日期：</label>
					      <div class="col-sm-8 col-lg-8 text">{{ checkDate | showDefault }} 日</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>对账周期：</label>
					      <div class="col-sm-8 col-lg-8 text">{{ checkCycle | showDefault }} 天</div>
					    </div>
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>付款日期：</label>
					      <div class="col-sm-8 col-lg-8 text">{{ payDate | showDefault }} 日</div>
					    </div>
				    </div>
				    <div class="form-group">
				      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>申请说明：</label>
				      <div class="col-sm-11 col-lg-8">
				      	<div class="row">
				      		<div class="col-sm-10 col-lg-9 top6">
				      			{{ common | showDefault }}
				      		</div>
				      	</div>
	                  </div>
				    </div>
				    <div class="form-group mt30">
				      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>附件：</label>
			      	  <div class="col-sm-10 lh25">
			      	  	<ul>
			      	  		<li v-for="(item, index) in creditAttachmentList" class="fileItem mr14"><a :href="getImage(item.attachmentUrl)">{{ item.attachmentName }}</a></li>
			      	  	</ul>
			      	  </div>
				    </div>
				    <h5 class="log"> </h5>
					<div class="form-group">
				      <label class="col-sm-2 col-lg-2 control-label">申请记录：</label>
				      <div class="col-sm-10 col-lg-10">
				      	<ul v-if="applyRecord.length > 0" class="log-list">
				      		<li v-for="item in applyRecord">{{item.date}}<span class="ml30">{{item.resource | toJson('mail')}}</span><span class="ml30">{{item.resource | toJson('common')}}</span></li>
				      	</ul>
				      	<div v-if="applyRecord.length == 0" class="lh30">
				      		暂无申请记录
				      	</div>
				      </div>
				    </div>
				</div>
		    </form>
		     <div class="btns col-sm-10 col-lg-10 col-sm-offset-1 col-lg-offset-1">
		    	<button type="button" class="btn btn-default cancel" @click="cancel">返回</button>
		    </div>
		</div>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/bootstrapValidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${ctx}/js/enterprise/cerEnterpriseDetailAccountPeriod.js"></script>
</body>
</html>