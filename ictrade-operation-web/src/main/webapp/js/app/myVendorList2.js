
var applyUserId = $("#userId").val()

var vm = new Vue({
	el: '#vendorManageList',
	data: {
//		url: ykyUrl.workflow+'/v2/apply?applyOrgId=901022135651139584',
		url: ykyUrl.workflow+'/v2/apply/myApply',
		queryParams : { //请求接口参数 
			size : 10, //分页参数
			page : 1, //当前页
			defaultStatus : true, //监测参数变化标识
			applyUserId:applyUserId,
			processIdList: 'ORG_SUPPLIER_ARCHIVES_REVIEW,ORG_SUPPLIER_INFO_CHANGE_REVIEW,ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW,ORG_SUPPLIER_INVALID_REVIEW,ORG_SUPPLIER_ENABLED_REVIEW',
		},
		gridColumns : [{
			key : 'applyId',
			name : '审核编码',
			default : '-',
			cutstring: true,	//字符串太长只显示前4个
		},{
			key : 'applyContentJsonObj',
			name : '供应商编码',
			cutstring: true,
			callback: {
				action : 'getPartyCode',
				params : [ '{applyContentJsonObj}']
			}
		}
		,{
			key : 'companyName',
			name : '供应商名称',
			default : '-',
			cutstring: true,	//字符串太长只显示前4个
		},{
			key : 'applyContentJsonObj',
			name : '所在地区',
			callback: {
				action : 'getGeneralHeadquarters',
				params : [ '{applyContentJsonObj}']
			}
		},{
			key : 'processId',
			name : '申请类型',
			width: '80px',
			text : {
				ORG_SUPPLIER_ARCHIVES_REVIEW : {
					'value': '建档'
				},
				ORG_SUPPLIER_INFO_CHANGE_REVIEW : {
					'value': '基本信息变更'
				},
				ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW : {
					'value': '产品线信息变更'
				},
				ORG_SUPPLIER_INVALID_REVIEW : {
					'value': '失效'
				},
				ORG_SUPPLIER_ENABLED_REVIEW : {
					'value': '启用'
				},
			}
		},{
			key : 'status',
			name : '审核状态',
			width: '80px',
			text : {
				WAIT_APPROVE : {
					'value': '待审核'
				},
				APPROVED : {
					'value': '通过'
				},
				REJECT : {
					'value': '不通过'
				},
			}
		},{
			key : 'createdDate',
			name : '申请时间',
			default : '-',
			cutstring: true
		},{
			key : 'applyContentJsonObj',
			name : '审核人',
			cutstring: true,
			callback: {
				action : 'getApprovePartyName',
				params : [ '{applyContentJsonObj}']
			}
		},{
			key : 'lastUpdateDate',
			name : '审核时间',
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
				callback: {
					action : 'operatePromotion',
					params : [ '{applyId}','{processId}']
				}
			}]
		}],
		
		pageflag : true, //是否显示分页
		refresh : false, //重载
		
	},
	methods: {
		//搜索方法
		search: function(){		
			if($("#applyId").val() === ''){
				delete vm.queryParams.applyId;
			}else{
				vm.queryParams.applyId = $("#applyId").val();
			}
			
			if($("#companyName").val() === ''){
				delete vm.queryParams.companyName;
			}else{
				vm.queryParams.companyName = $("#companyName").val();
			}
			
			if($("#status select").val() === ''){
				delete vm.queryParams.status;
			}else{
				vm.queryParams.status = $("#status select").val();
			}
			
			if($("#createDateStart").val() === ''){
				delete vm.queryParams.createDateStart;
			}else{
				vm.queryParams.createDateStart = $("#createDateStart").val();
			}
			
			if($("#createDateEnd").val() === ''){
				delete vm.queryParams.createDateEnd;
			}else{
				vm.queryParams.createDateEnd = $("#createDateEnd").val();
			}	
			
			if($("#applyDateStart").val() === ''){
				delete vm.queryParams.applyDateStart;
			}else{
				vm.queryParams.applyDateStart = $("#applyDateStart").val();
			}	
			
			if($("#applyDateEnd").val() === ''){
				delete vm.queryParams.applyDateEnd;
			}else{
				vm.queryParams.applyDateEnd = $("#applyDateEnd").val();
			}	
						
			vm.queryParams.page = 1;
			vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus
		},
	}
})

//获取供应商编码
function getPartyCode(index,params){
	var obj = params[0]
	var target = obj.partyCode
	return target
}


//获取申请人
function getContactUserName(index,params){
	var obj = params[0]
	var target = obj.contactUserName
	return target
}

//获取审核人
function getApprovePartyName(index,params){
	var obj = params[0]
	var target = obj.approvePartyName
	return target
}

//获取所在地区
function getGeneralHeadquarters(index,params){
	var obj = params[0]
	var target = obj.region
	return target
}


function operatePromotion(index,params){
	console.log(index,params);
	var applyId = params[0];
	var type = params[1];
	if(type == 'ORG_SUPPLIER_ARCHIVES_REVIEW'){//建档
		window.location.href = ykyUrl._this + '/myVendor/archiving.htm?&applyId='+applyId;
	}else if(type == 'ORG_SUPPLIER_INFO_CHANGE_REVIEW'){//基本信息审核
		window.location.href = ykyUrl._this + '/myVendor/baseinfo.htm?applyId='+ applyId;
	}else if(type == 'ORG_SUPPLIER_PRODUCTLINE_CHANGE_REVIEW'){//产品线信息变更
		window.location.href = ykyUrl._this + '/myVendor/productLine.htm?applyId='+ applyId;
	}else{//启用失效
		window.location.href = ykyUrl._this + '/myVendor/enable.htm?applyId='+ applyId;
	}	
}


function init(){
	//创建时间
	$('#registerDate .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true
	});
	
}



init();
