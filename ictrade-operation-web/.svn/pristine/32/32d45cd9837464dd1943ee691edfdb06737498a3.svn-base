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
				el : '#material-review',     //物料审核(操作人员)
				data : function () {
					return 	{
                        formData: {
                            url:'',    //form提交请求接口，可选
                            id:'seachForm',
                            data: getFormData(),            //初始化数据
                            columnCount: 3,
                            fields:[            //配置控件
                                 {
                                    keyname: "型号 ",           //控件文本
                                    type: "text",             //控件类型
                                    key: "manufacturerPartNumber",          //控件名
                                    name: "manufacturerPartNumber"  ,        //控件name
                                    "validate": {
                                        "minlength": 3
                                    },       //控件name
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
                                    "keyname": "提交时间",
                                    "type": "daterange",   		//区间类型
                                    "startkey": "startDate",   //star id
                                    "endkey": "endDate",  		//end id
                                    "startname": "startDate",  //star name
                                    "endname": "endDate" , 		//end name
                                    "dateRange":{}
                                },
                                {
                                    keyname: "审核状态",           //审核状态
                                    type: "select",             //控件类型
                                    key: "auditStatus",          //控件名
                                    name: "auditStatus",         //控件name
                                    options: [{
                                        value: '',
                                        text: '全部'
                                    },{
                                        value: '0',
                                        text: '待审核 '
                                    },{
                                        value: '1',
                                        text: '未通过'
                                    }]
                                }/*,{
                                    keyname: "操作人  ",          
                                    type: "text",             
                                    key: "auditUserName",         
                                    name: "auditUserName"        
                                }*/
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
                            }/*,{
                                "text": '新增',
                                "type": 'button',
                                "id": '',
                                "name": '',
                                "inputClass": 'btn upload',
                                "callback": {
                                    action: 'go-upload'
                                }
                            }*/],
                            "message":{ 
                                "manufacturerPartNumber":{
                               	 	minlength:'至少需要输入3个字符'
                               	 }  
                          }
                        },
                        url : ykyUrl.product + "/v1/products/standaudit", //访问数据接口  
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
                                key : 'id',  //物料id  
                                name : 'id',
                                hide : 'true'
                             },{
                                key : 'manufacturerPartNumber',
                                name : '物料型号',
                                align : 'center',
                                cutstring: true
                            }, {
                                key : 'manufacturer',
                                name : '原厂 ',
                                cutstring: true
                            },{
                                key : 'cateName',     	//次小类有问题  
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
                            }/*,{
                                key : 'restrictMaterialType',    //限制物料 
                                align : 'center',
                                name : '限制物料 ',
                                align : 'center',
                                width: '120px',
                                text : {
                                   N: {
                                        'value' : '否  '
                                       },
                                   P: {
                                        'value' : '特殊包装 '
                                    } ,
                                   F: {
                                        'value' : '特殊文件  '
                                    }
                                }
                            }*/,{
                                key : 'updatedTimeMillis',
                                name : '提交时间  ',
                                align : 'center',
                                cutstring: true
                            },{
                                key : 'auditStatus',    //审核状态 
                                align : 'center',
                                name : '审核状态  ',
                                align : 'center',
                                width: '120px',
                                /*text : {
                                   0 : {
                                        'value' : '待审核   '
                                    },
                                   1: {
                                        'value' : '未通过  '
                                    } ,
                                   2: {
                                        'value' : '通过  '
                                    }
                                 }*/
                                render: function (h, params) {
                                    var auditStatus= params.row.auditStatus ;
                                    var  reason = params.row.reason;
                                   
                                    if(auditStatus == '0'){
                                    	str = '待审核';
                                    }
                                    if(auditStatus == '1'){
                                    	str = '未通过';
                                    }
                                    if(auditStatus == '2'){
                                    	str = '通过';
                                    }
                                   return h('span',{
                                	   'class': {
       										cutstring: true
       									
                                       },
                                       attrs: {
                                           title: reason
                                         },
                                   },str) ;
                                }
                               },{ 
                                 key : 'operate',
                                 name : '操作',
                                 align : 'center',
                                 items : [  {
                                	 className : 'btn-detail',
                                     text: '详情',
                                     show : true,
                                     target : '_blank', 
                                     href : ykyUrl._this + '/operateMaterial/detail.htm?id={id}'  //物料详情链接  
                                     
                                 },
                                 {
                                     className : 'btn-edit',
                                     text: '编辑',
                                     target : '_blank', 
                                    // show : "'{auditStatus}'!='0'   && '{from}'!='digikey' && '{from}'!='mouser'",  //待审核不能编辑 
                                     show : "'{auditStatus}'!='0'",
                                     href :  ykyUrl._this + '/operateMaterial/edit.htm?id={id}&from=operation'     //物料编辑 
                                    
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
					exportMaterial: function(){
						var checkarray = this.$refs.gridTable.getChecked();    //选择导出物料集合，需转为字符串
						var addList = new Array();
						  if(checkarray.length > 0 ){
							  $.each(checkarray,function(index,ele){
								  addList.push(ele.id);
							  })
						  }
						var addString = addList.join(',');
						var  condition = $.param(vm.queryParams);
						    if(checkarray.length > 0){
						    	syncData( //导出 
						        		ykyUrl.product + "/v1/products/stand/excel/download?ids="+addString,
						        			'GET',
						        			null,
						        			function(res,err) {//res是阿里云地址 
						        			if(res){
						        			  window.location.href = res;
						        			 }
						        			
								             }
						         )
						    }else{
						    	syncData( //导出 
						        		ykyUrl.product + "/v1/products/stand/excel/download?"+ condition,
						        			'GET',
						        			null ,
						        			function(res,err) {
						        			if(res){
						        			 window.location.href = res;
						        			}
						        			
								           }
						         )
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
