<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />

<!--[if IE 8 ]>    <html class="ie8" lang="zh-CN"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="zh-CN"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="zh-CN">
<!--<![endif]-->

<head>
	<!-- jQuery 1.11.3 -->
  <c:import url ="/WEB-INF/jsp/include/main/constants.jsp"/>
  <title>优惠券设置列表页</title>

 <style type="text/css">	 
  .sum{
    padding-left: 0px;
    margin-left: 100px;
    margin-top: 20px;
    background-color: #c11f2e;
    color: white;
  
  }
  
  .notsum{
    padding-left: 0px;
    margin-left: 20px;
    margin-top: 20px;
    background-color: #eeeeee;
    color: #c11f2e;
  
  }
  
  
  .content-header {
    margin-left: 165px;
    border-bottom: 1px solid #d2d6de;
    background: #FCFDFF;
    position: relative;
    padding: 15px 15px 0 15px;
    height: 40px;
}

.content-header > h1 {
    margin: 0;
    font-size: 13px;
    color: #c2c3c7;
}

.chart {
    position: relative;
    overflow: hidden;
    width: 100%;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
    border-bottom-right-radius: 3px;
    border-bottom-left-radius: 3px;
    padding: 10px;
}
  .box {
    margin-left: 172px;
    position: relative;
    border-radius: 3px;
    background: #ffffff;
    border-top: 3px solid #d2d6de;
    margin-bottom: 20px;
    width: 92%;
    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);   
}

.table {
    display: table;
    width: 100%;
    border-collapse: collapse;
    table-layout: fixed;
    margin-bottom: 20px;
    margin: 0 auto;
    padding: 0;
    border: 1px solid #f4f4f4;
}

.table-thead {
    display: table-header-group;
}

.table>thead>tr>th {
    display: table-cell;
    height: 100%;
    border: 1px solid #eee;
    vertical-align: middle;
    border-top: 0;
    color: #333;
    font-weight: bold;
    font-size:16px;
}
.table>tbody>tr>td {
    display: table-cell;
    height: 100%;
    border: 1px solid #eee;
    vertical-align: middle;
    border-top: 0;
    color: #333;
    font-size:14px;
}

 .table2 tr{
     height:50px;
   }
   .table2 tr td:first-child{
     text-align:right;
   }
   
    .table2 tr td:nth-child(2){
     padding-left:20px;
   }
   
   
   .table2{
    color: #333;
    font-size:14px;
   }

    
 </style>
  <link rel="stylesheet" href="${ctx}/js/credenceUpload/credenceUpload.css?20161229" />
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
	<jsp:directive.include file="../include/main/header.jsp" />
	<!---index header  end   ---->
	<jsp:directive.include file="../include/main/menu.jsp" />
	
	<section class="content-header">
		  <h1>
		   后台支付管理
		  </h1>
	</section> 
	<div class="box">
	<div class="box-body" id="list" style="position: relative;">
	          <table class="table">
						  <thead class="table-thead">
						  
						  <tr>
						            <th width="100px">
                                                                                                                                  支付类型
                                    </th>
                                    <th width="100px">
                                                                                                                                  描述
                                    </th>
                                    <th width="100px">
                                                                                                                               最后更新人
                                    </th>
                                    <th width="100px">
                                                                                                                               最后更新时间
                                    </th>
                                    <th width="100px">
                                                                                                                                 支付状态
                                    </th>
                                    <th width="100px">
                                                                                                                                    生产环境状态
                                    </th>
                                    <th width="100px">
                                                                                                                                     操作
                                    </th>
                                    </tr>						   
						  </thead>					  		
						  <tbody class="tbody" id="centent">
						   						    
						  </tbody>
			</table> 
     </div>
     </div>
	       
	       <div style="display:none;width:100%;"id="editt">
	      
	               <div style="width:600px;height:600px;margin-left:300px;margin-top:50px;color: #333;">
	               <table class="table2">	               
	                       <tr >
	                       <td>支付类型:</td>
	                       <td id="parentPaymentMethodId"></td>	                       
	                       </tr>
	                       <tr>
	                       <td >支付状态:</td>
	                       <td>
	                       <select id="state">
							  <option value ="VALID">有效</option>
							  <option value ="NOT_VALID">无效</option>							 
							</select>
	                       </td>	                       
	                       </tr>
	                       <tr>
	                       <td >生产环境状态:</td>
	                       <td>
	                       <select id="dev">
							  <option value ="VALID">有效</option>
							  <option value ="NOT_VALID">无效</option>							 
							</select>                                              
	                       </td>	                       
	                       </tr> 
	                       <tr>
	                       <td >描述:</td>
	                       <td><input type="text" value="" style="width:420px" id="description"/></td>	                       
	                       </tr>              	               
	               </table>	
	               <input type="hidden" id="paymentMethodId" value="">
	               <input type="button" value="确定" class="sum" onclick="sum()">
	               <input type="button" value="取消" class="notsum" onclick="notsum()">
	                                 	               
	               </div>
	       
	       </div>
	       
	

	   
	<!---footer  start   ---->
	<jsp:directive.include file="../include/main/footer.jsp" />
	<!---footer  end   ---->
	<!-- layer -->
	
	<!-- layer -->
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script src="${ctx}/js/credenceUpload/credenceUpload.js?a=20170109"></script>
<script src="${ctx}/js/credenceUpload/oss_credence.js?b=20170100600"></script>
<script src="${ctx}/js/lib/jquery.validate.js"></script>
<script src="${ctx}/js/common/add.validate.js"></script>

