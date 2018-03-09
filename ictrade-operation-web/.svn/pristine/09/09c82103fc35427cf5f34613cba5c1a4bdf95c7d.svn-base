var zTree;
var activeMenuId = getQueryString("activeMenuId");
var menuTypeId = getQueryString("menuTypeID");
var setting = {
	data: {
		key : {
			name : "menuName"
		},
		simpleData: {
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
	callback: {
		onClick:zTreeOnClick,//点击事件回调
   		beforeDrag: zTreeBeforeDrag,//被拖拽之前的事件回调  
   		beforeDrop:zTreeBeforeDrop,//拽操作结束之前的事件回调
   		onDrop: zTreeOnDrop//拖拽操作结束的事件回调
	}
};

function zTreeOnClick(event, treeId, treeNode, clickFlag){ 
	clickMenu(treeNode.menuId,treeNode.level,treeNode);
	vm.editStatusTip = true;
	 pathList = treeNode.getPath();
	 getPathList(pathList);
}
function zTreeBeforeDrag(treeId, treeNodes){
	$("#menuName").blur();
	vm.editMenuDetail = [];
	clickMenu(treeNodes[0].menuId,treeNodes[0].level,treeNodes);
	vm.editStatusTip = true;
	pathList = treeNodes[0].getPath();
	getPathList(pathList);
}
function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType){
	if(treeNodes[0].level!==targetNode.level){
		return false;
	}
	if(treeNodes[0].level==targetNode.level && treeNodes[0].parentMenuId !== targetNode.parentMenuId){
		return false; 
	}   	
}
function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType){
	vm.menuData.menuId = vm.activeMenu.menuId;
	vm.menuData.menuName = vm.activeMenu.menuName;
	vm.menuData.menuTypeId = vm.activeMenu.menuTypeId;
	vm.menuData.menuUrl = vm.activeMenu.menuUrl;
	vm.menuData.orderSeq = targetNode?targetNode.orderSeq:vm.activeMenu.orderSeq;
	vm.menuData.menuStatus = 'ACTIVE';
	vm.menuData.styleTypeId = vm.activeMenu.styleTypeId;
	vm.menuData.parentMenuId = vm.activeMenu.parentMenuId;
	$.aAjax({
		url: ykyUrl.database + '/v1/menus/' + vm.menuData.menuId,
		type: 'PUT',
		data: JSON.stringify(vm.menuData),
		success:function(data){
			if(sessionStorage.getItem("operationMunus")){
				 sessionStorage.removeItem('operationMunus');
				 $.AdminLTE.layout.fix();
			 }
			location.href =  thisPrefix+'/menuMaintainList.htm?menuTypeId='+menuTypeId+'&activeMenuId='+vm.menuData.menuId;
		},
		error:function(data){
			console.log("error");
		}
	})
} 

var pathList;
function getPathList(list){//面包屑数据填充
	if(pathList[0]){
		 $("#pathName0").text(pathList[0].menuName);
		 $("#pathName0").prev().removeClass("dn");
	 }else{
		 $("#pathName0").text("");
		 $("#pathName0").prev().addClass("dn");
	 }
	if(pathList[1]){
		 $("#pathName1").text(pathList[1].menuName);
		 $("#pathName1").prev().removeClass("dn");
	 }else{
		 $("#pathName1").text("");
		 $("#pathName1").prev().addClass("dn");
	 }
	 if(pathList[2]){
		 $("#pathName2").text(pathList[2].menuName);
		 $("#pathName2").prev().removeClass("dn");
	 }else{
		 $("#pathName2").text("");
		 $("#pathName2").prev().addClass("dn");
	 }
	 if(pathList[3]){
		 $("#pathName3").text(pathList[3].menuName);
		 $("#pathName3").prev().removeClass("dn");
	 }else{
		 $("#pathName3").text("");
		 $("#pathName3").prev().addClass("dn");
	 }
}

