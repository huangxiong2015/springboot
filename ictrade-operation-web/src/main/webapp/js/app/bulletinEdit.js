var vm
$(function() {
	var valid = false;
	//var newsId = parseUrlParam(false).id;
	vm = new Vue(
			{
				el : "#bulletin-edit",
				data : {
					initData : {
						categoryTypeId : 'INFORMATION',
						attachUrl:'',
						newsContent:'',
						title:'',
						publishOrg:'易库易'
					},
					newsId : getQueryString('id'),
					newsHome: operationWebUrl + "/bulletin.htm",
					checkModel : {},
			        modalStyle:{
			        	width:1000,
						maxHeight:600,
						overflowY:'auto'
			        },
			        showModal:false,
			        showVendor:false,
			        showBrand:false,
			        modalTitle:'',
			        vendorSelect:{   
						keyname:'选择分销商',
						validate:{
		                    "required": true
		                },
		                id:'selbox',
		                name:'selbox',
		                options:[],
		                optionId: 'id',
		                optionName: 'name',
		                selected:[],
		                placeholder:'搜索分销商',
		                multiple:true
					},
					brandSelect:{   
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
					activeVendor:[],
					activeBrand:[]
				},
				created:function(){
					getSupplier();
					getManufacturer();
				},
				methods : {
				  choosedistributor:function(){//选择分销商
					  this.showModal = true;
					  this.showVendor = true;
					  this.modalTitle = '请选择分销商';
				  },
			      chooseManufacturer:function(){//选择制造商
			    	  this.showModal = true;
					  this.showBrand = true;
					  this.modalTitle = '请选择制造商';
			      },
					init : function(res) {
						this.initData.publishOrg = res.publishOrg;
						this.initData.title = res.title;
						this.initData.categoryTypeId = res.categoryTypeId;
						this.initData.attachUrl = res.newsAttachment && res.newsAttachment.attachUrl || ''; 
						this.checkModel.isTop = ('Y' == res.isTop);
						this.checkModel.isTitleRed = ('RED' == res.isTitleRed); 
						if(res.distributor){
							this.activeVendor = JSON.parse(res.distributor);
							var vendorArr = [];
							$.each(this.activeVendor,function(i,e){
								if(e.id){
									vendorArr.push(e.id);
								}
							})
							this.vendorSelect.selected = vendorArr;
						}
						if(res.manufacturer){
							this.activeBrand = JSON.parse(res.manufacturer);
							var brandArr = [];
							$.each(this.activeBrand,function(i,e){
								e.brandName = e.name;
								if(e.id){
									brandArr.push(Number(e.id));
								}
							})
							this.brandSelect.selected = brandArr;
						}
						setTimeout(function() {
							if (res.newsContent && res.newsContent.content) {
								var content = 'loadContent' + res.newsContent.content;
								window.frames[0].postMessage(content,ykyUrl.ictrade_ueditor);
							}
						}, 500);
					},
					saveNews : function(event) {
						this.initData.newsContent = event.data;
						$('#create').submit();
					},
					toggleModal:function(){//关闭弹窗
						this.showModal = false;
						this.showVendor = false;
						this.showBrand = false;
					},
					getVendorSelected:function(data){//获取选中的分销商
						var list = data.agentObj;
						var arr = [];
						this.activeVendor = JSON.parse(JSON.stringify(data.agentObj));
						if(list.length){
							$.each(list,function(i,e){
								if(e.id){
									arr.push(e.id);
								}
							})
						}
						this.vendorSelect.selected = arr;
						this.showModal = false;
						this.showVendor = false;
					},
					deleteVendorSelect:function(index,id){//删除选中的分销商
						this.activeVendor.splice(index,1);
						var selected = this.vendorSelect.selected;
						for(var i=0;i<selected.length;i++){
							if(selected[i] == id){
								selected.splice(i,1);
								break;
							}
						}
					},
					getBrandSelected:function(data){//获取选中的制造商
						var list = data.agentObj;
						var arr = [];
						this.activeBrand = JSON.parse(JSON.stringify(data.agentObj));
						if(list.length){
							$.each(list,function(i,e){
								if(e.id){
									arr.push(e.id);
								}
							})
						}
						this.brandSelect.selected = arr;
						this.showModal = false;
						this.showBrand = false;
					},
					deleteBrandSelect:function(index,id){//删除选中的制造商
						this.activeBrand.splice(index,1);
						var selected = this.brandSelect.selected;
						for(var i=0;i<selected.length;i++){
							if(selected[i] == id){
								selected.splice(i,1);
								break;
							}
						}
					},
				}
			});
	if (vm.newsId) {
		syncData(ykyUrl.info + '/v1/news/' + vm.newsId,
			'GET',
			null,
			function(res, msg) {
				vm.init(res);
		});
	}
	formValidate('#create', {}, function(){
		var formArry = $('#create').serializeObject();
		formArry.content = vm.initData.newsContent;
		formArry.isTop = vm.checkModel.isTop ? 'Y' : 'N';
		formArry.isTitleRed = vm.checkModel.isTitleRed ? 'RED' : 'DEFAULT';
		var brands = []; //manufacturer
		var vendors = [];  //分销商
		$.each(vm.activeBrand,function(i,item){
			var temp ={
				id: item.id,
				name:item.brandName
			}
			brands.push(temp);
		})
		$.each(vm.activeVendor,function(i,item){
			var tempVendor ={
				id: item.id,
				name:item.name
			}
			vendors.push(tempVendor);
		})
		delete formArry.selbox;
		formArry.manufacturer = JSON.stringify(brands);
		formArry.distributor = JSON.stringify(vendors);
		if (!vm.newsId) {
			syncData(
					ykyUrl.info + "/v1/news",
					"POST",
					formArry,
					function(data, err) {
						if (null != data) {
							document.location.href = operationWebUrl + "/bulletin.htm";
						}
					});
		} else {
			formArry.newsId = vm.newsId;
			syncData(
					ykyUrl.info + "/v1/news/" + vm.newsId,
					"PUT",
					formArry,
					function(data, err) {
						if (null != data) {
							document.location.href = operationWebUrl + "/bulletin.htm";
						}
					});
		}
	});
    setTimeout(function() {
		if (window.addEventListener) {
			window.addEventListener('message', vm.saveNews, false);
		} else if (window.attachEvent) {
			window.attachEvent('message', vm.saveNews, false);
		}
	},1000);
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
					console.log(file); 
					vm.initData.attachUrl = _yetAnotherFileUrl;
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
    }); 
	uploader.init();	
	
	$('.del_logo').on('click', function(){
		vm.initData.attachUrl ='';
	}); 
	
   $('.btn-concle').on('click', function(){
	   LS.set("bulletinEditCancel","Y");
	   window.location.href = operationWebUrl + "/bulletin.htm"; 
	});
   
   
 //获取分销商distributor
   function getSupplier(){
		var url = ykyUrl.info + '/v1/news?categoryTypeIdStr=VENDOR&pageSize=5000';
		var arr = [];
		syncData(url, 'GET', null, function(res,err){
			if(err == null){
				var list = res.list;
				$.each(list,function(i,item){
					arr.push({
						id:item.newsId,
						name:item.title
					})
				})
				vm.vendorSelect.options = arr;
			}
		})
   }

   //获取制造商
   function getManufacturer(){
	    var url = ykyUrl.product +"/v1/products/brands";
	    var arr = [];
	    syncData(url, 'GET', null, function(res,err){
	    	if(err == null){
	    		var list = res;
	    		$.each(list,function(i,item){
	    			arr.push({
	    				id:item.id,
	    				brandName:item.brandName
	    			})
	    		})
	    		vm.brandSelect.options = arr;
	    	}
	    })
   }
});


function requireSaveData() {
	//发送指令到iframe请求返回数据
	window.frames[0].postMessage('save_btn', ykyUrl.ictrade_ueditor);
}

