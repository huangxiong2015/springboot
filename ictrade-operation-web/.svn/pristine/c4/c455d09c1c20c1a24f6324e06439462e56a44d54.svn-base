<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
  <title> 精选商品</title>
 <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <style>  
    table tr{height: 40px;}
    .form-box .add-customer a {
    margin-left: 66%;
	}
  </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
	<%-- <jsp:directive.include file="../include/common/header.jsp" /> --%>
	<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	 <div class="content-wrapper" id="featured-list">
		<section class="content-header">
		  <h1>
		    	精选商品列表
		    <small>列表展示</small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active">精选商品列表</li>
		  </ol>
		</section> 
	   <section class="content">
	    <div class="row">
			<section class="col-md-12">
				<div class="box box-solid ">
					<div class="box-body customCol">
		              <form class="form-horizontal" name="seachForm" id="seachForm" method="get" action="${ctx}/featuredProduct.htm">
				   		 <div class="row">
	                  		<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
			                   <div class="row">
						              <div class="col-sm-5 col-md-4 col-lg-3 mt10">
						                  <div class="row">
						                      <label for="status" class="col-sm-4  col-lg-4 col-lg-4 control-label">状态：</label>
						                      <div class="col-sm-8 col-lg-8" >
					                        	<select id="status" name="status" class="input-sm form-control" v-model="queryParams.status">
						                      		<option value="">全部</option>
						                      		<option value="DRAFT">草稿</option>
						                      		<option value="PUBLISHED">已推荐</option>
						                      		<option value="HOLD">停用</option>
						                      	</select>
						                      </div>
						                  </div>
						              </div>
						              <div class="col-sm-7 col-md-4 col-lg-3 mt10">
						                  <div class="row">
						                      <label for="orderSeq" class="col-sm-4  col-lg-4 control-label">位置：</label>
						                      <div class="col-sm-7  col-lg-8" >
						                          <input type="text" class="input-sm form-control" id="orderSeq" name="orderSeq"  placeholder="位置" v-model="queryParams.orderSeq" >
						                      </div>
						                  </div>
						              </div> 
						          </div>
					          </div>
					          <div class="col-lg-1">
			                    <button class="btn btn-danger sendData mt10" id="search_all" >
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
	              <a href="${ctx}/featuredProduct/edit.htm" class="btn btn-sm btn-success pull-right"><i class="fa fa-plus"></i> 新增</a>
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
</body>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/featuredProductList.js"></script>
</html>
