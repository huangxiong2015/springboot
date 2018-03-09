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
  <title>用户管理</title>
</head>
<style>
.overflow{
	overflow: auto;
}
.box-body{
	padding: 10px 0;
}
em[class*='error'], i[class*='error'] {
	margin-top: 0px;
    position: absolute;
	margin-left: 20px;
    padding: 10px 12px 10px 12px;
    max-width: 272px;
    box-sizing: border-box;
    background-color: #fff4d7;
    font-size: 12px;
    color: #666;
}
em[class*='error']:before, i[class*='error']:before {
    border: solid transparent;
    margin-left: -25px;
    width: 0;
    font-size: 19px;
    border-right-color: #fff4d7;
    color: #fff4d7;
    top: 75%;
}

em[class*='error'] {
	position: absolute;
	top: 0;
	left:225px;
	display: inline-block;
	padding: 8px 10px;
	box-sizing: border-box;
	background-color: #fff4d7;
	font-size: 12px;
	color: #666;  /*transition:top 0.3s;*/
	margin-left:15px;
}
em[class*='error']:before {
	border: solid transparent;
	content: ' ';
	height: 0;
	right: 100%;
	position: absolute;
	width: 0;
	border-width: 5px;
	border-right-color: #fff4d7;
	top: 60%;
	border-width: 8px;
	margin-top: -11px;
}
</style>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>

	  <!-- 右边内容显示 -->
	  <div class="content-wrapper">
	  <section class="content-header">
		  <h1>
		   用户管理
		  </h1>
		</section> 
		<section class="content" id="userList">
			<div class="tree_area">
				<p style="background-color: #f8f8f8;line-height:36px;padding-left:10px;color:#333;font-size:14px;">部门视图</p>
				<div>
				   <ul id="treeDemo" class="ztree"></ul>
				</div>
			</div>
			<div class="h_content_area">
				<p style="background-color: #f8f8f8;line-height:36px;padding-left:10px;color:#333;font-size:14px;">部门员工维护</p>
				<div class="load_complete" style="margin:20px;">
					<p class="mb20 f16">
						<span id="pathName0"></span>
					    <span class="dn">></span> <span id="pathName1"></span>
						<span class="dn">></span> <span id="pathName2"></span>
						<span class="dn">></span> <span id="pathName3"></span>
						<a :href="'${ctx}/user.htm?action=addUser&deptId='+queryParams.deptId" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>
					</p>
					<div class="chart box-body" style="position: relative;" v-if="queryParams.deptId !== ''">
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
			</div>
			<div class="edit_cont" style="display:none;">
    			 <form id="changePassword" class="form-horizontal">
    			 	<div class="box-body col-md-10">
    			 		<div class="form-group ">
	  						<div class="col-md-11">
	  							<label for="password" class="col-sm-3 control-label">密码：<span class="text-red">*</span></label>
							  	<div class="col-sm-9">
							  		<input type="password" name="password" id="password" required="required" placeholder="6-12位英文字母、符号或数字" class="form-control" style="width: 200px;">
							  	</div>
	  						</div>
	  					</div>
	  					<div class="form-group ">
	  						<div class="col-md-11">
	  							<label for="confirmPassword" class="col-sm-3 control-label">确认密码：<span class="text-red">*</span></label>
							  	<div class="col-sm-9">
							  		<input type="password" name="confirmPassword" id="confirmPassword" required="required" placeholder="请再次输入密码" class="form-control" style="width: 200px;">
							  	</div>
	  						</div>
	  					</div>
    			 	</div>
    			 </form>
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
<script type="text/javascript" src="${ctx}/js/app/userList.js"></script>

</html>
