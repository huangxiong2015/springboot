<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
	<meta charset="utf-8"/>
	<meta name="author" content="" />
	<meta name="keywords" content="" />
	<meta name="viewport" content="width=device-width,initial-scale=1" />
	<title></title>
</head>
<body>

<div style="text-align: center;margin:0 auto;width: 1000px; ">
<h1>spring boot（javaLearn）</h1>
<table width="100%" border="1" cellspacing="1" cellpadding="0">
    <tr>
        <td>作者</td>
        <td>教程名称</td>
        <td>地址</td>
    </tr>
    
    <c:forEach var="learn" items="${learnResouces}">
				<tr class="text-info">
					<td>${learn.author}</td>
					<td>${learn.title}</td>
					<td><a href="${learn.url}" class="btn btn-search btn-green" target="_blank"><span>点我</span></a>
					</td>
				</tr>
	</c:forEach>
</table>
</div>
</body>

</html>