
$(function() {
	var vendorId = getQueryString('id');
	vm = new Vue({
		el : "#distributor-edit",
		data : {
			vendorId:getQueryString('id'),
			newsHome:ykyUrl._this + '/distributor.htm',
	    	initData:{
	    		abstracts:"", //分销商简介
			    attachUrl:"", //分销商图片url 编辑时存在newsAttachment.attachUrl
			    categoryTypeId:"VENDOR", //固定
			    content:"", //代理线、类别、热销型号、最新型号、活动图标、自定义图标内容、图标链接 json字符串
			    newsId:"", //分销商id
			    orderSeq:"", //排序位置
			    title:"" //分销商名称
	    	},
	    	content:{
	    		agents:[], //代理线
	    		cate:[], //类别
	    		hotSaleModel:[], //热销型号
	    		latestModel:[], //最新信号
		    	labelContent:'nothing', //活动图标
		    	customContent:'', //自定义图标内容
		        activityUrl:'', //图标标签链接
		        profile:'' //供应商简介
	    	},
	    	labelType:{
	    		nothing:'无',
	    		promotion:'促销',
	    		fullCut:'满减',
	    		fullDelivery:'满送',
	    		seckill:'秒杀',
	    		flashPurchase:'闪购',
	    		custom:'自定义',
	    	},
	    	showModal:false, //弹窗显示
	    	showFoot:true, //弹窗footer显示
	    	modalTitle:'请选择分销商', //弹窗标题
	    	modalStyle:{ //弹窗样式
	    		width:950,
				maxHeight:600,
				overflowY:'auto'
	    	},
	    	maxNum:16, //代理线、类别最大值
	    	showVendor:false, //显示供应商
	    	showTopHot:false, //选择热销型号
	    	showTopLatest:false, //选择最新型号
	    	showPrdAssociate:false, 
	    	showAgent:false,//选择代理线
	    	showCate:false,//选择分类
	    	agentDefaultTip:'请选择分销商',
	    	cateDefaultTip:'请选择分销商',
	    	agentTitle:'请选择代理线',
	    	cateTitle:'请选择类别',
	    	activeAgent:[], //选中的代理线
	    	activeCate:[], //选中的类别
	    	activeHotPrd:[], //选中的热销型号
	    	activeLastestPrd:[], //选中的最新型号
	    	vendorSelect:{
	    		keyname:'选择分销商',
				validate:{
                    "required": true
                },
                id:'selbox',
                name:'selbox',
                options:[],
                optionId: 'id',
                optionName: 'supplierName',
                selected:[],
                placeholder:'搜索分销商',
            	multiple:false //单选
	    	},
			tempActiveCate:[], //临时选中的分类
			agentMaxLen:20,
			cateMaxLen:10,
	    	agentSelect:{   //制造商弹窗组件数据
				keyname:'选择制造商',
				validate:{
                    "required": true
                },
                id:'selbox',
                name:'selbox',
                options:[],
                optionId: 'id',
                optionName: 'brandName',
                selected:[],
                placeholder:'搜索制造商',
                multiple:true,
                isFuzzySearch:true,
            	reloadApi:ykyUrl.product + '/v1/products/brands/alias?keyword={selbox}'
			},
			cateSelect:{   //分类弹窗组件数据
				api:ykyUrl.product + "/v1/products/categories/list",
				initData:[],
				id:'_id',
				name:'cateName',
				children: 'children'
			},
	    	topHot:{
	    		columns:[{key :'selected', name: '',width: '50px'},
	    		         {key :'index', name: '序号', align:'center', width: '50px'},
	    		         {key :'model', name: '型号', align:'center',cutstring: true,},
	    		         {key :'manufacturer', name: '原厂', align:'center',cutstring: true,}],
		        datas:[],
		        selectedItem:[],
	    		defaultTip:'暂无数据',
	    		checkflag:true
	    	},
	    	topLatest:{ //选择最新型号数据
	    		columns:[{key :'selected', name: '',width: '50px'},
	    		         {key :'index', name: '序号', align:'center', width: '50px'},
	    		         {key :'model', name: '型号', align:'center',cutstring: true,},
	    		         {key :'manufacturer', name: '原厂', align:'center',cutstring: true,}],
		        datas:[],
		        selectedItem:[],
	    		defaultTip:'暂无数据',
	    		checkflag:true
	    	},
	    	editPrdData:{
	    		id:'',
	    		prdUrl:'', //型号链接
				model:'', //型号
				manufacturer:'',  //原厂
				imageUrl:'' // 图片地址
	    	},
	    	productBasicApi: ykyUrl.product + '/v1/products/batch/basic',
	    	//productBasicApi:ykyUrl._this + '/data/basic.json',
	    	productRequestType: 'POST',
	    	isAdd:true,
	    	isEdit:true,
	    	isReadOnly:false,
	    	imageRecText:'请上传200*200尺寸图片，支持jpg、jpeg、png、gif格式大小不超过2M',
	    	prdType:'',
	    	hotList:{
	    		refresh:true,
	    		defaultTip:'请选择分销商',
	    		datas:[],
	    		columns:[
    		         	{key :'index', name: '序号', align:'center', width: '50px'},
    		         	{key :'model', name: '型号', align:'center',cutstring: true,},
    		         	{key :'manufacturer', name: '原厂', align:'center',cutstring: true,},
    		         	{key :'operate', name: '操作', align:'center',
    		         		items:[
    		         		       {className:'btn-detail',
    		         		    	text: '上移',
    		         		    	show: "'{seq}' != 0",
	       		         		    callback: {
	       		         		    	action: 'changSeq', 
	       		                        params: ['{rowData}',1,'hotList']
	       		         		    	}
		         		    	   	},
		         		    	   	{className:'btn-detail down-change',
    		         		    	 text: '下移',
    		         		    	 show: true,
	       		         		     callback: {
	       		         		    	action: 'changSeq', 
	       		                        params: ['{rowData}',2,'hotList']
	       		         		    	}
		         		    	   	},
		         		    	   {className:'btn-detail',
    		         		    	text: '详情',
    		         		    	show: true,
	       		         		    callback: {
	       		         		    	action: 'detailPrd', 
	       		                        params: ['{rowData}']
	       		         		    	}
		         		    	   	},
		         		    	   	{className:'btn-detail',
    		         		    	 text: '编辑',
    		         		    	 show: true,
	       		         		     callback: {
	       		         		    	action: 'editPrd', 
	       		                        params: ['{rowData}','hotList']
	       		         		    	}
		         		    	   	},
		         		    	   {className:'btn-detail',
    		         		    	text: '删除',
    		         		    	show: true,
	       		         		    callback: {
	       		         		    	action: 'delPrd', 
	       		                        params: ['{rowData}','hotList']
	       		         		    	}
		         		    	   	}
    		         		       ]
    		         	}
	    		         ]
	    		
	    	},
	    	newestList:{
	    		refresh:true,
	    		defaultTip:'暂无数据',
	    		datas:[],
		       columns:[
    		         	{key :'index', name: '序号', align:'center', width: '50px'},
    		         	{key :'model', name: '型号', align:'center',cutstring: true,},
    		         	{key :'manufacturer', name: '原厂', align:'center',cutstring: true,},
    		         	{key :'operate', name: '操作', align:'center',
    		         		items:[
    		         		       {className:'btn-detail',
    		         		    	text: '上移',
    		         		    	show: "'{seq}' != 0",
	       		         		    callback: {
	       		         		    	action: 'changSeq', 
	       		                        params: ['{rowData}',1,'newestList']
	       		         		    	}
		         		    	   	},
		         		    	   	{className:'btn-detail down-change',
    		         		    	 text: '下移',
    		         		    	 show: true,
	       		         		     callback: {
	       		         		    	action: 'changSeq', 
	       		                        params: ['{rowData}',2,'newestList']
	       		         		    	}
		         		    	   	},
		         		    	   {className:'btn-detail',
    		         		    	text: '详情',
    		         		    	show: true,
	       		         		    callback: {
	       		         		    	action: 'detailPrd', 
	       		                        params: ['{rowData}']
	       		         		    	}
		         		    	   	},
		         		    	   	{className:'btn-detail',
    		         		    	 text: '编辑',
    		         		    	 show: true,
	       		         		     callback: {
	       		         		    	action: 'editPrd', 
	       		                        params: ['{rowData}','newestList']
	       		         		    	}
		         		    	   	},
		         		    	   {className:'btn-detail',
    		         		    	text: '删除',
    		         		    	show: true,
	       		         		    callback: {
	       		         		    	action: 'delPrd', 
	       		                        params: ['{rowData}','newestList']
	       		         		    	}
		         		    	   	}
    		         		       ]
    		         	}
	    		         ]
	    	}
		},
		mounted:function(){
			var that = this;
			var uploader = createUploader({
				buttonId: "uploadBtn", 
				uploadType: "notice.publicRead", 
				url:  ykyUrl.webres,
				types: "jpg,png,jpeg,gif",
				fileSize: "5mb",
				isImage: true, 
				init:{
					FileUploaded : function(up, file, info) { 
			            layer.close(index);
						if (info.status == 200 || info.status == 203) {
							console.log(_yetAnotherFileUrl);
							that.initData.attachUrl = _yetAnotherFileUrl;
						} else {
							layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
						}
						up.files=[];
					}
				}
		    }); 
			uploader.init();
		},
		created(){
			var that = this;
			if(this.vendorId == ''){
				syncData(ykyUrl.info + '/v1/news?categoryTypeIdStr=VENDOR','GET',null, function(data, err) {
					if (err == null) {
						that.initData.orderSeq = data.total + 1;
					}
				});
			}else{
				this.getRelevantData(this.vendorId,false,'hot');////获取热销型号数据
				this.getRelevantData(this.vendorId,false,'latest');
				syncData(ykyUrl.info + '/v1/news/' + this.vendorId, 'GET', null, function(res,err){
					if(err == null){
						console.log(res);
						that.initData = res ? $.extend({},that.initData,res) : {};
						that.content = res.content ? $.extend({},that.content,JSON.parse(res.content)) : {};
						that.initData.attachUrl = res.newsAttachment && res.newsAttachment.attachUrl ? res.newsAttachment.attachUrl : '';
						that.agentSelect.selected = that.content.agents ? that.content.agents : [];
						that.cateSelect.initData = that.content.cateData ? JSON.parse(JSON.stringify(that.content.cateData)) : [];
						that.activeAgent = that.content && that.content.agentsData ? that.content.agentsData : [];
						that.activeCate = that.content && that.content.cateData ? that.content.cateData : [];
						that.hotList.datas = that.content.hotModel ? that.content.hotModel : [];
						that.newestList.datas = that.content.newestModel ? checkList(that.content.newestModel) : [];
						that.hotList.refresh = !that.hotList.refresh;
						that.newestList.refresh = !that.newestList.refresh;
						that.$nextTick(function(){
							that.hotList.refresh = !that.hotList.refresh;
							that.newestList.refresh = !that.newestList.refresh;
							setTimeout(function() {
								if (res.abstracts) {
									var content = 'loadContent' + res.abstracts;
									window.frames[0].postMessage(content,ykyUrl.ictrade_ueditor);
								}
							}, 1000);
						})
					}
				})
				
			}
			syncData(ykyUrl.party + '/v1/party/allparty','PUT',{status:'PARTY_ENABLED',roleType:'SUPPLIER'},function(res,err){
				if(err == null){
					$.each(res,function(i,e){
						if(e.partyGroup.groupName){
							e.supplierName = e.partyGroup.groupName;
						}
					})
					that.vendorSelect.options = res;
				}
			})			
			syncData(ykyUrl.product + "/v1/products/brands", 'GET', null, function(res,err) {   //制造商数据请求
				if (res) {
					that.agentSelect.options = res;
				}
			});
		},
		watch:{
			'initData.newsId':function(val,oldVal){
				if(oldVal){
					this.activeAgent = [];
					this.activeCate = [];
					this.agentSelect.selected = [];
					this.cateSelect.selected = [];
					this.hotList.datas = [];
					this.newestList.datas = [];
					this.hotList.refresh = !this.hotList.refresh;
					this.newestList.refresh = !this.newestList.refresh;
					this.$nextTick(function(){
						this.hotList.refresh = !this.hotList.refresh;
						this.newestList.refresh = !this.newestList.refresh;
					})
				}
			},
			'hotList.datas':function(val,oldVal){
				$.each(val,function(i,e){
					e.seq = i;
				})
			},
			'newestList.datas':function(val,oldVal){
				$.each(val,function(i,e){
					e.seq = i;
				})
			},
			'showModal':function(val,oldVal){
				if(val==false&&oldVal==true){
					this.showTopLatest = false;
					this.showTopHot = false;
				}
			}
		}, 
		methods : { 
			toggleModal:function(){
				this.showModal = false;
				this.showFoot = true;
				this.showVendor = false;
				this.showTopHot = false;
				this.showPrdAssociate = false;
				this.showAgent = false;
				this.showCate = false;
			},
			modalOk:function(){
				this.showModal = false;
				this.showFoot = true;
				if(this.showVendor){
					this.showVendor = false;
				}
			},
			showVendorModel:function(){
				this.showModal = true;
				this.showFoot = false;
				this.showVendor = true;
				this.modalTitle = '请选择分销商';
			},
			getActiveData:function(activeData,dataList,key){
				var arr = [];
				$.each(activeData,function(i,e){
					$.each(dataList,function(index,ele){
						if(e == ele[key]){
							arr.push(ele)
							return false
						}
					})
				})
				return arr;
			},
			getVendorSelected:function(ele){
				var initData = this.initData;
				initData.newsId = ele.id;
				initData.title = ele.supplierName;
				initData.attachUrl = ele.partyGroup && ele.partyGroup.logoImageUrlSmall ? ele.partyGroup.logoImageUrlSmall : ele.partyGroup.logoImageUrl ? ele.partyGroup.logoImageUrl : '' ;
				this.vendorSelect.selected = ele.id;
				this.showModal = false;
				this.showFoot = true;
				this.showVendor = false;
				this.getRelevantData(ele.id,true,'hot'); //新建的时候,分销商切换后,重新获取热销型号数据
				this.getRelevantData(ele.id,true,'latest');
			},
			getAgentSelected:function(data){
				this.activeAgent = data.agentObj;
				this.content.agents = data.agentId;
				this.showModal = false;
				this.showAgent = false;
				var arr = [];
				if(data && data.agentObj){
					$.each(data.agentObj,function(i,e){
						if(e.id){
							arr.push(e.id);
						}
					})
					this.agentSelect.selected = arr;
				}
			},
			getTempCateSelected:function(data){
				this.tempActiveCate = JSON.parse(JSON.stringify(data));
			},
			getCateSelected:function(){
				var cate1Obj = {};
				var list = JSON.parse(JSON.stringify(this.tempActiveCate));
				$.each(list,function(i,e){
					id = e.id && e.id.split('/')[0] ? e.id.split('/')[0] : null
					name = e.name && e.name.split('/')[0] ? e.name.split('/')[0] : null
					if(id && !cate1Obj[id]){
						cate1Obj[id] = name;
					}
				})
				var num = 0
				for(key in cate1Obj){
					++num
				}
				if(num>this.cateMaxLen){
					layer.msg('最多可选' + this.cateMaxLen + '项大类，当前选中' + num + '项大类');
					return;
				}
				this.activeCate = JSON.parse(JSON.stringify(this.tempActiveCate));
				this.cateSelect.initData = JSON.parse(JSON.stringify(this.tempActiveCate));
				this.showModal = false;
				this.showCate = false;
			},
			deleteAgentSelect:function(index,id){
				this.activeAgent.splice(index,1);
				this.content.agents.splice(index,1);
				var selected = this.agentSelect.selected;
				for(var i=0;i<selected.length;i++){
					if(selected[i] == id){
						selected.splice(i,1);
						break;
					}
				}
			},
			deleteCateSelect:function(index,id){
				this.activeCate.splice(index,1);
				this.cateSelect.initData = JSON.parse(JSON.stringify(this.activeCate));
			},
			agentSelectCancel:function(){
				this.showModal = false;
				this.showAgent = false;
			},
			cateSelectCancel:function(){
				this.showModal = false;
				this.showCate = false;
				this.tempActiveCate = JSON.parse(JSON.stringify(this.activeCate));
			},
			getRelevantData:function(id,isAsync,type){//获取热销型号数据
				var that = this;
				if('hot'==type){
					syncData(ykyUrl.product + '/v1/products/findProductInfos','GET',{arg:id,flag:'v'},function(res,err){
						if(err == null){
							var arr = [];
							$.each(res,function(i,e){
								var obj = {
										id:e.id ? e.id :'',
										prdUrl:e.id ? ykyUrl.portal + '/product/' + e.id + '.htm' : '',
										model:e.spu && e.spu.manufacturerPartNumber ? e.spu.manufacturerPartNumber : '',
										manufacturer:e.spu && e.spu.manufacturer ? e.spu.manufacturer : '',
										imageUrl:e.spu && e.spu.images ? that.getImageUrl(e.spu.images) : ''
								}
								arr.push(obj);
							})
							that.topHot.datas = arr;
							//新增的时候自动拉取前5条热销型号
							if(that.vendorId == ''&&arr.length>0&&that.hotList.datas.length==0){
								that.hotList.datas = arr.length>5?arr.slice(0,5):arr;
								that.hotList.refresh = !that.hotList.refresh;
								that.$nextTick(function(){
									that.hotList.refresh = !that.hotList.refresh;
								})
							}
							if(!res.length){
								that.hotList.defaultTip = '暂无数据';
							}
						}
					},isAsync)
				}else if('latest'==type){
					syncData(ykyUrl.product + '/v1/products/findNewProducts','GET',{arg:id,flag:'v'},function(res,err){
						if(err == null){
							var arr = [];
							$.each(res,function(i,e){
								var obj = {
										id:e.id ? e.id :'',
										prdUrl:e.id ? ykyUrl.portal + '/product/' + e.id + '.htm' : '',
										model:e.spu && e.spu.manufacturerPartNumber ? e.spu.manufacturerPartNumber : '',
										manufacturer:e.spu && e.spu.manufacturer ? e.spu.manufacturer : '',
										imageUrl:e.spu && e.spu.images ? that.getImageUrl(e.spu.images) : ''
								}
								arr.push(obj);
							})
							that.topLatest.datas = arr;
							//新增的时候自动拉取前5条最新型号
							if(that.vendorId == ''&&arr.length>0&&that.newestList.datas.length==0){
								that.newestList.datas = arr.length>5?arr.slice(0,5):arr;
								that.newestList.refresh = !that.newestList.refresh;
								that.$nextTick(function(){
									that.newestList.refresh = !that.newestList.refresh;
								})
							}
							if(!res.length){
								that.topLatest.defaultTip = '暂无数据';
							}
						}
					},isAsync)
				}
			},
			getImageUrl:function(data){
				var obj = {};
				$.each(data,function(i,e){
					obj[e.type] = e.url;
				})
				if(obj.large){
					return obj.large
				}else if(obj.stand){
					return obj.stand
				}else{
					return obj.thumbnail
				}
			},
			getActiveHotPrd:function(){
				var that = this;
				var checkedItem = eval('('+$(".top-hot-wrap #checkedIds").val()+')');
				if(that.hotList.datas.length){
					var manualAddList = [],list = [];
					$.each(this.hotList.datas,function(i,e){
						if(e.manualAdd){
							manualAddList.push(e);
						}
					})
					list = list.concat(manualAddList,checkedItem);
					if(list.length>5){
						layer.msg('热销型号最多为5条！');
						return;
					}else{
						this.hotList.datas = list;
					}
				}else{
					this.hotList.datas = checkedItem;
				}
				this.showModal = false;
				this.showFoot = true;
				this.showTopHot = false;
				this.hotList.refresh = !this.hotList.refresh;
				this.$nextTick(function(){
					this.hotList.refresh = !this.hotList.refresh;
				})
				
			},
			getActiveLatestPrd:function(){
				var that = this;
				var checkedItem = eval('('+$(".top-latest-wrap #checkedIds").val()+')');
				if(that.newestList.datas.length){
					//to do 
					var manualAddList = [],list = [];
					$.each(that.newestList.datas,function(i,e){
						if(e.manualAdd){
							manualAddList.push(e);
						}
					})
					list = list.concat(manualAddList,checkedItem);
					if(list.length>5){
						layer.msg('最新型号最多为5条！');
						return;
					}else{
						that.newestList.datas = list;
					}
				}else{
					if(checkedItem.length>5){
						layer.msg('最新型号最多为5条！');
						return;
					}else{
						that.newestList.datas = checkedItem;
					}
				}
				that.showModal = false;
				that.showFoot = true;
				that.showTopLatest = false;
				that.newestList.refresh = !that.newestList.refresh;
				that.$nextTick(function(){
					that.newestList.refresh = !that.newestList.refresh;
				})
			},
			addProduct:function(type){
				var that = this;
				this.prdType = type;
				this.showModal = true;
				this.showPrdAssociate = true;
				this.isEdit = false;
				this.isAdd = true;
				this.editPrdData = {
						id:'',
						imageUrl:'',
						manufacturer:'',
						model:'',
						prdUrl:'',
						//seq:this[type].datas.length-1
				}
				if(type == 'hotList'){
					this.modalTitle = '添加热销型号';
				}else{
					this.modalTitle = '添加最新型号';
				}
			},
			getProductInfo:function(data){
				var type = this.prdType;
				var id = data.id,flag = true;
				if(this.isAdd){
					$.each(this[type].datas,function(i,e){
						if(e.id == id){
							layer.msg('请勿重复添加！');
							flag = false;
							return false
						}
					})
					if(!flag){
						return;
					}
					this[type].datas.push(data);
				}else if(this.isEdit){
					$.each(this[type].datas,function(i,e){
						if(e.id == data.id){
							for(k in e){
								e[k] =  data[k] || data[k] =='' ? data[k] : e[k];
							}
							return false;
						}
					})
				}
				this.showModal = false;
				this.showPrdAssociate = false;
				this[type].refresh = !this[type].refresh;
				this.$nextTick(function(){
					this[type].refresh = !this[type].refresh;
				})
			},
			prdAssociateCancel:function(){
				this.showModal = false;
				this.showPrdAssociate = false;
			},
			showTopHotList:function(){
				var that = this;
				this.showModal = true;
				this.showFoot = false;
				this.showTopHot = true;
				this.showTopLatest = false;//最新型号不显示
				this.modalTitle = '请选择热销型号';
				var arr = [];
				$.each(that.hotList.datas,function(i,e){
					$.each(that.topHot.datas,function(idx,ele){
						if(e.id == ele.id){
							arr.push(ele);
							return false;
						}
					})
				})
				this.topHot.selectedItem = arr;
			},
			showTopLatestList:function(){
				var that = this;
				this.showModal = true;
				this.showFoot = false;
				this.showTopHot = false; //最热型号不显示
				this.showTopLatest = true;
				this.modalTitle = '请选择最新型号';
				var arr = [];
				$.each(that.newestList.datas,function(i,e){
					$.each(that.topLatest.datas,function(index,item){
						if(e.id == item.id){
							arr.push(item);
						}
					})
				})
				that.topLatest.selectedItem = arr;
			},
			saveData:function(event){
				vm.initData.abstracts = event.data;
				$('#create').submit();
			},
			cancel:function(){
				location.href = ykyUrl._this + '/distributor.htm';
			}
		}
	});

	//提交表单
	formValidate('#create', {}, function(){
		var initData = vm.initData
			type = vm.vendorId == '' ? 'POST' : 'PUT',
			url =  vm.vendorId == '' ? ykyUrl.info + '/v1/news/' : ykyUrl.info + '/v1/news/' + vm.vendorId; 
		if(initData.newsId == ''){
			layer.msg('请选择分销商！');
			return
		}
		if(vm.initData.abstracts ==''){
			layer.msg('请填写分销商简介！')
			return
		}
		var dataList = $('#create').serializeObject();
		dataList.abstracts = vm.initData.abstracts;
		if(vm.activeAgent.length){
			dataList.content.agentsData = vm.activeAgent;
			dataList.content.agents = vm.content.agents;
		}else{
			dataList.content.agents = [];
		}
		if(vm.activeCate.length){
			var arr = [];
			for(var i=0;i<vm.activeCate.length;i++){
				if(vm.activeCate[i].id){
					arr.push(vm.activeCate[i].id)
				}
			}
			dataList.content.cate = arr;
			dataList.content.cateData = vm.activeCate;
		}else{
			dataList.content.cateData = []
		}
		if(vm.hotList.datas.length){
			dataList.content.hotModel = vm.hotList.datas;
		}
		if(vm.newestList.datas.length){
			dataList.content.newestModel = vm.newestList.datas;
		}
		if(vm.content.labelContent != 'nothing'){
			dataList.content.isShowActive = 'Y';
		}else{
			dataList.content.isShowActive = 'N';
		}
		dataList.content = JSON.stringify(dataList.content);
		syncData(url, type, dataList, function(res,err){
			if(err == null){
				
				layer.msg('保存成功');
				setTimeout(function(){
					location.href = location.pathname;
				},2000);
				
			}
		})
	}); 
	
	
	
}); 
setTimeout(function() {
	if (window.addEventListener) {
		window.addEventListener('message', vm.saveData, false);
	} else if (window.attachEvent) {
		window.attachEvent('message', vm.saveData, false);
	}
},1000);
function requireSaveData() {
	//发送指令到iframe请求返回数据
	window.frames[0].postMessage('save_btn', ykyUrl.ictrade_ueditor);
}

