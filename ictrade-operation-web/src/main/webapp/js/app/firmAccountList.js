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
		return result
	}

	vm = new Vue(
			{
				el : '#enterprise-list',
				data : function () {
					return 	{
                        formData: {
                            url:'',    //form提交请求接口，可选
                            id:'seachForm',
                            data: getFormData(),            //初始化数据
                            columnCount: 3,
                            fields:[            //配置控件
                                 {
                                    keyname: "邮箱 ",           //控件文本
                                    type: "text",             //控件类型
                                    key: "mail",          //控件名
                                    name: "mail"         //控件name
                                },
                                {
                                    keyname: "联系人",          
                                    type: "text",             
                                    key: "contactUserName",         
                                    name: "contactUserName"        
                                },
                                {
                                    keyname: "手机号码 ",        
                                    type: "text", 
                                    key: "contactUserTel",
                                    name: "contactUserTel"
                                },
                                {
                                    keyname: "公司名称 ",       
                                    type: "text", 
                                    key: "name",
                                    name: "name"
                                },
                                {
                                    keyname: "账号状态",           //控件文本
                                    type: "select",             //控件类型
                                    key: "status",          //控件名
                                    name: "status",         //控件name
                                    options: [{
                                        value: '',
                                        text: '全部'
                                    },{
                                        value: 'PARTY_ENABLED',
                                        text: '有效'
                                    },{
                                        value: 'PARTY_DISABLED',
                                        text: '冻结'
                                    },{
                                        value: 'PARTY_NOT_VERIFIED',
                                        text: '未激活'
                                    }]
                                },
                                {
                                    keyname: "认证状态",           //控件文本
                                    type: "select",             //控件类型
                                    key: "verifyStatus",          //控件名
                                    name: "verifyStatus",         //控件name
                                    options: [{
                                        value: '',
                                        text: '全部'
                                    },{
                                        value: 'WAIT_APPROVE',
                                        text: '待审核'
                                    },{
                                        value: 'PARTY_VERIFIED',
                                        text: '通过'
                                    },{
                                        value: 'REJECTED',
                                        text: '不通过'
                                    },{
                                        value: 'INVALID',
                                        text: '失效'
                                    },{
                                        value: 'PARTY_NOT_VERIFIED',
                                        text: '未认证'
                                    }]
                                },
                                {
                                    keyname: "主账号类型",           //控件文本
                                    type: "select",             //控件类型
                                    key: "accountsStatus",          //控件名
                                    name: "accountsStatus",         //控件name
                                    options: [{
                                        value: '',
                                        text: '全部'
                                    },{
                                        value: 'MAIN',
                                        text: '主账号'
                                    },{
                                        value: 'SON',
                                        text: '子账号'
                                    },{
                                        value: 'COMMON',
                                        text: '平行账号'
                                    }]
                                },
                                {
                                    keyname: "注册时间",
                                    type: "daterange",   		//区间类型
                                    startkey: "registerStart",   //star id
                                    endkey: "registerEnd",  		//end id
                                    startname: "registerStart",  //star name
                                    endname: "registerEnd" , 		//end name
                                    dateRange:{}
                                },
                                {
                                    keyname: "最后登录时间",
                                    type: "daterange",   		//区间类型
                                    startkey: "lastLoginStart",   //star id
                                    endkey: "lastLoginEnd",  		//end id
                                    startname: "lastLoginStart",  //star name
                                    endname: "lastLoginEnd" , 		//end name
                                    dateRange:{}
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
	                                "inputClass": 'btn btn-danger',
	                                "callback": {
	                                    action: 'go-add'
	                                }
	                            }
	                            ]
                        },
                        url : ykyUrl.party + "/v1/enterprises", //访问数据接口  
                      
                        queryParams : { //请求接口参数
                            pageSize : 20, //分页参数
                            page : 1, //当前页                            
                            defaultStatus : true, 
                            //监测参数变化标识
                        },
                        gridColumns : [ //表格列
                             {
                                key : 'mail',  
                                name : '邮箱',
                                align : 'center',
                                cutstring: true,
                         		default:'--'
                             },
                             {
                                key : 'contactUserName',
                                name : '联系人',
                                align : 'center',
                                cutstring: true,
                         		default:'--'
                             },
                             {
                                 key : 'contactUserTel',
                                 name : '手机号码',
                                 align : 'center',
                                 cutstring: true,
                         		 default:'--'
                              },
                               {
                                   key : 'name',
                                   name : '公司名称',
                                   align : 'center',
                                   cutstring: true,
                           		   default:'--'
                                },
                                {
                                    key : 'status',
                                    name : '账号状态',
                                    align : 'center',
                                    cutstring: true,
                            		default:'--',
	                                  text : {
	                                	  PARTY_ENABLED : {
	                                           'value' : '有效 '
	                                       },
	                                       PARTY_DISABLED : {
	                                           'value' : '冻结 '
	                                       },
	                                       PARTY_NOT_VERIFIED : {
	                                           'value' : '未激活 '
	                                       }
	                                   }
                                 },
                                 {
                                     key : 'activeStatus',
                                     name : '认证状态',
                                     align : 'center',
                                     cutstring: true,
                             		 default:'--',
	                                  text : {
	                                	  WAIT_APPROVE : {
	                                           'value' : '待审核 '
	                                       },
	                                       PARTY_VERIFIED : {
	                                           'value' : '通过 '
	                                       },
	                                       REJECTED : {
	                                           'value' : '不通过 '
	                                       },
	                                       INVALID : {
	                                           'value' : '失效 '
	                                       },
	                                       PARTY_NOT_VERIFIED : {
	                                           'value' : '未认证 '
	                                       }
	                                   }
                                  },
                                  {
                                      key : 'accountsStatus',
                                      name : '账号类型',
                                      align : 'center',
                                      cutstring: true,
                                  	  default:'--',
	                                  text : {
	                                	  MAIN : {
	                                           'value' : '主账号 '
	                                       },
	                                       SON : {
	                                           'value' : '子账号 '
	                                       },
	                                       COMMON : {
	                                           'value' : '平行账号 '
	                                       }
	                                   }	
                                   },
                                   {
                                       key : 'regTime',
                                       name : '注册时间',
                                       align : 'center',
                                       cutstring: true,
                                   	   default:'--'
                                    },
                                    {
                                        key : 'lastLoginTime',
                                        name : '最后登录时间',
                                        align : 'center',
                                        cutstring: true,
                                		default:'--'
                                     },
                               
                            {
                                 key : 'operate',
                                 name : '操作',
                                 align : 'center',
                                 width: '200px', 
                                 items : [  
                                    {                                	 
                                     text: '冻结',
                                     show : "'{status}'=='PARTY_ENABLED'",
                                     callback:{
                                    	 action:'frozenAccount',
                                    	 params:['{mail}','{id}','{status}']
                                     }
                                    },
                                    {                                	 
                                        text: '取消冻结',
                                        show : "'{status}'=='PARTY_DISABLED'",
                                        callback:{
                                       	 action:'frozenAccount',
                                       	 params:['{mail}','{id}','{status}']
                                        }
                                       },
                                   {                                	 
                                       text: '详情',
                                       show : true,
                                       href : 'enterprise.htm?action=detail&id={id}&corporationId={corporationId}',
                                       target : '_blank'
                                      },
                                      {                                	 
                                          text: '编辑',
                                          show : true,
                                          href : 'enterprise.htm?action=entEdit&id={id}&corporationId={corporationId}',
                                          target : '_blank'
                                         },
                                         {                                	 
                                             text: '补发邮件',
                                             show : "'{status}'=='PARTY_NOT_VERIFIED'",
                                             callback:{
                                            	 action:'resendMail',
                                            	 params:['{mail}']
                                             }
                                            },
                                            {                                	 
                                                text: '建档记录',
                                                show : "'{vipId}'",
                                                href : 'certificationEntKf.htm?action=detail&id={vipId}',
                                                target : '_blank'
                                               },
                                    ]
                             }],       	
                        pageflag : true, //是否显示分页
                        refresh : false, //重载 
                        //checkflag :true,
                        showTotal: true,
                       // descr:''    //不通过审核的描述  

                    }
				},
				mounted: function () {
				  setTimeout(() => this.onSearchClick(this.formData.data), 500)
				},
				methods : {
                    onSearchClick: function (formdata) {
						var queryParams = this.queryParams
                        queryParams.pageSize = 10;
                        queryParams.page = 1;
                        queryParams.defaultStatus = !queryParams.defaultStatus
                        

                        for (var key in formdata) {
                        	queryParams[key] = formdata[key]
						}

                        LS.set("bulletinPageQueryParams",JSON.stringify(queryParams));

                        sessionStorage.setItem('searching', JSON.stringify(formdata))
					},
					exportExcels: function() {
						var date_url = ykyUrl._this + "/enterprise/excel.htm" + window.location.search;
						$("input[name=Authorization]").val($('#pageToken').val());
						var form = $("#exportForm"); //定义一个form表单
						form.attr("action", date_url);
						form.submit(); //表单提交
					},
					goAdd:function(){
						 window.location = ykyUrl._this + '/enterprise.htm?action=entAdd';
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
function frozenAccount(index, params){
	var mail = params[0] ? params[0] : '';
	var id = params[1];
	var partyStatus = params[2]=='PARTY_ENABLED'?'PARTY_DISABLED':'PARTY_ENABLED';	
	layer.confirm(params[2] =='PARTY_ENABLED'?'确定冻结  '+ mail +'?' : '确定取消冻结  '+ mail +'?',{
		offset: "auto",
		closeBtn: false,
		btn: ['确      定','取      消'], //按钮
		title: " "/*status =='PARTY_ENABLED'?'冻结':'取消冻结'*/,
		area: 'auto',			
		move: false,
		skin: "up_skin_class",
		yes:function(){
			$.aAjax({
				url:ykyUrl.party + '/v1/customers/frozen?accountId=' + id + '&partyStatus=' + partyStatus,
				type:'POST',
				success:function(data){
					vm.refresh = !vm.refresh;
					layer.closeAll();
				},
				error:function(res){
					layer.msg("网络异常，请稍后重试！");
				}
			})
		}
	})
}
function resendMail(index,params){
	var mail = params[0]
	$.aAjax({
		url:ykyUrl.party + '/v1/customer/reSend',
		type:'POST',
		data:JSON.stringify({
            mail: mail,
            opreationSystem:1
        }),
        success:function(data){
        	if(data == 'success'){
        		layer.msg('发送成功');
        	}
        },
        error:function(){
        	console.log('error');
        }
	})
}