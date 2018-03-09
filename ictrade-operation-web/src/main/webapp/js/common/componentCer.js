var apis = {
	address: function(id) {
		return ykyUrl.database+'/v1/geos/'+ id +'/BELONG_TO';
		/*return 'http://192.168.1.110:27081/v1/geos/'+ id +'/BELONG_TO';*/
	}
};

/*var Authorization = $("#pageToken").val();
Authorization = 110;
//重新定义带权限的AJAX
jQuery.support.cors = true;

$.extend({       
	aAjax: function(option){
		var defualts = {
				beforeSend: function(request) {
	                request.setRequestHeader("Authorization", Authorization);
	            },
	            contentType:"application/json",
	            xhrFields: {
	                withCredentials: true
	            },
	            crossDomain: true,
		  	    success: function(){},
		  	    error: function(){},
			};
		var defualts = {};	
		$.ajax($.extend({}, defualts, option));
	}
});*/



// 下拉列表
(function() {

	var select = {};

	select.init = function(options) {
		if(!options || !options.el || !options.data) return;

		var options = $.extend({}, options);

		select.buildUi(options);
		select.bindEvent(options);


	}

	//渲染列表
	select.buildUi = function(options) {
		var $dom = $(options.el);
		var data = options.data;
		var htmlStr = '';
		var defaultId = (data[0] && data[0].id) ? data[0].id : '';
		var defaultName = (data[0] && data[0].name) ? data[0].name : '';
		var hasSelected = false;

		$dom.addClass('ui-select');

		$.each(data, function(i, item) {


			if(item["selected"]) {
				defaultId = item.id;
				defaultName = item.name;
				hasSelected = true;
			} 

			htmlStr += '<li class="ui-select-item" data-id="'+ item.id +'">'+ item.name +'</li>';
		});

		if(!hasSelected) {
			defaultId = '';
			defaultName = options.defaultText;
		}

		htmlStr = '<span data-id="'+ defaultId +'">'+ defaultName +'</span>'
				+'<i class="ico ico-down"></i>'
				+'<ul class="ui-select-list">'+ htmlStr;
		htmlStr += '</ul>';

		$dom.html(htmlStr);

		defaultId && $dom.find('.ui-select-list li').filter('[data-id='+ defaultId +']').addClass('selected');
	}

	//事件处理
	select.bindEvent = function(options) {
		var $dom = $(options.el);
		var isOpen = false;

		$dom.on('click', function(e) {
			$(this).find('.ui-select-list').toggle();
			$(this).find('.ico').toggleClass('ico-down ico-up');
			isOpen = !isOpen;
		})

		//选中选项
		$dom.find('.ui-select-list').on('click', function(e) {
			var target = $(e.target);
			var name = target.html();
			var id = target.data('id');
			if(target.hasClass('ui-select-item')) {
				$dom.find('span').html(name)
								.attr('data-id', id);
				options.change && options.change({"id": id, "name": name});
				target.addClass('selected').siblings().removeClass('selected');				

			}
			$(this).hide();
			isOpen = false;
			$dom.find('.ico').toggleClass('ico-down ico-up');
			e.stopPropagation();
		})

		//屏蔽选中文字
		$dom.on('selectstart', function() {
			return false;
		})

		/*点击列表之外处收起列表*/
		$(document).on('click', function(e) {
			var target = $(e.target);
			if(!isOpen) return;
			// 如果点击的是当前列表，不收起
			if($dom[0] === target.closest('.ui-select')[0]) return;
			$dom.find('.ui-select-list').hide();
			isOpen = false;
			$dom.find('.ico').toggleClass('ico-down ico-up');
		})
	}

	select.change = function(el, id) {
		var $dom = $(el);
		var selectedItem = $dom.find('.ui-select-list li').filter('[data-id='+ id +']')
		$dom.find('span').html(selectedItem.html())
								.attr('data-id', selectedItem.data('id'));
		selectedItem.addClass('selected').siblings().removeClass('selected');
	}

	// 销毁
	select.destory = function(el) {
		var $dom = $(el);
		$dom.removeClass('ui-select');
		$dom.empty();
		$dom.replaceWith($dom.clone());
	}

	window.select = select;

})();


