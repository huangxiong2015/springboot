var	 vm ;
$(function(){
	
vm = new Vue({
        el: '#examineQualification',
        data: {
        	id:getQueryString("id"),//企业id
        	entId:getQueryString("entId"),//父企业id
        	applyId:getQueryString("applyId"),
        	type:getQueryString("type"),
        	form:getQueryString("form"),
        	industry:{},//所属行业
        	companytype:{},//公司类型
        	audit:{},
        	auditList:[],
        	companyInfo : {
        		id : '',
        		name : '',
        		corCategory : '',
        		industryCategory : '',
        		industryOther : '',
        		provinceName : '',
        		cityName : '',
        		countryName : '',
        		address : '',
        		dCode : '',
        		webSite : '',
        		partyCode : '',
        		accountStatus : '',
        		activeStatus : '',
        		isVipCenter : ''
        	},
        	zzInfo : {
        		regAddr : '',//注册地
        		busiLisType : '', //三证类型
        		busiLicPic : '', //营业执照
        		oocPic : '', //组织机构代码
        		taxPic : '', //税务登记
        		busiPdfName : '', //营业执照pdf
        		oocPdfName : '', //组织机构代码pdf
        		taxPdfName : '', //税务登记pdf
        		loaPic : '', //授权书
        		loaPdfName : '' //授权书pdf
        	},
        	info : {
        		socialCode : '',//统一社会信用代码
        		entName : '',//企业名称（全称）
        		location : '',//住所
        		busiRange : '',//经营范围
        		faxCode : '',//纳税人识别号
        		faxName : '',//纳税人名称
        		orgCode : '',//机构代码
        		orgName : '',//机构名称
        		orgLocation : '',//机构地址
        		orgCdate : '2000-01-01',//成立日期
        		orgLimit : '',//营业期限
        		hkEntName : '', //香港-公司名称
        		hkSignCdate : '', //香港-签发日期
        		hkBusiName : '', //香港-业务所用名称
        		hkAddr : '', //香港-地址
        		hkEffectiveDate : '', //生效日期
        		twEntName : '', //台湾-公司名称
        		twSignCdate : '', //台湾-签发日期
        		twFaxEntName : '', //台湾-税籍登记证-公司名称
        		twFaxSignCdate : '', //台湾-税籍登记证-签发日期
        		twFaxCode : '', //台湾-税务编码
        		abroadEntName : '', //ABROAD_ENT_NAME
        		abroadEntNum : '', //ABROAD_ENT_NUM
        		applyUser : '',
        		applyMail : '',
        		applyInformation : '',
        	},
        },
        created:function(){
        	var that = this;
        	//获取所属行业列表
        	syncData(ykyUrl.database + "/v1/category/industry", 'GET', null, function(res,err) {
    			if (!err && res !== '') {
    				$.each(res,function(idx,val){
    					that.industry[val.categoryId] = val.categoryName;
    				})
    			}
    		},false);
        	//获取公司类型
        	syncData(ykyUrl.database + "/v1/category/companytype", 'GET', null, function(res,err) {
    			if (!err && res !== '') {
    				$.each(res,function(idx,val){
    					that.companytype[val.categoryId] = val.categoryName;
    				})
    			}
    		},false);
        	//获取查询操作日志
        	syncData(ykyUrl.workflow + "/v1/task/"+ that.applyId, 'GET', null, function(res,err) {
    			if (!err && res !== '') {
    				that.auditList	= res.list ;
    			}
    		},false);
        	//获取企业详细信息
        	syncData(ykyUrl.party + "/v1/enterprises/entDetail/" + that.id + '/' + that.entId +'/CORPORATION', 'GET', null, function(res,err) {
    			if (!err && res !== '') {
    				var data = res;
    				if(typeof res === 'undefined') {
    	     			data = {};
    	     		}
    				that.companyInfo = {
    						id: data.id || '',
    		     			name: data.name || '-',
    		        		corCategory: data.map && data.map.CORPORATION_CATEGORY_ID || '',
    		        		industryCategory: data.map && data.map.INDUSTRY_CATEGORY_ID || '',
    		        		industryOther: data.map && data.map.INDUSTRY_CATEGORY_ID_OTHER || '',
    		        		CORPORATION_CATEGORY_ID_OTHER: data.map && data.map.CORPORATION_CATEGORY_ID_OTHER || '',
    		        		provinceName: data.provinceName || '-',
    		        		cityName: data.cityName || '',
    		        		countryName: data.countryName || '',
    		        		address: data.address || '',
    		        		dCode: data.map && data.map.D_CODE || '-',
    		        		webSite: data.map && data.map.WEBSITE_URL || '-',
    		        		partyCode: data.partyCode || '-',
    		        		accountStatus: data.accountStatus || '',
    		        		activeStatus: data.activeStatus || '',
    		        		mail:data.mail || '',
    		        		isVipCenter:data.isVipCenter || ''
    				}
    			}
    		},false);
        	//获取三证信息
        	syncData(ykyUrl.workflow + "/v2/apply/myApply", 'GET', {applyId: that.applyId}, function(res,err) {
    			if (!err && res !== '') {
    				var data = res;
    				var bigData = data.list[0] || {};
    				data = JSON.parse(data.list[0].applyContent) || {};
    				that.zzInfo = {
    						regAddr: data.map && data.map.REG_ADDR || '0',
    			 			busiLisType: data.map && data.map.BUSI_LIS_TYPE || '',
    			 			initialBusiLicPic: data.map && data.map.BUSI_LIC_PIC || '',
    			 			initialOocPic: data.map && data.map.OCC_PIC || '',
    			 			initialTaxPic: data.map && data.map.TAX_REG_PIC || '',
    			 			initialLoaPic: data.map && data.map.LOA || '',
    			 			busiLicPic: data.map && getImage(data.map.BUSI_LIC_PIC) || ykyUrl._this + '/images/defaultImg01.jpg',
    			 			oocPic: data.map && getImage(data.map.OCC_PIC) || ykyUrl._this + '/images/defaultImg01.jpg',
    			 			taxPic: data.map && getImage(data.map.TAX_REG_PIC) || ykyUrl._this + '/images/defaultImg01.jpg',
    			 			busiPdfName: data.map && data.map.BUSI_PDF_NAME || '',
    			 			oocPdfName: data.map && data.map.OCC_PDF_NAME || '',
    			 			taxPdfName: data.map && data.map.TAX_PDF_NAME || '',
    			 			loaPic: data.map && getImage(data.map.LOA) || '',
    			 			loaPdfName: data.map && data.map.LOA_PDF_NAME || ''
    				}
    				that.info = {
    						socialCode: data.map && data.map.SOCIAL_CODE || '',
    			 			entName: data.map && data.map.ENT_NAME || '',
    						location: data.map && data.map.LOCATION || '',
    						busiRange: data.map && data.map.BUSI_RANGE || '',
    						faxCode: data.map && data.map.FAX_CODE || '',
    						faxName: data.map && data.map.FAX_NAME || '',
    						orgCode: data.map && data.map.ORG_CODE || '',
    						orgName: data.map && data.map.ORG_NAME || '',
    						orgLocation: data.map && data.map.ORG_LOCATION || '',
    						orgCdate: data.map && data.map.ORG_CDATE || '',
    						orgLimit: data.map && data.map.ORG_LIMIT || '',
    						hkEntName: data.map && data.map.HK_ENT_NAME || '',
    						hkSignCdate: data.map && data.map.HK_SIGN_CDATE || '',
    						hkBusiName: data.map && data.map.HK_BUSI_NAME || '',
    						hkAddr: data.map && data.map.HK_ADDR || '',
    						hkEffectiveDate: data.map && data.map.HK_EFFECTIVE_DATE || '',
    						twEntName: data.map && data.map.TW_ENT_NAME || '',
    						twSignCdate: data.map && data.map.TW_SIGN_CDATE || '',
    						twFaxEntName: data.map && data.map.TW_FAX_ENT_NAME || '',
    						twFaxSignCdate: data.map && data.map.TW_FAX_SIGN_CDATE || '',
    						twFaxCode: data.map && data.map.TW_FAX_CODE || '',
    						abroadEntName: data.map && data.map.ABROAD_ENT_NAME || '',
    						abroadEntNum: data.map && data.map.ABROAD_ENT_NUM || '',
    						contactUserName: data.contactUserName,
    						mail: data.mail || '',
    						contactUserTel: data.contactUserTel || '',
    						createdDate: bigData.createdDate || '',
    						applyUser: data.applyUser,
    						applyMail: data.applyMail,
    						applyInformation: data.applyInformation,
    						processName:bigData.processName,
    						cNodeName:bigData.cNodeName  //新增判断初审，查看审核日志  
    				}
    				that.audit = {
    						approvePartyName: bigData.reviewUserName,	//审核人
    						lastUpdateDate: bigData.lastUpdateDate, //审核时间
    						reason: bigData.reason,	//审核原因
    						status:bigData.status //审核状态
    					};
    				that.$nextTick(validator);
    			}
    		},false);
        	
        }, 
        updated:function(){
        	var that = this;
        	$('.input-datetext').datepicker({
    			language: config.language,
    			format:  config.dateFormat,
                autoclose: true
    		}).on('change',function(ev){
    			var target = ev.currentTarget;
    			var key = $(target).attr('name');
    			that.info[key] = $(target).val();
    		});
        },
        methods:{
        	showVoucherPic:function(target){
        		layer.open({
        	  		type : 1,
        	  		title : false,
        	  		closeBtn : 0,
        	  		area : [ 'auto', 'auto' ], // 自定义宽度
        	  		skin : 'layer_cla', // 没有背景色
        	  		shadeClose : true,
        	  		maxWidth: $(window).width() * 0.9,
        	  		content : $(target)[0].outerHTML.replace(/class="img-thumbnail"/, 'style="max-width:1000px;width:auto;height:auto;"'),
        	  		success: function(layero) {
        	  			var maxWidth = $(window).width() * 0.9;
        	  			var maxHeight = $(window).height() * 0.9;
        	  			var src = layero.find('img').attr('src');        	  			
        	  			var imgEle = new Image();
        	  			imgEle.src = src;        	  			
        	  			if(imgEle.width > imgEle.height) {
        	  				layero.find('img').css('maxWidth', maxWidth);
        	  			}else {
        	  				layero.find('img').css('maxHeight', maxHeight);
        	  			}        	  			
        	  			var topPx = ($(window).height() - layero.find('img').height()) / 2;
        	  		  	var leftPx = ($(window).width() - layero.find('img').width()) / 2;
        	  	  		$('.layui-layer').css('top', topPx);
        	  	  		$('.layui-layer').css('left', leftPx);
        	  	  		$('.layui-layer-content').height('auto');
        	  		}
        	  	});
        	},
        	getIndustry:function(){//所属行业
        		var that = this;
        		var other,str = [];
        		var arr = that.companyInfo.industryCategory.split(",");
        		$.each(arr,function(i,e){
        			if(e == 1008){
        				other = that.companyInfo.industryOther;
        			}
        			str.push(that.industry[e]);
        		})
        		str = other ? str.join('+') + '(' + other + ')' : str.join('+');
        		return str;
        	},
        	regAddr:function(){//注册地
        		var that = this;
        		var obj = {
        			0:'大陆',
        			1:'香港',
        			2:'台湾',
        			3:'境外'
        		}
        		var str = obj[that.zzInfo.regAddr];
        		return str;
        	},
        	showInfo:function(target,event){//公司信息显示隐藏
        		var btn = event.currentTarget
        		$(target).slideToggle();
        	},
        	examineEvent:function(type){
        		var that = this;
        		var applyType, backUrl, map;
        		var operPrefix = 'http:' + operationWebUrl.replace("/main","") + ykyUrl.party;
        		if(that.type === 'ORG_DATA_REVIEW') {  //企业资质审核
        			var isVipCenter = that.companyInfo.isVipCenter;
        			if(isVipCenter == '0') { // 此页面不是从会员中心跳转过来的
        				backUrl =operPrefix + "/v1/enterprises/editEntApplySave";
        			}else{	//此页面是从会员中心跳转过来的
        				backUrl = operPrefix +"/v1/enterprises";
        			}
        			map = {  //三证信息
        					SOCIAL_CODE: that.info.socialCode,
        					ENT_NAME: that.info.entName,
        					LOCATION: that.info.location,
        					BUSI_RANGE: that.info.busiRange,
        					FAX_CODE: that.info.faxCode,
        					FAX_NAME: that.info.faxName,
        					ORG_CODE: that.info.orgCode,
        					ORG_LOCATION: that.info.orgLocation,
        	    			ORG_NAME: that.info.orgName,
        	    			ORG_CDATE: that.info.orgCdate,
        	    			ORG_LIMIT: that.info.orgLimit,
        	    			HK_ENT_NAME: that.info.hkEntName,  //香港-公司名称
        	    			HK_SIGN_CDATE: that.info.hkSignCdate, //香港-签发日期
        	    			HK_BUSI_NAME: that.info.hkBusiName, //香港-业务所用名称
        	    			HK_ADDR: that.info.hkAddr, //香港-地址
        	    			HK_EFFECTIVE_DATE: that.info.hkEffectiveDate, //生效日期
        	    			TW_ENT_NAME: that.info.twEntName, //台湾-公司名称
        	    			TW_SIGN_CDATE: that.info.twSignCdate, //台湾-签发日期
        	    			TW_FAX_ENT_NAME: that.info.twFaxEntName, //台湾-税籍登记证-公司名称
        	    			TW_FAX_SIGN_CDATE: that.info.twFaxSignCdate, //台湾-税籍登记证-签发日期
        	    			TW_FAX_CODE: that.info.twFaxCode, //台湾-税务编码
        	    			ABROAD_ENT_NAME: that.info.abroadEntName, //ABROAD_ENT_NAME
        	    			ABROAD_ENT_NUM: that.info.abroadEntNum, //ABROAD_ENT_NUM
        	    			
        	    			REG_ADDR:that.zzInfo.regAddr,//公司注册地
        	    			BUSI_LIS_TYPE:that.zzInfo.busiLisType,//注册类型
        	    			BUSI_LIC_PIC:that.zzInfo.initialBusiLicPic || '',
        	    			OCC_PIC:that.zzInfo.initialOocPic || '',
        	    			TAX_REG_PIC:that.zzInfo.initialTaxPic || '',
        	    			BUSI_PDF_NAME:that.zzInfo.busiPdfName || '',
        	    			OCC_PDF_NAME:that.zzInfo.oocPdfName || '',
        	    			TAX_PDF_NAME:that.zzInfo.taxPdfName || '',
        			}
        			
        		}else { //子账号审核
        			backUrl = operPrefix+"/v1/enterprises/entAuthorize";
        			
        			map = { //授权委托书
            			LOA:that.zzInfo.initialLoaPic || '',
            			LOA_PDF_NAME:that.zzInfo.loaPdfName || ''
        			}
        		}
        		var listData = {
        				applyContent: JSON.stringify({
        					id: that.companyInfo.id ,  //企业ID
        					occCode: that.info.orgCode,
        					operationType: that.type === 'ORG_DATA_REVIEW' ? '企业资质' : '子账号管理',
        					mail:that.info.mail,
        					contactUserName:that.info.contactUserName,
        					entUserId:that.id,
        					approvePartyName: $("#userName").val(),//审核人
        					name: that.companyInfo.name,
        					map: map, //三证信息
        					applyUser : that.info.applyUser,
        	        		applyMail : that.info.applyMail,
        	        		applyInformation : that.info.applyInformation,
        					
        				}),
        				approvePartyName: $("#userName").val(),//审核人
        				approvePartyId: $("#userId").val(),
        				id: that.companyInfo.id ,  //企业ID
        				callBackUrl: backUrl,  //回调url
        				remark:  ''   //审核意见
        			};
        		if(type == 'yes'){
        			applyType = 'APPROVED'; //通过
        		
        			$('.examine-type').text('通过');
        			layer.confirm($('.reason-wrap').html(),{
        				offset: "auto",
        				btn: ['确      认','取      消'], //按钮
        				title: " ",
        				area: 'auto',
        				maxWidth:'500px',
        				move: false,
        				skin: "up_skin_class",
        				yes:function(){
        					var reason = $.trim($('.up_skin_class #remark').val());
        					if(reason == ''){
        						$('.up_skin_class #remark').attr('placeholder','审核通过的原因不能为空');
        						return;
        					}
        					listData.remark = reason;
        					that.dyAjax(applyType,listData);
        				}
        			})
        		}else{
        			applyType = 'REJECT'; //驳回
        			$('.examine-type').text('不通过');
        			layer.confirm($('.reason-wrap').html(),{
        				offset: "auto",
        				btn: ['确      认','取      消'], //按钮
        				title: " ",
        				area: 'auto',
        				maxWidth:'500px',
        				move: false,
        				skin: "up_skin_class",
        				yes:function(){
        					var reason = $.trim($('.up_skin_class #remark').val());
        					if(reason == ''){
        						$('.up_skin_class #remark').attr('placeholder','审核不通过的原因不能为空');
        						return;
        					}
        					listData.remark = reason;
        					that.dyAjax(applyType,listData);
        				}
        			})
        			//setLetterNum();
        		}        		
        	},
        	dyAjax:function(type,postObj){//审核接口调用
        		var that = this;
        		var alertTxt = '';
        		if(type === 'APPROVED') {
    				alertTxt = '审核通过';
    			}else {
    				alertTxt = '驳回成功';
    			}
        		syncData(ykyUrl.workflow +"/v1/task/" + type + "/" + that.applyId,'PUT',postObj,function(res,err){
        			if(err == null){
        				layer.msg(alertTxt);
            			setTimeout(function(){
            				layer.closeAll()
            				if(that.form && that.form == 'period'){ //账期页面跳转过来的 
            					var str = location.pathname;
            					var Str = str.replace(/verify/, "credit");
            					location.href = location.origin + Str;
            				}else{
            					location.href = location.origin + location.pathname;
            				}
            			}, 2000)
        			}
        		})        		
        	}
        }
    }); 

})
//获取图片公共方法
function getImage(imageUrl) {
	var url = '';
	$.aAjax({
		url: ykyUrl.party + "/v1/enterprises/getImgUrl",
     	type:"POST",
     	async:false,
     	data:JSON.stringify({"id":imageUrl}),
     	success: function(data) {
     		if(data != '') {
	     		url = data;
     		}
     	}
 	});	
	return url;
}
function setLetterNum(e){
	var num = $(e).val().length;
	$(e).parent().find('.letter-num').text(num);
}

