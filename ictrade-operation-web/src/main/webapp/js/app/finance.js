var vm = '';
$(function(){
	function getFormData () {
	    var searchingFlag = sessionStorage.getItem('searchingFlag');
		var searchString = sessionStorage.getItem('searching');
        var result = {};

		if (searchString && searchingFlag) {
			try {
                result = JSON.parse(searchString);
			} catch (e) {}
		}

        sessionStorage.removeItem('searchingFlag')
        // sessionStorage.removeItem('searching')
        if (!searchingFlag) {
            sessionStorage.removeItem('searching')
        }
		return result;
	}
	
      vm = new Vue({
        el: '#finance-list',
        data: function(){
        	return{
                   formData: {                   
                    id:'seachForm',
                    data: getFormData(),            //初始化数据
                    columnCount: 3,
                    fields:[            //配置控件
                        {
                            keyname: "订单编号",           //控件文本
                            type: "text",             //控件类型
                            key: "orderNumber",          //控件名
                            name: "orderNumber"         //控件name
                        }, {
                            keyname: "状态",           //控件文本
                            type: "select",             //控件类型
                            key: "status",          //控件名
                            name: "status",         //控件name
                            options: [{
                                'value': '',
                                text: '全部'
                            },{
                            	'value': 'WAIT_SHIP',
                            	text: "待发货"
                			},{
                				'value': 'WAIT_RECEIVE',
                				text: "已发货"
                			},	{
                				'value': 'COMPLETED',
                				text: "交易完成"
                			}],
                            optionId:"value", 
                            optionName:"text"
                        },{
                            "keyname": "总价",
                            "type": "range",           //区间类型
                            "startkey": "totalPriceBegin",   //star id
                            "endkey": "totalPriceEnd",          //end id
                            "startname": "totalPriceBegin",  //star name
                            "endname": "totalPriceEnd"          //end name
                        },
                        {
                            keyname: "创建时间",
                            type: "daterange",        //时间区间类型
                            startkey: "createDateBegin",  //star id
                            endkey: "createDateEnd",        //end id
                            startname: "createDateBegin",    //star name
                            endname: "createDateEnd"         //end name
                        },{
                            keyname: "支付号",           //控件文本
                            type: "text",             //控件类型
                            key: "paymentNo",          //控件名
                            name: "paymentNo",         //控件name                        
                        },{
                            keyname: "账号",           //控件文本
                            type: "text",             //控件类型
                            key: "account",          //控件名
                            name: "account",         //控件name                        
                        },{
                            keyname: "支付平台",           //控件文本
                            type: "select",             //控件类型
                            key: "payment",          //控件名
                            name: "payment",         //控件name 
                            options: [{
            					"value": '',
           					    text: "全部"       					    	
                            },{
            					"value": 'WECHATPAY',
            					 text: "微信"
            				},{
            					"value": 'ALIPAY',
            					text: "支付宝"
            				},{
            					"value": 'UNIONPAY_B2B',
            					text: "企业网银"   //"企业网银支付"
            				},{
            					"value": 'UNIONPAY_B2C',
            					text: "银联"   //"银联支付"
            				},{
            					"value": 'EXT_OFFLINE',
            					text: "银行汇款"
            				},{
                                "value": 'CREDITPAY',
                                text: "账期支付"
							}],
                            optionId:"value", 
                            optionName:"text"
                        },{
                            keyname: "币种",           //控件文本
                            type: "select",             //控件类型
                            key: "currency",          //控件名
                            name: "currency",         //控件name 
                            options: [
                                {
	            					"value": '',
	            					"text": "全部"
	            				},{
	            					"value": 'CNY',
	            					"text": "RMB"
	            				},{
	            					"value": 'USD',
	            					"text": "USD"   
	            				}
            				],
                            optionId:"value", 
                            optionName:"text"
                        },{
                            keyname: "财务状态",           //控件文本
                            type: "select",             //控件类型
                            key: "finStatus",          //控件名
                            name: "finStatus",         //控件name                          
                            options: [{
            					"value": '',
            					 text: "全部"
            				},{
            					"value": 'N',
            					text: "待确认"
            				},{
            					"value": 'Y',
            					 text: "已确认"   
            				}],
                            optionId:"value", 
                            optionName:"text"
                        },{
                            keyname: "支付时间",
                            type: "daterange",        //时间区间类型
                            startkey: "paymentBegin",  //star id
                            endkey: "paymentEnd",        //end id
                            startname: "paymentBegin",    //star name
                            endname: "paymentEnd"         //end name
                        }                 
                    ],
                    buttons:[{
                        "text": '查询',
                        "type": 'submit',
                        "id": '',
                        "name": '',
                        "inputClass": 'btn-danger',
                        "callback": {
                            action: 'on-search'
                        }
                    }]
                },
                url : ykyUrl.transaction+"/v1/orders/search?type=1", //访问数据接口 
                queryParams : {           //请求接口参数 
                	pageSize: 10,        //分页参数
                    page:1 ,            //当前页  
                    defaultStatus:true,                          
                },                              			
                gridColumns: [          //表格列                
                    {
                    	key: 'index', 
                    	name: '序号', 
                    	width: '50px',
                    	align: 'center'                  	
                    },{
                    	key: 'id',
                    	name: '订单编号',  
                    	align: 'center',
                    	cutstring: true 
                    },
                    {
                    	key: 'orderPay', 
                    	name: '支付号',  
                    	align: 'center',                    
                    	child:[{
                        	key: 'id',
                        	name:'', 
                        	cutstring: true
                        }
                        ]                   	
                    },
                    {
                    	key: 'orderDate',
                    	name: '创建时间', 
                    	align: 'center', 
                    	cutstring: true, 
                    },{
                    	key: 'orderPay', 
                    	name: '支付时间',  
                    	align: 'center',                    
                    	child:[{
                        	key: 'createdDate',
                        	name:'',
                        	cutstring: true, 
                        	text:{
                        		undefined:{
                        			'value' : '',
                        		}
                        	}
                        }
                        ]                   	
                    },
                    {
                    	key: 'orderPay',
                    	name: '总价',
                        align: 'center',                         
                        child:[
                          	{
	                        	key: 'amount',
	                        	name:'' ,
	                        	cutstring: true 
	                        }
                        ]
                    },
                    {key: 'currency',
                    	name: '币种', 
                    	align: 'center',
                    	cutstring: true,
                    	text:{
                    		CNY:{
                    			'value' :'RMB'                   			
                    		},
    		                USD:{
    		                	'value' :'USD'		        			
    		        		}
                    	}
                    },
                    {
                    	key: 'purchaserName', 
                    	name: '账号',  
                    	align: 'center', 
                    	cutstring: true, 
                    	text:{
                    		undefined:{
                    			'value':''
                    		}
                    	}
                    },
                    {
                    	key: 'orderPay', 
                    	name: '支付平台',  
                    	align: 'center', 
                    	 child:[{
                         	key: 'paymentMethodId',
                         	name:'', 
                         	cutstring: true, 
                         	text:{
                         		ALIPAY:{
                          		  'value' : '支付宝'
                          	  },
                          	  EXT_OFFLINE:{
                          		  'value':'银行汇款'
                          	  },
                          	  UNIONPAY_B2B:{
                          		  'value':'在线支付（企业）'
                          	  },
                          	  WECHATPAY:{
                          		  'value':'微信支付'
                          	  },
                          	  UNIONPAY_B2C:{
                          		  'value':'在线支付'
                          	  },
							  CREDITPAY: {
								  'value': '账期支付'
                              }
                          	}
                         }
                         ]                	
                    },
                    {key: 'orderStatus',   name: '状态', align: 'center',width: '120px',cutstring: true, text : {
                    	WAIT_SHIP:{
                    		'value' : '待发货'
                        },
                        WAIT_RECEIVE:{
                        	'value' : '已发货'
                        },
                        COMPLETED:{
                        	'value' : '交易完成'
                        }
                    }
                    },
                    {
                    	key: 'orderPay', 
                    	name: '财务状态',  
                    	align: 'center',                    
                    	child:[{
                        	key: 'finVerificationStatus',
                        	name:'',
                        	text :{
                        		Y:{
                            		'value':'已确认'
                            	},
                            	undefined:{
                            		'value':'已确认'
                            	},
                            	N:{
                            		'value':'待确认'
                            	}
                        	},                        	
                        	cutstring: true, 
                        }
                        ]                   	
                    },
                    {key: 'operate', name: '操作', align: 'center', width: '150px',               	                  		
    	    			items:[ 
    	      				  {
    	      					    className : 'btn-status',
    	      					    text: '财务确认',
    	      					    show : "'{orderPay.finVerificationStatus}'=='N' || undefined",
    	      					    callback: {
    	      					    	confirm:{
    	      					    		title:'财务确认',
    	      					    		content: '确认财务正确吗？'
    	      					    	},
    	      					    	action: 'doChangeStatus',
    	      					    	params: ['{orderPay.id}','Y']
    	      					    }   
    	      					},    	      					    		      			  	      					
    	      					{
    	      					    className : 'btn-status',
    	      					    text: '详情',  
    	      					  show : "'{orderPay.finVerificationStatus}'!=''",
    	      					    href : "order.htm?action=detail&id={id} " ,
    	      					    target: "_blank"	      					    
    	      					},
    	      					{
    	      					    className : 'btn-status',
    	      					    text: '--',
    	      					    show : "'{orderPay.finVerificationStatus}'==''",     	      					    
    	      					},    	      					    	      					
    	             ]
         
                                   
                    }
                ], 
                pageflag : true,    //是否显示分页
                refresh: false,     //重载  
                showTotal:true
        		}	
        },
		
		methods:{         	       					
			onSearchClick: function (formdata) {
				console.log(formdata)
				 var queryParams = this.queryParams
		         queryParams.pageSize = 10;
		         queryParams.page = 1;
		         queryParams.defaultStatus = !queryParams.defaultStatus	;        
		        
		         for (var key in formdata) {
		        	 queryParams[key] = formdata[key] ? formdata[key] : undefined
				}
		
		        
		
			} 
	  }  
    });
      		
	  
});
function  doChangeStatus(index, params) {//方法
    syncData(ykyUrl.transaction + '/v1/orders/' + params[0]+"/finVerification", 'PUT', null , function (res , err) {//页面加载前调用方法    	    	
            window.setTimeout(function() {
                vm.refresh = !vm.refresh;//重载   
            }.bind(vm), 400);
        layer.close(index);
    });   
}
