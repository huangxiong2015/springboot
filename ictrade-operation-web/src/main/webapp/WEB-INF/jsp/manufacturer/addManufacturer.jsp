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
  <div class="content-wrapper" id="manufacturer-add">
		<section class="content-header">
		  <h1>
		    制造商
		    <small v-if="brandid!=''">编辑</small>
		    <small v-else>添加</small> 
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
		    <li class="active">制造商</li>
		  </ol>
		</section>   
	    
    <section class="content" style=" color:#666;">
      <form class="form-horizontal" name="manu-from" id="manu-from" onsubmit="return false;">    
          <div class="row">
              <section class="col-lg-12">
                <div class="box box-danger ">
                    <!-- Morris chart - Sales -->
                      <div class="box-header with-border">
                        <h3 class="box-title"><span v-if="brandid!=''">编辑</span><span v-else>添加</span>制造商</h3>
                      </div>
    
                      <div class="box-body ">
    
                              <div class="form-group">
                                  <div class="col-md-11">
                                      <label for="specurl" class="col-sm-3 control-label">制造商LOGO：</label>
                                      <div class="col-sm-6" > 
                                     	 <div class="logo-box manu_logo"> 
                                      	    <input class="file-btn pos-abs" type="button" value="上传文件" id="uploadBtn"  >  
                                            <span>
	                                            <img alt="" :src="manufacturerData.logo ? manufacturerData.logo : '${ctx}/images/defaultImg01.jpg'" class="d-logo"/>
	                                     		
	                                     		<a v-show="manufacturerData.logo && manufacturerData.logo!=''" href="javascript:void(0);" class="del_logo" > 删除 </a> 
                                     		</span>
                                     	 </div>
                                     	 <div class="manu_text">支持jpg、jpeg、png、gif，文件大小不超过5MB</div>
                                      	 <input type="hidden" v-model="manufacturerData.logo" id="logo" name="logo"> 
                                      </div> 
                                  </div>
                              </div>  
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label for="brandName" class="col-sm-3 control-label">制造商名称：<span class="text-red">*</span></label>
                                      <div class="col-sm-5" > 
                                          <input type="text" class="form-control" name="brandName" id="brandName"  v-model.trim="manufacturerData.brandName" required >
                                      </div>
                                  </div>
                              </div>
                              <div class="act-control-box" > 
	                              <div class="form-group"  >  
	                                 <div class="col-md-11" > 
                                       	  <label for="ykyno" class="col-sm-3 control-label">制造商别名：</label>
	                                       <div class="col-sm-8">
	                                       		<table class="table" v-if="manufacturerData.vendorAlias && manufacturerData.vendorAlias.length>0">
	                                       			<tr v-for="(item, $index) of manufacturerData.vendorAlias" >
	                                       				<td>{{item.name}}</td>
	                                       				<td>{{item.name != '' && item.vendorId == '' ? '不限': item.vendorName}}</td>
	                                       				<td width="60">
	                                       					<a href="javascript:void(0)" @click="delField($index)">删除</a>
	                                       					<a href="javascript:void(0)" @click="editField($index)">编辑</a>
	                                       				</td>
	                                       			</tr>
	                                       		</table>
	                                       </div>  
	                                  </div> 
	                              </div>      
	                              <!-- <div v-if="manufacturerData.brandAlias && manufacturerData.brandAlias.length>0"   v-for="(item, $index) of manufacturerData.brandAlias"   >
		                              <div class="form-group" v-if="item != manufacturerData.brandName && item != manufacturerData.brandName.toUpperCase()"> 
		                                  <div class="col-md-11" >
		                                      <div  class="col-sm-5 col-sm-offset-3">
	                                          	<input type="text" class="form-control" name="brandAlias[]" :id="'brandAlias'+ $index" v-model.trim="manufacturerData.brandAlias[$index]" onblur="this.value=this.value.toUpperCase()">  
		                                       </div> 
		                                       <div  class="col-sm-1 control-text"> <a href="javascript:void(0);" class="delControl" onclick="delField(this)"> 删除 </a></div>
				                           </div>
			                           </div>
		                           </div> -->
                              </div>
                              <div class="form-group" v-if="len < 2000"> 
                                   <div class="col-md-11">
                                  	  <div class="col-sm-5 col-sm-offset-3">
                                   		<a class="pull-right" href="javascript:void(0);" id="addControl" @click="addField"><i class="fa fa-plus"></i> 新增</a>
                                   	  </div>
                                   </div>
                              </div>
                              <div class="form-group ">
                                  <div class="col-md-11">
                                      <label for="desc" class="col-sm-3 control-label">制造商简介：</label>
                                      <div class="col-sm-5">
                                          <textarea class="form-control" name="desc" id="desc" rows=5 maxlength="1000"  v-model.trim="manufacturerData.desc"></textarea>
                                          <div class="pull-right"><span class="in-num">{{inNum}}</span>/1000</div>
                                      </div>
                                  </div>
                              </div>
                      </div>
    
                      <div class="box-footer text-center">
                          <button type="submit" class="btn btn-danger c-btn" >保存</button>
                          <button type="button" class="btn btn-concle c-btn" >取消</button>
                      </div>
                </div>
              </section>
          </div>
      </form>
      <span v-show="false">{{len}}</span>
    </section>
    <div class="alias-layer" style="display: none;padding: 20px;">
    	<form onsubmit="return false;" class="form-horizontal" novalidate="novalidate">
	    	<div class="form-group clearfix">
	    		<label class="form-label col-sm-3">别名：</label>
	    		<div class="col-sm-6">
	    			<input class="form-control" type="text" name="aliasName" v-model="aliasName"/>
	    		</div>
	    	</div>
	   		<letter-select
		            :keyname="keyname"
		            :id="id"
		            :options="options"
		            :name="name"
		            :option-id="optionId"
		            :option-name="optionName"
		            @get-selecteds="getBrandSelected"
		            :multiple="multiple"
		            :placeholder="placeholder"
		            :selected="selected"
		            ref="letter"    
		    ></letter-select>
    	</form>
    </div>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" /> 
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script>
<script type="text/javascript" src="${ctx}/js/app/manufacturer_add.js"></script>
</body>
</html>