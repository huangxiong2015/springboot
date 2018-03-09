<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!-- <!DOCTYPE> -->
<!DOCTYPE html>
<html lang="zh-CN">
<html>
<head>
<title>物料审核列表</title> 
<style type="text/css">
 .ml15{margin-left:15px;} .ml10{margin-left:10px;}
 .mr5{margin-right:5px;}  
 .row-table-pagination .check-span{margin-left:8px;top:4px;}
bootstrap.css:1798
.col-sm-4
</style>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="material-review">
		<section class="content-header">
		  <h1>物料审核</h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
		    <li class="active">物料审核</li>
		  </ol>
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
                  ></lemon-form>
	            </div>
	          </div>
	        </section>
		</div>   		
      <div class="row">
        <section class="col-sm-12">
          <div class="box ">
            <!-- Morris chart - Sales -->
            <div class="chart box-body " style="position: relative;">
              <!--表格组件-->
               <rowspan-table
                      :columns="gridColumns"
                      :pageflag="pageflag"
                      :query-params="queryParams"
                      :api="url"
                      :refresh="refresh"  
                      :checkflag = "checkflag"
                      ref="gridTable"
              >
              </rowspan-table>
				<div class="pull-left pl10">
				       <span class="mr5">选择当前页所有数据</span>
	                   <input type="button" class="btn btn-light" @click="checkPass" value="通过审核">
	                   <input type="button" class="btn btn-light" @click="opendescr" value="不通过审核">
	                   
	            </div>
           </div>

          </div>
        </section> 
      </div>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script type="text/javascript" src="${ctx}/js/app/materialReview1.js"></script>

<script type="text/javascript">
  var selectCatid = "2993,239";
</script>
</body>
</html>
