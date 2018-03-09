/**
 * 角色列表
 * create by roy.he@yikuyi.com
 * time: 2017-05-09
 */

var vm = new Vue({
    el: '#role-list',
    data: {
        url: ykyUrl.party + "/v1/dept/role/list", //访问数据接口
        queryParams: { //请求接口参数
            defaultStatus: true, //监测参数变化标识
            size: 10,
            page: 1,
        },
        gridColumns: [ //表格列
            {
                key: 'index',
                name: '编号',
                align: 'center',
                width: '50px',
            }, {
                key: 'name',
                name: '角色名称',
                align: 'center',
                default: '--',
                cutstring: true,
            }, {
                key: 'deptVoList',
                name: '所属部门',
                align: 'center',
                default: '--',
                cutstring: true,
                callback:{
                	action: formatDeptList,
                	params: ['{deptVoList}']
                }
            }, {
                key: 'createdDate',
                name: '创建时间',
                align: 'center',
                default: '--',
                cutstring: true
            }, {
                key: 'lastUpdateDate',
                name: '最后编辑时间',
                align: 'center',
                default: '--',
                cutstring: true
            }, {
                key: 'account',
                name: '操作人',
                align: 'center',
                default: '--',
                cutstring: true
            },
            //            {
            //				key : 'status',
            //				align : 'center',
            //				name : '状态',
            //            	width:'80px',
            //				text : {
            //					ENABLED : {
            //						'color' : 'green',
            //						'value' : '启用',
            //					},
            //					DISABLED : {
            //						'color' : 'red',
            //						'value' : '停用'
            //					}
            //				}
            //			},
            {
                key: 'operate',
                name: '操作',
                align: 'center',
                width: '150px',
                items: [

                    //				 {
                    //					className : 'btn-delete',
                    //					show : "'{status}'=='ENABLED'",
                    //					text : '停用',
                    //					callback : {
                    //						confirm : {
                    //							title : '停用',
                    //							content : '确认停用？'
                    //						},
                    //						action : 'doChangeStatus',
                    //						params : [ '{id}', 'DISABLED']
                    //					}
                    //				}, {
                    //					className : 'btn-delete',
                    //					show : "'{status}'=='DISABLED'",
                    //					text : '启用',
                    //					callback : {
                    //						confirm : {
                    //							title : '生效',
                    //							content : '确认生效？'
                    //						},
                    //						action : 'doChangeStatus',
                    //						params : [ '{id}', 'ENABLED']
                    //					}
                    //				},
                    {
                        className: 'btn-edit',
                        show: true,
                        text: '编辑',
                        href: ykyUrl._this +
                            '/role.htm?action=addRole&id={id}',
                    }
                ]
            }
        ],
        pageflag: true, //是否显示分页
        refresh: false, //重载
        selectUrl: ykyUrl.party + '/v1/dept/sonlist?parentId=99999999', //数据接口，必须
        id: 'id', //设置控件ID ，必须
        placeholder: {
            option: '全部'
        },
        name: 'name', //设置控件name ，必须
        inputClass: 'form-control', //设置下拉选项的class，可选
        styleObject: '', //设置style，可选
        selectedValue: '',
    },
    created() {
        var that = this;
        var qModel = parseUrlParam();
        for (var i in qModel) {
            that.queryParams[i] = qModel[i];
        }
        this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
    },
    methods: {
        /**
         * 搜索
         * @param {[string]} name [角色名称]
         * @param {[string]} deptId [所属部门]
         */
        goSearch: function() {
            if ($.trim($("#name").val()) !== '') {
                this.queryParams.name = $("#name").val();
            } else {
                if (this.queryParams.name) {
                    delete this.queryParams.name;
                }
            }

            if ($.trim($("#deptId select").val()) !== '') {
                this.queryParams.deptId = $("#deptId select").val();
            } else {
                if (this.queryParams.deptId) {
                    delete this.queryParams.deptId;
                }
            }

            //    		if($.trim($("#status").val()) !== ''){
            //    			this.queryParams.status = $("#status").val();
            //    		}else{
            //    			if(this.queryParams.status){
            //    				delete this.queryParams.status;
            //    			}
            //    		}
            this.queryParams.size = 10;
            this.queryParams.page = 1;
            this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
        }
    }
});
/**
 * 停用、启用角色，并刷新数据
 * @param  {[number]} index  [序号]
 * @param  {[string, string]} params [角色ID，角色状态]
 * @return {[Object]}        [角色列表]
 */
function doChangeStatus(index, params) {
    var postObj = {
        id: params[0],
        status: params[1]
    };
    syncData(ykyUrl.party + "/v1/dept/role/status", 'PUT',
        postObj,
        function(res, err) { //页面加载前调用方法
            window.setTimeout(function() {
                vm.refresh = !vm.refresh; //重载
            }.bind(vm), 400);
            layer.close(index);
        });
}

function formatDeptList(index,params){
	var str = '';
	for(var i = 0; i < params[0].length; i ++){
		if(i > 0){
			str +=', '+params[0][i].name
		}else{
			str = params[0][i].name
		}
	}
	return str
}
