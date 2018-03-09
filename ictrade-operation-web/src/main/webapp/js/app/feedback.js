$(function(){
    var vm = new Vue({
       el: '#manufacturer-list',
       data: { 
    	   //http://192.168.1.110:27083/v1/recommendations?status=DRAFT&page=1&pageSize=20
          // url : ykyUrl.product + "/v1/products/brands/list", //访问数据接口 
           url :ykyUrl.info + "/v1/recommendations?categoryId=20005", //访问数据接口 
          queryParams : {           //请求接口参数 
        	  pageSize: 10,        //分页参数
               page:1,            //当前页  
               defaultStatus:true  //监测参数变化标识
           }, 
           gridColumns: [          //表格列
               {key: 'index', name: '序号', align:'center'},
               {key: 'contentMap', name: '内容', align:'center',
            	   child : [{ key: 'content', name: '',  callback: { 
                       action: 'showTitle',//方法名
                       params: ['{content}']
                   } }] 
               },
               {key: 'lastUpdateDate', name: '反馈时间', align:'center'},
               {key: 'creatorName', name: '反馈用户 ', default:'--', align:'center'},
               {key: 'contentMap', name: '联系方式 ', align:'center',
            	   child : [{ key: 'contentTel', name: ''}]
                   
               },
               {key: 'operate', align:'center', name: '操作', items:[  
                   {
                       className : 'btn-view',
                       text: '预览',
                       show : true,
                       callback: { 
                           action: 'openFeedback',//方法名
                           params: ['{contentMap}']
                       } 
                     
                   }
                ]
               }
           ], 
           pageflag : true,    //是否显示分页
           refresh: false    //重载  
       }, 
       methods: { 
           //搜索方法
           onSearch: function () {
               var that = this;
               var qModel = $('#seachForm').serializeObject();
               this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
               for(var i in qModel){
                   that.queryParams[i] = qModel[i];
               }
           } 
       }
   }); 
    $('#createData .input-daterange').datepicker({
        format: config.dateFormat,
        autoclose: true
    }); 
});

function  openFeedback(index, params) {//预览  
	var data = {
			desc : params[0].content,
			contact : params[0].contentTel
	}
	var viewHtml=$('#viewContent').html();
	viewHtml = applyTpl(viewHtml, data)
	layer.open({
		  type: 1, 
		  title: '意见反馈',
		  move: false,
	      shade: 0.8,
	      area: ['500px','430px'],
	      offset:"80px",
		  content: viewHtml
		});
   console.log(params);
}
 
function showTitle(index ,params){
	var title = params[0];
	if(params[0].length>20){
		title="<span title='"+params[0]+"'>"+params[0].substr(0,20)+"...</span>";
	} 
	return title
}
