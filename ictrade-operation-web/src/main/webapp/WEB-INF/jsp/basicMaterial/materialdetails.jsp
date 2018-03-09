<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="request" />
<!-- <!DOCTYPE> -->
<!DOCTYPE html>
<html lang="zh-CN">
<html>
<head>
<title>物料详情</title>
<style type="text/css"> 
/* 物料详情  */
.table .name {
	background-color: #eeeeee;
	text-align: right;
	padding-right: 10px;
}

.table .value {
	background-color: #ffffff;
	text-align: left;
	padding-left: 10px;
}

a:hover, a:active, a:focus {
	color: #23527c;
}

#largeImg {
	position: absolute;
	z-index: 99999;
}

.litterImg {
	width:120px;
	height:120px;
}

.bigImg {
	width: 200px;
	height: 200px;
	border:2px solid #d6d6d6;
}
.content{
  background-color: #fff;
 }
 .col-xs-10 ul li{
  list-style: none;
  }
  .l{
    float:left;
  }
  .mt30{
     margin-top:30px;
   }
  .mt10{
  margin-top:10px;}
.mt20{
  margin-top:20px;}
  .r{
    float:right;
  }
  #todateCode{
    height: 185px;
    width: 100%;
    padding: 10px;
  }
  .logo-box #uploadback{
    position: absolute;
    bottom: 0;
    width: 120px;

  }
  input[type=button]{
   filter: alpha(opacity=0.8);
    -moz-opacity: 0.8;
    -khtml-opacity:0 .8;
    opacity: 0.8;
  }
  
  .manu_logo .file-btn{
    width: 120px;
    margin-top: 20px;
  }
  .remove-box{
   margin-top:35px;
   margin-left:10px;
  }
  .del_logo, .re-change, .manu_text{
   margin-right:10px;
  }
  .add-box{
  margin-top:20px;
 
  }
  
 .condition_inquery{
    background-color: #c11f2e;
    color: #fff;
    padding: 10px 20px;
    font-size: 14px;
    border-radius: 3px;
  }
  .return_btn{
    padding: 10px 20px;
    cursor: pointer;
    border: solid 1px #ddd;
    border-radius: 3px;
    font-size: 14px;
    color: #ffffff;
    margin-left: 30px;
    background-color: #c11f2e;
  }
   .return_btn:hover{
       color: #72afd2
       }
   .word-break-all{
   		word-break: break-all;
   }
   .mr6{
     margin-right:6px;
    } 
    .content-wrapper .content-header .header_tab {
    	height: 40px;
    	line-height: 40px;
    	padding: 8px 20px 12px 20px;
    	font-size: 14px;
    	color: #919191;
    	cursor: pointer;
	}
	.content-wrapper .content-header .header_tab.active {
   		background-color: #ecf0f5;
    	border-top: 2px solid #c11f2e;
	}
	.content-wrapper .material-header{
    	padding-top: 0;
    	padding-left: 28px;
	}
</style>

<c:import url ="/WEB-INF/jsp/include/lemon/constants.jsp"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
<c:import url ="/WEB-INF/jsp/include/lemon/layout.jsp"/>
	<div class="content-wrapper" id="extension_edit">
			<section class="content-header">
			  <h1>物料详情
			    <small class="edit_state"></small>
			  </h1>
			  <ol class="breadcrumb">
			    <li><a href="./"><i class="fa fa-dashboard"></i> 首页</a></li>
			  </ol>
			</section>
			<section class="content-header material-header">
		  		<a :class="{'header_tab':true,'active':tabValue === 'baseData'}" @click="tabValue = 'baseData'">编辑物料</a>
		  		<a :class="{'header_tab':true,'active':tabValue === 'tariff'}" @click="tabValue = 'tariff'">商检/关税</a>
		  	</section>
<section class="content" style="color:#666;">
<form class="form-horizontal" name="materials-view" id="materials-view" onsubmit="return false;" v-show="tabValue === 'baseData'">

