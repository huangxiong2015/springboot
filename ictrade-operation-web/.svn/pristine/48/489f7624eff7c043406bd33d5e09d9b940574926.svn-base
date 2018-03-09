var vm;
$(function() {
	var valid = false;
	var partyId = getQueryString('partyId');
	var vm = new Vue(
			{
				el : "#shipmentEdit",
				data : {
					initData : {
						'groupName':'',
					},
					partyId:''
				},
				methods : {
					init : function(res) {
						this.initData.groupName = res.groupName;
						this.partyId = res.partyId;
					},
				}
			});
	
	if (partyId) {
		syncData(ykyUrl.party + '/v1/carrier/' + partyId, 'GET', null, function(res, msg) {vm.init(res); });
	}
	
	formValidate('#manu-from', {}, function(){
		var formArry = $('#manu-from').serializeObject();
		if (!partyId) {
			syncData(
					ykyUrl.party + '/v1/carrier/',
					"PUT",
					formArry,
					function(data, err) {
						if (null != data) {
							layer.msg('保存成功！');
							setTimeout(function() {
								document.location.href = operationWebUrl + "/shipmentlist.htm";
							}, 500);
						}
					});
		} else {
			//formArry.partyId = partyId;
			syncData(
					ykyUrl.party + '/v1/carrier/' + partyId,
					"PUT",
					formArry,
					function(data, err) {
						if (null != data) {
							layer.msg('保存成功！');
							setTimeout(function() {
								document.location.href = operationWebUrl + "/shipmentlist.htm";
							}, 500);
						}
					});
		}
	}); 
	
});

 
function goBack(){
	history.go(-1);
}