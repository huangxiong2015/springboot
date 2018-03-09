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
	<title>快递管理</title>
	<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<div class="content-wrapper" id="shipmentEdit">
			<section class="content-header">
			  <h1>
			    	快递管理
			    <small v-if="partyId">编辑</small>
			    <small v-else>添加</small>
			  </h1>
			  <ol class="breadcrumb">
			    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
			    <li class="active"><a href="${ctx}/shipmentlist.htm">快递管理</a></li>
			  </ol>
			</section>   
		    
	    <section class="content" style=" color:#666;">
	      <form class="form-horizontal" name="manu-from" id="manu-from" onsubmit="return false;">    
	          <div class="row">
	              <section class="col-lg-12">
	                <div class="box box-danger ">
	                    <!-- Morris chart - Sales -->
	                      <div class="box-header with-border">
	                        <h3 class="box-title">
		                        <span v-if="partyId">编辑</span>
		                        <span v-else>添加</span>
	                        	<span>-快递管理</span>
                        	</h3>
	                      </div>
	                      <div class="box-body ">
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label class="col-sm-2 control-label"><span class="text-red">*</span>公司名称：</label>
                                      <div class="col-sm-4" >
                                          <input type="text" id="groupName" name="groupName" required="required" class="form-control ui-autocomplete-input" v-model="initData.groupName" />
                                      </div>
                                  </div>
                              </div>
	                      </div>
	                      <div class="box-footer text-center">
		                      <button type="submit" class="btn btn-danger c-btn" >保存</button>
		                      <button type="button" class="btn btn-danger c-btn" onclick="goBack();">取消</button>
		                  </div>
	                </div>
	              </section>
	          </div>
	      </form>
	    </section>
	</div>
	
	</div>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ---->
	<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
	<script type="text/javascript" src="${ctx}/js/app/shipmentEdit.js"></script>	
</body>
</html>