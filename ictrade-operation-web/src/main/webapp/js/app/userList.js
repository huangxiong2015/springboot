/**
 * 用户列表
 * create by roy.he@yikuyi.com
 * time: 2017-05-11 15:30
 */

var database = ykyUrl.party + '/v1/dept';
var zTreeObj, zNodes, treeObj;
var activeMenuId = getQueryString("activeMenuId");
var validate;

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
/**
 * 面包屑数据填充
 * @param  {[array]} list [部门列表]
 * @return {[string]}      [部门名称]
 */
function getPathList(list) { //面包屑数据填充
	if (pathList[0]) {
		$("#pathName0").text(pathList[0].name);
	} else {
		$("#pathName0").text('');
	}

	if (pathList[1]) {
		$("#pathName1").text(pathList[1].name);
		$("#pathName1").prev().show();
	} else {
		$("#pathName1").text("");
		$("#pathName1").prev().hide();
	}
	if (pathList[2]) {
		$("#pathName2").text(pathList[2].name);
		$("#pathName2").prev().show();
	} else {
		$("#pathName2").text("");
		$("#pathName2").prev().hide();
	}
	if (pathList[3]) {
		$("#pathName3").text(pathList[3].name);
		$("#pathName3").prev().show();
	} else {
		$("#pathName3").text("");
		$("#pathName3").prev().hide();
	}
}

function zTreeOnClick(event, treeId, treeNode, clickFlag) { //点击事件回调
	maintain.menuData = menuTep;
	maintain.editMenuDetail = [];
	maintain.clickMenu(treeNode.id, treeNode.level, treeNode.name);
	maintain.editStatusTip = true;
	pathList = treeNode.getPath();
	getPathList(pathList);
};

function zTreeBeforeDrag(treeId, treeNodes) { //被拖拽之前的事件回调
	return false;
}

function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) { //拽操作结束之前的事件回调
	return false;
}

function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) { //拖拽操作结束的事件回调
	return false;
};

var menuTep = {
	"id": "",
	"name": "",
	"orderSeq": "1",
	"menuUrl": "",
	"menuStatus": "",
	"menuTypeId": "HELP_CENTER",
	"styleTypeId": "",
	"parentId": "",
	"menuLogoUrl": "i_user"
};

var maintain = new Vue({
	el: "#userList",
	data: {
		dataList: [],
		childrenList: [],
		contentList: [],
		activeMenu: [],
		menuData: {},
		editMenuDetail: [],
		newContentBtn: false,
		editStatusTip: false,
		emptyValid: false,
		parentName: '',
		url: ykyUrl.party + '/v1/dept/findCustomerByDeptId', //访问数据接口
		queryParams: { //请求接口参数
			defaultStatus: true, //监测参数变化标识
			size: 10,
			page: 1,
			deptId: '',
		},
		gridColumns: [ //表格列
			{
				key: 'index',
				name: '编号',
				align: 'center',
				width: '50px',
			}, {
				key: 'userLoginId',
				name: '邮箱',
				align: 'center',
				default: '--',
				width: '180px',
				cutstring: true
			}, {
				key: 'partyAccount',
				name: '姓名',
				align: 'center',
				default: '--',
				width: '80px',
				cutstring: true
			}, {
				key: 'gender',
				name: '性别',
				align: 'center',
				default: '--',
				width: '50px',
				text: {
					0: {
						'value': '男',
					},
					1: {
						'value': '女',
					}
				}
			}, {
				key: 'roleTypeName',
				name: '拥有角色',
				align: 'center',
				default: '--',
				width: '180px',
				cutstring: true
			}, {
				key: 'deptName',
				name: '所属部门',
				align: 'center',
				default: '--',
				width: '120px',
				cutstring: true
			}, {
				key: 'phone',
				name: '手机号码',
				align: 'center',
				default: '--',
				width: '150px',
				cutstring: true
			}, {
				key: 'createdDate',
				name: '创建时间',
				align: 'center',
				default: '--',
				width: '180px',
				cutstring: true
			}, {
				key: 'creatorAccount',
				name: '操作人',
				align: 'center',
				default: '--',
				width: '80px',
				cutstring: true
			}, {
				key: 'statueId',
				align: 'center',
				name: '状态',
				width: '50px',
				text: {
					PARTY_ENABLED: {
						'color': 'green',
						'value': '启用',
					},
					PARTY_DISABLED: {
						'color': 'red',
						'value': '停用'
					}
				}
			}, {
				key: 'operate',
				name: '操作',
				align: 'center',
				width: '150px',
				items: [{
					className: 'btn-delete',
					show: "'{statueId}'=='PARTY_ENABLED'",
					text: '停用',
					callback: {
						confirm: {
							title: '停用',
							content: '确认停用？'
						},
						action: 'doChangeStatus',
						params: ['{partyId}', 'PARTY_DISABLED']
					}
				}, {
					className: 'btn-delete',
					show: "'{statueId}'=='PARTY_DISABLED'",
					text: '启用',
					callback: {
						confirm: {
							title: '生效',
							content: '确认生效？'
						},
						action: 'doChangeStatus',
						params: ['{partyId}', 'PARTY_ENABLED']
					}
				}, {
					className: 'btn-edit',
					show: true,
					text: '编辑',
					href: ykyUrl._this + '/user.htm?action=editUser&id={partyId}',
				}, {
					className: 'btn-delete',
					show: true,
					text: '重置密码',
					callback: {
						action: 'doChangePassword',
						params: ['{partyId}']
					}
				}, ]
			}
		],
		pageflag: true, //是否显示分页
		refresh: false, //重载
	},
	methods: {
		/**
		 * 选中菜单
		 * @param  {[number]} id    [部门ID]
		 * @param  {[number]} level [层级]
		 * @param  {[string]} name  [部门名称]
		 */
		clickMenu: function(id, level, name) {
			maintain.childrenList = [];
			maintain.contentList = [];
			maintain.parentName = name;
			$.each(this.dataList, function(idx, ele) {
				if (ele.parentId == id) {
					ele.level = Number(level) + 1;
					maintain.childrenList.push(ele)
				}
				if (ele.id == id) {
					ele.level = level;
					maintain.activeMenu = ele;
				}
			})
			if (id) {
				maintain.queryParams.page = 1;
				maintain.queryParams.size = 10;
				maintain.queryParams.deptId = id;
				maintain.queryParams.defaultStatus = !maintain.queryParams.defaultStatus;
			}
		}
	}
});

