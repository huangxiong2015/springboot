/*
 * by tb.liushuisheng
 * 
 * 待开发:
 * 	1. 初始化读取数据
 *  2. 广告文本处理
 *  3. 过滤sunrayActive中的内容是function的字段
 * 
 * */



var sunrayActive = {
		bannerUrl: '',	//banner图片
		adText: '',	//广告
		serchTips: '',	//搜索栏placeholder
		area1TitleUrl: '',	//区域1头部图片
		area2TitleUrl: '',	//区域2头部图片
		couponArea: {	//优惠券区域
			step1Pic: '',
			step2Pic: '',
			step3Pic: '',
			step1Txt: '',
			step2Txt: '',
			step3Txt: '3.进入闪购活动页使用',
			step3Link: '/ictrade-operation-web/topic/miaosha.htm'
		},
		activeDesText: '',	//活动说明
		seo: {
			title: '',
			keywords: '',
			description: ''
		}
};

//文件上传	
sunrayActive.fileUpInit = function(obj) {
	var uploader = createUploader({
		buttonId: obj.buttonId, 
		uploadType: obj.uploadType,
		url:  ykyUrl.webres,
		types: obj.types || "jpg,jpeg,gif,png",
		fileSize: obj.fileSize || "10mb",
		isImage: true, 
		init:{
			FileUploaded : function() {
				layer.close(index);
				obj.FileUploaded && obj.FileUploaded();
			}
		}
	}); 

	uploader.init();
}

sunrayActive.renderUpload = function(dom, buttonId, uploadedCallback) {
	
	var floatlayer = $('<div class="floatlayer"></div>');
	var bannerUpload = $('<div id="'+ buttonId +'" class="btn-upload">更换图片</div>');
	
	$(dom).append(floatlayer);
	$(dom).append(bannerUpload);
	
	$(dom).on({
		'mouseover': function() {
			floatlayer.show();
			bannerUpload.show();
		},
		'mouseout': function() {
			floatlayer.hide();
			bannerUpload.hide();
		}
	})
	
	this.fileUpInit({
		buttonId: buttonId,
		uploadType: 'productUpload.activityProductsUpload',
		types: 'jpg,jpeg,gif,png',
		FileUploaded: function(up, file, info) {
			
			uploadedCallback && uploadedCallback.call(dom, _yetAnotherFileUrl);
			
		}
	})
}

//上传banner
sunrayActive.uploadBanner = function() {
	
	this.renderUpload($('.panasonic-active .top_bg'), 'bannerUpload', function(_yetAnotherFileUrl) {
		sunrayActive.bannerUrl = _yetAnotherFileUrl;
			
		/*banner替换*/
		$('.panasonic-active .top_bg').css('background-image', 'url(' + _yetAnotherFileUrl + ')');
	})
	
}

//广告文本编辑
sunrayActive.editAdText = function(){
	
	$('#adText').on('blur', function() {
		sunrayActive.adText = $(this).html();
	})
	
}

//搜索placeholder
sunrayActive.searchPlaceholder = function() {
	$('.goods_search .editTips').on('click', function() {
		$('#go_condition').focus();
		$('#go_condition').select();
	})
	
	$('#go_condition').on('blur', function() {
		sunrayActive.serchTips = $(this).val();
	})
}

//各区域头部图片上传
sunrayActive.titleImg = function() {
	
	//区域1图片
	this.renderUpload($('.list-title-img'), 'area1HeadUpload', function(_yetAnotherFileUrl) {
		sunrayActive.area1TitleUrl = _yetAnotherFileUrl;
		
		$('.list-title-img').css('background-image', 'url(' + _yetAnotherFileUrl + ')');
	})
	
	
	//区域2图片
	this.renderUpload($('.give-coupon-img'), 'area2HeadUpload', function(_yetAnotherFileUrl) {
		sunrayActive.area2TitleUrl = _yetAnotherFileUrl;
		
		$('.give-coupon-img').css('background-image', 'url(' + _yetAnotherFileUrl + ')');
	})
}

