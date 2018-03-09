var database = ykyUrl.database + '/v1/menus';
var zTreeObj,zNodes,treeObj;
var activeMenuId = localStorage.getItem('activeMenuId');
//var activeMenuId = getQueryString("activeMenuId");

var setting = {//zTree配置
		 data : {
				key : {
					name : "menuName"
				},
				simpleData : {
					enable : true,
					idKey : "menuId",
					pIdKey : "parentMenuId",
					rootPId : -1
				}
			},
		   	edit:{
		   		enable:true,
		   		showRemoveBtn:false,//是否显示删除按钮
		   		showRenameBtn:false,//是否显示编辑名称按钮
		   		drag:{
		   			isCopy:false,//是否允许复制节点
		   			isMove:true,//是否允许移动节点
		   			prev: true,
					next: true,
		   			inner:false//是否允许成为目标节点的子节点
		   		}
		   	},
		   	view:{
		   		//selectedMulti:false//是否允许同时选中多个节点
		   	},
		   	callback:{
		   		onClick:zTreeOnClick,//点击事件回调
		   		beforeDrag: zTreeBeforeDrag,//被拖拽之前的事件回调  
		   		beforeDrop:zTreeBeforeDrop,//拽操作结束之前的事件回调
		   		onDrop: zTreeOnDrop//拖拽操作结束的事件回调		   		  
		   	}
	   };
var pathList;

function zTreeOnClick(event, treeId, treeNode, clickFlag) {//点击事件回调
	$("#menuName").blur();
	 maintain.menuData = maintain.initMenuData();
	 maintain.editMenuDetail = [];
	 maintain.clickMenu(treeNode.menuId,treeNode.level);
	 maintain.editStatusTip = true;	 
	 maintain.pathList = treeNode.getPath();
	 //getPathList(maintain.pathList);
};
function zTreeBeforeDrag(treeId, treeNodes) {//被拖拽之前的事件回调  
	$("#menuName").blur();
	 maintain.menuData = maintain.initMenuData();
	 maintain.editMenuDetail = [];
	 maintain.clickMenu(treeNodes[0].menuId,treeNodes[0].level);
	 maintain.editStatusTip = true;
	 maintain.pathList = treeNodes[0].getPath();
	 //getPathList(maintain.pathList);
} 
function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {//拽操作结束之前的事件回调
	if(treeNodes[0].level!==targetNode.level)
			 return false;
	
	if(treeNodes[0].level==targetNode.level && treeNodes[0].parentMenuId !== targetNode.parentMenuId)
  		 return false;    	
         
	
}
function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {//拖拽操作结束的事件回调
	 maintain.menuData.menuId = maintain.activeMenu.menuId;
	 maintain.menuData.menuName = maintain.activeMenu.menuName;
	 maintain.menuData.orderSeq = targetNode?targetNode.orderSeq:maintain.activeMenu.orderSeq;
	 maintain.menuData.menuStatus = 'ACTIVE';
	 maintain.menuData.styleTypeId = maintain.activeMenu.styleTypeId;
	 maintain.menuData.parentMenuId = maintain.activeMenu.parentMenuId;
	 syncData(ykyUrl.database + '/v1/menus/' + treeNodes[0].menuId, 'PUT', maintain.menuData, function(data){
		 if(data !== null){
			 maintain.getData();
		 }
	 })
	};
	 
    
   
/*var menuTep = {
        "menuId": "",        
        "menuName": "",
        "orderSeq": "1",
        "menuUrl": "",
        "menuStatus": "",
        "menuTypeId": "HELP_CENTER",
        "styleTypeId": "",
        "parentMenuId": "",
        "menuLogoUrl": "i_user"
      };*/

