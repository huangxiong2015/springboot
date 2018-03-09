<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">

<head>
	<!-- jQuery 1.11.3 -->
	<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
	<title>供应商管理列表</title>
	<link rel="stylesheet" href="${ctx}/js/credenceUpload/credenceUpload.css?20161229" />
  	<link rel="stylesheet" href="${ctx}/css/app/coupon.css" />
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	  
<div class="content-wrapper" id="vendorManageList">
	<section class="content-header">
	 	<h1>
		  供应商管理
		</h1>
	</section>
	<input type="hidden" id="userId" value="${userId}"/>
	<section class="content" >
		<div class="row">
			<div class="col-md-12">
	   			<div class="box box-solid ">
	   				<div class="box-body customCol">
	   					<form id="seachForm" class="form-horizontal" onsubmit="return false">
	   						<div class="row">
	   							<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
			   						<div class="col-md-4 margin10">
				   						<div class="form-group-sm">
					   						<label for="title" class="col-sm-4 control-label">审核编码：</label> 
					   						<div class="col-sm-7">
					   							<input type="text" name="applyId" id="applyId" placeholder="审核编码" class="form-control" v-model="queryParams.applyId"/>
					   						</div>
				   						</div>
				   					</div>		
				   					<div class="col-md-4 margin10">
				   						<div class="form-group-sm">
					   						<label for="title" class="col-sm-4 control-label">供应商名称：</label> 
					   						<div class="col-sm-7">
					   							<input type="text" name="companyName" id="companyName" placeholder="供应商名称" class="form-control" v-model="queryParams.companyName"/>
					   						</div>
				   						</div>
				   					</div>				   					
				   					<div class="col-md-4 margin10">
						            	<div class="form-group-sm">
						                      <label for="status" class="col-sm-4 col-md-4 control-label">状态：</label>
					                          <div class="col-sm-7 col-md-7 " id="status">
						                          <select class="form-control">
						                          	<option value="">全部</option>
						                            <option value="WAIT_APPROVE">待审核</option>
						                            <option value="APPROVED">通过</option>
						                            <option value="REJECT">不通过</option>						                     
						                          </select>
					                          </div>
					                    </div>
			                        </div>
			                <!--         <div class="col-md-4 margin10">
				   						<div class="form-group-sm">
					   						<label for="title" class="col-sm-4 control-label">申请人：</label> 
					   						<div class="col-sm-7">
					   							<input type="text" name="xxx" id="xxx" placeholder="申请人" class="form-control" v-model="queryParams.xxx"/>
					   						</div>
				   						</div>
				   					</div>	 -->			                        
			                       	                        		                        			                        	                  	   					
				   					<div class="col-md-4 margin10"> 
			                  			<div class="row">
			                      			<label for="createDate" class="col-sm-4 col-md-4 control-label">创建时间：</label>
			                      			<div class="col-sm-7 col-md-7 " id="registerDate">
			                          			<div id="createDateRange" class="input-daterange input-group">
				                          			<input type="text" name="createDateStart" id="createDateStart" class="form-control"  v-model="queryParams.createDateStart"> 
				                          			<span class="input-group-addon">至</span> 
				                          			<input type="text" name="createDateEnd" id="createDateEnd" class="form-control"  v-model="queryParams.createDateEnd">
			                          			</div>
			                      			</div>
			                  			</div>
			              			</div>				   					
						       		<div class="col-md-4 margin10"> 
			                  			<div class="row">
			                      			<label for="checkDate" class="col-sm-4 col-md-4 control-label">审核时间：</label>
			                      			<div class="col-sm-7 col-md-7 " id="registerDate">
			                          			<div id="checkDateRange" class="input-daterange input-group">
				                          			<input type="text" name="applyDateStart" id="applyDateStart" class="form-control"  v-model="queryParams.applyDateStart"> 
				                          			<span class="input-group-addon">至</span> 
				                          			<input type="text" name="applyDateEnd" id="applyDateEnd" class="form-control"  v-model="queryParams.applyDateEnd">
			                          			</div>
			                      			</div>
			                  			</div>
			              			</div>					               
				   				</div>
				   				
				   				
				   				<div class="col-lg-1">
				                    <button class="btn btn-danger sendData margin10" id="search_all" @click="search()">
				                      <i class="fa fa-search"></i>查询
				                    </button>
				                </div>
	   						</div>
	   					</form>
	   					
	   					
	   					
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


<form id="exportForm" action="" method="POST" target="_blank" style="display:none;">
	<input type="hidden" name="Authorization" value="Basic <shiro:principal property="loginName" />"/>
</form>
	 
<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<!---footer  end   ---->
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<%-- <script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script> 查询插件--%>
<script src="${ctx}/js/app/myVendorList2.js"></script>

</div>	    
</body>
</html>