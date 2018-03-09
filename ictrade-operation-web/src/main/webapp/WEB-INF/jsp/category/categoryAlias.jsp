<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="zh-CN">
<head> 
<title>分类别名维护</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head> 
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="categoryAlias">
		<section class="content-header">
		  <h1>
		    分类别名维护
		  </h1>
		</section>   
	    
    <section class="content">
      	<form class="form-horizontal" name="manu-from" id="manu-from" onsubmit="return false;">    
          <div class="row">
              <section class="col-lg-12">
                <div class="box box-danger ">
                      <div class="box-body ">
                      		<div class="form-group">
                                  <div class="col-md-11">
                                      <label for="specurl" class="col-sm-3 control-label">上传所有分类的映射关系：</label>
                                      <div class="col-sm-6" > 
                                      	<div>
                                     	 <span class="file-btn" style="cursor: pointer">添加文件 
                                     	 	<input type="file" id="uploadBtn" style="z-index: 1;width: 80px;height: 35px;opacity: 0;position: absolute;top: 0; left: 0;" class="valid">
                                     	 </span>
                                     	 <a href="javascript:void(0);" @click="download">下载所有分类的映射关系</a>
                                     	 </div>
                                     	 <div style="padding-top: 10px;"><a :href="fileUrl" target="_blank">{{fileName}}</a></div>
                                      </div> 
                                  </div>
                              </div>  
                      </div>
                  </div>
                  <div class="box-footer text-center">
                          <button @click="uploaderCategory" class="btn btn-danger c-btn" >保存</button>
                  </div>
              </section>
           </div>
        </form>
        <form id="exportForm" action="" method="GET" target="_blank" style="display:none;">
			<input type="hidden" name="Authorization"/>
	    </form>
	    <div class="alias-layer" style="display: none;padding: 20px;">
	    	<p v-for="item in errData">{{item}}</p>
	    </div>
    </section>
    
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" /> 
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/app/categoryAlias.js"></script>
</body>
</html>