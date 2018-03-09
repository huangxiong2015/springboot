var vm = '';
$(function(){
      vm = new Vue({
        el: '#featured-list',
        data: { 
            url : ykyUrl.info + "/v1/recommendations", //访问数据接口 
            queryParams : {           //请求接口参数 
            	pageSize: 10,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
                categoryId:'20060',
                status:''
            },
            gridColumns: [          //表格列
                {key: 'orderSeq', name: '位置',align: 'center'},
                {key: 'contentMap', name: '型号', align: 'center', child:[
                     {
                         key: "modelName",
                         name: ""
                     } 
                 ]
                },
                {key: 'lastUpdateDate', name: '更新时间',align: 'center'},
                {key: 'publishDate', name: '推荐时间', align: 'center', class : 'wid15', width:'250px',callback: 
                	{ 
                		action: 'getText',
                		params: ['{status}', '{publishDate}']
                	}
                },
                {key: 'creatorName', name: '创建人', align: 'center'},
                {key: 'status', name: '状态', align: 'center', text:{
                	DRAFT:{
                		value : '草稿'
                    },
                    PUBLISHED:{
                    	value : '已推荐'
                    },
                    HOLD:{
                    	value : '停用'
                    }
                }},
                {key: 'operate', name: '操作', align: 'center', items:[ 
                     {
                        className : 'btn-status',
                        text: '停用',
                        show : "'{status}'=='PUBLISHED'",
                        callback: {
                            confrim: {title:'停用',content:'确认停用？'},
                            action: 'changeType',
                            params: ['{recommendationId}','HOLD']
                        }   
                    },
                    {
                        className : 'btn-status',
                        text: '推荐',
                        show : "'{status}'=='DRAFT' || '{status}'=='HOLD'",
                        callback: {
                            confrim: {title:'推荐',content:'确认推荐？'},
                            action: 'changeType',
                            params: ['{recommendationId}','PUBLISHED']
                        }   
                    },
                     /*{
	                     className : 'btn-edit',
	                     text: '查看',
	                     show : true,
	                     callback:{
	                    	action:'getUrl',
	                    	params: ['{contentMap}']
	                     }
	                 },*/
	                 {
	                     className : 'btn-edit',
	                     text: '编辑',
	                     show : "'{status}'=='DRAFT' || '{status}'=='HOLD'",
	                     href : 'featuredProduct/edit.htm?recommendationId={recommendationId}',
	                 },
	                 {
	                     className : 'btn-btn-delete',
	                     text: '删除',
	                     show : "'{status}'=='DRAFT' || '{status}'=='HOLD'",
	                     callback: {
	                     	confrim: {title:'删除',content:'确认删除？'},
	                     	action: 'delFunc',
	                     	params: ['{recommendationId}']
	                     }
	                 }
	              ]
	             }
            ], 
            pageflag : true,    //是否显示分页
            refresh: false     //重载 

        }, 
        created(){
      	  var that = this;
      	  var qModel = parseUrlParam();
      	  for(var i in qModel){
                that.queryParams[i] = qModel[i];
            } 
            this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
      }
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
    syncData(ykyUrl.info + '/v1/recommendations/' + params[0], 'DELETE', null , function (res , err) {
    	//页面加载前调用方法
        //console.log(res);
        window.setTimeout(function() {
            vm.refresh = !vm.refresh;//重载
        }.bind(vm), 400);
        layer.close(index);
    });
    console.log(params);
};
function changeType(index, params){  //修改商品状态
	syncData(ykyUrl.info + '/v1/recommendations/'  + params[0] + '/' + params[1], 'PUT', null , function (res , err) {
    	//页面加载前调用方法
        //console.log(res);
        window.setTimeout(function() {
            vm.refresh = !vm.refresh;//重载
        }.bind(vm), 400);
        layer.close(index);
    });
    console.log(params);
};
function getUrl(index, params){
	var url = params[0].linkDetails;
	window.open(url); 
};
