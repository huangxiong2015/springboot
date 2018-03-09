<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>搜索推广</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<style>
	.mr10{margin-right:10px;}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="recuit-list">
		<section class="content-header">
		  <h1>
		    搜索推广列表
		    <small>列表展示</small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active">搜索推广列表</li>
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
	                    @go-add="goAdd"
	                  ></lemon-form>  
	            </div>
	          </div>
        </section>
      </div>
      <div class="row">
        <section class="col-sm-12">
          <div class="box ">
          	<div class="box-header with-border">
          		<div class="pull-left" style="padding-top:5px;">
          			所有已生效的方案推广商品总数为10条，当前已生效  <span v-text="hasSetRuleNum?hasSetRuleNum:0"></span> 条，您还可以设置  <span v-text="surplusSetRuleNum?surplusSetRuleNum:10"></span> 条。
          		</div>
          		<a :href="previewUrl" target="_blank" class="btn btn-sm btn-danger pull-right">预览</a>
			</div>
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
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/searchPromotionList.js"></script>
</body>
</html>