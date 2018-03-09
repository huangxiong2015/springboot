<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>活动管理列表</title> 
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
	   				<lemon-form
	                    ref="lemonForm"
	                    :form-data="formData"
	                    @on-search="onSearchClick"	                    
	                  ></lemon-form>	   			
	   			</div>
	   		</div>
	   	</div>
  </div>
      <div class="row">
        <section class="col-sm-12">
          <div class="box">
            <!-- Morris chart - Sales -->
            <div class="box-header with-border">
              <a href="${ctx}/promotion/edit.htm" class="btn btn-sm btn-danger pull-right">
				<i class="fa fa-plus"></i> 新增
			</a>
            </div>
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
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script type="text/javascript" src="${ctx}/js/app/promotionList.js"></script>
</body>
</html>