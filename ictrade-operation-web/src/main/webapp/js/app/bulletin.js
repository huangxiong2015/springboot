var vm = '';
$(function() {

	function getFormData () {
	    var searchingFlag = sessionStorage.getItem('searchingFlag')
		var searchString = sessionStorage.getItem('searching')
        var result = {}

		if (searchString && searchingFlag) {
			try {
                result = JSON.parse(searchString)
			} catch (e) {}
		}

        sessionStorage.removeItem('searchingFlag')
        // sessionStorage.removeItem('searching')
        if (!searchingFlag) {
            sessionStorage.removeItem('searching')
        }
        result['categoryTypeIdStr']='NOTICE,INFORMATION,MEDIAREPORT,VENDORAFFAIRS';
		return result
	}

	vm = new Vue(
			{
				el : '#manufacturer-list',
				data : function () {
					return 	{
                        formData: {
                            url:'',    //form提交请求接口，可选
                            id:'seachForm',
                            data: getFormData(),            //初始化数据
                            columnCount: 4,
                            fields:[            //配置控件
                                {
                                    keyname: "标题",           //控件文本
                                    type: "text",             //控件类型
                                    key: "title",          //控件名
                                    name: "title"         //控件name
                                }, {
                                    keyname: "类型",           //控件文本
                                    type: "select",             //控件类型
                                    key: "categoryTypeIdStr",          //控件名
                                    name: "categoryTypeIdStr",         //控件name
                                    options: [{
                                        value: 'NOTICE,INFORMATION,MEDIAREPORT,VENDORAFFAIRS',
                                        text: '全部'
                                    },{
                                        value: 'NOTICE',
                                        text: '官方公告'
                                    },{
                                        value: 'INFORMATION',
                                        text: '新闻资讯'
                                    },{
                                        value: 'MEDIAREPORT',
                                        text: '媒体报道'
                                    },{
                                        value: 'VENDORAFFAIRS',
                                        text: '供应商动态'
                                    }
                                    ] 
                                }, {
                                    keyname: "状态",           //控件文本
                                    type: "select",             //控件类型
                                    key: "status",          //控件名
                                    name: "status",         //控件name
                                    options: [{
                                        value: '',
                                        text: '全部'
                                    },{
                                        value: 'DRAFT',
                                        text: '草稿'
                                    },{
                                        value: 'PUBLISHED',
                                        text: '已发布'
                                    },{
                                        value: 'HOLD',
                                        text: '停用'
                                    }]
                                }, {
                                    keyname: "推荐",           //控件文本
                                    type: "select",             //控件类型
                                    key: "recommend",          //控件名
                                    name: "recommend",         //控件name
                                    options: [{
                                        value: '',
                                        text: '不限'
                                    },{
                                        value: 'RECOMMENDED',
                                        text: '已推荐'
                                    }]
                                }
                            ],
                            buttons:[{
                                "text": '查询',
                                "type": 'submit',
                                "id": '',
                                "name": '',
                                "inputClass": 'btn btn-danger',
                                "callback": {
                                    action: 'on-search'
                                }
                            }]
                        },
                        url : ykyUrl.info + "/v1/news", //访问数据接口
                        queryParams : { //请求接口参数
                            pageSize : 10, //分页参数
                            page : 1, //当前页
                            categoryTypeIdStr : "",
                            status:'',
                            defaultStatus : true,
                            recommend:''
                            //监测参数变化标识
                        },
                        gridColumns : [ //表格列
                            {
                                key : 'newsId',
                                name : 'id',
                                hide : 'true'
                            }, {
                                key : 'index',
                                name : '序号',
                                align : 'center',
                                width: '150px',
                            }, {
                                key : 'title',
                                name : '标题',
                                cutstring: true,
                                callback:{
                                    action : 'showTitle',
                                    params : [ '{isTop}','{isTitleRed}','{recommendedDate}','{title}' ]
                                }
                            }, {
                                key : 'categoryTypeId',
                                align : 'center',
                                name : '类型',
                                cutstring: true,
                                text : {
                                    INFORMATION : {
                                        'color' : 'black',
                                        'value' : '新闻资讯'
                                    },
                                    NOTICE : {
                                        'color' : 'black',
                                        'value' : '官方公告'
                                    },
                                    MEDIAREPORT : {
                                        'color' : 'black',
                                        'value' : '媒体报道'
                                    },VENDORAFFAIRS : {
                                        'color' : 'black',
                                        'value' : '供应商动态'
                                    }
                                }
                            },{
                                key : 'lastUpdateDate',
                                align : 'center',
                                cutstring: true,
								default : '-',
                                name : '更新时间'
                            },{
                                key : 'publishDate',
                                align : 'center',
                                cutstring: true,
                                name : '发布时间',
                                callback:{
                                    action : 'showLastUpdateDate',
                                    params : [ '{status}','{publishDate}' ]
                                }
                            },{
                                key : 'recommendedDate',
                                align : 'center',
                                cutstring: true,
								default : '-',
                                name : '推荐时间'
                            },{
                                key : 'author',
                                align : 'center',
                                cutstring: true,
                                name : '创建人'
                            }, {
                                key : 'status',
                                align : 'center',
                                name : '状态',
                                align : 'center',
                                width: '120px',
                                text : {
                                    PUBLISHED : {
                                        'color' : 'green',
                                        'value' : '已发布'
                                    },
                                    DRAFT : {
                                        'color' : 'black',
                                        'value' : '草稿'
                                    },
                                    HOLD : {
                                        'color' : 'red',
                                        'value' : '停用'
                                    }
                                }
                            }, {
                                key : 'operate',
                                name : '操作',
                                align : 'center',
                                items : [ {
                                    className : 'btn-delete',
                                    show : "'{status}'=='PUBLISHED' && ('{recommendedDate}' == '' || '{recommendedDate}' == undefined)",
                                    text : '推荐',
                                    callback : {
                                        confirm : {
                                            title : '推荐',
                                            content : '确认推荐？'
                                        },
                                        action : 'doChangeStatus',
                                        params : [ '{newsId}', 'RECOMMENDED' ]
                                    }
                                },{
                                    className : 'btn-delete',
                                    show : "'{status}'=='PUBLISHED' && '{recommendedDate}' != ''",
                                    text : '取消推荐',
                                    callback : {
                                        confirm : {
                                            title : '取消推荐',
                                            content : '确认取消？'
                                        },
                                        action : 'doChangeStatus',
                                        params : [ '{newsId}', 'CANCELRECOMMEND' ]
                                    }
                                },{
                                    className : 'btn-detail',
                                    text : '预览',
                                    show : true,
                                    href : ykyUrl.portal + '/news/detail/{newsId}.htm',
                                    target : '_blank'
                                }, {
                                    className : 'btn-delete',
                                    show : "'{status}'=='PUBLISHED'",
                                    text : '停用',
                                    callback : {
                                        confirm : {
                                            title : '停用',
                                            content : '确认停用？'
                                        },
                                        action : 'doChangeStatus',
                                        params : [ '{newsId}', 'HOLD' ]
                                    }
                                }, {
                                    className : 'btn-delete',
                                    show : "'{status}'!='PUBLISHED'",
                                    text : '发布',
                                    callback : {
                                        confirm : {
                                            title : '发布',
                                            content : '确认发布？'
                                        },
                                        action : 'doChangeStatus',
                                        params : [ '{newsId}', 'PUBLISHED' ]
                                    }
                                }, {
                                    className : 'btn-edit',
                                    text : '编辑',
                                    show : " '{status}'!='PUBLISHED' ",
                                    href : operationWebUrl+'/bulletin/edit.htm?id={newsId}',
                                    target : '_blank'
                                }, {
                                    className : 'btn-delete',
                                    text : '删除',
                                    show : "'{status}'!='PUBLISHED'",
                                    callback : {
                                        confirm : {
                                            title : '删除',
                                            content : '确认删除？'
                                        },
                                        action : 'delFunc',
                                        params : [ '{newsId}' ]
                                    }
                                } ]
                            } ],
                        pageflag : true, //是否显示分页
                        refresh : false
                        //重载

                    }
				},
				mounted: function () {
					setTimeout(() => this.onSearchClick(this.formData.data), 500)
				},
				methods : {
                    onAddNewClick: function (e) {
                        sessionStorage.setItem('searchingFlag', '1')
                    },
                    onSearchClick: function (formdata) {
						var queryParams = this.queryParams
                        queryParams.pageSize = 10;
                        queryParams.page = 1;
                        queryParams.defaultStatus = !queryParams.defaultStatus
                        queryParams.recommend = $("#recommend").val()

                        for (var key in formdata) {
                        	queryParams[key] = formdata[key]
						}

                        LS.set("bulletinPageQueryParams",JSON.stringify(queryParams));

                        sessionStorage.setItem('searching', JSON.stringify(formdata))
					}

				}
			});
 
		var queryParams  = LS.get("bulletinPageQueryParams");
		if(queryParams && LS.get("bulletinEditCancel")){
			var params = JSON.parse(queryParams);	
			//vm.queryParams= $.extend({},vm.queryParams, params);
			vm.queryParams.pageSize = params.pageSize;
			vm.queryParams.page = params.page;
			vm.queryParams.categoryTypeIdStr = params.categoryTypeIdStr;
			vm.queryParams.status = params.status || "";
			vm.queryParams.title = params.title || "";
			vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus;	
		}
		LS.remove("bulletinPageQueryParams");
		LS.remove("bulletinEditCancel");
		
});


