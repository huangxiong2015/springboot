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
  <title>运营后台</title>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <link rel="stylesheet" href="${ctx}/js/lib/zTree/zTreeStyle.css"/>
</head>

<style>
.menulist li {
	margin: 0;
	padding: 0;
	line-height: 30px;
	list-style: none;
}

.menulist{
	margin: 0;
	padding: 0;
}
label{
	font-weight: 400;
}
label.col-sm-3{
	padding-left: 0;
}
label > input[type=checkbox]{
	margin-right:5px;
}
</style>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<!---index nav  end   ---->
	<div class="content-wrapper" id="roleAdd">
		<section class="content-header">
			<h1>
		 		角色管理
		  		<small v-if="queryId !== '' ">编辑</small>
		  		<small v-if="queryId === '' ">添加</small>
	  		</h1> 
	  	</section>
	  	
	  	<section class="content" style="color: rgb(102, 102, 102);">
		  	<form name="create" id="create" onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
		  		<div class="row">
		  			<section class="col-lg-12">
		  				<div class="box box-danger ">
		  					<div class="box-header with-border">
							  	<h3 class="box-title">
								  	<span v-if="queryId !=='' ">编辑  -</span> 
								  	<span v-if="queryId === ''">添加  -</span> 
								  	<span>角色管理</span>
							  	</h3>
						  	</div>
						  	<input type="hidden" id="newsId" name="newsId">
						  	<div class="box-body ">
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">角色名称：<span class="text-red">*</span></label>
									  	<div class="col-sm-4">
									  		<input type="text" name="title" id="name" required="required" maxlength="60" :value="initData.name" class="form-control">
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="categoryTypeId" class="col-sm-2 control-label">所属部门：<span class="text-red">*</span></label>
			  							<div class="col-sm-9" id="deptId">
										  	<label class="col-sm-3" v-for="item in partyList"><input type="checkbox" :value="item.id" :checked="item.checked"/>{{item.name}}</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group" v-if="queryId !== ''">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">角色权限：</label>
			  							<div class="col-sm-9">
										  	<ul class="col-sm-12 menulist">
										  		<li class="col-sm-3" v-if="menuList.length === 0">无角色权限</li>
										  		<li class="col-sm-3" v-if="menuList.length > 0" v-for="item in menuList">{{item.menuName}}</li>
										  	</ul>
									  	</div>
			  						</div>
			  					</div>
			  				</div>
		  					<div class="box-footer text-center">
			  					<button type="button" @click="saveData();" class="btn btn-danger c-btn">保存</button> 
			  					<button type="button" @click="cancel" class="btn btn-concle c-btn">取消</button>
		  					</div>
		  				</div>	
		  			</section>
		  		</div>
		  	</form>
		 </section>
		 
	  	</div>
  
	   
	<script type="text/javascript">
		var selectCatid = "400,402";
	</script>
	<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/lib/zTree/jquery.ztree.all.js"></script>
	<!---footer  end   ---->
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-select.js"></script>
<script type="text/javascript" src="${ctx}/js/app/roleAdd.js"></script>
</div>	    
</body>
</html>
