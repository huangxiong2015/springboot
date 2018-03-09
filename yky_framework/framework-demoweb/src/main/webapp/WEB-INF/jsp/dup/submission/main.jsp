<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<c:out value="${ctx}"/>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<c:out value="${ctx}"/>/js/easyui/themes/icon.css">
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/jquery-1.12.3.min.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/JSON/json2.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/layer/layer.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/dup/submission/main.js"></script>
<title>spring防重复提交ＤＥＭＯ页面</title>
</head>
<body>
	<div class="easyui-panel" style="width: 400px; text-align: center;">
		<div style="padding: 10px 60px 20px 60px">
			<form id="dupSubmissionForm" action="dupSubmission/submit.htm" method="post">

				<input name="SPRINGMVC_SUBMISSION_TOKEN" type="hidden" value="${SPRINGMVC_SUBMISSION_TOKEN}"/>

				<table cellpadding="5px">
					<tr>
						<td>用户名：</td>
						<td><input class="easyui-textbox" type="text" name="userName" data-options="required:true"></input></td>
					</tr>
					<tr>
						<td>邮箱：</td>
						<td><input class="easyui-textbox" type="text" name="email" data-options="required:true"></input></td>
					</tr>
					<tr>
						<td>密码：</td>
						<td><input class="easyui-textbox" type="password" name="password" data-options="required:true"></input></td>
					</tr>
					<tr>
						<td>密码确认：</td>
						<td><input class="easyui-textbox" type="password" name="repPassword" data-options="required:true"></input></td>
					</tr>
				</table>
			</form>
		</div>
		<div style="text-align: center; padding: 5px">
		<!-- -->
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="ajaxSubmitForm()" id="ajaxSubmitButton" >&nbsp;</a> &nbsp;&nbsp;&nbsp;
		 
			<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="submitForm()">提交</a> &nbsp;&nbsp;&nbsp; 
			<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="clearForm()">清空</a>
		</div>
	</div>
</body>
</html>