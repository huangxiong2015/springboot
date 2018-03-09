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
<style  type="text/css">
 .reason-box textarea{
     width: 469px;
     height: 144px;
 }

</style>
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
        	<section class="col-sm-12" id="examineAccountPeriod">
			<input type="hidden" id="userName" value="${userName }">
			<input type="hidden" id="userId" value="${userId }">
          	<div class="box ">
	            <div class="chart box-body " style="position: relative;">
	            	<div class="form-group ">
                    	<div class="col-md-11">
                            <label class="col-sm-2 control-label">申请类型：</label>
                            <div class="col-sm-4" >
                            	<span class="show-btn" @click="showInfo('.applicant-info',$event)">
                            		<span v-html="info.processName?info.processName:'账期审核'"></span><i class="fa fa-angle-left pull-right"></i>
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
                            <label class="col-sm-2 control-label">结算币种：</label>
                            <div class="col-sm-4" >
                            	<span class="radio-wrap">
	                            	<input name="currency" type="radio" id="USD" value="USD" v-model="qzInfo.currency" disabled>
	                            	<label for="USD">美元</label>
                            	</span>
                            	<span class="radio-wrap">
	                            	<input name="currency" type="radio" id="CNY" value="CNY" v-model="qzInfo.currency" disabled>
	                            	<label for="CNY">人民币（含17%增值税）</label>
                            	</span>
                            </div>                            
                        </div>
                    </div>
                    <div class="form-group ">
                    	<div class="col-md-11">
                    		<label class="col-sm-2 control-label">授信额度：</label>
	                    	<div class="col-sm-4">	                    		                           
	                           	<div class="col-sm-4" >
	                           		<input class="form-control" type="text" name="creditQuota" id="creditQuota" v-model="qzInfo.creditQuota" readonly>
	                           	</div>                            	
	                           	<span class="s-unit" v-html="qzInfo.currency == 'CNY'?'RMB':'USD'"></span>	                            
	                    	</div>
	                    	<label class="col-sm-2 control-label">授信期限：</label>
                            <div class="col-sm-4">
                            	<span class="month-pay s-unit">月结</span>	                    		                           
	                           	<div class="col-sm-4" >
	                           		<input class="form-control" type="text" name="creditDeadline" id="creditDeadline" v-model="qzInfo.creditDeadline" readonly>
	                           	</div>                            	
	                           	<span class="s-unit">天</span>	                            
	                    	</div>                            
                        </div>
                    </div>
                    <div class="form-group">
                    	<div class="col-md-11">
                    		<label class="col-sm-2 control-label">对账日期：</label>
	                    	<div class="col-sm-4">	                    		                           
	                           	<div class="col-sm-4" >
	                           		<input class="form-control" type="text" name="checkDate" id="checkDate" v-model="qzInfo.checkDate" readonly>
	                           	</div>                            	
	                           	<span class="s-unit">日</span>	                            
	                    	</div>
	                    	<label class="col-sm-2 control-label">对账周期：</label>
                            <div class="col-sm-4">                  		                           
	                           	<div class="col-sm-5" >
	                           		<input class="form-control" type="text" name="checkCycle" id="checkCycle" v-model="qzInfo.checkCycle" readonly>
	                           	</div>                            	
	                           	<span class="s-unit">天</span>	                            
	                    	</div>                            
                        </div>
                    </div>
                    <div class="form-group ">
                    	<div class="col-md-11">
                    		<label class="col-sm-2 control-label">付款日期：</label>
	                    	<div class="col-sm-4">	                    		                           
	                           	<div class="col-sm-4" >
	                           		<input class="form-control" type="text" name="payDate" id="payDate" v-model="qzInfo.payDate" readonly>
	                           	</div>                            	
	                           	<span class="s-unit">日</span>	                            
	                    	</div>	                    	                           
                        </div>
                    </div>
                    <div class="form-group ">
                    	<div class="col-md-11">
                    		<label class="col-sm-2 control-label">申请说明：</label>
	                    	<div class="col-sm-10">	                    		                           
	                           	<div class="col-sm-8" >
	                           		<textarea class="form-control" type="text" name="common" id="common" v-model="qzInfo.common" readonly></textarea>
	                           	</div>                            		                           	                            
	                    	</div>	                    	                           
                        </div>
                    </div>
                    <div class="form-group attachment-list">
                    	<div class="col-md-11" v-for="(ele,index) in qzInfo.creditAttachmentList">
                    		<label class="col-sm-2 control-label" v-html="index ==0 ? '附件：':''"></label>
	                    	<div class="col-sm-8">	                    		                           
	                           	<a :href="getImage(ele.attachmentUrl)" target="_blank" style="color:#666;">
									<img width="32px" height="32px" alt="" src="${ctx }/images/pdf.png" class="pdf_icon" v-if="ele.attachmentName.indexOf('.pdf')>0">
									<img width="32px" height="32px" alt="" src="${ctx }/images/xls.jpg" class="pdf_icon" v-if="ele.attachmentName.indexOf('.xls')>0">
									<img width="32px" height="32px" alt="" src="${ctx }/images/rar.jpg" class="pdf_icon" v-if="ele.attachmentName.indexOf('.rar')>0||ele.attachmentName.indexOf('.zip')>0">
									<span class="dib" type="text" v-html="ele.attachmentName"></span>
								</a>
								                          		                           	                            
	                    	</div>	                    	                           
                        </div>                    
                    </div>                                                                                  
                     
                      <div class="audit-opinion btd"   >
                      	<div class="form-group ">
	                    	<div class="col-md-11">
                            		<table class="table table-head">
		  								<thead>
		  									<tr>
		  										<th>审核时间</th>
		  										<th>审核人</th>
		  										<th>审核状态</th>
		  										<th>审核意见</th>
		  									</tr>
		  								</thead>
		  							</table>
		  							 <div class="table-body">
		  								<table class="table" style="text-align: center;">
		  									<tbody>
		  										<tr v-for="(item, $index) in auditList" v-if="item.status == 'APPROVED' || item.status == 'REJECT'  " >
		  											<td >{{item.lastUpdateDate?item.lastUpdateDate:'--'}}</td>
		  											<td >{{item.approvePartyName?item.approvePartyName: '--'}}</td>
		  											<td  v-if="item.status == 'APPROVED'" >通过</td>
		  											<td  v-if="item.status == 'REJECT'  " >驳回</td>
		  											<td >{{item.remark?item.remark: '--'}}</td>
		  										</tr>
		  									</tbody>
		  								</table>
		  							</div>                      
	                        </div>
	                      </div>
                      </div>
                       <div class="btn-list btd" v-if="audit.status == 'WAIT_APPROVE'"  style="margin-top:20px;">
                      		<a class="btn btn-yes" @click="examineEvent('yes')">通过</a>
                      		<a class="btn btn-no" @click="examineEvent('no')">驳回</a>
                      </div>
                      <div class="reason-wrap">
                      	<p class="reason-text"></p>  
                      	<div class='reason-box'>
                      		<textarea id='remark' onkeyup="setLetterNum(this)" maxlength="200"></textarea>
                      		<span class="num-wrap"><span class="letter-num">0</span>/200</span>
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
<script type="text/javascript" src="${ctx}/js/app/examineAccountPeriod.js"></script>

</body>
</html>