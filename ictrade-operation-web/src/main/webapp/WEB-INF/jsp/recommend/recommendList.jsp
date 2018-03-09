<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<style>
.c_tip{background:#fff;color:#666;border:solid 1px #eee;}
.wid15{width:15%;}
</style>
<title>商品推荐</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="recommend-list">
		<section class="content-header">
		  <h1>
		    商品推荐
		    <small>列表展示</small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active">商品推荐</li>
		  </ol>
		</section> 
   <section class="content customCol">
    <%-- <div class="row">
		<section class="col-md-12">
			<div class="box box-solid ">
				<div class="box-body customCol">
	              <form class="form-horizontal" name="seachForm" id="seachForm" method="get" action="${ctx}/recommend.htm">
			   		 <div class="row">
                  		<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
		                   <div class="row">
					              <div class="col-sm-5 col-md-4 col-lg-3 margin10">
					                  <div class="form-group-sm">
					                      <label for="orderCode" class="col-sm-4  col-lg-4 col-lg-4 control-label">区域 </label>
					                      <div class="col-sm-8 col-lg-8" >
					                          <select id="categoryId" name="categoryId" disabled class="input-sm form-control">
					                          	<option value="20021">库存精选</option>
					                          	<option value="20022">库存特卖</option>
					                          	<option value="20023">库存最新</option>
					                          </select>
					                      </div>
					                  </div>
					              </div>
					              <div class="col-sm-7 col-md-4 col-lg-3 margin10">
					                  <div class="form-group-sm">
					                      <label for="orderStatus" class="col-sm-4  col-lg-4 control-label">状态 </label>
					                      <div class="col-sm-7  col-lg-8" >
					                          <select id="status" name="status" class="input-sm form-control" v-model="queryParams.status">
					                          	<option value="">全部</option>
					                          	<option value="DRAFT">草稿</option>
					                          	<option value="PUBLISHED">已推荐</option>
					                          	<option value="HOLD">停用</option>
					                          </select>
					                      </div>
					                  </div>
					              </div> 
					              <div class="col-sm-7 col-md-4 col-lg-3 margin10">
					                  <div class="form-group-sm">
					                      <label for="orderStatus" class="col-sm-4  col-lg-4 control-label">型号 </label>
					                      <div class="col-sm-7  col-lg-8" >
					                        <input type="text" class="form-control" id="content" name="content" placeholder="型号" v-model="queryParams.content" >
					                      </div>
					                  </div>
					              </div> 
					          </div>
				          </div>
				          <div class="col-lg-1">
		                    <button class="btn btn-sm btn-danger sendData margin10" id="search_all" >
		                      <i class="fa fa-search"></i>查询
		                    </button>
		                  </div>
	                  </div>
			       	</form>
				</div>
			 </div>
          </section>
      </div> --%>
      <div class="row">
        <section class="col-sm-12">
          <div class="box ">
            <!-- Morris chart - Sales -->
            <div class="box-header with-border">
              <!-- <a href="recommend/edit.htm" class="btn btn-sm btn-danger pull-right"><i class="fa fa-plus"></i> 新增</a> -->
            </div>
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
	  <modal
	  	 v-if="showModal"
	  	 :modal-style="modalStyle"
		 @close="showModal = false"
	     @click-cancel="showModal = false"
	     @click-ok="modalOk"
	   >
	  	 <h3 slot="header" v-text="modalHead"></h3>
	  	 <div class="form-group">
			<div class="col-md-12">
				<label class="col-sm-3 control-label">推荐位置：</label>
				<div class="col-sm-8">
					<select name="orderSeq" v-model="orderSeq" class="form-control">
	          			<option v-for="ele in seqList" :value="ele">{{ele}}</option>
	          		</select>			
				</div>
			</div>
		</div>
 	</modal>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/recommend.js"></script>
</body>
</html>