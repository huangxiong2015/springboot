<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<title>意见反馈列表</title>
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<style type="text/css">
 	.content-wrapper .content{
  	height: 100%;
  	min-height: 0px;
    }
  .layui-layer-content  td{
    padding-top:10px; 
  }
   .layui-layer-content  td  textarea{
   height:200px !important; 
  }
  
 
 </style>

</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="manufacturer-list">
		<section class="content-header">
		  <h1>
		          意见反馈
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active">意见反馈列表</li>
		  </ol>
		</section> 
   <section class="content">
    <div class="row">
		<section class="col-md-12">
			<div class="box box-solid ">
				<div class="box-body customCol">
	              <form class="form-horizontal" method="post" name="seachForm" id="seachForm" @submit.prevent="onSearch">
			   		 <div class="row">
                  		<div class="col-sm-12 col-md-11 col-lg-10 bor-rig-light">
		                   <div class="row">
					              <div class="col-sm-7 col-md-4 col-lg-6 margin10"> 
					                  <div class="form-group-sm">
					                      <label for="payData" class="col-sm-4 col-md-4 col-lg-3 control-label"> 创建时间 </label>
					                        <div id="createData" class="col-sm-7 col-md-8 col-lg-7 ">
					                          <div id="createDataRange" class="input-daterange input-group">
							                      <input type="text" name="startDate" id="startDate" class="input-sm form-control"> 
							                      <span class="input-group-addon">至</span> 
							                      <input type="text" name="endDate" id="endDate" class="input-sm form-control">
						                      </div>
					                      </div>
					                  </div>
					              </div>
					       </div>
				          </div>
				          <div class="col-lg-2">
		                    <button class="btn btn-sm btn-danger sendData margin10" id="search_all" >
		                      <i class="fa fa-search"></i>查询
		                    </button>
		                  </div>
	                  </div>
			       	</form>
				</div>
			 </div>
          </section>
      </div>
      <div class="row">
        <section class="col-sm-12">
          <div class="box ">
            <!-- Morris chart - Sales -->
           
            <div class="chart box-body " style="position: relative;">
              <!--表格组件-->
               <rowspan-table
                      :columns="gridColumns"
                      :pageflag="pageflag"
                      :query-params="queryParams"
                      :api="url"
                      :refresh="refresh"  
              >
              </rowspan-table>
 
            </div>

          </div>
        </section> 
      </div> 
   </section> 
</div>
<div class="hide" id="viewContent">
	<table align="center">
	       <tbody><tr>
	        <td>您有任何意见和建议，或使用中遇到的问题，请在本页面反馈 </td>
	       </tr>
		  <tr>
		   <td><b>反馈内容：</b><br><br>
		     <textarea type="text" name="" readonly="readonly" style="height:100px;width:380px;"> {desc} </textarea>
		   </td>
		  </tr>
		  <tr>
		   <td><b>联系方式</b>(选填)<br><br><input type="text" readonly="readonly" name="" value="{contact}"></td>
		  </tr>
	</tbody></table>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script  type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-modal.js"></script> 
<script  type="text/javascript" src="${ctx}/js/app/feedback.js"></script> 
 
</body>
</html>