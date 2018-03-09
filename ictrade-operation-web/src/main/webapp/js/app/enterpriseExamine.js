//获取行业类别
var industryCategoryData = {};
$.ajax({
	url: ykyUrl.database + '/v1/category/industry',
	type: 'GET',
	async: false,
	success: function(data) {
		if(data) {
			$.each(data, function(index, value) {
				industryCategoryData[value.categoryId] = value.categoryName;
			})
		}
	}
})

//获取公司类型
var corCategoryData = {};
	
$.ajax({
	url: ykyUrl.database + '/v1/category/companytype',
	type: 'GET',
	async: false,
	success: function(data) {
		if(data) {
			$.each(data, function(index, value) {
				corCategoryData[value.categoryId] = value.categoryName;
			})
		}
	}
})


//获取图片公共方法
var getImage = function(imageUrl) {
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

//获取路径中的参数值
function GetQueryString(name){
   var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if(r!=null)return  unescape(r[2]); return null;
}

//输入框限制字数40
$('input').on('blur', function() {
	var valStr = $(this).val().substr(0, 40);
	$(this).val(valStr);
})

var showData = avalon.define({
	$id : "data",
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
		regAddr : '',
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
		socialCode : '',
		entName : '',
		location : '',
		busiRange : '',
		faxCode : '',
		faxName : '',
		orgCode : '',
		orgName : '',
		orgLocation : '',
		orgCdate : '',
		orgLimit : '',
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
		abroadEntNum : '' //ABROAD_ENT_NUM
	},
	shType : '', //审核类型
	auditList : [],
	busiLisType : "",
	toHtml : function(data) {
		if (data == 'undefined' || !data) {
			return "--";
		} else {
			return data;
		}
	},
	showData : function(index, type) {
		if (type == 'part') {
			return index < 4
		}
		if (type == 'all') {
			$('.Infologomin').show();
			$('.look-morelog .look').hide();
		}
	},
	remark : ''
})

//根据id找到公司类型
avalon.filters.corCategoryName = function(str) {
	var result = '';
	
	if(str) {
		if(str == 2006) {
			result = corCategoryData[str] + '('+ showData.companyInfo.CORPORATION_CATEGORY_ID_OTHER +')';
		}else {
			result = corCategoryData[str];
		}
	}else {
		result = '-'
	}
	return result;
}

//根据id找到行业类别
avalon.filters.industryCategoryName = function(str) {
	var returnStr = '';
	var list = str.split(',');
	
	$.each(list, function(i, item) {
		if (item != '1008') {
			if (i === list.length - 1) {
				returnStr += industryCategoryData[item];
			} else {
				returnStr += industryCategoryData[item] + '+';
			}
		} else {
			if (i === list.length - 1) {
				returnStr += industryCategoryData[item] + '('
						+ showData.companyInfo.industryOther + ')';
			} else {
				returnStr += industryCategoryData[item] + '('
						+ showData.companyInfo.industryOther + ')'
						+ '+';
			}
		}
	})
	
	return str ? returnStr : '-';
}

avalon.filters.regAddrName = function(str) {
	var returnStr = '';
switch (str) {
case '0':
	returnStr = '大陆';
	break;
case '1':
	returnStr = '香港';
	break;
case '2':
	returnStr = '台湾';
	break;
case '3':
	returnStr = '境外';
		break;
	}
	return returnStr;
}


