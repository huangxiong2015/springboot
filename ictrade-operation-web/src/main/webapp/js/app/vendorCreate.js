/**
 * create by roy.he@yikuyi.com at 2017-8-4
 */
var validate;
var vm = new Vue({
	el: '#createVendor',
	data: {
		tabValue: 'baseData', //baseData,product,credit,sales
		baseDataGone: false,
		productGone: false,
		
		vendorLogo: '',
		fileName:'',
		fileUrl: '',
		categoryList: [],
		regionList: [],
		companyTypeList: [],
		deptList: [],
		userList: [],
		offerList: [],
		saveDescribe:'',
		
		keyname: '选择原厂',
        validate:{
            "required": true
        },
        id:'selbox',
        name:'selbox',
        api:ykyUrl.product +"/v1/products/brands",
        optionName: 'brandName',
        optionId: 'id',
        selected:[],
        multiple:true,   //复选
        placeholder:'搜索原厂',
        isFuzzySearch:true,
        reloadApi:ykyUrl.product + '/v1/products/brands/alias?keyword={selbox}',
        province : '',
        city : '',
        district : '',
        inputClass : 'l-input',
        styleObject: { width : "180px" },
        category: ykyUrl.product + '/v1/products/categories/children?parentCateId={pid}',
        cateId:'_id',
        cateName:'cateName',
        setProvince:'',
        setCity:'',
        setDistrict:'',
        parentId:'pid',
        queryParam:{
            id:''
        },
        catePlaceholder: {
        	province: '所有大类',
        	city: '所有小类',
        	district: '所有次小类',
        },
        
        baseData: {// 基本信息
        	groupNameFull: '',
        	groupName: '',
        	partyCode: '',
        	category: '100001',
        	isCore: 'Y',
        	region: '100100',
        	generalHeadquarters: '',
        	genre: '100200',
        	listed: 'N',
        	deptId: '',
        	logoImageUrl: '',	
        },
        partyProductLineList: [],//代理产品线
        productNotProxyLines: [],//不代理产品线
        prodctType:'PROXY', //代理线和非代理线
        vendorCreditVo: {//信用
        	checkDate: '',//对账日期（纯数字，代表每月几号）
        	creditDeadline: '',//授信期限
        	creditQuota: '',//授信额度 ,
        	currency: 'CNY',//币种 = ['CNY', 'USD']
        	partyBankAccount: [],//银行资料列表
        	payDate: '',//付款日期（纯数字，代表每月几号） ,
        	paymentTerms: '',//付款方式 ,
        	creditAttachmentList: [],//附件类
        },
        partyBankAccount:{
        	accountName:'',//账号名称
        	bankAccount: '',//银行账号
        	bankName: '',// 银行名称 
        	contactNumber: '',//电话 
        	currency: 'CNY',
        	isDefault: 'Y',//是否默认银行,是:Y,否：N ,
        	swiftCode: '',//
        	taxNumber: '',//税号
        	address: '',//地址
        },
        vendorSaleInfoVo: {
        	balanceDue: '',//应付余额
        	contactPersonInfoList: [],//联系人信息 
        	description:'', //装运条款 ,
        	isShowName: 'Y',//是否显示名称(Y/N) ,
        	lastPayDate: '',//最后付款日期 ,
        	lastPaymPrice: '',//最后付款金额 ,
        	lastTransactionDate: '',//最后交易日期 ,
        	lastTransactionPrice: '',//最后交易金额 
        	minOrderPriceCNY: '',//最小订单金额人民币
        	minOrderPriceUSD: '',//最小订单金额美元 ,
        	orderVerify: 'N',//订单是否审核(Y/N) ,  	
        	supportCurrency: '',//支持币种(RMB,USD,)
        	facilityList: [{facilityName:''}],//仓库信息列表 ,
        	skuMovStatus: 'Y',
        	vendorMovStatus: 'Y',
        },
        vendorMap:{
        	VENDOR_INFO_WEBSITE: '',
        	VENDOR_INFO_LEGALPERSON: '',
        	VENDOR_INFO_REGPRICE: '',
        	VENDOR_INFO_REGRADDRESS: '',
        	VENDOR_INFO_EMPLOYEENUM: '',
        	VENDOR_CREDIT_PURCHASEDEAL: 'NOT_SIGN',//采购协议 已签：ALREADY_SIGN，未签：NOT_SIGN，已特批：ALREADY_SPE_SIGN = ['ALREADY_SIGN', 'NOT_SIGN', 'ALREADY_SPE_SIGN']
        	VENDOR_CREDIT_PURCHASEDEALDATE: '',//采购协议有效期 	
        	VENDOR_CREDIT_SECRECYPROTOCOL: 'NOT_SIGN',//采购协议 已签：ALREADY_SIGN，未签：NOT_SIGN，已特批：ALREADY_SPE_SIGN = ['ALREADY_SIGN', 'NOT_SIGN', 'ALREADY_SPE_SIGN']
        	VENDOR_CREDIT_SECRECYPROTOCOLDATE: '',//采购协议有效期 	
        },
        
        contactPersonInfo: {
        	address: '',//地址
        	fixedtel: '',//电话 ,
        	isDefault: 'Y',//是否默认联系人：是:Y,否：N ,
        	lastNameLocal: '',// 联系人名字 ,
        	mail: '',//邮箱
        	occupation:'',// 联系人职位 ,
        	personalTitle: 'ENQUIRY',// 联系人职能 = ['ENQUIRY', 'ORDER', 'NOT_LIMIT'
        	tel: '',//手机   	
        },
        
        stockCode: '', 
        multBrand: true,
        productItem: {},
        productItems:[],
        productNotProxyItems:[],
        productSelectFlag: true,
        showRmb: true,
        showUsd: false,
        supportCurrency: ['RMB'],
        editProduct: false,
        productFlagNum: 1,
        enquiryNameCheckboxModel:[],
        enquiryNameChecked:false,
        offerNameCheckboxModel:[],
        offerNameChecked:false
	},
	created: function(){
		getOfferList(operateDeptId);
		getCategoryList();
		getRegionList();
		getCompanyType();
		getDeptList();
	},
	methods: {
		//询价员全选
		enquiryNameCheckedAll: function() {
		    var _this = this;
		    console.log(_this.enquiryNameCheckboxModel);
		    if (this.enquiryNameChecked) {//实现反选
		      _this.enquiryNameCheckboxModel = [];
		    }else{//实现全选
		      _this.enquiryNameCheckboxModel = [];
		      _this.offerList.forEach(function(item) {
		        _this.enquiryNameCheckboxModel.push(item.partyId);
		      });
		    }
		 },
		//报价员全选
		offerNameCheckedAll: function() {
		    var _this = this;
		    console.log(_this.offerNameCheckboxModel);
		    if (this.offerNameChecked) {//实现反选
		      _this.offerNameCheckboxModel = [];
		    }else{//实现全选
		      _this.offerNameCheckboxModel = [];
		      _this.offerList.forEach(function(item) {
		        _this.offerNameCheckboxModel.push(item.partyId);
		      });
		    }
		 },
		//选择原厂点击事件
		getBrandSelected: function(e){
			var _this = this;
			if(e.length > 1){
				_this.multBrand = false;			
				if(_this.prodctType === 'PROXY'){
					_this.productItems = [];
				}else{
					_this.productNotProxyItems = [];
				}
				for(var i = 0; i < e.length; i++){
					_this.productItem = {}; //将对象置空
					_this.productItem.brandId = e[i].id.toString();
					_this.productItem.brandName = e[i].brandName;
					_this.productItem.category1Id = '';
					_this.productItem.category1Name = '';
					_this.productItem.category2Id = '';
					_this.productItem.category2Name = '';
					_this.productItem.category3Id = '';
					_this.productItem.category3Name = '';
					_this.productItem.type = _this.prodctType;
					
					if(_this.prodctType === 'PROXY'){
						_this.productItems.push(_this.productItem);
						_this.productItems = _.uniqBy(_this.productItems, 'brandId')
					}else{
						_this.productNotProxyItems.push(_this.productItem);
						_this.productNotProxyItems = _.uniqBy(_this.productNotProxyItems, 'brandId')
					}
				}
			}else if(e.length === 1){
				if(_this.prodctType === 'PROXY'){
					_this.productItems = [];
				}else{
					_this.productNotProxyItems = [];
				}
				_this.multBrand = true;
				_this.productItem = {};
				_this.productItem.brandId = e[0].id.toString();
				_this.productItem.brandName = e[0].brandName;
				_this.productItem.type = _this.prodctType;
				if(_this.prodctType === 'PROXY'){
					_this.productItems.push(_this.productItem);
					_this.productItems = _.uniqBy(_this.productItems, 'brandId')
				}else{
					_this.productNotProxyItems.push(_this.productItem);
					_this.productNotProxyItems = _.uniqBy(_this.productNotProxyItems, 'brandId')
				}
			}else{
				_this.multBrand = true;
				if(_this.prodctType === 'PROXY'){
					_this.productItems = [];
				}else{
					_this.productNotProxyItems = [];
				}
				
			}
		},
		// 三级联动onchange赋值
		change : function (data) {//赋值
			var _this = this;
			if(!_this.productItem.brandId){
				if(_this.prodctType === 'PROXY'){
					_this.$refs.refs.resetSelected();
				}else{
					_this.$refs.refsNot.resetSelected();
				}
//				layer.msg('请先选择原厂！')
				return false;
			}
			this.$nextTick(function(){
				var obj = {};
				if(_this.prodctType === 'PROXY'){
					_this.productItems = [];
					obj = vm.$refs.refs.getSelected();
				}else{
					_this.productNotProxyItems = [];
					obj = vm.$refs.refsNot.getSelected();
				}
				if(obj.province){//赋值大类
					_this.productItem.category1Id = obj.province._id.toString();
					_this.productItem.category1Name = obj.province.cateName;
				}else{
					_this.productItem.category1Id = '';
					_this.productItem.category1Name = '';
				}
				if(obj.city){//赋值小类
					_this.productItem.category2Id = obj.city._id.toString();
					_this.productItem.category2Name = obj.city.cateName;
				}else{
					_this.productItem.category2Id = '';
					_this.productItem.category2Name = '';
				}
				
				if(obj.district){//赋值次小类
					_this.productItem.category3Id = obj.district._id.toString();
					_this.productItem.category3Name = obj.district.cateName;
				}else{
					_this.productItem.category3Id = '';
					_this.productItem.category3Name = '';
				}
				
				if(_this.prodctType === 'PROXY'){
					_this.productItems.push(_this.productItem);
				}else{
					_this.productNotProxyItems.push(_this.productItem);
				}
            });
        },
        //保存产品线
        saveProduct: function(){
        	var _this = this;
        	if(_this.editProduct){
        		
        		if(_this.prodctType === 'PROXY'){
        			if(_this.productItems.length > 1){
            			_this.partyProductLineList = _.uniqBy(_this.partyProductLineList.concat(_this.productItems), 'brandId');
            		}else{
            			_this.partyProductLineList[_this.productIndex] = _this.productItem;
            		}
        		}else{
        			if(_this.productNotProxyItems.length > 1){
            			_this.productNotProxyLines = _.uniqBy(_this.productNotProxyLines.concat(_this.productNotProxyItems), 'brandId');
            		}else{
            			_this.productNotProxyLines[_this.productIndex] = _this.productItem;
            		}
        		}

        	}else{
        		
        		if(_this.prodctType === 'PROXY'){
        			for(var i = 0; i< _this.partyProductLineList.length; i++){
            			for(var k = 0; k < _this.productItems.length; k++){
            				if(!_this.productItems[k].category1Id){
            					_this.productItems[k].category1Id = '';
            				}
            				if(!_this.productItems[k].category2Id){
            					_this.productItems[k].category2Id = '';
            				}
            				if(!_this.productItems[k].category3Id){
            					_this.productItems[k].category3Id = '';
            				}
            				if(_this.partyProductLineList[i].brandId === _this.productItems[k].brandId && _this.partyProductLineList[i].category1Id === _this.productItems[k].category1Id && _this.partyProductLineList[i].category2Id === _this.productItems[k].category2Id && _this.partyProductLineList[i].category3Id === _this.productItems[k].category3Id){
            					_this.productItems.splice(k,1);
            				}
            			}
            		}
            		_this.partyProductLineList = _this.partyProductLineList.concat(_this.productItems);
        		}else{
        			for(var i = 0; i< _this.productNotProxyLines.length; i++){
            			for(var k = 0; k < _this.productNotProxyItems.length; k++){
            				if(!_this.productNotProxyItems[k].category1Id){
            					_this.productNotProxyItems[k].category1Id = '';
            				}
            				if(!_this.productNotProxyItems[k].category2Id){
            					_this.productNotProxyItems[k].category2Id = '';
            				}
            				if(!_this.productNotProxyItems[k].category3Id){
            					_this.productNotProxyItems[k].category3Id = '';
            				}
            				if(_this.productNotProxyLines[i].brandId === _this.productNotProxyItems[k].brandId && _this.productNotProxyLines[i].category1Id === _this.productNotProxyItems[k].category1Id && _this.productNotProxyLines[i].category2Id === _this.productNotProxyItems[k].category2Id && _this.productNotProxyLines[i].category3Id === _this.productNotProxyItems[k].category3Id){
            					_this.productNotProxyItems.splice(k,1);
            				}
            			}
            		}
            		_this.productNotProxyLines = _this.productNotProxyLines.concat(_this.productNotProxyItems);
        		}
        		
        	}
        	
        	_this.productItem = {};
        	
        	if(_this.prodctType === 'PROXY'){
        		_this.productItems = [];
			}else{
				_this.productNotProxyItems = [];
			}
        	_this.productSelectFlag = false;
        	_this.editProduct = false;
        	_this.productIndex = '';        	
        },
        showProductSelect: function(){
        	var _this = this;
        	_this.editProduct = false;
        	_this.productItem = {};
        	
        	if(_this.prodctType === 'PROXY'){
        		_this.productItems = [];
			}else{
				_this.productNotProxyItems = [];
			}
        	_this.selected = []; 
        	_this.queryParam = {
        			id: ''
        	};
        	_this.setProvince = '';
        	_this.setCity = '';
        	_this.setDistrict = '';
        	_this.multiple = true;
        	_this.productSelectFlag = true;
        },
        //移除选中的原厂
        removeSelected: function(i){
        	var _this = this;
        	
        	if(_this.prodctType === 'PROXY'){
        		if(_this.productItems.length < 2){
            		if(_this.productItems[0].category1Id){
            			_this.$refs.refs.resetSelected();
            		}
            		_this.multBrand = true;
            	}
        		
        		_this.$refs.letter.del(Number(_this.productItems[i].brandId));
        		_this.productItems.splice(i,1);
			}else{
				if(_this.productNotProxyItems.length < 2){
	        		if(_this.productNotProxyItems[0].category1Id){
	        			_this.$refs.refsNot.resetSelected();
	        		}
	        		_this.multBrand = true;
	        	}
				
				_this.$refs.letterNot.del(Number(_this.productNotProxyItems[i].brandId));
				_this.productNotProxyItems.splice(i,1);
			}

        	_this.productItem = {};
        	_this.selected = [];
        	_this.queryParam = {
        			id: ''
        	};
        	if(_this.prodctType === 'PROXY'){        		
        		for(var j = 0; j < _this.productItems.length; j++){
        			_this.selected.push(Number(_this.productItems[j].brandId))
        		}
        	}else{
        		for(var j = 0; j < _this.productNotProxyItems.length; j++){
        			_this.selected.push(Number(_this.productNotProxyItems[j].brandId))
        		}
        	}     	
        },
        //删除单条产品线
        deleteProductItem: function(i){
        	var _this = this;
        	layer.open({
        		title: '提示',
        		move: false,
        		area: '500px',
        		content: '<h2>确定删除该产品线？</h2><p>删除后不可恢复哦!</p>',
        		btn: ['确定', '取消'],
        		yes: function(index, layero) {
        			
        			if(_this.prodctType === 'PROXY'){
        				_this.partyProductLineList.splice(i,1);
        				_this.productItems = [];
                	}else{
                		_this.productNotProxyLines.splice(i,1);
        				_this.productNotProxyItems = [];
                	}
        			
        			_this.productItem = {};
                	_this.productSelectFlag = false;
                	_this.editProduct = false;
        			layer.close(index);
        		},
        		cancel: function(index, layero) {
        		}
        	})
        },
        //编辑单条产品线
        editProductItem: function(i){
        	var _this = this;
        	_this.editProduct = true,
        	_this.productIndex = i;
        	
        	_this.selected = new Array;
        	localStorage.removeItem('productItem');
        	if(_this.prodctType === 'PROXY'){
        		_this.productItems = [];
        		localStorage.setItem('productItem', JSON.stringify(_this.partyProductLineList[i]));
        	}else{
        		_this.productNotProxyItems = [];
        		localStorage.setItem('productItem', JSON.stringify(_this.productNotProxyLines[i]));
        	}
    		_this.productItem = JSON.parse(localStorage.getItem('productItem'));
    		_this.productSelectFlag = true;
    		_this.queryParam = {};
    		_this.multiple = false;
    		_this.multBrand = true;
    		
			var selecteds;
			selecteds = Number(_this.productItem.brandId);
			_this.selected.push(selecteds);
        	_this.setProvince = _this.productItem.category1Id ? _this.productItem.category1Id.toString() : '';
        	_this.setCity = _this.productItem.category2Id ? _this.productItem.category2Id.toString(): '';
        	_this.setDistrict = _this.productItem.category3Id ? _this.productItem.category3Id.toString(): '';
        	_this.$nextTick(function(){
        		if(_this.prodctType === 'PROXY'){
        			_this.productItems.push(_this.productItem);
            	}else{
            		_this.productNotProxyItems.push(_this.productItem);
            	}
        	})
        },
        //上传产品线
        uploadProductLine: function(){
        	var _this = this;
        	layer.open({
				type: 1,
				title: '添加产品线',
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
					var url = ykyUrl.party+'/v1/vendorManage/uploadLine?fileUrl='+vm.fileUrl + '&type=' + vm.prodctType;
					syncData(url, 'POST', null, function(res, err){
						layer.close(index);
						if(err == null){
							_this.fileName='';
							_this.fileUrl='';
							if(_this.prodctType === 'PROXY'){      
								for(var i = 0; i< _this.partyProductLineList.length; i++){
				        			for(var k = 0; k < res.list.length; k++){
				        				if(!res.list[k].category1Id){
				        					res.list[k].category1Id = '';
				        				}
				        				if(!res.list[k].category2Id){
				        					res.list[k].category2Id = '';
				        				}
				        				if(!res.list[k].category3Id){
				        					res.list[k].category3Id = '';
				        				}
				        				if(!res.list[k].brandSign){
				        					res.list[k].brandSign = '';
				        				}
				        				if(!res.list[k].category1Sign){
				        					res.list[k].category1Sign = '';
				        				}
				        				if(!res.list[k].category2Sign){
				        					res.list[k].category2Sign = '';
				        				}
				        				if(!res.list[k].category3Sign){
				        					res.list[k].category3Sign = '';
				        				}
				        				if(_this.partyProductLineList[i].brandId === res.list[k].brandId 
				        						&& _this.partyProductLineList[i].category1Id === res.list[k].category1Id 
				        						&& _this.partyProductLineList[i].category2Id === res.list[k].category2Id 
				        						&& _this.partyProductLineList[i].category3Id === res.list[k].category3Id
				        						&& _this.partyProductLineList[i].brandSign === res.list[k].brandSign
				        						&& _this.partyProductLineList[i].category1Sign === res.list[k].category1Sign
				        						&& _this.partyProductLineList[i].category2Sign === res.list[k].category2Sign
				        						&& _this.partyProductLineList[i].category3Sign === res.list[k].category3Sign
				        						){
				        					res.list.splice(k,1);
				        				}
				        			}
				        		}
				        		_this.partyProductLineList = _this.partyProductLineList.concat(res.list);
							}else{
								for(var i = 0; i< _this.productNotProxyLines.length; i++){
				        			for(var k = 0; k < res.list.length; k++){
				        				if(!res.list[k].category1Id){
				        					res.list[k].category1Id = '';
				        				}
				        				if(!res.list[k].category2Id){
				        					res.list[k].category2Id = '';
				        				}
				        				if(!res.list[k].category3Id){
				        					res.list[k].category3Id = '';
				        				}
				        				if(!res.list[k].brandSign){
				        					res.list[k].brandSign = '';
				        				}
				        				if(!res.list[k].category1Sign){
				        					res.list[k].category1Sign = '';
				        				}
				        				if(!res.list[k].category2Sign){
				        					res.list[k].category2Sign = '';
				        				}
				        				if(!res.list[k].category3Sign){
				        					res.list[k].category3Sign = '';
				        				}
				        				if(_this.productNotProxyLines[i].brandId === res.list[k].brandId 
				        						&& _this.productNotProxyLines[i].category1Id === res.list[k].category1Id 
				        						&& _this.productNotProxyLines[i].category2Id === res.list[k].category2Id 
				        						&& _this.productNotProxyLines[i].category3Id === res.list[k].category3Id
				        						&& _this.productNotProxyLines[i].brandSign === res.list[k].brandSign
				        						&& _this.productNotProxyLines[i].category1Sign === res.list[k].category1Sign
				        						&& _this.productNotProxyLines[i].category2Sign === res.list[k].category2Sign
				        						&& _this.productNotProxyLines[i].category3Sign === res.list[k].category3Sign
				        						){
				        					res.list.splice(k,1);
				        				}
				        			}
				        		}
				        		_this.productNotProxyLines = _this.productNotProxyLines.concat(res.list);
							}
							layer.closeAll();
						}
					})
				},
				cancel: function(index, layero) {
					vm.fileName='';
					vm.fileUrl='';
				}
			})
        },
      //tab切换代理产品线和非代理产品线
        productLineChange: function(type){
        	var _this = this;
        	_this.prodctType = type;
        	
        },
        addBankInfo: function(type, i){
        	var _this = this;
        	var bankInfo = {};
        	if(type === 'add'){
        		resetBankinfo();
        	}else{
        		localStorage.removeItem('bankInfo')
        		localStorage.setItem('bankInfo', JSON.stringify(_this.vendorCreditVo.partyBankAccount[i]));
        		_this.partyBankAccount = JSON.parse(localStorage.getItem('bankInfo'));
        		_this.$nextTick(function(){
        			if(_this.partyBankAccount.isDefault === 'Y'){
            			$('.bank_layer input[name="isDefault"]').prop('checked', true);
            		}else{
                		$('.bank_layer input[name="isDefault"]').prop('checked', false);
            		}
        		})
        		
        	}
        	layer.open({
        		type: 1,
        		title: '银行资料',
        		area: '800px',
        		offset: '150px',
        		move: false,
        		skin: "s_select_data",
        		content: $(".bank_layer"),
        		btn: ['保存', '取消'],
        		yes: function(index, layero) {
        			var bankVerifyValid = bankVerify().form();
        			
        			if(!bankVerifyValid){
        				return false;
        			}
        			
        			if($('.bank_layer input[name="isDefault"]').prop('checked')){
        				_this.partyBankAccount.isDefault = 'Y';
        				_this.vendorCreditVo.partyBankAccount.forEach(function(item, i){
        					item.isDefault = 'N';
        				});
        			}else{
        				_this.partyBankAccount.isDefault = 'N';
        			}
        			if(type === 'add'){
        				_this.vendorCreditVo.partyBankAccount.push(_this.partyBankAccount);
        			}else{
        				_this.vendorCreditVo.partyBankAccount[i] = _this.partyBankAccount;
        			}
        			
        			resetBankinfo();
        			layer.close(index);
        		},
        		cancel: function(index, layero) {
        			
        		}
        	})
        },
        //删除银行信息
        deleteBankInfo: function(i){
        	var _this = this;
        	_this.vendorCreditVo.partyBankAccount.splice(i,1);
        },
        //新增仓库
        addFacility: function(){
        	var _this= this;
        	if(_this.vendorSaleInfoVo.facilityList.length === 10){
        		layer.msg('最多只能添加10个仓库')
        		return false;
        	}
        	_this.vendorSaleInfoVo.facilityList.push({facilityName:''}); 
        },
        //删除附件
        removeAttachment: function(i){
        	var _this = this;
        	_this.vendorCreditVo.creditAttachmentList.splice(i,1);
        },
        //保存基本信息数据
        saveBaseData: function(){
        	var _this = this;
    		$('#createVendorBase').submit();
        	if(!validate || $("#createVendorBase input.error").length > 0){
        		return false;
        	}
        	if(vm.baseData.listed === 'Y'){
        		vm.baseData.stockCode = vm.stockCode;
        	}
        	if(vm.enquiryNameCheckboxModel.length==0){
        		layer.msg('询价员不能为空')
        		return false;
        	}
        	if(vm.offerNameCheckboxModel.length==0){
        		layer.msg('报价员不能为空')
        		return false;
        	}
        	_this.baseData.deptId = $("[name='department']").val();
        	_this.baseData.deptName = $("[name='department'] option:selected").text();
        	/*_this.baseData.enquiryId = $("[name='enquiryId']").val();
        	_this.baseData.enquiryName = $("[name='enquiryId'] option:selected").text();*/
        	_this.baseData.enquiryList = vm.enquiryNameCheckboxModel;
        	_this.baseData.principalId = $("[name='principalId']").val();
        	_this.baseData.principalName = $("[name='principalId'] option:selected").text();
        	/*_this.baseData.offerId = $("[name='offerId']").val();
        	_this.baseData.offerName = $('[name="offerId"] option:selected').text();*/
        	_this.baseData.offerList = vm.offerNameCheckboxModel;
        	_this.baseData.categoryName = $("[name='category'] option:selected").text();
        	_this.baseData.regionName = $("[name='region'] option:selected").text();
        	_this.baseData.genreName = $('input[name="genre"]:checked').parent().text();
        	
        	_this.tabValue = 'product';
        	_this.baseDataGone = true;
        	
        	var equiryLength = _this.enquiryNameCheckboxModel.length;
        	var offerLength = _this.offerNameCheckboxModel.length;
        	var offerListLength = _this.offerList.length;
        	var enquiryArray = [];
        	var offerArray = [];
        	for(var i=0;i<offerListLength;i++){
        		for(var j=0;j<equiryLength;j++){
            		if(_this.offerList[i].partyId==_this.enquiryNameCheckboxModel[j]){
            			enquiryArray.push(_this.offerList[i].partyAccount);
            		}
            	}
				for(var k=0;k<offerLength;k++){
					if(_this.offerList[i].partyId==_this.offerNameCheckboxModel[k]){
						offerArray.push(_this.offerList[i].partyAccount);
            		}        		
				}
			}
        	_this.baseData.offerName = offerArray.join(",");
        	_this.baseData.enquiryName = enquiryArray.join(",");
        	
        },
        //保存产品线数据
        saveProductData: function(){
        	var _this = this;
    		var flag = true;
    		for(var i = 0; i < _this.partyProductLineList.length; i++){
    			if(_this.partyProductLineList[i].brandSign === 'Y' || _this.partyProductLineList[i].category1Sign === 'Y' || _this.partyProductLineList[i].category2Sign === 'Y' || _this.partyProductLineList[i].category3Sign === 'Y'){
    				layer.msg('已上传的产品线有误，请检查');
    				flag = false;
    				break;
    			}
    		}
    		if(!flag){
    			return false;
    		}
        	_this.tabValue = 'credit';
        	_this.productGone = true;
        },
      //保存数据
        saveData: function(){
        	var _this = this;
        	$('#createVendorCredit').submit();
        	if(!validate || $("#createVendorCredit input.error").length > 0){
        		return false;
        	}
        	if(_this.vendorCreditVo.partyBankAccount.length === 0){
        		layer.msg('请填写银行资料');
        		return false;
        	}
        	
        	var vendorObj = {
        			partyProductLineList: _this.partyProductLineList.concat(_this.productNotProxyLines),
        			vendorCreditVo : _this.vendorCreditVo,
        			vendorInfoVo : _this.baseData,
        			map: _this.vendorMap
        	};
        	
        	layer.open({
        		type: 1,
        		title: '确认提交',
        		area: '500px',
        		offset: '300px',
        		move: false,
        		skin: "s_select_data",
        		content: $('.save-describe'),
        		btn: ['确定', '取消'],
        		yes: function(i, layero) {
        			if(!_this.saveDescribe){
        				layer.msg('描述不能为空');
        				return false;
        			}
        			
        			if(_this.saveDescribe.length > 100){
        				layer.msg('描述长度不能超过100个字符');
        				return false;
        			}
        			vendorObj.describe = _this.saveDescribe;
        			syncData(ykyUrl.party + "/v1/vendorManage/addVendor", 'POST', vendorObj, function(res, err) {
                		if (!err) {
                			location.href = ykyUrl._this+'/vendor.htm';
                		}
                	});
        			layer.close(i);
        		},
        		cancel: function(i, layero) {
        		}
        	})
        },
	},
	watch: {
		'baseData.deptId': function(newVal){
			getPrincipalList(newVal);
		},
		'enquiryNameCheckboxModel': {
		    handler: function (val, oldVal) { 
		      if (this.enquiryNameCheckboxModel.length === this.offerList.length) {
		        this.enquiryNameChecked=true;
		      }else{
		        this.enquiryNameChecked=false;
		      }
		    },
		    deep: true
		  },
		  'offerNameCheckboxModel': {
		    handler: function (val, oldVal) { 
		      if (this.offerNameCheckboxModel.length === this.offerList.length) {
		        this.offerNameChecked=true;
		      }else{
		        this.offerNameChecked=false;
		      }
		    },
		    deep: true
		  }
	}
})
//获取所属分类数据
function getCategoryList(){
	syncData(ykyUrl.database + "/v1/category/companylist?categoryTypeId=VENDOR_THE_TYPE", 'GET', null, function(res, err) {
		if (!err) {
			vm.categoryList = res;
		}
	});
}

