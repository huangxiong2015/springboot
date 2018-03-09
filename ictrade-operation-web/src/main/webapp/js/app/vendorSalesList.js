/**
 * Created vendorSalesList.js by zr.xieyuanpeng@yikuyi.com on 2017年9月14日.
 */

var vm = new Vue({
	el: '#vendorManageList',
	data: {
		loseDescribe: '',
		url: ykyUrl.party+'/v1/vendorManage/getSellManageList',
		queryParams : { //请求接口参数 
			size : 10, //分页参数
			page : 1, //当前页
			defaultStatus : true //监测参数变化标识
		},
		gridColumns : [{
			key : 'partyCode',
			name : '供应商编码',
			default : '-',
			cutstring: true,	//字符串太长只显示前4个
		},{
			key : 'groupName',
			name : '供应商名称',
			default : '-',
			cutstring: true 
		},{
			key : 'category',
			name : '分类',
			default : '-',
			cutstring: true
		},{
			key : 'region',
			name : '所在地区',
			default : '-',
			cutstring: true
		},{
			key : 'isCore',
			name : '核心供应商',
			align : 'center',
			width: '90px',
			text : {
				Y : {
					'value': '是'
				},
				N : {
					'value': '否'
				}
			}
		},{
			key : 'orderVerify',
			name : '订单审核',
			align : 'center',
			width: '85px',
			default : '--',
			cutstring: true,
			text : {
				N : {
					'value': '否'
				},
				Y : {
					'value': '是'
				}
			}	
		},{
			key : 'enquiryName',
			name : '负责人',
			align : 'center',
			default : '--',
			cutstring: true
		},{
			key : 'creatorName',
			name : '创建人',
			align : 'center',
			default : '--',
			cutstring: true
		},{
			key : 'accountStatus',
			name : '状态',
			width: '80px',
			align : 'center',
			text : {
				VALID : {
					'value': '有效'
				},
				NOT_VALID : {
					'value': '无效'
				},
				INEFFECTIVE : {
					'value': '未生效'
				}
			}	
		},{
			key : 'createDate',
			name : '创建时间',
			align : 'center',
			default : '--',
			cutstring: true
		},{
			key : 'lastDealDate',
			name : '最后交易时间',
			align : 'center',
			default : '--',
			cutstring: true
		},{
			key : 'operate',
			align : 'center',
			name : '操作',
			items: [{
				className : 'btn-detail ml5',
				show : true,
				text : '详情',
				href : ykyUrl._this + '/salesVendor/detail.htm?status=salesDetail&partyId='+ '{partyId}'
			},{
				className : 'btn-detail ml5',
				show : "'{accountStatus}' != 'INEFFECTIVE'",//INEFFECTIVE 未生效
				text : '编辑',
				href : ykyUrl._this + '/salesVendor/edit.htm?status=salesEdit&partyId='+ '{partyId}'
			}]
		}],
		pageflag : true, //是否显示分页
		refresh : false, //重载
		formData:{
			id:'formId',
			columnCount:3,
			fields: [
				{
	                "keyname": "供应商编码",
	                "type": "text",
	                "key": "partyCode", 
	                "name": "partyCode"
	          }, {
	                "keyname": "供应商名称",
	                "type": "text",
	                "key": "groupName", 
	                "name": "groupName"
	          }, {
	                "keyname": "核心供应商",
	                "type": "select",
	                "key": "isCore",
	                "name": "isCore",
	                "optionId":"value",        //控件option的value对应的接口字段
	                "optionName":"text",    //控件option的Text对应的接口字段,
	                "options": [
						{
						    "value": "",
						    "text": "全部"
						},       
	                    {
	                        "value": "Y",
	                        "text": "是"
	                    },
	                    {
	                        "value": "N",
	                        "text": "否"
	                    }
	                ]
	            },{
	                "keyname": "所在地区",
	                "type": "select",
	                "key": "region",
	                "name": "region",
	                "inputClass":"",
	                "optionId":"categoryId",        //控件option的value对应的接口字段
	                "optionName":"categoryName",    //控件option的Text对应的接口字段,
	                "url": ykyUrl.database+"/v1/category/companylist?categoryTypeId=VENDOR_THE_REGION", //接口
	                "attr":{
	                	"placeholder":'全部'
	                }
	           },{
	                "keyname": "分类",
	                "type": "select",
	                "key": "category",
	                "name": "category",
	                "inputClass":"",
	                "optionId":"categoryId",        //控件option的value对应的接口字段
	                "optionName":"categoryName",    //控件option的Text对应的接口字段,
	                "url": ykyUrl.database+"/v1/category/companylist?categoryTypeId=VENDOR_THE_TYPE", //接口
	                "attr":{
	                	"placeholder":'全部'
	                }
	           },
	            {
	                "keyname": "订单审核",
	                "type": "select",
	                "key": "orderVerify",
	                "name": "orderVerify",
	                "optionId":"value",        //控件option的value对应的接口字段
	                "optionName":"text",    //控件option的Text对应的接口字段,
	                "options": [
						{
						    "value": "",
						    "text": "全部"
						},       
	                    {
	                        "value": "Y",
	                        "text": "是"
	                    },
	                    {
	                        "value": "N",
	                        "text": "否"
	                    }
	                ]
	            }
	            ,{
	                "keyname": "负责人",
	                "type": "text",
	                "key": "enquiryName", 
	                "name": "enquiryName"
	            },{
                    "keyname": "创建时间",
                    "type": "daterange",        //时间区间类型
                    "startkey": "beginTime",  //star id
                    "endkey": "endTime",        //end id
                    "startname": "beginTime",    //star name
                    "endname": "endTime"         //end name
                },{
                    "keyname": "最后交易日期",
                    "type": "daterange",        //时间区间类型
                    "startkey": "beginDealTime",  //star id
                    "endkey": "endDealTime",        //end id
                    "startname": "beginDealTime",    //star name
                    "endname": "endDealTime"         //end name
                },{
	                "keyname": "创建人",
	                "type": "text",
	                "key": "creatorName", 
	                "name": "creatorName"
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
                    action: 'on-search',        //方法名
                }
            }
		]
	},
	listData:[],
	listDataCache:[]
	},
	created:function(){		
	},
	methods: {
		//搜索方法
		searchData: function(searchParam){		
			var queryParams = this.queryParams
            queryParams.pageSize = 10
            queryParams.page = 1
            queryParams.defaultStatus = !queryParams.defaultStatus

            for (var key in searchParam) {
                queryParams[key] = searchParam[key] ? searchParam[key] : undefined;
            }
		}
	}
})

