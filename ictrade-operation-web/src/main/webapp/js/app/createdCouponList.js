/**
 * create by roy.he@yikuyi.com at 2017-7-21
 */


var vm = new Vue({
	el: '#couponList',
	data: {
		url: ykyUrl.pay+'/v1/coupons',
		queryParams : { //请求接口参数 
			size : 10, //分页参数
			page : 1, //当前页
			defaultStatus : true //监测参数变化标识
		},
		gridColumns : [{
			key: 'index',
			name: '序号',
			width: '50px'
		},{
			key : 'couponCate',
			name : '类型',
			cutstring: true,
			width: '80px',
			text : {
				OFFLINE_PROMO : {
					'value': '地推类型'
				},
				PLATFORM_PROMO : {
					'value': '活动推广'
				},
				EXACT_PROMO : {
					'value': '定向推广'
				}
			}	
		},{
			key : 'couponType',
			name : '形式',
			cutstring: true,
			default : '-',
			align : 'center',
			width: '80px',
			text : {
				DISCOUNT : {
					'value': '折扣劵'
				},
				DEDUCTION : {
					'value': '抵扣劵'
				}
			}	
		},{
			key : 'name',
			name : '名称',
			default : '-',
			cutstring: true
		},{
			key : 'createdDate',
			name : '创建时间',
			default : '-',
			cutstring: true
		},{
			key : 'unitAmountOrDiscount',
			name : '面值/折扣',
			width: '100px',
			default : '-',
			callback:{
				action : 'amountOrDiscount',
				params : ['{unitAmount}','{discountNumber}']
			}
		},{
			key : 'couponCurrency',
			name : '币种',
			width: '60px',
			align:'center',
			callback:{
				action : 'currency',
				params : ['{couponCurrency}']
			}
		},{
			key : 'thruTypeId',
			name : '有效时间',
			cutstring: true,
			callback:{
				action : 'validTime',
				params : ['{thruTypeId}','{fromDateFormat}','{thruDateFormat}','{limitMonth}' ]
			}
		},{
			key : 'thruTypeId',
			name : '使用条件',
			cutstring: true,
			callback:{
				action : 'condition',
				params : ['{vendorName}','{brandName}','{productTypeName}','{consumeLimitAmount}','{couponCurrency}','{useProductType}' ]
			}
		},{
			key : 'totalQty',
			name : '总发行量',
		},{
			key : 'usedQty',
			name : '剩余数量',
			callback:{
				action : 'qty',
				params : ['{totalQty}','{sendQty}']
			}
		},{
			key : 'statusId',
			name : '状态',
			width: '80px',
			callback: {
				action: 'getStatus',
				params: ['{statusId}','{totalQty}','{sendQty}']
			}
		},{
			key : 'operate',
			align : 'center',
			name : '操作',
			items: [ {
				className : 'btn-detail',
				text : '修改',
				show : "'{statusId}' === 'WAIT_SEND' || '{statusId}' === 'SENDING'",
				target: '_blank',
				href : ykyUrl._this + '/coupon/edit.htm?id=' + '{couponId}'
			}, {
				className : 'btn-detail ml5',
				show : "'{statusId}' === 'END'",
				text : '查看',
				href : ykyUrl._this + '/coupon/examine.htm?id=' + '{couponId}'
			},{
				className : 'btn-detail ml5',
				show : "'{statusId}' === 'WAIT_SEND' || '{statusId}' === 'SENDING'",
				text : '结束',
				callback: {
					action : 'endTicket',
					params : [ '{couponId}']
				}
			}]
		}],
		pageflag : true, //是否显示分页
		refresh : false, //重载 
	},
	methods: {
		search: function(){
		
			if($("#couponCate select").val() === ''){
				delete vm.queryParams.couponCate;
			}else{
				vm.queryParams.couponCate = $("#couponCate select").val();
			}
			
			if($("#name").val() === ''){
				delete vm.queryParams.name;
			}else{
				vm.queryParams.name = $("#name").val();
			}
			
			if($("#thruDateStart").val() === ''){
				delete vm.queryParams.thruDateStart;
			}else{
				vm.queryParams.thruDateStart = $("#thruDateStart").val();
			}
			
			if($('#thruDateEnd').val() === ''){
				delete vm.queryParams.thruDateEnd ;
			}else{
				vm.queryParams.thruDateEnd = $('#thruDateEnd').val();
			}
			
			if($("#createDateStart").val() === ''){
				delete vm.queryParams.createDateStart;
			}else{
				vm.queryParams.createDateStart = $("#createDateStart").val();
			}
			if($("#couponCurrency select").val() === ''){
				delete vm.queryParams.couponCurrency;
			}else{
				vm.queryParams.couponCurrency = $("#couponCurrency select").val();
			}
			
			if($('#createDateEnd').val() === ''){
				delete vm.queryParams.createDateEnd;
			}else{
				vm.queryParams.createDateEnd = $('#createDateEnd').val();
			}
			
			if($("#statusId select").val() === ''){
				delete vm.queryParams.statusId;
			}else{
				vm.queryParams.statusId = $("#statusId select").val();
			}
			
			if($("#couponType select").val() === ''){
				delete vm.queryParams.couponType;
			}else{
				vm.queryParams.couponType = $("#couponType select").val();
			}
			vm.queryParams.page = 1;
			vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus
		}
	}
})

