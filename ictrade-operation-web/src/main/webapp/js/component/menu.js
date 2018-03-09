//初始化左边栏高度 	
	$(function(){
  		
  		
  		var vm = new Vue({
  	        el : '#menu',
  	        data : {
  	            api: ykyUrl.database+"/v1/menus",
  	            params: {menuTypeId:'operation'}
  	        }
  	    })
  		/*var selectCatid = sessionStorage.getItem("menuId");
  		if(!selectCatid){	
  			selectCatid = '';
  		}
  		var menus = new Vue({
  			el: '#menu',
  			data: {
  				url:  ykyUrl.database+"/v1/menus",
  			    selected:selectCatid,
  				list:[],
  				idlist: []
  			},
  			mounted(){
				 var $this = this;
				 var datas  = sessionStorage.getItem("operationMunus");
			  		if(datas){
			  			this.list = JSON.parse(datas); 
			  		}
			  		else{
		             httpGet($this, $this.url, {menuTypeId:'operation'} , function (res , err) {//页面加载前调用方法
		                 if(res){
		                     $this.list = res;  
		            		 sessionStorage.setItem("operationMunus", JSON.stringify(res));
	
		            		 //刷新左侧菜单栏高度样式
		            		 $.AdminLTE.layout.fix();
		                 }
		             });
			  		}
  			},
  			methods:{ 
  			    setActiveLi:function(id, subid){ 
  					sessionStorage.setItem("menuId", id +','+ subid); 
  			    }
  			},
  			computed:{
  				retActive:function(){ 
  			    	var menu = selectCatid.split(','); 
  					return menu[0];
  			    },
  			    retActiveLi:function(){  
  			    	var menu = selectCatid.split(','); 
  			    	return  menu[1];
  			    }
  			}
  		})*/
  	});
	
	