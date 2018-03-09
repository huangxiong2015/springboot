
var vm = new Vue({
    el: '#manufacturer-list',
    data:{
			title:'',
            cacheValue:'',
            roomname: getQueryString('roomname')?getQueryString('roomname'):'',
    		listparam:{
            	'cacheType':'AUTO',
            	'key':'',
            	'database':getQueryString('database'),
            },
            cacheValue:'',
			getTableData: false
    },  
    created:function(){
    	  /*var that = this;
    	  var qModel = parseUrlParam();  
    	  for(var i in qModel){
              that.queryParams[i] = qModel[i];
          } 
          this.queryParams.defaultStatus = !this.queryParams.defaultStatus;*/
    	console.log(vm);
    },
    methods:{
    	onSearch : function(){ 
    		if($.trim($("#key").val())!=''){
    			if(vm.getTableData){
        			vm.queryParams.key =  $("#key").val();
        			vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus;
        		}else{
            			getDataList();
        		}
    		}	
    	}
    }
});  


function getCacheOne(index, params){ 
	syncData(ykyUrl.database + "/v1/cache/findCacheOne?database=" + params[0] + "&key=" + params[1]+"&cacheType="+vm.listparam.cacheType, 'GET',
			null, function(res, err) {// 页面加载前调用方法
				console.log(res);
				vm.cacheValue = res.value;
				layer.open({
					  type: 1,
					  shade: false,
					  title: '显示', //不显示标题
					  content: $("#valueWrap"), //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
					  btn: ['Ok', 'Close'],
					  area: ['600px', '400px'],
					  cancel: function(){
						  layer.close(layer.index)
					  },yes:function(){
						  layer.close(layer.index)
					  }
					});
	  });
	console.log(params);
}

var getDataList = function(){
	vm.getTableData = true;
	var inputKey = $("#key").val();
	vm.url = ykyUrl.database + "/v1/cache/findCacheKeys"; // 访问数据接口
	vm.queryParams = {           // 请求接口参数
	    	/*pageSize: 10,       
	        page:1, */    
	    	database:getQueryString('database'),
	        key:inputKey,
	        defaultStatus:true // 监测参数变化标识
    	}; 
	vm.gridColumns =[          // 表格列
					     {key: 'key', name: 'key', align:'center'},
					     {key : 'operate',name : '操作',align : 'center',width:'200px',items : [  
					        {
								className : 'btn-delete',
								show : true,
								text : '查看',
								callback : {
									action : 'getCacheOne',
									params : [ '{database}', '{key}']
								}
							}
							]
						}
					 ]
	vm.checkflag = true;
	vm.pageflag = false;
	vm.refresh = true;
};
