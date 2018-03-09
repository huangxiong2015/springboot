<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!-- <!DOCTYPE> -->
<!DOCTYPE html>
<html lang="zh-CN">
<html>
<head>
<title>编辑物料</title>
<style type="text/css">

</style>
<link rel="stylesheet" href="${ctx}/css/app/modify.css">
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<div class="content-wrapper" id="extension-edit">
			<section class="content-header">
			  <h1>
			    	编辑物料
			    <small class="edit_state"></small>
			  </h1>
			  <ol class="breadcrumb">
			    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
			  </ol>
			</section>
			<section class="content-header material-header">
		  		<a :class="{'header_tab':true,'active':tabValue === 'baseData'}" @click="tabValue = 'baseData'">编辑物料</a>
		  		<a :class="{'header_tab':true,'active':tabValue === 'tariff'}" @click="tabValue = 'tariff'">商检/关税</a>
		  	</section>
	<section class="content" style="color:#666;">
	
		<form class="form-horizontal" name="materials-view" id="materials-view" onsubmit="return false;" v-show="tabValue === 'baseData'">

	<!-- Main content -->
		 <div style="min-height: 406px;" >
			<section  class="content container-fluid" >
				<div class="row mt20">
					<div class="col-xs-6">
						<div class="row ">
							<div class="row ">
								<div class="col-xs-2 tr pl0">
									<label >物料型号：</label>
								</div>
								<div class="col-xs-10 c5e">
									<span  v-html="datas.manufacturerPartNumber ? datas.manufacturerPartNumber : '--' "></span>
								</div>
							</div>
							<div class="row mt30">
								<div class="col-xs-2 tr">
									<label>物料分类：</label>
								</div>
								<div class="col-xs-10 c5e">
									<span v-for="ele in datas.categories">
									   <i v-if="ele.cateLevel=='1'" class="l">{{ele.cateName}}></i>
									   <i v-if="ele.cateLevel=='2'" class="l">{{ele.cateName}}></i>
									   <i v-if="ele.cateLevel=='3'" class="l">{{ele.cateName}}</i>
									</span>
								</div>
							</div>
							<div class="row mt30">
								<div class="col-xs-2 tr">
									<label>原厂：</label>
								</div>
								<div class="col-xs-10 c5e">
									<span v-html="datas.manufacturer ? datas.manufacturer : '--' "></span>
								</div>
							</div>
							<div class="row mt30">
								<div class="col-xs-2 tr">
									<label >物料图片：</label>
									<span></span>
								</div>
								<div class="col-xs-10 c5e">
											<p>
											   <img  :src="datas.imageurl"  class="litterImg">
											</p>
										  
							    </div>
							</div>
							<div class="row mt30"  v-if="datas.extendInfo  !== undefined ">
								<div class="col-xs-2 tr">
									<label >细节图：</label>
									<span></span>
								</div>
								<div class="col-xs-10 c5e"  v-if="datas.extendInfo  !== undefined ">
								            <span  v-for = "item in datas.extendInfo.detailImages"  class = "mr6">
											   <img  :src="item.url"  class="litterImg">
											</span> 
							    </div>
							</div>
							<div class="row mt30">
								<div class="col-xs-2 tr">
									<label>SPEC文档地址：</label>
								</div>
								<div class="col-xs-10 c5e word-break-all">
									    <div  v-for="(ele,index) in datas.documents" v-if="ele.type == 'datasheets'" >
									       <p  v-for="(el,index) in ele.attaches" >
									         <a target="_blank"  v-html ='el.url' :href='el.url'  ></a>
									       </p>
									 	</div>
									 	<div  v-for="(ele,index) in datas.documents" v-if="ele.type == 'dataSheet'" >
									       <p>
									         <a target="_blank"  v-html ='ele.url' :href='ele.url'  ></a>
									       </p>
									 	</div>
								</div>
							</div>
							<div class="row mt30">
								<div class="col-xs-2 tr">
									<label>RoHS标准：</label>
								</div>
								<div class="col-xs-10 c5e">
								      <span  v-if="datas.rohs == true " >符合</span >
								       <span  v-if="datas.rohs == false " >不符合</span >
								       <span  v-if="datas.rohs == undefined " >未知</span >
									 
								</div>
							</div>
							<div class="row mt30">
								<div class="col-xs-2 tr ">
									<label>物料描述：</label>
								</div>
								 <div class="col-xs-10 c5e word-break-all">
								   <span class="" v-html="datas.description ?datas.description:'--'"></span>
								</div>
							</div>
								<div class="row mt30">
								<div class="col-xs-2 tr ">
									<label>物料名称：</label>
								</div>
								 <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  !== undefined ">
								   <span class="" v-html="datas.extendInfo.materialName ?datas.extendInfo.materialName:'--'"></span>
								 </div>
								 <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  == undefined ">
								   <span >--</span>
								 </div>
							</div>
							<div class="row mt30">
								<div class="col-xs-2 tr ">
									<label>促销词：</label>
								</div>
								 <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  !== undefined ">
								   <span class="" v-html="datas.extendInfo.promotionWord ?datas.extendInfo.promotionWord:'--'"></span>
								</div>
								  <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  == undefined ">
								   <span >--</span>
								 </div>
							</div>
						   <div class="row mt30">
								<div class="col-xs-2 tr ">
									<label>物料详情：</label>
								</div>
								 <div class="col-xs-10 c5e word-break-all"  v-if="datas.extendInfo  !== undefined ">
								   <span class="" v-html="datas.extendInfo.materialDetail ?datas.extendInfo.materialDetail:'--'"></span>
								</div>
								 <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  == undefined ">
								   <span >--</span>
								 </div>
							</div>
							<div class="row mt30  mb30">
								<div class="col-xs-2 tr ">
									<label>状态：</label>
								</div>
								<div class="col-xs-10 c5e">
									<span v-html="datas.status ? '失效':'有效'"></span>
								</div>
							</div>
							<div class="row mt30  mb30">
								<div class="col-xs-2 tr ">
									<label>是否受控：</label>
								</div>
								<div class="col-xs-10 c5e">
									<span v-html="name" ></span >
								</div>
							</div>

							<!-- <div class="row mt30">
								<div class="col-xs-2 tr">
									<label>操作日志：</label>
								</div>
								<div class="col-xs-10 c5e" v-if="datas.operateDate">
									<span >--</span>
								</div> 
								<div class="col-xs-10 c5e"  v-if="!datas.operateDate" >
										<p  v-for=  "(el,index) in operateDate">
										  <span >{{el.date}}</span>
										  <span class="ml10">{{el.actionDesc}}</span>
		                  		        </p> 
								</div>
							 </div> -->
							<!-- <div class="row mt30">
								<div class="col-xs-2 tr">
									<label></label>
								</div>
								<div class="col-xs-10 c5e">
									<span class="return_btn">返回</span>
								</div>
							</div> -->
						</div>
						     
					</div>
					<div class="col-xs-6">
						<div class="row">
							<div class="col-xs-2 pl0">
								<label>物料参数：</label>
							</div>
							<div class="col-xs-10 c5e">
								<table class="table">
									<tbody>
										 <tr v-for="ele in datas.parameters">
											<td class="name">{{ele.name}}</td>
											<td class="value">{{ele.value}}</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					 
				</div>
				
			</section>
		</div>
		<!-- Main content end -->
	    </form>
     
	     <form name="createTariff" autocomplete="off" id="createTariff" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'tariff'">
		   	
		   	<div class="row mt30  mb30">
				<div class="col-xs-1 tr ">
					<label>商检/关税：</label>
				</div>
				<div class="col-xs-10 c5e">     
					<span class="mcontrol" >
						<input id="pType" type="checkbox"  value="T"  v-model="isChecked2">
						<label for="pType">关税</label>
						<input id="fType" type="checkbox"  value="I"  v-model="isChecked3">
						<label for="fType">商检</label>
					</span> 
				</div>
			</div>
		   	
			<div class="bank-info-create center" style="margin-top:32px;margin-left:52px;"> 
				<button id="save_btn" type="button" class="btn btn-danger c-btn" @click="saveData()">保存</button>
				<button id="cancel_btn" type="button" class="btn btn-cancel c-btn" style="margin-left:15px;" @click="cancelData();">取消</button>
			</div>
	
		</form>
     
 	</section>
</div>

</div>

<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<!---footer  end   ---->
<script type="text/javascript" src="${ctx}/js/oss/common.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script src="${webres}/lib/serialize/2.5.0/jquery-serialize-object.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-multi-file.js"></script>
<script type="text/javascript" src="${ctx}/js/app/materialCommodity.js"></script>

<script type="text/javascript">

</script>
</body>
</html>