function showLastUpdateDate(index ,params){
	return params[0]=='PUBLISHED' ? params[1] : '-';
}

function showTitle(index ,params){
	var title="",style="",str="",showTitle=params[3];
	if(params[0]=='Y'){
		title += "<span style='color:blue;' class='tabcon-label' title=''>[置顶] </span>"
		style="padding:0 0 0 40px;"
	}
	if(params[1] == 'RED'){
		style +=" color:red;" 
	}
	if(params[2] != '' && params[2] != undefined){
		title += "<span title='' style='color: red;padding: 4px 8px;margin-left: -60px;margin-top: -5px;border: 1px solid red;' class='tabcon-label'>首页</span>"
	}
	if(params[3].length>=15){
		showTitle = params[3].substring(0,15);
		str="...";
	}
	title += "<span style='"+style+"' title='"+ params[3] +"' class='cutstring'>"+ showTitle + str +"</span>"
	return title;
}

function delFunc(index, params) {//删除方法
	syncData(ykyUrl.info + "/v1/news/" + params[0], 'DELETE', null,
			function(res, err) {//页面加载前调用方法
				console.log(res);
				window.setTimeout(function() {
					vm.refresh = !vm.refresh;//重载
				}.bind(vm), 400);
				layer.close(index);
			});
	console.log(params);
}
function doChangeStatus(index, params) {//修改状态方法
	syncData(ykyUrl.info + "/v1/news/" + params[0] + "/" + params[1], 'PUT',
			null, function(res, err) {//页面加载前调用方法
				console.log(res);
				window.setTimeout(function() {
					vm.refresh = !vm.refresh;//重载
				}.bind(vm), 400);
				layer.close(index);
			});
	console.log(params);
}
