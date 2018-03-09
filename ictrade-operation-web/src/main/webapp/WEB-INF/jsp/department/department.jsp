<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<!--[if IE 8 ]>    <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="zh-CN"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->

<head>
	<!-- jQuery 1.11.3 -->
<script src="${ctx}/js/lib/jquery-1.11.3.min.js"></script>
 <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <link rel="stylesheet" href="${ctx}/js/lib/zTree/zTreeStyle.css"/>
  <link rel="stylesheet" href="${ctx}/css/app/userList.css"/>
  <title>部门管理</title>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>

	  <!-- 右边内容显示 -->
	  <div class="content-wrapper">
	  <section class="content-header">
		  <h1>部门管理</h1>
		</section> 
		<section class="content s-help-maintain" id="maintainList">
			<div class="tree_area">
				<p class="module-tlt">部门视图</p>
				<div>
				   <ul id="treeDemo" class="ztree"></ul>
				</div>
			</div>
			<div class="h_content_area">
				<p class="module-tlt">部门维护</p>
				<div style="margin:20px;">
					<p class="mb20 f16">
						<span v-for="(ele,index) in pathList" v-text="index == 0 ? ele.name : '>' + ele.name"></span>
						<a href="javascript:;" class="btn btn-sm btn-danger pull-right" v-if="!newContentBtn" @click="editMenu()"><i class="fa fa-plus"></i> 新增</a>
					</p>
					<div class="box-body">
						<table class="table table-bordered table-hover " v-if="!newContentBtn">
			    		<thead>
				    		<tr>
				    			<th style="width:15%;">编号</th>
				    			<th style="width:11%;">部门名称</th>
				    			<th style="width:13%;">上级部门</th>
				    			<th style="width:13%;">创建时间</th>
				    			<th style="width:13%;">最后一次编辑时间</th>
				    			<th style="width:14%;">操作人</th>
				    			<th style="width:13%;">操作</th>				    			
				    		</tr>			    			
			    		</thead>
			    		<tbody>
			    			<tr class="show_list" v-if="childrenList.length" v-for="(ele,index) in childrenList">
				    			<td v-text="index+1"></td>
				    			<td v-text="ele.name"></td>
				    			<td v-text="activeMenu.name"></td>
				    			<td v-text="ele.createdDate"></td>
				    			<td v-text="ele.lastUpdateDate"></td>
				    			<td v-text="ele.account"></td>
				    			<td>
					    			<a href="javascript:;" class="oper-btn" @click="editMenu(ele)">编辑</a>					    			
				    			</td>		    			
			    			</tr>
			    			<tr v-if="!childrenList.length">
			    				<td colspan=7>暂无数据</td>
			    			</tr>			    			
			    		</tbody>	    		
		    			</table>
		    			
		    			<div class="new_save" v-if="!newContentBtn" >
		    			 <p class="module-tlt"><span v-if="editStatusTip">新增</span><span v-if="!editStatusTip">编辑</span>部门</p>
		    			 <p>
			    			 <span class="g3">部门名称：</span>
			    			 <input maxlength="12" type="text" id="menuName" style="width:200px;height:25px;line-height:25px;" v-model.trim="menuData.name" @focus="focusNewMenu">
		    			 </p>
		    			 <!-- <p v-if="activeMenu.level==1">
			    			 <span class="g3">表现形式：</span>
			    			 <select v-model="menuData.styleTypeId" style="width:200px;height:25px;line-height:25px;border:1px solid #e2e2e2;border-radius:3px;">
			    			 	<option value="">无</option>
			    			 	<option value="SLIDER">流程图</option>
			    			 	<option value="TAB">Tab标签</option>
			    			 </select>
		    			 </p> -->	    			 
		    			 <p><a @click="saveData" class="btn btn-danger c-btn">保存</a><a class="btn btn-default ml10 c-btn" @click="editMenu()">取消</a></p>
		    			</div>
					</div>					
				</div>
			</div>				   
		</div>
		</section>
		
		<script type="text/javascript">
			var selectCatid = "400,403";
		</script>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ---->
</div>
</div>
</body>
<script type="text/javascript" src="${ctx}/js/lib/jquery.base64.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/zTree/jquery.ztree.all.js"></script>
<script type="text/javascript" src="${ctx}/js/app/department.js"></script>

</html>