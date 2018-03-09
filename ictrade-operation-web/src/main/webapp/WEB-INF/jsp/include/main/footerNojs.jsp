<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<!--index footer start-->
<div class="footer_ fix clean">
    <div class="w infooter">
        <p>深圳易库易有限公司 版权所有</p>
        <p>Copyright@2000-2017 yikuyi.com Limited. All rights reserved <span class="partition">|</span><a target="_blank" href="http://www.miitbeian.gov.cn">粤ICP备13009307号</a></p>
    </div>
</div>
<!--index footer end-->
<input type="hidden" id="projectUrl" value="${ctx}">