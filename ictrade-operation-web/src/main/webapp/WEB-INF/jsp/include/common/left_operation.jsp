 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
 <%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
 <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
 <c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<div class="l profile_tree mt30 mb30 left_menu_tree">

	<ul class="vip_left mb0">
	   <a id="mainServerUrlPrefix" href="<spring:eval expression="@appProps.getProperty('main.serverUrlPrefix')"/>" hidden></a>
	   <a id="operationServerUrlPrefix" href="<spring:eval expression="@appProps.getProperty('operation.serverUrlPrefix')"/>" hidden></a>
	   <li class="first"><span><a id="adminWorkbench" href="${ctx }/cms.htm" class="g3">我的工作台</a></span></li>
    <shiro:hasPermission name="MENU:VIEW:225"> 
	   <li class="first">	   		
			<span class="first_title rel" id="101">会员管理<i></i></span>
			<ul class="sub_nav" id="1">
		      <shiro:hasPermission name="MENU:VIEW:226">
				<li class="second"><a id="vipMember">&bull;&nbsp;会员管理</a></li>
			  </shiro:hasPermission>
			</ul>
		</li>
     </shiro:hasPermission>
     
      
       <shiro:hasPermission name="MENU:VIEW:251"> 
		   <li class="first">	   		
				<span class="first_title rel" id="101">审核管理<i></i></span>
				<ul class="sub_nav" id="1">
				    <shiro:hasPermission name="MENU:VIEW:252"> 
					     <li class="second"><a id="enterpriseVerify">&bull;&nbsp;企业账号管理</a></li>
					</shiro:hasPermission>
				</ul>
			</li>
		</shiro:hasPermission>
	 
	 <shiro:hasPermission name="MENU:VIEW:237">		
		<li class="first">
			<span class="first_title rel" id="104">订单管理<i></i></span>
			<ul  class="sub_nav" id="4">
				<shiro:hasPermission name="MENU:VIEW:238">
				<li class="second"><a id="orderManagerList">&bull;&nbsp;订单管理</a></li>			
				</shiro:hasPermission>
			</ul>
		</li>
     </shiro:hasPermission>
     
     
     
	 <shiro:hasPermission name="MENU:VIEW:239">			
		<li class="first">
			<span  class="first_title rel" id="105">基础数据管理<i></i></span>
			<ul  class="sub_nav" id="5">
			<shiro:hasPermission name="MENU:VIEW:298">
				   <li class="second"><a id="basicMaterial">&bull;&nbsp;物料管理</a></li>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="MENU:VIEW:296">	
			      <li class="second"><a id="materailDetection">&bull;&nbsp;物料核对</a></li>
			  </shiro:hasPermission>
			</ul>
		</li>
	</shiro:hasPermission>
		
	 <shiro:hasPermission name="MENU:VIEW:242">		
		<li class="first">
			<span class="first_title rel" id="106">运维管理<i></i></span>
			<ul  class="sub_nav" id="6">
				 <shiro:hasPermission name="MENU:VIEW:243">
					<li class="second"><a id="news">&bull;&nbsp;公告资讯</a></li>
				 </shiro:hasPermission>					 				 	
				 <shiro:hasPermission name="MENU:VIEW:244">
					<li class="second"><a id="stockRecommend">&bull;&nbsp;商品推荐</a></li>
				 </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:245">
					<li class="second"><a id="purchaseRecommend">&bull;&nbsp;需求推荐</a></li>
				 </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:246">
					<li class="second"><a id="dynamic">&bull;&nbsp;动态维护</a></li>
				 </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:247">	
					<li class="second"><a id="recruit">&bull;&nbsp;诚聘英才</a></li>
				 </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:248">	
					<li class="second"><a id="feedback">&bull;&nbsp;意见反馈</a></li>
				 </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:249">	
					<li class="second"><a id="customerBrand">&bull;&nbsp;品牌客户</a></li>
				 </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:250">	
				 <li class="second"><a id="couponsManager">&bull;&nbsp;优惠券管理</a></li>
				 </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:274">	
				 <li class="second"><a id="distributorView">&bull;&nbsp;分销商展示</a></li>
				 </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:275">	
				 <li class="second"><a id="featuredProduct">&bull;&nbsp;精选商品</a></li>
				 </shiro:hasPermission>	
			      <shiro:hasPermission name="MENU:VIEW:276">
				   <li class="second"><a id="help">&bull;&nbsp;帮助中心</a></li>
				   <li class="second"><a id="modelMaintain">&bull;&nbsp;模块维护</a></li>
				  </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:283">
				   <li class="second"><a id="advert">&bull;&nbsp;广告维护</a></li>
				 </shiro:hasPermission>	
				 <shiro:hasPermission name="MENU:VIEW:293">
				   <li class="second"><a id="tools">&bull;&nbsp;仪器与工具</a></li>
				 </shiro:hasPermission>
			</ul>
		</li>
	</shiro:hasPermission>
	
	<shiro:hasPermission name="MENU:VIEW:269">
		<li class="first">
			<span  class="first_title rel" id="109">认证管理<i></i></span>
			<ul  class="sub_nav" id="9">
			 <shiro:hasPermission name="MENU:VIEW:270">
				<li class="second"><a id="certifiedMember">&bull;&nbsp;认证会员申请</a></li>
			 </shiro:hasPermission>	
			 <shiro:hasPermission name="MENU:VIEW:272">	
				<li class="second"><a id="certifiedMemberManger">&bull;&nbsp;认证会员审核</a></li>
			</shiro:hasPermission>	
			</ul>
		</li>
		
 </shiro:hasPermission>
	</ul>
