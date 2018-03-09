<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<html lang="zh-CN">
<head>
  <c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
  <title>SPU管理</title>
<style type="text/css">
.m_sure{
    border: 1px solid #e6e6e6;
    padding: 5px 10px;
    background-color: #ac2925;
    border-radius: 2px;
    color: #fff;
    cursor: pointer;
 }
.m_sure:hover{background-color: #d20032;}

.m_cancel{
    border: 1px solid #e6e6e6;
    padding: 5px 10px;
    background-color: #fff;
    border-radius: 2px;
    cursor: pointer;
 }
 .m_cancel:hover{
    border: 1px solid #ac2925;
    color: #ac2925;
 }
.ml20{
   margin-left: 20px;
   }
.man_box{
  height:400px;margin-top:10px;border:1px  solid  #e5e5e5;padding:10px;margin:10px;
}
.mannu_input{
  height:30px;
  }
</style>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>

	<div class="content-wrapper" id="productstandedit">
	    <section class="content-header">
            <h1>SPU管理</h1>
        </section>

        <section  class="content">
            <!-- 供应商 -->
            <div class="row">
                <section class="col-sm-12">
                     
                    <div class="box "  >
	                    <div class="form-group" style="height: 60px;line-height:60px">
			                <div class="col-md-12">
				                <label class="col-sm-1">型号</label>
				                <div class="col-sm-11" >
									<input type='text' class="mannu_input" v-model.trim='manufacturerPartNumber' />
				                </div>
			                 </div>
		               </div> 
                        <modal v-show="showModal" @close="showModal = false" @click-ok="modalOk" @click-cancel="toggleModal" :modal-style="modalStyle"  :modal-text="modalText">
                        	<div class="col-md-12" style="padding-bottom:40px;">
                        		<span>已选择：</span><span class="a-supplier" v-for="item in chosenManufacturer" :title="item.brandName" :data-id="item.id" v-html="item.brandName"></span>
                        	</div>
				            <div class="col-md-12" style="overflow: auto;max-height: 300px;">
				            	<div v-if="showManufacturer">
					            	<letter-select 
											:keyname="keyname"
											:id="id"
											:options="options"
											:name="name"
											:option-id="optionId"
											:option-name="optionName"
											@get-selecteds="getSelected"
											:multiple="multiple"
											:placeholder="placeholder"
											:selected="selected"
											:is-fuzzy-search="manufacturerIsFuzzySearch"
											:reload-api="manufacturerReloadApi"
											ref="letter" 							   
									></letter-select>
								</div>
				            </div>
				        </modal>
 
	                  <div class="form-group" style="height: 40px;">
	                  		<div class="col-md-12">
				                <label class="col-sm-1">制造商选择：</label>
				                <div class="col-sm-11" >
									<span class="a-supplier"  style="margin-right:5px;" v-for="item in activeManufacturerId" :title="item.brandName" :data-id="item.id" v-html="item.brandName"></span>
		                 			<span @click="choose()" style="color:#3c8dbc;cursor:pointer;">选择</span>
				                </div>
			                </div> 
			           </div>
	                   <div class="form-group" style="height: 100px;">
	                   
		                <div class="col-md-12">
		                	<label class="col-sm-1"></label>
			                <div class="col-sm-5">
								 <button  type="button" class='btn btn-danger c-btn'   @click="m_save()">保存</button>
								 <button  type="button" @click="m_cancel()" class="btn btn-cancle c-btn ml20">取消</button>
			                </div>
		                 </div>
	                  </div>   
                   </div>
               </section>
            </div>
        </section>
    </div>

	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<script src="${ctx}/js/lib/iview.min.js"></script>
</div>

<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-letter-select.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-modal.js"></script>
<script type="text/javascript" src="${ctx}/js/app/spuEdit.js"></script>


</body>
</html>
