/**
 * 新增用户
 * create by roy.he@yikuyi.com
 * time: 2017-05-12 11:00
 */
var validate;
var vm = new Vue({
	el: '#roleAdd',
	data: {
		initData: {
			name: '',
			mail: '',
			telNumber: '',
			userLoginId: ''
		},
		queryId: getQueryString('id'),
		paramDeptId: getQueryString('deptId'),
		deptId: '',
		roleVo: [],
		getRoleId: '',
		selectId: '',
		selectedValue: [],
		menuData: {
			"id": "",
			"name": "",
			"orderSeq": "1",
			"menuUrl": "",
			"menuStatus": "ACTIVE",
			"menuTypeId": "",
			"styleTypeId": "SLIDER",
			"parentId": "",
			"menuLogoUrl": ""
		},
		action: getQueryString('action')
	},
	methods: {
		/**
		 * 返回用户列表
		 */
		cancel: function() {
			location.href = ykyUrl._this + '/user.htm'
		},
		/**
		 * 新增、编辑用户
		 * vm.queryId存在时，编辑用户
		 * @param {[string]} mail {邮箱}
		 * @param {[string]} sex {性别}
		 * @param {[string]} telNumber {手机号码}
		 * @param {[string]} deptId {部门ID}
		 * @param {[array]} roleList {角色集合}
		 * @param {[string]} id {用户ID}
		 * @param {[string]} manager {是否为主管} //EXECUTIVE_DIRECTOR: 是，  ENTERPRISE_CUST: 否
		 *
		 * vm.queryId不存在是，新增用户
		 * @param {[string]} mail {邮箱}
		 * @param {[string]} sex {性别}
		 * @param {[string]} telNumber {手机号码}
		 * @param {[string]} deptId {部门ID}
		 * @param {[array]} roleList {用户权限集合}
		 * @param {[string]} password {用户密码}
		 * @param {[string]} manager {是否为主管} //EXECUTIVE_DIRECTOR: 是，  ENTERPRISE_CUST: 否
		 *
		 * @return {[type]} [description]
		 */
		saveData: function() {
			var postObj = {};
			$("#addUser").submit();
			if (!validate) {
				return;
			}

			postObj.mail = $("#mail").val();
			postObj.sex = $('input[name="sex"]:checked').val();
			postObj.telNumber = $("#telNumber").val();
			postObj.deptId = vm.selectId;
			postObj.manager = $('input[name="manager"]').is(':checked') ?
				true : false;
			postObj.roleList = [];
			postObj.name = $("#name").val();

			if ($(".roleList").find('input[type=checkbox]:checked').length === 0) { //判断是否选择用户权限
				layer.msg('请选择用户权限！')
				return;
			} else {
				$(".roleList").find('input[type=checkbox]:checked').each(function() { //将选中数据放入roleList中
					postObj.roleList.push($(this).val());
				})
			}

			if (vm.queryId) {
				//编辑用户
				postObj.id = vm.queryId;
				syncData(ykyUrl.party + "/v1/person/updateuser", 'PUT', postObj,
					function(res, err) {
						if (!err) {
							layer.msg('保存成功！')
							setTimeout(function(){
								location.href = location.origin + location.pathname;
							},2000)
						}
					},false);
			} else {
				//新增用户
				postObj.password = $("#password").val();
				syncData(ykyUrl.party + "/v1/person/adduser", 'POST', postObj, function(
					res, err) {
					if (!err) {
						location.href = location.origin + location.pathname;
					}
				});
			}
		}
	}
})

/**
 * 获取部门列表
 * @return {[array]} [部门列表]
 */
var menuData = function() {
	syncData(ykyUrl.party + "/v1/dept/list", 'GET', null, function(res, err) { //页面加载前调用方法
		var resData = res;
		if (vm.selectId !== '') { //编辑时设置选中节点
			for (var i = 0; i < resData.length; i++) {
				if (resData[i].id === vm.selectId) {
					resData[i].checked = true;
				}
			}
		} else { //新增时将所有部门设置为不可选
			for (var i = 0; i < resData.length; i++) {
				if (resData[i].id === vm.paramDeptId) {
					resData[i].checked = true;
				}
//				resData[i].chkDisabled = true;
			}
			vm.selectId = vm.paramDeptId;
		}
		zNodes = resData;
		zTreeObj = $.fn.zTree.init($("#ztree"), setting, zNodes); //初始化树
		treeObj = $.fn.zTree.getZTreeObj("ztree");
		treeObj.expandAll(true); //树设置默认全部展示
		var sNodes = treeObj.getCheckedNodes(); //获取选中节点数据
		if (sNodes.length > 0) {
			getRoleList(sNodes)
		}
	});
} 

