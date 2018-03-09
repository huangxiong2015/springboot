<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }"
	scope="request" />

<!--[if IE 8 ]>    <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="zh-CN"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
<link rel="stylesheet" href="${ctx}/css/app/activityDetail.css"/> 
<link rel="stylesheet" href="${ctx}/css/lib/jquery.mCustomScrollbar.css"/> 
<title>活动详情</title>
<style type="text/css">
.pd7{padding-top:7px;}
</style>
</head>

<body class="hold-transition skin-blue sidebar-mini">
	<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<!-- Main content -->
	  <div class="content-wrapper" id="activity-detail">
       <section class="content" style=" color:#666;">
	      <form class="form-horizontal" name="manu-from" id="manu-from" onsubmit="return false;" >    
	          <div class="row">
	              <section class="col-lg-12">
	                <div class="box box-danger ">
	                   
	                      <div class="box-header with-border">
	                        <h3 class="box-title"><span class="edit_state">活动详情</span></h3>
	                      </div>
	                      <div class="box-body ">
	                               <div class="form-group ">
	                                   <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">活动名称：</label>
	                                     <div class="col-sm-4  pd7"  >
	                                        <span  v-html="datas.name ? datas.name : '--' ">--</span>
	                                      </div>
	                                  </div> 
	                             
	                              </div>
	                              <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label">活动类型：</label>
	                                      <div class="col-sm-4  pd7"  >
	                                       <!--   <span  v-html="datas.type ? datas.type : '--' ">--</span> -->
	                                       <!-- <span  v-if="datas.type == '10001'  " >限时促销</span> -->
	                                       <span>限时促销 </span>
	                                      </div>
	                                  </div>
	                              </div>
	                   			 <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label" >活动时间：</label>
	                                      <div class="col-sm-4  pd7" >
	                                      <span  v-html="datas.startDates"></span>
	                                        <span >~</span>
	                                      <span  v-html="datas.endDates"></span>
	                                      </div>
	                                  </div>
	                              </div>
	                             <div class="form-group ">
	                                  <div class="col-md-11">
	                                      <label class="col-sm-2 control-label" >活动商品：</label>
	                                      <div class="col-sm-4  pd7" >
	                                     
	                                      </div>
	                                  </div>
	                              </div>
	               <!--start content -->
	                      <!-- <div  style="height:700px;" > -->
	                          
	                          <div class="form-group "  style="padding-left: 15%;" >
	                                  <div class="haha "  v-for="(ele, $index) in prelist"  @click="clickRow($event, $index)">
	                                     <!--  <label class="col-sm-3 control-label"></label>  -->
	                                       <div class="col-sm-11   table_hh"   >
	                                          <!--table-start:-->
	                                                <div  class="periodsName"  >
	                                                   <span v-html="'时段'+arr[$index]">
	                                                   </span>:&nbsp;&nbsp;<span v-html="ele.startTime"></span>&nbsp;~ &nbsp;<span v-html="ele.endTime">
	                                                   </span>
	                                                   <span class="showText" >
	                                                      <em  v-show="index === $index">-</em>
	                                                      <em  v-show="index != $index">+</em>
	                                                      <em class="closeData" v-show="index === $index">收起</em>
	                                                      <em class="open" v-show="index != $index">展开</em>
	                                                      
	                                                    </span>
	                                                 </div>
                                                   
                                                    <div class="promotion "  v-show="index === $index"  >
                                                        <div class="periodsNum"> 活动商品：已上传&nbsp;<em v-html="ele.list.length"></em>&nbsp;条</div> 
													      <table cellpadding="0" cellspacing ="0" class="">
													        <thead>
													            <tr>
													                <th class="first bbd bld "  style="width:30%;">商品信息</th>
													                <th  class="bbd" style="width:10%;">供应商</th>
													                <th  class="bbd" style="width:10%;">库存</th>
													                <th  class="bbd" style="width:15%;">仓库</th>
													                <th  class="bbd"  style="width:15%;">阶梯</th>
													                <th  class="bbd brd"  style="width:20%;">限时价</th>
													            </tr>
													        </thead>
													        <tbody  v-for="item in  ele.list" >
													            <tr >
													             <td colspan="6"><div class="goodstitle"  v-html="item.title" title>3hahahahahahahah</div></td>
													            </tr>
													
													            <tr>   
													                <td  class="bbd bld"  style="height:100px;width:254px;">
													                    <div class="goodsInfo l">
													                      <!-- <a href="//www.baidu.com" :href="product.href"> -->
													                        <img :src="item.image1 && item.image1 !==''?item.image1:'${ctx}/images/static/defaultImg.120.jpg' ">
													                     <!--  </a>  --> 
													                                                       
													                    </div>
													                    <div class="l prd-info">
													                        <p>
													                            <span   :title="item.manufacturerPartNumber"    v-html="item.manufacturerPartNumber ?item.manufacturerPartNumber:'--'">型号</span>  
													                        </p>
													                         <p>
													                                                                                     类型：<span  :title="item.category3Name" v-html="item.category3Name ?item.category3Name:'--'">品牌/制造商</span>
													                        </p>
													                        <p>
													                          	  品牌：<span  :title="item.manufacturer" v-html="item.manufacturer ?item.manufacturer:'--'">品牌/制造商</span>
													                        </p>
													                    </div>
													                </td>
													                <td class="bbd" v-html="item.vendorName ?item.vendorName:'--'" >供应商</td>
													                <td class="bbd" v-html="item.totalQty ?item.totalQty:'--'">库存</td>
													                <td class="bbd" v-html="item.sourceName?item.sourceName:'--'">仓库名称</td>
													                <td class="bbd">
													                    <span class="pq-break" v-for="el in item.qtyBreak" v-html="el + '+'"></span>
													                </td>
													                <td class="bbd brd"   >
													                	<span class="pq-break price" v-for="el in item.priceBreak" v-html="item.currencyUomId=='CNY'?'￥'+el:'$'+el"></span>									                    
													                </td>
													            </tr>
													        </tbody>
													    </table>
													  </div>
																										
                                               <!--table-end:-->
	                                      </div>
	                                  </div>
	                              </div>
	                <!-- </div> -->
	               <!-- end  content -->
	                            
	                           </div>
	                             
	                      </div>
	                  
	              </section>
	
	          </div>
	      </form>
	    </section>
	    </div>
	<!-- Main content end -->
	<script type="text/javascript">
		var selectCatid = "239,298";
	</script>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/lemon/footer.jsp" />
	<!---footer  end   ---->
	
	<%-- <script type="text/javascript" src="${ctx}/js/lib/jquery.mCustomScrollbar.js"></script>  --%>
	
	<script type="text/javascript" src="${ctx}/js/app/activityDetail.js"></script>

	
	<script type="text/javascript" >
	 
	 /*  $(".promotion").mCustomScrollbar(); */
	
	</script> 
</body>
</html>