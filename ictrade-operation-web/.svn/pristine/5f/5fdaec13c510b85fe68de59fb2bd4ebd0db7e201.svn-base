<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>redis缓存管理详情页</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/> 
<style>
.table .table-tr .behide span{width:280px;display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;}
.cache-area{display:block;margin:40px auto 0 auto;}
</style>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="manufacturer-list">
		<section class="content-header">
		  <h1>
		  	 redis缓存管理详情页
		    <small>详情展示</small>
		  </h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		    <li class="active">redis缓存详情页</li>
		  </ol>
		</section> 
   <section class="content">
	              <form class="form-horizontal" method="get" name="seachForm" id="seachForm" @submit.prevent="onSearch">
    <div class="row">
		<section class="col-md-12">
			<div class="box box-solid ">
				<div class="box-body customCol">
			   		 <div class="row">
                  		<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
		                   <div class="row">
					              <div class="col-sm-5 col-md-4 col-lg-4 margin10">
					                  <div class="form-group-sm">
					                      <label for="title" class="col-sm-4  col-md-5 col-lg-4 control-label">key名称 </label>
					                      <div class="col-sm-7 col-md-7 col-lg-8" >
					                          <input type="text" class="input-sm form-control" id="key" name="key" v-model="listparam.key"  placeholder="请输入查找的key" >
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
				</div>
			 </div>
          </section>
      </div>
      <div class="row">
        <section class="col-sm-12">
          <div class="box ">
            <!-- Morris chart - Sales -->
             <div class="box-header with-border">
              	<div class="form-group-sm">
              		<div class="col-sm-2  col-lg-2"  >
                        <span  v-if="listparam.database=='0'" style="padding-top:6px;display:inline-block;">
						  数据库DB:0
						</span>
						<span  v-else-if="listparam.database=='1'" style="padding-top:6px;display:inline-block;">
						   数据库DB:1
						</span>
                   </div>
                   <div class="col-sm-3  col-lg-3"  style="padding-top:6px;display:inline-block;">
                        	<span>缓冲池房间名：</span><span v-text="roomname"></span>
                   </div>
                   <label for="author" class="col-sm-2 col-md-2  col-lg-3 control-label">文本内容显示类型 </label>
                   <div class="col-sm-4  col-lg-4" >
                      <div class="col-sm-7  col-lg-8" >
                        <select id="cacheType" name="cacheType" class="input-sm form-control" v-model="listparam.cacheType">
                          <option value ="STRING">字符串</option>
						  <option value ="OBJECT">对象</option>
						  <option value ="AUTO">自动,先对象后字符串</option>
                        </select>
                     </div>
                   </div>
               </div>
            </div>
            <div class="chart box-body " style="position: relative;" v-if="getTableData">
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
			       	</form>
   </section>
	<div id="valueWrap" style="display:none;">
		<textarea name="cacheValue" class="cache-area" id="cacheValue" cols="60" rows="10" v-model="cacheValue"></textarea>
	</div>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/js/app/redisCacheDetail.js"></script>
</body>
</html>