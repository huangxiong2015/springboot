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
        content : showOriIdStr.get(0).outerHTML.replace(/id="\S+"/, 'class="layer-img" style="max-width:1000px;max-height:800px;width:auto;height:auto;"')
    });
    
    $('.layui-layer').css('width', $('.layer-img').width());
    $('.layui-layer').css('left', ($(window).width() - $('.layer-img').width()) / 2);
}

var API = {
	getEntInfo: ykyUrl.party + '/v1/enterprises/entDetail/1/' +  getQueryString("id") + '/VIP_CORPORATION',
	getZzInfo: ykyUrl.workflow + '/v1/apply/PROCESS/ORG_PROXY_REVIEW',
	applyRecord: function(listAction) { return ykyUrl.party + "/v1/audit?actions=" + listAction + "&actionId=" + getQueryString("id");} //申请记录记录
}



var app = new Vue({
	el: '#son',
	data: {
		name: '',	//公司名称
		partyCode: '',	//YKY编码
		accountList: [], //账户列表
		accountStatus: '', //子账号状态
		reason: '',	//申请说明
		mail: '', //当前选中的主账号邮箱
		entUserId: '', //选中主账号的ID
		applyRecord: []	//申请记录
	},
	mounted: function() {
		
		//查询企业名称和YKY编码
		syncData(API.getEntInfo, 'GET', null, function(data) {
			if(data) {
				renderData(data);
			}
		});
		
		//查询资质
		syncData(API.getZzInfo, 'POST', {applyOrgId: getQueryString("id")}, function(data) {
			if(data && data.list && data.list[0]) {
				var content = JSON.parse(data.list[0].applyContent);
				var imgSrc = '';
				var fileType = content.map.LOA.indexOf('.pdf') != -1 ? 'pdf' : 'img';
				
				if(fileType == 'pdf') { //如果是pdf
					imgSrc = ykyUrl._this + '/images/pdf_icon.png';
					$('#img8').attr('title', content.map.LOA_PDF_NAME);
				}else {
					imgSrc = app.getImage(content.map.LOA);
				}
				
				$('#img8').attr({
					'src': imgSrc,
					'data-src': content.map.LOA
				});
				
				$("#img8").on("click.init",function(){
					var imgEle = $('#img8');
					if(fileType == 'pdf') {
						window.open(app.getImage(content.map.LOA));
					}else {
						showVoucherPic(imgEle);
				        reSizeImg(imgEle);
					}
				})
				app.mail = content.mail;
				app.reason = data.list[0].reason;
			}
		});
		
		//申请记录
		record();
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
		sonAccountStatus: function() {
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
			return this.reason.length;
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
			if(getQueryString('source') == 'certificationEntRz') {
				location.href = ykyUrl._this + '/certificationEntRz.htm?action=edit&id=' + getQueryString("id");
			}else {
				location.search = 'action=detail&id=' + getQueryString("id");
			}
		},
		//账期管理详情
		clickAccountPeriod: function() {
			if(getQueryString('source') == 'certificationEntRz') {
				location.search = 'action=detailAccountPeriod&source=certificationEntRz&id=' + getQueryString("id");
			}else {
				location.search = 'action=detailAccountPeriod&id=' + getQueryString("id");
			}
		},
		//字数统计
		chCounter: function() {
			this.reason = this.reason.substr(0, 100);
		},
		//账号类型
		accountType: function(status) {
			switch(status) {
				case 'COMMON':
					return '平行账号';
					break;
				case 'SON':
					return '子账号';
					break;
				case 'MAIN':
					return '主账号';
					break;
			}
		},
		//阿里云图片转码
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
		//取消
        cancel: function () {
			location.href = ykyUrl._this + '/certificationEntKf.htm';
        }
	}
})

//读取数据
function renderData(data) {
	app.name = data.name;
	app.partyCode = data.partyCode || '';
	app.accountStatus = data.accountStatus;
}

//查询企业账号及子账号
$.aAjax({
	url: ykyUrl.party + '/v1/enterprises/' + getQueryString("id") + '/accounts',
	async: false,
	type: 'GET',
	//去掉数据模型中的所有函数
	success: function(data) {
		app.accountList = data;
	}
});

//申请记录
function record() {
	syncData(API.applyRecord('Sun Account Apply'), 'GET', null, function(data) {
		if(data && data.list) {
			app.applyRecord = data.list;
		}
	})
}