//获取所属地区数据
function getRegionList(){
	syncData(ykyUrl.database + "/v1/category/companylist?categoryTypeId=VENDOR_THE_REGION", 'GET', null, function(res, err) {
		if (!err) {
			vm.regionList = res;
		}
	});
}

//获取公司类型数据
function getCompanyType(){
	syncData(ykyUrl.database + "/v1/category/companylist?categoryTypeId=VENDOR_COMPANY_TYPE", 'GET', null, function(res, err) {
		if (!err) {
			vm.companyTypeList = res;
		}
	});
}

//获取分管部门数据
function getDeptList(){
	syncData(ykyUrl.party + "/v1/dept/sonlist?parentId=99999999", 'GET', null, function(res, err) {
		if (!err) {
			vm.deptList = res;
		}
	});
}

//获取负责人、询价员数据
function getPrincipalList(id){
	var tempUrl  = 'http://operation-sit.yikuyi.com/services-party/v1/dept/findCustomerByDeptId?page=1&size=100&deptId=5e65555b-9c23-11e7-9';
	syncData(ykyUrl.party + "/v1/dept/findCustomerByDeptId?page=1&size=100&deptId="+id, 'GET', null, function(res, err) {
		if (!err) {;
			vm.userList = res.list;
		}
	});
}

