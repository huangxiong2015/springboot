/**
 * Created vendorBaseInfoAudit.js by zr.xieyuanpeng@yikuyi.com on 2017年8月21日.
 */

var vm = new Vue({
	el: '#baseInfoAudit',
	data:{
		applyId:getQueryString("applyId"),
		auditInfo:{},
		auditApplyContent:{},
		applyContentJsonObj:{},
		approveLogList:[],
		approveLogListLength:0,
		thisUrl:ykyUrl._this,
		categoryName:'',
		isNotMyVendor:false 
	},
	created: function(){
		var _this = this;
		var applyId = getQueryString("applyId");
		var thisHref = location.href;
		_this.isNotMyVendor = thisHref.indexOf("myVendor")==-1?true:false;  
		var applyUrl = _this.isNotMyVendor?ykyUrl.workflow + "/v2/apply":ykyUrl.workflow + "/v2/apply/myApply";
		//修改title
		if(!_this.isNotMyVendor&&window.document.title){
			window.document.title = "我的申请详情";
		}
		syncData(applyUrl, 'GET', {'applyId': applyId}, function(res,err) {
			if (!err && res !== '') {
				var data = res;
				var bigData = data.list[0] || {};
				data = JSON.parse(data.list[0].applyContent) || {};
				//to do，麻烦后台把格式给我
				_this.auditInfo = bigData;
				_this.applyContentJsonObj = bigData.applyContentJsonObj;
				_this.auditApplyContent = data;
				
				if(bigData.applyContentJsonObj.category){
					getCategoryName(bigData.applyContentJsonObj.category);
				}
			}
		},true);  
		getAuditLog(applyId);
	},
	methods:{
		examineEvent:function(type){
    		var that = this;
    		var applyType, backUrl, map;
    		var operPrefix = 'http:' + operationWebUrl.replace("/main","")+ykyUrl.party;
    		//账期审核
			backUrl = operPrefix+"/v1/vendorManage/apply/basedata/callback";    			
			/*map = {
				applyContentJsonObj:that.applyContentJsonObj
			}*/
			var listData = {
    				applyContent: that.auditInfo.applyContent,
    				approvePartyId: $("#userId").val(),
    				approvePartyName: $("#userName").val(),
    				id: that.auditInfo.applyOrgId,  //企业ID
    				remark:  '',   //审核意见
    				callBackUrl: backUrl,  //回调url
    			};
			var mailParam = {
					"companyName":that.auditInfo.companyName,
					"roleTypeEnum":'CEO',
					"vendorApplyType":that.auditInfo.processId,
					"cNodeId":that.auditInfo.cNodeId,
					"applyUserId":that.auditInfo.applyUserId
			}
    		if(type == 'yes'){
    			applyType = 'APPROVED'; //通过        	
    			layer.confirm("<p style='font-weight:bold;'>确定审核通过？</p><p></p>",{
    				offset: "auto",
    				btn: ['确      认','取      消'], //按钮
    				title: " ",
    				area: 'auto',
    				maxWidth:'500px',
    				move: false,
    				skin: "up_skin_class",
    				yes:function(){
    					that.dyAjax(applyType,listData,mailParam);
    				}
    			})
    		}else{
    			applyType = 'REJECT'; //驳回
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
    	dyAjax:function(type,postObj,mailParam){//审核接口调用
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
    				if(mailParam&&mailParam.cNodeId.indexOf('_1')!=-1&&type == 'APPROVED'){
    					triggleMailToPerson(mailParam);
    				}
        			setTimeout(function(){
        				layer.closeAll();
        				location.href = ykyUrl._this+"/auditVendor.htm";
        			}, 2000)
    			}
    		})
    	},
	}
})


/*统计textarea字数*/
function setLetterNum(e){
	var num = $(e).val().length;
	$(e).parent().find('.letter-num').text(num);
}

/*给CEO发邮件*/
function triggleMailToPerson(paramObj){
	syncData(ykyUrl.party +"/v1/vendorManage/sendMailTwo/" +paramObj.companyName+"?roleTypeEnum="+paramObj.roleTypeEnum+"&vendorApplyType="+paramObj.vendorApplyType+"&applyUserId="+paramObj.applyUserId,"POST",{},function(res,err){
		if(err == null){
			console.log("mail service success");
		}
	})
}
function getCategoryName(cateId){
	syncData(ykyUrl.database + "/v1/category/companylist?categoryTypeId=VENDOR_THE_TYPE", 'GET', null, function(res, err) {
		if (!err) {
			$.each(res,function(index,item){
				if(item.categoryId==cateId){
					vm.categoryName = item.categoryName;
				}
			})
		}
	});
}

//获取审核日志
function getAuditLog(applyId){
	syncData(ykyUrl.party + "/v1/audit?actions=Apply Authentication&actionId="+applyId, 'GET', null, function(res, err) {
		if (!err) {	
			vm.approveLogListLength = res.list.length;
			vm.approveLogList = res.list;
		}
	});
}
