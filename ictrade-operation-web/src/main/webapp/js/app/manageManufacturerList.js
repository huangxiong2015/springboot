var vm 
$(function(){

	 vm= new Vue({
		el: '#vendorManageList',
		data: {
			url: ykyUrl.info + "/v1/news?categoryTypeIdStr=BRAND",
			queryParams : { //请求接口参数 
				size : 10, //分页参数
				page : 1, //当前页
				defaultStatus : true //监测参数变化标识
			},
			gridColumns : [          // 表格列s
	                         {key: 'index', name: '序号', align:'center', width: '50px'},
	                         {key: 'attachUrl', name: '制造商LOGO', class : 'wid10',  align:'center',  title:'title', type: 'image', default: ykyUrl._this + '/images/defaultImg01.jpg'},
	                         {key: 'title', name: '制造商名称', class : 'behide',lign : 'center',cutstring: true,},
	                         {key: 'author', name: '创建人',align : 'center',cutstring: true,},
	                         {
	                         	key: 'createdDate', 
	                         	name: '创建时间',  
	                         	align:'center',
	                         	cutstring: true
	                     	},
	                         {key: 'lastUpdateDate', name: '更新时间',  align:'center',cutstring: true,},
	                         {key: 'lastUpdateUserName', name: '最近编辑人', align:'center', default: '--',cutstring: true,},
	                         {key:'status',name:'状态',align:'center',
	                         	width: '120px',
	                         	text:{
	         	                	DRAFT:{
	         	                		value : '不显示'
	         	                    },
	         	                    PUBLISHED:{
	         	                    	value : '显示'
	         	                    },
	                         },
	                         },
	                         {key : 'operate',name : '操作',align : 'center',width:'150px',items : [  
	                            {
	         						className : 'btn-delete',
	         						show : "'{status}'!='PUBLISHED'",
	         						text : '显示',
	         						callback : {
	         							confirm : {
	         								title : '显示',
	         								content : '确认显示？'
	         							},
	         							action : 'changeStatus',
	         							params : [ '{newsId}', 'PUBLISHED' ]
	         						}
	         					}, {
	         						className : 'btn-delete',
	         						show : "'{status}'=='PUBLISHED'",
	         						text : '不显示',
	         						callback : {
	         							confirm : {
	         								title : '不显示',
	         								content : '确认不显示？'
	         							},
	         							action : 'changeStatus',
	         							params : [ '{newsId}', 'DRAFT' ]
	         						}
	         					}, {
	                                 className : 'btn-edit',
	                                 text: '编辑',
	                                 show : "'{status}'!='PUBLISHED'",
	                                 href : ykyUrl._this + '/manageManufacturer/edit.htm?id={newsId}',
	                                 target: '_self'
	                             },
	                             {
	                                 className : 'btn-detail',
	                                 text: '详情',
	                                 show : true,
	                                 href : ykyUrl._this + '/manageManufacturer/detail.htm?id={newsId}',
	                                 target: '_self'
	                             } 
	         					]
	         				}
                         ],
			pageflag : true, //是否显示分页
			refresh : false, //重载
			
			formData:{
				id:'formId',
				columnCount:3,
				fields: [
					{
		                "keyname": "制造商名称",
		                "type": "text",
		                "key": "title", 
		                "name": "title"
		          }, {
		                "keyname": "创建人",
		                "type": "text",
		                "key": "author", 
		                "name": "author"
		          },{
	                    "keyname": "更新时间",
	                    "type": "daterange",        //时间区间类型
	                    "startkey": "createDateStart",  //star id
	                    "endkey": "createDateEnd",        //end id
	                    "startname": "createDateStart",    //star name
	                    "endname": "createDateEnd"         //end name
	                }       
			],
			buttons:[
				{
	                "text": '查询',
	                "type": 'button',
	                "id": '',
	                "name": '',
	                "inputClass": 'btn btn-danger',
	            	"callback": {
	                    action: 'on-search',        //方法名
	                }
	            }
			]
		}
		},
		methods: {
			//搜索方法
			searchData: function(searchParam){		
				var queryParams = this.queryParams
	            queryParams.pageSize = 10
	            queryParams.page = 1
	            queryParams.defaultStatus = !queryParams.defaultStatus
	
	            for (var key in searchParam) {
	                queryParams[key] = searchParam[key] ? searchParam[key] : undefined
	            }
			}
		}
	})
})
function changeStatus(index, params){ 
	syncData(ykyUrl.info + "/v1/news/" + params[0] + "/" + params[1], 'PUT',
			null, function(res, err) {// 页面加载前调用方法
				console.log(res);
				window.setTimeout(function() {
					vm.refresh = !vm.refresh;// 重载
				}.bind(vm), 400);
				layer.close(index);
			});
}