var zTreeObj,zNodes,treeObj;
var rootNode = {  //根菜单，数据库内不存在这个
  "createdDate" : "",
  "creator" : "",
  "lastUpdateDate" : "",
  "lastUpdateUser" : "",
  "menuId" : "",
  "menuName" : "根菜单",
  "menuUrl" : "",
  "menuTypeId" : "",
  "styleTypeId" : "",
  "orderSeq" : 1,
  "menuStatus" : "",
  "menuLogoUrl" : ""
}
var  initData = function(){
	$.aAjax({
		 url:menuTypeId?ykyUrl.database +'/v1/menus/list?menuTypeId='+menuTypeId:ykyUrl.database +'/v1/menus/list',
		 type:'GET',
		 success:function(data){
			 zNodes = data;
			 rootNode.menuName = getQueryString("menuTypeId")?getQueryString("menuTypeId"):'根菜单';
			 zNodes.push(rootNode); //增加根菜单，数据库内不存在这个
			 vm.$data.dataList = zNodes;
			 zTreeObj = $.fn.zTree.init($("#ztree"), setting, zNodes);
			 treeObj = $.fn.zTree.getZTreeObj("ztree");
			 var nodes = treeObj.getNodes();
			 if (nodes.length>0 && !activeMenuId){
			 	treeObj.selectNode(nodes[0]);//选中默认节点
			 	treeObj.expandNode(nodes[0]);//展开默认节点
			 	clickMenu(nodes[0].menuId,nodes[0].level,nodes[0]);
			 	}else{
				 	$.each(zNodes,function(i,e){
				 		if(e.menuId == activeMenuId){
				 			var a_nodes = treeObj.getNodeByParam('menuId',activeMenuId);
				 			treeObj.selectNode(a_nodes);//选中指定节点
				 			treeObj.expandNode(a_nodes);//展开指定节点
				 			pathList = a_nodes.getPath();
				 			getPathList(pathList); /*更新右边面包屑*/
				 			clickMenu(a_nodes.menuId,a_nodes.level,a_nodes);
				 		}
				 	})
				 }
			 vm.menuData.menuTypeId = menuTypeId;
		 },
		 error:function(data){
			 console.log('error');
		 }
	})
}