/**
 * 停用、启用用户
 * @param  {[number]} index  [序号]
 * @param  {[array]} params [参数：{用户ID，修改后的状态}]
 * @return {[array]}        [新的部门列表]
 */
function doChangeStatus(index, params) {
	syncData(ykyUrl.party + "/v1/customers/updateStateId?partyId=" + params[0] +
		'&statusId=' + params[1], 'POST',
		null,
		function(res, err) { //页面加载前调用方法
			window.setTimeout(function() {
				maintain.refresh = !maintain.refresh; //重载
			}.bind(maintain), 400);
			layer.close(index);
		});
}

/**
 * base64加密用户id
 * @param  {[string]} mingwen [部门id]
 * @param  {[number]} times   [时间戳]
 * @return {[string]}         [base64码]
 */
function encodeBase64(mingwen, times) {
	var code = "";
	var num = 1;
	if (typeof times == 'undefined' || times == null || times == "") {
		num = 1;
	} else {
		var vt = times + "";
		num = parseInt(vt);
	}

	if (typeof mingwen == 'undefined' || mingwen == null || mingwen == "") {

	} else {
		$.base64.utf8encode = true;
		code = mingwen;
		for (var i = 0; i < num; i++) {
			code = $.base64.btoa(code);
		}
	}
	return code;
}

/**
 * 重置密码
 * @param  {[number]} index  [序号]
 * @param  {[array]} params [部门id]
 */
function doChangePassword(index, params) {
	$("#password").val('');
	$("#confirmPassword").val('');
	layer.open({
		type: 1,
		title: '重置密码',
		area: '700px',
		offset: '150px',
		move: false,
		skin: "s_select_data",
		content: $(".edit_cont"),
		btn: ['保存', '取消'],
		yes: function(index, layero) {
			var password = $.trim($("#password").val());
			if (password && password !== '') {
				$("#changePassword").submit();
				if (!validate) {
					return;
				}
				syncData(ykyUrl.party + '/v1/person/' + encodeBase64(params[0]) +
					'?passWord=' + password, 'PUT',
					null,
					function(res, err) { //页面加载前调用方法
						layer.close(index);
					});
			}
		},
		cancel: function(index, layero) {
			$("#password").val('');
			$("#confirmPassword").val('');
		}
	})
}

$("#changePassword").validate({ // jquery validate 校验
	rules: {
		password: {
			required: true,
			maxlength: 12,
			minlength: 6,
		},
		confirmPassword: {
			required: true,
			equalTo: '#password',
		}
	},
	messages: {
		password: {
			required: '密码不能为空',
			maxlength: '6-12位英文字母、符号或数字',
			minlength: '6-12位英文字母、符号或数字'
		},
		confirmPassword: {
			required: '确认密码不能为空',
			equalTo: '两次输入密码不一致'
		}
	},
	errorElement: "em",
	success: function(label) {
		$(label).parent().find('.tip').remove();
		$(label).removeClass("error icon-triangle-leftmin"); // speech-bubble speech-bubble-left

		$(label).addClass("icon-confirm");
		if ($(label).parent().find('input').val() === '') {
			$(label).removeClass("icon-confirm");
		}
	},
	errorPlacement: function(error, element) {
		var f = error[0].getAttribute("for");
		var ele = $("em[for=" + f + "]");
		if (ele.length > 0) {
			ele[0].remove();
		}
		element.parent().find('.tip').remove();
		error.appendTo(element.parent());
		$(error).addClass("error icon-triangle-leftmin");
	},
	submitHandler: function(form) { // 表单提交时修改校验状态
		validate = true;
	}
});


maintain.menuData = menuTep;
$.aAjax({
	url: database + '/list',
	type: 'GET',
	success: function(data) {
		zNodes = data;
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getNodes();
		maintain.dataList = data;
		if (nodes.length > 0 && !activeMenuId) {
			treeObj.selectNode(nodes[0]); //选中默认节点
			treeObj.expandNode(nodes[0]); //展开默认节点
			pathList = treeObj.getSelectedNodes()[0].getPath();
			getPathList(pathList);
			maintain.clickMenu(nodes[0].id, nodes[0].level, nodes[0].name);
		} else {
			$.each(zNodes, function(i, e) {
				if (e.id == activeMenuId) {
					var a_nodes = treeObj.getNodeByParam('id', activeMenuId);
					treeObj.selectNode(a_nodes); //选中指定节点
					treeObj.expandNode(a_nodes); //展开指定节点
					pathList = treeObj.getSelectedNodes()[0].getPath();
					getPathList(pathList);
					maintain.clickMenu(a_nodes.id, a_nodes.level, a_nodes.name);
				}
			})
		}
		//$(".load_complete").removeClass("dn");
	},
	error: function(data) {
		console.log('error');
	}
});
