var vm
$(function() {
	var valid = false;
	 vm = new Vue(
			{
				el : "#activityInformation",
				data : {
					initData : {
						activityId:'',//活动id
						name:'',//活动名称
				        type:'10000',//活动类型
				        startDates:'',//活动开始时间
				        endDates:'',//活动结束时间
				        periodsList:[{"startTime": "00:00","endTime": "24:00"}],
				        promoDiscountStatus:'Y',//是否打折
				        promoDiscount:'',//折扣
				        isSystemQty:'N',//是否使用系统库存数
				        iconStatus:'Y',//是否显示促销标志      
				        iconScenes:{LIST:'',DETAIL:'',CART:''},//促销标志场景及图片路径
				        specifySupplier:'false',//是否指定供应商
				        supplierRule:''
					},
					activityId : $("#activityId").val(),
					from : getQueryString('from'),
					newsHome: operationWebUrl + "/shoppromotion.htm",
					couponList:[],//优惠券列表
					selectCouponList:[],//选中的优惠券列表
					temporaryCouponList:[],//选中的优惠券临时列表
					status:'VALID',//是否使用优惠券
					scenesList:['LIST'],
					iconUrlList:{LIST:'',DETAIL:'',CART:''},
					scenesObj:{LIST:'列表页',DETAIL:'详情页',CART:'列表弹窗'},
					activeSupplier:[],
					temporarySupplier:[],
					letterSelect:{
						keyname:'选择供销商',//字段名
						validate:{
		                    "required": true
		                },
		                id:'selbox',
		                name:'selbox',
		                options:[],//数据列表
		                optionId: 'id',//数据id字段
		                optionName: 'supplierName',//数据名称字段
		                selected:[],//默认选中
		                placeholder:'搜索供销商'//输入框提示
					}
					
				},
				created: function(){
					var that = this;
					that.getCouponList('');//获取优惠券列表
					if(that.activityId){
						that.getCouponList(that.activityId);//获取当前活动优惠券列表
					}
					that.getSupplierList();//获取供应商列表
				},
				watch:{
					'initData.iconStatus':function(val,oldVal){//是否显示促销标志
						var that = this;
						that.scenesList = ['LIST'];
						that.iconUrlList = {LIST:'',DETAIL:'',CART:''};
					},
					scenesList:function(val,oldVal){//促销标志显示页面列表
						var that = this;
						var obj = {LIST:'',DETAIL:'',CART:''};
						$.each(val,function(i,e){
							obj[e] = that.iconUrlList[e];
						})
						that.iconUrlList = obj;
					},
					'initData.specifySupplier':function(val,oldVal){//是否按分销商添加
						var that = this;
						if(val == 'true'){
							that.initData.isSystemQty = 'Y';
							that.initData.promoDiscountStatus = 'Y';
							that.activeSupplier = [];
						}else if(val == 'false'){
							that.initData.isSystemQty = 'N';
						}
					}
				},
				methods : {
					init : function(res) {
						var that = this;
						var initData = that.initData;
						initData.activityId = res.activityId;
						initData.name = res.name;
						initData.type = res.type;
						initData.startDates = res.startDates;
						initData.endDates = res.endDates;
						initData.periodsList = res.periodsList;
						initData.promoDiscountStatus = res.promoDiscountStatus == 'Y' ? 'Y':'N';
						initData.promoDiscount = res.promoDiscount?res.promoDiscount:'';
						initData.isSystemQty = res.isSystemQty == 'Y' ? 'Y' : 'N';
						initData.iconStatus = res.iconStatus == 'Y' ? 'Y' : 'N';						
						initData.iconScenes = res.iconScenes;
						initData.specifySupplier = res.specifySupplier ? 'true' : 'false';						
						that.iconUrlList = res.iconStatus == 'Y' && initData.iconScenes ? JSON.parse(initData.iconScenes):{LIST:'',DETAIL:'',CART:''};
						that.scenesList = [];
						$.each(that.iconUrlList,function(key,val){
							if(val){
								that.scenesList.push(key);
							}
						})
						var selectSupplier = res.supplierRule ? JSON.parse(res.supplierRule) : '';
						var selectSupplierArr = []
						$.each(selectSupplier,function(idx,ele){
							$.each(that.letterSelect.options,function(i,e){
								if(ele.supplierId == e.id){
									selectSupplierArr.push(e);
								}
							})
						})
						that.$nextTick(function(){
							setDate();
							$(".startDates.form_datetime").datetimepicker('setEndDate',res.endDates);
							$(".endDates.form_datetime").datetimepicker('setStartDate',res.startDates);	
							that.activeSupplier = selectSupplierArr;
					   });
					},
					saveData : function(nextPage) {//保存数据
						var that = this;
						var initData = that.initData;
						var flag = that.timeValidate();
						if(!flag){
							return;
						}
						var url,type;
						if(nextPage == 'list'){
							if(that.activityId){
								url = ykyUrl.product + "/v1/activities/"+vm.activityId;	
								type = 'PUT';
							}else{
								url = ykyUrl.product + "/v1/activities";
								type = 'POST';
							}
						}else{
							type = 'POST';
							if(that.activityId){
								url = ykyUrl.product + "/v1/activities/"+vm.activityId+"/draft";							
							}else{
								url = ykyUrl.product + "/v1/activities/draft";
							}
						}
						
						if(that.status == 'VALID'&& that.selectCouponList.length==0){
							layer.msg('请选择优惠券');
							return;
						}
						if(initData.promoDiscountStatus == 'Y' && $("#promoDiscount").hasClass('error_bor')){
							layer.msg('请输入有效折扣');
							return;
						}else if(initData.promoDiscountStatus == 'N'){
							initData.promoDiscount = '';
						}
						if(initData.specifySupplier == 'true' && !that.activeSupplier.length){
							layer.msg('请选择活动分销商');
							return;
						}else if(initData.specifySupplier == 'true' && that.activeSupplier.length){
							var supplierArr = [];
							$.each(that.activeSupplier,function(i,e){
								var obj = {};
								obj['supplierId'] = e.id;
								obj['supplierName'] = e.supplierName;
								supplierArr.push(obj);
							})
							initData.supplierRule = JSON.stringify(supplierArr);
						}
						if(initData.iconStatus == 'Y' && !that.scenesList.length){
							layer.msg('请选择图标显示页面');
							return;
						}
						var iconFlag = false;
						if(initData.iconStatus == 'Y'){
							$.each(that.scenesList,function(index,ele){
								var msg = '请上传' + that.scenesObj[ele] + '促销图标'
								if(!that.iconUrlList[ele]){
									layer.msg(msg);
									iconFlag = true
									return false;
								}
							})
						}
						if(iconFlag){
							return;
						}
						initData.iconScenes = JSON.stringify(that.iconUrlList);					
						
						if(that.from){
							that.initData.from = that.from;
						}
						syncData(
								url,
								type,
								that.initData,
								function(data, err) {
									if(err==null){
										that.initData.activityId = data.activityId;
										that.saveCouponData(nextPage);
									}else if(err=='活动名称有重叠'){
										$("#name").addClass("error_bor");
									}
								});
					},
					timeValidate : function(){
						var that = this;
						var flag = true;
						var input = $('.box-body input[type="text"]')
						$.each(input,function(index,ele){
							if($(this).val()==''){
								$(this).addClass('error_bor');
								if($(this).attr('id')=='name'){
									$('.ipt-vail').removeClass('dn');
								}
								flag = false;
							}
						})
						if(flag){
							return true;
						}else{
							return false;
						}
					},
					getToUrl:function(flag){//跳转至下一页面
						var that = this;
						if(flag == 'list'){//选择了分销商保存后跳转到列表页
							location.href = ykyUrl._this + '/shoppromotion.htm';
						}else{
							if((that.activityId && !that.from) || (that.activityId && that.from=='maintain')){
								location.href = ykyUrl._this + '/shoppromotion/maintain/' + that.initData.activityId + '.htm?from=information';
							}else{
								location.href = ykyUrl._this + '/shoppromotion/upload/' + that.initData.activityId + '.htm';
							}
						}
					},
					getCouponList:function(activityId){//获取优惠券列表
						var that = this;
						var data = {activityId:activityId};
						syncData(ykyUrl.pay + "/v1/coupons/couponActivity/findByActivityId", 'GET', data, function(res,err) {
							if (!err && res !== '' && !activityId) {//获取所有优惠券列表
								$.each(res,function(i,e){
									var obj = {
											couponId:e.couponId,
											couponName:e.couponName
									}
									that.couponList.push(obj);
								})
							}else{//获取当前活动优惠券列表
								if(res.length){
									$.each(res,function(i,e){//优惠券couponList只有couponId、couponName
										var obj = {
												couponId:e.couponId,
												couponName:e.couponName
										}
										that.selectCouponList.push(obj);
									})
									that.temporaryCouponList = that.selectCouponList;
								}else{
									that.status = 'NOT_VALID';
								}								
							}
						})
					},
					selectCoupon:function(){//选择优惠券
						var that = this;
						$('.coupon-par').addClass('show');						
					},
					saveCouponList:function(){//保存优惠券选择
						var that = this;
						that.selectCouponList = that.temporaryCouponList;
						$('.coupon-par').removeClass('show');
					},
					cancelCouponList:function(type){//取消选择优惠券
						var that = this;
						if(type == 'cancel'){
							that.temporaryCouponList = that.selectCouponList;
						}
						$('.coupon-par').removeClass('show');
					},
					deleteCoupon:function(item){//删除优惠券
						var that = this;
						var cId = item.couponId;
						$.each(that.selectCouponList,function(i,e){
							if(e.couponId == cId){
								that.selectCouponList.splice(i,1);
								return false;
							}
						})
					},
					saveCouponData:function(flag){//优惠券保存请求
						var that = this;
						var couponIds = [];
						if(that.status == 'VALID'){
							$.each(that.selectCouponList,function(i,e){
								couponIds.push(e.couponId);
							})
						}
						var data = {
								activityId:that.initData.activityId,
								activityName:that.initData.name,
								couponIds:couponIds,
								status:that.status
								};
						var par = $.param(data)
						syncData(ykyUrl.pay + "/v1/coupons/couponActivity/add", 'POST', data, function(res,err) {
							if (!err && res !== '') {
								that.getToUrl(flag);
							}
						})
					},
					discountVali:function(e){//折扣校验
						var that = this;
						var reg = /^0\.(0[1-9]|[1-9]{1,2})$/;
						var target = e.currentTarget;						
						var value = $.trim(target.value);
						if(!reg.test (Number(value))){
							$(target).addClass('error_bor');
						}else{
							$(target).removeClass('error_bor');
							that.initData.promoDiscount = Number(value);
						}
					},
					deleteIcon:function(key){//删除促销标志
						var that = this;
						that.iconUrlList[key] = '';
					},
					getSupplierList:function(){//获取供应商列表
						var that = this;
						var data = {
									roleType: "SUPPLIER",
									status: "PARTY_ENABLED"
									}
						syncData(ykyUrl.party +"/v1/party/allparty",'PUT',data,function(res,err){
							if(err == null){
								$.each(res,function(i,e){
									if(e.partyGroup.groupName){
										e.supplierName = e.partyGroup.groupName;
									}
								})
								that.letterSelect.options = res;
							}							
						},false)
					},
					getSelected:function(obj){//选择分销商返回选中数据
						this.temporarySupplier = [];
						this.temporarySupplier.push(obj);
					},
					showSupplierList:function(){//显示分销商弹窗
						var that = this;
						that.letterSelect.selected = [];
						$.each(that.activeSupplier,function(i,e){
							that.letterSelect.selected.push(e.id);
						})
						vm.$refs.letter.letterReload();//字母组件默认选中全部、输入框清空
						layer.open({
							type: 1,
							title: '请选择分销商',
							area: ['800px','300px'],
							offset: '300px',
							move: false,
							skin: "s_select_data",
							content: $("#supplierWrap"),
							btn: ['确认', '取消'],
							yes:function(){
								layer.closeAll();
								that.changeActiveSupplier(that.temporarySupplier);								
							},
							cancel:function(){
								
							}
						})
					},
					changeActiveSupplier:function(arr){
						this.activeSupplier = arr;
					}
				},
				
			});
	 $("#name").on("focus",function(){
		 if($(this).val()=='' && $(".ipt-vail").hasClass("dn")){			
			 $(".ipt-tips").removeClass("dn");
		 }
	 })
	 $("#name").on("blur",function(){
		 $(".ipt-tips").addClass("dn");
		 if($(this).val()==''){			 
			 $(".ipt-vail").removeClass("dn");
			 $(this).addClass("error_bor")
		 }else{
			 vm.initData.name = $.trim($(this).val()); 
			 $(".ipt-vail").addClass("dn");
			 $(this).removeClass("error_bor")
		 }
	 })
	 function setDate(){
		 $('.startDates.form_datetime').datetimepicker({
				language: config.language,
				format: 'yyyy-mm-dd',
				minView: 'month',
		        startView: 2,
		        autoclose: true,
		        startDate:new Date,
		    }).on('change',function(ev){
		    	var startDate = $('#startDates').val();
			    $(".endDates.form_datetime").datetimepicker('setStartDate',startDate);
		        vm.initData.startDates = startDate;
		        });
			 //	时间控件绑定选择面板弹出 
			$('.endDates.form_datetime').datetimepicker({
				language: config.language,
				format: 'yyyy-mm-dd',
				minView: 'month',
		        startView: 2,
		        autoclose: true,
		        startDate:new Date,
		    }).on('change',function(ev){
		    	var endDate = $('#endDates').val();
		        $(".startDates.form_datetime").datetimepicker('setEndDate',endDate);
		        vm.initData.endDates = endDate;
		    });
	 }
	 function isInclude(val){
		 var arr = vm.scenesList;
		 var bool = false;
		 $.each(arr,function(i,e){
			 if(e==val){
				 bool = true;
				 return false;
			 }
		 })
		 return bool;
	 }
	if(!vm.activityId && !vm.from){
		setDate();
	}
	
	if (vm.activityId && vm.from) {//获取草稿活动详情
		syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });		
	}else if (vm.activityId) {//获取正式活动详情
		syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/standard', 'GET', null, function(res, msg) {vm.init(res); });
	}
	
	$('.btn-concle').on('click', function(){		
		window.location.href = operationWebUrl + "/shoppromotion.htm"; 					
		});
	var uploader0 = createUploader({
		buttonId: "uploadIcon0", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "5mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.iconUrlList['LIST'] = _yetAnotherFileUrl;
					if(!isInclude('LIST')){
						vm.scenesList.push('LIST');
					}
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	var uploader1 = createUploader({
		buttonId: "uploadIcon1", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "5mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.iconUrlList['DETAIL'] = _yetAnotherFileUrl;
					if(!isInclude('DETAIL')){
						vm.scenesList.push('DETAIL');
					}
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	var uploader2 = createUploader({
		buttonId: "uploadIcon2", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "5mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.iconUrlList['CART'] = _yetAnotherFileUrl;
					if(!isInclude('CART')){
						vm.scenesList.push('CART');
					}
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	uploader0.init();
	uploader1.init();
	uploader2.init();
});