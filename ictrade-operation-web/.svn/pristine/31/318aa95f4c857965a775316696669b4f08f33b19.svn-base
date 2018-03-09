$(function(){ 
	  var vm = new Vue({
	        el: '#manufacturer-detail',
	        data: { 
	        	manuData:{},
  				logData:{}
	        }
	   });
	  var brandid = getQueryString('id');
	  if(brandid){
		  syncData(ykyUrl.product + "/v1/products/brands/" + brandid , "get", null, function (data, err) {
	          if (data) {
	        	  vm.manuData = data;  
	          }  
	      });
		  var data={
				  actionId : brandid,
				  actionRst : 'SUCCEEDED',
				  actions: 'Brand Modify',
				  applicationCodes: 'PRODUCT'
		  }
		  syncData(ykyUrl.party + "/v1/audit" , "get", data, function (data, err) {
	          if (data) {
	        	  vm.logData = data.list;  
	          } 
	      });
	  }
});