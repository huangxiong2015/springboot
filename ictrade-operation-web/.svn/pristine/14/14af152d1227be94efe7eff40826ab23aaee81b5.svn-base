var app = new Vue({
	el: '#detail',
	data: {
		id:getQueryString("id"),
		info:{
			name:'',
			telNumber:'',
			fixedTel:'',
			mail:'',
			qq:'',
			personalTitle:'',
			provinceName:'',
			cityName:'',
			countryName:'',
			address:''
		},
		positionList:{
			buyer:'采购员',
			purchasingManager:'采购经理',
			engineer:'工程师',
			other:'其他'
		}
	},
	created: function() {
		var _this = this;
		syncData(ykyUrl.party + '/v1/customers/' + _this.id, 'GET', null, function(data){
			if(data) {
				_this.info =$.extend({},_this.info,data);				
			}
		})
	},
	methods:{
		showAddress:function(){
			var info = this.info;
			if(!info.provinceName && !info.cityName && !info.countryName && !info.address){
				return '-'
			}else{
				var str = info.provinceName + info.cityName + info.countryName + info.address;
				return str;
			}
		}
	}	       	
})