<!-- Main content -->
	 <div style="min-height: 406px;" >
		<section  class="content container-fluid" >
			<div class="row mt20">
				<div class="col-xs-6">
					<div class="row ">
						<div class="row ">
							<div class="col-xs-2 tr pl0">
								<label >物料型号：</label>
							</div>
							<div class="col-xs-10 c5e">
								<span  v-html="datas.manufacturerPartNumber ? datas.manufacturerPartNumber : '--' "></span>
							</div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr">
								<label>物料分类：</label>
							</div>
							<div class="col-xs-10 c5e">
								<span v-for="ele in datas.categories">
								   <i v-if="ele.cateLevel=='1'" class="l">{{ele.cateName}}></i>
								   <i v-if="ele.cateLevel=='2'" class="l">{{ele.cateName}}></i>
								   <i v-if="ele.cateLevel=='3'" class="l">{{ele.cateName}}</i>
								</span>
							</div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr">
								<label>原厂：</label>
							</div>
							<div class="col-xs-10 c5e">
								<span v-html="datas.manufacturer ? datas.manufacturer : '--' "></span>
							</div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr">
								<label >物料图片：</label>
								<span></span>
							</div>
							<div class="col-xs-10 c5e">
										<p>
										   <img  :src="datas.imageurl"  class="litterImg">
										</p>
									  
						    </div>
						</div>
						<div class="row mt30"  v-if="datas.extendInfo  !== undefined ">
							<div class="col-xs-2 tr">
								<label >细节图：</label>
								<span></span>
							</div>
							<div class="col-xs-10 c5e"  v-if="datas.extendInfo  !== undefined ">
							            <span  v-for = "item in datas.extendInfo.detailImages"  class = "mr6">
										   <img  :src="item.url"  class="litterImg">
										</span> 
						    </div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr">
								<label>SPEC文档地址：</label>
							</div>
							<div class="col-xs-10 c5e word-break-all">
								    <div  v-for="(ele,index) in datas.documents" v-if="ele.type == 'datasheets'" >
								       <p  v-for="(el,index) in ele.attaches" >
								         <a target="_blank"  v-html ='el.url' :href='el.url'  ></a>
								       </p>
								 	</div>
								 	<div  v-for="(ele,index) in datas.documents" v-if="ele.type == 'dataSheet'" >
								       <p>
								         <a target="_blank"  v-html ='ele.url' :href='ele.url'  ></a>
								       </p>
								 	</div>
							</div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr">
								<label>RoHS标准：</label>
							</div>
							<div class="col-xs-10 c5e">
							      <span  v-if="datas.rohs == true " >符合</span >
							       <span  v-if="datas.rohs == false " >不符合</span >
							       <span  v-if="datas.rohs == undefined " >未知</span >
								 
							</div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr ">
								<label>物料描述：</label>
							</div>
							 <div class="col-xs-10 c5e word-break-all">
							   <span class="" v-html="datas.description ?datas.description:'--'"></span>
							</div>
						</div>
							<div class="row mt30">
							<div class="col-xs-2 tr ">
								<label>物料名称：</label>
							</div>
							 <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  !== undefined ">
							   <span class="" v-html="datas.extendInfo.materialName ?datas.extendInfo.materialName:'--'"></span>
							 </div>
							 <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  == undefined ">
							   <span >--</span>
							 </div>
						</div>
						<div class="row mt30">
							<div class="col-xs-2 tr ">
								<label>促销词：</label>
							</div>
							 <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  !== undefined ">
							   <span class="" v-html="datas.extendInfo.promotionWord ?datas.extendInfo.promotionWord:'--'"></span>
							</div>
							  <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  == undefined ">
							   <span >--</span>
							 </div>
						</div>
					   <div class="row mt30">
							<div class="col-xs-2 tr ">
								<label>物料详情：</label>
							</div>
							 <div class="col-xs-10 c5e word-break-all"  v-if="datas.extendInfo  !== undefined ">
							   <span class="" v-html="datas.extendInfo.materialDetail ?datas.extendInfo.materialDetail:'--'"></span>
							</div>
							 <div class="col-xs-10 c5e word-break-all"   v-if="datas.extendInfo  == undefined ">
							   <span >--</span>
							 </div>
						</div>
						<div class="row mt30  mb30">
							<div class="col-xs-2 tr ">
								<label>状态：</label>
							</div>
							<div class="col-xs-10 c5e">
								<span v-html="datas.status ? '失效':'有效'"></span>
							</div>
						</div>
						<div class="row mt30  mb30">
							<div class="col-xs-2 tr ">
								<label>是否受控：</label>
							</div>
							<div class="col-xs-10 c5e">
								<span v-html="name" ></span >
							</div>
						</div>
						
						
						<div class="row mt30">
							<div class="col-xs-2 tr">
								<label>操作日志：</label>
							</div>
							<div class="col-xs-10 c5e" v-if="datas.operateDate">
								<span >--</span>
							</div> 
							<div class="col-xs-10 c5e"  v-if="!datas.operateDate" >
									<p  v-for=  "(el,index) in operateDate">
									  <span >{{el.date}}</span>
									  <span class="ml10">{{el.actionDesc}}</span>
	                  		        </p> 
							</div>
						 </div>
						<!-- <div class="row mt30">
							<div class="col-xs-2 tr">
								<label></label>
							</div>
							<div class="col-xs-10 c5e">
								<span class="return_btn">返回</span>
							</div>
						</div> -->
					</div>
					     
				</div>
				<div class="col-xs-6">
					<div class="row">
						<div class="col-xs-2 pl0">
							<label>物料参数：</label>
						</div>
						<div class="col-xs-10 c5e">
							<table class="table">
								<tbody>
									 <tr v-for="ele in datas.parameters">
										<td class="name">{{ele.name}}</td>
										<td class="value">{{ele.value}}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				 
			</div>
			
		</section>
	</div>
	<!-- Main content end -->
    </form>
    
    <form name="createTariff" autocomplete="off" id="createTariff" onsubmit="return false;" class="form-horizontal" novalidate="novalidate" v-show="tabValue === 'tariff'">
	   	
	   	<div class="row mt30  mb30">
			<div class="col-xs-1 tr ">
				<label>商检/关税：</label>
			</div>
			<div class="col-xs-10 c5e">     
				<span v-html="controlledName" ></span> 
			</div>
		</div>

	</form>
    
  </section>
