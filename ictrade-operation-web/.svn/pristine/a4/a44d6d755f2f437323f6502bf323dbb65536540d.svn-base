 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
 <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
 <c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<div class="l profile_tree mt30">
	<ul>
		<li class="first">
			<span>基础数据管理</span>
			<ul >
				<li class="second"><a id="item">&bull;&nbsp;物料管理</a></li>
			</ul>
		</li>
		<li class="first">
			<span>运维管理</span>
			<ul >
				<li class="second"><a id="news">&bull;&nbsp;公告资讯</a></li>
				<li class="second"><a id="stockRecommend">&bull;&nbsp;商品推荐</a></li>
				<li class="second"><a id="purchaseRecommend">&bull;&nbsp;需求推荐</a></li>
				<li class="second"><a id="dynamic">&bull;&nbsp;动态维护</a></li>
				<li class="second"><a id="recruit">&bull;&nbsp;诚聘英才</a></li>
				<li class="second"><a id="feedback">&bull;&nbsp;意见反馈</a></li>
				<li class="second"><a id="customerBrand">&bull;&nbsp;品牌客户</a></li>
				<li class="second"><a id="brandManage">&bull;&nbsp;品牌维护</a></li>
				<li class="second"><a id="help">&bull;&nbsp;帮助中心</a></li>
				<li class="second"><a id="advert">&bull;&nbsp;广告维护</a></li>
			</ul>
		</li>
		<li class="first">
			<span>认证管理</span>
			<ul >
			    <shiro:hasAnyRoles name="ADMIN,CUSTOMER_SERVICE">
				<li class="second"><a id="certifiedMember">&bull;&nbsp;认证会员申请</a></li>
				</shiro:hasAnyRoles>
				<shiro:hasAnyRoles name="ADMIN,CUSTOMER_SERVICE">
				<li class="second"><a id="juniorMemberManager">&bull;&nbsp;初级会员审核</a></li>
				</shiro:hasAnyRoles>
				<li class="second"><a id="certifiedMemberManger">&bull;&nbsp;认证会员审核</a></li>
				<li class="second"><a id="comChangeName">&bull;&nbsp;公司名称变更审核</a></li>
			</ul>
		</li>
		<li class="first">
			<span>客户关系管理</span>
			<ul >
			<shiro:hasAnyRoles name="CSO,SALES_MANAGER,SALES_DIRECTOR">
				<shiro:hasAnyRoles name="DEPT_SPONSOR">
					<li class="second"><a id="salesManager">&bull;&nbsp;业务员管理</a></li>
				</shiro:hasAnyRoles>
			</shiro:hasAnyRoles>
			<shiro:hasAnyRoles name="ADMIN,CSO,CSM,CUSTOMER_SERVICE,SALES_DIRECTOR,SALES_MANAGER">
				<shiro:hasAnyRoles name="ADMIN,CSM,CUSTOMER_SERVICE,DEPT_SPONSOR">
					<li class="second"><a id="customerManager">&bull;&nbsp;客户管理</a></li>
				</shiro:hasAnyRoles>
			</shiro:hasAnyRoles>
			<shiro:hasAnyRoles name="CSO,SALES_MANAGER,SALES_DIRECTOR,SALES_OPERATOR">
				<shiro:hasAnyRoles name="DEPT_SPONSOR,SALES_OPERATOR">	
					<li class="second"><a id="myCustomer">&bull;&nbsp;我的客户</a></li>
					<li class="second"><a id="newCustom">&bull;&nbsp;新增客户</a></li>
				</shiro:hasAnyRoles>
			</shiro:hasAnyRoles>
			<shiro:hasAnyRoles name="ADMIN,CSM,CUSTOMER_SERVICE">
				<li class="second"><a id="preCustomManager">&bull;&nbsp;预客户管理</a></li>
			</shiro:hasAnyRoles>
			<shiro:hasAnyRoles name="DEPT_SPONSOR">
				<shiro:hasAnyRoles name="CSM">
					<li class="second"><a id="customServiceManager">&bull;&nbsp;客服管理</a></li>
				</shiro:hasAnyRoles>
			</shiro:hasAnyRoles>
			</ul>
		</li>
		<li class="first">
			<span>系统管理</span>
			<ul >
				<shiro:hasAnyRoles name="CSM,CSO,CUSTOMER_SERVICE,ADMIN,DEPT_SPONSOR,SALES_DIRECTOR,SALES_MANAGER,SALES_OPERATOR">
				<li class="second"><a id="organizedMgr">&bull;&nbsp;机构管理</a></li>
				</shiro:hasAnyRoles>
				<shiro:hasAnyRoles name="ADMIN,DEPT_SPONSOR">
					<li class="second"><a id="deptManager">&bull;&nbsp;部门管理</a></li>
				</shiro:hasAnyRoles>
				<shiro:hasAnyRoles name="ADMIN,DEPT_SPONSOR">
					<li class="second"><a id="deptUserManager">&bull;&nbsp;用户管理</a></li>
				</shiro:hasAnyRoles>
				
				<shiro:hasAnyRoles name="ADMIN">
					<li class="second"><a id="roleManager">&bull;&nbsp;角色管理</a></li>
				</shiro:hasAnyRoles>
				
				<shiro:hasAnyRoles name="ADMIN,CSM,CUSTOMER_SERVICE">
					<shiro:hasAnyRoles name="ADMIN,DEPT_SPONSOR">
						<li class="second"><a id="salesAffiliate">&bull;&nbsp;设置销售客服</a></li>
					</shiro:hasAnyRoles>
				</shiro:hasAnyRoles>
			</ul>
		</li>	
	</ul>
</div>
<script type="text/javascript">
//url收集，如果仅仅是参数不同，需要由参数少到参数长的顺序排列
var menus = {
		//业务员管理
		"salesManager":["/sales.htm"],	
		"customerManager":["/customerMgr.htm"],
		"myCustomer":["/myCustomer.htm"],
		"newCustom":["/customer.htm?action=add"],
		"preCustomManager":["/preCustomManager.htm"],
		"customServiceManager":["/customerService.htm"],
		"organizedMgr":["/organizedMgr.htm"],
		"deptManager":["/dept.htm"],
		"deptUserManager":["/deptUser.htm?action=list"],
		"roleManager":["/ykyRole.htm?","/ykyRole.htm?action=edit"],
		"salesAffiliate":["/salesAffiliate.htm","/salesAffiliate.htm?action=setUp"],
		"dynamic":["/dynamic.htm"],
		"comChangeName":["/entNameVerify.htm?status=1","/entNameVerify.htm"],
		"juniorMemberManager":["/jmVerify.htm?status=1","/jmVerify.htm"],
		"certifiedMemberManger":["/cmVerify.htm?status=1","/cmVerify.htm"],
		"certifiedMember":["/member.htm"],
		"purchaseRecommend":["/purchasePoint.htm"],
        "news":["/news.htm?action=list","news.htm?action=add","news.htm?action=detailNews"],
        "stockRecommend":["/recommend.htm"],
        "recruit":["/recruit.htm"],
        "feedback":["/feedback.htm?action=list"],
        "brandManage":["/recommend.htm?action=toBrand"],
        "item":["/item.htm"],
        "customerBrand":["/customerBrand.htm"],
        "help":["/help.htm"],
        "advert":["/advert.htm"]
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