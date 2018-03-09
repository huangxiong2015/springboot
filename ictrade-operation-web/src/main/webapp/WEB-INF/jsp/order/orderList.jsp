<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<html lang="zh-CN">
<head>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <title>订单列表</title>
  <!-- 引入样式 -->
  <link rel="stylesheet" href="${ctx}/css/lib/iview.css">
  <!-- 引入组件库 -->
  <style>
  	.row-table-pagination .check-span{
  		margin-left:8px;
  	}
  	.row-table-pagination .table-checkall-text{
  		top: 1px;
  		position:relative;
  	}
  	.ivu-btn-text:hover, .ivu-btn-text, .ivu-btn-text:focus{
  		    color: #72afd2;
  	}
  	.ivu-btn-text{
  		color: #3c8dbc;
  	}
  </style>
  <style>
    .ivu-btn-text span {
        color: #3c8dbc;
    }
  </style>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>

	<div class="content-wrapper" id="order-list">
	    <section class="content-header">
            <h1>订单列表</h1>
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
                            <rowspan-table
                                  :columns="gridColumns"
                                  :pageflag="pageflag"
                                  :query-params="queryParams"
                                  :api="url"
                                  :refresh="refresh"
                                  :show-total="showTotal"
                                  :checkflag="checkflag"
                                  ref="lemoGrid"
                            ></rowspan-table>
                        </div>
                        <div class="foot-buttons" style="padding: 10px;">
                            <i-button @click="exportData()">导出</i-button>
                            <i-button @click="exportData('all')">导出全部</i-button>
                        </div>

                   </div>
               </section>
            </div>


        </section>

        <div>
            <el-dialog ref="dialog" @put-ok="onPutOk"></el-dialog>
            <upload-dialog ref="uploadDialog" @put-ok="onPutOk"></upload-dialog>
        </div>


    </div>

	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<script src="${ctx}/js/lib/iview.min.js"></script>
</div>

<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script type="text/javascript" src="${ctx}/js/app/orderList.js"></script>


</body>
</html>
