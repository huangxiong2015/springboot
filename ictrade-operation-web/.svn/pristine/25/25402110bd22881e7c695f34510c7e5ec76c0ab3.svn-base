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
  <title>redis管理</title>

 <style type="text/css">	
 
 .right{
    margin-left: 172px;
    min-height: 684px; 
 } 
    
  .redis-header {   
    border-bottom: 1px solid #d2d6de;
    background: #FCFDFF;
    position: relative;
    padding: 15px 15px 0 15px;
    height: 40px;
} 

.redis-header > h1 {
    margin: 0;
    font-size: 13px;
    color: #c2c3c7;
}
   
    .list{     
    font-size: 14px;
    }
    
    
    
    .list_top {     
     position: relative;    
     left:15px;
     top:15px;
     padding-left:15px;
     width: 100%;
     background: #ffffff;
     border-radius: 3px;
     height:54px;    
     line-height: 54px;
     color: #333;
     font-weight:bold;         
    }
    
    .list_top_left1 {     
           float:left;
    }
    .list_top_left2 {     
           float:left;
           margin-top: 9px;
    }
    .list_top_left3 {     
           float:left;
           margin-left: 66px;          
    }
    .list_top_left4 {     
           float:left;
           margin-top: 11px;
           margin-left: 11px;
           width:300px;         
    }
    .list_top_left5 {     
            float:left; 
            margin-left: 200px;
            margin-top: -20px;          
    }
     .list_centent {     
	        position: relative;
		    border-radius: 3px;
		    background: #ffffff;
		    margin-bottom: 20px;
		    width: 100%;
		    border: 1px solid #d2d6de; 
		    font-size: 13px;
            color: #AAAAAA; 
            min-height: 684px;   
            margin-left: 13px;  
            margin-top: 30px; 
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
    word-break: keep-all;
    white-space:nowrap;
}

.details{
 width: 100%;
 min-height: 684px; 
}


.details-top{
      margin-left: 20px;
      margin-top: 20px;
      position:absolute;
      

}

.details_left{   
      margin-left: 20px;
      margin-top: 100px;
      width:700px;
      
      position:absolute;
}

