
String.prototype.replaceAll = function(s1,s2){ 
	return this.replace(new RegExp(s1,"gm"),s2); 
}
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
		approvedProof: '',
		specified: true,
		vendorList: [],
		brandList: [],
		categoryList: [],
		useLimitPerCustomer: '',
		useProductType: 1,
		businessType: 'PERSON_REG',
		couponProductList: [],
		approvedProofShow: '',
		fileName: '',
		fileUrl: '',
		vendorNames: [],
		vendorIds: '',
		vendorNameString: '',
		vendorToggle: false,
		brandNames: [],
		brandIds: '',
		brandNameString: '',
		brandToggle: false,
		cateNames: [],
		categoryNames:[],
		categoryIds: '',
		categoryNameString: '',
		categoryToggle: false,
		activityUrl: '',
		discountNumberFormated:undefined,
		vendorDatakeyname: '选择供应商', //供应商组件
		vendorDatavalidate:{
            "required": true
        },
        vendorDataid:'selbox',
        vendorDataname:'selbox',
        //api :　componentApiURL + 'data.json',
        vendorDataoptions:[

        ],
        vendorDataoptionId: 'id',
        vendorDataoptionName: 'name',
        vendorDataselected:[],
        vendorDatamultiple:true,   //复选
        vendorDataplaceholder:'搜索供应商',
        vendorDatasels:[],
        brandDatakeyname: '选择制造商',
        brandDatavalidate:{
            "required": true
        },
        brandDataid:'selbox',
        brandDataname:'selbox',
        brandDataoptions:[
                 
        ],
        brandDataoptionId: 'id',
        brandDataoptionName: 'brandName',
        brandDataselected:[],
        brandDatamultiple:true,   //复选
        brandDataplaceholder:'搜索制造商',
        brandDatasels:[],
        brandDataFuzzySearch:true,
        brandDataReloadApi:ykyUrl.product + '/v1/products/brands/alias?keyword={selbox}',
        showBrandData:false,
        showCateData:false,
        api: ykyUrl.product+'/v1/products/categories/list',
        VendorInitData:[],
        isCategoryBoolean:false,
        vendorCompomentchildren:'children',
        vendorCompomentid:'_id',
        vendorCompomentname:'cateName',
        lemonTabCheckedList:[],
        activeVendor:[],
        activeBrand:[],
        activeCate:[]
	},
	beforeCreate:function(){
	},
	created:function(){
		
	},
	methods: {
		vendorDatagetSelected: function(obj){  //获取选中值
	         this.vendorDatasels=obj;
	      },
	      vendorDatadel: function (id) {
	          vm.$refs.vendorDataletter.del(id);
	          //vm.$refs.letter.letterReload();
	      },
	      getLemonTabCheckedList:function(obj){
	    	  this.lemonTabCheckedList = obj;
	      },
	      chooseVendor:function(){
	    	  var that = this;
	    	  layer.open({
					type: 1,
					title: '请选择供应商',
					area: ['1000px','500px'],
					offset: '100px',
					move: false,
					skin: "s_select_data",
					content: $("#vendorApp"),
					btn: ['确认', '取消'],
					yes:function(){
						layer.closeAll();
						that.changeVendor();
					},
					cancel:function(){
						that.vendorDatasels = JSON.parse(JSON.stringify(that.activeVendor));
						that.vendorDataselected = that.vendorIds.split(",");
					}
				})
	      },
	      changeVendor:function(){
	    	  var that = this;
		      var vendorNameArr = [];
		      var vendorIdArr = [];
	    	  $.each(that.vendorDatasels,function(i,item){
	    		  vendorNameArr.push(item.name);
	    		  vendorIdArr.push(item.id);
	    	  })
	    	  this.activeVendor = JSON.parse(JSON.stringify(that.vendorDatasels));
	    	  this.vendorIds = vendorIdArr.join(',');
	    	  this.vendorNames = vendorNameArr;
		      this.vendorNameString = vendorNameArr.join(', ');
	      },
			brandDatagetSelected: function(obj){  //获取选中值
	         this.brandDatasels=obj;
	      },
	      brandDatadel: function (id) {
	          vm.$refs.brandDataletter.del(id);
	          //vm.$refs.letter.letterReload();
	      },
	      chooseBrand:function(){
	    	  var that = this;
	    	  this.showBrandData = true;
	    	  layer.open({
					type: 1,
					title: '请选择制造商',
					area: ['1000px','500px'],
					offset: '100px',
					move: false,
					skin: "s_select_data",
					content: $("#brandApp"),
					btn: ['确认', '取消'],
					yes:function(){
						layer.closeAll();
						that.changeBrand();
						that.showBrandData = false;
					},
					cancel:function(){
						that.showBrandData = false;
						that.brandDatasels = JSON.parse(JSON.stringify(that.activeBrand));
						var ids = that.brandIds.split(",");
						var arr = [];
						for(var i=0;i<ids.length;i++){
							arr.push(Number(ids[i]));
						}
						that.brandDataselected = arr;
					}
				})
	      },
	      changeBrand:function(){
	    	  var that = this;
		      var brandNameArr = [];
		      var brandIdArr = [];
	    	  $.each(that.brandDatasels,function(i,item){
	    		  brandNameArr.push(item.brandName);
	    		  brandIdArr.push(item.id);
	    	  })
	    	  this.activeBrand = JSON.parse(JSON.stringify(that.brandDatasels));
	    	  this.brandIds = brandIdArr.join(',');
	    	  this.brandNames = brandNameArr;
		      this.brandNameString = brandNameArr.join(', ');
		      this.brandDataselected = brandIdArr;
	      },
	      categorySelect:function(){
				var that = this;
				this.showCateData = true;
				layer.open({
					type: 1,
					title: '选择类别',
					area: ['900px','600px'],
					offset: '50px',
					move: false,
					skin: "",
					content: $("#categoryWrap"),
					btn: ['确认', '取消'],
					yes:function(){
						layer.closeAll();
						that.changeCategory();
						that.showCateData = false;
					},
					cancel:function(){
						that.showCateData = false;
						that.VendorInitData = JSON.parse(JSON.stringify(that.activeCate));
					}
				})
			},
			changeCategory:function(){
		    	  var that = this;
			      var categoryList = vm.lemonTabCheckedList;
			      this.activeCate = JSON.parse(JSON.stringify(this.lemonTabCheckedList));
			      this.VendorInitData = JSON.parse(JSON.stringify(this.lemonTabCheckedList));
			      var cateList =[];
			      var cateNameList = [];
			      $.each(categoryList,function(i,item){
			    	  cateList.push({
			    		  id:item.id,
			    		  name:item.name
			    	  })
			    	  cateNameList.push(item.name);
			      });
			      that.categoryNames = cateNameList;
			      that.categoryList = cateList;
			      that.categoryNameString = (that.categoryNames.join(",")).replaceAll("/","-");
		      },
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
		saveData: function(){
			var couponId = getQueryString("id");
			if(!/^[0-9]*[1-9][0-9]*$/.test(vm.initData.totalQty)){
				layer.msg('发行量只能是正整数');
				return false;
			}
			
			if(!Number(vm.initData.totalQty)){
				layer.msg('请输入正确的发行量');
				return false;
			}
			
			if(Number(vm.initData.totalQty)> Number(vm.original)){
				popChangeQtyFrame(couponId);
			}else{
				layer.msg('发行量不能小于上一次的'+vm.original+'张');
			}
			
			
		},
		uploadProducts: function(){	
			layer.open({
				type: 1,
				title: '添加商品',
				area: '700px',
				offset: '300px',
				move: false,
				skin: "s_select_data",
				content: $("#file-upload"),
				btn: ['保存', '取消'],
				yes: function(index, layero) {
					if(vm.fileName === ''){
						layer.msg('请选择文件！')
						return false;
					}
					var index = layer.load(1, {
					  shade: [0.1,'#fff'] //0.1透明度的白色背景
					});
					
					$.aAjax({
						url: ykyUrl.pay+'/v1/coupons/parseProductsFile?couponId='+vm.id+'&fileUrl='+vm.fileUrl,
						type: 'POST',
						success: function(res){
							layer.close(index);
							var dataList = [];
							for(var i =0; i< res.productList.length; i++){
								var item = res.productList[i];
								dataList.push(eval('('+item+')'));
							}
							vm.couponProductList = dataList;
							vm.fileName='';
							vm.fileUrl='';
							layer.closeAll();
						},
						error: function(err){
							layer.close(index);
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
				},
				cancel: function(index, layero) {
					vm.fileName='';
					vm.fileUrl='';
				}
			})	
		},
		deleteProduct: function(i){
			vm.couponProductList.splice(i, 1);
		},
		toDefaultUrl: function(){
			return ykyUrl.portal+'/search/inventory/';
		}
	},
	watch: {//深度 watcher

	},
})

var popChangeQtyFrame = function(id){
	var num = vm.initData.totalQty;
	var inputLayer = layer.open({
		type: 1,
	  	title: "提示信息", //不显示标题
		shade: 0.5,
		btn: ['确定', '取消'],
		offset: '300px',
		area: ['620px', '180px'],
		skin:"up_skin_class",
		content: "<div class='layer_cont'>确定将发行量修改为<span>"+num+"</span>张吗？</div>",
		yes:function(){
			changeTotalQty(id,num,inputLayer);
		},
		cancel: function(){
			
		}
	})
}

var changeTotalQty = function(id,total,inputLayer){
	vm.submiting = true;
	var param ={
		"couponId": id,
		"totalQty": total,
		'activityUrl': vm.initData.activityUrl
	}
	
	param.useProductType = vm.useProductType;
	if(vm.useProductType === 0){
		var brandIdList = vm.brandIds.split(",");
		var brandNameList = vm.brandNameString.split(",");
		var vendorList = vm.vendorIds.split(",");
		var vendorNameList = vm.vendorNameString.split(",");
		var productTypeIdList = vm.categoryIds.split(",");
		var productTypeName = vm.categoryNameString.split(",");
		var brands = [];
		var vendors = [];
		$.each(brandIdList,function(i,item){
			var temp ={
				id: item,
				name:brandNameList[i]
			}
			brands.push(temp);
		})
		$.each(vendorList,function(i,item){
			var tempVendor ={
				id: item,
				name:vendorNameList[i]
			}
			vendors.push(tempVendor);
		})
		param.brand = JSON.stringify(brands);
		param.vendor = JSON.stringify(vendors);
		var categoryListTemp = vm.categoryList;
		var filtertedList = [];
		var categoryListTempLength = categoryListTemp.length;
		if(categoryListTempLength>0){
			for(i = 0; i < categoryListTempLength; i++){
			    for(j = i + 1; j < categoryListTempLength; j++){
			      if(categoryListTemp[i].id === categoryListTemp[j].id){
			        j = ++i;
			      }
			    }
			    filtertedList.push(categoryListTemp[i]);
			  }
		}
		param.category = JSON.stringify(filtertedList); 
	}else{
		if(vm.couponProductList.length === 0){
			layer.msg('请上传指定商品！')
			vm.submiting = false;
			layer.close(inputLayer);
			return false;
		}else{
			param.couponProductList = [];
			for(var i =0; i<vm.couponProductList.length; i++){
				if(vm.couponProductList[i].id === ''){
					layer.msg('商品信息中含有不合法数据，请删除！')
					vm.submiting = false;
					layer.close(inputLayer);
					break;
				}
				var item = {
					productId: vm.couponProductList[i].id,
					manufacturer: vm.couponProductList[i].spu.manufacturer,
					manufacturerPartNumber: vm.couponProductList[i].spu.manufacturerPartNumber,
					sourceId: vm.couponProductList[i].sourceId,
					vendorId: vm.couponProductList[i].vendorId
				};
				param.couponProductList.push(item);
			}
		}
	}
	
	if(!vm.submiting){
		return false;
	}
	
	$.aAjax({
		url: ykyUrl.pay + "/v1/coupons/updateTotalQtyById",
     	type:"POST",
     	data:JSON.stringify(param),
     	dataType:"json",
     	success: function(data) {
 			layer.close(inputLayer);
 			layer.msg("优惠券总发行量修改成功 ！")
 			window.location.href = location.origin + ykyUrl._this + '/coupon.htm'	
     	},
	    error:function(e){
	    	layer.msg("系统错误，审批失败，请联系系统管理员："+e.responseText);
	    }
 	});
}

function getDetail(id,action){
	var detailUrl = ykyUrl.pay+"/v1/coupons/detail/"+id;
	$.aAjax({
		url: detailUrl,
		type:"GET",
	    success: function(data) {
	    	if(data){
	    		vm.original = data.totalQty;
	    		if(data.discountNumber&&data.discountNumber!=""){
	    			vm.discountNumberFormated = numMulti(data.discountNumber,10);
	    		}
	    		vm.initData = data;
	    		vm.useProductType = vm.initData.useProductType;
	    		if(vm.initData.categoreList|| vm.initData.vendorList || vm.initData.brandList){

	    			
	    			if(vm.initData.vendorList.length==0){
	    				vm.vendorNames = [];
	    			}else{
	    				var vendorArr = vm.initData.vendorList;
	    				var vendorIds = [];
	    				var vendorName = [];
	    				$.each(vendorArr,function(i,item){
	    					vendorIds.push(item.firstClassId);
	    					vendorName.push(item.firstClassName);
	    				})
	    				
	    				vm.vendorNames = vendorName;
	    				vm.vendorIds = vendorIds.join(',');;
		    			vm.vendorNameString = vendorName.join(',');
		    			vm.vendorDataselected=vendorIds;
	    			}
	    			
	    			if(vm.initData.brandList.length ==0){
	    				vm.brandNames = [];
	    			}else{
	    				var brandArr = vm.initData.brandList;
	    				var brandIds = [];
	    				var brandName = [];
	    				$.each(brandArr,function(i,item){
	    					var obj = {
	    							id:item.firstClassId,
	    							brandName:item.firstClassName
	    					}
	    					brandIds.push(Number(item.firstClassId));
	    					brandName.push(item.firstClassName);
	    					vm.brandDatasels.push(obj);
	    					vm.activeBrand.push(obj);
	    				})
	    				vm.brandNames = brandName;
	    				vm.brandIds = brandIds.join(',');
		    			vm.brandNameString = brandName.join(',');;
		    			vm.brandDataselected=brandIds;
	    			}
	    			if(vm.initData.categoreList.length >0){
	    				var cateList = vm.initData.categoreList;
	    				var cateNameList = [];
	    				var tempList = [];
	    				$.each(cateList,function(i,item){
	    					var cateItem = {
	    						'id':item.firstClassId+(item.secondClassId?'/'+item.secondClassId:''),
	    						'name':item.firstClassName+(item.secondClassName?'/'+item.secondClassName:'')
	    					}
	    					var nameItem =item.firstClassName+(item.secondClassName?'/'+item.secondClassName:'');
	    					tempList.push(cateItem);
	    					cateNameList.push(nameItem);
	    				})
	    				console.log( tempList,1);
	    				vm.VendorInitData = tempList;
	    				vm.activeCate = JSON.parse(JSON.stringify(tempList));
	    				vm.categoryList =tempList;
	    				console.log( tempList,2);
	    				
	    				vm.categoryNames=cateNameList;
	    				vm.categoryNameString = (vm.categoryNames.join(',')).replaceAll("/",'-');
	    			}
	    			vm.isCategoryBoolean = true;
	    			
	    		}
	    		getPrivateUrl(data);
	    		if(vm.action == 'edit'){
	    			getBrand();
		    		getSupplier();
		    		/*getCategory();*/
	    		}
	    		if(data.useProductType == 1){
	    			getProductList(vm.id);
	    		}
	    	}
	    },
	    error:function(e){
	    	layer.msg("系统错误，审批失败，请联系系统管理员："+e.responseText)
	    }
	});
}

function getBrand(){
	var brandUrl =ykyUrl.product +"/v1/products/brands";
	$.aAjax({
		url: brandUrl,
		type:"GET",
		async: false,
	    success: function(data) {
	    	vm.brandList = data;
	    	var tempbrandList = [];
	    	$.each(data,function(i,item){
	    		tempbrandList.push({
	    			'id':item.id,
	    			'brandName':item.brandName
	    		})
	    	})
	    	vm.brandDataoptions= tempbrandList;
	    },
	    error:function(e){
	    	layer.msg("系统错误，审批失败，请联系系统管理员："+e.responseText)
	    }
	});
}

function getSupplier(){
	var supplierUrl =ykyUrl.party +"/v1/party/allparty";
	var params = {
		   "roleType": "SUPPLIER",
		    "status": "PARTY_ENABLED"
		}
	$.aAjax({
		url: supplierUrl,
		type:"PUT",
		data:JSON.stringify(params),
		async: false,
		contentType:'application/json',
		dataType:'json',
	    success: function(data) {
	    	vm.vendorList = data;
	    	var tempVendorList = [];
	    	$.each(data,function(i,item){
	    		tempVendorList.push({
	    			'id':item.id,
	    			'name':item.partyGroup.groupName
	    		})
	    	})
	    	vm.vendorDataoptions= tempVendorList;
	    },
	    error:function(e){
	    	layer.msg("系统错误，审批失败，请联系系统管理员："+e.responseText)
	    }
	});
}

function getProductList(id){
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
			vm.fileName='';
			vm.fileUrl='';
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

function getLogList(id){
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

function getPrivateUrl(dataParam){
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
	    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
	    }
 	});
}


function initUI(id,action){
	if("edit"==action){
		$(document).attr("title","编辑优惠券页");
	}else if("examine"==action){
		$(document).attr("title","修改优惠券页");
	}
	if(id){
		getDetail(id,action);
		getLogList(id);
	}else{
		console.log("id为空,系统错误，审批失败，请联系系统管理员：");
	}
}

$(function(){
	var databaseTotalQty;
	vm.id = getQueryString("id");
	vm.action = location.href.indexOf('edit') > -1 ? 'edit': 'examine';
	initUI(vm.id,vm.action);
})

var uploadProduct = createUploader({
	buttonId: "uploadProductFile", 
	uploadType: "coupon.product", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "xls,xlsx,csv",//可允许上传文件的类型
	fileSize: "5mb", //最多允许上传文件的大小
	isImage: false, //是否是图片
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.fileName = file.name;
				vm.fileUrl = _fileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploadProduct.init(); 



