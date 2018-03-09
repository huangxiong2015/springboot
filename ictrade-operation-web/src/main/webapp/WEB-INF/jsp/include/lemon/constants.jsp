<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>  
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<c:set var="webres" value="${appProps['webstatic.webres']}" scope="request" /> 
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<link rel="shortcut icon" href="${ctx}/favicon.ico"/> 
<meta name="renderer" content="webkit">
<meta http-equiv="Access-Control-Allow-Origin" content="*">
 <!-- Bootstrap 3.3.6 -->
<link rel="stylesheet" href="${webres}/lib/bootstrap/3.3.6/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${webres}/lib/font-awesome/4.5.0/css/font-awesome.min.css">
<!-- Date Picker -->
<link rel="stylesheet" href="${webres}/lib/bootstrap-datepicker/1.6.4/bootstrap-datepicker3.min.css">
<!-- DateTime Picker -->
<link rel="stylesheet" href="${webres}/lib/bootstrap-datetimepicker/2.4.4/bootstrap-datetimepicker.min.css">

<link rel="stylesheet" href="${webres}/lib/pace/1.0.2/pace.css">
<!--layer-->
<link rel="stylesheet" href="${webres}/lib/layer/2.2/skin/layer.css">

<link rel="stylesheet" href="${ctx}/css/common/font.css">  
<!-- 自定义 -->  
<link rel="stylesheet" href="${ctx}/css/lemon/lemon.css?v=20170904"> 

<!-- jQuery 2.2.0 -->
<script src="${webres}/lib/jquery/2.2.0/jquery.min.js"></script> 
<script type="text/javascript" src="${ctx}/js/md5.js"></script>
<script>
var dataPort = '<spring:eval expression="@appProps.getProperty('gateway.api.quotation.serverUrlPrefix')"/>';
var adminWebUrl = '<spring:eval expression="@appProps.getProperty('admin.serverUrlPrefix')"/>';
var operationWebUrl = '<spring:eval expression="@appProps.getProperty('operation.serverUrlPrefix')"/>';
var ykyUrl = {
		main : '<spring:eval expression="@appProps.getProperty('main.serverUrlPrefix')" />',
		admin : '<spring:eval expression="@appProps.getProperty('admin.serverUrlPrefix')" />',
		quotation : '<spring:eval expression="@appProps.getProperty('quotation.serverUrlPrefix')" />',
		transaction : '<spring:eval expression="@appProps.getProperty('gateway.api.transaction.serverUrlPrefix')" />',
		product : '<spring:eval expression="@appProps.getProperty('gateway.api.product.serverUrlPrefix')" />',
		pay : '<spring:eval expression="@appProps.getProperty('gateway.api.pay.serverUrlPrefix')" />',
		portal : '<spring:eval expression="@appProps.getProperty('portal.serverUrlPrefix')" />',
		party: '<spring:eval expression="@appProps.getProperty('gateway.api.party.serverUrlPrefix')" />',
		infoPort : '<spring:eval expression="@appProps.getProperty('gateway.api.party.serverUrlPrefix')" />',
		database: '<spring:eval expression="@appProps.getProperty('gateway.api.basedata.serverUrlPrefix')" />', 
		webres : '<spring:eval expression="@appProps.getProperty('webstatic.webres')" />', 
		ictrade_ueditor: '<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')" />',  
		crawler: '<spring:eval expression="@appProps.getProperty('gateway.api.crawler.serverUrlPrefix')" />',
		workflow: '<spring:eval expression="@appProps.getProperty('gateway.api.workflow.serverUrlPrefix')" />',
		info: '<spring:eval expression="@appProps.getProperty('gateway.api.info.serverUrlPrefix')" />',
		msg: '<spring:eval expression="@appProps.getProperty('gateway.api.message.serverUrlPrefix')" />',
		shipment: '<spring:eval expression="@appProps.getProperty(\'gateway.api.shipment.serverUrlPrefix\')" />',
		_this : '${ctx}',
		certificationDeptId : '<spring:eval expression="@appProps.getProperty('deptId')"/>'
};
var operateDeptId = '<spring:eval expression="@appProps.getProperty('operateDeptId')"/>';
</script>