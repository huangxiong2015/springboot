var vm = '';
$(function(){
      vm = new Vue({
        el: '#payment-list',
        data: { 
            url : ykyUrl.pay + '/payments/getPayMothodAll', //访问数据接口 
            queryParams : {           //请求接口参数 
            	//pageSize: 10,        //分页参数
                //page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
                //categoryId:'20021',
                //status:''
            	//roleType: "CARRIER"
            },
            gridColumns: [          //表格列                
                {key: 'parentPaymentMethodId', name: '支付类型', align: 'center',},
                {key: 'description', name: '描述', align: 'center',cutstring: true,default : '--',},
                {key: 'lastAccount', name: '最后更新人', align: 'center',cutstring: true,default : '--',},
                {key: 'lastUpdateDate', name: '最后更新时间', align: 'center',},
                {key: 'state', name: '支付状态', align: 'center', text:{
                	VALID:{
                		value : '有效',
                		color : 'black'
                    },
                    NOT_VALID:{
                    	value : '无效',
                		color : 'black'
                    }
                }},
                {key: 'dev', name: '生产环境状态', align: 'center', text:{
                	VALID:{
                		value : '有效',
                		color : 'black'
                    },
                    NOT_VALID:{
                    	value : '无效',
                		color : 'black'
                    }
                }},
                {key: 'operate', name: '操作', align: 'center', width: '150px', items:[
                   {
                        className : 'btn-edit',
                        text: '编辑',
                        show : "true",
                        href : 'PaymentMethods/edit.htm?paymentMethodId={paymentMethodId}' 
                    },
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