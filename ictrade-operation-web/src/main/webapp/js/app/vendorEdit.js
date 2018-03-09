/**
 * create by roy.he@yikuyi.com at 2017-8-18
 */
var validate;
var aliasvalidate;
var partyId = getQueryString('partyId');
var vm = new Vue({
	el: '#detailVendor',
	data: {
		ruleId:'',  //修改传，新增不传
		ruleType:0, //规则类型
		reRuleId:'',
		sourceIds:[],   //仓库id的集合
		sourceAll:[],   //仓库选择不限
		manufacturerIds:[],   //选择的制造商 
        tempManufacturerIds:[], //临时存放选择的制造商 ，点击确认再存进去 
		movdescription:'',   //规则备注 
		cnyMovAmount:'',//国内mov
		usdMovAmount:'',//香港mov
		exchangeRate:'',//汇率
		taxRate:1.17,//增值税
		conversionAmount:'',//换算金额
        isChecked: false, //不限的checkbox是否已经被选择
        disabled:'',
		tabValue: 'baseData', //baseData,product,credit,sales
		subTabValue:'nextbaseData', //nextbaseData,nextmovData
		thereTabValue:'movList' , //movList,movEdit
		detailTabValue:'detail',  //detail,list
		movList: [],   //mov列表集合 
		specialList:[],//专属特价集合
		baseDataGone: false,
		productGone: false,
		creditGone: false,
		status: '', //applyNo什么意思？
		saveDescribe: '',
		vendorMap: {},
		partyId:getQueryString('partyId'),
		baseData: {
			deptId: '',
			vendorInfoAttributeMap:{}
		},
		productLines: [], //代理产品线列表
		productNotProxyLines: [], //不代理产品线列表
		creditInfo: {
			creditAttributeMap:{}
		},
		salesInfo: {
			saleAttributeMap:{},
			vendorMovStatus: 'N',
			skuMovStatus: 'N',
			contactPersonInfoList: [],
		},
		aliasInfo:{  //供应商别名
			aliasList:[{
				aliasName:"",
				partyId:this.partyId,
				supplierAliasId:""
			}],
			hasAddVendorAliasBtn:true
		},
		auditList: [],
		vendorLogo: '',
		categoryList: [],
		regionList: [],
		wareList: [],   //仓库列表
		companyTypeList: [],
		deptList: [],
		userList: [],
		offerList: [],
		fileName: '',
		showRmb: true,
        showUsd: false,
        supportCurrency: ['RMB'],
        departmentId: '',
        enquiryId: '',
        principalId: '',
        offerId: '',
        saleDepartmentId: '',
        saleEnquiryId: '',
        salePrincipalId: '',
        saleOfferId: '',
        salesInfoEnquiryNameCheckboxModel:[], //询价员，报价员
        salesInfoEnquiryNameChecked:false,
        salesInfoOfferNameCheckboxModel:[],
        salesInfoOfferNameChecked:false,
        noSales: false,
        noContactInfo: false,
        baseDescribe: '',
        prodctDescribe: '',
        prodctType:'PROXY', //代理线 PROXY 和非代理线 NOT_PROXY
        creditDescribe: '',
        salesDescribe: '',
        productFlagNum: 1,
        noEdit: false,
        salesEdit: false,
		stockCode:'',
		productSelectFlag: false,
		showManSelectFlag:false,  //制造商面板 
		mdefalut:false,   //默认是都显示 "重新选择"
		choiceFlag:true, 
		editProduct: false,
		productIndex: '',
		multBrand: true,
		productItem: {},
        productItems:[],
        productNotProxyItems:[],

        focusFieldsname:'关注领域',
        focusFieldsId:'focusFields',
        focusFieldsName:'focusFields',
        focusFieldsOptions: [],
        focusFieldsClass:'form-control vendor-input-s inline',
        focusFieldsPlaceholder:'输入之后按回车键',

        productCategorysname:'优势产品类别',
        productCategorysId:'productCategorys',
        productCategorysName:'productCategorys',
        productCategorysOptions: [],
        productCategorysClass:'form-control vendor-input-s inline',
        productCategorysPlaceholder:'输入之后按回车键',

        majorClientsname:'主要客户',
        majorClientsId:'majorClients',
        majorClientsName:'majorClients',
        majorClientsOptions: [],
        majorClientsClass:'form-control vendor-input-s inline',
        majorClientsPlaceholder:'输入之后按回车键',

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

        mkeyname: '选择制造商',
        validate:{
            "required": true
        },
        mid:'selbox',
        mname:'selbox',
      //  mapi:ykyUrl.party +"/v1/vendors/onlyProductLine/899549959613841408",    //制造商查询接口  
        //mapi:ykyUrl.party +"/v1/vendors/onlyProductLine/"+partyId,    //制造商查询接口  
        moptions:[],
        moptionName: 'brandName',
        moptionId: 'brandId',
        mselected:[],
        multiple:true,   //复选
        mplaceholder:'搜索制造商',
        
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

        contactPersonInfo: {
        	address: '',//地址
        	fixedtel: '',//电话 ,
        	isDefault: 'Y',//是否默认联系人：是:Y,否：N ,
        	lastNameLocal: '',// 联系人名字 ,
        	mail: '',//邮箱
        	occupation:'',// 联系人职位 ,
        	personalTitle: 'ENQUIRY',// 联系人职能 = ['ENQUIRY', 'ORDER', 'NOT_LIMIT'
        	tel: '',//手机
        	partyProductLineIdList: [],
        	partyProductLineList: [],
        },
        checkboxModel:[],
        checked:'',
        searchData: '',
        checkProductLine: [],
        enquiryNameCheckboxModel:[], //询价员，报价员
        enquiryNameChecked:false,
        offerNameCheckboxModel:[],
        offerNameChecked:false,
        showModal:false,
        modalTitle:'添加型号',
        modalStyle:{
        	width:950,
			maxHeight:600,
			overflowY:'auto'
        },
        showPrdAssociate:false,
        showSpecialEdit:false, //显示专属特价编辑界面
        showSpecialDetail:false, //显示专属特价详情界面
        currentSpecialRuleId:'', //当前专属特价规则id
        isAddSpecialRule:false, //添加or编辑专属特价规则
        specialRuleStatusList:{ //专属特价显示类型
        	'ALL':'所有商品',
        	'APPOINT':'指定商品',
        	'INOPERATIVE':'不生效'
        },
        specialRuleTypeList:{ //专属特价规则类型
        	'RULE':'产品线',
        	'MPN':'型号'
        },
        specialMpnTypeList:{ //专属特价型号添加方式
        	'UPLOAD':'EXCEL上传',
        	'INPUT':'手工添加'
        },
        specialRuleStatus:'INOPERATIVE', //专属特价显示类型
        specialRuleType:'RULE', //专属特价规则类型
        speListRuleType:'',//专属特价中列表搜索参数规则类型
        specialMpnType:'UPLOAD', //专属特价型号添加方式
        speListCreatedDateStart:'', //创建时间起始
        speListCreateDateEnd:'', //创建时间结束
        specialRemark:'', // 专属特价规则备注
        specialUploadFileName:'', //专属特价
        specialUploadFileUrl:'',
        specialRulesText:'',//专属特价显示文案
        speaiclRuleProductLine:{   //专属特价产品线
        	keyname: '选择制造商',
            validate: {
                "required": true
            },
            id: 'selbox',
            name: 'selbox',
            //api :　componentApiURL + 'data.json',
            options: [

            ],
            optionId: 'id',
            optionName: 'brandName',
            selected: [],
            multiple: true, //复选
            placeholder: '搜索制造商',
            sels: [],
            showSearch:true,//显示搜索框
            showLetter:true,//显示字母
            manufacturerIsFuzzySearch:true, //制造商模糊搜索
            manufacturerReloadApi:ykyUrl.product + '/v1/products/brands/alias?keyword={selbox}', //模糊搜索api
            chosenManufacturer:[],
            wareList:[],
            isAllChecked:false,
    		//特价产品线
    		 cateApi: ykyUrl.product +'/v1/products/categories/list',
             cateInitData: [],
             id:'_id',
             cateName:'cateName',
             children:'children',
             checkedCategories:[],
             mfrName:"",
             sourceName:"",
             addOrEdit:"POST"//"POST"与"PUT"
        },
        showTabcate:false,
        speaiclRuleShowManufacturer:false,
        specialRuleShowModal: false,
        specialRuleModalStyle: {
            width: 1000,
            height: 550,
            overflow:"auto",
            border:'#f00 1px solid'
        },
        specialRuleModalText:{
        	okText:"确定",
            cancelText:"取消",
            title:"选择制造商"
        },
        productAssociate:{
        	api: ykyUrl.product + '/v1/products/batch/basic',
        	requestType: 'POST',
        	productData:{
        		id:'', //商品id
	    		prdUrl:'', //型号链接
				model:'', //型号
				manufacturer:'',  //原厂
				manufacturerId:'', //原厂id
				sourceId:'', //仓库id
				sourceName:'' //仓库名称
        	},
        	isAdd:true,
        	showImage:false
        },
        uploadPrdApi: ykyUrl.product + '/v1/specialOfferProductDraft/getDraft',  //查询专属特价商品
        //uploadPrdStandardApi: ykyUrl.product + '/v1/specialOfferProductDraft/getDraft',
        uploadPrdStandardApi: ykyUrl.product + '/v1/specialOfferProduct/getStandard',  //查询专属特价商品草稿数据
        uploadPrdPageflag:true,
        uploadPrdCheckflag:true,
        uploadPrdQueryParams:{
        	 pageSize:10,
        	 size:10,
        	 page:1,
        	 defaultStatus:true,
        	 ruleId:''
         },
        uploadPrdColumns:[
                       	{key :'selected', name: '',width: '50px', hide:false},
       		         	{key :'index', name: '序号', align:'center', width: '50px'},
    		         	{key :'mpn', name: '型号', align:'center',default : '-',cutstring: true,textColor:{
    		    	    	condition: "'{status}'=='UNABLE'",
    		    	    	color: 'red'
    		    	    }},
    		         	{key :'brandName', name: '制造商', align:'center',cutstring: true,default : '-',textColor:{
    		    	    	condition: "'{status}'=='UNABLE'",
    		    	    	color: 'red'
    		    	    }},
    		         	{key :'vendorName', name: '分销商', align:'center',cutstring: true,default : '-',textColor:{
    		    	    	condition: "'{status}'=='UNABLE'",
    		    	    	color: 'red'
    		    	    }},
    		         	{key :'sourceName', name: '仓库', align:'center',cutstring: true,default : '-',textColor:{
    		    	    	condition: "'{status}'=='UNABLE'",
    		    	    	color: 'red'
    		    	    }},
    		         	{key :'remark', name: '备注', align:'center',cutstring: true,default : '-',textColor:{
    		    	    	condition: "'{status}'=='UNABLE'",
    		    	    	color: 'red'
    		    	    }},
    		         	{key :'operate', name: '操作', align:'center', hide:false,
    		         		items:[
    	         		    	   {className:'btn-detail',
    		         		    	text: '删除',
    		         		    	show: true,
        		         		    callback: {
        		         		    	action: 'delSpeaiclPrd', 
        		                        params: ['{id}']
        		         		    	}
    	         		    	   	}
    		         		       ]
    		         	}
     		         ],
         speListUrl : ykyUrl.product + "/v1/specialOfferRule?vendorId="+ partyId, //访问数据接口 
         speListQueryParams : {           //请求接口参数 
		            	pageSize: 10,        //分页参数
		                page:1,              //当前页
		                defaultStatus:true,  //监测参数变化标识
		            	ruleType:'',
		            	createdDateStart:'',
		            	createDateEnd:'',
		            },
        speListColumns: [          //表格列
		                {key: 'index', name: '序号',default : '-',width: '50px'},
		                {
		                	key: 'ruleType', 
		                	name: '规则类型', 
		                	align : 'center',
		                	default : '-',
		                	text : {
		                		RULE : {
									'value' : '产品线'
								},
								MPN : {
									'value' : '型号'
								},
							},
							cutstring: true
		                },
		                {
		                	key: '',
		                	name: '应用条件',
		                	align : 'center',
			            	render: function (h, params) {
			            		var ruleType = params.row.ruleType;
						        var str = '',str1 = '',str2 = '',showText = [];
					        	if(ruleType == 'MPN'){
					        		var mpnName = params.row.mpn ? params.row.mpn+'...': '-';//后台默认传前3条，加个省略表示更多没显示，小于等于3条的情况不考虑，也加省略号
					        		showText.push(h('div', {
	                                	'class': {
											showText: true,
											cutstring: true
	                                	},	
	                                	attrs: {
	 	                                   title: mpnName
		                                 },
	                                }, mpnName))
					        	}else if(ruleType == 'RULE'){
					        		var mfrName = params.row.mfrName;
							        var catName = params.row.catName;
							        var sourceName = params.row.sourceName?params.row.sourceName:((params.row.sourceIds && params.row.sourceIds[0]=="*")?"不限":"");
					        		if(mfrName || catName||sourceName){
						        		str1 = '制造商:'+ mfrName;
						        		str2 = "仓库:"+ sourceName;
						        		str3 = '分类:'+ catName;
						        		var showText = [];
						        		if(mfrName){
						        			showText.push(h('div', {
			                                	'class': {
													showText: true,
													cutstring: true
			                                	},	
			                                	attrs: {
			 	                                   title: str1
				                                 },
				     							style: {
				    								paddingTop: '5px'
				    							}
			                                }, str1));
						        		}
						        		if(sourceName){
						        			showText.push(h('div', {
			                                	'class': {
													showText: true,
													cutstring: true
			                                	},	
			                                	attrs: {
			 	                                   title: str2
				                                 },
				                                 style: {
					    								paddingTop: '5px'
					    							}
			                                }, str2));
						        		}
						        		if(catName){
						        			showText.push(h('div', {
					                                	'class': {
															showText: true,
															cutstring: true
					                                	},	
					                                	attrs: {
					 	                                   title: str3
						                                 },
						     							style: {
						    								paddingTop: '5px'
						    							}
					                                }, str3));
						        		}
						        	}else{
						        		showText = '-';
						        	}
					        	}
						        
	                           return h('div',{},showText) ;
	                        },
		                	cutstring: true
	                	}, 
		                {key: 'createdTimeMillis', name: '创建时间',align : 'center',default : '-',cutstring: true},
		                {key: 'lastUpdateUserName', name: '操作人',align : 'center',default : '-',width: '100px',cutstring: true},
						{
							key : 'operate',
							name : '操作',
							align : 'center',
							default : '-',
							width: '150px',
							items : [   {
								className : 'btn-edit',
								text : '编辑',
								show : true,
								callback:{
									action:"editSpecialRule",
									params:['{rowData}'],
									
								},
							},{
								className : 'btn-delete',
								text : '详情',
								show : true,
								callback:{
									action:"specialRuleDetail",
									params:['{rowData}'],
									
								},
							},{
								className : 'btn-delete',
								text : '删除',
								show : true,
								callback : {
									action : 'delFunc',
									params : [ '{id}' ]
								}
							} ]
						}
		            ], 
        speListPageflag : true,    //是否显示分页
        speListRefresh: false    //重载         
	},
	
	created: function(){
		var partyId = getQueryString('partyId');
		var ruleId = getQueryString('ruleId'); //编辑的url拼接上这个 
		getmapi(partyId);
		getRegionList();
		getCategoryList();
		getCompanyType();
		getBaseData(partyId)
		getProductLine(partyId)
		getCreditInfo(partyId);
		getSalesInfo(partyId);
		getWareList(partyId);  
		getVendorAlias(partyId);
		//getMovData('918009524839776256');  //ruleId 
		//getMovData(ruleId);  //编辑时规则id 
		getMovList();   //mov列表数据 
		getAuditList(partyId);
		getExchangeRate(); //获取汇率
		if(getQueryString('status') === 'applyNo'){
			this.noSales = true;
		}
		
		if(getQueryString('status') !== 'salesEdit'){
			this.salesEdit = true;
			getVendorSpecialOffer(partyId);
		}else{ //销售编辑
			this.noEdit = true;
			this.tabValue = 'sales';
			getVendorSpecialOffer(partyId);
		}
		
	},
	methods: {
		isAllWarechecked: function (index) {
			var isChecked = this.speaiclRuleProductLine.isAllChecked;
			if(isChecked){
				this.speaiclRuleProductLine.wareList.forEach(function(item,index) {
					item.isChecked = false;
			    })
			    $('.input_ware_check').attr("disabled",true); //选中不限，其他不可选
			}else{
				$('.input_ware_check').attr("disabled",false);
			}
		},
		getCheckedCategories:function(obj){
			this.speaiclRuleProductLine.checkedCategories = obj;
		},
		onSearchSpeListClick: function () {
			if(this.specialRuleStatus == 'ALL' || this.specialRuleStatus == 'INOPERATIVE'){
        		return false;
        	}
			vm.speListQueryParams.ruleType = vm.speListRuleType;
			vm.speListQueryParams.createdDateStart = vm.speListCreatedDateStart;
			vm.speListQueryParams.createDateEnd = vm.speListCreateDateEnd;
			vm.speListQueryParams.defaultStatus = !vm.speListQueryParams.defaultStatus;
		},
		
		deleteManufacturerSelect:function(index,id){
			this.speaiclRuleProductLine.chosenManufacturer.splice(index,1);
		},
		deleteManufacturer:function(index,id){
			this.speaiclRuleProductLine.sels.splice(index,1);
		},
		specialRuleGetSelected: function(obj){  //获取选中值
  		    this.speaiclRuleProductLine.sels  = obj;
        },
        speaiclRuleProductLineDel:function(){
        	vm.$refs.speaiclRuleProductLineLetter.del(id);
        },
		choseSpecialRuleManufacturer:function(){
			//to do
			this.specialRuleShowModal = true;
			var list  = this.speaiclRuleProductLine.chosenManufacturer;
			this.speaiclRuleProductLine.sels= this.speaiclRuleProductLine.chosenManufacturer;
			var arr = [];
			if(list.length>0){
				$.each(list,function(index,item){
					var id = item.id;
	        		var idFormated = id;
					arr.push(idFormated);
				})
        	}
			this.speaiclRuleProductLine.selected = arr;
			this.speaiclRuleShowManufacturer = true;
		},
		choseSpecialRuleWharehouse:function(){
			/*this.specialRuleShowModal = true;*/
		},
		modalOk: function(data) { //ok调用方法
            this.specialRuleShowModal = false;
            this.speaiclRuleShowManufacturer = false;
            this.speaiclRuleProductLine.chosenManufacturer = this.speaiclRuleProductLine.sels;
        },
        toggleModal: function() { //显示隐藏；cancel调用方法
            this.speaiclRuleShowManufacturer = false;
            this.specialRuleShowModal = !this.specialRuleShowModal;
        },
        updateRulesText:function(){
        	var text = this.specialRulesText;
        	specialOfferUpdateRuleText(partyId,text);
        },
        updateRuleStatus:function(e){
        	specialOfferUpdateRuleStatus(partyId,e);
        },
		//添加供应商别名
		addVendorAlias:function(){
			var that =this;
			var item = {
				aliasName:"",
				partyId:this.partyId,
				supplierAliasId:""
			}
			this.aliasInfo.aliasList.push(item);
			if(this.aliasInfo.aliasList.length>=10){
				this.aliasInfo.hasAddVendorAliasBtn = false;
			}
			var index = that.aliasInfo.aliasList.length - 1;
			Vue.nextTick(function () {
				  	// DOM 更新了
					$('[name*="vendorAlias"]').eq(index).rules('add', {
						required: true,
			            messages: {
			                required: "供应商别名不能为空"
			            }
					});
					$('[name*="vendorAlias"]').eq(index).rules('add', {
						isAliasExits:['']
					});
				})

		},
		/*保存供应商别名*/
		saveAliasData:function(){
			var that = this;
			$('#createVendorAlias').submit();
			if($("#createVendorAlias input.error").length > 0){
        		return false;
        	}
			var param = []
			var aliasList = that.aliasInfo.aliasList;
			$.each(aliasList,function(index,item){
				if($.trim(item.aliasName)!=""){
					if(item.supplierAliasId!=""){
						param.push(item);
					}else{
						param.push({
							aliasName:item.aliasName,
				            partyId:that.partyId,
						})
					}
				}
			});
			
			$.aAjax({		
				type: 'POST',
				url :  ykyUrl.party + "/v1/vendorManage/saveSupplierAlias",
				data:JSON.stringify(param),
				async:true,
				success : function(data){
						layer.msg('供应商别名保存成功！')
						location.href= ykyUrl._this + '/vendor.htm';
				},error:function(e){
					if(e){
						if(e.responseJSON&&e.responseJSON.errCode=='EXIST_SUPPLIERALIAS_NAME'){
							layer.msg('已存在供应商别名！');
						}
					}
				}
			})
		},
		deleteSingleAlias:function(el,index){
			var that  = this;
			if(el&&el.supplierAliasId!=""){
				layer.confirm("<p>确认要删除该别名？</p>",{
					offset: "auto",
					btn: ['确      认','取      消'], //按钮
					title: " ",
					area: 'auto',
					maxWidth:'500px',
					move: false,
					skin: "up_skin_class",
					yes:function(){
						syncData(ykyUrl.party+"/v1/vendorManage/deleteSupperAlias/"+el.supplierAliasId,"DELETE",null,function(res, err){
							if (!err) {
								layer.msg('供应商别名删除成功！')
								if(that.aliasInfo.aliasList.length>1){
									that.aliasInfo.aliasList.splice(index,1);
								}else if(that.aliasInfo.aliasList.length==1){
									that.aliasInfo.aliasList[index] = {
											aliasName:"",
											partyId:this.partyId,
											supplierAliasId:""
										} 
								}
								vm.$forceUpdate();
							}
						},true);
					}
				})
			}else{
				if(that.aliasInfo.aliasList.length>1){
					that.aliasInfo.aliasList.splice(index,1);
				}else if(that.aliasInfo.aliasList.length==1){
					that.aliasInfo.aliasList[index] = {
							aliasName:"",
							partyId:this.partyId,
							supplierAliasId:""
						} 
				}
				vm.$forceUpdate();
			}
		},
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
		 salesInfoEnquiryNameCheckedAll: function() {
			var _this = this;
		    if (this.salesInfoEnquiryNameChecked) {//实现反选
		      _this.salesInfoEnquiryNameCheckboxModel = [];
		    }else{//实现全选
		      _this.salesInfoEnquiryNameCheckboxModel = [];
		      _this.offerList.forEach(function(item) {
		        _this.salesInfoEnquiryNameCheckboxModel.push(item.partyId);
		      });
		    }
		 },
		//报价员全选
		 salesInfoOfferNameCheckedAll: function() {
		    var _this = this;
		    if (this.salesInfoOfferNameChecked) {//实现反选
		      _this.salesInfoOfferNameCheckboxModel = [];
		    }else{//实现全选
		      _this.salesInfoOfferNameCheckboxModel = [];
		      _this.offerList.forEach(function(item) {
		        _this.salesInfoOfferNameCheckboxModel.push(item.partyId);
		      });
		    }
		 },
	 cancelData:function(){
		 var _this = this;
		 _this.thereTabValue = 'movList'; 
		_this.showManSelectFlag = false;   //取消关闭制造商面板
		/*var isHK = $("#HK").is(":checked");
        var isCH = $("#CH").is(":checked");
		if(!isHK){
			vm.usdMovAmount = '';   //未选中就清空 
		}
		if(!isCH){
			vm.cnyMovAmount = '';   //未选中就清空 
		}	*/
	 },
	 addRules:function(){   //新增mov规则，清空所有选项数据
		 var _this = this;
		    vm.isChecked  =  false ;
		    vm.ruleId = '' ;
			vm.vendorId = '' ;
			vm.ruleType = '0' ;
			//vm.sourceIds = [] ;
			var  resList =  vm.wareList ;
			resList.forEach(function (item) { 
				   item.isChecked = false ;  //仓库全部不选 
				})
		    $('.input_check').attr("disabled",false); //仓库全部ke选
			vm.manufacturerIds = [] ;
			vm.tempManufacturerIds = [] ;
			vm.cnyMovAmount = '' ;
			vm.usdMovAmount = '' ;
			$('#usdMovAmount').removeClass('snValid')
			$('#HK').prop('checked',true);  
			$('#cnyMovAmount').removeClass('snValid')
			$('#CH').prop('checked',true);  //初始化时，选中
			vm.movdescription = '' ;
			vm.status = '';
		 _this.thereTabValue = 'movEdit'; 
		 $('#MovData').removeClass("disable");
		
	 },
	 reviseRules:function(id){
		 var _this = this;
		 _this.thereTabValue = 'movEdit';
		 vm.isChecked  =  false ;
		 var ruleId = id;
		 var obj = parseUrlParam(false);
		  obj.ruleId = ruleId ;
		  var url = $.param(obj);
		 
		//  window.location.href = location.pathname+'?'+ url;  
		  
		  getMovData(id); //初始化规则数据
//		  getAllbrands();
	   },	
		changeStatus:function(status,id,e){
			if($(e.currentTarget).hasClass('disabled')){
				return;
			}
			$.aAjax({
				url: ykyUrl.product + '/v2/rules/mov/' + id + '/status?status=' + status,
				type:'PUT',
				dataType:"json",
				success:function(data){
					$(e.currentTarget).addClass('disabled');
					//停用
					if(status=='DISABLED'){
						layer.msg("<i class='icon-right-c icon_style'></i>MOV规则停用成功！",{
							area: 'auto',
							maxWidth:'500px',
							skin: "up_skin_class"
						})
						setTimeout(function(){
		 	  	    		//location.reload()
							getMovList();   //刷新列表数据
							vm.thereTabValue = 'movList';
		 	  	    	},2000);
					}
					//启用成功
					if(status=='ENABLED'){
						layer.msg("<i class='icon-right-c icon_style'></i>【MOV规则启用成功！",{
							area: 'auto',
							maxWidth:'500px',
							skin: "up_skin_class"
						})
						setTimeout(function(){
		 	  	    		//location.reload()
							getMovList();   //刷新列表数据
							vm.thereTabValue = 'movList';
		 	  	    	},2000);	
					}
				},
				error:function(XMLHttpRequest){
					if(XMLHttpRequest.status == 400){
						layer.msg("<i class='icon-plaint icon_style'></i>已启用MOV规则，请勿重复启用！",{
							area: 'auto',
							maxWidth:'500px',
							skin: "up_skin_class"
						})
					}
				}
			})
		},
		deleteRule:function(id,e){
			if($(e.currentTarget).hasClass('disabled')){
				return;
			}
			layer.confirm("<p>确认要删除规则？</p>",{
				offset: "auto",
				btn: ['确      认','取      消'], //按钮
				title: " ",
				area: 'auto',
				maxWidth:'500px',
				move: false,
				skin: "up_skin_class",
				yes:function(){
					$.aAjax({
						url: ykyUrl.product  + '/v2/rules/mov/' + id,
						type:'DELETE',
						success:function(data){
							$(e.currentTarget).addClass('disabled');
							layer.msg("<i class='icon-right-c icon_style'></i>删除成功！",{
								area: 'auto',
								maxWidth:'500px',
								skin: "up_skin_class"
							})
							setTimeout(function(){
			 	  	    	//	location.reload()
								getMovList();   //刷新列表数据
    							vm.thereTabValue = 'movList';
			 	  	    	},2000);
						},
						error:function(){
							console.log("error");
						}
					})
				}
			})
		},
		onmovChange:function(){
			vm.subTabValue = 'nextmovData';
			
		},
		oninfoChange:function(){
			vm.subTabValue = 'nextbaseData' ;
		},
		onSpecialChange:function(){
			vm.subTabValue = 'nextSpecialData';
		},
		showaFacility:function(el,index){
			if($(el.currentTarget).prop('checked')){
				vm.salesInfo.facilityList[index].isShowaFacility = 'Y';
			}else{
				vm.salesInfo.facilityList[index].isShowaFacility = 'N';
			}
		},
		saveMovData:function(e){
			
				//	保存mov设置
			var  addData ={
					  "ruleId": "",//修改传，新增不传
					  "vendorId": "",//供应商id
					  "ruleType": "",//规则类型  0为供应商mov,1为商品mov
					  "sourceIds": [],//仓库ID集合，不选默认为none
					  "manufacturerIds": [],//制造商id集合,不选默认为none
					  "cnyMovAmount": "",//国内最小订单金额
					  "usdMovAmount": "",//国外最小订单金额
					  "description": "",//规则备注  
					  "status": ""//新增为DISABLED   status
					};
			//  销售设置接口
			//  http://192.168.1.110:27083/v2/rules/mov   post是新增接口
			//  http://192.168.1.110:27083/v2/rules/mov   put是修改接口
			
			
			//赋值传参对象 
			addData.ruleId = vm.ruleId ;
			addData.vendorId  = getQueryString('partyId');  //供应商id
			addData.ruleType  = vm.ruleType ; 
			
			//保存之间校验 
			    var that = this;
		        var target = e.currentTarget;
		        if ($(target).hasClass("disable")) {  //不能多次点击   
		            return;
		        }
		        var isHK = $("#HK").is(":checked");
		        var isCH = $("#CH").is(":checked");
		        var ischeckAll = $('.input_checkall').is(":checked") ; //仓库是否选中不限 
		        if (vm.manufacturerIds.length == 0  ) {  //制造商    
		        	//vm.manufacturerIds.push('none')
		        	addData.manufacturerIds.push('none') ;
		        } else {
		        	var mlist = vm.manufacturerIds;
		        	$.each(mlist,function(index,el){
		        		addData.manufacturerIds.push(el.brandId);
		        	})
		        	
		        }
		        var list =  vm.wareList;
		         list.forEach(function (item,index) {
	        		 if(item.isChecked == true){
	        			 addData.sourceIds.push(item.id);
	        		   }
		 		    })
		 		 if(ischeckAll){
		 			 addData.sourceIds.push('none');   //仓库不限传none 
		 		 }
		        if( addData.sourceIds.length == 0   ){  //仓库  选择不限
		        	 layer.msg("至少选择一个仓库");   
		        	 return;
		        }
		        
		        if (!isHK && !isCH) {
		            layer.msg("至少选择一个MOV");
		            return;
		        }
		        if (isHK && !that.usdMovAmount) {
		            $("#usdMovAmount").addClass("snValid");
		            return;
		        } else {
		            if (isHK && that.usdMovAmount) {
		                if ($("#usdMovAmount").hasClass("snValid")) {
		                    return;
		                }
		                addData.usdMovAmount = that.usdMovAmount;
		            }
		        }
		        if (isCH && !that.cnyMovAmount) {
		            $("#cnyMovAmount").addClass("snValid");
		            return;
		        } else {
		            if (isCH && that.cnyMovAmount) {
		                if ($("#cnyMovAmount").hasClass("snValid")) {
		                    return;
		                }
		                addData.cnyMovAmount = that.cnyMovAmount;
		            }
		        }
		        if (that.ruleId) {  //新增  
		        	addData.ruleId = that.ruleId;
		        	addData.status = 'DISABLED' ;
				};
		        addData.description = vm.movdescription ;  //描述 
		        
		        $.aAjax({
		            url: ykyUrl.product + "/v2/rules/mov",
		            type: that.ruleId ? "PUT" : "POST",
		            data: JSON.stringify(addData),
		            success: function(data) {
		            	vm.reRuleId = data.ruleId  ; //规则id 
		                $(target).addClass("disable");
		              //  window.location.href = ykyUrl._this + "/mov.htm";   跳转至mov列表页  
		            	//询问框
		            	layer.confirm("<p>恭喜！规则保存成功 </p><p>是否立即启用规则 </p>", {
		            		  	offset: "auto",
		            		  	//time:3000,
		            		  	area:['450px','auto'],
		            			btn: ['启用 ','取消 '], //按钮
		            			title: " ",
		            			shade: 0,
		            			move: false,
		            			skin: "up_skin_class",
		            	}, 
		            	//同意的情况，启用此规则
		            	function(index,e){
                             var  id = vm.reRuleId ;  //新返回的规则id 
		        			if($(e.currentTarget).hasClass('disabled')){
		        				return;
		        			}
		        			$.aAjax({
		        				url: ykyUrl.product + '/v2/rules/mov/' + id + '/status?status=ENABLED',
		        				type:'PUT',
		        				dataType:"json",
		        				success:function(data){
		        					var status = data.status ;
		        					$(e.currentTarget).addClass('disabled');
		        					//启用成功
		        					if(status=='ENABLED'){
		        						layer.msg("<i class='icon-right-c icon_style'></i>【MOV规则启用成功！",{
		        							area: 'auto',
		        							maxWidth:'500px',
		        							skin: "up_skin_class"
		        						})
		        						setTimeout(function(){
		        							getMovList();   //刷新列表数据
		        							vm.thereTabValue = 'movList';
		        		 	  	    	},2000);	
		        					}
		        				},
		        				error:function(XMLHttpRequest){
		        					if(XMLHttpRequest.status == 400){
		        						layer.msg("<i class='icon-plaint icon_style'></i>已启用MOV规则，请勿重复启用！",{
		        							area: 'auto',
		        							maxWidth:'500px',
		        							skin: "up_skin_class"
		        						})
		        					}
		        				}
		        			})
		        		
		            	}, 
		            	//取消
		            	function(){
		            		 layer.closeAll(); 
		            		 getMovList();   //刷新列表数据
				           	 vm.thereTabValue = 'movList';
				           	  
		            	});
		            	
		             
		            },
		            error: function(data) {
		            	var error = data.responseJSON.errMsg; //弹出错误提示 ，重复规则校验
		            	
		            	layer.confirm("<p>"+error+  " </p>", {
	            		  	offset: "auto",
	            		  	//time:3000,
	            		  	area:['450px','auto'],
	            			btn: ['确认'], //按钮
	            			title: " ",
	            			shade: 0,
	            			move: false,
	            			skin: "up_skin_class",
	            	},function(){
	            		 layer.closeAll(); 
	            	});
		            	//layer.msg(error,{icon:2,offset:120}); 
		            }
		        });
		    
			
		},
		m_sure : function(list){//传参数就不会影响了，不知道为什么
			var _this = this;
			_this.showManSelectFlag = false;
			//vm.manufacturerIds = vm.tempManufacturerIds ;  //点击确认，保存 对应的供应商 
			var _this = this;
        	
        	 /*for(var i = 0; i< _this.manufacturerIds.length; i++){
        			for(var k = 0; k < _this.tempManufacturerIds.length; k++){
        				
        				if(_this.manufacturerIds[i].brandId === _this.tempManufacturerIds[k].brandId){
        					_this.tempManufacturerIds.splice(k,1);
        				}
        			}
        		}*/
        		_this.manufacturerIds = list;
        	
        	
        	
        	//_this.tempManufacturerIds  = [];
        
        	
			
		},
		m_cancel:function(){
			var _this = this;
			_this.showManSelectFlag = false;   //取消的话，制造商还是上一次选择的那些，需要一个临时的集合，把新选的存起来，点击确认再保存。
			
		},
		computeCNYMov:function(){
			//vm.getExchangeRate();
			var that = this;
			var usdMov = that.usdMovAmount;
			var cnyMov = that.usdMovAmount*that.exchangeRate*that.taxRate;		
			that.conversionAmount = cnyMov.toFixed(2);
			that.cnyMovAmount = that.conversionAmount;
		},
		cnyMovValidate:function(e,type){
			var that = this;
			var isHK = $('#HK').is(':checked');
			if(isHK && that.usdMovAmount && that.cnyMovAmount && that.conversionAmount && Number(that.cnyMovAmount)< Number(that.conversionAmount)){
				//layer.msg('国内最小订单金额需大于汇率换算金额');
				$('.ch-error').text('金额不能小于汇率换算金额' + that.conversionAmount);
				$('.ch-error').show();
				$('#cnyMovAmount').addClass('snValid');
			}else{
				$('.ch-error').hide();
				$('#cnyMovAmount').removeClass('snValid')
			}		
		},
		movAmountVail:function(event,type){  //香港mov校验 
			var that = this;
			var reg = /^(0|[1-9][0-9]{0,6})(\.[0-9]{1,2})?$/;
			var target = event.currentTarget;
			var val = $.trim($(target).val());		
			if((reg.test(val) && val>0) || val==''){
				$(target).removeClass("snValid");
				if(type == 'usd'){
					that.computeCNYMov();
				}
			}else{
				$(target).addClass("snValid");
			}
			
		},
        onCheckboxChange: function () {
			var isChecked = this.isChecked;
			if(isChecked){
				this.wareList.forEach(function (item,index) {
					item.isChecked = false;
			    })
			    $('.input_check').attr("disabled",true); //选中不限，其他不可选
			}else{
				$('.input_check').attr("disabled",false);
			}
			
		/*	if($('.input_check').eq(0).attr("disabled") === "disabled"){
				
			}else{
				
			}*/
			 
			
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
					_this.productItem.isNew = true;
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
				_this.productItem.isNew = true;
				_this.productItem.type = _this.prodctType;
				_this.productItem.brandId = e[0].id.toString();
				_this.productItem.brandName = e[0].brandName;
				
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
				_this.productItem.isNew = true;
				
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
            			_this.productLines = _.uniqBy(_this.productLines.concat(_this.productItems), 'brandId');
            		}else{
            			_this.productLines[_this.productIndex] = _this.productItem;
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
        			for(var i = 0; i< _this.productLines.length; i++){
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
            				if(_this.productLines[i].brandId === _this.productItems[k].brandId && _this.productLines[i].category1Id === _this.productItems[k].category1Id && _this.productLines[i].category2Id === _this.productItems[k].category2Id && _this.productLines[i].category3Id === _this.productItems[k].category3Id){
            					_this.productItems.splice(k,1);
            				}
            			}
            		}
            		_this.productLines = _this.productLines.concat(_this.productItems);
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
        showManufacturerSelect: function(){
        	var _this = this;
        	_this.choiceFlag =  false;
        	_this.showManSelectFlag = true;
        	vm.mselected = [];
        	vm.tempManufacturerIds = [];
        	for(var j = 0; j < vm.manufacturerIds.length; j++){
        		vm.mselected.push(vm.manufacturerIds[j].brandId);
        		vm.tempManufacturerIds.push(vm.manufacturerIds[j]);
        	}
        	
        	//var alist = vm.manufacturerIds ;
        	//vm.tempManufacturerIds = alist;
//        	_this.mselected = _this.manufacturerIds;
        	
        },
        getSelected: function(obj){  //获取选中值
           //  this.manufacturerIds=obj;  //manefacture 是存放值的一个数组 
        	vm.tempManufacturerIds = obj ;
             console.log(obj);
         },
        delmanfacture: function (i) {   //移除临时集合中已选择的制造商 
        	//还需要去掉选中项
        	var _this = this;
        	if(_this.prodctType === 'PROXY'){
        		_this.$refs.letter.del(Number(_this.tempManufacturerIds[i].brandId));
			}else{
				_this.$refs.letterNot.del(Number(_this.tempManufacturerIds[i].brandId));
			}
        	vm.tempManufacturerIds.splice(i,1) ;  
        	//_this.productItem = {};
        	/*_this.mselected = [];
        	for(var j = 0; j < _this.tempManufacturerIds.length; j++){
        		_this.mselected.push(Number(_this.tempManufacturerIds[j].brandId))
        	}*/
         },
        delshowSelect:function(index){
        	vm.manufacturerIds.splice(index,1)    //移除最后面板(集合)的制造商   
        	
        },
        getExchangeRate:function(){
        		//获取汇率
        		 $.aAjax({
        		 	url:ykyUrl.database +'/v1/basedata/exchangerate/USD/CNY',
        		 	type:"GET",
        		 	success:function(data){			
        		 		vm.exchangeRate = data.exchangeRate;
        		 		if(vm.usdMovAmount){
        		 			vm.conversionAmount = (vm.usdMovAmount*vm.exchangeRate*vm.taxRate).toFixed(2)
        		 		}
        		 	},
        		 	error:function(data){
        		 		console.log('error');
        		 	}
        		 })
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
        	var checkObj = {},
        	vendorId = getQueryString('partyId'),
        	catId = '';
        	if(_this.prodctType === 'PROXY'){
        		checkObj = _this.productLines[i];
        	}else{
        		checkObj = _this.productNotProxyLines[i];
        	}
        	if(checkObj.category3Id && checkObj.category3Id !== ''){
        		catId = checkObj.category3Id;
        	}else if(checkObj.category2Id && checkObj.category2Id !== ''){
        		catId = checkObj.category2Id;
        	}else if(checkObj.category1Id && checkObj.category1Id !== ''){
        		catId = checkObj.category1Id;
        	}
        	
        	if(catId === '' || checkObj.brandSign === 'Y' || checkObj.category1Sign === 'Y' || checkObj.category2Sign === 'Y' || checkObj.category3Sign === 'Y' || checkObj.isNew){
        		layer.open({
	        		title: '提示',
	        		move: false,
	        		area: '500px',
	        		content: '<h2>确定删除该产品线？</h2><p>删除后不可恢复哦!</p>',
	        		btn: ['确定', '取消'],
	        		yes: function(index, layero) {
	        			if(_this.prodctType === 'PROXY'){
	        				_this.productLines.splice(i,1);
	                	}else{
	                		_this.productNotProxyLines.splice(i,1);
	                	}
	        			layer.close(index);
	        		},
	        		cancel: function(index, layero) {
	        		}
	        	})
        	}else{
        		syncData(ykyUrl.product + "/v1/inventory?vendorId="+vendorId+"&cat="+catId, 'GET', null, function(res, err) {
            		if (!err) {
            			if(res && res.productInfo.length > 0){
            				layer.open({
            	        		title: '提示',
            	        		move: false,
            	        		content: '该产品线存在上架商品，请先下架商品后再删除',
            	        		btn: ['知道了']
            	        	})
            			}else{
            				layer.open({
            	        		title: '提示',
            	        		move: false,
            	        		area: '500px',
            	        		content: '<h2>确定删除该产品线？</h2><p>删除后不可恢复哦!</p>',
            	        		btn: ['确定', '取消'],
            	        		yes: function(index, layero) {
            	        			if(_this.prodctType === 'PROXY'){
            	        				_this.productLines.splice(i,1);
            	                	}else{
            	                		_this.productNotProxyLines.splice(i,1);
            	                	}
            	        			layer.close(index);
            	        		},
            	        		cancel: function(index, layero) {
            	        		}
            	        	})
            			}
            		}
            	});
        	}
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
        		
        		if(!_this.productLines[i].isNew){
            		var checkObj = _this.productLines[i],
                	vendorId = getQueryString('partyId'),
                	catId = '';
                	
                	if(checkObj.category3Id && checkObj.category3Id !== ''){
                		catId = checkObj.category3Id;
                	}else if(checkObj.category2Id && checkObj.category2Id !== ''){
                		catId = checkObj.category2Id;
                	}else if(checkObj.category1Id && checkObj.category1Id !== ''){
                		catId = checkObj.category1Id;
                	}
            		syncData(ykyUrl.product + "/v1/inventory?vendorId="+vendorId+"&cat="+catId, 'GET', null, function(res, err) {
                		if (!err) {
                			if(res && res.productInfo.length > 0){
                				layer.open({
                	        		title: '提示',
                	        		move: false,
                	        		content: '该产品线存在上架商品，请先下架商品后再编辑',
                	        		btn: ['知道了']
                	        	})
                	        	return false;
                			}else{
                				localStorage.setItem('productItem', JSON.stringify(_this.productLines[i]));
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
                	        		_this.productItems.push(_this.productItem);
                	        	})
                			}
                		}
                	});
            	}else{
            		localStorage.setItem('productItem', JSON.stringify(_this.productLines[i]));
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
    	        		_this.productItems.push(_this.productItem);
    	        	})
            	}
        	}else{
        		_this.productNotProxyItems = [];
        		
        		if(!_this.productNotProxyLines[i].isNew){
            		var checkObj = _this.productNotProxyLines[i],
                	vendorId = getQueryString('partyId'),
                	catId = '';
                	
                	if(checkObj.category3Id && checkObj.category3Id !== ''){
                		catId = checkObj.category3Id;
                	}else if(checkObj.category2Id && checkObj.category2Id !== ''){
                		catId = checkObj.category2Id;
                	}else if(checkObj.category1Id && checkObj.category1Id !== ''){
                		catId = checkObj.category1Id;
                	}
            		syncData(ykyUrl.product + "/v1/inventory?vendorId="+vendorId+"&cat="+catId, 'GET', null, function(res, err) {
                		if (!err) {
                			if(res && res.productInfo.length > 0){
                				layer.open({
                	        		title: '提示',
                	        		move: false,
                	        		content: '该产品线存在上架商品，请先下架商品后再编辑',
                	        		btn: ['知道了']
                	        	})
                	        	return false;
                			}else{
                				localStorage.setItem('productItem', JSON.stringify(_this.productNotProxyLines[i]));
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
                	        		_this.productNotProxyItems.push(_this.productItem);
                	        	})
                			}
                		}
                	});
            	}else{
            		localStorage.setItem('productItem', JSON.stringify(_this.productNotProxyLines[i]));
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
    	        		_this.productNotProxyItems.push(_this.productItem);
    	        	})
            	}
        	}
        	
        },
      //上传产品线
        uploadProductLine: function(){
        	var _this = this;
        	var vendorId = getQueryString('partyId');
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
					var url = ykyUrl.party+'/v1/vendorManage/uploadLine?partyId='+vendorId+'&fileUrl='+vm.fileUrl + '&type=' + vm.prodctType;
					syncData(url, 'POST', null, function(res, err){
						layer.close(index);
						if(err == null){
							_this.fileName='';
							_this.fileUrl='';
							
							if(_this.prodctType === 'PROXY'){        		
								for(var i = 0; i< _this.productLines.length; i++){
									if(res.list && res.list.legnth){
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
					        				if(_this.productLines[i].brandId === res.list[k].brandId 
					        						&& _this.productLines[i].category1Id === res.list[k].category1Id 
					        						&& _this.productLines[i].category2Id === res.list[k].category2Id 
					        						&& _this.productLines[i].category3Id === res.list[k].category3Id
					        						&& _this.productLines[i].brandSign === res.list[k].brandSign
					        						&& _this.productLines[i].category1Sign === res.list[k].category1Sign
					        						&& _this.productLines[i].category2Sign === res.list[k].category2Sign
					        						&& _this.productLines[i].category3Sign === res.list[k].category3Sign
					        						){
					        					res.list.splice(k,1);
					        				}
					        			}
									}
				        		}
				        		_this.productLines = _this.productLines.concat(res.list);
				        	}else{
				        		for(var i = 0; i< _this.productNotProxyLines.length; i++){
				        			if(res.list && res.list.length){
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
        addBankInfo: function(type, i){
        	var _this = this;
        	var bankInfo = {};
        	if(type === 'add'){
        		resetBankinfo();
        	}else{
        		localStorage.removeItem('bankInfo')
        		localStorage.setItem('bankInfo', JSON.stringify(_this.creditInfo.partyBankAccount[i]));
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
        				_this.creditInfo.partyBankAccount.forEach(function(item, i){
        					item.isDefault = 'N';
        				});
        			}else{
        				_this.partyBankAccount.isDefault = 'N';
        			}
        			if(type === 'add'){
        				_this.creditInfo.partyBankAccount.push(_this.partyBankAccount);
        			}else{
        				_this.creditInfo.partyBankAccount[i] = _this.partyBankAccount;
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
        	_this.creditInfo.partyBankAccount.splice(i,1);
        },
      //删除附件
        removeAttachment: function(i){
        	var _this = this;
        	_this.creditInfo.creditAttachmentList.splice(i,1);
        	_this.productFlagNum++;
        },
      //新增仓库
        addFacility: function(){
        	var _this= this;
        	if(_this.salesInfo.facilityList.length === 10){
        		layer.msg('最多只能添加10个仓库')
        		return false;
        	}
        	_this.salesInfo.facilityList.push({facilityName:'',isShowaFacility:'N'}); 
        	_this.productFlagNum++;
        },
      //添加编辑联系人
        addContact: function(type, i){
        	var _this = this;
        	var contactInfo = {};
        	if(type === 'add'){
        		resetContactinfo();
        		_this.$nextTick(function(){
        			if(_this.contactPersonInfo.isDefault === 'Y'){
            			$('.contact_layer input[name="isDefault"]').prop('checked', true);
            		}else{
                		$('.contact_layer input[name="isDefault"]').prop('checked', false);
            		}
        			$('.contact_layer input[value="ENQUIRY"]').prop('checked',true);
    				$('.contact_layer input[value="ORDER"]').prop('checked',false);
        		})
        	}else{
        		localStorage.removeItem('contactInfo');
        		localStorage.setItem('contactInfo', JSON.stringify(_this.salesInfo.contactPersonInfoList[i]));
        		_this.contactPersonInfo = JSON.parse(localStorage.getItem('contactInfo'));
        		_this.contactPersonInfo.partyProductLineList = [];
        		
        		if(_this.contactPersonInfo.partyProductLineIdList){
        			for(var j = 0; j < _this.contactPersonInfo.partyProductLineIdList.length; j++){
        				for(var k = 0; k < _this.salesInfo.partyProductLineList.length; k++){
        					if(_this.contactPersonInfo.partyProductLineIdList[j] === _this.salesInfo.partyProductLineList[k].partyProductLineId){
        						_this.contactPersonInfo.partyProductLineList.push(_this.salesInfo.partyProductLineList[k]);
        					}
        				}
        			}
        		}
        		     		
        		_this.$nextTick(function(){
        			if(_this.contactPersonInfo.isDefault === 'Y'){
            			$('.contact_layer input[name="isDefault"]').prop('checked', true);
            		}else{
                		$('.contact_layer input[name="isDefault"]').prop('checked', false);
            		}
        			
        			if(_this.contactPersonInfo.personalTitle === 'NOT_LIMIT'){
        				$('.contact_layer input[value="ENQUIRY"]').prop('checked',true);
        				$('.contact_layer input[value="ORDER"]').prop('checked',true);
        			}else if(_this.contactPersonInfo.personalTitle === 'ORDER'){
        				$('.contact_layer input[value="ORDER"]').prop('checked',true);
        				$('.contact_layer input[value="ENQUIRY"]').prop('checked',false);
        			}else{
        				$('.contact_layer input[value="ENQUIRY"]').prop('checked',true);
        				$('.contact_layer input[value="ORDER"]').prop('checked',false);
        			}
        		})	
        	}
        	layer.open({
        		type: 1,
        		title: '供应商联系人',
        		area: '800px',
        		offset: '150px',
        		move: false,
        		skin: "s_select_data",
        		content: $(".contact_layer"),
        		btn: ['保存', '取消'],
        		yes: function(index, layero) {
        			var contactVerifyValid = contactVerify().form();
        			
        			if(!contactVerifyValid){
        				return false;
        			}
        			
        			if($('.contact_layer input[name="isDefault"]').prop('checked')){
        				_this.contactPersonInfo.isDefault = 'Y';
        				if(_this.salesInfo.contactPersonInfoList && _this.salesInfo.contactPersonInfoList.length > 0){
        					_this.salesInfo.contactPersonInfoList.forEach(function(item, i){
            					item.isDefault = 'N';
            				});
        				}
        			}else{
        				_this.contactPersonInfo.isDefault = 'N';
        			}
        			if(type === 'add'){
        				_this.salesInfo.contactPersonInfoList.push(_this.contactPersonInfo);
        			}else{
        				_this.salesInfo.contactPersonInfoList[i] = _this.contactPersonInfo;
        			}
        			
        			resetContactinfo();
        			layer.close(index);
        		},
        		cancel: function(index, layero) {
        		}
        	})
        },
        //删除联系人信息
        deleteContactInfo: function(i){
        	var _this = this;
        	_this.salesInfo.contactPersonInfoList.splice(i,1);
        },
        showProductLine: function(){
        	var _this = this;
        	_this.checkboxModel = _this.contactPersonInfo.partyProductLineIdList?_this.contactPersonInfo.partyProductLineIdList:[];
        	layer.open({
        		type: 1,
        		title: '选择产品线',
        		area: '800px',
        		offset: '150px',
        		move: false,
        		skin: "s_select_data",
        		content: $(".productline_layer"),
        		btn: ['保存', '取消'],
        		yes: function(i, layero) {
        			_this.contactPersonInfo.partyProductLineIdList = _this.checkboxModel;
        			_this.contactPersonInfo.partyProductLineList = [];
        			for(var j = 0; j < _this.checkboxModel.length; j++){
        				for(var k = 0; k < _this.salesInfo.partyProductLineList.length; k++){
        					if(_this.checkboxModel[j] === _this.salesInfo.partyProductLineList[k].partyProductLineId){
        						_this.contactPersonInfo.partyProductLineList.push(_this.salesInfo.partyProductLineList[k]);
        					}
        				}
        			}
        			_this.productFlagNum += 1;
        			layer.close(i);
        		},
        		cancel: function(i, layero) {
        		}
        	})
        },
        checkedAll: function(){
        	var _this = this;
            if (this.checked) {//实现反选
              _this.checkboxModel = [];
            }else{//实现全选
              _this.checkboxModel = [];
              _this.productLines.forEach(function(item) {
                _this.checkboxModel.push(item.partyProductLineId);
              });
            }
        },
        saveBaseData: function(){
        	var _this = this;
    		$('#createVendorBase').submit();
    		
        	if(!validate || $("#createVendorBase input.error").length > 0){
        		return false;
        	}
        	if(_this.enquiryNameCheckboxModel.length==0){
        		layer.msg('询价员不能为空');
        		return false;
        	}
        	if(_this.offerNameCheckboxModel.length==0){
        		layer.msg('报价员不能为空');
        		return false;
        	}
        	_this.baseData.deptId = $("[name='deptId']").val();
        	_this.baseData.deptName = $("[name='deptId'] option:selected").text();
        	/*_this.baseData.enquiryId = $("[name='enquiryId']").val();
        	_this.baseData.enquiryName = $("[name='enquiryId'] option:selected").text();*/
        	_this.baseData.principalId = $("[name='principalId']").val();
        	_this.baseData.principalName = $("[name='principalId'] option:selected").text();
        	/*_this.baseData.offerId = $("[name='offerId']").val();
        	_this.baseData.offerName = $("[name='offerId'] option:selected").text();*/
        	_this.baseData.categoryName = $("[name='category'] option:selected").text();
        	_this.baseData.regionName = $("[name='region'] option:selected").text();
        	_this.baseData.genreName = $('input[name="genre"]:checked').parent().text();
        	_this.baseData.enquiryList = _this.enquiryNameCheckboxModel;
        	_this.baseData.offerList = _this.offerNameCheckboxModel;
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
        	layer.open({
        		type: 1,
        		title: '确认提交',
        		area: '500px',
        		offset: '300px',
        		move: false,
        		skin: "s_select_data",
        		content: $('.basedata-describe'),
        		btn: ['确定', '取消'],
        		yes: function(i, layero) {
        			var postData;
        			postData = _this.baseData;
        			if(!_this.baseDescribe){
        				layer.msg('描述不能为空');
        				return false;
        			}
        			
        			if(_this.baseDescribe.length > 100){
        				layer.msg('描述长度不能超过100个字符');
        				return false;
        			}
        			postData.describe = _this.baseDescribe;
        			postData.partyId = getQueryString('partyId');
        			delete _this.baseData.vendorInfoAttributeMap.VENDOR_CREDIT_PURCHASEDEAL;
        			delete _this.baseData.vendorInfoAttributeMap.VENDOR_CREDIT_PURCHASEDEALDATE;
        			delete _this.baseData.vendorInfoAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOL;
        			delete _this.baseData.vendorInfoAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE;
        			delete _this.baseData.vendorInfoAttributeMap.VENDOR_SALE_INFOVO_FOCUSFIELDS;
        			delete _this.baseData.vendorInfoAttributeMap.VENDOR_SALE_INFOVO_MAJORCLIENTS;
        			delete _this.baseData.vendorInfoAttributeMap.VENDOR_SALE_INFOVO_PRODUCTCATEGORYS;
        			delete _this.baseData.enquiryId;
        			delete _this.baseData.offerId;
        			syncData(ykyUrl.party + "/v1/vendorManage/updateVendorInfoVo", 'POST', postData, function(res, err) {
        				if (!err) {
        					layer.msg('基本信息提交成功！')
        					location.href= ykyUrl._this + '/myVendor.htm';
        				}
        			});
        			layer.close(i);
        		},
        		cancel: function(i, layero) {
        		}
        	})
        },
        //tab切换代理产品线和非代理产品线
        productLineChange: function(type){
        	var _this = this;
        	_this.prodctType = type;
        	
        },
        //保存产品线数据
        saveProductData: function(){
        	var _this = this;
    		
    		var flag = true;
    		for(var i = 0; i < _this.productLines.length; i++){
    			if(_this.productLines[i].brandSign === 'Y'){
    				layer.msg('已上传的产品线有误，请检查');
    				flag =  false;
    				break;
    			}
    		}
    		
    		if(!flag){
    			return false;
    		}
        	
    		layer.open({
        		type: 1,
        		title: '确认提交',
        		area: '500px',
        		offset: '300px',
        		move: false,
        		skin: "s_select_data",
        		content: $('.product-describe'),
        		btn: ['确定', '取消'],
        		yes: function(i, layero) {
        			var postData = {};
        			postData.partyProductLineList = _this.productLines.concat(_this.productNotProxyLines);
        			postData.describe = _this.prodctDescribe;
        			postData.partyId = getQueryString('partyId');
        			if(!_this.prodctDescribe){
        				layer.msg('描述不能为空');
        				return false;
        			}
        			
        			if(_this.prodctDescribe.length > 100){
        				layer.msg('描述长度不能超过100个字符');
        				return false;
        			}
        			syncData(ykyUrl.party + "/v1/vendorManage/updatePartyProductLine", 'POST', postData, function(res, err) {
        				if (!err) {
        					layer.msg('产品线信息提交成功！')
        					location.href= ykyUrl._this + '/myVendor.htm';
        				}
        			});
        			layer.close(i);
        		},
        		cancel: function(i, layero) {
        		}
        	})
        },
        saveCreditData: function(){
        	var _this = this;
        	$('#createVendorCredit').submit();
        	if(!validate || $("#createVendorCredit input.error").length > 0){
        		return false;
        	}
        	if(_this.creditInfo.partyBankAccount.length === 0){
        		layer.msg('请填写银行资料');
        		return false;
        	}
        	
        	layer.open({
        		type: 1,
        		title: '确认提交',
        		area: '500px',
        		offset: '300px',
        		move: false,
        		skin: "s_select_data",
        		content: $('.credit-describe'),
        		btn: ['确定', '取消'],
        		yes: function(i, layero) {
        			var postData;
        			postData = _this.creditInfo;
        			if(!_this.creditDescribe){
        				layer.msg('描述不能为空');
        				return false;
        			}
        			
        			if(_this.creditDescribe.length > 100){
        				layer.msg('描述长度不能超过100个字符');
        				return false;
        			}
        			postData.describe = _this.creditDescribe;
        			postData.partyId = getQueryString('partyId');
        			delete _this.creditInfo.creditAttributeMap.VENDOR_SALE_INFOVO_FOCUSFIELDS;
        			delete _this.creditInfo.creditAttributeMap.VENDOR_SALE_INFOVO_MAJORCLIENTS;
        			delete _this.creditInfo.creditAttributeMap.VENDOR_SALE_INFOVO_PRODUCTCATEGORYS;
        			delete _this.creditInfo.creditAttributeMap.VENDOR_INFO_EMPLOYEENUM;
        			delete _this.creditInfo.creditAttributeMap.VENDOR_INFO_LEGALPERSON;
        			delete _this.creditInfo.creditAttributeMap.VENDOR_INFO_REGPRICE;
        			delete _this.creditInfo.creditAttributeMap.VENDOR_INFO_REGRADDRESS;
        			delete _this.creditInfo.creditAttributeMap.VENDOR_INFO_WEBSITE;
        			syncData(ykyUrl.party + "/v1/vendorManage/updateVendorCreditVo", 'POST', postData, function(res, err) {
        				if (!err) {
        					layer.msg('信用信息提交成功！')
        					location.href= ykyUrl._this + '/vendor.htm';
        				}
        			});
        			layer.close(i);
        		},
        		cancel: function(i, layero) {
        		}
        	})
        },
        saveSalesData: function(){
        	var _this = this;
        	
        	$('#createVendorSales').submit();
        	
        	//检查仓库是否重名
    		var flag = checkWarehouse();
    		
        	if(flag){
        		layer.msg('仓库名称不能重复');
        		return false;
        	}
        	if(!validate || $("#createVendorSales input.error").length > 0 || $("#createVendorSales textarea.error").length > 0){
        		return false;
        	}
        	if(_this.salesInfo.contactPersonInfoList.length === 0 &&(_this.salesInfo && $('#userId').val() !== _this.salesInfo.offerId)){
        		layer.msg('请填写联系人信息');
        		return false;
        	}
        	
        	_this.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_FOCUSFIELDS = eval('('+$('#focusFields').val()+')').join(',');
        	_this.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_PRODUCTCATEGORYS = eval('('+$('#productCategorys').val()+')').join(',');
        	_this.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_MAJORCLIENTS = eval('('+$('#majorClients').val()+')').join(',');
        	/*_this.salesInfo.enquiryId = $("[name='saleEnquiryId']").val();
        	_this.salesInfo.enquiryName = $("[name='saleEnquiryId'] option:selected").text();*/
        	_this.salesInfo.principalId = $("[name='salePrincipalId']").val();
        	_this.salesInfo.principalName = $("[name='salePrincipalId'] option:selected").text();
        	/*_this.salesInfo.offerId = $("[name='saleOfferId']").val();
        	_this.salesInfo.offerName = $("[name='saleOfferId'] option:selected").text();*/
        	_this.salesInfo.enquiryList = _this.salesInfoEnquiryNameCheckboxModel;
        	_this.salesInfo.offerList = _this.salesInfoOfferNameCheckboxModel;
        	
        	var equiryLength = _this.salesInfoEnquiryNameCheckboxModel.length;
        	var offerLength = _this.salesInfoOfferNameCheckboxModel.length;
        	var offerListLength = _this.offerList.length;
        	var enquiryArray = [];
        	var offerArray = [];
        	for(var i=0;i<offerListLength;i++){
        		for(var j=0;j<equiryLength;j++){
            		if(_this.offerList[i].partyId==_this.salesInfoEnquiryNameCheckboxModel[j]){
            			enquiryArray.push(_this.offerList[i].partyAccount);
            		}
            	}
				for(var k=0;k<offerLength;k++){
					if(_this.offerList[i].partyId==_this.salesInfoOfferNameCheckboxModel[k]){
						offerArray.push(_this.offerList[i].partyAccount);
            		}        		
				}
			}
        	_this.salesInfo.offerName = offerArray.join(",");
        	_this.salesInfo.enquiryName = enquiryArray.join(",");
        	
        	layer.open({
        		type: 1,
        		title: '确认提交',
        		area: '500px',
        		offset: '300px',
        		move: false,
        		skin: "s_select_data",
        		content: $('.sales-describe'),
        		btn: ['确定', '取消'],
        		yes: function(i, layero) {
        			var postData;
        			postData = _this.salesInfo;
        			if(!_this.salesDescribe){
        				layer.msg('描述不能为空');
        				return false;
        			}
        			
        			if(_this.salesDescribe.length > 100){
        				layer.msg('描述长度不能超过100个字符');
        				return false;
        			}
        			postData.describe = _this.salesDescribe;
        			postData.partyId = getQueryString('partyId');
        			delete _this.salesInfo.saleAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE;
        			delete _this.salesInfo.saleAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOL;
        			delete _this.salesInfo.saleAttributeMap.VENDOR_CREDIT_PURCHASEDEALDATE;
        			delete _this.salesInfo.saleAttributeMap.VENDOR_CREDIT_PURCHASEDEAL;
        			delete _this.salesInfo.saleAttributeMap.VENDOR_INFO_EMPLOYEENUM;
        			delete _this.salesInfo.saleAttributeMap.VENDOR_INFO_LEGALPERSON;
        			delete _this.salesInfo.saleAttributeMap.VENDOR_INFO_REGPRICE;
        			delete _this.salesInfo.saleAttributeMap.VENDOR_INFO_REGRADDRESS;
        			delete _this.salesInfo.saleAttributeMap.VENDOR_INFO_WEBSITE;
        			syncData(ykyUrl.party + "/v1/vendorManage/updateVendorSaleInfoVo", 'POST', postData, function(res, err) {
        				if (!err) {
        					layer.msg('销售信息提交成功！')
        					location.href= ykyUrl._this + '/salesVendor.htm';
        				}
        			});
        			layer.close(i);
        		},
        		cancel: function(i, layero) {
        		}
        	})
        },
        saveCreateBaseData: function(){
        	var _this = this;
    		$('#createVendorBase').submit();
        	if(!validate || $("#createVendorBase input.error").length > 0){
        		return false;
        	}
        	if(vm.baseData.listed === 'Y'){
        		vm.baseData.stockCode = vm.stockCode;
        	}
        	
        	_this.baseData.deptId = $("[name='deptId']").val();
        	_this.baseData.deptName = $("[name='deptId'] option:selected").text();
        	_this.baseData.enquiryId = $("[name='enquiryId']").val();
        	_this.baseData.enquiryName = $("[name='enquiryId'] option:selected").text();
        	_this.baseData.principalId = $("[name='principalId']").val();
        	_this.baseData.principalName = $("[name='principalId'] option:selected").text();
        	_this.baseData.offerId = $("[name='offerId']").val();
        	_this.baseData.offerName = $("[name='offerId'] option:selected").text();
        	_this.baseData.categoryName = $("[name='category'] option:selected").text();
        	_this.baseData.regionName = $("[name='region'] option:selected").text();
        	_this.baseData.genreName = $('input[name="genre"]:checked').parent().text();
        	
        	_this.tabValue = 'product';
        },
        saveCreateProductData: function(){
        	var _this = this;
    		var flag = true;
    		for(var i = 0; i < _this.productLines.length; i++){
    			if(_this.productLines[i].brandSign === 'Y' || _this.productLines[i].category1Sign === 'Y' || _this.productLines[i].category2Sign === 'Y' || _this.productLines[i].category3Sign === 'Y'){
    				layer.msg('已上传的产品线有误，请检查');
    				flag = false;
    				break;
    			}
    		}
    		if(!flag){
    			return false;
    		}
        	_this.tabValue = 'credit';
        },
        //保存数据
        saveData: function(){
        	var _this = this;
        	$('#createVendorCredit').submit();
        	if(!validate || $("#createVendorCredit input.error").length > 0){
        		return false;
        	}
        	if(_this.creditInfo.partyBankAccount.length === 0){
        		layer.msg('请填写银行资料');
        		return false;
        	}
        	
        	_this.vendorMap.VENDOR_INFO_WEBSITE = _this.baseData.vendorInfoAttributeMap.VENDOR_INFO_WEBSITE;
        	_this.vendorMap.VENDOR_INFO_REGRADDRESS = _this.baseData.vendorInfoAttributeMap.VENDOR_INFO_REGRADDRESS;
        	_this.vendorMap.VENDOR_INFO_LEGALPERSON = _this.baseData.vendorInfoAttributeMap.VENDOR_INFO_LEGALPERSON;
        	_this.vendorMap.VENDOR_INFO_REGPRICE = _this.baseData.vendorInfoAttributeMap.VENDOR_INFO_REGPRICE;
        	_this.vendorMap.VENDOR_INFO_EMPLOYEENUM = _this.baseData.vendorInfoAttributeMap.VENDOR_INFO_EMPLOYEENUM;
        	_this.vendorMap.VENDOR_CREDIT_PURCHASEDEAL = _this.creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEAL;
        	_this.vendorMap.VENDOR_CREDIT_PURCHASEDEALDATE = _this.creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEALDATE;
        	_this.vendorMap.VENDOR_CREDIT_SECRECYPROTOCOL = _this.creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOL;
        	_this.vendorMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE = _this.creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE;
        	
        	//询价员，报价员
        	_this.baseData.enquiryList = _this.enquiryNameCheckboxModel;
        	_this.baseData.offerList = _this.offerNameCheckboxModel;
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
        	
        	var vendorObj = {
        			partyId: getQueryString('partyId'),
        			partyProductLineList: _this.productLines.concat(_this.productNotProxyLines),
        			vendorCreditVo : _this.creditInfo,
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
        deleteManufacturerSelect:function(index,id){  //专属特价部分函数
			this.speaiclRuleProductLine.chosenManufacturer.splice(index,1);
		},
		deleteManufacturer:function(index,id){
			this.speaiclRuleProductLine.sels.splice(index,1);
		},
		specialRuleGetSelected: function(obj){  //获取选中值
			var list = [];
			$.each(obj,function(index,item){
				list.push({
					"id":item.id,
					"brandName":item.brandName
				})
			})
  		    this.speaiclRuleProductLine.sels  = list;
        },
        speaiclRuleProductLineDel:function(){
        	vm.$refs.speaiclRuleProductLineLetter.del(id);
        },
		choseSpecialRuleManufacturer:function(){
			this.specialRuleShowModal = true;
			var list  = this.speaiclRuleProductLine.chosenManufacturer;
			this.speaiclRuleProductLine.sels= this.speaiclRuleProductLine.chosenManufacturer;
			var arr = [];
			if(list.length>0){
				$.each(list,function(index,item){
					var id = item.id;
	        		var idFormated = id;
					arr.push(idFormated);
				})
        	}
			this.speaiclRuleProductLine.selected = arr;
			this.speaiclRuleShowManufacturer = true;
		},
		choseSpecialRuleWharehouse:function(){
			/*this.specialRuleShowModal = true;*/
		},
		modalOk: function(data) { //ok调用方法
            this.specialRuleShowModal = false;
            this.speaiclRuleShowManufacturer = false;
            this.speaiclRuleProductLine.chosenManufacturer = this.speaiclRuleProductLine.sels;
        },
        toggleModal: function() { //显示隐藏；cancel调用方法
            this.speaiclRuleShowManufacturer = false;
            this.specialRuleShowModal = !this.specialRuleShowModal;
        },
        updateRulesText:function(){
        	var text = this.specialRulesText;
        	specialOfferUpdateRuleText(partyId,text);
        },
        updateRuleStatus:function(e){
        	specialOfferUpdateRuleStatus(partyId,e);
        },
        addSpecialRule: function(){
        	if(this.specialRuleStatus == 'ALL' || this.specialRuleStatus == 'INOPERATIVE'){
        		return false;
        	}
        	this.showSpecialEdit = true;
        	this.showTabcate = true;
        	this.isAddSpecialRule = true;
        	this.specialRuleType = 'RULE';
        	this.speaiclRuleProductLine.addOrEdit = "POST";
        	var that = this;
        	syncData(ykyUrl.product + '/v1/specialOfferRule/getRuleId', 'GET', null, function(res, err){
        		if(err == null){  //专属特价为型号时、应用型号为excel上传时需传ruleId,所以新增规则时先生成ruleId
                	that.currentSpecialRuleId = res;
                	that.uploadPrdQueryParams.ruleId = res;
        		}
        	})
        },
        specialInitRule: function(){ //专属特价数据初始化
        	this.specialRuleType = 'RULE';
        	this.specialMpnType = 'UPLOAD';
        	this.specialRemark = '';
        	this.specialUploadFileName = '';
        	this.specialUploadFileUrl = '';
        	this.speaiclRuleProductLine.chosenManufacturer = [];//专属特价产品线相关参数初始化
        	var wareList = this.speaiclRuleProductLine.wareList;
			$.each(wareList,function(index,item){
				item.isChecked = false;
			})
			this.speaiclRuleProductLine.wareList = wareList;
			this.speaiclRuleProductLine.isAllChecked = false;//特价仓库不限置为不选
			$('.input_ware_check').attr("disabled",false);//特价仓库除不限外的inut设置为可选
			this.speaiclRuleProductLine.cateInitData = [];
			this.speaiclRuleProductLine.addOrEdit = "POST";
			
        	this.showTabcate = false;
        	this.uploadPrdColumns[0].hide = false;
        	this.uploadPrdColumns[this.uploadPrdColumns.length-1].hide = false;
        	this.uploadPrdQueryParams.page = 1;
        },
        specialCancelEvent: function(){ //专属特价取消事件
        	this.speListQueryParams.defaultStatus = !this.speListQueryParams.defaultStatus;
        	this.isAddSpecialRule = false;
        	this.showSpecialEdit = false;
        	this.showSpecialDetail = false;
        	this.showTabcate = false;
        	this.specialInitRule();
        },
        specialSaveEvent: function(){ //专属特价保存事件
        	var that = this;
        	if(this.specialRuleType == 'RULE'){ //规则类型为产品线
        		var manufacturerList = [];
        		var wareArr = [];
        		var list = this.speaiclRuleProductLine.chosenManufacturer;
        		var warelist = this.speaiclRuleProductLine.wareList;
        		$.each(list,function(index,item){
        			if(item.id){
        				manufacturerList.push(item.id);
        			}
        		});
        		$.each(warelist,function(index,item){
        			if(item.id&&item.isChecked==true){
        				wareArr.push(item.id);
        			}
        		})
        		
        		var cateList = [];
        		var checkedCategoriesList = this.speaiclRuleProductLine.checkedCategories;
        		$.each(checkedCategoriesList,function(index,item){
        			cateList.push(item.id);
        		})
        		var params  = {
        		   "id":this.currentSpecialRuleId,
        		   "vendorId":partyId,
				   "ruleType": "RULE",
				   "mfrIds":manufacturerList,
				   "sourceIds":wareArr.length?wareArr:(this.speaiclRuleProductLine.isAllChecked==true?["*"]:[]),
				   "catIds":cateList,
				   "desc":this.specialRemark
        		}
        		if(params.mfrIds.length>0||params.sourceIds.length>0||params.catIds.length>0){
        			postOrPutSpecialOfferRule(params,this.speaiclRuleProductLine.addOrEdit);
        			this.specialCancelEvent();
        		}else{
        			layer.msg('请至少选择一个应用条件！');
        		}
        	}else if(this.specialRuleType == 'MPN'){ //规则类型为型号
        		var dataObj = {
        				vendorId: partyId,
        				ruleType:'MPN',
        				mpnType: this.specialMpnType,
        				desc:this.specialRemark
        		}
        		var url = this.isAddSpecialRule ? ykyUrl.product + '/v1/specialOfferRule?ruleId=' + this.currentSpecialRuleId : ykyUrl.product + '/v1/specialOfferRule';
        		var type = this.isAddSpecialRule ? 'POST' : 'PUT';
        		if(!this.isAddSpecialRule){
        			dataObj['id'] = this.currentSpecialRuleId;
        		}
        		var layerIdx = layer.load();
        		syncData(url, type, dataObj, function(res,err){
        			layer.close(layerIdx);
        			if(err == null){
        				that.specialCancelEvent();
        			}
        		})
        	}
        },
        editAssociatePrd: function(){ //专属特价手动添加编辑型号
        	this.showModal = true;
        	this.showPrdAssociate = true;
        	//this.productAssociate.isAdd = true;
        	this.productAssociate.productData = {
        			id:'',
					imageUrl:'',
					manufacturer:'',
					model:'',
					prdUrl:'',
					sourceId:'',
					sourceName:''
        	}
        },
        prdAssociateCancel: function(){
        	this.showModal = false;
        	this.showPrdAssociate = false;
        },
        getProductInfo: function(data){
        	var that = this;
        	if(data.vendorId != this.partyId){
        		layer.msg('请添加当前供应商下的型号！');
        		return;
        	}
        	var obj = {
        			vendorId:this.partyId,
        			ruleId:this.currentSpecialRuleId,
        			mpn:data.model ? data.model : '',
        			vendorName:data.vendorName,
					mpnUrl:data.prdUrl ? data.prdUrl : '',
					brandId:data.manufacturerId ? data.manufacturerId : '',
					brandName:data.manufacturer ? data.manufacturer : '',
					productId:data.id ? data.id : '',
					imageUrl:data.imageUrl ? data.imageUrl : '',
					sourceId:data.sourceId ? data.sourceId : '',
					sourceName:data.sourceName ? data.sourceName : ''
        	}
        	syncData(ykyUrl.product + '/v1/specialOfferProductDraft', 'POST', obj, function(res, err){
        		if(err == null){
        			that.showModal = false;
                	that.showPrdAssociate = false;
        			that.uploadPrdQueryParams.defaultStatus = !that.uploadPrdQueryParams.defaultStatus;
        		}
        	})
        },
        uploadPrdmultDel: function(id){
        	var that = this, ids = [], checkedItem;
        	if(id){
        		ids.push(id);
        	}else{
        		checkedItem = eval('('+$("#checkedIds").val()+')');
				if(checkedItem.length > 0){
					for(var i =0; i<checkedItem.length; i++){
						ids.push(checkedItem[i].id)
					}
				}else{
					layer.msg('请选择需要删除的数据')
					return false;
				}
        	}
			var index = layer.confirm('确认删除？',{
				 btn: ['确认','取消'] //按钮
			},function(){
				var url = ykyUrl.product + '/v1/specialOfferProductDraft/delete';
				multiDelete(url, 'DELETE', ids, function(res, err) {//页面加载前调用方法
					that.uploadPrdQueryParams.defaultStatus = !that.uploadPrdQueryParams.defaultStatus;
				});
				layer.close(index);
			})
        },
        exportSpeaiclPrd:function(type){
        	var conTr = $(".uploadPrdList .table-container tbody tr");
			var conTd = $(".uploadPrdList .table-container tbody tr td"); 
			if(conTr.length == 1 && conTd.length == 1) {
				layer.msg('没有数据可以下载')
				return false;
			}
        	if(type == 'error'){
        		$("input[name=ids]").val('');
        		$("input[name=status]").val('UNABLE');
        	}else{
        		var ids = '', checkedItem;
    			checkedItem = eval('('+$("#checkedIds").val()+')');
    			if(checkedItem.length > 0){
    				for(var i =0; i<checkedItem.length; i++){
    					if(ids !== ''){
    						ids += ','+checkedItem[i].id
    					}else{
    						ids = checkedItem[i].id;
    					}
    				}
    			}else{
    				layer.msg('请选择需要下载的数据')
    				return false;
    			}
        		$("input[name=ids]").val(ids);
        		$("input[name=status]").val('');
        	}
        	$("input[name=ruleId]").val(this.currentSpecialRuleId);
        	var date_url = ykyUrl.product + "/v1/specialOfferProductDraft/export";
			var form=$("#exportForm");//定义一个form表单
		    form.attr("action",date_url);
		    form.submit();//表单提交
        }
	},
	watch: {
		'departmentId': function(newVal,oldVal){
			if(!newVal){
				vm.departmentId = oldVal;
				return false;	
			}
			getUserList(newVal);
			if(this.offerList.length  === 0){
				getOfferList(operateDeptId)
			}
			
		},
		'saleDepartmentId': function(newVal,oldVal){
			if(!newVal){
				vm.saleDepartmentId = oldVal;
				return false;	
			}
			getSalesUserList(newVal);
			if(this.offerList.length  === 0){
				getSalesOfferList(operateDeptId)
			}
		},
		'supportCurrency': function(newVal){
			for(var i = 0; i < newVal.length; i++){
				if(newVal[i] === 'RMB'){
					$('input[value="RMB"]').prop('checked',true);
					vm.showRmb = true;
				}
				if(newVal[i] === 'USD'){
					$('input[value="USD"]').prop('checked',true);
					vm.showUsd = true;
				}
			}
		},
		'checkboxModel': {
		    handler: function (val, oldVal) { 
		      if (this.checkboxModel.length === this.productLines.length) {
		        this.checked=true;
		      }else{
		        this.checked=false;
		      }
		    },
		    deep: true
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
		  },
		  'salesInfoEnquiryNameCheckboxModel': {
			    handler: function (val, oldVal) { 
			      if (this.salesInfoEnquiryNameCheckboxModel.length === this.offerList.length) {
			        this.salesInfoEnquiryNameChecked=true;
			      }else{
			        this.salesInfoEnquiryNameChecked=false;
			      }
			    },
			    deep: true
		  },
		  'salesInfoOfferNameCheckboxModel': {
			    handler: function (val, oldVal) { 
			      if (this.salesInfoOfferNameCheckboxModel.length === this.offerList.length) {
			        this.salesInfoOfferNameChecked=true;
			      }else{
			        this.salesInfoOfferNameChecked=false;
			      }
			    },
			    deep: true
		  }
	},
	
});
//专属特价型号手动添加商品详情
/*function detailInputPrd(index,params){
	var data = params[0];
	var obj = {
			id:data.productId, //商品id
    		prdUrl:data.mpnUrl, //型号链接
			model:data.mpn, //型号
			manufacturer:data.brand,  //原厂
			manufacturerId:data.brandId, //原厂id
			imageUrl:data.imageUrl
	}
	vm.productAssociate.productData = obj;
	vm.showModal = true;
	vm.showPrdAssociate = true;
	vm.productAssociate.isAdd = false;
}*/
//专属特价型号手动添加商品编辑
function editInputPrd(index,params){
	var data = params[0];
	var obj = {
			id:data.productId, //商品id
    		prdUrl:data.mpnUrl, //型号链接
			model:data.mpn, //型号
			manufacturer:data.brand,  //原厂
			manufacturerId:data.brandId, //原厂id
			imageUrl:data.imageUrl
	}
	vm.productAssociate.productData = obj;
	vm.showModal = true;
	vm.showPrdAssociate = true;
	//vm.productAssociate.isAdd = true;
}
//专属特价型号商品删除
function delSpeaiclPrd(index,params){
	vm.uploadPrdmultDel(params[0]);
}

//判断仓库是否重复
function checkWarehouse(){
	var list = vm.salesInfo.facilityList;
	var arrList = [];
	var hash = {};
	for(var i = 0; i < list.length; i++){
		arrList.push(list[i].facilityName);
	}
	for(var i in arrList) {
       if(hash[arrList[i]]){
           return true;
       }
       hash[arrList[i]] = true;
    }
   return false;
}
	
function getBaseData(id){
	syncData(ykyUrl.party + "/v1/vendors/vendorInfo/"+id, 'GET', null, function(res, err) {
		if (!err) {
			Vue.nextTick(function(){
				vm.baseData = res;
				var userId = $('#userId').val();
				if(res){
					if((res.enquiryId && $.inArray(res.enquiryList, userId)!=-1 ) || (res.principalId && res.principalId === userId)){
						vm.noEdit = true;
					}
					if(!vm.baseData.vendorInfoAttributeMap){
						vm.baseData.vendorInfoAttributeMap = {}
					}
					
					if(!vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_WEBSITE){
						vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_WEBSITE = ''
					}
					if(!vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_LEGALPERSON){
						vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_LEGALPERSON = ''
					}
					if(!vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_REGPRICE){
						vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_REGPRICE = ''
					}
					if(!vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_REGRADDRESS){
						vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_REGRADDRESS = ''
					}
					if(!vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_EMPLOYEENUM){
						vm.baseData.vendorInfoAttributeMap.VENDOR_INFO_EMPLOYEENUM = ''
					}
					if(!vm.baseData.category){
						vm.baseData.category = '100001'
					}
					if(!vm.baseData.region){
						vm.baseData.region = '100100'
					}
					if(!vm.baseData.generalHeadquarters){
						vm.baseData.generalHeadquarters = ''
					}
					if(!vm.baseData.genre){
						vm.baseData.genre = '100200'
					}
					if(!vm.baseData.enquiryId){
						vm.baseData.enquiryId = ''
					}
					if(!vm.baseData.principalId){
						vm.baseData.principalId = ''
					}
					if(!vm.baseData.deptId){
						vm.baseData.deptId = ''
					}
					if(!vm.baseData.offerId){
						vm.baseData.offerId = '';
					}
				}
				getDeptList();
				if(!vm.baseData.offerId || !vm.baseData.enquiryId){
					getOfferList(operateDeptId)
				}
				getPrivateUrl(vm.baseData.logoImageUrl);
			})
			
		}
	});
}

function getProductLine(id){
	syncData(ykyUrl.party + "/v1/vendors/productLine/"+id, 'GET', null, function(res, err) {
		if (!err) {
			var productList = [];
			var productListNot = [];
			for(var i = 0; i < res.length; i++){
				if(res[i].type == 'NOT_PROXY'){
					productListNot.push(res[i]);
				}else{
					productList.push(res[i]);
				}
			}
			vm.productLines = productList;
			vm.productNotProxyLines = productListNot;
		}
	});
}

function getCreditInfo(id){
	syncData(ykyUrl.party + "/v1/vendors/vendorCredit/"+id, 'GET', null, function(res, err) {
		if (!err) {
			vm.creditInfo = res;
			
			if(res){	
				if(!vm.creditInfo.creditAttributeMap){
					vm.creditInfo.creditAttributeMap = {};
				}
				if(!vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEAL){
					vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEAL = 'NOT_SIGN';
				}
				if(!vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEALDATE){
					vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEALDATE = '';
				}
				if(!vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOL){
					vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOL = 'NOT_SIGN';
				}
				if(!vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE){
					vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE = '';
				}
				if(!vm.creditInfo.checkDate){
					vm.creditInfo.checkDate = '';
				}
				if(!vm.creditInfo.creditDeadline){
					vm.creditInfo.creditDeadline = '';
				}
				if(!vm.creditInfo.creditQuota){
					vm.creditInfo.creditQuota = '';
				}
				if(!vm.creditInfo.currency){
					vm.creditInfo.currency = 'CNY';
				}
				if(!vm.creditInfo.partyBankAccount){
					vm.creditInfo.partyBankAccount = [];
				}
				if(!vm.creditInfo.payDate){
					vm.creditInfo.payDate = '';
				}
				if(!vm.creditInfo.paymentTerms){
					vm.creditInfo.paymentTerms = '';
				}
				if(!vm.creditInfo.creditAttachmentList){
					vm.creditInfo.creditAttachmentList = [];
				}
			}
		}
	});
}

function getSalesInfo(id){
	syncData(ykyUrl.party + "/v1/vendors/vendorSaleInfo/"+id, 'GET', null, function(res, err) {
		if (!err) {
			if(res.offerId && res.offerId === $('#userId').val() && res.enquiryId !== $('#userId').val() && res.principalId !== $('#userId').val()){
				vm.noContactInfo = true;
			}
			if(getQueryString('status') !== 'salesEdit' && !res.orderVerify){
				vm.noSales = true;
			}
			vm.salesInfo = res;
			
			if(res){
				if(!vm.salesInfo.saleAttributeMap){
					vm.salesInfo.saleAttributeMap = {};
				}
				if(!vm.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_FOCUSFIELDS){
					vm.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_FOCUSFIELDS = '';
				}
				if(!vm.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_MAJORCLIENTS){
					vm.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_MAJORCLIENTS = '';
				}
				if(!vm.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_PRODUCTCATEGORYS){
					vm.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_PRODUCTCATEGORYS = '';
				}
				if(!vm.salesInfo.saleAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE){
					vm.salesInfo.saleAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE = '';
				}
				if(!vm.salesInfo.contactPersonInfoList){
					vm.salesInfo.contactPersonInfoList = [];
				}
				if(!vm.salesInfo.description){
					vm.salesInfo.description = '';
				}
				if(!vm.salesInfo.isShowName){
					vm.salesInfo.isShowName = 'Y';
				}
				if(!vm.salesInfo.lastPayDate){
					vm.salesInfo.lastPayDate = '';
				}
				if(!vm.salesInfo.lastPaymPrice){
					vm.salesInfo.lastPaymPrice = '';
				}
				if(!vm.salesInfo.lastTransactionDate){
					vm.salesInfo.lastTransactionDate = '';
				}
				if(!vm.salesInfo.lastTransactionPrice){
					vm.salesInfo.lastTransactionPrice = '';
				}
				if(!vm.salesInfo.minOrderPriceCNY){
					vm.salesInfo.minOrderPriceCNY = '';
				}
				if(!vm.salesInfo.minOrderPriceUSD){
					vm.salesInfo.minOrderPriceUSD = '';
				}
				if(!vm.salesInfo.orderVerify){
					vm.salesInfo.orderVerify = 'N';
				}
				if(!vm.salesInfo.supportCurrency){
					vm.salesInfo.supportCurrency = '';
				}
				if(!vm.salesInfo.facilityList){
					vm.salesInfo.facilityList = [{facilityName:'',isShowaFacility:'N'}];
				}
				if(!vm.salesInfo.skuMovStatus){
					vm.salesInfo.skuMovStatus = 'N';
				}
				if(!vm.salesInfo.vendorMovStatus){
					vm.salesInfo.vendorMovStatus = 'N';
				}
				if(!vm.salesInfo.isAutoIntegrateQty){
					vm.salesInfo.isAutoIntegrateQty = 'N';
				}
				if(!vm.salesInfo.productInvalidDays){
					vm.salesInfo.productInvalidDays = '';
				}
				
				if(!vm.salesInfo.offerId){
					vm.salesInfo.offerId = '';
				}
				if(!vm.salesInfo.enquiryId){
					vm.salesInfo.enquiryId = '';
				}
			}
			vm.majorClientsOptions= vm.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_MAJORCLIENTS.split(',');
			vm.focusFieldsOptions= vm.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_FOCUSFIELDS.split(',');
			vm.productCategorysOptions= vm.salesInfo.saleAttributeMap.VENDOR_SALE_INFOVO_PRODUCTCATEGORYS.split(',');
			vm.supportCurrency = vm.salesInfo.supportCurrency.split(',');
			if(res){	
				for(var i = 0; i<vm.salesInfo.contactPersonInfoList.length; i++){
					if(vm.salesInfo.contactPersonInfoList[i].partyProductLineIdList){
						vm.salesInfo.contactPersonInfoList[i].partyProductLineList = [];
						for(var j = 0; j < vm.salesInfo.contactPersonInfoList[i].partyProductLineIdList.length; j++){
	        				for(var k = 0; k < vm.salesInfo.partyProductLineList.length; k++){
	        					if(vm.salesInfo.contactPersonInfoList[i].partyProductLineIdList[j] === vm.salesInfo.partyProductLineList[k].partyProductLineId){
	        						vm.salesInfo.contactPersonInfoList[i].partyProductLineList.push(vm.salesInfo.partyProductLineList[k]);
	        					}
	        				}
	        			}
					}    					        		
				}
				if(vm.salesInfo.skuMovStatus === 'Y'){
					$('input[name="skuMovStatus"]').prop('checked',true);
				}
				if(vm.salesInfo.vendorMovStatus === 'Y'){
					$('input[name="vendorMovStatus"]').prop('checked',true);
				}
				
				Vue.nextTick(function(){
					for(var i = 0;i < res.facilityList.length; i++){
						if(res.facilityList[i].isShowaFacility === 'Y'){
							$('input[name="isShowaFacility"]').eq(i).prop('checked',true);
						}
					} 
				})
				
			}
			if(!vm.salesInfo.enquiryList || !vm.salesInfo.offerList){
				getSalesOfferList(operateDeptId)
			}
			if(getQueryString('status') !== 'applyNo' && !vm.noSales){
				uploaderSpecialExcel.init(); 
			}
		}
	});
}

function getAuditList(id){
	syncData(ykyUrl.party + "/v1/audit?actionRst=SUCCEEDED&size=1000&actionId="+id, 'GET', null, function(res, err) {
		if (!err) {
			vm.auditList = res.list;
		}
	});
}

//获取图片地址
var getPrivateUrl =  function(url){
	$.aAjax({
		url: ykyUrl.party + "/v1/enterprises/getImgUrl",
     	type:"POST",
     	data:JSON.stringify({"id":url}),
     	success: function(data) {
     		if(null !=data || data == ""){
     			vm.vendorLogo = data;
     		}
     	},
     	error:function(e){
	    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
	    }
 	});
}


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

//获取仓库数据
function getWareList(id){  //传入供应商id  
	syncData(ykyUrl.party + "/v1/facility?id="+id, 'GET', null, function(res, err) {
		if (!err) {
			var  mlist = res;
			
			var  isNone = vm.isChecked ;
			if(isNone){
				mlist.forEach(function (item) {
					item.isChecked = false;
			    })
			    
			    $('.input_checkall').prop('checked',true);  //选中不限 
				$('.input_check').attr("disabled",true);
			}else{
				var slist = vm.sourceIds;
				$.each(mlist,function (j,item) {  
					// item.isChecked = true ;
	            	 
	            	 $.each(slist,function(i,el){
	            		if(el == item.id){
	            			 item.isChecked = true ;
	            			 $('.input_check').eq(i).attr("disabled",false);
	            		//	 $('.input_check').eq(i).prop('checked',true); 
	            		 }/*else{
	            			 item.isChecked = false ;
	            			// $('.input_check').eq(i).attr("disabled",true);  //不可选
	            		 }*/
	            	 })
					
				})
			}
		
			  /*res.forEach(function (item) { 
				   item.isChecked = false ;
				})*/
			vm.wareList = JSON.parse(JSON.stringify(mlist));
			var tempList = res;
			$.each(res,function(index,item){
				tempList[index].isChecked = false;
			})
			vm.speaiclRuleProductLine.wareList = tempList;
			/*vm.wareList.forEach(function (item,i) {
				if( item.isChecked == true){
					  $('.input_check').eq(i).prop('checked',true)
				}
				  
			})*/
		}
	});
}

//获取供应商URL,mapi
function getmapi(partyId){
	 $.aAjax({
		 	url:ykyUrl.party +"/v1/vendors/onlyProductLine/"+partyId,
		 	type:"GET",
		 	success:function(data){		
		 		if(data && data.length > 0){
		 			vm.moptions = data;
		 			getAllbrands("nomoptions");
		 		}else{
		 			getAllbrands();
		 		}
		 	},
		 	error:function(data){
		 		console.log('error');
		 	}
		 })
		
	
}

function  getAllbrands(type){
	 $.aAjax({
		 	url:ykyUrl.product +"/v1/products/brands",
		 	type:"GET",
		 	success:function(res){	
		 		for(var i = 0; i< res.length; i++){
		 			res[i].brandId = res[i].id;
		 		}
		 		if(type!="nomoptions"){
		 			vm.moptions = res;
		 		}
		 		
		 		var arr = [];
		 		var list = res;
	    		$.each(list,function(i,item){
	    			arr.push({
	    				id:item.id,
	    				brandName:item.brandName
	    			})
	    		})
	    		vm.speaiclRuleProductLine.options = arr;
		 	},
		 	error:function(res){
		 		console.log('error');
		 	}
		 })
}

//获取供应商别名
function getVendorAlias(partyId){
	syncData(ykyUrl.party + "/v1/vendorManage/supperAliasNameList?partyId="+partyId, 'GET', null, function(res, err) {
		if(!err&&res&&res.length){
			vm.aliasInfo.aliasList = res;
			if(res.length>=10){
				vm.aliasInfo.hasAddVendorAliasBtn = false;
				
			}
			Vue.nextTick(function () {
				// DOM 更新了
				validateVendorAlias();
			})
		}else{
			Vue.nextTick(function () {
				// DOM 更新了
				validateVendorAlias();
			})  
		}
	})
}
//获取mov数据
function getMovData(ruleId){
	vm.manufacturerIds = [];
	$('#usdMovAmount').removeClass('snValid')
	$('#cnyMovAmount').removeClass('snValid')
	var partyId = getQueryString('partyId');
	$('#MovData').removeClass("disable");   //初始化mov编辑页数据时，
	syncData(ykyUrl.product + "/v2/rules/mov/"+ ruleId, 'GET', null, function(res, err) {
		if (!err) {
			//赋值
			     if(res.ruleId){
			    	  vm.ruleId = res.ruleId;
			      }
				 if(res.ruleType){
					 vm.ruleType = res.ruleType;
				 }
				 if(res.sourceIds){//重置选择的仓库
					 vm.sourceIds = res.sourceIds ;
					 if(res.sourceIds[0]  ==  'none'){
						 vm.isChecked = true;
						  }
					 getWareList(partyId); 
				 }
				 if(res.manufacturerIds){  //制造商id集合 
					// vm.mdefalut = false;   //初始化时，集合有值，显示重新选择 
					 var  idlist =  res.manufacturerIds;
					//获取制造商列表 
	        		 $.aAjax({
	        		 	url:ykyUrl.party +"/v1/vendors/onlyProductLine/"+partyId+"?type=PROXY",
	        		 	type:"GET",
	        		 	success:function(data){	//data是制造商集合，没值的时候，请求全部的制造商 
	        		 		   if(data.length == 0 ){
	        		 			  $.aAjax({
	        		 				 	url:ykyUrl.product +"/v1/products/brands",
	        		 				 	type:"GET",
	        		 				 	success:function(res){
	        		 				 		for(var i = 0; i< res.length; i++){
	        		 				 			res[i].brandId = res[i].id;
	        		 				 		}
	        		 				 		if(res){
	        		 				 			var manuflist =  res;
	        		 				 			$.each(idlist,function(i,ele){
		    			        		 			 $.each(manuflist,function(j,elm){
		    			        		 				 if(ele ==  elm.brandId){
		    			 		   							vm.manufacturerIds.push(elm);
		    			 		   						 }
		    			        		 			 })
		    			   						
		    			   					   });	
	        		 				 		}
	        		 				 		
	    			        		 		
	        		 				 	},
	        		 				 	error:function(res){
	        		 				 		console.log('error');
	        		 				 	}
	        		 				 })  
	        		 		   }else{
	        		 			  var manuflist = data;
			        		 		$.each(idlist,function(i,ele){
			        		 			 $.each(manuflist,function(j,elm){
			        		 				 if(ele ===  elm.brandId){
			 		   							vm.manufacturerIds.push(elm);
			 		   						 }
			        		 			 })
			   						
			   					   });
	        		 		   }
	        		 		
	        		 	},
	        		 	error:function(data){
	        		 		console.log('error');
	        		 	}
	        		 })
				    vm.mselected = res.manufacturerIds;
				 }
				 if(res.cnyMovAmount){
					vm.cnyMovAmount = res.cnyMovAmount;	
					 $('#CH').prop('checked',true);  //修改初始化时，选中
				  }else{
					  $('#CH').prop('checked',false);
					  vm.cnyMovAmount = '';
				  }
				 if(res.usdMovAmount){
					 vm.usdMovAmount = res.usdMovAmount;
					 $('#HK').prop('checked',true);
				 }else{
					 $('#HK').prop('checked',false);
					 vm.usdMovAmount = '';
				 }
				 if(res.description){
					vm.movdescription = res.description;
				 }else{
					 vm.movdescription = '';
				 }
				 if(res.status){
					 vm.status = res.status;
				 }
				 if(res.createdDate){
					 vm.createdDate = res.createdDate;
				 }
				if( res.creator){
					 vm.creator = res.creator;
				}
				if(res.lastUpdateDate){
					 vm.lastUpdateDate = res.lastUpdateDate;
				}
				if(res.lastUpdateUser){
				    vm.lastUpdateUser = res.lastUpdateUser;
				}
			}
		
	});
	
}

//获取汇率
function getExchangeRate(){
	//获取汇率
	 $.aAjax({
	 	url:ykyUrl.database +'/v1/basedata/exchangerate/USD/CNY',
	 	type:"GET",
	 	success:function(data){			
	 		vm.exchangeRate = data.exchangeRate;
	 		if(vm.usdMovAmount){
	 			vm.conversionAmount = (vm.usdMovAmount*vm.exchangeRate*vm.taxRate).toFixed(2)
	 		}
	 	},
	 	error:function(data){
	 		console.log('error');
	 	}
	 })
}
//获取mov列表数据
function getMovList(){
	var vendorId = getQueryString('partyId');
	syncData(ykyUrl.product + "/v2/rules/mov?page=1&size=20&vendorId="+vendorId, 'GET', null, function(res, err) {
		if (!err) {
			
			vm.movList = res;
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
			vm.departmentId = vm.baseData.deptId;
			vm.saleDepartmentId = vm.baseData.deptId;
		}
	});
}

//获取负责人、询价员数据
function getUserList(id){
	syncData(ykyUrl.party + "/v1/dept/findCustomerByDeptId?page=1&size=100&deptId="+id, 'GET', null, function(res, err) {
		if (!err) {
			vm.userList = res.list;
			if(res && res.list.length > 0){
				if(vm.baseData.principalId){
					vm.principalId = vm.baseData.principalId
				}
			}
		}
	});
}

//获取负责人数据
function getSalesUserList(id){
	syncData(ykyUrl.party + "/v1/dept/findCustomerByDeptId?page=1&size=100&deptId="+id, 'GET', null, function(res, err) {
		if (!err) {
			vm.userList = res.list;
			if(res && res.list.length > 0){
				if(vm.salesInfo.principalId){
					vm.salePrincipalId = vm.salesInfo.principalId
				}
			}
		}
	});
}
//获取运营部下所有人
function getOfferList(id){
	syncData(ykyUrl.party + "/v1/dept/findCustomerByDeptId?page=1&size=100&deptId="+id, 'GET', null, function(res, err) {
		if (!err) {
			vm.offerList = res.list;
			if(vm.offerList && vm.offerList.length > 0){
				if(vm.baseData.enquiryList){
					vm.enquiryNameCheckboxModel = vm.baseData.enquiryList;
					
				}
				if(vm.baseData.offerList){
					vm.offerNameCheckboxModel = vm.baseData.offerList;
				}
			}
		}
	});
}

//获取运营部下所有人
function getSalesOfferList(id){
	syncData(ykyUrl.party + "/v1/dept/findCustomerByDeptId?page=1&size=100&deptId="+id, 'GET', null, function(res, err) {
		if (!err) {
			vm.offerList = res.list;
			if(vm.offerList && vm.offerList.length > 0){
				if(vm.salesInfo.enquiryList){
					vm.salesInfoEnquiryNameCheckboxModel = vm.salesInfo.enquiryList;
				}
				if(vm.salesInfo.offerList){
					vm.salesInfoOfferNameCheckboxModel = vm.salesInfo.offerList;
				}
				if(vm.baseData.enquiryList){
					vm.enquiryNameCheckboxModel = vm.baseData.enquiryList;
					
				}
				if(vm.baseData.offerList){
					vm.offerNameCheckboxModel = vm.baseData.offerList;
				}
			}
		}
	});
}
//重置银行信息数据
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
//重置联系信息数据
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
//添加专属特价规则相关信息
function postOrPutSpecialOfferRule(params,type){
	var that = vm;
	if(params.id){
		syncData(ykyUrl.product + "/v1/specialOfferRule?ruleId="+params.id, type?type:'POST', params, function(res,err) {
			if (!err) {
				that.speListQueryParams.defaultStatus = !that.speListQueryParams.defaultStatus;
			}
		},false);
	}
}
//查询某个供应商专属特价设置
function getVendorSpecialOffer(partyId){
	if(partyId){
		syncData(ykyUrl.product + "/v1/specialOffer?id="+partyId, 'GET',null, function(res,err) {
			if (!err&&res) {
				vm.specialRuleStatus = res.ruleStatus;
				vm.specialRulesText = res.ruleText;
			}
		});
	}
}
function specialOfferUpdateRuleStatus(partyId,status){
	var obj = {
		"ALL":"所有商品",
		"INOPERATIVE":"不生效",
		"APPOINT":"指定商品"
	}
	if(partyId&&status){
		syncData(ykyUrl.product + "/v1/specialOffer/updateRuleStatus?id="+partyId+"&ruleStatus="+status+"&statusName="+obj[status],'POST',null, function(res,err) {
			if (err||res=="fail") {
				//to do
				layer.msg("供应商专属特价显示切换失败,请重试");
			}
		},false);
	}
}
function specialOfferUpdateRuleText(partyId,text){
	if(partyId&&text){
		syncData(ykyUrl.product + "/v1/specialOffer/updateRuleText?id="+partyId+"&ruleText="+text,'POST',null, function(res,err) {
			if (err||res=="fail") {
				//to do
				layer.msg("通讯失败，请重新填写供应商专属特价显示文案");
			}
		},false);
	}
}
//编辑特价
function editSpecialRule(id,params){
	var $this = vm;
	var ele = params[0];
	if($this.specialRuleStatus != 'APPOINT'){
		return
	}
	$this.specialRuleType = ele.ruleType;
	$this.showSpecialEdit = true;
	$this.speaiclRuleProductLine.addOrEdit = "PUT";
	$this.currentSpecialRuleId = ele.id;
	var detailData = {};
	syncData(ykyUrl.product + "/v1/specialOfferRule/detail?id="+ele.id,'GET',null, function(res,err) {
		if (!err&&res) {
			detailData = res;
		}
	},false);
	$this.specialRemark = detailData.desc;
	if(detailData.ruleType=="RULE"){  //编辑规则类型产品线
		$this.speaiclRuleProductLine.selected = (typeof detailData.mfrIds =='string')?JSON.parse(detailData.mfrIds):detailData.mfrIds;
		if(detailData.mfrNameArray){
			var list = JSON.parse(detailData.mfrNameArray);
			var arr = [];
			$.each(list,function(index,item){
				arr.push({
					"id":item.id,
					"brandName":item.name
				});
			})
			$this.speaiclRuleProductLine.chosenManufacturer = arr;
		}
		if(detailData.sourceIds!=""){
			var sourceList  = detailData.sourceIds;
			var wareList = $this.speaiclRuleProductLine.wareList;
			$.each(wareList,function(index,item){
				for(var i=0;i<sourceList.length;i++){
					if(item.id==sourceList[i]){
						item.isChecked = true;
					}
				}
			})
			if(sourceList.length==1&&sourceList[0]=="*"){
				$this.speaiclRuleProductLine.isAllChecked = true;
				$('.input_ware_check').attr("disabled",true);
			}
		}
		if(detailData.catNameArray){
			cateList = detailData.catNameArray;
			var cateListTemp = [];
			var cateListFormated = [];
			$.each(cateList,function(index,item){
				var cateListTemp = JSON.parse(item);
				var cateItem = {
					"name":"",
					"id":""
				}
				if(cateListTemp.length==3){
					if(cateListTemp[0].id=="*"&&cateListTemp[1].id&&cateListTemp[2].id){
						cateItem.name = "不限"
						cateItem.id = "*/*/*";
					}
					if(cateListTemp[0].id!="*"&&cateListTemp[1].id=="*"&&cateListTemp[2].id=="*"){
						cateItem.name = cateListTemp[0].name;
						cateItem.id = cateListTemp[0].id+"/*/*";
					}
					if(cateListTemp[0].id!="*"&&cateListTemp[1].id!="*"&&cateListTemp[2].id=="*"){
						cateItem.name = cateListTemp[1].name;
						cateItem.id = cateListTemp[0].id+"/"+cateListTemp[1].id+"/*";
					}
					if(cateListTemp[0].id!="*"&&cateListTemp[1].id!="*"&&cateListTemp[2].id!="*"){
						cateItem.name = cateListTemp[2].name;
						cateItem.id = cateListTemp[0].id+"/"+cateListTemp[1].id+"/"+cateListTemp[2].id+"";
					}
				}
				if(cateItem.name!=""&&cateItem.id!=""){
					cateListFormated.push(cateItem);
				}
			})
			 $this.speaiclRuleProductLine.cateInitData = cateListFormated;
		}
		$this.showTabcate = true;
	}else if(ele.ruleType=="MPN"){ //规则类型型号
		syncData(ykyUrl.product + '/v1/specialOfferRule/copyProductToDraft?ruleId=' + $this.currentSpecialRuleId, 'POST', null, function(res, err){
			if(err == null){
				$this.specialMpnType = ele.mpnType;
				$this.uploadPrdQueryParams.ruleId = $this.currentSpecialRuleId;
				$this.uploadPrdQueryParams.defaultStatus = !$this.uploadPrdQueryParams.defaultStatus;
			}
		})
	}
}
function specialRuleDetail(id,params){
	var $this = vm;
	var ele = params[0];
	if($this.specialRuleStatus != 'APPOINT'){
		return
	}
	$this.specialRuleType = ele.ruleType;
	$this.specialRemark = ele.desc;
	$this.currentSpecialRuleId = ele.id;
	$this.showSpecialDetail = true;
	if(ele.ruleType == 'RULE'){
		$this.speaiclRuleProductLine.mfrName = ele.mfrName;
		$this.speaiclRuleProductLine.sourceName = ele.sourceName?ele.sourceName:(ele.sourceIds&&ele.sourceIds[0]=="*"?"不限":"");
		$this.speaiclRuleProductLine.catName = ele.catName;
		
	}else if(ele.ruleType == 'MPN'){
		$this.uploadPrdColumns[0].hide = true;
		$this.uploadPrdColumns[$this.uploadPrdColumns.length-1].hide = true;
		$this.uploadPrdQueryParams.ruleId = ele.id;
	}
}
//
//校验银行信息
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
//校验联系人信息
var contactVerify = function(){
	return $("#contactLayer").validate({
		rules: {
			lastNameLocal: {
				required: true,
			},
			occupation: {
				required: true,
			},
			mail: {
				required: true,
				isMail: [''],
			},
			address: {
				required: true,
			},
			
		},
		messages: {
			lastNameLocal: {
				required: '联系人不能为空',
			},
			occupation: {
				required: '联系人职位不能为空',
			},
			mail: {
				required: '邮箱不能为空',
			},
			address: {
				required: '联系人地址不能为空',
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
	/* 插件验证end */
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
		category: {
			required: true,
		},
		website: {
			noCn: [''],
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
		category:{
			required: '所属分类不能为空',
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
			min: 0,
			floatTwo: ['']
		},
		creditDeadline: {
			required:true,
		},
		checkDate: {
			noZero: [''],
		},
		payDate: {
			noZero: [''],
		},
	},
	messages: {
		creditDeadline: {
			required: "授信期限不能为空！"
		},
		creditQuota: {
			number: '请输入不小于0数字',
			min: '请输入不小于0数字',
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
 * 销售信息表单校验
 */
$("#createVendorSales").validate({
	rules: {
		minOrderPriceCNY: {
			number: true,
			floatTwo: [''],
			floatZero: [''],
		},
		minOrderPriceUSD: {
			number: true,
			floatTwo: [''],
			floatZero: [''],
		},
		productInvalidDays: {
			number: true,
			digits: true
		},
		description: {
			maxlength: 100,
		}
	},
	messages: {
		minOrderPriceCNY: {
			number: '请输入不小于0数字',
			min: '请输入不小于0数字',
		},
		minOrderPriceUSD: {
			number: '请输入不小于0数字',
			min: '请输入不小于0数字',
		},
		productInvalidDays:{
			number: '请输入不小于0数字',
			digits: '必须输入整数'
		},
		description: {
			maxlength: '装运条款最多只能输入100个字符'
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


//供应商别名表单校验
jQuery.validator.addMethod("isAliasExits", function(value, element, param) { 
    var returnValue = false;
    var par = false;
    var hasSameMail = -3;  //未初始化
    var $dom = $(element);
    var _index = $dom.data("indexnum");
    var list = vm.aliasInfo.aliasList; //当前所有别名
    var aliasData = list[_index];
    
	for(var i = 0; i < list.length; i++){
		if(i!=_index){
			if(aliasData.aliasName==list[i].aliasName||$.trim(aliasData.aliasName)==list[i].aliasName){
				param[0] = "该供应商别名已存在";
				return false;
			}
		}
	}

    $.aAjax({
        url: ykyUrl.party + "/v1/vendorManage/isExistAliasName?aliasName=" +value.trim(),
        type: "GET",
        async: false,
        success: function(data) {
        	if(aliasData.supplierAliasId==""){  //新建别名
        		if(data){
            		returnValue = true;//存在
            	}
        	}else if(aliasData.supplierAliasId!=""){ //编辑
        		if(data&&data.supplierAliasId!= aliasData.supplierAliasId){
            		returnValue = true;//存在
            	}
        	}
        },
        error: function() {
            console.error("判断用户名是否存在接口错误");
        }
    });
	
	if(returnValue){
		param[0] = "该供应商别名已存在";
	}else{
		par = true;
	}
    return par;
    
}, "{0}");

function validateVendorAlias(){
	$("#createVendorAlias").validate({
		rules: {
			"vendorAlias0": {
				required: true,
				isAliasExits:[''],
			}
		},
		messages: {
			"vendorAlias0": {
				required: '供应商别名不能为空',
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
			aliasvalidate = true;
		}
	});
	if(vm&&vm.aliasInfo.aliasList.length>1){
		var list = vm.aliasInfo.aliasList;
		 $('[name*="vendorAlias"]').each(function (index,item) {
		        if(index!==0){
		        	$(this).rules('add', {
			            required: true,
			            isAliasExits:[''],
			            messages: {
			                required: "供应商别名不能为空"
			            }
			        });
		        }
		    });
	}
}

//供应商logo上传
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

//上传产品线文件
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


//上传协议
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
				if(!vm.creditInfo.creditAttachmentList){
					vm.creditInfo.creditAttachmentList = [];
				}
				var files = {
					attachmentName: file.name,
					attachmentUrl: _fileUrl,
				}
				vm.creditInfo.creditAttachmentList.push(files);
				vm.productFlagNum++;
				
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploaderDeal.init(); 

//专属特价应用型号EXCEL上传
var uploaderSpecialExcel = createUploader({
	buttonId: "uploadSpecialExcel", 
	uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "xls,xlsx,csv,pdf,zip,rar",//可允许上传文件的类型
	fileSize: "5mb", //最多允许上传文件的大小
	isImage: false, //是否是图片
	multi_selection: true,
	init:{
		FileUploaded : function(up, file, info) {
			if (info.status == 200 || info.status == 203) {
				vm.specialUploadFileName = file.name;
				vm.specialUploadFileUrl = _fileUrl;
				var data = {
						fileUrl:_fileUrl,
						oriFileName:file.name,
						ruleId:vm.currentSpecialRuleId,
						vendorId:partyId,
						vendorName:vm.baseData.groupNameFull
				}
				var url = ykyUrl.product + '/v1/specialOfferProductDraft/parse'
				syncData(url, 'POST', data, function(res, err){
					layer.close(index);
					vm.uploadPrdQueryParams.defaultStatus = !vm.uploadPrdQueryParams.defaultStatus;
				})
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
 
$(function(){
	vm.status = getQueryString('status');
	
	//初始化支持币种选择
	for(var i = 0; i < vm.supportCurrency.length; i++){
		if(vm.supportCurrency[i] === 'RMB'){
			$('input[value="RMB"]').prop('checked',true);
		}
		if(vm.supportCurrency[i] === 'USD'){
			$('input[value="USD"]').prop('checked',true);
		}
	}
	
	
	$('input[name="skuMovStatus"]').click(function(){
		if($(this).prop('checked')){
			vm.salesInfo.skuMovStatus = 'Y';
		}else{
			vm.salesInfo.skuMovStatus = 'N';
		}
	})
	
	
	$('input[name="vendorMovStatus"]').click(function(){
		if($(this).prop('checked')){
			vm.salesInfo.vendorMovStatus = 'Y';
		}else{
			vm.salesInfo.vendorMovStatus = 'N';
		}
	})
		
	if(vm.contactPersonInfo.personalTitle === 'ORDER'){
		$('input[value="ORDER"]').prop('checked',true);
	}
	if(vm.contactPersonInfo.personalTitle === 'ENQUIRY'){
		$('input[value="ENQUIRY"]').prop('checked',true);
	}
	
	currencyChecked($('input[name="currencyRmb"]'), $('input[name="currencyUsd"]'), 'RMB');
	currencyChecked($('input[name="currencyUsd"]'), $('input[name="currencyRmb"]'), 'USD');
	personalTitleChecked($('input[name="enquiry"]'),$('input[name="order"]'));
	personalTitleChecked($('input[name="order"]'),$('input[name="enquiry"]'));
	
	$('#secrecyTime .input-daterange,#dealTime .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true
	}).on('changeDate', function(ev){
		vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_PURCHASEDEALDATE = $('input[name="purchaseDealDate"]').val();
		vm.creditInfo.creditAttributeMap.VENDOR_CREDIT_SECRECYPROTOCOLDATE= $('input[name="secrecyProtocolDate"]').val();
	});
	
	//初始化时间插件
	$('#createData .input-daterange').datepicker({
        format: config.dateFormat,
        autoclose: true,
        todayHighlight: true,
    }).on('changeDate', function(ev){
		vm.speListCreatedDateStart = $('input[name="speListStartDate"]').val();
		vm.speListCreateDateEnd = $('input[name="speListEndDate"]').val();
	});
	
});

function currencyChecked(dom, dom1, currency){
	dom.click(function(){
		if($(this).prop('checked')){
			if(dom1.prop('checked')){
				$(this).prop('checked', true);
				vm.supportCurrency.push(currency);
				if(currency === 'RMB'){
					vm.showRmb = true;
				}
				if(currency === 'USD'){
					vm.showUsd = true;
				}
			}else{
				return false;
			}
		}else{
			if(dom1.prop('checked')){
				$(this).prop('checked', false);
				vm.supportCurrency = _.pullAllWith(vm.supportCurrency, [currency], _.isEqual);
				if(currency === 'RMB'){
					vm.showRmb = false;
				}
				if(currency === 'USD'){
					vm.showUsd = false;
				}
			}else{
				return false;
			}
		}
	})
}

function personalTitleChecked(dom, dom1, title){
	dom.click(function(){
		if($(this).prop('checked')){
			if(dom1.prop('checked')){
				$(this).prop('checked', true);
				vm.contactPersonInfo.personalTitle = 'NOT_LIMIT';
			}else{
				vm.contactPersonInfo.personalTitle = dom1.val();
				return false;
			}
		}else{
			if(dom1.prop('checked')){
				$(this).prop('checked', false);
				vm.contactPersonInfo.personalTitle = dom1.val();
			}else{
				vm.contactPersonInfo.personalTitle = $(this).val();
				return false;
			}
		}
	})
}

//专属特价列表数据 
function delFunc(index, params) {//删除方法
	if(vm.specialRuleStatus != 'APPOINT'){
		return
	}
	var layerIdx = layer.confirm("<p>确认要删除规则？</p>",{
		offset: "auto",
		btn: ['确      认','取      消'], //按钮
		title: " ",
		area: 'auto',
		maxWidth:'500px',
		move: false,
		skin: "up_skin_class",
		yes:function(){
			syncData(ykyUrl.product + "/v1/specialOfferRule?id=" + params[0] + '&vendorId='+partyId, 'DELETE', null,
					function(res, err) {//页面加载前调用方法
						layer.close(layerIdx);
						console.log(res);
						window.setTimeout(function() {
							vm.speListRefresh = !vm.speListRefresh;//重载
						}.bind(vm), 400);
						layer.close(index);
					});
		}
	})
}