.details_right{   
      margin-left: 800px;
      margin-top: 100px;
      width:400px; 
      min-height:500px;    
      position:absolute;
}

   .sum{
    padding-left: 0px;
    margin-left: 100px;
    margin-top: 20px;
    background-color: #c11f2e;
    color: white;
  
  }
  .details_right{
  font-size: 14px;
    color: #333;
  }
  
  .table2{
    font-size: 15px;
    color: black;
    text-align: left;
    width:700px;  
    border: 1px solid;
    margin-top: 10px;  
    table-layout:fixed;/* 只有定义了表格的布局算法为fixed，下面td的定义才能起作用。 */
  }
  .table2 tr {
  height:36px;
  }
  
  .thead2 tr{
	background:  #999;
  }
  .tbody2 tr:nth-child(odd){
	background: #f5f5f5;
  }
  .tbody2 tr:nth-child(even){
	background: #ddd;
  }
    
  .tbody2 span{
	padding: 3px 8px;
    background-color: #c11f2e;
    color: white;
    cursor: pointer;
    border-radius: 3px;
  }
  
 .tbody2 td{
    width:100%;
    word-break:keep-all;/* 不换行 */
    white-space:nowrap;/* 不换行 */
    overflow:hidden;/* 内容超出宽度时隐藏超出部分的内容 */
    text-overflow:ellipsis;/* 当对象内文本溢出时显示省略标记(...) ；需与overflow:hidden;一起使用。*/
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
	
	<div class="right">
	
	<section class="redis-header">
		  <h1>
		   redis缓存管理
		  </h1>
	</section> 
	<div class="list" id="list" style="display:black">
	  <!--  <div class="list_top">
	       <div class="list_top_left1">
	                                     选择库：
	       </div>
	       
	       <div class="list_top_left2">
	         <select id="db" class="form-control undefined">
	         <option value="0">0</option> 
	         <option value="1">1</option>
	         <option value="2">2</option>
	         <option value="3">3</option>
	         <option value="4">4</option>
	         <option value="5">5</option>
	         <option value="6">6</option>
	         <option value="7">7</option>
	         <option value="8">8</option>
	         <option value="9">9</option>
	         <option value="10">10</option>
	         <option value="11">11</option>
	         <option value="12">12</option>
	         <option value="13">13</option>
	         <option value="14">14</option>
	         <option value="15">15</option>
	         </select>                                    
	       </div>
	       
	       <div class="list_top_left3">
	                                                缓冲池名称(key):
	       </div>
	       
	        <div class="list_top_left4">
	              <input type="text" id="key" class="input-sm form-control">
	       </div>
	       
	       <div class="list_top_left5">
	             <button id="find" class="btn btn-danger sendData mt30" onclick="init()"><i class="fa fa-search"></i>查询 </button>			                   
	       </div>         	
       </div> -->
       
       <div class="list_centent">                  
                 <table class="table">
						  <thead class="table-thead">						  
						          <tr>
						            <th width="10%">数据库DB</th>
						            <th width="30%">缓冲池房间名</th>
						            <th width="30%">数量</th>
						            <th width="20%">操作</th>                                  
                                   </tr>						   
						  </thead>					  		
						  <tbody class="tbody" id="centent">                    							        						   						    
						  </tbody>					  		  
			</table>                
       </div>
	
	</div><!-- list -->
	
	 <div class="details" id="details" style="display:none">
	 <div class="details-top">
	    <h3>数据库DB:<span id="db">1</span></h3>
	    <h3>缓冲池房间名:<span id="keyName">10</span></h3>
	    <h3>文本内容显示类型:
		    <select id="cacheType">
			  <option value ="STRING">字符串</option>
			  <option value ="OBJECT">对象</option>
			  <option value ="AUTO">自动,先对象后字符串</option>							 
			</select>
	    </h3> 
	   
	 </div>
	       
	       <div class="details_left">
	          <input type="text" placeholder="请输入查找的key" id="key" style="width:300px">
	          <input type="button" value="查找" class="sum" onclick="findKeys()" style="margin-left:0px;">
	          <input type="button" value="返回" class="sum" onclick="back()" >	                
	          <table class="table2">
	            <thead class="thead2">
	               <tr>
	                 <th width="80%">key</th>	                
	                 <th width="20%">操作</th>               
	               </tr>
	            </thead>
	            <tbody class="tbody2" id="content2">                    
	            </tbody>                    
	          </table>
	       	
	       </div>
	       <div class="details_right">
	              <div>key：<span id="textareaName" style="color: #d91600;"></span></div>
	              <textarea style="width:500px;height:500px" id="details_value">           
	              </textarea>
	       </div>
	            
	</div><!-- details --> 
	
	       
	</div><!-- right --> 
	       
	

	   
	<!---footer  start   ---->
	<jsp:directive.include file="../include/main/footer.jsp" />
	<!---footer  end   ---->
	<!-- layer -->
	
	<!-- layer -->
<script src="${ctx}/js/lib/plupload-2.1.2/js/plupload.full.min.js"></script>
<script src="${ctx}/js/credenceUpload/credenceUpload.js?a=20170109"></script>
<script src="${ctx}/js/credenceUpload/oss_credence.js?b=20170100600"></script>
<script src="${ctx}/js/lib/jquery.validate.js"></script>
<script src="${ctx}/js/app/add.validate.js"></script>

</div>	    
	
<script type="text/javascript">

$(function(){
	
	
	 init();   //初始化数据

})

//初始化数据
function init(){ 	
	$.aAjax({
		url: ykyUrl.database + '/v1/cache/findControlList',
	    type: 'GET',
	    async: false,
	    data:{},
	    success: function(data){	
	    	if(null != data && data.list.length > 0){		    		    		  	
	    		var content = "";   	    	
	   	    	for(var i=0;i<data.list.length;i++){
	   	    		var d = data[i];
	   	    		var html ='<tr>';	 
	   	    	    	html +='<td>'+d.database+'</td>';
	   	    	    	html +='<td>'+d.key+'</td>';   	    	    	  	    	    	
	   	    	        html +='<td>'+d.keyCount+'</td>';	   	    	    	
	   	    	        html +='<td><a class="edit" href="javascript:delQ(\''+d.database+'\',\''+d.key+'\')">删除</a>';
	   	    	    	html +='<a class="edit" href="javascript:details(\''+d.database+'\',\''+d.key+'\')">详情</a></td>';    	    	    	
		   	    		html +='</tr>';
		   	    		content = content + html;	   	    		   	    		
	   	    	} 
	   	    	$("#centent").empty().html(content);	   	    	
	    	}
	    },
	   	error:function(e){
	   		alert("服务器异常");
	    }
	    });
	
}	


//列表详情页面删除 房间名
function delQ(database,key){
	
	if(window.confirm('你确定要删除吗？库:'+database+'key:'+key)){
		var da = $.trim(database);
		var ke = $.trim(key);
		
		
		$.aAjax({
			url: ykyUrl.database + '/v1/cache/deleteCacheKeyName?database='+da+'&key='+ke,
		    type: 'GET',
		    async: false,
		    data:{},
		    success: function(data){	
		    	if(null != data){	 		    		
		   	    	alert("删除成功");
		   	    	init();
		    	}
		    },	
		    });
	
     }else{
  
        return false;
    }			
}


//房间详情页面删除
function del(database,key){
	
	if(window.confirm('你确定要删除吗？库:'+database+'key:'+key)){
		var da = $.trim(database);
		var ke = $.trim(key);
		

		$.aAjax({
			url: ykyUrl.database + '/v1/cache/deleteCacheKey?database='+da+'&key='+ke,
		    type: 'GET',
		    async: false,
		    data:{},
		    success: function(data){	
		    	if('SUCCESS'==data){
		    		alert("删除成功");
		    		details(databaseA,keyA);
		    	}else{
		    		alert("删除失败");
		    	}
		    },
		   	error:function(e){
		   		alert("服务器异常");
		    }
		    });
     }else{
  
        return false;
    }			
}



function details(database,key){	
		
	$("#list").hide();
	$("#details").show();	
	
   	$("#db").html(database);
   	$("#keyName").html(key);
   	$("#key").val("");
   	$("#content2").empty();
    $("#textareaName").html("");
   	$("#details_value").val(""); 	   	     			
}

function findKeys(){	
	var database=$("#db").html();
	var keyName=$("#keyName").html();
	var cacheType=$("#cacheType option:selected").val();
	var key1=$.trim($("#key").val());
	/* if(key1==""){
		alert("key不能为空");
		return false;
	} */
	var key = keyName+":"+key1;		
	$.aAjax({
		url: ykyUrl.database + '/v1/cache/findCacheKeys?database='+database+'&key='+key,
	    type: 'GET',
	    async: false,
	    data:{},
	    success: function(data){	
	    	if(null != data && data.keys.length > 0){
	    		var content = "";   
	    		var data1 = data.keys;
	   	    	for(var i=0;i<data1.length;i++){
	   	    		var d = data1[i];
	   	    		var html ='<tr>';	    	    		    
	   	    	    	html +='<td title=\''+d.substring(d.indexOf(":")+1)+'\'>'+d.substring(d.indexOf(":")+1)+'</td>';
	   	    	    	html +='<td><span onclick="findValue(\''+database+'\',\''+d+'\')">查看</span></td>';   	    	    	
		   	    		html +='</tr>';
		   	    		content = content + html;	   	    		   	    		
	   	    	} 
	   	    	$("#content2").empty().html(content);	
	    			   	    	
	    	}
	    },
	   	error:function(e){
	   		alert("服务器异常");
	    }
	    }); 
}



function findValue(database,key){	
	var cacheType=$("#cacheType option:selected").val();
	$("#textareaName").html(key);		
	$.aAjax({
		url: ykyUrl.database + '/v1/cache/findCacheOne?database='+database+'&key='+key+'&cacheType='+cacheType,
	    type: 'GET',
	    async: false,
	    data:{},
	    success: function(data){	
	    	if(null != data){	 
	    		$("#details_value").val(data.value); 	   	    	
	    	}
	    },
	   	error:function(e){
	   		alert("服务器异常");
	    }
	    }); 
		
}




function back(){
	$("#list").show();
	$("#details").hide();	
	$("#centent").empty();
	init();	
}

 	
</script>
<script>

</script>
</body>
</html>
