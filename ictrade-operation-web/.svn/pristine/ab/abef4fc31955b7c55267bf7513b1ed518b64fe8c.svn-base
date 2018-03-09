   var vm ;
$(function(){
	
      vm = new Vue({
        el: '#manufacturer-list',
        data: {
            url : ykyUrl.info + "/v1/news?categoryTypeIdStr=VENDOR", // 访问数据接口
            queryParams : {           // 请求接口参数
            	pageSize: 10,        // 分页参数
                page:1,              // 当前页
                defaultStatus:true  // 监测参数变化标识
            },
            title:'',
            gridColumns: [          // 表格列
                {key : 'newsId',name : 'id',hide : 'true'}, 
                {key: 'index', name: '序号', align:'center', width: '50px'},
                {key: 'attachUrl', name: '分销商LOGO', class : 'wid10',  align:'center',  title:'title', type: 'image', default: ykyUrl._this + '/images/defaultImg01.jpg'},
                {key: 'title', name: '分销商名称', class : 'behide',lign : 'center',cutstring: true,},
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
                        href : ykyUrl._this + '/distributor.htm?action=edit&id={newsId}',
                        target: '_self'
                    },
                    {
                        className : 'btn-detail',
                        text: '详情',
                        show : true,
                        href : ykyUrl._this + '/distributor.htm?action=detail&id={newsId}',
                        target: '_self'
                    } 
					]
				}
            ], 
            pageflag : true,    // 是否显示分页
            refresh: false     // 重载
        },  
        created(){
        	  var that = this;
        	  var qModel = parseUrlParam();  //给对象赋值
        	  for(var i in qModel){
                  that.queryParams[i] = qModel[i];
              } 
              this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
        },
        methods:{
        	onSearch : function(){ 
        	//	 var qModel = $('#seachForm').serializeObject();
        		 var url=window.location.pathname;
        		 var parms = "" 
        		 if($('#title').val() != "" && typeof($('#title').val())!= "undefined" && $('#author').val() != "" && typeof($('#author').val())!= "undefined" && $('#startDate').val() != "" && typeof($('#startDate').val())!= "undefined")	
        		 {
        			 parms = "?title=" + encodeURIComponent($('#title').val())+"&author="+ encodeURIComponent($('#author').val())+"&createDateStart="+  encodeURIComponent($('#startDate').val())+"&createDateEnd="+encodeURIComponent($('#endDate').val());
        		 }else if($('#title').val() != "" && typeof($('#title').val())!= "undefined" &&  $('#author').val() != "" && typeof($('#author').val())!= "undefined" ){
        			 parms = "?title=" + encodeURIComponent($('#title').val())+"&author="+ encodeURIComponent($('#author').val())
        		 }else if($('#title').val() != "" && typeof($('#title').val())!= "undefined" ){ 
        			 parms = "?title=" + encodeURIComponent($('#title').val())
        		 }else if($('#author').val() != "" && typeof($('#author').val())!= "undefined" && $('#startDate').val() != "" && typeof($('#startDate').val())!= "undefined"){
        			 parms = "?author="+ encodeURIComponent($('#author').val()) +"&createDateStart="+  encodeURIComponent($('#startDate').val())+"&createDateEnd="+encodeURIComponent($('#endDate').val());
        		 }else if($('#author').val() != "" && typeof($('#author').val())!= "undefined"){
        			 parms = "?author="+ encodeURIComponent($('#author').val())  ;
        		 }else if($('#startDate').val() != "" && typeof($('#startDate').val())!= "undefined"){
        			 parms = "?createDateStart="+  encodeURIComponent($('#startDate').val())+"&createDateEnd="+encodeURIComponent($('#endDate').val());
        		 } 
        		 window.location.href= url + parms;
        	}
        }
    });  
     $('#createData .input-daterange').datepicker({
         format: config.dateFormat,
         autoclose: true
     }); 
     
     
     
     
     
}); 


function changeStatus(index, params){ 
	syncData(ykyUrl.info + "/v1/news/" + params[0] + "/" + params[1], 'PUT',
			null, function(res, err) {// 页面加载前调用方法
				console.log(res);
				window.setTimeout(function() {
					vm.refresh = !vm.refresh;// 重载
				}.bind(vm), 400);
				layer.close(index);
			});
	console.log(params);	
}
