<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>物料核对</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" href="${ctx}/css/app/material.css">
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="help-edit">
	<section class="content-header">
	  <h1>物料核对</h1>
	  <ol class="breadcrumb">
	    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
	    <li class="active"><a  href="javascript:;">物料核对</a></li>
	  </ol>
	</section> 
    <section class="content" style=" color:#666;">
	    <div class="row">
	        <section class="col-lg-12">
	          <div class="box box-danger ">
	                <div class="content_main">
						<img class="content_main_img" src="${ctx}/images/materialImg.png">
						<div class="content_main_msg">
							<p class="f-tip">上传核对文件，自动检测非标准物料并导出</p>
							<p class="l-tip">按模板文件格式上传，将自动与标准物料库数据进行匹配</p>
						</div>
					</div>							  	  			   
	          </div>
	          <div class="content_btn">
          		<div class="form-group ">
          			<a id="file" class="btn btn-sm btn-danger"  href="javascript:;">立即上传</a>
					<a class="btn_down" href="${ctx}/template/Material Detection.xlsx">Excel模板下载</a>				
          		</div>
				<div class="form-group">仅支持Excel表格文件上传，上传后将页面呈现非标准物料</div>
			  </div>
	          <form id="exportForm" action="material/excel/down.htm" method="POST" target="_blank" style="display:none;">
						<input type="hidden" name="Authorization" value=""/>
						<input id="fileUrl" type="hidden" name="fileUrl" value=""/>
					</form>
	          </section>
	    </div>
	</section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />

<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${ctx}/js/oss/oss_uploader.js"></script>
<script>
var uploader = createUploader({
	buttonId: "file", 
	uploadType: "productUpload.materialDetectionUpload", 
	url:  ykyUrl.webres,
	types: "csv,xlsx,xls",
	fileSize: "10mb",
	isImage: false, 
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				$("input[name='Authorization']").val($('#pageToken').val());
				$("#fileUrl").val(_fileUrl);
				$("#exportForm").submit();
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploader.init();
</script>
</body>
</html>