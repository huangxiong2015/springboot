<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<html lang="zh-CN">
<head>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <title>SPU管理</title>
<style type="text/css">
.addspu{
 margin-left:10px;
}

</style>


</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>

	<div class="content-wrapper" id="productstand">
	    <section class="content-header">
            <h1>SPU管理</h1>
        </section>
        <section  class="content">
            <div class="row">
                <section class="col-md-12">
                    <div class="box box-solid ">
                        <div class="box-body customCol">
                           <lemon-form
                                ref="lemonForm"
                                :form-data="formData"
                                @form-submit="formSubmit"
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
                                  ref="lemoGrid"
                            ></rowspan-table>
                        </div>
                   </div>
               </section>
            </div>
        </section>
    </div>
	<jsp:directive.include file="../include/lemon/footer.jsp" />
</div>

<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script type="text/javascript" src="${ctx}/js/app/productstand.js"></script>


</body>
</html>
