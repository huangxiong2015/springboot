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
				el : '#material-review',   //物料审核（审核人员）
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
                                    },
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
                                    keyname: "提交人  ",          
                                    type: "text",             
                                    key: "auditUserName",         
                                    name: "auditUserName"        
                                },
                                {
                                    keyname: "审核状态",           //控件文本
                                    type: "select",             //控件类型
                                    key: "auditStatus",          //控件名
                                    name: "auditStatus",         //控件name
                                    options: [{
                                        value: '',
                                        text: '全部'
                                    },{
                                        value: '0',
                                        text: '待审核  '
                                    },{
                                        value: '1',
                                        text: '未通过  '
                                    }]
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
                            }],
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
                                 key: 'selected',
                                 name: '',
                                 disabled:"'{auditStatus}'==='1' || '{auditStatus}'==='2'",  //通过与未通过不可选择checkbox  
                                 width:'40px'
                              },{
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
                                key : 'categories',     	//次小类有问题  
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
                                key : 'restrictMaterialType',    //限制物料 
                                align : 'center',
                                name : '限制物料 ',
                                align : 'center',
                                width: '120px',
                                render: function (h, params) {
                                    var res=  params.row.restrictMaterialType || '';
                                    var str =  '--';
                            	   var list =[];
                            	    if(res == 'N'){
                            	    	str = '否';
                    			    }else{
                    			       var strlist = res.split('-');
                    			    	$.each(strlist,function(ind,el){
                    			    		if(el == 'F'){
                    			    			list.push('受控');
                    			    		}
                    			    		if(el == 'T'){
                    			    			list.push('关税');
                    			    		}
                    			    		if(el == 'I'){
                    			    			list.push('商检');
                    			    		}
                    			    	})
                    			    	if(list.length == 0){
                    			    		str = '--'
                    			    	}else{
                    			    		str = list.join('、');
                    			    	}
                    			    	
                    			    }
                                   return h('span',{
                                	   'class': {
       										cutstring: true
       									
                                       },
                                       attrs: {
                                           title: str
                                         },
                                   },str) ;
                                }
                             
                            }, {
                                key : 'updateName',
                                name : '提交人 ',
                                align : 'center',
                                default : '--',
                                cutstring: true
                            }, {
                                key : 'updatedTimeMillis',
                                name : '提交时间  ',
                                align : 'center',
                                default : '--',
                                cutstring: true
                            },{
                                key : 'auditStatus',    //限制物料 
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
                             },{ //auditStatus
                                 key : 'operate',
                                 name : '操作',
                                 align : 'center',
                                 items : [  {
                                	 className : 'btn-detail',
                                     text: '详情',
                                     show : true,
                                     href : ykyUrl._this + '/approveMaterial/detail.htm?id={id}',  //物料详情链接  
                                     target : '_blank'
                                 }]
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
					checkPass: function(){
						var checkarray = this.$refs.gridTable.getChecked();    //如果集合为空，提示  显示  请选择未审核的物料
						var addData ={
								  "auditStats":2,
								  "ids": []
								};
						if(checkarray.length > 0){
							$.each(checkarray,function(index,ele){
								addData.ids.push(ele.id);
							})
						}
						
						if(checkarray.length > 0){
							//询问框
								layer.confirm("<p>确认通过审核吗？</p>", {
									  	offset: "auto",
									  	//time:3000,
									  	area:['450px','auto'],
										btn: ['确认 ','取消 '], //按钮
										title: " ",
										shade: 0,
										move: false,
										skin: "up_skin_class",
								}, 
								//确认，调接口 
								function(){
									syncData( //通过审核 
							        		ykyUrl.product + "/v1/products/standaudit/audit",
							        			'PUT',
							        			addData,
							        			function(res,err) {
							        			
								        			 if(err == null){
								        				 layer.closeAll();
								        				 layer.msg('操作成功', {time: 3000}); 
								        				 vm.onSearchClick();
								        			  }
								        			 
											    }
							        		)
								    }, 
								 //取消
								function(){
									layer.closeAll(); 
								});
						}else{
							layer.msg('请选择未审核的物料', {time: 3000});
						}
						
					
					},
					checknoPass:function(){
						var checkarray = this.$refs.gridTable.getChecked();
						 var addData ={
								  "auditStats":1,
								  "descr": "",   //不通过的描述 
								  "ids": []
								};
						 addData.descr = $('.descr').val() ;
						 if(checkarray.length > 0 ){
								$.each(checkarray,function(index,ele){
									addData.ids.push(ele.id);
								})
							};
						 syncData( //不通过审核 
						        		ykyUrl.product + "/v1/products/standaudit/audit",
						        			'PUT',
						        			addData,
						        			function(res,err) {
						        			  if(err == null){
						        				  layer.closeAll();
								        		  layer.msg('操作成功', {time: 3000});
								        		  vm.onSearchClick();
						        			  }
						        			
						        			
								            }
						       )
						
					  },
                   opendescr: function () {//预览  
                	   var checkarray = this.$refs.gridTable.getChecked();
                	   if(checkarray.length >0 ){
                		   var viewHtml=' <div  id="viewContent"><table align="center"><tbody><tr><td><textarea type="text" id="auditOpinion" name="auditOpinion" class="descr" maxlength="100"  style="margin-top:20px;height:120px;width:380px;" > </textarea></td></tr></tbody></table></div>';
     					  
   					  	layer.open({
   					  		  type: 1, 
   					  		  title: '审核意见',
   					  		  move: false,
   					  	      shade: 0.8,
   					  	      area: ['500px','300px'],
   					  	      offset:"80px",
   					  		  content: viewHtml,
   						  	  btn: ['确认', '取消'],
   							  yes: function(index, layero) {
   								if(!$.trim($('#auditOpinion').val())){
									  $('#auditOpinion').css('border-color','#eb0038');
									  $('#auditOpinion').focus();
									  return;
								  }
   								  vm.checknoPass();
   							  },
   							  cancel: function(index, layero) {
   								  layer.closeAll(); 	
   							  }
   					  		});
   					  	$('#auditOpinion').on('blur',function(){
					  		if($.trim($(this).val())){
					  			$(this).css('border-color','#a9a9a9');
					  		}
					  	})
   					 
                	   }else{
                		   layer.msg('请选择未审核的物料', {time: 3000});
                	   }
					  	
					  	
					  },
					  cancel:function(){
						  layer.closeAll(); 
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