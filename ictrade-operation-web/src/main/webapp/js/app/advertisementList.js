var vm = '';
$(function() {
	vm = new Vue(
			{
        el: '#advertisement-list',
        data: { 
            url : ykyUrl.info + "/v1/recommendations", //访问数据接口 
            queryParams : {           //请求接口参数 
            	pageSize: 10,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
                categoryTypeId:'advertisement',
                categoryId:'',
                extend1:'',
                extend2:'',
                status:''
            },
            goUrl:'',     //预览
            initData: {
	    		category: [{categoryId: '6001', categoryName: '图片广告'}],
	    		page: [{pageId: '7001', pageName: '首页'}, {pageId: '7002', pageName: '登录页'},{pageId: '7003', pageName: '商品详情页'}],	//广告页面
	    		position: [{positionId: '8001', positionName: '顶部'},{positionId: '8002', positionName: '侧边栏'}]
	    	},
            gridColumns: [          //表格列                
                {
                	key: 'index', 
                	name: '编号', 
                	width: '50px',
                	align: 'center'
                }, 
                {
                	key: 'contentMap', 
                	name: '广告标题',
                	align:'center',
               	    child : [{ key: 'title',cutstring: true,  name: ''}]  
                },{
                	key: 'categoryId',
                	name: '广告类型', 
                	align: 'center',
                	cutstring: true,
                	text:{
                		6001:{
                    		value : '图片广告'
                        }
                    }
                },
                {
                	key: 'contentMap',
                	name: '广告页面 ', 
                	align:'center',
              	    child : [{ key: 'pageName',cutstring: true, name: ''}]  
                 },
                {
                	 key: 'contentMap', 
                	 name: '广告位置 ', 
                	 align:'center',
             	     child : [{ key: 'positionName',cutstring: true, name: '',default: '-' }]
                },
                {
                	key: 'startDate', 
                	name: '投放时间',  
                	align: 'center', 
                	width:'300px',
                	callback: {
                    	action: 'returnText',
                    	params: ['{startDate}','{expiryDate}']      //2个返回字段拼接在1列
                    }   
                },{
                	key: 'createdDate', 
                	name: '创建时间', 
                	width: '120px', 
                	cutstring: true,
                	align: 'center', 
                },{
                	key: 'lastUpdateDate', 
                	name: '最后编辑时间', 
                	width: '120px', 
                	cutstring: true,
                	align: 'center', 
                },
                {
                	key: 'creatorName',
                	name: '操作人', 
                	align: 'center',
                	cutstring: true,
                },
                {
                	key: 'status', 
                	name: '状态', 
                	align: 'center',
                	width: '120px', 
                	text:{
	                	DRAFT:{
	                		value : '停用'
	                    },
	                    PUBLISHED:{
	                    	value : '启用'
	                    },
	                    HOLD:{
	                    	value : '停用'
	                    },
	                    PUT:{
	                    	value : '投放中'
	                    },
	                    END:{
	                    	value : '已结束'
	                    }
                }},
                {
                	key: 'operate', 
                	name: '操作', 
                	align: 'center', 
                	width: '150px', 
                	items:[
					 {
						className : 'btn-published',
						show : "'{status}' =='DRAFT' || '{status}'=='HOLD' || '{status}'=='DRAFT' ",
						text : '启用',
						callback : {
							confirm : {
								title : '启用',
								content : '确认启用？'
							},
							action : 'changeStatus',
							params : [ '{recommendationId}', 'PUBLISHED' ]
						}
					 },
                     {
                        className : 'btn-edit',
                        text: '编辑',
                        show : " '{status}'=='HOLD' || '{status}'=='DRAFT' ",
                        href : 'advertisement/edit.htm?recommendationId={recommendationId}',
                        //target : '_blank'
                       
                    },
					{
					    className : 'btn-status',
					    text: '停用',
					    show : "'{status}'=='PUBLISHED' || '{status}'=='PUT'  ",
					    callback: {
                        	action: 'changeStatus',
                        	params: ['{recommendationId}','HOLD']
                        }   
					},
					{
                        className : 'btn-detail',
                        text: '详情',
                        show : "'{status}'=='PUBLISHED' || '{status}'=='PUT' || '{status}'=='END' ",
                        href : 'advertisement/detail.htm?recommendationId={recommendationId}',
                        target : '_blank'
                       
                    },
                    {
                        className : 'btn-look',
                        text: '查看',
                        show : "'{status}'=='PUT'",
                        href : ykyUrl.portal+'/index.htm', //修改为主页预览地址  //www.yikuyi.com
                        target : '_blank'
                       
                    },
                    {
                        className : 'btn-delete',
                        text: '删除',
                        show : "'{status}' =='DRAFT' || '{status}'=='HOLD'  || '{status}'=='END'",
                        callback: {
                        	confirm : {
								title : '删除',
								content : '确认删除？'
							},
                        	action: 'delFunc',
                        	params: ['{recommendationId}']
                        }
                    }
                 ]
                }
            ], 
            pageflag : true,    //是否显示分页
            refresh: false     //重载 

        },methods:{
        	onSearch : function(){ 
        		  window.location.href = window.location.pathname 
        		  + "?content=" + encodeURIComponent($('#title').val()) 
        		  +"&extend1="+ encodeURIComponent($('#extend1').val()) 
        		  +"&extend2="+ encodeURIComponent($('#extend2').val()) 
        		  +"&categoryId="+ encodeURIComponent($('#categoryId').val()) 
        		  +"&status="+encodeURIComponent($('#status').val());
        		  
        		  //extend1  广告页面    extend2 广告位置 
        	}
        },
        created(){
      	  var that = this;
      	  var qModel = parseUrlParam();
      	  for(var i in qModel){
                that.queryParams[i] = qModel[i];
            } 
            this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
       },
     
    });
 
});

function returnText(index, params){  
	if(!params[0] || !params[1]) {
		return '-';
	}
	return params[0] + "~" + params[1];
}
function getText(index, params){ 
	var str='';
	if(params[0]=='HOLD' || params[0]=='DRAFT')  
		str='--'
	else
		str= params[1]
	return str;
}
function  delFunc(index, params) {  //删除方法
    syncData(ykyUrl.info + '/v1/recommendations/' + params[0], 'DELETE', null , function (res , err) {//页面加载前调用方法
            window.setTimeout(function() {
                vm.refresh = !vm.refresh;//重载
            }.bind(vm), 400);
        layer.close(index);
    });
    console.log(params);
}
function changeStatus(index, params){//修改状态
	syncData(ykyUrl.info + '/v1/recommendations/' + params[0] + '/' + params[1], 'PUT', null , function (res , err) {//页面加载前调用方法 		
            window.setTimeout(function() {
                vm.refresh = !vm.refresh;//重载
            }.bind(vm), 400);         
        layer.close(index);
    });   
}