function editPrd(index, params){
	vm.showModal = true;
	vm.showPrdAssociate = true;
	vm.editPrdData = params[0];
	vm.prdType = params[1];
	vm.isEdit = true;
	vm.isAdd = false;
	vm.isReadOnly = false;
	if(vm.prdType == 'hotList'){
		vm.modalTitle = '编辑热销型号';
	}else{
		vm.modalTitle = '编辑最新型号';
	}
}
function detailPrd(index, params){
	vm.showModal = true;
	vm.showPrdAssociate = true;
	vm.editPrdData = params[0];
	vm.prdType = params[1];
	vm.isAdd = false;
	vm.isEdit = false;
	vm.isReadOnly = true;
	if(vm.prdType == 'hotList'){
		vm.modalTitle = '热销型号详情';
	}else{
		vm.modalTitle = '最新型号详情';
	}
}
function delPrd(index, params){
	var seq = params[0].seq,
		key = params[1],
		list =  vm[key]['datas'];
	list.splice(seq,1);
}
function changSeq(index,params){
	var seq = params[0].seq,
		type = params[1],
		key = params[2],
		list = vm[key]['datas'];
	if(type == 1){
		list.splice(seq-1,2,list[seq],list[seq-1]);
	}else{
		list.splice(seq,2,list[seq+1],list[seq]);
	}
}
function checkList(list){
	var dataList = [];
	$.each(list,function(index,ele){
		if(ele.imageUrl == undefined){
			ele.imageUrl = '';
		}
		dataList.push(ele);
	})
	return dataList;
}