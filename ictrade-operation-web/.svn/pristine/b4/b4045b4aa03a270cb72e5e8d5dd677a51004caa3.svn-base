var vm = '';
$(function(){
      vm = new Vue({
        el: '#extension-list',
        data: { 
            url : ykyUrl.info + "/v1/recommendations", //访问数据接口 
            queryParams : {           //请求接口参数 
            	pageSize: 10,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
                categoryTypeId:'extension',
                categoryId:'',
                status:'',
                content:'' //查状态为已结束和投放中时，需要传给后端
            },
            gridColumns: [          //表格列                
                {
                	key: 'orderSeq', 
                	name: '位置', 
                	width: '50px',
                	align: 'center'
                },{
                	key: 'desc',
                	name: '描述', 
                	cutstring: true,
                	align: 'center'
                },{
                	key: 'categoryId',
                	name: '类型', 
                	align: 'center',
                	cutstring: true,
                	text:{
                		20021:{
                    		value : '库存精选'
                        },
                		20022:{
                    		value : '库存特卖  '
                        },
                		20023:{
                    		value : '库存最新  '
                        }, 
                		20005:{
                    		value : '意见反馈 '
                        },
                		20060:{
                    		value : '推广位 '
                        },
                		4001:{
                    		value : '活动'
                        },
                        4002:{
                        	value : '分销商'
                        },
                        4003:{
                        	value : '制造商'
                        }
                    }
                },{
                	key: 'lastUpdateDate', 
                	name: '投放时间', 
                	cutstring: true,
                	align: 'center',
                	width: '300px',
                	render:function(h,params){
                		var startDate = params.row.startDate ? params.row.startDate : '',
            				expiryDate = params.row.expiryDate ? params.row.expiryDate : '',
    						categoryId = params.row.categoryId ? params.row.categoryId : '',
    						str;
                		if(startDate && expiryDate && categoryId =='4001'){
                			str = startDate + '~' + expiryDate;
                		}else{
                			str = '-';
                		}
                		return h('span',{},str);
                	}
                },{
                	key: 'lastUpdateDate', 
                	name: '更新时间', 
                	cutstring: true,
                	align: 'center', 
                },
                {
                	key: 'publishDate',
                	name: '发布时间',
                    align: 'center', 
                    cutstring: true,
                    callback: { 
                	action: 'getText',
                	params: ['{status}', '{publishDate}']
                }
                },
                {key: 'creatorName', name: '创建人', align: 'center',cutstring: true,},
                {key: 'status', name: '状态', align: 'center',width: '120px', text:{
                	DRAFT:{
                		value : '草稿'
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
                {key: 'operate', name: '操作', align: 'center', width: '150px', items:[ 
					{
					    className : 'btn-status',
					    text: '上移', //分销商、制造商启用状态，活动投放中状态可以上移
					    show : "(('{status}'=='PUBLISHED' && '{categoryId}' !='4001') || ('{status}'=='PUT' && '{categoryId}' =='4001'))&& '{orderSeq}'!='1'",
					    callback: {
					    	action: 'changeSeq',
					    	params: ['{recommendationId}','up','{orderSeq}']
					    }   
					},
					{
					    className : 'btn-status',
					    text: '下移', //分销商、制造商启用状态，活动投放中状态可以下移
					    show : "(('{status}'=='PUBLISHED' && '{categoryId}' !='4001') || ('{status}'=='PUT' && '{categoryId}' =='4001')) && '{orderSeq}'!='5'",
					    callback: {
					    	action: 'changeSeq',
					    	params: ['{recommendationId}','down','{orderSeq}']
					    }   
					},
					{
					    className : 'btn-status',
					    text: '停用',
					    show : "'{status}'=='PUBLISHED' || '{status}'=='PUT'",
					    callback: {
                        	action: 'changeStatus',
                        	params: ['{recommendationId}','HOLD']
                        }   
					},
                    {
                        className : 'btn-edit',
                        text: '编辑',
                        show : "'{status}'=='DRAFT' || '{status}'=='HOLD'",
                        href : 'extension.htm?action=toedit&recommendationId={recommendationId}'
                       
                    },
                    {
                        className : 'btn-delete',
                        text: '删除',
                        show : "'{status}'=='DRAFT' || '{status}'=='HOLD' || '{status}'=='END'",
                        callback: {
                        	confirm : {
								title : '删除',
								content : '确认删除？'
							},
                        	action: 'delFunc',
                        	params: ['{recommendationId}']
                        }
                    }, {
						className : 'btn-published',
						show : "'{status}'!='PUBLISHED' && '{status}'!='END' && '{status}'!='PUT'",
						text : '启用',
						callback : {
							confirm : {
								title : '发布',
								content : '确认发布？'
							},
							action : 'changeStatus',
							params : [ '{recommendationId}', 'PUBLISHED' ]
						}
					}
                 ]
                }
            ], 
            pageflag : true,    //是否显示分页
            refresh: false     //重载 

        },
        watch:{
        	'queryParams.status':function(val){
        		if(val == 'END' || val == 'PUT'){
        			this.queryParams.content = '4001';
        		}else{
        			this.queryParams.content = '';
        		}
        	}
        },
        methods:{
        	onSearch : function(){ 
        		 //var qModel = $('#seachForm').serializeObject();
        		 
        		  window.location.href = window.location.pathname 
        		  + "?desc=" + encodeURIComponent($('#desc').val()) 
        		  +"&categoryId="+  encodeURIComponent($('#categoryId').val()) 
        		  +"&status="+encodeURIComponent($('#status').val())
        		  +"&content="+this.queryParams.content;
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
function getText(index, params){ 
	var str='';
	if(params[0]=='HOLD' || params[0]=='DRAFT')  
		str='--'
	else
		str= params[1]
	return str;
}
function  delFunc(index, params) {//删除方法
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
function changeSeq(index, params){//修改位置
	syncData(ykyUrl.info + '/v1/recommendations/updateOrderSeq/' + params[0] + '/' + params[1] + '/' + params[2], 'PUT', null , function (res , err) {	
            window.setTimeout(function() {
            	layer.msg('修改成功！')
                vm.refresh = !vm.refresh;//重载
            }.bind(vm), 400);         
        layer.close(index);
    });   
}