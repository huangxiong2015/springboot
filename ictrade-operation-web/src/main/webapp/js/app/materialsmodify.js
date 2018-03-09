var	 vm ;
var  id = getQueryString("id");    //活动id
var from = getQueryString("from"); //manage物料管理列表页    operation物料管理（操作）列表页  
$(function() {
vm = new Vue({
        el: '#extension-edit',
        data: {
	            datas:{},
	            mainUrl:'',    //图片上传路径
	            attachUrl1:'',    // detailImages细节图 
	            attachUrl2:'', 
	            attachUrl3:'', 
	            attachUrl4:'', 
	            cur: 1,
	            curPage: 1,
	            t:0,                   //上传计数
	            len:0,
	            description:'',
	            status : 0,     //status 状态  
	            isChecked1: false,  //选中1，false就是未选中
	            isChecked2: false,
	            isChecked3: false,
	            restrictMaterialType:'',  //后台
	            isrohs:true ,     //rohs 特殊物料 
	            iframeUrl:$("#iframeUrl_Id").val(),  //富文本id 
	            extendInfo:{
	            	"detailImages": [ ],
	            	    "id": id,
	            	    //"isControlMaterial": "",   //是否限制物料           
	            	    "materialDetail": "",      //物料详情  
	            	    "materialName": "",        //物料名称    
	            	    "promotionWord": ""        //促销词   
	            },    //新增扩展字段 
				uploadFiles: { 
	                "keyname":'SPEC文档地址:',     //label         
	                "isImage": "false",    //是否為圖片
	                "key": "other",        //字段id
	                "name": "other[]",      //字段name 
	                "attachFiles":[],   //设置默认值
	                "config": {						//设置上传按钮
	                    "buttonId": "faceUploadBtn",
	                    "types": "pdf",
	                    "url": ykyUrl.webres,
	                    "uploadType": "notice.publicRead",
	                    "fileSize": "20mb"
	                }, 
	                "max":5  //最多上传数   
				},	
				formid:'materials-modify',
				api: ykyUrl.product + "/v1/products/standaudit/" ,
				
				subNum:0
				
		},
        created:function(){
        	var that = this;
        	nowUrl = '';
        	 if(from == 'operation'){//审核操作人员 
        			nowUrl = ykyUrl.product + "/v1/products/standaudit/" +id;
    		 }else if(from == 'manage'){
    				nowUrl = ykyUrl.product + "/v1/products/stand/" +id;
    		  }
        	//查询活动详情
        	syncData(
        			nowUrl,
        			'GET',
        			null,
        			function(res,err) {
						if(res){     //初始化 
							that.datas = res;
							   if(res.description){
								   vm.description = res.description ;  //描述 

									  var text = vm.description;
								      var counter = text.length;
								      $("#number").text(counter);
							   }
								
							   if(res.rohs  != undefined){
								   vm.isrohs = res.rohs ;   //isrohs
							   }else if(res.rohs == undefined ){
								   vm.isrohs = '';
							   }
							   if(res.status ){
								   vm.status = res.status ;  //状态 
							   }
							   if(vm.datas.images && vm.datas.images.length >0 ){
								   $.each(vm.datas.images,function(index,ele){
								    	if(ele.type == "standard" ){
								    		 vm.mainUrl = ele.url;      //上传主图赋值 
								    	}
								    }) 
							   }
							   if(res.extendInfo && res.extendInfo.detailImages.length >0 ){
								   $.each(res.extendInfo.detailImages,function(num,ele){
									   vm['attachUrl'+(num+1)]= ele.url;     //上传细节图赋值初值
								    }) 
							   }
							   if(res.extendInfo && res.extendInfo.materialName){
								  vm.extendInfo.materialName  = res.extendInfo.materialName;
							   }
							   
							   if(res.extendInfo && res.extendInfo.promotionWord){
									  vm.extendInfo.promotionWord  = res.extendInfo.promotionWord;
								   }
							   var list = [];
							   if(res.documents && res.documents.length >0){
								   //值存在类型为datasheets的集合里，portal是这样取值的，其他忽略掉  
								   $.each(res.documents,function(index,ele){
									   if(ele.type == 'datasheets'){
										    $.each(ele.attaches,function(ind,el){
										    	list.push(el.url); //文档地址赋值  
										    })
									   }else if(ele.type == 'dataSheet'){
										   list.push(ele.url); 
									   }
								    	 
								    })
							   }
							   vm.uploadFiles.attachFiles  = list; 
							    //限制物料
							   
							   if(res.restrictMaterialType){ //限制物料
								    if(res.restrictMaterialType == 'N'){
								    	vm.isChecked1 = false;
								    }else{
								    	var str = res.restrictMaterialType ; //'F-T-I'
								    	var strlist = str.split('-');
								    	$.each(strlist,function(ind,el){
								    		if(el == 'F'){
								    			vm.isChecked1 = true
								    		}
								    		if(el == 'T'){
								    			vm.isChecked2 = true
								    		}
								    		if(el == 'I'){
								    			vm.isChecked3 = true
								    		}
								    	})
								    }
							   }
							   
							   setTimeout(function() {   //物料详情
									if (res.extendInfo && res.extendInfo.materialDetail ) {
										var content = 'loadContent' + res.extendInfo.materialDetail;    //初始化  
										window.frames[0].postMessage(content,ykyUrl.ictrade_ueditor);
									}
								}, 500);
						     
						}
				    }
        		) 
        		
        },
        mounted(){
        	 
            //富文本提交数据，写一个监听器 
           		if (window.addEventListener) {
           			window.addEventListener('message', this.saveNews, false);
           		} else if (window.attachEvent) {
           			window.attachEvent('message', this.saveNews, false);
           		} 
           		
          
        },
        methods: {
        	saveNews : function(event) {
				vm.extendInfo.materialDetail = event.data;  //富文本标签保存到物料详情字段里面 put
				$('#'+this.formid).submit(); 
				//this.subNum++;
				/*if(this.subNum ==2){
					
				}*/
			},
        	deleteImage:function(attachUrl){//删除商品图片
				var that = this;
				var list = [];
				vm.extendInfo.detailImages = [];
				var arr = ['attachUrl1','attachUrl2','attachUrl3','attachUrl4'];
				vm[attachUrl] = "";   //清空当前值 
				$.each(arr,function(n,ele){
					if(vm[ele]){
						list.push(vm[ele]);
						vm[ele] = "";   //把几个值清空
					}
				})
				 
   	     
				$.each(list,function(m,ele){
					var extendobj={     //细节图        
			                   "type": "littleImg",
		   	                   "url":''};
					if(ele){
						vm['attachUrl'+(m+1)] = ele;
						extendobj.url = ele;
						vm.extendInfo.detailImages.push(extendobj);  //extendInfo对象已经保存了，不用再重复保存了
					}
				})
			},
           imageInit:function(i, idx){
        	   var that=this;
        	    var  loaders = createUploader({
        	             buttonId:"uploadback"+idx,   //这个地方不一样就可以初始化5个了 
        	             uploadType: "notice.publicRead",   //此处需要修改 
        	          	 url:  ykyUrl.webres,
        	          	 types: "jpg,png,jpeg",
        	          	 fileSize: "2mb",
        	          	 isImage: true,
        	             init:{
        	                 FileUploaded : function(up, file, info) {
        	                     layer.close(index);
        	                     if (info.status == 200 || info.status == 203) {
        	                    	  vm["attachUrl"+i] = _yetAnotherFileUrl;
         	                    	
        	                     } else {
        	                         layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120});
        	                     }
        	                     up.files=[];
        	                 }
        	             }
        	         });
        	    loaders.init();
           },
           saveData:function(){
        	  
        	   var that = this;
               var formid = this.formid; 
               var api = this.api + id;
              
               formValidate('#'+formid, null, function() {
            	   id =  vm.datas.id 
                   vm.datas.extendInfo = vm.extendInfo;  //多文本赋值
                    
                   //@todo 手动赋值
                   var imgeList = [{type: "thumbnail", url: ""},{type: "standard", url: ""},{type: "large", url: ""}];
                   $.each(imgeList,function(index,el){
                           el.url = vm.mainUrl;
                   })
                   
                   if(vm.datas.images && vm.mainUrl !='' ){
                       vm.datas.images = imgeList ;  //组装好3张图 ，赋值给 images对象 
                   }
                   //增加细节图
                   vm.extendInfo.detailImages = [];
                   var Url1 = vm.attachUrl1 ;
                   var Url2 = vm.attachUrl2 ;
                   var Url3 = vm.attachUrl3 ;
                   var Url4 = vm.attachUrl4 ;
                   var urList =[Url1,Url2,Url3,Url4];
						
                   $.each(urList,function(m,ele){
							var extendobj   =  {     //保存细节图        
					                   "type": "littleImg",
				  	                   "url":''
				  	                	   };
							if(ele){
								extendobj.url = ele;
								vm.extendInfo.detailImages.push(extendobj);  //extendInfo对象已经保存了，不用再重复保存了
							}
						})
						
                    var obj={
                           attaches:[],
                           type:'datasheets'
                        }
                      //校验不规范文档地址 
                        if(vm.uploadFiles.attachFiles.length > 0 ){
                            var itemList = vm.uploadFiles.attachFiles;
                            $.each(itemList,function(ind,el){
                                  if(el){
                                      itemList[ind] = cutUrl(el,ind); 
                                  }
                                 
                            })
                            
                        }
                       $.each(vm.datas.documents,function(index,ele){
                            if(ele.type == 'datasheets'){
                                ele.attaches = [];  //重置文档集合 
                                //var itemList = vm.uploadFiles.attachFiles ;//name直接去掉了,文档地址直接赋值 
                                   $.each(itemList,function(ind,el){
                                        var item ={
                                                   url:''   
                                                  };
                                       if(el){
                                           item.url = el ;
                                           ele.attaches.push(item);
                                       }
                                   })
                             }else if(ele.type == 'dataSheet'){
                            	 vm.datas.documents = [];  //重置文档集合 
                            	 $.each(itemList,function(ind,el){
                            		var item ={
                            				name:'',
                            				type:'dataSheet',
                                            url:''
                                            };
                                    if(el){
                                    	item.url = el ;
                                    	vm.datas.documents.push(item);
                                    }
                                })
                             }
                            
                        }) 
                       
                        //documents集合为空，往里面新增 
                       if(!vm.datas.documents || vm.datas.documents.length == 0){
                           vm.datas.documents = [];
                               $.each(itemList,function(ind,el){
                                   var item ={
                                           url:''   
                                          };
                                   if(el){
                                       item.url = el ;
                                       obj.attaches.push(item);
                                   }
                                   
                                   
                               })
                           vm.datas.documents.push(obj);
                       }
                       vm.datas.rohs  =  vm.isrohs ;   //isrohs
                       vm.datas.status = vm.status ;  //状态 
                       //限制物料
                	   var strlist = [];
                	   if(vm.isChecked1 == true){
                		   strlist.push('F');
                	   }
                	   if(vm.isChecked2 == true){
                		   strlist.push('T');
                	   }
                	   if(vm.isChecked3 == true){
                		   strlist.push('I');
                	   }
                	   if(strlist.length > 0){
                		   vm.datas.restrictMaterialType = strlist.join('-');
                	   }else{
                		   vm.datas.restrictMaterialType = 'N';
                	   }
                       vm.datas.extendInfo = vm.extendInfo;   //扩展字段
                      // return 
                   syncData(
                		 api,
       					"PUT",
       					vm.datas,
       					function(data, err) {
       						if (null != data) {
	                             $("#save_btn").addClass('disabled');
	                             $("#cancel_btn").addClass('disabled');
	                              layer.msg("操作成功 ！",{icon:1,offset:120});
	                              setTimeout(function(){ 
	                                  if(from == 'operation'){
	                                      window.location = ykyUrl._this + '/operateMaterial.htm';
	                                  }else if(from == 'manage'){
	                                      window.location = ykyUrl._this + '/basicMaterial.htm';
	                                  }
	                              },2000)
	                         }
       					});
                   
               }, null);
           },

        } 
    });

