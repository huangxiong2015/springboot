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
  <title>精选商品</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <style type="text/css">
  	#area:disabled{
	border:1px solid #DDD;
	background-color:#F5F5F5;
	color:#ACA899;
	}
	#logo{
		border:none;
	}
  	.select_image{
	    display: inline-block;
	    padding: 6px 17px;
	    cursor: pointer;
	    border: solid 1px #ddd;
	    color:#919191;
	    margin-left: 12px;
  	}
  	.pl5{padding-left:5px;}
  	.condition_inquery{
	    background-color: #c11f2e;
	    color: #fff;
	    padding: 10px 20px;
	    font-size: 14px;
        border-radius: 3px;
  	}
  	.return_btn{
  		padding: 10px 20px;
    	cursor: pointer;
    	border: solid 1px #ddd;
    	border-radius: 3px;
    	font-size: 14px;
    	color:#919191;
    	margin-left:30px;
  	}
  	.return_btn:hover{
  		color: #b1191a;
    	border: solid 1px #b1191a;
  	}
  	.condition_inquery:hover{
  		color:#fff;
  	}
  </style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="featured-add">
		<section class="content-header">
		  <h1>
		    	精选商品
		    <small>添加</small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
		    <li class="active">精选商品</li>
		  </ol>
		</section>   
	    
    <section class="content" style=" color:#666;">
      <form class="form-horizontal" name="manu-from" id="manu-from" onsubmit="return false;">    
          <div class="row">
              <section class="col-lg-12">
                <div class="box box-danger ">
                    <!-- Morris chart - Sales -->
                      <div class="box-header with-border">
                        <h3 class="box-title"><span v-if=""></span><span>添加</span>精品商品</h3>
                      </div>
                      <div class="box-body ">
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label for="orderSeq" class="col-sm-2 control-label">位置：</label>
                                      <div class="col-sm-4" >
                                      	  <select class="form-control" name="orderSeq" id="orderSeq">
                                      	  	<option value="1">1</option>
                                      	  	<option value="2">2</option>
                                      	  	<option value="3">3</option>
                                      	  	<option value="4">4</option>
                                      	  	<option value="5">5</option>
                                      	  </select>
                                          <!-- <input type="text" class="form-control" name="position" id="position"  :value="manufacturerData.brandName" required> -->
                                      </div>
                                  </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label for="modelName" class="col-sm-2 control-label">型号<span class="text-red">*</span></label>
                                      <div class="col-sm-4" >
                                          <input type="text" class="form-control" name="modelName" id="modelName"  :value="" required>
                                      </div>
                                  </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label for="detailUrl" class="col-sm-2 control-label">详情链接<span class="text-red">*</span></label>
                                      <div class="col-sm-4" >
                                          <input type="text" class="form-control" name="detailUrl" id="detailUrl"  :value="" required>
                                      </div>
                                  </div>
                              </div>
                              <div class="l time_box  text-big form-group" >
				                   <label class="time_list f14 control-label col-sm-2 l dib mr10 pl5">图片地址 <span class="text-red">*</span></label>
 				                   <a id="selectImage" href="javascript:void(0);"  class="select_image">选择文件</a>
				                   <input style="display: none;width:50%;" class="input_info" id="imageAddress" value="" />
								    <div id="showImage">
				                   	 	<img id="s_img" style="width: 100px;margin-left:17%;margin-top:10px;margin-bottom:10px;" onclick="image_preview();" />
				                   </div>
				                   <input style="display: none;width:50%;" class="input_info" id="b_img" value="" />
				                   <div id="showBigImage" style="display: none;" >							
			                   	 		<img style="margin-left:60px;" />
								  </div>	
								
							  </div>
                              <%-- <div class="form-group ">
                                  <div class="col-md-11">
                                      <label for="specurl" class="col-sm-2 control-label">图片：</label>
                                      <div class="col-sm-4 logo-box">
                                          <input v-show="brandid==''" type="file" class="form-control" name="logo" id="logo" url="true">
                                          <img v-show="brandid!=''" alt="" :src="manufacturerData.logo ? manufacturerData.logo : '${ctx}/images/defaultImg01.jpg'" class="manu_logo" />
                                     			<!-- <a href="javascript:void(0);" class="del_logo" > 删除 </a>  -->支持jpg、jpeg、png、bmp，文件大小不超过5MB
                                      </div> 
                                  </div>
                              </div> --%>  
                      </div>
                      <div class="ovh btn mt20 ml30 pl30" style="margin-left: 300px;margin-bottom: 30px;">
			              <a id="save_btn" class="l mr30 btnStyle01 dib f16 tc active ml30 condition_inquery" href="javascript:;" onclick="save()">保 存</a>
			              <a id="cancel_btn" class="l mr20 btnStyle01 dib f16 tc return_btn" onclick="goBack()">取 消</a>
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
<jsp:directive.include file="../include/lemon/footer.jsp" /> 
<script type="text/javascript" src="${ctx}/js/oss/common.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/jquery-ui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/app/featuredProductEdit.js"></script>
</body>
</html>