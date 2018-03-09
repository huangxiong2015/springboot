//by zengwei&yansha v1.0.0
var signatureData = {};
function createUploader(option) {  
	var currentGetSignatureTime = Date.parse(new Date()); // 上一次获取Signature的时间
	var generatePostObjectSignatureUrl = ykyUrl.database+"/v1/oss/upload?uploadType=";// 获取文件直传的路径，如果重写了获取方法需更改此变量  
	  var defaultOption = {
				runtimes : 'html5,flash,silverlight',
				browse_button : option.buttonId, 
				multi_selection : option.multiSelection?option.multiSelection:false,
				flash_swf_url : option.url + '/js/plupload-2.1.2/js/Moxie.swf',
				silverlight_xap_url : option.url + '/js/plupload-2.1.2/js/Moxie.xap',
				url : 'http://oss.aliyuncs.com',
				filters : {
					mime_types : [ // 只允许上传图片和zip文件
					{
						title : "files",
						extensions : option.types 
					} ],
					max_file_size : option.fileSize, // 最大只能上传2mb的文件
					prevent_duplicates : true
				// 不允许选取重复文件
				},
				init : {
					PostInit : function() {
						//$('#ossfile').html('');
					},
					FilesAdded : function(up, files) {
						index = layer.load(); 
						uploadFile(option.uploadType, up, files[0], false, option.isImage); // 单选 				
					},
					BeforeUpload : function(up, file) { 
						/*$('#ossfile').html('<div id="' + file.id + '"><b>' + file.name + ' (' + plupload.formatSize(file.size) + ')</b>'
						+'<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
						+'</div>');*/
					},
					UploadProgress : function(up, file) {
						 //updateProgressBar(file)
					},
					FileUploaded : function(up, file, info) {
						if (info.status == 200 || info.status == 203) {
							var name = file.name;
						} else {
							layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
						}
						up.files=[];
					},
					Error : function(up, err) {
						if (err.code == -600) {
							layer.msg("选择的文件太大了,应控制在"+ option.fileSize +"以内！",{icon:2,offset:120}); 
						} else if (err.code == -601) {
							layer.msg("选择的文件类型仅支持"+ option.types +"！",{icon:2,offset:120});
						} else if (err.code == -602) {

						} else {
//	 						alert("\nError xml:" + err.response);
						}
						up.files=[];
					}
				}
			}
	    option.init =$.extend({}, defaultOption.init, option.init);
	  	defaultOption = $.extend({}, defaultOption, option) ;
		var pluploader = new plupload.Uploader(defaultOption);

		return pluploader;

		//上传文件,isCallbackByAliyunOSS是否使用阿里云回调方式
		//根据文件上传类型获取文件直传的Signature 
		function getSignatures(uploadType, key, isImage, up, file, isCallbackByAliyunOSS) {
		 var flag = true; 
		 $.aAjax({
		     type: "GET",
		     url: generatePostObjectSignatureUrl + uploadType + "&key=" + key + "&isImage=" + isImage, 
		     dataType: "json",
		     async: true,  //false同步时firefx兼容问题
		     success: function(data) {
		         _fileUrl = data.url.replace(/http\:|https\:/,"");
		         _yetAnotherFileUrl = data&&data.image&&data.image.replace(/http\:|https\:/,"");//ykystatic.com域名的图片地址
		         signatureData = data;
		         uploadStart(up, file, isCallbackByAliyunOSS, signatureData);
		     },
		     error: function(data) {
		         flag = false;
		         //layer.close(index);
		         alert(data.status + " : " + data.statusText + " : " + data.responseText);
		     }
		 });
		 return flag;
		}

		function uploadFile(uploadType, up, file, isCallbackByAliyunOSS, isImage) {
		    var fileSize = file.size;
		    var fileName = encodeURIComponent(file.name);
		    var lastModf = file.lastModified;
		    var key = fileName;
		    var fix = fileName.substring(fileName.lastIndexOf("."));
		    getSignatures(uploadType, key, isImage, up, file, isCallbackByAliyunOSS); 
		}

		function uploadStart(up, file, isCallbackByAliyunOSS, signatureData){
			var postData = $.extend({}, signatureData);
		    var callbackData = {};
		    //判断是http还是https add by zw
		    var host = window.location.protocol =="http:" ? "host" : "posthost";
		    if (file != null && isCallbackByAliyunOSS) {
		        postData = generatePostObjectCallback(file);
		    }
		    postData["key"] = signatureData.key;
		    postData["OSSAccessKeyId"] = signatureData.OSSAccessKeyId;
		    postData["policy"] = signatureData.policy;
		    postData["Signature"] = signatureData.Signature;
		    postData["x-oss-security-token"] = signatureData.securityToken;
		    postData["success_action_status"] = "200";
		    up.setOption("url", postData[host]);
		    up.setOption("multipart_params", postData);
		    up.start();
		}
		function updateProgressBar(file){  
			var progBar = $('#' +file.id+ ' div div');
			progBar.html(file.percent + '%');
			progBar.css('width' ,  file.percent+'%');
			progBar.attr('aria-valuenow', file.percent);
		} 
	} 
  