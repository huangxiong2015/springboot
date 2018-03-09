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
			<form id="infoFrom" class="form-horizontal infoFrom">
				<label :class="{'verify-status':true,danger:status !== 'APPROVED',success: status === 'APPROVED'}" b>
					<template v-if="status=='WAIT_APPROVE'">待审核</template>
					<template v-else-if="status=='APPROVED'">审核通过</template>
					<template v-else-if="status=='REJECT'">驳回</template>
					<template v-else>未申请</template>
				</label>
				<div class="col-sm-11">
					<div class="form-group">
				      <label class="col-sm-2 col-lg-2 control-label text"><span class="cRed">*</span>结算币种：</label>
				      <div class="col-sm-10 col-lg-6 text">
					      <label class="radio-inline"><input type="radio" name="currency" value="USD" v-model="currency"/>美金</label>
					      <label class="radio-inline"><input type="radio" name="currency" value="CNY" v-model="currency"/>人民币（含17%增值税）</label>
				      </div>
				    </div>
				    <div class="clearfix">
				    	<div class="tips" style="display:none;"></div>
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>授信额度：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" name="creditQuota" class="form-control" id="" placeholder="7位数字" v-model="creditQuota"><span v-if="currency=='CNY'">RMB</span><span v-if="currency=='USD'">USD</span></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>授信期限：</label>
					      <div class="col-sm-8 col-lg-8 text"><span class="mr5">月结</span><input name="creditDeadline" type="text" class="form-control" id="" placeholder="1-4个数字" v-model="creditDeadline">天</div>
					    </div>
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>对账日期：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" name="checkDate" class="form-control" id="" placeholder="1-2个数字" v-model="checkDate">日</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>对账周期：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" name="checkCycle" class="form-control" id="" placeholder="1-4个数字" v-model="checkCycle">天</div>
					    </div>
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>付款日期：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" name="payDate" class="form-control" id="" placeholder="1-2个数字" v-model="payDate">日</div>
					    </div>
				    </div>
				    <div class="form-group">
				      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>申请说明：</label>
				      <div class="col-sm-11 col-lg-8">
				      	<div class="row">
				      		<div class="col-sm-10 col-lg-9">
				      			<textarea class="form-control" name="common" rows="3" v-model="common" placeholder="中英文、数字及符号组成" @input="chCounter()"></textarea>
				      		</div>
				      		<div class="col-sm-2 col-lg-1 credit-memo">
				      			<span>{{ counter }}/100</span>
				      		</div>
				      	</div>
	                  </div>
				    </div>
				    <div class="form-group mt30">
				      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>附件：</label>
				      <div class="col-sm-10 col-lg-10">
				      	 <input id="addFile" :class="['btn', 'addFile', {'btn-success': creditAttachmentList.length<5}]" :disabled="creditAttachmentList.length==5" type="button" value="添加文件" />
				      	 <span class="file-tips">支持格式：.pdf  .xls  .xlsx  .rar .zip，最多支持5个文件，单个文件大小：10MB以内</span>
			      	  </div>
			      	  <div class="col-sm-10 col-sm-offset-2 mt20"><a v-for="(item, index) in creditAttachmentList" class="fileItem border mr14" @click="openLink(getImage(item.attachmentUrl))" href="javascript:;">{{ item.attachmentName }}<i class="icon-close-min close" @click.stop="removeItem(index)"></i></a></div>
				    </div>
				    <h5 class="log">申请客户信息</h5>
				    <div class="clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>姓名：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" name="applyUser" class="form-control" id="" placeholder="20位中英文字符" v-model="applyUser"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>联系方式：</label>
					      <div class="col-sm-8 col-lg-8 text"><span class="mr5"></span><input type="text" name="applyInformation" class="form-control" id="" placeholder="请输入联系方式" v-model="applyInformation" maxlength="17"></div>
					    </div>
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>邮箱：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" name="applyMail" class="form-control" id="" placeholder="正确的邮箱地址" v-model="applyMail" maxlength="50"></div>
					    </div>
				    </div>
				    <h5 class="log">申请记录</h5>
					<div class="form-group">
				      <label class="col-sm-2 col-lg-2 control-label">申请记录：</label>
				      <div class="col-sm-10 col-lg-10">
				      	<ul v-if="applyRecord.length > 0" class="log-list">
				      		<li v-for="item in applyRecord">{{item.date}}<span class="ml30">{{item.userName}}</span><span class="ml30">{{item.resource | toJson('common')}}</span></li>
				      	</ul>
				      	<div v-if="applyRecord.length == 0" class="lh30">
				      		暂无申请记录
				      	</div>
				      </div>
				    </div>
				</div>
			    <div class="btns col-sm-10 col-lg-10 col-sm-offset-1 col-lg-offset-1">
			    	<button type="submit" id="save" class="btn btn-primary" v-if="status != 'WAIT_APPROVE'">提交</button>
			    	<button type="button" class="btn btn-default cancel" @click="cancel" v-if="status != 'WAIT_APPROVE'">取消</button>
			    	<button type="button" class="btn btn-default cancel" @click="cancel" v-if="status == 'WAIT_APPROVE'">返回</button>
			    </div>
		    </form>
		</div>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/bootstrapValidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${ctx}/js/enterprise/cerEnterpriseEditAccountPeriod.js"></script>
</body>
</html>