/**
 * Created archivingAudit.js by zr.xieyuanpeng@yikuyi.com on 2017年8月21日.
 */

var vm = new Vue({
	el: '#detailVendor',
	data: {
		applyId:getQueryString('applyId'),
		tabValue: 'baseData', //baseData,product,credit,sales
		baseData: {
			vendorInfoAttributeMap:{}
		},
		productLines: [],
		productLinesNot:[],
		prodctType : 'PROXY',
		creditInfo: {
			creditAttributeMap:{}
		},
		salesInfo: {
			saleAttributeMap:{}
		},
		isSalesInfoNotNull:true,
		infoMap:{},
		vendorLogo: '',
		auditInfo:{},
		auditApplyContent:{},
		applyContentJsonObj:{},
		approveLogList:[],
		approveLogListLength:0,
		thisUrl:ykyUrl._this,
		downLoadUrl:[],
		creditAttachListLength:0,
		isNotMyVendor:false,
	},
	created: function(){
		var _this =this;
		var applyId = _this.applyId;
		var thisHref = location.href;
		_this.isNotMyVendor = thisHref.indexOf("myVendor")==-1?true:false;  
		var applyUrl = _this.isNotMyVendor?ykyUrl.workflow + "/v2/apply":ykyUrl.workflow + "/v2/apply/myApply";
		//修改title
		if(!_this.isNotMyVendor&&window.document.title){
			window.document.title = "我的申请详情";
		}
		syncData(applyUrl, 'GET', {'applyId':_this.applyId}, function(res,err) {
			if (!err && res !== '') {
				var data = res;
				var bigData = data.list[0] || {};
				data = JSON.parse(data.list[0].applyContent) || {};
				_this.auditInfo = bigData;
				_this.applyContentJsonObj = bigData.applyContentJsonObj;
				_this.auditApplyContent = data;
				
				var partyId = _this.auditInfo.applyOrgId;
				
				if(bigData&&bigData.applyContentJsonObj){
					
					var allInfoVo = bigData.applyContentJsonObj;
					if(allInfoVo.map){
						_this.infoMap = allInfoVo.map;
					}
					//基本信息
					if(allInfoVo.vendorInfoVo){
						_this.baseData = allInfoVo.vendorInfoVo;
						var tempAllInfoVo = allInfoVo.vendorInfoVo;
						var deptId = tempAllInfoVo.deptId;
						var enquiryList = tempAllInfoVo.enquiryList;
						var offerList = tempAllInfoVo.offerList;
						var equiryName =[];
						var offerName = [];
						if(tempAllInfoVo.enquiryName==""&&enquiryList&&enquiryList.length>0){
							//获取负责人、询价员数据
							syncData(ykyUrl.party + "/v1/dept/findCustomerByDeptId?page=1&size=100&deptId="+deptId, 'GET', null, function(res, err) {
								if (!err) {
									if(res && res.list.length > 0){
										var personArr = res.list;
										var personArrLength = personArr.length;
										for(var index=0;index<personArrLength;index++){
											//询价员
											for(var m=0;m<enquiryList.length;m++){
												if(personArr[index].partyId==enquiryList[m]){
													equiryName.push(personArr[index].partyAccount);
												}
											}
											//报价员
											for(var n=0;n<offerList.length;n++){
												if(personArr[index].partyId==offerList[n]){
													offerName.push(personArr[index].partyAccount);
												}
											}
										}
										tempAllInfoVo.enquiryName = equiryName.join(",");
										tempAllInfoVo.offerName = offerName.join(",");
										_this.baseData.enquiryName = tempAllInfoVo.enquiryName;
										_this.baseData.offerName = tempAllInfoVo.offerName;
									}
								}
							});
						}
					}
					if(!allInfoVo.vendorInfoVo.vendorInfoAttributeMap){
						_this.baseData.vendorInfoAttributeMap = {};
						_this.baseData.vendorInfoAttributeMap.VENDOR_INFO_WEBSITE = "";
					}
					if(_this.baseData.logoImageUrl){
						getPrivateUrl(_this.baseData.logoImageUrl);
					}
					//产品线
					if(allInfoVo.partyProductLineList){
						var dataList = allInfoVo.partyProductLineList;
						var productList = [];
						var productListNot = [];
						for(var i = 0;i < dataList.length; i++){
							if(dataList[i].type == 'NOT_PROXY'){
								productListNot.push(dataList[i]);
							}else{
								productList.push(dataList[i]);
							}
						}
						_this.productLines = productList;
						_this.productLinesNot = productListNot;
					}
					/*信用*/
					if(allInfoVo.vendorCreditVo){
						_this.creditInfo = allInfoVo.vendorCreditVo;
						var creditAttachList = allInfoVo.vendorCreditVo.creditAttachmentList;
						var attachUrls = [];
						_this.creditAttachListLength = creditAttachList.length;
						if(creditAttachList.length>0){
							$.each(creditAttachList,function(index,item){
								if(item.attachmentUrl){
									attachUrls.push(item.attachmentUrl);
								}
							})
							getPrivateUrls(attachUrls);
						}
					}
					if(!allInfoVo.vendorCreditVo.creditAttributeMap){
						_this.creditInfo.creditAttributeMap = {};
					}
					/*销售信息*/
					if(allInfoVo.vendorSaleInfoVo){
						_this.salesInfo = allInfoVo.vendorSaleInfoVo;
						for(var i = 0; i<_this.salesInfo.contactPersonInfoList.length; i++){
							if(_this.salesInfo.contactPersonInfoList[i].partyProductLineIdList){
								_this.salesInfo.contactPersonInfoList[i].partyProductLineList = [];
								for(var j = 0; j < _this.salesInfo.contactPersonInfoList[i].partyProductLineIdList.length; j++){
			        				for(var k = 0; k < _this.salesInfo.partyProductLineList.length; k++){
			        					if(_this.salesInfo.contactPersonInfoList[i].partyProductLineIdList[j] === _this.salesInfo.partyProductLineList[k].partyProductLineId){
			        						_this.salesInfo.contactPersonInfoList[i].partyProductLineList.push(_this.salesInfo.partyProductLineList[k]);
			        					}
			        				}
			        			}
							}    					        		
						}
						if(!allInfoVo.vendorSaleInfoVo.saleAttributeMap){
							allInfoVo.vendorSaleInfoVo.saleAttributeMap = {};
						}
					}
					if(allInfoVo.vendorSaleInfoVo ==null){  /*销售信息没有的时候不显示*/
						_this.isSalesInfoNotNull = false;
					}
					//
				}
				
			}
		},true);
		if(applyId){
			getAuditLog(applyId);
		}
	},
	methods: {
		examineEvent:function(type){
    		var that = this;
    		var applyType, backUrl, map;
    		var operPrefix = 'http:' + operationWebUrl.replace("/main","")+ykyUrl.party;
    		//账期审核
			backUrl = operPrefix+"/v1/vendorManage/apply/vendor/callback";    			
			var listData = {
    				applyContent: that.auditInfo.applyContent,
    				approvePartyId: $("#userId").val(),
    				approvePartyName: $("#userName").val(),
    				callBackUrl: backUrl,  //回调url
    				id: that.auditInfo.applyOrgId,  //企业ID
    				remark:  '',   //审核意见
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
    			layer.confirm("<p style='font-weight:bold;'>确定审核通过？</p><p>审核通过后，该供应商可在平台上传、销售授权商品</p>",{
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
    	//tab切换代理产品线和非代理产品线
        productLineChange: function(type){
        	var _this = this;
        	_this.prodctType = type;
        	getProductLine(_this.id,type);
        },
	},
	watch: {
		
	}
})


function getPrivateUrl(url){
	$.aAjax({
		url: ykyUrl.party + "/v1/enterprises/getImgUrl",
     	type:"POST",
     	data:JSON.stringify({"id":url}),
     	success: function(data) {
     		if(null !=data || data == ""){
     			vm.vendorLogo = data;
     		}
     	},
     	error:function(e){
	    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
	    }
 	});
}

function getPrivateUrls(urlArr){
	var urls = urlArr;
	var emptylist = [];
	function getPrivateUrlSubProcedure(urls){
		$.aAjax({
			url: ykyUrl.party + "/v1/enterprises/getImgUrl",
	     	type:"POST",
	     	data:JSON.stringify({"id":urls[0]}),
	     	success: function(data) {
	     		emptylist.push(data);
     			urls = urls.splice(1);
	     		if(urls.length!=0){
	     			while(urls[0]==""&&urls.length>0){
	     				emptylist.push("");
	         			urls.splice(1);
	     			}
	     			if(urls.length>0){
	     				getPrivateUrlSubProcedure(urls);
	     			}
	     			
	     		}else if(urls.length==0){
	     			vm.downLoadUrl = emptylist;
	     			/*return emptylist;*/
	     		}
	     	},
	     	error:function(e){
	     		emptylist.push(urls[0]);
	     		urls = urls.splice(1);
	     		if(urls.length!=0){
	     			while(urls[0]==""&&urls.length>0){
	     				emptylist.push("");
	         			urls.splice(1);
	     			}
	     			if(urls.length>0){
	     				getPrivateUrlSubProcedure(urls);
	     			}
	     			
	     		}else if(urls.length==0){
	     			vm.downLoadUrl = emptylist;
	     			/*return emptylist;*/
	     		}
		    	console.log("系统错误，审批失败，请联系系统管理员："+e.responseText);
		    }
	 	});
	}
	getPrivateUrlSubProcedure(urls);
}
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

//获取审核日志
function getAuditLog(applyId){
	syncData(ykyUrl.party + "/v1/audit?actions=Apply Authentication&actionId="+applyId, 'GET', null, function(res, err) {
		if (!err) {	
			vm.approveLogListLength = res.list.length;
			vm.approveLogList = res.list;
		}
	});
}
