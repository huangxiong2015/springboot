var vm = new Vue(
{
	el : '#finance-list',
	data : {
		url : ykyUrl.info + "/v1/recommendations?categoryId=11001", //访问数据接口 
		queryParams : { //请求接口参数 
			pageSize : 10, //分页参数
			page : 1, //当前页
			defaultStatus : true //监测参数变化标识
		},
		gridColumns : [ //表格列
		{
			key: 'index', 
			name: '序号', 
			align:'center'
		}, //支持索引index，可设置 hide:true 隐藏列  
		{
			key : 'extend1',
			align : 'center',
			name : '公司名称',
			cutstring: true,
			default : '-',
			cutstring: true
		},
		{
			key : 'extend2',
			align : 'center',
			name : '公司类型',
			default : '-',
			cutstring: true,
			text:{
				agent:{
            		value : '代理商'
                },
                brand:{
            		value : '原厂'
                }
            }
		},
		{
			key : 'contentMap',
			align : 'center',
			name : '产品线',
			child : [{
				key: 'agent',
				default : '-',
				cutstring: true
			}]
		}, 
		{
			key : 'contentMap',
			align : 'center',
			default : '-',
			name : '联系人',
			child : [{
				key: 'contactUserName',
				default : '-',
				cutstring: true
			}]
		},
		{
			key : 'contentMap',
			align : 'center',
			cutstring: true,
			name : '联系方式',
			child : [{
				key: 'phone',
				cutstring: true,
				default : '-'
			}]
		},
		{
			key : 'createdDate',
			align : 'center',
			cutstring: true,
			name : '申请时间',
			default : '-'
		},
		{
            key : 'operate',
            name : '操作',
            align : 'center',
            items : [ {
                className : 'btn-detail',
                text : '详情',
                show: true,
                target: '_blank',
                href : ykyUrl._this + '/supplier/detail/{recommendationId}.htm',
            },{
                className : 'btn-delete',
                text : '删除',
                show: true,
                callback: {
                	confirm: {
                		title : '删除',
						content : '确认删除？'
                	},
                	action: 'delFunc',
                	params: ['{recommendationId}']
                }
            }]
		}],
		pageflag : true, //是否显示分页
		showTotal: true, //显示列表总数
		refresh : false
	//重载 

	},
	methods : {
		//搜索方法
		onSearch : function() {
			var that = this;
			var qModel = $('#seachForm').serializeObject();
			this.queryParams.size = 10;
			this.queryParams.page = 1;
			this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
			for ( var i in qModel) {
				that.queryParams[i] = qModel[i];
			}
			LS.set("queryParams",JSON.stringify(that.queryParams));
		}
	}
});

var queryParams  = LS.get("queryParams");
if(!queryParams){
	var params = queryParams ? JSON.parse(queryParams) : {};
	vm.queryParams.size = params.pageSize || 10;
	vm.queryParams.page = params.page || 1;
	vm.queryParams.extend1 = params.extend1 || "";
	vm.queryParams.startDate = params.startDate || "";
	vm.queryParams.endDate = params.endDate || "";	
}

//申请时间
$('#applyDate .input-daterange').datepicker({
    format: config.dateFormat,
    autoclose: true
});

//删除供应商
function delFunc(index, params) {
	
	syncData(ykyUrl.info + '/v1/recommendations/' + params[0], 'DELETE', null , function (res , err) {//页面加载前调用方法
        window.setTimeout(function() {
            vm.refresh = !vm.refresh;//重载
        }.bind(vm), 400);
        layer.close(index);
    });
	
}