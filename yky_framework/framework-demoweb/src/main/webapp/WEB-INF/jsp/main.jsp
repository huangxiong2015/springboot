<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<c:out value="${ctx}"/>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<c:out value="${ctx}"/>/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<c:out value="${ctx}"/>/style/css3-multiple-accordion-menu/css/style.css">
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/jquery-1.12.3.min.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/JSON/json2.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/layer/layer.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}"/>/js/main/main.js"></script>

<title>DEMO演示</title>
</head>
<body class="easyui-layout">
	<div id="north" data-options="region:'north',border:false" style="height: 70px; background: #B3DFDA; padding: 3px">
		<center>
			<h1>DEMO演示</h1>
		</center>
	</div>
	<div id="west" data-options="region:'west',split:true,title:'菜单'" style="width: 200px; padding1: 1px; overflow: hidden;">
		<ul>
			<li class="block"><input checked="checked" type="checkbox" name="item" id="item1" /> <label for="item1"><i aria-hidden="true" class="icon-users"></i> Friends <span>124</span></label>
				<ul class="options">
					<li><a href="#"><i aria-hidden="true" class="icon-search"></i> Find New Friends</a></li>
					<li><a href="#"><i aria-hidden="true" class="icon-point-right"></i> Poke A Friend</a></li>
					<li><a href="#"><i aria-hidden="true" class="icon-fire"></i> Incinerate Existing Friends</a></li>
				</ul>
			</li>
			<li class="block"><input type="checkbox" name="item" id="item2" /> <label for="item2"><i aria-hidden="true" class="icon-film"></i> Videos <span>1,034</span></label>
				<ul class="options">
					<li><a href="#"><i aria-hidden="true" class="icon-movie"></i> My Videos <span>7</span></a></li>
					<li><a href="#"><i aria-hidden="true" class="icon-download"></i> My Downloaded Videos <span>3</span></a></li>
					<li><a href="#"><i aria-hidden="true" class="icon-warning"></i> My Well Dodgy Videos <span>1,024</span></a></li>
				</ul></li>
			<li class="block"><input type="checkbox" name="item" id="item3" /> <label for="item3"><i aria-hidden="true" class="icon-images"></i> Galleries <span>4</span></label>
				<ul class="options">
					<li><a href="#" target="_blank"><i aria-hidden="true" class="icon-deviantart"></i> My Deviant Art</a></li>
					<li><a href="#" target="_blank"><i aria-hidden="true" class="icon-dribbble"></i> Latest Dribbble Images</a></li>
					<li><a href="#" target="_blank"><i aria-hidden="true" class="icon-flickr"></i> Sample Flickr Stream</a></li>
					<li><a href="#" target="_blank"><i aria-hidden="true" class="icon-picassa"></i> Sample Picasa Stream</a></li>
				</ul></li>
			<li class="block"><input type="checkbox" name="item" id="item4" /> <label for="item4"><i aria-hidden="true" class="icon-microphone"></i> Podcasts <span>1</span></label>
				<ul class="options">
					<li><a href="#" target="_blank"><i aria-hidden="true" class="icon-music"></i> CSS-Tricks</a></li>
				</ul></li>
			<li class="block"><input type="checkbox" name="item" id="item5" /> <label for="item5"><i aria-hidden="true" class="icon-android"></i> Robots <span>3</span></label>
				<ul class="options">
					<li><a href="#" target="_blank"><i aria-hidden="true" class="icon-eye"></i> Hal 9000</a></li>
					<li><a href="#" target="_blank"><i aria-hidden="true" class="icon-cloud"></i> Skynet</a></li>
					<li><a href="#" target="_blank"><i aria-hidden="true" class="icon-reddit"></i> Johnny 5</a></li>
				</ul></li>
		</ul>

	</div>
	<div id="east" data-options="region:'east',split:true,collapsed:true,title:'East'" style="width: 100px;">页面右侧</div>
	<div id="south" data-options="region:'south',border:false" style="height: 60px; background: #A9FACD; padding: 3px;">
		<center>
			<h3>深圳市易库易科技有限公司版权所有</h3>
		</center>
	</div>
	<div id="center" class="easyui-tabs" data-options="region:'center',title:'Center'">
		<div title="描述" style="padding: 20px; display: none;">对APP消息推送功能测试</div>
	</div>

</body>
</html>