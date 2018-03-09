var vm = '';
$(function() {
	
	function getFormData () {
	    var searchingFlag = sessionStorage.getItem('searchingFlag')
		var searchString = sessionStorage.getItem('searching')
        var result = {
	    	promotionStatus:'DISABLE,ENABLE'
	    }

		if (searchString && searchingFlag) {
			try {
                result = JSON.parse(searchString)
			} catch (e) {}
		}

        sessionStorage.removeItem('searchingFlag')
        sessionStorage.removeItem('searching')
        if (!searchingFlag) {
            sessionStorage.removeItem('searching')
        }      
		return result
	}
	
	vm = new Vue(
			{
        el: '#activity-list',
        data: { 
            url : ykyUrl.product + "/v1/promotions", //访问数据接口 
            queryParams : {           //请求接口参数 
            	pageSize: 10,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
                promotionName:'',    //活动名称
                promotionStatus:'DISABLE,ENABLE',  //活动状态
                createDateStart:'',  //创建开始时间
                createDateEnd:'',    //创建结束时间   
            },
            formData: {
                url:'',    //form提交请求接口，可选
                id:'seachForm',
                data: getFormData(),            //初始化数据
                columnCount: 3,
                fields:[            //配置控件
                     {
                        keyname: "活动名称",           //控件文本
                        type: "text",             //控件类型
                        key: "promotionName",          //控件名
                        name: "promotionName"         //控件name
                    },
                    {
                        keyname: "活动状态",           //控件文本
                        type: "select",             //控件类型
                        key: "promotionStatus",          //控件名
                        name: "promotionStatus",         //控件name
                        options: [{
                            value: 'DISABLE,ENABLE',
                            text: '全部'
                        },{
                            value: 'ENABLE',
                            text: '已发布'
                        },{
                            value: 'DISABLE',
                            text: '停用'
                        }]
                    },
                    {
                        keyname: "创建时间",
                        type: "daterange",   		//区间类型
                        startkey: "createDateStart",   //star id
                        endkey: "createDateEnd",  		//end id
                        startname: "createDateStart",  //star name
                        endname: "createDateEnd" , 		//end name
                        dateRange:{}
                    },
                    
                    
                ],
                buttons:[{
                        "text": '查询',
                        "type": 'submit',
                        "id": '',
                        "name": '',
                        "inputClass": 'btn btn-danger',
                        "callback": {
                            action: 'on-search'
                        }
                    }
                    ]
            },
            gridColumns: [          //表格列                
                {
                	key: 'promotionId', 
                	name: '活动ID', 
                	cutstring: true,
                	align: 'center'
                }, 
                {
                	key: 'promotionName', 
                	name: '活动名称',
                	cutstring: true,
                	align:'center'
               	     
                },{
                	key: 'promotionType',
                	name: '活动类型', 
                	align: 'center',
                	cutstring: true,
                	text:{
                		"ORDINARY":{
                    		value : '普通活动'
                        },
                        "RECOMMEND":{
                    		value : '购物车推荐'
                        }
                    }
                },{
                	key: 'createType',
                	name: '创建方式', 
                	align: 'center',
                	cutstring: true,
                	default:'-',
                	text:{
                		"TEMPLATE":{
                    		value : '模板创建'
                        },
                        "CUSTOM":{
                    		value : '定制开发'
                        }
                    }
                },{
                	key: 'activityTime',
                	name: '活动时间 ', 
                	align:'center',
                 	width:'300px',
                 	cutstring: true,
                 	callback: {
                    	action: 'returnText',
                    	params: ['{startDate}','{endDate}']      //2个返回字段拼接在1列
                    }   
                 },{
                	key: 'createdDate',
                	cutstring: true,
                	name: '创建时间',  
                	align: 'center'
                	
                },{
                	key: 'lastUpdateDate', 
                	name: '最后编辑时间', 
                	width: '120px', 
                	cutstring: true,
                	align: 'center', 
                },{
                	key: 'lastUpdateUserName',
                	name: '操作人', 
                	align: 'center',
                	cutstring: true,
                },{
                	key: 'timeStatus',
                	name: '时间状态', 
                	align: 'center',
                	cutstring: true,
                	default:'-',
                	text:{
                		"TEMPLATE":{
                    		value : '进行中'
                        },
                        "CUSTOM":{
                    		value : '已结束'
                        },
                        "CUSTOM":{
                    		value : '未开始'
                        }
                    }
                },{
                	key: 'promotionStatus', 
                	name: '状态', 
                	align: 'center',
                	cutstring: true,
                	//width: '120px', 
                	text:{
                		ENABLE:{
	                		value : '已发布'
	                    },
	                    DISABLE:{
	                    	value : '停用'
	                    }
                	}
                },{
                	key: 'operate', 
                	name: '操作', 
                	align: 'center', 
                	cutstring: true,
                	width: '170px', 
                	items:[
					{
					    className : 'btn-edit',
					    text: '装修',
					    show : "'{createType}' == 'TEMPLATE' && '{promotionType}' != 'RECOMMEND'",
					    href : 'promotion/{promotionId}/decorate.htm',
					},
					 {
						className : 'btn-published',
						show : "'{promotionStatus}' =='DISABLE' ",
						text : '启用',
						callback : {
							confirm : {
								title : '启用',
								content : '确认启用？'
							},
							action : 'changeStatus',
							params : [ '{promotionId}', 'ENABLE' ]
						}
					 },
                     
					{
					    className : 'btn-status',
					    text: '停用',
					    show : "'{promotionStatus}'=='ENABLE'",
					    callback: {
					    	confirm : {
								title : '停用',
								content : '确认停用？'
							},
                        	action: 'changeStatus',
                        	params: ['{promotionId}','DISABLE']
                        }   
					},
					{
					    className : 'btn-edit',
					    text: '编辑',
					    show : "'{promotionStatus}' =='DISABLE' ",
					    href : 'promotion/edit/{promotionId}.htm',   //编辑页
					   
					},
					/*{
                        className : 'btn-detail',
                        text: '详情',
                        show : "'{promotionStatus}'=='ENABLE'",
                        //href : 'activity/detail.htm?activityId={activityId}',
                        target : '_blank'
                       
                    },*/
                    {
                        className : 'btn-look',
                        text: '查看',
                        show : "'{promotionStatus}'=='ENABLE' && '{promotionType}' != 'RECOMMEND'",
                        callback:{
                        	action: 'goToPage',
                        	params: ['{createType}','{promotionUrl}','{promotionId}',]
                        }
                        	
                    },
                    {
                        className : 'btn-delete',
                        text: '删除',
                        show : "'{promotionStatus}'=='DISABLE'",
                        callback: {
                        	confirm : {
								title : '删除',
								content : '确认删除？'
							},
                        	action: 'changeStatus',
                        	params: ['{promotionId}','DELETE']
                        }
                    },
                    {
                        className : 'btn-edit',
                        text: '复制',
                        show : "'{createType}' == 'TEMPLATE'",
                        callback: {
                        	confirm : {
								title : '复制',
								content : '确认复制？'
							},
                        	action: 'copyActivity',
                        	params: ['{promotionId}']
                        }
                    }
                 ]
                }
            ], 
            pageflag : true,    //是否显示分页
            showTotal: true, //显示列表总数
            refresh: false     //重载 

        },
        mounted: function () {
			  setTimeout(() => this.onSearchClick(this.formData.data), 500)
		},
        methods:{
        	onSearchClick: function (formdata) {
				var queryParams = this.queryParams
                queryParams.pageSize = 10;
                queryParams.page = 1;
                queryParams.defaultStatus = !queryParams.defaultStatus
                

                for (var key in formdata) {
                	queryParams[key] = formdata[key]
				}

                LS.set("bulletinPageQueryParams",JSON.stringify(queryParams));

                sessionStorage.setItem('searching', JSON.stringify(formdata))
			},
        }
     
    }); 
 
});

function returnText(index, params){  
	return new Date(params[0]).Format('yyyy-MM-dd') + "~" + new Date(params[1]).Format('yyyy-MM-dd');
}

function changeStatus(index, params){//修改状态
	var idx=layer.load(1, {
		  shade: [0.1,'#fff'] 
		});
	syncData(ykyUrl.product + '/v1/promotions/' + params[0] + '/status?status=' + params[1], 'PUT', null , function (res , err) {//页面加载前调用方法 		
            window.setTimeout(function() {
                vm.refresh = !vm.refresh;//重载
            }.bind(vm), 400);         
        layer.close(idx);
    });   
}
function copyActivity(index,params){//复制活动
	syncData(ykyUrl.product + '/v1/promotions/reproduction/' + params[0], 'POST', null , function (res , err) {//页面加载前调用方法 		
        window.setTimeout(function() {
            vm.refresh = !vm.refresh;//重载
        }.bind(vm), 400);         
    layer.close(index);
});
}
function goToPage(index,params){
	if(params[0] == 'CUSTOM'){
		window.open(params[1]);
	}else{
		window.open(ykyUrl.portal + '/promotion/' + params[2] + '.htm');
	}
	
}