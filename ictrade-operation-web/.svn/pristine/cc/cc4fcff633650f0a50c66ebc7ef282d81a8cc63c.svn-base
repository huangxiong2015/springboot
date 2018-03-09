<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>财务核对</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper">	  
	    <!-- Main content -->
		   <c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
		   <div class="content-wrapper" id="finance-list">
			   <section class="content-header">
			   		<h1>财务核对</h1>
			   </section> 	
				<section  class="content">
				 	<div class="row">
					 	<section class="col-md-12">
						 	<div class="box box-solid ">
							 	<div class="box-body customCol">
							       <lemon-form  
								        ref="lemonForm"
			                    		:form-data="formData"
			                    		@on-search="onSearchClick"
			                        ></lemon-form>					
								</div>
						 	</div>	
					 	</section>				 			 	  
				 	</div>
				 	<!-- 表格 -->
					<div class="row">
						<section class="col-sm-12">
				          	<div class="box ">
			      				<div class="chart box-body " style="position: relative;" >
					              <!--表格组件-->
					               <rowspan-table
					                      :columns="gridColumns"
					                      :pageflag="pageflag"
					                      :query-params="queryParams"
					                      :api="url"
					                      :refresh="refresh"  
					                      :show-total="showTotal"
					              >
					              </rowspan-table>					 
					            </div>
			
			               </div>
		       		   </section> 
					</div>
				</section>							
			</div>
			<!-- /.content -->
	
	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ----> 
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script type="text/javascript" src="${ctx}/js/app/finance.js"></script>

</div>	    
</body>
</html>
