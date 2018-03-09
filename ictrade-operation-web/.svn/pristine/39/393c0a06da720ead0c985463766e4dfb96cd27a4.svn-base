
var vm = new Vue({
			el : "#help-edit",
			data : {
				contentId: getQueryString("contentId"),
				categoryId: getQueryString("categoryId"),
				pathName1: getQueryString("pathName1"),
				pathName2: getQueryString("pathName2"),
				pathName3: getQueryString("pathName3"),
				newsContent:'',
				newsHome:operationWebUrl + "/helpMaintain.htm"
			},
			methods : {
				init : function(res) { 
					setTimeout(function() {
						if (res.newsContent && res.newsContent.content) {
							var content = 'loadContent' + res.newsContent.content;
							window.frames[0].postMessage(content,ykyUrl.ictrade_ueditor);
						}
					}, 500);
				},
				saveData : function(event) {
					var _this = this;
					_this.newsContent = event.data;
					var url = _this.contentId ? ykyUrl.info + '/v1/news/' + _this.contentId : ykyUrl.info + '/v1/news/';
						type = _this.contentId ? 'PUT' : 'POST',
						data = {
							categoryTypeId: "HELP",
							categoryId: _this.categoryId,
							content: _this.newsContent,
							title:""
						};
					syncData(url, type, data, function(data){
						layer.msg('保存成功！');
						localStorage.setItem('activeMenuId',_this.categoryId);
						setTimeout(function(){
							location.href = ykyUrl._this + '/helpMaintain.htm';
						},1000);
					})
				}
			}
		});
if (vm.contentId) {
	syncData(ykyUrl.info + '/v1/news/' + vm.contentId,'GET',null,function(res, msg) {
			vm.init(res);
	});
}	
setTimeout(function() {
	if (window.addEventListener) {
		window.addEventListener('message', vm.saveData, false);
	} else if (window.attachEvent) {
		window.attachEvent('message', vm.saveData, false);
	}
},1000);

	
$('.btn-concle').on('click', function(){
   window.location.href = operationWebUrl + "/helpMaintain.htm"; 
});



function requireSaveData() {
	//发送指令到iframe请求返回数据
	window.frames[0].postMessage('save_btn', ykyUrl.ictrade_ueditor);
}