<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>企业账期审核</title> 
<style type="text/css"> 
.customCol .hide{
   display:none;
 }
</style>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="enterprise-list">
	<section class="content-header">
	  <h1>企业账期审核</h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active">企业账期审核</li>
	  </ol>
	</section>
	<div class="tabs" style="background: #fff;margin-bottom:10px;padding:0 0 0 30px;">
       <div class="tab" :class="{'active':isActive==0}"><a  href="#" @click="setTab(0)">待审核</a> </div>
       <div class="tab" :class="{'active':isActive==1}"><a  href="#" @click="setTab(1)">全部申请</a> </div>
    </div> 
	<section class="content">
			     <div class="row"   >
				          <section class="col-md-12">
				          <div class="box box-solid ">
				            <div class="box-body customCol" v-show="isActive==0"> 
			                   <lemon-form
			                        :form-data="formData1"    
			                        @form-submit="formSubmit"
			                    ></lemon-form>
				           </div>
				           <div class="box-body customCol" v-show="isActive==1"> 
			                   <lemon-form
			                        :form-data="formData2"
			                        @form-submit="formSubmit"
			                 ></lemon-form>
				            </div> 
				          </div>
				        </section>
					</div>   		
			      <div class="row">
			        <section class="col-sm-12">
			          <div class="box ">
			            <div class="chart box-body " style="position: relative;"  >
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
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script type="text/javascript" src="${ctx}/js/app/crditList.js"></script>

</body>
</html>