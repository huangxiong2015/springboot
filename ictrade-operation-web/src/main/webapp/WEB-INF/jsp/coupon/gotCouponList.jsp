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
	<!-- jQuery 1.11.3 -->
  <c:import url ="/WEB-INF/jsp/include/main/constants.jsp"/>
  <title>领券详情页</title>

 <style type="text/css">
 	/* head_info信息部分 */
 	.stat_cont{
 		height:40px;
 		line-height:40px;
 		padding-left:25px;
 		background-color:#fffdee;
 		margin-bottom:20px;
 	}
 	.head_info_t{
 		margin-top:20px;
 		margin-bottom:20px;
 	}
 	.yky_r{
 		color:#b1191a;
 	}
 	.head_info .yky_l{
 		color:#337ab7;
 	}
 	.head_info .yky_l:link,.head_info .yky_l::hover,.head_info .yky_l:active,.head_info .yky_l:visited{
 		color:#337ab7;
 	}
 	.head_info .yky_l:hover{
 		text-decoration:underline!important;
 	}
 	/* checkout btn tool bar */
	.tool_bar {
	    margin: 0 auto;
	    height: 56px;
	    line-height: 56px;
	    border-bottom: 1px solid #eee;
	}
	.tool_bar span:first-child {
	    margin-left: 11px;
	    margin-right: 23px;
	}
	.ck_btn:link, .ck_btn:visited, .ck_btn:hover, .ck_btn:active {
	    display: inline-block;
	    width: 98px;
	    height: 30px;
	    margin-right: 5px;
	    line-height: 30px;
	    text-align: center;
	    cursor: pointer;
	}
	.ck_btn:link {
	    border: 1px solid #ddd;
	    color: #333;
	}
	
	/* 未访问的链接 */
	.ck_btn:visited {
	    border: 1px solid #ddd;
	    color: #333;
	}
	
	/* 已访问的链接 */
	.ck_btn:hover {
	    border: 1px solid #b1191a;
	    color: #b1191a;
	}
	
	/* 鼠标移动到链接上 */
	.ck_btn:active {
	    border: 1px solid #b1191a;
	    color: #b1191a;
	}
 	.condition_inquery{background-color:#c11f2e;color:#fff;padding:10px 10px;font-size:14px;border-radius:3px;}
	.col_bt_time input{width:37%;}
	.col_bt .col_input_1{width:40%;}
  	.col_bt label,.col_bt_time label{text-align:right;}
  	.col_input_2{width:12%;}
  	.col_input_3{width:15%;}
  	.mt25{margin-top:25px;}
  	.table img{width: 32px;height: 32px;}
  	.ui-selectmenu-button.ui-button {width: 14%;margin: 0px;margin-top: -3px;height: 30px;line-height: 30px;}
  	#status-button{width: 16%;}
  	.content-wrapper .content-header .pitch_tab{border-top: 0px;}
  	.item_page .showpages {height: 120px;margin-bottom: 30px;}
  	
  	.up_skin_class .y_prompt_content {width: 330px;margin-top: 25px;}
  	.operation {margin-bottom: 20px;}
  	.w340{width: 340px;height: 100px;padding: 5px 10px;resize: none;}
  	
  	.table td a{padding-right: 0px;}
  	.lh25{line-height:25px;}
  	.titleClass span{display: inline-block;width: 100%;}
  	
  	.ms-controller{visibility: hidden;}
  	.col_bt{height:32px;}
  	.bred{border-color:#b1191a;}
	/*浮窗*/
	.fly_skin_class{margin: auto;background-color: #fff;border: 3px solid #afafaf !important;}
	.fly_skin_class .layui-layer-content{font-size:16px;color: #333;text-align: left;line-height: 91px !important;}
	.fly_skin_class .icon-right-c{position:relative;top:2px;font-size: 22px; color: #87b848; margin: 32px 0 0 0;font-weight: bold;display: inline-block;padding-right: 25px;}
	/*浮窗*/
	/* 统计记录条数 */
	.record_st{
		margin-top:10px;
		margin-bottom: 15px;
		font-size:14px;
	}
	
	/* status search invoice type search */
	.status{
	    position: relative;
	}
	.status_list{
	    display: none;
	    position: absolute;
	    border: 1px solid #e4e4e4;
	    background-color: #fff;
	    top: 0;
	    z-index: 9;
	    text-align: center;
	}
	.status:hover .status_list{
	    display: block;
	}
	.status_list li{
	    display: block;
	    height: 28px;
	    line-height: 28px;
	   /*  width: 80px; */
	    cursor: pointer;
	}
	.status_list{
	    width: 100%;
	}
	.status_list li:hover{
	    background: #f8f8f8;
	}
	.status_list li:hover a{
	    color: #b1191a;
	}
	.icon-down_arrow1{
		margin-left:5px;
	}
 </style>
  <link rel="stylesheet" href="${ctx}/js/credenceUpload/credenceUpload.css?20161229" />
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
	  <div class="content-wrapper ms-controller"  ms-controller=list>
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<a href="#" class="header_tab pitch_tab">领券详情</a>
		</section>
	    <!-- Main content -->
		<section class="content container-fluid">
			<div class="head_info" >
				<div class="row head_info_t">
					<div class="col-xs-8">
						<span :if="@data.couponCate=='PLATFORM_PROMO'">【平台推广券】</span >
						<span :if="@data.couponCate=='EXACT_PROMO'">【定向推广券】</span >
						<span :if="@data.couponCate=='OFFLINE_PROMO'">【地推券】</span >
						<span ms-text="@data.name"></span>
					</div>
					<div class="col-xs-2" style="text-align:right;"><a ms-attr="{href:'${ctx}/coupon/detail.htm?id='+@data.couponId}" class="yky_l">查看优惠券详情</a></div>
				</div>
				<div>
					<div class="col-xs-10 stat_cont">
						<p>已派发优惠券<span class="yky_r" ms-text="@data.sendQty"></span>张
						<span >，还剩<em class="yky_r">{{@data.totalQty-@data.sendQty}}</em>张派发中</span>
						…
						</p>
					</div>
				</div>
			</div>
			<div class="row mt20" ms-controller="search" id=searchFrom>
				<div class="col-xs-10"  style="border-right:1px solid #e2e2e2">
					<div class="col-xs-12">
						<div class="col-xs-6 col_bt_time pr0 pl0 mb10">
							<label class="">领券时间：</label>
							<span class="rel">
								<input id="createDateStat" name="createDateStat" class="y_main_head_input ovh" 
									style="padding-right:46px;" type="text" ms-duplex="@parameter.createDateStat" 
									ms-attr="{value:@parameter.createDateStat}">
				                <span class="y_timer"><i class="cc1 icon-calendar"></i></span>
							</span>
							<span>至</span>
							<span class="rel">
				                <input id="createDateEnd" name="createDateEnd" class="y_main_head_input ml10 ovh" style="padding-right:46px;" type="text" ms-duplex="@parameter.createDateEnd" ms-attr="{value:@parameter.createDateEnd}">
				                  <span class="y_timer"><i class="cc2 icon-calendar"></i></span>
							</span>
							<span class="content_top_close" onclick="showDiv();"></span>								
						</div>
						<div class="col-xs-6 col_bt_time pr0">
							<label class="">使用时间：</label>
							<span class="rel">
								<input id="useDate" name="useDate" class="y_main_head_input ovh" style="padding-right:46px;" type="text" ms-duplex="@parameter.useDate" ms-attr="{value:@parameter.useDate}">
				                <span class="y_timer"><i class="cc3 icon-calendar"></i></span>
							</span>
							<span>至</span>
							<span class="rel">
				                <input id="useDateEnd" name="useDateEnd" class="y_main_head_input ml10 ovh" style="padding-right:46px;" type="text" ms-duplex="@parameter.useDateEnd" ms-attr="{value:@parameter.useDateEnd}">
				                  <span class="y_timer"><i class="cc4 icon-calendar"></i></span>
							</span>
							<span class="content_top_close" onclick="showDiv();"></span>								
						</div>
					</div>
					<div class="col-xs-12" :if="(@data.couponCate=='PLATFORM_PROMO')">
						<div class="col-xs-6 col_bt pr0 mb10 pl0">	
							<label>&ensp;&ensp;订单号：</label>						
							<input class="col_input_1" type="text" name="orderId" data-duplex="@parameter.orderId" ms-attr="{value:@parameter.orderId}" style="width:37%;">
						</div>
						<div class="col-xs-6 col_bt pr0 mb10 ">	
							<label class="" :if="(@data.couponCate=='PLATFORM_PROMO')">&ensp;&ensp;&ensp;&ensp;账号：</label>	
							<label class="" :if="(@data.couponCate!='PLATFORM_PROMO')" style="margin-left:9px;">&nbsp;&nbsp;&nbsp;&nbsp;账号：</label>						
							<input class="col_input_1" type="text" name="account" data-duplex="@parameter.account" ms-attr="{value:@parameter.account}" style="width:37%;">
						</div>	
					</div>
					<div class="col-xs-12" :if="(@data.couponCate=='EXACT_PROMO')||(@data.couponCate=='OFFLINE_PROMO')">
						<div class="col-xs-4 col_bt pr0 mb10 pl0">	
							<label>&ensp;&ensp;订单号：</label>						
							<input class="col_input_1" type="text" name="orderId" data-duplex="@parameter.orderId" ms-attr="{value:@parameter.orderId}" style="width:63%;">
						</div>
						<div class="col-xs-4 col_bt pr0  pl0 mb10" :if="(@data.couponCate=='EXACT_PROMO')||(@data.couponCate=='OFFLINE_PROMO')">	
							<label class=""  :if="@data.couponCate=='EXACT_PROMO'">&ensp;&ensp;&ensp;&ensp;操作员：</label>
							<label class=""  :if="@data.couponCate=='OFFLINE_PROMO'">地推人员：</label>							
							<input class="col_input_1" type="text" name="offlinePartyName" data-duplex="@parameter.offlinePartyName" ms-attr="{value:@parameter.offlinePartyName}" style="width:63%;">
						</div>
						<div class="col-xs-4 col_bt pr0 mb10 pl0">	
							<label class="" :if="(@data.couponCate=='PLATFORM_PROMO')">&ensp;&ensp;&ensp;&ensp;账号：</label>	
							<label class="" :if="(@data.couponCate!='PLATFORM_PROMO')" style="margin-left:9px;">&nbsp;&nbsp;&nbsp;&nbsp;账号：</label>						
							<input class="col_input_1" type="text" name="account" data-duplex="@parameter.account" ms-attr="{value:@parameter.account}" style="width:63%;">
						</div>
					</div>
				</div>
				<div class="col-xs-2 mt25">
					<a href="javascript:;" class="condition_inquery" ms-click="@goSearch()">
						<span ><i class="icon-sousuo mr5"></i>查询</span>
					</a>
				</div>			
				
			</div>
			<div class="row">
				<div class="col-xs-12">
					<p class="record_st">共<span class="yky_r" ms-text="@list.total"></span>条记录</p>
				</div>
			</div>
			<!-- 表格 -->
			<div class="row ">
				<div class="col-xs-12" id="result_table">
					<table class="table">
						<thead>
							<tr :if="@data.couponCate=='EXACT_PROMO'">
								<!-- <th width="5%"></th> -->
								<th width="8%">序号</th>
								<th width="12%">账号</th>
								<th width="10%">操作员</th>
								<th width="18%">领劵时间</th>
								<th width="8%" class="status">
									<span >使用状态<i class="icon-down_arrow1"></i>
			    				   </span>
					    			<ul class="status_list">
					    				<li><a href="javascript:void(0);"  onclick="return searchData('statusId','');">全部状态</a></li>
					    				<li><a href="javascript:void(0);"  onclick="return searchData('statusId','UNUSE');">未使用</a></li>
				    					<li><a href="javascript:void(0);"  onclick="return searchData('statusId','USED');">已使用</a></li>
				    					<li><a href="javascript:void(0);"  onclick="return searchData('statusId','EXPIRE');">已过期</a></li>
					    			</ul>
								</th>
								<th width="18%">使用时间</th>
								<th width="10%">订单号</th>
								<th width="12%">备注</th>
							</tr>
							<tr :if="@data.couponCate=='PLATFORM_PROMO'">
								<!-- <th width="5%"></th> -->
								<th width="8%">序号</th>
								<th width="13%" >账号</th>
								<th width="22%">领劵时间</th>
								<th width="15%" class="status">
									<span >使用状态<i class="icon-down_arrow1"></i>
			    				   </span>
					    			<ul class="status_list">
					    				<li><a href="javascript:void(0);"  onclick="return searchData('statusId','');">全部状态</a></li>
					    				<li><a href="javascript:void(0);"  onclick="return searchData('statusId','UNUSE');">未使用</a></li>
				    					<li><a href="javascript:void(0);"  onclick="return searchData('statusId','USED');">已使用</a></li>
				    					<li><a href="javascript:void(0);"  onclick="return searchData('statusId','EXPIRE');">已过期</a></li>
					    			</ul>
								</th>
								<th width="22%">使用时间</th>
								<th width="16%">订单号</th>
							</tr>
							<tr :if="@data.couponCate=='OFFLINE_PROMO'">
								<!-- <th width="5%"></th> -->
								<th width="8%">序号</th>
								<th width="12%" >账号</th>
								<th width="14%" >地推人员</th>
								<th width="18%">领劵时间</th>
								<th width="8%" class="status">
									<span >使用状态<i class="icon-down_arrow1"></i>
			    				   </span>
					    			<ul class="status_list">
					    				<li><a href="javascript:void(0);"  onclick="return searchData('statusId','');">全部状态</a></li>
					    				<li><a href="javascript:void(0);"  onclick="return searchData('statusId','UNUSE');">未使用</a></li>
				    					<li><a href="javascript:void(0);"  onclick="return searchData('statusId','USED');">已使用</a></li>
				    					<li><a href="javascript:void(0);"  onclick="return searchData('statusId','EXPIRE');">已过期</a></li>
					    			</ul>
								</th>
								<th width="18%">使用时间</th>
								<th width="12%">订单号</th>
							</tr>
						</thead>
						<tbody >
							<tr :if="@isfirst || !@list.list.length" >
								<td colspan="7" style="padding:20px 0;" id="loadingData" ms-html="@isLoading" :if="@data.couponCate=='OFFLINE_PROMO'">数据加载中</td>
								<td colspan="6" style="padding:20px 0;" id="loadingData" ms-html="@isLoading" :if="@data.couponCate=='PLATFORM_PROMO'">数据加载中</td>
								<td colspan="8" style="padding:20px 0;" id="loadingData" ms-html="@isLoading" :if="@data.couponCate=='EXACT_PROMO'">数据加载中</td>
							</tr>
							<tr ms-for="($index, co) in @list.list">
								<!-- <td><input type="checkbox" /></td> -->
								<td>{{$index+1}}</td>
								<td>
									<span>{{co.account}}</span>
								</td>
								<td :if="@data.couponCate=='EXACT_PROMO'">
									<span :if="co.offlinePartyName!=undefined">{{co.offlinePartyName}}</span>
								</td>
								<td :if="@data.couponCate=='OFFLINE_PROMO'">
									<span :if="co.offlinePartyName!=undefined">{{co.offlinePartyName}}</span>
								</td>
								<td>{{co.createdDate}}</td>
								<td>
									<span :if="co.statusId=='UNUSE'">未使用</span>
									<span :if="co.statusId=='USED'">已使用</span>
									<span :if="co.statusId=='EXPIRE'">已过期</span>
								</td>
								<td>
									<span :if="co.statusId=='USED'">{{co.useDate}}</span>
								</td>
								<td><span :if="co.statusId=='USED'"><a target="_blank" :attr="{href:co.orderLink?co.orderLink:''}">{{co.orderId}}</a></span></td>
								<td :if="@data.couponCate=='EXACT_PROMO'" :attr="{title:co.remark?co.remark:''}">{{co.remark}}</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="col-xs-12 tool_bar"  :if="@list.list.length">
					<!-- <span><input type="checkbox" /><label for="" class="ml10">全选</label></span> -->
					<a href="javascript:void(0);" class="ck_btn" :click="@exportRecord('thisPage')">导出</a>
					<a href="javascript:void(0);" class="ck_btn" :click="@exportRecord('all')">导出全部记录</a>
				</div>
			</div>
		</section>
		<!-- /.content -->
		
		<div class="item_page pl30">
			<div class="l showpages">
	            <span class="l mr5">每页显示数量</span>
	            <div class="show_number_extrude l">
	            	<span class="spn">{{showList.list.pageSize}}</span><i class="icon-down"></i><i class="icon-upward dn"></i>
	                <div class="show_number_cont">
	                    <ul>
	                        <li ms-if="showList.list.pageSize != '10'" onclick="changeSize(10);"><a href="javascript:;">10</a></li>
	                        <li ms-if="showList.list.pageSize != '20'" onclick="changeSize(20);"><a href="javascript:;">20</a></li>
	                        <li ms-if="showList.list.pageSize != '30'" onclick="changeSize(30);"><a href="javascript:;">30</a></li>
	                        <li ms-if="showList.list.pageSize != '40'" onclick="changeSize(40);"><a href="javascript:;">40</a></li>
	                        <li ms-if="showList.list.pageSize != '50'" onclick="changeSize(50);"><a href="javascript:;">50</a></li>
	                    </ul>
	                </div>
	        	</div>
			</div>
			<!-- 分页 -->
			<div id="pager" class="r">
				  <div id="kkpager"></div>
			</div>
			<input type="hidden" value="" id="total">
			<input type="hidden" value="" id="pageSize">
		</div>
	  </div>
	 
	   
	<script type="text/javascript">
		var selectCatid = "242,250";
	</script>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/main/footer.jsp" />
	<!---footer  end   ---->
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script src="${ctx}/js/credenceUpload/credenceUpload.js?a=20170109"></script>
<script src="${ctx}/js/credenceUpload/oss_credence.js?b=20170100600"></script>
<script src="${ctx}/js/lib/jquery.validate.js"></script>
<script src="${ctx}/js/common/add.validate.js"></script>

</div>	    
	<script type="text/javascript">
	function desLen(){
		var val = $("#description")[0].value;
		$('#des')[0].innerHTML= val.length+'/100';
	}
	
    function changeSize(count){
    	showList.list.pageSize = count;
    	showList.list.pageNum = 1;
    	search.parameter.pageSize = count;
    	search.parameter.page = 1;
    	$(".condition_inquery")[0].click();
    }
    
    /* search */
    
    var getParameters = function(){
    	var arr = ["orderId","createDateStat","createDateEnd","useDate","useDateEnd","account","offlinePartyName"],
		par ={};
		
    	for(var i=0; i< arr.length; i++){
    		par[arr[i]] = decodeURI(getQueryString(arr[i]));
    	}
    	return par;
    }
    /* 优惠券详情部分 */
    var getDetail = function(id){
    	var detailUrl = ykyUrl.pay+"/v1/coupons/detail/"+id;
    	$.aAjax({
		     url:detailUrl,
		     type:"GET",
		     success: function(data) {
		    	 showList.data = data;
		     },
		     error:function(e){
		  	    console.log("系统错误，审批失败，请联系系统管理员："+e);
			}
		})
    }
    /* 优惠券详情部分 */
	var search = avalon.define({
	    $id: "search",
	    parameter: getParameters(),
	    goSearch:function(){
	    	var wrap = $("#searchFrom");
	    	var inputList = wrap.find("input"),
	    		selectList = wrap.find("select"),
	    		parStr = "?",
	    		par = getQueryString("type"),
	    		parObj = {};
	    	
	    	if(par)
	    		parObj["type"] = par;
	    	
	    	function toUrl(pArr){
	    		for(var k in pArr){
	    			console.log(k)
	    			parStr += k + "=" + pArr[k] +"&";
	    		}
	    	}
	    	$.each(inputList,function(i,input){
	    		var val = input.value;
	    		if(val !== ""){
	    			parObj[input.name] = encodeURI(input.value);
	    		}
	    	})
	    	console.log(selectList)
	    	$.each(selectList,function(i,select){
	    		var val = "",
	    			selectObj = $(select,"option:selected");
	    		console.log(selectObj.val());
	    		if(selectObj.val()){
	    			parObj[select.name] = select.value;	        			
	    		}
	    	})
	    	
	    	toUrl(parObj);
	    	parStr = parStr.substring(0,parStr.length-1);
	    	
	     	var str = "?"
	    	if(parStr != ""){
	    		str = "&"
	    	}
			var id = getQueryString("id");
			var requredParam ="&id="+id;
	    	window.location.href=window.location.pathname+parStr+str+"page=1&size="+showList.list.pageSize+requredParam;//"+showList.list.pageNum+"
	    	
	    }
	})

	 /* 条件筛选 */
	var searchData = function(paramName,data){  //nType 1表示类型，2表示币种，3表示状态
		var mYRequest = GetUrlPars();
		var isUrlExist = false;
			mYRequest[paramName] = data; //do nothing
		var paramStr ='';
		var num = 0;
		for(key in mYRequest){
			if(mYRequest[key]!=''){
				if(num==0){
					paramStr+="?"+key+"="+mYRequest[key];
				}else{
					paramStr+="&"+key+"="+mYRequest[key];
				}
				num++;
			}
		}
		window.location.href= window.location.pathname+paramStr;
	}
	var  GetUrlPars=function(){
	    var url=location.search;
	    var theRequest = new Object();
	    if(url.indexOf("?")!=-1)
	    {
	       var str = url.substr(1);
	       strs = str.split("&");
	       for(var i=0;i<strs.length;i++)
	      {
	          var sTemp = strs[i].split("=");
	          theRequest[sTemp[0]]=(sTemp[1]);
	      }
	    }
	    return theRequest;
	 }
	var showList = avalon.define({
	    $id: "list",
	    list: [],
	    data:{},
	   	isLoading:"数据加载中...",
	   	isfirst:true,
	    allchecked: false,
	    handleOutTicket:function(e){
	    	var el = $(e.target),
			id = el.data("couponid");
	    	popHandleFrame(id);
	    },
	    changeStatus:function(e){
	    	var el = $(e.target),
			id = el.data("couponid");
	    	popSetCouponStatusFrame(id);
	    },
	   	audited:getQueryString("type"),
	   	exportRecord:function(exportType){
	   		var data = {};
	   		var dataObj = getParameters();
	   		$.each(dataObj,function(key,val){
	   			if(val){
	   				data[key] = val;
	   			}
	   		})
	   		data.couponId = getQueryString("id");
	   		if(exportType=="all"){
	   			data.export="ALL";
	   		}else if(exportType=="thisPage"){
	   			data.page = getQueryString("page")?getQueryString("page"):1;
		   		data.size = showList.list.pageSize;
	   		}	   		
	   		
	   		$.aAjax({
	   			url:ykyUrl.pay + '/v1/coupons/exportCouponList',
	   			type:'POST',
	   			contentType:'application/json',
	   			data:JSON.stringify(data),
	   			success:function(data){
	   				location.href=data;
	   			},
	   			error:function(data){
	   				console.log("error");
	   			}
	   		})
	   	}
	});

 	var queryPage = 0,querySize = 0;
 	if(!showList.list.pageNum){
 		queryPage = 1;
 	}else{
 		queryPage = showList.list.pageNum
 	}
 	if(!showList.list.pageSize){
 		querySize = 10;
 	}else{
 		querySize = showList.list.pageSize;
 	}
 	
 	var queryPage = getQueryString("page"),
 	querySize = getQueryString("size"),
	listPage = queryPage ? queryPage : 1,
	listSize = querySize ? querySize : 10;
 	var couponId = getQueryString("id");
 	var listUrl = ykyUrl.pay+"/v1/coupons/"+couponId+"/statsDetailsList";
 	/* var listUrl = 'http://localhost:27085'+"/v1/coupons"; */
 	 
	var tempData = "";
	if(window.location.search.indexOf("?") != -1){
		tempData = "?"+window.location.search.split("?")[1];
	}else{
		tempData = "?page="+listPage+"&size="+listSize;
	}
	//判断id是否存在
	if(!couponId){
		layer.msg('id参数错误', {icon: 2});
	}else{
		$.aAjax({
		     url: listUrl+tempData,
		     type:"GET",
		     success: function(data) {
		    	 for(var i = 0; i< data.list.length; i++){
		    		 data.list[i].checked = false;
		    	 }
		    	 if( data.list.length == 0)
			    	 	showList.isLoading = "暂无数据";
		    	 
		    	 $.each(data.list,function(index,ele){
		    		 if(ele.orderId){
		    			 ele.orderLink =  ykyUrl._this + '/order.htm?action=detail&id=' + ele.orderId;
		    		 }
		    		 
		    	 })
		    	 showList.list=data;
		    	 showList.isfirst = false;
		    	 
		    	 setPageOption(data);
		    	 loadPage(window.location.pathname+GetRequestWithout(window.location.search,"page"));
		    	 
		     },
		     error:function(e){
	  	    	layer.closeAll('loading');
		  	    console.log("系统错误，审批失败，请联系系统管理员："+e);
			}
		})
		getDetail(couponId);
	}
	
	//收款失败，输入框必填
  	function validateVal(){
  		if($("#description").val() == ""){
  			$("#description").addClass('bred');
  		}else{
  			$("#description").removeClass('bred');
  		}
  	}
	 
</script>


<script type="text/javascript">
$(function(){
		
		//日期控件
		var start = {
		elem: '#createDateStat',
		format: 'YYYY-MM-DD hh:mm:ss',
		// min: laydate.now(),    //设定最小日期为当前日期
		istime: true, //是否显示时间
		istoday: false,
		isclear: false, //是否显示清空
		//选择好日期的回调
		choose: function(dates) {
			$(this.elem).trigger("blur");
			var startTime = $('#createDateStat').val();
			var endTime = $('#createDateEnd').val();
			var arr1 = startTime.split('-');
			var sdate = new Date(arr1[0], parseInt(arr1[1] - 1), arr1[2]);
			var arr2 = endTime.split('-');
			var edate = new Date(arr2[0], parseInt(arr2[1] - 1), arr2[2]);
			if(sdate > edate) {
				layer.tips('开始时间不能大于结束时间', '#registerEnd', {
					tips: 1,
					time: 1000,
				});
				$('#registerStart').val('');
			};
		}
	};
	var end = {
		elem: '#createDateEnd',
		format: 'YYYY-MM-DD hh:mm:ss',
		// min: laydate.now(),    //设定最小日期为当前日期
		istime: true, //是否显示时间
		istoday: false,
		isclear: false, //是否显示清空
		//选择好日期的回调
		choose: function(dates) {
			$(this.elem).trigger("blur");
			var startTime = $('#createDateStat').val();
			var endTime = $('#createDateEnd').val();
			var arr1 = startTime.split('-');
			var sdate = new Date(arr1[0], parseInt(arr1[1] - 1), arr1[2]);
			var arr2 = endTime.split('-');
			var edate = new Date(arr2[0], parseInt(arr2[1] - 1), arr2[2]);
			if(sdate > edate) {
				layer.tips('开始时间不能大于结束时间', '#registerEnd', {
					tips: 1,
					time: 1000,
				});
				$('#createDateEnd').val('');
			};
		}
	};
	$("#createDateStat").on("click", function() {
		laydate(start);
	});
	$(".cc1").on("click", function() {
		laydate(start);
	});
	$("#createDateEnd").on("click", function() {
		laydate(end);
	});
	$(".cc2").on("click", function() {
		laydate(end);
	});
	
	var start2 = {
			elem: '#useDate',
			format: 'YYYY-MM-DD hh:mm:ss',
			// min: laydate.now(),    //设定最小日期为当前日期
			istime: true, //是否显示时间
			istoday: false,
			isclear: false, //是否显示清空
			//选择好日期的回调
			choose: function(dates) {
				$(this.elem).trigger("blur");
				var startTime = $('#useDate').val();
				var endTime = $('#useDateEnd').val();
				var arr1 = startTime.split('-');
				var sdate = new Date(arr1[0], parseInt(arr1[1] - 1), arr1[2]);
				var arr2 = endTime.split('-');
				var edate = new Date(arr2[0], parseInt(arr2[1] - 1), arr2[2]);
				if(sdate > edate) {
					layer.tips('开始时间不能大于结束时间', '#lastLoginEnd', {
						tips: 1,
						time: 1000,
					});
					$('#useDate').val('');
				};
			}
		};
		var end2 = {
			elem: '#useDateEnd',
			format: 'YYYY-MM-DD hh:mm:ss',
			// min: laydate.now(),    //设定最小日期为当前日期
			istime: true, //是否显示时间
			istoday: false,
			isclear: false, //是否显示清空
			//选择好日期的回调
			choose: function(dates) {
				$(this.elem).trigger("blur");
				var startTime = $('#useDate').val();
				var endTime = $('#useDateEnd').val();
				var arr1 = startTime.split('-');
				var sdate = new Date(arr1[0], parseInt(arr1[1] - 1), arr1[2]);
				var arr2 = endTime.split('-');
				var edate = new Date(arr2[0], parseInt(arr2[1] - 1), arr2[2]);
				if(sdate > edate) {
					layer.tips('开始时间不能大于结束时间', '#useDateEnd', {
						tips: 1,
						time: 1000,
					});
					$('#useDateEnd').val('');
				};
			}
		}; 
  		$("#useDate").on("click", function() {
			laydate(start2);
		});
		$(".cc3").on("click", function() {
			laydate(start2);
		});
		$("#useDateEnd").on("click", function() {
			laydate(end2);
		});
		$(".cc4").on("click", function() {
			laydate(end2);
		});
		
		$("#status").selectmenu()
		.selectmenu( "menuWidget" )
	    .addClass( "overflow" );
		
		
	});
  	
</script>
<script>
	
</script>
</body>
</html>