// 省市区三级联动
(function() {
	var selectArea = {};
	selectArea.init = function(el, defaultData) {
		if(!el) return;
		var $dom = $(el);
		selectArea.data = defaultData || {};
		selectArea.buildUi($dom, defaultData);
	}

	//构建dom节点
	selectArea.buildUi = function($dom, defaultData) {	
		var defaultData = $.extend({
			provinceId: defaultData.provinceId?defaultData.provinceId:0, 
			cityId: defaultData.cityId !== 'undefined' && defaultData.cityId !== ''?defaultData.cityId:0, 
			countryId: defaultData.countryId !== 'undefined' && defaultData.countryId !== ''?defaultData.countryId:0,
			provinceDefaultText: defaultData.provinceText?defaultData.provinceText:'省/直辖市',
			cityDefaultText: defaultData.cityText?defaultData.cityText:'市',
			countryDefaultText: defaultData.countryText?defaultData.countryText:'区/县'
		}, defaultData);
		
		console.log(defaultData);
		var firstSetValue = { //是否第一次设置选项
			province: true,
			city: true,
			country: true
		}; 

		/*建立省市区div*/
		$dom.html('<div class="province"></div><div class="city"></div><div class="country"></div>');
	
		//省数据
		renderList('province', '100000', function(selectProvinceData) {
			renderList('city', selectProvinceData.id, function(selectCityData) {
					renderList('country', selectCityData.id);
			});
			renderList('country', $dom.find('.city .selected').data('id'), function(selectcountryData) {
				selectArea.data = {
					provinceId: defaultData.provinceId, 
					cityId: selectCityData.id, 
					countryId: selectcountryData.id
				};
			});
		})
		//市数据
		renderList('city', defaultData.provinceId, function(selectCityData) {
			renderList('country', selectCityData.id, function(selectcountryData) {
				selectArea.data = {
					provinceId: defaultData.provinceId, 
					cityId: selectCityData.id, 
					countryId: selectcountryData.id
				};
			});
		});
		//区数据
		renderList('country', defaultData.cityId);

		//列表渲染
		function renderList(type, areaId, change) {
			if(areaId === 0) {
				select.init({
					el: $dom.find('.' + type),
					data: [],
					defaultText: defaultData[type+'DefaultText']
				});
				return;
			};

			$.ajax({
				url: apis.address(areaId),
				type: 'GET',
				async: false,
				success: function(data) {

					var listData = [];
					$.each(data, function(i, item) {
						if(item.id === defaultData[type + 'Id'] && firstSetValue[type]) {
							item.selected = true;
							firstSetValue[type] = false;
						}
						listData.push(item);
					})

					select.destory($dom.find('.' + type));

					select.init({
						el: $dom.find('.' + type),
						data: listData,
						defaultText: defaultData[type+'DefaultText'],
						change: change
					});
				},
				error: function(err) {
					throw "接口错误" + err.responseText;
				}
			})
		}

	}
	

	window.selectArea = selectArea;

})();

// 单选框
(function() {
	$('.radio-item input').on('change', function() {
		$(this).next('.radio').toggleClass('radio-selected');
		$(this).parent().siblings('.radio-item').find('.radio').toggleClass('radio-selected');
	})
})();

