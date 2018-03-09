<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>企业认证管理</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/bootstrapValidator/css/bootstrapValidator.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/common/base.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseEdit.css" />
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="detail">
	<section class="content-header">
	  <a href="javascript:;" class="header_tab pitch_tab">基本信息</a>
	  <a href="javascript:;" class="header_tab" @click="clickSon">子账号管理</a>
	  <a href="javascript:;" class="header_tab" @click="clickAccountPeriod">账期管理</a>
	</section>
	<section class="content">
		<h5 class="title" v-cloak>{{name}}<em>{{partyCode ? partyCode : '-'}}</em></h5>
		<div class="row form-wrap">
			<form class="form-horizontal infoFrom">
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">公司名称：</label>
			      <div class="col-sm-10 col-lg-6 text" v-cloak>{{ name }}</div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">公司类型：</label>
			      <div class="col-sm-10 col-lg-6 text" v-cloak>{{ getCompanytype(corCategory, corCategoryOther) }}</div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">所属行业：</label>
			      <div class="col-sm-10 col-lg-6 text" v-cloak>{{ getIndustry(industry, industryOther) }}</div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">公司地址：</label>
			      <div class="col-sm-10 col-lg-6 text" v-cloak>{{ provinceName + cityName + countryName + address }}</div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">邓氏编码：</label>
			      <div class="col-sm-10 col-lg-6 text" v-cloak>{{ dCode }}</div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">公司官网：</label>
			      <div class="col-sm-10 col-lg-6 text" v-cloak>{{ webSite }}</div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">YKY客户编码：</label>
			      <div class="col-sm-10 col-lg-6 text" v-cloak>{{ partyCode }}</div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">信用备注：</label>
			      <div class="col-sm-10 col-lg-6 text" v-cloak>{{ creditComments }}</div>
			    </div>
		    <h5 class="status"><span>企业资质</span><label v-if="activeStatus == 'PARTY_VERIFIED'" class="verify-status success">通过</label><label v-if="activeStatus == 'INVALID'" class="verify-status danger">失效</label></h5>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">公司注册地：</label>
			      <div class="col-sm-10 col-lg-6 text" v-cloak>{{ regAddrName(regAddr) }} </div>
			    </div>
				<div v-if="regAddr == '0'" class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">营业执照类型：</label>
			      <div v-if="busiLisType == 'COMMON'" class="col-sm-10 col-lg-6 text">普通营业执照</div>
			      <div v-if="busiLisType == '3-TO-1'" class="col-sm-10 col-lg-6 text">三证合一营业执照</div>
			    </div>
			    <template>
			    	<div class="form-group">
				      <label v-if="regAddr == '0'" class="col-sm-2 col-lg-2 control-label">营业执照影印件：</label>
				      <label v-if="regAddr == '1'" class="col-sm-2 col-lg-2 control-label">注册证书（CR）：</label>
				      <label v-if="regAddr == '2'" class="col-sm-2 col-lg-2 control-label">盈利事業登記證：</label>
				      <label v-if="regAddr == '3'" class="col-sm-2 col-lg-2 control-label">CERTIFICATE OF INCORPORATION：</label>
				      <div v-if="!busiPdfName" class="col-sm-10 col-lg-6">
				      	<img id="busiLicPic" :src="getImage(busiLicPic)" class="img-thumbnail" @click="showPic('busiLicPic', $event)">
			      	  </div>
				      <div v-if="busiPdfName" class="col-sm-10 col-lg-6">
				      	<a :href="getImage(busiLicPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text" v-cloak>{{busiPdfName}}</span> 
			      	  </div>
				    </div>
				    <div v-if="busiLisType == 'COMMON' || busiLisType == '3-TO-1'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">统一社会信用代码：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ socialCode }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">企业名称（全称）：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ entName }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">住所：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ location }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">经营范围：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ busiRange }}</div>
					    </div>
					    <template v-if="busiLisType == '3-TO-1'">
					    	<div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">纳税人识别号：</label>
						      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ faxCode }}</div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">纳税人名称：</label>
						      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ faxName }}</div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">组织机构代码：</label>
						      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgCode }}</div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">机构名称：</label>
						      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgName }}</div>
						    </div><div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">机构地址：</label>
						      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgLocation }}</div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">成立日期：</label>
						      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgCdate }}</div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">营业期限：</label>
						      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgLimit }}</div>
						    </div>
					    </template>
				    </div>
				    <div v-if="busiLisType == 'HK-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">公司名称：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ hkEntName }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">签发日期：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ hkSignCdate }}</div>
					    </div>
				    </div>
				    <div v-if="busiLisType == 'TW-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">公司名稱：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ twFaxEntName }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">簽發日期：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ twSignCdate }}</div>
					    </div>
				    </div>
				    <div v-if="busiLisType == 'ABROAD-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">Company Name：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ abroadEntName }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">Company Number：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ abroadEntNum }}</div>
					    </div>
				    </div>
			    </template>
				<template id="taxRegPic" v-if="busiLisType !== '3-TO-1' && busiLisType !== 'ABROAD-CODE'">
			    	<div class="form-group">
				      <label v-if="regAddr == '0'" class="col-sm-2 col-lg-2 control-label">税务登记证影印件：</label>
				      <label v-if="regAddr == '1'" class="col-sm-2 col-lg-2 control-label">商业登记证（BR）：</label>
				      <label v-if="regAddr == '2'" class="col-sm-2 col-lg-2 control-label">稅籍登记证：</label>
				      <div v-if="!taxPdfName" class="col-sm-10 col-lg-6">
				      	<img id="taxPic" :src="getImage(taxPic)" class="img-thumbnail" @click="showPic('taxPic', $event)">
			      	  </div>
				      <div v-if="taxPdfName" class="col-sm-10 col-lg-6">
				      	<a :href="getImage(taxPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text" v-cloak>{{taxPdfName}}</span>
			      	  </div>
				    </div>
				    <div v-if="busiLisType == 'COMMON'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">纳税人识别号：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ faxCode }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">纳税人名称：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ faxName }}</div>
					    </div>
				    </div>
				    <div v-if="busiLisType == 'HK-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">业务所用名称：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ hkBusiName }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">地址 ：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ hkAddr }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">生效日期 ：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ hkEffectiveDate }}</div>
					    </div>
				    </div>
				    <div v-if="busiLisType == 'TW-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">公司名稱：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ twEntName }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">簽發日期：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ twSignCdate }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">稅務編碼：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ twFaxCode }}</div>
					    </div>
				    </div>
			    </template>
			    <template v-if="busiLisType == 'COMMON'">
			    	<div class="form-group">
				      <label class="col-sm-2 col-lg-2 control-label">组织机构代码影印件：</label>
				      <div v-if="!oocPdfName" class="col-sm-10 col-lg-6">
				      	<img id="oocPic" :src="getImage(oocPic)" class="img-thumbnail" @click="showPic('oocPic', $event)">
			      	  </div>
				      <div v-if="oocPdfName" class="col-sm-10 col-lg-6">
				      	<a :href="getImage(oocPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{oocPdfName}}</span>
			      	  </div>
				    </div>
				    <div class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">组织机构代码 ：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgCode }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">机构名称：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgName }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">机构地址：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgLocation }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">成立日期：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgCdate }}</div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">营业期限：</label>
					      <div class="col-sm-8 col-lg-8 text" v-cloak>{{ orgLimit }}</div>
					    </div>
				    </div>
			    </template>
			    <h5 class="log">修改记录</h5>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">修改记录：</label>
			      <div class="col-sm-10 col-lg-10">
			      	<ul v-if="applyRecord.length > 0" class="log-list">
			      		<li v-for="item in applyRecord" v-cloak>{{item.date}}<span class="ml30" v-cloak>{{item.userName}}</span><span v-if="isRzPage" class="ml30" v-cloak>{{item.resource | toJson('reason')}}</span></li>
			      	</ul>
			      	<div v-if="applyRecord.length == 0" class="lh30">
			      		暂无修改记录
			      	</div>
			      </div>
			    </div>
			    <div class="btns col-sm-10 col-lg-10 col-sm-offset-2 col-lg-offset-2">
			    	<button type="button" class="btn btn-default cancel" @click="cancel">返回</button>
			    </div>
			</form>
		</div>
	</section>	
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
	<script type="text/javascript" src="${ctx}/js/common/component.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/bootstrapValidator/js/bootstrapValidator.min.js" />
<script type="text/javascript" src="${ctx}/js/enterprise/cerEnterpriseDetail.js"></script>
</body>
</html>