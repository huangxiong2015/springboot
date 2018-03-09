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
  <title>帮助中心</title>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>

	  <!-- 右边内容显示 -->
	  <div class="content-wrapper">
	  <section class="content-header">
		  <h1>模块维护</h1>
		</section> 
		<section class="content s-help-maintain" id="maintainList">
			<div class="tree_area">
				<p class="module-tlt">帮助中心类目视图</p>
				<div>
				   <ul id="treeDemo" class="ztree"></ul>
				</div>
			</div>
			<div class="h_content_area">
				<p class="module-tlt">维护子类目</p>
				<div style="margin:20px;">
					<p class="mb20 f16">
						<span id="pathName0">帮助中心</span>
						<span v-text="pathList[1] ? '>' + pathList[1].menuName : ''"></span>
						<span v-text="pathList[2] ? '>' + pathList[2].menuName : ''"></span>
						<span v-text="pathList[3] ? '>' + pathList[3].menuName : ''"></span>
						<a href="javascript:;" class="btn btn-sm btn-danger pull-right" v-if="!newContentBtn" @click="editMenu()"><i class="fa fa-plus"></i> 新增</a>
						<a href="javascript:;" class="btn btn-sm btn-danger pull-right" v-if="newContentBtn" @click="newContent()"><i class="fa fa-plus"></i> 新增</a>
					</p>
					<div class="box-body">
						<table class="table table-bordered table-hover " v-if="!newContentBtn">
			    		<thead>
				    		<tr>
				    			<th style="width:15%;">类目名称</th>
				    			<th style="width:11%;">表现形式</th>
				    			<th style="width:13%;">创建时间</th>
				    			<th style="width:13%;">最后一次编辑时间</th>
				    			<th style="width:14%;">操作人</th>
				    			<th style="width:13%;">操作</th>				    			
				    		</tr>			    			
			    		</thead>
			    		<tbody>
			    			<tr class="show_list" v-for="ele in childrenList">
				    			<td v-text="ele.menuName"></td>
				    			<td>
				    				<span v-if="ele.level!==2">--</span>
				    				<span v-if="ele.level==2">
					    				<em v-if="ele.styleTypeId==''">无</em>
					    				<em v-if="ele.styleTypeId=='SLIDER'">流程图</em>
					    				<em v-if="ele.styleTypeId=='TAB'">Tab标签</em>
					    			</span>				    				
				    			</td>
				    			<td v-text="ele.createdDate"></td>
				    			<td v-text="ele.lastUpdateDate"></td>
				    			<td v-text="ele.creatorName"></td>
				    			<td>
					    			<a href="javascript:;" class="oper-btn" @click="editMenu(ele)">编辑</a>
					    			<a href="javascript:;" class="oper-btn" @click="deleteMenu(ele.menuId)">删除</a>
				    			</td>		    			
			    			</tr>			    			
			    		</tbody>	    		
		    			</table>
		    			<table class="table table-bordered table-hover" v-if="newContentBtn">
				    		<thead>
					    		<tr>
					    			<th style="width:17%;">更新时间</th>
					    			<th style="width:11%;">发布时间</th>
					    			<th style="width:11%;">创建人</th>
					    			<th style="width:11%;">状态</th>
					    			<th style="width:14%;">操作</th>				    							    			
					    		</tr>			    			
				    		</thead>
				    		<tbody>
				    			<tr v-for="ele in contentList">
					    			<td v-text="ele.lastUpdateDate"></td>
					    			<td v-text="ele.status=='PUBLISHED'?ele.publishDate:'--'"></td>
					    			<td v-text="ele.author"></td>
					    			<td>
					    				<span v-if="ele.status=='DRAFT'">待发布</span>
					    				<span v-if="ele.status=='PUBLISHED'">已发布</span>
					    				<span v-if="ele.status=='HOLD'">停用</span>
					    			</td>
					    			<td>
					    				<a href="javascript:;" class="oper-btn" v-if="ele.status=='DRAFT' || ele.status=='HOLD'" @click="editStatus('PUBLISHED',ele.newsId)">发布</a></span>
					    				<a href="javascript:;" class="oper-btn" v-if="ele.status=='PUBLISHED'" @click="editStatus('HOLD',ele.newsId)">停用</a></span>
					    				<a href="javascript:;" class="oper-btn" @click="preveiwContent(ele)">预览</a>
					    				<a href="javascript:;" class="oper-btn" v-if="ele.status!=='PUBLISHED'" @click="editContent(ele.newsId)">编辑</a>
					    				<a href="javascript:;" class="oper-btn" v-if="ele.status!=='PUBLISHED'" @click="deleteContent(ele.newsId,'DELETED')">删除</a>
					    			</td>		    			
				    			</tr>			    			
				    		</tbody>	    		
		    			</table>
		    			<div class="new_save" v-if="!newContentBtn" >
		    			 <p class="module-tlt"><span v-if="editStatusTip">新增</span><span v-if="!editStatusTip">编辑</span>分类</p>
		    			 <p>
			    			 <span class="g3">分类名称：</span>
			    			 <input maxlength="5" type="text" id="menuName" style="width:200px;height:25px;line-height:25px;" v-model.trim="menuData.menuName" @focus="focusNewMenu">
		    			 </p>
		    			 <p v-if="activeMenu.level==1">
			    			 <span class="g3">表现形式：</span>
			    			 <select v-model="menuData.styleTypeId" style="width:200px;height:25px;line-height:25px;border:1px solid #e2e2e2;border-radius:3px;">
			    			 	<option value="">无</option>
			    			 	<option value="SLIDER">流程图</option>
			    			 	<option value="TAB">Tab标签</option>
			    			 </select>
		    			 </p>	    			 
		    			 <p>
		    			 	<a @click="saveData" class="btn btn-danger c-btn">保存</a><a class="btn btn-default ml10 c-btn" @click="editMenu()">取消</a>
		    			 </p>
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
<script type="text/javascript" src="${ctx}/js/app/helpMaintainList.js"></script>

</html>