//资质认证
(function() {

	var zzRenzheng = {};
	// busiLicPic: '', //营业执照影印件(大陆)  /  注册证书(香港)
	// taxRegPic: '', //税务登记证影印件(大陆) / 商业登记证(香港)
	// occPic: '', //组织结构代码影印件(大陆)
	// loaPic: '', //企业授权委托书(大陆) / 企业认证授权书(香港)

	zzRenzheng.data = {
		//大陆
		"01": {
			"3To1": [{
				"text": "营业执照影印件",
				"imgSrc": "",
				"yangSrc": ""
			}],
			"PUTONG": [{
				"text": "营业执照影印件",
				"imgSrc": "",
				"yangSrc": ""
			},{
				"text": "税务登记证影印件",
				"imgSrc": "",
				"yangSrc": ""
			},{
				"text": "组织机构代码证影印件",
				"imgSrc": "",
				"yangSrc": ""
			}]
		},
		//香港
		"02": [{
			"text": "注册证书(CR)",
			"imgSrc": "",
			"yangSrc": ""
		},{
			"text": "商业登记证(BR)",
			"imgSrc": "",
			"yangSrc": ""
		}],
		//台湾
		"03": [{
			"text": "盈利事业登记证",
			"imgSrc": "",
			"yangSrc": ""
		},{
			"text": "税籍登记证",
			"imgSrc": "",
			"yangSrc": ""
		}],
		//境外
		"04": [{
			"text": "CERTIFICATE OF INCORPORATION",
			"imgSrc": "",
			"yangSrc": ""
		}]
	};


	zzRenzheng.init = function(selector, options) {
		var $dom = $(selector);

		var selectData = [{
				"id": "01",
				"name": "大陆"
			},{
				"id": "02",
				"name": "香港"
			},{
				"id": "03",
				"name": "台湾"
			},{
				"id": "04",
				"name": "境外"
			}];

		if(options && options.type) {
			$.each(selectData, function(i, item) {
				if(item.id === options.type) {
					selectData[i].selected = true;
				}
			})
		}else {
			selectData[0].selected = true;
		}	

		//地区类别下拉列表
		select.init({
			el: $dom.find('.qual-type'),
			data: selectData,
			change: function(data) {
				zzRenzheng.changeArea($dom, data, options);

			}
		});

		//默认类型列表
		zzRenzheng.changeArea($dom, {"id": options && options.type ? options.type : "01"}, options);

		zzRenzheng.bindEvent($dom, options);
	}

	// 改变地区
	zzRenzheng.changeArea = function($dom, data, options) {
		var listHtml = '';	//列表的html
		var qualData = [];  //渲染认证列表的数据
		var zzType = $dom.find('.radio-selected').data('name');

		if(data.id === '01') {
			$dom.find('.dalu_type').show();
			qualData = zzRenzheng.data['01'][zzType];
		}else {
			$dom.find('.dalu_type').hide();
			qualData = zzRenzheng.data[data.id];
		}

		$.each(qualData, function(i, item) {
			listHtml += zzRenzheng.createQualItem(item);
		})

		$dom.find('.qual-list').html(listHtml);

		$dom.find('.qual-list').find('.upload-btn').each(function() {
			var previewImg = $(this).parent();
			var fileMaxSize = options.fileMaxSize || 5;
			var callback = options.callback || function() {};
			var browseButtonId = $(this).attr('id');
			zzRenzheng.upload(browseButtonId, fileMaxSize, previewImg, function(imageUrlPreview) {
				var uploadFileType = previewImg.find('img').data('type');
				if(imageUrlPreview) {
					if(uploadFileType === 'img') {
						layerPic.init(previewImg.find('img').attr('id'));
					}else if(uploadFileType === 'pdf') {
						previewImg.find('img').attr('src', '../../images/pdf_icon.png');
					}
				}
			});
		})
	};

	// 创建上传item
	zzRenzheng.createQualItem = function(itemData) {
		var ret = '';
		ret = '<div class="input-group qual-item">\
				<label class="input-text"><span class="must">*</span>'+ itemData.text +'：</label>\
				<div class="input-content">\
					<div class="uploadImg">\
						<img id="img'+ Math.floor(Math.random()*10000) +'" src="" alt="">\
						<button id="uploadBtn'+ Math.floor(Math.random()*10000) +'" class="upload-btn">点此上传</button>\
					</div>\
					<div class="uploadCz">\
						<p class="tips">文件格式：.jpg .jpeg .png .bmp .pdf，文件大小：5M以内<br>\
			上传的文件需加盖公司公章或财务章</p>\
						<button class="ck-btn">参考样本</button>\
					</div>\
				</div>\
			</div>';
		return ret;
	}

	// 事件处理
	zzRenzheng.bindEvent = function($dom, options) {

		$dom.find('.dalu_type .radio-item input[type=radio]').on('change', function() {
			zzRenzheng.changeArea($dom, {"id": "01"}, options);
		});
	}

	// 上传封装  参数： 上传按钮 | 文件大小 | 预览图节点ID | 大图节点ID | 回调
	zzRenzheng.upload = function(browseButtonId, fileSize, previewImg, callback) {
		// uploader = createUploader("img0", 5, "uploadImg0", "showImg0", reImg.bind(null,
		// 0));
		var uploader = createUploader(browseButtonId, fileSize, previewImg, callback);
		uploader.init();
	}

	window.zzRenzheng = zzRenzheng;

})();

//图片弹窗
(function() {

	//弹出原图
	function showVoucherPic(showOriIdStr) {
		
	  	layer.open({
	  		type : 1,
	  		title : false,
	  		closeBtn : 0,
	  		skin : 'layer_cla', // 没有背景色
	  		shadeClose : true,
	  		maxWidth: 1000,
	  		content : $("#"+showOriIdStr)[0].outerHTML
	  	});
	}


	/**
	   * 重置图片宽高
	   * @param obj 图片对象
	   */
	function reSizeImg(obj){
		var maxWidth = $(window).width()*0.9;
	  	var maxHeight = $(window).height()*0.9;
	  	var imgWidth=obj.width;
	  	var imgHeight=obj.height;
	  	var p =1 ;
	  	var pWidth = 1;
	  	var pHeight = 1;
	  	if(imgWidth>maxWidth)
	  	{
	  		pWidth=imgWidth/maxWidth;
	  	}
	  	if(imgHeight>maxHeight){
	  		pHeight = imgHeight/maxHeight;
	  	}
	  	if(pWidth>pHeight){
	  		p = pWidth;
	  	}else{
	  		p = pHeight;
	  	}
	  	if(p>1){//收缩图片尺寸
	  		obj.width=Math.floor(imgWidth/p);
	  		obj.height=Math.floor(imgHeight/p);
	  	}
	}

	var layerPic = {};

	layerPic.init = function(id) {
		$('#' + id).on('click', function() {
			showVoucherPic(id);
	        reSizeImg($("#" + id).get(0));
		});
	}

	window.layerPic = layerPic;

})();


// 图片上传