//优惠券步骤图片及文本
sunrayActive.couponStep = function() {
	
	var _this = this;
	var conHtml = '<form class="form-horizontal form-con">'
		+'<div class="form-group">'
		+	'<label class="col-sm-3 control-label" for="exampleInputEmail1">文字</label>'
		+	'<div class="col-sm-8">'
		+		'<input type="text" class="form-control" id="linkText" placeholder="请输入文字">'
		+	'</div>'
		+'</div>'
		+'<div class="form-group">'
		+	'<label class="col-sm-3 control-label" for="exampleInputEmail1">链接</label>'
		+	'<div class="col-sm-8">'
		+		'<input type="text" class="form-control" id="link" placeholder="请输入链接">'
		+	'</div>'
		+'</div>'
	+'</form>';
	
	$('.get-coupon-step .coupon-step').each(function() {
		_this.renderUpload($(this), 'step'+ $(this).index() +'Upload', function(_yetAnotherFileUrl) {
			sunrayActive.area1TitleUrl = _yetAnotherFileUrl;
			
			$(this).css('background-image', 'url(' + _yetAnotherFileUrl + ')');
		})
	})
	
	$('.get-coupon-step .stepTxt, .get-coupon-step .editTips').on('mouseover', function(e) {
		e.stopPropagation();
	})
	
	//编辑文本
	$('.get-coupon-step .editTips').on('click', function() {
		
		if($('.get-coupon-step .editTips').index($(this)) < 2) {
			$(this).prev('.stepTxt').focus();
		}else {
			
			var index = layer.confirm(conHtml, {
				  title: '编辑链接',
				  btn: ['保存','取消'], //按钮
				  area: ['500px', '240px'],
				  success: function() {
					  $('#linkText').val(sunrayActive.couponArea.step3Txt);
					  $('#link').val(sunrayActive.couponArea.step3Link);
				  }
				}, function(){
					sunrayActive.couponArea.step3Txt = $('#linkText').val();
					sunrayActive.couponArea.step3Link = $('#link').val();
					
					$('.coupon-step-three a span').text(sunrayActive.couponArea.step3Txt);
					$('.coupon-step-three a').attr('href', sunrayActive.couponArea.step3Link);
					
					layer.msg('保存成功!');
					
				}, function(){
					layer.close(index);
				});
			
		}
		
	})
	
}

//活动说明
sunrayActive.activeDes = function() {
	$('#iframe').get(0).onload = function() {
		var editHtml = '<p style="font-size:16px;color:#000;letter-spacing:0;line-height:40px;margin-bottom:0;">1、松下电阻8折特惠期间下单，每成单一次可得到一张3M口罩闪购优惠券。</p>'
			+'<p style="font-size:16px;color:#000;letter-spacing:0;line-height:40px;margin-bottom:0;">2、每张闪购（3M口罩）订单只能使用一张3M口罩闪购优惠券。</p>'
			+'<p style="font-size:16px;color:#000;letter-spacing:0;line-height:40px;margin-bottom:0;">3、本场活动获取的3M口罩闪购优惠券将在第一时间发送至您的易库易账户中，可在易库易用户中心&gt;资产中心&gt;<a class="active-voucher" style="color:#4c91e2;text-decoration:none;" target="_blank" href="http://localhost:8080/customer-web/assetcenter/myCoupon.htm">我的优惠券</a>中查看。</p>'
			+'<p style="font-size:16px;color:#000;letter-spacing:0;line-height:40px;margin-bottom:0;">4、参与本次活动获得的闪购券仅限“限时闪购”活动中3M口罩订单商品（不限口罩型号）使用， 闪购券有限期为2017年8月1日-2017年11月1日。</p>'
			+'<p style="font-size:16px;color:#000;letter-spacing:0;line-height:40px;margin-bottom:0;">5、本次松下电阻8折活动时间：2017年8月1日-2017年9月1日。</p>'
			+'<p style="font-size:16px;color:#000;letter-spacing:0;line-height:40px;margin-bottom:0;">6、活动最终解释权归易库易所有，活动请咨询在线客服或致电客服热线：400-930-0083。</p>';

		window.frames[0].postMessage('loadContent' + editHtml, ykyUrl.ictrade_ueditor);
	}

}

//网页SEO交互
sunrayActive.seo = function() {
	
	//读取seo信息
	var readSeoInfo = function() {
		$('#title').html(sunrayActive.seo.title);
		$('#keywords').html(sunrayActive.seo.keywords);
		$('#description').html(sunrayActive.seo.description);
		$('title').text(sunrayActive.seo.title);
		$('meta[name=keywords]').attr('content', sunrayActive.seo.keywords);
		$('meta[name=description]').attr('content', sunrayActive.seo.description);
	}
	
	readSeoInfo();
	
	//激活seo信息浮层
	$('.seo-setting').on('click', function() {
		var floatlayer = $('<div class="floatlayer" style="display:block;position:fixed;"></div>');
		$('body').append(floatlayer);
		$('.seoInfo').show();
	})
	
	
	//取消
	$('.seoInfo .cancel').on('click', function() {
		$('.floatlayer').hide();
		$('.seoInfo').hide();
	});
	

	//保存
	$('.seoInfo .save').on('click', function() {
		layer.msg('保存seo信息成功', {
			offset: ['140px', '140px'],
			time: 500,
			end: function() {
				setTimeout(function() {
					$('.seoInfo').hide();
					$('.floatlayer').hide();
				}, 200);
			}
		});
		sunrayActive.seo = {
				title: $('#title').val(),
				keywords: $('#keywords').val(),
				description: $('#description').val()
		}
		
		readSeoInfo();
	});
	
	
}


$(function() {
	
	//banner
	sunrayActive.uploadBanner();
	
	//广告文本编辑
	sunrayActive.editAdText();
	
	//搜索placeholder
	sunrayActive.searchPlaceholder();
	
	//各区域头部图片上传
	sunrayActive.titleImg();
	
	//优惠券步骤图片及文本
	sunrayActive.couponStep();
	
	//活动说明
	sunrayActive.activeDes();
	
	//网页seo交互
	sunrayActive.seo();
})