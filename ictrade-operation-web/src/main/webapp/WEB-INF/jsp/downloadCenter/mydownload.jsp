<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="${ctx}/favicon.ico"/>
<title> 我的下载中心</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="manufacturer-list">
		<section class="content-header">
		  <h1>
		     我的导出
		    <small>列表展示</small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="./"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active">我的导出</li>
		  </ol>
		</section> 
   <section class="content">
	  <form class="form-horizontal" method="get" name="seachForm" id="seachForm" @submit.prevent="onSearch">
       <div class="row">
	        <section class="col-sm-12">
	          <div class="box ">
	            <!-- Morris chart - Sales -->
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
<script type="text/javascript" src="${ctx}/js/app/mydownload.js"></script>
</body>
</html>