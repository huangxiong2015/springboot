<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>企业账号审核</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
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
	        <section class="col-md-12">
	          <div class="box box-solid ">
	            <div class="box-body customCol">
	              <form class="form-horizontal" method="post" name="seachForm" id="seachForm" @submit.prevent="onSearch">
	                <div class="row">
	                  <div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
	                    <div class="row" >
	                      <div class="col-md-4 margin10">
	                        <label for="auditCode" class="col-sm-4 control-label">审核编码</label>
	                        <div class="col-sm-7">
	                          <input type="text" class="form-control" id="applyId" name="applyId" placeholder="审核编码" v-model="queryParams.applyId" >
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10">
	                        <label for="companyName" class="col-sm-4 control-label">公司名称</label>
	                        <div class="col-sm-7">
	                          <input type="text" class="form-control" id="companyName" name="companyName" placeholder="公司名称" v-model="queryParams.companyName" >
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10">
	                        <label for="applyType" class="col-sm-4 control-label">申请类型</label>
	                        <div class="col-sm-7" >
	                          <select class="form-control" id="processIdList"  name="processIdList" v-model="queryParams.processIdList"  >
	                          	<option value="ORG_DATA_REVIEW,ORG_PROXY_REVIEW">全部</option>
	                            <option value="ORG_DATA_REVIEW">企业资质</option>
	                            <option value="ORG_PROXY_REVIEW">子账号管理</option>
	                            <option value="ORG_ACCOUNT_PERIOD_REVIEW">账期</option>
	                          </select>
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10">
	                        <label for="reviewUserName" class="col-sm-4 control-label">审核人</label>
	                        <div class="col-sm-7">
	                          <input type="text" class="form-control" id="reviewUserName" name="reviewUserName" placeholder="审核人" v-model="queryParams.reviewUserName" >
	                        </div>
	                      </div>
						 <div class="col-md-4 margin10">
	                        <label for="auditStatus" class="col-sm-4 control-label">审核状态</label>
	                        <div class="col-sm-7" >
	                          <select class="form-control" id="status"  name="status" v-model="queryParams.status">
	                          	<option value="">全部</option>
	                            <option value="APPROVED">通过</option>
	                            <option value="REJECT">不通过</option>
	                            <option value="WAIT_APPROVE">待审核</option>
	                          </select>
	                        </div>
	                      </div>
			              <div class="col-md-4 margin10"> 
			                  <div class="row">
			                      <label for="createDate" class="col-sm-4 col-md-4 control-label">申请时间</label>
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
			                  <div class="row">
			                      <label for="applyDate" class="col-sm-4 col-md-4 control-label">审核时间</label>
			                      <div class="col-sm-7 col-md-7 " id="applyDate">
			                          <div id="applyDateRange" class="input-daterange input-group">
				                          <input type="text" name="applyDateStart" id="applyDateStart" class="form-control"  v-model="queryParams.applyDateStart"> 
				                          <span class="input-group-addon">至</span> 
				                          <input type="text" name=applyDateEnd id="applyDateEnd" class="form-control"  v-model="queryParams.applyDateEnd">
			                          </div>
			                      </div>
			                  </div>
			              </div>
	                    </div>
	                  </div>
	                  <div class="col-lg-1">
	                    <button class="btn btn-danger sendData margin10" id="search_all" >
	                      <i class="fa fa-search"></i>查询
	                    </button>
	                  </div>
	                </div>
	              </form>
	            </div>
	          </div>
	        </section>
		</div>   		
      <div class="row">
        <section class="col-sm-12">
          <div class="box ">
            <div class="chart box-body " style="position: relative;">
              <!--表格组件-->
               <rowspan-table
                      :columns="gridColumns"
                      :pageflag="pageflag"
                      :query-params="queryParams"
                      :api="url"
                      :refresh="refresh"
                      :show-total="showTotal"
              >
              </rowspan-table>
 
            </div>

          </div>
        </section> 
      </div>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/enterpriseList.js"></script>
</body>
</html>