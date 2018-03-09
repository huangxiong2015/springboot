<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">

<head>
	<!-- jQuery 1.11.3 -->
	<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
	<title>制造商管理列表</title>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	  
<div class="content-wrapper" id="vendorManageList">
	<section class="content-header">
	 	<h1>
		  制造商管理
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
								></lemon-form>
	   					</form>
	   				</div>
	   			</div>
	   		</div>
		</div>
		<div class="row">
	        <section class="col-sm-12">
		          <div class="box ">
		          		<div class="box-header">
		   					<a href="${ctx}/manageManufacturer/edit.htm" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>
		   				</div>
			            <div class="chart box-body " style="position: relative;">
				              <!--表格组件-->
				               <rowspan-table
			                      :columns="gridColumns"
			                      :pageflag="pageflag"
			                      :query-params="queryParams"
			                      :api="url"
			                      :refresh="refresh" 
				              ></rowspan-table>
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
<script src="${ctx}/js/app/manageManufacturerList.js"></script>

</div>	    
</body>
</html>