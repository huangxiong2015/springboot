var vm = '';
var partyCarrierVo = {"partyStatus": "PARTY_DISABLED"};
$(function(){
      vm = new Vue({
        el: '#shipment-list',
        data: { 
            url : ykyUrl.party+"/v1/carrier", //访问数据接口 ?page=1&size=10
            queryParams : {           //请求接口参数 
            	size: 10,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
            },
            gridColumns: [          //表格列                
                {key: 'index', name: '序号', align: 'center',},
                {key: 'createDate', name: '创建时间', align: 'center',cutstring: true,default : '--',},
                {key: 'lastUpdateDate', name: '修改时间', align: 'center',cutstring: true,default : '--',},
                {key: 'groupName', name: '公司名称', align: 'center',cutstring: true,default : '--',},
                {key: 'partyStatus', name: '状态', align: 'center', text:{
                	PARTY_ENABLED:{
                		value : '启用',
                		color : 'black'
                    },
                    PARTY_DISABLED:{
                    	value : '停用',
                    	color : 'red'
                    }
                }},
                {key: 'operate', name: '操作', align: 'center', width: '150px', items:[ 
					{
					    className : 'btn-status',
					    text: '停用',
					    show : "'{partyStatus}'=='PARTY_ENABLED'",
					    callback: {
                        	action: 'changeStatus',
                        	params: ['{partyId}','PARTY_DISABLED']
                        }   
					},
					{
					    className : 'btn-status',
					    text: '启用',
					    show : "'{partyStatus}'=='PARTY_DISABLED'",
					    callback: {
                        	action: 'changeStatus',
                        	params: ['{partyId}','PARTY_ENABLED']
                        }   
					},
                   {
                        className : 'btn-edit',
                        text: '编辑',
                        show : "'{partyStatus}'!='PARTY_ENABLED'",
                        href : 'shipmentlist/create.htm?partyId={partyId}'
                    },
                    /*{
                        className : 'btn-btn-delete',
                        text: '删除',
                        show : "'{status}'=='DRAFT' || '{status}'=='HOLD'",
                        callback: {
                        	confrim: {title:'删除',content:'确认删除？'},
                        	action: 'delFunc',
                        	params: ['{recommendationId}']
                        }
                    }*/
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
              //this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
        }
    });
});

/*function  delFunc(index, params) {//删除方法
    syncData(ykyUrl.info + '/v1/recommendations/' + params[0], 'DELETE', null , function (res , err) {//页面加载前调用方法
            window.setTimeout(function() {
                vm.refresh = !vm.refresh;//重载
            }.bind(vm), 400);
        layer.close(index);
    });
    console.log(params);
}*/
function changeStatus(index, params){//修改状态 
	var query={};
	query={
			'partyStatus':params[1],
			//'partyStatus':'PARTY_DISABLED',
			
	}
	syncData(ykyUrl.party + '/v1/carrier/' + params[0], 'PUT', query , function (res , err) {//页面加载前调用方法 	
            window.setTimeout(function() {
                vm.refresh = !vm.refresh;//重载
            }.bind(vm), 400);         
        layer.close(index);
    });   
}