$(function(){
	
	//审核类型
	showData.shType = GetQueryString('type');
	
	//拉取企业信息
	var listUrl = ykyUrl.party + '/v1/enterprises/entDetail/'+ GetQueryString("id") +'/'+ GetQueryString("entId") +'/CORPORATION';
 	$.aAjax({
		url: listUrl,
     	type:"GET",
     	success: function(data) {
     		if(typeof data === 'undefined') {
     			data = {};
     		}
     		
	     	showData.companyInfo = {
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
	     	};
     	},
     	error:function(e){
 	    	layer.closeAll('loading');
  	    	console.error("系统错误，审批失败，请联系系统管理员："+e);
		}
	})
	
	
	//拉取三证信息
	$.aAjax({
		url: ykyUrl.workflow + '/v1/apply/APPLY/' + GetQueryString("applyId"),
		type: 'POST',
		data: JSON.stringify({queryVo: {}}),
		success: function(data) {
			
			try {
				var data = JSON.parse(data.list[0].applyContent) || {};
				showData.zzInfo = {
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
			 	
			 	showData.info = {
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
						hkEntName: data.map && data.map.HK_ENT_NAME || '',  //香港-公司名称
						hkSignCdate: data.map && data.map.HK_SIGN_CDATE || '', //香港-签发日期
						hkBusiName: data.map && data.map.HK_BUSI_NAME || '', //香港-业务所用名称
						hkAddr: data.map && data.map.HK_ADDR || '', //香港-地址
						hkEffectiveDate: data.map && data.map.HK_EFFECTIVE_DATE || '', //生效日期
						twEntName: data.map && data.map.TW_ENT_NAME || '', //台湾-公司名称
						twSignCdate: data.map && data.map.TW_SIGN_CDATE || '', //台湾-签发日期
						twFaxEntName: data.map && data.map.TW_FAX_ENT_NAME || '', //台湾-税籍登记证-公司名称
						twFaxSignCdate: data.map && data.map.TW_FAX_SIGN_CDATE || '', //台湾-税籍登记证-签发日期
						twFaxCode: data.map && data.map.TW_FAX_CODE || '',  //台湾-税务编码
						abroadEntName: data.map && data.map.ABROAD_ENT_NAME || '', //ABROAD_ENT_NAME
						abroadEntNum: data.map && data.map.ABROAD_ENT_NUM || '', //ABROAD_ENT_NUM
						contactUserName: data.contactUserName,
						mail: data.mail
			 	}
				
			}catch(e) {
				console.error('接口没有三证的资料，或者资料不全');
			}
			
		},
		error: function(e) {
			console.error('请联系系统管理员，error:' + e.responseText);
		}
	})
	
	
//	审核
	$('.btns button').on('click', function() {
		var type, backUrl, map;
		var operPrefix = $("#operationserverUrlPrefix").val().replace("/main","") + ykyUrl.party;
		
		if(showData.shType === 'ORG_DATA_REVIEW') {  //企业资质审核
			var isVipCenter = showData.companyInfo.isVipCenter;
			if(isVipCenter == '0') { // 此页面不是从会员中心跳转过来的
				backUrl =operPrefix + "/v1/enterprises/editEntApplySave";
			}else{	//此页面是从会员中心跳转过来的
				backUrl = operPrefix +"/v1/enterprises";
			}
			
			map = {  //三证信息
					SOCIAL_CODE: showData.info.socialCode,
					ENT_NAME: showData.info.entName,
					LOCATION: showData.info.location,
					BUSI_RANGE: showData.info.busiRange,
					FAX_CODE: showData.info.faxCode,
					FAX_NAME: showData.info.faxName,
					ORG_CODE: showData.info.orgCode,
					ORG_LOCATION: showData.info.orgLocation,
	    			ORG_NAME: showData.info.orgName,
	    			ORG_CDATE: showData.info.orgCdate,
	    			ORG_LIMIT: showData.info.orgLimit,
	    			HK_ENT_NAME: showData.info.hkEntName,  //香港-公司名称
	    			HK_SIGN_CDATE: showData.info.hkSignCdate, //香港-签发日期
	    			HK_BUSI_NAME: showData.info.hkBusiName, //香港-业务所用名称
	    			HK_ADDR: showData.info.hkAddr, //香港-地址
	    			HK_EFFECTIVE_DATE: showData.info.hkEffectiveDate, //生效日期
	    			TW_ENT_NAME: showData.info.twEntName, //台湾-公司名称
	    			TW_SIGN_CDATE: showData.info.twSignCdate, //台湾-签发日期
	    			TW_FAX_ENT_NAME: showData.info.twFaxEntName, //台湾-税籍登记证-公司名称
	    			TW_FAX_SIGN_CDATE: showData.info.twFaxSignCdate, //台湾-税籍登记证-签发日期
	    			TW_FAX_CODE: showData.info.twFaxCode, //台湾-税务编码
	    			ABROAD_ENT_NAME: showData.info.abroadEntName, //ABROAD_ENT_NAME
	    			ABROAD_ENT_NUM: showData.info.abroadEntNum, //ABROAD_ENT_NUM
	    			
	    			REG_ADDR:showData.zzInfo.regAddr,//公司注册地
	    			BUSI_LIS_TYPE:showData.zzInfo.busiLisType,//注册类型
	    			BUSI_LIC_PIC:showData.zzInfo.initialBusiLicPic || '',
	    			OCC_PIC:showData.zzInfo.initialOocPic || '',
	    			TAX_REG_PIC:showData.zzInfo.initialTaxPic || '',
	    			BUSI_PDF_NAME:showData.zzInfo.busiPdfName || '',
	    			OCC_PDF_NAME:showData.zzInfo.oocPdfName || '',
	    			TAX_PDF_NAME:showData.zzInfo.taxPdfName || '',
			}
			
		}else { //子账号审核
			backUrl = operPrefix+"/v1/enterprises/entAuthorize";
			
			map = { //授权委托书
    			LOA:showData.zzInfo.initialLoaPic || '',
    			LOA_PDF_NAME:showData.zzInfo.loaPdfName || ''
			}
		}
		
		var listData = {
			applyContent: JSON.stringify({
				id: showData.companyInfo.id ,  //企业ID
				occCode: showData.info.orgCode,
				operationType: showData.shType === 'ORG_DATA_REVIEW' ? '企业资质' : '子账号管理',
				mail:showData.info.mail,
				contactUserName:showData.info.contactUserName,
				entUserId:GetQueryString("id"),
				approvePartyName: $("#userName").val(),//审核人
				name: showData.companyInfo.name,
				map: map //三证信息
			}),
			approvePartyId: $("#userId").val(),
			id: showData.companyInfo.id ,  //企业ID
			callBackUrl: backUrl,  //回调url
			remark:  $('#remark').val()   //审核意见
		};
		
		if($("#remark").length > 0 && $.trim($("#remark").val()) === ''){
			layer.msg('审核意见不能为空');
			$("#remark").focus();
			return false;
		}
		
		if($(this).attr('id') === 'ok') {
			type = 'APPROVED'; //通过
			
			if($("#socialCode").length > 0 && $.trim($("#socialCode").val()) === ''){
				layer.msg('统一社会信用代码不能为空');
				$("#socialCode").focus();
				return false;
			}
			
			if($("#orgCode").length > 0 && $.trim($("#orgCode").val()) === ''){
				layer.msg('机构代码不能为空');
				$("#orgCode").focus();
				return false;
			}
			
			if($("#orgCode2").length > 0 && $.trim($("#orgCode2").val()) === ''){
				layer.msg('机构代码不能为空');
				$("#orgCode").focus();
				return false;
			}			
			
			if($('#entName').size() > 0 && $.trim($('#entName').val()) === '') {
				layer.msg('请填写公司名称');
				$('#entName').focus();
				return false;
			}
			
			if($('#hkEntName').size() > 0 && $.trim($('#hkEntName').val()) === '') {
				layer.msg('请填写公司名称');
				$('#entName').focus();
				return false;
			}
			
			if($('#twEntName').size() > 0 && $.trim($('#twEntName').val()) === '') {
				layer.msg('请填写公司名称');
				$('#twEntName').focus();
				return false;
			}
			
			if($('#abroadEntName').size() > 0 && $.trim($('#abroadEntName').val()) === '') {
				layer.msg('请填写公司名称');
				$('#abroadEntName').focus();
				return false;
			}
			
			dyAjax(type, listData);
		}else {
			type = 'REJECT'; //驳回
			
			layer.alert("是否确认驳回该企业的认证申请？",{
	    		offset: "auto",
	    		btn: ['确      认', '取      消'], //按钮
				title: " ",
				area: ['350px', '170px'],
				move: false,
				skin: "up_skin_class",
				yes:function(){
					dyAjax(type, listData);
				},
				end:function(){
					
				}
			})
		}	
	})	
})

//调用审核接口封装
function dyAjax(type, postObj) {
	
	$.aAjax({
		url:  ykyUrl.workflow +"/v1/task/" + type + "/" + GetQueryString("applyId"),
		type: 'PUT',
		data: JSON.stringify(postObj),
		success: function(data) {
			
			var alertTxt = '';
			
			if(type === 'APPROVED') {
				alertTxt = '审核通过';
			}else {
				alertTxt = '驳回成功';
			}
			
			layer.alert(alertTxt, {
	    		offset: "auto",
				btn: false, //按钮
				title: " ",
				area: ['350px', '150px'],
				move: false,
				skin: "up_skin_class"
	    	});
			
			setTimeout(function(){
				layer.closeAll()
				location.href = location.origin + location.pathname;
			}, 1000)
		},
		error: function(e) {
			console.error('接口错误，请联系系统管理员， error: ' + e.respoonseText);
		}
	})
}
