$(function(){
	$("#save").on('click',function(){
		var _this = this;
		setTimeout(function(){
			$(_this).removeAttr('disabled');
		},500);
	})
})

var API = {
	entInfo: ykyUrl.party + '/v1/enterprises/entDetail/' + getQueryString('id') + '/' + getQueryString('corporationId') + '/CORPORATION',
	contactInfo: ykyUrl.party + '/v1/customers/' + getQueryString('id')
}

var companyAddressChangeCount = 0;
var contactAddressChangeCount = 0;

var editApp = new Vue({
	el: '#edit',
	data: {
		checkBoxLayer: {
            listDataUrl: ykyUrl.database + '/v1/category/industry',
            selectedData: [],
            otherAttr: ''
        },
		initData: {
			companytypeList: {},	//公司类型列表
			industryList: {},	//行业列表
			positionList:[{"id":"buyer","name":"采购员"},{"id":"purchasingManager","name":"采购经理"},{"id":"engineer","name":"工程师"},{"id":"other","name":"其他"}] //职业列表
		},
		entInfo: {
			id: getQueryString("id"),									//个人编码
			corporationId: getQueryString('corporationId'),				//企业编码
			name: '',													//公司名称
			categoryId: '',												//公司类型
			otherCategory: '',										//公司类型-其他
			industryCategory: '',										//所属行业
			otherIndustryCategory: '',									//所属行业-其他
			province: '',												//省ID
			city: '',													//市ID
			country: '',												//区ID
			provinceName: '',											//省名字
			cityName: '',												//市名字
			countryName: '',											//区名字
			address: '',												//详细地址
			dCode: '',													//邓氏编码
			website: '',												//公司官网
			partyCode: '-',												//YKY客户编码
		},
		quaData: {
			activeStatus: '',	
			regAddr: '0',												//公司注册地
			busilisType: '3-TO-1',											//营业执照类型
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
			province: '',												//省ID
			city: '',													//市ID
			country: '',												//区ID
			provinceName: '',											//省名字
			cityName: '',												//市名字
			countryName: '',											//区名字
			address: ''													//详细地址
		},
		companySelect:{
			 //组件使用参数
           api: ykyUrl.database + '/v1/geos/{parentGeoId}/BELONG_TO',//设置请求城市数据接口URL
           inputClass : 'l-input', //设置控件class
           nameProvince : 'company[province]',
           nameCity : 'company[city]',
           nameDistrict : 'company[district]',
           idProvince : 'cProvince',//设置id,可选
           idCity : 'cCity',//设置id,可选
           idDistrict : 'cDistrict',//设置id,可选
           setProvince:'',
           setCity:'',
           setDistrict:'',
           parentId: 'parentGeoId',
           isRead:	true,	//是不是第一次读取
           queryParam:{    //参数
           	parentGeoId:'100000'
           }
		},
		contactSelect:{	//组件使用参数
           api: ykyUrl.database + '/v1/geos/{parentGeoId}/BELONG_TO',//设置请求城市数据接口URL
           inputClass : 'l-input', 					//设置控件class
           nameProvince : 'company[province]',
           nameCity : 'company[city]',
           nameDistrict : 'company[district]',
           setProvince:'',
           setCity:'',
           setDistrict:'',
           parentId: 'parentGeoId',
           isRead:	true,							//是不是第一次读取
           queryParam:{    							//参数
           	parentGeoId:'100000'
           }
		},
		loginUserName: '',							//登录人名字
		isApply: getQueryString('action') == 'entAdd' ? 3 : 0,	//标志位，判断改动了哪些数据 0修改了除三证之外的数据或者没改动 	1 修改了三证信息	3 新增企业
		reason: '',			//修改说明	
		comments: ''								//客服备注         
	},
	created: function() {
		getCategoryList(this);
		
		if(this.isApply !== 3) {
			getEntInfo(this);
			getContactInfo(this);
		}
		this.$nextTick(formValidator);
	},
	methods: {
		//认证组件回调
		zzDataChange: function(type, value) {
			this.quaData[type] = value;
			if(getQueryString('action')!=='entAdd') {
				this.isApply = 1;
			}
		},
		//行业选择框回调1
		selectedDataChange: function (value) {
	        this.checkBoxLayer.selectedData = value;
	        this.entInfo.industryCategory = value.join(',');
	    },
	    //行业选择框回调2
	    attrChange: function (value) {
	       this.checkBoxLayer.otherAttr = value;
           this.entInfo.otherIndustryCategory = value;
	    },
		//企业信息-地址联动
		companyAddressChange: function (data) {
			companyAddressChangeCount += 1;
			
			this.entInfo.province = data.province || '';
			this.entInfo.city = data.city || '';
			this.entInfo.country = data.district || '';
			
			if(companyAddressChangeCount > 3) {
				this.entInfo.address = '';
			}
        },
        //联系人-地址联动
        contactAddressChange:function(data){
        	
        	contactAddressChangeCount += 1;
        	
			this.contactInfo.province = data.province || '';
			this.contactInfo.city = data.city || '';
			this.contactInfo.country = data.district || '';
			
			if(contactAddressChangeCount > 4) {
				this.contactInfo.address = '';
			}
        },
		//保存
        saveData:function(){        	
        	save(this);
        },
        cancel: function () {
        	location.href = location.pathname;
        }
	}        	
})

