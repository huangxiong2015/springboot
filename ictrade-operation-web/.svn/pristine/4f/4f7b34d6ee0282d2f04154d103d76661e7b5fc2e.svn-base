//阿里云私有图片转码
var getImage = function(imageUrl) {

	var url = imageUrl ? imageUrl : '';
	
	if(url == '') return '';
	
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
}

var upStart = function(signatureData, par){
	var postData = $.extend({}, signatureData);
	var callbackData = {};
	var up = par.up;
	postData["key"] = signatureData.key;
	postData["OSSAccessKeyId"] = signatureData.OSSAccessKeyId;
	postData["policy"] = signatureData.policy;
	postData["Signature"] = signatureData.Signature;
	postData["x-oss-security-token"] = signatureData.securityToken;
	postData["success_action_status"] = "200";
	up.setOption("url", postData.host);
	up.setOption("multipart_params", postData);
	up.start();
}

//根据文件上传类型获取文件直传的Signature
var getSignatures = function(uploadType, fileName, isImage, par) {
	currentGetSignatureTime = Date.parse(new Date());
	$.aAjax({
		type : 'GET',
		url : ykyUrl.database + "/v1/oss/upload?uploadType=" + uploadType + "&key="+fileName +"&isImage="+isImage , 
		contentType : "application/json",
		xhrFields: {
	        withCredentials: true
	    },
	    crossDomain: true,
		success : function(data) {
			upStart(data, par);
		},
		error : function(data) {
			
		}
	});
};

//创建上传(必须上传)
var createUploader = function(browseButtonId,fileSize,callback) {
	var pluploader = new plupload.Uploader({
		runtimes : 'html5,flash,silverlight',
		browse_button : browseButtonId,
		multi_selection : false,
		flash_swf_url : 'js/lib/plupload-2.1.2/js/Moxie.swf',
		silverlight_xap_url : 'js/lib/plupload-2.1.2/js/Moxie.xap',
		url : 'http://oss.aliyuncs.com',
		filters : {
			mime_types : [ // 只允许上传图片
			{
				title : "Image files",
				extensions : "jpg,jpeg,bmp,png,pdf,PDF,gif"
			} ],
			max_file_size : fileSize + 'mb', // 最大只能上传5mb的文件
			prevent_duplicates : false//允许选取重复文件
		},
		init : {
			FilesAdded : function(up, files) {
				index = layer.load(); 
				
				getSignatures("ent.certificates", files[0].name, true, {
					up: up
				});
			},
			FileUploaded : function(up, file, info) {
				layer.close(index);
				if (info.status == 200 || info.status == 203) {
					var params = pluploader.getOption("multipart_params");

					var imageUrl = params["imageUrl"];// 数据库保存
					var imageUrlPreview = getImage(imageUrl); // 渲染到页面
					var isPdf = (imageUrl.toLowerCase().indexOf('.pdf') != -1)	//是否是pdf
					
					callback && callback({
						imageUrl: imageUrl,
						isPdf: isPdf,
						fileName: file.name
					});
				}
			},
			Error : function(up, err) { 
				if (err.code == -600) {
					layer.msg("选择的文件太大了,应控制在5M以内！",{icon:2,offset:120}); 
				} else if (err.code == -601) {
					layer.msg("选择的文件类型仅支持jpg、jpeg、png、bmp！",{icon:2,offset:120}); 
				} else if (err.code == -602) {
					layer.msg("不允许上传重复文件！",{icon:2,offset:120}); 
				} else {
					layer.msg("Error xml:" + err.response,{icon:2,offset:120});
				}
			}
		}
	});
	
	return pluploader;

}

