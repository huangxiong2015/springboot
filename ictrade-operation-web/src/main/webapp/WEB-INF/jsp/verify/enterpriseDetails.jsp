<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }"
	scope="request" />

<!--[if IE 8 ]>    <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="zh-CN"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->

<head>
<!-- jQuery 1.11.3 -->
<c:import url="/WEB-INF/jsp/include/main/constants.jsp" />
<link rel="stylesheet" href="${ctx}/css/app/enterpriseDetails.css"></link>
<title>企业账号审核详情</title>

<style type="text/css">
.condition_inquery {
	background-color: #c11f2e;
	color: #fff;
	padding: 10px 10px;
	font-size: 14px;
	border-radius: 3px;
}

.col_bt_time input {
	width: 37%;
}

.col_bt label, .col_bt_time label {
	text-align: right;
}

.col_input_2 {
	width: 69%;
}

.col_input_3 {
	width: 59%;
}

.col_input_4 {
	width: 49%;
}

.mt25 {
	margin-top: 25px;
}

.content-wrapper .content-header .pitch_tab {
	border-top: 0px;
}

.qualifications  .dd2 span.dib {
	max-width: 160px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	margin: 14px 10px 0 10px;
}

.ms-controller {
	visibility: hidden;
}

.qualifications .dd2 img.pdf_icon {
	border: none;
	margin-top: 10px;
	width: 32px;
	height: 32px;
}

.cur {
	cursor: pointer;
}

.info_new {
	float: left;
}

.sz-wrap {
	margin-left: 10px;
	width: 97%;
	border-bottom: 1px solid #e4e4e4;
}

.general {
	width: 100%;
	margin-left: 0;
}

