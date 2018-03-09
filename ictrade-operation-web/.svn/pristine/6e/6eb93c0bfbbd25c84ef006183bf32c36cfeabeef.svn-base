<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<html lang="zh-CN">
<head>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <title>账期订单详情</title>

  <!-- 引入样式 -->
  <link rel="stylesheet" href="${ctx}/css/lib/iview.css">
  <!-- 引入组件库 -->

  <style>
    .form-content {
        position: relative;
        top: 7px;
    }

    .account-status-button {
        display: inline-block;
        margin-left: 50px;
        border-color: #dddee1;
        width: 60px;
        height: 36px;
        color: white;
        line-height: 36px;
        text-align: center;
        font-size: 16px;
        position: relative;
        top: -5px;
    }
	.account-status-button.normal{
		background-color: #13af15;
    	border-color: #13af15;
	}
	.account-status-button.frozen{
		background-color: #eb0038;
    	border-color: #eb0038;
	}
    .company-name {
        display: inline-block;
        margin-left: 25px;
        font-size: 30px;
        font-weight: bold;
    }

  </style>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
    <input id="enterpriseId" value="${enterpriseId}">
	<div class="content-wrapper" id="order-list">
	    <section class="content-header">
            <h1>账期订单详情</h1>
        </section>
		<!--公司名称 -->
		<div style="margin-top: 30px;">
		    <span id="groupName" class="company-name"></span>
            <span id="accountPeriodStatus" class="account-status-button"></span>
        </div>
		
        <div class="box-body customCol" style="margin: 10px;">
          <form class="form-horizontal" novalidate="novalidate">
            <div>
              <div class="col-sm-11 col-sm-6 col-md-6 col-lg-4">
                <div class="form-group"><label class="col-sm-4 col-md-4 col-lg-2 control-label"> 授信额度： </label>
                  <div id="creditQuota" class="col-sm-7 col-md-8 col-lg-9 checkVal form-content" >
                  </div>
                  <input id="currency"  type="hidden" >
                </div>
              </div>
              <div class="col-sm-11 col-sm-6 col-md-6 col-lg-4">
                <div class="form-group"><label class="col-sm-4 col-md-4 col-lg-2 control-label"> 账期余额： </label>
                  <div id="" class="col-sm-7 col-md-8 col-lg-9 checkVal form-content">
                    <span id="realtimeCreditQuota" v-show="btnType"></span>
                    <input type="text" id="realtimeCreditQuotaInput" v-show="!btnType">
                    <a href="javascript: void 0" @click="onSaveModifyClick">{{btnType ? '修改' : '保存'}}</a>
                  </div>
                </div>
              </div>
              <div class="col-sm-11 col-sm-6 col-md-6 col-lg-4">
                <div class="form-group"><label class="col-sm-4 col-md-4 col-lg-2 control-label"> 授信期限： </label>
                  <div id="creditDeadline" class="col-sm-7 col-md-8 col-lg-9 checkVal form-content">
                  </div>
                </div>
              </div>
              <div class="col-sm-11 col-sm-6 col-md-6 col-lg-4">
                <div class="form-group"><label class="col-sm-4 col-md-4 col-lg-2 control-label"> 对账日期： </label>
                  <div id="checkDate" class="col-sm-7 col-md-8 col-lg-9 checkVal form-content">
                  </div>
                </div>
              </div>
              <div class="col-sm-11 col-sm-6 col-md-6 col-lg-4">
                <div class="form-group"><label class="col-sm-4 col-md-4 col-lg-2 control-label"> 对账周期： </label>
                  <div id="checkCycle" class="col-sm-7 col-md-8 col-lg-9 checkVal form-content">
                  </div>
                </div>
              </div>
              <div class="col-sm-11 col-sm-6 col-md-6 col-lg-4">
                <div class="form-group"><label class="col-sm-4 col-md-4 col-lg-2 control-label"> 付款日期： </label>
                  <div id="payDate" class="col-sm-7 col-md-8 col-lg-9 checkVal form-content">
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>


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
                        </div>

                   </div>
               </section>
            </div>


        </section>

    </div>

	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<script src="${ctx}/js/lib/iview.min.js"></script>
</div>

<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script type="text/javascript" src="${ctx}/js/app/paymentDaysDetail.js"></script>


</body>
</html>
