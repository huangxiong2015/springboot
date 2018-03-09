var API = {
		entInfo: ykyUrl.party + '/v1/enterprises/entDetail/' + getQueryString("id") + '/' + getQueryString('corporationId') + '/CORPORATION',
		contactInfo: ykyUrl.party + '/v1/customers/' + getQueryString("id"),
		operationlist: ykyUrl.party + "/v1/audit?actions=Enterprise Modify&actionId=" + getQueryString('corporationId')
}

var enterpriseInfo = new Vue({
	el: '#enterprise-info',
	data: {
		initData: {
			companytypeList: {},	//公司类型列表
			industryList: {},	//行业列表
			positionList:[{"id":"buyer","name":"采购员"},{"id":"purchasingManager","name":"采购经理"},{"id":"engineer","name":"工程师"},{"id":"other","name":"其他"}] //职业列表
		},
		basicInfo: {
			id: getQueryString("id"),									//个人编码
			corporationId: getQueryString('corporationId'),				//企业编码
			name: '',													//公司名称
			categoryId: '',												//公司类型
			otherCategoryId: '',										//公司类型-其他
			industryCategory: '',										//所属行业
			otherIndustryCategory: '',									//所属行业-其他
			address: '',												//公司地址
			dCode: '',													//邓氏编码
			website: '',												//公司官网
			partyCode: '',												//YKY客户编码
		},
		quaInfo: {
			activeStatus: '',											//资质状态
			regAddr: '',												//公司注册地
			busilisType: '',											//营业执照类型
			busiPdfName: '',											//营业执照文件名
			busilicPic: '',												//营业执照 | 注册证书(CR) | 營利事業登記證（公司設立登記表）| CERTIFICATE OF INCORPORATION
			taxPdfName: '',												//税务登记证文件名
			taxPic:	'',													//税务登记证 | 商业登记证(BR) | 稅籍登记证 
			oocPdfName: '',												//组织机构代码证文件名
			oocPic: ''													//组织机构代码证
		},
		contactInfo: {
			name: '',													//联系人姓名
			telNumber: '',												//手机号码
			mail: '',													//邮箱
			fixedTel: '',												//固定电话
			qq: '',														//qq
			personalTitle: '',											//职位
			address: ''													//联系地址
		},
		operationlist: [],												//操作记录
		showOperationlist: [],											//显示的操作记录
		comments: ''													//客服备注
	},
	computed: {
		categoryIdName: function() {
			var _this = this;
			var returnValue = '';
			
			$.each(this.initData.companytypeList, function(index, item) {
				if(item.categoryId == _this.basicInfo.categoryId) {
					returnValue = item.categoryName;
					
					if(item.categoryId == '2006') {
						returnValue += '(' + _this.basicInfo.otherCategoryId + ')';
					}
					
					return false;
				}
			}) 
			
			return returnValue;
		},
		industryCategoryName: function() {
			var _this = this;
			var returnValue = [];
			
			$.each(this.basicInfo.industryCategory.split(','), function(i, industryItem) {
				$.each(_this.initData.industryList, function(index, item) {
					if(item.categoryId == industryItem) {
						
						if(item.categoryId == '1008') {
							returnValue.push(item.categoryName + '('+ _this.basicInfo.otherIndustryCategory +')');
						}else {
							returnValue.push(item.categoryName);
						}
						return false;
					}
				}) 
			});
			
			return returnValue.join(',');
		},
		//公司注册地
		regAddr: function() {
			return ['大陆', '香港', '台湾', '境外'][this.quaInfo.regAddr];
		},
		//营业执照类型
		busilisType: function() {
			if(this.quaInfo.busilisType == 'COMMON') {
				return '普通营业执照';
			}else if(this.quaInfo.busilisType == '3-TO-1') {
				return '三证合一营业执照';
			}else {
				return '';
			}
		},
		//职位名称
		personalTitleName: function() {
			var _this = this;
			var returnValue = '';
			
			$.each(this.initData.positionList, function(i, item) {
				if(item.id == _this.contactInfo.personalTitle) {
					returnValue = item.name;
					return false;
				}
			});
			
			return returnValue;
		}
	},
	created: function() {
		initData();
		getEntInfo();
		getContactInfo();
		getOperationlist();
	},
	methods: {
		//图片转码
		getShowImage: function(imageUrl) {
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
		},
		showPic: function(event) {
            showVoucherPic($(event.target));
		},
		showMore: function() {
			this.showOperationlist = this.operationlist;
			$('.more').remove();
		}
	}
});

