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
<script src="${ctx}/js/lib/jquery-1.11.3.min.js"></script>
  <c:import url ="/WEB-INF/jsp/include/main/constants.jsp"/>
  <title>运营后台</title>

 <style type="text/css">
 	.condition_inquery{background-color:#c11f2e;color:#fff;padding:10px 10px;font-size:14px;border-radius:3px;}
	.col_bt_time input{width:37%;}
  	.col_bt label,.col_bt_time label{text-align:right;}
  	.col_input_2{width:69%;}
  	.col_input_3{width:59%;}
  	.col_input_4{width:49%;}
  	.mt25{margin-top:25px;}
 </style>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

	<!---index header  start   ---->
	<jsp:directive.include file="../include/main/header.jsp" />
	<!---index header  end   ---->
	 <!---index nav  start   ---->
	<jsp:directive.include file="../include/main/menu.jsp" />
	<!---index nav  end   ---->
	  
	  <!-- 右边内容显示 -->
	  <div class="content-wrapper ms-controller"  ms-controller=list>
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<a href="${ctx}/goods.htm?type=0" class="header_tab">待审核</a>
			<a href="${ctx}/goods.htm?type=1" class="header_tab">已通过审核</a>
		</section>
	    <!-- Main content -->
		<section class="content container-fluid">
			<div class="row mt20" ms-controller="search" id=searchFrom>
				<div class="col-xs-10"  style="border-right:1px solid #e2e2e2">
						<div class="col-xs-2 col_bt pl0 pr0 mb10">
							<label>商品编码：</label>						
							<input class="col_input_4" style="width:55%;" type="text" name="id" data-duplex="@parameter.id" ms-attr="{value:@parameter.id}" >
						</div>
						<div class="col-xs-2 col_bt pr0 mb10">	
							<label>品牌：</label>						
							<input class="col_input_2" type="text" name="brand" data-duplex="@parameter.brand" ms-attr="{value:@parameter.brand}">
						</div>	
						<div class="col-xs-2 col_bt pr0 mb10">
							<label>型号：</label>						
							<input class="col_input_2" type="text" name="materialPartNo" data-duplex="@parameter.materialPartNo" ms-attr="{value:@parameter.materialPartNo}">	
						</div>	
						<div class="col-xs-6 col_bt_time pr0 mb10">
							<label class="">时间范围：</label>
							<span class="rel">
								<input id="beginTime" name="beginDateStr" class="y_main_head_input ovh" style="padding-right:46px;" type="text" data-duplex="@parameter.beginDateStr" ms-attr="{value:@parameter.beginDateStr}">
				                <span class="y_timer"><i class="cc1 icon-calendar"></i></span>
							</span>
							<span>至</span>
							<span class="rel">
				                <input id="endTime" name="endDateStr" class="y_main_head_input ml10 ovh" style="padding-right:46px;" type="text" data-duplex="@parameter.endDateStr" ms-attr="{value:@parameter.endDateStr}">
				                  <span class="y_timer"><i class="cc2 icon-calendar"></i></span>
							</span>
							<span class="content_top_close" onclick="showDiv();"></span>								
						</div>
						<div class="col-xs-2 col_bt pl0 pr0">
							<label style="width:60px">主标题：</label>
							<input class="col_input_3" style="width:55%;" type="text" name="name" data-duplex="@parameter.name" ms-attr="{value:@parameter.name}">
						</div>
						<div class="col-xs-2 col_bt pr0">
							<label>提交公司：</label>
							<input class="col_input_4" type="text" name="enterpriseName" data-duplex="@parameter.enterpriseName" ms-attr="{value:@parameter.enterpriseName}">
						</div>		
						<div class="col-xs-2 col_bt pr0">
							<label>提交人：</label>
							<input class="col_input_3" type="text" name="createName" data-duplex="@parameter.createName" ms-attr="{value:@parameter.createName}">
						</div>	
						<div class="col-xs-6 col_bt pr0">
							<label>类型：</label>
							<select id="province" name="category1Id" ms-duplex="@firstSelected">
								<option value="">全部</option>	
								<option  ms-for="el in @first" ms-attr="{value:el.cateId}" >{{el.cateName}}</option>
							</select>
							<select id="city" name="category2Id"  ms-duplex="@secondSelected">
								<option value="">全部</option>
								<option  ms-for="el in @second" ms-attr="{value:el.cateId}" >{{el.cateName}}</option>
							</select>
							<select id="town" name="category3Id" ms-duplex="@thirdSelected">
								<option value="">全部</option>
								<option  ms-for="el in @third" ms-attr="{value:el.cateId}" >{{el.cateName}}</option>					
							</select>
						</div>
				</div>
				
				<div class="col-xs-2 mt25">
					<a href="javascript:;" class="condition_inquery" ms-click="@goSearch()">
						<span ><i class="icon-sousuo mr5"></i>查询</span>
					</a>
				</div>				
				
			</div>
			
			<!-- 表格 -->
			<div class="row mt20">
				<div class="col-xs-12" id="result_table">
					<table class="table">
						<thead>
							<tr>
								<th ms-if="@audited!=1" width="30px">
									<input type="checkbox"  id="checkbox_a1" ms-duplex-checked="@allchecked" 
                           data-duplex-changed="@checkAll" class="chk_1"/>
									<label for="checkbox_a1"></label>
								</th>
								<th width="50px">序号</th>
								<th width="9%">商品编号</th>
								<th width="7%">品牌</th>
								<th width="8%">型号</th>
								<th width="13%">主标题</th> 
								<th width="8%">类型</th>
								<th width="101px">提交公司</th>
								<th width="140px">提交人</th>
								<th width="120px">提交时间</th>
								<th width="140px">操作</th>
							</tr>
						</thead>
						<tbody >
							<tr :if="@isfirst || !@list.list.length" >
								<td colspan="11" style="padding:20px" id="loadingData" ms-html="@isLoading">数据加载中</td>
							</tr>
							<tr ms-for="($index, el) in @list.list">
								<td ms-if="@audited!=1">
									<input type="checkbox" ms-duplex-checked="el.checked" data-duplex-changed="@checkOne" ms-attr="{id:'checkbox_'+$index}"  class="chk_1"/>
									<label ms-attr="{for:'checkbox_'+$index}" ></label>
								</td>
								<td>{{$index + 1}}</td>
								<td ms-attr="{title:el.id}">{{el.id}}</td>
								<td ms-attr="{title:el.brand}">{{el.brand}}</td>
								<td ms-attr="{title:el.materialPartNo}">{{el.materialPartNo}}</td>
								<td ms-attr="{title:el.name?el.name:''}">{{el.name?el.name:''}}</td>
								<td ms-attr="{title:el.category3Name}" >{{el.category3Name}}</td>
								<td ms-attr="{title:el.enterpriseName}">{{el.enterpriseName}}</td>
								<td ms-attr="{title:el.createName?el.createName:el.mail}">{{el.createName?el.createName:el.mail}}</td>
								<td ms-attr="{title:el.lastUpdateDate}">{{el.lastUpdateDate}}</td>
								<td>
									<a ms-attr="{href:'${ctx}/goods.htm?action=detail&id='+el.id}" target="_blank">详情</a>
									<a href="javascript:;" ms-if="@audited!=1" ms-click="@auditClick(el, $event,el.id)">通过</a>
									<a href="javascript:;" ms-if="@audited!=1" ms-click="@unAuditClick(el, $event,el.id)">不通过</a>
								</td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
			
		</section>
		<!-- /.content -->
		
		<div>
			<a href="javascript:;" ms-if="@audited!=1"  class="condition_inquery l ml30">
				<span ms-click="@allAuditClick()">批量通过</span>
			</a>
			<!-- 分页 -->
			<div id="pager" class="r">
				  <div id="kkpager"></div>
			</div>
			<input type="hidden" value="" id="total">
			<input type="hidden" value="" id="pageSize">
		</div>		
	  </div>	  
	   
	<script type="text/javascript">
		var selectCatid = "286,287";
	</script>
	<!---footer  start   ---->
	<jsp:directive.include file="../include/main/footer.jsp" />
	<!---footer  end   ---->

</div>	    
<!-- ./wrapper -->
	    <script>
 
	    /* search */
	    
	    var getParameters = function(){
        	var arr = ["id","brand","materialPartNo","name","beginDateStr","endDateStr","enterpriseName","createName","category1Id","category2Id","category3Id"],
    		par ={};
    	
	    	for(var i=0; i< arr.length; i++){
	    		par[arr[i]] = decodeURI(getQueryString(arr[i]));
	    	}
	    	return par;
	    }
	    
	    var search = avalon.define({
	        $id: "search",
	        parameter: getParameters(),
	        catList:[],
	        first:[],
	 	    second:[],
	 	    third:[],
	 	    firstSelected: "",
            secondSelected: "",
            thirdSelected: "",            
            /* curValue:function(){
            	var curSelect = ["category1Id","category2Id","category3Id"],
                parList={};
            	for(var i=0;i<curSelect.length;i++){
            		if(!getQueryString(curSelect[i])){           		
                		search.firstSelect = "";
                		search.secondSelected = "";
                		search.thirdSelected = "";
                	}else{
                		search.firstSelect = getQueryString(curSelect[0])
                		search.secondSelected = getQueryString(curSelect[1])
                		search.thirdSelected = getQueryString(curSelect[2])                		
                	}
            	}            	
            }, */
	        goSearch:function(){
	        	var wrap = $("#searchFrom");
	        	var inputList = wrap.find("input"),
	        		selectList = wrap.find("select"),
	        		parStr = "?",
	        		par = getQueryString("type"),
	        		parObj = {};
	        	
	        	if(par)
	        		parObj["type"] = par;
	        	
	        	function toUrl(pArr){
	        		for(var k in pArr){
	        			console.log(k)
	        			parStr += k + "=" + pArr[k] +"&";
	        		}
	        		
	        	}
	        	
	        	$.each(inputList,function(i,input){
	        		var val = input.value;
	        		if(val !== ""){
	        			parObj[input.name] = encodeURI(input.value);
	        		}
	        	})
	        	console.log(selectList)
	        	$.each(selectList,function(i,select){
	        		var val = "",
	        			selectObj = $(select,"option:selected");
	        		console.log(selectObj.val());
	        		if(selectObj.val()){
	        			parObj[select.name] = select.value;	        			
	        		}
	        	})
	        	
	        	toUrl(parObj);
	        	parStr = parStr.substring(0,parStr.length-1);

	        	window.location.href=window.location.pathname+parStr;
	        	
	        }

	    })
	    /* search.curValue(); */
	    

	    /* $.aAjax({
	         url: "/ictrade-admin-web/rest/menu/operation",
	         data:{}, //去掉数据模型中的所有函数
	         success: function(data) {
	        	 menus.list=data;
	         }
	     }) */
	     
	     /* search end */
	
	 
	 var showList = avalon.define({
 	    $id: "list",
 	    list: [],
 	   	isLoading:"数据加载中...",
 	   	isfirst:true,
 	    allchecked: false,
 	   	audited:getQueryString("type"),
        checkAll: function (e) {
           var checked = e.target.checked;
           showList.list.list.forEach(function (el) {
               el.checked = checked
           })
        },
        checkOne: function (e) {
           var checked = e.target.checked
           if (checked === false) {
        	   showList.allchecked = false
           } else {//avalon已经为数组添加了ecma262v5的一些新方法
        	   showList.allchecked = showList.list.list.every(function (el) {
                   return el.checked
               })
           }
        },
 	    project : ykyUrl._this,
 	    auditUrl : ykyUrl.admin+"/rest/goods/checkBasicInfo",
 	    auditFun : function(id,type,remark,load){
 	    	var _this = this;
 	    	$.aAjax({
 	  	     url: _this.auditUrl,
 	  	     type:"GET",
 	  	     data:{type : type,id : id, remark : remark}, //去掉数据模型中的所有函数
 	  	     success: function(data) {
 	  	    	 //console.log(data);
 	  	    	 layer.close(load);
 	  	    	 if(data.code == 200){
	 	  	    	layer.msg(data.message, {time: 2000});
	 	  	    	setTimeout(function(){
	 	  	    		location.reload()
	 	  	    	},2000);
 	  	    	 }else{
 	  	    		layer.alert('<br/>' + data.message,{skin: "up_skin_class",area: ['300px', '210px']}); 
 	  	    	 }
 	  	     },
 	  	     error:function(){
 	  	    	layer.closeAll('loading');
 	  	    	layer.alert('系统错误，审批失败，请联系系统管理员',{skin: "up_skin_class",area: ['300px', '210px']});
 	  	     }
 	  	 })
 	    },
 	    auditClick:function(el,e,id){
 	    	var _this = this,
 	    	auditType = 1;
 	    	layer.confirm("<img class='y_prompt_box'  src='${ctx}/js/lib/layer/skin/default/y_ask.png'></i><div class='y_prompt_content' ><span class='sp1'>确认通过吗？</span><span>确认通过后，此商品将流转至运营部</span></div>", {
				offset: "auto",
				btn: ['确      认', '取      消'], //按钮
				title: " ",
				area: ['450px', '280px'],
				move: false,
				skin: "up_skin_class",
				yes:function(){
					//console.log(_this.project);
					var index = layer.load();
					_this.auditFun(id,auditType,"",index);
				}

			})
 	    },
 	    unAuditClick:function(el,e,id){
 	    	var _this = this,
 	    	auditType = 2;
	    	layer.confirm("<div class='y_unpass_tlt'>确认不通过吗？</div><div class='warn'><i class='icon-plaint'></i>理由不能为空</div><textarea class='y_unpass_content unpass_reason' placeHolder='请在此输入不通过的理由' maxlength='200'  onkeyup='calcWordsCount()'></textarea>", {
				offset: "auto",
				btn: ['确      认', '取      消'], //按钮
				title: " ",
				area: ['480px', '340px'],
				move: false,
				skin: "up_skin_class",
				yes:function(){
					if(!$.trim($(".unpass_reason").val())){						
						$(".warn").show();													
						return false;
					}else{
						var index = layer.load();
						_this.auditFun(id,auditType,$(".unpass_reason").val(),index);
					}
				}

			})
	    },
	    allAuditClick:function(){
	    	var idList="";	    	
	    	showList.list.list.forEach(function (el) {
	           if(el.checked)
	        	   idList+=el.id+","
	        })
	    	idList = idList.slice(0,-1);
	    	if(idList==""){
	    		return false;
	    	}
	    	var _this = this,
	    	auditType = 3;
	    	layer.confirm("<img class='y_prompt_box'  src='${ctx}/js/lib/layer/skin/default/y_ask.png'></i><div class='y_prompt_content' ><span class='sp1'>确认通过吗？</span><span>确认通过后，此商品将流转至运营部</span></div>",{
	    		offset: "auto",
				btn: ['确      认', '取      消'], //按钮
				title: "审核",
				area: ['450px', '280px'],
				move: false,
				skin: "up_skin_class",
				yes:function(){
					//console.log(_this.project);
					var index = layer.load();
					_this.auditFun(idList,auditType,"",index);
				}
	    	})
	    }
 	 })
 	 
 	 
 	 var queryType = getQueryString("type"),
 	 	 queryPage = getQueryString("page"),
  		listType = queryType ? queryType : 0,
		listPage = queryPage ? queryPage : 1,
		listUrl = ykyUrl.admin+"/rest/goods/list",
		typeUrl = ykyUrl.admin+"/rest/goods/toolsClassify";
 	 
	var setTab = function (index){
		var tabs = $(".content-header .header_tab"),
			status = {s0:0,s1:1};
		
		if(tabs.length)
			tabs.eq(status["s"+index]).addClass("pitch_tab");
	}
	
	setTab(listType);
	/* 请求商品列表数据 */
 	 $.aAjax({
	     url: listUrl+window.location.search,
	     //data:{type : listType,page:listPage}, //去掉数据模型中的所有函数
	     success: function(data) {
	    	 //var ck = {checked: false};
	    	 for(var i = 0; i< data.list.length; i++){
	    		 data.list[i].checked = false;
	    	 }
	    	 if( data.list.length == 0)
		    	 	showList.isLoading = "暂无数据";
	    	 
	    	 showList.list=data;
	    	 showList.isfirst = false;
	    	 

	    	 setPageOption(data);
	    	 loadPage(window.location.pathname+GetRequestWithout(window.location.search,"page"));
	    	 
	     }
	 })
	 /* 请求类型数据 */
	 
	 $.aAjax({
		 url: typeUrl,
		 success:function(data){
			 $.each(data,function(i,e){
				 if(e.cateId==99958){					 
					 search.catList = e;
					 setTimeout(function(){
						 setCatdefault()
					 }, 300);
					 /* search.first.push(e) */
				 }
			 })	
		 }
	 })
	 
	 function setCatdefault(e){
        var e = search.catList;
        if(getQueryString("category1Id") == e.cateId){
        	search.first.push(e);
            search.firstSelected = e.cateId;
            search.second= e.subList;
            $.each(search.second,function(i,ele){
                if(getQueryString("category2Id") == ele.cateId){
                    search.secondSelected = ele.cateId;
                    $.each(search.second[i].subList,function(index,data){
                        if(getQueryString("category3Id") == data.cateId){
                            search.third = search.second[i].subList;
                            search.thirdSelected = data.cateId;
                        }
                    })
                }
            })
        }else{
        	search.first.push(e);
            search.firstSelected = '';
        }
        $("select").selectmenu("refresh");
    }
	
	function calcWordsCount(){
		var valueLength = $(".unpass_reason")[0].value.length;		
		if(valueLength){
			$(".warn").hide();
		}else{
			$(".warn").show();
		}
	}
	 
	 $("#province").on("change",function(){
		 search.firstSelected = $(this, "option:selected").val();
		 $("#city").selectmenu("refresh")
	 })
	
	 	
	  $("#city").on("change",function(){
		 search.secondSelected = $(this, "option:selected").val();
		 $("#town").selectmenu("refresh")
	 })
	 
	 search.$watch("firstSelected", function (a) {
		 search.second = search.catList.subList;
		 search.secondSelected='';
     })
     search.$watch("secondSelected", function (a) {
    	 $.each(search.catList.subList,function(i,sub){
    		 if(sub.cateId == a)
    			 search.third = sub.subList;
    		 	 search.thirdSelected=''
    	 })
     })

        </script>


<script type="text/javascript">
  	$(function(){
  		//日期控件
  		var start = {
				elem: '#beginTime',
				format: 'YYYY-MM-DD hh:mm:ss',
				// min: laydate.now(),    //设定最小日期为当前日期
				istime: true, //是否显示时间
				istoday: false,
				isclear: false, //是否显示清空
				//选择好日期的回调
				choose: function(dates) {
					$(this.elem).trigger("blur");
					var startTime = $('#beginTime').val();
					var endTime = $('#endTime').val();
					var arr1 = startTime.split('-');
					var sdate = new Date(arr1[0], parseInt(arr1[1] - 1), arr1[2]);
					var arr2 = endTime.split('-');
					var edate = new Date(arr2[0], parseInt(arr2[1] - 1), arr2[2]);
					if(sdate > edate) {
						layer.tips('开始时间不能大于结束时间', '#beginTime', {
							tips: 1,
							time: 1000,
						});
						$('#beginTime').val('');
					};
				}
			};
			var end = {
				elem: '#endTime',
				format: 'YYYY-MM-DD hh:mm:ss',
				// min: laydate.now(),    //设定最小日期为当前日期
				istime: true, //是否显示时间
				istoday: false,
				isclear: false, //是否显示清空
				//选择好日期的回调
				choose: function(dates) {
					$(this.elem).trigger("blur");
					var startTime = $('#beginTime').val();
					var endTime = $('#endTime').val();
					var arr1 = startTime.split('-');
					var sdate = new Date(arr1[0], parseInt(arr1[1] - 1), arr1[2]);
					var arr2 = endTime.split('-');
					var edate = new Date(arr2[0], parseInt(arr2[1] - 1), arr2[2]);
					if(sdate > edate) {
						layer.tips('开始时间不能大于结束时间', '#endTime', {
							tips: 1,
							time: 1000,
						});
						$('#endTime').val('');
					};
				}
			};
  		$("#beginTime").on("click", function() {
			laydate(start);
		});
		$(".cc1").on("click", function() {
			laydate(start);
		});
		$("#endTime").on("click", function() {
			laydate(end);
		});
		$(".cc2").on("click", function() {
			laydate(end);
		});
  		
  		/*弹框 ： 通过，不通过，批量通过 */
		/* $('.adopt').on('click', function() {

			layer.confirm("<img class='y_prompt_box'  src='../../js/lib/laydate/skins/default/y_ask.png'></i><div class='y_prompt_content' ><span class='sp1'>确认通过吗？</span><span>确认通过后，此商品将流转至运营部</span></div>", {
				offset: "auto",
				btn: ['确      认', '取      消'], //按钮
				title: "删除确认",
				area: ['450px', '280px'],
				move: false,
				skin: "up_skin_class",

			})
		}); */
		

  		
  		var url = ykyUrl.main+"/myEnterpriseMaterial.htm?status=1&condition=&size=20";
  		//加载分页
  		//loadPage(url);
  		
  		$("#province").selectmenu({
  			change: function( event, ui ) {
  				$(this).trigger("change")
  			}
  		})
  		.selectmenu( "menuWidget" )
  	    .addClass( "overflow" );
  		$("#province-button")[0].style.marginLeft="0px";
  		
  		$("#city").selectmenu({
  			change: function( event, ui ) {
  				$(this).trigger("change")
  			}
  		})
  		.selectmenu( "menuWidget" )
  	    .addClass( "overflow" );
  		
  		
  		$("#town").selectmenu()
  		.selectmenu( "menuWidget" )
  	    .addClass( "overflow" );
  	});
  	
  	
  	
</script>

</body>
</html>
