var vm = '';
$(function(){	 
     vm = new Vue({
        el: '#recuit-list',
        data: { 
            previewUrl: ykyUrl.portal + '/search/inventory.htm',
        	formData: {
                id:'seachForm',
                columnCount: 3,
                fields:[            //配置控件
                     {
                        keyname: "描述",           //控件文本
                        type: "text",             //控件类型
                        key: "desc",          //控件名
                        name: "desc"         //控件name
                    },
                    {
                        keyname: "状态",           //控件文本
                        type: "select",             //控件类型
                        key: "status",          //控件名
                        name: "status",         //控件name
                        options: [{
                            value: '',
                            text: '全部'
                        },{
                            value: 'PUBLISHED',
                            text: '已生效'
                        },{
                            value: 'DRAFT',
                            text: '未生效'
                        },{
                            value: 'HOLD',
                            text: '已停用'
                        },{
                            value: 'UNVALID',
                            text: '已失效'
                        }]
                    },
                    {
                        keyname: "创建时间",
                        type: "daterange",   		//区间类型
                        startkey: "startTime",   //star id
                        endkey: "endTime",  		//end id
                        startname: "startTime",  //star name
                        endname: "endTime" , 		//end name
                        dateRange:{}
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
                    },{
                    	"text": '新增',
                        "type": 'button',
                        "id": '',
                        "name": '',
                        "inputClass": 'btn btn-danger',
                        "callback": {
                            action: 'go-add'
                        }
                    }
                    ]
            },
            url : ykyUrl.info + "/v1/recommendations/searchlistpage", //访问数据接口 
            queryParams : {           //请求接口参数 
                defaultStatus:true,  //监测参数变化标识
                desc:'',
                status:'',
                startTime:'',
                endTime:''
            },
            goodsQuantityTotal:"3",
            gridColumns: [          //表格列
                {key: 'desc', name: '描述',align : 'center',width: '200px', cutstring: true},
                {key: 'desc', name: '对象',align : 'center',width: '200px', cutstring: true,
                	render:function(h, params){
                		var data = params && params.row;
                		var arr = [];
                		if(data.distributorName){
                			arr.push(data.distributorName);
                		}
                		if(data.sourceName){
                			arr.push(data.sourceName);
                		}
                		if(data.categoryTypeName){
                			arr.push(data.categoryTypeName);
                		}
                		var str = arr.join(' + ');
                		return h('div',{
                			'class':{
                				cutstring: true
                			},
                			attrs: {
                				title: str
                			}
                		},str)
                	}},
                {key: 'goodsQuantity', name: '推广商品数',align : 'center'},
                {key: 'orderSeq', name: '排序',align : 'center'},
                {key: 'createdDate', name: '创建时间',align : 'center',cutstring: true},
                {key: 'lastUpdateDate', name: '更新时间',align : 'center',cutstring: true},
                {key: 'publishDate', name: '生效时间',align : 'center',default:'-',cutstring: true},
                {key: 'expiryDate', name: '推广截止日',align : 'center',default:'-',cutstring: true,
                	render:function(h,params){
                		var expiryDate = params.row.expiryDate;
                		expiryDate = expiryDate ? new Date(expiryDate).Format('yyyy-MM-dd') : '-';
                		return h('span',{
                			'class': {
								cutstring: true
								
                            },
                            attrs: {
                                title: expiryDate
                              },
                        },expiryDate)
                	}
                },
                {
					key : 'status',
					align : 'center',
					name : '状态',
					text : {
						PUBLISHED : {
							'color' : 'green',
							'value' : '已生效',
						},
						DRAFT : {
							'color' : 'black',
							'value' : '未生效'
						},
						HOLD : {
							'color' : 'red',
							'value' : '已停用'
						},
						UNVALID : {
							'color' : '#666',
							'value' : '已失效'
						}
					}
				},
				{
					key : 'operate',
					name : '操作',
					align : 'center',
					items : [ {
						className : 'btn-detail',
						text : '详情',
						show : true,
						href : ykyUrl._this+'/searchPromotion/detail.htm?recommendationId={recommendationId}',
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
							params : [ '{recommendationId}', 'HOLD' ]
						}
					}, {
						className : 'btn-delete',
						show : "'{status}'!='PUBLISHED' && '{status}'!='UNVALID'",
						text : '生效',
						callback : {
							confirm : {
								title : '生效',
								content : '确认生效？'
							},
							action : 'doChangeStatus',
							params : [ '{recommendationId}', 'PUBLISHED' ]
						}
					}, {
						className : 'btn-edit',
						text : '编辑',
						show : " '{status}'!='PUBLISHED' && '{status}'!='UNVALID'",
						href : ykyUrl._this+'/searchPromotion/edit.htm?recommendationId={recommendationId}',
					}, {
						className : 'btn-delete',
						text : '删除',
						show : " '{status}'!='PUBLISHED' && '{status}'!='UNVALID'",
						callback : {
							confirm : {
								title : '删除',
								content : '确认删除？'
							},
							action : 'doChangeStatus',
							params : [ '{recommendationId}', 'DELETED' ]
						}
					}]
				}
            ], 
            pageflag : true,    //是否显示分页
            refresh: false ,    //重载  
            selectUrl :ykyUrl.database + '/v1/category/companylist?categoryTypeId=OCCUPATION_TYPE',    //数据接口，必须
            id : 'categoryId',        //设置控件ID ，必须
            name : 'categoryName',        //设置控件name ，必须
            inputClass : '',    //设置下拉选项的class，可选
            styleObject: '',   //设置style，可选
            hasSetRuleNum: '', //已设置规则数
            surplusSetRuleNum: '' //剩余设置规则数
        }, 
        created(){
        	this.showRuleNum();
        },
        methods:{
        	onSearchClick:function(formdata){
        		for(var k in formdata){
        			this.queryParams[k] = formdata[k];
        		}
        		this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
        		this.showRuleNum();
        	},
        	goAdd:function(){
        		window.location = ykyUrl._this + '/searchPromotion/edit.htm';
        	},
        	showRuleNum:function(){
            	var that = this;
            	var data = {status: 'PUBLISHED'}
            	syncData(this.url, 'GET', data, function(res, err){
            		if(err == null){
            			that.hasSetRuleNum = res.list && res.list.length ? res.list.length : 0;
            			that.surplusSetRuleNum = 10 - that.hasSetRuleNum;
            		}
            	})
        	}
        }
    });
});

function doChangeStatus(index, params) {//发布
	syncData(ykyUrl.info + "/v1/recommendations/searchstatus/" + params[0] + "/" + params[1], 'PUT',
			null, function(res, err) {//页面加载前调用方法
				window.setTimeout(function() {
					vm.refresh = !vm.refresh;//重载
					if(params[1] != "DELETED"){
						vm.showRuleNum();
					}
				}.bind(vm), 400);
				layer.close(index);
			});
};