</div>	    
	
<script type="text/javascript">

$(function(){
	
	
	init();  //初始化数据

})

//初始化数据
function init(){ 
	
	$.aAjax({
		 url: ykyUrl.pay + '/payments/getPayMothodAll',
	    type: 'GET',
	    async: false,
	    data:{},
	    success: function(data){	    	
	   	    if(null != data && data.length > 0){
	   	    	var content = "";
	   	    	html ='';
	   	    	for(var i=0;i<data.length;i++){
	   	    		var d = data[i];
	   	    		html ='<tr>';
	   	    		if(d.paymentMethodId != undefined){
	   	    			html +='<td>'+d.paymentMethodId+'</td>';
	   	    		}else{
	   	    			html +='<td>--</td>';
	   	    		}
	   	    		
	   	    		if(d.description != undefined){
	   	    			html +='<td>'+d.description+'</td>';
	   	    		}else{
	   	    			html +='<td>--</td>';
	   	    		}
	   	    		
	   	    		if(d.lastAccount != undefined){
	   	    			html +='<td>'+d.lastAccount+'</td>';
	   	    		}else{
	   	    			html +='<td>--</td>';
	   	    		}
	   	    		
	   	    		
	   	    		if(d.lastUpdateDate != undefined){
	   	    			html +='<td>'+d.lastUpdateDate+'</td>';
	   	    		}else{
	   	    			html +='<td>--</td>';
	   	    		}
	   	    		
	   	    		if(d.state != undefined){
	   	    			if(d.state =="VALID"){
	   	    				html +='<td>有效</td>';
	   	    			}else{
	   	    				html +='<td>无效</td>';
	   	    			}
	   	    			
	   	    		}else{
	   	    			html +='<td>无效</td>';
	   	    		}
	   	    		
	   	    		if(d.dev != undefined){
	   	    			if(d.dev=="VALID"){
	   	    				html +='<td>有效</td>';
	   	    			}else{
	   	    				html +='<td>无效</td>';
	   	    			}
	   	    			
	   	    		}else{
	   	    			html +='<td>无效</td>';
	   	    		}	   	    			   	    		
	   	    		html +='<td><a class="edit"  href="javascript:edit(\''+d.paymentMethodId+'\')">编辑</a></td>';
	   	    		html +='</tr>';
	   	    		content = content + html;	   	    		   	    		
	   	    	} 
	   	    	
	   	    	$("#centent").empty().html(content);
	   	    }else{
	   	    	alert("无数据");
	   	    }
	   	 },
	   	error:function(e){
	   		alert("服务器异常");
	    }
	    });
	
}	

//编辑
function edit(id){
	if(id==null || $.trim(id)==""){
		return;
	}

	
	$("#parentPaymentMethodId").html("");
	$("#description").val("");
	$("#paymentMethodId").val("");
	
	$.aAjax({
		 url: ykyUrl.pay + '/payments/getPayMothodById?paymentMethodId='+id,
	    type: 'GET',
	    async: true,
	    data:{},
	    success: function(data){	
	    	
	    	if(null!=data){
	    		$("#parentPaymentMethodId").html(data.paymentMethodId);	    		
	    		$("#state").val(data.state);
	    		$("#dev").val(data.dev);
	    		$("#description").val(data.description);	
	    		$("#paymentMethodId").val(data.paymentMethodId);	    		
	    		$("#list").hide();
	    		$("#editt").show();
	    	}	    	
	    }	
	 });
	
	
}


//提交数据
function sum(){

	
	var state=$("#state option:selected").val();
	var dev=$("#dev option:selected").val();
	var description=$("#description").val();
	var paymentMethodId=$("#paymentMethodId").val();

	
	if($.trim(description)==""){
		alert("描述不能为空");
		return;
	}
	
	if($.trim(paymentMethodId)==""){
		alert("无效页面");
		return;
	}
	var entity={};
	entity.state=state;
	entity.dev=dev;
	entity.description=description;
	entity.paymentMethodId=paymentMethodId;
	$.aAjax({
		 url: ykyUrl.pay + '/payments/updatePayMothod',
	    type: 'POST',
	    async: true,
	    data:JSON.stringify(entity),
	    success: function(data){	
	    	
	    	if(null !=data){
	    		if(data.code=="200"){
	    			alert("修改成功");
	    			$("#editt").hide();
	    			$("#list").show();
	    			init();
	    		}
    		
	    	}	    	
	    }	
	 });	
}
//取消返回
function notsum(){
	$("#editt").hide();
	$("#list").show();
	init();
}


  	
</script>
<script>
	
</script>
</body>
</html>
