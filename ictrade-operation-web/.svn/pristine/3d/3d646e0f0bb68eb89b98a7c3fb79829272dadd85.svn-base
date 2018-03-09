   var vm ;
$(function(){
	
      vm = new Vue({
        el: '#manufacturer-list',
        data: {
            url : ykyUrl.database + "/v1/cache/findControlList", // 访问数据接口
            queryParams : {           // 请求接口参数
            	pageSize: 10,        // 分页参数
                page:1,              // 当前页
                defaultStatus:false  // 监测参数变化标识
            },
            title:'',
            gridColumns: [          // 表格列
                {key : 'database',name : '数据库DB',default: 0,align : 'center'}, 
                {key: 'key', name: '缓冲池房间名', align:'center'},
                {key: 'keyCount', name: '数量', class : 'behide',align : 'center',default: 0,},
            
                {key : 'operate',name : '操作',align : 'center',width:'200px',items : [  
                   {
						className : 'btn-delete',
						show : true,
						text : '删除',
						callback : {
							action : 'deleteCacheKeyName',
							params : [ '{database}', '{key}' ]
						}
					},
                    {
                        className : 'btn-detail',
                        text: '详情',
                        show : true,
                        href : ykyUrl._this + '/redisCacheDetail.htm?database={database}&roomname={key}',
                        target: '_self'
                    } 
					]
				}
            ], 
            pageflag : true,    // 是否显示分页
            refresh: false     // 重载
        },  
        created(){
        	 /* var that = this;
        	  var qModel = parseUrlParam();  //给对象赋值
        	  for(var i in qModel){
                  that.queryParams[i] = qModel[i];
              } 
              this.queryParams.defaultStatus = !this.queryParams.defaultStatus;*/
        },
        methods:{
        	/*onSearch : function(){ 
        	}*/
        }
    });  
            
}); 

function deleteCacheKeyName(index, params){ 
	/*layer.confirm('你确定要删除吗？库:'+database+'key:'+key, {
		  btn: ['Ok','Close'] 
		}, function(){
			syncData(ykyUrl.product + "/v1/cache/deleteCacheKeyName?database=" + params[0] + "&key=" + params[1], 'GET',
					null, function(res, err) {
						console.log(res);
						window.setTimeout(function() {
							vm.refresh = !vm.refresh;
						}.bind(vm), 400);
						layer.close(index);
					});
			console.log(params);	
		}, function(){
			layer.close(index);
		});*/
	
	layer.open({
		  type: 1,
		  shade: false,
		  title: '显示', //不显示标题
		  content: '<div style="text-align: center;padding-top: 100px;">'+'你确定要删除吗？库:'+params[0]+' key:'+params[1]+'</div>', //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
		  btn: ['Ok', 'Close'],
		  area: ['500px', '300px'],
		  cancel: function(){
			  layer.close(layer.index)
		  },yes:function(){
			  syncData(ykyUrl.database + "/v1/cache/deleteCacheKeyName?database=" + params[0] + "&key=" + params[1], 'GET',
						null, function(res, err) {// 页面加载前调用方法
							console.log(res);
							/*window.setTimeout(function() {
								vm.refresh = !vm.refresh;// 重载
							}.bind(vm), 400);
							layer.close(layer.index)*/
							window.location.reload();
						});
				console.log(params);
		  }
		});
}

