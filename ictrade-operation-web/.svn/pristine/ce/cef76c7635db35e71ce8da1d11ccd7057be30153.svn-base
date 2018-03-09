
//创建上传
  function createUploader(browseButtonId,uploadType,isImage) {
		var pluploader = new plupload.Uploader({
			runtimes : 'html5,flash,silverlight',
			browse_button : browseButtonId,
			multi_selection : false,
			flash_swf_url : 'js/plupload-2.1.2/js/Moxie.swf',
			silverlight_xap_url : 'js/plupload-2.1.2/js/Moxie.xap',
			url : 'http://oss.aliyuncs.com',
			filters : {
				mime_types : [ // 只允许上传图片和zip文件
				{
					title : "files",
					extensions : "csv,xlsx,xls"
				} ],
				max_file_size : '10mb', // 最大只能上传2mb的文件
				prevent_duplicates : true
			// 不允许选取重复文件
			},
// 			chunk_size : "10kb",
// 			resize : {
// 				width : 60,
// 				height : 60,
// 				crop : true,
// 				quality : 90,
// 				preserve_headers : false
// 			},
			init : {
				PostInit : function() {
				},
				FilesAdded : function(up, files) {
					/*var vendorId = $('input:radio:checked').val();
					if(vendorId===undefined){
						layer.msg("请先选择供应商！", {
							icon : 2,
							offset : 120
						});
						return;
					}*/
					index = layer.load();
					uploadFile(up, files[0], false,uploadType,isImage); // 单选
				},
				BeforeUpload : function(up, file) {
				},
				UploadProgress : function(up, file) {
				},
				FileUploaded : function(up, file, info) {
					if (info.status == 200 || info.status == 203) {
						var name = file.name;
						excelFile(_fileUrl,name);
					} else {
						layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
					}
					up.files=[];
				},
				Error : function(up, err) {
					if (err.code == -600) {
						layer.msg("选择的文件太大了,应控制在2M以内！",{icon:2,offset:120}); 
					} else if (err.code == -601) {
						layer.msg("选择的文件类型仅支持csv,xlsx,xls！",{icon:2,offset:120});
					} else if (err.code == -602) {

					} else {
// 						alert("\nError xml:" + err.response);
					}
					up.files=[];
				}
			}
		});

		return pluploader;

	} 
	
//创建上传
  function createUploader2(browseButtonId,uploadType,isImage) {
		var pluploader = new plupload.Uploader({
			runtimes : 'html5,flash,silverlight',
			browse_button : browseButtonId,
			multi_selection : false,
			flash_swf_url : '../js/plupload-2.1.2/js/Moxie.swf',
			silverlight_xap_url : '../js/plupload-2.1.2/js/Moxie.xap',
			url : 'http://oss.aliyuncs.com',
			filters : {
				mime_types : [ // 只允许上传图片和zip文件
				{
					title : "files",
					extensions : "csv,xlsx,xls"
				} ],
				max_file_size : '10mb', // 最大只能上传2mb的文件
				prevent_duplicates : true// 不允许选取重复文件
			},
			init : {
				PostInit : function() {
				},
				FilesAdded : function(up, files) {
					index = layer.load();
					layer.close(index);
					uploadFile(up, files[0], false,uploadType,isImage); // 单选
				},
				BeforeUpload : function(up, file) {
					beforeUpload(file.name)					
				},
				UploadProgress : function(up, file) {
					updateProgressBar(file["percent"])
				},
				FileUploaded : function(up, file, info) {
					if (info.status == 200 || info.status == 203) {
						fileUploaded();
						fileName=file.name;
					} else {
						layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
					}
					up.files=[];
				},
				Error : function(up, err) {
					if (err.code == -600) {
						layer.msg("选择的文件太大了,应控制在10M以内！",{icon:2,offset:120}); 
					} else if (err.code == -601) {
						layer.msg("选择的文件类型仅支持csv,xlsx,xls！",{icon:2,offset:120});
					} else if (err.code == -602) {

					} else {
// 						alert("\nError xml:" + err.response);
					}
					up.files=[];
				}
			}
		});

		return pluploader;

	}
  //上传图片
  function createUploader3(browseButtonId,uploadType,isImage) {
		var pluploader = new plupload.Uploader({
			runtimes : 'html5,flash,silverlight',
			browse_button : browseButtonId,
			multi_selection : false,
			flash_swf_url : '../js/plupload-2.1.2/js/Moxie.swf',
			silverlight_xap_url : '../js/plupload-2.1.2/js/Moxie.xap',
			url : 'http://oss.aliyuncs.com',
			filters : {
				mime_types : [ // 只允许上传图片和zip文件
		   			{
		   				title : "Image files",
		   				extensions : "jpg,gif,png,bmp,jpeg,pdf"
		   			} ],
		   			max_file_size : '10mb', // 最大只能上传10mb的文件
		   			prevent_duplicates : true
		   		},
			init : {
				PostInit : function() {
				},
				FilesAdded : function(up, files) {
					index = layer.load();
					layer.close(index);
					uploadFile(up, files[0], false,uploadType,isImage); // 单选
				},
				BeforeUpload : function(up, file) {
					
				},
				UploadProgress : function(up, file) {
					
				},
				FileUploaded : function(up, file, info) {
					if (info.status == 200 || info.status == 203) {
						var params = pluploader.getOption("multipart_params"),
							imageUrl=params["imageUrl"] ,//原图url
							thumbImageUrl= params["thumbnailUrl"] ;//缩略图 
							imageName=file.name;
						afterUploaded(imageName,imageUrl,thumbImageUrl)
					} else {
						layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
					}
					up.files=[];
				},
				Error : function(up, err) {
					if (err.code == -600) {
						layer.msg("选择的文件太大了,应控制在10M以内！",{icon:2,offset:120}); 
					} else if (err.code == -601) {
						layer.msg("选择的文件类型仅支持jpg,gif,png,bmp,jpeg,pdf！",{icon:2,offset:120});
					} else if (err.code == -602) {

					} else {
//						alert("\nError xml:" + err.response);
					}
					up.files=[];
				}
			}
		});

		return pluploader;

	}
	//上传文件,isCallbackByAliyunOSS是否使用阿里去回调方式
	function uploadFile(up, file, isCallbackByAliyunOSS,uploadType,isImage) {
		var fileSize = file.size;
		var fileName = encodeURIComponent(file.name);
		var lastModf = file.lastModified;            
		var key = fileName;
		var fix = fileName.substring(fileName.lastIndexOf('.'));
		
		getSignatures(uploadType,key,isImage);
		
		var postData = $.extend({}, signatureData);
		var callbackData = {};
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
	  
	function excelFile(_fileUrl,fileName){
//		var vendorId = $('input:radio:checked').val();
		$("#fileUrl").val(_fileUrl);
		$("#exportForm").submit();
		layer.close(index);
		return;
		$.aAjax({
			type : "GET",  //http://localhost:9040/v1/material/materialDetection?fileUrl=54464
			url : "http://localhost:9040/v1/materail/materialDetection?fileUrl="+_fileUrl, //dataPort + 
			data : {},
			contentType : "application/json",
			async : false,
			success : function(msg) {
				layer.msg("上传成功", {
					icon : 1,
					offset : 120
				});
				layer.close(index);
			},
			error : function() {
				layer.msg("上传文件失败,请重新上传！", {
					icon : 2,
					offset : 120
				});
				layer.close(index);
			}
		});
	}