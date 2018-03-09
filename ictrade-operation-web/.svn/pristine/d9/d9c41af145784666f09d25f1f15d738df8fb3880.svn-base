//移除数组中第一个匹配传参的那个元素
Array.prototype.remove=function(id) {  
    //遍历数组  
    for (var i = 0; i < this.length; i++) {  
       if(this[i]==id){
    	   this.splice(i, 1);
    	   break;
       }
    }   
}  
sessionStorage.removeItem('isManager');
var vm;
//获取登陆用户详细
function getUserInfo(){
	$.aAjax({
		  url: ykyUrl.party + '/v1/person/detail?partyId=' + $('#userId').val(),
		  type: 'GET',
		  success: function(data) {
			  if(data.manager){
				  /*vm.isManager = true;*/
				  getSubordinateInfo();
			  }else{
				 /* vm.isManager = false;*/
				  sessionStorage.setItem('isManager', false);
				  initVue();
			  }
		  }
	});
}
//获取登录用户直接下属列表
function getSubordinateInfo(){
	$.aAjax({
		  /*url: ykyUrl.party + '/v1/person/reportsto?partyId=' + $('#userId').val(),*/
		  url: ykyUrl.party + '/v1/dept/findCustomerByDeptId?defaultStatus=false&size=100&page=1&deptId='+ykyUrl.certificationDeptId,
		  type: 'GET',
		  success: function(data) {
//			  vm.listData = vm.listDataCache = data.list;
			  if(data.list && data.list.length) {
				 /* vm.isSub = true;*/
				  sessionStorage.setItem('isManager', true);
				  initVue(data);
			  }else {
				  /*vm.isSub = false;*/
				  sessionStorage.setItem('isManager', false);
				  initVue(data);
			  }
		  }
	});
}
getUserInfo();

function initVue(data){
 vm= new Vue({
	el: '#vendorManageList',
	data: {
		loseDescribe: '',
		url: ykyUrl.party+'/v1/vendorManage/getVendorManageList',
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
			width: '80px',
			text : {
				Y : {
					'value': '是'
				},
				N : {
					'value': '否'
				}
			}
		},{
			key : 'payType',
			name : '付款条件',
			default : '-',
			cutstring: true
		},{
			key : 'enquiryName',
			name : '询价员',
			default : '-',
			cutstring: true
		},{
			key : 'creatorName',
			name : '创建人',
			default : '-',
			cutstring: true
		},{
			key : 'accountStatus',
			name : '状态',
			width: '80px',
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
			default : '-',
			cutstring: true
		},{
			key : 'lastDealDate',
			name : '最后交易时间',
			default : '-',
			cutstring: true
		},{
			key : 'operate',
			align : 'center',
			name : '操作',
			items: [{
				className : 'btn-detail ml5',
				show : true,
				text : '详情',
				href : ykyUrl._this + '/vendor/detail.htm?partyId='+ '{partyId}'
			},{
				className : 'btn-detail ml5',
				show : "'{accountStatus}' != 'INEFFECTIVE'",//INEFFECTIVE 未生效
				text : '编辑',
				href : ykyUrl._this + '/vendor/edit.htm?partyId='+ '{partyId}'
			},{
				className : 'btn-detail ml5',
				show : "'{accountStatus}' == 'INEFFECTIVE' && '{applyStatus}' !='Y'",//INEFFECTIVE 未生效
				text : '编辑',
				href : ykyUrl._this + '/vendor/edit.htm?status=applyNo&partyId='+ '{partyId}'
			},{
				className : 'btn-detail ml5',
				show : "'{verifyStatusId}' === 'INVALID' && '{accountStatus}' != 'INEFFECTIVE' || (!'{verifyStatusId}' && '{accountStatus}' === 'INEFFECTIVE')",
				text : '启用',
				callback: {
					action : 'startStatus',
					params : [ '{partyId}']
				}
			},{
				className : 'btn-detail ml5',
				show : "('{verifyStatusId}' === 'START') || (!'{verifyStatusId}' && '{accountStatus}' === 'VALID')",
				text : '失效',
				callback: {
					action : 'loseStatus',
					params : [ '{partyId}']
				}
			},{
				className : 'authorize-btn btn-detail ml5',
				show : sessionStorage.getItem('isManager') + ' === true',
				text : '授权',
				callback: {
					action : 'authorizeAction',
					params : [ '{partyId}','{groupName}','{creater}']
				}
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
	           },{
	                "keyname": "付款条件",
	                "type": "text",
	                "key": "payType", 
	                "name": "payType"
	            },{
	                "keyname": "询价员",
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
	selectedList:[],
	selectedListName:[],
	listData:[],
	listDataCache:[],
	isSub:false,
	isManager:false,
	ajaxCount:0,
	},
	created:function(){
		if(data){
			 this.listData = this.listDataCache = data.list;
		}
	},
//	updated:function(){
//		if(this.ajaxCount<1){   //防止循环调用
//			getUserInfo();
//			this.ajaxCount = this.ajaxCount+1;
//		}
//	},
	methods: {
		//搜索方法
		searchData: function(searchParam){		
			var queryParams = this.queryParams
            queryParams.pageSize = 10
            queryParams.page = 1
            queryParams.defaultStatus = !queryParams.defaultStatus

            for (var key in searchParam) {
                queryParams[key] = searchParam[key] ? searchParam[key] : undefined
            }
		},
		selectFn: function(gysId, name){
			if($(event.target).is(":checked")){
				this.selectedList.push(gysId);
				this.selectedListName.push(name);
			}else {
				this.selectedList.remove(gysId);
				this.selectedListName.remove(name);
			}
		}
	}
})
}
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

//partyId,groupName,creater
function authorizeAction(index,params){
	var id, name, createorId;
	id = params[0];
	name = params[1];
	createorId = params[2];
	//获取当前供应商已授权的用户
	$.aAjax({
		url: ykyUrl.party + '/v1/vendors/'+ id +'/relation',
		type: 'GET',
		success: function(data) {
			vm.selectedList = [];
			$("[name='subName']").prop("checked",false);
			$.each(data, function(index, item) {
				
				if(item.partyIdFrom) {
					/*vm.selectedList.push(item.partyIdFrom);*/
					vm.selectedListName.push($('[data-id='+ item.partyIdFrom +']').html());
					$('[data-id='+ item.partyIdFrom +']').click();
				}
			});
		}
	});
	
	layer.open({
	  type: 1,
	  title: '供应商授权',
	  area: ['540px', ''],
	  btn: ["保存", "返回"],
	  content: $('#subList'),
	  yes: function(index) {
		 dataList = {
			  companyName: name,
			  id: vm.selectedList.join(','),
			  name: vm.selectedListName.join(',')
		  };
		  $.aAjax({
			  url: ykyUrl.party + '/v1/vendors/'+ id +'/save',
			  type: 'POST',
			  data: JSON.stringify(dataList),
			  success: function(data) {
				  if(data === 'success') {
					  $('#infoSuc').show();
					  setTimeout(function() {
						  $('#infoSuc').hide();
					  }, 3000);
					  
					  layer.close(index);
				  }else {
					  layer.msg('授权设置失败！');
				  }
			  },error:function(e){
				  layer.msg('授权设置失败！');
			  }
		  })
	  }
	});
}

function init(){
	//创建时间
	$('#registerDate .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true
	});
}

init();
