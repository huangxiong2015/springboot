/**
 * Created couponStatisticsDetail.js by zr.xieyuanpeng@yikuyi.com on 2017年11月13日.
 */

var gridArrayExact = [                //定向
         			{
        				"key": "index", 
        				"name": "账号",
        				"width":"60px",
        				"align":"center"
        			},
        			{
        				"type": "text",
        				"key": "account", 
        				"name": "账号",
        				"align":"center"
        			},
        			{
        				"type": "text",
        				"key": "offlinePartyName", 
        				"name": "操作员",
        				"align":"center"
        			},
        			{
        				"type": "date",
        				"key": "createdDate", 
        				"name": "领卷时间",
        				"align":"center"
        			},
        			{
        				"type": "text",
        				"key": "statusId", 
        				"name": "使用状态",
        				"align":"center",
        				"callback": {
        					action: 'getUseStatus',
        					params: ['{statusId}']
        				}	
        			},
        			{
        				"keyname": "使用时间",
        				"type": "date",
        				"key": "useDate", 
        				"name": "使用时间",
        				default : '--',
        				"align":"center"
        			},
        			{
           				key : 'operation',
           				align : 'center',
           				name : '订单号',
           				render: function (createElement,params) {
                            var rowData = params.row, label = '';
                            if(rowData.orderId){
                            	var renderBtn = [
             	                                createElement('a', {
            	                                    props: {
            	                                        type: 'text'
            	                                    },
            	                                    on: {
            	                                        click: function () {
            	                                            window.open(ykyUrl._this + '/order.htm?action=detail&id=' + rowData.orderId)
            	                                        }
            	                                    }
            	                                },rowData.orderId)
            	                            ]
                            }else{
                            	var renderBtn = [
	             	                                createElement('a', {
	            	                                    props: {
	            	                                        type: 'text'
	            	                                    },
	            	                                },"--")
	            	                            ]
                            }
                            return createElement('span', renderBtn);

                        }
           			},
        			{
        				"type": "text",
        				"key": "remark", 
        				"name": "备注",
        				default : '--',
        				"align":"center"
        			}
                ];
var gridArrayPlatform = [                //平台
                  			{
                 				"key": "index", 
                 				"name": "账号",
                 				"width":"60px",
                 				"align":"center"
                 			},
                 			{
                 				"keyname": "账号",
                 				"type": "text",
                 				"key": "account", 
                 				"name": "账号",
                 				"align":"center"
                 			},
                 			{
                 				"keyname": "领卷时间",
                 				"type": "date",
                 				"key": "createdDate", 
                 				"name": "领卷时间",
                 				"align":"center"
                 			},
                 			{
                 				"keyname": "使用状态",
                 				"type": "text",
                 				"key": "statusId", 
                 				"name": "使用状态",
                 				"align":"center",
                 				"callback": {
                 					action: 'getUseStatus',
                 					params: ['{statusId}']
                 				}	
                 			},
                 			{
                 				"keyname": "使用时间",
                 				"type": "date",
                 				"key": "useDate", 
                 				"name": "使用时间",
                 				default : '--',
                 				"align":"center"
                 			},
                 			{
    	           				key : 'operation',
    	           				align : 'center',
    	           				name : '订单号',
    	           				render: function (createElement,params) {
    	                            var rowData = params.row, label = '';
    	                            if(rowData.orderId){
    	                            	var renderBtn = [
    	             	                                createElement('a', {
    	            	                                    props: {
    	            	                                        type: 'text'
    	            	                                    },
    	            	                                    on: {
    	            	                                        click: function () {
    	            	                                            window.open(ykyUrl._this + '/order.htm?action=detail&id=' + rowData.orderId)
    	            	                                        }
    	            	                                    }
    	            	                                },rowData.orderId)
    	            	                            ]
    	                            }else{
    	                            	var renderBtn = [
    		             	                                createElement('a', {
    		            	                                    props: {
    		            	                                        type: 'text'
    		            	                                    },
    		            	                                },"--")
    		            	                            ]
    	                            }
    	                            return createElement('span', renderBtn);

    	                        }
    	           			}
                         ];