</div>

</div>



<!---footer  start   ---->
<jsp:directive.include file="../include/lemon/footer.jsp" />
<!---footer  end   ---->
<script type="text/javascript" src="${ctx}/js/oss/common.js"></script>
<script type="text/javascript" src="${ctx}/assets/lemon/1.0.0/lemon-multi-file.js"></script>

<script type="text/javascript">
var	 vm ;
var  id = getQueryString("id");    //活动id

$(function() {
vm = new Vue({
        el: '#extension_edit',
        data: {
            datas:{
            	  extendInfo:{
                  	"detailImages": [],
                  	    "id": id,
                  	   // "isControlMaterial": "",   //是否限制物料           
                  	    "materialDetail": "",      //物料详情  
                  	    "materialName": "",        //物料名称    
                  	    "promotionWord": ""        //促销词   
                  }, 
            },
            name :'--',
            controlledName:'--',  // 商检/税收
            attachUrl1:'',     	  //图片上传路径
            cur: 1,
            curPage: 1,
            operateDate:[],
            tabValue:'baseData',
	   },
       created: function(){
       	var that = this;
       	//查询活动详情
       	syncData(
       		ykyUrl.product + "/v1/products/stand/"+id ,
       			'GET',
       			null,
       			function(res,err) {
						if(res){     //初始化 
							that.datas = res;
							 that.extendInfo = res.extendInfo ;
							 that.imageurl(that.datas.images);

							if(res.restrictMaterialType){
								that.controlMaterial(res.restrictMaterialType);
							}
						}
       		},true);
       	var operaData = {
				  actions:"Material Modify",
				  applicationCodes:"PRODUCT",
				  actionRst:"SUCCEEDED",
				  page:"1",
				  size:"10"
	            }
		  syncData( //不通过审核 
	  		ykyUrl.party + "/v1/audit?actionId=" + id,
	  			'GET',
	  			operaData,
	  			function(res,err) {
	  				that.operateDate = res.list;
		            }
	         ) 
       },
        methods: {
           controlMaterial:function(str){
	        	var text =  '--';
	       	    //  var name = ['受控','关税','商检'];   *F:受控；T：关税；I：商检
	       	   	var list =[];
	       	    if(str == 'N'){
	       	    	vm.name = '否';
	       	    	text = '否';
			    }else{
			       var strlist = str.split('-');
			    	$.each(strlist,function(ind,el){
			    		if(el == 'F'){
			    			vm.name = '受控';
			    		}
			    		if(el == 'T'){
			    			list.push('关税');
			    		}
			    		if(el == 'I'){
			    			list.push('商检');
			    		}
			    	})
			    	if(list.length > 0){
			    		text = list.join('、');
			    	}else{
			    		text = '否';
			    	}
			    }
	       	   vm.controlledName = text;
           },
          imageurl:function(data){
        	  if(!data.length){/* 图片集合为空 */
    	    		//return true;
    	    	}
    	    	var hasStandard = false   ;
    	    	$.each(data,function(index,ele){
    	    		if( ele.type == 'standard'){
    	    			hasStandard = true   ;
    	    			vm.datas.imageurl = ele.url;
    	    		}
    	    	})
    	    	
    	    	
    	    	if(!hasStandard || vm.datas.imageurl == ''){
    	    		vm.datas.imageurl = ykyUrl._this +'/images/defaultImg01.jpg';
    	    	}
    	    	
   	    	},
   	   		//获取操作日志接口 
		  operateDate:function(){
			  
			  var operaData = {
					  actions:"Material Modify",
					  applicationCodes:"PRODUCT",
					  actionRst:"SUCCEEDED",
					  page:"1",
					  size:"10"
			  }
			  syncData( //不通过审核 
        		ykyUrl.party + "/v1/audit?actionId=" + id,
        			'GET',
        			operaData,
        			function(res,err) {
        				vm.datas.operateDate = res.list;
		            }
		        )
		  }
        }
    });

	$(".return_btn").on("click",function(){
		history.go(-1);
	});

});




</script>
</body>
</html>
