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
  <title>供应商启用_失效审核</title>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <link rel="stylesheet" href="${ctx}/css/app/vendor.css"/>
  <style>
  	.apply-status{
  		height:50px;
  		line-height:50px;
  	}
  	.reason-wrap{
		display:none;
	}
	.reason-box{
		position:relative;
	}
	.reason-box textarea{
		width:350px;
		height:120px;
	}
	.reason-box .num-wrap{
		position:absolute;
		bottom:10px;
		right:20px;
	}
	.ml5{margin-left:5px;}
	.audit-log .vendor-text{padding-top:2px;}
	.audit-log p{margin-bottom:5px;}
  </style>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<!---index nav  end   ---->
	<div class="content-wrapper" id="enableAudit">
		<section class="content-header">
			<h1>
		 		供应商审核
	  		</h1> 
	  	</section>
	  	<div class="col-lg-12 apply-status">
	  		<div class="col-md-9">
	  			<!-- 审核人信息 -->
	  				<input type="hidden" id="userName" value="${userName }">
					<input type="hidden" id="userId" value="${userId }">
		  		<!-- 审核人信息 -->
				<label for="title" class="col-sm-2 control-label">供应商名称：</label>
			  	<div class="col-sm-7">
			  		<div class="vendor-name">
			  			<a v-bind:href="thisUrl+'/vendor/detail.htm?partyId='+auditInfo.applyOrgId" target="_blank">{{auditInfo.companyName}}</a>
			  		</div>
			  	</div>
			</div>
	  		<div class="col-md-3 text-right" v-show="auditInfo.status == 'WAIT_APPROVE'" v-if="isNotMyVendor">
	  			<button class="btn-danger btn" @click="examineEvent('yes')">通过</button>
	  			<button class="btn-danger btn" @click="examineEvent('no')">不通过</button>
	  		</div>
	  	</div>
	  	
  		<section class="content" style="color: rgb(102, 102, 102);">
	  		<div class="row">
	  			<section class="col-lg-12">
	  				<div class="box box-danger ">
	  					<div class="box-header">
	  						<div>审核信息</div>
	  					</div>
					  	<div class="box-body ">
					  		<!-- 基本信息表单 -->
					  		<form name="createVendorBase" id="createVendorBase" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" >
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">申请类型：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
										  		<span v-if="auditInfo.processId == 'ORG_SUPPLIER_ARCHIVES_REVIEW'">建档</span>
										  		<span v-if="auditInfo.processId == 'ORG_SUPPLIER_INFO_CHANGE_REVIEW'">基本信息变更</span>
					                            <span v-if="auditInfo.processId == 'ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW'">产品线变更</span>
					                            <span v-if="auditInfo.processId == 'ORG_SUPPLIER_INVALID_REVIEW'">失效申请</span>
					                            <span v-if="auditInfo.processId == 'ORG_SUPPLIER_ENABLED_REVIEW'">启用申请</span>
				                            </div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">申请人：</label>
									  	<div class="col-sm-9" >
									  		<div class="vendor-text">
									  			{{applyContentJsonObj.applyMail?applyContentJsonObj.applyMail:'--'}}
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">申请时间：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text" v-html="auditInfo.createdDate?auditInfo.createdDate:'--'">
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">申请说明：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text" v-html="applyContentJsonObj.describe?applyContentJsonObj.describe:'--'">
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">审核状态：</label>
									  	<div class="col-sm-9">
									  		<div class="vendor-text">
									  			<span v-if="auditInfo.status == 'APPROVED'">通过</span>
					                            <span v-if="auditInfo.status == 'REJECT'">不通过</span>
					                            <span v-if="auditInfo.status == 'WAIT_APPROVE'">待审核</span>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">审核记录：</label>
									  	<div class="col-sm-9 audit-log">
									  		<div class="vendor-text" v-if="approveLogListLength>0">
								  				<p v-for="(item,$index) in approveLogList">
									  				<span>{{item.date}}</span>
									  				<span class="ml5" >{{item.userName}}</span>
									  				<span class="ml5" v-if="item.actionDesc">{{item.actionDesc}}</span>
								  				</p>
									  		</div>
									  		<div class="vendor-text" v-if="approveLogListLength==0">
									  			<p>--</p>
									  		</div>
									  	</div>
			  						</div>
			  					</div>
			  					
		  					</form>
		  					<!-- 基本信息表单 -->
		  					<!-- 不通过原因 -->
		  					<div class="reason-wrap">
		                      	<p>请输入审核不通过的原因：</p>
		                      	<div class='reason-box'>
		                      		<textarea id='remark' onkeyup="setLetterNum(this)" maxlength="100"></textarea>
		                      		<span class="num-wrap"><span class="letter-num">0</span>/100</span>
		                      	</div>
		                     </div>
		  					<!-- 不通过原因 -->
		  				</div>
	  				</div>	
	  			</section>
	  		</div>
	 	</section>
	 </div>
	  	
	<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/app/vendorEnableAudit.js"></script>
</div>	    
</body>
</html>
