<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<!--[if IE 8 ]>    <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="zh-CN"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<c:import url ="/WEB-INF/jsp/include/main/constants.jsp"/>
	<link rel="stylesheet" href="${ctx}/css/app/goods.css">
	<title>商品管理-详情</title>

	
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
	<jsp:directive.include file="../include/main/header.jsp" />
	<!---index header  end   ---->
	 <!---index nav  start   ---->
	<jsp:directive.include file="../include/main/menu.jsp" />
	<!---index nav  end   ---->
	  
	  <!-- 右边内容显示 -->
	  <div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<a href="#" class="header_tab pitch_tab">商品详情</a>
		</section>
	    <!-- Main content -->
		<section ms-controller="list" class="content container-fluid ms-controller">			
			<div class="row mt20">
				<div class="col-xs-1 tr pl0">
					<label>商品编号：</label>
				</div>
				<div class="col-xs-11 c5e">
					<span ms-text="@list.id"></span>
				</div>
			</div>
			<div class="row mt30">
				<div class="col-xs-1 tr">
					<label>类型：</label>
				</div>
				<div class="col-xs-11 c5e">
					<span>{{@list.category1.name}}--{{@list.category2.name}}--{{@list.category3.name}}</span>
				</div>
			</div>
			<div class="row mt30">
				<div class="col-xs-1 tr">
					<label>品牌：</label>
				</div>
				<div class="col-xs-11 c5e">
					<span  ms-attr="{title:brand.name}"  ms-text="@list.brand.name"></span>
				</div>
			</div>
			<div class="row mt30">
				<div class="col-xs-1 tr">
					<label>型号：</label>
				</div>
				<div class="col-xs-11 c5e">
					<span ms-text="@list.partNo"></span>
				</div>
			</div>
			<div class="row mt30">
				<div class="col-xs-1 tr">
					<label>主标题：</label>
				</div>
				<div class="col-xs-11 c5e">
					<span ms-text="@list.title"></span>
				</div>
			</div>
			<div class="row mt30">
				<div class="col-xs-1 tr">
					<label>副标题：</label>
				</div>
				<div class="col-xs-11 c5e">
					<span ms-text="@list.subTitle"></span>
				</div>
			</div>
			<div class="row mt30 goods_list">
				<div class="col-xs-1 tr pt14">
					<label>规格：</label>
				</div>
				<div class="col-xs-11 c5e">
					<dl>
						<dd ms-for="el in @list.specs">
							<div class="goods_imgs"><img ms-attr="{src: el.specImageUrl}" ></div>
							<div class="goods_dis" ms-attr="{title:el.name}" ms-text="el.name"></div>
						</dd>
					</dl>
				</div>
			</div>
			<div class="row mt20 goods_list">
				<div class="col-xs-1 tr pt14">
					<label>颜色：</label>
				</div>
				<div class="col-xs-11 c5e">
					<dl>
						<dd ms-for="el in @list.colors">
							<div class="goods_imgs"><img ms-attr="{src: el.imageUrl}"></div>
							<div class="goods_dis" ms-attr="{title:el.name}"  ms-text="el.name"></div>
						</dd>
					</dl>
				</div>
			</div>
			<div class="row mt20 goods_list">
				<div class="col-xs-1 tr pt14">
					<label>预览图：</label>
				</div>
				<div class="col-xs-11 c5e">
					<dl>
						<dd ms-for="el in @list.previews">
							<div class="goods_imgs"><img ms-attr="{src: el.imageUrl}"></div>
						</dd>
					</dl>
				</div>
			</div>
			<div class="row mt20 goods_list">
				<div class="col-xs-1 tr pt14">
					<label>搭配：</label>
				</div>
				<div class="col-xs-11 c5e">
					<dl>
						<dd ms-for="el in @list.matches">
							<div style="width:100px;height:100px;border:1px solid #e2e2e2;"><img width="98px" height="98px" ms-attr="{src:el.goods.previews[0].imageUrl}"></div>
							<div class="over_tw" style="width:100px;height:30px;border:1px solid #e2e2e2;border-top:0px;line-height:30px;text-align:center;" ms-attr="{title:el.goods.title}" ms-text="el.goods.title"></div>
						</dd>
					</dl>
				</div>
			</div>
			<div class="row mt20 mb20">
				<div class="col-xs-1 tr pt10">
					<label>内容：</label>
				</div>
				<div class="col-xs-11 c5e">
					<div class="goods_content" ms-html=@list.desc></div>
				</div>
			</div>
						
		</section>
		<!-- /.content -->
		
		<div style="margin-left:10%;padding-bottom:60px;margin-top:16px;">
		 	<a href="javascript:window.close();" style="color:#4F86DD;font-size:14px;">&lt;返回</a>
		 </div>
	 
	</div>	  
	   
	<script type="text/javascript">
		var selectCatid = "286,287";
	</script>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/main/footer.jsp" />
	<!---footer  end   ---->

</div>	    
<!-- ./wrapper -->
 <script>
 	
 	var detailList = avalon.define({
 		$id: "list",
 		list: [],
 		id:getQueryString("id")
 	})
 	
 	$.aAjax({
 		
 		 url: ykyUrl.admin+'/rest/goods/'+detailList.id,
 	     data:detailList.id, 
 	     //去掉数据模型中的所有函数
 	     success: function(data) {
 	    	detailList.list=data;
 	     }
 	})
 </script>
</body>
</html>