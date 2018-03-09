   var vm ;
$(function(){
	
      vm = new Vue({
        el: '#manufacturer-list',
        data: {
            url : ykyUrl.product + "/v1/products/manufacturers", //访问数据接口 
            queryParams : {           //请求接口参数 
            	size: 10,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
                brandName:''
            },
            brandName:'',
            gridColumns: [          //表格列
                {key: 'index', name: '序号', align:'center', width:'50px'},
                {key: 'logo', name: '制造商LOGO', class : 'wid10', title:'brandName', type: 'image', align:'center', default: ykyUrl._this + '/images/defaultImg01.jpg'},
                {key: 'brandName', name: '制造商名称', cutstring:true},
                {key: 'creator', name: '创建人'},
                {key: 'createdDate', name: '创建时间',  align:'center'},
                {key: 'lastUpdateUser', name: '最近编辑人', align:'center', default: '--'},
                {key: 'operate', name: '操作', align:'center', items:[  
                    {
                        className : 'btn-edit',
                        text: '编辑',
                        show : true,
                        href : ykyUrl._this + '/manufacturer/edit.htm?id={id}',
                        target: '_self'
                    },
                    {
                        className : 'btn-detail',
                        text: '详情',
                        show : true,
                        href : ykyUrl._this + '/manufacturer/detail.htm?id={id}',
                        target: '_self'
                    }
                 ]
                }
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
     $('#createData .input-daterange').datepicker({
         format: config.dateFormat,
         autoclose: true,
         todayHighlight: true
     }); 
}); 