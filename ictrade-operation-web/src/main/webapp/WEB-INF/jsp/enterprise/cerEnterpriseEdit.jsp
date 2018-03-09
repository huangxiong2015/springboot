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

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

	<c:import url ="/WEB-INF/jsp/include/main/constants.jsp"/>
	<title>认证企业账户编辑</title>
	<link rel="stylesheet" href="${ctx}/css/app/account_Manage.css"/>
	<link rel="stylesheet" href="${ctx}/css/common/component.css"/>
	<link rel="stylesheet" href="${ctx}/css/app/firmAccountDetail.css"/>
	<!-- jQuery 1.11.3 -->
	<link rel="stylesheet" href="${ctx}/css/app/enterpriseDetails.css"></link>
<%-- 	<link rel="stylesheet" href="${ctx}/js/lib/laydate/need/laydate.css"></link>
	<link rel="stylesheet" href="${ctx}/js/lib/laydate/skins/dahong/laydate.css"></link>
	<script type="text/javascript" src="${ctx}/js/lib/laydate/laydate.js"></script> --%>

<style type="text/css">
 	.condition_inquery{background-color:#c11f2e;color:#fff;padding:10px 10px;font-size:14px;border-radius:3px;}
	.col_bt_time input{width:37%;}
  	.col_bt label,.col_bt_time label{text-align:right;}
  	.col_input_2{width:69%;}
  	.col_input_3{width:59%;}
  	.col_input_4{width:49%;}
  	.mt25{margin-top:25px;}
  	.content-wrapper .content-header .pitch_tab {border-top: 0px;}
.qualifications  .dd2 span.dib{max-width: 160px; white-space: nowrap; overflow: hidden;text-overflow: ellipsis;margin: 14px 10px 0 10px;}
  	.ms-controller{visibility: hidden;}
.qualifications .dd2 img.pdf_icon{border: none;margin-top:10px;width:32px;height:32px;}
	.cur{cursor:pointer;}
	.qualifications input { width: 300px;}
	#remark { padding: 10px; width: 300px; height: 100px; vertical-align: top;}
	.sh_label { vertical-align: top;}
	.btns { padding-bottom: 60px; padding-left: 30px;}
	em[class*='error'], i[class*='error'] { max-width: 500px;}
	.info_new .dataTime em[class*='error'] {left: 933px;top: 230px;}
	.info_new .dataTime2 em[class*='error'] {left: 933px;top: 541px;}
	.info_new .code em[class*='error'] {left: 933px;top: 21px;}
	.info_new .organization em[class*='error']{left: 933px;top: 333px;}
	.qualifications .status { background: green;text-align: center; padding: 0 20px; color: #fff;}
 .getother { top: 54px;left: 341px;}
 em[for="otherAttr"] {top: 44px;left: 463px;}
 </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<!---index header  start   ---->
	<jsp:directive.include file="../include/main/header.jsp" />
	<!---index header  end   ---->
	 <!---index nav  start   ---->
	<jsp:directive.include file="../include/main/menu.jsp" />
	<!---index nav  end   ---->

	  <!-- 右边内容显示 -->
	  <div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<a href="#" class="header_tab pitch_tab">认证编辑企业信息</a>
		</section>
	    <!-- Main content -->
		<section ms-controller="list" class="content container-fluid ms-controller">
		<input type="hidden" id="id" value="${id }" />
		<input type="hidden" id="userId" value="${userId }" />
		<!-- <input type="hidden" id="mail" name="mail" :duplex="@toHtml(@list.mail)" /> -->
		<form action="#" id="infoAllForm">
		<form action="#" id="infoForm">
			<h2 class="input-title">企业信息</h2>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>公司名称：</label>
				</div>
				<div class="col-xs-11">
				   <input class="col_input_1" type="hidden" id="registeAddr" :duplex="@toHtml(@list.registeAddr)"   >
					<input class="col_input_1" type="hidden" id="busiLisType" :duplex="@toHtml(@list.busiLisType)"  >
					<input class="col_input_1" type="text" id="Entname" name="Entname"  >
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>公司类型：</label>
				</div>
				<div class="col-xs-11 c5e">
					<select id="corCategory" name="corCategory" ms-duplex="@list.map.CORPORATION_CATEGORY_ID" class="companyType f12 pl10 g3"  >
						<option value="">请选择</option>
						<option :for="(index,ele) in @corCategoryList" :attr="{value:ele.categoryId}" :text="ele.categoryName" ></option>
					</select>
				</div>
			</div>
			<div class="row mt20 dn">
				<div class="col-xs-1 tr"></div>
				<div class="col-xs-11">
					<input class="col_input_1" type="text" id="corCategoryOther"
						name="corCategoryOther" placeholder="请填写公司类型">
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>所属行业：</label>
				</div>
				<div class="col-xs-11 c5e">
					<input id="industryCategory" name="industryCategory" type="hidden" :duplex="@toHtml(@list.map.INDUSTRY_CATEGORY_ID)"  />
					<!-- industryList   categoryId -->
					<div id="company_genre" class="company_genre l mt10">
						<div class="dib pl5 f12 company_type mb5" :for="(index,ele) in @industryList">
							<span class="check-box-white">
								<input type="checkbox" :attr="{value:ele.categoryId}"/>
							</span>
							<i class="ml10 g3">{{ele.categoryName}}</i>
						</div>
					</div>
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>公司地址：</label>
				</div>
				<div class="col-xs-11 c5e">
					<select id="Entprovince" name="Entprovince" style="width: 100px;" ms-controller='provinceList' ms-duplex="@activeProvince" data-duplex-changed="@changeProvince();"  >
						<option value="">省/直辖市</option>
						<option :for="(index,ele) in @list" :attr="{value:ele.id}" :text="ele.name"></option>
					</select>

					<select id="Entcity" name="Entprovince" style="width: 100px; margin-left: 15px" ms-controller='cityList' ms-duplex="@activeCity" data-duplex-changed="@changeCity();"  >
						<option value="">市</option>
						<option :for="(index,ele) in @list" :attr="{value:ele.id}" :text="ele.name"></option>
					</select>
					<select id="Entcountry" name="Entprovince" style="width: 100px; margin-left: 15px" ms-controller=countryList ms-duplex="@activeCountry"   >
						<option value="">区/县</option>
						<option :for="(index,ele) in @list" :attr="{value:ele.id}" :text="ele.name"></option>
					</select>
				</div>
				<div class="col-xs-1 tr">
				</div>
				<div class="col-xs-11 c5e">
					<input class="col_input_1" type="text" id="Entaddress" name="Entaddress" maxlength="250">
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>邓氏编码：</label>
				</div>
				<div class="col-xs-11 c5e">
					<input class="col_input_1" type="text" id="dCode" name="dCode"/>
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label>公司官网：</label>
				</div>
				<div class="col-xs-11 c5e">
					<input class="col_input_1" type="text" id="webSite" name="webSite" maxlength="50" / >
				</div>
			</div>
			<div class="row mt20">
				<div class="col-xs-1 tr">
					<label>YKY客户编码：</label>
				</div>
				<div class="col-xs-11 c5e">
					<input class="col_input_1" type="text" id="partyCode" name="partyCode" class="partyCode" maxlength="50" / >
					<span class="lh32 hasPartyCode" :text="@toHtml(@list.partyCode)" id="isPartyCode"></span>
					<input class="col_input_1" type="hidden" id="partyCodeNew" maxlength="50" / >
				</div>
			</div>
			<div class="row mt20 saveCompany">
				<div style="margin-left:10%;">
					<a href="javascript:void(0);" onclick="save();" style="background: #b1191a;color: #fff!important;padding: 10px 15px;border-radius: 3px;">保存</a>
				 </div>
			</div>
			
			
			<!-- <div class="row mt20">
				<div class="col-xs-1 tr">
					<label>公司简介：</label>
				</div>
				<div class="col-xs-11 c5e">
					<input class="col_input_1" type="text" id="description" name="description" maxlength="200" :duplex="@toHtml(@list.description)" / >
				</div>
			</div> -->
			<div class=" ms-controller" ms-controller=data>
			<div class="row mt30 qualifications">
				<div class="col-xs-12">
				<p class="info">
						<span class="l dib mr40">企业资质</span>
						<span class="l dib status" :if="@companyInfo.activeStatus == 'PARTY_NOT_VERIFIED'">未认证</span>
						<span class="l dib status" :if="@companyInfo.activeStatus == 'REJECTED'">不通过</span>
						<span class="l dib status" :if="@companyInfo.activeStatus == 'WAIT_APPROVE'">待审核</span>
						<span class="l dib status" :if="@companyInfo.activeStatus == 'INVALID'">失效</span>
						<span class="l dib status" :if="@companyInfo.activeStatus == 'PARTY_VERIFIED'">通过</span>
					</p>
				</div>
				<div class="col-xs-12 mt20">
					<label class="mr10">公司注册地：</label><span id="regAddr">{{@zzInfo.regAddr | regAddrName}}</span>
				</div>
				<div class="col-xs-12 mt20" id="business">
					<input type="radio" name="busiLisType" class="input_radio" value="COMMON" disabled :duplex="@zzInfo.busiLisType"/><span class="sp1">普通营业执照</span>
					<input type="radio" name="busiLisType" class="input_radio ml70" value="3-TO-1" disabled :duplex="@zzInfo.busiLisType"/><span class="sp2">企业三证合一</span>
					<span class="sp3">注：(自2015年10月1日起登记机关颁发的含统一信用代码的营业执照) </span>
				</div>
				<div class="col-xs-12 mt30 general" id="busiLicPic">
					<div class="dd2 pt20">
						<label class="mt10" :if="@zzInfo.regAddr == '0'">营业执照影印件</label>
						<span class="mt10" :if="@zzInfo.regAddr == '1'" >注册证书（CR）</span>
						<span class="mt10" :if="@zzInfo.regAddr == '2'" >盈利事業登記證</span>
						<span class="mt10" :if="@zzInfo.regAddr == '3'" >CERTIFICATE OF INCORPORATION</span>
					</div>					
					<div :if="!@zzInfo.busiPdfName" class="dd2 PicImgurl0 pt20">
						<div>
							<img :attr="{src: @zzInfo.busiLicPic}" onclick="showVoucherPic('busiLicPicImg');" />
						</div>
						<div class="dn">
							<img :attr="{src: @zzInfo.busiLicPic}" id="busiLicPicImg" />
						</div>
					</div>
					<div :if="@zzInfo.busiPdfName" class="dd2 pdffile0 mt10">
						<a id="pdfPic0" :attr="{href: @zzInfo.busiLicPic}" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text" id="pdfName0">{{@zzInfo.busiPdfName}}</span> 
					</div>
					<div class="l info_new">
	                     <div class="" :if="@zzInfo.busiLisType == 'COMMON' || @zzInfo.busiLisType == '3-TO-1'">
	                        <div class="mt20 item code">
	                            <label class="l"><span style="color:red;float:none;">*</span>统一社会信用代码：</label><input id="creditCode" name="creditCode" type="text" :duplex="@info.socialCode" />
	                        </div>
	                        <div class="mt20 item" >
	                            <label class="l">企业名称（全称）：</label><input type="text" :duplex="@info.entName" />
	                        </div>
	                        <div class="mt20 item" >
	                            <label class="l">住所：</label><input type="text" :duplex="@info.location" />
	                        </div>
	                        <div class="mt20 item" >
	                            <label class="l">经营范围：</label><input type="text" :duplex="@info.busiRange" />
	                        </div>
	                        <div :if="@zzInfo.busiLisType == '3-TO-1'">
		                        <div class="mt20 item">
		                            <label class="l">纳税人识别号 :</label><input type="text" :duplex="@info.faxCode" />
		                        </div>
		                        <div class="mt20 item">
		                            <label class="l">纳税人名称 ：</label><input type="text" :duplex="@info.faxName" />
		                        </div>
		                        <div class="mt20 item organization">
		                            <label class="l"><span style="color:red;float:none;">*</span>组织机构代码 ：</label><input id="organizationCode" name="organizationCode"  type="text" :duplex="@info.orgCode" />
		                        </div>
		                        <div class="mt20 item">
		                            <label class="l">机构名称 ：</label><input type="text" :duplex="@info.orgName" />
		                        </div>
		                        <div class="mt20 item">
		                            <label class="l">机构地址：</label><input type="text" :duplex="@info.orgLocation" />
		                        </div>
		                        <div class="mt20 item">
		                            <label class="l">成立日期：</label><input type="text" :duplex="@info.orgCdate" onclick="laydate();"/>
		                        </div>
		                        <div class="mt20 item dataTime2">
		                            <label class="l"><span style="color:red;float:none;">*</span>营业期限：</label><input id="businessTerm" name="businessTerm" type="text" :duplex="@info.orgLimit" onclick="laydate();"/>
		                        </div>
	                        </div>
	                     </div>
	                     <div :if="@zzInfo.busiLisType == 'HK-CODE'">
	                        <div class="mt20 item">
	                            <label class="l">公司名称：</label><input type="text" :duplex="@info.hkEntName" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">签发日期：</label><input type="text" :duplex="@info.hkSignCdate" onclick="laydate();"/> 
	                        </div>
			         	</div>
	                     <div :if="@zzInfo.busiLisType == 'TW-CODE'">
	                        <div class="mt20 item">
	                            <label class="l">公司名稱：</label><input type="text" :duplex="@info.twFaxEntName" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">簽發日期：</label><input type="text" :duplex="@info.twSignCdate" onclick="laydate();"/>
	                        </div>
			         	</div>
	                     <div :if="@zzInfo.busiLisType == 'ABROAD-CODE'">
	                        <div class="mt20 item">
	                            <label class="l">Company Name：</label><input type="text" :duplex="@info.abroadEntName" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">Company Number：</label><input type="text" :duplex="@info.abroadEntNum" />
	                        </div>
			         	</div>
                    </div>
				</div>
				<div class="col-xs-12 general" id="taxRegPic" :if="@zzInfo.busiLisType !== '3-TO-1' && @zzInfo.busiLisType !== 'ABROAD-CODE'">
				
					<div class="dd2 pt20">
						<label class="mt10" :if="@zzInfo.regAddr == '0'">税务登记证影印件</label>
						<span class="mt10" :if="@zzInfo.regAddr == '1'">商业登记证（BR）</span>
						<span class="mt10" :if="@zzInfo.regAddr == '2'">稅籍登记证</span>
					</div>				
					<div :if="!@zzInfo.taxPdfName" class="dd2 PicImgurl1 pt20">
						<div>
							<img :attr="{src: @zzInfo.taxPic}"  onclick="showVoucherPic('taxRegPicImg')" />
						</div>
						<div class="dn">
							<img :attr="{src: @zzInfo.taxPic}" id="taxRegPicImg" />
						</div>
					</div>
					<div :if="@zzInfo.taxPdfName" class="dd2 pdffile1 mt10">
						<a id="pdfPic1" :attr="{href: @zzInfo.taxPic}" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text" id="pdfName1">{{ @zzInfo.taxPdfName }}</span> 
					</div>
					<div class="dn"></div>
					<div class="l info_new">
                     	<div :if="@zzInfo.busiLisType == 'COMMON'">
	                        <div class="mt20 item">
	                            <label class="l">纳税人识别号 :</label><input type="text" :duplex="@info.faxCode" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">纳税人名称 ：</label><input type="text" :duplex="@info.faxName" />
	                        </div>
                        </div>
                        <div :if="@zzInfo.busiLisType == 'HK-CODE'">
	                        <div class="mt20 item">
	                            <label class="l">业务所用名称 :</label><input type="text" :duplex="@info.hkBusiName" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">地址 ：</label><input type="text" :duplex="@info.hkAddr" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">生效日期 ：</label><input type="text" :duplex="@info.hkEffectiveDate"  onclick="laydate();"/>
	                        </div>
	                    </div>
                        <div :if="@zzInfo.busiLisType == 'TW-CODE'">
	                        <div class="mt20 item">
	                            <label class="l">公司名稱 :</label><input type="text" :duplex="@info.twEntName" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">簽發日期 ：</label><input type="text" :duplex="@info.twSignCdate"  onclick="laydate();"/>
	                        </div>
	              xx          <div class="mt20 item">
	                            <label class="l">稅務編碼：</label><input type="text" :duplex="@info.twFaxCode" />
	                        </div>
	                    </div>
                     </div>
				</div>
				<div :if="@zzInfo.busiLisType == 'COMMON'" class="col-xs-12 general" id="occPic">
					<div class="dd2 pt20">
						<label class="mt10">组织机构代码影印件</label>
					</div>
					
					<div :if="!@zzInfo.oocPdfName" class="dd2 PicImgurl2 mt10">
							<div>
								<img :attr="{src: @zzInfo.oocPic}" onclick="showVoucherPic('occPicImg')" />
							</div>
							<div class="dn">
								<img :attr="{src: @zzInfo.oocPic}" id="occPicImg" />
							</div>
					</div>
					<div :if="@zzInfo.oocPdfName" class="dd2 pdffile2 mt10">
						<a id="pdfPic2" :attr="{href: @zzInfo.oocPic}" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text" id="pdfName2">{{ @zzInfo.oocPdfName }}</span> 
					</div>
					
					<div class="l info_new">
						<div>
	                        <div class="mt20 item code">
		                            <label class="l"><span style="color:red;float:none;">*</span>组织机构代码 ：</label><input id="organizationCode" name="organizationCode" type="text" :duplex="@info.orgCode" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">机构名称 ：</label><input type="text" :duplex="@info.orgName" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">机构地址：</label><input type="text" :duplex="@info.orgLocation" />
	                        </div>
	                        <div class="mt20 item">
	                            <label class="l">成立日期：</label><input type="text" :duplex="@info.orgCdate" onclick="laydate();"/>
	                        </div>
	                        <div class="mt20 item dataTime">
		                            <label class="l"><span style="color:red;float:none;">*</span>营业期限：</label><input id="businessTerm" name="businessTerm":duplex="@info.orgLimit" onclick="laydate();"/>
	                        </div>
	                    </div>
                	</div>
				</div>
			</div>
			<div class="row mt30 record mb20">
				<div class="col-xs-12">
					<p class="info">子账号管理: 
						<span :if="@companyInfo.accountStatus == 'ACCOUNT_VERIFIED'">通过</span>
						<span :if="@companyInfo.accountStatus == 'ACCOUNT_REJECTED'">不通过</span>
						<span :if="@companyInfo.accountStatus == 'ACCOUNT_WAIT_APPROVE'">待审核</span>
						<span :if="@companyInfo.accountStatus == 'ACCOUNT_NOT_VERIFIED'">未开通</span>
					</p>
				</div>
				<div :if="@zzinfo.loaPdfName">
					<label style="display:inlint-block; width: 100px;">企业认证授权书 :</label>
					<div :if="!@zzInfo.loaPdfName" class="dd2 PicImgurl2 mt10"
						style="display: inline-block;">
						<div class="dd2">
							<img :attr="{src: @zzInfo.loaPic}"
								onclick="showVoucherPic('loaPicImg')"
								style="width: 160px; height: 120px; border: 1px solid #D4D4D4;" />
						</div>
						<div class="dn">
							<img :attr="{src: @zzInfo.loaPic}" id="loaPicImg" />
						</div>
					</div>
					<div :if="@zzInfo.loaPdfName" class="dd2 pdffile3 mt10"
						style="display: inline-block;">
						<a id="pdfPic3" :attr="{href: @zzInfo.loaPic}" target="_blank">
							<img width="32px" height="32px" alt=""
							src="${ctx }/images/pdf.png" class="pdf_icon">
						</a> <span class="dib" type="text" id="pdfName3"
							style="margin-left: 20px;">{{ @zzInfo.loaPdfName }}</span>
					</div>
				</div>
			</div>
			</div>
			<h2 class="input-title">修改日志</h2>
			<div class="row mt20 mb10">
				<div class="col-xs-1 tr">
					<label><span class="cRed">*</span>修改说明：</label>
				</div>
				<div class="col-xs-11 c5e">
					<textarea id="reason" class="col_input_1" name="reason"  rows="5" cols="80"></textarea>
				</div>
			</div>
			<div class="row mt20 mb10">
				<div class="col-xs-1 tr">
					<label>认证备注：</label>
				</div>
				<div class="col-xs-11 c5e">
					<textarea id="comments" class="col_input_1" name="comments"  rows="5" cols="80"></textarea>
				</div>
			</div>

			<div id="infoSuc"  class="infoSuc tc" style="display:none">
				<p class="g3 f16"><span class="icon-right-c"></span>资料修改成功</p>
			</div>
		</section>
		<!-- /.content -->
		<div style="margin-left:10%;padding-bottom:150px;margin-top:35px;">
			<a href="javascript:void(0);" onclick="saveAll();" style="background: #b1191a;color: #fff!important;padding: 10px 15px;border-radius: 3px;">保存</a>
		 	<a href="${ctx }/enterprise.htm?action=certificationEnt" class="reset_btn">取消</a> 
		 </div>
	 	</form>
	 	</form>
	</div>
<%-- 	<input type="hidden" value="${userId}" id="applyUserId"> --%>
		<script type="text/javascript">
		var selectCatid = "225,227";
	</script>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/main/footer.jsp" />
	<!---footer  end   ---->
	<script type="text/javascript" src="${ctx}/js/lib/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/js/common/add.validate.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
	<%-- <script type="text/javascript" src="${ctx}/js/oss/oss_credence.js"></script> --%>
	<script type="text/javascript" src="${ctx}/js/oss/oss_zz.js"></script>
	<%-- <script type="text/javascript" src="${ctx}/js/common/component.js"></script> --%>
	<script type="text/javascript" src="${ctx}/js/enterprise/cerEnterpriseEdit.js"></script>
	<script type="text/javascript" src="${ctx}/js/enterprise/certified.js"></script>
</div>
<!-- ./wrapper -->
<script>
	var showData = avalon.define({
	    $id: "data",
		companyInfo: {
			id: '',
			name: '',
			corCategory: '',
			industryCategory: '',
			industryOther: '',
			provinceName: '',
			cityName: '',
			countryName: '',
			address: '',
			dCode: '',
			webSite: '',
			partyCode: '',
			accountStatus: '',
			activeStatus: '',
			isVipCenter: '',
			reason:'',
			comments:''	
		},
		zzInfo: {
			regAddr: '',
			busiLisType: '', //三证类型
			busiLicPic: '', //营业执照
			oocPic: '', //组织机构代码
			taxPic: '', //税务登记
			busiPdfName: '', //营业执照pdf
			oocPdfName: '', //组织机构代码pdf
			taxPdfName: '', //税务登记pdf
			loaPic: '', //授权书
			loaPdfName: '' //授权书pdf
		},
		info:{
			socialCode: '',
			entName: '',
			location: '',
			busiRange: '',
			faxCode: '',
			faxName: '',
			orgCode: '',
			orgName: '',
			orgLocation: '',
			orgCdate: '',
			orgLimit: '',
			hkEntName: '',  //香港-公司名称
			hkSignCdate: '', //香港-签发日期
			hkBusiName: '', //香港-业务所用名称
			hkAddr: '', //香港-地址
			hkEffectiveDate: '', //生效日期
			twEntName: '', //台湾-公司名称
			twSignCdate: '', //台湾-签发日期
			twFaxEntName: '', //台湾-税籍登记证-公司名称
			twFaxSignCdate: '', //台湾-税籍登记证-签发日期
			twFaxCode: '',  //台湾-税务编码
			abroadEntName: '', //ABROAD_ENT_NAME
			abroadEntNum: '' //ABROAD_ENT_NUM
		},
		shType: '',
		auditList:[],
		busiLisType:"",
		toHtml: function(data){
			if(data=='undefined' || !data){
				return "--";
			}else{
				return data;
			}
		},
		showData:function(index,type){
			if(type=='part'){
				return index<4
			}
			if(type=='all'){				
				$('.Infologomin').show();
				$('.look-morelog .look').hide();
			}
		},
		remark: '',
		shType: ''
	})
	
	avalon.filters.corCategoryName = function(str) {
		var corCategoryData = {
				2000: 'OEM(原始设备制造商)',
				2001: 'EMS/CM(电子制造服务商)',
				2002: 'IDH(独立设计公司)',
				2003: 'ODM(原始设计制造商)',
				2004: 'Reseller(Trade/Disty) 贸易商/代理商',
				2005: '大学/研究所',
				2006: '个人'
		}
		return str ? corCategoryData[str] : '-';
	}
	
	avalon.filters.industryCategoryName = function(str) {
		var returnStr = '';
		var list = str.split(',');
		var industryCategoryData = {
				1000: '安防',
				1001: '汽车',
				1002: '通信设备',
				1003: '企业系统',
				1004: '家用电器',
				1005: '工业应用',
				1006: '照明工业',
				1007: '个人电子产品',
				1008: '其他'
		}
		
		$.each(list, function(i, item) {
			if(item !== '1008') {
				if(i === list.length - 1) {
					returnStr += industryCategoryData[item];
				}else {
					returnStr += industryCategoryData[item] + '+';
				}
			}else {
				if(i === list.length - 1) {
					returnStr += industryCategoryData[item] + '('+ this.companyInfo.industryOther +')';
				}else {
					returnStr += industryCategoryData[item] + '('+ this.companyInfo.industryOther +')' + '+';
				}
			}
		})
		
		return str ? returnStr : '-';
	}
	
	avalon.filters.regAddrName = function(str) {
		var returnStr = '';
		switch(str) {
			case '0':
				returnStr = '大陆';
				break;
			case '1':
				returnStr = '香港';
				break;
			case '2':
				returnStr = '台湾';
				break;
			case '3':
				returnStr = '境外';
				break;
		}
		return returnStr;
	}
</script>
</body>
</html>
