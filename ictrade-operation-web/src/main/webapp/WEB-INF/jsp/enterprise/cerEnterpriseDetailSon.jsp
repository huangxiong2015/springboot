<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">
<head>
<title>子账号管理</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/bootstrapValidator/css/bootstrapValidator.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/common/base.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseEdit.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseEditSon.css" />
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper">
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="son">
	<section class="content-header">
	  <a href="javascript:;" class="header_tab" @click="clickBasicInfo">基本信息</a>
	  <a href="javascript:;" class="header_tab pitch_tab">子账号管理</a>
	  <a href="javascript:;" class="header_tab" @click="clickAccountPeriod">账期管理</a>
	</section>
	<input type="hidden" value="${userId}" id="applyUserId">
	<section class="content">
		<h5 class="title">{{name}}<em>{{partyCode ? partyCode : '-'}}</em></h5>
		<div id="edit" class="row form-wrap">
			<form class="form-horizontal infoFrom">
				<label :class="{'verify-status':true,danger:accountStatus !== 'APPROVED',success: accountStatus === 'APPROVED'}">{{sonAccountStatus}}</label>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>设置主账号：</label>
			      <div class="col-sm-10 col-lg-6 text">
					  {{mail ? mail : '-'}}
			      </div>
			    </div>
			    <div class="form-group mt30">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>企业授权委托书：</label>
			      <div class="col-sm-10 col-lg-10">
			      	 <div class="input-content">
					   <div class="uploadImg uploadImgLoa">
					    <img id="img8" class="LOA_PIC" src="" alt="" data-src="" />
					   </div>
					  </div>
		      	  </div>
			     <%--  <div class="col-sm-10 col-lg-6">
			      	<a :href="11" target="_blank">
						<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
					</a>
					<span class="dib" type="text"></span>
		      	  </div> --%>
			    </div>
			    <h5 class="log">申请记录</h5>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">申请记录：</label>
			      <div class="col-sm-10 col-lg-10">
			      	<ul v-if="applyRecord.length > 0" class="log-list">
			      		<li v-for="item in applyRecord">{{item.date}}<span class="ml30">{{item.mail}}</span><span class="ml30">{{item.resource | toJson('reason')}}</span></li>
			      	</ul>
			      	<div v-if="applyRecord.length == 0" class="lh30">
			      		暂无申请记录
			      	</div>
			      </div>
			    </div>
			    <h5 class="log">账号管理</h5>
		    </form>
		    <table class="table table-bordered ml20">
		      <thead>
		        <tr>
		          <th>邮箱</th>
		          <th>姓名</th>
		          <th>手机号</th>
		          <th>账号状态</th>
		          <th>账号类型</th>
		          <th>注册时间</th>
		        </tr>
		      </thead>
		      <tbody>
		        <tr v-for="item in accountList">
		          <td>{{ item.mail }}</td>
		          <td>{{ item.name }}</td>
		          <td>{{ item.tel || '-' }}</td>
		          <td>{{ item.status == 'PARTY_ENABLED' ? '有效' : '无效' }}</td>
		          <td>{{ accountType(item.personTypeStatus) }}</td>
		          <td>{{ item.regTime || '-' }}</td>
		        </tr>
		      </tbody>
		    </table>
		     <div class="btns col-sm-10 col-lg-10 col-sm-offset-1 col-lg-offset-1 mt30">
		    	<button type="button" class="btn btn-default cancel" @click="cancel">返回</button>
		    </div>
		</div>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-city.js"></script>
<script type="text/javascript" src="${ctx}/js/common/component.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_zz.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/bootstrapValidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${ctx}/js/enterprise/cerEnterpriseDetailSon.js"></script>
</body>
</html>