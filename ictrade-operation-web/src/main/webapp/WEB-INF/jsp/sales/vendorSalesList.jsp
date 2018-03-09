<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">

<head>
	<!-- jQuery 1.11.3 -->
	<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
	<title>供应商销售</title>
	<link rel="stylesheet" href="${ctx}/js/credenceUpload/credenceUpload.css?20161229" />
  	<link rel="stylesheet" href="${ctx}/css/app/vendor.css" />
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	  
<div class="content-wrapper" id="vendorManageList">
	<section class="content-header">
	 	<h1>
		  供应商销售
		</h1>
	</section>
	<section class="content" >
		<div class="row">
			<div class="col-md-12">
	   			<div class="box box-solid ">
	   				<div class="box-body customCol">
	   					<form id="seachForm" class="form-horizontal" onsubmit="return false">
				   				<lemon-form  
								:form-data="formData"   
								@on-search="searchData"
								@on-add="on-add"
								></lemon-form>
	   					
	   					</form>
	   				</div>
	   			</div>
	   		</div>
		</div>
		<div class="row">
	        <section class="col-sm-12">
		          <div class="box ">
		          		<!-- <div class="box-header">
		   				</div> -->
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
	
	<div class="lose-describe describe-layer" style="display: none;">
		<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate" id="salesDescribeLayer">
			<div class="form-group">
				<h2>确定该供应商已失效？</h2>
				<div>失效供应商需走审批流程。失效后，该供应商的商品无法报价和上传商品。</div>
				<div class="discribe-textarea">
					<textarea class="form-control vendor-input inline" v-model="loseDescribe" maxlength="100"></textarea>
					<span>{{loseDescribe.length}}/100</span>
				</div>
			</div>
		</form>
	</div>
</div>


	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ---->
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script src="${ctx}/js/app/vendorSalesList.js"></script>

</div>	    
</body>
</html>