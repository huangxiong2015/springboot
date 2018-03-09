<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<c:import url="/WEB-INF/jsp/include/common/dataStatistics.jsp"/>
<div class="header">
         <shiro:guest>
	       <div class="wrapper auto ">
	         <div class="l dt f0">
				    <a class="dib vm f14" href="<spring:eval expression="@appProps.getProperty('portal.serverUrlPrefix')" />/index.htm" target="_blank">首页</a>
				    <span class="dib vm spacer ml20 mr20"></span>
				    <a class="dib vm f14" href="${ctx}/reg.htm">免费注册</a>
				    <span class="dib vm spacer ml20 mr20"></span>
				    <a class="dib vm f14 " href="<spring:eval expression="@appProps.getProperty('admin.serverUrlPrefix')" />${pageContext.request.contextPath}/index.htm" />立即登录</a>
			 </div>	
	      	 <ul class="r dt f0 ">
				    <li class="l dli">
				      <div class="li_container">
				          <span class="dib vm f14">客服专线 : 400-930-0083</span>
				      </div>
				    </li>
				    <li class="l spacer"></li>
				    <li class="l dli">
				      <div class="li_container">
				        <span class="dib vm f14 ">服务时间: 09:00-18:00</span>
				      </div>
				    </li>
				  </ul>
			</div>
	  </shiro:guest>
	  <shiro:user>
	     <div class="wrapper auto ">
			  <div class="l dt f0 ">
			    <a class="dib vm f14" href="<spring:eval expression="@appProps.getProperty('portal.serverUrlPrefix')" />/index.htm" target="_blank">首页</a>
			    <span class="dib vm spacer ml20 mr20"></span>
			    <a class="dib vm f14 " href="javascript:void(0);" style="cursor:default; color:#aaa;"><shiro:principal property="name" /></a>
			  	<span class="dib vm spacer ml20 mr20"></span>
			  	<span class="dib vm f14">[<a class="dib vm f14"  style="color:red;" href="<spring:eval expression="@appProps.getProperty('login.cas.serverUrlPrefix')" />/logout?service=<spring:eval expression="@appProps.getProperty('full.operation.serverUrlPrefix')" />/logout.htm">&nbsp;安全退出&nbsp;</a>]</span>
			  </div>
			  <ul class="r dt f0 ">
			    <li class="l dli">
			      <div class="li_container">
			          <span class="dib vm f14">客服专线 : 400-930-0083</span>
			      </div>
			    </li>
			    <li class="l spacer"></li>
			    <li class="l dli">
			      <div class="li_container">
			        <span class="dib vm f14 ">服务时间: 09:00-18:00</span>
			      </div>
			    </li>
			  </ul>
			  </div>
			</div>
	  </shiro:user> 
 </div>
 
 <div class="back_end_box">
	<div class="wrapper auto">
      <a class="l logo dib mt20 poi" href="<spring:eval expression="@appProps.getProperty('portal.serverUrlPrefix')" />/index.htm" target="_blank"></a>
      <a class="l f30 uesr_te mt20 ml10" href="javascript:void(0);" id="sys_title" style="color:#ffffff;cursor:default;">运营后台</a>
	</div>
  </div>
  <jsp:directive.include file="../../include/common/variable.jsp"/> 
  <!--  add by tangrong 2016-10-12 for basic authentication -->
  <input id="pageToken" type="hidden" value="Basic <shiro:principal property="loginName" />"/>