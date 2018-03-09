<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>诚聘英才</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="recuit-list">
		<section class="content-header">
		  <h1>
		    诚聘英才列表
		    <small>列表展示</small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active">诚聘英才列表</li>
		  </ol>
		</section> 
   <section class="content">
    <div class="row">
		<section class="col-md-12">
			<div class="box box-solid ">
				<div class="box-body customCol">
	              <form class="form-horizontal" name="seachForm" id="seachForm" method="get" action="${ctx}/recruit.htm">
			   		 <div class="row">
                  		<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
		                   <div class="row">
					              <div class="col-sm-5 col-md-4 col-lg-3 margin10">
					                  <div class="form-group-sm">
					                      <label for="orderCode" class="col-sm-4  col-lg-4 col-lg-4 control-label">类型 </label>
					                      <div class="col-sm-8 col-lg-8" id="app-select">
					                          <select-control
											        :id="id"
											        :name="id"
											        :api="selectUrl"
											        :option-id = "id"
											        :option-name = "name"
											        :selected-value = "queryParams.categoryId" 
											    ></select-control>
					                      </div>
					                  </div>
					              </div>
					              
					              <div class="col-sm-7 col-md-4 col-lg-3 margin10">
						              <div class="form-group-sm">
						                <label for="status" class="col-sm-4 control-label">状态</label>
			                        	<div class="col-sm-7" >
				                          <select class="form-control" id="status"  name="status" v-model="queryParams.status" >
				                          	<option value="">全部</option>
				                            <option value="DRAFT">待发布</option>
				                            <option value="PUBLISHED">已发布</option>
				                            <option value="HOLD">停用</option>
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
              <a href="${ctx}/recruit/edit.htm" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>
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
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script src="${ctx}/assets/lemon/1.0.0/lemon-select.js"></script>  
<script type="text/javascript" src="${ctx}/js/app/recuitList.js"></script>
</body>
</html>