//获取运营部下所有人
function getOfferList(id){
	var tempUrl  = 'http://operation-sit.yikuyi.com/services-party/v1/dept/findCustomerByDeptId?page=1&size=100&deptId=5e65555b-9c23-11e7-9';
	syncData(ykyUrl.party + "/v1/dept/findCustomerByDeptId?page=1&size=100&deptId="+id, 'GET', null, function(res, err) {
		if (!err) {
			vm.offerList = res.list;
		}
	});
}

function resetBankinfo(){
	var _this = vm;
	_this.partyBankAccount = {
		accountName:"",
		address:"",
		bankAccount:"",
		bankName:"",
		contactNumber:"",
		currency:"CNY",
		isDefault:"Y",
		swiftCode:"",
		taxNumber:"",
	}
}

function resetContactinfo(){
	var _this = vm;
	_this.contactPersonInfo = {
		address: '',//地址
    	fixedtel: '',//电话 ,
    	isDefault: 'Y',//是否默认联系人：是:Y,否：N ,
    	lastNameLocal: '',// 联系人名字 ,
    	mail: '',//邮箱
    	occupation:'',// 联系人职位 ,
    	personalTitle: 'ENQUIRY',// 联系人职能 = ['ENQUIRY', 'ORDER', 'NOT_LIMIT'
    	tel: '',//手机   	
	}
}


