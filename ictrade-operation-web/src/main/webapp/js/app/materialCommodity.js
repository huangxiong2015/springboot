var	 vm ;
var  id = getQueryString("id"); 
var from = getQueryString("from"); //manage物料管理列表页    operation物料管理（操作）列表页  
$(function(){
	vm = new Vue({
		el: '#extension-edit',
		data:{
			datas:{
          	  extendInfo:{
                	"detailImages": [],
                	    "id": id,
                	   // "isControlMaterial": "",   //是否限制物料           
                	    "materialDetail": "",      //物料详情  
                	    "materialName": "",        //物料名称    
                	    "promotionWord": ""        //促销词   
                }, 
          },
          name :'否',
          attachUrl1:'',     	  //图片上传路径
          cur: 1,
          curPage: 1,
          operateDate:[],
          tabValue:'tariff',
          isChecked1: false,  //选中1，false就是未选中
          isChecked2: false,  //关税
          isChecked3: false,  //商检
          restrictMaterialType:'',  //后台
          formid:'createTariff',
          api: ykyUrl.product + "/v1/products/standaudit/" ,
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
							that.extendInfo = res.extendInfo ;
							that.imageurl(that.datas.images);

						   if(res.restrictMaterialType){ //限制物料
							    if(res.restrictMaterialType == 'N'){
							    	vm.isChecked1 = false;
							   	    vm.isChecked2 = false;
							   	    vm.isChecked3 = false;
							    }else{
							    	var str = res.restrictMaterialType ; //'F-T-I' F受控 T关税 I商检
							    	var strlist = str.split('-');
							    	$.each(strlist,function(ind,el){
							    		if(el == 'F'){
							    			vm.isChecked1 = true;
							    			vm.name = "受控"
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
        methods: {
           saveData:function(){  
        	   var that = this;
               var formid = this.formid; 
               var api = this.api + id;
            	   
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
                   
           },
           cancelData:function(){
        	   if(from == 'operation'){
                   window.location = ykyUrl._this + '/operateMaterial.htm';
               }else if(from == 'manage'){
                   window.location = ykyUrl._this + '/basicMaterial.htm';
               }
           },
          imageurl:function(data){
        	  if(!data.length){/* 图片集合为空 */
    	    		//return true;
    	    	}
    	    	var hasStandard = false   ;
    	    	$.each(data,function(index,ele){
    	    		if( ele.type == 'standard'){
    	    			hasStandard = true   ;
    	    			vm.datas.imageurl = ele.url;
    	    		}
    	    	})
    	    	
    	    	
    	    	if(!hasStandard || vm.datas.imageurl == ''){
    	    		vm.datas.imageurl = ykyUrl._this +'/images/defaultImg01.jpg';
    	    	}
    	    	
   	    	},

        } 
        
	})
})