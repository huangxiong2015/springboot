$(function(){
	$('.radio-item input').on('change', function() {
		$(this).next('.radio').toggleClass('radio-selected');
		$(this).parent().siblings('.radio-item').find('.radio').toggleClass('radio-selected');
	})
	$("#save").on('click',function(){
		var _this = this;
		setTimeout(function(){
			$(_this).removeAttr('disabled');
		},500);
	})
	zzRenzheng.init('#qual', {
		type: 0,
		busilisType:  '3-TO-1' ,
		imgSrc:{
			BUSI_LIC_PIC: '', //营业执照影印件(大陆)  /  注册证书(香港)
			TAX_REG_PIC: '', //税务登记证影印件(大陆) / 商业登记证(香港)
			OCC_PIC: '',
		}
	});	
})
var app = new Vue({
	el: '#edit',
	data: {
		entInformation:{
			id : getQueryString('id'),
			entUserId: getQueryString('id'),
			isVipCenter: '0',
			name: '',//公司名称
			corCategory: '',//公司类型
			industryCategory: '',//所属行业
			otherAttrs: [],
			province: '',//注册地址省Id
			provinceName: '',//注册地址省名称
			city: '',//注册地址市Id
			cityName: '',//注册地址市名称
			country: '',//注册地址县Id
			countryName: '',//注册地址县名称
			address: '',//详细注册地址
			webSite: '',//公司官网
			dCode: '',//邓氏编码
			personalTitle: '',//联系人职位
			contactUserTel: '',//联系人手机号
			mail: '',//联系人邮箱
			contactUserQQ: '',//联系人qq
			personProvince: '',//联系地址省Id
			personProvinceName: '',//联系地址省名称
			personCity: '',//联系地址市Id
			personCityName: '',//联系地址市名称
			personCountry: '',//联系地址县Id
			personCountryName: '',//联系地址县名称
			personAddress: '',//详细联系地址
			comments: '',//客服备注
			contactUserName:'',//联系人名称
	        fixedTel: '',//固定电话
			map: {
				REG_ADDR: '',
				BUSI_LIS_TYPE: '',
				BUSI_LIC_PIC: '',
				OCC_PIC: '',
				TAX_REG_PIC: '',
				BUSI_PDF_NAME: '',
				TAX_PDF_NAME: '',
				OCC_PDF_NAME: ''
			},
			applyUser:'',//联系人姓名
			applyMail:'',//联系人邮箱
			applyInformation:'',//联系人手机号
		},
		corCategoryOther:'',//其他公司类型
		otherAttr:'',//其他所属行业
		reason:'',//修改说明		
		companytypeList:{},//公司类型列表
		industryList: {},	//行业列表
		industryNameList:{},
		activeIndustryList:[],//选中的行业列表
		temporaryIndustryList:[],//临时行业列表
		isShowIndustryOther:false,//行业是否选中其他
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
		contactSelect:{
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
		positionList:[//职业列表
	              {"id":"buyer","name":"采购员"},
	              {"id":"purchasingManager","name":"采购经理"},
	              {"id":"engineer","name":"工程师"},
	              {"id":"other","name":"其他"}
              ],            
	},
	created: function() {
		var _this = this,
			entInfo = _this.entInformation;
		var categoryUrl = ykyUrl.database + '/v1/category/companytype',
			industryUrl = ykyUrl.database + '/v1/category/industry',
			userInfoUrl = ykyUrl.party + '/v1/customers/'+ $('#applyUserId').val() +'/username',
			basicInfoUrl = ykyUrl.party + '/v1/customers/' + getQueryString('id');
		//获取公司类型
		syncData(categoryUrl, 'GET', {}, function(data) {
			if(data) {
				_this.companytypeList = data;				
			}
		},false);
		//获取所属行业
		syncData(industryUrl, 'GET', {}, function(data) {
			if(data) {
				_this.industryList = data;
				var obj = {};
				$.each(data,function(idx,ele){
					obj[ele.categoryId] = ele.categoryName
				})
				_this.industryNameList = obj;
			}
		},false);
		//根据用户id获取用户姓名
		syncData(userInfoUrl, 'GET', null, function(data){
			if(data){
				entInfo.contactUserName = data.lastNameLocal;				
			}
		})
		syncData(basicInfoUrl, 'GET', null , function(data){
			if(data){
				entInfo.applyUser = data.name;
				entInfo.applyInformation = data.telNumber;
				entInfo.applyMail = data.mail;
				entInfo.contactUserQQ = data.qq;
				entInfo.personalTitle = data.personalTitle;				
				entInfo.fixedTel = data.fixedTel;
				entInfo.personAddress = data.address;
				_this.contactSelect.setProvince = data.province;
				_this.contactSelect.setCity = data.city;
				_this.contactSelect.setDistrict = data.country;
			}
		})
		_this.$nextTick(validator);
	},
	watch: {		
		temporaryIndustryList:function(val,oldVal){
			var flag = false;
			for(var i = 0;i<val.length;i++){
				if(val[i] == 1008){
					flag = true;
					this.isShowIndustryOther = true;
				}
			}
			if(!flag){
				this.isShowIndustryOther = false;				
			}
		},
	},
	methods: {
		//公司地址联动
		companyAddressChange: function (data) {//data：返回选中的结果；该方法在组件中有调用 ，必须
			var entInfo = this.entInformation;
			entInfo.province = data.province || '';//将结果赋值到对象province
			entInfo.city = data.city || '';
			entInfo.country = this.district = data.district || '';
			entInfo.provinceName = data.province ? $('#cProvince :selected').text() : '';
			entInfo.cityName = data.city ? $('#cCity :selected').text() : '';
			entInfo.countryName = data.district ? $('#cDistrict :selected').text() : '';			
            if(entInfo.address && (entInfo.province && entInfo.city && entInfo.country) || (!entInfo.province || !entInfo.city || !entInfo.country) ){
            	$('input[name="address"]').trigger('change');
            }
        },
        //联系人地址联动
        contactAddressChange:function(data){
        	var entInfo = this.entInformation;
        	entInfo.personProvince = data.province || '';//将结果赋值到对象province
        	entInfo.personCity = data.city || '';
        	entInfo.personCountry = this.district = data.district || '';
        	entInfo.personProvinceName = data.province ? $('#province :selected').text() : '';
        	entInfo.personCityName = data.city ? $('#city :selected').text() : '';
        	entInfo.personCountryName = data.district ? $('#district :selected').text() : '';
        	if(entInfo.personAddress && (entInfo.personProvince && entInfo.personCity && entInfo.personCountry) || (!entInfo.personProvince || !entInfo.personCity || !entInfo.personCountry)){
            	$('input[name="personAddress"]').trigger('change');
            }
        },
        //显示行业选择浮层
        toggleIndustry: function(list) {
        	var _this = this;        	
        	$('.industry_content').show();
        	_this.temporaryIndustryList = _this.activeIndustryList;
        },
        //确认行业选择
        confirmIndustry: function(list) {
        	var _this = this;        	
    		if(_this.isShowIndustryOther && !_this.otherAttr){
    			$('input[name="otherAttr"]').css('border-color','#b1191a');
        		$('input[name="otherAttr"]').attr('placeholder','行业不能为空');
        		$('input[name="otherAttr"]').focus();
        		return;
    		}else if(!_this.isShowIndustryOther){    			
    			$('input[name="otherAttr"]').css('border-color','#e4e4ea');
        		$('input[name="otherAttr"]').attr('placeholder','请填写所属行业');
    		}
        	_this.activeIndustryList = list.sort();
        	$('.industry_content').hide();        	
        },
        //取消行业选择
        cancelIndustry: function(list) {
        	var _this = this;
        	var flag = true;
        	$('.industry_content').hide();
        	$.each(list,function(idx,ele){
        		if(ele == 1008){
        			flag = false;
        		}
        	})
        	if(flag){
        		_this.otherAttr = '';
        	}       	
        },
        //移除选中行业
        removeSelect: function(event) {
        	var id = $(event.target).parent('.area-item').attr('data-id');
        	var index = this.activeIndustryList.indexOf(id);
        	this.activeIndustryList.splice(index, 1);        	
        	if(id == '1008') {
        		this.isShowIndustryOther = false;
        		this.otherAttr = '';
        	}        	
        },
		//保存
        saveData:function(){        	
        	var _this = this;
        	var entInformation = this.entInformation;
        	entInformation.contactUserTel = entInformation.applyInformation;
        	entInformation.mail = entInformation.applyMail;
        	entInformation.industryCategory = this.activeIndustryList.join(',');
        	if(!entInformation.industryCategory){
        		layer.msg('请选择所属行业！');
        		return;
        	}        	
        	//校验资质信息
			if($('.BUSI_LIC_PIC').size() > 0) {
				if($('.BUSI_LIC_PIC').attr('src') == '') {
					layer.msg('资质信息不能为空！')
					return;
				}
			}
			
			if($('.TAX_REG_PIC').size() > 0) {
				if($('.TAX_REG_PIC').attr('src') == '') {
					layer.msg('资质信息不能为空！')
					return;
				}
			}
			
			if($('.OCC_PIC').size() > 0) {
				if($('.OCC_PIC').attr('src') == '') {
					layer.msg('资质信息不能为空！')
					return;
				}
			}
			var obj = {
        			CORPORATION_CATEGORY_ID_OTHER:_this.corCategoryOther,//其他公司类型
        			INDUSTRY_CATEGORY_ID_OTHER:_this.otherAttr//其他所属行业
        	}
        	entInformation.otherAttrs.push(obj);
        	//获取资质类型
        	var REG_ADDR = $('.qual-type > span').data('id');
        	var BUSI_LIS_TYPE = (function(reg_addr) {
        		var returnVal = '';
        		var dalu_type = $('.dalu_type .radio-selected').data('name');        		
        		switch(reg_addr + '') {
        			case '0':
        				returnVal = dalu_type;
        				break;
        			case '1':
        				returnVal = 'HK-CODE';
        				break;
        			case '2':
        				returnVal = 'TW-CODE';
        				break;
        			case '3':
        				returnVal = 'ABROAD-CODE';
        				break;
        		}        		
        		return returnVal;        		
        	})(REG_ADDR);
        	//企业资质
        	var map = {
        			REG_ADDR: $('.qual-type > span').attr('data-id'),
    				BUSI_LIS_TYPE: BUSI_LIS_TYPE,
    				BUSI_LIC_PIC: $('.BUSI_LIC_PIC').attr('data-src'),
    				OCC_PIC: $('.OCC_PIC').attr('data-src'),
    				TAX_REG_PIC: $('.TAX_REG_PIC').attr('data-src'),
    				BUSI_PDF_NAME: $('.BUSI_LIC_PIC').attr('data-pdfname'),
    				TAX_PDF_NAME: $('.TAX_REG_PIC').attr('data-pdfname'),
    				OCC_PDF_NAME: $('.OCC_PIC').attr('data-pdfname')
    				}
        	entInformation.map = map;
        	console.log(entInformation);
        	var jsonVal = {
        			applyContent:JSON.stringify(entInformation),
        			applyUserId: getQueryString('id'),
        			processId:'ORG_DATA_REVIEW',
        			reason: _this.reason,
        			applyPageUrl:"",
        			applyOrgId: '',
        			callBackUrl: ''
        		};
        	console.log(jsonVal);
        	syncData(ykyUrl.party + '/v1/enterprises/editEntApply', 'POST', jsonVal, function(data) {
        		$("#save").attr('disabled','disabled');
        		layer.msg('账号升级申请提交成功');
        		setTimeout(function() {
    				location.href = ykyUrl._this + '/enterprise/customers.htm';	
    			}, 1000);
    		});        	
        },
        cancel: function () {
        	location.href = location.pathname;
        }
	}        	
})
//校验
function validator() {
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
	        address:{//注册地址
	        	validators: {
	        		notEmpty: {
	        			message:'请完善注册地址'
	        		},
	        		callback:{
	        			message:' ',
	        			callback:function(value, validator,target){
	        				$(target).removeAttr('style');
	        				$(target).next().removeAttr('style');
	        				$(".address-error small").eq(1).text('');
	        				var flag = true;
	        				var info = app.entInformation,
		        				province = info.province,
		        				city = info.city,
		        				country = info.country;
	        				if(!province || !city || !country){
	        					flag = false;
	        				}
	        				if(!flag && $(".address-error small").eq(0).css('display') == 'none'){
	        					setTimeout(function(){
	        						$(target).css({
		        						'border-color':'#d2d6de',
		        						'background-color':'#fff',
		        						'color':'#4D4D4D'
		        					})
		        					$(target).next().css('display','none');
	        					},300)
	        					$(target).next().css('display','none');
	        					$(".address-error small").eq(1).text('请完善注册地址');
	        				}
	        				return flag;
	        			}
	        		},
	        		
	        		
	        	},
	        	container:'.address-error'
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
	        applyInformation: {//联系人手机号
	        	validators: {
	        		notEmpty: {  
	                    message: '联系人手机号码不能为空'  
	                },
	                regexp: {
	                	regexp:/^((\+?86)|(\(\+86\)))?(13[0-9][0-9]{8}|15[0-9][0-9]{8}|18[0-9][0-9]{8}|147[0-9]{8}|17[0-9]{9}|1349[0-9]{7})$/,
	                	message:'请填写正确的手机号码'
	                }
	        	},
	        	container: '.applyInformation-error'
	        },
	        applyMail:{//联系人邮箱
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
                        callback: function(value, validator) {
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
                        }  
                    } 
	        	},
	        	container: '.applyMail-error'
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
	        personAddress:{//联系地址
	        	validators: {
	        		notEmpty: {
	        			message:'请完善联系地址'
	        		},
	        		callback:{
	        			message:' ',
	        			callback:function(value, validator,target){
	        				$(target).removeAttr('style');
	        				$(target).next().removeAttr('style');
	        				$(".personAddress-error small").eq(1).text('');
	        				var flag = true;
	        				var info = app.entInformation,
		        				province = info.personProvince,
		        				city = info.personCity,
		        				country = info.personCountry;
	        				if(!province || !city || !country){
	        					flag = false;
	        				}
	        				if(!flag && $(".personAddress-error small").eq(0).css('display') == 'none'){
	        					setTimeout(function(){
	        						$(target).css({
		        						'border-color':'#d2d6de',
		        						'background-color':'#fff',
		        						'color':'#4D4D4D'
		        					})
		        					$(target).next().css('display','none');
	        					},300)
	        					$(".personAddress-error small").eq(1).text('请完善联系地址');
	        				}
	        				return flag;
	        			}
	        		},	        			        		
	        	},
	        	container:'.personAddress-error'
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
	    	 debugger;
	    } 
	}).on('success.form.bv', function(e){
		debugger;
		//return;
		e.preventDefault();
		app.saveData();
	});
}