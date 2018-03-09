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
  <title>领卷详情页</title>
  <style>
  	.ml20{margin-left:20px;}
  </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	  
<div class="content-wrapper" id="couponStatistics">
	<section class="content-header">
	 	<h1>
		   领卷详情
		</h1>
	</section>
	<section class="content" >
		<div class="row">
			<div class="col-md-12">
	   			<div class="box box-solid ">
	   				<div class="box-body customCol">
	   					<div>
	   						<p>
		   						<span v-if="totalStatistics.couponCate=='PLATFORM_PROMO'">【活动推广券】</span >
		   						<span v-if="totalStatistics.couponCate=='EXACT_PROMO'">【定向推广券】</span >
								<span v-if="totalStatistics.couponCate=='OFFLINE_PROMO'">【地推券】</span >
								<span >{{totalStatistics.name}}</span>
		   					</p>
	   					</div>
	   					<div>
	   						<div class="col-md-10"><p> 已派发优惠券<span class="text-red">{{totalStatistics.sendQty}}</span>张<span >，还剩<span class="text-red">{{totalStatistics.totalQty-totalStatistics.sendQty}}</span>张派发中</span>…</p></div>
	   						<div class="col-md-2"><span class="pull-right"><a :href="'${ctx}/coupon/detail.htm?id='+couponId">查看代金券详情</a></span></div>
	   					</div>
	   				</div>
	   			</div>
	   		</div>
		</div> 
		<div class="row">
			<div class="col-md-12">
	   			<div class="box box-solid ">
	   				<div class="box-body customCol">
	   					<form class="form-horizontal" onsubmit="return false" id="seachForm">
	   						<div class="row">
	   							<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
				   					<div class="col-md-4 margin10">
										<div class="form-group-sm">
						                      <label for="createDate" class="col-sm-4 col-md-4 control-label">领券时间</label>
						                      <div class="col-sm-7 col-md-7 " id="createDate">
						                          <div id="createDateRange" class="input-daterange input-group">
							                          <input type="text" name="createDateStat" id="createDateStat" class="form-control"  v-model="queryParams.createDateStat"> 
							                          <span class="input-group-addon">至</span> 
							                          <input type="text" name="createDateEnd" id="createDateEnd" class="form-control"  v-model="queryParams.createDateEnd">
						                          </div>
						                      </div>
						                  </div>
									</div>
									<div class="col-md-4 margin10">
										<div class="form-group-sm">
					                        <label for="useDate" class="col-sm-4 col-md-4 control-label">使用时间</label>
					                        <div class="col-sm-7 col-md-7 " id="useDate">
					                            <div id="useDateRange" class="input-daterange input-group">
						                            <input type="text" name="useDate" id="useDate" class="form-control"  v-model="queryParams.useDate"> 
						                            <span class="input-group-addon">至</span> 
						                            <input type="text" name="useDateEnd" id="useDateEnd" class="form-control"  v-model="queryParams.useDateEnd">
					                            </div>
					                        </div>
					                    </div>
						            </div>
						            <div class="col-md-4 margin10">
				   						<div class="form-group-sm">
					   						<label for="orderId" class="col-sm-4 control-label">订单号</label> 
					   						<div class="col-sm-7">
					   							<input type="text" name="orderId" id="orderId" placeholder="订单号" class="form-control" v-model="queryParams.orderId"/>
					   						</div>
				   						</div>
				   					</div> 
				   					<div class="col-md-4 margin10" v-if="(totalStatistics.couponCate=='EXACT_PROMO')||(totalStatistics.couponCate=='OFFLINE_PROMO')">
				   						<div class="form-group-sm">
					   						<label for="orderId" class="col-sm-4 control-label"	v-if="totalStatistics.couponCate=='EXACT_PROMO'">操作员</label>
											<label for="orderId" class="col-sm-4 control-label" v-if="totalStatistics.couponCate=='OFFLINE_PROMO'">地推人员</label>	 
					   						<div class="col-sm-7">
					   							<input type="text" v-if="totalStatistics.couponCate=='EXACT_PROMO'" name="offlinePartyName" id="offlinePartyName" placeholder="操作员" class="form-control" v-model="queryParams.offlinePartyName"/>
					   							<input type="text" v-if="totalStatistics.couponCate=='OFFLINE_PROMO'" name="offlinePartyName" id="offlinePartyName" placeholder="地推人员" class="form-control" v-model="queryParams.offlinePartyName"/>
					   						</div>
				   						</div>
				   					</div>
				   					<div class="col-md-4 margin10">
				   						<div class="form-group-sm">
					   						<label for="orderId" class="col-sm-4 control-label">账号</label> 
					   						<div class="col-sm-7">
					   							<input type="text" name="account" id="account" placeholder="账号" class="form-control" v-model="queryParams.account"/>
					   						</div>
				   						</div>
				   					</div>
				   					<div class="col-md-4 margin10" >
				   						<div class="form-group-sm">
					   						<label for="statusId" class="col-sm-4 control-label">使用状态</label> 
					   						<div class="col-sm-7" >
					   							<select class="form-control" id="statusId" name="statusId" >
						                          	<option value="">全部</option>
						                            <option value="UNUSE">未使用</option>
						                            <option value="USED">已使用</option>
						                            <option value="EXPIRE">已过期</option>
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
				                      :pageflag = "pageflag"
				                      :show-total = "showTotal"
				              >
				              </rowspan-table>
				              <div class="pull-left ml20">
				                   <input type="button" class="btn btn-light" v-on:click="exportRecord('thisPage')" value="导出">
				                   <input type="button" class="btn btn-light" v-on:click="exportRecord('all')" value="导出全部记录">   
				              </div>
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
<script src="${ctx}/js/app/couponStatisticsDetail.js"></script>
</div>	    
</body>
</html>
