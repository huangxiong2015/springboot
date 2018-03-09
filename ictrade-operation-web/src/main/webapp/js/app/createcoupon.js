/**
 * create by roy.he@yikuyi.com at 2017-7-24
 */

/*js float类型乘法精度*/
function accMul(arg1,arg2)
{
	var m=0,s1=arg1.toString(),s2=arg2.toString();
	try{m+=s1.split(".")[1].length}catch(e){}
	try{m+=s2.split(".")[1].length}catch(e){}
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}
String.prototype.replaceAll = function(s1,s2){ 
return this.replace(new RegExp(s1,"gm"),s2); 
}
var validate;
var vm = new Vue({
	el: '#createCoupon',
	data: {
		couponCateList: [{
			value: 'PLATFORM_PROMO',
			text:'活动推广劵',
		},{
			value: 'EXACT_PROMO',
			text:'定向推广劵',
		},{
			value: 'OFFLINE_PROMO',
			text:'地推类型劵',
		}],
		couponCate: 'PLATFORM_PROMO',
		name: '',
		thruTypeIdList: [{
			value: 'EXACT_DATE_ZONE',
			text: '限时领取'
		},{
			value: 'FROM_GET_DATE',
			text: '限时使用'
		}],
		couponTypeList:[{
			value: 'DEDUCTION',
			text: '抵扣劵'
		},{
			value: 'DISCOUNT',
			text: '折扣劵'
		}],
		thruTypeId: 'EXACT_DATE_ZONE',
		couponType:'DEDUCTION',
		discountNumber:'',
		discountNumberFormat: '',
		limitMonth: '',
		thruDate: '',
		fromDate: '',
		unitAmount: '',
		couponCurrency: 'CNY',
		totalQty: '',
		couponPartyType: 'UNLIMIT',
		couponMethodType: 'AUTO_SEND',
		consumeLimitAmount: '',
		couponText: '',
		approvedProof: '',
		specified: true,
		vendorList: [],
		brandList: [],
		categoryList: [],
		generalClass:[],
		subClassList:[],
		currentClassId:'',
		useLimitPerCustomer: '',
		useProductType: 1,
		businessType: 'ACTIVITY_ORDER',
		couponProductList: [],
		approvedProofShow: '',
		fileName: '',
		fileUrl: '',
		submiting: false,
		vendorModel: [],
		vendorNames: [],
		vendorIds: '',
		vendorNameString: '',
		vendorToggle: false,
		brandModel: [],
		brandNames: [],
		brandIds: '',
		brandNameString: '',
		brandToggle: false,
		categoryNames: [],
		categoryIds: '',
		categoryNameString: '',
		activityUrl: '',
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
        initData: [],
        categoryList:[],
        lemonTabCheckedList:[],
        activeVendor:'',
        activeBrand:'',
        activeCate:[]
	},
	created: function(){
		getSupplier();
		getBrand();
	},
	methods: {
		  vendorDatagetSelected: function(obj){  //获取选中值
	         this.vendorDatasels=obj;
	          console.log(obj);
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
		          console.log(obj);
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
					that.initData = JSON.parse(JSON.stringify(that.activeCate));
				}
			})
		},
		changeCategory:function(){
	    	  var that = this;
		      var categoryList = vm.lemonTabCheckedList;
		      this.activeCate = JSON.parse(JSON.stringify(this.lemonTabCheckedList));
		      this.initData = JSON.parse(JSON.stringify(this.lemonTabCheckedList));
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
		saveData: function(){
			vm.submiting = true;
			var postData = {};
			postData.couponCate = vm.couponCate;
			postData.name = vm.name;
			/*postData.unitAmount = vm.unitAmount;*/
			postData.couponCurrency = vm.couponCurrency;
			postData.consumeLimitAmount = vm.consumeLimitAmount;
			postData.totalQty = vm.totalQty;
			postData.couponText = vm.couponText;
			postData.couponPartyType = vm.couponPartyType
			postData.couponMethodType = vm.couponMethodType
			postData.thruTypeId = vm.thruTypeId;
			postData.activityUrl = vm.activityUrl;
			postData.couponType = vm.couponType;	
			$('#createCouponForm').submit();
			if(!validate){
				vm.submiting = false;
				return false;
			}
			
			if(vm.thruTypeId === 'EXACT_DATE_ZONE'){
				if(!vm.fromDate || vm.fromDate === '' || !vm.fromDate || vm.fromDate === ''){
					layer.msg('起始时间和结束时间不能为空');
					vm.submiting = false;
					return false;
				}else{
					postData.fromDate = vm.fromDate;
					postData.thruDate = vm.thruDate;
				}
			}else if(vm.thruTypeId === 'FROM_GET_DATE'){
				if(vm.limitMonth === ''){
					layer.msg('有效时间不能为空');
					vm.submiting = false;
					return false;
				}else{
					postData.limitMonth = vm.limitMonth;
				}
			}
			
			//折扣形式验证
			if(vm.couponType === 'DEDUCTION'){  //抵扣
				if(!vm.unitAmount || vm.unitAmount === ''){
					layer.msg('面值不能为空');
					vm.submiting = false;
					return false;
				}else{
					postData.unitAmount = vm.unitAmount;
					/*postData.discountNumber = "";*/
				}
			}else if(vm.couponType === 'DISCOUNT'){ //折扣
				
				var reg = /^0\.(0[1-9]|[1-9]{1,2})$/;						
				var value = $.trim(vm.discountNumber);
				if(!reg.test (Number(value))){
					$("#discountNumber").addClass('error');
					layer.msg('折扣格式不对');
					vm.submiting = false;
					return false;
				}else{
					$("#discountNumber").removeClass('error');
					postData.discountNumber = Number(vm.discountNumber);
					postData.unitAmount = "";
				}
				
			}
			
			if(vm.approvedProof === ''){
				layer.msg('请上传优惠券申请凭证');
				vm.submiting = false;
				return false;
			}else{
				postData.approvedProof = vm.approvedProofShow;
			}
			
			if(vm.couponMethodType === 'AUTO_SEND'){
				postData.businessType = vm.businessType
			}
			
			if(!$('#useLimitPerCustomer input[value="0"]').prop('checked') && vm.useLimitPerCustomer == '0'){
				layer.msg('每用户限领不能为空')
				vm.submiting = false;
				return false;
			}else if($('#useLimitPerCustomer input[value="0"]').prop('checked')){
				postData.useLimitPerCustomer = '0';
				vm.useLimitPerCustomer='';
			}else{
				if(parseInt(vm.useLimitPerCustomer) <= 0){
					layer.msg('每用户限领不能小于0')
					vm.submiting = false;
					return false;
				}else{
					postData.useLimitPerCustomer = vm.useLimitPerCustomer;
				}
			}
			
			if(Number(vm.unitAmount) > Number(vm.consumeLimitAmount)&&vm.couponType === 'DEDUCTION'){
				layer.msg('使用限额必须大于等于单张面值');
				vm.submiting = false;
				return false;
			}
			
			if(Number(vm.totalQty) < Number(vm.useLimitPerCustomer)){
				layer.msg('总发行量必须大于等于每用户限领张数');
				vm.submiting = false;
				return false;
			}
			postData.useProductType = vm.useProductType;
			if(vm.useProductType === 0){     //指定上传
				var brandIdList = vm.brandIds.split(",");
				var brandNameList = vm.brandNameString.split(",");
				var vendorList = vm.vendorIds.split(",");
				var vendorNameList = vm.vendorNameString.split(",");
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
				postData.brand = JSON.stringify(brands);
				postData.vendor = JSON.stringify(vendors);
				postData.category = JSON.stringify(vm.categoryList);
			}else{
				if(vm.couponProductList.length === 0){
					layer.msg('请上传指定商品！')
					vm.submiting = false;
					return false;
				}else{
					postData.couponProductList = [];
					for(var i =0; i<vm.couponProductList.length; i++){
						if(vm.couponProductList[i].id === ''){
							layer.msg('商品信息中含有不合法数据，请删除！')
							vm.submiting = false;
							break;
						}
						var item = {
							productId: vm.couponProductList[i].id,
							manufacturer: vm.couponProductList[i].spu.manufacturer,
							manufacturerPartNumber: vm.couponProductList[i].spu.manufacturerPartNumber,
							sourceId: vm.couponProductList[i].sourceId,
							vendorId: vm.couponProductList[i].vendorId
						};
						postData.couponProductList.push(item);
					}
				}
			}
			
			if(!vm.submiting){
				return false;
			}
			
			syncData(ykyUrl.pay + "/v1/coupons/addCoupon", 'POST', postData,
				function(res, err) {
					vm.submiting = false;
					if (res.code === '200') {
						location.href = location.origin + ykyUrl._this + '/coupon.htm';
					}else{
						layer.msg('创建失败！');
					}
				});
		},
		cancel: function(){
			history.back(-1)
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
						url: ykyUrl.pay+'/v1/coupons/parseProductsFile?fileUrl='+vm.fileUrl,
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
		couponCateChange: function(){
			if(vm.couponCate === 'PLATFORM_PROMO'){
				vm.couponMethodType = 'AUTO_SEND';
				this.$nextTick(function () {
					$('input[value="AUTO_SEND"]').prop('checked', true);
				})
			}else{
				vm.couponMethodType = 'EXACT_SEND';
				this.$nextTick(function () {
					$('input[value="EXACT_SEND"]').prop('checked', true);
				})		
			}					
		},
		showBigImg: function(){
			if(vm.approvedProof !=''){
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
		deleteProduct: function(i){
			vm.couponProductList.splice(i, 1);
		}
	},
	watch: {//深度 watcher
	  discountNumber:function (val, oldVal) {
		var reg = /^0\.(0[1-9]|[1-9]{1,2})$/;						
		var value = $.trim(val);
		if(reg.test (Number(value))){
			if(value<0.7){
				$("#discountNumber").parent("div").append($("<em id='tooLowDiscount' style='margin-left:135px' class='error icon-triangle-leftmin'>折扣超大、请确认设置是否正确</em>"));
				
			}else if(value>=0.7){
				$("#tooLowDiscount").remove();
			}
		}else{
			$("#tooLowDiscount").remove();
		}
		
		var re = /^0\.(0[1-9]|[1-9]{1,2})$/;
		if(re.test(val)){
			var valNum = parseFloat(val);
			this.discountNumberFormat = accMul(valNum,10);
		}else{
			this.discountNumberFormat = val;
		}
	  }
	},
	
})


$('#createCouponForm').validate({
	rules: {
		name: {
			required: true,
			maxlength: 30,
		},
		/*unitAmount: {
			required: true,
			min: 1,
			digits: true,
		},*/
		totalQty: {
			required: true,
			min: 1,
			digits: true,
		},
		couponText: {
			required: true,
			maxlength: 100,
		},
		consumeLimitAmount: {
			required: true,
			min: 1,
			digits: true,
		},
		activityUrl: {
			url: true
		}
	},
	messages: {
		name: {
			required: '优惠券名称不能为空',
			maxlength: '优惠券名称不能超过30个字'
		},
		/*unitAmount: {
			required: '面值不能为空',
			min: '面值必须大于1的整数',
			digits: '面值必须大于1的整数'
		},*/
		totalQty: {
			required: '发行量不能为空',
			min: '发行量必须大于1的整数',
			digits: '发行量必须大于1的整数'
		},
		couponText: {
			required: '请填写优惠券备注',
			maxlength: '备注最多不能超过100字'		
		},
		consumeLimitAmount: {
			required: '使用限额不能为空',
			min: '使用限额必须大于1的整数',
			digits: '使用限额必须大于1的整数'	
		},
		activityUrl: {
			url: '请输入正确的链接'
		}
	},
	errorElement: "em",
	success: function(label) {
		$(label).parent().find('.tip').remove();
		$(label).removeClass("error icon-triangle-leftmin"); // speech-bubble speech-bubble-left

		$(label).addClass("icon-confirm");
		if ($(label).parent().find('input').val() === '') {
			$(label).removeClass("icon-confirm");
		}
	},
	errorPlacement: function(error, element) {
		var f = error[0].getAttribute("for");
		var ele = $("em[for=" + f + "]");
		if (ele.length > 0) {
			ele[0].remove();
		}
		element.parent().find('.tip').remove();
		error.appendTo(element.parent());
		$(error).addClass("error icon-triangle-leftmin");
		validate = false;
	},
	submitHandler: function(form) { // 表单提交时修改校验状态
		validate = true;
	}
})

function getBrand(){
	var brandUrl =ykyUrl.product +"/v1/products/brands";
	$.aAjax({
		url: brandUrl,
		type:"GET",
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



function init(){
	$.validator.messages.digits = '请输入大于1的整数'
	$('#thruDateRange.input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true,
	    todayHighlight : true,  
	    startDate: new Date() 
	}).on('changeDate', function(ev){
		vm.fromDate = $('#thruDateStart').val();
		vm.thruDate = $('#thruDateEnd').val();
	});
	
	$('#auto-send').prop('checked', true);
	$('#businessType input[value="NO_LIMIT"]').prop('checked', true)
	$('#useLimitPerCustomer input[value="0"]').prop('checked', true)
	$('#useLimitPerCustomer input[value="0"]').click(function(){
		vm.useLimitPerCustomer = '';
	})
	$('#businessType input[value="0"]').prop('checked', true)
	
	if(vm.couponPartyType === 'UNLIMIT'){
		$('#couponPartyType input').each(function(){
			$(this).prop('checked', true);
		})
	}else if(vm.couponPartyType === 'PARTY_GROUP'){
		$('#couponPartyType input[value="PARTY_GROUP"]').prop('checked', true)
	}else{
		$('#couponPartyType input[value="PERSON"]').prop('checked', true)
	}
	
	$('#couponPartyType input').click(function(){
		if($('#couponPartyType input:checked').length === 0){
			return false;
		}	
		if(!$('#couponPartyType input[value="PERSON"]').prop('checked')){
			vm.couponPartyType = 'PARTY_GROUP';
		}else if(!$('#couponPartyType input[value="PARTY_GROUP"]').prop('checked')){
			vm.couponPartyType = 'PERSON';
		}else{
			vm.couponPartyType = 'UNLIMIT';
		}
	})
	
	var business = ['PERSON_REG','COMPANY_REG','UP_COMPANY','APPROVE_COMPANY','ACTIVITY_ORDER'];
	var partyTypeGroup = $('#couponPartyType input[value="PARTY_GROUP"]'),
		partyTypePerson = $('#couponPartyType input[value="PERSON"]');
	
	if(vm.couponMethodType == 'AUTO_SEND' && vm.businessType == 'ACTIVITY_ORDER'){
		partyTypeGroup.prop('checked', true);
		partyTypePerson.prop('checked', true);
		vm.couponPartyType = 'UNLIMIT';
	}
		
	$('#businessType input').click(function(){
		if($('#businessType input[value='+business[0]+']').prop('checked')){
			vm.couponPartyType = 'PERSON';
			partyTypePerson.prop('checked', true);
			partyTypeGroup.prop('checked', false);
		}else if($('#businessType input[value='+business[1]+']').prop('checked') || $('#businessType input[value='+business[2]+']').prop('checked') || $('#businessType input[value='+business[3]+']').prop('checked')){
			vm.couponPartyType = 'PARTY_GROUP';
			partyTypeGroup.prop('checked', true);
			partyTypePerson.prop('checked', false);
		}else{
			partyTypeGroup.prop('checked', true);
			partyTypePerson.prop('checked', true);
			vm.couponPartyType = 'UNLIMIT';
		}
	})
}

var uploader = createUploader({
	buttonId: "uploadFile", 
	uploadType: "coupon.voucher", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "jpg,jpeg,png,bmp",//可允许上传文件的类型
	fileSize: "5mb", //最多允许上传文件的大小
	isImage: true, //是否是图片
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.approvedProof = signatureData.image;
				vm.approvedProofShow = _fileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
});
uploader.init();

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

init();