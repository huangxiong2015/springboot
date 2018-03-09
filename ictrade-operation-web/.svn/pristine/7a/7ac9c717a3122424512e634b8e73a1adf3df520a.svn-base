var vm;

$(function() {
	 vm = new Vue({
			        el: '#extension_edit',
			        data: {
			        	id:getQueryString("recommendationId"),
			        	flag:false,
						initData : {
							categoryTypeId:'extension', //推广位置类型
						    categoryId:'4001',//类型 4001活动 4002精选分销商  4003制造商
						    orderSeq:'1',//推广位置
						    desc:'', //推广描述
						    startDate:'', //投放开始时间
						    expiryDate:'' //投放结束时间
						},
						content:{
					        activityLink:'',//活动链接
					        character:"",//分销商、制造商添加描述
					        currentId:"",//分销商、制造商id
					        facturerName:'',//分销商、制造商名称
					        imageUrl:'',//活动背景图、分销商制造商推广logo
					        imageUrlLogo:"",//分销商制造商推广logo
					        linkDetails:"",//分销商、制造商详情链接
					        labelContent:"none",//分销商标签内容
					        customContent:"",//分销商自定义标签内容
					        activityUrl:""//分销商标签链接
					    },
					    modalStyle:{ //弹窗样式
				        	width:1000,
							maxHeight:600,
							overflowY:'auto'
				        },
				        modalTitle:'', //弹窗标题
				        showModal:false,
				        showVendor:false,
				        showBrand:false,
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
			                multiple:false
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
			                multiple:false,
			                isFuzzySearch:true,
			            	reloadApi:ykyUrl.product + '/v1/products/brands/alias?keyword={selbox}'
						}
					},
					watch:{
						'initData.categoryId':function(val){
							if(this.flag){
								this.content = {
								        activityLink:'',
								        character:"",
								        currentId:"",
								        facturerName:'',
								        imageUrl:'',
								        imageUrlLogo:"",
								        linkDetails:"",
								        labelContent:"none",
								        customContent:"",
								        activityUrl:""
								    }
							}else{
								this.flag = true;
							}
						}
					},
					created:function(){
						//获取分销商主句
						syncData(ykyUrl.info + "/v1/news?categoryTypeIdStr=VENDOR&pageSize=9999",'GET',null,function(res,err){
							if(err == null){
								var arr = [];
								if(res.list.length){
									$.each(res.list,function(i,e){
										if(e.newsId && e.title){
											arr.push({
												id:e.newsId,
												name:e.title
											})
										}
									})
								}
								vm.vendorSelect.options = arr;
							}
						})
						//获取制造商数据
						 syncData(ykyUrl.product + '/v1/products/brands?size=9999','GET',null,function(res,err){
							 if(err == null){
								 var arr = [];
								 if(res.length){
									 $.each(res,function(i,e){
										 if(e.id && e.brandName){
											 arr.push({
												 id:String(e.id),
												 brandName:e.brandName
											 })
										 }
									 })
									 vm.brandSelect.options = arr;
								 }
							 }
						 })
						this.$nextTick(function(){
							setDate();
						})
					},
					methods : {
						saveData : function(event) {
							$('#create').submit();
						},
					    chooseVendor:function(){  //选择分销商
						  this.showModal = true;
						  this.showVendor = true;
						  this.modalTitle = '请选择分销商';
					    },
				        chooseBrand:function(){  //选择制造商
				    	  this.showModal = true;
						  this.showBrand = true;
						  this.modalTitle = '请选择制造商';
				        },
					    toggleModal:function(){  //关闭弹窗
							this.showModal = false;
							this.showVendor = false;
							this.showBrand = false;
						},
						getVendorSelected:function(data){  //获取选中的分销商
							this.content.currentId = data.id;
							this.content.facturerName = data.name;
							this.content.linkDetails = ykyUrl.portal + '/vendor/' + data.id + '.htm'
							this.vendorSelect.selected = data.id;
							this.showModal = false;
							this.showVendor = false;
						},
						getBrandSelected:function(data){  //获取选中的制造商
							this.content.currentId = data.id;
							this.content.facturerName = data.brandName;
							this.content.linkDetails = ykyUrl.portal + '/brand/' + data.id + '.htm'
							this.brandSelect.selected = data.id;
							this.showModal = false;
							this.showBrand = false;
						}
					}
	 		}); 
	 
 var uploader1 = createUploader({
		buttonId: "uploadback", 
		uploadType: "notice.publicRead", 
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",
		fileSize: "500kb",
		isImage: true, 
		init:{
			FileUploaded : function(up, file, info) {
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.content.imageUrl = _yetAnotherFileUrl;
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
    }); 
	uploader1.init();
	function setDate(){
		$('#startDate').datetimepicker({
		    language:  config.language, 
		    minView: 'day',   //设置只显示到小时
		    format: 'yyyy-mm-dd hh:00:00',
		    autoclose: true,
		    startDate:new Date
		}).on('change',function(ev){
		    var startDate = $('#startDate').val();
		    $("#expiryDate").datetimepicker('setStartDate',startDate);
		    vm.initData.startDate = startDate;
		});
		
		$('#expiryDate').datetimepicker({
			 language:  config.language, 
			 minView: 'day',   //设置只显示到小时
		     format: 'yyyy-mm-dd hh:00:00',
		     autoclose: true,
		     startDate:new Date
		}).on('change',function(ev){
		    var expiryDate = $('#expiryDate').val();
		    $("#startDate").datetimepicker('setEndDate',expiryDate);
		    vm.initData.expiryDate = expiryDate;
		});
	 }
	formValidate('#create', {}, function(){
		var initData = vm.initData;
		if(initData.categoryId == '4001' && (!initData.startDate || !initData.expiryDate)){
			layer.msg('请选择投放时间！');
			return;
		}
		if(!vm.content.imageUrl){
			var str = initData.categoryId == '4001' ? '背景图' : '推广logo';
			layer.msg('请上传' + str + '!');
			return;
		}
		var formArry = $('#create').serializeObject();
		var id = vm.id
		var url = id ? ykyUrl.info + '/v1/recommendations/' + id : ykyUrl.info + '/v1/recommendations';
		var type = id ? 'PUT' : 'POST';
		if(formArry.content && formArry.content.activityLink){
			formArry.content.activityLink = resetUrl(formArry.content.activityLink);
		}
		if(formArry.categoryId != '4001'){
			delete formArry.startDate
			delete formArry.expiryDate
		}
		formArry.content = JSON.stringify(formArry.content);
		
		syncData(url,type,formArry,function(res,err){
			if(err == null){
				layer.msg('保存成功！');
				setTimeout(function(){
					location.href = ykyUrl._this + "/extension.htm";
				},2000);
			}
		})
	});
	//请求获取初始数据 
	if(vm.id){
		syncData(ykyUrl.info + '/v1/recommendations/' + vm.id,'GET',null,function(res,err){
			if(err == null){
				vm.content = $.extend({},vm.content,res.contentMap);
				vm.initData = $.extend({},vm.initData,res);
				if(res.categoryId == '4002'){
					vm.vendorSelect.selected = vm.content.currentId;
				}else if(res.categoryId == '4003'){
					vm.brandSelect.selected = String(vm.content.currentId);
				}
			}
		})
	}
	$("#cancel_btn").on("click",function(){
		history.go(-1);
	})
	function resetUrl(url){
		if(!resetUrl){
			return ""
		}else if(url.indexOf("http:")>=0){
			return url.replace("http:","");
		}else if(url.indexOf("https:")>=0){
			return url.replace("https:","");
		}else{
			if(url.substring(0,2) === "//"){
				return url;
			}else{
				return "//" + url;
			}
		}
	}
});