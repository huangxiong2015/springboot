<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>企业用户详情</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/firmAccountDetailNew.css" />
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
<div class="content-wrapper" id="enterprise-list">
	<section class="content-header">
	  <h1>企业信息详情</h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active">企业信息详情</li>
	  </ol>
	</section> 
	<section id="enterprise-info" class="content">
		<div class="section-item">
    		<h5 class="title">基本信息</h5>
    		<div class="con">
    			<div class="row mb20">
    				<label for="">个人编码：</label>
    				<span>{{ basicInfo.id }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">企业编码：</label>
    				<span>{{ basicInfo.corporationId }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">公司名称：</label>
    				<span>{{ basicInfo.name }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">公司类型：</label>
    				<span>{{ categoryIdName }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">所属行业：</label>
    				<span>{{ industryCategoryName }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">公司地址：</label>
    				<span>{{ basicInfo.address }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">邓氏编码：</label>
    				<span>{{ basicInfo.dCode }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">公司官网：</label>
    				<span>{{ basicInfo.website }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">YKY客户编码：</label>
    				<span>{{ basicInfo.partyCode }}</span>
    			</div>
    		</div>
		</div>   	
		<div class="line"></div>	
    	<div class="section-item">
    		<h5 class="title">
    			企业资质
    			<em v-if="quaInfo.activeStatus == 'PARTY_NOT_VERIFIED'"  class="verify-status danger">未认证</em>
				<em v-if="quaInfo.activeStatus == 'WAIT_APPROVE'"  class="verify-status danger">待审核</em>
				<em v-if="quaInfo.activeStatus == 'REJECTED'"  class="verify-status danger">不通过</em>
				<em v-if="quaInfo.activeStatus == 'PARTY_VERIFIED'"  class="verify-status success">通过</em>
				<em v-if="quaInfo.activeStatus == 'INVALID'"  class="verify-status danger">已失效</em>
   			</h5>
    		<div class="con">
    			<div class="row mb20">
    				<label for="">公司注册地：</label>
    				<span>{{ regAddr }}</span>
    			</div>
    			<div v-if="quaInfo.regAddr == '0'" class="row mb20">
    				<label for="">营业执照类型：</label>
    				<span>{{ busilisType }}</span>
    			</div>
    			<!-- 大陆-普通营业执照 -->
    			<template v-if="quaInfo.regAddr == '0' && quaInfo.busilisType == 'COMMON'">
    				<div v-if="quaInfo.busiPdfName" class="row mb20">
	    				<label for="">营业执照：</label>
	    				<a :href="getShowImage(quaInfo.busilicPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{quaInfo.busiPdfName}}</span>
	    			</div>
    				<div v-if="!quaInfo.busiPdfName" class="row mb20">
	    				<label for="">营业执照：</label>
	    				<img src="" class="img-thumbnail" :src="getShowImage(quaInfo.busilicPic)" @click="showPic($event)">
	    			</div>
	    			<div v-if="quaInfo.taxPdfName" class="row mb20">
	    				<label for="">税务登记证：</label>
	    				<a :href="getShowImage(quaInfo.taxPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{quaInfo.taxPdfName}}</span>
	    			</div>
    				<div v-if="!quaInfo.taxPdfName" class="row mb20">
	    				<label for="">税务登记证：</label>
	    				<img src="" class="img-thumbnail" :src="getShowImage(quaInfo.taxPic)" @click="showPic($event)">
	    			</div>
	    			<div v-if="quaInfo.oocPdfName" class="row mb20">
	    				<label for="">组织机构代码证：</label>
	    				<a :href="getShowImage(quaInfo.oocPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{quaInfo.oocPdfName}}</span>
	    			</div>
    				<div v-if="!quaInfo.oocPdfName" class="row mb20">
	    				<label for="">组织机构代码证：</label>
	    				<img src="" class="img-thumbnail" :src="getShowImage(quaInfo.oocPic)" @click="showPic($event)">
	    			</div>
    			</template>
    			<!-- 大陆-三证合一 -->
    			<template v-if="quaInfo.regAddr == '0' && quaInfo.busilisType == '3-TO-1'">
    				<div v-if="quaInfo.busiPdfName" class="row mb20">
	    				<label for="">营业执照：</label>
	    				<a :href="getShowImage(quaInfo.busilicPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{quaInfo.busiPdfName}}</span>
	    			</div>
    				<div v-if="!quaInfo.busiPdfName" class="row mb20">
	    				<label for="">营业执照：</label>
	    				<img id="taxPic" src="" class="img-thumbnail" :src="getShowImage(quaInfo.busilicPic)" @click="showPic($event)">
	    			</div>
    			</template>
    			<!-- 香港 -->
    			<template v-if="quaInfo.regAddr == '1'">
    				<div v-if="quaInfo.busiPdfName" class="row mb20">
	    				<label for="">注册证书(CR)：</label>
	    				<a :href="getShowImage(quaInfo.busilicPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{quaInfo.busiPdfName}}</span>
	    			</div>
    				<div v-if="!quaInfo.busiPdfName" class="row mb20">
	    				<label for="">注册证书(CR)：</label>
	    				<img src="" class="img-thumbnail" :src="getShowImage(quaInfo.busilicPic)" @click="showPic($event)">
	    			</div>
	    			<div v-if="quaInfo.taxPdfName" class="row mb20">
	    				<label for="">商业登记证(BR)：</label>
	    				<a :href="getShowImage(quaInfo.taxPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{quaInfo.taxPdfName}}</span>
	    			</div>
    				<div v-if="!quaInfo.taxPdfName" class="row mb20">
	    				<label for="">商业登记证(BR)：</label>
	    				<img src="" class="img-thumbnail" :src="getShowImage(quaInfo.taxPic)" @click="showPic($event)">
	    			</div>
    			</template>
    			<!-- 台湾 -->
    			<template v-if="quaInfo.regAddr == '2'">
    				<div v-if="quaInfo.busiPdfName" class="row mb20">
	    				<label for="">盈利事業登記證：</label>
	    				<a :href="getShowImage(quaInfo.busilicPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{quaInfo.busiPdfName}}</span>
	    			</div>
    				<div v-if="!quaInfo.busiPdfName" class="row mb20">
	    				<label for="">盈利事業登記證：</label>
	    				<img src="" class="img-thumbnail" :src="getShowImage(quaInfo.busilicPic)" @click="showPic($event)">
	    			</div>
	    			<div v-if="quaInfo.taxPdfName" class="row mb20">
	    				<label for="">稅籍登記證：</label>
	    				<a :href="getShowImage(quaInfo.taxPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{quaInfo.taxPdfName}}</span>
	    			</div>
    				<div v-if="!quaInfo.taxPdfName" class="row mb20">
	    				<label for="">稅籍登記證：</label>
	    				<img src="" class="img-thumbnail" :src="getShowImage(quaInfo.taxPic)" @click="showPic($event)">
	    			</div>
    			</template>
    			<!-- 境外 -->
    			<template v-if="quaInfo.regAddr == '3'">
    				<div v-if="quaInfo.busiPdfName" class="row mb20">
	    				<label for="">CERTIFICATE OF INCORPORATION：</label>
	    				<a :href="getShowImage(quaInfo.busilicPic)" target="_blank">
							<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
						</a>
						<span class="dib" type="text">{{quaInfo.busiPdfName}}</span>
	    			</div>
    				<div v-if="!quaInfo.busiPdfName" class="row mb20">
	    				<label for="">CERTIFICATE OF INCORPORATION：</label>
	    				<img src="" class="img-thumbnail" :src="getShowImage(quaInfo.busilicPic)" @click="showPic($event)">
	    			</div>
    			</template>
    		</div>
		</div>	
		<div class="line"></div>	
    	<div class="section-item">
    		<h5 class="title">联系人信息</h5>
    		<div class="con">
    			<div class="row mb20">
    				<label for="">联系人姓名：</label>
    				<span>{{ contactInfo.name }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">手机号码：</label>
    				<span>{{ contactInfo.telNumber }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">邮箱：</label>
    				<span>{{ contactInfo.mail }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">固定电话：</label>
    				<span>{{ contactInfo.fixedTel }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">QQ：</label>
    				<span>{{ contactInfo.qq }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">职位：</label>
    				<span>{{ personalTitleName }}</span>
    			</div>
    			<div class="row mb20">
    				<label for="">联系地址：</label>
    				<span>{{ contactInfo.address }}</span>
    			</div>
    		</div>
		</div>
		<div class="line"></div>	
    	<div class="section-item change-record">
    		<h5 class="title">修改记录</h5>
    		<div class="con">
    			<table class="table table-hover">
				  <thead>
				  	<tr>
				  		<td>序号</td>
				  		<td>操作人</td>
				  		<td>时间</td>
				  		<td>修改说明</td>
				  	</tr>
				  </thead>
				  <tbody>
				  	<tr class="no-record" v-if="showOperationlist.length == 0"><td colspan="4">暂无操作记录</td></tr>
				  	<tr v-if="showOperationlist.length > 0" v-for="(item, index) in showOperationlist">
				  		<td>{{ index + 1 }}</td>
				  		<td>{{ item.userName }}</td>
				  		<td>{{ item.date }}</td>
				  		<td>{{ item.actionDesc }}</td>
				  	</tr>
				  </tbody>	
				</table>
				<p v-if="showOperationlist.length > 5" class="more mt20"><a href="javascript:;" @click="showMore()">查看更多信息&gt;&gt;</a></p>
    		</div>
		</div>
		<div class="line"></div>	
    	<div class="section-item">
    		<h5 class="title">联系人信息</h5>
    		<div class="con">
    			<div class="row mb20">
    				<label for="">客服备注：</label>
    				<span>{{ comments }}</span>
    			</div>
    		</div>
		</div>
    </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/bootstrapValidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${ctx}/js/enterprise/firmAccountDetail.js"></script>
</body>
</html>