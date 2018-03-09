var	 vm ;
$(function(){
	
vm = new Vue({
        el: '#examineAccountPeriod',
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
        	qzInfo : {
        		applyName:'',//申请人
				checkCycle:'',//对账期限
				checkDate:'',//对账日期（纯数字，代表每月几号）
				common:'',//描述说明
				companyName:'',//公司名称
				creditAttachmentList:'',//附件类
				creditDeadline:'',//授信期限（月结xxx天）
				creditQuota:'',//授信额度
				currency:'',//币种
				mail:'',//邮箱
				partyCreditId:'',
				partyId:'',//认证企业PARTY_ID
				payDate:'',//付款日期（纯数字，代表每月几号）
				realtimeCreditQuota:''//授信余额
        	},
        	info : {
        		contactUserName: '',
				mail: '',
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
        	syncData(ykyUrl.party + "/v1/enterprises/entDetail/" + that.id + '/' + that.entId +'/VIP_CORPORATION', 'GET', null, function(res,err) {
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
        	syncData(ykyUrl.workflow + "/v2/apply/myApply", 'GET', {applyId : that.applyId}, function(res,err) {
    			if (!err && res !== '') {
    				var data = res;
    				var bigData = data.list[0] || {};
    				data = JSON.parse(data.list[0].applyContent) || {};
    				that.qzInfo = {
    						approvePartyName: data.approvePartyName || '',//申请人
    						checkCycle: data.checkCycle || '',//对账期限
    						checkDate: data.checkDate || '',//对账日期（纯数字，代表每月几号）
    						common: data.common || '',//描述说明
    						contactUserName: data.contactUserName || '',
    						creditAttachmentList: data.creditAttachmentList || '',//附件类
    						creditDeadline: data.creditDeadline || '',//授信期限（月结xxx天）
    						creditQuota: data.creditQuota || '',//授信额度
    						currency: data.currency || '',//币种
    						name: data.name || '',//公司名称
    						mail: data.mail || '',//邮箱
    						partyId: data.partyId || '',//认证企业PARTY_ID
    						payDate: data.payDate || '',//付款日期（纯数字，代表每月几号）
    						realtimeCreditQuota: data.realtimeCreditQuota || '',//授信余额
    				}
    				that.info = {
    						contactUserName: data.contactUserName,
    						mail: data.mail || '',
    						contactUserTel: data.contactUserTel || '',
    						createdDate: bigData.createdDate || '',
    						applyUser: data.applyUser,
    						applyMail: data.applyMail,
    						applyInformation: data.applyInformation,
    						processName:bigData.processName,
    						partyCode:bigData.partyCode  //新增YKY客户编码
    				}
    				that.audit = {
    						approvePartyName: bigData.reviewUserName,	//审核人
    						lastUpdateDate: bigData.lastUpdateDate, //审核时间
    						reason: bigData.reason,	//审核原因
    						status:bigData.status //审核状态
    					};
    			}
    		},false);  
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
        	  		content : $(target)[0].outerHTML,
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
        	showInfo:function(target,event){//公司信息显示隐藏
        		var btn = event.currentTarget
        		/*if($(btn).hasClass('show')){
        			$(btn).removeClass('show');
        		}else{
        			$(btn).addClass('show');
        		}*/
        		$(target).slideToggle();
        	},
        	examineEvent:function(type){
        		var that = this;
        		var applyType, backUrl, map;
        		var operPrefix = 'http:' + operationWebUrl.replace("/main","") + ykyUrl.party;
        		var qzInfo = that.qzInfo;
        		//账期审核
    			backUrl = operPrefix+"/v1/enterprises/accountPeriodAudit";    			
    			map = {
					approvePartyName: $('#userName').val(),//申请人
					checkCycle: qzInfo.checkCycle,//对账期限
					checkDate: qzInfo.checkDate,//对账日期（纯数字，代表每月几号）
					common: qzInfo.common,//描述说明
					contactUserName: qzInfo.contactUserName,
					creditAttachmentList: qzInfo.creditAttachmentList,//附件类
					creditDeadline: qzInfo.creditDeadline,//授信期限（月结xxx天）
					creditQuota: qzInfo.creditQuota,//授信额度
					currency: qzInfo.currency,//币种
					name: qzInfo.name,//公司名称
					partyId: qzInfo.partyId,//认证企业PARTY_ID
					payDate: qzInfo.payDate,//付款日期（纯数字，代表每月几号）
					realtimeCreditQuota: qzInfo.realtimeCreditQuota,//授信余额
					applyUser : that.info.applyUser,
	        		applyMail : that.info.applyMail,
	        		applyInformation : that.info.applyInformation,
	        		mail:that.info.mail
    			}
        		var listData = {
        				applyContent: JSON.stringify(map),
        				approvePartyId: $("#userId").val(),
        				id: that.companyInfo.id ,  //企业ID
        				callBackUrl: backUrl,  //回调url
        				remark:  '',   //审核意见
        				approvePartyName: $('#userName').val(),//申请人
        				partyCode:that.info.partyCode  //新增YKY编码
        			};
        		if(type == 'yes'){
        			applyType = 'APPROVED'; //通过        	请输入审核通过的原因
        			layer.confirm($('.reason-wrap').html(),{
        				offset: "auto",
        				btn: ['确      认','取      消'], //按钮
        				title: " ",
        				area: ['500px', '300px'],
        				//maxWidth:'800px',
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
        			var index = parent.layer.getFrameIndex(window.name); 
        			layer.title('请输入审核通过的原因', index)
        		}else{
        			applyType = 'REJECT'; //驳回      请输入审核不通过的原因	
        			
        			layer.confirm($('.reason-wrap').html(),{
        				offset: "auto",
        				btn: ['确      认','取      消'], //按钮
        				title: " ",
        				area: ['500px', '300px'],
        				//maxWidth:'800px',
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
        			var index = parent.layer.getFrameIndex(window.name); 
        			layer.title('请输入审核不通过的原因	', index)
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
        				
        				if("APPROVED" == type){
        					 syncData(ykyUrl.party +"/v1/enterprises/sendNodeCreitMail/" + that.entId,'GET',postObj,function(res,err){
        						 console.log(res);
        					})
        				}
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
        	},
        	getImage:function(imageUrl) {
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
        }
    }); 

})
function setLetterNum(e){
	var num = $(e).val().length;
	$(e).parent().find('.letter-num').text(num);
}