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
	getApplyInfo: function(mail) { return ykyUrl.party + '/v1/customers/getUsersByMail?mail=' + mail; },
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
		applyRecord: []
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
					}
				})
				app.mail = content.applyMail;
				app.reason = content.reason;
				app.accountStatus = data.list[0].status;
				
				//主账号如果冻结了，选中的主账号清空
				for(var i=0; i<app.accountList.length; i++) {
					if(app.accountList[i].mail == app.mail && app.accountList[i].status == 'PARTY_DISABLED') {
						app.mail = '';
					}
				}
				
			}
		});
		
		//申请记录
		record();
		
		//图片初始化
		initAll();
		
	},
	updated: function() {
		this.$nextTick(function() {
			if($('#setMainAccount option').size() == 1) {
				$('#setMainAccount').attr('disabled', true);
				$('#setMainAccount').find('option').text('没有符合条件的账号可供选择');
			}else {
				$('#setMainAccount').removeAttr('disabled');
				$('#setMainAccount').find('option').eq(0).text('请选择');
			}
		})
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
		},
		'hasMainAccount': function() {
			if(this.hasMainAccount) {	//如果账号列表中有主账号，过滤掉平行账号
				for(var i=0; i<this.filterAccountList.length; i++) {
					if(this.filterAccountList[i].personTypeStatus == 'COMMON') {
						this.filterAccountList.splice(i,1);
					}
				}
			}
		}
	},
	filters: {
		toJson: function(jsonStr, key) {
			return JSON.parse(JSON.parse(jsonStr)[0].applyContent)[key] || '-';
		}
	},
	computed: {
		//子账号管理状态
		sonAccountStatus: function() {
			
			if(!this.accountStatus) {
				return '未申请';
			}
			
			switch(this.accountStatus) {
				case 'APPROVED':
					return '通过';
					break;
				case 'REJECT':
					return '不通过';
					break;
				case 'WAIT_APPROVE':
					return '待审核';
					break;
			}
		},
		counter: function() { //字数统计
			return this.reason.length;
		},
		hasMainAccount: function() {
			var accountList = this.accountList;
			for(var i=0; i<accountList.length; i++) {
				if(accountList[i].personTypeStatus == 'MAIN') {
					return true;
				}
			}
			return false;
		},
		filterAccountList: function() {  //如果有主账号，过滤平行账号后的账号列表
			return this.accountList.concat();  
		}
	},
	methods: {
		//基本信息
		clickBasicInfo: function() {
			location.href = ykyUrl._this + '/certificationEntKf.htm?action=edit&id=' + getQueryString("id");
		},
		//账期管理
		clickAccountPeriod: function() {
			location.href = ykyUrl._this + '/certificationEntKf.htm?action=editAccountPeriod&id=' + getQueryString("id");
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
		//点击参考样本
		clickCankao: function() {
			var imgEle = $('#locPicSample');
			showVoucherPic(imgEle);
		},
		//冻结/取消冻结账号
		frozenAccount: function(id, account, status){
			var partyStatus = status == 'PARTY_ENABLED' ? 'PARTY_DISABLED' : 'PARTY_ENABLED';
			layer.confirm(partyStatus =='PARTY_ENABLED'?'确定取消冻结  '+ account +'?' : '确定冻结  '+ account +'? 账号冻结状态无法成为主账号!',{
				offset: "auto",
				closeBtn: false,
				btn: ['确      定','取      消'], //按钮
				title: " ",
				area: 'auto',			
				move: false,
				skin: "up_skin_class",
				yes:function(){
					$.aAjax({
						url:ykyUrl.party + '/v1/customers/frozen?accountId=' + id + '&partyStatus=' + partyStatus,
						type:'POST',
						success:function(data){
							location.reload();
						},
						error:function(res){
							layer.msg("网络异常，请稍后重试！");
						}
					})
				}
			})				
		},
		//校验
		validator: function() {
			if(!this.mail) {
				layer.msg('没有主账号可以选择，不能提交！');
				return false;
			}

			if(!$("#img8").attr("data-src")) {
				layer.msg('请上传企业授权委托书！');
				return false;
			}

			if(!this.reason) {
				layer.msg('申请说明不能为空！');
				return false;
			}
			
			return true;
		},
		//提交
		submit: function() {
			var _this = this;
			var applyInfo = {}; //申请人信息
			var mail = '';
			
			if(!this.validator()) return;
			
			//申请人信息
			$.aAjax({
				url: API.getApplyInfo(_this.mail),
				type: 'GET',
				async: false,
				success: function(data) {
					if(data) {
						applyInfo = data;
					}
				}
			})
			
			//申请人邮箱
		/*	$.aAjax({
				url: ykyUrl.party + '/v1/customers/'+ $('#applyUserId').val() +'/username',
				type: 'GET',
				async: false,
				success: function(data) {
					if(data) {
						mail = data.mail;
					}
				}
			})*/
			
			var subContent = {
					"id" : getQueryString("id"),
					"map": {
						"LOA": $("#img8").attr("data-src") || '',//授权委托书
						"LOA_PDF_NAME": $("#img8").attr("data-pdfname") || '',
					},
					"name": _this.name, //公司名称
					"mail":  _this.mail, //申请人邮箱
					"entUserId": _this.entUserId,
					"applyUser": applyInfo.name,
					"applyMail": _this.mail,	//选中的主账号
					"applyInformation": applyInfo.mobile,
					"contactUserName": $('#contactUserName').val(),
					"reason": _this.reason
			};
			
			var subjsonVal = {
					"applyContent":JSON.stringify(subContent),
					"applyUserId":$('#applyUserId').val(),
					"processId":"ORG_PROXY_REVIEW",//授权委托书流程
					"applyOrgId": getQueryString("id"),
					"applyPageUrl":"",
					"callBackUrl":""
			};
			
			$.aAjax({//保存子账号管理
				url:ykyUrl.party + "/v1/enterprises/entApplyAuthorize",
				type: "POST",
				async: false,
				data:JSON.stringify(subjsonVal),
				success:function(data){
					layer.msg('提交成功');
					setTimeout(function() {
						location.reload();
					}, 500);
				},
				error:function(){
					console.log("error");
				}
			})
		},
		//取消
        cancel: function () {
        	location.href = location.pathname;
        }
	}
})

//读取数据
function renderData(data) {
	app.name = data.name;
	app.partyCode = data.partyCode || '';
}

//查询企业账号及子账号
$.aAjax({
	url: ykyUrl.party + '/v1/enterprises/' + getQueryString("id") + '/accounts',
	async: false,
	type: 'GET',
	//去掉数据模型中的所有函数
	success: function(data) {
		if(data) {
			app.accountList = data;
		}
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

//上传图片初始化
function initAll() {	
	var  uploader = createUploader('uploadBtnLoa', 5, $('#uploadBtnLoa').parent(), function(imageUrlPreview) {
		var uploadFileType = imageUrlPreview.indexOf("pdf")<0 ? "img" : "pdf";
		var imgEle = $('#img8');
		$("#img8").unbind(".plugin");
		$("#img8").unbind(".init");
		if(imageUrlPreview) {
			if(uploadFileType === 'img') {
				$("#img8").on("click.plugin",function(){
					showVoucherPic(imgEle);
				})
			}else if(uploadFileType === 'pdf') {
				$('.uploadImgLoa').find('img').attr('src', ykyUrl._this + '/images/pdf_icon.png');
				$('.uploadImgLoa').find('img').on('click.plugin', function() {
					$("#img8").unbind(".img");
					window.open($(this).data('url'));
				})
			}
		}
		
		
	});
	uploader.init();
}



