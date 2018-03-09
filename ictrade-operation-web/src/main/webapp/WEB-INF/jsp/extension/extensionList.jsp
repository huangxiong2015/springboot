<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="${ctx}/favicon.ico"/>
<style>

</style>
<title>推广位管理 </title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="extension-list">
		<section class="content-header">
		  <h1> 推广位管理 </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active"> 推广位管理 </li>
		  </ol>
		</section> 
	   <section class="content">
	    <div class="row">
			<section class="col-md-12">
				<div class="box box-solid ">
					<div class="box-body customCol">
		              <form class="form-horizontal" name="" id="" method="get" action=""  @submit.prevent="onSearch">
				   		 <div class="row">
	                  		<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
			                   <div class="row">
						              <div class="col-sm-5 col-md-4 col-lg-3 margin10">
						                  <div class="form-group-sm">
						                      <label for="orderStatus" class="col-sm-4  col-lg-4 control-label">描述 </label>
						                      <div class="col-sm-7  col-lg-8" >
						                        <input type="text" class="input-sm form-control" id="desc" name="desc"  v-model="queryParams.desc" >
						                      </div>
						                  </div>
						              </div>
						               <div class="col-sm-7 col-md-4 col-lg-3 margin10">
						                  <div class="form-group-sm">
						                      <label for="orderStatus" class="col-sm-4  col-lg-4 control-label">类型 </label>
						                      <div class="col-sm-7  col-lg-8" >
						                          <select id="categoryId" name="categoryId" class="input-sm form-control" v-model="queryParams.categoryId">
						                          	<option value="">全部</option>
						                          	<option value="4001">活动</option>
						                          	<option value="4002">分销商</option>
						                          	<option value="4003">制造商</option>
						                          </select>
						                      </div>
						                  </div>
						              </div> 
						              <div class="col-sm-7 col-md-4 col-lg-3 margin10">
						                  <div class="form-group-sm">
						                      <label for="orderStatus" class="col-sm-4  col-lg-4 control-label">状态 </label>
						                      <div class="col-sm-7  col-lg-8" >
						                         <select class="form-control" id="status"  name="status" v-model="queryParams.status"  >
						                          	<option value="">全部</option>
						                            <option value="PUBLISHED">启用</option>
						                            <option value="HOLD">停用</option>
						                            <option value="END">已结束</option>
						                            <option value="PUT">投放中</option>
						                            <option value="DRAFT">草稿</option>
	                          					 </select>
						                      </div>
						                  </div>
						              </div> 
						  
						          </div>
					          </div>
					          <div class="col-lg-1">
			                    <button class="btn btn-sm btn-danger sendData margin10" id="search_all" >
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
	            <!-- Morris chart - Sales -->
	            <div class="box-header with-border">
	              <a href="${ctx}/extension.htm?action=toedit" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>
	            </div>
	            <div class="chart box-body " style="position: relative;">
	              <!--表格组件-->
	               <rowspan-table
	                      :columns="gridColumns"
	                      :pageflag="pageflag"
	                      :query-params="queryParams"
	                      :api="url"
	                      :refresh="refresh"  
	              >
	              </rowspan-table>
	 
	            </div>
	
	          </div>
	        </section> 
	      </div>
	   </section>
	</div>
</div>

<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/extensionlist.js"></script>
</body>
</html>