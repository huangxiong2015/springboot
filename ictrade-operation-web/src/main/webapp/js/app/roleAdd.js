/**
 * 新增角色和更新角色
 * create by roy.he@yikuyi.com
 * time: 2017-05-10
 */
var vm = new Vue({
	el: '#roleAdd',
	data: {
		initData: {
			name: '',
		},
		partyList: [],
		queryId: getQueryString('id'),
		deptIdList: [],
		menuList: [],
		menuData: {
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
	},
	methods: {
		/**
		 * 返回角色列表
		 */
		cancel: function() {
			location.href = ykyUrl._this + '/role.htm'
		},
		/**
		 * 保存数据
		 * 1. 存在vm.queryId时，更新数据
		 * @param {[string]} name {角色名称}
		 * @param {[string]} deptId {所属部门}
		 * @param {[number]} id {角色ID}
		 *
		 * 2. vm.queryId不存在时，新增数据
		 * @param {[string]} name {角色名称}
		 * @param {[string]} deptId {所属部门}
		 */
		saveData: function() {
			var postObj = {};
			if ($.trim($("#name").val()) === '') { //检查角色名称是否为空
				layer.msg('角色名称不能为空！')
				return false
			} else {
				postObj.name = $("#name").val();
			}

			if ($("#deptId").find('input[type="checkbox"]:checked').length === 0) { //检查所属部门是否选择
				layer.msg('请选择所属部门！')
				return false
			} else {
				postObj.deptVoList = [];
				$("#deptId").find('input[type="checkbox"]:checked').each(function(){
					var deptObj = {};
					deptObj.id = $(this).val();
					postObj.deptVoList.push(deptObj);
				})
			}

			if (vm.queryId) {
				//修改角色信息
				postObj.id = vm.queryId;
				syncData(ykyUrl.party + "/v1/dept/role", 'PUT', postObj, function(res,
					err) {
					if (!err) {
						location.href = location.origin + location.pathname;
					}
				});
			} else {
				//新增角色
				syncData(ykyUrl.party + "/v1/dept/role", 'POST', postObj, function(res,
					err) {
					if (!err) {
						location.href = location.origin + location.pathname;
					}
				});
			}
		}
	}
})

/**
 * 获取角色拥有的菜单列表
 * @return {[menuList]} [菜单列表]
 */
function getMenuList() {
	syncData(ykyUrl.database + "/v1/menus/" + vm.queryId + "/rolemenuall", 'GET',
		null,
		function(res, err) { //页面加载前调用方法
			if (!err) {
				vm.menuList = res;
			}
		});
}

/**
 * 获取所有一级部门列表
 * @return {[partyList]} [一级部门列表]
 */
function getPartyList(){
	syncData(ykyUrl.party + '/v1/dept/sonlist?parentId=99999999', 'GET', null, function(res, err){
		if(!err){
			vm.partyList = res;
			if(vm.deptIdList && vm.deptIdList.length > 0){
				for(var i = 0; i < vm.deptIdList.length; i++){
					for(var j = 0; j < vm.partyList.length; j++){
						if(vm.deptIdList[i].id === vm.partyList[j].id){
							vm.partyList[j].checked = true;
						}
					}
				}
			}
		}
	})
}

/**
 * 更新时，获取初始化数据
 * @param  {[id]} vm [角色id]
 * @return {[initData]}    [初始化数据]
 */
if (vm.queryId) {
	syncData(ykyUrl.party + "/v1/dept/role/detail", 'GET', {
		id: vm.queryId
	}, function(res, err) { //页面加载前调用方法
		if (!err) {
			vm.initData.name = res.name;
			vm.selectedValue = res.deptId;
			vm.deptIdList = res.deptVoList;
			getMenuList();
			getPartyList();
		}
	});
}else{
	getPartyList();
}
