<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>广告位-详情</title>
 <link rel="stylesheet" href="${ctx}/css/app/extensionEdit.css">
<style  type="text/css">
   .pd7{padding-top:7px;}

</style>
 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<div class="content-wrapper" id="advertise_edit">
			<section class="content-header">
			  <h1>广告位
			    <small class="edit_state"></small>
			  </h1>
			  <ol class="breadcrumb">
			    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
			    <li class="active">广告位详情</li>
			  </ol>
			</section>   
		    
	    <section class="content" style=" color:#666;">
	      <form class="form-horizontal" name="manu-from" id="manu-from" onsubmit="return false;" >    
	          <div class="row">
	              <section class="col-lg-12">
	                <div class="box box-danger ">
	                   
	                      <div class="box-header with-border">
	                        <h3 class="box-title"><span class="edit_state">详情</span>&nbsp;-&nbsp;广告位</h3>
	                      </div>
	                      <div class="box-body ">
	                               <div class="form-group ">
	                                   <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">广告标题：</label>
	                                     <div class="col-sm-4  pd7"  >
	                                         <span  v-text="info.content.title">--</span>
	                                      </div>
	                                  </div> 
	                             
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">广告类型：</label>
	                                      <div class="col-sm-4  pd7"  >
	                                         <span  v-text="info.content.categoryName">--</span>
	                                      </div>
	                                  </div>
	                              </div>
	                   			 <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">广告页面：</label>
	                                      <div class="col-sm-4  pd7" >
	                                      <span  v-text="info.content.pageName">--</span>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">广告位置：</label>
	                                       <div class="col-sm-4  pd7">
	                                       <span v-if="!info.content.positionName || info.content.positionName==''">--</span>
	                                       <span v-if="info.content.positionName!=''">{{info.content.positionName}}</span>
	                                      </div>
	                                  </div>
	                              </div>
	                              <div class="form-group">
	                                   <div class="col-md-11">
	                                     <label for="createData" class="col-sm-2 control-label">投放时间：</label> 
	                                       <div class="col-sm-4  pd7" >
	                                       <span v-if="info.startDate == ''">-</span>
	                                       <span v-if="info.startDate != ''">
	                                       		<span  v-text="info.startDate">--</span>&nbsp;&nbsp;至&nbsp;&nbsp;<span  v-text="info.expiryDate">--</span>
	                                       </span>
	                                      </div>
	                                  </div>
						         </div>
						         
						         <!-- 分销商 -->
						         <div class="form-group " v-if="info.distributor.length>0">
	                                  <div class="col-md-11">
	                                     <label class="col-sm-2 control-label">所属分销商：</label>
	                                      <div class="col-sm-4  pd7" >
	                                       <span  class="group-name" v-for="item in info.distributor">{{item.name}},</span>
	                                      </div>
	                                  </div>
	                              </div>
						         <!-- 分销商 -->
						         <!-- 制造商 -->
						         <div class="form-group " v-if="info.manufacturer.length>0">
	                                  <div class="col-md-11">
	                                     <label class="col-sm-2 control-label">所属制造商：</label>
	                                      <div class="col-sm-4  pd7" >
	                                       <span  class="group-name" v-for="item in info.manufacturer">{{item.name}},</span>
	                                      </div>
	                                  </div>
	                              </div>
						         <!-- 制造商 -->
	                             <div class="form-group ">
	                                  <div class="col-md-11">
	                                     <label class="col-sm-2 control-label">曝光占比：</label>
	                                      <div class="col-sm-4  pd7" >
	                                       <span v-if="!info.content.proportion || info.content.proportion==''">--</span>
	                                       <span v-if="info.content.proportion!=''">{{info.content.proportion}}</span>
	                                      </div>
	                                  </div>
	                              </div>
	                             
	                         	 <div class="form-group" >
				                    <div class="col-md-11">
				                        <label for="specurl" class="col-sm-2 control-label">广告效果图：</label>
				                        <div class="col-sm-7" > 
				                       	  <div class="logo-box"  > 
				                            <img alt="" :src="info.content.image ? info.content.image : '${ctx}/images/static/defaultImg.120.jpg'" class="background" />
				                       	  </div>
				                        	
				                        </div> 
				                    </div>
				                    
               					 </div> 
               					 <div class="form-group " v-if="!(info.extend1=='7001'&&info.extend2=='8002')"> <!-- 首页侧边栏不要背景色 -->
	                               <div class="col-md-11">
	                                   <label class="col-sm-2 control-label">广告背景色：</label>
	                                    <div class="col-sm-4  pd7" >
	                                       <span v-if="!info.content.background || info.content.background==''">--</span>
	                                       <span v-if="info.content.background&&info.content.background!=''">{{info.content.background}}</span>
	                                   </div>
	                              </div>
	                            </div>
               					<div class="form-group ">
	                               <div class="col-md-11">
	                                   <label class="col-sm-2 control-label">广告链接：</label>
	                                    <div class="col-sm-4 pd7" >
	                                       <span v-if="!info.content.url || info.content.url==''">--</span>
	                                       <span v-if="info.content.url!=''">{{info.content.url}}</span>
	                                   </div>
	                              </div>
	                            </div>
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
<script type="text/javascript" src="${ctx}/js/oss/common.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script> 
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>  
<script type="text/javascript" src="${ctx}/js/lib/pinyin.js"></script>  
<script type="text/javascript" src="${ctx}/js/app/advertisementdetail.js"></script>  
</body>
</html>