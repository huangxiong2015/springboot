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
			var oldName = $dom.find('span').text();
			var oldId = $dom.find('span').attr('data-id');
			var target = $(e.target);
			var name = target.html();
			var id = target.data('id');
			if(target.hasClass('ui-select-item')) {
				$dom.find('span').html(name)
								.attr('data-id', id);
				options.change && options.change({"id": id, "name": name, "oldId": oldId, "oldName": oldName}, $(this));
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

	//静态方法-改变value
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

	//值改变触发
	select.selectChange = function(el, callback) {
		var $dom = $(el);

		$dom.find('.ui-select-item').on('click', function() {
			callback && callback();
		})
	}

	window.select = select;

})();


// 省市区三级联动
(function() {

	var selectArea = {};
	selectArea.init = function(el, defaultData, changeCallback) {
		if(!el) return;
		var $dom = $(el);

		selectArea.data = defaultData || {};
		selectArea.buildUi($dom, defaultData, changeCallback);
	}

	//构建dom节点
	selectArea.buildUi = function($dom, defaultData, changeCallback) {

		var defaultData = $.extend({
			provinceId: 0, 
			cityId: 0, 
			countyId: 0,
			provinceDefaultText: '省/直辖市',
			cityDefaultText: '市',
			countyDefaultText: '区/县'
		}, defaultData);

		var firstSetValue = { //是否第一次设置选项
			province: true,
			city: true,
			county: true
		}; 

		/*建立省市区div*/
		$dom.html('<div class="province"></div><div class="city"></div><div class="county"></div>');
	
		//省数据
		renderList('province', '100000', function(selectProvinceData) {
			
			renderList('city', selectProvinceData.id, function(selectCityData) {
					renderList('county', selectCityData.id);
			});
			renderList('county', $dom.find('.city .selected').data('id'), function(selectCountyData) {
				selectArea.data = {
					provinceId: defaultData.provinceId, 
					cityId: selectCityData.id, 
					countyId: selectCountyData.id
				};
			});
		})
		//市数据
		renderList('city', defaultData.provinceId, function(selectCityData) {
			renderList('county', selectCityData.id, function(selectCountyData) {
				selectArea.data = {
					provinceId: defaultData.provinceId, 
					cityId: selectCityData.id, 
					countyId: selectCountyData.id
				};
			});
		});
		//区数据
		renderList('county', defaultData.cityId);

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
						change: function(data, $target) {
							change && change(data);
							//省市区只要一个改变就会执行
							changeCallback && changeCallback(data, $target);
						}
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
	zzRenzheng.changeStatus = false;
	zzRenzheng.cacheData = {
			'3-TO-1': {
				BUSI_LIC_PIC: '',
				BUSI_PDF_NAME: '',
				BUSI_LIC_PIC_dataType: '',
				BUSI_LIC_PIC_dataUrl: '',
				BUSI_LIC_PIC_dataSrc: ''
			},
			'COMMON': {
				BUSI_LIC_PIC: '',
				TAX_REG_PIC: '',
				OCC_PIC: '',
				BUSI_PDF_NAME: '',
				TAX_PDF_NAME: '',
				OCC_PDF_NAME: '',
				BUSI_LIC_PIC_dataType: '',
				TAX_REG_PIC_dataType: '',
				OCC_PIC_dataType: '',
				BUSI_LIC_PIC_dataUrl: '',
				TAX_REG_PIC_dataUrl: '',
				OCC_PIC_dataUrl: '',
				BUSI_LIC_PIC_dataSrc: '',
				TAX_REG_PIC_dataSrc: '',
				OCC_PIC_dataSrc: ''
			},
			'HK-CODE': {
				BUSI_LIC_PIC: '',
				TAX_REG_PIC: '',
				BUSI_PDF_NAME: '',
				TAX_PDF_NAME: '',
				BUSI_LIC_PIC_dataType: '',
				TAX_REG_PIC_dataType: '',
				BUSI_LIC_PIC_dataUrl: '',
				TAX_REG_PIC_dataUrl: '',
				BUSI_LIC_PIC_dataSrc: '',
				TAX_REG_PIC_dataSrc: ''
			},
			'TW-CODE': {
				BUSI_LIC_PIC: '',
				TAX_REG_PIC: '',
				BUSI_PDF_NAME: '',
				TAX_PDF_NAME: '',
				BUSI_LIC_PIC_dataType: '',
				TAX_REG_PIC_dataType: '',
				BUSI_LIC_PIC_dataUrl: '',
				TAX_REG_PIC_dataUrl: '',
				BUSI_LIC_PIC_dataSrc: '',
				TAX_REG_PIC_dataSrc: ''
			},
			'ABROAD-CODE': {
				BUSI_LIC_PIC: '',
				BUSI_PDF_NAME: '',
				BUSI_LIC_PIC_dataType: '',
				BUSI_LIC_PIC_dataUrl: '',
				BUSI_LIC_PIC_dataSrc: ''
			}
		};
	// BUSI_LIC_PIC: '', //营业执照影印件(大陆)  /  注册证书(香港)
	// TAX_REG_PIC: '', //税务登记证影印件(大陆) / 商业登记证(香港)
	// OCC_PIC: '', //组织结构代码影印件(大陆)

	zzRenzheng.data = {
		//大陆
		"0": {
			"COMMON": [{
				"text": "营业执照影印件",
				"classText": 'BUSI_LIC_PIC',
				"referUrl": 'dl-busiLic.jpg'
			},{
				"text": "税务登记证影印件",
				"classText": 'TAX_REG_PIC',
				"referUrl": 'dl-taxReg.jpg'
			},{
				"text": "组织机构代码证影印件",
				"classText": 'OCC_PIC',
				"referUrl": 'dl-occ.jpg'
			}],
			"3-TO-1": [{
				"text": "营业执照影印件",
				"classText": 'BUSI_LIC_PIC',
				"referUrl": 'dl-busiLic.jpg'
			}]
		},
		//香港
		"1": [{
			"text": "注册证书(CR)",
			"classText": 'BUSI_LIC_PIC',
			"referUrl": 'hk-CR.jpg'
		},{
			"text": "商业登记证(BR)",
			"classText": 'TAX_REG_PIC',
			"referUrl": 'hk-BR.jpg'
		}],
		//台湾
		"2": [{
			"text": "盈利事業登記證",
			"classText": 'BUSI_LIC_PIC',
			"referUrl": 'tw-busiLic.jpg'
		},{
			"text": "稅籍登記證",
			"classText": 'TAX_REG_PIC',
			/*"referUrl": 'tw-busiLic.pdf'*/
		}],
		//境外
		"3": [{
			"text": "CERTIFICATE OF INCORPORATION",
			"classText": 'BUSI_LIC_PIC',
			"referUrl": 'jw-limited.jpg'
		}]
	};


	zzRenzheng.init = function(selector, options) {
		var $dom = $(selector);

		var selectData = [{
				"id": "0",
				"name": "大陆"
			},{
				"id": "1",
				"name": "香港"
			},{
				"id": "2",
				"name": "台湾"
			},{
				"id": "3",
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
				zzRenzheng.changeStatus = true;
				zzRenzheng.changeArea($dom, data, options);
			}
		});

		//默认地区
		zzRenzheng.changeArea($dom, {"id": options && options.type ? options.type : "0"}, options);
		
		//默认资质信息
		zzRenzheng.defaultZzInfo($dom, {"id": options && options.type ? options.type : "0"}, options);

		//默认选中
		$dom.find('.radio-selected').prev('input').prop('checked', true);
		
		zzRenzheng.bindEvent($dom, options);
	}
	
	//阿里云私有图片转码
	zzRenzheng.getImage = function(imageUrl) {

		var url = imageUrl ? imageUrl : '';
		//获取图片公共方法
		$.aAjax({
			url: ykyUrl.party + "/v1/enterprises/getImgUrl",
			type: "POST",
			async: false,
			data: JSON.stringify({
				"id": imageUrl
			}),
			success: function(data) {
				if (data !== "") {
					url = data;
				}
			}
		});
		return url;
	}

	// 改变地区
	zzRenzheng.changeArea = function($dom, data, options, daluType) { //daluType更改之前的大陆三证类型
		var listHtml = '';	//列表的html
		var qualData = [];  //渲染认证列表的数据
		var zzType = $dom.find('.radio-selected').data('name');
		
		if(!zzRenzheng.changeStatus) { //第一次渲染
			zzType = options.busilisType || '3-TO-1';
		}else {
			zzType = $dom.find('.radio-selected').data('name');
		}

		if(data.id == '0') {
			$dom.find('.dalu_type').show();
			qualData = zzRenzheng.data['0'][zzType];
		}else {
			$dom.find('.dalu_type').hide();
			qualData = zzRenzheng.data[data.id];
		}
		
		//切换地区了，重新加上上传
		if(zzRenzheng.changeStatus) {
			
			var curAreaId = data.oldId || '0'
			
			//获取认证类型
			var busiType = (function() {
				switch(curAreaId) {
					case '0':
						return daluType || $dom.find('.radio-selected').attr('data-name');
						break;
					case '1':
						return 'HK-CODE';
						break;
					case '2':
						return 'TW-CODE';
						break;
					case '3':
						return 'ABROAD-CODE';
						break;
				}
			})();
			
			
			//缓存三证数据
			switch(busiType) {
				case '3-TO-1':
					zzRenzheng.cacheData['3-TO-1'] = {
						BUSI_LIC_PIC: $('.BUSI_LIC_PIC').attr('src'),
						BUSI_PDF_NAME: $('.BUSI_LIC_PIC').attr('data-pdfname'),
						BUSI_LIC_PIC_dataType: $('.BUSI_LIC_PIC').attr('data-type'),
						BUSI_LIC_PIC_dataUrl: $('.BUSI_LIC_PIC').attr('data-url'),
						BUSI_LIC_PIC_dataSrc: $('.BUSI_LIC_PIC').attr('data-src')
					};
					break;
				case 'COMMON':
					zzRenzheng.cacheData['COMMON'] = {
						BUSI_LIC_PIC: $('.BUSI_LIC_PIC').attr('src'),
						TAX_REG_PIC: $('.TAX_REG_PIC').attr('src'),
						OCC_PIC: $('.OCC_PIC').attr('src'),
						BUSI_PDF_NAME: $('.BUSI_LIC_PIC').attr('data-pdfname'),
						TAX_PDF_NAME: $('.TAX_REG_PIC').attr('data-pdfname'),
						OCC_PDF_NAME: $('.OCC_PIC').attr('data-pdfname'),
						BUSI_LIC_PIC_dataType: $('.BUSI_LIC_PIC').attr('data-type'),
						TAX_REG_PIC_dataType: $('.TAX_REG_PIC').attr('data-type'),
						OCC_PIC_dataType: $('.OCC_PIC').attr('data-type'),
						BUSI_LIC_PIC_dataUrl: $('.BUSI_LIC_PIC').attr('data-url'),
						TAX_REG_PIC_dataUrl: $('.TAX_REG_PIC').attr('data-url'),
						OCC_PIC_dataUrl: $('.OCC_PIC').attr('data-url'),
						BUSI_LIC_PIC_dataSrc: $('.BUSI_LIC_PIC').attr('data-src'),
						TAX_REG_PIC_dataSrc: $('.TAX_REG_PIC').attr('data-src'),
						OCC_PIC_dataSrc: $('.OCC_PIC').attr('data-src')
					};
					break;
				case 'HK-CODE':
				case 'TW-CODE':
					zzRenzheng.cacheData[busiType] = {
						BUSI_LIC_PIC: $('.BUSI_LIC_PIC').attr('src'),
						TAX_REG_PIC: $('.TAX_REG_PIC').attr('src'),
						BUSI_PDF_NAME: $('.BUSI_LIC_PIC').attr('data-pdfname'),
						TAX_PDF_NAME: $('.TAX_REG_PIC').attr('data-pdfname'),
						BUSI_LIC_PIC_dataType: $('.BUSI_LIC_PIC').attr('data-type'),
						TAX_REG_PIC_dataType: $('.TAX_REG_PIC').attr('data-type'),
						BUSI_LIC_PIC_dataUrl: $('.BUSI_LIC_PIC').attr('data-url'),
						TAX_REG_PIC_dataUrl: $('.TAX_REG_PIC').attr('data-url'),
						BUSI_LIC_PIC_dataSrc: $('.BUSI_LIC_PIC').attr('data-src'),
						TAX_REG_PIC_dataSrc: $('.TAX_REG_PIC').attr('data-src')
					};
					break;
				case 'ABROAD-CODE':
					zzRenzheng.cacheData['ABROAD-CODE'] = {
						BUSI_LIC_PIC: $('.BUSI_LIC_PIC').attr('src'),
						BUSI_PDF_NAME: $('.BUSI_LIC_PIC').attr('data-pdfname'),
						BUSI_LIC_PIC_dataType: $('.BUSI_LIC_PIC').attr('data-type'),
						BUSI_LIC_PIC_dataUrl: $('.BUSI_LIC_PIC').attr('data-url'),
						BUSI_LIC_PIC_dataSrc: $('.BUSI_LIC_PIC').attr('data-src')
					};
					break;
			}
			
			
		}

		$.each(qualData, function(i, item) {
			listHtml += zzRenzheng.createQualItem(item, options);
		})

		$dom.find('.qual-list').html(listHtml);
		
		
		
		//切换地区pdf也可以点击
		$dom.find('.uploadImg img').on('click', function() {
			var $this = $(this);
			var isPdf = $(this).attr('data-url') && $(this).attr('data-url').indexOf('.pdf') >= 0;
			if(isPdf) {
				window.open($(this).attr('data-url'));
			}
		})	
		
		//判断是否是pdf文件
		var isPdfFile = function(type) {
			try {
				return $('.' + type).attr('src') && $('.' + type).attr('data-src').indexOf('.pdf') !== -1;
			}catch(e) {
				return false;
			}
		}
		
		//大陆资质类型处理
		if(zzType == '3-TO-1' || zzType =='COMMON') {
			$dom.find('.radio').removeClass('radio-selected');
			$dom.find('.radio[data-name='+ zzType +']').addClass('radio-selected');
		}
		
		//切换地区了，重新加上上传
		if(!zzRenzheng.changeStatus) return;
		
		
		//切換地區三证缓存還是要加到節點上
		
		curAreaId = data.id || '0'
		
		//获取认证类型
		busiType = (function() {
			var result = '';
			daluType = daluType == '3-TO-1' ? 'COMMON' : '3-TO-1';
			
			switch(curAreaId + '') {
				case '0':
					result = daluType || $dom.find('.radio-selected').attr('data-name');
					break;
				case '1':
					result = 'HK-CODE';
					break;
				case '2':
					result = 'TW-CODE';
					break;
				case '3':
					result = 'ABROAD-CODE';
					break;
			}
			return result;
		})();
		
		if(zzRenzheng.cacheData[busiType]) {
			$dom.find('.BUSI_LIC_PIC').attr('data-pdfname', zzRenzheng.cacheData[busiType].BUSI_PDF_NAME);
			$dom.find('.TAX_REG_PIC').attr('data-pdfname', zzRenzheng.cacheData[busiType].TAX_PDF_NAME);
			$dom.find('.OCC_PIC').attr('data-pdfname', zzRenzheng.cacheData[busiType].OCC_PDF_NAME);
			$dom.find('.BUSI_LIC_PIC').attr('data-src', zzRenzheng.cacheData[busiType].BUSI_LIC_PIC_dataSrc);
			$dom.find('.TAX_REG_PIC').attr('data-src', zzRenzheng.cacheData[busiType].TAX_REG_PIC_dataSrc);
			$dom.find('.OCC_PIC').attr('data-src', zzRenzheng.cacheData[busiType].OCC_PIC_dataSrc);
			$dom.find('.BUSI_LIC_PIC').attr('data-url', zzRenzheng.cacheData[busiType].BUSI_LIC_PIC_dataUrl);
			$dom.find('.TAX_REG_PIC').attr('data-url', zzRenzheng.cacheData[busiType].TAX_REG_PIC_dataUrl);
			$dom.find('.OCC_PIC').attr('data-url', zzRenzheng.cacheData[busiType].OCC_PIC_dataUrl);
			$dom.find('.BUSI_LIC_PIC').attr('data-type', zzRenzheng.cacheData[busiType].BUSI_LIC_PIC_dataType);
			$dom.find('.TAX_REG_PIC').attr('data-type', zzRenzheng.cacheData[busiType].TAX_REG_PIC_dataType);
			$dom.find('.OCC_PIC').attr('data-type', zzRenzheng.cacheData[busiType].OCC_PIC_dataType);
			$dom.find('.BUSI_LIC_PIC').attr('src', zzRenzheng.cacheData[busiType].BUSI_LIC_PIC);
			$dom.find('.TAX_REG_PIC').attr('src', zzRenzheng.cacheData[busiType].TAX_REG_PIC);
			$dom.find('.OCC_PIC').attr('src', zzRenzheng.cacheData[busiType].OCC_PIC);
		}

		//点击显示大图
		isPdfFile('BUSI_LIC_PIC') || layerPic.init($dom.find('.BUSI_LIC_PIC').attr('id'), $dom.find('.BUSI_LIC_PIC'));
		isPdfFile('TAX_REG_PIC') || layerPic.init($dom.find('.TAX_REG_PIC').attr('id'), $dom.find('.TAX_REG_PIC'));
		isPdfFile('OCC_PIC') || layerPic.init($dom.find('.OCC_PIC').attr('id'), $dom.find('.OCC_PIC'));
		
		//参考样本
		$dom.find('.ck-btn').each(function() {
			var dataUrl = $(this).data('src');
			if(dataUrl.indexOf('.pdf') == -1) {
				//是图片地址
				layerPic.init($(this).attr('id'), $(this).next('img'));
			}else {
				//是pdf
				$(this).on('click', function() {
					window.open(dataUrl);
				});
			}
		})
		
		
		$dom.find('.qual-list').find('.upload-btn').each(function() {
			var previewImg = $(this).parent();
			var fileMaxSize = options.fileMaxSize || 5;
			var callback = options.callback || function() {};
			var browseButtonId = $(this).attr('id');
			zzRenzheng.upload(browseButtonId, fileMaxSize, previewImg, function(imageUrlPreview) {
				var uploadFileType = previewImg.find('img').attr('data-type');
				previewImg.find('img').off('click');
				if(imageUrlPreview) {
					if(uploadFileType === 'img') {
						layerPic.init(previewImg.find('img').attr('id'), previewImg.find('img'));
					}else if(uploadFileType === 'pdf') {
						previewImg.find('img').attr('src', ykyUrl._this + '/images/pdf_icon.png');
						previewImg.find('img').on('click', function() {
							window.open($(this).attr('data-url'));
						})
					}
				}
				
			});
		})
		
		
	};
	
	//默认资质信息
	zzRenzheng.defaultZzInfo = function($dom, data, options) {

		//判断是否是pdf文件
		var isPdfFile = function(type) {
			try {
				return options.imgSrc[type].indexOf('.pdf') !== -1;
			}catch(e) {
				return false;
			}
		}
		
		//点击显示大图
		isPdfFile('BUSI_LIC_PIC') || layerPic.init($dom.find('.BUSI_LIC_PIC').attr('id'), $dom.find('.BUSI_LIC_PIC'));
		isPdfFile('TAX_REG_PIC') || layerPic.init($dom.find('.TAX_REG_PIC').attr('id'), $dom.find('.TAX_REG_PIC'));
		isPdfFile('OCC_PIC') || layerPic.init($dom.find('.OCC_PIC').attr('id'), $dom.find('.OCC_PIC'));
		
		//如果默认有pdf文件存在
		if(options.imgSrc && options.imgSrc.BUSI_PDF_NAME) {
			$dom.find('.BUSI_LIC_PIC').attr('data-pdfname', options.imgSrc.BUSI_PDF_NAME);
		}
		if(options.imgSrc && options.imgSrc.TAX_PDF_NAME) {
			$dom.find('.TAX_REG_PIC').attr('data-pdfname', options.imgSrc.TAX_PDF_NAME);
		}
		if(options.imgSrc && options.imgSrc.OCC_PDF_NAME) {
			$dom.find('.OCC_PIC').attr('data-pdfname', options.imgSrc.OCC_PDF_NAME);
		}

		$dom.find('.qual-list').find('.upload-btn').each(function() {
			var previewImg = $(this).parent();
			var fileMaxSize = options.fileMaxSize || 5;
			var callback = options.callback || function() {};
			var browseButtonId = $(this).attr('id');
			zzRenzheng.upload(browseButtonId, fileMaxSize, previewImg, function(imageUrlPreview) {
				var uploadFileType = previewImg.find('img').attr('data-type');
				previewImg.find('img').off('click');
				if(imageUrlPreview) {
					if(uploadFileType === 'img') {
						layerPic.init(previewImg.find('img').attr('id'), previewImg.find('img'));
					}else if(uploadFileType === 'pdf') {
						previewImg.find('img').attr('src', ykyUrl._this + '/images/pdf_icon.png');
						previewImg.find('img').on('click', function() {
							window.open($(this).attr('data-url'));
						})
					}
				}
				
			});
		})
		
		//参考样本
		$dom.find('.ck-btn').each(function() {
			var dataUrl = $(this).data('src');
			if(dataUrl.indexOf('.pdf') == -1) {
				//是图片地址
				layerPic.init($(this).attr('id'), $(this).next('img'));
			}else {
				//是pdf
				$(this).on('click', function() {
					window.open(dataUrl);
				});
			}
		})
	};

	// 创建上传item
	zzRenzheng.createQualItem = function(itemData, options) {
		var ret = '';
		var imgSrc = options && options.imgSrc ? options.imgSrc[itemData.classText] : '';
		var zmImgSrc = zzRenzheng.getImage(imgSrc);
		var fileType = imgSrc.indexOf('.pdf') != -1 ? 'pdf' : 'img';
		var dataUrl = fileType === 'pdf' ? ' data-url="' + zmImgSrc + '" ' : '';
		var imgBaseUrl = ykyUrl._this + '/images/sanzheng/'
		var srcAttr = fileType === 'pdf' ? ykyUrl._this + '/images/pdf_icon.png' : zmImgSrc;
		var ckImgSrc = itemData.referUrl ? '<span id="ck-btn'+ Math.floor(Math.random()*10000) +'" class="ck-btn" data-src="'+ imgBaseUrl + itemData.referUrl +'">参考样本</span><img class="ckImg" src="'+ imgBaseUrl + itemData.referUrl +'" style="display:none"/>' : '';
		ret = '<div class="input-group qual-item">\
				<label class="input-text"><span class="must">*</span>'+ itemData.text +'：</label>\
				<div class="input-content">\
					<div class="uploadImg">\
						<img id="img'+ Math.floor(Math.random()*10000) +'" class="'+ itemData.classText +'" '+ dataUrl +' data-src="'+ imgSrc +'" data-type="'+ fileType +'" src="'+ srcAttr +'" alt="">\
						<button id="uploadBtn'+ Math.floor(Math.random()*10000) +'" class="upload-btn">点此上传</button>\
					</div>\
					<div class="uploadCz">\
						<p class="tips">文件格式：.jpg .jpeg .png .bmp .pdf，文件大小：5M以内<br>\
			上传的文件需加盖公司公章或财务章</p>\
						'+ ckImgSrc +
					'</div>\
				</div>\
			</div>';
		return ret;
	}

	// 事件处理
	zzRenzheng.bindEvent = function($dom, options) {
		
		$dom.find('.dalu_type .radio-item input[type=radio]').on('change', function() {
			var $this = $(this);
			var busitype = (function() {
				return $this.next('.radio').attr('data-name') == '3-TO-1' ? 'COMMON' : '3-TO-1';
			})();
			zzRenzheng.changeStatus = true;
			zzRenzheng.changeArea($dom, {"id": "0"}, options, busitype);
		});
	}

	// 上传封装  参数： 上传按钮 | 文件大小 | 预览图节点ID | 回调
	zzRenzheng.upload = function(browseButtonId, fileSize, previewImg, callback) {
		// uploader = createUploader("img0", 5, "uploadImg0", "showImg0", reImg.bind(null,
		// 0));
		var uploader = createUploader(browseButtonId, fileSize, previewImg, callback);
		uploader.init();
	}

	zzRenzheng.onlyRead = function(dom) {
		$(dom).find('.ui-select').off('click');
		$(dom).find('.dalu_type .radio-item input[type=radio]').off('change');
		$(dom).find('.upload-btn').remove();
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
	  		content : showOriIdStr.get(0).outerHTML.replace(/style="display:none"/, '')
	  	});
	}


	/**
	  * 重置图片宽高
	  * @param obj 图片对象
	  */
	function reSizeImg(obj){
		var maxWidth = $(window).width()*0.9;
	  	var maxHeight = $(window).height()*0.9;
	  	
	  	var imgObj = new Image();
	  	imgObj.src = obj.attr('src');
	  	
	  	imgObj.onload = function() {
	  		var imgWidth = imgObj.width;
		  	var imgHeight = imgObj.height;
		  	var imgClass = obj.attr('class');
		  	
		  	var imgEle = $('.layui-layer-content .' + imgClass);

		  	if(imgWidth >= imgHeight) {
		  		imgEle.width(maxWidth);
		  		
		  		if(imgEle.height() >= $(window).height()) {
		  			imgEle.css('width', 'auto');
		  			imgEle.height(maxHeight);
		  		}
		  		
		  	}else {
		  		imgEle.height(maxHeight);
		  		
		  		if(imgEle.width() >= $(window).width()) {
		  			imgEle.css('height', 'auto');
		  			imgEle.width(maxWidth);
		  		}
		  		
		  	}
		  	
		  	var topPx = ($(window).height() - $('.layui-layer-content .' + imgClass).height()) / 2;
		  	var leftPx = ($(window).width() - $('.layui-layer-content .' + imgClass).width()) / 2;
	  		$('.layui-layer').width('auto');
	  		$('.layui-layer').css('top', topPx);
	  		$('.layui-layer').css('left', leftPx);
	  		$('.layui-layer-content').height('auto');
	  	}
	  	
	}

	var layerPic = {};

	layerPic.init = function(id, imgEle) {
		if(!imgEle.attr('src')) return;
		$('#' + id).on('click', function() {
			showVoucherPic(imgEle);
	        reSizeImg(imgEle);
		});
	}

	window.layerPic = layerPic;

})();


// 图片上传
