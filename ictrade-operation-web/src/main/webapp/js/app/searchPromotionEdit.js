var vm;
var recommendationId = getQueryString("recommendationId");
$(function() {
	var valid = false;
	vm = new Vue(
			{
				el : "#recuit-edit",
				data : {
					initData : {
						distributor: "",
						recommendationId: "",
						status: "",
						source: "",
						orderSeq:'1',      //排序
						categoryType1:'', //大类id
						categoryType2:'', //小类id
						categoryType3:'', //次小类id
						goodsQuantity:'', //推荐商品数
						desc:'',          //描述
						expiryTime:''     //推广截止日
					},
				    activeVendor:'',//选中的分销商id
				    activeBrand:'',//选中的制造商id
				    activeVendorName:'',//选中的分销商名称
				    activeBrandName:'',//选中的制造商名称
				    temporaryVendor:'',//临时分销商id
				    temporaryBrand:'',//临时制造商id
				    temporaryVendorName:'',//临时分销商名称
				    temporaryBrandName:'',//临时制造商名称
		            api: ykyUrl.product+"/v1/products/categories/children?parentCateId={parentCateId}",//设置请求城市数据接口URL
					//api:"http://192.168.1.110:27083/v1/products/categories/children",
		            inputClass : 'form-control', //设置控件class
		            styleObject: { width : "150px" }, //设置控件style 
		            nameFirst : 'categoryType1',
		            nameSecond : 'categoryType2',
		            nameThird : 'categoryType3',
		            idFirst : 'first',//设置id,可选
		            idSecond : 'second',//设置id,可选
		            idThird : 'third',//设置id,可选
		            parentId: "parentCateId",
		            optionId:"_id",
		            optionName:"cateName",
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
		                selected:'unlimited',
		                placeholder:'搜索分销商',
		            	multiple:false, //单选
			    	},
			    	brandSelect:{
			    		keyname:'选择原厂',
						validate:{
		                    "required": true
		                },
		                id:'selbox',
		                name:'selbox',
		                options:[],
		                optionId: 'id',
		                optionName: 'brandName',
		                selected:'unlimited',
		                placeholder:'搜索原厂',
		            	multiple:false, //单选
		                isFuzzySearch:true,
		            	reloadApi:ykyUrl.product + '/v1/products/brands/alias?keyword={selbox}'
			    	},
			    	showModal:false,
			    	modalTitle:'',
			    	modalStyle:{
			    		width:1000,
						maxHeight:600,
						overflowY:'auto'
			    	},
			    	showVendor:false,
			    	showBrand:false,
			    	
				},
				created: function(){
					var that = this;
					syncData(ykyUrl.product + '/v1/products/brands ', 'GET', null, function(res, err){
						if(err == null){
							res.unshift({id:'unlimited',brandName:'不限'})
							that.brandSelect.options = res;
						}
					});
					syncData(ykyUrl.infoPort + '/v1/party/allparty', 'PUT', {roleType:'SUPPLIER', status:'PARTY_ENABLED'}, function(res, err){
						if(err == null){
							$.each(res, function(i, e){
								if(e.partyGroup && e.partyGroup.groupName){
									e.supplierName = e.partyGroup.groupName;
								}
							})
							res.unshift({id:'unlimited',supplierName:'不限'})
							that.vendorSelect.options = res;
						}
					});
					this.$nextTick(function(){
						$('.form_datetime').datetimepicker({
							language: config.language,
							format: 'yyyy-mm-dd',
							minView: 'month',
					        startView: 2,
					        autoclose: true,
					        todayHighlight: true,
					        startDate:new Date,
					    })
					})
				    
				},
				methods : {
					change: function (data) {
		                console.log(data);
					},
					chooseVendor: function(){
						this.showModal = true;
						this.showVendor = true;
						this.modalTitle = '请选择分销商';
					},
					chooseBrand: function(){
						this.showModal = true;
						this.showBrand = true;
						this.modalTitle = '请选择原厂';
					},
					toggleModal: function(){
						this.showModal = false;
						this.showVendor = false;
						this.showBrand = false;
					},
					modalOk: function(){
						if(this.showVendor){
							this.activeVendor = this.temporaryVendor;
							this.activeVendorName = this.temporaryVendorName;
							this.vendorSelect.selected = this.temporaryVendor;
						}else if(this.showBrand){
							this.activeBrand = this.temporaryBrand;
							this.activeBrandName = this.temporaryBrandName;
							this.brandSelect.selected = this.temporaryBrand;
						}
						this.toggleModal();
					},
					getVendorSelected: function(data){
						this.temporaryVendorName = data.supplierName;
						this.temporaryVendor = data.id
					},
					getBrandSelected: function(data){
						this.temporaryBrandName = data.brandName;
						this.temporaryBrand = data.id
					},
					checkGoodsQuantity: function(e){
						var val = $(e.currentTarget).val();
						if(val){
							if(val > 10){
								$("#extend").addClass("b_red");
								layer.msg("推广商品数不超过10条！",{
									skin:"c_tip"
								});
							}else{
								$("#extend").removeClass("b_red");
							}
						}
					},
					submitData: function(){
						$("#searchPromotion").submit();
					},
					goBack: function(){
						history.go(-1);
					},
					emptyExpiryTime: function(){
						$("input[name='expiryTime']").val('');
					}
				}
			});

	formValidate('#searchPromotion', {}, function(){
		if(vm.activeVendor === '' && vm.activeBrand === '' && $("select[name='categoryType1']").val() === '' && $("select[name='categoryType2']").val() === '' && $("select[name='categoryType3']").val() === ''){
			layer.msg("分销商、原厂、分类必填一项！",{
				skin:"c_tip"
			});
			return;
		}
		var num = vm.initData.goodsQuantity;
		if(num > 10){
			$("#extend").addClass("b_red");
			layer.msg("推广商品数不超过10条！",{
				skin:"c_tip"
			});
			return;
		};
		if($("select[name='categoryType1']").val() === '' && $("select[name='categoryType2']").val() === '' && $("select[name='categoryType3']").val() === ''){
			$("#categoryType").val('');
		}else if($("select[name='categoryType1']").val() !== '' && $("select[name='categoryType2']").val() === '' && $("select[name='categoryType3']").val() === ''){
			$("#categoryType").val($("select[name='categoryType1']").val())
		}else if($("select[name='categoryType1']").val() !== '' && $("select[name='categoryType2']").val() !== '' && $("select[name='categoryType3']").val() === ''){
			$("#categoryType").val($("select[name='categoryType2']").val())
		}else if($("select[name='categoryType1']").val() !== '' && $("select[name='categoryType2']").val() !== '' && $("select[name='categoryType3']").val() !== ''){
			$("#categoryType").val($("select[name='categoryType3']").val())
		}
		var data = $('#searchPromotion').serializeObject();
		data.distributor = data.distributor != 'unlimited' ? data.distributor : '';
		data.source = data.source != 'unlimited' ? data.source : '';
		var url = recommendationId ? ykyUrl.info + '/v1/recommendations/upsearchpromotion/' : ykyUrl.info + '/v1/recommendations/addsearchpromotion';
		var type = recommendationId ? 'PUT' : 'POST';
		syncData(url, type, data, function(res,err){
			if(err == null){
				setTimeout(function(){
					   location.href = ykyUrl._this + "/searchPromotion.htm";
					   },1000);
			}
		})
	});
	
});
//请求获取初始数据 
if(recommendationId){
	$.aAjax({
		url:ykyUrl.info + '/v1/recommendations/searchpromotion/' + recommendationId,
		type:'GET',
		success:function(data){
			if(data){
				vm.initData.orderSeq = data.orderSeq;
				vm.initData.desc = data.desc;
				vm.initData.distributor = data.distributor;
				vm.initData.source = data.source; 
				vm.initData.categoryType1 = data.categoryType1;
				vm.initData.categoryType2 = data.categoryType2;
				vm.initData.categoryType3 = data.categoryType3;
				vm.initData.status = data.status;
				vm.initData.recommendationId = data.recommendationId;
				vm.initData.goodsQuantity = data.goodsQuantity;
				if(data.distributor){
					vm.activeVendor = data.distributor;
					vm.activeVendorName = data.distributorName;
					vm.vendorSelect.selected = data.distributor;
				}
				if(data.source){
					vm.activeBrand = data.source;
					vm.activeBrandName = data.sourceName;
					vm.brandSelect.selected = data.source;
				}
				if(data.expiryDate){
					var expiryDate = new Date(data.expiryDate).Format('yyyy-MM-dd');
					$("input[name='expiryTime']").val(expiryDate);
				}
				//编辑页面分类数据的展示
				if(data.categoryType1 != ""){
					$.aAjax({
						url:ykyUrl.product + '/v1/products/categories/' + data.categoryType1, //访问制造商列表接口
						type:'GET',		
						success:function(data){
							
						},
						error:function(data){
							console.log("error");
						}
					})
				}
			}
		},
		error:function(){
			console.log("error")
		}
	})
}
