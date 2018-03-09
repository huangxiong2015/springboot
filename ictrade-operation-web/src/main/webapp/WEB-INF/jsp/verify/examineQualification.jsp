<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>企业账号审核</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/bootstrapValidator/css/bootstrapValidator.min.css" />
<link rel="stylesheet" href="${ctx}/css/app/examine.css"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="enterprise-list">
		<section class="content-header">
		  <h1>企业账号审核</h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
		    <li class="active">企业账号审核</li>
		  </ol>
		</section> 
	<section class="content">   		
    	<div class="row">
        	<section class="col-sm-12" id="examineQualification">
			<input type="hidden" id="userName" value="${userName }">
			<input type="hidden" id="userId" value="${userId }">
          	<div class="box ">
	            <div class="chart box-body " style="position: relative;">
	            	<div class="form-group ">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">申请类型：</label>
                            <div class="col-sm-4" >
                            	<span class="show-btn" @click="showInfo('.applicant-info',$event)">
                            		<span v-html="info.processName?info.processName:'三证等资料审核'"></span><i class="fa fa-angle-left pull-right"></i>
                            	</span>                            	
                            </div>
                            <label class="col-sm-2 control-label">申请时间：</label>
                            <div class="col-sm-4"><span v-html="info.createdDate?info.createdDate:'--'"></span></div>
                        </div>
                    </div>
                    <div class="form-group applicant-info">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">申请人：</label>
                            <div class="col-sm-2" ><span v-html="info.contactUserName?info.contactUserName:'--'"></span></div>                            
                        </div>
                        <div class="col-md-11">
                            <label class="col-sm-2 control-label">客户联系人：</label>
                            <div class="col-sm-2" ><span v-html="info.applyUser?info.applyUser:'--'"></span></div>
                            <label class="col-sm-1 control-label">邮箱：</label>
                            <div class="col-sm-3" ><span v-html="info.applyMail?info.applyMail:'--'"></span></div>
                            <label class="col-sm-2 control-label">手机号码：</label>
                            <div class="col-sm-1" ><span v-html="info.applyInformation?info.applyInformation:'--'">--</span></div>
                        </div>                        
                    </div> 
                    <div class="form-group ">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">审核状态：</label>
                            <div class="col-sm-4" >
                            	<span v-if="audit.status == 'APPROVED'">通过</span>
	                            <span v-if="audit.status == 'REJECT'">不通过</span>
	                            <span v-if="audit.status == 'WAIT_APPROVE'">待审核</span>
                            </div>                           
                        </div>
                    </div>                    
                    <div class="form-group ">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">公司名称：</label>
                            <div class="col-sm-8" >
                            	<span class="show-btn" @click="showInfo('.company-info',$event)">
                            		<span v-html="companyInfo.name"></span><i class="fa fa-angle-left pull-right"></i>
                            	</span>
                            </div>                            
                        </div>
                    </div>
                    <div class="form-group company-info">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">公司类型：</label>
                            <div class="col-sm-4" ><span v-html="companyInfo.corCategory=='2006'?companytype[companyInfo.corCategory]+'('+companyInfo.CORPORATION_CATEGORY_ID_OTHER+')':companytype[companyInfo.corCategory]"></span></div>                            
                        </div>
                        <div class="col-md-11">
                            <label class="col-sm-2 control-label">所属行业：</label>
                            <div class="col-sm-8" ><span v-html="getIndustry()"></span></div>                                                      
                        </div>
                        <div class="col-md-11">
                            <label class="col-sm-2 control-label">公司地址：</label>
                            <div class="col-sm-4" ><span v-html="companyInfo.provinceName + companyInfo.cityName + companyInfo.countryName + companyInfo.address"></span></div>                                                      
                        </div>
                        <div class="col-md-11">
                            <label class="col-sm-2 control-label">邓氏编码：</label>
                            <div class="col-sm-4" ><span v-html="companyInfo.dCode?companyInfo.dCode:'--'"></span></div>                                                      
                        </div>
                        <div class="col-md-11">
                            <label class="col-sm-2 control-label">公司官网：</label>
                            <div class="col-sm-4" ><span v-html="companyInfo.webSite?companyInfo.webSite:'--'"></span></div>                                                      
                        </div>
                        <div class="col-md-11">
                            <label class="col-sm-2 control-label">客户编码：</label>
                            <div class="col-sm-4" ><span v-html="companyInfo.partyCode?companyInfo.partyCode:'--'"></span></div>                                                      
                        </div>                        
                    </div>
                    <div class="form-group ">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">公司注册地：</label>
                            <div class="col-sm-2" ><span v-html="regAddr()"></span></div>                            
                        </div>
                    </div>
                    <div class="form-group" v-if="zzInfo.regAddr == 0">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">营业执照类型：</label>
                            <div class="col-sm-2" ><span v-html="zzInfo.busiLisType=='COMMON'?'普通营业执照':'企业三证合一'"></span></div>                            
                        </div>
                    </div>
                    <form id="infoForm" class="form-horizontal infoFrom">
                    <div class="box-wrap">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">
                            	<span v-if="zzInfo.regAddr == '0'">营业执照影印件</span>
                            	<span v-if="zzInfo.regAddr == '1'">注册证书（CR）</span>
                            	<span v-if="zzInfo.regAddr == '2'">盈利事業登記證</span>
                            	<span v-if="zzInfo.regAddr == '3'">CERTIFICATE OF INCORPORATION</span>
                            </label>                            
                            <div class="col-sm-2" v-if="!zzInfo.busiPdfName">
                            	<img class="img-thumbnail" id="busiLicPic" :src="zzInfo.busiLicPic" @click="showVoucherPic('#busiLicPic')">                            	
                            </div>
                            <div class="col-sm-2" v-if="zzInfo.busiPdfName">
                            	<a id="pdfPic0" :href="zzInfo.busiLicPic" target="_blank">
									<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
								</a>
								<span class="dib" type="text" id="pdfName0" v-html="zzInfo.busiPdfName"></span>
                            </div> 
                            <div class="col-sm-8 certifi-info">
                            	<div class="dl-sw" v-if="zzInfo.busiLisType == '3-TO-1' || zzInfo.busiLisType == 'COMMON'">
                            	<div class="form-group ">
	                            	<div class="col-md-11">
			                            <label class="col-sm-4 control-label"><span class="text-red">*</span>统一社会信用代码：</label>
			                            <div class="col-sm-5" ><input name="socialCode" class="form-control help-block" v-model="info.socialCode" maxlength="50"></div>                            
			                       		<div class="socialCode-err err-wrap col-sm-3"></div>
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label"><span class="text-red">*</span>企业名称（全称）：</label>
			                            <div class="col-sm-5" ><input name="entName" id="entName" class="form-control help-block" v-model="info.entName" maxlength="50"></div>                            
			                        	<div class="entName-err err-wrap col-sm-3"></div>
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">住所：</label>
			                            <div class="col-sm-5" ><input name="location" class="form-control help-block" v-model="info.location" maxlength="50"></div>                            
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">经营范围：</label>
			                            <div class="col-sm-5" ><input name="busiRange" class="form-control help-block" v-model="info.busiRange" maxlength="50"></div>                            
			                        </div>
		                        </div>
			                        <div class="dl-sw" v-if="zzInfo.busiLisType == '3-TO-1'">
			                        <div class="form-group ">
				                        <div class="col-md-11">
				                            <label class="col-sm-4 control-label">纳税人识别号：</label>
				                            <div class="col-sm-5" ><input name="faxCode" class="form-control help-block" v-model="info.faxCode" maxlength="50"></div>                            
				                        	<div class="faxCode-err err-wrap col-sm-3"></div>
				                        </div>
			                        </div>
			                        <div class="form-group ">
				                        <div class="col-md-11">
				                            <label class="col-sm-4 control-label">纳税人名称：</label>
				                            <div class="col-sm-5" ><input name="faxName" class="form-control help-block" v-model="info.faxName" maxlength="50"></div>                            
				                        	<div class="faxName-err err-wrap col-sm-3"></div>
				                        </div>
			                        </div>
			                        <div class="form-group ">			                        
				                        <div class="col-md-11">
				                            <label class="col-sm-4 control-label"><span class="text-red">*</span>机构代码：</label>
				                            <div class="col-sm-5" ><input name="orgCode" class="form-control help-block" v-model="info.orgCode" maxlength="50"></div>                            
				                        </div>
			                        </div>
			                        <div class="form-group ">
				                        <div class="col-md-11">
				                            <label class="col-sm-4 control-label">机构名称 ：</label>
				                            <div class="col-sm-5" ><input name="orgName" class="form-control help-block" v-model="info.orgName" maxlength="50"></div>                            
				                        </div>
			                        </div>
			                        <div class="form-group ">
				                        <div class="col-md-11">
				                            <label class="col-sm-4 control-label">机构地址：</label>
				                            <div class="col-sm-5" ><input name="orgLocation" class="form-control help-block" v-model="info.orgLocation" maxlength="50"></div>                            
				                        </div>
			                        </div>
			                        <div class="form-group ">
				                        <div class="col-md-11">
				                            <label class="col-sm-4 control-label">成立日期：</label>
				                            <div class="col-sm-5 set-date"><input name="orgCdate" class="form-control help-block input-datetext" v-model="info.orgCdate"></div>                            
				                        </div>
			                        </div>
			                        <div class="form-group ">
				                        <div class="col-md-11">
				                            <label class="col-sm-4 control-label"><span class="text-red">*</span>营业期限：</label>
				                            <div class="col-sm-5" ><input name="orgLimit" class="form-control help-block input-datetext" v-model="info.orgLimit"></div>                            
				                        </div>
			                        </div>
			                        </div>			                        
		                        </div>
		                        <div class="hk-zs" v-if="zzInfo.busiLisType == 'HK-CODE'">
		                        <div class="form-group ">	
		                        	<div class="col-md-11">
			                            <label class="col-sm-4 control-label"><span class="text-red">*</span>公司名称：</label>
			                            <div class="col-sm-5" ><input name="hkEntName" id="hkEntName" class="form-control help-block" v-model="info.hkEntName" maxlength="50"></div>                            
			                       		<div class="hkEntName-err err-wrap col-sm-3"></div>
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">签发日期：</label>
			                            <div class="col-sm-5" ><input name="hkSignCdate" class="form-control help-block input-datetext" v-model="info.hkSignCdate"></div>                            
			                        </div>
		                        </div>
		                        </div>
		                        <div class="tw-zs" v-if="zzInfo.busiLisType == 'TW-CODE'">
		                        <div class="form-group ">
		                        	<div class="col-md-11">
			                            <label class="col-sm-4 control-label"><span class="text-red">*</span>公司名稱：</label>
			                            <div class="col-sm-5" ><input name="twEntName" id="twEntName" class="form-control help-block" v-model="info.twEntName" maxlength="50"></div>                            
			                        	<div class="twEntName-err err-wrap col-sm-3"></div>
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">簽發日期：</label>
			                            <div class="col-sm-5" ><input name="twSignCdate" class="form-control help-block input-datetext" v-model="info.twSignCdate"></div>                            
			                        </div>
		                        </div>
		                        </div>
		                        <div class="jw-zs" v-if="zzInfo.busiLisType == 'ABROAD-CODE'">
	                        	<div class="form-group ">
		                        	<div class="col-md-11">
			                            <label class="col-sm-4 control-label"><span class="text-red">*</span>Company Name：</label>
			                            <div class="col-sm-5" ><input name="abroadEntName" class="form-control help-block" v-model="info.abroadEntName" maxlength="50"></div>                            
			                        	<div class="abroadEntName-err err-wrap col-sm-3"></div>
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">Company Number：</label>
			                            <div class="col-sm-5" ><input name="abroadEntNum" class="form-control help-block" v-model="info.abroadEntNum" maxlength="50"></div>                            
			                        </div>
		                        </div>
		                        </div>
                            </div>                                                                                 
                        </div>
                     </div>
                     <div class="box-wrap" v-if="zzInfo.busiLisType !== '3-TO-1' && zzInfo.busiLisType !== 'ABROAD-CODE'">
                        <div class="col-md-11">
                            <label class="col-sm-2 control-label">
                            	<span v-if="zzInfo.regAddr == '0'">税务登记证影印件</span>
                            	<span v-if="zzInfo.regAddr == '1'">商业登记证（BR）</span>
                            	<span v-if="zzInfo.regAddr == '2'">稅籍登记证</span>
                            </label>
                            <div class="col-sm-2" v-if="!zzInfo.taxPdfName">
                            	<img class="img-thumbnail" id="taxPic" :src="zzInfo.taxPic" @click="showVoucherPic('#taxPic')">
                            </div>
                            <div class="col-sm-2" v-if="zzInfo.taxPdfName">
                            	<a id="pdfPic1" :href="zzInfo.taxPic" target="_blank">
									<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
								</a>
								<span v-html="zzInfo.taxPdfName"></span>
                            </div> 
                            <div class="col-sm-8 certifi-info">
                            	<div class="dl-sw" v-if="zzInfo.busiLisType == 'COMMON'">
                            	<div class="form-group ">
                            		<div class="col-md-11">
			                            <label class="col-sm-4 control-label">纳税人识别号：</label>
			                            <div class="col-sm-5" ><input name="faxCode" class="form-control help-block" v-model="info.faxCode" maxlength="50"></div>                            
			                        	<div class="faxCode-err err-wrap col-sm-3"></div>
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">纳税人名称：</label>
			                            <div class="col-sm-5" ><input name="faxName" class="form-control help-block" v-model="info.faxName" maxlength="50"></div>                            
			                        	<div class="faxName-err err-wrap col-sm-3"></div>
			                        </div>
		                        </div>
                            	</div>                            	
		                        			                        		                        		                        			                        
		                        <div class="hk-sw" v-if="zzInfo.busiLisType == 'HK-CODE'">
		                        <div class="form-group ">
		                        	<div class="col-md-11">
			                            <label class="col-sm-4 control-label">业务所用名称：</label>
			                            <div class="col-sm-5" ><input name="hkBusiName" class="form-control help-block" v-model="info.hkBusiName" maxlength="50"></div>                            
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">地址：</label>
			                            <div class="col-sm-5" ><input name="hkAddr" class="form-control help-block" v-model="info.hkAddr" maxlength="50"></div>                            
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">生效日期：</label>
			                            <div class="col-sm-5" ><input name="hkEffectiveDate" class="form-control help-block input-datetext" v-model="info.hkEffectiveDate"></div>                            
			                        </div>
		                        </div>
		                        </div>
		                        <div class="tw-sw" v-if="zzInfo.busiLisType == 'TW-CODE'">
		                        <div class="form-group ">
		                        	<div class="col-md-11">
			                            <label class="col-sm-4 control-label">公司名稱：</label>
			                            <div class="col-sm-5" ><input name="twFaxEntName" class="form-control help-block" v-model="info.twFaxEntName" maxlength="50"></div>                            
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">簽發日期：</label>
			                            <div class="col-sm-5" ><input name="twSignCdate" class="form-control help-block input-datetext" v-model="info.twSignCdate"></div>                            
			                        </div>
		                        </div>
		                        <div class="form-group ">
			                        <div class="col-md-11">
			                            <label class="col-sm-4 control-label">稅務編碼：</label>
			                            <div class="col-sm-5" ><input name="twFaxCode" class="form-control help-block" v-model="info.twFaxCode" maxlength="50"></div>                            
			                        </div>
		                        </div>
		                        </div>		                        
                            </div>                                                                                 
                        </div>
                     </div>
                     <div class="box-wrap" v-if="zzInfo.busiLisType == 'COMMON'">
                        <div class="col-md-11">
                            <label class="col-sm-2 control-label">组织机构代码影印件：</label>
                            <div class="col-sm-2" v-if="!zzInfo.oocPdfName">
                            	<img class="img-thumbnail" id="oocPic" :src="zzInfo.oocPic" @click="showVoucherPic('#oocPic')">
                            </div>
                            <div class="col-sm-2" v-if="zzInfo.oocPdfName">
                            	<a id="pdfPic2" :href="zzInfo.oocPic" target="_blank">
									<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
								</a>
								<span class="dib" type="text" id="pdfName2" v-html="zzInfo.oocPdfName"></span>
                            </div> 
                            <div class="col-sm-8 certifi-info">
                            <div class="form-group ">
		                        <div class="col-md-11">
		                            <label class="col-sm-4 control-label"><span class="text-red">*</span>机构代码：</label>
		                            <div class="col-sm-5" ><input name="orgCode" class="form-control help-block" v-model="info.orgCode" maxlength="50"></div>                            
		                        </div>
	                        </div>
	                        <div class="form-group ">
		                        <div class="col-md-11">
		                            <label class="col-sm-4 control-label">机构名称 ：</label>
		                            <div class="col-sm-5" ><input name="orgName" class="form-control help-block" v-model="info.orgName" maxlength="50"></div>                            
		                        </div>
	                        </div>
	                        <div class="form-group ">
		                        <div class="col-md-11">
		                            <label class="col-sm-4 control-label">机构地址：</label>
		                            <div class="col-sm-5" ><input name="orgLocation" class="form-control help-block" v-model="info.orgLocation" maxlength="50"></div>                            
		                        </div>
	                        </div>
	                        <div class="form-group ">
		                        <div class="col-md-11">
		                            <label class="col-sm-4 control-label">成立日期：</label>
		                            <div class="col-sm-5" ><input name="orgCdate" class="form-control help-block input-datetext" v-model="info.orgCdate"></div>                            
		                        </div>
	                        </div>
	                        <div class="form-group ">
		                        <div class="col-md-11">
		                            <label class="col-sm-4 control-label"><span class="text-red">*</span>营业期限：</label>
		                            <div class="col-sm-5" ><input name="orgLimit" class="form-control help-block input-datetext" v-model="info.orgLimit"></div>                            
		                        </div>
	                        </div>
                            </div>                                                                                 
                        </div>
                      </div>
                      <div class="btn-list"  v-if="audit.status == 'WAIT_APPROVE'">
                      		<button type="submit" id="save" class="btn-yes btn">通过</button>
                      		<button type="button" class="btn btn-no" @click="examineEvent('no')">驳回</button>
                      </div>
                      </form>                      
                      <div class="audit-opinion" v-if="audit.status !== 'WAIT_APPROVE'">
                      	<div class="form-group ">
	                    	<div class="col-md-11">
	                            <label class="col-sm-1 control-label">审核人：</label>
	                            <div class="col-sm-4">
	                            	<span v-html="audit.approvePartyName?audit.approvePartyName:'--'"></span>                            	
	                            </div>                            
	                        </div>
	                      </div>
	                      <div class="form-group ">
	                    	<div class="col-md-11">
	                            <label class="col-sm-1 control-label">审核时间：</label>
	                            <div class="col-sm-4" >
	                            	<span v-html="audit.lastUpdateDate?audit.lastUpdateDate:'--'"></span>                            	
	                            </div>                            
	                        </div>
	                      </div>
	                      <div class="form-group ">
	                    	<div class="col-md-11">
	                            <label class="col-sm-1 control-label">审核意见：</label>
	                            <div class="col-sm-4" >
	                            	<span v-html="audit.reason?audit.reason:'--'"></span>                            	
	                            </div>                            
	                        </div>
	                      </div>
                      </div>
                      <div class="reason-wrap">
                      	<p>请输入审核<span class="examine-type">不通过<span>的原因：</p>
                      	<div class='reason-box'>
                      		<textarea id='remark' onkeyup="setLetterNum(this)" maxlength="100"></textarea>
                      		<span class="num-wrap"><span class="letter-num">0</span>/100</span>
                      	</div>
                      </div>
                    </div>                                        	              	 
	            </div>
				</section>
          	</div>
        	</section> 
      	</div>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/bootstrapValidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${ctx}/js/app/examineQualification.js"></script>

</body>
</html>