var vm = new Vue({
	el: '#contArea',
	data: {
	  dataList:[],
	  childrenList:[],
	  contentList:[],
	  activeMenu:[],
	  menuData:{
		"menuId": "",        
        "menuName": "",
        "orderSeq": "1",
        "menuUrl": "",
        "menuStatus": "ACTIVE",
        "menuTypeId": "",
        "styleTypeId": "SLIDER",
        "parentMenuId": "",
        "menuLogoUrl": ""
	  },
	  editMenuDetail:[],
	  editStatusTip:true,
	  roleList:[]
	},
	methods: {
	    deleteMenu:function(event){
	      var _thisDom = $(event.target);
	      var menuId =_thisDom[0].dataset.menuid;
	      /*删除*/
	      var menuNode = treeObj.getNodeByParam('menuId',menuId);
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
							console.log("删除成功")
							 $.aAjax({
								 url:ykyUrl.database +'/v1/menus/'+ menuId,
								 type:"DELETE",
								 success:function(data){
									 //location.reload();
									 if(sessionStorage.getItem("operationMunus")){
										 sessionStorage.removeItem('operationMunus');
										 $.AdminLTE.layout.fix();
									 }
									 location.href =  thisPrefix+'/menuMaintainList.htm?menuTypeId='+menuTypeId+'&activeMenuId='+ vm.activeMenu.menuId;
								 },
								 error:function(data){
									 console.log("error");
								 }
							 })
						}
				 })
			 }
	      /*删除*/
	    },
	    editMenu:function(ele,event){//新增、编辑菜单信息
			 if(ele.menuName){//编辑
				 vm.editStatusTip = false;
				 vm.menuData.menuId = ele.menuId;
				 vm.menuData.menuName = ele.menuName;
				 vm.menuData.orderSeq = ele.orderSeq;
				 vm.menuData.menuStatus = ele.menuStatus;
				 vm.menuData.styleTypeId = ele.styleTypeId;
				 vm.menuData.menuTypeId = ele.menuTypeId;
				 vm.menuData.menuUrl = ele.menuUrl;
				 vm.menuData.parentMenuId = ele.parentMenuId?ele.parentMenuId:vm.activeMenu.menuId;
				 vm.menuData.menuLogoUrl = ele.menuLogoUrl;	
				 $("#menuIdItem").hide();
				 layer.open({
						type: 1,
						title:'编辑菜单',
					    area: '700px',
					    offset: '150px',
					    move:false,
					    skin:"s_select_data",
					    content:$(".edit_cont")
					    ,btn: ['保存', '取消']
					    ,yes: function(index, layero){
					    	vm.menuData.menuName = $.trim(vm.menuData.menuName);
					    	vm.menuData.menuId = $.trim(vm.menuData.menuId);
							 if(vm.menuData.menuName&&vm.menuData.menuId){
								 $.aAjax({
									 url: ykyUrl.database + '/v1/menus/' +vm.menuData.menuId,
									 type: 'PUT',
									 data: JSON.stringify(vm.menuData),
									 success:function(data){
										layer.close(index);
										if(sessionStorage.getItem("operationMunus")){
											 sessionStorage.removeItem('operationMunus');
											 $.AdminLTE.layout.fix();
										 }
										location.href = thisPrefix+'/menuMaintainList.htm?menuTypeId='+menuTypeId;			 
									 },
									 error:function(data){
										 console.log("error");
									 }
								 })
							 }	
					   }
					   ,cancel: function(index, layero){
						   
					  }
					})
				 $("#menuName").focus();
			 }else if(vm.editMenuDetail.menuName){
				 $("#menuName").focus();
				 vm.editStatusTip = false;
				 vm.menuData.menuId = vm.editMenuDetail.menuId;
				 vm.menuData.menuName = vm.editMenuDetail.menuName;
				 vm.menuData.orderSeq = vm.editMenuDetail.orderSeq;
				 vm.menuData.menuStatus = vm.editMenuDetail.menuStatus;
				 vm.menuData.styleTypeId = vm.editMenuDetail.styleTypeId;
				 vm.menuData.menuTypeId = vm.editMenuDetail.menuTypeId;
				 vm.menuData.menuUrl = vm.editMenuDetail.menuUrl;
				 vm.menuData.parentMenuId = vm.editMenuDetail.parentMenuId?vm.editMenuDetail.parentMenuId:vm.activeMenu.menuId;
				 vm.menuData.menuLogoUrl = vm.editMenuDetail.menuLogoUrl;
				 $("#menuIdItem").show();
				 layer.open({
						type: 1,
						title:'编辑菜单',
					    area: '700px',
					    offset: '150px',
					    move:false,
					    skin:"s_select_data",
					    content:$(".edit_cont")
					    ,btn: ['保存', '取消']
					    ,yes: function(index, layero){
					    	vm.menuData.menuName = $.trim(vm.menuData.menuName);
					    	vm.menuData.menuId = $.trim(vm.menuData.menuId);
							 if(vm.menuData.menuName&&vm.menuData.menuId){
								 $.aAjax({
									 url: ykyUrl.database + '/v1/menus/' +vm.menuData.menuId,
									 type: 'PUT',
									 data: JSON.stringify(vm.menuData),
									 success:function(data){
										layer.close(index);
										if(sessionStorage.getItem("operationMunus")){
											 sessionStorage.removeItem('operationMunus');
											 $.AdminLTE.layout.fix();
										 }
										 if(vm.activeMenu.menuId){
											 location.href = thisPrefix+'/menuMaintainList.htm?menuTypeId='+menuTypeId+'&activeMenuId='+ vm.activeMenu.menuId;
										 }else{
											 location.href = thisPrefix+'/menuMaintainList.htm?menuTypeId='+menuTypeId;
										 }				 
									 },
									 error:function(data){
										 console.log("error");
									 }
								 })
							 }	
					   }
					   ,cancel: function(index, layero){
						   
					  }
					})
				 $("#menuName").focus();
			 }else{//新增
				 var lengthTemp =  vm.childrenList.length;
				 vm.editStatusTip = true;
				 vm.menuData.menuId = "";
				 vm.menuData.menuName = "";
				 vm.menuData.orderSeq = vm.childrenList.length ? vm.childrenList[lengthTemp-1].orderSeq + 1 : 1;
				 vm.menuData.menuStatus = 'ACTIVE';
				 vm.menuData.menuUrl = "";
				 vm.menuData.parentMenuId = vm.activeMenu.menuId;
				 $("#menuIdItem").show();
				 layer.open({
						type: 1,
						title:'新增菜单',
					    area: '700px',
					    offset: '150px',
					    move:false,
					    skin:"s_select_data",
					    content:$(".edit_cont")
					    ,btn: ['保存', '取消']
					    ,yes: function(index, layero){
					    	vm.menuData.menuName = $.trim(vm.menuData.menuName);
					    	vm.menuData.menuId = $.trim(vm.menuData.menuId);
							 if(vm.menuData.menuName&&vm.menuData.menuId){
								 $.aAjax({
									 url: ykyUrl.database+ '/v1/menus',
									 type: 'POST',
									 data: JSON.stringify(vm.menuData),
									 success:function(data){
										layer.close(index);
										if(sessionStorage.getItem("operationMunus")){
											 sessionStorage.removeItem('operationMunus');
											 $.AdminLTE.layout.fix();
										 }
										 if(vm.activeMenu.menuId){
											 location.href = thisPrefix+'/menuMaintainList.htm?menuTypeId='+menuTypeId+'&activeMenuId='+ vm.activeMenu.menuId;
										 }else{
											 location.href = thisPrefix+'/menuMaintainList.htm?menuTypeId='+menuTypeId;
										 }					 
									 },
									 error:function(e){
										 layer.close(index);
										 layer.msg(JSON.parse(e.responseText).errMsg, {icon: 2});
										 console.log("error");
									 }
								 })
							 }	
					   }
					   ,cancel: function(index, layero){
						   
					  }
					})
					$("#menuName").focus();	 
			 }
			 
		 },
	    roleManager:function(event){
	    	var _thisDom = $(event.target);
		    var menuId =_thisDom[0].dataset.menuid;
	    	getRolelist(menuId);
	    	var thisLayer = layer.open({
	  			type: 1,
			  	title: "角色管理", //不显示标题
				shade: 0.5,
				btn: ['确定', '取消'],
				offset:"100px",
				area: ['500px', '400px'],
				skin:"up_skin_class",
	  			content: $('.role_cont'), //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
	  			cancel: function(){
	  				vm.roleList = tempRoleList;
	  			},
	  			yes:function(index, layero){
	  				layer.close(layer.index);
	  				saveRole(menuId);
	  			}
			});
	    }
	  }
	})
