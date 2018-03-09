<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>公告资讯管理</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="help-edit">
	<section class="content-header">
	  <h1>
			模块维护
	  		<small v-if="contentId!=''">编辑</small>
		    <small v-else>添加</small> 
	  </h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active"><a  :href="newsHome">帮助中心</a></li>
	  </ol>
	</section> 
    <section class="content" style=" color:#666;">
    <form class="form-horizontal" name="create" id="create" onsubmit="return false;">    
    <div class="row">
        <section class="col-lg-12">
          <div class="box box-danger ">
              <!-- Morris chart - Sales -->
                <div class="box-header with-border">
                  <h3 class="box-title">
                  	<span v-if="contentId!=''">编辑  -</span>
		    		<span v-else>添加  -</span>
                  	<span>模块维护</span>
                  </h3>
                </div>
                <div class="box-body "> 
				 <div class="form-group ">
                    <div class="col-md-11">
                        <label for="categoryTypeId" class="col-sm-2 control-label">类型</label>
                        <div class="col-sm-4" v-text="pathName1"></div>
                    </div>
                </div>  
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="title" class="col-sm-2 control-label">子类型</label>
                        <div class="col-sm-4" v-text="pathName2"></div>
                    </div>
                </div>
               <div class="form-group ">
                    <div class="col-md-11">
                        <label for="publishOrg" class="col-sm-2 control-label">次类型</label>
                        <div class="col-sm-4" v-text="pathName3"></div>
                    </div>
                </div>
                <div class="form-group ">
                    <div class="col-md-11">
                        <label for="desc" class="col-sm-2 control-label">内容</label>
                         <div class="col-sm-10" >
		          			<!-- 使用的时，将这个部分引入到页面中start -->
							<div id="desc" class="ueditor_wrapper"> 
							    <iframe style="width:100%;border: none;min-height:546px;" src="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" scrolling="no" frameborder="0"  allowTransparency="true">
							    </iframe>
							</div>
							<!-- 使用的时，将这个部分引入到页面中end --> 
						</div>
                    </div>
                </div>
                
                </div>
				<input type="hidden" id="iframeUrl_Id" value="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" />
                <div class="box-footer text-center">
                    <button type="button" class="btn btn-danger c-btn" onclick="requireSaveData();">保存</button>
                    <button type="button" class="btn btn-concle c-btn" >取消</button>
                </div>
          </div>
          </section>
          </div>
	</form>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/app/helpMaintainEdit.js"></script>

</body>
</html>