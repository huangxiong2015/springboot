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

	<link rel="stylesheet" type="text/css" href="${ctx }/css/common/component.css" />
	<link rel="stylesheet" type="text/css" href="${ctx }/css/app/certification.css" />
	<!-- jQuery 1.11.3 -->
	<c:import url ="/WEB-INF/jsp/include/main/constants.jsp"/>
	<title>认证企业管理</title>
	<style>
	.ui-area .country{margin:0 0 0 10px;}
	</style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

<jsp:directive.include file="../include/main/header.jsp" />
<jsp:directive.include file="../include/main/menu.jsp" />

		<!-- 右边内容显示 -->
<div class="content-wrapper ms-controller" ms-controller="list">
	<section class="content-header">
		<a href="${ctx}/enterprise.htm?action=certificationEnt" class="header_tab pitch_tab">认证企业管理</a>
	</section>
	<section class="content container-fluid">
		<div class="row mt20" ms-controller="search" id="searchFrom">
			<div class="col-xs-11"  style="border-right:1px solid #e2e2e2">
					<div class="col-xs-2 col_bt pl0 pr0 mb10" style="width: 21%;">
						<label>YKY客户编码：</label>
						<input class="col_input_1" type="text" name="partyCode" id="partyCode" data-duplex="@parameter.partyCode" ms-attr="{value:@parameter.partyCode}" >
					</div>
					<div class="col-xs-2 col_bt pl0 pr0 mb10" style="width: 19%;">
						<label>公司名称：</label>
						<input class="col_input_1" type="text" name="name" id="name" data-duplex="@parameter.name" ms-attr="{value:@parameter.name}">
					</div>
					<div class="col-xs-3 col_bt pl0 pr0 mb10" style="width: 24%;">
						<label>公司类型：</label>
						<select class="xl" id="corCategory" name="corCategory" data-duplex="@parameter.corCategory" ms-attr="{value:@parameter.corCategory}"></select>
					</div>
					<div class="col-xs-2 col_bt pl0 pr0 mb10">
						<label>认证状态：</label>
						<select style="width: 55%;" class="xl" id="activeStatus" name="activeStatus" ms-duplex="@parameter.activeStatus">
							<option value="">全部</option>
							<option value="PARTY_NOT_VERIFIED">未申请</option>
							<option value="WAIT_APPROVE">待审核</option>
							<option value="REJECTED">不通过</option>
							<option value="PARTY_VERIFIED">通过</option>
							<option value="INVALID">失效</option>
						</select>
					</div>
					<div class="col-xs-2 col_bt pl0 pr0 mb10" style="width: 18.66666667%;">
						<label>子账号功能：</label>
						<select class="xl"  id="accountStatus" name="accountStatus" ms-duplex="@parameter.accountStatus">
							<option value="">全部</option>
							<option value="ACCOUNT_WAIT_APPROVE">待审核</option>
							<option value="ACCOUNT_VERIFIED">通过</option>
							<option value="ACCOUNT_NOT_VERIFIED">未开通</option>
							<option value="ACCOUNT_REJECTED">不通过</option>
						</select>
					</div>
					<div class="col-xs-11 mb10">
						<label style="padding-left:31px">行业：</label>
						<div class="f-ib">
							<a class="select_industry"><em class="select_tip">请选择所属行业</em><i class="icon-down_arrow1 g9"></i></a>
							<div class="industry_content dn">
								<div class="industry_list  mCustomScrollbar _mCS_3 _mCS_1" style="max-height:80px;"></div>
								<div class="industry_btn mt20 tc"><a class="btn_y">确认</a><a class="btn_c">取消</a></div>
							</div>
							<div class="select-area"></div>	 		
							<input  id="industry" name="industry" type="hidden"  data-duplex="@parameter.industry">	
						</div>
					</div>										
					<div class="col-xs-6 col_bt pl0 pr0  mb10">
						<label class="l lh" style="padding-left:45px">区域：</label>
						<div id="ld" class="ui-area"></div>
					</div>

					<div class="col-xs-6 col_bt_time pl0 pr0 mb10">
						<label class="">审核时间：</label>
						<span class="rel">
							<input id="applyDateStart" name="applyDateStart" class="y_main_head_input ovh" style="padding-right:46px;" type="text" data-duplex="@parameter.applyDateStart" ms-attr="{value:@parameter.applyDateStart}">
			                <span class="y_timer"><i class="cc1 icon-calendar"></i></span>
						</span>
						<span>至</span>
						<span class="rel">
			                <input id="applyDateEnd" name="applyDateEnd" class="y_main_head_input ml10 ovh" style="padding-right:46px;" type="text" data-duplex="@parameter.applyDateEnd" ms-attr="{value:@parameter.applyDateEnd}">
			                  <span class="y_timer"><i class="cc2 icon-calendar"></i></span>
						</span>
						<span class="content_top_close" onclick="showDiv();"></span>
					</div>
					<div class="col-xs-6 col_bt_time mb10 col_bt pl0 pr0 ">
						<label style="padding-left:21px">营业期限：</label>
						<span class="rel">
							<input id="orgLimitStart" name="orgLimitStart" class="y_main_head_input ovh" style="padding-right:46px;" type="text" data-duplex="@parameter.orgLimitStart" ms-attr="{value:@parameter.orgLimitStart}">
			                <span class="y_timer"><i class="cc3 icon-calendar"></i></span>
						</span>
						<span>至</span>
						<span class="rel">
			                <input id="orgLimitEnd" name="orgLimitEnd" class="y_main_head_input ml10 ovh" style="padding-right:46px;" type="text" data-duplex="@parameter.orgLimitEnd" ms-attr="{value:@parameter.orgLimitEnd}">
			                  <span class="y_timer"><i class="cc4 icon-calendar"></i></span>
						</span>
						<span class="content_top_close" onclick="showDiv();"></span>
					</div>
			</div>

			<div class="col-xs-1 mt25">
				<a href="javascript:;" class="condition_inquery" ms-click="@goSearch()">
					<span ><i class="icon-sousuo mr5"></i>查询</span>
				</a>
			</div>

		</div>

		<!-- 表格 -->
		<div class="row mt20 ">
			<div class="col-xs-12" id="result_table">
				<table class="table mb10">
					<thead>
						<tr>
							<th width="5%">YKY客户编码</th>
							<th width="8%">公司名称</th>
							<th width="8%">公司类型</th>
							<th width="5%">行业</th>
							<th width="8%">区域</th>
							<th width="4%">认证状态</th>
							<th width="4%">子账号管理</th>
							<th width="6%">审核时间</th>
							<th width="6%">营业期限</th>
							<th width="8%">操作</th>
						</tr>
					</thead>
					<tbody >

						<tr :for="(index,ele) in @list.list" >
						
							<td ms-attr="{title:ele.partyCode?ele.partyCode :'--'}">{{ele.partyCode?ele.partyCode :'--'}}</td>
							<td ms-attr="{title:ele.name? ele.name :'--'}">{{ele.name? ele.name :'--'}}</td>
							<td ms-attr="{title:ele.companyName?ele.companyName :'--'}">{{ele.companyName?ele.companyName:'--'}}</td>
							<td ms-attr="{title:ele.industryCategoryName?ele.industryCategoryName:'--'}">{{ele.industryCategoryName?ele.industryCategoryName:'--'}}</td>
							<td ms-attr="{title:ele.address}">{{ele.address}}</td>
							<td >
							
								 <span :visible="ele.activeStatus=='PARTY_NOT_VERIFIED'">未申请</span>
								<span  :visible="ele.activeStatus=='WAIT_APPROVE'">待审核</span>
								<span  :visible="ele.activeStatus== 'REJECTED'">不通过</span>
								<span  :visible="ele.activeStatus=='PARTY_VERIFIED'">通过</span>
								<span  :visible="ele.activeStatus== 'INVALID'">失效</span> 
							</td> 
							
							<td>	
								<span :visible="ele.accountStatus=='ACCOUNT_WAIT_APPROVE'">待审核</span>
								<span :visible="ele.accountStatus=='ACCOUNT_VERIFIED'">通过</span>
								<span :visible="ele.accountStatus=='ACCOUNT_NOT_VERIFIED'">未开通</span>
								<span :visible="ele.accountStatus=='ACCOUNT_REJECTED'">不通过</span>
							</td>
							<!-- <td>11</td>
							<td>22</td> -->
							<td ms-attr="{title:ele.approvedDate?ele.approvedDate:'--'}">{{ele.approvedDate?ele.approvedDate:'--'}}</td>
							<td ms-attr="{title:ele.orgLimit?ele.orgLimit:'--'}">{{ele.orgLimit?ele.orgLimit:'--'}}</td>
							<td>
								<a class="poi" ms-attr="{href:'${ctx}/enterprise.htm?action=cerEntDetail&id='+ele.id}" target="_blank">详情</a>
								<a class="poi" ms-attr="{href:'${ctx}/enterprise.htm?action=cerEntEdit&id='+ele.id}" target="_blank">编辑</a>
								<a :visible="ele.activeStatus != 'INVALID'" class="poi" id="failure" ms-click="@failureClick(ele.id);">失效</a>
							</td>
						</tr>
						<tr :if="@list.list.length==0">
									<td colspan = "10">暂无数据</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
			<div class="item_page pl30">
			<div class="l showpages">
	            <span class="l mr5">每页显示数量</span>
 	            <div class="show_number_extrude l">
 	            	<span class="spn">{{@list.pageSize}}</span><i class="icon-down"></i><i class="icon-upward dn"></i>
 	                <div class="show_number_cont">
 	                    <ul>
	                        <li onclick="changeSize(10);"><a href="javascript:;">10</a></li>
 	                        <li onclick="changeSize(20);"><a href="javascript:;">20</a></li>
 	                        <li onclick="changeSize(30);"><a href="javascript:;">30</a></li>
 	                        <li onclick="changeSize(40);"><a href="javascript:;">40</a></li>
	                        <li onclick="changeSize(50);"><a href="javascript:;">50</a></li>
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
		

	</section>


</div>

<form id="exportForm" action="" method="POST" target="_blank" style="display:none;">
	<input type="hidden" name="Authorization" value="Basic <shiro:principal property="loginName" />"/>
</form>
		<script type="text/javascript">
		var selectCatid = "225,227";
	</script>
<jsp:directive.include file="../include/main/footer.jsp" />
</div>
<script type="text/javascript" src="${ctx }/js/lib/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="${ctx }/js/common/componentCer.js"></script>
<script src="${ctx}/js/enterprise/cerEnterpriseList.js"></script>

<script type="text/javascript">
		var defaultData={
				provinceId: search.parameter.province !=='' && search.parameter.province !== 'undefined'?search.parameter.province:0,
				cityId:search.parameter.city !== '' && search.parameter.city !== 'undefined'?search.parameter.city:0,
				countryId:search.parameter.country !== '' && search.parameter.country !== 'undefined' ? search.parameter.country:0,
				provinceText:'',
				cityText: '',
				countyText:'',
		} 
		selectArea.init('#ld',defaultData);			


</script>

</body>
</html>
