var database = ykyUrl.database + '/v1/menus';
var zTreeObj,zNodes,treeObj;
var activeMenuId = localStorage.getItem('activeMenuId');


var setting = { //zTree配置
		data: {
			key: {
				name: "name"
			},
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "parentId",
				rootPId: -1
			}
		},
		edit: {
			enable: true,
			showRemoveBtn: false, //是否显示删除按钮
			showRenameBtn: false, //是否显示编辑名称按钮
			drag: {
				isCopy: false, //是否允许复制节点
				isMove: true, //是否允许移动节点
				prev: true,
				next: true,
				inner: false //是否允许成为目标节点的子节点
			}
		},
		callback: {
			onClick: zTreeOnClick, //点击事件回调
			beforeDrag: zTreeBeforeDrag, //被拖拽之前的事件回调
			beforeDrop: zTreeBeforeDrop, //拽操作结束之前的事件回调
			onDrop: zTreeOnDrop //拖拽操作结束的事件回调
		}
	};
var pathList;

function zTreeOnClick(event, treeId, treeNode, clickFlag) {//点击事件回调
	$("#menuName").blur();
	maintain.menuData = maintain.initMenuData();
	maintain.editMenuDetail = [];
	maintain.clickMenu(treeNode.id, treeNode.level, treeNode.name);
	maintain.editStatusTip = true;
	maintain.pathList = treeNode.getPath();
};
function zTreeBeforeDrag(treeId, treeNodes) {//被拖拽之前的事件回调  
	return false;
} 
function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {//拽操作结束之前的事件回调
	return false;    	         	
}
function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {//拖拽操作结束的事件回调
	return false;
};
	 
    

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
		pathList:[],
	},
	methods:{
		getData:function(){
			var _this = this;
			syncData(ykyUrl.party + '/v1/dept/list', 'GET', null, function(data){
				if(data){
					zNodes = data;
					zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
					treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var nodes = treeObj.getNodes();
					 var curMenuId = activeMenuId || _this.activeMenu.id;
					maintain.dataList = data;
					if (nodes.length > 0 && !curMenuId) {
						treeObj.selectNode(nodes[0]); //选中默认节点
						treeObj.expandNode(nodes[0]); //展开默认节点
						_this.pathList = treeObj.getSelectedNodes()[0].getPath();					
						maintain.clickMenu(nodes[0].id, nodes[0].level, nodes[0].name);
					} else {
						$.each(zNodes, function(i, e) {
							if (e.id == curMenuId) {
								var a_nodes = treeObj.getNodeByParam('id', curMenuId);
								treeObj.selectNode(a_nodes); //选中指定节点
								treeObj.expandNode(a_nodes); //展开指定节点
								_this.pathList = treeObj.getSelectedNodes()[0].getPath();
								maintain.clickMenu(a_nodes.id, a_nodes.level, a_nodes.name);
							}
						})
					}
					_this.menuData.name = '';
				}
			})
		},
		initMenuData:function(){
			return {
		        id: "",        
		        name: "",        
		        parentId: "",
		      }
		},
		clickMenu:function(id,level,name){//选中菜单
			var _this = this;
			_this.childrenList = [];
			_this.contentList = [];
			$.each(_this.dataList, function(idx, ele) {
					if (ele.parentId == id) {
						ele.level = Number(level) + 1;
						_this.childrenList.push(ele)
					}
					if (ele.id == id) {
						ele.level = level;
						_this.activeMenu = ele;
					}
				})
		},
		editMenu:function(ele){//新增、编辑菜单信息
			var _this = this;
			$("#menuName").blur();
			if (ele) {
				$("#menuName").focus();
				_this.editMenuDetail = ele;
				_this.focusNewMenu(ele);
			} else {
				$("#menuName").focus();
				_this.editMenuDetail = [];
				_this.focusNewMenu();
			}
		 },
		 focusNewMenu:function(ele){
			 var _this = this;
			 if (ele && ele.name) { //编辑
					$("#menuName").focus();
					_this.editStatusTip = false;
					_this.emptyValid = false;
					_this.menuData.id = ele.id;
					_this.menuData.name = ele.name;
					_this.menuData.orderSeq = ele.orderSeq;
					_this.menuData.menuStatus = ele.menuStatus;
					_this.menuData.styleTypeId = ele.styleTypeId;
					_this.menuData.parentId = ele.parentId ? ele.parentId : _this.activeMenu
						.id;
					_this.menuData.menuLogoUrl = ele.menuLogoUrl;
				} else if (_this.editMenuDetail.name) {
					$("#menuName").focus();
					_this.editStatusTip = false;
					_this.emptyValid = false;
					_this.menuData.id = _this.editMenuDetail.id;
					_this.menuData.name = _this.editMenuDetail.name;
					_this.menuData.orderSeq = _this.editMenuDetail.orderSeq;
					_this.menuData.menuStatus = _this.editMenuDetail.menuStatus;
					_this.menuData.styleTypeId = _this.editMenuDetail.styleTypeId;
					_this.menuData.parentId = _this.editMenuDetail.parentId ? _this.editMenuDetail
						.parentId : _this.activeMenu.id;
					_this.menuData.menuLogoUrl = _this.editMenuDetail.menuLogoUrl;
				} else { //新增
					$("#menuName").focus();
					_this.editStatusTip = true;
					_this.emptyValid = false;
					_this.menuData.id = "";
					_this.menuData.name = "";
					_this.menuData.orderSeq = _this.childrenList.length == 0 ? 1 :
						Number(_this.childrenList[_this.childrenList.length - 1].orderSeq) +
						1;
					_this.menuData.menuStatus = 'ACTIVE';
					_this.menuData.styleTypeId = "";
					_this.menuData.parentId = _this.activeMenu.id;
				}
		 },
		 saveData:function(){//保存菜单
			 var _this = this;
			 
			var postObj = {
				name: _this.menuData.name,
				parentId: _this.menuData.parentId,
			};
			if (_this.menuData.name) {
				//_this.editStatusTip = false;
				var flag = _this.menuData.id ? false : true;
				if (_this.menuData.id)
					postObj.id = _this.menuData.id;
				syncData(ykyUrl.party + '/v1/dept', flag ? 'POST' : 'PUT', postObj, function(data){
					if(data != null){
						_this.getData();
					}
				})
			} else {
				_this.emptyValid = true;
			}
		 }
	},
	
})
maintain.menuData = maintain.initMenuData();
maintain.getData();
