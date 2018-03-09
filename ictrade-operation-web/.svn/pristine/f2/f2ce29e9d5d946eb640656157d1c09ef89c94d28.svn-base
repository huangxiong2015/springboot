<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- 尾脚 -->
<style>
    /*提示文字  */
	.ts_text {
	    font-size: 14px;
	    background: #23b8c2;
	    height: 26px;
	    line-height: 26px;
	    color: #fff;
	    padding: 0 10px;
	    position: absolute;
	    top: 40px;
	    right: 0;
	    cursor: pointer;
	    display:none;
	}
</style>
<div id="foot" class="mt15">
	<div id="foot1" class="wrapper auto">
		<div id="links">
			    <span style="margin-right:25px;">友情链接：</span>
				<a title="亚太资源网" href="http://www.apacsource.com" target="_blank">亚太资源网</a>
				<a title="电子设计应用" class="link3" href="http://www.eaw.com.cn" target="_blank">电子设计应用</a>
				<a title="电子元件交易网" class="link3" href="http://www.114ic.com" target="_blank">电子元件交易网</a>
				<a title="中华液晶网" class="link3" href="http://www.fpdisplay.com" target="_blank">中华液晶网</a>
				<a title="ic资料网" class="link3" href="http://www.114ic.cn" target="_blank">ic资料网</a>
				<a title="工控世界网" class="link3" href="http://www.gonkon.com" target="_blank">工控世界网</a>
				<a title="电源在线" class="link3" href="http://www.cps800.com" target="_blank">电源在线</a>
		<!-- 		<a title="中国电子工业网" class="link3" href="http://www.dianzinet.com" target="_blank">中国电子工业网</a> -->
				<a title="电子资源下载" class="link3" href="http://share.eepw.com.cn/" target="_blank">电子资源下载</a>
				<a title="UPS应用" class="link3" href="http://www.upsapp.com" target="_blank">UPS应用</a>
				<a title="连接世界" class="link3" href="http://www.connworld.com" target="_blank">连接世界</a>
			<div style="margin-left:88px;">
			<a title="中国智能化网" class="link3" href="http://www.zgznh.com" target="_blank">中国智能化网</a>
			<a title="仪众国际仪器仪表" class="link3" href="http://www.1718china.com" target="_blank">仪众国际仪器仪表</a>
			<a title="全球ic采购网" class="link3" href="http://www.qic.com.cn/" target="_blank">全球ic采购网</a>
			<a title="电子展览网" class="link3" href="http://www.dz-z.com" target="_blank">电子展览网</a>
			<!-- <a title="中国集成电路网" class="link3" href="http://www.ic918.com" target="_blank">中国集成电路网</a> -->
			<a title="中采网ic160" class="link3" href="http://www.ic160.com" target="_blank">中采网ic160</a>
		<!-- 	<a title="上海赛格电子市场" class="link3" href="http://www.shseg.com/" target="_blank">上海赛格电子市场</a> -->
			<a title="IC商务网" class="link3" href="http://www.buy-ic.com" target="_blank">IC商务网</a>
			<a title="icbuy亿芯网" class="link3" href="http://www.icbuy.com/" target="_blank">icbuy亿芯网</a>
			<a title="中电港" class="link3" href="http://www.cecport.com" target="_blank">中电港</a>
			<a title="元器件交易查询网" class="link3" href="http://www.b2bic.com/" target="_blank">元器件交易查询网</a>
			</div>	
	</div>
	<div id="foot2">
		<div id="contacts" class="auto">
			<div class="l mt30 ml10">
				<ul>
					<li><a>联系我们</a>
						<span class="ml25"   style="margin:0 20px;">|</span><a href="${ctx }/aboutus.htm">关于网站</a>
						<span class="spacer" style="margin:0 20px;">|</span><a>IC电子零件</a>
						<span class="spacer" style="margin:0 20px;">|</span><a>IC网址导航</a>
					</li>
					<li>客服热线：400-930-0083　(服务时间：周一至周五 9:00-18:00 )</li>
					<li>copyright@2000-2017 IC-trade.com Limited. All rights reserved</li>
				</ul>
			</div>
		</div>
	</div>
</div>
  <div class="user_box header">
		<div class="ts_text">这里是个提示</div>
  </div>
<!-- <script>try{var iframe=document.createElement('<iframe name="ifr"></iframe>')}catch(e){var iframe=document.createElement('iframe');iframe.name='ifr'}iframe.src=ykyUrl.portal+"/index/blank4login.htm";document.body.appendChild(iframe);$("[name=ifr]").hide()</script> -->
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
 
  ga('create', '<spring:eval expression="@appProps.getProperty('ga.id')" />', 'auto');
  ga('send', 'pageview');

	/*
	 * 显示一个提示文字
	 */
  	<%
  		//获取message。如果从session中获取，则获取后将session清空
  		String resultMessage = request.getParameter("resultMessage");
  		if(resultMessage == null){
  			resultMessage = (String)request.getAttribute("resultMessage");
  		}
  		if(resultMessage == null){
  			resultMessage = (String)request.getSession().getAttribute("resultMessage");
  			request.getSession().removeAttribute("resultMessage");
  		}
  	%>
  	var resultMessage = "<%if(resultMessage!=null){%><%=resultMessage%><%}%>";
  	$(function(){
  			
  		if(resultMessage != ""){
  			showMessage(resultMessage);
  		}
  	});
	$(function(){
		if(resultMessage != ""){
			//showMessage(resultMessage);
		}
	});
	function showMessage(message){
		$(".ts_text").text(message);
		$(".ts_text").toggle("slow","swing",true);
		setTimeout(function(){
			$(".ts_text").toggle("slow","swing",false);
		},3000);
	}
</script>