<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
 
 <!-- 首部导航 -->
  <header class="main-header">
    <!-- Logo --> 
    <a href="#" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
	    <span class="logo-mini">YKY</span>
      <!-- logo for regular state and mobile devices -->
	    <span class="logo-lg"><b>YKY</b>易库易</span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
       <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>
      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li class="dropdown">
              <span> 欢迎您， <a class="dib vm f14 mt2" href="javascript:void(0);" style="cursor:default; color:#eb0038;"><shiro:principal property="name" /></a></span>
          </li>       
          <li class="dropdown"> 
              <a href="<spring:eval expression="@appProps.getProperty('login.cas.serverUrlPrefix')" />/logout?service=<spring:eval expression="@appProps.getProperty('full.operation.serverUrlPrefix')" />/logout.htm" class="rel" href="#"><i class="icon-out f16"></i>退出</a>
		  </li>  
        </ul>
      </div>
    </nav>
  </header>
  <!--  add by tangrong 2016-10-12 for basic authentication --> 
  <input id="pageToken" type="hidden" value="Basic <shiro:principal property="loginName" />"/>