//保存
function save() {
	
	//所属行业、地址校验
	if(!editApp.entInfo.industryCategory) {
		layer.msg('所属行业不能为空');
		return false;
	}
	
	if(editApp.entInfo.province) {
		if(!(editApp.entInfo.city && editApp.entInfo.country && editApp.entInfo.address)) {
			layer.msg('公司地址请填写完整');
			return false;
		}
	}
	
	if(editApp.contactInfo.province) {
		if(!(editApp.contactInfo.city && editApp.contactInfo.country && editApp.contactInfo.address)) {
			layer.msg('联系地址请填写完整');
			return false;
		}
	}
	
	var entInfo = editApp.entInfo;
	var contactInfo = editApp.contactInfo;
	var quaData = editApp.quaData;
	var requestData = {};
	
	//查询当前登录用户名和邮箱
	  $.aAjax({
			url:ykyUrl.party + "/v1/customers/"+$("#applyUserId").val()+"/username",
			type: "GET",
			contentType: 'application/json',
			async: false,
			success:function(data){
				if(data){
					editApp.loginUserName = data.lastNameLocal;	
				} 
			},
			error:function(){
				console.log("error");
			}
		})
	
	var applyContent = {
		"id": entInfo.corporationId,  //企业id
	    "entUserId": entInfo.id || $("#applyUserId").val(),//用户id
	    "name": entInfo.name,//公司名称
	    "corCategory": entInfo.categoryId,//公司类型code
	    "industryCategory": entInfo.industryCategory,//所属行业code
	    "otherAttrs": [{"INDUSTRY_CATEGORY_ID_OTHER": entInfo.otherIndustryCategory},{"CORPORATION_CATEGORY_ID_OTHER": entInfo.otherCategory}],//公司类型，所属行业其他输入值
	    "province": entInfo.province,//省份code
	    "provinceName": entInfo.province ? $('#cProvince option[value='+ entInfo.province +']').text() : '',//省份名
	    "city": entInfo.city,//城市code
	    "cityName": entInfo.city ? $('#cCity option[value='+ entInfo.city +']').text() : '',//城市名
	    "country": entInfo.country,//县code
	    "countryName": entInfo.country ? $('#cDistrict option[value='+ entInfo.country +']').text() : '',//县名
	    "address": entInfo.address,//详细地址
	    "dCode": entInfo.dCode,//邓氏编码
	    "partyCode": entInfo.entInfo,	//易库易编码
	    "webSite": entInfo.website,//公司网址
	    "reason": editApp.reason,//修改说明
	    "comments": editApp.comments,//修改备注
	    "personName": contactInfo.name,//个人联系人名称
	    "contactUserTel": contactInfo.telNumber,//联系电话
	    "mail": contactInfo.mail,//邮箱
	    "contactUserQQ": contactInfo.qq,//联系qq
	    "personalTitle": contactInfo.personalTitle,//职位
	    "personAddress": contactInfo.address,//个人详细地址
	    "personProvince": contactInfo.province,//省份code
	    "personProvinceName": contactInfo.province ? $('#province option[value='+ contactInfo.province +']').text() : '',//省份名
	    "personCity": contactInfo.city,//城市code
	    "personCityName": contactInfo.city ? $('#city option[value='+ contactInfo.city +']').text() : '',//城市名
	    "personCountry": contactInfo.country,//县code
	    "personCountryName": contactInfo.country ? $('#district option[value='+ contactInfo.country +']').text() : '',//县名
	    "fixedTel": contactInfo.fixedTel,//固定电话
	    "isVipCenter": "0",//从后台编辑判断
	    "contactUserName": editApp.loginUserName,//当前登录人名字
	    "map": {
	        "REG_ADDR": quaData.regAddr,
	        "BUSI_LIS_TYPE": quaData.busilisType,
	        "BUSI_LIC_PIC": quaData.busilicPic,
	        "TAX_REG_PIC": quaData.taxPic,
	        "OCC_PIC": quaData.oocPic,
	        "BUSI_PDF_NAME": quaData.busiPdfName,
	        "TAX_PDF_NAME": quaData.taxPdfName,
	        "OCC_PDF_NAME": quaData.oocPdfName
	    },
	    "applyUser": contactInfo.name,
	    "applyMail": contactInfo.mail,
	    "applyInformation": contactInfo.telNumber,
		"isApply": editApp.isApply//用来判断是新增还是编辑，如果为3则表示新增企业，0表示只修改了企业或者个人信息，1表示修改了3证
	}
	
	if(getQueryString('action') == 'entAdd') {
		requestData = {
			"applyContent": JSON.stringify(applyContent),
			"applyUserId": $("#applyUserId").val(),
		    "processId": "ORG_DATA_REVIEW",
		    "reason": editApp.reason,
		}
	}else {
		requestData = {
			"applyContent": JSON.stringify(applyContent),
			"applyUserId": $("#applyUserId").val(),
		    "processId": "ORG_DATA_REVIEW",
		    "applyOrgId": editApp.entInfo.corporationId,
		    "reason": editApp.reason,
		}
	}
	
	
	$.aAjax({
		url: ykyUrl.party + '/v1/enterprises/editEntApply',
		type: 'POST',
		data: JSON.stringify(requestData),
		success: function(data) {
			layer.msg('保存成功');
			setTimeout(function() {
				
				location.href = location.pathname;
				
			}, 1000);
		},
		error: function() {
			
		}
	})
}

