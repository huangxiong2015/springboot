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
  <title>菜单维护</title>
  <link rel="stylesheet" href="${ctx}/css/common/module.css" />
  <link rel="stylesheet" href="${ctx}/css/app/menuMaintain.css" />
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper">  
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/> 
  <div class="content-wrapper" id="featured-add">
		<section class="content-header">
			<a href="#" class="header_tab pitch_tab">模块维护</a>
		</section>
		<section class="content container-fluid">
			<div class="tree_area">
				<div id="ztree" class="ztree">
				</div>
			</div>
			<div class="cont_area" id="contArea">
				<p class="title">维护菜单</p>
				<div style="margin:20px;">
					<p class="mb20">
						<span id="pathName0">&nbsp;</span>
						<em class="dn">></em><span id="pathName1"></span>
						<em class="dn">></em><span id="pathName2"></span>
						<em class="dn">></em><span id="pathName3"></span>
						<a href="${ctx}/menuMaintain.htm" class="btn btn-sm btn-default pull-right ml10"  >返回</a>
						<a href="javascript:void(0);"  v-on:click="editMenu($event)" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>		
					</p>
					<!-- <table class="list_tb">
						
					</table> -->
					<table cellspacing="0" cellpadding="0" class="table table-bordered table-hover" >
			    		<thead class="table-thead">
				    		<tr class="table-tr">
				    			<th style="width:18%;" class="table-th">菜单Id</th>
				    			<th style="width:13%;" class="table-th">类目名称</th>
				    			<th style="width:7%;" class="table-th">表现形式</th>
				    			<th style="width:13%;" class="table-th">创建时间</th>
				    			<th style="width:10%;" class="table-th">操作人</th>
				    			<th style="width:18%; padding:8px 0;" class="table-th">操作</th>				    			
				    		</tr>			    			
			    		</thead>
			    		<tbody>
			    			<tr v-for="(ele,index) in childrenList" class="table-tr">
			    				<td  class="table-td"><span v-text="ele.menuId" v-bind="{'title':ele.menuId}"></span></td>
				    			<td v-text="ele.menuName" class="table-td"></td>
				    			<td class="table-td">
				    				<em v-if="ele.styleTypeId==''">--</em>
				    				<em v-if="ele.styleTypeId=='SLIDER'">树型</em>
				    				<em v-if="ele.styleTypeId=='TAB'">Tab标签</em>			    				
				    			</td>
				    			<td  class="table-td">
				    				<span v-text="ele.createdDate" v-bind="{'title':ele.createdDate}"></span>
				    			</td>
				    			<td  class="table-td" ><span v-text="ele.creatorName" v-bind="{'title':ele.creatorName}"></span></td>
				    			<td class="table-td" style="text-align:center;padding:8px 0;">
				    			<a href="javascript:void(0);" class="mrl10" v-on:click="editMenu(ele,$event)" >编辑</a>
				    			<a href="javascript:void(0);" class="ml10" v-on:click="deleteMenu" v-bind="{'data-menuid':ele.menuId}">删除</a>
				    			<a href="javascript:void(0);" class="ml10" v-on:click="roleManager" v-bind="{'data-menuid':ele.menuId}">角色管理</a>
				    			</td>		    			
			    			</tr>			    			
			    		</tbody>
			    	</table>
			    	<div class="f14  mt10 edit_cont" style="display:none;">
	    			 <p class="mt20 pl20 " id="menuIdItem">
		    			 <span class="g3 menu_label">菜单Id：</span>
		    			 <input maxlength="12" v-model="menuData.menuId" type="text" id="menuId" style="width:200px;height:25px;line-height:25px;" >
	    			 </p>
	    			  <p class="mt20 pl20">
		    			 <span class="g3 menu_label">菜单名称：</span>
		    			 <input maxlength="10" v-model="menuData.menuName" type="text" id="menuName" style="width:200px;height:25px;line-height:25px;" >
	    			 </p>
	    			 <p class="mt20 pl20">
		    			 <span class="g3 menu_label">菜单类型：</span>
		    			 <span  v-text="menuData.menuTypeId" type="text" id="menuTypeId" style="width:200px;height:25px;line-height:25px;"></span>
	    			 </p>
	    			 <p class="mt20 pl20">
		    			 <span class="g3 menu_label">菜单Url：</span>
		    			 <input  v-model="menuData.menuUrl" type="text" id="menuUrl" style="width:500px;height:25px;line-height:25px;">
	    			 </p>
	    			 <p class="mt20 pl20" >
		    			 <span class="g3 menu_label">呈现形式：</span>
		    			 <select  style="width:200px;height:25px;line-height:25px;border:1px solid #e2e2e2;border-radius:3px;" v-model="menuData.styleTypeId">
		    			 	<option value="SLIDER">树型</option>
		    			 	<option value="TAB">Tab标签</option>
		    			 </select>
	    			 </p>
	    			</div>
	    			<!-- 角色管理面板 -->
					<div class="role_cont clearfix">
						<div class="role_item" v-for="(ele,index) in roleList">
							<span class="ck_wrap">
								<!-- <input type="checkbox" class="role_checkbox chk_1"  v-bind="{value:ele.roleTypeId,id:'checkbox_'+index}" v-if="ele.roleCheck > 0" checked="checked" /> -->
								<!-- <input type="checkbox" class="role_checkbox chk_1"  v-bind="{value:ele.roleTypeId,id:'checkbox_'+index}" v-else /> -->
								<input type="checkbox" class="role_checkbox chk_1"  v-bind="{value:ele.roleTypeId,id:'checkbox_'+index}" v-model="ele.roleCheck" />
								<label v-bind="{for:'checkbox_'+index}"></label>
							</span>
							<span class="role_name" v-text="ele.roleName"></span>
						</div>
					</div>
					<!-- 角色管理面板 -->	   
				</div>
			</div>
		</section>
  </div>   
    
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" /> 
<script type="text/javascript" src="${ctx}/js/oss/common.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/zTree/jquery.ztree.all.js"></script>
<script>
	var thisPrefix = '${ctx}';
</script>
<script type="text/javascript" src="${ctx}/js/app/menuMaintainSubList.js"></script> 
</html>