<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">
<head>
<title>个人升级企业</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/js/lib/bootstrapValidator/css/bootstrapValidator.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/common/base.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/common/component.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseList.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/cerEnterpriseEdit.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/personUpEnterprise.css" />
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
		<h1>个人升级企业</h1> 
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li> 
			<li class="active">个人用户</li>
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
			      	<input type="text" class="form-control" name="name" placeholder="公司名称" v-model.trim="entInformation.name">
			      </div>
			      <div class="col-sm-4 name-error text"></div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>公司类型：</label>
			      <div class="col-sm-6 col-lg-6 text">
				      <select class="form-control" name="corCategory" v-model="entInformation.corCategory">
				      	<option value="">请选择</option>
					    <option v-for="item in companytypeList" :value="item.categoryId">{{ item.categoryName }}</option>
					  </select>
			      </div>
			      <div class="col-sm-4 corCategory-error text"></div>
			    </div>	    
				<div v-show="entInformation.corCategory==2006" class="form-group">
					<div class="col-sm-6 col-lg-6 col-sm-offset-2 text">
				 	 	<input type="text" name="corCategoryOther" class="form-control" placeholder="请填写公司类型" v-model="corCategoryOther">
					</div>
			      	<div class="col-sm-4 corCategoryOther-error text"></div>
				</div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>所属行业：</label>
			      <div class="col-sm-6 col-lg-6 text">
				      <div class="f-ib"> 
					   <a class="select_industry" @click="toggleIndustry(activeIndustryList)"><em class="select_tip">请选择所属行业</em><i class="icon-down_arrow1 g9"></i></a> 
					   <div class="industry_content dn"> 
					   <div class="industry_list" style="max-height: 85px; overflow-y: auto;"> 
					   		<span class="company_type noselect" v-for="item in industryList">
					   			<input class="check-box-input" type="checkbox" style="display:none" :id="item.categoryId" :value="item.categoryId" v-model="temporaryIndustryList" />
					   			<label :for="item.categoryId">
						   			 <span class="check-box-white isfirst"></span>
						       		 <i class="g3">{{ item.categoryName }}</i>
					   			</label>	
					   			<input v-if="item.categoryId == 1008" v-show="isShowIndustryOther" @click.stop v-model.trim="otherAttr" name="otherAttr" class="other_attr v-success" maxlength="10" placeholder="请填写所属行业"/>					   							   			
					   		</span>					   		
					    </div>					    
					    <div class="industry_btn mt20 tc">
					     <a class="btn_y" @click="confirmIndustry(temporaryIndustryList)">确认</a>
					     <a class="btn_c" @click="cancelIndustry(activeIndustryList)">取消</a>
					    </div>
					   </div>
					   <div class="select-area">
					   	<div v-for="item in activeIndustryList" v-if="item != ''" class="area-item" :data-id="item"><span v-html="item != 1008? industryNameList[item]:industryNameList[item] + '(' + otherAttr + ')'"></span><i class="icon-close-min close" @click="removeSelect"></i></div>
					   </div>
					  </div>
				 </div>	
			    </div>
				<div class="form-group" style="margin-bottom:0;">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>注册地址：</label>
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
				<div class="form-group">
					<div class="col-sm-6 col-lg-6 col-sm-offset-2 text">
			      		<input type="text" name="address" class="form-control" placeholder="详细地址" v-model.trim="entInformation.address" maxlength="250">
					</div>
			      	<div class="col-sm-4 address-error text"></div>
				</div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">邓氏编码：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" name="dCode" class="form-control" id="" placeholder="邓氏编码" v-model.trim="entInformation.dCode">
			      </div>
			    </div>
				<div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">公司官网：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" name="webSite" class="form-control" id="" placeholder="公司官网" v-model.trim="entInformation.webSite">
			      </div>
			      <div class="col-sm-4 webSite-error text"></div>
			    </div>
				<div class="area">
				<div class="module-title">企业资质</div>				
				<!-- 资质开始 -->
				<div id="qual" class="ui-qual">
					<div class="input-group">
						<label class="input-text"><span class="must">*</span>公司注册地：</label>
						<div class="input-content qual-type"></div>
					</div>
					<div class="input-group dalu_type">
						<label class="input-text"><span class="must">*</span>营业执照类型：</label>
						<div class="input-content">
							<label class="radio-item">
								<input type="radio" name="a">
								<i class="radio radio-selected" data-name="3-TO-1"></i>三证合一营业执照（自2015年10月1日起登记机关颁发的含统一信用代码的营业执照）
							</label>
							<label class="radio-item">
								<input type="radio" name="a">
								<i class="radio" data-name="COMMON"></i>普通营业执照
							</label>
						</div>
					</div>
					<div class="qual-list">						
					</div>
				</div>								
	        	<!-- 资质结束 -->
				</div>			    
			    <div class="module-title">联系人信息</div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>联系人姓名：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="联系人姓名" name="applyUser" v-model.trim="entInformation.applyUser">
			      </div>
			      <div class="col-sm-4 applyUser-error text"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>手机号码：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="手机号码" name="applyInformation" v-model.trim="entInformation.applyInformation">
			      </div>
			      <div class="col-sm-4 applyInformation-error text"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>邮箱：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="邮箱" name="applyMail" v-model.trim="entInformation.applyMail">
			      </div>
			      <div class="col-sm-4 applyMail-error text"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">固定电话：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="固定电话" name="fixedTel" v-model.trim="entInformation.fixedTel">
			      </div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">联系QQ：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<input type="text" class="form-control" placeholder="联系QQ" name="qq" v-model.trim="entInformation.contactUserQQ">
			      </div>
			      <div class="col-sm-4 qq-error text"></div>
			    </div>
			    <div class="form-group">
			      <label class="col-sm-2 col-lg-2 control-label">职位：</label>
			      <div class="col-sm-6 col-lg-6 text">
			      	<select class="form-control" name="personalTitle" v-model="entInformation.personalTitle">
			      		<option value="">请选择</option>
			      		<option v-for="item in positionList" :value="item.id" v-html="item.name"></option>
			      	</select>
			      </div>
			    </div>			    
			    <div class="form-group" style="margin-bottom:0;">
			      <label class="col-sm-2 col-lg-2 control-label"><span class="cRed">*</span>联系地址：</label>
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
			    <div class="form-group">
					<div class="col-sm-6 col-lg-6 col-sm-offset-2 text">
			      		<input type="text" class="form-control" placeholder="详细地址" name="personAddress" v-model.trim="entInformation.personAddress" maxlength="250">
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
			      			<textarea class="form-control" name="comments" rows="3" v-model.trim="entInformation.comments" placeholder="客服备注"></textarea>
			      		</div>
			      	</div>
                  </div>
			    </div>				
			    <div class="btns col-sm-10 col-lg-10 col-sm-offset-2 col-lg-offset-2">
			    	<button id="save" type="submit" class="btn btn-primary">保存</button>
			    	<button type="button" class="btn btn-default cancel" @click="cancel">取消</button>
			    </div>
			    <input type="hidden" value="${userId}" id="applyUserId">
			</form>
		</div>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<%-- <script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script> --%>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-city.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/jquery.base64.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/bootstrapValidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_zz.js"></script>
<script type="text/javascript" src="${ctx}/js/common/component.js"></script>
<script type="text/javascript" src="${ctx}/js/app/personUpEnterprise.js"></script>
</body>
</html>