var vm = '';
$(function() {
	vm = new Vue(
			{
        el: '#activity-list',
        data: { 
            url : ykyUrl.product + "/v1/activities", //访问数据接口 
            queryParams : {           //请求接口参数 
            	pageSize: 10,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
                prStatus:'',  //状态
                type:'10000',	//活动类型是商品促销
                name:''
            },
            gridColumns: [          //表格列                
                {
                	key: 'activityId', 
                	name: '活动ID', 
                	cutstring: true,
                	align: 'center'
                }, 
                {
                	key: 'name', 
                	name: '活动名称',
                	cutstring: true,
                	align:'center'
               	     
                },{
                	key: 'type',
                	name: '活动类型', 
                	align: 'center',
                	cutstring: true,
                	text:{
                		"10000":{
                    		value : '商品促销'
                        }
                    }
                },
                {
                	key: 'activityTime',
                	name: '活动时间 ', 
                	align:'center',
                 	width:'300px',
                 	cutstring: true,
                 	callback: {
                    	action: 'returnText',
                    	params: ['{startDates}','{endDates}']      //2个返回字段拼接在1列
                    }   
                 },
                {
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
                },
                {
                	key: 'lastUpdateUserName',
                	name: '操作人', 
                	align: 'center',
                	cutstring: true,
                },
                {
                	key: 'prStatus', 
                	name: '状态', 
                	align: 'center',
                	cutstring: true,
                	width: '120px', 
                	text:{
                		STOP:{
	                		value : '停用'
	                    },
	                    NS:{
	                    	value : '启用'
	                    },
	                    GOING:{
	                    	value : '进行中'
	                    },
	                    OVER:{
	                    	value : '已结束'
	                    }
                }},
                {
                	key: 'operate', 
                	name: '操作', 
                	align: 'center', 
                	cutstring: true,
                	width: '150px', 
                	items:[
					 {
						className : 'btn-published',
						show : "'{prStatus}' =='STOP' ",
						text : '启用',
						callback : {
							confirm : {
								title : '启用',
								content : '确认启用？'
							},
							action : 'changeStatus',
							params : [ '{activityId}', 'ENABLE' ]
						}
					 },
                     {
                        className : 'btn-edit',
                        text: '编辑',
                        show : "'{prStatus}' =='STOP' ",
                        href : 'shoppromotion/information/{activityId}.htm',   //编辑页
                        //target : '_blank'
                       
                    },
					{
					    className : 'btn-status',
					    text: '停用',
					    show : "'{prStatus}'=='GOING' || '{prStatus}'=='NS'  ",
					    callback: {
					    	confirm : {
								title : '停用',
								content : '确认停用？'
							},
                        	action: 'changeStatus',
                        	params: ['{activityId}','UNABLE']
                        }   
					},
					{
                        className : 'btn-detail',
                        text: '详情',
                        show : "'{prStatus}'=='GOING' || '{prStatus}'=='OVER' || '{prStatus}'=='NS' ",
                        href : 'shoppromotion/detail.htm?activityId={activityId}',
                        target : '_blank'
                       
                    },
                    {
                        className : 'btn-look',
                        text: '查看',
                        show : "'{prStatus}'=='GOING' && '{specifySupplier}' != 'true'",
                        href : ykyUrl.portal+'/activity/{activityId}.htm', //跳转到活动页
                        target : '_blank'
                        	
                    },
                    {
                        className : 'btn-delete',
                        text: '删除',
                        show : "'{prStatus}'=='OVER' || '{prStatus}'=='STOP' ",
                        callback: {
                        	confirm : {
								title : '删除',
								content : '确认删除？'
							},
                        	action: 'delFunc',
                        	params: ['{activityId}']
                        }
                    }
                 ]
                }
            ], 
            pageflag : true,    //是否显示分页
			showTotal: true, //显示列表总数
            refresh: false     //重载 
            
        },
        methods:{
        	onSearch : function(){ 
        		var that = this;
        		var qModel = $('#seachForm').serializeObject();
        		this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
        		for ( var i in qModel) {
					that.queryParams[i] = qModel[i];
				}
        	}
        }
     
    });
	$('#createData .input-daterange').datepicker({
        format: config.dateFormat,
        autoclose: true
    }); 
 
});

function returnText(index, params){  
	return params[0] + "~" + params[1];
}

function  delFunc(index, params) {  //删除方法
    syncData(ykyUrl.product + '/v1/activities/' + params[0]+'/delete/activity', 'DELETE', null , function (res , err) {//页面加载前调用方法
            window.setTimeout(function() {
                vm.refresh = !vm.refresh;//重载
            }.bind(vm), 400);
        layer.close(index);
    });
    console.log(params);
}
function changeStatus(index, params){//修改状态
	layer.closeAll();
	var index = layer.load(1);
	syncData(ykyUrl.product + '/v1/activities/' + params[0] + '/' + params[1], 'PUT', null , function (res , err) {//页面加载前调用方法 		
            window.setTimeout(function() {
                vm.refresh = !vm.refresh;//重载
            }.bind(vm), 400);         
        layer.close(index);
    });   
}