function loseStatus(index,params){
	
	syncData(ykyUrl.product + "/v1/inventory?vendorId="+params[0], 'GET', null, function(res, err) {
		if (!err) {
			if(res.productInfo && res.productInfo.length > 0){
				layer.open({
					  title: '提示',
					  content: '该供应商下存在上架商品，请通知相关人员下架商品后再将供应商失效。',
					  btn: ['知道了'],
					});
			}else{
				postLoseStatus(params[0])
			}
		}
	});
}

function postLoseStatus(params1){
	var _this = vm;
	layer.open({
		type: 1,
		title: '提示',
		area: '500px',
		offset: '300px',
		move: false,
		skin: "s_select_data",
		content: $('.lose-describe'),
		btn: ['确定', '取消'],
		yes: function(i, layero) {
			if(!_this.loseDescribe){
				layer.msg('失效原因不能为空');
				return false;
			}
			if(_this.loseDescribe.length > 100){
				layer.msg('失效原因长度不能超过100个字符');
				return false;
			}
			syncData(ykyUrl.party + "/v1/vendorManage/startOrLose?partyId="+params1+"&startOrLose=LOSE&describe="+_this.loseDescribe, 'POST', null, function(res, err) {
				if (!err) {
					layer.msg('供应商失效审核提交成功！')
				}
			});
			layer.close(i);
		},
		cancel: function(i, layero) {
		}
	})
}

function startStatus(index,params){
	layer.open({
		title: '提示',
		move: false,
		area: '500px',
		content: '<h2>确定启用该供应商？</h2><p>启用供应商需走审批流程，审核通过后，可上传该供应商的商品。</p>',
		btn: ['确定', '取消'],
		yes: function(i, layero) {
			syncData(ykyUrl.party + "/v1/vendorManage/startOrLose?partyId="+params[0]+"&startOrLose=START", 'POST', null, function(res, err) {
				if (!err) {
					layer.msg('供应商启用审核提交成功！')
				}
			});
			layer.close(i);
		},
		cancel: function(i, layero) {
		}
	})
}

function init(){
	//创建时间
	$('#registerDate .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true
	});
}

init();
