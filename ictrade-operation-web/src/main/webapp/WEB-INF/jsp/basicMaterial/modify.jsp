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
<section class="content" style="color:#666;">
<form class="form-horizontal" name="materials-modify" id="materials-modify" onsubmit="return false;">

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
							    				                  <!--    <i>支持不大于2M的jpg、jpeg、png格式图形文件 </i> -->
								<div class="logo-box">
				                     <input class="btn" type="button" value="上传文件" id="uploadback" >
				                     <img alt="" :src="mainUrl ? mainUrl : '${ctx}/images/static/defaultImg.120.jpg'" class="background" />
                                     <input type="text" v-model="mainUrl" id="mainUrl" name="mainUrl"  style="visibility: hidden;height:0;width: 0px;" >
				                </div>
				                 
							</div>
								

						</div>
					<div class="row mt30">
							<div class="col-xs-2 tr">
								<label ></label>
								<span></span>
							</div>
							<div class="col-xs-10 c5e">
				                 
				                   
				                 <div class="logo-box">
				                    <i class="icon-close-min i-delete" v-show="attachUrl1"  v-on:click="deleteImage('attachUrl1')"></i>
				                     <input class="btn small-box" type="button" value="上传文件" id="uploadback0"  v-show="mainUrl">
				                     <img alt="" :src="attachUrl1 ? attachUrl1 : '${ctx}/images/static/defaultImg.120.jpg'" class="background" />
                                     <input type="text" v-model="attachUrl1" id="attachUrl1" name="attachUrl1"  style="visibility: hidden;height:0;width: 0px;" >
				                </div>
				                
				                <div class="logo-box">
				                     <i class="icon-close-min i-delete" v-show="attachUrl2" v-on:click="deleteImage('attachUrl2')" ></i>
				                     <input class="btn small-box" type="button" value="上传文件" id="uploadback1" v-show="attachUrl1" >
				                     <img alt="" :src="attachUrl2 ? attachUrl2 : '${ctx}/images/static/defaultImg.120.jpg'" class="background" />
                                     <input type="text" v-model="attachUrl2" id="attachUrl2" name="attachUrl3"  style="visibility: hidden;height:0;width: 0px;" >
				                </div>
				                <div class="logo-box">
				                     <i class="icon-close-min i-delete"  v-show="attachUrl3" v-on:click="deleteImage('attachUrl3')"></i>
				                     <input class="btn small-box" type="button" value="上传文件" id="uploadback2" v-show="attachUrl2">
				                     <img alt="" :src="attachUrl3 ? attachUrl3 : '${ctx}/images/static/defaultImg.120.jpg'" class="background" />
                                     <input type="text" v-model="attachUrl3" id="attachUrl3" name="attachUrl3"  style="visibility: hidden;height:0;width: 0px;" >
				                </div>
				                <div class="logo-box">
				                     <i class="icon-close-min i-delete" v-show="attachUrl4" v-on:click="deleteImage('attachUrl4')"></i>
				                     <input class="btn small-box" type="button" value="上传文件" id="uploadback3"  v-show="attachUrl3">
				                     <img alt="" :src="attachUrl4 ? attachUrl4 : '${ctx}/images/static/defaultImg.120.jpg'" class="background" />
                                     <input type="text" v-model="attachUrl4" id="attachUrl4" name="attachUrl4"  style="visibility: hidden;height:0;width: 0px;" >
				                </div>
				                  
				                 
							</div>
								

						</div>
						<div class="row mt30   include"   >
				               <multi-file
						            :keyname="uploadFiles.keyname"
						            :file-id= "uploadFiles.key"
						            :file-name= "uploadFiles.name"
						            :button-id= "uploadFiles.config.buttonId"
						            :upload-type= "uploadFiles.config.uploadType"
						            :url= "uploadFiles.config.url"
						            :types= "uploadFiles.config.types"
						            :file-size= "uploadFiles.config.fileSize"
						            :is-image= "uploadFiles.isImage" 
						            :attach-files="uploadFiles.attachFiles"
						            :max-length="uploadFiles.max" 
						            ref="files"
                              ></multi-file>
                         

						</div>

				
						<div class="row mt30">
							<div class="col-xs-2 tr">
								<label>RoHS标准：</label>
							</div>
							<div class="col-xs-10 c5e">
								<span>
									<input id="isrohs" type="radio" name="isrohs"  value="true"  v-model="isrohs" required>
									<label for="isrohs">符合</label></span>
									<span>
									<input id="isnorohs" type="radio" name="isrohs" value="false" v-model="isrohs" >
									<label for="isnorohs">不符合</label>
									<input id="norohs" type="radio" name="isrohs" value="" v-model="isrohs" >
									<label for="norohs">未知</label>
								</span>

							</div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr ">
								<label>物料描述：</label>
							</div>
							 <div class="col-xs-10 c5e">
								<textarea   id="todateCode" name="description"  maxlength="800"   v-model="datas.description" >
                                </textarea>
                                <p><span class="r">/800</span><span class="r" id='number'>0</span></p >
							</div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr ">
								<label>物料名称：</label>
							</div>
							 <div class="col-xs-10 c5e">
								<input  id="materialName" name="materialName" maxlength="50"   v-model="extendInfo.materialName"  placeholder="50字以内">
							</div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr ">
								<label>促销词：</label>
							</div>
							 <div class="col-xs-10 c5e">
								<input   id="saleName" name="promotionWord"  maxlength="50"   v-model="extendInfo.promotionWord" placeholder="50字以内">
							</div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr ">
								<label>物料详情：</label>
							</div>
							
								  <iframe name="coniframe" style="width:100%;border: none;min-height:546px;margin-left:114px;" src="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" scrolling="no" frameborder="0"  allowTransparency="true">
								  </iframe>
						
							<input type="hidden" id="iframeUrl_Id" value="<spring:eval expression="@appProps.getProperty('ueditor.iframeUrl')"/>" />
						</div>
						<div class="row mt30  mb30">
							<div class="col-xs-2 tr ">
								<label>状态：</label>
							</div>
							<div class="col-xs-10 c5e">
								<span> 
								<input id="status" type="radio" name="status" value="0" v-model = "status">
									<label for="status">有效</label></span>
									<span>
									<input id="nostatus" type="radio" name="status" value="1"  v-model = "status">
									<label for="nostatus">失效</label>
								
								</span>
							</div>
						</div>
						<div class="row mt30  mb30">
							<div class="col-xs-2 tr ">
								<label>是否受控：</label>
							</div>
							<div class="col-xs-10 c5e">
						  	        
							<span class="mcontrol" >
							     <input id="nType" type="checkbox" value="F"  v-model="isChecked1">
								<label for="nType">受控</label>
							</span> 
							</div>
						</div>
					</div>
					     <div class="ovh mt20 ml30 pl30" style="margin-left: 300px;margin-bottom: 30px;">
				              <a id="save_btn" class="btn btn-danger condition_inquery" onclick="requireSaveData();"   >提交</a>
				              <a id="cancel_btn" class="btn return_btn" href="javascript:;">取 消</a>
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
<script type="text/javascript" src="${ctx}/js/app/materialsmodify.js"></script>

<script type="text/javascript">

</script>
</body>
</html>
