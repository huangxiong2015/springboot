var vm
$(function() {
	var valid = false;
	 vm = new Vue(
			{
				el : "#activityInformation",
				data : {
					initData:{
						createType:'CUSTOM',//创建方式  CUSTOM:定制开发 , TEMPLATE:模板创建
						startDate:'',//促销开始时间 
						endDate:'',//促销结束时间
						faceImgUrl:'',//活动封面图片地址URL
						promotionId:$('#promotionId').val(),//活动促销id
						promotionName:'',//促销名称
						promotionUrl:'',//活动链接url
						isUseCoupon:'Y',//是否使用代金券
						seoTitle:'',
						seoKeyword:'',
						seoDescription:'',
						promotionType:'ORDINARY', //活动类型
						promotionContent:{}
					},
					recommendContent:{
						productId:'',
						productUrl:'',
						productQuantity:'',
						cartType:[],
						productMpn:'',
						productCateName:'',
						moq:''
					},
					couponList:[],
					temporaryCouponList:[],
					selectCouponList:[],
					newsHome: operationWebUrl + "/promotion.htm",
					seoField: [{
					        	   label:'title',
					        	   name:'seoTitle',
					        	   placeholder:'多词间用，分开',
					        	   maxLength:50
					        	   },
				        	   {
					        	   label:'keyword',
					        	   name:'seoKeyword',
					        	   placeholder:'多词间用，分开',
					        	   maxLength:100
					        	   },
				        	   {
					        	   label:'description',
					        	   name:'seoDescription',
					        	   placeholder:'多词间用，分开',
					        	   maxLength:200
					        	   }]
				},
				created:function(){
					var that = this;
					syncData(ykyUrl.pay + "/v1/coupons/couponActivity/findByActivityId", 'GET', null, function(res,err) {//获取优惠券列表
						if (err == null) {
							$.each(res,function(i,e){
								var obj = {
										couponId:e.couponId,
										couponName:e.couponName
								}
								that.couponList.push(obj);
							})
						}
					},false)
					if (this.initData.promotionId) {
						syncData(ykyUrl.product + '/v1/promotions/' + this.initData.promotionId , 'GET', null, function(res, err) {
							if(err == null){
								vm.init(res);
							}
						});		
					}else{
						this.$nextTick(function(){
							setDate();							
					   });
					}
				},
				methods : {
					init : function(res) {
						var that = this;
						res.startDate = new Date(res.startDate).Format('yyyy-MM-dd');
						res.endDate = new Date(res.endDate).Format('yyyy-MM-dd');
						this.initData = $.extend({},this.initData,res);
						this.recommendContent = $.extend({},this.recommendContent,this.initData.promotionContent);
						if(res.couponIds){
							var arr = res.couponIds.split(',');
							$.each(arr,function(i,e){
								$.each(that.couponList,function(idx,ele){
									if(e == ele.couponId){
										that.selectCouponList.push(ele);
										return false
									}
								})
							})
						}
						that.temporaryCouponList = that.selectCouponList;
						that.$nextTick(function(){
							setDate();
							$(".startDates.form_datetime").datetimepicker('setEndDate',res.endDate);
							$(".endDates.form_datetime").datetimepicker('setStartDate',res.startDate);
					   });
					},
					saveData : function() {//保存数据					
						$('#create').submit();						
					},				
					cancelClick:function(){
						location.href = ykyUrl._this + '/promotion.htm';
					},
					selectCoupon:function(){
						$('.coupon-par').addClass('show');
					},
					closeCouponList:function(type){
						$('.coupon-par').removeClass('show');
						if(type == 'save'){
							this.selectCouponList = this.temporaryCouponList;
						}else if(type == 'cancel'){
							this.temporaryCouponList = this.selectCouponList;
						}
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
					getProductData: function(){
						var that = this,
							recommendContent = this.recommendContent,
							url = recommendContent.productUrl,
							prdId = getProductId(url, 'name');
						this.recommendContent.productId = prdId;
						var data = [prdId]
						syncData(ykyUrl.product + '/v1/products/batch/basic', 'POST', data, function(res, msg) {
							if(!res.length){
								recommendContent.moq = '';
								recommendContent.productId = '';
								recommendContent.productMpn = '';
								recommendContent.productCateName = '';
								layer.msg("未关联到商品相关信息!",{
									skin:"c_tip"
								})
								return;
							}else{
								var spu = res[0].spu,
									cate = [],
									cateObj = {};
								if(spu.categories && spu.categories.length){
									for(var i = 0;i<spu.categories.length;i++){
										var cateLevel = spu.categories[i].cateLevel;
										cateObj[cateLevel] = spu.categories[i].cateName;
									}
								}
								for(var k in cateObj){
									cate.push(cateObj[k])
								}
								cate = cate.join(' > ');
								recommendContent.productMpn = spu.manufacturerPartNumber;
								recommendContent.productCateName = cate;
								recommendContent.moq = res[0].minimumQuantity;
							}							
							});
					}
				},
				
			});
	 $("#promotionName").on("focus",function(){
		 if($(this).val()=='' && !$(this).parent().hasClass('has-error')){			
			 $(".ipt-tips").removeClass("dn");
		 }else if($(this).val()=='' && $(this).parent().hasClass('has-error')){
			 $(".ipt-tips").addClass("dn");
			 $(".ipt-vail").removeClass("dn");
		 }
	 })
	 $("#promotionName").on("blur",function(){
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
		    	var startDate = $('#startDate').val();
			    $(".endDates.form_datetime").datetimepicker('setStartDate',startDate);
			    vm.initData.startDate = startDate;	
			    if(startDate){
			    	$('#startDate').valid()
			    }
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
		    	var endDate = $('#endDate').val();
		        $(".startDates.form_datetime").datetimepicker('setEndDate',endDate);
		        vm.initData.endDate = endDate;
		        if(endDate){
		        	$('#endDate').valid()
			    }
		    });
	 }

	

	var uploader = createUploader({
		buttonId: "uploadBtn", 
		uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "5mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.initData.faceImgUrl = signatureData.image.replace(/http\:|https\:/,"");
					
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	
	uploader.init();
	
	formValidate('#create', {}, function(){
		if(vm.initData.promotionType == 'RECOMMEND' && !vm.recommendContent.cartType.length){
			layer.msg('请选择购物车类别');
			return;
		}
		if(vm.initData.promotionType =='ORDINARY' && !vm.initData.faceImgUrl){
			layer.msg('请上传活动封面！')
			return;
		}
		if(vm.initData.promotionType =='ORDINARY' && vm.initData.createType == 'TEMPLATE' && vm.initData.isUseCoupon == 'Y' && vm.selectCouponList.length==0){
			layer.msg('请选择优惠券');
			return;
		}
		var formArry = $('#create').serializeObject();	
		formArry.faceImgUrl = vm.initData.faceImgUrl;
		formArry.promotionId = vm.initData.promotionId;
		if(vm.initData.isUseCoupon == 'Y' && vm.initData.createType == 'TEMPLATE'){
			var arr = [];
			$.each(vm.selectCouponList,function(i,e){
				arr.push(e.couponId);
			})
			formArry.couponIds = arr.join(',');
		}else{
			formArry.isUseCoupon = 'N';
			formArry.couponIds = '';
		}
		if(vm.initData.promotionType == 'ORDINARY'){
			delete formArry.promotionContent;
		}else{
			formArry.createType = ''; //活动类型为购物车推荐时创建方式传''
		}
		var url = ykyUrl.product + '/v1/promotions',
			type = vm.initData.promotionId ? 'PUT' : 'POST';
		syncData(url,type,formArry,function(res,msg){
			if(msg == null){
				location.href = ykyUrl._this + '/promotion.htm';
			}
		})
	});
	
});