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
			align : 'center',
			width: '60px',
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
			key : 'sendQty',
			name : '领取数'
		},{ 
			key : 'usedQty',
			name : '使用数'
		},{ 
			key : 'expiredQty',
			name : '过期数'
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
				text : '领劵详情',
				show : true,
				target: '_blank',
				href : ykyUrl._this + '/coupon/getCouponList.htm?id=' + '{couponId}'
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

function validTime(index, params){
	var valid = '';
	if(params[0] === 'EXACT_DATE_ZONE' || params[0] === 'exact_date_zone'){
		valid = params[1]+'至'+params[2];
	}else{
		valid = '领取后'+params[3]+'天内有效';
	}
	
	return valid;
}

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
	}else if(params[0]==''){
		cy = '--';
	}
	
	return cy;
}

function qty(index, params) {
	var num = '';
	num = parseInt(params[0]) - parseInt(params[1]);
	
	return num;
}

function popHandleFrame(index, params){
	var inputLayer = layer.open({
		type: 1,
	  	title: "提示信息", //不显示标题
		shade: 0.5,
		btn: ['发放优惠券', '取消'],
		area: ['620px', '440px'],
		skin:"up_skin_class",
		content: $(".handle_g_layer"),
		yes:function(){
			var userNames = $.trim($("#userNames").val());
			var remark = $.trim($("#remark").val());
			if(!userNames||!remark){
				$(".handle_g_layer .error_tip").removeClass("dn");
				if(!userNames){
					$("#userNames").addClass("error_border");
				}
				if(!remark){
					$("#remark").addClass("error_border");
				}
				return;
			}
			
			postPlatformCoupon(params[0],"EXACT_PROMO",inputLayer);
			initHandleLayer();				
		},
		cancel: function(){
			initHandleLayer();				
		}
	})
}
var postplatformCouponUrl = ykyUrl.pay+"/v1/coupons/platformCoupon";
var postPlatformCoupon = function(id,type,layerToHandle){
	var params={
		  "couponCate": type,
		  "couponId": "",
		  "partyIds": "",
		  "remark": ""
		}
	params.couponId = id;
	params.partyIds = $.trim($("#userNames").val());
	params.remark = $("#remark").val();
	$.aAjax({
		url: postplatformCouponUrl,
		type:"POST",
		dataType: 'JSON',
		data:JSON.stringify(params),
		contentType:'application/json',
	    success: function(data) {
	    	layer.close(layerToHandle);
	    	if(data.statusId){
	    		layer.msg("<i class='icon-right-c'></i>优惠券发放成功 ！", {
	    			  time: 1000,
	    			  area:['506px','140px'],
	    			  skin:"fly_skin_class"      
	    			})
	    		/* setTimeout(function(){window.location.reload();},2000); */ //重新刷新
	    	}
	    	if(data.flag){
	    		layer.msg(data.flag, {
	    			  time: 3000,
	    			  area:['506px','140px'],
	    			  skin:"fly_skin_class"      
	    			})
	    	}
	    },
	    error:function(data){
	    	if(data.status == 500 && data.responseJSON.errMsg){
	    		layer.msg(data.responseJSON.errMsg, {
	    			  time: 3000,
	    			  area:['506px','140px'],
	    			  skin:"fly_skin_class"
	    			})
	    	}
	    }
	});
	
}

function initHandleLayer(){
	$(".handle_g_layer .error_tip").addClass("dn");
	$("#userNames").val("");
	$("#remark").val("");
	$("#userNames").removeClass("error_border");
	$("#remark").removeClass("error_border");
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