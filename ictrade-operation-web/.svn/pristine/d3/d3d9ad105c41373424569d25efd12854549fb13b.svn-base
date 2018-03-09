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
	
	if(!imageUrl) return;
	
	var url = '';
	$.aAjax({
		url: ykyUrl.party + "/v1/enterprises/getImgUrl",
     	type:"POST",
     	async:false,
     	data:JSON.stringify({"id":imageUrl}),
     	success: function(data) {
     		if(data != '') {
	     		url = data;
     		}else {
     			console.warn('图片地址转换失败，无返回值！！');
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

var showData = avalon.define({
	$id : "data",
	companyInfo : {
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
		activeStatus : ''
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
	audit: {
		approvePartyName: '',	//审核人
		lastUpdateDate: '', //审核时间
		reason: ''	//审核原因
	},
	shType: '', //审核类型
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
	}
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
	
	/* 拉取企业信息 */
	var listUrl = ykyUrl.party + '/v1/enterprises/entDetail/'+ GetQueryString("id") +'/'+ GetQueryString("entId") +'/CORPORATION';
 	$.aAjax({
		url: listUrl,
     	type:"GET",
     	success: function(data) {
     		if(typeof data === 'undefined') {
     			data = {};
     		}
     		
	     	showData.companyInfo = {
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
	        		activeStatus: data.activeStatus || ''
	     	};
     	},
     	error:function(e){
 	    	layer.closeAll('loading');
  	    	console.log("系统错误，审批失败，请联系系统管理员："+e);
		}
	})
	
	//拉取三证信息
	$.aAjax({
		url: ykyUrl.workflow + '/v1/apply/APPLY/' + GetQueryString("applyId"),
		type: 'POST',
		data: JSON.stringify({queryVo: {}}),
		success: function(data) {
			
			try {
				var bigData = data.list[0] || {};
				var data = JSON.parse(data.list[0].applyContent) || {};
				showData.zzInfo = {
			 			regAddr: data.map && data.map.REG_ADDR || '0',
			 			busiLisType: data.map && data.map.BUSI_LIS_TYPE || '',
			 			initialBusiLicPic: data.map && data.map.BUSI_LIC_PIC,
			 			initialOocPic: data.map && data.map.OCC_PIC,
			 			initialTaxPic: data.map && data.map.TAX_REG_PIC,
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
						abroadEntNum: data.map && data.map.ABROAD_ENT_NUM || '' //ABROAD_ENT_NUM
			 	};
				
				showData.audit = {
					approvePartyName: bigData.reviewUserName,	//审核人
					lastUpdateDate: bigData.lastUpdateDate, //审核时间
					reason: bigData.reason	//审核原因
				};
				
			}catch(e) {
				console.error('接口没有三证的资料，或者资料不全');
			}
			
		},
		error: function(e) {
			console.error('请联系系统管理员，error:' + e.responseText);
		}
	})
	
	
})