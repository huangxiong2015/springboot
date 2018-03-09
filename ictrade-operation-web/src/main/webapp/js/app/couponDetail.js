var vm = new Vue({
	el: '#editCoupon',
	data: {
		initData: '',
		id: '',
		action: '',
		logList: '',
		logListNull: false,
		specified: false,
		profImg: '',
		original: '',
		couponProductList: [],
	},
	methods: {
		showBigImg: function(){
			if(vm.profImg !=''){
				layer.open({
					  type: 1,
					  title: false,
					  closeBtn: 0,
					  area: ['800px', '500px'],
					  offset: '100px',
					  shadeClose: true,
					  content: $('.originalImage')
				});
			}
		},
		toDefaultUrl: function(){
			return ykyUrl.portal+'/search/inventory/';
		}
	}
})

var getProductList = function(id){
	$.aAjax({
		url: ykyUrl.pay+'/v1/coupons/parseProductsFile?couponId='+id,
		type: 'POST',
		success: function(res){
			var dataList = [];
			for(var i =0; i< res.productList.length; i++){
				var item = res.productList[i];
				dataList.push(eval('('+item+')'));
			}
			vm.couponProductList = dataList;
		},
		error: function(err){
			var errorTop = layer.open({
				type: 1,
				title: '错误',
				area: ['700px', '300px'],
				offset: '300px',
				move: false,
				skin: "s_select_data",
				content: err.responseJSON.errMsg,
				btn: ['确定'],
				yes: function(index, layero){
					layer.close(errorTop);
				}
			});
		}
	});
}

function toName(list){
	if(list.length==0||list==""||list==undefined){
		return "不限";
	}else if(list&&list.length>0){
		var cateList = [];
		$.each(list,function(i,item){
			var cateName = item.firstClassName+(item.secondClassId?"-"+item.secondClassName:"");
			cateList.push(cateName);
		})
		var categoryString = cateList.join();
		return categoryString;
	}
}
var getDetail = function(id){
	var detailUrl = ykyUrl.pay+"/v1/coupons/detail/"+id;
	$.aAjax({
		url: detailUrl,
		type:"GET",
	    success: function(data) {
	    	if(data){
	    		vm.original = data.totalQty;
	    		var tempInitData = data;
	    		if(data.brandList){
	    			tempInitData.brandListString = toName(data.brandList);
	    		}
	    		if(data.vendorList){
	    			tempInitData.vendorListString = toName(data.vendorList);
	    		}
	    		if(data.categoreList){
	    			tempInitData.categoreListString = toName(data.categoreList);
	    		}
	    		vm.initData = tempInitData;
	    		if(data.useProductType == 1){
	    			getProductList(vm.id);
	    		}
	    		getPrivateUrl(data);
	    	}
	    },
	    error:function(e){
	    	layer.msg("系统错误，审批失败，请联系系统管理员："+e.responseText)
	    }
	});
}

var getLogList = function(id){
	var logListUrl = ykyUrl.pay+"/v1/coupons/getCouponStatusList/"+id+"?actionName=EDIT";
	$.aAjax({
		url: logListUrl,
		type:"GET",
	    success: function(data) {
	    	vm.logList = data;
	    	if(data.length>0){
	    		vm.logListNull = true;
	    	}else{
	    		vm.logListNull = false;
	    	}
	    },
	    error:function(e){
	    	layer.msg("系统错误，审批失败，请联系系统管理员："+e.responseText)
	    }
	});
}

var getPrivateUrl =  function(dataParam){
	var temp = dataParam;
	$.aAjax({
		url: ykyUrl.party + "/v1/enterprises/getImgUrl",
     	type:"POST",
     	data:JSON.stringify({"id":temp.approvedProof}),
     	success: function(data) {
     		if(null !=data || data == ""){
     			vm.profImg = data;
     		}
     	},
     	error:function(e){
     		couponData.data = temp;
     		couponData.yetAnotherData = temp;
	    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
	    }
 	});
}


var initUI = function(id){
	if(id){
		getDetail(id);
		getLogList(id);
	}else{
		console.log("id为空,系统错误，审批失败，请联系系统管理员：");
	}
}
$(function(){
	var databaseTotalQty;
	vm.id = getQueryString("id");
	initUI(vm.id);
})
