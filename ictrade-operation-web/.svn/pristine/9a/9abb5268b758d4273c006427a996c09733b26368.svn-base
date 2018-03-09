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
					newsHome: operationWebUrl + "/timepromotion/list.htm",
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
					selectAll:false
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
						}else{
							that.selectList = [];
						}						
					},
				},
				methods : {
					init : function(res) {
						var that = this;
						that.enableLength = 0;
						that.periodsList = res.periodsList;
						that.renderPeriods();
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
						var url = ykyUrl.product + '/v1/activities/'+that.activityId+'/products/draft';
						//var url = 'http://operation-sit.yikuyi.com/services-product/v1/activities/' +that.activityId+'/products/draft';//sit环境
						syncData(url, 
								'GET', 
								that.queryParams, 
								function(res, msg) {							
									that.prdList = res.list; 
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
						if(that.prdList.length<1){
							layer.msg('请上传商品!');
							return
						}
						syncData(ykyUrl.product + '/v1/activities/'+that.activityId, 
								'PUT', 
								null, 
								function(res, msg) {
									if(msg == null){
										location.href = ykyUrl._this + '/timepromotion/list.htm';
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
					deleteProduct : function(activityProductId,activityId,periodsId){//删除活动商品
						var that = this;
						var index = layer.confirm('确认删除？',{
							 btn: ['确认','取消'] //按钮
						},function(){
							syncData(ykyUrl.product + "/v1/activities/" + activityProductId + "/delete?activityId=" + activityId + "&periodsId=" + periodsId, 'DELETE', null,
									function(res, err) {//页面加载前调用方法
										if(err == null){
											if(that.prdList.length==1){
												syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });
											}else{
												that.changePeriods(periodsId);
											}
										}
									});
							layer.close(index);
						})						
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
										//vm.queryParamsAll.processId = res;
										getDataList(res);
										layer.closeAll();
										
										vm.$nextTick(function(){
											vm.$children[0].ickeck = [];
											vm.$children[0].isAll = false;
											$('#checkedIds').val('');
											$('.chk_al1').prop('checked', false)
											 layer.open({
												type:1,
												title:" ",
												closeBtn:0,
												area:"90%",
												/*offset:"100px",*/
												move:false,
												content:$("#uploadProduct"),
												skin: "s_upload_prd",
												btn: ['保存'],
												yes:function(){
													that.changePeriods(that.periodsId);
													vm.getTableData = !vm.getTableData;
													layer.closeAll();
												}
											})
										});
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
						if($(".table-container tbody tr").length < 0) {
							layer.msg('没有数据可以下载')
							return false;
						}
						var ids = '', checkedItem;
						checkedItem = eval('('+$("#checkedIds").val()+')');
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
						if($(".table-container tbody tr").length < 0) {
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
						var ids = [], checkedItem;
						if(type == 'uploadList'){
							checkedItem = eval('('+$("#checkedIds").val()+')');
						}else if(type == 'productList'){							
							checkedItem = that.selectList;							
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
							multiDelete(ykyUrl.product + "/v1/activities/" + that.activityId + "/periods/" + that.periodsId + "/products", 'DELETE', ids,
									function(res, err) {//页面加载前调用方法
										if(err == null && type == "productList"){
											if(that.prdList.length==1){
												syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });
											}else{
												that.changePeriods(that.periodsId);
											}
										}else if(err == null && type == "uploadList"){
											vm.queryParamsAll.defaultStatus = !vm.queryParamsAll.defaultStatus;
										}
									});
							layer.close(index);
						})
					}
				},
			});	
	 
	 var getDataList = function(processId){
			vm.getTableData = true;
			vm.url = ykyUrl.product + '/v1/activities/'+vm.activityId+'/products/draft',
			vm.queryParamsAll = { //请求接口参数
		            defaultStatus: true, //监测参数变化标识
		            periodsId: vm.periodsId,
		            size: 10,
		            page: 1,
		            processId:processId
		        };
			vm.gridColumns = [
			    {key :'selected', name: '',width: '50px'},
			    {key: 'index', name: '序号',width: '50px'},    
			    {key: 'manufacturer', name: '制造商', cutstring: true,textColor:{
			    	condition: "'{status}'=='UNABLE'",
			    	color: 'red'
			    }},
			    {key: 'manufacturerPartNumber', name: '型号', width: '180px', cutstring: true, textColor:{
			    	condition: "'{status}'=='UNABLE'",
			    	color: 'red'
			    }},
			    {key: 'vendorName', name: '分销商', cutstring: true,textColor:{
			    	condition: "'{status}'=='UNABLE'",
			    	color: 'red'
			    }},
			    {key: 'totalQty', name: '库存', width:'90px', cutstring: true},
			    {key: 'sourceName', name: '仓库', cutstring: true,textColor:{
			    	condition: "'{status}'=='UNABLE'",
			    	color: 'red'
			    }},
			    {key: 'currencyUomId', name: '币种', width: '50px', callback:{
					action : 'changeCurrency',
					params : ['{currencyUomId}']
				}},
			    {key: 'qtyBreak', name: '阶梯', endString: '+', type: 'array',width:'100px',textColor:{
			    	condition: "'{status}'=='UNABLE'",
			    	color: 'red'
			    }},
			    {key: 'priceBreak', name: '限时价', type:'array',textColor:{
			    	condition: "'{status}'=='UNABLE'",
			    	color: 'red'
			    }},
			    {key: 'desc', name: '备注', cutstring: true, textColor:{
			    	condition: "'{status}'=='UNABLE'",
			    	color: 'red'
			    }},
			    {
					key : 'operate',
					name : '操作',
					align : 'center',
					width: '60px',
					items : [ {
						className : 'btn-delete',
						text : '删除',
						show : true,
						callback : {
							confirm : {
								title : '删除',
								content : '确认删除？'
							},
							action : 'deleteItem',
							params : [ '{activityProductId}', vm.activityId, vm.periodsId]
						}
					} ]
				}
			    
			    ]
			vm.checkflag = true;
			vm.pageflag = false;
			vm.refresh = true;
			
			vm.changeCurrency = function(){
				console.log(1111)
			}
		};

	if (vm.activityId) {//获取草稿活动详情
		syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });
		//syncData('http://operation-sit.yikuyi.com/services-product/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });//sit环境
	}
	$(".btn-concle").on("click",function(){
		if(vm.from){
			window.location.href = operationWebUrl + "/timepromotion/information/"+vm.activityId+".htm?from=maintain"; 
		}else{
			window.location.href = operationWebUrl + "/timepromotion/upload/"+vm.activityId+".htm"; 
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

var deleteItem = function(index, params){
	syncData(ykyUrl.product + "/v1/activities/"+params[0]+"/delete?activityId="+params[1]+"&periodsId="+params[2], 'DELETE', null, function(res,err) {
		if (!err) {
			layer.msg('删除成功');
			vm.queryParamsAll.defaultStatus = !vm.queryParamsAll.defaultStatus;
		}
	})
};

function changeCurrency(index,e){
	if(e[0]){
		var upperStr = e[0].toUpperCase();
		
		if(upperStr === 'CNY'){
			upperStr = 'RMB';
		}
		return upperStr;
	}	
};