function amountOrDiscount(index, params){
	var text = "-";
	if(params[0]&&params[0]!=""){
		text = params[0];
	}else if(params[1]&&params[1]!=""){
		text = numMulti(Number(params[1]),10)+"折";
	}
	return text;
}
function getStatus(index, params){
	var status = ''
	if(params[0] === 'WAIT_SEND'){
		status = '待发放'
	}else if (params[0] === 'SENDING' && (params[1] - params[2])){
		status = '发放中'
	}else if(params[0] === 'END'){
		status = '已结束'
	}else if(params[0] === 'SENDING' && !(params[1] - params[2])){
		status = '已领完'
	}	
	return status;
}

function validTime(index, params){
	var valid = '';
	if(params[0] === 'EXACT_DATE_ZONE' || params[0] === 'exact_date_zone'){
		valid = params[1]+'至'+params[2];
	}else{
		valid = '领取后'+params[3]+'天内有效';
	}
	
	return valid;
}

function condition(index, params){
	var condite = '';
	if(params[5] == 0){
		if(!params[1]&&!params[0]&&!params[2]){
			condite = "全平台通用";
		}else if(params[0]&&params[1]&&params[2]){
			condite = "供应商:"+params[0]+",制造商:"+params[1]+",分类:"+params[2];
		}else if(params[0]&&!params[1]&&params[2]){
			condite = "供应商:"+params[0]+",制造商:不限"+",分类:"+params[2];
		}else if(!params[0]&&params[1]&&params[2]){
			condite = "供应商:不限,"+"制造商:"+params[1]+",分类:"+params[2];
		}else if(!params[0]&&!params[1]&&params[2]){
			condite = "供应商:不限,制造商:不限,分类:"+params[2];
		}else if(!params[0]&&params[1]&&!params[2]){
			condite = "供应商:不限,制造商:"+params[1]+",分类:不限";
		}else if(params[0]&&!params[1]&&!params[2]){
			condite = "供应商:"+params[0]+",制造商:不限,分类:不限";
		}else if(params[0]&&params[1]&&!params[2]){
			condite = "供应商:"+params[0]+",制造商:"+params[1]+",分类:不限";
		}
	}else{
		condite = "指定商品使用";
	}
	
	
	if(params[4] === 'CNY'){
		condite += ',满'+params[3]+'元使用'
	}else{
		condite += ',满'+params[3]+'美元使用'
	}
	
	return condite;
}


function currency(index, params){
	var cy = '';
	if(params[0] === 'CNY' || params[0] === 'RMB' || params[0] === 'cny' || params[0] === 'rmb'){
		cy = 'RMB';
	}else if(params[0] === 'USD' || params[0] === 'usd'){
		cy = 'USD';
	}else if(params[0] === ''){
		cy = '--';
	}
	
	return cy;
}

function qty(index, params) {
	var num = '';
	num = parseInt(params[0]) - parseInt(params[1]);
	return num;
}

/* 结束代金劵活动操作 */
function endTicket(index, params){
	var inputLayer = layer.open({
		type: 1,
	  	title: "确定结束发放？", //不显示标题
		shade: 0.5,
		btn: ['确定', '取消'],
		area: ['510px', '200px'],
		skin:"up_skin_class",
		content: '<div class="coupon-layer">结束发放后用户将无法继续领取优惠券，但并不影响已领取的优惠券使用，请及时删除推广页面。</div>',
		yes:function(){
			endCouponPromotion(params[0],inputLayer);
		},
		cancel: function(){
			
		}
	})
}

function endCouponPromotion(id,layerToHandle){
	$.aAjax({
		url: ykyUrl.pay+"/v1/coupons/editCouponStateId?couponId="+id+"&statusId=END",
		type:"POST",
		dataType: 'JSON',
		contentType:'application/json',
	    success: function(data) {
	    	layer.close(layerToHandle);
	    	if(data.code=='200'){
	    		layer.msg("<i class='icon-right-c'></i>优惠券活动结束成功 ！", {
	    			  time: 2000,
	    			  area:['506px','145px'],
	    			  skin:"succ_skin_class"      
	    			})
	    		vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus;
	    	}else{
	    		layer.msg("<i class='icon-exclamark'></i>优惠券活动结束失败，请重试 ！", {
	    			  time: 2000,
	    			  area:['506px','145px'],
	    			  skin:"succ_skin_class"      
	    			})
	    	}
	    },
	    error:function(e){
	    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
	    }
	});
	
}

function validateVal(){
	if($("#description").val() == ""){
		$("#description").addClass('bred');
	}else{
		$("#description").removeClass('bred');
	}
}

function init(){
	//创建时间
	$('#createDate .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true
	});
	
	//有效时间
	$('#thruDate .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true 
	});
}

init();