vm.$watch('menuData.menuId', function (newVal, oldVal) {
	  // 限制只能输入数字
		if(newVal!==oldVal){
			vm.menuData.menuId = newVal.replace(/[^\d]/g,'');
		}
	})
function clickMenu(id,level,node){
	  vm.childrenList = [];
	  vm.contentList = [];
	  vm.menuData.menuTypeId = node.menuTypeId?node.menuTypeId:menuTypeId;
		$.each(vm.dataList,function(idx,ele){
			if(ele.parentMenuId == id){
				ele.level = Number(level)+1;
				vm.childrenList.push(ele)
			}
			if(ele.menuId == id){
				ele.level = level;
				vm.activeMenu = ele;
			}
		})
}

var tempRoleList; //暂存role数据，如果弹窗时不保存，恢复到vm.roleList
function getRolelist(id){
	$.aAjax({
		 url: ykyUrl.database+ '/v1/menus/'+id+'/menurole',
		 type: 'GET',
		 success:function(data){
			 if(data.length>0){
				 vm.roleList = data;
				 tempRoleList = data;
			 }				 
		 },
		 error:function(data){
			 console.log("error");
		 }
	 })
}
function saveRole(id){
	var roleIdsArray = [];
	$(".role_cont input[type='checkbox']:checked").each(function(i){  
        var roleId = $(this).val();
        roleIdsArray.push(roleId);
    });  
	var params={
		"roleIdList":roleIdsArray
	}
	$.aAjax({
		 url: ykyUrl.database+ '/v1/menus/'+id+'/saverole',
		 type: 'PUT',
		 data: JSON.stringify(params),
		 success:function(data){
			 if(data=='SUCCESS'){
				 if(sessionStorage.getItem("operationMunus")){
					 sessionStorage.removeItem('operationMunus');
					 $.AdminLTE.layout.fix();
				 }
				 layer.msg("<div style='text-align:center;padding-top:30px;'><i class='icon-right-c'></i>修改菜单角色配置成功 ！</div>", {
	    			  time: 1000,
	    			  area:['506px','140px'],
	    			  skin:"fly_skin_class"      
	    			})
//	    		setTimeout(function(){window.location.reload();},2000); 
			 }	
		 },
		 error:function(data){
			 console.log("error");
		 }
	 })
}
initData();
