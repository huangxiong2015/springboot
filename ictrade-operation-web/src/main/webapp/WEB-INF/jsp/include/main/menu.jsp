<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!-- 左边导航  -->
  <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- sidebar menu: : style can be found in sidebar.less -->
      <ul class="sidebar-menu ms-controller" ms-controller="menu">
      	<li>
			<a href="javaScript:;">
				<i class="fa icon-home mr10"></i> <span>首<span style="margin-left:24px;">页</span></span>
			</a>
		</li>
      	<li class="treeview" ms-class="@setActive(el.menuId),treeview" ms-attr="{id:'cat_'+el.menuId}" ms-for="($index, el) in @list" >
			<a href="javaScript:;">
				<i class="fa " ms-class="'icon-'+[el.menuId]"></i> <span class="first-menu">{{el.menuName}}</span><i class="fa fa-angle-left pull-right"></i>
			</a>
			<ul class="treeview-menu">
				<li ms-class="@setActiveLi(sub.menuId)" ms-for="($index, sub) in el.subList" ms-attr="{id:'cat_'+sub.menuId}">
					<!-- <a ms-attr="{href:(el.menuId=='286' || el.menuId=='237' || sub.menuId=='296' || el.menuId=='225' || sub.menuId=='229' || sub.menuId=='297' || sub.menuId == '251' || sub.menuId == '252')?@goodsUrl+sub.menuUrl:@project+sub.menuUrl}">{{sub.menuName}}</a>-->
					<a ms-attr="{href:sub.menuUrl}">{{sub.menuName}}</a>
				</li> 
			</ul>
		</li>
      </ul>
    </section>
    <!-- /.sidebar -->
  </aside>
  
 