function validator() {
	$('#infoForm').bootstrapValidator({   
	    feedbackIcons: {  
	        valid: 'glyphicon glyphicon-ok',  
	        invalid: 'glyphicon glyphicon-remove',  
	        validating: 'glyphicon glyphicon-refresh'  
	    },
	    fields: {  
	    	socialCode: {//统一社会信用代码
	            validators: {
	            	notEmpty: {  
	                    message: '统一社会信用代码不能为空'  
	                },
	                /*regexp: {
                        regexp: /^[A-Za-z0-9]+$/,
                        message: '50位,数字、字母及其组合'
                    }*/
	            },
	            container: '.socialCode-err'
	        },
	        entName: {//企业名称
	            validators: {
	            	notEmpty: {  
	                    message: '企业名称不能为空'  
	                },
	            	stringLength: {
	                	min: 4,
	                    max: 50,
                        message: '4-50位，字符类型不限'
                    }
	            },
	            container: '.entName-err'
	        },
	        hkEntName: {
	        	validators: {
	            	notEmpty: {  
	                    message: '公司名称不能为空'  
	                }
	            },
	            container: '.hkEntName-err'
	        },
	        twEntName: {
	        	validators: {
	            	notEmpty: {  
	                    message: '公司名稱不能为空'  
	                }
	            },
	            container: '.twEntName-err'
	        },
	        abroadEntName: {
	        	validators: {
	            	notEmpty: {  
	                    message: 'Company Name不能为空'  
	                }
	            },
	            container: '.abroadEntName-err'
	        },
	        /*faxCode: {//纳税人识别号
	            validators: {
	                regexp: {
                        regexp: /^[A-Za-z0-9]+$/,
                        message: '50位,数字、字母及其组合'
                    }
	            },
	            container: '.faxCode-err'
	        },
	        orgCode: {//组织机构代码
	            validators: {
	                regexp: {
                        regexp: /^[A-Za-z0-9]+$/,
                        message: '50位,数字、字母及其组合'
                    }
	            },
	            container: '.faxName-err'
	        },*/
	    },
	    live: 'enabled,submitted',
	    submitButtons: '#save',
	    submitHandler: function (validator, form, submitButton) {
	    	
	    } 
	}).on('success.form.bv', function(e){
		e.preventDefault();
		vm.examineEvent('yes');
		$("#save").removeAttr('disabled');
	}); 
}