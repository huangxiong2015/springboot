<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>企业账号审核</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
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
                            	<span v-html="info.processName?info.processName:'授权委托书审核'"></span>                            	
                            </div>
                            <label class="col-sm-2 control-label">申请时间：</label>
                            <div class="col-sm-4"><span v-html="info.createdDate?info.createdDate:'--'"></span></div>
                        </div>
                    </div>
                    <div class="form-group wrap-info">
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
                            	<span v-html="companyInfo.name"></span>
                            </div>                            
                        </div>
                    </div>
                    <div class="form-group wrap-info">
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
                    <div class="form-group box-wrap">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">
                            	<span>企业认证授权书:</span>                            	
                            </label>                            
                            <div class="col-sm-2" v-if="!zzInfo.loaPdfName">
                            	<img id="loaPic" class="img-thumbnail" :src="zzInfo.loaPic" @click="showVoucherPic('#loaPic')">
                            </div> 
                            <div class="col-sm-8" v-if="zzInfo.loaPdfName">
                            	<a id="pdfPic0" :href="zzInfo.loaPic" target="_blank">
									<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon">
								</a>
								<span class="dib" type="text" id="pdfName0" v-html="zzInfo.loaPdfName"></span>
                            </div>                                                                                                             
                        </div>
                     </div>                                          
                    <div class="form-group ">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">审核人：</label>
                            <div class="col-sm-4">
                            	<span v-html="audit.approvePartyName?audit.approvePartyName:'--'"></span>                            	
                            </div>                            
                        </div>
                      </div>
                      <div class="form-group ">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">审核时间：</label>
                            <div class="col-sm-4" >
                            	<span v-html="audit.lastUpdateDate?audit.lastUpdateDate:'--'"></span>                            	
                            </div>                            
                        </div>
                      </div>
                      <div class="form-group ">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">审核意见：</label>
                            <div class="col-sm-4" >
                            	<span v-html="audit.status == 'WAIT_APPROVE'?'--':audit.reason?audit.reason:'--'"></span>
                            </div>                            
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
<script type="text/javascript" src="${ctx}/js/app/examineSubAccount.js"></script>

</body>
</html>