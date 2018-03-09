<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %> 

<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<c:set var="webres" value="${appProps['webstatic.webres']}" scope="request" /> 
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<link rel="shortcut icon" href="${ctx}/favicon.ico"/>

  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.6 -->
  <link rel="stylesheet" href="${ctx}/css/lib/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css">
  <!-- Theme style -->
  <%-- <link rel="stylesheet" href="${ctx}/css/common/base.css"> --%>

	<!--[if lt IE 9]>
	    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js "></script>
	    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js "></script>
	<![endif]-->

	<link rel="stylesheet" type="text/css" href="${ctx }/css/lib/kkpager_red.css" />
	<link rel="stylesheet" href="${ctx }/js/lib/jquery-ui-1.20.0/jquery-ui.css">
	<link rel="stylesheet"src="${ctx }/js/lib/layer/skin/layer.css">

	<link href="${ctx}/css/common/common.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${ctx}/js/lib/avalon/avalon.js"></script>
	<c:import url="/WEB-INF/jsp/include/common/dataStatistics.jsp"/>
	
	<script type="text/javascript" >
		var dataPort = '<spring:eval expression="@appProps.getProperty('gateway.api.quotation.serverUrlPrefix')"/>';
		var adminWebUrl = '<spring:eval expression="@appProps.getProperty('admin.serverUrlPrefix')"/>';
		//var dataPort = 'http://192.168.1.253:9093';
		var ykyUrl = {
				customer : '<spring:eval expression="@appProps.getProperty('customer.serverUrlPrefix')" />',
				main : '<spring:eval expression="@appProps.getProperty('main.serverUrlPrefix')" />',
				admin : '<spring:eval expression="@appProps.getProperty('admin.serverUrlPrefix')" />',
				quotation : '<spring:eval expression="@appProps.getProperty('quotation.serverUrlPrefix')" />',
				transaction : '<spring:eval expression="@appProps.getProperty('gateway.api.transaction.serverUrlPrefix')" />',
				pay : '<spring:eval expression="@appProps.getProperty('gateway.api.pay.serverUrlPrefix')" />',
				portal : '<spring:eval expression="@appProps.getProperty('portal.serverUrlPrefix')" />',
				database: '<spring:eval expression="@appProps.getProperty('gateway.api.basedata.serverUrlPrefix')" />',
				product: '<spring:eval expression="@appProps.getProperty('gateway.api.product.serverUrlPrefix')" />',
				workflow: '<spring:eval expression="@appProps.getProperty('gateway.api.workflow.serverUrlPrefix')" />',
				party: '<spring:eval expression="@appProps.getProperty(\'gateway.api.party.serverUrlPrefix\')" />',
				shipment: '<spring:eval expression="@appProps.getProperty(\'gateway.api.shipment.serverUrlPrefix\')" />',
				info: '<spring:eval expression="@appProps.getProperty('gateway.api.info.serverUrlPrefix')" />',
				msg: '<spring:eval expression="@appProps.getProperty('gateway.api.message.serverUrlPrefix')" />',
				_this : '${ctx}'
		};
		avalon.config({
		    debug: false
		 })
		 
		 var ykyStatus = {
			paymentStatus:{
				ALIPAY:"ALIPAY",  //支付宝支付
				EXT_OFFLINE:"EXT_OFFLINE",   //银行汇款
				UNIONPAY_B2B:"UNIONPAY_B2B", //企业网银支付
				UNIONPAY_B2C:"UNIONPAY_B2C", //银联支付
				WECHATPAY:"WECHATPAY"        //微信支付
			},
			status:{
				WAIT_PAID:"WAIT_PAID",        //未支付
				WAIT_SHIP:"WAIT_SHIP",  //待发货
				WAIT_RECEIVE:"WAIT_RECEIVE",            //已发货
				WAIT_CON_PSENT:"WAIT_CON_PSENT",   //平台确认中
				PSENT_RT_FAIL:"PSENT_RT_FAIL",   //平台确认失败
				PAY_FAILED:"PAY_FAILED",		//支付失败
				COMPLETED:"COMPLETED",        //交易完成
				CANCELLED:"CANCELLED",       //交易关闭
				REFUNDED:"REFUNDED",       //已退款
				WAIT_APPROVE:"WAIT_APPROVE"  //待审核
			}
		}
	</script>
	
	