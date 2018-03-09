/**
 * 
 */

var signatureData = {};
var currentGetSignatureTime = Date.parse(new Date()); // 上一次获取Signature的时间
var generatePostObjectSignatureUrl = ykyUrl.database+"/v1/oss/upload?uploadType=";// 获取文件直传的路径，如果重写了获取方法需更改此变量
//var generatePostObjectSignatureUrl = "http://localhost:9060/v1/oss/upload?uploadType=";
//ykyUrl.database
// 根据文件上传类型获取文件直传的Signature
function getSignatures(uploadType,key,isImage,par) {
	var flag = true;
	var now = Date.parse(new Date());
//	if (!signatureData["host"] || (now - currentGetSignatureTime) > (60 * 1000)) // 如果获取Signature时间大于1分钟，那么重新获取
//	{
		currentGetSignatureTime = now;
		$.aAjax({
			type : 'GET',
			url : generatePostObjectSignatureUrl + uploadType + "&key="+key +"&isImage="+isImage , //
			dataType : 'json',
			async : true,
			//data : JSON.stringify({}),
			contentType : "application/json",
			xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
			success : function(data) {
				signatureData = data;
				//signatureData["host"] = data["posthost"];
				upStart(par);
				imageUrl = data.imageUrl
			},
			error : function(data) {
				flag = false;
				//deleted by xh because of Loophole 
				//alert(data.status + " : " + data.statusText + " : " + data.responseText);
			}
		});
//	}
	return flag;
};
//创建上传(必须上传)
function createUploader(browseButtonId,fileSize,ida,idaa) {
	var pluploader = new plupload.Uploader({
		runtimes : 'html5,flash,silverlight',
		browse_button : browseButtonId,
		multi_selection : false,
		flash_swf_url : 'js/lib/plupload-2.1.2/js/Moxie.swf',
		silverlight_xap_url : 'js/lib/plupload-2.1.2/js/Moxie.xap',
		url : 'http://oss.aliyuncs.com',
		filters : {
			mime_types : [ // 只允许上传图片和zip文件
			{
				title : "Image files",
				extensions : "jpg,jpeg,bmp,png"
			} ],
			max_file_size : fileSize + 'mb', // 最大只能上传5mb的文件
			prevent_duplicates : false//允许选取重复文件
		},
		/*
		 * chunk_size : "10kb", resize : { width : 60, height : 60, crop : true,
		 * quality : 90, preserve_headers : false },
		 */
		init : {
			PostInit : function() {
			},
			FilesAdded : function(up, files) {
				uploadFile(up, files[0], false); // 单选
			},
			BeforeUpload : function(up, file) {
				$("#progressbar").css({
					display : ""
				});
				$("#progressbar").progressbar({
					value : 0
				});
			},
			UploadProgress : function(up, file) {
				$("#progressbar").progressbar({
					value : file["percent"]
				});
			},
			FileUploaded : function(up, file, info) {
				if (info.status == 200 || info.status == 203) {
					/*saveImgData();*/
					var params = pluploader.getOption("multipart_params");
					imageUrl = params["imageUrl"];// 数据库保存				
					if(!imageUrl || imageUrl.length <= 0||imageUrl.toLowerCase().indexOf('.pdf')>0){
						var url = params["url"];
						var accessUrl = params["accessUrl"];
					}else{
						image = params["image"];// 原图url
						
						var _img = $("#"+ida)[0]; //原图image
						_img.src = image;
						_img.setAttribute("data-url",imageUrl);
						
						var _img2 = $("#"+idaa)[0]; //原图image--大图
						_img2.src = image;
						
						thumbnail = params["thumbnail"]; // 缩url
						couponPost.postData.approvedProof = imageUrl;
						getPrivateUrl(imageUrl);
						$(".shotImage").css({"display":"inline-block"});
						$("#cApprovedProofError").hide();
					}
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
					//layer.msg("Error xml:" + err.response,{icon:2,offset:120});
				}
				$("#progressbar").css({
					display : "none"
				});
			}
		}
	});
	
	return pluploader;

}

// 上传文件,isCallbackByAliyunOSS是否使用阿里去回调方式
function uploadFile(up, file, isCallbackByAliyunOSS) {
	var fileSize = file.size;
	var fileName = file.name;
	var lastModf = file.lastModified;
	var key = fileName;
	getSignatures("coupon.voucher", key, true, {up:up, file:file, isCallbackByAliyunOSS:isCallbackByAliyunOSS});
	//getSignatures("approval.privateRead", key, true);
	
}

function uploadImage(up, file, isCallbackByAliyunOSS) {
	 var fileSize = file.size;
   var fileName = file.name;
   var lastModf = file.lastModified;            
   var key = fileName;
	getSignatures("ent.certificates",key,true,{up:up, file:file, isCallbackByAliyunOSS:isCallbackByAliyunOSS});
}

function upStart(par){
	var postData = $.extend({}, signatureData);
	var callbackData = {};
	var up = par.up;
	var isCallbackByAliyunOSS = par.isCallbackByAliyunOSS;
	var file = par.file;
	if (file != null && isCallbackByAliyunOSS) { // 如果file不为空，并且使用阿里云OSS回调方式，那么生成OSS回调
		postData = generatePostObjectCallback(file);
	}
	postData["key"] = signatureData.key;
	postData["OSSAccessKeyId"] = signatureData.OSSAccessKeyId;
	postData["policy"] = signatureData.policy;
	postData["Signature"] = signatureData.Signature;
	postData["x-oss-security-token"] = signatureData.securityToken;
	postData["success_action_status"] = "200";
	up.setOption("url", postData.posthost);
	up.setOption("multipart_params", postData);
	up.start();
}

/**
 * 删除上传的文件（服务器上的文件不处理）
 * @param showId 缩略图容器ID
 * @param id 隐藏控件ID
 * @param showOriId 原图容器ID
 */
function deleteCurPic(showId,id,showOriId){
	$("#"+showId).html("");
	$("#"+id).val("");
	$("#" + showOriId).html("");
}

//private需要调用getImgUrl接口
var getPrivateUrl =  function(src){
	$.aAjax({
		url: ykyUrl.party + "/v1/enterprises/getImgUrl",
     	type:"POST",
     	data:JSON.stringify({"id":src}),
     	success: function(data) {
     		if(null !=data || data == ""){
     			$("#minIMG").attr("src",data);
     			$("#maxIMG").attr("src",data);
     		}
     	}
 	});
}