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
function createUploader(browseButtonId) {
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
			max_file_size : '5mb', // 最大只能上传5mb的文件
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
				/*$("#progressbar").css({
					display : "none"
				});
				$("#progressbar").progressbar({
					value : 0
				});*/
				if (info.status == 200 || info.status == 203) {
					var params = pluploader.getOption("multipart_params");
					var imageUrl = params["imageUrl"];// 数据库保存
					url0 = imageUrl;
					
					if(!imageUrl || imageUrl.length <= 0||imageUrl.toLowerCase().indexOf('.pdf')>0){
						var url = params["url"];
						var accessUrl = params["accessUrl"];
					}else{

						var image = params["image"];// 原图url
						var thumbnail = params["thumbnail"]; // 缩url
						console.log(image);
						
						var _img = $("#voucherImg")[0];   //数据库保存
						_img.src = image;  
						var _img1 = $("#voucherImg1")[0]; //缩略图
						_img1.className = "";
						_img1.src = image;
						url1 = thumbnail;
						var _img2 = $("#voucherImg2")[0]; //原图
						_img2.src = image;
						url2 = image;
						
						var credFileContent = $("#credFileContent")[0];
						if(credFileContent.style.display == "block"){
							credFileContent.style.display = "none";
						}
					}
					
				} else {
					
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
	getSignatures("payment.voucher", key, true, {up:up, file:file, isCallbackByAliyunOSS:isCallbackByAliyunOSS});
	//getSignatures("approval.privateRead", key, true);
	
}

function uploadImage(up, file, isCallbackByAliyunOSS) {
	 var fileSize = file.size;
   var fileName = file.name;
   var lastModf = file.lastModified;            
   var key = fileName;
	getSignatures("payment.voucher",key,true,{up:up, file:file, isCallbackByAliyunOSS:isCallbackByAliyunOSS});
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
	
	var host = '';
    if(location.protocol.indexOf("https:") != -1){
    	host = postData.posthost;
    }else{
    	host = postData.host;
    }
    up.setOption("url", host);
	up.setOption("multipart_params", postData);
	up.start();
}


// 弹出原图
function showOriPic(showOriIdStr) {
	var jqOriImgObj = $("#"+showOriIdStr+" img");
	if(jqOriImgObj.length>0){
		reSizeImg(jqOriImgObj.get(0));
	}
	$("#"+showOriIdStr).attr("class","disnone");
	
	layer.open({
		type : 1,
		title : false,
		closeBtn : 0,
		area : 'auto', // 自定义宽度
		skin : 'layer_cla', // 没有背景色
		offset:'150px',
		shadeClose : true,
		maxWidth:1000,
		content : $('#' + showOriIdStr),
		end:function(){
			$("#"+showOriIdStr).attr("class","showHidden");
			$("#"+showOriIdStr).removeAttr("style");
			jqOriImgObj.removeAttr("width");
			jqOriImgObj.removeAttr("height");
		}
	});
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

