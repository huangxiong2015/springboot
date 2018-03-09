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
  <title>支付方式编辑</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<div class="content-wrapper" id="paymentEdit">
			<section class="content-header">
			  <h1>
			    	支付方式
			    <small v-if="paymentMethodId">编辑</small>
			    <small v-else>添加</small>
			  </h1>
			  <ol class="breadcrumb">
			    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
			    <li class="active"><a href="${ctx}/recommend.htm">支付方式</a></li>
			  </ol>
			</section>   
		    
	    <section class="content" style=" color:#666;">
	      <form class="form-horizontal" name="manu-from" id="payment-from" onsubmit="return false;">    
	          <div class="row">
	              <section class="col-lg-12">
	                <div class="box box-danger ">
	                    <!-- Morris chart - Sales -->
	                      <div class="box-header with-border">
	                        <h3 class="box-title">
		                        <span v-if="paymentMethodId">编辑</span>
		                        <span v-else>添加</span>
	                        	<span>-支付方式</span>
                        	</h3>
	                      </div>
	                      <div class="box-body ">
	                      		  <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">支付类型：</label>
	                                      <div class="col-sm-4" >
	                                          <span id="parentPaymentMethodId" style="display:inline-block;margin-top:8px;" v-text="initData.parentPaymentMethodId"></span>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">支付状态：</label>
	                                      <div class="col-sm-4" >
	                                      	  <select class="form-control" id="state" name="state" v-model="initData.state">
												<option value="VALID">有效</option>
												<option value="NOT_VALID">无效</option>
											</select>
	                                      </div>
	                                  </div>
	                              </div>
	                              
	                               <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">生产环境状态：</label>
	                                      <div class="col-sm-4" >
	                                      	  <select class="form-control" id="dev" name="dev" v-model="initData.dev">
												<option value="VALID">有效</option>
												<option value="NOT_VALID">无效</option>
											</select>
	                                      </div>
	                                  </div>
	                              </div>
	                              
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label"><span class="text-red">*</span>描述：</label>
	                                      <div class="col-sm-4" >
	                                          <input type="text" id="description" name="description" required="required" class="form-control ui-autocomplete-input" v-model="initData.description">
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
	<script type="text/javascript" src="${ctx}/js/app/paymentMethodsEdit.js"></script>	
</body>
</html>
