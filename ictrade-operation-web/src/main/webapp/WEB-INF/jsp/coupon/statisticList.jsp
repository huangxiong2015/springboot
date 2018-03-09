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
	<!-- jQuery 1.11.3 -->
   <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <title>优惠券统计列表页</title>
  <link rel="stylesheet" href="${ctx}/js/credenceUpload/credenceUpload.css?20161229" />
  <link rel="stylesheet" href="${ctx}/css/app/coupon.css" />
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	  
<div class="content-wrapper" id="couponList">
	<section class="content-header">
	 	<h1>
		   优惠券
		</h1>
	</section>
	<section class="content" >
		<div class="row">
			<div class="col-md-12">
	   			<div class="box box-solid ">
	   				<div class="box-body customCol">
	   					<div class="head_cont">
							<a class="head_tab " href="${ctx}/coupon.htm">优惠券创建</a>
							<a class="head_tab" href="${ctx}/coupon/couponHandleList.htm">优惠券发放</a>
							<a class="head_tab active" >优惠券统计</a>
						</div>
	   					<form class="form-horizontal" onsubmit="return false">
	   						<div class="row">
	   							<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
			   						<div class="col-md-4 margin10">
				   						<div class="form-group-sm">
					   						<label for="title" class="col-sm-4 control-label">名称</label> 
					   						<div class="col-sm-7">
					   							<input type="text" name="name" id="name" placeholder="名称" class="form-control" v-model="queryParams.name"/>
					   						</div>
				   						</div>
				   					</div> 
				   					<div class="col-md-4 margin10">
										<div class="form-group-sm">
						                      <label for="registerDate" class="col-sm-4 col-md-4 control-label">创建时间</label>
						                      <div class="col-sm-7 col-md-7 " id="createDate">
						                          <div id="createDateDateRange" class="input-daterange input-group">
							                          <input type="text" name="createDateStart" id="createDateStart" class="form-control"  v-model="queryParams.createDateStart"> 
							                          <span class="input-group-addon">至</span> 
							                          <input type="text" name="createDateEnd" id="createDateEnd" class="form-control"  v-model="queryParams.createDateEnd">
						                          </div>
						                      </div>
						                  </div>
									</div>
									<div class="col-md-4 margin10">
										<div class="form-group-sm">
					                        <label for="thruDate" class="col-sm-4 col-md-4 control-label">有效时间</label>
					                        <div class="col-sm-7 col-md-7 " id="thruDate">
					                            <div id="thruDateRange" class="input-daterange input-group">
						                            <input type="text" name="thruDateStart" id="thruDateStart" class="form-control"  v-model="queryParams.thruDateStart"> 
						                            <span class="input-group-addon">至</span> 
						                            <input type="text" name="thruDateEnd" id="thruDateEnd" class="form-control"  v-model="queryParams.thruDateEnd">
					                            </div>
					                        </div>
					                    </div>
						            </div>
						            <div class="col-md-4 margin10">
						            	<div class="form-group-sm">
						                      <label for="couponCurrency" class="col-sm-4 col-md-4 control-label">币种</label>
					                          <div class="col-sm-7 col-md-7 " id="couponCurrency">
						                          <select class="form-control">
						                          	<option value="">全部币种</option>
						                            <option value="CNY">RMB</option>
						                            <option value="USD">USD</option>
						                          </select>
					                          </div>
					                    </div>
			                        </div>
						            <div class="col-md-4 margin10">
						            	<div class="form-group-sm">
						                      <label for="lastLoginDate" class="col-sm-4 col-md-4 control-label">类型</label>
					                          <div class="col-sm-7 col-md-7 " id="couponCate">
						                          <select class="form-control">
						                          	<option value="">全部类型</option>
						                            <option value="OFFLINE_PROMO">地推类型</option>
						                            <option value="PLATFORM_PROMO">活动推广</option>
						                            <option value="EXACT_PROMO">定向推广</option>
						                          </select>
					                          </div>
					                    </div>
			                        </div>
			                        <div class="col-md-4 margin10">
						            	<div class="form-group-sm">
						                      <label for="lastLoginDate" class="col-sm-4 col-md-4 control-label">状态</label>
					                          
					                          <div class="col-sm-7 col-md-7 " id="statusId">
						                          <select class="form-control">
						                          	<option value="">全部</option>
						                            <option value="WAIT_SEND">待发放</option>
						                            <option value="SENDING">发放中</option>
						                            <option value="END">已结束</option>
						                          </select>
					                          </div>
					                    </div>
			                        </div>
			                        <div class="col-md-4 margin10">
						            	<div class="form-group-sm">
						                      <label for="couponType" class="col-sm-4 col-md-4 control-label">优惠券形式</label>
					                          
					                          <div class="col-sm-7 col-md-7 " id="couponType">
						                          <select class="form-control">
						                          	<option value="">全部</option>
						                            <option value="DISCOUNT">折扣劵</option>
						                            <option value="DEDUCTION">抵扣劵</option>
						                          </select>
					                          </div>
					                    </div>
			                        </div>
				   				</div>
				   				<div class="col-lg-1">
				                    <button class="btn btn-danger sendData margin10" id="search_all" @click="search()">
				                      <i class="fa fa-search"></i>查询
				                    </button>
				                </div>
	   						</div>
	   					</form>
	   				</div>
	   			</div>
	   		</div>
		</div>
		<div class="row">
	        <section class="col-sm-12">
		          <div class="box ">
		          		<!-- <div class="box-header with-border">
		          			<a href="/main/role.htm?action=addRole" class="btn btn-sm btn-danger pull-right btn-add-coupon">
		          				<i class="fa fa-plus"></i> 创建优惠券
							</a>
						</div> -->
			            <div class="chart box-body " style="position: relative;">
				              <!--表格组件-->
				               <rowspan-table
				                      :columns="gridColumns"
				                      :pageflag="pageflag"
				                      :query-params="queryParams"
				                      :api="url"
				                      :refresh="refresh" 
				                      :pageflag = "pageflag"
				              >
				              </rowspan-table>
			            </div>
		          </div>
	        </section> 
      </div>
	</section>
</div>
	 
	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ---->
	<!-- layer -->
	<div class="handle_g_layer">
		<p class="cont_title tc"><span class="f18 dn error_tip rel"><i class="icon-exclamark"></i>请输入定向发放用户信息</span></p>
		<div class="item">
			<span class="label" style="margin-top: -10px;">用户账号：</span>
			<div class="input_wrap">
				<textarea name="userNames" id="userNames" cols="60" rows="10" placeholder="最大可输入10个用户名称，以“,”间隔"></textarea>
			</div>
		</div>
		<div class="item">
			<span class="label">备注：</span>
			<div class="input_wrap">
				<input type="text" name="remark" id="remark" class="same_input" placeholder="输入发放原因" maxlength="20">
			</div>
		</div>
	</div>
	<!-- layer -->
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script src="${ctx}/js/app/statisticList.js"></script>

</div>	    
</body>
</html>