</div>
<script type="text/javascript">
//url收集，如果仅仅是参数不同，需要由参数少到参数长的顺序排列
var menus_admin = {		
		//我的工作台
		"adminWorkbench":["/cms.htm"],
		
		//公司经营品类维护		
		"enterpriseBusiness":["/enterpriseBusiness.htm?action=list","/enterpriseBusiness.htm?action=add","/enterpriseBusiness.htm?action=edit"],
		"bidList":["/admin/bidList.htm"],
		
		
		
		//库存与采购
		"stock":["/admin/stock.htm","/admin.htm?action=handle&id="],
		"purchase":["/admin/purchase.htm","/admin.htm?action=handlePurchase&id="],

		//物料管理 
		"item":["/item.htm"],
		"materialBrand":["/materialBrand.htm"],
		"itemCheck":["/itemCheck.htm"],
		"categorylist":["/item/materialCategoryTree.htm"],
		"categoryParamMgr":["/categoryParamMgr.htm"],
	    "adminAuditTrail":["/adminAuditTrail.htm"],
		
		//运维管理 
        "news":["/news.htm?action=list","/news.htm?action=add","news.htm?action=detailNews"],
        "featuredProduct":["/featuredProduct.htm"],
        "stockRecommend":["/recommend.htm"],
        "purchaseRecommend":["/purchasePoint.htm"],
        "dynamic":["/dynamic.htm"],
        "recruit":["/recruit.htm"],
        "feedback":["/feedback.htm?action=list"],
        "customerBrand":["/customerBrand.htm"],
        "brandManage":["/recommend.htm?action=toBrand"],
        "couponsManager":["/coupons.htm"],
        "distributorView":["/distributorView.htm"],
        "help":["/help.htm"],
        "topsell":["/topsellingTools.htm?action=hotgoods"],
   
        "alternativeMgr":["/alternative.htm"],
        "advert":["/advert.htm"],
        //商品管理
//         "goodsManager":["/goodsMgrList.htm"],
        //系统管理 
    	"organizedMgr":["/organizedMgr.htm"],
		"deptManager":["/dept.htm"],
		"deptUserManager":["/deptUser.htm?action=list"],
		"roleManager":["/ykyRole.htm","/ykyRole.htm?action=edit"],
		"salesAffiliate":["/salesAffiliate.htm","/salesAffiliate.htm?action=setUp"],
		
		//认证管理 
		"comChangeName":["/entNameVerify.htm"],
		"juniorMemberManager":["/jmVerify.htm?status=1","/jmVerify.htm"],
		"certifiedMemberManger":["/cmVerify.htm"],
		"certifiedMember":["/member.htm"],
		//询报价管理 
		"inquireList":["/requestForQuotation.htm"],
	
        //报表管理 
		"entReport":["/report/userReport.htm"],
		"entAddReport":["/report/userAddReport.htm"],
		"orderNumReport":["/report/orderNumReport.htm"],
		"orderPriceReport":["/report/orderPriceReport.htm"],
		"offerQueryReport":["/report/offerQueryReport.htm"],
		
		//订单管理 
		/* "orderList":["/adminorder/orderList.htm","/adminorder/"], */
		"tools":["/instrumentsTools.htm","/getGoodsTools.htm"],
		
        
}

