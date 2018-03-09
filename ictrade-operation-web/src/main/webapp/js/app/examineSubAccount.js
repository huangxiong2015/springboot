var	 vm ;
$(function(){
	
vm = new Vue({
        el: '#examineQualification',
        data: {
        	id:getQueryString("id"),//企业id
        	entId:getQueryString("entId"),//父企业id
        	applyId:getQueryString("applyId"),
        	type:getQueryString("type"),
        	industry:{},//所属行业
        	companytype:{},//公司类型
        	audit:{},
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
        		loaPic : '', //授权书
        		loaPdfName : '' //授权书pdf
        	},
        	info : {
        		applyUser : '',
        		applyMail : '',
        		applyInformation : '',
        		reason:'',
        		mail:''
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
        	syncData(ykyUrl.workflow + "/v2/apply", 'GET', {applyId: that.applyId}, function(res,err) {
    			if (!err && res !== '') {
    				var data = res;
    				var bigData = data.list[0] || {};
    				data = JSON.parse(data.list[0].applyContent) || {};
    				that.zzInfo = {
    			 			loaPic: data.map && getImage(data.map.LOA) || '',
    			 			loaPdfName: data.map && data.map.LOA_PDF_NAME || ''
    				}
    				that.info = {
    						contactUserName: data.contactUserName,
    						mail: data.mail || '',
    						contactUserTel: data.contactUserTel || '',
    						createdDate: bigData.createdDate || '',
    						applyUser: data.applyUser,
    						applyMail: data.applyMail,
    						applyInformation: data.applyInformation,
    						reason:data.reason,
    						processName:bigData.processName
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
        		if($(btn).hasClass('show')){
        			$(btn).removeClass('show');
        		}else{
        			$(btn).addClass('show');
        		}
        		$(target).toggle(300);
        	},
        	examineEvent:function(type){
        		var that = this;
        		var applyType, backUrl, map;
        		var operPrefix = 'http:' + operationWebUrl.replace("/main","") + ykyUrl.party;
        		//var operPrefix = 'http://192.168.1.110:27082';
        		//子账号审核
    			backUrl = operPrefix+"/v1/enterprises/entAuthorize";    			
    			map = { //授权委托书
        			LOA:that.zzInfo.loaPic || '',
        			LOA_PDF_NAME:that.zzInfo.loaPdfName || ''
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
        	        		reason:that.info.reason,
        				}),
        				approvePartyId: $("#userId").val(),
        				id: that.companyInfo.id ,  //企业ID
        				callBackUrl: backUrl,  //回调url
        				remark:  ''   //审核意见
        			};
        		if(type == 'yes'){
        			applyType = 'APPROVED'; //通过        			
        			/*layer.confirm("<p style='font-weight:bold;'>确定审核通过？</p><p>审核通过后，该用户可以认证企业身份采购</p>",{
        				offset: "auto",
        				btn: ['确      认','取      消'], //按钮
        				title: " ",
        				area: 'auto',
        				maxWidth:'500px',
        				move: false,
        				skin: "up_skin_class",
        				yes:function(){
        					that.dyAjax(applyType,listData);
        				}
        			})*/
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
        						$('.up_skin_class #remark').attr('placeholder','审核不通过的原因不能为空');
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
            				location.href = location.origin + location.pathname;
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
