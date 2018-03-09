var Authorization = $("#pageToken").val();
	//重新定义带权限的AJAX

$.extend({       
	aAjax: function(option){
		var defualts = {
					beforeSend: function(request) {
		                request.setRequestHeader("Authorization", Authorization);
		            },
		            contentType:"application/json",
		            xhrFields: {
		                withCredentials: true
		            },
		            crossDomain: true,
			  	    success: function(){},
			  	    error: function(){},
				}
		var opts = $.extend({}, defualts, option);
		
		$.ajax(opts);
	}
});

//获取当前url参数
function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null){
		return decodeURI(r[2]);
	}else{
		return "";
	}
} 

function GetRequestWithout(url, param) {
	if(url==null || url==""){
		return "";
	}
    var theRequest = new Object();
	var index = url.indexOf("?");
	    if ( index != -1) {
	        var str_params = url.substr(index + 1);
	var str_url = url.substr(0, index + 1);
	    strs = str_params.split("&");
	var temp;
    for (var i = 0; i < strs.length; i++) {
		temp = strs[i].split("=")[0];
		if(temp != param) {
			str_url += strs[i];
			str_url += "&";
		}
     }
	str_url = str_url.substr(0, str_url.length - 1);
	    }
    return str_url;
}

function setPageOption(data){
	$("#total").val(data.total);
	$("#pageSize").val(data.pageSize);
}

/* menus */
var menusData = getlocaltionData(),
	isMenuCache = false;
function getlocaltionData(){
	var menus = sessionStorage.getItem("operationMunus");
	if(menus)
		return JSON.parse(menus);
	
	return[];	
	
}
if(!selectCatid){	
	var selectCatid = "";
}
var menus = avalon.define({
    $id: "menu",
    list: [],
    selected:selectCatid,
    goodsManage:[],
    project : ykyUrl.admin,
    goodsUrl :ykyUrl._this,
    setActive:function(id){
    	var idlist = this.selected.split(",");
    	var active = "";
    	$.each(idlist, function(index,cat){
			if(cat == id)
				active = "active";
		})
		
		return active;
    },
    setActiveLi:function(id){
    	var idlist = this.selected.split(",");
    	var active = "";
    	$.each(idlist, function(index,cat){
			if(cat == id)
				active = "menu_active";
		})
		
		return active;
    }
})
var parentList = [],list = [],subLs = {} ,subList=[];
if(!isMenuCache || menusData.length ==0){
	$.aAjax({
		 //url: menus.project+"/rest/menu/operation",
		 url: ykyUrl.database+"/v1/menus?menuTypeId=operation",
	     data:{}, //去掉数据模型中的所有函数
	     success: function(data) {
	    	 menus.list=data;
    		 sessionStorage.setItem("operationMunus",JSON.stringify(data));
    		 
    		 /*if(data.length > 0){    			 
    			 $.each(data,function(i,el){
    				 if(el.parentMenuId == ""){
    					 parentList.push(el);
    				 }else{
    					 list.push(el);
    				 }
    			 });
    			 
    			 $.each(parentList,function(i,el){
    				 subList = [];
    				 $.each(list,function(j,node){
    					 if(el.menuId == node.parentMenuId && node.parentMenuId != ""){  //如果父类list中的id ==子类list中的父id
    						 subList.push(node);
    					 }
    				 });
    				 subLs = {subList:subList};
    				 parentList[i] = $.extend({},parentList[i],subLs);
    			 });
    		 }
    		 menus.list = $.extend({},menus.list.$model,parentList);
    		 */
    		 

    		 //刷新左侧菜单栏高度样式
    		 $.AdminLTE.layout.fix();
	     }
	 })
 }


//弹出原图
function showVoucherPic(showOriIdStr) {
	
  	layer.open({
  		type : 1,
  		title : false,
  		closeBtn : 0,
  		area : [ 'auto', 'auto' ], // 自定义宽度
  		skin : 'layer_cla', // 没有背景色
  		shadeClose : true,
  		maxWidth: $(window).width() * 0.9,
  		content : $("#"+showOriIdStr)[0].outerHTML,
  		success: function(layero) {
  			var maxWidth = $(window).width() * 0.9;
  			var maxHeight = $(window).height() * 0.9;
  			var src = layero.find('img').attr('src');
  			
  			var imgEle = new Image();
  			imgEle.src = src;
  			
  			if(imgEle.width > imgEle.height) {
  				layero.find('img').css('maxWidth', maxWidth);
  			}else {
  				layero.find('img').css('maxHeight', maxHeight);
  			}
  			
  			var topPx = ($(window).height() - layero.find('img').height()) / 2;
  		  	var leftPx = ($(window).width() - layero.find('img').width()) / 2;
  	  		$('.layui-layer').css('top', topPx);
  	  		$('.layui-layer').css('left', leftPx);
  	  		$('.layui-layer-content').height('auto');
  		}
  	});
}


/**
   * 重置图片宽高
   * @param obj 图片对象
   */
function reSizeImg(obj){
	var maxWidth = $(window).width()*0.9;
  	var maxHeight = $(window).height()*0.9;
  	var imgWidth=obj.width;
  	var imgHeight=obj.height;
  	var p =1 ;
  	var pWidth = 1;
  	var pHeight = 1;
  	if(imgWidth>maxWidth)
  	{
  		pWidth=imgWidth/maxWidth;
  	}
  	if(imgHeight>maxHeight){
  		pHeight = imgHeight/maxHeight;
  	}
  	if(pWidth>pHeight){
  		p = pWidth;
  	}else{
  		p = pHeight;
  	}
  	if(p>1){//收缩图片尺寸
  		obj.width=Math.floor(imgWidth/p);
  		obj.height=Math.floor(imgHeight/p);
  	}
}

 /* menus end */

//解决乘法精度丢失问题
function numMulti(num1, num2) { 
	var baseNum = 0; 
	try { 
		baseNum += num1.toString().split(".")[1].length; 
	} catch (e) { 
	} 
	try { 
		baseNum += num2.toString().split(".")[1].length; 
	} catch (e) { 
	} 
	
	return Number(num1.toString().replace(".", "")) * Number(num2.toString().replace(".", "")) / Math.pow(10, baseNum); 
}; 