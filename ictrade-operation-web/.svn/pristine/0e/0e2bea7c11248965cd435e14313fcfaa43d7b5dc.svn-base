var vm= new Vue({
    el: "#redisCache",                    //该ID标签的范围，必须包含实例中的方法和属性的所属标签，一般将改ID绑定到class=“content-wrapper”上
    data: {
        queryParams: {                //搜索字段及分页字段
        	keyName:'',
        	key:'',
        },
        showData:{
        	keyName:'',
        	key:'',
        	value:''
        }
    },
    methods: { 

         /*搜索方法*/
    	searchByParam: function () {        //该方法表单提交时绑定，如：<form id="seachForm" @submit.prevent="onSearch">
    		event.preventDefault();
            var that = this;
            /*var qModel = $('#seachForm').serializeObject(); //获取表单中字段值，"seachForm"为form表单ID
            for(var item in qModel){
                that.queryParams[item] = qModel[item];  //改变queryParams参数
            }*/
            if(!that.queryParams.keyName||!that.queryParams.key){
            	layer.open({
            		  title: ' '
            		  ,content: '缓冲池名称和key不能为空！'
            		}); 
            }else{
            	 getCacheValue( that.queryParams.keyName,that.queryParams.key);
            }
        },
        deleteData:function(keyName,key){
        	deleteCache(keyName,key);
        },
        showAll:function(){
        	layer.open({
        		  type: 1,
        		  skin: 'layui-layer-rim', //加上边框
        		  area: ['700px', '400px'], //宽高
        		  content: $("#valueWrapper")
        	});
        }
    }
})

var getCacheValue = function(keyName,key){
	$.aAjax({
		url: ykyUrl.database + '/v1/cache/'+keyName+'/bycache?key='+key ,
		type: 'GET',
		success:function(data){
			if(data){
				vm.showData.keyName = keyName;
				vm.showData.key = key;
				vm.showData.value = data;
			}
		},
		error:function(data){
			console.log("error");
		}
	})
}
var deleteCache = function(keyName,key){
	if(!keyName){
		
	}
	layer.alert("确认是否删除？",{
		 offset: "auto",
			btn: ['确      认','取      消'], //按钮
			title: " ",	
			area: 'auto',
			maxWidth:'500px',
			move: false,
			skin: "up_skin_class",
			closeBtn: false,
			yes:function(){
				$.aAjax({
					url: key?ykyUrl.database + '/v1/cache/'+keyName+'/delcache?keyName='+keyName+'&key='+key:ykyUrl.database + '/v1/cache/'+keyName+'/delcache?keyName='+keyName,
					type: 'PUT',
					success:function(data){
						//to do
						layer.closeAll();
						if(data=='SUCCESS'){
							if(key){
								layer.msg('删除缓冲池'+ keyName +'key：'+key+"成功"); 
							}else{
								layer.msg('删除缓冲池'+ keyName +"成功");
							}
							vm.showData.key = '';
							vm.showData.value = '';
						}
					},
					error:function(data){
						console.log("error");
					}
				})
			}
	 })
}

var clipboard = new Clipboard('.copy_btn', {
    target: function() {
        return document.querySelector('#valueCont');
    }
});

clipboard.on('success',function(e) {
	layer.tips('已成功复制到粘贴板','.copy_btn',{
		  tips: [3, '#dd4b39']
		});
});

clipboard.on('error', function(e) {
    console.log(e);
});
$(function() {
	
});