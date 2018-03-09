<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- 打开此页面随即刷新父页面 -->
<script>
	if('<c:out value="${closetype}"/>'=='refresh'){
		parent.location.reload();
	}else if('<c:out value="${closetype}"/>'=='toIndex'){
		parent.location.href="myCustomer.htm";
	}
	else{
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index);
	}
</script>