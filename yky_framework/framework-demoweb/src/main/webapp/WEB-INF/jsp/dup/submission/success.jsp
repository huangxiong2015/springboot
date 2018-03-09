<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<c:out value="${ctx}"/>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<c:out value="${ctx}"/>/js/easyui/themes/icon.css">
<title>spring防重复提交ＤＥＭＯ页面</title>
</head>
<body>
	<div class="easyui-panel" title="提交成功-按F5按钮测试重复提交" style="width: 400px; text-align: center;">
		<div style="padding: 10px 60px 20px 60px">
			<form id="dupSubmissionForm" action="dupSubmission/submit.htm" method="post">
				<table cellpadding="5px">
					<tr>
						<td>用户名：</td>
						<td><input class="easyui-textbox" type="text" name="userName" value="${userName}"  readonly="readonly"></input></td>
					</tr>
					<tr>
						<td>邮箱：</td>
						<td><input class="easyui-textbox" type="text" name="email" value="${email}"  readonly="readonly"></input></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>


<script type="text/javascript" src="<c:out value="${ctx}"/>/js/jquery-1.12.3.min.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/easyui/jquery.easyui.min.js"></script>


</html>