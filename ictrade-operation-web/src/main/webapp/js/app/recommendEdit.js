var vm
$(function() {
	var valid = false;
	 vm = new Vue(
			{
				el : "#recommendEdit",
				data : {
					recommendationId : getQueryString('recommendationId'),
					initData : {
						categoryId:"20021",
						orderSeq:1,
						content:{
							dictionaryItemId:"20021",//区域
							modelName:"",//型号
							category1Id:"",//大类id
							category1Name:"",//大类名称
							category2Id:"",//小类id
							category2Name:"",//小类名称
							category3Id:"",//次小类id
							category3Name:"",//次小类名称
							linkDetails:"",//详情链接
							fileOrAuto:2,//联想or上传
							imageUrl:"",//图片地址
							bImageUrl:"",//大图地址
							manufacturer:"",
							name:"库存精选",
							productId:""
						},								        
					},					
					newsHome:operationWebUrl + "/recommend.htm",
				},				
				methods : {
					init : function(res) {
						var that = this;
						that.oldUrl = res.contentMap.linkDetails;
						that.initData.categoryId = res.categoryId;
						that.initData.orderSeq = res.orderSeq;
						that.initData.content = $.extend(that.initData.content,res.contentMap);
						that.$nextTick(function(){
							that.editInit = false;
						})						
					},
					saveData:function(){
						var that = this;
						if(!that.initData.content.linkDetails){
							$("#linkDetails").addClass("b_red");
							layer.msg("请输入详情链接！",{
								skin:"c_tip"
							});
							return;
						}
						if(that.initData.content.fileOrAuto==1 && !that.initData.content.imageUrl){
							layer.msg("请上传图片！",{
								skin:"c_tip"
							});
							return;
						}
						if(!that.initData.content.modelName){
							layer.msg("请输入有效的详情链接！",{
								skin:"c_tip"
							});
							return;
						}
						var url,type;
						var data = {};
						if(that.recommendationId){
							url = ykyUrl.info + '/v1/recommendations/' + that.recommendationId;
							type = 'PUT';
						}else{
							url = ykyUrl.info + '/v1/recommendations';
							type = 'POST'
						}
						data.categoryId = that.initData.categoryId;
						data.orderSeq = that.initData.orderSeq;
						data.content = JSON.stringify(that.initData.content);
						syncData(url, type, data, function(res, msg) {
							if(msg==null){
								layer.msg("保存成功！",{
									skin:"c_tip"
								});
								$("#save_btn").unbind("click");
								$("#cancel_btn").unbind("click");
								setTimeout(function(){
									if(recommendationId){
										history.go(-1);
									}else{
										location.href = ykyUrl._this + "/recommend.htm";
									}
								},2000)
							}else{
								that.initData.content = JSON.parse(that.initData.content);
							}
						});
					},
					getProduct:function(url){//获取商品信息
						var that = this;
						var prdId = that.getProductId(url,'id');
						that.initData.content.productId = prdId;
						var id = [prdId];
						if(that.editInit){
							return;
						}
						syncData(ykyUrl.product + '/v1/products/batch/basic', 'POST', id, function(res, msg) {
							if(!res.length){
								that.nullProductId(res);
								layer.msg("未关联到商品相关信息!",{
									skin:"c_tip"
								})
								return;
							}else{
								that.associationData(res[0]); 
							}							
							});
					},
					getProductId:function(url,name) {//获取商品id
						if(!url){
							return;
						}
						var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
						var c = url.split('?')[1],r;
						if(c){
							r=c.match(reg);
						}
						if (r != null){
							return decodeURI(r[2]);
						}else{
							var str1 = url.split('product/')[1];
							var str2 = str1?str1.split('.htm')[0]:'';
							if(str2){
								return str2;
							}else{
								return "";
							}			
						}
					},
					nullProductId:function(res){//清空商品信息
						var that = this;
						var obj = {
									dictionaryItemId:"20021",//区域
									modelName:"",//型号
									category1Id:"",//大类id
									category1Name:"",//大类名称
									category2Id:"",//小类id
									category2Name:"",//小类名称
									category3Id:"",//次小类id
									category3Name:"",//次小类名称
									//linkDetails:"",//详情链接
									//fileOrAuto:2,//联想or上传
									imageUrl:res.imageUrl?res.imageUrl:"",//图片地址
									bImageUrl:res.bImageUrl?res.bImageUrl:"",//大图地址
									manufacturer:"",
									name:"库存精选",
									productId:res.productId?res.productId:""
								}								
						
						that.initData.content = $.extend({},that.initData.content,obj);
					},
					associationData:function(res){//渲染数据						
						var that = this;
						that.nullProductId(that.initData.content);
						if(res.spu==undefined){
							return;
						}
						if(res.spu.categories==undefined){
							return;
						}
						
						that.initData.content.modelName = res.spu.manufacturerPartNumber;
						that.initData.content.manufacturer = res.spu.manufacturer;
						$.each(res.spu.categories,function(index,ele){
							if(ele.cateLevel==1){
								that.initData.content.category1Id = ele._id;
								that.initData.content.category1Name = ele.cateName;
							}
							if(ele.cateLevel==2){
								that.initData.content.category2Id = ele._id;
								that.initData.content.category2Name = ele.cateName;
							}
							if(ele.cateLevel==3){
								that.initData.content.category3Id = ele._id;
								that.initData.content.category3Name = ele.cateName;
							}
							
						})
						if(that.initData.content.fileOrAuto == 2){
							that.getImgUrl(res.spu.images);
						}else{
							that.initData.content.bImageUrl = '';
							that.initData.content.imageUrl = '';
						}
						
					},
					getImgUrl:function(images){//获取联想图片
						var that = this;
						var largeImg,standImg,thumbnailImg;
						var spuImages = images;
						if(that.initData.content.fileOrAuto==1){
							return;
						}
						if(undefined == spuImages || !spuImages.length){
							that.initData.content.imageUrl = "";
							that.initData.content.bImageUrl = "";
							return;
						}						
						for(var i=0;i<spuImages.length;i++){
							if(spuImages[i].type=="large"){
								largeImg = spuImages[i].url;
								continue;
							}
							if(spuImages[i].type=="stand"){
								standImg = spuImages[i].url;
								continue;
							}
							if(spuImages[i].type=="thumbnail"){
								thumbnailImg = spuImages[i].url;
								continue;
							}
						}
						var s_img,b_img;
						if(null != thumbnailImg){
							b_img = thumbnailImg;
							s_img = thumbnailImg;
							that.initData.content.imageUrl = thumbnailImg;
							that.initData.content.bImageUrl = thumbnailImg;
						}
						if(null != standImg){
							b_img = standImg;
							s_img = standImg;
							that.initData.content.imageUrl = standImg;
							that.initData.content.bImageUrl = standImg;
						}
						if(null != largeImg){
							b_img = largeImg;
							that.initData.content.bImageUrl = largeImg;
						}
						if(that.initData.content.imageUrl.indexOf("/photos/nophoto/p") > -1){
							that.initData.content.imageUrl ="";
							that.initData.content.bImageUrl ="";
						}						
					},
					showBigImage:function(){//点击显示大图
						var that = this;
						var content;
						if(!that.initData.content.imageUrl){
							return;
						}
						if(that.initData.content.bImageUrl){
							content = $("#bImageUrl");
						}else{
							content = $("#imageUrl");
						}
						layer.open({
						  	  type: 1,
						  	  title: false,
						  	  closeBtn: 0,
						  	  area: ['auto','auto'],  //自定义宽度
						  	  skin: 'layer_cla', //没有背景色
						  	  shadeClose: true,
						  	  content: content
						  	});
					},
					changePrdUrl:function(){//修改商品链接
						var that = this;
						if(that.initData.content.linkDetails == that.oldUrl){
							return;
						}
						that.getProduct(that.initData.content.linkDetails);
						that.oldUrl = that.initData.content.linkDetails;
					}
					
				},
				watch:{
					'initData.content.fileOrAuto':function(val){
						var that = this;
						if(that.editInit){
							return;
						}
						if(val==2){							
							var id = [that.getProductId(that.initData.content.linkDetails,'id')];
							syncData(ykyUrl.product + '/v1/products/batch/basic', 'POST', id, function(res, msg) {
								if(res==''){
									that.nullProductId(res);
									layer.msg("未关联到商品相关信息!",{
										skin:"c_tip"
									})
									return;
								}else if(res[0] && res[0].spu.images){
									that.getImgUrl(res[0].spu.images);
								}
							});
						}else{
							that.initData.content.imageUrl = "";
							that.initData.content.bImageUrl = "";
						}
					}
				}
				
			});
	if (vm.recommendationId) {//获取草稿活动详情
		vm.editInit = true;
		syncData(ykyUrl.info + '/v1/recommendations/' + vm.recommendationId, 'GET', null, function(res, msg) {vm.init(res); });		
	}
	var uploader1 = createUploader({
		buttonId: "selectImage",
		uploadType: "notice.publicRead", //上传后文件存放路径
		url:  ykyUrl.webres,
		types: "jpg,png,jpeg,gif",//可允许上传文件的类型
		fileSize: "10mb", //最多允许上传文件的大小
		isImage: true, //是否是图片
		init:{
			FileUploaded : function(up, file, info) { 
	            layer.close(index);
				if (info.status == 200 || info.status == 203) {
					vm.initData.content.imageUrl = _yetAnotherFileUrl;
					vm.initData.content.bImageUrl = _yetAnotherFileUrl;
				} else {
					layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
				}
				up.files=[];
			}
		}
	});
	uploader1.init();	
});