var zTree;
var setting = { //树相关配置
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
	view: {
		//selectedMulti:false//是否允许同时选中多个节点
		dblClickExpand: false,
		showLine: false
	},
	check: {
		chkboxType: {
			"Y": "ps",
			"N": "ps"
		},
		enable: true,
		chkStyle: "radio",
		radioType: "all"
	},
	callback: {
		onClick: zTreeOnClick, //点击事件回调
		beforeDrag: zTreeBeforeDrag, //被拖拽之前的事件回调
		beforeDrop: zTreeBeforeDrop, //拽操作结束之前的事件回调
		onDrop: zTreeOnDrop, //拖拽操作结束的事件回调
		onCheck: zTreeOnCheck,
		beforeCheck: zTreeBeforeCheck
	}
};
/**
 * 树选中事件
 * @param  {[type]} e        [事件]
 * @param  {[type]} treeId   [树id]
 * @param  {[type]} treeNode [树节点]
 */
function zTreeOnCheck(e, treeId, treeNode) {
	var sNodes = treeObj.getCheckedNodes();
	if (sNodes.length > 0) {
		getRoleList(sNodes)
	}
	vm.selectId = treeNode.id;
}
/**
 * 获取角色列表
 * @param  {[array]} nodes [节点数据]
 */
function getRoleList(nodes) {
	var node = nodes[0].getParentNode();
	if (!node) {
		if (vm.getRoleId !== nodes[0].id) {
			vm.getRoleId = nodes[0].id;
			syncData(ykyUrl.party + "/v1/dept/findRoleVoByDeptId?deptId=" + nodes[0].id,
				'GET', null,
				function(res, err) { //页面加载前调用方法
					if (!err) {
						vm.roleVo = res;
						if (vm.queryId !== '') {
							for (var i = 0; i < vm.roleVo.length; i++) { //设置角色列表默认选中
								for (var j = 0; j < vm.selectedValue.length; j++) {
									if (vm.roleVo[i].id === vm.selectedValue[j]) {
										vm.roleVo[i].check = true;
									}
								}
							}
						}
					}
				});
		}
	} else {
		var nodeArr = [];
		nodeArr.push(node);
		getRoleList(nodeArr);
	}
}

function zTreeBeforeCheck(treeId, treeNode) {
	if(vm.queryId !== ''){
		if(treeNode.getCheckStatus().checked){
			return false;
		}
	}else{
		layer.msg('部门不可编辑')
	    return false;
	}
};

function zTreeOnClick(e, treeId, treeNode) {
	treeObj.expandNode(treeNode);
}

function zTreeBeforeDrag(treeId, treeNodes) {
	return false;
}

function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {
	return false;
}

function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
	return false;
}

var zTreeObj, zNodes, treeObj;
$("#addUser").validate({ //jquery validate 校验
	rules: {
		mail: {
			required: true,
			addrMailCheck: [""]
		},
		password: {
			required: true,
			maxlength: 12,
			minlength: 6,
		},
		confirmPassword: {
			required: true,
			equalTo: '#password',
		},
		name: {
			required: true,
			maxlength: 50,
		},
	},
	messages: {
		mail: {
			required: '账户名称不能为空',
		},
		password: {
			required: '密码不能为空',
			maxlength: '6-12位英文字母、符号或数字',
			minlength: '6-12位英文字母、符号或数字'
		},
		confirmPassword: {
			required: '确认密码不能为空',
			equalTo: '两次输入密码不一致'
		},
		name: {
			required: '用户姓名不能为空',
			maxlength: '50字以内中英文字母、符号或数字'
		},
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
	submitHandler: function(form) { //表单提交是校验
		validate = true;
	}
});

if (vm.queryId) {
	// 编辑时，获取用户详情
	syncData(ykyUrl.party + "/v1/person/detail", 'GET', {
		partyId: vm.queryId
	}, function(res, err) { //页面加载前调用方法
		if (!err) {
			vm.initData.name = res.name;
			vm.initData.mail = res.mail;
			vm.initData.telNumber = res.telNumber;
			vm.initData.userLoginId = res.userLoginId;
			if (res.sex && res.sex === '1') {
				$('#sex').find('input[value=1]').prop('checked', true);
			} else {
				$('#sex').find('input[value=0]').prop('checked', true);
			}
			
			if(res.manager){
				$("#manager").prop('checked', true);
			}else{
				$("#manager").prop('checked', false);
			}

			vm.selectedValue = res.roleList;
			vm.selectId = res.deptId;
			menuData();
		}
	});
	$("#mail").rules('remove'); //移除mail校验规则
} else {
	$('#sex').find('input[value=0]').prop('checked', true); //默认选中男
	menuData();
	$("#mail").rules('add', { //添加mail是否已存在校验
		accountCheck: [""],
	});
}
