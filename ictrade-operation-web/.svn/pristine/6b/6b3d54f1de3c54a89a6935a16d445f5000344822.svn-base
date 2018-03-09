<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>活动管理-商品促销</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" href="${ctx}/css/app/activity.css"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
<div class="content-wrapper" id="activity-upload">
	<section class="content-header">
	  <h1>
	   活动管理
	  </h1>
	</section> 
	<section class="content">
		 <div class="row">
		  	<div class="col-md-12">
		  		<div class="box">
		  			<div class="box-body customCol">
		  				<h2 class="page-title">添加商品</h2>
		  				<ul class="time-tab">
		  					<li v-for="time in aperList" :class="{active : time.periodsId == periodsId}"><a href="javascript:void(0)" :id="time.periodsId" @click="changePeriods(time.periodsId)">{{time.startTime}}~{{time.endTime}} </a></li>
		  				</ul>
		  			</div>
		  		</div>
		  	</div>
		</div>
		<div class="row">
			<input type="hidden" value="${activityId }" id="activityId"/>
			<section class="col-sm-12">
				<div class="box">
					<div class="box-header with-border">
						<a class="btn btn-sm btn-danger pull-right add-product" @click="addProduct()">
							<i class="fa fa-plus"></i> 新增商品
						</a>
					</div>
					<div class="chart box-body" style="position: relative;" v-if="getTableData">
					   <rowspan-table
					          :columns="gridColumns"
					          :pageflag="pageflag"
					          :query-params="queryParams"
					          :checkflag = "checkflag"
					          :api="url"
					          :refresh="refresh"  
					  >
					  </rowspan-table>
					  <div class="activity-downlad">
					  	<button id="multipleDelete" class="btn btn-success sendData margin10" @click="multipleDelete"> 批量删除</button>
					  	<button id="export" class="btn btn-success sendData margin10" @click="downloadSelect"><i class="fa fa-download mr5"></i> 导出</button>
					  	<button id="export" class="btn btn-success sendData margin10" @click="downloadError"><i class="fa fa-download mr5"></i> 导出异常商品</button>
				  		</div>
					</div>
					<div class="box-footer text-center activity-info">
						<button type="button" class="btn btn-concle c-btn" @click="backPrev">上一步</button>
						<button type="button" class="btn btn-danger c-btn" @click="toNext">下一步：维护商品</button> 
					</div>	
				</div>
			</section> 
		</div>
	</section>
	
	<section id="file-upload" style="display:none;">
		<form>
			<div class="form-group">
				<label class="form-label">添加商品：</label>
				<div class="form-control">
					<span class="btn btn-primary">
						上传文件
						<input type="file" id="uploadBtn"/>
					</span>
					<span v-show="fileName !== ''">{{fileName}} <i class="label-success">上传成功</i></span>
					 <a href="${ctx}/resources/download/秒杀模板.xlsx">Excel模板下载</a>	
					<div class="tip">*支持xls、xlsx、csv的Excel文件、大小不超过5M，上传后将页面呈现上传商品</div>
				</div>
			</div>
		</form>
	</section>
	<form id="exportForm" action="" method="POST" target="_blank" style="display: none;">
		<input type="hidden" name="Authorization">
		<input name="ids" type="hidden"/>
		<input name="status" type="hidden"/>
		<input name="activityId" type="hidden"/>
		<input name="periodsId" type="hidden"/>
	</form>
</div>
<script type="text/javascript">
var selectCatid = "400,402";
</script>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/activityUpload.js"></script>
</body>
</html>