//初始化 上传图片
var uploader1 = createUploader({
	buttonId: "uploadback",
	uploadType: "notice.publicRead",   //此处需要修改 
	url:  ykyUrl.webres,
	types: "jpg,png,jpeg",
	fileSize: "2mb",
	isImage: true,
	init:{
		FileUploaded : function(up, file, info) {
          layer.close(index);
			if (info.status == 200 || info.status == 203) {
				console.log(_yetAnotherFileUrl);
				console.log(file);
				vm.mainUrl = _yetAnotherFileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120});
			}
			up.files=[];
		}
	}
});
 uploader1.init();



//初始化 上传细节图片 
	  var arr = [1,2,3,4];
	  $.each(arr,function(idx,n){
		  //n为1,2,3,4
		   vm.imageInit(n,idx); 
	  })
 
  $("#isControlMaterial").change(function(){
	/* if(vm.MaterialType == 'N'){
  	   vm.isChecked1 = 'false';
  	   vm.isChecked2 = 'false';
  	   vm.isChecked3 = 'false';
     }else{
        vm.isChecked1 = 'true';
    	vm.isChecked2 = 'true';
    	vm.isChecked3 = 'true';
     }*/
	});

  

  $("#todateCode").on("blur keyup input", function() {
      var text = $("#todateCode").val();
      var counter = text.length;
      $("#number").text(counter);
  });

  

  
   
  //取消按钮 
  $("#cancel_btn").on("click",function(){
	  if($(this).hasClass('disabled')){
		  return;
	  }
	    if(from == 'operation'){
		  window.location = ykyUrl._this + '/operateMaterial.htm';
		 }else if(from == 'manage'){
		  window.location = ykyUrl._this + '/basicMaterial.htm';
		 }
	});
   
   function  cutUrl(url,index){
		var  Link = url;
		
		 if(Link.indexOf("http:")>=0){
			return Link;
		 }else if(Link.indexOf("https:")>=0){
			return Link;
		 }else{
			if(Link.substring(0,2) === "//"){
				return Link;
			}else{
				Link  = "//" + Link;
				return  Link ;
			}
		}
   }
});
function requireSaveData() {  //富文本
	//发送指令到iframe请求返回数据
   window.frames[0].postMessage('save_btn', ykyUrl.ictrade_ueditor);
   vm.saveData();
}


