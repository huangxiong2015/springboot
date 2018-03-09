<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>招商加盟</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="finance-list">
		<section class="content-header">
		  <h1>招商加盟</h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
		    <li class="active">招商加盟</li>
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
	                        <label for="entName" class="col-sm-4 control-label">公司名称</label>
	                        <div class="col-sm-7">
	                          <input type="text" class="form-control" id="entName" name="extend1" placeholder="公司名称" v-model="queryParams.extend1" >
	                        </div>
	                      </div>
			              <div class="col-md-4 margin10"> 
			                  <div class="row">
			                      <label for="applyDate" class="col-sm-4 col-md-4 control-label">申请时间</label>
			                      <div class="col-sm-7 col-md-7 " id="applyDate">
			                          <div id="applyDateDateRange" class="input-daterange input-group">
				                          <input type="text" name="startDate" id="startDate" class="form-control"  v-model="queryParams.startDate"> 
				                          <span class="input-group-addon">至</span> 
				                          <input type="text" name="endDate" id="endDate" class="form-control"  v-model="queryParams.endDate">
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
<script type="text/javascript" src="${ctx}/js/app/supplierList.js"></script>
</body>
</html>