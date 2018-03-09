var vm
$(function() {
	var valid = false;
	 vm = new Vue(
			{
				el : "#commodityMaintain",
				data : {
					initData : {
						activityId:'',
						name:'',
				        type:'flash',
				        startDates:'',
				        endDates:'',
				        aperList:[{startTime:'',endTime:''},]
					},
					queryParams : {//请求接口参数 
		                page:1,//当前页
		                periodsId:'',//时间区间id
		                status:'ENABLE',//状态
		                all:0,
		                pageSize:10
		            },
					prdList : [],
					periodsList : [],
					editInfo:{					    
					    "activityProductId" : "",
					    "activityId" : "",//活动id
					    "periodsId" : "",//时间区间id
					    "productId" : "",//商品id
					    "manufacturerPartNumber" : "",//制造商型号 
					    "manufacturer" : "",//制造商
					    "sourceId" : "",//仓库id 
					    "sourceName" : "",//仓库名称
					    "vendorId" : "",//供应商id 
					    "vendorName" : "",//供应商名称
					    "discount" : "",//供应商名称
					    "currencyUomId" : "",//币种					  
					    "title" : "",//标题
					    "subTitle" : "",//副标题
					    "image1" : "",
					    "image2" : "",
					    "image3" : "",
					    "image4" : "",
					    "image5" : "",
					    "totalQty" : "",//总库存数 
					    "qty" : ""//当前库存数 
					  },
					activityId : $("#activityId").val(),
					from : getQueryString('from'),
					newsHome: operationWebUrl + "/shoppromotion.htm",
					loadingData:'加载中...',
					enableLength:0,
					showDeleteBtn:true,
					index:'',
					iframeUrl:$("#iframeUrl_Id").val(),
					periodsId:'',
					fileName:'',
					fileUrl: '',
					getTableData: false,
					selectList:[],
					selectAll:false,
					selectFlag:false,
					showPagesizeChange:true,
					all:3,
					singleDelete:false
				},
				watch:{
					selectAll:function(val,oldVal){
						var that = this;
						var arr = [];
						if(val == true){
							that.prdList.forEach(function(data){
								arr.push(data);
							})
							that.selectList = _.uniq(that.selectList.concat(
			                        arr));
						}else if(val == false && that.selectFlag){
							that.selectList = [];
						}						
					},
					selectList:function(val,oldVal){
						var that = this;
						if(val.length == that.prdList.length){
							that.selectAll = true;
							that.selectFlag = true;
						}else{
							that.selectAll = false;
							that.selectFlag = false;
						}
					}
				},
				methods : {
					init : function(res) {
						var that = this;
						that.enableLength = 0;
						that.periodsList = res.periodsList;
						that.renderPeriods();
					},
					putQuery: function (params) {   //更改组件参数
		                var that = this;
		                that.queryParams = params;
		                that.changePeriods(that.periodsId);
		            },
					renderPeriods :function(){//时间段渲染
						var that = this;
						if(that.periodsId){
							that.queryParams.periodsId = that.periodsId;
						}else if(that.periodsList && that.periodsList.length){
							$.each(that.periodsList,function(index,ele){
								if(ele.status == 'ENABLE'){
									that.queryParams.periodsId = ele.periodsId;
									return false;
								}
							})
							$.each(that.periodsList,function(index,ele){
								if(ele.status == 'ENABLE'){
									that.enableLength = ++that.enableLength;
								}
							})
						}
						that.changePeriods(that.queryParams.periodsId);
					},
					changePeriods : function(periodsId){//选择时间段
						var that = this;
						that.queryParams.periodsId = periodsId;
						that.periodsId = periodsId;
						if(!periodsId){
							that.loadingData = '没有查询到相关数据';
							return;
						}
						if(that.from == 'information'){
							that.queryParams.status = '';
						}
						var url = ykyUrl.product + '/v1/activities/'+that.activityId+'/products/draft';
						//var url = 'http://operation-sit.yikuyi.com/services-product/v1/activities/' +that.activityId+'/products/draft';//sit环境
						syncData(url, 
								'GET', 
								that.queryParams, 
								function(res, msg) {
									//var res = $.extend({},res.popo);
									that.prdList = res.list; 
									that.queryParams.all = res.pages;
									if(!res.list.length){
										that.loadingData = '没有查询到相关数据'
									}
									if(that.enableLength==1 && that.prdList.length==1){
										that.showDeleteBtn = false;
									}
								});
					},
					saveData : function(event) {//保存数据
						var that = this;
						syncData(ykyUrl.product + '/v1/activities/'+that.activityId+'/draft', 
								'PUT', 
								null, 
								function(res, msg) {
									if(msg == null){
										location.href = ykyUrl._this + '/shoppromotion.htm';
									}									
								})
					},
					editProduct :function(activityProductId){//编辑商品
						var obj = {					    
							    "activityProductId" : "",
							    "activityId" : "",//活动id
							    "periodsId" : "",//时间区间id
							    "productId" : "",//商品id
							    "manufacturerPartNumber" : "",//制造商型号 
							    "manufacturer" : "",//制造商
							    "sourceId" : "",//仓库id 
							    "sourceName" : "",//仓库名称
							    "vendorId" : "",//供应商id 
							    "vendorName" : "",//供应商名称
							    "discount" : "",//供应商名称
							    "currencyUomId" : "",//币种					  
							    "title" : "",//标题
							    "subTitle" : "",//副标题
							    "image1" : "",
							    "image2" : "",
							    "image3" : "",
							    "image4" : "",
							    "image5" : "",
							    "totalQty" : "",//总库存数 
							    "qty" : ""//当前库存数 
							  }
						var that = this;
						var url = ykyUrl.product + '/v1/activityproducts/' + activityProductId;
						//var url = 'http://operation-sit.yikuyi.com/services-product/v1/activityproducts/' + activityProductId;//sit环境
						syncData(url,
								'GET', 
								null, 
								function(res, msg) {
									that.editInfo = $.extend(obj,res); 
									setTimeout(function() {
										if (res.description && res.description) {
											var content = 'loadContent' + res.description;
											top.frames[0].postMessage(content,ykyUrl.ictrade_ueditor);
										}
										$(".modal-mask").css("display","table");
										$("#commodityEdit").scrollTop(0)
									}, 300);
								});												
					},
					closeEdit:function(){//关闭编辑弹窗
						var that = this;
						$("#qty").removeClass("error_bor");
						$(".modal-mask").css("display","none");
						document.all.coniframe.src = that.iframeUrl;						
					},
					saveEdit:function(){//弹窗保存按钮
						requireSaveData();
					},
					savaProductInfo:function(event){//保存弹窗商品编辑
						var that = this;
						that.editInfo.description = event.data;
						flag = false;
				    	  $("#create").submit()
				    	  if(!flag){
				    		  return;
				    	  }
				    	  var prdUrl = ykyUrl.product + "/v1/activityproducts/" + that.editInfo.activityProductId;
				    	  //var prdUrl = "http://operation-sit.yikuyi.com/services-product/v1/activityproducts/" + that.editInfo.activityProductId;//sit环境
				    	  syncData(prdUrl, 'PUT', that.editInfo,
									function(res, err) {//页面加载前调用方法
										if(err == null){
											var url = ykyUrl.product + '/v1/activities/'+vm.activityId+'/products/draft';
											//var url = 'http://operation-sit.yikuyi.com/services-product/v1/activities/'+vm.activityId+'/products/draft';//sit环境
												syncData(url, 
													'GET', 
													that.queryParams, 
													function(res, msg) {
														that.prdList = res.list;
													});
										}
									});
				    	  $(".modal-mask").css("display","none");
				    	  document.all.coniframe.src = that.iframeUrl;
					},
					deleteImage:function(imageIndex){//删除商品图片
						var that = this;
						var list = [];
						var arr = ['image1','image2','image3','image4','image5'];
						that.editInfo[imageIndex] = "";
						$.each(arr,function(index,ele){
							if(that.editInfo[ele]){
								list.push(that.editInfo[ele])
								that.editInfo[ele] = "";
							}
						})
						$.each(list,function(index,ele){
							if(ele){
								that.editInfo['image'+(index+1)] = ele;
							}
						})
					},
					deleteProduct : function(item){//删除活动商品
						var that = this;
						that.multipleDelete(item);						
					},
					getUrl : function(activityId,periodsId){
						return ykyUrl._this + '/activity/maintain.htm?activityId=' + activityId + '&periodsId=' + periodsId;
					},
					addProduct:function(){//新增商品
						var that = this;
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
									url: ykyUrl.product+'/v1/activities/'+vm.activityId+'/products/parse?periodsId='+vm.periodsId+'&oriFileName='+ vm.fileName+'&fileUrl='+vm.fileUrl,
									type: 'POST',
									success: function(res){
										layer.close(index);
										vm.fileName='';
										vm.fileUrl='';										
										layer.closeAll();
										that.changePeriods(that.periodsId);
										vm.getTableData = !vm.getTableData;
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
					downloadSelect: function(){
						var that = this;
						if(that.prdList.length == 0) {
							layer.msg('没有数据可以下载')
							return false;
						}
						var ids = '', checkedItem;
						checkedItem = that.selectList;
						if(checkedItem.length > 0){
							for(var i =0; i<checkedItem.length; i++){
								if(ids !== ''){
									ids += ','+checkedItem[i].activityProductId
								}else{
									ids = checkedItem[i].activityProductId;
								}
							}
						}else{
							layer.msg('请选择需要下载的数据')
							return false;
						}
						$("input[name=Authorization]").val($('#pageToken').val());
						$("input[name=ids]").val(ids);
						$("input[name=activityId]").val($("#activityId").val());
						$("input[name=periodsId]").val(vm.periodsId);
						$("input[name=status]").val('');
						var date_url = ykyUrl._this + "/activity/export.htm";
						var form=$("#exportForm");//定义一个form表单
					    form.attr("action",date_url);
					    form.submit();//表单提交						
					},
					downloadError: function(){
						var that = this;
						if(that.prdList.length == 0) {
							layer.msg('没有数据可以下载')
							return false;
						}
						$("input[name=Authorization]").val($('#pageToken').val());
						$("input[name=ids]").val('');
						$("input[name=status]").val('UNABLE');
						$("input[name=activityId]").val($("#activityId").val());
						$("input[name=periodsId]").val(vm.periodsId);
						var date_url = ykyUrl._this + "/activity/export.htm";
						var form=$("#exportForm");//定义一个form表单
					    form.attr("action",date_url);
					    form.submit();//表单提交
					},
					multipleDelete:function(type){//批量删除
						var that = this;
						var ids = [], checkedItem = [];
						if(type == 'productList'){							
							checkedItem = that.selectList;							
						}else{
							checkedItem.push(type);
							that.singleDelete = true;
						}												
						if(checkedItem.length > 0){
							for(var i =0; i<checkedItem.length; i++){
								ids.push(checkedItem[i].activityProductId)
							}
						}else{
							layer.msg('请选择需要删除的数据')
							return false;
						}
						var index = layer.confirm('确认删除？',{
							 btn: ['确认','取消'] //按钮
						},function(){
							multiDelete(ykyUrl.product + "/v1/activities/" + that.activityId + "/periods/" + that.periodsId + "/products?activityType=1000", 'DELETE', ids,
									function(res, err) {//页面加载前调用方法
										if(err == null){
											var curPage = that.queryParams.page
											var isAll = that.selectList.length==that.prdList.length || (that.prdList.length==1&&that.singleDelete) ? true : false;
											if(isAll && curPage == that.queryParams.all && curPage>1){
												--that.queryParams.page;
											}
											that.selectList = [];
											that.selectAll = false;
											that.singleDelete = false;
											if(that.prdList.length==1){
												syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });
											}else{
												that.changePeriods(that.periodsId);
											}
										}
									});
							layer.close(index);
						})
					}
				},
			});	

	if (vm.activityId) {//获取草稿活动详情
		syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });
		//syncData('http://operation-sit.yikuyi.com/services-product/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });//sit环境
	}
	$(".btn-concle").on("click",function(){
		if(vm.from){
			window.location.href = operationWebUrl + "/shoppromotion/information/"+vm.activityId+".htm?from=maintain"; 
		}else{
			window.location.href = operationWebUrl + "/shoppromotion/upload/"+vm.activityId+".htm"; 
		}
	})	
	$("#create").validate({
		rules:{
			qty:{
				required:true,
				positiveinteger:true,
			}
		},
		messages:{
			qty:{
				required:' ',
				positiveinteger:' '
			}
		},
		success:function(label,element){
			$(element).removeClass("error_bor");
		},
		errorPlacement:function(error, element){
			$(element).addClass("error_bor");
		},
		submitHandler : function(){
    		flag = true;
    	}
	})
	jQuery.validator.addMethod("positiveinteger", function(value, element) {
	   var aint=parseInt(value);	
	    return aint>0&& (aint+"")==value;   
	  }, "请输入正整数");   

	uploadInit();
