<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<!--[if IE 8 ]>    <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="zh-CN"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->

<head>
	<!-- jQuery 1.11.3 -->
   <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <title>账期订单列表</title>
  <link rel="stylesheet" href="${ctx}/js/credenceUpload/credenceUpload.css?20161229" />
  <link rel="stylesheet" href="${ctx}/css/app/coupon.css" />
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	  
<div class="content-wrapper" id="paymentOrderList">
	<section class="content-header">
	 	<h1>
		   账期结算
		</h1>
	</section>
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
					   						<label for="title" class="col-sm-4 control-label">YKY客户编码：</label> 
					   						<div class="col-sm-7">
					   							<input type="text" name="partyCode" id="partyCode" placeholder="YKY客户编码" class="form-control" v-model="queryParams.partyCode"/>
					   						</div>
				   						</div>
				   					</div>
				   					
				   					<div class="col-md-4 margin10">
				   						<div class="form-group-sm">
					   						<label for="title" class="col-sm-4 control-label">公司名称：</label> 
					   						<div class="col-sm-7">
					   							<input type="text" name="groupName" id="groupName" placeholder="公司名称" class="form-control" v-model="queryParams.groupName"/>
					   						</div>
				   						</div>
				   					</div>  
				   					
						            <div class="col-md-4 margin10">
						            	<div class="form-group-sm">
						                      <label for="couponCurrency" class="col-sm-4 col-md-4 control-label">账期权限：</label>
					                          <div class="col-sm-7 col-md-7 " id="accountPeriodStatus">
						                          <select class="form-control">
						                          	<option value="">全部</option>
						                            <option value="PERIOD_VERIFIED">正常</option>
						                            <option value="PERIOD_DISABLED">冻结</option>
						                          </select>
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
				                      :query-params="queryParams"
				                      :api="url"
				                      :refresh="refresh" 
				                      :pageflag="pageflag"
				              >
				              </rowspan-table>
				              <!-- <div class="col-sm-12"><button id="export" @click="exportExcels()" class="btn btn-success sendData margin10"><i class="fa fa-download mr5"></i> 导出</button></div>  -->
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
<script src="${ctx}/js/app/paymentdaysList.js"></script>

</div>	    
</body>
</html>
