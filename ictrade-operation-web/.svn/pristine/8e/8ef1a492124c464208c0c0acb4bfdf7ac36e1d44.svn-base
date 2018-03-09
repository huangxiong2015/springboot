<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="${ctx}/favicon.ico"/>
<title>数据爬取</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="manufacturer-list">
		<section class="content-header">
		  <h1>
		  
		  </h1>
		</section> 
   <section class="content">
    <div class="row">
		<section class="col-md-12">
			<div class="box box-solid ">
				<div class="box-body customCol">
			   		 <div class="row">
                  		<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
		                   <div class="row">
					              <div class="col-sm-5 col-md-4 col-lg-4 margin10">
					                  <div class="form-group-sm">
					                      <label for="orderCode" class="col-sm-4  col-md-5 col-lg-4" style="padding-left:30px;padding-right: 10px;">请输入URL</label>
					                      <div class="col-sm-7 col-md-7 col-lg-8" >
					                          <input type="text" class="input-sm form-control" id="skuId" name="skuId">
					                      </div>
					                  </div>
					              </div>
					          </div>
				          </div>
				          <div class="col-lg-1">
		                    <button class="btn btn-sm btn-danger sendData margin10" onclick="crawlerBySkuId()" >
		                      <i class=""></i>开始爬取
		                    </button>
		                  </div>
	                  </div> 
				</div>
			 </div>
          </section>
      </div>
   </section>
</div>

<script type="text/javascript">

function crawlerBySkuId(){
	var skuId = $("#skuId").val();
	var crawlerUrl = ykyUrl.product+"/v1/crawler/detail?param="+encodeURIComponent(skuId);
	$.aAjax({
		url: crawlerUrl,
		type:"GET",
	    success: function(data) {
	    	alert(data.message);
	    },
	    error:function(e){
	    	console.log("系统错误，调用接口失败，请联系系统管理员："+e.responseText);
	    }
	});
	
}

</script>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
</body>
</html>