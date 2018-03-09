var app = new Vue({
	el: '#supplierDetail',
	data: {
		entName: '',
		agent: '',
		contactUserName: '',
		phone: '',
		createdDate: '',
		companyType:'',
		companyTypeList:{
			agent:'代理商',
			brand:'原厂'
		}
	},
	methods:{
		showCompantType:function(type){
			if(type == 'agent'){
				return '代理产品线：'
			}else if(type == 'brand'){
				return '原厂产品线：'
			}else{
				return '产品线：'
			}
		}
	}
});

$.ajax({
	url: ykyUrl.info + '/v1/recommendations/' + $('#detailId').val(),
	typ: 'GET',
	success: function(data) {
		if(data) {
			app.entName = data.extend1;
			app.agent = data.contentMap.agent;
			app.contactUserName = data.contentMap.contactUserName;
			app.phone = data.contentMap.phone;
			app.createdDate = data.createdDate;
			app.companyType = data.extend2;
		}
	}
})

