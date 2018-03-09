<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">
<head>
<title>编辑企业信息</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/bootstrapValidator/css/bootstrapValidator.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/common/base.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/lemon/lemon-checkbox-layer.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/lemon/lemon-qua-select.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseList.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseEdit.css" />
<style>
.content-wrapper .content-header{
	padding-top:15px;
}
.main-header .sidebar-toggle:before{
	top: -6px;
}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper">
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper per-up-ent" id="edit">
	<section class="content-header">
		<h1>编辑企业信息</h1> 
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li> 
			<li class="active">企业用户</li>
		</ol>
	</section>
	<section class="content">		
		<div id="edit" class="row form-wrap">
			<form id="infoFrom" class="form-horizontal infoFrom">
				<div class="tips" style="display:none;"></div>
				<div class="module-title">企业信息</div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>公司名称：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" name="name" placeholder="公司名称" v-model.trim="entInfo.name">
			      </div>
			      <div class="col-sm-4 name-error text"></div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>公司类型：</label>
			      <div class="col-sm-6 col-lg-6 text">
				      <select class="form-control" name="corCategory" v-model="entInfo.categoryId">
				      	<option value="">请选择</option>
					    <option v-for="item in initData.companytypeList" :value="item.categoryId">{{ item.categoryName }}</option>
					  </select>
			      </div>
			      <div class="col-sm-4 corCategory-error text"></div>
			    </div>	    
				<div v-show="entInfo.categoryId==2006" class="form-group">
					<div class="col-sm-6 col-lg-6 col-sm-offset-2 text">
				 	 	<input type="text" name="corCategoryOther" class="form-control" placeholder="请填写公司类型" v-model="entInfo.otherCategory">
					</div>
			      	<div class="col-sm-4 corCategoryOther-error text"></div>
				</div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>所属行业：</label>
			      <div class="col-sm-6 col-lg-6 text">
				      <checkbox-layer
			            :list-data-url="checkBoxLayer.listDataUrl"
			            :selected-data="checkBoxLayer.selectedData"
			            :other-attr="checkBoxLayer.otherAttr"
			            @selected-data-change="selectedDataChange"
			            @attr-change="attrChange"
			          ></checkbox-layer>
				 </div>	
			    </div>
				<div class="form-group" style="margin-bottom:0;">
			      <label class="col-sm-2 col-lg-2 control-label">公司地址：</label>
			      <div class="col-sm-6 col-lg-6 text">
				    <select-province
			            :api="companySelect.api"
			            @onchange="companyAddressChange"
			            :input-class="companySelect.inputClass"
			            :id-district="companySelect.idDistrict"
					    :id-province="companySelect.idProvince"
					    :id-city="companySelect.idCity"
			            :name-district="companySelect.nameDistrict"
			            :name-province="companySelect.nameProvince"
			            :name-city="companySelect.nameCity"
			            :set-province="companySelect.setProvince"
			            :set-city="companySelect.setCity"
			            :set-district="companySelect.setDistrict"
			            :query-param="companySelect.queryParam"
			            :parent-id="companySelect.parentId"
				    ></select-province>
			      </div>
			    </div>
				<div v-show="entInfo.country" class="form-group">
					<div class="col-sm-6 col-lg-6 col-sm-offset-2 text">
			      		<input type="text" name="address" class="form-control" placeholder="详细地址" v-model.trim="entInfo.address" maxlength="250">
					</div>
			      	<div class="col-sm-4 address-error text"></div>
				</div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">邓氏编码：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" name="dCode" class="form-control" id="" placeholder="邓氏编码" v-model.trim="entInfo.dCode">
			      </div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">公司官网：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" name="webSite" class="form-control" id="" placeholder="公司官网" v-model.trim="entInfo.website">
			      </div>
			      <div class="col-sm-4 webSite-error text"></div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">YKY客户编码：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<span>{{ entInfo.partyCode ? entInfo.partyCode : '-' }}</span>
			      </div>
			    </div>
				<div class="area">
				<div class="module-title">
					企业资质
					<em v-if="quaData.activeStatus == 'PARTY_NOT_VERIFIED'"  class="verify-status danger">未认证</em>
					<em v-if="quaData.activeStatus == 'WAIT_APPROVE'"  class="verify-status danger">待审核</em>
					<em v-if="quaData.activeStatus == 'REJECTED'"  class="verify-status danger">不通过</em>
					<em v-if="quaData.activeStatus == 'PARTY_VERIFIED'"  class="verify-status success">通过</em>
					<em v-if="quaData.activeStatus == 'INVALID'"  class="verify-status danger">已失效</em>
				</div>				
				<!-- 资质开始 -->
				<qua-select
			        :busilis-type="quaData.busilisType"
			        :reg-addr="quaData.regAddr"
			        :busi-pic="quaData.busilicPic"
			        :tax-pic="quaData.taxPic"
			        :ooc-pic="quaData.oocPic"
			        :busi-pdf-name="quaData.busiPdfName"
			        :tax-pdf-name="quaData.taxPdfName"
			        :ooc-pdf-name="quaData.oocPdfName"
			        :file-size="5"
			        @zz-data-change="zzDataChange"
			    ></qua-select>					
	        	<!-- 资质结束 -->
				</div>			    
			    <div class="module-title">联系人信息</div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>联系人姓名：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="联系人姓名" name="applyUser" v-model.trim="contactInfo.name">
			      </div>
			      <div class="col-sm-4 applyUser-error text"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>手机号码：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="手机号码" name="telNumber" v-model.trim="contactInfo.telNumber">
			      </div>
			      <div class="col-sm-4 telNumber-error text"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>邮箱：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="邮箱" name="mail" v-model.trim="contactInfo.mail">
			      </div>
			      <div class="col-sm-4 mail-error text"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">固定电话：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="固定电话" name="fixedTel" v-model.trim="contactInfo.fixedTel">
			      </div>
			      <div class="col-sm-4 fixedTel-error text"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">联系QQ：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="联系QQ" name="qq" v-model.trim="contactInfo.qq">
			      </div>
			      <div class="col-sm-4 qq-error text"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">职位：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<select class="form-control" name="personalTitle" v-model="contactInfo.personalTitle">
			      		<option value="">请选择</option>
			      		<option v-for="item in initData.positionList" :value="item.id" v-html="item.name"></option>
			      	</select>
			      </div>
			    </div>			    
			    <div class="form-group" style="margin-bottom:0;">
			      <label class="col-sm-2 col-lg-2 control-label">联系地址：</label>
			      <div class="col-sm-6 col-lg-6 text">
				    <select-province
			            :api="contactSelect.api"
			            @onchange="contactAddressChange"
			            :input-class="contactSelect.inputClass"			            
			            :name-district="contactSelect.nameDistrict"
			            :name-province="contactSelect.nameProvince"
			            :name-city="contactSelect.nameCity"
			            :set-province="contactSelect.setProvince"
			            :set-city="contactSelect.setCity"
			            :set-district="contactSelect.setDistrict"
			            :query-param="contactSelect.queryParam"
			            :parent-id="contactSelect.parentId"
				    ></select-province>
			      </div>
			    </div>
			    <div v-show="contactInfo.country" class="form-group">
					<div class="col-sm-6 col-lg-6 col-sm-offset-2 text">
			      		<input type="text" class="form-control" placeholder="详细地址" name="personAddress" v-model.trim="contactInfo.address" maxlength="250">
					</div>
			      	<div class="col-sm-4 personAddress-error text"></div>
				</div>
			    <div class="module-title">修改日志</div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>修改说明：</label>
			      <div class="col-sm-6 col-lg-6">			      			      		
	      			<textarea class="form-control" name="reason" rows="3" v-model.trim="reason" placeholder="修改说明"></textarea>		      			      				      					      	
                  </div>
                  <div class="col-sm-4 reason-error text"></div>	
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">客服备注：</label>
			      <div class="col-sm-11 col-lg-8">
			      	<div class="row">
			      		<div class="col-sm-10 col-lg-9">
			      			<textarea class="form-control" name="comments" rows="3" v-model.trim="comments" placeholder="客服备注"></textarea>
			      		</div>
			      	</div>
                  </div>
			    </div>				
			    <div class="btns col-sm-10 col-lg-10 col-sm-offset-2 col-lg-offset-2">
			    	<button id="save" type="submit" class="btn btn-danger">保存</button>
			    	<button type="button" class="btn btn-default cancel" @click="cancel">取消</button>
			    </div>
			    <input type="hidden" value="${userId}" id="applyUserId">
			</form>
		</div>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/lib/jquery.base64.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/bootstrapValidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-city.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-checkbox-layer.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-qua-select.js"></script>
<script type="text/javascript" src="${ctx}/js/enterprise/enterpriseEdit.js"></script>
<script>
	if(getQueryString('action') == 'entAdd') {
		document.title = '新增企业信息';
		$('.content-header h2').text('新增企业信息');
	}
</script>
</body>
</html>