<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>角色管理</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="role-list">
		<section class="content-header">
		  <h1>
		   角色管理
		  </h1>
		</section> 
   <section class="content">
   <div class="row">
	   	<div class="col-md-12">
	   		<div class="box">
	   			<div class="box-body customCol">
	   			<form class="form-horizontal">
	   				<div class="row">
	   				<div class="col-sm-12 col-md-11 col-lg-11 bor-rig-light">
	   						<div class="col-md-3 margin10">
		   						<div class="form-group-sm">
			   						<label for="title" class="col-sm-4 control-label">角色名称</label> 
			   						<div class="col-sm-7">
			   							<input type="text" name="name" id="name" placeholder="角色名称" class="form-control"/>
			   						</div>
		   						</div>
		   					</div> 
	   						<div class="col-md-3 margin10">
		   						<div class="form-group-sm">
			   						<label for="categoryTypeIdStr" class="col-sm-4 control-label">所属部门</label> 
			   						<div class="col-sm-7" id="deptId">
				   						<select-control
									        :id="id"
									        :name="name"
									        :option-id="id"
									        :option-name="name"
									        :api="selectUrl"
									        :selected-value="selectedValue"
									        :placeholder="placeholder"
									        :inputClass="inputClass"
									    ></select-control>
			   						</div>
		   						</div>
	   						</div> 
	   						<!-- <div class="col-md-3 margin10">
		   						<label for="status" class="col-sm-4 control-label">状态</label> 
		   						<div class="col-sm-7">
			   						<select id="status" name="status" class="form-control">
				   						<option value="">全部</option>
										<option value="ENABLED">启用</option>
										<option value="DISABLED">停用</option>
			   						</select>
		   						</div>
	   						</div> -->
	   				</div> 
	   				<div class="col-lg-1">
		   				<a class="btn btn-danger mt10" @click="goSearch">
		   					<i class="fa fa-search"></i>查询
			            </a></div>
		            </div>
		            </form>
	   			</div>
	   		</div>
	   	</div>
  </div>
      <div class="row">
        <section class="col-sm-12">
          <div class="box">
            <!-- Morris chart - Sales -->
            <div class="box-header with-border">
              <a href="${ctx}/role.htm?action=addRole" class="btn btn-sm btn-danger pull-right">
				<i class="fa fa-plus"></i> 新增
			</a>
            </div>
            <div class="chart box-body" style="position: relative;">
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
<script type="text/javascript">
var selectCatid = "400,402";
</script>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-select.js"></script>
<script type="text/javascript" src="${ctx}/js/app/roleList.js"></script>
</body>
</html>