var menus_main = {
		"certificationAudit":["/certificationAudit.htm"],
		"ykyAuthList":["/ykyAuth.htm"],
		"approveList":["/vipCenter/approveList.htm","/approveDetail.htm?id="],
}
var menus_operation = {
		//商品管理
		"goodsManager":["/goods.htm"],
		"materailDetection":["/material.htm"],
		"basicMaterial":["/basicMaterial.htm"],
        "modelMaintain":["/helpMaintain.htm"],
		"orderManagerList":["/order.htm"],
		"enterpriseManager":["/verify.htm"],
		//会员管理 
		"vipMember":["/enterprise.htm","/customers.htm"],
		"enterpriseVerify":["/verify.htm"],
		/* 运维管理 */
		"couponsManager":["/coupons.htm"], 
}
$(function(){
	addPropertys(menus_admin,"${ctx}");//admin下面的
	var mainServerUrlPrefix = $("#mainServerUrlPrefix").attr("href");
	addPropertys(menus_main,mainServerUrlPrefix); ///main下面的
	
	var operationServerUrlPrefix = $("#operationServerUrlPrefix").attr("href");
	addPropertys(menus_operation,operationServerUrlPrefix); //operation下面的
	/* 左边栏展开和收缩 */
	//var arrSubNav = document.getElementsByClassName('sub_nav');
	//var arrTitle = document.getElementsByClassName('first_title');
	var arrSubNav = $('.sub_nav');
	var arrTitle = $('.first_title');
	 for (var i = 0; i < arrTitle.length; i++) {  
		 var idTitle = arrTitle[i].getAttribute('id');
         var idSubNav = idTitle % 100; //根据title的id获取子菜单div的id
      
         var state = document.getElementById(idSubNav).style.display;
         
         var isPiched = document.getElementById(idSubNav).getElementsByTagName('li');
         for(var j =0; j < isPiched.length;j++ ){
	         var piched = isPiched[j].getElementsByTagName('a')[0].style.color;
	         
		     if(piched == 'rgb(204, 0, 0)'){
		    	 $('#' + idSubNav).show();
		    	 $('#' + idTitle + ' i').removeClass('img_down');
		    	 $('#' + idTitle + ' i').addClass('img_up');		    	 
		    	 break;
		     }else{
		    	 $('#' + idSubNav).hide();
		    	 $('#' + idTitle + ' i').removeClass('img_up');
		    	 $('#' + idTitle + ' i').addClass('img_down');		   
		     }
         }
	 } 
	
	
	for (var i = 0; i < arrTitle.length; i++) {
	      arrTitle[i].onclick = function () {
	          var idTitle = this.getAttribute('id');
	          var idSubNav = idTitle % 100; //根据title的id获取子菜单div的id
	       
	     /*      for (var i = 0; i < arrSubNav.length; i++ ) {
	               arrSubNav[i].style.display = 'none';
	          }
	          document.getElementById(idSubNav).style.display = 'block';
	         */
	          var state = document.getElementById(idSubNav).style.display;
	      
	          if(state == 'block' || state == '' ){
	        	  $('#' + idSubNav).hide();
	        	  $('#' + idTitle + ' i').removeClass('img_up');
	        	  $('#' + idTitle + ' i').addClass('img_down');
	          }else{
	        	  $('#' + idSubNav).show();
	        	  $('#' + idTitle + ' i').removeClass('img_down');
	        	  $('#' + idTitle + ' i').addClass('img_up');
	          }
	          
	      }	
     };
});

function addPropertys(menus,domain){
	var url = window.location.href;
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
		$("#"+id).attr("href",domain+menus[id][0]);
	}
}
</script>