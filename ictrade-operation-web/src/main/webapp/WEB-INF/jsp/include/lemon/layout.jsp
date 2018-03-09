<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
  
<jsp:directive.include file="header.jsp" />

<!-- 左边导航  -->
  <aside class="main-sidebar" id="menu">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- sidebar menu: : style can be found in sidebar.less -->
      	<ul class="sidebar-menu ms-controller" >
	      	<li>
				<a href="./">
					<i class="fa icon-home mr10"></i> <span>首<span style="margin-left:24px;">页</span></span>
				</a>
			</li>
		</ul>
      <left-menu :api="api" :params="params"></left-menu>
     <!--  <ul class="sidebar-menu ms-controller" >
      	<li>
			<a href="./">
				<i class="fa icon-home mr10"></i> <span>首<span style="margin-left:24px;">页</span></span>
			</a>
		</li>
      	<li v-for="(el, $index) in list" :id="'cat_'+ el.menuId"  :class="{ 'active': retActive == el.menuId}" > 
			<a href="javaScript:;">
				<i class="fa fa-circle-o"></i><span class="first-menu">{{el.menuName}}</span><i class="fa fa-angle-left pull-right"></i>
			</a>
			<ul class="treeview-menu">
				<li v-for="(sub, $index) in el.subList" :id="'cat_'+ sub.menuId" class="treeview" :class="{ 'active': retActiveLi == sub.menuId}" @click="setActiveLi(el.menuId, sub.menuId)">   
					<a :href="sub.menuUrl"><i class="fa fa-circle-o"></i> {{sub.menuName}}</a>
				</li> 
			</ul>
		</li>
      </ul> -->
    </section>
    <!-- /.sidebar -->
  </aside>
  
 