//校验
function formValidator() {
	
	$('#infoFrom').bootstrapValidator({   
	    feedbackIcons: {//验证后验证字段的提示字体图标  
	        valid: 'glyphicon glyphicon-ok',  
	        invalid: 'glyphicon glyphicon-remove',  
	        validating: 'glyphicon glyphicon-refresh'  
	    },
	    trigger:'change',
	    fields: {  
	    	name: {//公司名称
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
	            container: '.name-error'
	        },
	        corCategory:{//公司类型
	        	validators: {
	        		notEmpty: {  
	                    message: '请选择公司类型'  
	                },
	        	},
	        	container: '.corCategory-error'
	        },
	        corCategoryOther:{//其他公司类型
	        	validators: {
	        		notEmpty: {  
	                    message: '请填写公司类型'  
	                },
	        	},
	        	container: '.corCategoryOther-error'
	        },
	        webSite: {//公司官网
	        	validators: {  
	                regexp: {
	                	regexp: /^((https|http|ftp|rtsp|mms):\/\/)?(([0-9a-z_\!~\*'\(\)\.&\=\+$%\-]+:)?[0-9a-z_\!~\*'\(\)\.&\=\+$%\-]+@)?(([0-9]{1,3}\.){3}[0-9]{1,3}|([0-9a-z_\!~\*'\(\)\-]+\.)*([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\.[a-z]{2,3})(:[0-9]{1,4})?((\/?)|(\/[0-9a-z_\!~\*'\(\)\.;\?:@&\=\+$,%#\-]+)+\/?)$/gi,
	                	message: '请输入正确的网址，如http://www.yikuyi.com！'
	                }
	            },
	            container: '.webSite-error'
	        },
	        applyUser: {//联系人姓名
	        	validators: {
	        		notEmpty: {  
	                    message: '联系人姓名不能为空'  
	                },
	                stringLength: {
	                	max: 20,
	                	message: '公司名称长度只能在4-50位字符之间'
	                },
	                regexp: {
	                	regexp: /^[\u4E00-\u9FA5a-zA-Z_\s]+$/,
	                	message: '联系人不可包含除汉字、英文和_外的字符！'
	                }
	        	},
	        	container: '.applyUser-error'
	        },
	        telNumber: {//联系人手机号
	        	validators: {
	        		notEmpty: {  
	                    message: '联系人手机号码不能为空'  
	                },
	                regexp: {
	                	regexp:/^((\+?86)|(\(\+86\)))?(13[0-9][0-9]{8}|15[0-9][0-9]{8}|18[0-9][0-9]{8}|147[0-9]{8}|17[0-9]{9}|1349[0-9]{7})$/,
	                	message:'请填写正确的手机号码'
	                }
	        	},
	        	container: '.telNumber-error'
	        },
	        fixedTel: {
	        	validators: {
	        		regexp: {
	        			regexp: /^[0-9\-]+$/,
		        		message: '请填写正确的固定电话号码'
	        		}
	        	},
	        	container: '.fixedTel-error'
	        },
	        mail:{//联系人邮箱
	        	validators: {
	        		notEmpty: {  
	                    message: '联系人邮箱不能为空'  
	                },
	                regexp: {
	                	regexp:/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i,
	                	message:'请填写正确的电子邮箱'
	                },	               
                    callback: {  
                        message: '一个邮箱只能关联一个企业，该邮箱已有关联企业，请重新填写',                       
                        callback:getQueryString('action') == 'entAdd' ? function(value, validator) {
                        	var mailReg= new RegExp(/^[A-Za-z0-9d]+([-_.]+[A-Za-z0-9d]+)*@([A-Za-z0-9d]+[-.])+[A-Za-zd]{1,5}$/);
                        	if(value && mailReg.test(value)){
                        		var flag = true;
                            	syncData(ykyUrl.party + "/v1/account/"+$.base64.encode(value), 'GET', null, function(data) {
                            		flag=!data; 
                        		},false);
                               return flag;
                        	}else{
                        		return true
                        	}                       	
                        } : function(){
                        	return true
                        } 
                    } 
	        	},
	        	container: '.mail-error'
	        },
	        qq:{//联系人qq
	        	validators: {
	        		regexp: {
	        			regexp:/^[1-9][0-9]{4,19}$/,
	        			message:'由5至20位数字组成，不能以0开头！'
	        		}
	        	},
	        	container:'.qq-error'
	        },
	        reason:{//修改说明
	        	validators: {
	        		notEmpty: {  
	                    message: '修改说明不能为空'  
	                },	                
	        	},
	        	container: '.reason-error'
	        },
	    },
	    live: 'enabled,submitted',
	    submitButtons: '#save',//提交的按钮
	    submitHandler: function (validator, form, submitButton) {  
	    	 
	    } 
	}).on('success.form.bv', function(e){
		e.preventDefault();
		save();
	});
}

//查询企业信息和资质信息
function getEntInfo(editApp) {
	
	$.aAjax({
		url: API.entInfo,
		type: 'GET',
		async: false,
		success: function(data) {
			var map = data.map || {};
			var entInfoData = {
					name: data.name || '',
					categoryId: map && map.CORPORATION_CATEGORY_ID || '',
					otherCategory: map && map.CORPORATION_CATEGORY_ID_OTHER || '',
					industryCategory: map && map.INDUSTRY_CATEGORY_ID || '',
					otherIndustryCategory: map && map.INDUSTRY_CATEGORY_ID_OTHER || '',
					province: data.province || '',
					city: data.city || '',
					country: data.country || '',
					provinceName: data.provinceName || '',
					cityName: data.cityName || '',
					countryName: data.countryName || '',
					address: data.address || '',
					dCode: map && map.D_CODE || '',
					website: map && map.WEBSITE_URL || '',
					partyCode: data.partyCode || ''
			};
			var quaData = {
				activeStatus: data.activeStatus,
				regAddr: map && map.REG_ADDR || '0',
				busilisType: map && map.BUSI_LIS_TYPE || '3-TO-1',
				busiPdfName: map && map.BUSI_PDF_NAME || '',
				busilicPic: map && map.BUSI_LIC_PIC || '',
				taxPdfName: map && map.TAX_PDF_NAME || '',
				taxPic:	map && map.TAX_REG_PIC || '',
				oocPdfName: map && map.OCC_PDF_NAME || '',
				oocPic: map && map.OCC_PIC || ''
			}
			editApp.entInfo = $.extend({}, editApp.entInfo, entInfoData);
			editApp.quaData = $.extend({}, editApp.quaData, quaData);
			
			editApp.comments = data.comments || '';
			
			//行业
			editApp.checkBoxLayer.selectedData = map && map.INDUSTRY_CATEGORY_ID && map.INDUSTRY_CATEGORY_ID.split(',').map(function(item) {return Number(item)}) || [];
			editApp.checkBoxLayer.otherAttr = map && map.INDUSTRY_CATEGORY_ID_OTHER || '';
			
			/*省市区*/
		    editApp.companySelect.setProvince = data.province;
		    editApp.companySelect.setCity = data.city;
		    editApp.companySelect.setDistrict = data.country;
		},
		error: function() {
			
		}
	})
	
}

//查询联系人信息
function getContactInfo(editApp) {
	
	$.aAjax({
		url: API.contactInfo,
		typ: 'GET',
		success: function(data) {
			var contactInfo = {
				name:  data.name || '',
				telNumber: data.telNumber || '',
				mail: data.mail || '',
				fixedTel: data.fixedTel || '',
				qq: data.qq || '',
				personalTitle: data.personalTitle || '',
				city: data.city || '',
				country: data.country || '',
				provinceName: data.provinceName || '',
				cityName: data.cityName || '',
				countryName: data.countryName || '',
				address: data.address || ''
			};
			
			editApp.contactInfo = $.extend({}, editApp.contactInfo, contactInfo);
			
			/*省市区*/
		    editApp.contactSelect.setProvince = data.province;
		    editApp.contactSelect.setCity = data.city;
		    editApp.contactSelect.setDistrict = data.country;
		},
		error: function() {
			
		}
	})
}

//获取公司类型列表
function getCategoryList(editApp) {
	
	var categoryUrl = ykyUrl.database + '/v1/category/companytype';
	//获取公司类型
	syncData(categoryUrl, 'GET', {}, function(data) {
		if(data) {
			editApp.initData.companytypeList = data;				
		}
	},false);
}