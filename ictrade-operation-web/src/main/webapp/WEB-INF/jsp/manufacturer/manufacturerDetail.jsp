<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="zh-CN">
<head> 
<title>制造商</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head> 
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="manufacturer-detail">
	<section class="content-header">
	  <h1>
	    制造商
	    <small>详情</small>
	  </h1>
	  <ol class="breadcrumb">
	    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
	    <li class="active">制造商详情</li>
	  </ol>
	</section>   
	    
    <section class="content" >
		<div class="row">
          <section class="col-lg-12">
	            <div class="box box-danger ">
	                <!-- Morris chart - Sales -->
	                  <div class="box-header with-border">
	                    <h3 class="box-title">制造商信息</h3>
	                  </div>
	                  <div class="box-body form-horizontal"> 
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label for="specurl" class="col-sm-3 control-label">制造商LOGO：</label>
                                      <div class="col-sm-5"> 
                                          <img alt="" :src="manuData.logo ? manuData.logo : '${ctx}/images/defaultImg01.jpg'"  width="100"/>
                                      </div> 
                                  </div>
                              </div>  
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label for="brandName" class="col-sm-3 control-label">制造商名称：<span class="text-red">*</span></label>
                                      <div class="col-sm-5 control-text" > 
                                        {{manuData.brandName}}
                                      </div>
                                  </div>
                              </div>
                              <div class="form-group act-control-box" >  
                                  <div class="col-md-11" >  
                                       	  <label for="ykyno" class="col-sm-3 control-label">制造商别名：</label> 
	                                      <div class="col-sm-5 control-text">
	                                        <table class="table control-text" v-if="manuData.vendorAlias && manuData.vendorAlias.length>0">
	                                       			<tr v-for="(item, $index) of manuData.vendorAlias" >
	                                       				<td>{{item.name}}</td>
	                                       				<td>{{item.name != '' && item.vendorId == '' ? '不限': item.vendorName}}</td>
	                                       			</tr>
	                                       		</table>
	                                      </div>
                                        
                                  </div> 
                              </div>    
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label for="desc" class="col-sm-3 control-label">制造商简介：</label>
                                      <div class="col-sm-5 control-text">{{manuData.desc}}
                                      </div>
                                  </div>
                              </div>
                      </div>
      			</div>
     			<div class="box box-danger ">
	                   <div class="box-header with-border">
	                    <h3 class="box-title">操作日志</h3>
	                  </div>
	                  <div class="box-body dl-yky">
	                  	<ul>
	                  		<li class="control-text" v-for="(item , i) in logData">
	                  			<span>{{item.date}}  {{item.actionDesc}}</span>
	                  		</li> 
	                  	</ul> 
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
<script type="text/javascript" src="${ctx}/js/app/manufacturer_detail.js"></script>
</body>
</html>