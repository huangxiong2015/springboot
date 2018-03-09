<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="zh-CN">
<head> 
<title>分销商详情</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<style>
	.form-horizontal .control-label{padding-top:0;}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="distributor-detail">
	<section class="content-header">
	  <h1>
	    分销商
	    <small>详情</small>
	  </h1>
	  <ol class="breadcrumb">
	    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
	    <li class="active">分销商详情</li>
	  </ol>
	</section>   
	    
    <section class="content" >
		<div class="row">
          <section class="col-lg-12">
	            <div class="box box-danger ">
	                <!-- Morris chart - Sales -->
	                  <div class="box-header with-border">
	                    <h3 class="box-title">分销商信息</h3>
	                  </div>
	                  <div class="box-body form-horizontal"> 
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label class="col-sm-3 control-label">分销商LOGO：</label>
                                      <div class="col-sm-5"> 
                                          <img :alt="initData.title ? initData.title :''" :src="initData.attachUrl ? initData.attachUrl : '${ctx}/images/defaultImg01.jpg'"  width="160"/>
                                      </div> 
                                  </div>
                              </div>  
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label class="col-sm-3 control-label">分销商名称：</label>
                                      <div class="col-sm-5 control-text" v-text="initData.title?initData.title:'-'"></div>
                                  </div>
                              </div>  
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label class="col-sm-3 control-label">活动图标：</label>
                                      <div class="col-sm-5 control-text" v-text="activeIcon?activeIcon:'-'"></div>
                                  </div>
                              </div>
                              <div class="form-group"  v-if="content.labelContent != 'nothing'">
                                  <div class="col-md-11">
                                      <label class="col-sm-3 control-label">图标链接：</label>
                                      <div class="col-sm-5 control-text" v-text="content.activityUrl?content.activityUrl:'-'"></div>
                                  </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label class="col-sm-3 control-label">供应商简介：</label>
                                      <div class="col-sm-5 control-text" v-html="content.profile?content.profile:'-'" style="word-break: break-all;"></div>
                                  </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label class="col-sm-3 control-label">分销商简介：</label>
                                      <div class="col-sm-5 control-text" v-html="initData.abstracts?initData.abstracts:'-'" style="word-break: break-all;"></div>
                                  </div>
                              </div>  
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label class="col-sm-3 control-label">排序位置：</label>
                                      <div class="col-sm-5 control-text" v-text="initData.orderSeq?initData.orderSeq:'-'"> </div>
                                  </div>
                              </div>   
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label class="col-sm-3 control-label">优势代理线：</label>
                                      <div class="col-sm-5 control-text" v-text="agentName?agentName:'-'"></div>
                                  </div>
                              </div>
                             <div class="form-group ">
                                  <div class="col-md-11">
                                      <label class="col-sm-3 control-label">优势产品类别：</label>
                                      <div class="col-sm-5 control-text" v-text="cateName?cateName:'-'"></div>
                                  </div>
                             </div>
                             <div class="form-group ">
			                    <div class="col-md-11">
			                        <label class="col-sm-3 control-label">热销型号：</label>
			                        <div class="col-sm-5 hot-sale-list">
			                        	<rowspan-table
			                       		 	v-if="hotList.refresh"
			                        		:columns="hotList.columns"
			                        		:init-datas="hotList.datas"
			                        		:default-tip="hotList.defaultTip"
			                        	></rowspan-table>
			                        </div>
			                    </div>
			                </div>
			                <div class="form-group ">
			                    <div class="col-md-11">
			                        <label class="col-sm-3 control-label">最新型号：</label>
			                        <div class="col-sm-5 hot-sale-list">
			                        	<rowspan-table
			                        		v-if="newestList.refresh"
			                        		:columns="newestList.columns"
			                        		:init-datas="newestList.datas"
			                        		:default-tip="newestList.defaultTip"
			                        	></rowspan-table>
			                        </div>
			                       	
			                    </div>
			                </div>
                      </div>
      			</div> 
            </section>
            <div class="box-footer text-center"> 
                <button type="button" class="btn btn-concle c-btn" onclick="history.go(-1);">返回</button>
            </div> 
		</div>
		
    </section>
    
   </div>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />  
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/distributorDetail.js"></script> 
</body>
</html>