$(function() {
	var valid = false;
	var newsId = parseUrlParam(false).id;
	var vm = new Vue(
			{
				el : "#recuit-edit",
				data : {
					initData : {
						categoryTypeId : 'RECRUITMENT',
						categoryId:"20051",
						attachUrl:'',
						regionCategoryId:'5001',
						extend:'',
						newsContent:'',
						title:''
					},
					checkModel : {},
					selectUrl :ykyUrl.database + '/v1/category/companylist?categoryTypeId=OCCUPATION_TYPE',    //数据接口，必须

					typeId : 'categoryId',        //设置控件ID ，必须
					typeName : 'categoryId',        //设置控件name ，必须
		            typeOptionName:"categoryName",
		            inputClass : '',    //设置下拉选项的class，可选
		            styleObject: '',    //设置style，可选
		            required :'required',
		            
		            selectRegionUrl :ykyUrl.database + '/v1/category/companylist?categoryTypeId=RECRUIT_ADDRESS',    //数据接口，必须
		            cityId : 'regionCategoryId',        //设置控件ID ，必须
		            cityName : 'regionCategoryId',        //设置控件name ，必须
		            placeId : 'categoryId',        
		            placeName : 'categoryId',        
		            placeOptionName:"categoryName",
		            inputClass : '',    //设置下拉选项的class，可选
		            styleObject: '',    //设置style，可选
		            required :'required',
				},
				methods : {
					init : function(res) {
						this.initData.title = res.title;
						this.initData.extend = res.extend;
						this.initData.regionCategoryId = res.regionCategoryId;
						this.initData.categoryTypeId = res.categoryTypeId;
						this.initData.categoryId = res.categoryId;
						this.initData.attachUrl = res.newsAttachment && res.newsAttachment.attachUrl; 
						this.checkModel.isTop = ('Y' == res.isTop);
						//this.checkModel.isTitleRed = ('RED' == res.isTitleRed); 
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
	
	if (newsId) {
		syncData(ykyUrl.info + '/v1/news/' + newsId, 'GET', null, function(res, msg) {vm.init(res); });
	}
	formValidate('#create', {}, function(){
		var formArry = $('#create').serializeObject();
		formArry.content = vm.initData.newsContent;
		formArry.isTop = vm.checkModel.isTop ? 'Y' : 'N';
		//formArry.isTitleRed = vm.checkModel.isTitleRed ? 'RED' : 'DEFAULT';
		if (!newsId) {
			syncData(
					ykyUrl.info + "/v1/news",
					//"http://localhost:27083/v1/news",
					"POST",
					formArry,
					function(data, err) {
						if (null != data) {
							document.location.href = operationWebUrl + "/recruit.htm";
						}
					});
		} else {
			formArry.newsId = newsId;
			syncData(
					ykyUrl.info + "/v1/news/" + newsId,
					//"http://localhost:27083/v1/news"+ newsId,
					"PUT",
					formArry,
					function(data, err) {
						if (null != data) {
							history.go(-1);
							//document.location.href = operationWebUrl + "/recruit.htm";
						}
					});
		}
	});
	if (window.addEventListener) {
		window.addEventListener('message', vm.saveNews, false);
	} else if (window.attachEvent) {
		window.attachEvent('message', vm.saveNews, false);
	}	
	
});



function requireSaveData() {
	//发送指令到iframe请求返回数据
	window.frames[0].postMessage('save_btn', ykyUrl.ictrade_ueditor);
}
function goBack(){
	history.go(-1);
	//window.history.back();
	//location.href = ykyUrl._this + "/recruit.htm"
}