/**
 * 基本信息表单校验
 */
$("#createVendorBase").validate({
	rules: {
		groupNameFull: {
			required: true,
		},
		groupName: {
			required: true,
			vendorShotNameCheck: [''],
		},
		partyCode: {
			required: true,
			noCn: [''],
		},
		website: {
			noCn: [''],
		},
		department: {
			required: true,
		},
		principalId: {
			required: true,
		},
		legalPerson: {
			noNumber: ['']
		},
		stockCode: {
			required: true,
			noCn: [''],
		}
	},
	messages: {
		groupNameFull: {
			required: '供应商全称不能为空',
		},
		groupName: {
			required: '供应商简称不能为空',
		},
		partyCode: {
			required: '供应商编码不能为空',
		},
		department:{
			required: '分管部分不能为空'
		},
		principalId: {
			required: '负责人不能为空',
		},
		stockCode: {
			required: '股票代码不能为空',
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
	},
	submitHandler: function(form) { // 表单提交时修改校验状态
		validate = true;
	}
});

/**
 * 信用表单校验
 */
$("#createVendorCredit").validate({
	rules: {
		creditQuota: {
			number: true,
			floatZero: [''],
			floatTwo: ['']
		},
		creditDeadline: {
			noZero: [''],
		},
		checkDate: {
			noZero: [''],
		},
		payDate: {
			noZero: [''],
		},
	},
	messages: {
		creditQuota: {
			number: '请输入不小于0数字',
			min: '请输入不小于0数字',
		},
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
	},
	submitHandler: function(form) { // 表单提交时修改校验状态
		validate = true;
	}
});

var bankVerify = function(){
	return $("#bankLayer").validate({
		rules: {
			accountName: {
				required: true,
			},
			bankName: {
				required: true,
			},
			bankAccount: {
				required: true,
				noCn: [''],
			},
			taxNumber: {
				required: true,
				noCn: [''],
			},
			address: {
				required: true,
			},
			contactNumber: {
				required: true,
			},
			swiftCode: {
				required: true,
			}
		},
		messages: {
			accountName: {
				required: '账户名称不能为空',
			},
			bankName: {
				required: '开户银行不能为空',
			},
			bankAccount: {
				required: '银行账号不能为空',
			},
			taxNumber: {
				required: '税号不能为空',
			},
			address: {
				required: '地址不能为空',
			},
			contactNumber: {
				required: '电话不能为空',
			},
			swiftCode: {
				required: 'SWIFT CODE不能为空',
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
		},
		submitHandler: function(form) { // 表单提交时修改校验状态
			validate = true;
		}
	});
	/* 插件验证end */
}



// 供应商logo上传
var uploaderLogo = createUploader({
	buttonId: "upload-logo", 
	uploadType: "vendor.logo", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "png,bmp,jpeg,jpg",//可允许上传文件的类型
	fileSize: "10mb", //最多允许上传文件的大小
	isImage: true, //是否是图片
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.vendorLogo = signatureData.image;
				vm.baseData.logoImageUrl = _fileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploaderLogo.init();

var uploaderProduct = createUploader({
	buttonId: "uploadProductFile", 
	uploadType: "vendor.product", //上传后文件存放路径
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
uploaderProduct.init(); 



var uploaderDeal = createUploader({
	buttonId: "upload-deal", 
	uploadType: "vendor.accessories", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "xls,xlsx,csv,pdf,zip,rar",//可允许上传文件的类型
	fileSize: "10mb", //最多允许上传文件的大小
	isImage: false, //是否是图片
	multi_selection: true,
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				var files = {
					attachmentName: file.name,
					attachmentUrl: _fileUrl,
				}
				vm.vendorCreditVo.creditAttachmentList.push(files);
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploaderDeal.init(); 

$(function(){
	$('#secrecyTime .input-daterange,#dealTime .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true
	}).on('changeDate', function(ev){
		vm.vendorMap.VENDOR_CREDIT_PURCHASEDEALDATE = $('input[name="purchaseDealDate"]').val();
		vm.vendorMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE= $('input[name="secrecyProtocolDate"]').val();
	});
});