<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="${ctx}/favicon.ico"/>
<title>category管理</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<style>

#addCategory{
	margin: 30px;
}

#addCategory .form-label{
	font-size: 12px;
	font-weight: normal;
	width: 100px;
	padding: 6px 0;
	float:left;
	line-height: 30px;
}

#addCategory .form-control{
	height: auto;
	border: none;
	padding-left: 120px;
	min-height: 30px;
}
#addCategory .form-control em{
	color:red
}
#addCategory .form-control .icon-confirm{
	display: none !important;
}
#addCategory .form-control span{
	position:relative;
	margin-right: 30px;
	display:inline-block;
}
#addCategory .form-control input[type="text"]{
	width: 220px;
	height: 30px;
}
#addCategory .form-control input[type="file"]{
	width: 96px;
	height: 32px;
	display: inline-block;
	position: absolute;
	left: 0;
	top: 0;
	opacity: 0;	
}
</style>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
<div class="content-wrapper" id="manufacturer-list">
<section class="content-header">
	<h1>
	  category列表 <small>列表展示</small>
	</h1>
</section> 
<section class="content">
<form class="form-horizontal" name="seachForm" id="seachForm" onsubmit="return false">
  	<!-- search part -->
	<div class="row">
		<section class="col-md-12">
			<div class="box box-solid ">
				<div class="box-body customCol">
					<div class="row">
						<div class="col-sm-12 col-md-10 col-lg-10 bor-rig-light">
							<div class="row">
								<div class="col-sm-5 col-md-4 col-lg-4 margin10">
								   <div class="form-group-sm">
								        <label for="orderCode" class="col-sm-4  col-md-5 col-lg-4 control-label">分类类型ID </label>
								        <div class="col-sm-7 col-md-7 col-lg-8" >
								            <input type="text" class="input-sm form-control" id="searchCateTypeId" name="searchCateTypeId" placeholder="分类类型ID" >
								        </div>
								    </div>
								</div>
								<div class="col-sm-7 col-md-4 col-lg-3 margin10">
								     <div class="form-group-sm">
								         <label for="orderStatus" class="col-sm-4 col-md-4  col-lg-4 control-label">分类名称 </label>
								         <div class="col-sm-7  col-lg-7" >
								             <input type="text" class="input-sm form-control" id="searchCategoryName" name="searchCategoryName"  placeholder="分类名称" >
								         </div>
								     </div>
								</div> 
							</div>
						</div>
						<div class="col-lg-2">
					        <button class="btn btn-sm btn-danger sendData margin10 pull-right" id="search_all" @click="onSearch">
					          <i class="fa fa-search"></i>查询
					        </button>
					      </div>
					   </div> 
				</div>
			 </div>
		</section>
	</div>
</form>
  	<!-- search part -->
  	<div class="row">
  		<div class="col-sm-12">
  			<div class="box">
  				<div class="box-header">
  					<a @click="addCategory" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a>
   				</div>
   				<div class="box-body">
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
   		</div>
   	</div>
 </section>
 <section id="addCategory" style="display:none;">
	<form>
		<div class="form-group">
			<label class="form-label">分类ID：</label>
			<div class="form-control">
				<input type="text" v-model="categoryId" name="categoryId" id="categoryId"/>
			</div>
		</div>
		<div class="form-group">
			<label class="form-label">分类类型ID：</label>
			<div class="form-control">
				<input type="text" v-model="categoryTypeId" name="categoryTypeId" id="categoryTypeId"/>
			</div>
		</div>
		<div class="form-group">
			<label class="form-label">分类名称：</label>
			<div class="form-control">
				<input type="text" v-model="categoryName" name="categoryName" id="categoryName"/>
			</div>
		</div>
		<div class="form-group">
			<label class="form-label">上传大图：</label>
			<div class="form-control">
				<span class="btn btn-default">
						上传文件
					<input type="file" id="uploadBtnBig" name="categoryImageUrlBig"/>
				</span>
				<span v-show="categoryImageUrlBig !== ''">{{fileBigName}} <i class="label-success">上传成功</i></span>
			</div>
		</div>
		<div class="form-group">
			<label class="form-label">上传小片：</label>
			<div class="form-control">
				<span class="btn btn-default">
						上传文件
					<input type="file" id="uploadBtnSmall" name="categoryImageUrlSmall"/>
				</span>
				<span v-show="categoryImageUrlSmall !== ''">{{fileSmallName}} <i class="label-success">上传成功</i></span>
			</div>
		</div>
	</form>
</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/categoryList.js"></script>
</body>
</html>