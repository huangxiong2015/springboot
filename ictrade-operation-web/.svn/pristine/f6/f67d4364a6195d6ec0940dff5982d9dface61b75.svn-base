var vm;
var addData;
var recommendationId = getQueryString("recommendationId");
var action=getQueryString("action");

$(function() {
	var validate= false;
	vm = new Vue({
			        el: '#advertise_edit',
			        data: {
			        	onlyRead:false,
						recommendationId: getQueryString('recommendationId'),
						checkModel : {},
					    query:"",
					    first:"",
					    url:'',//详情链接
					    numToChinsese:{
					    	1:'一',
					    	2:'二',
					    	3:'三',
					    	4:'四',
					    	5:'五',
					    	6:'六',
					    	7:'七',
					    	8:'八',
					    	9:'九'
					    },
					    info:{
					    	"categoryId": "6001",       //广告类型
						    "categoryTypeId": "advertisement",    //固定要传类型为广告
							//"content": "",
							"expiryDate": "",    //结束时间
							"startDate": "",     //开始时间
							"extend1":"7001",     //广告页面
							"extend2":"8001",       //广告位置
					    	"content":{
						    	title:'',
								categoryId: "6001",  //广告类型
								categoryName:"图片广告",     
								pageId:'7001',        //广告页面
								pageName:'首页', 
								positionId:'8001',    //广告位置
								positionName:'顶部',    
								proportion:'',     //曝光占比
								image:'',     //图片路径
								url:'',      //链接地址
								background:''      //背景色
						    },
					    }					    					    	
					},
					methods : {
						init : function(res) {
							this.initData.title = res.title;
							this.initData.categoryTypeId = res.categoryTypeId;
							this.initData.attachUrl = res.newsAttachment && res.newsAttachment.attachUrl; 
						
						
						},
						saveNews : function(event) {
							this.initData.newsContent = event.data;
							$('#create').submit();
						}
					}
	 		    });
	
	//请求获取初始数据 ，赋值
	if(recommendationId){
		$.aAjax({
			url:ykyUrl.info + '/v1/recommendations/' + recommendationId,
			type:'GET',
			success:function(data){	
				data.content  =	$.extend({}, JSON.parse(data.content), {categoryName:"图片广告"});
				var info = data.content;
				var bigImagesAdList = [];
				var smallImagesAdList = [];
				var bList = info.bigImagesAdList;
				var sList = info.smallImagesAdList;
				if(bList&&bList.length){
					$.each(bList,function(i,item){
						if(item.image!=''){
							bigImagesAdList.push(item);
						}
					})
				}
				if(sList&&sList.length){
					$.each(sList,function(i,item){
						if(item.image!=''){
							smallImagesAdList.push(item);
						}
					})
				}
				info.bigImagesAdList = bigImagesAdList;
				info.smallImagesAdList = smallImagesAdList;
				data.content = info;
				vm.info = $.extend({},vm.info,data);    //初始化content
			},
			error:function(){
				console.log("error")
			}
		})
		
		
	}
	
	$("#cancel_btn").on("click",function(){
		history.go(-1);
	})
 });


