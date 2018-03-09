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
  <link rel="stylesheet" href="${ctx}/js/lib/zTree/zTreeStyle.css"/>
  <link rel="stylesheet" href="${ctx}/css/common/base.css"/>
  <link rel="shortcut icon" href="${ctx}/favicon.ico"/>
  <title>菜单维护</title>
  <link rel="stylesheet" href="${ctx}/css/app/menuMaintain.css" />
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="featured-add">
		<section class="content-header">
		 	<h1>模块维护</h1> 
		</section>
		<section class="content container-fluid">
			<div class="cont_area" id="contArea" style="margin-left:0;width:100%;">
				<p class="title">维护菜单</p>
				<div style="margin:20px;">
					<p class="mb20">
						<span id="pathName0">一级菜单</span>
						<a href="javascript:void(0);"  v-on:click="editMenu($event)" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>
					</p>
					<!-- <table class="list_tb">
						
					</table> -->
					<table cellspacing="0" cellpadding="0" class="table table-bordered table-hover mb20">
			    		<thead class="table-thead">
				    		<tr>
				    			<th style="width:25%;">菜单类型(menuTypeId)</th>
				    			<th style="width:25%;">菜单Id</th>
				    			<th style="width:25%;">创建时间</th>
				    			<th style="width:25%;">操作</th>		    			
				    		</tr>			    			
			    		</thead>
			    		<tbody>
			    			<tr v-for="(ele,index) in dataList">
				    			<td><div class="tabcon" v-text="ele.menuTypeId"></div></td>
				    			<td><div class="tabcon" v-text="ele.menuId" ></div></td>
				    			<td><div class="tabcon" v-text="ele.createdDate" style="text-align:center;"></div></td>
				    			<td  style="text-align:center;"><div class="tabcon"><span class="poi" v-bind="{'data-id':ele.menuTypeId}"  v-on:click="subMenu($event);">详情</span></div></td>    			
			    			</tr>			    			
			    		</tbody>
			    	</table>
			    	<div class="f14 mt10 edit_cont" style="display:none;">
		    			 <!-- <p style="background-color: #f8f8f8;height:30px;line-height:30px" class="pl20 g3"><span >新增</span>分类</p> -->
		    			 <p class="mt20 pl20">
			    			 <span class="g3 menu_label">菜单Id：</span>
			    			 <input maxlength="12"  v-model="menuData.menuId" type="text" id="menuId" style="width:200px;height:25px;line-height:25px;" >
		    			 </p>
		    			 <p class="mt20 pl20">
			    			 <span class="g3 menu_label">菜单名称：</span>
			    			 <input maxlength="10"  v-model="menuData.menuName" type="text" id="menuName" style="width:200px;height:25px;line-height:25px;" >
		    			 </p>
		    			 <p class="mt20 pl20">
			    			 <span class="g3 menu_label">菜单类型：</span>
			    			 <input   type="text" v-model="menuData.menuTypeId" id="menuTypeId" style="width:200px;height:25px;line-height:25px;">
		    			 </p>
		    			 <p class="mt20 pl20">
			    			 <span class="g3 menu_label">菜单Url：</span>
			    			 <input   type="text" v-model="menuData.menuUrl" id="menuUrl" style="width:500px;height:25px;line-height:25px;">
		    			 </p>
		    			 <p class="mt20 pl20" >
			    			 <span class="g3 menu_label">呈现形式：</span>
			    			 <select  style="width:200px;height:25px;line-height:25px;border:1px solid #e2e2e2;border-radius:3px;" >
			    			 	<option value="SLIDER">树型</option>
		    			 		<option value="TAB">Tab标签</option>
			    			 </select>
		    			 </p>
	    			</div>	   
				</div>
			</div>
		</section>
  </div>   
    
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" /> 
<script type="text/javascript" src="${ctx}/js/oss/common.js"></script>
<script>
	var thisPrefix = '${ctx}';
</script>
<script type="text/javascript" src="${ctx}/js/app/menuMaintainList.js"></script> 
</html>