//弹出原图
function showVoucherPic(showOriIdStr) {

    layer.open({
        type : 1,
        title : false,
        closeBtn : 0,
        skin : 'layer_cla', // 没有背景色
        shadeClose : true,
        content : showOriIdStr.get(0).outerHTML.replace(/class="img-thumbnail"/, 'class="layer-img" style="max-width:1000px;width:auto;height:auto;"')
    });

    $('.layui-layer').css('width', $('.layer-img').width());
    $('.layui-layer').css('left', ($(window).width() - $('.layer-img').width()) / 2);
}


//初始化数据
function initData() {
	var categoryUrl = ykyUrl.database + '/v1/category/companytype';
	var industryUrl = ykyUrl.database + '/v1/category/industry';
	syncData(categoryUrl, 'get', {}, function(data) {
		if(data) {
			enterpriseInfo.initData.companytypeList = data;
		}
	});
	syncData(industryUrl, 'get', {}, function(data) {
		if(data) {
			enterpriseInfo.initData.industryList = data;
		}
	});
}

//查询企业信息和资质信息
function getEntInfo() {
	
	$.aAjax({
		url: API.entInfo,
		type: 'GET',
		success: function(data) {
			var map = data.map || {};
			var entInfoData = {
					name: data.name || '-',
					categoryId: map && map.CORPORATION_CATEGORY_ID || '-',
					otherCategoryId: map && map.CORPORATION_CATEGORY_ID_OTHER || '-',
					industryCategory: map && map.INDUSTRY_CATEGORY_ID || '-',
					otherIndustryCategory: map && map.INDUSTRY_CATEGORY_ID_OTHER || '-',
					address: data.provinceName && data.provinceName + data.cityName + data.countryName + data.address || '-',
					dCode: map && map.D_CODE || '-',
					website: map && map.WEBSITE_URL || '-',
					partyCode: data.partyCode || '-'
			};
			var quaInfo = {
				activeStatus: data.activeStatus,
				regAddr: map && map.REG_ADDR || '',
				busilisType: map && map.BUSI_LIS_TYPE || '',
				busiPdfName: map && map.BUSI_PDF_NAME || '',
				busilicPic: map && map.BUSI_LIC_PIC || '',
				taxPdfName: map && map.TAX_PDF_NAME || '',
				taxPic:	map && map.TAX_REG_PIC || '',
				oocPdfName: map && map.OCC_PDF_NAME || '',
				oocPic: map && map.OCC_PIC || ''
			}
			enterpriseInfo.basicInfo = $.extend({}, enterpriseInfo.basicInfo, entInfoData);
			enterpriseInfo.quaInfo = $.extend({}, enterpriseInfo.quaInfo, quaInfo);
			
			enterpriseInfo.comments = data.comments || '';
			enterpriseInfo.showOperationlist = enterpriseInfo.operationlist.slice(0, 5);
		},
		error: function() {
			
		}
	})
	
}

//查询个人信息
function getContactInfo() {
	
	$.aAjax({
		url: API.contactInfo,
		typ: 'GET',
		success: function(data) {
			var contactInfo = {
				name:  data.name || '-',
				telNumber: data.telNumber || '-',
				mail: data.mail || '-',
				fixedTel: data.fixedTel || '-',
				qq: data.qq || '-',
				personalTitle: data.personalTitle || '-',
				address: data.provinceName && data.provinceName + data.cityName + data.countryName + data.address || '-'
			};
			
			enterpriseInfo.contactInfo = $.extend({}, enterpriseInfo.contactInfo, contactInfo);
		},
		error: function() {
			
		}
	})
	
}

//查询操作记录
function getOperationlist() {
	
	$.aAjax({
		url: API.operationlist,
		type: 'GET',
		success: function(data) {
			if(data && data.list) {
				enterpriseInfo.operationlist = data.list;
			}
		},
		error: function() {
			
		}
	})
	
}
