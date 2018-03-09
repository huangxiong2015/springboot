$(function() {
	var valid = false;
	//var newsId = parseUrlParam(false).id;
	var vm = new Vue(
			{
				el : "#distributor-edit",
				data : {
					initData : {
						categoryTypeId : 'INFORMATION',
						attachUrl:'',
						newsContent:'',
						title:''
					},
					newsId : getQueryString('id'),
					newsHome: operationWebUrl + "/distributor.htm",
					checkModel : {},
				},
				methods : {
					init : function(res) {
						this.initData.title = res.title;
						this.initData.categoryTypeId = res.categoryTypeId;
						this.initData.attachUrl = res.newsAttachment && res.newsAttachment.attachUrl; 
						this.checkModel.isTop = ('Y' == res.isTop);
						this.checkModel.isTitleRed = ('RED' == res.isTitleRed); 
						setTimeout(function() {
							if (res.newsContent && res.newsContent.content) {
								var content = 'loadContent' + res.newsContent.content;
								window.frames[0].postMessage(content,ykyUrl.ictrade_ueditor);
							}
						}, 500);
					},
					saveNews : function(event) {
						this.initData.newsContent = event.data;
						$('#create').submit();
					}
				}
			});
	if (vm.newsId) {
		syncData(ykyUrl.product + '/v1/news/' + vm.newsId, 'GET', null, function(res, msg) {vm.init(res); });
	}
	formValidate('#create', {}, function(){
		var formArry = $('#create').serializeObject();
		formArry.content = vm.initData.newsContent;
		formArry.isTop = vm.checkModel.isTop ? 'Y' : 'N';
		formArry.isTitleRed = vm.checkModel.isTitleRed ? 'RED' : 'DEFAULT';
		if (!vm.newsId) {
			syncData(
					ykyUrl.product + "/v1/news",
					"POST",
					formArry,
					function(data, err) {
						if (null != data) {
							document.location.href = operationWebUrl + "/distributor.htm";
						}
					});
		} else {
			formArry.newsId = vm.newsId;
			syncData(
					ykyUrl.product + "/v1/news/" + vm.newsId,
					"PUT",
					formArry,
					function(data, err) {
						if (null != data) {
							document.location.href = operationWebUrl + "/distributor.htm";
						}
					});
		}
	});
	if (window.addEventListener) {
		window.addEventListener('message', vm.saveNews, false);
	} else if (window.attachEvent) {
		window.attachEvent('message', vm.saveNews, false);
	}	
	var uploader = createUploader({
		buttonId: "uploadBtn", 
		uploadType: "notice.publicRead", 
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",
		fileSize: "5mb",
		isImage: true, 
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					console.log(_yetAnotherFileUrl);
					console.log(file); 
					vm.initData.attachUrl = _yetAnotherFileUrl;
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
    }); 
	uploader.init();	
	
	$('.del_logo').on('click', function(){
		vm.initData.attachUrl ='';
	}); 
	
   $('.btn-concle').on('click', function(){
	   LS.set("bulletinEditCancel","Y");
	   window.location.href = operationWebUrl + "/bulletin.htm"; 
	});
});



function requireSaveData() {
	//发送指令到iframe请求返回数据
	window.frames[0].postMessage('save_btn', ykyUrl.ictrade_ueditor);
}