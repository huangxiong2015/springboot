/**
 * Created classifiedAdsEdit.js by zr.xieyuanpeng@yikuyi.com on 2017年10月26日.
 */
var vm;
var addData;
var uploader = {};
var recommendationId = getQueryString("recommendationId");
var defaultImgUrl = ''; //ykyUrl._this+'/images/static/defaultImg.120.jpg'
var numToText ={
	1:'一',
	2:'二',
	3:'三',
	4:'四',
	5:'五',
	6:'六',
	7:'七',
	8:'八',
	9:'九'
}
var validate= false;
vm = new Vue({
    el: '#advertise_edit',
    data: {
    	initData: {
    		category: [{categoryId: '6001', categoryName: '图片广告'}],
    		page: [{pageId: '7004', pageName: '类目导航'}],	//广告页面
    		position: []
    	},
    	pageTitle: '新增',
    	title: '',	//广告标题
    	categoryId: '6001', //广告类型
    	pageId: '7004',	//广告页面
    	positionId: '1', //广告位置
    	startDate: '',  //开始时间
    	expiryDate: '', //结束时间
		proportion: '', //曝光占比
		image: '',	//广告效果图
		bigImagesAdList:[{image:defaultImgUrl,url:'',type:'big'},{image:defaultImgUrl,url:'',type:'big'}],
		smallImagesAdList:[{image:defaultImgUrl,url:'',type:'small'},{image:defaultImgUrl,url:'',type:'small'},{image:defaultImgUrl,url:'',type:'small'},{image:defaultImgUrl,url:'',type:'small'},{image:defaultImgUrl,url:'',type:'small'},{image:defaultImgUrl,url:'',type:'small'},{image:defaultImgUrl,url:'',type:'small'},{image:defaultImgUrl,url:'',type:'small'},{image:defaultImgUrl,url:'',type:'small'},],
		numToChinsese:numToText,
		smallImgNum:9,
		linkUrl: '',	//广告链接
		categoryName: '图片广告',	//广告类型Value
		pageName: '类目导航',	//广告页面Value
		positionName: '无源器件', //广告位置Value
		saveBtnAble:true,
	},
	mounted: function() {
		datetimeInit();
		
		this.startDate = moment().format('YYYY-MM-DD H:00:00');
		this.expiryDate = moment().format('YYYY-MM-DD H:00:00');
	},
	beforeCreate:function(){
		
	},
	created:function(){
		var $this = this;
		getFirstCategories(function(){
			if(recommendationId) {
				$this.pageTitle = '编辑';
				queryInfo();
				$('.breadcrumb .active').text('广告位编辑');
				$('title').text('广告位-编辑');
			}else{
			}
		});
		
	},
	watch: {
		pageId: function(val, oldVal) {
			var $this = this;
			$.each(this.initData.page, function(i, item) {
				if(item.pageId == val) {
					$this.pageName = item.pageName;
				}
			}) 
		},
		positionId:function(val,oldVal){
			var $this = this;
			$.each(this.initData.position, function(i, item) {
				if(item.positionId == val) {
					$this.positionName = item.positionName;
				}
			})
		}
	},
	methods : {
		//删除上传的图片
		deleteImg:function(message){
			if(message){
				if('small'==message.type&&message.index!=undefined){
					this.smallImagesAdList[message.index].image ='';
				}else if('big'==message.type&&message.index!=undefined){
					this.bigImagesAdList[message.index].image ='';
				}
			}
		},
		//广告链接格式化
		linkUrlFormat: function() {
			if(!this.linkUrl) return;
			
			if(this.linkUrl.indexOf('http://') == 0 || this.linkUrl.indexOf('https://') == 0) {
				this.linkUrl = this.linkUrl.replace(/^(http:)|(https:)/, '');
			}else if(this.linkUrl.indexOf('//') != 0) {
				this.linkUrl = '//' + this.linkUrl;
			}
		},
		//取消
		cancel: function() {
			history.go(-1);
		},
		//保存
		save: function() {
			var _this = this;
			if(!_this.saveBtnAble){
				return false;
			}else{
				_this.saveBtnAble = false;
				save();
			}
		}
	}
});

