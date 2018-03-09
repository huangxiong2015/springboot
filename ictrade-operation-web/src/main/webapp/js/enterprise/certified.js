
	var getImage = function(imageUrl) {
		var url = '';
		//获取图片公共方法
		$.aAjax({
			url: ykyUrl.party + "/v1/enterprises/getImgUrl",
	     	type:"POST",
	     	async:false,
	     	data:JSON.stringify({"id":imageUrl}),
	     	success: function(data) {

	     		/* if(data != ""){
	     			//如果type为1则是pdf文件，超链接新打开一个页面
	     			if(type ==1){
	     				 $("#"+imageId).attr('href',data);
	     			}else{
	     				
	     				$("#"+imageId +" img")[0].src = data;
	         			$("#"+imageId +"Img")[0].src = data;
	         			
	         			$("#"+imageId +"Img")[0].onload = function(){
	         				reSizeImg($("#"+imageId +"Img")[0]);
	         			}
	     			}
	     		
	     		} */
	     		if(data != '') {
		     		url = data;
	     		}
	     	}
	 	});
		
		return url;
	}

$(function(){
	
	
	//获取营业执照pdf名字getOthersValue(apply.otherAttrs,'BUSI_PDF_NAME')
	//获取税务登记pdf名字getOthersValue(apply.otherAttrs,'TAX_PDF_NAME')
	//获取组织机构pdf名字getOthersValue(apply.otherAttrs,'OCC_PDF_NAME')
	/* 请求数据 */
	//var listUrl = ykyUrl.workflow + "/v1/apply/APPLY/"+GetQueryString("id");
	var listUrl = ykyUrl.party + '/v1/enterprises/entDetail/1/' +  GetQueryString("id") + '/VIP_CORPORATION';//http://localhost:27082
 	$.aAjax({
		url: listUrl,
		//url: listUrl2,
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
	        		provinceName: data.provinceName || '-',
	        		cityName: data.cityName || '',
	        		countryName: data.countryName || '',
	        		address: data.address || '',
	        		dCode: data.map && data.map.D_CODE || '-',
	        		webSite: data.map && data.map.WEBSITE_URL || '-',
	        		partyCode: data.partyCode || '-',
	        		accountStatus: data.accountStatus || '',
	        		activeStatus: data.activeStatus || 'WAIT_APPROVE',
	        		activeStatus: data.activeStatus || '0'
	     	};
	     	
	     	showData.zzInfo = {
	     			regAddr: data.map && data.map.REG_ADDR || '0',
	     			busiLisType: data.map && data.map.BUSI_LIS_TYPE || '',
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
	     	}
     	},
     	error:function(e){
 	    	layer.closeAll('loading');
  	    	console.log("系统错误，审批失败，请联系系统管理员："+e);
		}
	})
	
	$('.btns button').on('click', function() {
		var type, backUrl;
		var operPrefix = $("#operationserverUrlPrefix").val().replace("/main","");
		if(showData.shType === 'entZz') {
			var isVipCenter = showData.companyInfo.isVipCenter;
			if(isVipCenter == '0') {
				backUrl = operPrefix+ykyUrl.party + "/v1/enterprises/editEntApplySave";
			}else if(isVipCenter == '1') {
				backUrl = operPrefix+ykyUrl.party + "/v1/enterprises";
			}
		}else {
			backUrl = operPrefix+ykyUrl.party + "/v1/entAuthorize";
		}
		
		var listData = {
			applyContent: JSON.stringify({
				id: showData.companyInfo.id ,  //企业ID
				occCode: showData.info.orgCode,
				operationType: showData.shType === 'entZz' ? '企业资质' : '子账号管理',
				map: { //三证信息
					SOCIAL_CODE: showData.info.socialCode,
					ENT_NAME: showData.info.entName,
					LOCATION: showData.info.location,
					BUSI_RANGE: showData.info.busiRange,
					FAX_CODE: showData.info.faxCode,
					FAX_NAME: showData.info.faxName,
					ORG_CODE: showData.info.orgCode,
	    			orgName: showData.info.orgName,
	    			ORG_NAME: showData.info.orgLocation,
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
				}
			}),
			approvePartyId: $("#userId").val(),
		  	approvePartyName: $("#userName").val(),
			id: showData.companyInfo.id ,  //企业ID
			callBackUrl: backUrl,  //回调url
			remark:  showData.reason   //审核意见
		};
		
		if($(this).attr('id') === 'ok') {
			type = 'APPROVED'; //保存
			dyAjax(type);
		}else {
			type = 'REJECT'; //取消
			layer.alert("是否确认取消该企业的认证信息？",{
	    		offset: "auto",
	    		btn: ['确      认', '取      消'], //按钮
				title: " ",
				area: ['350px', '170px'],
				move: false,
				skin: "up_skin_class",
				yes:function(){
					dyAjax(type);
				},
				end:function(){
					
				}
			})
		}
		
		 
		//调用接口封装
		function dyAjax(type) {
			$.aAjax({
				url: ykyUrl.workflow+"/v1/task/"+type+"/"+showData.companyInfo.id,
				type: 'PUT',
				data: JSON.stringify(listData),
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
				},
				error: function(e) {
					throw e.respoonseText;
				}
			})
		}
		
	})
	
	
})


//获取路径中的参数值
function GetQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}
//other取值
function getOthersValue(otherValue,key){
	var other;
	if(otherValue!= "" && typeof(otherValue) != "undefined"){
		for(var i=0;i<otherValue.length;i++){
		   other = otherValue[i][key];
	 	   if(other){
	 		break;
	 	   }
		}
	}
	return other;
}
function getPdf(applyData,picImgurl,pdffile,pdfPicID,pdfName,key,applyDataType,otherValue){

	if(applyData!= "" && typeof(applyData) != "undefined" && applyData.toLowerCase().indexOf('.pdf')>-1){ 
 		//获取pdf
 		$('.' + picImgurl).addClass('dn');
 		$('.' + pdffile).removeClass('dn');
 		getImage(pdfPicID,applyData,1);
 		$("#" +pdfName).text(getOthersValue(otherValue,key));
 	}else{
			//获取图片
 		$('.' + pdffile).addClass('dn');
 		$('.' + picImgurl).removeClass('dn');
 		getImage(applyDataType,applyData,null,pdffile,picImgurl);
 	}
}

function getImage(imageId,imageUrl,type,pdffile,picImgurl){

	//获取图片公共方法
	$.aAjax({
		url: ykyUrl.party + "/v1/enterprises/getImgUrl",
     	type:"POST",
     	async:false,
     	data:JSON.stringify({"id":imageUrl}),
     	success: function(data) {

     		if(data != ""){
     			//如果type为1则是pdf文件，超链接新打开一个页面
     			if(type ==1){
     				 $("#"+imageId).attr('href',data);
     			}else{
     				
     				$("#"+imageId +" img")[0].src = data;
         			$("#"+imageId +"Img")[0].src = data;
         			
         			$("#"+imageId +"Img")[0].onload = function(){
         				reSizeImg($("#"+imageId +"Img")[0]);
         			}
     			}
     		
     		}
     	}
 	});
}