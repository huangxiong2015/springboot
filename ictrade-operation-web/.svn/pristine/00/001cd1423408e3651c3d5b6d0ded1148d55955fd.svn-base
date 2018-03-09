<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>活动管理列表-商品促销 </title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="activity-list">
		<section class="content-header"><h1>商品促销</h1></section> 
   <section class="content">
   <div class="row">
	   	<div class="col-md-12">
	   		<div class="box">
	   			<div class="box-body customCol">
	   			<form class="form-horizontal" id="seachForm"  @submit.prevent="onSearch">
	   				<div class="row">
	   				<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
	   						<div class="col-md-3 ">
		   						<div class="form-group-sm">
			   						<label for="title" class="col-sm-4 control-label">活动名称</label> 
			   						<div class="col-sm-7">
			   							<input type="text" name="name" id="name"  class="form-control" v-model="queryParams.name" />
			   						</div>
		   						</div>
		   					</div>
		   					<div class="col-md-2 ">
		   						<div class="form-group-sm">
			   						<label for="title" class="col-sm-4 control-label">活动状态</label> 
			   						<div class="col-sm-7">
			   							<select id="prStatus"  name="prStatus" v-model="queryParams.prStatus" class="input-sm form-control" >
						                     <option value="">全部</option>
						                     <option value="NS">启用 </option>
						                     <option value="STOP">停用 </option>
						                     <option value="GOING">进行中 </option>
						                     <option value="OVER">已结束 </option>
						                 </select>
			   						</div>
		   						</div>
		   					</div> 
		   					<div class="col-md-5 ">
			   					<div class="form-group-sm">
			   					     <label for="createData" class="col-sm-4 col-md-4 col-lg-2 control-label">创建时间 </label>
			   					     <div id="createData" class="col-sm-7 col-md-7 col-lg-9 ">
				   					   <div id="createDataRange" class="input-daterange input-group">
				   					     <input type="text" name="startTime" id="startTime" class="input-sm form-control"  v-model="queryParams.startTime">
				   					      <span class="input-group-addon">至</span>
				   					      <input type="text" name="endTime" id="endTime" class="input-sm form-control" v-model="queryParams.endTime">
				   					     </div>
			   					     </div>
			   					  </div>
		   					 </div>
	   				</div> 
	   				<div class="col-lg-1">
		   				<a class="btn btn-danger mt10" @click="onSearch">
		   					<i class="fa fa-search"></i>查询
			            </a></div>
		            </div>
		            </form>
	   			</div>
	   		</div>
	   	</div>
  </div>
      <div class="row">
        <section class="col-sm-12">
          <div class="box">
            <!-- Morris chart - Sales -->
            <%-- <div class="box-header with-border">
              <a href="${ctx}/shoppromotion/information.htm" class="btn btn-sm btn-danger pull-right">
				<i class="fa fa-plus"></i> 新增
			</a>
            </div> --%>
            <div class="chart box-body" style="position: relative;">
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
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript">
var selectCatid = "400,402";
	
</script>

<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/shopPromotionList.js"></script>
</body>
</html>