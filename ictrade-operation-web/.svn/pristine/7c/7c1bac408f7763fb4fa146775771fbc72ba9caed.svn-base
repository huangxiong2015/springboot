<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>交期准确率</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<style>
.pinyin-h{
	display:none;
}
.pinyin-c{
	width:inherit;
	height: 100px;
    overflow-y: auto;
}
.err-bor{
	border-color:#b11919;
}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="manufacturer-list">
		<section class="content-header">
		  <h1>
		  	分销商交期准确率
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active">分销商交期准确率</li>
		  </ol>
		</section> 
   <section class="content">
	   <form name="create" id="create" onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
	      <div class="row">
	        <section class="col-sm-12">
	          <div class="box ">
	            <!-- Morris chart - Sales -->
	            <div class="box-header with-border">
	              <a href= "javascript:;" class="btn btn-sm btn-danger pull-right" @click="addData"><i class="fa fa-plus"></i> 新增</a>
	            </div>
	            <div class="chart box-body">
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
	      <delivery-accuracy-edit
	      		v-if="showModal"
	      		:letter-select="letterSelect"
	      		:leadtime-data = "ldData"
	      		:is-add = "isAdd"
	      		@get-Selects="getSelects"
	            @click-ok="modalOk"
	            @click-close="toggleModal"
	      ></delivery-accuracy-edit>
	      
	   </form>		     
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-modal.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script>
<script type="text/javascript" src="${ctx}/js/app/deliveryAccuracyList.js"></script>
</body>
</html>