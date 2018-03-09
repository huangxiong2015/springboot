<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>分销商列表</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/> 
<style>
.table .table-tr .behide span{width:280px;display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;}
.table tr td img {max-height: 70px; max-width: 80% !important; width: auto;}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="manufacturer-list">
		<section class="content-header">
		  <h1>
		  	 分销商列表
		    <small>列表展示</small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active">分销商列表</li>
		  </ol>
		</section> 
   <section class="content">
	              <form class="form-horizontal" method="get" name="seachForm" id="seachForm" @submit.prevent="onSearch">
    <div class="row">
		<section class="col-md-12">
			<div class="box box-solid ">
				<div class="box-body customCol">
			   		 <div class="row">
                  		<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
		                   <div class="row">
					              <div class="col-sm-5 col-md-4 col-lg-4 margin10">
					                  <div class="form-group-sm">
					                      <label for="title" class="col-sm-4  col-md-5 col-lg-4 control-label">分销商名称 </label>
					                      <div class="col-sm-7 col-md-7 col-lg-8" >
					                          <input type="text" class="input-sm form-control" id="title" name="title" v-model="queryParams.title"  placeholder="分销商名称" >
					                      </div>
					                  </div>
					              </div>
					              <div class="col-sm-7 col-md-4 col-lg-3 margin10">
					                  <div class="form-group-sm">
					                      <label for="author" class="col-sm-4 col-md-4  col-lg-4 control-label">创建人 </label>
					                      <div class="col-sm-7  col-lg-7" >
					                          <input type="text" class="input-sm form-control" id="author" name="author"  v-model="queryParams.author" placeholder="创建人" >
					                      </div>
					                  </div>
					              </div> 
					              <div class="col-sm-7 col-md-4 col-lg-5 margin10"> 
					                  <div class="form-group-sm">
					                      <label for="lastUpdateDate" class="col-sm-4 col-md-4 col-lg-2 control-label">更新时间 </label>
					                      <div class="col-sm-7 col-md-7 col-lg-9 " id="createData">
					                          <div id="createDataRange" class="input-daterange input-group">
						                          <input type="text" name="startDate" id="startDate" class="input-sm form-control"  v-model="queryParams.createDateStart"> 
						                          <span class="input-group-addon">至</span> 
						                          <input type="text" name="endDate" id="endDate" class="input-sm form-control"  v-model="queryParams.createDateEnd">
					                          </div>
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
				</div>
			 </div>
          </section>
      </div>
      <div class="row">
        <section class="col-sm-12">
          <div class="box ">
            <!-- Morris chart - Sales -->
            <div class="box-header with-border">
              <a href= "${ctx}/distributor.htm?action=add" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>
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
			       	</form>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/distributor.js"></script>
</body>
</html>