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

var API = {
	getEntInfo: ykyUrl.party + '/v1/enterprises/entDetail/1/' +  getQueryString("id") + '/VIP_CORPORATION', //查询企业信息
	getAccountPeriodInfo: ykyUrl.workflow + '/v1/apply/PROCESS/ORG_ACCOUNT_PERIOD_REVIEW',	//查询账期信息
	apply: ykyUrl.party +'/v1/enterprises/accountPeriodApply',	//申请账期
	applyRecord: function(listAction) { return ykyUrl.party + "/v1/audit?actions=" + listAction + "&actionId=" + getQueryString("id");} //申请记录
}

var app = new Vue({
	el: '#son',
	data: {
		name: '',	//公司名称
		partyCode: '',	//YKY编码
		status: '', //账期状态
		mail: '', //申请人邮箱
		contactUserName: '', //申请人
		currency: 'USD',	//结算币种
		creditQuota: '', //授信额度
		creditDeadline: '',	//授信期限
		checkDate: '',	//对账日期
		checkCycle: '',	//对账周期
		payDate: '',	//付款日期
		applyUser: '',	//联系人
		applyMail: '',	//联系人邮箱
		applyInformation: '',	//联系方式
		common: '',	//申请说明
		creditAttachmentList: [],	//附件列表
		applyRecord: []	//账期申请记录
	},
	mounted: function() {
		fileUpInit();
		
		//查询企业名称和YKY编码
		syncData(API.getEntInfo, 'GET', null, function(data) {
			if(data) {
				app.name = data.name;
				app.partyCode = data.partyCode || '';
			}
		});
		
		//查询账期
		syncData(API.getAccountPeriodInfo, 'POST', {applyOrgId: getQueryString("id")}, function(data) {
			if(data && data.list && data.list[0]) {
				var content = JSON.parse(data.list[0].applyContent);
				
				app.status = data.list[0].status;
				app.currency = content.currency || 'USD';
				app.creditQuota = content.creditQuota || '';
				app.creditDeadline = content.creditDeadline || '';
				app.checkDate = content.checkDate || '';
				app.checkCycle = content.checkCycle || '';
				app.payDate = content.payDate || '';
				app.common = content.common || '';
				app.applyUser = content.applyUser || '';
				app.applyInformation = content.applyInformation || '';
				app.applyMail = content.applyMail || '';
				app.creditAttachmentList = content.creditAttachmentList || [];
			}
		});
		
		//查询账期申请记录
		queryApplyRecord();
		
		//按钮移除禁用
		$('.btns').on('mouseover', function() {
			$('#save').removeAttr('disabled');
		});
		
		//校验
		validator();
		
	},
	watch: {
		'mail': function() {
			var mail = this.mail;
			var _this = this;
			
			$.each(this.accountList, function(i, item) {
				if(item.mail == mail) {
					_this.entUserId = item.id;
					return true;
				}
			})
		}
	},
	computed: {
		//账期状态
		accountPeriodStatus: function() {
			switch(this.accountStatus) {
				case 'ACCOUNT_VERIFIED':
					return '通过';
					break;
				case 'ACCOUNT_REJECTED':
					return '驳回';
					break;
				case 'ACCOUNT_WAIT_APPROVE':
					return '待审核';
					break;
				case 'ACCOUNT_NOT_VERIFIED':
					return '未开通';
					break;
				case 'PERIOD_DISABLED':
					return '冻结';
					break;
			}
		},
		counter: function() { //字数统计
			return this.common.length;
		}
	},
	filters: {
		toJson: function(jsonStr, key) {
			return JSON.parse(JSON.parse(jsonStr)[0].applyContent)[key] || '-';
		}
	},
	methods: {
		//基本信息
		clickBasicInfo: function() {
			location.href = ykyUrl._this + '/certificationEntKf.htm?action=edit&id=' + getQueryString("id");
		},
		//子账号管理
		clickSonAccount: function() {
			location.href = ykyUrl._this + '/certificationEntKf.htm?action=editSonAccount&id=' + getQueryString("id");
		},
		//阿里云转码
		getImage: function(imageUrl) {
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
		//打开附件
		openLink: function(link) {
			window.open(link);
		},
		//删除附件
		removeItem: function(index) {
			var l = layer.confirm('删除文件后不可恢复哦，您确认要删除吗？', {
				  btn: ['确认','取消'] //按钮
				}, function(){
				  layer.close(l);
				  app.creditAttachmentList.splice(index, 1);
				}, function(){
				  
				});
		},
		//子账号管理
		clickSonAccount: function() {
			location.href = ykyUrl._this + '/certificationEntKf.htm?action=editSonAccount&id=' + getQueryString("id");
		},
		//字数统计
		chCounter: function() {
			this.common = this.common.substr(0, 100);
		},
		//取消
        cancel: function () {
			location.href = ykyUrl._this + '/certificationEntKf.htm';
        }
	}
})

//文件上传初始化	
function fileUpInit() {
	var uploader = createUploader({
		buttonId: "addFile", 
		uploadType: "bill.apply",
		url:  ykyUrl.webres,
		types: "pdf,xls,xlsx,rar,zip",
		fileSize: "10mb",
		isImage: false, 
		init:{
			FileUploaded : function(up, file, info) {
	            layer.close(index);
				
				if (info.status == 200 || info.status == 203) {
					console.log(file.name);
					app.creditAttachmentList.push({
						"attachmentName": file.name, 
						"attachmentUrl": _fileUrl
					});
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	}); 

	uploader.init();
}

//查询申请记录
function queryApplyRecord() {
	syncData(API.applyRecord('Account Period'), 'GET', null, function(data) {
		
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
	    	creditQuota: {   
	            validators: {  
	                notEmpty: {  
	                    message: '授权额度不能为空'  
	                },
	                regexp: {
                        regexp: /^[0-9]{1,7}$/,
                        message: '7位数字'
                    }
	            }
	        },
	        creditDeadline: {
	        	 validators: {  
	                notEmpty: {  
	                    message: '授信期限不能为空'  
	                },
	                regexp: {
                        regexp: /^[0-9]{1,4}$/,
                        message: '1-4位数字'
                    }
	            }
	        },
	        checkDate: {
	        	 validators: {  
	                notEmpty: {  
	                    message: '对账日期不能为空'  
	                },
	                regexp: {
	                	regexp: /^[0-9]{1,2}$/,
                        message: '1-2位数字'
                    }
	            }
	        },
	        checkCycle: {
	        	validators: {  
	                notEmpty: {  
	                    message: '对账周期不能为空'  
	                },
	                regexp: {
	                	regexp: /^[0-9]{1,4}$/,
                        message: '1-4位数字'
                    }
	            }
	        },
	        payDate: {
	        	validators: {  
	                notEmpty: {  
	                    message: '付款日期不能为空'  
	                },
	                regexp: {
	                	regexp: /^[0-9]{1,2}$/,
                        message: '1-2位数字'
                    }
	            }
	        },
	        common: {
	        	validators: {  
	                notEmpty: {  
	                    message: '申请说明不能为空'  
	                }
	            }
	        },
	        applyUser: {
	        	validators: {
	        		notEmpty: {
	        			message: '姓名不能为空'
	        		},
	        		regexp: {
	        			regexp: /^[\u4E00-\u9FA5a-zA-Z_\s]+$/,
	        			message: '联系人不可包含除汉字、英文和_外的字符'
	        		},
	        		stringLength: {
                        max: 20,
                        message: '姓名长度不超过20位'
	        		}    	
	        	}
	        },
	        applyInformation: {
	        	validators: {
	        		notEmpty: {
	        			message: '联系方式不能为空'
	        		},
	        		regexp: {
	        			regexp: /^[0-9\-\s]{1,50}$/,
	        			message: '不符合联系方式规则'
	        		}   	
	        	}
	        },
	        applyMail: {
	        	validators: {
	        		notEmpty: {
	        			message: '邮箱不能为空'
	        		},
	        		regexp: {
	        			regexp: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,
	        			message: '不符合邮箱规则'
	        		}   	
	        	}
	        }
	    },
	    disable: true,
	    live: 'enabled,submitted ',
	    submitButtons: '#save',
        container: '.tips',
	    submitHandler: function (validator, form, submitButton) { 
	    } 
	}).on('success.form.bv', function(e){
		e.preventDefault();
		save();
	}); 
}

//提交
function save() {
	
	if(app.creditAttachmentList.length == 0) {
		layer.msg('请上传附件!');
		return;
	}
	
	var mail = '';
	
	var content = {
			"partyId": getQueryString("id"),
			"name": app.name,
			"contactUserName": $('#applyUserName').val(),
			"checkCycle": app.checkCycle,
			"checkDate": app.checkDate,
			"common": app.common,
			"creditDeadline": app.creditDeadline,
			"creditQuota": app.creditQuota,
			"currency": app.currency,
			"payDate": app.payDate,
			"realtimeCreditQuota": app.realtimeCreditQuota,
			"creditAttachmentList": app.creditAttachmentList,
			"applyUser": app.applyUser,
			"applyMail": app.applyMail,
			"mail": app.applyMail,
			"applyInformation": app.applyInformation
	}
	
	var dataList = {
		    "applyContent": JSON.stringify(content),
		    "applyUserId": $('#applyUserId').val(),
		    "processId": "ORG_ACCOUNT_PERIOD_REVIEW",
		    "applyOrgId": getQueryString("id"),
		    "applyPageUrl": "",
		    "callBackUrl": "",
		    "partyCode":app.partyCode
		}

	
	syncData(API.apply, 'POST', dataList, function(res, err) {
		layer.msg('提交成功');
		setTimeout(function() {
			location.reload();
		}, 500)
		
		$('#save').removeAttr('disabled');
	})
	
}
