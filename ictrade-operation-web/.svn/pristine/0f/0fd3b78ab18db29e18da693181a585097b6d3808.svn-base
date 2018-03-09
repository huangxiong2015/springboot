var vm;
$(function() {
	var valid = false;
	var paymentMethodId = getQueryString('paymentMethodId');
	var vm = new Vue(
			{
				el : "#paymentEdit",
				data : {
					initData : {
						dev:'VALID',
						state:'VALID',
						description:'',
						parentPaymentMethodId:''
					},
					paymentMethodId:''
				},
				methods : {
					init : function(res) {
						this.initData.dev = res.dev;
						this.initData.state = res.state;
						this.initData.description = res.description;
						this.initData.parentPaymentMethodId = res.parentPaymentMethodId;
					},
				}
			});
	
	if (paymentMethodId) {
		syncData(ykyUrl.pay + '/payments/getPayMothodById?paymentMethodId=' + paymentMethodId, 'GET', null, function(res, msg) {vm.init(res); });
	}
	
	formValidate('#payment-from', {}, function(){
		var formArry = $('#payment-from').serializeObject();
		if (!paymentMethodId) {
			/*syncData(
					ykyUrl.pay + '/payments/updatePayMothod',
					"POST",
					formArry,
					function(data, err) {
						if (null != data) {
							layer.msg('保存成功！');
							document.location.href = operationWebUrl + "/PaymentMethodsList.htm";
						}
					});*/
				layer.msg('操作失败！');
		} else {
			formArry.paymentMethodId = paymentMethodId;
			syncData(
					ykyUrl.pay + '/payments/updatePayMothod',
					"POST",
					formArry,
					function(data, err) {
						if (null != data) {
							layer.msg('保存成功！');
							setTimeout(function() {
								document.location.href = operationWebUrl + "/PaymentMethodsList.htm";
							}, 500);
						}
					});
		}
	}); 
	
});

 
function goBack(){
	history.go(-1);
}