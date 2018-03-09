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
label{
	font-weight: normal;
}
.roleList{
	margin: 0 ;
	padding: 0;
}
.roleList li{
	list-style: none;
	margin: 0 ;
	padding: 0;
}

.roleList > li{
	padding: 5px 0;
}
.with-border{
border-bottom: 1px solid #ccc;
margin-bottom: 15px;
line-height: 24px;
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
	left:425px;
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
.icon-confirm:before{
	content: ''
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
		 		用户管理
		  		<small v-if="queryId !== '' ">编辑</small>
		  		<small v-if="queryId === '' ">添加</small>
	  		</h1> 
	  	</section>
	  	
	  	<section class="content" style="color: rgb(102, 102, 102);">
		  	<form id="addUser" action="#" class="form-horizontal">
		  		<div class="row">
		  			<section class="col-lg-12">
		  				<div class="box box-danger ">
		  					<div class="box-header with-border">
							  	<h3 class="box-title">
								  	<span v-if="queryId !=='' ">编辑  -</span> 
								  	<span v-if="queryId === ''">添加  -</span> 
								  	<span>用户管理</span>
							  	</h3>
						  	</div>
						  	<input type="hidden" id="newsId" name="newsId">
						  	<div class="box-body "><div class="form-group ">
		  						<div v-if="action=='editUser'" class="col-md-11">
			  							<label for="mail" class="col-sm-2 control-label">登录账号：</label>
									  	<div class="col-sm-10" style="padding-top: 7px;">{{ initData.userLoginId }}</div>
			  						</div>
			  					</div>
						  		<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="mail" class="col-sm-2 control-label">电子邮箱：<span class="text-red">*</span></label>
									  	<div class="col-sm-10">
									  		<input type="text" name="mail" id="mail" required="required" maxlength="60" :value="initData.mail" placeholder="电子邮箱作为登录账户名" class="form-control" style="width: 400px;">
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group " v-if="queryId===''">
			  						<div class="col-md-11">
			  							<label for="password" class="col-sm-2 control-label">密码：<span class="text-red">*</span></label>
									  	<div class="col-sm-10">
									  		<input type="password" name="password" id="password" required="required" :value="initData.password" placeholder="6-12位英文字母、符号或数字" class="form-control" style="width: 400px;">
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group " v-if="queryId===''">
			  						<div class="col-md-11">
			  							<label for="confirmPassword" class="col-sm-2 control-label">确认密码：<span class="text-red">*</span></label>
									  	<div class="col-sm-10">
									  		<input type="password" name="confirmPassword" id="confirmPassword" required="required"  :value="initData.confirmPassword" placeholder="请再次输入密码" class="form-control" style="width: 400px;">
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="name" class="col-sm-2 control-label">用户姓名：<span class="text-red">*</span></label>
									  	<div class="col-sm-10">
									  		<input type="text" name="name" id="name" required="required" :value="initData.name" placeholder="50字以内中英文字母、符号或数字" class="form-control" style="width: 400px;">
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">用户性别：</label>
									  	<div class="col-sm-10" id="sex">
									  		<label><input type="radio" name="sex" value="0"/> 男</label>
									  		<label><input type="radio" name="sex" value="1"/> 女</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="title" class="col-sm-2 control-label">所属部门：<span class="text-red">*</span></label>
									  	<div class="col-sm-10">
									  		<ul id="ztree" class="ztree"></ul>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">部门主管：</label>
									  	<div class="col-sm-10">
									  		<label><input type="checkbox" id="manager" name="manager"/> 是</label>
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label for="telNumber" class="col-sm-2 control-label">手机号码：</label>
									  	<div class="col-sn-10">
									  		<input type="text" name="telNumber" id="telNumber" :value="initData.telNumber"  placeholder="请输入手机号码" class="form-control" style="width: 400px;">
									  	</div>
			  						</div>
			  					</div>
			  					<div class="form-group ">
			  						<div class="col-md-11">
			  							<label class="col-sm-2 control-label">用户权限：<span class="text-red">*</span></label>
			  							<div class="col-sm-10">
										  	<ul class="roleList col-md-12">
										  		<li v-for="item in roleVo" class="col-md-12 with-border">
										  			<div class="col-md-3"><input type="checkbox" :value="item.id" :checked="item.check ? true : false"/> {{item.name}}</div>
										  			<ul class="col-md-9" >
										  				<li v-for="list in item.menuNameList" class="col-md-2">{{list}}</li>
										  			</ul>
										  		</li>
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
	var selectCatid = "400,403";
	</script>
	<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/js/lib/zTree/jquery.ztree.all.js"></script>
	<!---footer  end   ---->
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-select.js"></script>
<script type="text/javascript" src="${ctx}/js/common/add.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/app/userAdd.js"></script>
</div>	    
</body>
</html>