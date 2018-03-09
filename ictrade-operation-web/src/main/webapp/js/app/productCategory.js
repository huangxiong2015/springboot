/**
 * create by roy.he@yikuyi.com at 2017/10/25
 */
//var database = ykyUrl.database + '/v1/menus';
var zTreeObj,zNodes,treeObj;
//var activeMenuId = localStorage.getItem('activeMenuId');


var setting = { //zTree配置
		data: {
			key: {
				name: "cateName"
			},
			simpleData: {
				enable: true,
				idKey: "_id",
				pIdKey: "parentId",
				rootPId: 0
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
			beforeClick: zTreeBeforeClick,//点击之前回调
			onClick: zTreeOnClick, //点击事件回调
			beforeDrag: zTreeBeforeDrag, //被拖拽之前的事件回调
			beforeDrop: zTreeBeforeDrop, //拽操作结束之前的事件回调
			onDrop: zTreeOnDrop //拖拽操作结束的事件回调
		}
	};
var pathList;

function zTreeBeforeClick(treeId, treeNode, clickFlag) {
	if(treeNode.getParentNode()&& treeNode.getParentNode().getParentNode() && treeNode.getParentNode().getParentNode().getParentNode()){
		return false;
	}
};

function zTreeOnClick(event, treeId, treeNode, clickFlag) {//点击事件回调
	maintain.editMenuDetail = [];
	maintain.clickMenu(treeNode._id, treeNode.level, treeNode.cateName, treeNode);
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
		categoryName: '',
		cateId:'',
		editMenuDetail:[],
		newContentBtn:false,
		editStatusTip:true,
		pathList:[],
		parent: {},
		status:1,
		fontColor:[],
		url: ykyUrl.product + '/v2/products/categories/children', //访问数据接口
		queryParams: { //请求接口参数
			defaultStatus: true, //监测参数变化标识
//			size: 10,
//			page: 1,
			parentCateId: '',
//			status: 1
		},
		gridColumns: [ //表格列
		               {
		   				key: 'cateName',
		   				name: '分类名称',
		   				align: 'center',
		   				default: '--',
		   				cutstring: true
		   			},
		   			{
		   				key: 'icon',
		   				name: '类别ICON',
		   				width: '120px',
		   				type: 'image',
		   				default: ykyUrl._this + '/images/defaultImg01.jpg'
		   			},
		   			{
		   				key: 'createdDate',
		   				name: '创建时间',
		   				align: 'center',
		   				default: '--',
		   				cutstring: true
		   			},
		   			{
		   				key: 'lastUpdateDate',
		   				name: '最后一次编辑时间',
		   				align: 'center',
		   				default: '--',
		   				cutstring: true
		   			},
		   			{
		   				key: 'operatedUserName',
		   				name: '操作人',
		   				align: 'center',
		   				default: '--',
		   				cutstring: true
		   			},
		   			{
						key: 'operate',
						name: '操作',
						align: 'center',
						width: '150px',
						items: [{
							className: 'btn-edit',
							show: true,
							text: '编辑',
							href: '#editCategory',
							callback: {
								action: 'editCategory',
								params: ['{status}', '{_id}', '{cateName}','{icon}','{cateLevel}','{style}']
							}
						},]
					}
		],
		pageflag: false, //是否显示分页
		refresh: false, //重载
		keyname:'上传',
        isImage: "true",
        key: "other",
        name: "other",
        validate:{},
        inputClass:'',
        attachFile:'',
        defaultPic: ykyUrl._this + '/images/defaultImg01.jpg',
        config: {						//设置上传按钮
            buttonId: "faceUploadBtn",
            types: "jpg,png,jpeg,gif",
            url: pluginURL,
            uploadType: "ent.logo",
            fileSize: "2mb"
        },
        attr:{
            disabled:true
        },
        manuText: '*支持jpg、jpeg、png、gif格式，大小不超过2M'
	},
	methods:{
		getData:function(){
			var _this = this;
			syncData(ykyUrl.product + '/v1/products/categories/list', 'GET', null, function(data){
				if(data){
					var rootParent = [{
							cateLevel: 0,
							cateName: '产品分类',
							_id: 0,
							children:[]
					}];
					rootParent[0].children = data;
//					data.push(rootParent);
					rootParent.forEach(function(item){
						if(item.children && item.children.length > 0){
							_this.filterData(item)
						}
					})
					
					zNodes = rootParent;
					zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
					treeObj = $.fn.zTree.getZTreeObj("treeDemo");
					var nodes = treeObj.getNodes();
					var curMenuId = _this.activeMenu._id;
					maintain.dataList = data;
					if (nodes.length > 0 && !curMenuId) {
						treeObj.selectNode(nodes[0]); //选中默认节点
						treeObj.expandNode(nodes[0]); //展开默认节点
						_this.pathList = treeObj.getSelectedNodes()[0].getPath();					
						maintain.clickMenu(nodes[0]._id, nodes[0].level, nodes[0].cateName, nodes[0]);
					} else {
						$.each(zNodes, function(i, e) {
							if (e._id == curMenuId) {
								var a_nodes = treeObj.getNodeByParam('_id', curMenuId);
								treeObj.selectNode(a_nodes); //选中指定节点
								treeObj.expandNode(a_nodes); //展开指定节点
								_this.pathList = treeObj.getSelectedNodes()[0].getPath();
								maintain.clickMenu(a_nodes._id, a_nodes.level, a_nodes.cateName, a_nodes);
							}
							
							$.each(e.children, function(j, node){
								if(node._id == curMenuId){
									var a_nodes = treeObj.getNodeByParam('_id', curMenuId);
									treeObj.selectNode(a_nodes); //选中指定节点
									treeObj.expandNode(a_nodes); //展开指定节点
									_this.pathList = treeObj.getSelectedNodes()[0].getPath();
									maintain.clickMenu(a_nodes._id, a_nodes.level, a_nodes.cateName, a_nodes);
								}
								
								$.each(node.children, function(k, snode){
									if(snode._id == curMenuId){
										var a_nodes = treeObj.getNodeByParam('_id', curMenuId);
										treeObj.selectNode(a_nodes); //选中指定节点
										treeObj.expandNode(a_nodes); //展开指定节点
										_this.pathList = treeObj.getSelectedNodes()[0].getPath();
										maintain.clickMenu(a_nodes._id, a_nodes.level, a_nodes.cateName, a_nodes)
									}
									
									$.each(snode.children, function(n, ssnode){
										if(ssnode._id == curMenuId){
											var a_nodes = treeObj.getNodeByParam('_id', curMenuId);
											treeObj.selectNode(a_nodes); //选中指定节点
											treeObj.expandNode(a_nodes); //展开指定节点
											_this.pathList = treeObj.getSelectedNodes()[0].getPath();
											maintain.clickMenu(a_nodes._id, a_nodes.level, a_nodes.cateName, a_nodes)
										}
									})
								})
							})
						})
					}
				}
			})
		},
		filterData: function(data){
			var _this = this;
			data.children.forEach(function(el){
				el.parentId = data._id;
				
				if(el.children && el.children.length > 0){
					_this.filterData(el)
				}
			})
		},
		clickMenu:function(id,level,name, node){//选中节点
			var _this = this;
			if (id || id == 0) {
				if(id == 0){
					maintain.queryParams.parentCateId = '';
					_this.parent = {};
				}else{
					maintain.queryParams.parentCateId = id;
					_this.parent = {
						_id: node._id,
						cateName: node.cateName,
						cateLevel: node.cateLevel
					};
					
					_this.activeMenu = {
						_id: node._id,
					};
				}
				_this.newContentBtn = false;
				_this.categoryName = '';
				$("#other").val('');
				$('input[name="isShowaColor"]').prop('checked', false);
				_this.status = 1;
				_this.attachFile = ykyUrl._this + '/images/defaultImg01.jpg';
				maintain.queryParams.defaultStatus = !maintain.queryParams.defaultStatus;
			}
		},
		addCategory: function(){
			$('#cateName').focus();
			this.newContentBtn = false;
			this.categoryName = '';
			$("#other").val('');
			$('input[name="isShowaColor"]').prop('checked', false);
			this.status = 1;
			this.attachFile = ykyUrl._this + '/images/defaultImg01.jpg';
			maintain.queryParams.defaultStatus = !maintain.queryParams.defaultStatus;
		},
		 saveData:function(e){//保存菜单
			 var _this = this;
			 var $dom = $(e.currentTarget);
			 if($dom.hasClass('disabled')){
				 return;
			 }
			 
			 if(!_this.categoryName){
				 layer.msg('分类名称不能为空！');
				 return false;
			 }
			 console.log(_this.parent);
			 if((!$('#other').val() && !_this.parent.cateLevel) || (!$('#other').val() && $('#other').val().indexOf('/images/defaultImg01.jpg') > -1)){
				 layer.msg('分类ICON不能为空！');
				 return false;
			 }
			 
			 $dom.addClass('disabled');
			 if(!_this.newContentBtn){
				 var curentLevel = _this.parent.cateLevel?_this.parent.cateLevel+1:1;
				 var postObj = {
					parent: _this.parent,
					icon: $("#other").val(),
					cateName: _this.categoryName,
					status:maintain.status,
					cateLevel:curentLevel,
					style:{
						fontColor:maintain.fontColor[0]
					}
				};
				 $.aAjax({
						url: ykyUrl.product + '/v1/products/categories',
						type: 'POST',
						data: JSON.stringify(postObj),
						contentType: 'application/json',
						dataType: 'json',
						success: function(res){
							$dom.removeClass('disabled');
							if(res != null){
								layer.msg('新增成功');
								_this.getData()
								_this.newContentBtn = false;
								$('#cateName').focus();
								_this.categoryName = '';
								$("#other").val('');
								_this.attachFile = '';
								_this.status = 1;
								$('input[name="isShowaColor"]').prop('checked', false);
							}else{
								layer.msg(res.errCode)
							}
						},
						error: function(xhr, textStatus, errorThrown){
							$dom.removeClass('disabled');
							if(xhr.status == 400){
								layer.msg('该分类名称已存在！')
							}
						}
					})
			 }else{
				 var postObj = {
					parent: _this.parent,
					_id: _this.cateId,
					icon: $("#other").val(),
					cateName: _this.categoryName,
					status:maintain.status,
					style:{
						fontColor:maintain.fontColor[0]
					}
				};
				
				$.aAjax({
					url: ykyUrl.product + '/v1/products/categories',
					type: 'PUT',
					data: JSON.stringify(postObj),
					contentType: 'application/json',
					dataType: 'json',
					success: function(res){
						$dom.removeClass('disabled');
						if(res != null){
							layer.msg('修改成功');
							_this.getData()
							_this.newContentBtn = false;
							$('#cateName').focus();
							_this.categoryName = '';
							$("#other").val('');
							_this.attachFile = '';
							_this.status = 1;
							$('input[name="isShowaColor"]').prop('checked', false);
						}else{
							layer.msg(res.errCode)
						}
					},
					error: function(xhr, textStatus, errorThrown){
						$dom.removeClass('disabled');
						if(xhr.status == 400){
							layer.msg('该分类名称已存在！')
						}
					}
				})
			 }
		 },
		cancelEdit: function(){
			this.categoryName = '';
			$("#other").val('');
			this.attachFile = ykyUrl._this + '/images/defaultImg01.jpg';
			maintain.queryParams.defaultStatus = !maintain.queryParams.defaultStatus;
			this.newContentBtn = false;
			this.status = 1;
			$('input[name="isShowaColor"]').prop('checked', false);
		}
	},
})

function editCategory(index,ele){//新增、编辑菜单信息
//	var _this = this;
	maintain.newContentBtn = true;
	maintain.categoryName = ele[2];
	maintain.cateId = ele[1];
	maintain.cateLevel = ele[4];
	maintain.status = ele[0];
	//maintain.style = ele[5];
	console.log(ele);
	if(ele[5] && ele[5].fontColor){
		maintain.fontColor = ['red'];
		$('input[name="isShowaColor"]').prop('checked', true);
	}else{
		maintain.fontColor = [];
		$('input[name="isShowaColor"]').prop('checked', false);
	}
	if(ele[3]){
		maintain.attachFile = ele[3];
	}else{
		maintain.attachFile = '';
	}
 }
maintain.getData();
