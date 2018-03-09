   var vm ;
$(function(){
	
      vm = new Vue({
        el: '#manufacturer-list',
        data: {
           url : ykyUrl.msg + "/v1/task", //访问数据接口 
        	//url:'http://192.168.3.31:27088/v1/task',
            queryParams : {           //请求接口参数 
            	pageSize:20,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
            },
            gridColumns: [          //表格列
                {key: 'id', name: 'id',  hide : 'true'},
                {key: 'bizType', name: '业务类型',  align:'center',
                	 text : {
                		 MATERIAL : {
                             'value' : '物料 '
                         },
                        MANUFACTURER: {
                             'value' : '制造商 '
                         },
                        PRODUCT: {
                             'value' : '商品 '
                         },
                         SUPPER_PRODUCT_LINE: {
                             'value' : '供应商产品线'
                         }
                       }
                }, // Material -物料;  Manufacturer -制造商;  Product - 商品"
               {key: 'url', name: '文件路径',  hide : 'true', align:'center',default: '--',cutstring:true,},
               {key: 'beginTime', name: '开始时间', align:'center', default: '--', cutstring:true,},
               {key: 'endTime', name: '结束时间', align:'center', default: '--', cutstring:true,},
               {key: 'message', name: '信息', align:'center', default: '--', cutstring:true,},
               {key: 'status', name: '状态',  align:'center',
                   text : {
                 	 INITIAL : {
                 		 'color' : 'gray',
                         'value' : '未启动 '
                     },
                    PROCESSING : {
                         'value' : '处理中 '
                     },
                     SUCCESS : {
                    	 'color' : 'green',
                         'value' : '成功 '
                     },
                     FAIL : {
                    	 'color' : 'red',
                         'value' : '失败 '
                     }
                 } },{ 
                     key : 'operate',
                     name : '操作',
                     align : 'center',
                     //default:'--',
                     items : [  {
                    	 className : 'btn-detail',
                         text: '下载',
                         show : "'{action}' == 'DOWNLOAD' &&  '{status}' == 'SUCCESS' " ,  
                         href : '{url}',  //物料详情链接  
                         target : '_blank'
                     },
                     {
                         className : 'btn-upload',
                         text: '上传',
                         show : "'{action}' =='UPLOAD'"  
                     },
                     {
                         className : 'btn-del',
                         text: '删除',
                         show : "'{action}' =='DELETE'"  
                     }]
                 }
              /* { key: 'action', 
                   name: '动作', 
                   cutstring:true,
                   text : {
                 	UPLOAD : {
                 		'color' : 'black',
                         'value' : '上传 '
                     },
                      DOWNLOAD: {
                         'color' : 'green',
                         'value' : '下载 '
                     },
                      DELETE : {
                     	'color' : 'red',
                         'value' : '删除 '
                     }
                   }
                  },*/
               
            ], 
            pageflag : true,    //是否显示分页
            refresh: false     //重载 

        },  
        created(){
        	  var that = this;
        	  var qModel = parseUrlParam(); 
        	  console.log(unescape(qModel['brandName']));
        	  for(var i in qModel){
                  that.queryParams[i] = qModel[i];
              } 
              this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
        },
        methods:{
        	onSearch : function(){ 
        		 var qModel = $('#seachForm').serializeObject();
        		 
        		  window.location.href = window.location.pathname 
        		  + "?brandName=" + encodeURIComponent($('#brandName').val()) 
        		  + "&creator="+ encodeURIComponent($('#creator').val())  
        		  +"&startDate="+  encodeURIComponent($('#startDate').val()) 
        		  +"&endDate="+encodeURIComponent($('#endDate').val());
        	}
        }
    });  
    
}); 