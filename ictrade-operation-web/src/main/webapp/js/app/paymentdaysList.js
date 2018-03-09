/**
 * create by roy.he@yikuyi.com at 2017-7-21
 */


var vm = new Vue({
	el: '#paymentOrderList',
	data: {
		url: ykyUrl.pay+'/v1/accountperiod/paymentdaysList',
		//url:'http://localhost:27085'+'/v1/accountperiod/paymentdaysList',
		queryParams : { //请求接口参数 
			size : 10, //分页参数
			page : 1, //当前页
			defaultStatus : true //监测参数变化标识
		},
		gridColumns : [{
			key : 'partyCode',
			name : 'YKY客户编码',
			default : '-',
			cutstring: true,	
		},{
			key : 'groupName',
			name : '公司名称',
			default : '-',
			cutstring: true
		},{
			key : 'creditQuota',
			name : '账期额度',
			default : '-',
			cutstring: true
		},{
			key : 'realtimeCreditQuota',
			name : '账期余额',
			default : '-',
			cutstring: true
		},{
			key : 'checkDate',
			name : '对账日期',
			default : '-',
			cutstring: true
		},{
			key : 'payDate',
			name : '付款日期',
			default : '-',
			cutstring: true
		},{
			key : 'periodOrderNum',
			name : '账期订单',
			default : '-',
			cutstring: true
		},{
			key : 'accountPeriodStatus',
			name : '账期权限',
			width: '80px',
			text : {
				PERIOD_VERIFIED : {
					'value': '正常'
				},
				PERIOD_DISABLED : {
					'value': '冻结'
				}
			}	
		},{
			key : 'operate',
			align : 'center',
			name : '操作',
			items: [{
				className : 'btn-detail ml5',
				show : true,
				text : '详情',
				href : ykyUrl._this + '/bill.htm?action=detail&enterpriseId='+ '{partyId}'
			},{
				className : 'btn-detail ml5',
				show : "'{accountPeriodStatus}' === 'PERIOD_VERIFIED'",
				text : '冻结',
				callback: {
					action : 'operatePromotion',
					params : [ '{partyId}','PERIOD_DISABLED']
				}
			},{
				className : 'btn-detail ml5',
				show : "'{accountPeriodStatus}' === 'PERIOD_DISABLED'",
				text : '取消冻结',
				callback: {
					action : 'operatePromotion',
					params : [ '{partyId}','PERIOD_ENABLED']
				}
			}]
		}],
		pageflag : true, //是否显示分页
		refresh : false, //重载 
	},
	methods: {
		search: function(){		
			if($("#partyCode").val() === ''){
				delete vm.queryParams.partyCode;
			}else{
				vm.queryParams.partyCode = $("#partyCode").val();
			}
			
			if($("#groupName").val() === ''){
				delete vm.queryParams.groupName;
			}else{
				vm.queryParams.groupName = $("#groupName").val();
			}		
			if($("#accountPeriodStatus select").val() === ''){
				delete vm.queryParams.accountPeriodStatus;
			}else{
				vm.queryParams.accountPeriodStatus = $("#accountPeriodStatus select").val();
			}
						
			vm.queryParams.page = 1;
			vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus
		},
		exportExcels: function(){
			var date_url = ykyUrl.pay+ "/v1/accountperiod/excel?" + $('#seachForm').serialize();
			//var date_url = 'http://localhost:27085'+ "/v1/accountperiod/excel?" + $('#seachForm').serialize();
			$("input[name=Authorization]").val($('#pageToken').val());
		    var form=$("#exportForm");//定义一个form表单
		    form.attr("action",date_url);
		    form.submit();//表单提交
		}
	}
})




function operatePromotion(index,params){
	console.log(index,params);
	var partyStatus = params[1] == 'PERIOD_ENABLED' ? 'PERIOD_ENABLED' : 'PERIOD_DISABLED';
	layer.confirm(
			  partyStatus =='PERIOD_DISABLED' ? '确定冻结客户账期 ? 冻结后将无法提交账期订单' 
					  : '确定取消冻结  ',
					  {
		offset: "auto",
		closeBtn: false,
		btn: ['确      定','取      消'], //按钮
		title: " ",
		area: 'auto',			
		move: false,
		skin: "up_skin_class red-btn",
		yes:function(){
			$.aAjax({
				url: ykyUrl.party+"/v1/enterprises/frozenCredit/"+params[0]+"/"+params[1],
				//url: 'http://localhost:27082'+"/v1/enterprises/frozenCredit/"+params[0]+"/"+params[1],
				type:"PUT",
				dataType: 'text',
				contentType:'application/json',
			    success: function(data) {
			    	vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus
			    	location.reload();
			    },
			    error:function(e){
			    	layer.msg("操作失败，请重试 ！")
			    }
			})
		}
	})
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