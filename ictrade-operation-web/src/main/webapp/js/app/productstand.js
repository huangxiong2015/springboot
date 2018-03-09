var vm ;
$(function(){
           vm = new Vue({
                	el: '#productstand',
                    data: {
                    	url : ykyUrl.product + "/v1/products/stand/findWhiteListInfo", //访问数据接口 
                        queryParams : {           //请求接口参数 
                        	size: 20,        //分页参数
                            page:1,              //当前页
                            defaultStatus:true,  //监测参数变化标识
                        },
                        formData: {
 			        	   id: 'seachForm',
 		                   name: 'seachForm',
                           columnCount: 3,
                            fields:[            // 配置控件
                                 {
                                    keyname: "型号 ",           // 控件文本
                                    type: "text",             // 控件类型
                                    key: "manufacturerPartNumber",          // 控件名
                                    name: "manufacturerPartNumber"          // 控件name
                                },{
                                    keyname: "制造商名称",          
                                    type: "text",             
                                    key: "manufacturer",         
                                    name: "manufacturer"        
                                }
                            ],
                            buttons: [
                                      {
                                          "text": '查询',
                                          "nativeType": 'submit',
                                          "type": 'danger',
                                          "inputClass": 'btn btn-danger',
                                          "id": '',
                                          "name": '',
                                          "size":'sm'
                                      },
                                      {
                                          "text": '新增',
                                          "nativeType": 'button',
                                          "id": '',
                                          "inputClass": 'btn addspu',
                                          "name": '',
                                          "size":'sm'
                                          
                                      }
                                  ]
                          
                        },
                        gridColumns: [          //表格列
                            {key: 'manufacturerPartNumber', name: '型号', cutstring:true},
                            {key: 'manufacturer', name: '制造商名称', align:'center',cutstring: true},
                            {key: 'creatorName', name: '创建人'},
                            {key: 'createdTime', name: '创建时间',  align:'center'},
                            {key: 'lastUpdateUserName', name: '最近编辑人', align:'center', default: '--'},
                         
                       /*     {key: 'operate', name: '操作', align:'center', items:[  
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
                            }*/
                        ], 
                        pageflag : true,    //是否显示分页
                        refresh: false     //重载 

                    },  
                    created(){
                    	  var that = this;
                    	  var qModel = parseUrlParam(); 
                    	  for(var i in qModel){
                              that.queryParams[i] = qModel[i];
                          } 
                          this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
                    },
                    methods:{
                    	 /* 搜索方法 */
    	                formSubmit: function(params) {
    	                	console.log(111);
    	                    var that = this;
    	                    this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
    	                    for (var i in params) {
    	                        that.queryParams[i] = params[i];
    	                    }
    	                }
    	                
    	                
                    }
                });  
                     //点击新增
		           $('.addspu').on("click", function() {
		        		window.location = ykyUrl._this + '/productstand/edit.htm';
		        	});
                
            }); 