.row {
	margin-left: 0;
	margin-right: 0;
}
.qualifications  .dd2 + .dd2 {
	width: 200px;
}
.info_new .item, .info_new label {
	line-height: 30px;
}
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
		<div class="content-wrapper ms-controller" ms-controller=data>
			<section class="content-header">
				<a href="#" class="header_tab pitch_tab">企业账号审核详情</a>
			</section>
			<section class="content container-fluid">
				<div class="row mt20 information">
					<div class="col-xs-12">
						<p class="info">企业信息</p>
					</div>
					<div class="col-xs-12 mt20">
						<label>公司名称：</label><span>{{@companyInfo.name}}</span>
					</div>
					<div class="col-xs-12 mt20">
						<label>公司类型：</label><span id="corCategory">{{@companyInfo.corCategory
							| corCategoryName}}</span>
					</div>
					<div class="col-xs-12 mt20">
						<label>所属行业：</label><span id="industryCategory">{{@companyInfo.industryCategory
							| industryCategoryName }}</span>
					</div>
					<div class="col-xs-12 mt20">
						<label>公司地址：</label><span>{{@companyInfo.provinceName +
							@companyInfo.cityName + @companyInfo.countryName +
							@companyInfo.address}}</span>
					</div>
					<div class="col-xs-12 mt20">
						<label>邓氏编码：</label><span>{{@companyInfo.dCode}}</span>
					</div>
					<div class="col-xs-12 mt20">
						<label>公司官网：</label><span>{{@companyInfo.webSite}}</span>
					</div>
					<div class="col-xs-12 mt20">
						<label>YKY客户编码：</label><span>{{@companyInfo.partyCode}}</span>
					</div>
				</div>
				<div :if="@shType=='ORG_DATA_REVIEW'" class="row mt30 qualifications">
					<div class="col-xs-12">
						<p class="info">
							企业资质： <span :if="@companyInfo.activeStatus == 'PARTY_VERIFIED'">通过</span>
							<span :if="@companyInfo.activeStatus == 'WAIT_APPROVE'">待审核</span>
							<span :if="@companyInfo.activeStatus == 'REJECTED'">不通过</span>
						</p>
					</div>
					<div class="col-xs-12 mt20">
						<label class="mr10">公司注册地：</label><span id="regAddr">{{@zzInfo.regAddr
							| regAddrName}}</span>
					</div>
					<div class="col-xs-12 mt20" id="business"
						:if="@zzInfo.regAddr == 0">
						<input type="radio" name="busiLisType" class="input_radio"
							value="COMMON" disabled :duplex="@zzInfo.busiLisType" /><span
							class="sp1">普通营业执照</span> <input type="radio" name="busiLisType"
							class="input_radio ml70" value="3-TO-1" disabled
							:duplex="@zzInfo.busiLisType" /><span class="sp2">企业三证合一</span> <span
							class="sp3">注：(自2015年10月1日起登记机关颁发的含统一信用代码的营业执照) </span>
					</div>
					<div class="sz-wrap fix">
						<div class="col-xs-12 mt30 general" id="busiLicPic">
							<div class="dd2 pt20">
								<label class="mt10" :if="@zzInfo.regAddr == '0'">营业执照影印件：</label>
								<span class="mt10" :if="@zzInfo.regAddr == '1'">注册证书（CR）：</span>
								<span class="mt10" :if="@zzInfo.regAddr == '2'">盈利事業登記證：</span>
								<span class="mt10" :if="@zzInfo.regAddr == '3'">CERTIFICATE OF INCORPORATION：</span>
							</div>
							<div :if="!@zzInfo.busiPdfName" class="dd2 PicImgurl0 pt20">
								<div>
									<img :attr="{src: @zzInfo.busiLicPic}"
										onclick="showVoucherPic('busiLicPicImg');" />
								</div>
								<div class="dn">
									<img :attr="{src: @zzInfo.busiLicPic}" id="busiLicPicImg" />
								</div>
							</div>
							<div :if="@zzInfo.busiPdfName" class="dd2 pdffile0 mt10">
								<a id="pdfPic0" :attr="{href: @zzInfo.busiLicPic}"
									target="_blank"> <img width="32px" height="32px" alt=""
									src="${ctx }/images/pdf.png" class="pdf_icon">
								</a> <span class="dib" type="text" id="pdfName0">{{@zzInfo.busiPdfName}}</span>
							</div>
							<div class="info_new">
								<div class=""
									:if="@zzInfo.busiLisType == 'COMMON' || @zzInfo.busiLisType == '3-TO-1'">
									<div class="mt20 item fix">
										<label class="l">统一社会信用代码：</label><span>{{@toHtml(@info.socialCode)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">企业名称（全称）：</label><span>{{@toHtml(@info.entName)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">住所：</label><span>{{@toHtml(@info.location)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">经营范围：</label><span>{{@toHtml(@info.busiRange)}}</span>
									</div>
									<div :if="@zzInfo.busiLisType == '3-TO-1'">
										<div class="mt20 item fix">
											<label class="l">纳税人识别号 :</label><span>{{@toHtml(@info.faxCode)}}</span>
										</div>
										<div class="mt20 item fix">
											<label class="l">纳税人名称 ：</label><span>{{@toHtml(@info.faxName)}}</span>
										</div>
										<div class="mt20 item fix">
											<label class="l">机构代码 ：</label><span>{{@toHtml(@info.orgCode)}}</span>
										</div>
										<div class="mt20 item fix">
											<label class="l">机构名称 ：</label><span>{{@toHtml(@info.orgName)}}</span>
										</div>
										<div class="mt20 item fix">
											<label class="l">机构地址：</label><span>{{@toHtml(@info.orgLocation)}}</span>
										</div>
										<div class="mt20 item fix">
											<label class="l">成立日期：</label><span>{{@toHtml(@info.orgCdate)}}</span>
										</div>
										<div class="mt20 item fix">
											<label class="l">营业期限：</label><span>{{@toHtml(@info.orgLimit)}}</span>
										</div>
									</div>
								</div>
								<div :if="@zzInfo.busiLisType == 'HK-CODE'">
									<div class="mt20 item fix">
										<label class="l">公司名称：</label><span>{{@toHtml(@info.hkEntName)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">签发日期：</label><span>{{@toHtml(@info.hkSignCdate)}}</span>
									</div>
								</div>
								<div :if="@zzInfo.busiLisType == 'TW-CODE'">
									<div class="mt20 item fix">
										<label class="l">公司名稱：</label><span>{{@toHtml(@info.twEntName)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">簽發日期：</label><span>{{@toHtml(@info.twSignCdate)}}</span>
									</div>
								</div>
								<div :if="@zzInfo.busiLisType == 'ABROAD-CODE'">
									<div class="mt20 item fix">
										<label class="l">Company Name：</label><span>{{@toHtml(@info.abroadEntName)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">Company Number：</label><span>{{@toHtml(@info.abroadEntNum)}}</span>
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 general" id="taxRegPic"
							:if="@zzInfo.busiLisType!=='3-TO-1' && @zzInfo.busiLisType!=='ABROAD-CODE'">

							<div class="dd2 pt20">
								<label class="mt10" :if="@zzInfo.regAddr == '0'">税务登记证影印件：</label>
								<span class="mt10" :if="@zzInfo.regAddr == '1'">商业登记证（BR）：</span>
								<span class="mt10" :if="@zzInfo.regAddr == '2'">稅籍登记证：</span>
							</div>
							<div :if="!@zzInfo.taxPdfName" class="dd2 PicImgurl1 pt20">
								<div>
									<img :attr="{src: @zzInfo.taxPic}"
										onclick="showVoucherPic('taxRegPicImg')" />
								</div>
								<div class="dn">
									<img :attr="{src: @zzInfo.taxPic}" id="taxRegPicImg" />
								</div>
							</div>
							<div :if="@zzInfo.taxPdfName" class="dd2 pdffile1 mt10">
								<a id="pdfPic1" :attr="{href: @zzInfo.taxPic}" target="_blank">
									<img width="32px" height="32px" alt=""
									src="${ctx }/images/pdf.png" class="pdf_icon">
								</a> <span class="dib" type="text" id="pdfName1">{{
									@zzInfo.taxPdfName }}</span>
							</div>
							<div class="dn"></div>
							<div class="info_new">
								<div :if="@zzInfo.busiLisType == 'COMMON'">
									<div class="mt20 item fix">
										<label class="l">纳税人识别号 :</label><span>{{@toHtml(@info.faxCode)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">纳税人名称 ：</label><span>{{@toHtml(@info.faxName)}}</span>
									</div>
								</div>
								<div :if="@zzInfo.busiLisType == 'HK-CODE'">
									<div class="mt20 item fix">
										<label class="l">业务所用名称 :</label><span>{{@toHtml(@info.hkBusiName)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">地址 ：</label><span>{{@toHtml(@info.hkAddr)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">生效日期 ：</label><span>{{@toHtml(@info.hkEffectiveDate)}}</span>
									</div>
								</div>
								<div :if="@zzInfo.busiLisType == 'TW-CODE'">
									<div class="mt20 item fix">
										<label class="l">公司名稱 :</label><span>{{@toHtml(@info.twFaxEntName)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">簽發日期 ：</label><span>{{@toHtml(@info.twFaxSignCdate)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">稅務編碼：</label><span>{{@toHtml(@info.twFaxCode)}}</span>
									</div>
								</div>
							</div>
						</div>
						<div :if="@zzInfo.busiLisType == 'COMMON'"
							class="col-xs-12 general" id="occPic">
							<div class="dd2 pt20">
								<label class="mt10">组织机构代码影印件：</label>
							</div>

							<div :if="!@zzInfo.oocPdfName" class="dd2 PicImgurl2 mt10">
								<div>
									<img :attr="{src: @zzInfo.oocPic}"
										onclick="showVoucherPic('occPicImg')" />
								</div>
								<div class="dn">
									<img :attr="{src: @zzInfo.oocPic}" id="occPicImg" />
								</div>
							</div>
							<div :if="@zzInfo.oocPdfName" class="dd2 pdffile2 mt10">
								<a id="pdfPic2" :attr="{href: @zzInfo.oocPic}" target="_blank"> <img width="32px"
									height="32px" alt="" src="${ctx }/images/pdf.png"
									class="pdf_icon">
								</a> <span class="dib" type="text" id="pdfName2">{{@zzInfo.oocPdfName}}</span>
							</div>

							<div class="info_new">
								<div>
									<div class="mt20 item fix">
										<label class="l">机构代码 ：</label><span>{{@toHtml(@info.orgCode)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">机构名称 ：</label><span>{{@toHtml(@info.orgName)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">机构地址：</label><span>{{@toHtml(@info.orgLocation)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">成立日期：</label><span>{{@toHtml(@info.orgCdate)}}</span>
									</div>
									<div class="mt20 item fix">
										<label class="l">营业期限：</label><span>{{@toHtml(@info.orgLimit)}}</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div :if="@shType=='ORG_PROXY_REVIEW'" class="row mt30 record mb20">
					<div class="col-xs-12">
						<p class="info">
							子账号管理：<span
								:if="@companyInfo.accountStatus == 'ACCOUNT_VERIFIED'">通过</span>
							<span :if="@companyInfo.accountStatus == 'ACCOUNT_REJECTED'">不通过</span>
							<span :if="@companyInfo.accountStatus == 'ACCOUNT_WAIT_APPROVE'">待审核</span>
						</p>
					</div>
					<div :if="@zzInfo.loaPic || @zzinfo.loaPdfName">
						<label class="mt10"
							style="display: inline-block; margin-top: 10px; width: 120px; font-size: 14px;">企业认证授权书
							:</label>
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
				<div class="row mt30 record mb20">
					<div class="col-xs-12 mb20">
						<p class="info">审核记录</p>
					</div>
					<div id="recordInfo">
						<div class="">
							<div class="col-xs-12 mt20">
								<label>审核人：</label><span>{{@audit.approvePartyName}}</span>
							</div>
							<div class="col-xs-12 mt20">
								<label>审核时间：</label><span>{{@audit.lastUpdateDate}}</span>
							</div>
							<div class="col-xs-12 mt20">
								<label>审核意见：</label><span>{{@audit.reason}}</span>
							</div>
						</div>
					<div class="content_div"></div>
				</div>
			</section>
		</div>
		<!-- 右边内容显示 -->
		<script type="text/javascript">
			var selectCatid = "251,252";
		</script>
		<jsp:directive.include file="../include/main/footer.jsp" />
	</div>
	<script type="text/javascript" src="${ctx}/js/app/enterpriseDetails.js"></script>
</body>
</html>
