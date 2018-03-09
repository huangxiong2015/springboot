 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
 <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
 <c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<div class="l profile_tree mt30 left_menu_tree">
	<ul class="vip_left">
	  <shiro:hasPermission name="MENU:VIEW:256">
			<li class="first">
				<span style="border-bottom:1px solid #e1e1e1;"><a id="crmWorkbenchManager" style="color:#333;">我的工作台</a></span>
				<span style="color:#333;" class="rel">客户关系管理<i class="img_down"></i></span>
				<ul>  
				 <shiro:hasPermission name="MENU:VIEW:257">
						<li class="second"><a id="salesManager">&bull;&nbsp;业务员管理</a></li>
				</shiro:hasPermission>	
				<shiro:hasPermission name="MENU:VIEW:258">
						<li class="second"><a id="customerManager">&bull;&nbsp;客户管理</a></li>
				</shiro:hasPermission>	
				<shiro:hasPermission name="MENU:VIEW:259">
						<li class="second"><a id="myCustomer">&bull;&nbsp;我的客户</a></li>
				</shiro:hasPermission>	
				<shiro:hasPermission name="MENU:VIEW:260">
						<li class="second"><a id="newCustom">&bull;&nbsp;新增客户</a></li>
				</shiro:hasPermission>	
				<shiro:hasPermission name="MENU:VIEW:261">
					<li class="second"><a id="preCustomManager">&bull;&nbsp;预客户管理</a></li>
				</shiro:hasPermission>	
				<shiro:hasPermission name="MENU:VIEW:262">
						<li class="second"><a id="customServiceManager">&bull;&nbsp;客服管理</a></li>
				</shiro:hasPermission>	
				</ul>
			</li>
		</shiro:hasPermission>
	</ul>
</div>
<script type="text/javascript">
//url收集，如果仅仅是参数不同，需要由参数少到参数长的顺序排列
var menus = {
		//我的工作台
		"crmWorkbenchManager":["/crm.htm"],
		//业务员管理		
		"salesManager":["/sales.htm"],	
		"customerManager":["/customerMgr.htm"],
		"myCustomer":["/myCustomer.htm"],
		"newCustom":["/customer.htm?action=add"],
		"preCustomManager":["/preCustomManager.htm"],
		"customServiceManager":["/customerService.htm"]
	

}
$(function(){
	var url = window.location.href
	//循环寻找
	for(var id in menus){
		//高亮显示
		var menu = menus[id];
		for(var i = menu.length-1;i>=0;i--){
			if(url.indexOf(menu[i])>=0){
				$(".second a").css({"color":"#666","font-weight":"normal"});
				$("#"+id).css({"color":"#cc0000","font-weight":"bold"});
				break;
			}
		}
		//增加链接
		$("#"+id).attr("href","${ctx}"+menus[id][0]);
	}
	
});
</script>