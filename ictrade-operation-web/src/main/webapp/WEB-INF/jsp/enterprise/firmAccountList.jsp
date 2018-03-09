<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>企业用户</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/app/customerlist.css" />
<style>
.ml10{margin-left:10px;}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="enterprise-list">
	<section class="content-header">
	  <a href="${ctx}/enterprise.htm" class="header_tab pitch_tab">企业用户</a>
	  <a href="${ctx}/enterprise/customers.htm" class="header_tab">个人用户</a>
	</section>
	<section class="content">
		<div class="row">
	        <section class="col-md-12">
	          <div class="box box-solid ">
	            <div class="box-body customCol">
	            	<lemon-form
	                    ref="lemonForm"
	                    :form-data="formData"
	                    @on-search="onSearchClick"
	                    @go-add="goAdd"
	                  ></lemon-form>  
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
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script type="text/javascript" src="${ctx}/js/app/firmAccountList.js"></script>
</body>
</html>