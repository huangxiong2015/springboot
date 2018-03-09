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
	apply: ykyUrl.workflow + '/v1/apply',	//申请账期
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
				app.creditAttachmentList = content.creditAttachmentList || [];
			}
		});
		
		//查询账期申请记录
		queryApplyRecord();
		
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
		//子账号管理状态
		accountPeriodStatus: function() {
			switch(this.accountStatus) {
				case 'ACCOUNT_VERIFIED':
					return '通过';
					break;
				case 'ACCOUNT_REJECTED':
					return '不通过';
					break;
				case 'ACCOUNT_WAIT_APPROVE':
					return '待审核';
					break;
				case 'ACCOUNT_NOT_VERIFIED':
					return '未开通';
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
		},
		//默认显示-
		showDefault: function(value) {
			return value || '-';
		}
	},
	methods: {
		//基本信息
		clickBasicInfo: function() {
			if(getQueryString('source') == 'certificationEntRz') {
				location.href = ykyUrl._this + '/certificationEntRz.htm?action=edit&id=' + getQueryString("id");
			}else {
				location.search = 'action=detail&id=' + getQueryString("id");
			}
		},
		//子账号管理
		clickSonAccount: function() {
			if(getQueryString('source') == 'certificationEntRz') {
				location.search = 'action=detailSonAccount&source=certificationEntRz&id=' + getQueryString("id");
			}else {
				location.search = 'action=detailSonAccount&id=' + getQueryString("id");
			}
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
			app.creditAttachmentList.splice(index, 1);
		},
		//字数统计
		chCounter: function() {
			this.common = this.common.substr(0, 100);
		},
		//取消
        cancel: function () {
			location.href = location.pathname;
        }
	}
})

//文件上传初始化	
function fileUpInit() {
	var uploader = createUploader({
		buttonId: "addFile", 
		uploadType: "bill.apply",
		url:  ykyUrl.webres,
		types: "pdf,xls,xlsx,rar",
		fileSize: "10mb",
		isImage: false, 
		init:{
			FileUploaded : function(up, file, info) {
	            layer.close(index);
	            
				if(app.creditAttachmentList.length >= 5) {
	            	layer.msg('附件不能超过5个');
	            	return;
	            }
				
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
