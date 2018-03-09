/**
 * create by roy.he@yikuyi.com at 2017-8-18
 */

var partyId =  getQueryString('partyId');
var vm = new Vue({
	el: '#detailVendor',
	data: {
		id: getQueryString('partyId'),
		tabValue: 'baseData', //baseData,product,credit,sales
		subTabValue : 'nextbaseData' ,//nextbaseData,nextmovData
		movList:[],
		noSales: false,
		noContactInfo: false,
		baseData: {
			vendorInfoAttributeMap:{}
		},
		productLines: [],
		prodctType : 'PROXY',
		creditInfo: {
			creditAttributeMap:{}
		},
		salesInfo: {
			saleAttributeMap:{},
			contactPersonInfoList:[],
		},
		aliasInfo:{
			aliasList:[]
		},
		vendorLogo: '',
		auditList: [],
		downLoadUrl:[],
		specialRuleStatusList:{ //专属特价显示类型
        	'ALL':'所有商品',
        	'APPOINT':'指定商品',
        	'INOPERATIVE':'不生效'
        },
        specialRuleStatus:'',
        specialRulesText:'',
        specialRuleType:'RULE', //专属特价规则类型
        speListRuleType:'',
        speaiclRuleProductLine:{   //专属特价产品线
             mfrName:"",
             catName:"",
             sourceName:"",
        },
        showSpecialDetail:false,
        uploadPrdStandardApi: ykyUrl.product + '/v1/specialOfferProduct/getStandard',  //查询专属特价商品草稿数据
        uploadPrdApi: ykyUrl.product + '/v1/specialOfferProductDraft/getDraft',  //查询专属特价商品
        uploadPrdPageflag:true,
        uploadPrdCheckflag:true,
        uploadPrdQueryParams:{
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
        speListCreatedDateStart:'', //创建时间起始
        speListCreateDateEnd:'', //创建时间结束
		speListUrl : ykyUrl.product + "/v1/specialOfferRule?vendorId="+ partyId, //访问数据接口 
		speListQueryParams : {           //请求接口参数 
        	pageSize: 20,        //分页参数
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
				        var sourceName = params.row.sourceName?params.row.sourceName:(params.row.sourceIds && params.row.sourceIds[0]=="*"?"不限":"");
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
           {key: 'lastUpdateUserName', name: '操作人',align : 'center',default : '-',width: '80px'},
		{
			key : 'operate',
			name : '操作',
			align : 'center',
			default : '-',
			width: '150px',
			items : [   {
				className : 'btn-edit',
				text : '编辑',
				show : false,
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
				show : false,
				callback : {
					confirm : {
						title : '删除',
						content : '确认删除？'
					},
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
		var _this =this;

		getBaseData(_this.id);
		
		getProductLine(_this.id,_this.prodctType);
		
		getCreditInfo(_this.id);
		
		getSalesInfo(_this.id)
		
		getAuditList(_this.id)
		
		getMovList(); //mov列表数据
		getVendorAlias(_this.id);
		getVendorSpecialOffer(_this.id);
	},
	methods: {
		specialInitRule: function(){ //专属特价数据初始化
        	this.specialRuleType = 'RULE';
        	this.specialMpnType = 'UPLOAD';
        	this.specialRemark = '';
        	this.specialUploadFileName = '';
        	this.specialUploadFileUrl = '';		
        	this.uploadPrdColumns[0].hide = false;
        	this.uploadPrdColumns[this.uploadPrdColumns.length-1].hide = false;
        },
		specialCancelEvent: function(){ //专属特价取消事件
        	this.speListQueryParams.defaultStatus = !this.speListQueryParams.defaultStatus;
        	this.isAddSpecialRule = false;
        	this.showSpecialDetail = false;
        	this.specialInitRule();
        },
		oninfoChange:function(){
			vm.subTabValue = 'nextbaseData' ; //nextbaseData,nextmovData
			$('.nav_tab a').eq(1).removeClass('tab');
			$('.nav_tab a').eq(2).removeClass('tab');
			$('.nav_tab a').eq(0).addClass('tab');
		},
		onmovChange:function(){
			vm.subTabValue = 'nextmovData';
			$('.nav_tab a').eq(0).removeClass('tab');
			$('.nav_tab a').eq(2).removeClass('tab');
			$('.nav_tab a').eq(1).addClass('tab');
			
		},
		onSpecialOfferChange:function(){
			vm.subTabValue = 'nextSpecialData';
			$('.nav_tab a').eq(0).removeClass('tab');
			$('.nav_tab a').eq(1).removeClass('tab');
			$('.nav_tab a').eq(2).addClass('tab');
		},
		onSearchSpeListClick: function () {
			vm.speListQueryParams.ruleType = vm.speListRuleType;
			vm.speListQueryParams.createdDateStart = vm.speListCreatedDateStart;
			vm.speListQueryParams.createDateEnd = vm.speListCreateDateEnd;
			vm.speListQueryParams.defaultStatus = !vm.speListQueryParams.defaultStatus;
		},
		//tab切换代理产品线和非代理产品线
        productLineChange: function(type){
        	var _this = this;
        	_this.prodctType = type;
        	getProductLine(_this.id,type);
        },
        //导出产品线
        exportProductList:function(){
        	var _this = this;
        	var  flag = true;
			var id = partyId;
			var name = vm.baseData.groupNameFull;
			//if()
            if(flag){
		    	$.aAjax({//全部导出 
					//url : ykyUrl.party + "http://192.168.3.213:27082/v1/vendorManage/export?partyId=RS&partyName=RS",
					url : ykyUrl.party + "/v1/vendorManage/export?partyId="+ id +"&partyName="+ name,
					type : 'GET',
					success : function(response,msg,res) {
						url = ykyUrl._this+'/download.htm';
						if(res.status === 200){
							layer.open({
								  type: 1,
								  area: ["500px", "250px"],
								  anim: 2,
								  skin: "up_skin_class down-load",
								  btn: ['确认'],
								  content: '<p style="margin:20px;">您的下载任务已经在进行中,请稍后在运营后台的<a href='+url+'>下载中心->我的下载</a>进行查看或者下载<p>',
								  yes: function(index, layero){
										  layer.closeAll();
										  }
							
							});
						}
					},
					error : function(data) {
						err = data.responseJSON.errCode;
						layer.msg(err,{icon:2,offset:120});
					}
				})
            }else{
            	layer.msg('请筛选要导出的数据（不超过20w条）', {time: 3000});
            }
        },
	}
})
//获取mov列表数据
function getMovList(){
	var vendorId = getQueryString('partyId');
	syncData(ykyUrl.product + "/v2/rules/mov?page=1&size=20&vendorId="+vendorId, 'GET', null, function(res, err) {
		if (!err) {
			
			vm.movList = res;
		}
	});
}

function getBaseData(id){
	syncData(ykyUrl.party + "/v1/vendors/vendorInfo/"+id, 'GET', null, function(res, err) {
		if (!err) {
			vm.baseData = res;
			if(vm.baseData.logoImageUrl){
				getPrivateUrl(vm.baseData.logoImageUrl);
			}
		}
	});
}

function getProductLine(id,type){
	syncData(ykyUrl.party + "/v1/vendors/productLine/"+id+"?type="+ type, 'GET', null, function(res, err) {
		if (!err) {
			vm.productLines = res;
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
				if(res.creditAttachmentList){
					var creditAttachList = res.creditAttachmentList;
					var attachUrls = [];
					/*vm.creditAttachListLength = creditAttachList.length;*/
					if(creditAttachList.length>0){
						$.each(creditAttachList,function(index,item){
							if(item.attachmentUrl){
								attachUrls.push(item.attachmentUrl);
							}
						})
						getPrivateUrls(attachUrls);
					}
				}
			}
		}
	});
}

function getSalesInfo(id){
	syncData(ykyUrl.party + "/v1/vendors/vendorSaleInfo/"+id, 'GET', null, function(res, err) {
		if (!err) {
			if(res.offerId && res.offerId === $('#userId').val()){
				vm.noContactInfo = true;
			}
			if(!res.orderVerify && getQueryString('status') !== 'salesDetail'){
				vm.noSales = true;
			}
			vm.salesInfo = res;
			
			if(res){	
				if(!vm.salesInfo.contactPersonInfoList){
					vm.salesInfo.contactPersonInfoList = [];
				}
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
			}
		}
	});
}

//获取供应商别名
function getVendorAlias(partyId){
	syncData(ykyUrl.party + "/v1/vendorManage/supperAliasNameList?partyId="+partyId, 'GET', null, function(res, err) {
		if (!err) {
			if(res&&res.length){
				vm.aliasInfo.aliasList = res;
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
//查看某条特价详情
function specialRuleDetail(id,params){
	var $this = vm;
	var ele = params[0];
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
function getPrivateUrls(urlArr){
	var urls = urlArr;
	var emptylist = [];
	function getPrivateUrlSubProcedure(urls){
		$.aAjax({
			url: ykyUrl.party + "/v1/enterprises/getImgUrl",
	     	type:"POST",
	     	data:JSON.stringify({"id":urls[0]}),
	     	success: function(data) {
	     		emptylist.push(data);
     			urls = urls.splice(1);
	     		if(urls.length!=0){
	     			while(urls[0]==""&&urls.length>0){
	     				emptylist.push("");
	         			urls.splice(1);
	     			}
	     			if(urls.length>0){
	     				getPrivateUrlSubProcedure(urls);
	     			}
	     			
	     		}else if(urls.length==0){
	     			vm.downLoadUrl = emptylist;
	     			/*return emptylist;*/
	     		}
	     	},
	     	error:function(e){
	     		emptylist.push(urls[0]);
	     		urls = urls.splice(1);
	     		if(urls.length!=0){
	     			while(urls[0]==""&&urls.length>0){
	     				emptylist.push("");
	         			urls.splice(1);
	     			}
	     			if(urls.length>0){
	     				getPrivateUrlSubProcedure(urls);
	     			}
	     			
	     		}else if(urls.length==0){
	     			vm.downLoadUrl = emptylist;
	     			/*return emptylist;*/
	     		}
		    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
		    }
	 	});
	}
	getPrivateUrlSubProcedure(urls);
}

//初始化时间插件
$('#createData .input-daterange').datepicker({
    format: config.dateFormat,
    autoclose: true,
    todayHighlight: true,
}).on('changeDate', function(ev){
	vm.speListCreatedDateStart = $('input[name="speListStartDate"]').val();
	vm.speListCreateDateEnd = $('input[name="speListEndDate"]').val();
});
