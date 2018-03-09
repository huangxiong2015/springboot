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
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseList.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseEdit.css" />
<style>
	.control-label[for="id"] {
		display: none;
	}
	.checkVal {
		width: 107% !important;
	}
	.city-picker > div {
		margin-right: -20px;
	}
	.select-area {
		width: auto;
	}
</style>
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper">
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="edit">
	<section class="content-header">
	  <a href="javascript:;" class="header_tab pitch_tab">基本信息</a>
	  <a href="javascript:;" class="header_tab" @click="clickSon">子账号管理</a>
	  <a href="javascript:;" class="header_tab" @click="clickAccountPeriod">账期管理</a>
	</section>
	<section class="content">
		<h5 class="title">{{name}}<em>{{partyCode ? partyCode : '-'}}</em></h5>
		<div id="edit" class="row form-wrap">
			<form id="infoFrom" class="form-horizontal infoFrom">
				<div class="tips" style="display:none;"></div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>公司名称：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	{{name}}
			      </div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>公司类型：</label>
			      <div class="col-sm-6 col-lg-6 text">
				      <select class="form-control" v-model="corCategory">
					    <option v-for="item in initData.companytypeList" :value="item.categoryId">{{ item.categoryName }}</option>
					  </select>
			      </div>
			    </div>
				<div v-show="corCategory==2006" class="form-group">
					<div class="col-sm-6 col-lg-6 col-sm-offset-2 text">
				 	 	<input type="text" name="corCategoryOther" class="form-control" id="" placeholder="其他行业" v-model="corCategoryOther">
					</div>
			      	<div class="col-sm-4 tips1 text"></div>
				</div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>所属行业：</label>
			      <div class="col-sm-6 col-lg-6 text">
				      <div class="f-ib"> 
					   <a class="select_industry" @click="toggleIndustry"><em class="select_tip">请选择所属行业</em><i class="icon-down_arrow1 g9"></i></a> 
					   <div class="industry_content dn"> 
					    <div class="industry_list  mCustomScrollbar _mCS_3 _mCS_1" style="max-height: 80px; overflow: hidden;"> 
					     <div class="mCustomScrollBox mCS-light" id="mCSB_1" style="position: relative; height: 100%; overflow: hidden; max-width: 100%; max-height: 80px;">
					      <div class="mCSB_container mCS_no_scrollbar" style="position:relative; top:0;">
					       <div class="company_type noselect" unselectable="on" v-for="item in initData.industryList" @click="selectIndustryItem">
					        <span class="check-box-white isfirst" :data-id="item.categoryId"></span>
					        <i class="g3">{{ item.categoryName }}</i>
					        <input v-if="item.categoryId == 1008" v-show="initData.isShowIndustryOther" @click.stop v-model="initData.otherAttr" id="industryOther" name="industryOther" class="other_attr v-success" maxlength="10" placeholder="请填写所属行业"/>
					       </div>
					      </div>
					      <div class="mCSB_scrollTools" style="position: absolute; display: none;">
					       <div class="mCSB_draggerContainer">
					        <div class="mCSB_dragger" style="position: absolute; top: 0px;" oncontextmenu="return false;">
					         <div class="mCSB_dragger_bar" style="position:relative;"></div>
					        </div>
					        <div class="mCSB_draggerRail"></div>
					       </div>
					      </div>
					     </div>
					    </div>
					    <div class="industry_btn mt20 tc">
					     <a class="btn_y" @click="confirmIndustry">确认</a>
					     <a class="btn_c" @click="cancelIndustry">取消</a>
					    </div>
					   </div>
					   <div class="select-area">
					   	<div v-for="item in initData.selectedIndustry" v-if="item != ''" class="area-item" :data-id="item">{{ item | findIndustryName }}<i class="icon-close-min close" @click="removeSelect"></i></div>
					   </div>
					   <input id="industry" name="industry" type="hidden" data-duplex="@parameter.industry" value="" />
					  </div>
				 </div>	
			    </div>
				<div class="form-group" style="margin-bottom:0;">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>公司地址：</label>
			      <div class="col-sm-6 col-lg-6 text">
				    <select-province
			            :api="selectBox.api"
			            @onchange="change"
			            :input-class="selectBox.inputClass"
			            :name-district="selectBox.nameDistrict"
			            :name-province="selectBox.nameProvince"
			            :name-city="selectBox.nameCity"
			            :set-province="selectBox.setProvince"
			            :set-city="selectBox.setCity"
			            :set-district="selectBox.setDistrict"
			            :query-param="selectBox.queryParam"
			            :parent-id="selectBox.parentId"
				    ></select-province>
			      </div>
			    </div>
				<div v-show="country!=''" class="form-group">
					<div class="col-sm-6 col-lg-6 col-sm-offset-2 text">
			      		<input type="text" name="address" class="form-control" id="" placeholder="详细地址" v-model="address">
					</div>
			      	<div class="col-sm-4 tips2 text"></div>
				</div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">邓氏编码：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" name="dCode" class="form-control" id="" placeholder="邓氏编码" v-model="dCode">
			      </div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">公司官网：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" name="webSite" class="form-control" id="" placeholder="公司官网" v-model="webSite">
			      </div>
			      <div class="col-sm-4 tips3 text"></div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">YKY客户编码：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      
			      	<span v-if="partyCode && readPartyCode">{{ partyCode }}</span>
			      	<input v-else type="text" name="partyCode" class="form-control" id="" placeholder="YKY客户编码" v-model="partyCode" @blur="validateCode">
			      </div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">信用备注：</label>
			      <div class="col-sm-11 col-lg-8">
			      	<div class="row">
			      		<div class="col-sm-10 col-lg-9">
			      			<textarea class="form-control" rows="3" v-model="creditComments" @input="chCounter('creditComments', 100)"></textarea>
			      		</div>
			      		<div class="col-sm-2 col-lg-1 credit-memo">
			      			<span>{{ creditComments.length }}/100</span>
			      		</div>
			      	</div>
                  </div>
			    </div>
		    <h5 class="status"><span>企业资质</span><label v-if="activeStatus == 'PARTY_VERIFIED'" class="verify-status success">通过</label><label v-if="activeStatus == 'INVALID'" class="verify-status danger">失效</label></h5>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">公司注册地：</label>
			      <div class="col-sm-6 col-lg-6 text">{{ regAddrName(regAddr) }} </div>
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
						<span class="dib" type="text">{{busiPdfName}}</span>
			      	  </div>
				    </div>
				    <div v-if="busiLisType == 'COMMON' || busiLisType == '3-TO-1'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>统一社会信用代码：</label>
					      <div class="col-sm-8 col-lg-8 text"><input name="socialCode" type="text" class="form-control" id="" placeholder="不能为空" v-model="socialCode"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">企业名称（全称）：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="entName"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">住所：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="location"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">经营范围：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="busiRange"></div>
					    </div>
					    <template v-if="busiLisType == '3-TO-1'">
					    	<div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">纳税人识别号：</label>
						      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="faxCode"></div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">纳税人名称：</label>
						      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="faxName"></div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>组织机构代码：</label>
						      <div class="col-sm-8 col-lg-8 text"><input name="orgCode" type="text" class="form-control" id="" placeholder="不能为空" v-model="orgCode"></div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">机构名称：</label>
						      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="orgName"></div>
						    </div><div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">机构地址：</label>
						      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="orgLocation"></div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">成立日期：</label>
						      <div class="col-sm-8 col-lg-8 text"><input name="orgCdate" type="text" class="form-control datetimepicker" id="" placeholder="" v-model="orgCdate"></div>
						    </div>
						    <div class="form-group pull-left col-sm-6">
						      <label class="col-sm-4 col-lg-4 control-label">营业期限：</label>
						      <div class="col-sm-8 col-lg-8 text"><input name="orgLimit" type="text" class="form-control datetimepicker" id="" placeholder="" v-model="orgLimit"></div>
						    </div>
					    </template>
				    </div>
				    <div v-if="busiLisType == 'HK-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">公司名称：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="hkEntName"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">签发日期：</label>
					      <div class="col-sm-8 col-lg-8 text"><input name="hkSignCdate" type="text" class="form-control datetimepicker" id="" placeholder="" v-model="hkSignCdate"></div>
					    </div>
				    </div>
				    <div v-if="busiLisType == 'TW-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">公司名稱：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="twFaxEntName"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">簽發日期：</label>
					      <div class="col-sm-8 col-lg-8 text"><input name="twSignCdate" type="text" class="form-control datetimepicker" id="" placeholder="" v-model="twSignCdate"></div>
					    </div>
				    </div>
				    <div v-if="busiLisType == 'ABROAD-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">Company Name：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="abroadEntName"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">Company Number：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="abroadEntNum"></div>
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
						<span class="dib" type="text">{{taxPdfName}}</span>
			      	  </div>
				    </div>
				    <div v-if="busiLisType == 'COMMON'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">纳税人识别号：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="faxCode"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">纳税人名称：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="faxName"></div>
					    </div>
				    </div>
				    <div v-if="busiLisType == 'HK-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">业务所用名称：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="hkBusiName"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">地址 ：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="hkAddr"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">生效日期 ：</label>
					      <div class="col-sm-8 col-lg-8 text"><input name="hkEffectiveDate" type="text" class="form-control datetimepicker" id="" placeholder="" v-model="hkEffectiveDate"></div>
					    </div>
				    </div>
				    <div v-if="busiLisType == 'TW-CODE'" class="info clearfix">
				    	<div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">公司名稱：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="twEntName"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">簽發日期：</label>
					      <div class="col-sm-8 col-lg-8 text"><input name="twSignCdate" type="text" class="form-control datetimepicker" id="" placeholder="" v-model="twSignCdate"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">稅務編碼：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="twFaxCode"></div>
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
					      <label class="col-sm-4 col-lg-4 control-label"><span class="cRed">*</span>组织机构代码 ：</label>
					      <div class="col-sm-8 col-lg-8 text"><input name="orgCode" type="text" class="form-control" id="" placeholder="不能为空" v-model="orgCode"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">机构名称：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="orgName"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">机构地址：</label>
					      <div class="col-sm-8 col-lg-8 text"><input type="text" class="form-control" id="" placeholder="" v-model="orgLocation"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">成立日期：</label>
					      <div class="col-sm-8 col-lg-8 text"><input name="orgCdate" type="text" class="form-control datetimepicker" id="" placeholder="" v-model="orgCdate"></div>
					    </div>
					    <div class="form-group pull-left col-sm-6">
					      <label class="col-sm-4 col-lg-4 control-label">营业期限：</label>
					      <div class="col-sm-8 col-lg-8 text"><input name="orgLimit" type="text" class="form-control datetimepicker" id="" placeholder="" v-model="orgLimit"></div>
					    </div>
				    </div>
			    </template>
			    <h5 class="log">修改记录</h5>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">修改日志：</label>
			      <div class="col-sm-11 col-lg-8">
			      	<div class="row">
			      		<div class="col-sm-10 col-lg-9">
			      			<textarea class="form-control" name="reason" rows="3" v-model="reason" placeholder="中英文、数字及符号组成" @input="chCounter('reason', 50)"></textarea>
			      		</div>
			      		<div class="col-sm-2 col-lg-1 credit-memo">
			      			<span>{{ reason.length }}/50</span>
			      		</div>
			      	</div>
                  </div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">修改记录：</label>
			      <div class="col-sm-10 col-lg-10">
			      	<ul v-if="applyRecord.length > 0" class="log-list">
			      		<li v-for="item in applyRecord">{{item.date}}<span class="ml30">{{item.userName}}</span><span class="ml30">{{item.resource | toJson('reason')}}</span></li>
			      	</ul>
			      	<div v-if="applyRecord.length == 0" class="lh30">
			      		暂无申请记录
			      	</div>
			      </div>
			    </div>
			    <div class="btns col-sm-10 col-lg-10 col-sm-offset-2 col-lg-offset-2">
			    	<button id="save" type="submit" class="btn btn-danger">保存</button>
			    	<button type="button" class="btn btn-default cancel" @click="cancel">取消</button>
			    </div>
			</form>
		</div>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-city.js"></script>
<script type="text/javascript" src="${ctx}/js/common/component.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/bootstrapValidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${ctx}/js/enterprise/cerEnterpriseEditRz.js"></script>
</body>
</html>