<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>广告位-列表</title>
  <style>
	.content-wrapper .content-header {
	    height: 40px;
	    background-color: #FCFDFF;
	    margin-top: 0px;
	}
	.content-wrapper .tab-header{
		padding-top: 0;
		padding-left:28px;
	}
	.content-wrapper .content-header .header-tab-item {
	    height: 40px;
	    line-height: 40px;
	    padding: 8px 20px 12px 20px;
	    font-size: 14px;
	    color: #919191;
	    cursor:pointer;
	}
	.content-wrapper .content-header .header-tab-item.active {
	    background-color: #ecf0f5;
	    border-top: 2px solid #c11f2e;
	}
  </style>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>

<body  class="hold-transition skin-blue sidebar-mini detection">
 <div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="advertisement-list">
		<section class="content-header">
		  <h1>  广告位管理 </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active"> 广告位管理 </li>
		  </ol>
		</section> 
		<div class="content-header tab-header" >
	  		<a href="javascript:void(0);" class="active header-tab-item">站内广告</a>
	  		<a href="${ctx}/advertisement/classifiedAdsList.htm" class="header-tab-item">类别广告</a>
	  	</div>
	   <section class="content">
	    <div class="row">
			<section class="col-md-12">
				<div class="box box-solid ">
					<div class="box-body customCol">
		              <form class="form-horizontal" name="" id="" method="get" action=""   @submit.prevent="onSearch">
				   		 <div class="row">
	                  		<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
			                   <div class="row">
						              <div class="col-sm-5 col-md-4 col-lg-3 margin10">
						                  <div class="form-group-sm">
						                      <label for="orderStatus" class="col-sm-4  col-lg-4 control-label">广告标题 </label>
						                      <div class="col-sm-7  col-lg-8" >
						                        <input type="text" class="input-sm form-control" id="title" name="title"  v-model="queryParams.content" >
						                      </div>
						                  </div>
						              </div>
						               <div class="col-sm-7 col-md-4 col-lg-3 margin10">
						                  <div class="form-group-sm">
						                      <label for="orderStatus" class="col-sm-4  col-lg-4 control-label">广告类型 </label>
						                      <div class="col-sm-7  col-lg-8" >
						                          <select id="categoryId" name="categoryId" class="input-sm form-control" v-model="queryParams.categoryId">
						                          	<option value="">全部</option>
						                          	<option v-for="item in initData.category" :value="item.categoryId">{{ item.categoryName }}</option>
						                          </select>
						                      </div>
						                  </div>
						              </div> 
						              <div class="col-sm-7 col-md-4 col-lg-3 margin10">
						                  <div class="form-group-sm">
						                      <label for="orderStatus" class="col-sm-4  col-lg-4 control-label">广告页面 </label>
						                      <div class="col-sm-7  col-lg-8" >
						                          <select id="extend1" name="extend1" class="input-sm form-control" v-model="queryParams.extend1">
						                          	<option value="">全部</option>
						                          	<option v-for="item in initData.page" :value="item.pageId">{{ item.pageName }}</option>
						                          </select>
						                      </div>
						                  </div>
						              </div> 
						              <div class="col-sm-7 col-md-4 col-lg-3 margin10">
						                  <div class="form-group-sm">
						                      <label for="orderStatus" class="col-sm-4  col-lg-4 control-label">广告位置 </label>
						                      <div class="col-sm-7  col-lg-8" >
						                          <select id="extend2" name="extend2" class="input-sm form-control" v-model="queryParams.extend2">
						                          	<option value="">全部</option>
						                          	<option v-for="item in initData.position" :value="item.positionId">{{ item.positionName }}</option>
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
						                           <!--  <option value="DRAFT">草稿</option> -->
						                            <!-- <option value="PUBLISHED">已发布</option> -->
						                             <option value="PUBLISHED">启用</option>
						                             <option value="HOLD">停用</option>
						                             <option value="END">已结束</option>
						                             <option value="PUT">投放中</option>
						                            
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
	              <a href="${ctx}/advertisement/edit.htm" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>
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
<script type="text/javascript" src="${ctx}/js/app/advertisementList.js"></script>
</body>
</html>