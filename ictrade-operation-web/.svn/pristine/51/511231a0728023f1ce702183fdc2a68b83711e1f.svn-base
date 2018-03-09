<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>文件上传历史记录</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="history-list">
		<section class="content-header">
		  <h1>文件上传历史</h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
		    <li class="active">物料管理</li>
		  </ol>
		</section> 
	<section class="content"> 		
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
            </div>
          </div>
        </section> 
        <form id="exportForm" action="excel/down.htm" method="POST" target="_blank" style="display:none;">
			<input type="hidden" name="Authorization" value="" />"/>
			<input id="docId" type="hidden" name="docId" value=""/>
		</form>
      </div>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/history.js"></script>
</body>
</html>