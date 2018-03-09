//如果是编辑，则存在该条记录的ID 
//var recommendationId = $("#recommendationId").val();
var recommendationId = getQueryString("recommendationId");
var content = {			
		dictionaryItemId:'',/* 区域id */
		imageUrl:'',/* 图片地址 */
		bImageUrl:'',/* 大图地址 */
		linkDetails:'',/* 商品详情地址 */
		modelName:'',/* 型号 */
}
var productData = {
		categoryId:'20060',
		content:'',
		orderSeq:''
}
var availableTags=[];

if(recommendationId){
	$.aAjax({
		url:ykyUrl.info + '/v1/recommendations/' + recommendationId,
		type:'GET',
		success:function(data){					
			$("#orderSeq").val(data.orderSeq);//位置
			$("#modelName").val(data.contentMap.modelName);//型号
			$("#detailUrl").val(data.contentMap.linkDetails);//详情链接					
			$("#imageAddress").val(data.contentMap.imageUrl);//图片地址
			$("#b_img").val(data.contentMap.bImageUrl);//大图地址
			$("#showImage img").attr("src",data.contentMap.imageUrl);
			$("#showBigImage img").attr("src",data.contentMap.bImageUrl);
			content = data.contentMap;
		},
		error:function(){
			console.log("error")
		}
	})
}

	$(function(){

	    //初始化上传图片
 	    uploader = createUploader('selectImage','showImage','imageAddress','b_img');
 		uploader.init();
//		uploader = createUploaderOSS('selectImage','showImage','32_32','imageAddress',"b_img","oss.htm?action=delRecomendFile&fileUrl=","notice.publicRead");
//		uploader.init();
		 		
		//联想图片地址
	   $("#imageAddress").autocomplete({
	       source: availableTags
	  });
	   
	   var cache = {}; 
	   $("#modelName").autocomplete({
	       source: function(request, response) {
				var term = request.term;
				if (term in cache) {
					response($.map(cache[term], function(item) {
						return {
							label : item.partNo,
							value : item.partNo
						}
					}));
					return;
				}
				$.ajax({
					url : ykyUrl._this + "/rest/item/findType",
					type : "POST",
					data : {
						'type' : request.term //获取输入框内容
					},
					success : function(data) {
						cache[term] = data;
						response($.map(data, function(item) {//此处是将返回数据转换为 JSON对象，并给每个下拉项补充对应参数
							return {
								// 设置item信息
								label : item.partNo, //下拉显示内容
								value : item.partNo, //下拉项对应数值
							}
						}));
					},
					error:function(e){
						//alert(e);
					}
				});
			},
			
			minLength : 1, // 输入框字符个等于1时开始查询
			select : function(event, ui) {	//选中某项时执行的操作
				autoImageUrl(ui.item.label);//选中后去联想出型号对应的图片URL
				
			}
	  });
	}); 
	
	//新增
	function save(){
		//位置
		//var postion = $("#orderSeq").val();
		//型号
		var type = $("#modelName").val();
		//链接
		var linkDetails = $("#detailUrl").val();
		
		if(!type || !linkDetails){
			layer.alert('型号或链接为空', {
				  icon: 2,
				  offset : ['50%' , '45%']
				})
			return ;
		}
		
		//上传图片的url
		var imageAddress = $("#imageAddress").val();
		if(!imageAddress){
			layer.alert('请上传图片', {
				  icon: 2,
				  offset : ['50%' , '45%']
				})
			return ;
		}
		//大图的url
		var bImg = $("#b_img").val();
		content.linkDetails = linkDetails;
		content.modelName = type;
		content.imageUrl = imageAddress;
		content.bImageUrl = bImg;	
		
		requestSaveService();
		
		/* var sum = [];				
		//往数组里面添加值
		sum.push(postion);
		sum.push(type);
		sum.push(linkDetails);
		sum.push(imageAddress);
		sum.push(bImg); */
		
		/* $.ajax({
			type: "POST",
			  url: "/v1/recommendations",
			  data: {'sum':sum.join(",,"),'id':id},
			  async: false,
			  success: function(msg){
				 if(msg=="false"){
					 layer.alert('操作失败',{
							icon: 6,
							time: 1500,
							offset : ['50%' , '45%'],
							end: function(){
								window.history.back();
							}
						});
				 }
				 window.history.back();
			  }
		}); */
	};
	
	function requestSaveService(){
		/* productData.categoryId = $("#dictionaryItemId").val(); */
		productData.orderSeq = $("#orderSeq").val();
		productData.content = JSON.stringify(content);
		$.aAjax({
			url:recommendationId ? ykyUrl.info + '/v1/recommendations/' + recommendationId : ykyUrl.info + '/v1/recommendations',
			type:recommendationId ? 'PUT':'POST',		
			data:JSON.stringify(productData),
			success:function(data){
				//debugger;
				layer.msg("保存成功！",{
					skin:"c_tip"
				});
				//$("#save_btn").unbind("click");
				//$("#cancel_btn").unbind("click");
				setTimeout(function(){
					if(recommendationId){
						history.go(-1);
					}else{
						location.href = ykyUrl._this + "/featuredProduct.htm";
					}
				},2000)				
			},
			error:function(data){
				console.log("error");
			}
		})
	};
	
	
	//型号联想出物料图片，后台人员根据图片上传广告图片
	/* function autoImage_preview(){
		layer.open({
		  	  type: 1,
		  	  title: false,
		  	  closeBtn: 0,
		  	  area: ['auto','auto'],  //自定义宽度
		  	  skin: 'layer_cla', //没有背景色
		  	  shadeClose: true,
		  	  content: $('#autoBigimg')
		  	});
	} */
	
	
	
	
	//打开缩略图
	function image_preview(){
	 	var url = $("#b_img").val();
 		$("#showBigImage").html('<img class="wh50_img" style="max-width:800px;" src="'+url+'">');
	 	if(url){
	 		setTimeout(function(){
	 			layer.open({
				  	  type: 1,
				  	  title: false,
				  	  closeBtn: 0,
				  	  area: ['auto','auto'],  //自定义宽度
				  	  skin: 'layer_cla', //没有背景色
				  	  shadeClose: true,
				  	  content: $('#showBigImage')
				  	});
	 		}, 400);
	 	}else{	 	

	 			//var recommendationId = $("#recommendationId").val();
	 			$.ajax({
	 				type: "POST",
	 				  url: ykyUrl._this + "/rest/item/findBigImage",
	 				  data:{'recommendationId':recommendationId},
	 				  async: false,
	 				  success: function(msg){
	 					  if(msg){
	 						 $("#imageAddress").val(msg[0]);
		 					 $("#b_img").val(msg[1]);
		 					//大图显示
							$("#showBigImage").html('<img class="wh50_img" style="max-width:800px;" src="'+msg[1]+'">');
		 					 
		 					layer.open({
		 					  	  type: 1,
		 					  	  title: false,
		 					  	  closeBtn: 0,
		 					  	  area: ['auto','auto'],  //自定义宽度
		 					  	  skin: 'layer_cla', //没有背景色
		 					  	  shadeClose: true,
		 					  	  content: $('#showBigImage')
		 					  	});

	 					  }
	 					
	 				  }	 
	 			});
	 	}
	}
	
	  //根据型号联想图片URL
	 function autoImageUrl(param){
		$.ajax({
			type: "POST",
			  url:  ykyUrl._this + "/rest/item/findByAuto",
			  data: {'param':param},
			  async: false,
			  success: function(msg){
				  availableTags.splice(0,availableTags.length);
				  var prefix = $("#imagePrefix").val();
				  if(msg[0]){
					  var s_img=prefix+msg[0];
					  var b_img=prefix+msg[1];
					  //显示缩略图
					  $("#showAutoImage").html('<img alt="" class="wh50_img" style="max-width:800px;" src="'+s_img+'" onclick="image_preview()">');
					  //存放缩略图地址
					  $("#autoImageAddress").val(s_img);
					  //存放大图地址
					  $("#autoBigimgAddress").val(b_img);
					  //大图显示
					  $("#autoBigimg").html('<img class="wh50_img" src="'+b_img+'">');
					  
				  }
				  				  
			  }
		});
	} 
	
	//创建上传
	   function createUploader(browseButtonId,showid,id,bid) {

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
					title : "Image files",
					extensions : "jpeg,jpg,gif,png,bmp"
				} ],
				max_file_size : '2mb', // 最大只能上传10mb的文件
				prevent_duplicates : true
			// 不允许选取重复文件
			},
			//chunk_size : "10kb",
			/* resize : {
				//width : 60,
				height : 60,
				crop : true,
				quality : 90,
				preserve_headers : false
			}, */
			init : {
				PostInit : function() {
				},
				FilesAdded : function(up, files) {
					uploadFile(up, files[0], false); // 单选
				},
				BeforeUpload : function(up, file) {
					/* $("#progressbar").css({
						display : ""
					});
					$("#progressbar").progressbar({
						value : 0
					}); */
				},
				UploadProgress : function(up, file) {
					/* $("#progressbar").progressbar({
						value : file["percent"]
					}); */
				},
				FileUploaded : function(up, file, info) {
					/* $("#progressbar").css({
						display : "none"
					});
					$("#progressbar").progressbar({
						value : 0
					}); */
					if (info.status == 200 || info.status == 203) {
						var params = pluploader.getOption("multipart_params");
						var imageUrl = params["imageUrl"] ;//数据库保存 
						var image = params["image"] ;// 原图url
						var thumbnail = params["thumbnail"] ; //缩url
						var thumbnailUrl = params["thumbnailUrl"] ; //缩url
						
						//缩略图显示
						$("#"+showid).html('<img alt="" class="wh50_img" style="margin-left:17%;margin-top:10px;margin-bottom:10px;" src="'+thumbnail+'" onclick="image_preview()">');
						//原图显示
						$("#showBigImage").html('<img alt="" style="max-width:800px;" class="wh50_img" src="'+image+'">');
						//缩略图地址
						$("#"+id).val(imageUrl);
						//$("#"+id).val(thumbnail);
						//原图地址
						$("#"+bid).val(image);
// 						$("#portraitUrl").html(imageUrl);

					} else {
						$("#headPortraitPath").html(info.response);
					}
				},
				Error : function(up, err) {
					if (err.code == -600) {
						alert("\n选择的文件太大了,可以根据应用情况，在upload.js 设置一下上传的最大大小");
					} else if (err.code == -601) {
						alert("\n选择的文件后缀不对,可以根据应用情况，在upload.js进行设置可允许的上传文件类型");
					} else if (err.code == -602) {

					} else {
						alert("\nError xml:" + err.response);
					}
					/* $("#progressbar").css({
						display : "none"
					}); */
				}
			}
		});

		return pluploader;

	} 
	//上传文件,isCallbackByAliyunOSS是否使用阿里去回调方式
	  function uploadFile(up, file, isCallbackByAliyunOSS) {
		  var fileSize = file.size;
	      var fileName = file.name;
	      var lastModf = file.lastModified;            
	      var key = fileName;
	      
	    getSignatures("recommend.publicRead",key,true);
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
						
	

	function goBack(){
		//window.history.back();
		//location.href = ykyUrl._this + "/featuredProduct.htm"
		history.go(-1);
	}