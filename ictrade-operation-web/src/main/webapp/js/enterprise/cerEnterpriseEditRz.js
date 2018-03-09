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
		applyRecord: [], //修改记录
		name: '',	//公司名称
		corCategory : '', //公司类型
		corCategoryOther : '', //公司类型-其他
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
		readPartyCode: false,
		creditComments: '',	//信用备注
		reason: '',	//修改记录
		activeStatus: '',	//认证状态
		initData: {
			selectedIndustry: [],	//选中的行业
			tempSelectIndustry: [],	//行业选择浮层选中临时存储选中的行业
			tempCancelSelectIndustry: [],	//行业选择浮层选中临时存储选中的行业
			otherAttr: '',
			isShowIndustryOther: false,
			companytypeList: {},	//公司类型列表
			industryList: {},	//行业列表
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
		abroadEntNum: '', //ABROAD_ENT_NUM
		selectBox:{
			 //组件使用参数
            api: ykyUrl.database + '/v1/geos/{parentGeoId}/BELONG_TO',//设置请求城市数据接口URL
            inputClass : 'l-input', //设置控件class
            nameProvince : 'company[province]',
            nameCity : 'company[city]',
            nameDistrict : 'company[district]',
            setProvince:'',
            setCity:'',
            setDistrict:'',
            parentId: 'parentGeoId',
            isRead:	true,	//是不是第一次读取
            queryParam:{    //参数
            	parentGeoId:'100000'
            }
		},
		partyCodeExit: false	//判断易库易编码是否存在
	},
	computed: {
		counter: function() {//字数统计
			return this.creditComments.length;
		}
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
		
	},
	watch: {
		'initData.selectedIndustry': function() {
			if(this.industry.indexOf('1008')) {
				this.industryOther = this.initData.otherAttr;
			}
			this.industry = this.initData.selectedIndustry.join(',');
		},
		'initData.otherAttr': function() {
			this.industryOther = this.initData.otherAttr;
		},
		'industry': function() {
			this.initData.selectedIndustry = this.industry.split(',');
		},
		'industryOther': function() {
			this.initData.otherAttr = this.industryOther;
			if(this.industryOther) {
				this.initData.isShowIndustryOther = true;
			}
		},
		'busiLisType': function() {
			//表单校验
			this.$nextTick(function() {
				validator();
			});
		}
	},
	filters: {
		findIndustryName: function(value) {
			var industryList = app.initData.industryList;
			var returnValue = '';
			
			$.each(industryList, function(i, item) {
				if(item.categoryId == value) {
					returnValue = item.categoryName;
					
					if(value == '1008') {
						returnValue += '('+ app.initData.otherAttr +')';
					}
					
					return false;
				}
			})
			
			return returnValue;
		},
		toJson: function(jsonStr, key) {
			return JSON.parse(jsonStr)[0][key] || '-';
		}
	},
	methods: {
		//子账号管理
		clickSon: function() {
			location.search = 'action=detailSonAccount&source=certificationEntRz&id=' + getQueryString('id');
		},
		//账期管理
		clickAccountPeriod: function() {
			location.search = 'action=detailAccountPeriod&source=certificationEntRz&id=' + getQueryString("id");
		},
		//字数统计
		chCounter: function(key, size) {
			this[key] = this[key].substr(0, size);
		},
		change: function (data) {//data：返回选中的结果；该方法在组件中有调用 ，必须
            this.province = data.province || '';//将结果赋值到对象province
            this.city = data.city || '';
            this.country = this.district = data.district || '';
            this.provinceName = data.province ? $('#province :selected').text() : '';
            this.cityName = data.city ? $('#city :selected').text() : '';
            this.countryName = data.district ? $('#district :selected').text() : '';
        },
		//公司类型
		getCompanytype: function(category) {
			var companyType = '';
			
			if(!category) return '';
			
			$.each(app.initData.companytypeList, function(i, item) {
				if(item.categoryId == category) {
					companyType = item.categoryName;
					return false;
				}
			})
			return companyType;
		},
		//校验YKY编码
		validateCode: function() {
			validateYkyCode();
		},
		//行业
		getIndustry: function(industry, otherAttr) {
			var industryList = [];
			
			if(!industry) return '';
			
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
        //显示行业选择浮层
        toggleIndustry: function() {
        	var _this = this;
        	
        	$('.industry_content').show();
        	
        	$('.company_type span').removeClass('check-box-red');
        	$('.company_type span').each(function() {
        		var id = $(this).attr('data-id');
        		var selectedIndustry = _this.initData.selectedIndustry;
        		if(selectedIndustry.indexOf(id) != -1) {
        			$(this).addClass('check-box-red');
        		}
        		
        	})
        	
        },
        //确认行业选择
        confirmIndustry: function() {
        	var _this = this;
        	
        	if(this.initData.otherAttr == '' && $('#industryOther').is(':visible')) {
        		$('#industryOther').addClass('hong');
        		return;
        	}
        	
        	$.each(this.initData.tempSelectIndustry, function(i, item) {
        		if(_this.initData.selectedIndustry.indexOf(item) == -1) {
        			_this.initData.selectedIndustry.push(item);
        		}
        	})
        	
        	$.each(this.initData.tempCancelSelectIndustry, function(i, item) {
        		var index = _this.initData.selectedIndustry.indexOf(item);
        		if(index != -1) {
        			_this.initData.selectedIndustry.splice(index, 1);
        		}
        	})
        	
        	_this.initData.selectedIndustry.sort();
        	
        	if(_this.initData.selectedIndustry.indexOf('1008') == -1) {
        		_this.initData.otherAttr = '';
        	}
        	
        	this.initData.tempSelectIndustry = [];
        	this.initData.tempCancelSelectIndustry = [];
        	$('.industry_content').hide();
        },
        //取消行业选择
        cancelIndustry: function() {
        	$('.industry_content').hide();
        	this.initData.tempSelectIndustry = [];
        	this.initData.tempCancelSelectIndustry = [];
        	this.initData.isShowIndustryOther = this.initData.selectedIndustry.indexOf('1008') != -1;
        },
        //选择行业
        selectIndustryItem: function(event) {
        	var target = $(event.target).closest('.company_type').find('span');
        	var tempSelectIndustry = this.initData.tempSelectIndustry;
        	var tempCancelSelectIndustry = this.initData.tempCancelSelectIndustry;
        	var id = target.attr('data-id');
        	target.toggleClass('check-box-red');
        	
        	if(id == '1008') {
        		this.initData.isShowIndustryOther = !this.initData.isShowIndustryOther;
        	}
        	
        	if(target.hasClass('check-box-red')) {
        		this.initData.tempSelectIndustry.push(id);
        		this.initData.tempSelectIndustry.sort();
        		
        		this.initData.tempCancelSelectIndustry.splice(tempCancelSelectIndustry.indexOf(id), 1);
        	}else {
        		
        		this.initData.tempCancelSelectIndustry.push(id);
        		this.initData.tempCancelSelectIndustry.sort();
        		
        		this.initData.tempSelectIndustry.splice(tempSelectIndustry.indexOf(id), 1);
        		
        		if(id == '1008') {
        			$('#industryOther').removeClass('hong');
        		}
        	}
        },
        //移除选中
        removeSelect: function(event) {
        	var id = $(event.target).parent('.area-item').attr('data-id');
        	var index = this.initData.selectedIndustry.indexOf(id);
        	this.initData.selectedIndustry.splice(index, 1);
        	
        	if(id == '1008') {
        		this.initData.isShowIndustryOther = false;
        		this.initData.otherAttr = '';
        	}
        	
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
	app.initData.otherAttr = app.industryOther = data.map && data.map.INDUSTRY_CATEGORY_ID_OTHER;
	app.province = data.province;
	app.city = data.city;
	app.country = data.country;
	app.provinceName = data.provinceName;
	app.cityName = data.cityName;
	app.countryName = data.countryName;
	app.address = data.address;
	app.dCode = data.map && data.map.D_CODE || '';
	app.webSite = data.map && data.map.WEBSITE_URL || '';
	app.partyCode = data.partyCode || '';
	
	if(app.partyCode) {
		app.readPartyCode = true;
	}
	
	app.creditComments = data.creditComments || '';
	app.activeStatus = data.activeStatus;
	app.regAddr = data.map && data.map.REG_ADDR;
	app.busiLisType = data.map && data.map.BUSI_LIS_TYPE;
    app.busiLicPic = data.map && data.map.BUSI_LIC_PIC || '';
    app.oocPic = data.map && data.map.OCC_PIC || '';
    app.taxPic = data.map && data.map.TAX_REG_PIC || '';
    app.busiPdfName = data.map && data.map.BUSI_PDF_NAME || '';
    app.oocPdfName = data.map && data.map.OCC_PDF_NAME || '';
    app.taxPdfName = data.map && data.map.TAX_PDF_NAME || '';
    app.socialCode = data.map && data.map.SOCIAL_CODE || '';
    app.entName = data.map && data.map.ENT_NAME || '';
    app.location = data.map && data.map.LOCATION || '';
    app.busiRange = data.map && data.map.BUSI_RANGE || '';
    app.faxCode = data.map && data.map.FAX_CODE || '';
    app.faxName = data.map && data.map.FAX_NAME || '';
    app.orgCode = data.map && data.map.ORG_CODE || '';
    app.orgName = data.map && data.map.ORG_NAME || '';
    app.orgLocation = data.map && data.map.ORG_LOCATION || '';
    app.orgCdate = data.map && data.map.ORG_CDATE || '';
    app.orgLimit = data.map && data.map.ORG_LIMIT || '';
    app.hkEntName = data.map && data.map.HK_ENT_NAME || '';
    app.hkSignCdate = data.map && data.map.HK_SIGN_CDATE || '';
    app.hkBusiName = data.map && data.map.HK_BUSI_NAME || '';
    app.hkAddr = data.map && data.map.HK_ADDR || '';
    app.hkEffectiveDate = data.map && data.map.HK_EFFECTIVE_DATE || '';
    app.twEntName = data.map && data.map.TW_ENT_NAME || '';
    app.twSignCdate = data.map && data.map.TW_SIGN_CDATE || '';
    app.twFaxEntName = data.map && data.map.TW_FAX_ENT_NAME || '';
    app.twFaxSignCdate = data.map && data.map.TW_FAX_SIGN_CDATE || '';
    app.twFaxCode = data.map && data.map.TW_FAX_CODE || '';
    app.abroadEntName = data.map && data.map.ABROAD_ENT_NAME || '';
    app.abroadEntNum = data.map && data.map.ABROAD_ENT_NUM || '';
    
    /*省市区*/
    app.selectBox.setProvince = app.province;
    app.selectBox.setCity = app.city;
    app.selectBox.setDistrict = app.country;
}

//修改记录
function record() {
	syncData(API.applyRecord('EntQualifications Modify'), 'GET', null, function(data) {
		if(data && data.list) {
			app.applyRecord = data.list;
		}
	})
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
	    	corCategoryOther: {   
	            validators: {  
	                notEmpty: {  
	                    message: '其他行业不能为空'  
	                }, 
	                regexp: {
	                	regexp: /^[a-zA-Z\u4e00-\u9fa5\s`\-\=~\!@#$%\^&\*\(\)_\+\[\]\{\}\\\|;':",\.\/<>\?。 ？ ！ ， 、 ； ： “ ” ‘ ' （ ） 《 》 〈 〉 【 】 『 』 「 」 ﹃ ﹄ 〔 〕 … — ～ ﹏ ￥×＠＃％＆＋｛｝｜／｀１２３４５６７８９０－＝’·]{1,30}$/,
	                	message: '1-30位中文、英文、符号及组合'
	                }
	            },
	            container: '.tips1'
	        },  
	        address: {   
	            validators: {  
	                stringLength: {
	                	max: 250,
	                	message: '详细地址长度不能超过250位'
	                }
	            },
	            container: '.tips2'
	        }, 
	        webSite: {
	        	validators: {  
	                regexp: {
	                	regexp: /^((https|http|ftp|rtsp|mms):\/\/)?(([0-9a-z_\!~\*'\(\)\.&\=\+$%\-]+:)?[0-9a-z_\!~\*'\(\)\.&\=\+$%\-]+@)?(([0-9]{1,3}\.){3}[0-9]{1,3}|([0-9a-z_\!~\*'\(\)\-]+\.)*([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\.[a-z]{2,3})(:[0-9]{1,4})?((\/?)|(\/[0-9a-z_\!~\*'\(\)\.;\?:@&\=\+$,%#\-]+)+\/?)$/gi,
	                	message: '请输入正确的网址，如http://www.yikuyi.com！'
	                }
	            },
	            container: '.tips3'
	        },
	        socialCode: {
	        	validators: {
		        	notEmpty: {  
	                    message: '统一社会信用代码不能为空'  
	                }
	        	},
	            container: '.none'
	        },
	        orgCode: {
	        	validators: {
		        	notEmpty: {  
	                    message: '组织机构代码不能为空'  
	                }
	        	},
	            container: '.none'
	        }
	    },
	    live: 'enabled,submitted',
	    submitButtons: '#save',
	    submitHandler: function (validator, form, submitButton) {  
	    	 
	    } 
	}).on('success.form.bv', function(e){
		e.preventDefault();
		save();
	});
}

//校验易库易编码
function validateYkyCode() {
	if(app.partyCode) {
		$.aAjax({
			url: ykyUrl.party + '/v1/partycode/' + app.partyCode,
			type: 'GET',
			async: false,
			success: function(data) {
				
				if(data != '') {
					layer.msg('易库易编码'+ data +'已存在，请重新输入！');
					app.partyCodeExit = true;
				}else {
					app.partyCodeExit = false;
				}
				
			},
			error: function(e) {
				throw '接口错误, Error: ' + e.responseText;
			}
		})
	}
}

//保存
function save() {
	
	if(!app.industry) {
		layer.msg('请选择所属行业');
		return;
	}
	
	if(!app.province || !app.city || !app.country) {
		layer.msg('请选择省市区');
		return;
	}
	
	var entInformation = {
		"id": getQueryString("id"),//id
		"name": app.name,//公司名称
		"corCategory": app.corCategory,//公司类型
		"industryCategory": app.industry,//所属行业
		"otherAttrs": [ 
		                {"INDUSTRY_CATEGORY_ID_OTHER": app.industryOther},
						{"CORPORATION_CATEGORY_ID_OTHER": app.corCategoryOther}
	                ], //所属行业其他
		"province": app.province,//省id
		"provinceName": app.provinceName,//省name
		"city": app.city,//市id
		"cityName": app.cityName,//市name
		"country": app.country,//县id
		"countryName": $('#district :selected').text() || '',//县name
		"address": app.address,//公司地址
		"dCode": app.dCode,//邓氏编码
		"webSite": app.webSite,//公司官网
		"partyCode": app.partyCode,
		"creditComments": app.creditComments, //信用备注
		"reason": app.reason, //修改记录
		 map: { //三证信息
			SOCIAL_CODE: app.socialCode,
			ENT_NAME: app.entName,
			LOCATION: app.location,
			BUSI_RANGE: app.busiRange,
			FAX_CODE: app.faxCode,
			FAX_NAME: app.faxName,
			ORG_CODE: app.orgCode,
			ORG_LOCATION: app.orgLocation,
			ORG_NAME: app.orgName,
			ORG_CDATE: app.orgCdate,
			ORG_LIMIT: app.orgLimit,
			HK_ENT_NAME: app.hkEntName,  //香港-公司名称
			HK_SIGN_CDATE: app.hkSignCdate, //香港-签发日期
			HK_BUSI_NAME: app.hkBusiName, //香港-业务所用名称
			HK_ADDR: app.hkAddr, //香港-地址
			HK_EFFECTIVE_DATE: app.hkEffectiveDate, //生效日期
			TW_ENT_NAME: app.twEntName, //台湾-公司名称
			TW_SIGN_CDATE: app.twSignCdate, //台湾-签发日期
			TW_FAX_ENT_NAME: app.twFaxEntName, //台湾-税籍登记证-公司名称
			TW_FAX_SIGN_CDATE: app.twFaxSignCdate, //台湾-税籍登记证-签发日期
			TW_FAX_CODE: app.twFaxCode, //台湾-税务编码
			ABROAD_ENT_NAME: app.abroadEntName, //ABROAD_ENT_NAME
			ABROAD_ENT_NUM: app.abroadEntNum, //ABROAD_ENT_NUM
			REG_ADDR: app.regAddr,//公司注册地
			BUSI_LIS_TYPE: app.busiLisType,//注册类型
			BUSI_LIC_PIC: app.busiLicPic,
			OCC_PIC: app.oocPic,
			TAX_REG_PIC: app.taxPic,
			BUSI_PDF_NAME: app.busiPdfName,
			OCC_PDF_NAME: app.oocPdfName,
			TAX_PDF_NAME: app.taxPdfName,
			LOA: app.loaPic,
			LOA_PDF_NAME: app.loaPdfName

		}
	}
	
	if(!app.readPartyCode && app.partyCode) {  //如果第一次编辑易库易编码需要判断唯一性
		
		validateYkyCode();
		if(app.partyCodeExit) {
			return;
		};
		
		//保存yky编码
		var partyVO = {
			partyId: getQueryString('id'),
			codePrefix: app.partyCode
		}
		
		$.aAjax({
			url:ykyUrl.party + '/v1/partycode?partyId='+ partyVO.partyId +'&partyCode=' + partyVO.codePrefix,
			type: "POST",
			async: false,
			/*data:JSON.stringify(partyVO),*/
			success:function(data){
				 
			},
			error:function(e){
				throw '接口错误,Err: ' + e.responseText;
			}
		});
	}
	
	$.aAjax({//保存企业信息
		url:ykyUrl.party + "/v1/enterprises/updateQualifications",
		type: "POST",
		async: false,
		data:JSON.stringify(entInformation),
		success:function(data){
			layer.msg('保存成功！');
			
			setTimeout(function() {
				location.reload();
			}, 1000);
		},
		error:function(){
			console.log("error");
		}
	})
}