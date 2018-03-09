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
	<input type="hidden" value="${userName}" id="contactUserName">
	<section class="content">
		<h5 class="title">{{name}}<em>{{partyCode ? partyCode : '-'}}</em></h5>
		<div id="edit" class="row form-wrap">
			<form class="form-horizontal infoFrom">
				<label :class="{'verify-status':true,danger:accountStatus !== 'APPROVED',success: accountStatus === 'APPROVED'}">{{sonAccountStatus}}</label>
				
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>设置主账号：</label>
			      <div class="col-sm-10 col-lg-6 text">
				      <select id="setMainAccount" class="form-control" v-model="mail">
					    <option value="">请选择</option>
					    <option v-for="item in filterAccountList" v-if="item.mailStatus=='Y' && item.status=='PARTY_ENABLED'" :value="item.mail">{{ item.mail }}</option>
					  </select>
			      </div>
			    </div>
			    <div class="form-group mt30">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>企业授权委托书：</label>
			      <div class="col-sm-10 col-lg-10">
			      	 <div class="input-content">
					   <div class="uploadImg uploadImgLoa">
					    <img id="img8" class="LOA_PIC" src="" alt="" data-src="" />
					    <span id="uploadBtnLoa" class="upload-btn tc" style="cursor: pointer; z-index: 1;">点此上传</span>
					    <div id="html5_1bmh27j6qub0gt99kd1u071glb8_container" class="moxie-shim moxie-shim-html5" style="position: absolute; top: 76px; left: 0px; width: 163px; height: 32px; overflow: hidden; z-index: 0;">
					     <input id="html5_1bmh27j6qub0gt99kd1u071glb8" type="file" style="font-size: 999px; opacity: 0; position: absolute; top: 0px; left: 0px; width: 100%; height: 100%;" accept="image/jpeg,image/bmp,image/png,application/pdf,.PDF,image/gif" />
					    </div>
					   </div>
					   <div class="uploadCz">
					    <p class="tips">文件格式：.jpg .jpeg .png .bmp .pdf，文件大小：5M以内<br /> 上传的文件需加盖公司公章或财务章</p>
					    <p><a class="blue dib download" style="color:#3377aa;" href="${ctx }/resources/download/企业授权委托书.doc">下载《企业授权委托书》模版</a></p>
					    <span class="ck-btn" @click="clickCankao">参考样本</span>
					    <span class="dn"><img id="locPicSample" src="${ctx }/images/sanzheng/dl-loa.jpg" /></span>
					   </div>
					  </div>
		      	  </div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 control-label"><span class="cRed">*</span>申请说明：</label>
			      <div class="col-sm-11 col-lg-8">
			      	<div class="row">
			      		<div class="col-sm-10 col-lg-9">
			      			<textarea class="form-control" rows="3" v-model="reason" @input="chCounter()"></textarea>
			      		</div>
			      		<div class="col-sm-2 col-lg-1 credit-memo">
			      			<span>{{ counter }}/100</span>
			      		</div>
			      	</div>
                  </div>
			    </div>
			    <h5 class="log">申请记录</h5>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">申请记录：</label>
			      <div class="col-sm-10 col-lg-10">
			      	<ul v-if="applyRecord.length > 0" class="log-list"><li v-for="item in applyRecord">{{item.date}}<span class="ml30">{{item.mail}}</span><span class="ml30">{{item.resource | toJson('reason')}}</span></li>
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
		          <th>最后登录时间</th>
		          <th>登入次数</th>
		          <th>操作</th>
		        </tr>
		      </thead>
		      <tbody>
		        <tr v-for="item in accountList">
		          <td :title="item.mail">{{ item.mail }}</td>
		          <td :title="item.name">{{ item.name }}</td>
		          <td :title="item.tel || ''">{{ item.tel || '-' }}</td>
		          <td>{{ item.status == 'PARTY_ENABLED' ? '有效' : '无效' }}</td>
		          <td>{{ accountType(item.personTypeStatus) }}</td>
		          <td :title="item.regTime">{{ item.regTime || '-' }}</td>
		          <td :title="item.lastLoginTime">{{ item.lastLoginTime || '-' }}</td>
		          <td>{{ item.loginCount }}</td>
		          <td><a href="javascript:;" @click="frozenAccount(item.id, item.mail, item.status)">{{ item.status == 'PARTY_ENABLED' ? '冻结' : '启用' }}</a></td>
		        </tr>
		      </tbody>
		    </table>
		     <div class="btns col-sm-10 col-lg-10 col-sm-offset-1 col-lg-offset-1">
		    	<button type="button" class="btn btn-danger" @click="submit" v-if="accountStatus != 'WAIT_APPROVE'">提交</button>
		    	<button type="button" class="btn btn-default cancel" @click="cancel" v-if="accountStatus == 'WAIT_APPROVE'">返回</button>
		    	<button type="button" class="btn btn-default cancel" @click="cancel" v-if="accountStatus != 'WAIT_APPROVE'">取消</button>
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
<script type="text/javascript" src="${ctx}/js/enterprise/cerEnterpriseEditSon.js"></script>
</body>
</html>