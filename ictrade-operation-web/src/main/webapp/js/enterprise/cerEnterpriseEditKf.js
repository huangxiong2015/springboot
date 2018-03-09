//获取当前url参数
function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null){
		return decodeURI(r[2]);
	}else{
		return "";
	}
}

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

var API = {
		getEntInfo: ykyUrl.party + '/v1/enterprises/entDetail/1/' +  getQueryString("id") + '/VIP_CORPORATION',
		applyRecord: function(listAction) { return ykyUrl.party + "/v1/audit?actions=" + listAction + "&actionId=" + getQueryString("id");} //修改记录
}



var app = new Vue({
	el: '#edit',
	data: {
		name: '',	//公司名称
		corCategory : '', //公司类型
		corCategoryOther: '',	//公司类型-其他
		industry : '', //行业
		industryOther: '', //行业-其他
		province: '', //省
		city: '',	//市
		country: '', //区
		provinceName: '', //省-名称
		cityName: '',	//市-名称
		countryName: '', //区-名称
		address: '',	//详细地址
		dCode: '',	//邓氏编码
		webSite: '', //公司官网
		partyCode: '',	//YKY编码
		creditComments: '',	//信用备注
		activeStatus: '',	//认证状态
		applyRecord: [],	//修改记录
		initData: {
			companytypeList: {},	//公司类型列表
			industryList: {},	//行业列表
		},
		regAddr: '',
		busiLisType: '', //三证类型
		busiLicPic: '', //营业执照
		oocPic: '', //组织机构代码
		taxPic: '', //税务登记
		busiPdfName: '', //营业执照pdf
		oocPdfName: '', //组织机构代码pdf
		taxPdfName: '', //税务登记pdf
		loaPic: '', //授权书
		loaPdfName: '', //授权书pdf
		socialCode: '',
		entName: '',
		location: '',
		busiRange: '',
		faxCode: '',
		faxName: '',
		orgCode: '',
		orgName: '',
		orgLocation: '',
		orgCdate: '',
		orgLimit: '',
		hkEntName: '',  //香港-公司名称
		hkSignCdate: '', //香港-签发日期
		hkBusiName: '', //香港-业务所用名称
		hkAddr: '', //香港-地址
		hkEffectiveDate: '', //生效日期
		twEntName: '', //台湾-公司名称
		twSignCdate: '', //台湾-签发日期
		twFaxEntName: '', //台湾-税籍登记证-公司名称
		twFaxSignCdate: '', //台湾-税籍登记证-签发日期
		twFaxCode: '',  //台湾-税务编码
		abroadEntName: '', //ABROAD_ENT_NAME
		abroadEntNum: '' //ABROAD_ENT_NUM
		
	},
	created: function() {
		var _this = this;
		var categoryUrl = ykyUrl.database + '/v1/category/companytype';
		var industryUrl = ykyUrl.database + '/v1/category/industry';
		syncData(categoryUrl, 'get', {}, function(data) {
			if(data) {
				_this.initData.companytypeList = data;
			}
		});
		syncData(industryUrl, 'get', {}, function(data) {
			if(data) {
				_this.initData.industryList = data;
			}
		});
	},
	updated: function() {
		var _this = this;
		$('.datetimepicker').datepicker({
			format: 'yyyy-mm-dd',
			autoclose: true
		}).on('change', function() {
			var attrName = $(this).attr('name');
			_this[attrName] = $(this).val();
		});
	},
	mounted: function() {
		syncData(API.getEntInfo, 'GET', null, function(data) {
			if(data) {
				renderData(data);
			}
		});
		
		//修改记录
		record();
		
		//表单校验
		validator();
	},
	methods: {
		//公司类型
		getCompanytype: function(category, corCategoryOther) {
			var companyType = '';
			
			if(!category) return '-';
			
			$.each(app.initData.companytypeList, function(i, item) {
				if(item.categoryId == category) {
					companyType = item.categoryName;
					
					if(category == '2006') {
						companyType += '('+ corCategoryOther +')';
					}
					
					return false;
				}
			})
			return companyType;
		},
		//账期管理
		clickAccountPeriod: function() {
			location.href = ykyUrl._this + '/certificationEntKf.htm?action=editAccountPeriod&id=' + getQueryString("id");
		},
		//跳转子账号管理页面
		clickSon: function() {
			location.href = ykyUrl._this + '/certificationEntKf.htm?action=editSonAccount&id=' + getQueryString('id');
		},
		//行业
		getIndustry: function(industry, otherAttr) {
			var industryList = [];
			
			if(!industry) return '-';
			
			$.each(industry.split(','), function(i, item) {
				var name = industryIdToName(item, otherAttr);
				industryList.push(name);
			})
			
			return industryList.join(',');
		},
		//公司注册地
		regAddrName: function(str) {
			var returnStr = '';
			switch(str) {
				case '0':
					returnStr = '大陆';
					break;
				case '1':
					returnStr = '香港';
					break;
				case '2':
					returnStr = '台湾';
					break;
				case '3':
					returnStr = '境外';
					break;
			}
			return returnStr;
		},
        //从mogodb获取的数据，私有图片需转换 add by helinmei
        getImage: function (imageUrl) {

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
        showPic: function(id,event) {
            showVoucherPic($(event.target));
		},
		//取消
        cancel: function () {
        	location.href = location.pathname;
        }
	}
})

//根据行业ID拿到行业的名称
function industryIdToName(id, otherAttr) {
	var industryName = '';
	
	$.each(app.initData.industryList, function(i, item) {
		if(item.categoryId == id) {
			industryName = item.categoryName;
			if(id == '1008' && otherAttr) {
				industryName += '('+ otherAttr +')';
			}
			return false;
		}
	})
	return industryName;
}

//读取数据
function renderData(data) {
	app.name = data.name;
	app.corCategory = data.map && data.map.CORPORATION_CATEGORY_ID;
    app.corCategoryOther = data.map && data.map.CORPORATION_CATEGORY_ID_OTHER;
	app.industry = data.map && data.map.INDUSTRY_CATEGORY_ID;
    app.industryOther = data.map && data.map.INDUSTRY_CATEGORY_ID_OTHER;
	app.province = data.province;
	app.city = data.city;
	app.country = data.country;
	app.provinceName = data.provinceName;
	app.cityName = data.cityName;
	app.countryName = data.countryName;
	app.address = data.address;
	app.dCode = data.map && data.map.D_CODE || '-';
	app.webSite = data.map && data.map.WEBSITE_URL || '-';
	app.partyCode = data.partyCode || '-';
	app.creditComments = data.creditComments || '-';
	app.activeStatus = data.activeStatus;
	app.regAddr = data.map && data.map.REG_ADDR;
	app.busiLisType = data.map && data.map.BUSI_LIS_TYPE;
    app.busiLicPic = data.map && data.map.BUSI_LIC_PIC || '';
    app.oocPic = data.map && data.map.OCC_PIC || '';
    app.taxPic = data.map && data.map.TAX_REG_PIC || '';
    app.busiPdfName = data.map && data.map.BUSI_PDF_NAME || '';
    app.oocPdfName = data.map && data.map.OCC_PDF_NAME || '';
    app.taxPdfName = data.map && data.map.TAX_PDF_NAME || '';
    app.socialCode = data.map && data.map.SOCIAL_CODE || '-';
    app.entName = data.map && data.map.ENT_NAME || '-';
    app.location = data.map && data.map.LOCATION || '-';
    app.busiRange = data.map && data.map.BUSI_RANGE || '-';
    app.faxCode = data.map && data.map.FAX_CODE || '-';
    app.faxName = data.map && data.map.FAX_NAME || '-';
    app.orgCode = data.map && data.map.ORG_CODE || '-';
    app.orgName = data.map && data.map.ORG_NAME || '-';
    app.orgLocation = data.map && data.map.ORG_LOCATION || '-';
    app.orgCdate = data.map && data.map.ORG_CDATE || '-';
    app.orgLimit = data.map && data.map.ORG_LIMIT || '-';
    app.hkEntName = data.map && data.map.HK_ENT_NAME || '-';
    app.hkSignCdate = data.map && data.map.HK_SIGN_CDATE || '-';
    app.hkBusiName = data.map && data.map.HK_BUSI_NAME || '-';
    app.hkAddr = data.map && data.map.HK_ADDR || '-';
    app.hkEffectiveDate = data.map && data.map.HK_EFFECTIVE_DATE || '-';
    app.twEntName = data.map && data.map.TW_ENT_NAME || '-';
    app.twSignCdate = data.map && data.map.TW_SIGN_CDATE || '-';
    app.twFaxEntName = data.map && data.map.TW_FAX_ENT_NAME || '-';
    app.twFaxSignCdate = data.map && data.map.TW_FAX_SIGN_CDATE || '-';
    app.twFaxCode = data.map && data.map.TW_FAX_CODE || '-';
    app.abroadEntName = data.map && data.map.ABROAD_ENT_NAME || '-';
    app.abroadEntNum = data.map && data.map.ABROAD_ENT_NUM || '-';
    
    /*省市区*/
    app.setProvince = data.province;
    app.setCity = data.city;
    app.setDistrict = data.country;
}

//校验
function validator() {
	$('#infoFrom').bootstrapValidator({   
	    feedbackIcons: {  
	        valid: 'glyphicon glyphicon-ok',  
	        invalid: 'glyphicon glyphicon-remove',  
	        validating: 'glyphicon glyphicon-refresh'  
	    },
	    fields: {  
	    	entName: {   
	            validators: {  
	                notEmpty: {  
	                    message: '公司名称不能为空'  
	                },  
	                stringLength: { 
	                	min: 4,
	                    max: 50,  
	                    message: '公司名称长度只能在4-50位字符之间'  
	                }
	            },
	            container: '.tips'
	        },  
	    },
	    live: 'enabled,submitted ',
	    submitButtons: '#save',
	    submitHandler: function (validator, form, submitButton) {  
	    	
	    } 
	}).on('success.form.bv', function(e){
		e.preventDefault();
		save();
	}); 
}


//修改记录
function record() {
	syncData(API.applyRecord('EntUpdateName Modify'), 'GET', null, function(data) {
		if(data && data.list) {
			app.applyRecord = data.list;
		}
	})
}

//保存
function save() {
	
	$.aAjax({//保存企业信息
		url:ykyUrl.party + '/v1/enterprises/' + getQueryString("id") + '/companyName',
		type: "PUT",
		data: app.name,
		success:function(data){
			layer.msg('提交成功！');
			
			setTimeout(function() {
				location.reload();
			}, 1000);
		},
		error:function(){
			console.log("error");
		}
	})
}

