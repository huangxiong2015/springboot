/**
 * create by roy.he@yikuyi.com at 2017/11/03
 */

var vm = new Vue({
	el: '#maintenanceData',
	data:{
		vendorList: [],
		url: ykyUrl.product+'/v1/product/mapping/errors',
		queryParams : { // 请求接口参数
			pageSize : 10, // 分页参数
			page : 1, // 当前页
			dataType: 'BRAND',
			defaultStatus : true // 监测参数变化标识
		},
		gridColumns : [{
			key :'selected',
			name: '',
			width: '40px',
			disabled: "{status} == 0"
		},{
			key : 'index',
			name : '序号',
			width: '80px'
		},{
			key: 'brandName',
			name: '制造商名称',
			default: '--',
			cutstring: true
		},{
			key: 'vendorName',
			name: '数据来源',
			default: '--',
			cutstring: true
		},{
			key:'createdDate',
			name:'创建时间',
			default: '--',
			cutstring: true
		},{
			key:'lastUpdateDate',
			name:'更新时间',
			default: '--',
			cutstring: true
		},{
			key:'oprName',
			name:'操作人',
			default: '--',
			cutstring: true
		},{
			key:'status',
			name:'状态',
			align: 'center',
			text: {
				0: {
					value: '已标准'
				},
				1: {
					value: '待标准'
				}
			}
		},{
			key:'operate',
			name:'操作',
			items: [{
				className: 'btn-detail',
				text:'标准化',
				show: '{status}==1',
				href: ykyUrl._this+'/manufacturer.htm',
				target: '_blank'
			},{
				className: 'btn-detail',
				text:'更新',
				show: '{status}==1',
				callback: {
					action : 'updateData',
					params : [ '{_id}']
				}
			}]
		}],
		pageflag: true,
		refresh: false,
		checkflag: true,
		formData:{
			id:'seachForm',
			columnCount:3,
			fields: [
				{
				    "keyname": "更新时间",
				    "type": "daterange",       
				    "startkey": "startDate", 
				    "endkey": "endDate",      
				    "startname": "startDate",  
				    "endname": "endDate"       
				},
				{
	                "keyname": "制造商名称",
	                "type": "text",
	                "key": "brandName", 
	                "name": "brandName"
	          },{
	                "keyname": "操作人",
	                "type": "text",
	                "key": "oprUserName", 
	                "name": "oprUserName"
	          },{
	                "keyname": "状态",
	                "type": "select",
	                "key": "status",
	                "name": "status",
	                "inputClass":"",
	                "optionId":"value",        // 控件option的value对应的接口字段
	                "optionName":"text",    // 控件option的Text对应的接口字段,
	                "options": [
						{
						    "value": "",
						    "text": "全部"
						},       
	                    {
	                        "value": "0",
	                        "text": "已标准"
	                    },
	                    {
	                        "value": "1",
	                        "text": "待标准"
	                    }
	                ]
	            },{
	                "keyname": "数据来源",
	                "type": "select",
	                "key": "vendorId",
	                "name": "vendorId",
	                "inputClass":"",
	                "optionId":"id",        // 控件option的value对应的接口字段
	                "optionName":"displayName",    // 控件option的Text对应的接口字段,
	                "options": [],
	           
	           }       
			],
			buttons:[
				{
	                "text": '查询',
	                "type": 'button',
	                "id": '',
	                "name": '',
	                "inputClass": 'btn btn-danger',
	            	"callback": {
	                    action: 'on-search',        // 方法名
	                }
	            }
			]
		},
	},
	created: function(){
		var _this = this;
		syncData(ykyUrl.party + '/v1/party/partygroups', 'PUT', {}, function(data,err){
			if(!err){
				var validData = [{id: '', displayName: '全部'}];
				data.forEach(function(item){
					if(item.isAutoIntegrateQty === 'Y'){
						validData.push(item);
					}
				})
				_this.vendorList = validData;
				_this.formData.fields[4].options = validData;
			}
		})
	},
	methods: {
		searchData: function(searchParam){		
			var queryParams = this.queryParams
            queryParams.size = 10
            queryParams.page = 1
            queryParams.defaultStatus = !queryParams.defaultStatus

            for (var key in searchParam) {
                queryParams[key] = searchParam[key] ? searchParam[key] : undefined
            }
		},
		updateChecked: function(){
			var arr = eval('('+$("#checkedIds").val()+')'), ids = [];
			
			arr.forEach(function(item){
				ids.push(item._id);
			});
			
			if(ids.length == 0){
				layer.msg('请选择需要更新的数据')
				return false;
			}
			
			syncData(ykyUrl.product + "/v1/product/mapping/errors", 'PUT', ids, function(res, err) {
				if (!err) {
					if((ids.length - Number(res)) === 0){
						layer.msg('数据更新成功！')
					}else{
						layer.open({
							type: 1,
			        		title: '更新提示',
			        		move: false,
			        		skin: "s_select_data",
			        		content: '<p style="padding: 15px 20px;">数据更新完成，'+(ids.length - Number(res))+'条数据更新失败</p>',
			        		btn: ['知道了'],
		        		
						})
					}	
					vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus
				}
			});
		}
	}
});


function updateData(index, params){
	
	syncData(ykyUrl.product + "/v1/product/mapping/errors", 'PUT', [params[0]], function(res, err) {
		if (!err) {
			if(res && res>0){
				layer.msg('数据更新成功');
				vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus
			}else{
				layer.open({
					type: 1,
	        		title: '更新提示',
	        		move: false,
	        		skin: "s_select_data",
	        		content: '<div style="padding: 15px 20px;"><p style="font-weight:700;font-size: 14px;">更新失败，未找到匹配数据</p><p>请先至制造商管理中进行维护</p></div>',
	        		btn: ['去维护','取消'],
	        		yes: function(index, layero){
	        			window.location.href=ykyUrl._this + '/manufacturer.htm'
	        		},
	        		cancel: function(index, layero){
	        			
	        		}
        		
				})
			}
			
		}else{
			layer.open({
				type: 1,
        		title: '更新提示',
        		move: false,
        		skin: "s_select_data",
        		content: '<div style="padding: 15px 20px;"><p style="font-weight:700;font-size: 14px;">更新失败，未找到匹配数据</p><p>请先至制造商管理中进行维护</p></div>',
        		btn: ['去维护','取消'],
        		success: function(index, layero){
        			window.location.href=ykyUrl._this + '/manufacturer.htm'
        		},
        		cancel: function(index, layero){
        			
        		}
    		
			})
		}
	});
}