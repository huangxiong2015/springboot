<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>个人用户列表</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/customerlist.css" />
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="customer-list">
	<section class="content-header">
	  <a href="${ctx}/enterprise.htm" class="header_tab">企业用户</a>
	  <a href="${ctx}/enterprise/customers.htm" class="header_tab pitch_tab">个人用户</a>
	</section>
	<section class="content">
		<div class="row">
	        <section class="col-md-12">
	          <div class="box box-solid ">
	            <div class="box-body customCol">
	              <form class="form-horizontal" method="post" name="seachForm" id="seachForm" @submit.prevent="onSearch">
	                <div class="row">
	                  <div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
	                    <div class="row" >
	                      <div class="col-md-4 margin10">
	                        <label for="auditCode" class="col-sm-4 control-label">邮箱</label>
	                        <div class="col-sm-7">
	                          <input type="text" class="form-control" id="mail" name="mail" placeholder="邮箱" v-model="queryParams.mail" >
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10">
	                        <label for="name" class="col-sm-4 control-label">联系人</label>
	                        <div class="col-sm-7">
	                          <input type="text" class="form-control" id="name" name="name" placeholder="联系人" v-model="queryParams.name" >
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10">
	                        <label for="phone" class="col-sm-4 control-label">手机号码</label>
	                        <div class="col-sm-7">
	                          <input type="text" class="form-control" id="phone" name="phone" placeholder="手机号码" v-model="queryParams.phone" >
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10">
	                        <label for="status" class="col-sm-4 control-label">账号状态</label>
	                        <div class="col-sm-7" >
	                          <select class="form-control" id="status" name="status" v-model="queryParams.status"  >
	                          	<option value="">全部</option>
	                            <option value="PARTY_ENABLED">有效</option>
	                            <option value="PARTY_DISABLED">冻结</option>
	                          </select>
	                        </div>
	                      </div>
			              <div class="col-md-4 margin10"> 
			                  <div class="row">
			                      <label for="registerDate" class="col-sm-4 col-md-4 control-label">注册时间</label>
			                      <div class="col-sm-7 col-md-7 " id="registerDate">
			                          <div id="registerDateDateRange" class="input-daterange input-group">
				                          <input type="text" name="registerStart" id="registerStart" class="form-control"  v-model="queryParams.registerStart"> 
				                          <span class="input-group-addon">至</span> 
				                          <input type="text" name="registerEnd" id="registerEnd" class="form-control"  v-model="queryParams.registerEnd">
			                          </div>
			                      </div>
			                  </div>
			              </div>
			              <div class="col-md-4 margin10"> 
			                  <div class="row">
			                      <label for="lastLoginDate" class="col-sm-4 col-md-4 control-label">最后登录时间</label>
			                      <div class="col-sm-7 col-md-7 " id="lastLoginDate">
			                          <div id="lastLoginDateRange" class="input-daterange input-group">
				                          <input type="text" name="lastLoginStart" id="lastLoginStart" class="form-control"  v-model="queryParams.lastLoginStart"> 
				                          <span class="input-group-addon">至</span> 
				                          <input type="text" name="lastLoginEnd" id="lastLoginEnd" class="form-control"  v-model="queryParams.lastLoginEnd">
			                          </div>
			                      </div>
			                  </div>
			              </div>
	                    </div>
	                  </div>
	                  <div class="col-lg-1">
	                    <button class="btn btn-danger sendData margin10" id="search_all" >
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
            <div class="chart box-body " style="position: relative;">
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
              <form id="exportForm" action="" method="POST" target="_blank" style="display:none;">
				<input type="hidden" name="Authorization"/>
			  </form>
 			  <button id="export" class="btn btn-success sendData margin10" @click="exportExcels"><i class="fa fa-download mr5"></i>导出</button>
            </div>
          </div>
        </section> 
      </div>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript">
	var selectCatid = "225,226";
</script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/customerlist.js"></script>
</body>
</html>