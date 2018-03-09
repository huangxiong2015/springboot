<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %> 

<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<c:set var="webres" value="${appProps['webstatic.webres']}" scope="request" /> 
<link rel="stylesheet" type="text/css" href="${ctx}/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/base.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/module.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/profile.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/desktop.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/kkpager_red.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/left.css">
<link rel="stylesheet" type="text/css" href="${ctx}/js/layer/skin/layer.css">

<script type="text/javascript" src="${ctx}/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/kkpager.js"></script>
<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctx}/js/ykypage.js"></script>
<script type="text/javascript" src="${ctx}/js/yky.common.js"></script>
<script type="text/javascript" src="${ctx}/js/md5.js"></script>
<c:import url="/WEB-INF/jsp/include/common/dataStatistics.jsp"/>	
<script>
var dataPort = '<spring:eval expression="@appProps.getProperty('gateway.api.quotation.serverUrlPrefix')"/>';
var adminWebUrl = '<spring:eval expression="@appProps.getProperty('admin.serverUrlPrefix')"/>';
var operationWebUrl = '<spring:eval expression="@appProps.getProperty('operation.serverUrlPrefix')"/>';
var ykyUrl = {
		//main : '<spring:eval expression="@appProps.getProperty('main.serverUrlPrefix')" />',
		//admin : '<spring:eval expression="@appProps.getProperty('admin.serverUrlPrefix')" />',
		quotation : '<spring:eval expression="@appProps.getProperty('quotation.serverUrlPrefix')" />',
		transaction : '<spring:eval expression="@appProps.getProperty('gateway.api.transaction.serverUrlPrefix')" />',
		pay : '<spring:eval expression="@appProps.getProperty('gateway.api.pay.serverUrlPrefix')" />',
		portal : '<spring:eval expression="@appProps.getProperty('portal.serverUrlPrefix')" />',
		database: '<spring:eval expression="@appProps.getProperty('gateway.api.basedata.serverUrlPrefix')" />',
		party: '<spring:eval expression="@appProps.getProperty('gateway.api.party.serverUrlPrefix')" />',
		info: '<spring:eval expression="@appProps.getProperty('gateway.api.info.serverUrlPrefix')" />',
		msg: '<spring:eval expression="@appProps.getProperty('gateway.api.message.serverUrlPrefix')" />',
		_this : '${ctx}'
};

</script>