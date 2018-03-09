var vm = '';
$(function() {

	function getFormData () {
	    var searchingFlag = sessionStorage.getItem('searchingFlag')
		var searchString = sessionStorage.getItem('searching')
        var result = {}

		if (searchString && searchingFlag) {
			try {
                result = JSON.parse(searchString)
			} catch (e) {}
		}

        sessionStorage.removeItem('searchingFlag')
        sessionStorage.removeItem('searching')
        if (!searchingFlag) {
            sessionStorage.removeItem('searching')
        }
       // result['categoryTypeIdStr']='NOTICE,INFORMATION,MEDIAREPORT';
		return result
	}

	vm = new Vue(
			{
				el : '#material-review',      //物料管理列表页面
				data : function () {
					return 	{
                        formData: {
                            url:'',    //form提交请求接口，可选     
                            id:'seachForm',
                            data: getFormData(),            //初始化数据
                            columnCount: 3,
                            fields:[            //配置控件
                                 {
                                    keyname: "物料型号 ",           //控件文本
                                    type: "text",             //控件类型
                                    key: "manufacturerPartNumber",          //控件名
                                    name: "manufacturerPartNumber" ,        //控件name
                                    "validate": {
                                        "minlength": 3
                                    } 
                                },{
                                    keyname: "原厂  ",          
                                    type: "text",             
                                    key: "manufacturer",         
                                    name: "manufacturer"        
                                },
                                {
                                    "keyname": "分类 ",        //分类使用城市组件 
                                    "type": "city", 
                                    "inputClass": 'ml10',					//设置style
                                    "validate": {
                                        "citySel": false
                                    },
                                    "items": {
                                        "province": {			//设置一级属性
                                            id: 'cate1Name',
                                            name: 'cate1Name' 
                                        },
                                        "city": {				//设置二级属性
                                            id: 'cate2Name',
                                            name: 'cate2Name' 
                                        },
                                        "district": {			//设置三级属性
                                            id: 'cate3Name',
                                            name: 'cate3Name' 
                                        }
                                    },
                                    "url": ykyUrl.product + "/v1/products/categories/children",    //分类接口 
                           
                                    "attr":{
                                        "disabled":true
                                     },
                                    "optionId":"_id",
                                    "optionName":"cateName",
                                    "parentId":"parentCateId"
                                },
                               {
                                    "keyname": "更新时间",
                                    "type": "daterange",   		//区间类型
                                    "startkey": "startDate",   //star id
                                    "endkey": "endDate",  		//end id
                                    "startname": "startDate",  //star name
                                    "endname": "endDate" , 		//end name
                                    "dateRange":{}
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
                                        value: '0',
                                        text: '有效 '
                                    },{
                                        value: '1',
                                        text: '失效'
                                    }]
                                },{
                                    keyname: "操作人  ",          
                                    type: "text",             
                                    key: "auditUserName",         
                                    name: "auditUserName"        
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
                                "inputClass": 'btn upload',
                                "callback": {
                                    action: 'go-upload'
                                }
                            }],
                            
                          "message":{ 
                                 "manufacturerPartNumber":{
                                	 	minlength:'至少需要输入3个字符'
                                	 }  
                           }
                        },
                        url : ykyUrl.product + "/v1/products/stand", //访问数据接口  
                        queryParams : { //请求接口参数
                            pageSize : 20, //分页参数
                            page : 1, //当前页
                            status:'',
                            manufacturerPartNumber:'',   //型号 
                            manufacturer : '' ,   //原厂 
                            defaultStatus : true, 
                            //监测参数变化标识
                        },
                        gridColumns : [ //表格列
                             {
                                 key: 'selected',
                                 name: '',
                                 width:'40px'
                              },{
                                key : 'id',  //物料id  
                                name : 'id',
                                hide : 'true'
                             },{
                                key : 'manufacturerPartNumber',
                                name : '型号',
                                align : 'center',
                                cutstring: true
                            }, {
                                key : 'manufacturer',
                                name : '原厂 ',
                                cutstring: true
                            },{
                                key : 'cate1Name',     	//大类
                                align : 'center',
                                cutstring: true,
                                name : '大类 ',
                                render: function (h, params) {
                                    var categories= params.row.categories ;
                                    var str = '';
                                    $.each(categories,function(index,ele){
                                    	if(ele.cateLevel == 1){
                                    		str = ele.cateName;
                                    	}
                                    })
                              
                                   return h('span',{
                                	   'class': {
       										cutstring: true
       									
                                       },
                                       attrs: {
                                           title: str
                                         },
                                   },str) ;
                                }
                            	 
                            },{
                                key : 'cate2Name',     	//小类
                                align : 'center',
                                cutstring: true,
                                name : '小类 ',
                                render: function (h, params) {
                                    var categories= params.row.categories ;
                                    var str = '';
                                    $.each(categories,function(index,ele){
                                    	if(ele.cateLevel == 2){
                                    		str = ele.cateName;
                                    	}
                                    })
                              
                                   return h('span',{
                                	   'class': {
       										cutstring: true
       									
                                       },
                                       attrs: {
                                           title: str
                                         },
                                   },str) ;
                                }
                            	 
                            },{
                                key : 'cate3Name',     	//次小类
                                align : 'center',
                                cutstring: true,
                                name : '次小类 ',
                                render: function (h, params) {
                                    var categories= params.row.categories ;
                                    var str = '';
                                    $.each(categories,function(index,ele){
                                    	if(ele.cateLevel == 3){
                                    		str = ele.cateName;
                                    	}
                                    })
                              
                                   return h('span',{
                                	   'class': {
       										cutstring: true
       									
                                       },
                                       attrs: {
                                           title: str
                                         },
                                   },str) ;
                                }
                            	 
                            },{
                                key : 'status',    //状态  
                                align : 'center',
                                name : '状态',
                                align : 'center',
                                width: '120px',
                                default :'有效',
                                text : {
                                    0 : {
                                        'value' : '有效 '
                                    },
                                    1 : {
                                        'value' : '失效 '
                                    }
                                } 
                            
                            },{
                                key : 'updatedTimeMillis',
                                name : '更新时间  ',
                                align : 'center',
                                default :'--',
                                cutstring: true
                            },{
                                key : 'updateName',
                                name : '操作人 ',
                                align : 'center',
                                default : '--',
                                cutstring: true
                            },{ 
                                 key : 'operate',
                                 name : '操作',
                                 align : 'center',
                                 items : [  {
                                	 className : 'btn-detail',
                                     text: '详情',
                                     show : true,
                                     href : ykyUrl._this + '/basicMaterial/materialdetails.htm?id={id}',  //物料详情链接  
                                     target : '_blank'
                                 },
                                 {
                                     className : 'btn-edit',
                                     text: '编辑',
                                    // show : "'{audit}'!='0'  && '{from}'!='digikey' && '{from}'!='mouser'",  //待审核不能编辑 
                                     show :  "'{audit}'!='0'",    //物料管理均可编辑 
                                     href :  ykyUrl._this + '/basicMaterial/modify.htm?id={id}&from=manage',     //物料编辑 
                                     target : '_blank'
                                 },
                                 {
                                     className : 'btn-commodity',
                                     text: '商检',
                                    // show : "'{audit}'!='0'  && '{from}'!='digikey' && '{from}'!='mouser'",  //待审核不能编辑 
                                     show :  "'{audit}'!='0'",    //物料管理均可编辑 
                                     href :  ykyUrl._this + '/basicMaterial/commodity.htm?id={id}&from=manage',     //物料编辑 
                                     target : '_blank'
                                 } ]
                             }],       	
                        pageflag : true, //是否显示分页
                        refresh : false, //重载 
                        checkflag :true,
                        descr:''    //不通过审核的描述  

                    }
				},
				mounted: function () {
				  setTimeout(() => this.onSearchClick(this.formData.data), 500)
				},
				methods : {
                    onAddNewClick: function (e) {
                        sessionStorage.setItem('searchingFlag', '1')
                    },
                    onSearchClick: function (formdata) {
						var queryParams = this.queryParams
						
                        

                        for (var key in formdata) {
                        	queryParams[key] = formdata[key]
						}
                        //校验型号
                        if(queryParams.manufacturerPartNumber !='' && queryParams.manufacturerPartNumber.length < 3  ){
							return 
						}
                        queryParams.pageSize = 20;
                        queryParams.page = 1;
                        queryParams.defaultStatus = !queryParams.defaultStatus     //组件监听参数变化的标识 
                        LS.set("bulletinPageQueryParams",JSON.stringify(queryParams));

                        sessionStorage.setItem('searching', JSON.stringify(formdata))
					},
					exportMaterial: function(){  //导出CheckBox 
						var checkarray = this.$refs.gridTable.getChecked();    //选择导出物料集合，需转为字符串
						var addList = new Array();
						  if(checkarray.length > 0 ){
							  $.each(checkarray,function(index,ele){
								  addList.push(ele.id);
							  })
						  }
						var addString = addList.join(',');
						
						    if(checkarray.length > 0){
						    	$.aAjax({//导出 
									url : ykyUrl.product + "/v1/products/stand/excel/download?ids="+addString,
									type : 'GET',
									success : function(response,msg,res) {
										url = ykyUrl._this+'/download.htm';
										if(res.status === 200 ){
											layer.open({
												  type: 1,
												  area: ["360px", "240px"],
												  anim: 2,
												  skin: "up_skin_class",
												  btn: ['确认'],
												  content: '<p style="margin:20px;">您的下载任务已经在进行中,请稍后在运营后台的<a href='+url+'>下载中心->我的下载</a>进行查看或者下载<p>',
												  yes: function(index, layero){
														  layer.closeAll();
													}
											
											});
										}
									},
									error : function(data) {
										err = data.responseJSON.errCode;
										layer.msg(err,{icon:2,offset:120});
									}
								})
						    	/*syncData( //导出 
						        		ykyUrl.product + "/v1/products/stand/excel/download?ids="+addString,
						        			'GET',
						        			null,
						        			function(res,err) {//res是阿里云地址 
						        			if(res){
						        				//window.location.href = res;    弹出文案
						        			}
						        			
								             }
						            )*/
						    }else{
						    	layer.msg('请选择需导出的物料', {time: 3000});
						    }
						
						 
						
					},
					exportAll:function(){//全部导出
						var  flag =false;
						var  paramObj = vm.queryParams;
						var  condition = $.param(vm.queryParams);
						for (var key in paramObj) {
							if( key != 'pageSize' && key != 'page' && key !='defaultStatus'  ){
								if(paramObj[key] === ""  ){
									flag = false
								}else{
									flag = true;
									break;
								}
							}
				        }
                      
                        if(flag){
            		    	$.aAjax({//全部导出 
								url : ykyUrl.product + "/v1/products/stand/excel/download?"+ condition,
								type : 'GET',
								success : function(response,msg,res) {
									url = ykyUrl._this+'/download.htm';
									if(res.status === 200){
										layer.open({
											  type: 1,
											  area: ["360px", "240px"],
											  anim: 2,
											  skin: "up_skin_class",
											  btn: ['确认'],
											  content: '<p style="margin:20px;">您的下载任务已经在进行中,请稍后在运营后台的<a href='+url+'>下载中心->我的下载</a>进行查看或者下载<p>',
											  yes: function(index, layero){
													  layer.closeAll();
													  }
										
										});
									}
								},
								error : function(data) {
									err = data.responseJSON.errCode;
									layer.msg(err,{icon:2,offset:120});
								}
							})
                        	/*syncData( //导出 
    				        		ykyUrl.product + "/v1/products/stand/excel/download?"+ condition,
    				        			'GET',
    				        			null ,
    				        			function(res,err) {
    				        			if(res){
    				        				window.location.href = res;
    				        			}
    				        		
    						           }
    				         )*/
                        }else{
                        	layer.msg('请筛选要导出的数据（不超过20w条）', {time: 3000});
                        }
					
					
						
					},
					goUpload:function(){  //自定义回调函数，可选
				           window.location = ykyUrl._this + '/basicMaterial/upload.htm';
				        }
				 }
					  
					  
					   
					
				
			});
 
		var queryParams  = LS.get("bulletinPageQueryParams");
		if(queryParams && LS.get("bulletinEditCancel")){
			var params = JSON.parse(queryParams);	
			vm.queryParams= $.extend({},vm.queryParams, params);
			vm.queryParams.pageSize = params.pageSize;
			vm.queryParams.page = params.page;
			vm.queryParams.status = params.status || "";
			vm.queryParams.manufacturerPartNumber = params.manufacturerPartNumber || "";
			vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus;	
		}
		LS.remove("bulletinPageQueryParams");
		LS.remove("bulletinEditCancel");
		
	
});
