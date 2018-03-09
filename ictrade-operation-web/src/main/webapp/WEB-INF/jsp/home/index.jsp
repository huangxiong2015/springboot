<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">
<head> 
<style>
.c_tip{background:#fff;color:#666;border:solid 1px #eee;}
.wid15{width:15%;}
.info-box-icon .fa{ line-height: 90px; }
.info-box-number i{color:#333; font-size:20px; font-style: normal;}
.homeTitle{ color: #444; padding-bottom: 15px;}
.progress-description, .info-box-text{color:#666}
</style>
<title>后台管理</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="recommend-list">
		<section class="content-header">
		  <h1>
		    后台管理
		    <small>首页</small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		  </ol>
		</section> 
   <section class="content">
    <div class="row">
		<section class="col-md-12">
			<div class="col-md-12 ">
				<h4 class="homeTitle">欢迎进入易库易运营管理后台！</h4>
			</div>
			
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box">
					<span class="info-box-icon bg-yellow"><i class="fa icon-225"></i></span>
					<div class="info-box-content">
						<span class="info-box-text">今日注册企业数:</span>
						<span class="info-box-number"><i class="ac_num" id="regNum"></i></span>
					</div>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box">
					<span class="info-box-icon bg-red"><i class="fa icon-225"></i></span>
					<div class="info-box-content">
						<span class="info-box-text">今日注册个人数</span>
						<span class="info-box-number"><i class="ac_num" id="customerRegNum"></i></span>
					</div>
				</div>
			</div>
			<div class="col-md-3 col-sm-6 col-xs-12">
				<div class="info-box">
					<span class="info-box-icon bg-aqua"><i class="fa icon-225"></i></span>
					<div class="info-box-content">
						<span class="info-box-text">今日登录用户数:</span>
						<span class="info-box-number"><i class="ac_num" id="logNum"></i></span>
					</div>
				</div>
			</div>
          </section>
      </div>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script>
$.aAjax({
	url: ykyUrl.party+'/v1/statistics?type=LOGIN_NUM',
	success:function(number){
		$("#logNum").html(number);
	}
});
$.aAjax({
	url: ykyUrl.party+'/v1/statistics?type=REG_NUM',
	success:function(number){
		$("#regNum").html(number);
	}
});
var date = new Date().toLocaleDateString().replace(/\//g,'-');
var data = {
	registerStart:date,
	registerEnd:date	
}
syncData(ykyUrl.party+'/v1/customers','GET',data,function(res,err){
	if(err == null){
		$("#customerRegNum").html(res.total);
	}
})
		

</script>
</body>
</html>