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
				        type:'10001',//活动类型
				        startDates:'',//活动开始时间
				        endDates:'',//活动结束时间
				        periodsList:[{"startTime": "","endTime": ""}],
				        promoDiscountStatus:'Y',//是否打折
				        promoDiscount:'',//折扣
				        isSystemQty:'N',//是否使用系统库存数
				        iconStatus:'Y',//是否显示促销标志      
				        iconScenes:{LIST:'',DETAIL:'',CART:''}//促销标志场景及图片路径
					},
					datas:{},
					activityId : $("#activityId").val(),
					from : getQueryString('from'),
					newsHome: operationWebUrl + "/timepromotion.htm",
					num:['一','二','三','四','五','六','七','八','九','十'],
					today:'',
					nowTime:'',
					couponList:[],//优惠券列表
					selectCouponList:[],//选中的优惠券列表
					temporaryCouponList:[],//选中的优惠券临时列表
					status:'VALID',//是否使用优惠券
					scenesList:['LIST'],
					iconUrlList:{LIST:'',DETAIL:'',CART:''},
					scenesObj:{LIST:'列表页',DETAIL:'详情页',CART:'列表弹窗'}
					
				},
				created: function(){
					var that = this;
					that.getCouponList('');
					if(that.activityId){
						that.getCouponList(that.activityId);
					}
				},
				mounted:function(){
					var n=0;
					for(var k in this.iconUrlList){
						this.uploadImgInit(k,n);
						n++;
					}
					
				},
				watch:{
					'initData.iconStatus':function(val,oldVal){
						var that = this;
						that.scenesList = ['LIST'];
						that.iconUrlList = {LIST:'',DETAIL:'',CART:''};
					},
					scenesList:function(val,oldVal){
						var that = this;
						var obj = {LIST:'',DETAIL:'',CART:''};
						$.each(val,function(i,e){
							obj[e] = that.iconUrlList[e];
						})
						that.iconUrlList = obj;
					}
				},
				methods : {
					init : function(res) {
						var that = this;
						var initData = that.initData;
						that.getDate();
						that.datas = res;
						initData.activityId = res.activityId;
						initData.name = res.name;
						initData.type = res.type;
						initData.startDates = res.startDates;
						initData.endDates = res.endDates;
						initData.promoDiscountStatus = res.promoDiscountStatus == 'Y' ? 'Y':'N';
						initData.promoDiscount = res.promoDiscount?res.promoDiscount:'';
						initData.isSystemQty = res.isSystemQty == 'Y' ? 'Y' : 'N';
						initData.iconStatus = res.iconStatus == 'Y' ? 'Y' : 'N';
						initData.iconScenes = res.iconScenes;
						that.iconUrlList = res.iconStatus == 'Y' && initData.iconScenes ? JSON.parse(initData.iconScenes):{LIST:'',DETAIL:'',CART:''};
						that.scenesList = [];
						$.each(that.iconUrlList,function(key,val){
							if(val){
								that.scenesList.push(key);
							}
						})
						if(that.activityId && (!that.from || that.from=='maintain')){
							that.initData.periodsList = [];
							$.each(res.periodsList,function(index,ele){
								if(ele.status == 'ENABLE'){
									that.initData.periodsList.push(ele);
								}
							})
						}else{
							that.initData.periodsList = $.extend(that.initData.periodsList,res.periodsList);
						}
						that.$nextTick(function(){
							setDate();
							$(".startDates.form_datetime").datetimepicker('setEndDate',res.endDates);
							$(".endDates.form_datetime").datetimepicker('setStartDate',res.startDates);
							$.each($(".time-range input"),function(){
								$(this).val(that.today + ' ' + $(this).val());
							})
							this.setTime();
							$.each($(".time-range input"),function(){
								var time = $(this).val().slice(11,$(this).val().length)
								$(this).val(time);
							})
					   });
					},
					uploadImgInit:function(key,idx){
						var that = this;
						createUploader({
							buttonId: "uploadIcon" + idx, 
							uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
							url:  ykyUrl.webres,
							types: "jpg,png,jpeg,gif",//可允许上传文件的类型
							fileSize: "5mb", //最多允许上传文件的大小
							isImage: true, //是否是图片
							init:{
								FileUploaded : function(up, file, info) { 
						            layer.close(index);
									if (info.status == 200 || info.status == 203) {
										that.iconUrlList[key] = signatureData.image.replace(/http\:|https\:/,"");
										if(!isInclude(key)){
											that.scenesList.push(key);
										}
									} else {
										layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
									}
									up.files=[];
								}
							}
						}).init();
					},
					saveData : function(event) {
						var that = this;
						var initData = that.initData;
						var flag = that.timeValidate();
						if(!flag){
							return;
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
						var url;
						if(that.activityId){
							url = ykyUrl.product + "/v1/activities/"+vm.activityId+"/draft";							
						}else{
							url = ykyUrl.product + "/v1/activities/draft";
						}
						if(that.from){
							that.initData.from = that.from;
						}
						syncData(
								url,
								"POST",
								that.initData,
								function(data, err) {
									if(err==null){
										that.initData.activityId = data.activityId;
										that.saveCouponData();
									}else if(err=='活动名称有重叠'){
										$("#name").addClass("error_bor");
									}
								});
					},
					deleteTime : function(idx){//删除时间段
						var that = this;
						var index = layer.confirm('确认删除？',{
							 btn: ['确认','取消'] //按钮
						},function(){
							that.initData.periodsList.splice(idx,1);
							layer.close(index);
						})
					},
					addTime : function(){//新增时间段						
						var that = this;
						var obj;
						if(that.activityId && that.from=='upload'){
							obj = {startTime:'',endTime:'',activityId:that.activityId};
						}else{
							obj = {startTime:'',endTime:''};
						}
						that.initData.periodsList.push(obj);
						that.$nextTick(function(){
						   that.setTime();
					   });
					},
					getDate : function(){
						var that = this;
						var now = new Date, 
							year = now.getFullYear(),
							month = (now.getMonth()+1)<10 ? '0' + (now.getMonth()+1):(now.getMonth()+1),
							date = now.getDate()<10 ? '0' + now.getDate():now.getDate(),
							hour = now.getHours()<10 ? '0' + now.getHours():now.getHours(),
							minute = now.getMinutes()<10 ? '0' + now.getMinutes():now.getMinutes();
							that.today = year + '-' + month + '-' + date;
			        		that.nowTime = hour + ':' + minute;
					},
					setTime:function(){//时间插件初始化
						var that = this;						
						$(".startTime.form_datetime").unbind(".plugin");//方法移除，防止多次进入change事件
						$(".endTime.form_datetime").unbind(".plugin");
						$('.startTime.form_datetime').datetimepicker({
							language:  config.language, 
							startView: 1,
						    minView: 'hour',   //设置只显示到小时
					        format: 'hh:ii',
					        minuteStep: 30,
					        autoclose: true
					    }).on('change.plugin',function(ev){
					    	that.startTimeChange(this);					        
				        });
						 //	时间控件绑定选择面板弹出 
						$('.endTime.form_datetime').datetimepicker({
							language: config.language,				            
				            startView: 1,
				            minView: 'hour',
				            format: 'hh:ii',
				            minuteStep: 30,
				            autoclose: true,
					    }).on('change.plugin',function(ev){
					        that.endTimeChange(this);
					    });
					},
					startTimeChange : function(dom){//时间段开始change事件
						var that = this;
						var dom = $(dom).find('input');
						var errorTips = '<p class="error"><span class="error_tips">*活动时段冲突</span></p>';
						that.getDate();
				        var obj = {
				        		startTime:$(dom).val(),
				        		endTime:$(dom).parents('.input-daterange').find('input.endTime').val(),
				        		preStartTime:$(dom).parents('tr').prev('tr').length?$(dom).parents('tr').prev('tr').find('input.startTime').val():undefined,
				        		preEndTime:$(dom).parents('tr').prev('tr').length?$(dom).parents('tr').prev('tr').find('input.endTime').val():undefined,
				        		endDates:that.initData.endDates,				        							        
				        }
				        var flag = that.timeComparison(dom,obj);
				        if(!flag){
				        	return;
				        }
				        if(obj.endDates == that.today && obj.startTime<that.nowTime){
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].startTime = '';
				        	layer.tips('开始时间需大于当前时间', $(dom),{					        		
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return;
				        }
				        if(obj.endTime && obj.startTime>=obj.endTime){//开始时间需小于结束时间
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].startTime = ''
				        	layer.tips('开始时间需小于结束时间', $(dom),{
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return;
				        }
				        if(obj.preEndTime && obj.startTime<obj.preEndTime){//开始时间需大于上时段的结束时间
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].startTime = ''
				        	if(!$(dom).parents('.input-daterange').find('.error').length){
				        		$(dom).parents('.input-daterange').append(errorTips);
				        	}
				        	return;
				        }else{
				        	$(dom).parents('.input-daterange').find('.error').remove();
				        	$(dom).parents('tr').prev('tr').find('.error').remove();
				        }
				        that.initData.periodsList[$(dom).data('index')].startTime = obj.startTime;
				        if(obj.startTime){
				        	$(dom).removeClass("error_bor")
				        }
					},
					endTimeChange : function(dom){//时间段结束change事件
						var that = this;
						var dom = $(dom).find('input');
						var errorTips = '<p class="error"><span class="error_tips">*活动时段冲突</span></p>';
						that.getDate();
				        var obj = {
				        		endTime:$(dom).val(),
				        		startTime:$(dom).parents('.input-daterange').find('input.startTime').val(),
				        		preStartTime:$(dom).parents('tr').prev('tr').length?$(dom).parents('tr').prev('tr').find('input.startTime').val():undefined,
				        		preEndTime:$(dom).parents('tr').prev('tr').length?$(dom).parents('tr').prev('tr').find('input.endTime').val():undefined,
				        		nextStartDate:$(dom).parents('tr').next('tr').length?$(dom).parents('tr').next('tr').find('input.startTime').val():undefined,
				        		endDates:that.initData.endDates,
				        }
				        var flag = that.timeComparison(dom,obj);
				        if(!flag){
				        	return;
				        }
				        if(obj.endDates == that.today && obj.endTime<that.nowTime){
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].endTime = ''
				        	layer.tips('结束时间需大于当前时间', $(dom),{					        		
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return;
				        }					        
				        if(obj.startTime && obj.startTime>=obj.endTime){//结束时间需大于开始时间
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')].endTime = ''
				        	layer.tips('结束时间需大于开始时间', $(dom),{
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return;
				        }
				        if(obj.preEndTime && obj.endTime<=obj.preEndTime){//结束时间需大于上时段的结束时间
				        	that.initData.periodsList[$(dom).data('index')].endTime = obj.endTime;
				        	if(!$(dom).parents('.input-daterange').find('.error').length){
				        		$(dom).parents('.input-daterange').append(errorTips);
				        	}			        		
			        		return;
				        }
				        if(obj.nextStartDate && obj.endTime>obj.nextStartDate){//结束时间需小于下时段的开始时间
				        	that.initData.periodsList[Number($(dom).data('index'))+1].startTime = ''
				        	if(!$(dom).parents('tr').next('tr').find('.error').length){
				        		$(dom).parents('tr').next('tr').find('.input-daterange').append(errorTips);
				        	}else{
				        		$(dom).parents('tr').next('tr').find('.error_tips').text("*活动时段冲突");
				        	}
				        }
				        that.initData.periodsList[$(dom).data('index')].endTime = obj.endTime;
				        $(dom).siblings('.error').remove();
				        if(obj.endTime){
				        	$(dom).removeClass("error_bor")
				        }
					},
					timeComparison : function(dom,obj){//时间段比较
						var that = this;
						var type = $(dom).data('type')
						if(!obj.endDates){
							$(dom).val('');
							that.initData.periodsList[$(dom).data('index')][type] = ''
				        	layer.tips('请选择活动时间', $(dom),{					        		
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return false
				        }
				        if(obj.preStartTime == '' || obj.preEndTime == ''){//上一个时段不能为空
				        	$(dom).val('');
				        	that.initData.periodsList[$(dom).data('index')][type] = ''
				        	layer.tips('请完善上一个时间段', $(dom),{					        		
	  		       				tips: 1,
	  		     				time: 1000,
	  		       			});
				        	return false
				        }
				        return true;
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
						if(!flag){
							return false;
						}
						that.getDate();
						var errorTips = '<p class="error over_tips"><span class="error_tips">*当前时间已过期，请重新输入有效时间</span></p>';
						if(that.initData.endDates==that.today){
							$.each(that.initData.periodsList,function(index,ele){
								if(ele.endTime<that.nowTime || ele.startTime<that.nowTime){
									$("#endTime" + index).parents('.input-daterange').append(errorTips);
									flag = false;
								}
							})
						}
						if(!flag){
							return false;
						}else{
							return true;
						}
					},
					getToUrl:function(){
						var that = this;
						if((that.activityId && !that.from) || (that.activityId && that.from=='maintain')){
							location.href = ykyUrl._this + '/timepromotion/maintain/' + that.initData.activityId + '.htm?from=information';
						}else{
							location.href = ykyUrl._this + '/timepromotion/upload/' + that.initData.activityId + '.htm';
						}
					},
					getCouponList:function(activityId){
						var that = this;
						var data = {activityId:activityId};
						syncData(ykyUrl.pay + "/v1/coupons/couponActivity/findByActivityId", 'GET', data, function(res,err) {
							if (!err && res !== '' && !activityId) {
								$.each(res,function(i,e){
									var obj = {
											couponId:e.couponId,
											couponName:e.couponName
									}
									that.couponList.push(obj);
								})
							}else{
								if(res.length){
									$.each(res,function(i,e){//优惠券couponList只有couponId、couponName,经过处理才能双向绑定
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
					saveCouponList:function(){//
						var that = this;
						that.selectCouponList = that.temporaryCouponList;
						$('.coupon-par').removeClass('show');
					},
					cancelCouponList:function(type){
						var that = this;
						if(type == 'cancel'){
							that.temporaryCouponList = that.selectCouponList;
						}
						$('.coupon-par').removeClass('show');
					},
					deleteCoupon:function(item){
						var that = this;
						var cId = item.couponId;
						$.each(that.selectCouponList,function(i,e){
							if(e.couponId == cId){
								that.selectCouponList.splice(i,1);
								return false;
							}
						})
					},
					saveCouponData:function(){
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
								that.getToUrl();
							}
						})
					},
					discountVali:function(e){
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
					deleteIcon:function(key){
						var that = this;
						that.iconUrlList[key] = '';
					},
					showUploadBtn:function(key){
						var that = this;
						var bool = false;
						$.each(that.scenesList,function(i,e){
							if(e==key){
								bool = true;
								return false;
							}
						})
						return bool
					}
				}
				
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
		    	vm.getDate();
		    	var endDate = $('#endDates').val();
		        $(".startDates.form_datetime").datetimepicker('setEndDate',endDate);
		        vm.initData.endDates = endDate;
		        if(endDate>vm.today){
		        	$("p.over_tips").remove();
		        }
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
		vm.setTime();
		setDate();
	}
	if (vm.activityId && vm.from) {//获取草稿活动详情
		syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/draft' , 'GET', null, function(res, msg) {vm.init(res); });		
	}else if (vm.activityId) {//获取正式活动详情
		syncData(ykyUrl.product + '/v1/activities/'+vm.activityId+'/standard', 'GET', null, function(res, msg) {vm.init(res); });
	}
	$('.btn-concle').on('click', function(){		
		window.location.href = operationWebUrl + "/timepromotion.htm"; 					
		});
	$('.close-box').on('click',function(){
		$('.coupon-par').removeClass('show');
	})
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
	//uploader0.init();
	//uploader1.init();
	//uploader2.init();
});