//时间控件初始化
function datetimeInit() {
	$('#startDate').datetimepicker({
	    language:  config.language, 
	    minView: 'day',   //设置只显示到小时
	    format: "yyyy-mm-dd hh:00:00",
	    autoclose: true
	}).on('change',function(ev){
	    var startDate = $('#startDate').val();
	    if(new Date(startDate)>new Date(vm.expiryDate)){
	    	vm.expiryDate = startDate;
	    	$("#expiryDate").val(startDate);
	    }
	    vm.startDate = startDate;
	});
	
	$('#expiryDate').datetimepicker({
		 language:  config.language, 
		 minView: 'day',   //设置只显示到小时
	     format: "yyyy-mm-dd hh:00:00",
	     autoclose: true
	}).on('change',function(ev){
	    var expiryDate = $('#expiryDate').val();
	    vm.expiryDate = expiryDate;
	});
}
//请求获取初始数据 ，赋值
function queryInfo() {
	$.aAjax({
		url:ykyUrl.info + '/v1/recommendations/' + recommendationId,
		type:'GET',
		success:function(data){	
			vm.startDate = data.startDate || moment().format('YYYY-MM-DD H:00:00');
			vm.expiryDate = data.expiryDate || moment().format('YYYY-MM-DD H:00:00');
			vm.categoryId = data.categoryId || '',
			vm.pageId = data.extend1,
			vm.positionId = data.extend2;
			if(data.contentMap&&data.contentMap.bigImagesAdList){
				vm.bigImagesAdList = data.contentMap.bigImagesAdList;
			}
			if(data.contentMap&&data.contentMap.smallImagesAdList){
				vm.smallImagesAdList = data.contentMap.smallImagesAdList;
			}
		},
		error:function(){
			console.log("error")
		}
	})
}

function getFirstCategories(fn){
	syncData(ykyUrl.product + '/v1/products/categories/children', 'GET', null , function (res , err) {//页面加载前调用方法 		
         if(res&&res.length>0){
        	 var categoriesList = []
        	 $.each(res,function(i,item){
        		 var obj={
    				 positionId: item._id,
    				 positionName: item.cateName 
        		 }
        		 categoriesList.push(obj);
        	 })
        	 vm.initData.position = categoriesList;
         }
         if(typeof fn==="function"){
        	 fn();
         }
	}); 
}
//保存数据
function save() {

	var contentData = {
			pageName: vm.pageName,   //广告页面
			positionName: vm.positionName,
			bigImagesAdList:vm.bigImagesAdList,
			smallImagesAdList:vm.smallImagesAdList
			
	   }
	var listData = {
		    "categoryTypeId": "classifiedAd",    //固定要传类型为广告
			categoryId: vm.categoryId,  //广告类型
			"content": JSON.stringify(contentData),
			extend1: vm.pageId,    			//广告页面
			extend2: vm.positionId,			//广告位置
			"expiryDate": vm.expiryDate,    	 //结束时间
			"startDate": vm.startDate,   		//开始时间
	   }
	$.aAjax({
		url: ykyUrl.info + '/v1/recommendations/' + (recommendationId ? recommendationId : ''),
		type:recommendationId ? 'PUT':'POST',		
		data:JSON.stringify(listData),
		success:function(data){
			layer.msg("保存成功！",{
				skin:"c_tip"
			});
			setTimeout(function(){
			   location.href = ykyUrl._this + "/advertisement/classifiedAdsList.htm";  //保存成功跳转列表页
			  /* vm.saveBtnAble = true;*/
			},2000)
		},
		error:function(data){
			vm.saveBtnAble = true;
			if(data.responseJSON.errCode == "ADVERTISEMENTTITLE_EXIST"){
				layer.msg("广告名称已存在！",{
					skin:"c_tip"
				});
			}
		}
	})
}


function initUploader(buttonId,imageType,arrayIndex){
	uploader[buttonId] = createUploader({
		buttonId: buttonId, 
		uploadType: "notice.publicRead",   //文件夹地址要后台给
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",
		fileSize: "2mb",
		isImage: true, 
		init:{
			FileUploaded : function(up, file, info) { 
				if (info.status == 200 || info.status == 203) {
					console.log(_yetAnotherFileUrl);
					console.log(file); 
					if('big'==imageType){
						vm.bigImagesAdList[arrayIndex].image = _yetAnotherFileUrl;
					}else if('small'==imageType){
						vm.smallImagesAdList[arrayIndex].image = _yetAnotherFileUrl;
					}
					
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
				layer.close(index);
			}
		}
	}); 
	uploader[buttonId].init();
}


var bigImgBtnList = $(".uploadback-btn-big");
var smallImgBtnList = $(".uploadback-btn-small");
$.each(bigImgBtnList,function(i,item){
	var id = $(item).attr("id");
	var imageType = $(item).data("imagetype");
	initUploader(id,imageType,i);
})
$.each(smallImgBtnList,function(i,item){
	var id = $(item).attr("id");
	var imageType = $(item).data("imagetype");
	initUploader(id,imageType,i);
})
