<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" /> 
<html lang="zh-CN">
<head> 
<title>认证企业管理</title> 
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" type="text/css" href="${ctx }/css/common/component.css" />
<link rel="stylesheet" type="text/css" href="${ctx }/css/app/cerEnterpriseList.css" />
</head>
<body class="hold-transition skin-blue sidebar-mini detection">
<div class="wrapper"> 
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
  <div class="content-wrapper" id="cerEnterprise-list">
		<section class="content-header">
		  <h1>认证企业管理</h1>
		  <ol class="breadcrumb">
		    <li><a href="#"><i class="fa fa-dashboard"></i>Home</a></li>
		    <li class="active">认证企业管理</li>
		  </ol>
		</section> 
	<section class="content">
		<div class="row">
	        <section class="col-md-12">
	          <div class="box box-solid ">
	            <div class="box-body customCol">
	              <form class="form-horizontal" method="post" name="seachForm" id="seachForm" @submit.prevent="onSearch">
	                <div class="row">
	                  <div class="col-sm-11 col-md-11 col-lg-11 bor-rig-light">
	                    <div class="row" >
	                      <div class="col-md-4 margin10">
	                        <label for="partyCode" class="col-sm-4 control-label">YKY客户编码</label>
	                        <div class="col-sm-7">
	                          <input type="text" class="form-control" id="partyCode" name="partyCode" placeholder="YKY客户编码" v-model="queryParams.partyCode" >
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10">
	                        <label for="name" class="col-sm-2 control-label company_name">公司名称</label>
	                        <div class="col-sm-7">
	                          <input type="text" class="form-control" id="name" name="name" placeholder="公司名称" v-model="queryParams.name" >
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10">
	                        <label for="corCategory" class="col-sm-4 control-label">公司类型</label>
	                        <div class="col-sm-7" >
	                          <select class="form-control" id="corCategory"  name="corCategory" v-model="queryParams.corCategory"  >
	                          	<option value="">全部</option>
	                            <option v-for="item in initData.companytypeList" :value="item.categoryId">{{ item.categoryName }}</option>
	                          </select>
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10">
	                        <label for="applyType" class="col-sm-4 control-label">行业</label>
	                        <div class="col-sm-7" >
	                         <div class="f-ib"> 
							   <a class="select_industry" @click="toggleIndustry"><em class="select_tip">请选择所属行业</em><i class="icon-down_arrow1 g9"></i></a> 
							   <div class="industry_content dn"> 
							    <div class="industry_list  mCustomScrollbar _mCS_3 _mCS_1" style="max-height: 80px; overflow: hidden;"> 
							     <div class="mCustomScrollBox mCS-light" id="mCSB_1" style="position: relative; height: 100%; overflow: hidden; max-width: 100%; max-height: 80px;">
							      <div class="mCSB_container mCS_no_scrollbar" style="position:relative; top:0;">
							       <div class="company_type noselect" unselectable="on" v-for="item in initData.industryList" @click="selectIndustryItem">
							        <span class="check-box-white isfirst" :data-id="item.categoryId"></span>
							        <i class="g3">{{ item.categoryName }}</i>
							        <input v-if="item.categoryId == 1008" v-show="initData.isShowIndustryOther" @click.stop v-model="initData.otherAttr" id="industryOther" name="industryOther" class="other_attr v-success" maxlength="10" placeholder="请填写所属行业"/>
							       </div>
							      </div>
							      <div class="mCSB_scrollTools" style="position: absolute; display: none;">
							       <div class="mCSB_draggerContainer">
							        <div class="mCSB_dragger" style="position: absolute; top: 0px;" oncontextmenu="return false;">
							         <div class="mCSB_dragger_bar" style="position:relative;"></div>
							        </div>
							        <div class="mCSB_draggerRail"></div>
							       </div>
							      </div>
							     </div>
							    </div>
							    <div class="industry_btn mt20 tc">
							     <a class="btn_y" @click="confirmIndustry">确认</a>
							     <a class="btn_c" @click="cancelIndustry">取消</a>
							    </div>
							   </div>
							   <div class="select-area">
							   	<div v-for="item in initData.selectedIndustry" class="area-item" :data-id="item">{{ item | findIndustryName }}<i class="icon-close-min close" @click="removeSelect"></i></div>
							   </div>
							   <input id="industry" name="industry" type="hidden" data-duplex="@parameter.industry" value="" />
							  </div>
	                          	
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10 addressArea">
							 <label class="l lh select_label">区域</label>
							 <div id="app-select">
							    <select-province
							            :api="selectBox.api"
							            @onchange="change"
							            :input-class="selectBox.inputClass"
							            :name-district="selectBox.nameDistrict"
							            :name-province="selectBox.nameProvince"
							            :name-city="selectBox.nameCity"
							            :set-province="selectBox.setProvince"
							            :set-city="selectBox.setCity"
							            :set-district="selectBox.setDistrict"
							            :query-param="selectBox.queryParam"
							            :parent-id="selectBox.parentId"
							    ></select-province>
							</div>
						  </div>
	                      <div class="col-md-4 margin10">
	                        <label for="verifyStatus" class="col-sm-4 control-label">认证状态</label>
	                        <div class="col-sm-7" >
	                          <select class="form-control" id="verifyStatus" name="verifyStatus" v-model="queryParams.verifyStatus"  >
	                          	<option value="">全部</option>
	                          	<option v-for="item in initData.activeStatusList" :value="item.code">{{ item.value }}</option>
	                          </select>
	                        </div>
	                      </div>
	                      <div class="col-md-4 margin10 kaitongquanxian">
	                        <label for="auth" class="col-sm-4 control-label">开通权限</label>
	                        <div class="col-sm-7" >
	                          <select class="form-control" id="auth" name="auth" @change="selectAuto">
	                          	<option value="">全部</option>
	                            <option value="ACCOUNT_VERIFIED">子账号</option>
	                            <option value="PERIOD_VERIFIED">账期</option>
	                          </select>
	                        </div>
	                      </div>
                      </div>
                      </div>
	                  <div class="col-lg-1">
	                    <button class="btn btn-danger sendData margin10" id="search_all" >
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
            <div class="chart box-body " style="position: relative;">
              <!--表格组件-->
               <rowspan-table
                      :columns="gridColumns"
                      :pageflag="pageflag"
                      :query-params="queryParams"
                      :api="url"
                      :refresh="refresh"
                      :show-total="showTotal"
              >
              </rowspan-table>
              <form id="exportForm" action="" method="get" target="_blank" style="display:none;">
				<input type="hidden" name="Authorization"/>
			  </form>
 			  <button v-if="" id="export" class="btn btn-success sendData margin10 export" @click="exportExcels"><i class="fa fa-download mr5"></i>导出</button>
            </div>

          </div>
        </section> 
      </div>
   </section>
</div>
<jsp:directive.include file="../include/lemon/footer.jsp" />
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-grid.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-city.js"></script>
<script type="text/javascript" src="${ctx }/js/common/component.js"></script>
<script type="text/javascript" src="${ctx}/js/enterprise/cerEnterpriseListKf.js"></script>
</body>
</html>