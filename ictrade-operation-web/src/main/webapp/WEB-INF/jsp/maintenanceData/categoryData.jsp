<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">

<head>
	<!-- jQuery 1.11.3 -->
	<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
	<title>类别数据</title>
	<link rel="stylesheet" href="${ctx}/js/credenceUpload/credenceUpload.css?20161229" />
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	  
<div class="content-wrapper" id="maintenanceData">
	<section class="content-header tab-cont">
	 	<h1 class="tabs">
			<a href="${ctx}/maintenance.htm" class="tab">制造商数据</a>
			<a href="javascript:void(0);" class="tab active">类别数据</a>
		</h1>
	</section>
	<section class="content" >
		<div class="row">
			<div class="col-md-12">
	   			<div class="box box-solid ">
	   				<div class="box-body customCol">
	   					
				   				<lemon-form  
								:form-data="formData"   
								@on-search="searchData"
								
								></lemon-form>
	   					
	   				</div>
	   			</div>
	   		</div>
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
			                      :checkflag="checkflag"
			                      :api="url"
			                      :refresh="refresh" 
				              >
				              </rowspan-table>
				              <div class="pull-left" style="padding-left: 15px;top: -5px;position:relative;"><input type="button" value="更新" @click="updateChecked()" class="btn btn-light"></div>
			            </div>
		          </div>
	        </section>
      </div>
	</section>
</div>


	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ---->
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script src="${ctx}/js/app/categoryData.js"></script>

</div>	    
</body>
</html>