var gridArrayOffline = [                //地推
            			{
           				"key": "index", 
           				"name": "账号",
           				"width":"60px",
           				"align":"center"
	           			},
	           			{
	           				"type": "text",
	           				"key": "account", 
	           				"name": "账号",
	           				"align":"center"
	           			},
	           			{
	           				"type": "text",
	           				"key": "offlinePartyName", 
	           				"name": "地推人员",
	           				"align":"center"
	           			},
	           			{
	           				"type": "date",
	           				"key": "createdDate", 
	           				"name": "领卷时间",
	           				"align":"center"
	           			},
	           			{
	           				"type": "text",
	           				"key": "statusId", 
	           				"name": "使用状态",
	           				"align":"center",
	           				"callback": {
	           					action: 'getUseStatus',
	           					params: ['{statusId}']
	           				}	
	           			},
	           			{
	           				"type": "date",
	           				"key": "useDate", 
	           				"name": "使用时间",
	           				default : '--',
	           				"align":"center"
	           			},
	           			{
	           				key : 'operation',
	           				align : 'center',
	           				name : '订单号',
	           				render: function (createElement,params) {
	                            var rowData = params.row, label = '';
	                            if(rowData.orderId){
	                            	var renderBtn = [
	             	                                createElement('a', {
	            	                                    props: {
	            	                                        type: 'text', 
	            	                                    },
	            	                                    attrs:{
	            	                                    	href:ykyUrl._this+'/order.htm?action=detail&id='+rowData.orderId
	            	                                    },
	            	                                },rowData.orderId)
	            	                            ]
	                            }else{
	                            	var renderBtn = [
		             	                                createElement('a', {
		            	                                    props: {
		            	                                        type: 'text'
		            	                                    },
		            	                                },"--")
		            	                            ]
	                            }
	                            return createElement('span', renderBtn);

	                        }
	           			}
	           		 ];
var vm = new Vue({
	el:"#couponStatistics",
	data:{
		url: ykyUrl.pay+"/v1/coupons/"+getQueryString("id")+"/statsDetailsList",  //访问数据接口
        queryParams: {                //搜索字段及分页字段
        	defaultStatus : false,     //判断是否修改搜索条件
            size : 10, //分页参数
			page : 1, //当前页
        },                            //请求接口参数
        gridColumns: [],
        pageflag : true, //是否显示分页
		refresh : false, //重载 
		showTotal: true,
		totalStatistics:{
			
		},
        couponId:getQueryString("id")
	},
	beforeCreate:function(){
		var id = getQueryString("id");
		var $this = this;
		httpGet($this,ykyUrl.pay+"/v1/coupons/detail/"+id,null,function(res, err){
			if(res){
				$this.totalStatistics = res;
				if("PLATFORM_PROMO" == res.couponCate){
					$this.gridColumns = gridArrayPlatform;
				}else if("OFFLINE_PROMO" == res.couponCate){
					$this.gridColumns = gridArrayOffline;
				}else if("EXACT_PROMO" == res.couponCate){
					$this.gridColumns = gridArrayExact;
				}
			}
		});
	},
	methods:{
		/*搜索方法*/
		search: function () {        //该方法表单提交时绑定，如：<form id="seachForm" @submit.prevent="onSearch">
            var that = this;
            var qModel = $('#seachForm').serializeObject(); //获取表单中字段值，"seachForm"为form表单ID
            for(var item in qModel){
            	if(qModel[item]!=""){
            		 that.queryParams[item] = qModel[item];  //改变queryParams参数
            	}
            	if(qModel[item]==""){
            		delete that.queryParams[item];
            	}
            }
            vm.queryParams.page = 1;
            that.queryParams.defaultStatus = !that.queryParams.defaultStatus;//判断是否修改搜索条件
        },
        exportRecord:function(exportType){
        	var that = this;
	   		var data = {};
	   		
	   		var qModel = $('#seachForm').serializeObject(); //获取表单中字段值，"seachForm"为form表单ID
            for(var item in qModel){
            	if(qModel[item]!=""){
            		 data[item] =  qModel[item];
            	}
            }    
	   		data.couponId = that.couponId;
	   		if(exportType=="all"){
	   			data.export="ALL";
	   		}else if(exportType=="thisPage"){
	   			data.page = that.queryParams.page?that.queryParams.page:1;
		   		data.size = that.queryParams.size;
	   		}	   		
	   		
	   		$.aAjax({
	   			url:ykyUrl.pay + '/v1/coupons/exportCouponList',
	   			type:'POST',
	   			contentType:'application/json',
	   			data:JSON.stringify(data),
	   			success:function(data){
	   				location.href=data;
	   			},
	   			error:function(data){
	   				console.log("error");
	   			}
	   		})
	   	}
	}
})

function getUseStatus(index, params){
	var status = '';
	if(params[0] === 'UNUSE'){
		status = '未使用'
	}else if (params[0] === 'USED'){
		status = '已使用'
	}else if(params[0] === 'EXPIRE'){
		status = '已过期'
	}
	return status;
}

function init(){
	//领卷时间
	$('#createDate .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true
	});
	
	//使用时间
	$('#useDate .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true 
	});
}

init();