var maintain = new Vue({
	el:"#maintainList",
	data:{
		dataList:[],
		childrenList:[],
		contentList:[],
		activeMenu:[],
		menuData:{},
		editMenuDetail:[],
		newContentBtn:false,
		editStatusTip:true,
		pathList:[]
	},
	methods:{
		getData:function(){
			var _this = this;
			syncData(ykyUrl.database + '/v1/menus/helpcenter', 'GET', null, function(data){
				if(data){
					 zNodes = data;
					 zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
					 treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					 var nodes = treeObj.getNodes();
					 var curMenuId = activeMenuId || _this.activeMenu.menuId;
					 _this.dataList = data;
					 if (nodes.length>0 && !curMenuId){
					 	treeObj.selectNode(nodes[0]);//选中默认节点
					 	treeObj.expandNode(nodes[0]);//展开默认节点
					 	_this.clickMenu(nodes[0].menuId,nodes[0].level);
					 	}else{
						 	$.each(zNodes,function(i,e){
						 		if(e.menuId == curMenuId){
						 			var a_nodes = treeObj.getNodeByParam('menuId',curMenuId);
						 			treeObj.selectNode(a_nodes);//选中指定节点
						 			treeObj.expandNode(a_nodes);//展开指定节点
						 			_this.pathList = treeObj.getSelectedNodes()[0].getPath();
						 			//getPathList(_this.pathList);
						 			_this.clickMenu(a_nodes.menuId,a_nodes.level);
						 		}
						 	})
						 }
					 if(activeMenuId){
						 localStorage.removeItem('activeMenuId');
					 }
					 _this.editStatusTip = true;
					 _this.menuData.menuName = '';
				}
			})
		},
		initMenuData:function(){
			return {
		        menuId: "",        
		        menuName: "",
		        orderSeq: "1",
		        menuUrl: "",
		        menuStatus: "",
		        menuTypeId: "HELP_CENTER",
		        styleTypeId: "",
		        parentMenuId: "",
		        menuLogoUrl: "i_user"
		      }
		},
		clickMenu:function(id,level){//选中菜单
			var _this = this;
			maintain.childrenList = [];
			maintain.contentList = [];
			$.each(this.dataList,function(idx,ele){
				if(ele.parentMenuId == id){
					ele.level = Number(level)+1;
					maintain.childrenList.push(ele)
				}
				if(ele.menuId == id){
					ele.level = level;
					maintain.activeMenu = ele;
				}
			})
			if((level==2&&maintain.activeMenu.styleTypeId=="")||level==3){
				maintain.newContentBtn = true;
			}else{
				maintain.newContentBtn = false;
			}
			/* 获取文案内容列表 */
			if(maintain.childrenList.length==0 && level>1 && maintain.activeMenu.styleTypeId==""){
				$.aAjax({
					 url:ykyUrl.info + '/v1/news',
					 type:'GET',
					 data:{
						 categoryTypeIdStr:'HELP',
						 categoryId:id
					 },
					 success:function(data){
						 maintain.contentList = data.list;
					 },
					 error:function(data){
						 console.log("error");
					 }
				 })
			}
		},
		editMenu:function(ele){//新增、编辑菜单信息
			var _this = this;
			 $("#menuName").blur();
			 if(ele){
				 //$("#menuName").focus(ele);
				 maintain.editMenuDetail = ele;
				 maintain.focusNewMenu(ele);
			 }else{
				 maintain.editMenuDetail = [];
				 //$("#menuName").focus();			 
				 maintain.focusNewMenu();
			 }
			 if(maintain.activeMenu.styleTypeId === "SLIDER"){
				}
		 },
		 focusNewMenu:function(ele){
			 var _this = this;
			 if(ele && ele.menuName){//编辑
				 $("#menuName").focus();
				 maintain.editStatusTip = false;
				 maintain.menuData.menuId = ele.menuId;
				 maintain.menuData.menuName = ele.menuName;
				 maintain.menuData.orderSeq = ele.orderSeq;
				 maintain.menuData.menuStatus = ele.menuStatus;
				 maintain.menuData.styleTypeId = ele.styleTypeId;
				 maintain.menuData.parentMenuId = ele.parentMenuId?ele.parentMenuId:maintain.activeMenu.menuId;
				 maintain.menuData.menuLogoUrl = ele.menuLogoUrl;			 
			 }else if(maintain.editMenuDetail.menuName){
				 $("#menuName").focus();
				 maintain.editStatusTip = false;
				 maintain.menuData.menuId = maintain.editMenuDetail.menuId;
				 maintain.menuData.menuName = maintain.editMenuDetail.menuName;
				 maintain.menuData.orderSeq = maintain.editMenuDetail.orderSeq;
				 maintain.menuData.menuStatus = maintain.editMenuDetail.menuStatus;
				 maintain.menuData.styleTypeId = maintain.editMenuDetail.styleTypeId;
				 maintain.menuData.parentMenuId = maintain.editMenuDetail.parentMenuId?maintain.editMenuDetail.parentMenuId:maintain.activeMenu.menuId;
				 maintain.menuData.menuLogoUrl = maintain.editMenuDetail.menuLogoUrl;
			 }else{//新增
				 $("#menuName").focus();
				 maintain.editStatusTip = true;
				 maintain.menuData.menuId = "";
				 maintain.menuData.menuName = "";
				 maintain.menuData.orderSeq = maintain.childrenList.length == 0 ? 1 : Number(maintain.childrenList[maintain.childrenList.length-1].orderSeq) + 1;
				 maintain.menuData.menuStatus = 'ACTIVE';
				 maintain.menuData.styleTypeId = "";
				 maintain.menuData.parentMenuId = maintain.activeMenu.menuId;
				 
			 }
		 },
		 saveData:function(){//保存菜单
			 var _this = this,
			 	menuName = $.trim(_this.menuData.menuName),
			 	menuId = _this.menuData.menuId;
			 if(menuName){				 
				 var url = menuId ? ykyUrl.database + '/v1/menus/' + menuId : ykyUrl.database + '/v1/menus/';
				 var type = menuId ? 'PUT' : 'POST';
				 syncData(url, type, _this.menuData, function(data){
					 if(data){
						 _this.getData()
					 }
				 })
			 }
		 },
		 deleteMenu:function(id){//删除菜单
			 var _this = this;
			 var menuNode = treeObj.getNodeByParam('menuId',id);
			 if(menuNode.children){
				 layer.alert("该分类下已有子分类，无法删除。请先删除分类下的所有子分类才能删除此分类。",{
					 offset: "auto",
						btn: ['确      认'], //按钮
						title: " ",	
						area: 'auto',
						maxWidth:'500px',
						move: false,
						skin: "up_skin_class",
						closeBtn: false,
						})
			 }else{
				 var data = {
						 categoryTypeIdStr:'HELP',
						 categoryId:id
					 }
				 syncData(ykyUrl.info + '/v1/news', 'GET', data, function(data){
					 if(data){
						 if(!data.list.length){
							 layer.alert("确认是否删除？",{
								 offset: "auto",
									btn: ['确      认','取      消'], //按钮
									title: " ",	
									area: 'auto',
									maxWidth:'500px',
									move: false,
									skin: "up_skin_class",
									closeBtn: false,
									yes:function(){
										syncData(database +'/'+ id, 'DELETE', null, function(data){
											if(data){
												layer.msg('删除成功！');
												 _this.getData()
											}
										})
									}
							 })
						 }else{
							 var flag = true;
							 $.each(list,function(index,ele){
								 if(ele.status == "PUBLISHED"){
									 flag = false;
									 layer.alert("该分类已发布，无法删除。请先停用已发布的帮助文案才能删除此分类。",{
										 offset: "auto",
											btn: ['确      认'], //按钮
											title: " ",	
											area: 'auto',
											maxWidth:'500px',
											move: false,
											skin: "up_skin_class",
											closeBtn: false,										
									 })
								 }
							 });
							 if(flag){
								 layer.alert("该分类下有待发布的文案信息，请确认是否删除？",{
									 offset: "auto",
										btn: ['确      认','取      消'], //按钮
										title: " ",	
										area: 'auto',
										maxWidth:'500px',
										move: false,
										skin: "up_skin_class",
										closeBtn: false,
										yes:function(){
											syncData(database +'/'+ id, 'DELETE', null, function(data){
												layer.msg('删除成功！');
												 _this.getData();
											})
										}
								 })
							 }
						 }
						 
					 }
				 })
			 }
		 },
		 newContent:function(){
			 var pathList = this.pathList;
			 var pathName1,pathName2,pathName3;
	    	 pathName1 = pathList[1]?pathList[1].menuName:'';
	    	 pathName2 = pathList[2]?pathList[2].menuName:'';
	    	 pathName3 = pathList[3]?pathList[3].menuName:'';
	    	 window.location.href=ykyUrl._this + '/helpMaintain/edit.htm?categoryId=' +  maintain.activeMenu.menuId + '&pathName1='+ pathName1+ '&pathName2='+ pathName2+ '&pathName3='+ pathName3;
		 },
		 editContent:function(id){
			 var pathList = this.pathList;
			 var pathName1,pathName2,pathName3;
	    	 pathName1 = pathList[1]?pathList[1].menuName:'';
	    	 pathName2 = pathList[2]?pathList[2].menuName:'';
	    	 pathName3 = pathList[3]?pathList[3].menuName:'';
	    	 window.location.href=ykyUrl._this + '/helpMaintain/edit.htm?categoryId=' +  maintain.activeMenu.menuId + '&pathName1='+ pathName1+ '&pathName2='+ pathName2+ '&pathName3='+ pathName3+'&contentId='+id;
		 },
	     editStatus:function(status,contentId){
	    	 var _this = this;
	    	 syncData(ykyUrl.info + '/v1/news/' + contentId + '/' + status, 'PUT', null, function(data){
	    		 if(data == ''){
	    			 if(status=='PUBLISHED'){
	    				 layer.msg("发布成功！")
	    				 setTimeout(function(){
	    					 _this.getData();
	    				 },1000)
	        			 
	    			 }
	    			 if(status=='HOLD'){
	    				 layer.msg("停用成功！")
	        			 setTimeout(function(){
	        				 _this.getData();
	    				 },1000)
	    			 }
	    		 }
	    	 })	    	 
	     },
	     deleteContent:function(contentId,status){
	    	 var _this = this;
	    	 layer.alert("确定删除？删除后不可恢复哦~",{
				 offset: "auto",
					btn: ['确      认','取      消'], //按钮
					title: " ",	
					area: ['350px'],
					maxWidth:'500px',
					move: false,
					skin: "up_skin_class",
					closeBtn: false,
					yes:function(){
						layer.closeAll();
						syncData(ykyUrl.info + '/v1/news/' + contentId, 'DELETE', null, function(data){
							_this.getData();
						})
					}								
			 })
	    	 
	     },
	     preveiwContent:function(ele){
	    	 window.open(ykyUrl.portal+'/help/'+ele.categoryId+'.htm?newsId='+ele.newsId);
	     }
	},
	
})
maintain.menuData = maintain.initMenuData();
maintain.getData();
