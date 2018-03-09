<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>  
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>松下电阻8折特惠活动 - 易库易</title>
<meta name="keywords" content="松下电阻,电子元器件采购,电子元器件优惠活动,易库易"/>
<meta name="description" content="易库易联合新蕾电子（SUNRAY）独家提供松下（Panasonic）电阻8折特惠活动，下单再送闪购券，价格优惠，品质保障，易库易电子元器件全透明交易平台，一站式超级采购!"/> 
<link rel="shortcut icon" href="${ctx}/favicon.ico"/>
<link href="${ctx}/css/common/activitytemplet/common.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/css/common/activitytemplet/kkpager_red.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${webres}/lib/layer/2.2/skin/layer.css">
<link href="${ctx}/css/common/activitytemplet/loginActive.css" rel="stylesheet" type="text/css"/>
</head>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<body class="panasonic-active">
	<div class="seo-setting" title="网页SEO设置"><i class="glyphicon glyphicon-cog"></i></div>
	<div class="seoInfo">
		<h3>网页SEO设置</h3>
		<form action="">
			<div class="form-group">
			   <label for="exampleInputEmail1">网页标题</label>
			   <input type="text" class="form-control" id="title" placeholder="请输入网页标题">
			 </div>
			 <div class="form-group">
			   <label for="exampleInputPassword1">网页关键词</label>
			   <input type="text" class="form-control" id="keywords" placeholder="请输入网页关键词">
			 </div>
			 <div class="form-group">
			   <label for="description">网页描述</label>
			   <textarea id="description" class="form-control" rows="3" placeholder="请输入网页描述"></textarea>
			 </div>
			 <div class="btns mt40">
				 <a href="javascript:;" class="btn btn-default save">保存</a>
				 <a href="javascript:;" class="btn btn-default cancel ml20">取消</a>
			 </div>
		</form>
	</div>
	<div class="top_bg">
		<!-- <div class="floatlayer"></div>
		<div id="bannerUpload" class="btn-upload">更换banner</div> -->
	</div>
	<div class="w mw12">
		<!-- 精选ADI产品 -->
		<div id="adText" class="active-sale-title" contenteditable="true">
			<p class="">没有什么能够“阻”挡</p>
			<p class="">你对八折的向往</p>
			<p class="">易库易携手知名授权分销商新蕾电子</p>
			<p class="">甄选松下优质电阻</p>
			<div unselectable="on" onselectstart="return false;" class="editTips">编辑</div>
		</div>
		<div class="active_search">
			<div class="goods_search rel">
				<div unselectable="on" onselectstart="return false;" class="editTips">编辑</div>
				<input type="text" class="go_search" id="go_condition" maxlength="50" value="搜索更多新蕾松下产品" />
				<span class="search_btn">搜索</span>
				
				<!-- 关联词显示 -->
				<div class="predictive_box" style="display:none;">
					<i class="show_up"></i>
				</div>
			</div>
		</div>
		
		<div class="product-list">
			<div class="list-title-img"></div>
			<div class="product-list-title">
				<ul>
					<li>型号</li>
					<li>制造商</li>
					<li>分销商</li>
					<li>库存</li>
					<li>原价</li>
					<li>现价</li>
					<li>操作</li>
				</ul>
			</div>
			<div class="product-list-box">
				<ul class="zhanwei">
					占个位
				</ul>
			</div>
		</div>
		
		<div class="give-coupon">
			<div class="give-coupon-img"></div>
			<ul class="get-coupon-step">
				<li class="coupon-step-one coupon-step">
					<div class="stepTxt" contenteditable="true">1.下单活动商品</div>
					<div unselectable="on" onselectstart="return false;" class="editTips">编辑</div>
				</li>
				<li class="left-show"></li>
				<li class="coupon-step-two coupon-step">
					<div class="stepTxt" contenteditable="true">2.免费获得闪购优惠券</div>
					<div unselectable="on" onselectstart="return false;" class="editTips">编辑</div>
				</li>
				<li class="left-show"></li>
				<li class="coupon-step-three coupon-step">
					<a href="${ctx }/topic/miaosha.htm">
						<div class="stepTxt"><span>3.进入闪购活动页使用</span></div>
					</a>
					<div unselectable="on" onselectstart="return false;" class="editTips">编辑</div>
				</li>
			</ul>
			<div class="show-coupon-list">
				<div class="coupon-num">
					<span>当前已有</span>
					<span class="num-box"><i>000000</i></span>
					<span>参与活动</span>
				</div>
				<div class="coupon-list-box">
					<div class="zhanwei">占个位</div>
					<ul class="">
						
					</ul>
				</div>	
			</div>	
		</div>
		
		<div class="activity-description">
			<div class="description-title-img"></div>
			<div class="activity-description-list">
				<!-- 使用的时，将这个部分引入到页面中start -->
				<div id="desc" class="ueditor_wrapper"> 
				    <iframe id="iframe" style="width:800px;margin: 0 auto;border: none;min-height:546px;" src="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" scrolling="no" frameborder="0"  allowTransparency="true">
				    </iframe>
				</div>
				<!-- 使用的时，将这个部分引入到页面中end --> 
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctx }/js/lib/jquery-1.11.3.min.js"></script>
	<script src="${ctx}/js/common/request.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/layer/layer.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
	<script type="text/javascript" src="${ctx }/js/app/sunrayActive.js"></script>
</body>
</html>