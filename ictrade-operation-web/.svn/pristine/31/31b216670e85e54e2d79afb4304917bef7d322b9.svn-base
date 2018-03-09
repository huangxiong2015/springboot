$(function(){
	//获取营业执照pdf名字getOthersValue(apply.otherAttrs,'BUSI_PDF_NAME')
	//获取税务登记pdf名字getOthersValue(apply.otherAttrs,'TAX_PDF_NAME')
	//获取组织机构pdf名字getOthersValue(apply.otherAttrs,'OCC_PDF_NAME')
	/* 请求数据 */
	//var listUrl = "http://operation-uat.ic-trade.com/services-workflow/v1/apply/APPLY/"+GetQueryString("id");
	var listUrl = ykyUrl.workflow + "/v1/apply/APPLY/"+GetQueryString("id");
 	$.aAjax({
		url: listUrl,
     	type:"POST",
     	data:JSON.stringify(showData.parameter),
     	success: function(data) {
	     	showData.list=data;
    	 	$.each(data.list,function(idx,el){
    		 	var apply = el.applyContent;
    		 	if(apply){	    			 
    		 		showData.list.list[idx].applyContent = JSON.parse(apply);
    		 	}
    		 	apply = showData.list.list[idx].applyContent;
    		 	if(!apply.occPic){
    		 		$("#occPic").addClass("dn");
    		 		if(!apply.taxRegPic){
    		 			showData.isShow = "Mainland";
    		 			//企业三证合一
    		 			$("#taxRegPic").addClass("dn");
    		 			$('input[name="license"]')[1].checked=true;
    		 		}else{
    		 			showData.isShow = "HK";
						//香港
						$("#business").addClass("dn");
    		 		}
    		 	}else{
    		 		showData.isShow = "Mainland";
    		 		//普通营业执照
    		 		$('input[name="license"]')[0].checked=true;
    		 	}
    		 	//function getPdf(applyData,picImgurl,pdffile,pdfPicID,pdfName,key,applyDataType,otherValue)
    		 	getPdf(apply.busiLicPic,'PicImgurl0','pdffile0','pdfPic0','pdfName0','BUSI_PDF_NAME','busiLicPic',showData.list.list[0].applyContent.otherAttrs) //获取营业执照图片,注册证书,IMG.PDF
    		 	getPdf(apply.taxRegPic,'PicImgurl1','pdffile1','pdfPic1','pdfName1','TAX_PDF_NAME','taxRegPic',showData.list.list[0].applyContent.otherAttrs) //税务登记,商业登记证 IMG.PDF
    		 	getPdf(apply.occPic,'PicImgurl2','pdffile2','pdfPic2','pdfName2','OCC_PDF_NAME','occPic',showData.list.list[0].applyContent.otherAttrs) //组织机构代码影印件,IMG.PDF
    		 	getPdf(apply.loaPic,'PicImgurl3','pdffile3','pdfPic3','pdfName3','LOA_PDF_NAME','loaPic',showData.list.list[0].applyContent.otherAttrs) //获取企业授权委托书,IMG.PDF
 /*    		 	if(apply.busiLicPic != ""){    		 		
      			//获取营业执照图片
    		 		getImage("busiLicPic",apply.busiLicPic);
    		 	}

    		 	if(apply.loaPic != "" && typeof(apply.loaPic) != "undefined" && apply.loaPic.toLowerCase().indexOf('.pdf')>-1){ 
    		 		//获取企业授权委托书图片
    		 		
    		 		$('.PicImgurl').addClass('dn');
    		 		$('.pdffile').removeClass('dn');
    		 		getImage("pdfPic",apply.loaPic,1);
    		 		$("#pdfNameTwo").text(getOthersValue(apply.otherAttrs,'LOA_PDF_NAME'));
    		 	}else{
		 			//获取企业授权委托书图片
    		 		$('.pdffile').addClass('dn');
    		 		$('.PicImgurl').removeClass('dn');
    		 		getImage("loaPic",apply.loaPic);
    		 	}
    		 	
    		 	if(apply.occPic != ""){
    		 		//组织机构代码影印件
    		 		getImage("occPic",apply.occPic);
    		 	}
    		 	if(apply.taxRegPic != ""){ 
    		 		//税务登记
    		 		getImage("taxRegPic",apply.taxRegPic);
    		 	}*/
        		
        		/* 请求数据  */
        		var listUrl = ykyUrl.party + "/v1/enterprises/certificate/"+data.list[0].applyOrgId;
        		$.aAjax({
        			url: listUrl,
        	     	type:"GET",
        	     	success: function(data) {        	     		
        	    	 	showData.info=data;       	    	    
        	    	 	showData.busiLisType = data.busiLisType;
        	    	 	//busiLisType 为COMMON普通营业执照，3-TO-1企业三证合一，HK-CODE香港
        	     	},
        	     	error:function(e){
        	 	    	layer.closeAll('loading');
        	  	    	console.log("系统错误，审批失败，请联系系统管理员："+e);
        			}
        		});
        	})
    	 	if(showData.list.list.length>0){
	    	 	//GET 公司行业
	    		$.aAjax({
	    			url:ykyUrl.database + "/v1/category/industry",
	    			type: 'GET',
	    			 async: false,
	    		    success: function(data){
	    		    	var str = "";
	    		    	var ids = showData.list.list[0].applyContent.industryCategory;
	    		    	var list = ids.split(",");
	    		    	for(var i=0;i<list.length;i++){
	    		    		for(var j=0;j<data.length;j++){
	    		    			if(list[i] == data[j].categoryId){
	    		    				str += data[j].categoryName+"、";
	    		    				break ;
	    		    			}
	    		    		}
	    		    		var other = showData.list.list[0].applyContent.otherAttrs;
	    		    		if(other!= "" && typeof(other) != "undefined"){
	    		    			if(list[i]=="1008"){
		    		    			str =str.substring(0,str.length-1)+"("+getOthersValue(showData.list.list[0].applyContent.otherAttrs,'INDUSTRY_CATEGORY_ID_OTHER')+")、";
		    		    		}	
	    		    		}else{
	    		    			str =str.substring(0,str.length-1)+"、";
	    		    		}
	    		    	}
	    		    	
	    		    	showData.industryCategory = str;
	    		    	//如果是最后一个‘、’号则去掉 add by helinmei
	    		    	var suffixStr="";
	    		    	if(str != "" && typeof(str) != "undefined"){
		        	 		var suffix = str.substring(str.length-1,str.length);
		    		    	if(suffix == "、"){
		    		    		suffixStr = str.substring(0,str.length-1);
		    		    	}
	    		    	}
	    		    	$("#industryCategory")[0].innerHTML = suffixStr;
	    		    }
	    		});
	    		//GET 公司类型
	    		$.aAjax({
	    		url:ykyUrl.database + "/v1/category/companytype",
	    		type: 'GET',
	    		    async: true,
	    		    success: function(data){
	    		    	var str = "";
	    		    	var corCategoryId = showData.list.list[0].applyContent.corCategory;
	    		    	for(var j=0;j<data.length;j++){
    		    			if(corCategoryId == data[j].categoryId){
    		    				str = data[j].categoryName;
    		    				break ;
    		    			}
    		    		}
	    		    	$("#corCategory")[0].innerHTML = str;
	    		    }
	    		});
	    		
/*	    	 	//审核人
	    	 	$.aAjax({
	    			url:ykyUrl.party + "/v1/users/"+showData.list.list[0].lastUpdateUser,
	    			type: 'GET',
	    		    async: false,
	    		    success: function(data){
	    		    	showData.list.list[0].lastUpdateUser = data.name;
	    		    }
	    		});*/
	    	 	//审核人

	    		//http://192.168.1.110:27090/v1/apply/112233445566/555?page=1&pageSize=20
	    		
	    	 	var listUrl = ykyUrl.workflow + "/v1/apply/"+data.list[0].applyOrgId+"/ORG_REGISTER_FLOW";
	    	 	$.aAjax({
	    			url:listUrl,
	    			type: 'GET',
	    		    async: false,
	    		    success: function(data){
	    		    	//根据用户id查询用户名
	    		    	for(var i=0;i<data.list.length;i++){
	    		    		var lastUpdateUser = data.list[i].lastUpdateUser;
	    		    		$.aAjax({
	    		    		//	url:ykyUrl.party + "/v1/users/"+lastUpdateUser,
	    		    			url:ykyUrl.party + "/v1/customers/"+lastUpdateUser+"/username",
	    		    			type: 'GET',
	    		    		    async: false,
	    		    		    success: function(data1){
	    		    		    	data.list[i].lastUpdateUser = data1.lastNameLocal;
	    		    		    }
	    		    		});
	    		    	}
	    		    	showData.auditList = data.list;
	    		    }
	    		});	
	    	 	
    	 	}
     	},
     	error:function(e){
 	    	layer.closeAll('loading');
  	    	console.log("系统错误，审批失败，请联系系统管理员："+e);
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