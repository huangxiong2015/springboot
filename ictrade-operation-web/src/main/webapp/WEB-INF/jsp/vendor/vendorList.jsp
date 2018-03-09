<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">

<head>
	<!-- jQuery 1.11.3 -->
	<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
	<title>供应商管理列表</title>
	<link rel="stylesheet" href="${ctx}/js/credenceUpload/credenceUpload.css?20161229" />
  	<link rel="stylesheet" href="${ctx}/css/app/vendor.css" />
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	  
<div class="content-wrapper" id="vendorManageList">
	<section class="content-header">
	 	<h1>
		  供应商管理
		</h1>
	</section>
	<section class="content" >
		<div class="row">
			<div class="col-md-12">
	   			<div class="box box-solid ">
	   				<div class="box-body customCol">
	   					<form id="seachForm" class="form-horizontal" onsubmit="return false">
				   				<lemon-form  
								:form-data="formData"   
								@on-search="searchData"
								@on-add="on-add"
								></lemon-form>
	   					
	   					</form>
	   				</div>
	   			</div>
	   		</div>
		</div>
		<div class="row">
	        <section class="col-sm-12">
		          <div class="box ">
		          		<div class="box-header">
		   					<a href="${ctx}/vendor/create.htm" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>
		   				</div>
			            <div class="chart box-body " style="position: relative;">
				              <!--表格组件-->
				               <rowspan-table
			                      :columns="gridColumns"
			                      :pageflag="pageflag"
			                      :query-params="queryParams"
			                      :api="url"
			                      :refresh="refresh" 
				              >
				              </rowspan-table>
			            </div>
		          </div>
	        </section>
      </div>
	</section>
	
	<div class="lose-describe describe-layer" style="display: none;">
		<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate" id="salesDescribeLayer">
			<div class="form-group">
				<h2>确定该供应商已失效？</h2>
				<div>失效供应商需走审批流程。失效后，该供应商的商品无法报价和上传商品。</div>
				<div class="discribe-textarea">
					<textarea class="form-control vendor-input inline" v-model="loseDescribe" maxlength="100"></textarea>
					<span>{{loseDescribe.length}}/100</span>
				</div>
			</div>
		</form>
	</div>
	
	<!-- 提示弹窗 -->
	<input type="hidden" id="userName" value="${userName }">
	<input type="hidden" id="userId" value="${userId }">
	<div id="infoSuc" class="infoSuc tc" style="display:none">
		<p class="g3 f16"><i class="icon-right-c"></i>授权设置成功！</p>
	</div>
	<div id="subList" class="subList dn" style="display:none;">
		<label class="fl lh30">选择授权人：</label>
		<div class="item-con fl">
			<div class="dib pl5 f12 subItem" v-for="(item, index) in listData">
			    <span class="check-box-white" v-on:click="selectFn(listData[index].partyId,listData[index].partyAccount)">
			    	<input type="checkbox"  v-bind:data-id="listData[index].partyId" class="styled" name="subName" v-bind:value="listData[index].partyId" />
			    </span>
			    <i v-bind:title="item.partyAccount" class="ml10 g3">{{ item.partyAccount }}</i>
		    </div>
		</div>
	</div>
	<!-- 提示弹窗 -->
</div>


	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ---->
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-form.js"></script>
<script src="${ctx}/js/app/vendorList.js"></script>

</div>	    
</body>
</html>