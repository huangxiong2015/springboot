<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="zh-CN">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应链金融详情</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/> 
<style>
.col-sm-5{padding-top:7px;}
.work-break-all{word-break: break-all;}
</style>
</head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="distributor-detail">
    <section class="content" >
		<div class="row">
          <section class="col-lg-12">
	            <div class="box box-danger ">
	                <!-- Morris chart - Sales -->
	                  <input id="detailId" type="hidden" value="${id}" />
	                  <div class="box-header with-border">
	                    <h3 class="box-title">供应链金融详情</h3>
	                  </div>
	                  <div id="supplierDetail" class="box-body form-horizontal"> 
                           <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">企业名称：</label>
                                   <div class="col-sm-5" v-text="initData.extend1?initData.extend1:'-'"></div>
                               </div>
                           </div>
                           <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">企业执照：</label>
                                   <div class="col-sm-5">
                                   		<img :src="content.imageUrl?content.imageUrl:'${ctx}/images/defaultImg01.jpg'"  width="100" @click="showBigImage"/>
                                   		<div id="bigImageWrap" style="display:none;">
                                   			<img :src="content.imageUrl?content.imageUrl:'${ctx}/images/defaultImg01.jpg'" style="max-width:1000px"/>
                                   		</div>
                                   </div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">联系人：</label>
                                   <div class="col-sm-5" v-text="content.contactUserName?content.contactUserName:'-'"></div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">联系电话：</label>
                                   <div class="col-sm-5 work-break-all" v-text="content.tel?content.tel:'-'"></div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">手机号：</label>
                                   <div class="col-sm-5 work-break-all" v-text="content.phone?content.phone:'-'"></div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">联系邮箱：</label>
                                   <div class="col-sm-5 work-break-all" v-text="content.email?content.email:'-'"></div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">服务：</label>
                                   <div class="col-sm-5" v-text="server"></div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">申请时间：</label>
                                   <div class="col-sm-5" v-text="initData.createdDate?initData.createdDate:'-'"></div>
                               </div>
                           </div>
                      </div>
      			</div> 
            </section>
		</div>
    </section>
   </div>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />  
<script type="text/javascript" src="${ctx}/js/app/supplyChainFinanceDetail.js"></script> 
</body>
</html>