function uploadInit(){
	var uploader1 = createUploader({
		buttonId: "uploadBtn1", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "2mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.editInfo.image1 = _yetAnotherFileUrl;
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	var uploader2 = createUploader({
		buttonId: "uploadBtn2", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "2mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.editInfo.image2 = _yetAnotherFileUrl;
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	var uploader3 = createUploader({
		buttonId: "uploadBtn3", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "2mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.editInfo.image3 = _yetAnotherFileUrl;
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	var uploader4 = createUploader({
		buttonId: "uploadBtn4", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "2mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.editInfo.image4 = _yetAnotherFileUrl;
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	var uploader5 = createUploader({
		buttonId: "uploadBtn5", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "2mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.editInfo.image5 = _yetAnotherFileUrl;
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	var uploader = createUploader({
		buttonId: "uploadBtn", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
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
	uploader.init(); 
	uploader1.init();
	uploader2.init();
	uploader3.init();
	uploader4.init();
	uploader5.init();
}
if (window.addEventListener) {
	window.addEventListener('message', vm.savaProductInfo, false);
} else if (window.attachEvent) {
	window.attachEvent('message', vm.savaProductInfo, false);
}
function requireSaveData() {
	//发送指令到iframe请求返回数据
	top.frames[0].postMessage('save_btn', ykyUrl.ictrade_ueditor);
}
});