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
<style>
.lh32{line-height:32px;}
.select_image{display: inline-block;padding: 6px 17px;cursor: pointer;border: solid 1px #ddd;}
.return_btn{padding:10px 20px;cursor: pointer;border: solid 1px #ddd;border-radius: 3px;font-size:14px;}
.return_btn:hover{color:#b1191a;border:solid 1px #b1191a;}
.c_tip{background:#fff;border:solid 1px #ddd;color:#666;text-align: center;}
.b_red{border:solid 1px #b1191a;}
.ui-state-disabled{opacity:1;background:#eee !important;}
.condition_inquery{background-color: #c11f2e;color: #fff;padding: 10px 20px;font-size: 14px;border-radius: 3px;}
.condition_inquery:hover, .condition_inquery:active, .condition_inquery:focus{color:#fff;}
.return_btn{padding: 10px 20px;cursor: pointer;border: solid 1px #ddd;border-radius: 3px;font-size: 14px;color:#919191;margin-left:30px;}
.return_btn:hover{color: #b1191a;border: solid 1px #b1191a;}
.dn{display:none;}
.l{float:left;} 
.img_wrap img{max-width:800px;} 	
</style>
<title>商品推荐</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<div class="content-wrapper" id="recommendEdit">
			<section class="content-header">
			  <h1>
			    	商品推荐
			    <small v-if="recommendationId">编辑</small>
			    <small v-else>添加</small>
			  </h1>
			  <ol class="breadcrumb">
			    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
			    <li class="active"><a href="${ctx}/recommend.htm">商品推荐</a></li>
			  </ol>
			</section>   
		    
	    <section class="content" style=" color:#666;">
	      <form class="form-horizontal" name="manu-from" id="manu-from" onsubmit="return false;">    
	          <div class="row">
	              <section class="col-lg-12">
	                <div class="box box-danger ">
	                    <!-- Morris chart - Sales -->
	                      <div class="box-header with-border">
	                        <h3 class="box-title">
		                        <span v-if="recommendationId">编辑</span>
		                        <span v-else>添加</span>
	                        	<span>-商品推荐</span>
                        	</h3>
	                      </div>
	                      <div class="box-body ">
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">区域：</label>
	                                      <div class="col-sm-4" >
	                                      	  <select class="form-control" id="dictionaryItemId" name="dictionaryItemId" disabled v-model="initData.content.dictionaryItemId">
												<option value="20021">库存精选</option>
												<option value="20022">库存特卖</option>
												<option value="20023">库存最新</option>
											</select>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">位置：</label>
	                                      <div class="col-sm-4" >
	                                      	  <select class="form-control" id="orderSeq" name="orderSeq" v-model="initData.orderSeq">
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>									
											</select>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">型号：</label>
	                                      <div class="col-sm-4" >
	                                          <span id="modelName" class="lh32" v-html="initData.content.modelName?initData.content.modelName:'--'">--</span>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">分类：</label>
	                                      <div class="col-sm-4" >
	                                          	<span id="category1Name" class="ml10 mr10" v-html="initData.content.category1Name?initData.content.category1Name:'--'">--</span>
												<span  class="category2_c" v-if="initData.content.category2Name">></span>
												<span id="category2Name" class="ml10 mr10" v-html="initData.content.category2Name">--</span>
												<span  class="category3_c" v-if="initData.content.category3Name">></span>
												<span id="category3Name" class="ml10" v-html="initData.content.category3Name">--</span>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">详情链接：</label>
	                                      <div class="col-sm-4" >
	                                          <input type="text" id="linkDetails" name="linkDetails" class="form-control ui-autocomplete-input" v-model="initData.content.linkDetails" v-on:blur="changePrdUrl">
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label"></label>
	                                      <div class="col-sm-4" >
	                                          	<span class="ova dib"><input type="radio" name="fileOrAuto" value=2 class="l" v-model="initData.content.fileOrAuto" /><label class="l mt2 ml5">自动联想</label></span>
												<span class="ova dib"><input type="radio" name="fileOrAuto" value=1 class="l"  style="margin-left:10px;" v-model="initData.content.fileOrAuto" /><label class="l mt2 ml5">上传图片</label></span>

	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">图片地址：</label>
	                                      <div class="col-sm-4" >
	                                      	<span class="lh32" v-show="initData.content.fileOrAuto==2" v-html="initData.content.imageUrl?initData.content.imageUrl:'--'"></span>
	                                      	<span id="selectImage" href="javascript:void(0);" class="select_image" v-show="initData.content.fileOrAuto==1">选择文件</span>
	                                      	<div class="img_wrap">
		                                      	<img id="imageUrl" v-bind:src="initData.content.modelName && initData.content.fileOrAuto==2 && !initData.content.imageUrl?'${ctx}/images/static/defaultImg.120.jpg':initData.content.imageUrl" v-on:click="showBigImage">
		                                      	<img id="bImageUrl" v-bind:src="initData.content.modelName && initData.content.fileOrAuto==2 && !initData.content.imageUrl?'':initData.content.bImageUrl" class="dn">
	                                      	</div>
	                                      </div>
	                                  </div>
	                              </div>	                              	                               
	                      </div>
	                      <div class="ovh btn mt20 ml30 pl30" style="margin-left: 300px;margin-bottom: 30px;">
				              <a id="save_btn" class="l mr30 btnStyle01 dib f16 tc active ml30 condition_inquery" href="javascript:;" v-on:click="saveData">保 存</a>
				              <a id="cancel_btn" class="l mr20 btnStyle01 dib f16 tc return_btn" href="${ctx}/recommend.htm">取 消</a>
				          </div>
	                </div>
	              </section>
	          </div>
	      </form>
	    </section>
	    	<!-- 阿里云OSS图片地址前缀 -->
			<input type="hidden" value="<spring:eval expression="@appProps.getProperty('item.ImgUrl')"/>photos/" id="imagePrefix">
			<input type="hidden" value="${recommendationId}" id="recommendationId">
	</div>
	
	</div>
	
	
	
	<!-- Main content end -->
	<script type="text/javascript">
		 var selectCatid = "242,244"; 
	</script>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ---->
	<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
	<script type="text/javascript" src="${ctx}/js/app/recommendEdit.js"></script>	
</body>
</html>