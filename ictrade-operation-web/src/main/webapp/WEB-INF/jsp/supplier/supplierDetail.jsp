<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="zh-CN">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>加盟详情</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/> 
</head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<style>
.form-horizontal .control-label{padding-top:0;}
.content section { padding: 0;}
</style>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="distributor-detail">
    <section class="content" >
		<div class="row">
          <section class="col-lg-12">
	            <div class="box box-danger ">
	                <!-- Morris chart - Sales -->
	                  <input id="detailId" type="hidden" value="${id }" />
	                  <div class="box-header with-border">
	                    <h3 class="box-title">加盟详情</h3>
	                  </div>
	                  <div id="supplierDetail" class="box-body form-horizontal"> 
                           <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">公司名称：</label>
                                   <div class="col-sm-5">{{entName}}</div>
                               </div>
                           </div>
                           <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">公司类型：</label>
                                   <div class="col-sm-5" v-html="companyType?companyTypeList[companyType]:'-'"></div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label" v-html="showCompantType(companyType)"></label>
                                   <div class="col-sm-5">{{agent}}</div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">联系人：</label>
                                   <div class="col-sm-5">{{contactUserName}}</div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">联系方式：</label>
                                   <div class="col-sm-5">{{phone}}</div>
                               </div>
                           </div>
                            <div class="form-group ">
                               <div class="col-md-11">
                                   <label for="specurl" class="col-sm-3 control-label">申请时间：</label>
                                   <div class="col-sm-5">{{createdDate}}</div>
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
<script type="text/javascript" src="${ctx}/js/app/supplierDetail.js"></script> 
</body>
</html>