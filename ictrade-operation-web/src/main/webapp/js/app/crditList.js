var vm1 = '';
var ishide = 0;

$(function() {
	vm1 = new Vue(
			{
				el : '#enterprise-list',
				data : { 
                    name: 'actions',
                    isActive:0,
	                url: ykyUrl.workflow+'/v2/apply?processId=ORG_ACCOUNT_PERIOD_REVIEW&status=WAIT_APPROVE', // 访问数据接口
                   // url:'http://192.168.3.184:27090/v2/apply?processId=ORG_ACCOUNT_PERIOD_REVIEW&status=WAIT_APPROVE',
					queryParams : { // 请求接口参数
							size : 10, // 分页参数
							page : 1, // 当前页
							// status : '', //审核状态
							applyContentJsonObj:'',
							applyId:'',
							reviewUserName:'',
							processNodeName:'',
							startDate:'',
							endDate:'',
			                defaultStatus: true // 监测参数变化标识
						},
			           formData1: {
			        	   id: 'seachForm',
		                   name: 'seachForm',
                           columnCount: 3,
                           fields:[            // 配置控件
                                {
                                   keyname: "审核编码 ",           // 控件文本
                                   type: "text",             // 控件类型
                                   key: "applyId",          // 控件名
                                   name: "applyId"          // 控件name
                               },{
                          		 "keyname": "申请时间",
                                 "type": "daterange",   		// 区间类型
                                 "startkey": "createDateStart",   // lastUpdateDate
                                 "endkey": "createDateEnd",  		// end id
                                 "startname": "createDateStart",  // star name
                                 "endname": "createDateEnd" , 		// end name
                                 "dateRange":{},
                                 
                               },{
                                   keyname: "公司名称",          
                                   type: "text",             
                                   key: "companyName",         
                                   name: "companyName"        
                               },  //partyCode
                                {
                                   keyname: "YKY客户编码",          
                                   type: "text",             
                                   key: "partyCode",         
                                   name: "partyCode"        
                               },
                               /*{
                                   keyname: "审核人  ",          
                                   type: "text",             
                                   key: "reviewUserName",         
                                   name: "reviewUserName"        
                               },*/{
                                   keyname: "审核状态",           // 控件文本
                                   type: "select",             // 控件类型
                                   key: "processNodeName",          // 控件名
                                   name: "processNodeName",         // 控件name
                                   options: [{
                                	    value: '',
                                	    text: '全部'
                                	},{
                                	    value: '账期申请初审',
                                	    text: '待初审 '
                                	},{
                                	    value: '账期申请复核',
                                	    text: '待复核'
                                	},{
                                	    value: '账期申请签批',  // APPROVED 通过
                                	    // REJECT 不通过
                                	    text: '待签批'
                                	}]
                                
                               }
                              
                           ],
                           buttons: [
                                     {
                                         "text": '提交',
                                         "nativeType": 'submit',
                                         "type": 'danger',
                                         "id": '',
                                         "name": '',
                                         "size":'sm'
                                     }
                                 ]
                         
                       },
                       formData2: {
			        	   id: 'seachForm2',
		                   name: 'seachForm2',
                           columnCount: 3,
                           fields:[            // 配置控件
                                {
                                   keyname: "审核编码 ",           // 控件文本
                                   type: "text",             // 控件类型
                                   key: "applyId",          // 控件名
                                   name: "applyId"          // 控件name
                               },{
                                   keyname: "公司名称",          
                                   type: "text",             
                                   key: "companyName",         
                                   name: "companyName"        
                               },{
                            		 "keyname": "审核时间",
                                     "type": "daterange",   		// 区间类型
                                     "startkey": "applyDateStart",   // lastUpdateDate
                                     "endkey": "applyDateEnd",  		// end id
                                     "startname": "applyDateStart",  // star name
                                     "endname": "applyDateEnd" , 		// end name
                                     "dateRange":{}
                                },
                                 {
                                    keyname: "YKY客户编码",          
                                    type: "text",             
                                    key: "partyCode",         
                                    name: "partyCode"        
                                },
                                {
                                   keyname: "审核人  ",          
                                   type: "text",             
                                   key: "reviewUserName",         
                                   name: "reviewUserName"        
                                },{
                                   keyname: "审核状态",           // 控件文本
                                   type: "select",             // 控件类型
                                   key: "processNodeName",          // 控件名
                                   name: "processNodeName",         // 控件name
                                   options: [{
                                	    value: '',
                                	    text: '全部'
                                	},{
                                	    value: '账期申请初审',
                                	    text: '待初审 '
                                	},{
                                	    value: '账期申请复核',
                                	    text: '待复核'
                                	},{
                                	    value: '账期申请签批',  
                                	    text: '待签批'
                                	},{
                                        value: 'APPROVED',
                                        text: '通过'
                                    },{
                                        value: 'REJECT',
                                        text: '不通过'
                                    }]
                                
                               }
                              
                           ],
                           buttons: [
                                     {
                                         "text": '提交',
                                         "nativeType": 'submit',
                                         "type": 'danger',	
                                         "id": '',
                                         "name": '',
                                         "size":'sm'
                                     }
                                 ]
                         
                       },
					gridColumns : [   // 表格列   partyCode
					    {
							key : 'partyCode',
							align : 'center',
							name : 'YKY客户编码',
							default : '-',
							cutstring: true
						},
						{
							key : 'applyId',
							align : 'center',
							name : '审核编码',
							default : '-',
							cutstring: true
						}, 
						{
							key : 'applyContentJsonObj',
							align : 'center',
							name : '公司名称',
							child : [{
								key: 'name',
								default : '-',
								cutstring: true
							}],
							cutstring: true,
							callback:{
								action : 'showTitle',
								params : [ 'N','','{name}' ]
							}
						}, 
						{
							key : 'applyContentJsonObj',
							align : 'center',
							cutstring: true,
							default : '-',
							name : '申请人',
							child : [{
								key: 'contactUserName',
								default : '-',
								cutstring: true
							}]
							},
							{
								key : 'applyContentJsonObj',
								align : 'center',
								cutstring: true,
								name : '邮箱',
								child : [{
									key: 'mail',
									cutstring: true,
									default : '-'
								}]
							},  
							{
								key : 'processName',
								align : 'center',
								cutstring: true,
								name : '申请类型',
								default : '-'
							},
							{
								key : 'createdDate',
								align : 'center',
								cutstring: true,
								name : '申请时间',
								default : '-'
							}, 
							{
								key : 'lastUpdateDate',
								align : 'center',
								cutstring: true,
								hide : !ishide,
								name : '审核时间',
								default : '-'
							}, 
							{
								key : 'processNodeName',
								align : 'center',
								name : '审核状态',
								cutstring: true,
							    render: function (h, params) {
							    	var status = params.row.status ;
							        var processNodeName = params.row.processNodeName ;
							        var str = '';
							        
							        if(status == 'APPROVED'){
							        	str = '通过'	
							        }else if(status == 'REJECT'){
							        	str = '不通过'	
							        }else if(processNodeName == '账期申请初审'){
							        	str = '待初审'
							        }else if(processNodeName == '账期申请复核'){
							        	str = '待复核'
							        }else if(processNodeName == '账期申请签批'){
							        	str = '待签批'
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
								},  
								{
									key : 'reason',
									align : 'center',
									cutstring: true,
									name : '审核意见',
									hide : !ishide,
									render: function(h,params){
										var status = params.row.status,str;
										var processNodeName =  params.row.processNodeName ;
										if(processNodeName =='账期申请初审' && status !='REJECT'){
											str = '-'
										}else{
											str = params.row.reason ? params.row.reason : '-';
										}
										return h('span',{
											'class': {
												cutstring: true
												
			                                },
			                                attrs: {
			                                    title: str
			                                  },
			                            },str);
									}
									},
									{
										key : 'reviewUserName',
										align : 'center',
										cutstring: true,
										hide : !ishide,
										name : '审核人',
										default : '-'
									}, 
									{
										key : 'operate',
										align : 'center',
										name : '操作',
										items: [ {
											className : 'btn-detail',
											text : '详情',
											target: '_blank',
											show : true,
											callback: {
												action: 'toDetail',
												params: ['{applyContentJsonObj}', '{applyOrgId}', '{processId}', '{applyId}']
											}
										}, {
											className : 'btn-examine',
											show : "'{dataAll}' !== 'Y' || '{dataAll}' == 'undefined' ",
											text : '审核',
											callback: {
												action: 'toExamine', //applyId   流程id
												params : [ '{applyContentJsonObj}', '{applyOrgId}', '{processId}', '{applyId}' ]  // applyOrgId , 企业ID , processId , 申请类型（企业资质、子账号管理） ,
											}
										}]
									}],
									pageflag : true, // 是否显示分页
									showTotal: true, // 显示列表总数
									refresh : false  // 重载
								},
								created(){
									// 请求接口 赋值给data对象
								   var	 getFormData = function(){
									   
								   }
								},
				methods : {
					changeColumn: function (n) {     // 修改列
	                                    // 隐藏标识 0  待审核  1全部申请 
						 ishide = n;
	                    this.handleHide();
	                },
					 handleHide: function () {   // 判断列隐藏
		                    var cols=this.gridColumns;
		                    for(var i =0; i<cols.length; i++){
		                        if(undefined!=this.gridColumns[i].hide){// 列隐藏设置
		                           this.gridColumns[i].hide=ishide==0;  // 列隐藏设置
		                        }
		                        if(this.gridColumns[i].key=='operate'){// 指定列
		                          // this.gridColumns[i].hide=ishide==1;//设置显示、隐藏
		                        }
		                     }
		                },
				     /* 搜索方法 */
	                formSubmit: function(params) {
	                	console.log(111);
	                    var that = this;
	                    this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
	                    for (var i in params) {
	                        that.queryParams[i] = params[i];
	                    }
	                },
	                
	                setTab: function (n) {
	                	    
		                	this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
	                	
	                    this.isActive=n;
	                    this.changeColumn(n);
	                    if(n==1){
	                        this.url= ykyUrl.workflow + '/v2/apply/myApply?processId=ORG_ACCOUNT_PERIOD_REVIEW&s='+n;
	                    }else{
	                   	     this.url= ykyUrl.workflow + '/v2/apply?processId=ORG_ACCOUNT_PERIOD_REVIEW&status=WAIT_APPROVE&s='+n;
	                    }
	                    
	                   
	                    var queryParams = this.queryParams;
	                	 for (var key in  queryParams) {
	                		 if(key !=  'defaultStatus' )
	                		 queryParams[key] = "";   // 切换tab时清空参数
	                     }
	                	 this.queryParams.size = 10 ; // 分页参数
	                	 this.queryParams.page = 1; // 当前页
	                	    $('#applyId').val('');
		                	$('#applyContentJsonObj').val('');
		                	$('#reviewUserName').val('');
		                	$('#processNodeName').val('');
		                	$('#startDate').val('');
		                	$('#endDate').val('')
	                	
	                }
				}
			});
	
	
	
		
});

// 操作-跳转审核页面
function toExamine(index, params) {
	var id = params[0].entUserId;
	var entId = params[1];
	var type = params[2];
	var applyId = params[3];
	if(type == 'ORG_DATA_REVIEW'){
		var newHref = ykyUrl._this + '/credit.htm?action=examineQualifi&id='+ id +'&entId=' + entId + '&type=' + type + '&applyId=' + applyId ;
		window.open(newHref);
	}else if(type == 'ORG_PROXY_REVIEW'){
		var newHref = ykyUrl._this + '/credit.htm?action=examineSubAccount&id='+ id +'&entId='+ entId + '&type=' + type + '&applyId=' + applyId ;
		window.open(newHref);
	}else if(type == 'ORG_ACCOUNT_PERIOD_REVIEW'){
		var newHref = ykyUrl._this + '/credit.htm?action=examineAccountPeriod&id='+ id +'&entId='+ entId + '&type=' + type + '&applyId=' + applyId;
		window.open(newHref);
	}
}

// 操作-跳转详情页面
function toDetail(index, params) {
	var id = params[0].entUserId;
	var entId = params[1];
	var type = params[2];
	var applyId = params[3];
	if(type == 'ORG_DATA_REVIEW'){
		var newHref = ykyUrl._this + '/credit.htm?action=examineQualifiDetail&id='+ id +'&entId=' + entId + '&type=' + type + '&applyId=' + applyId ;
		window.open(newHref);
	}else if(type == 'ORG_PROXY_REVIEW'){
		var newHref = ykyUrl._this + '/credit.htm?action=examineSubAccountDetail&id='+ id +'&entId=' + entId + '&type=' + type + '&applyId=' + applyId;
		window.open(newHref);
	}else if(type == 'ORG_ACCOUNT_PERIOD_REVIEW'){
		var newHref = ykyUrl._this + '/credit.htm?action=examineAccountPeriodDetail&id='+ id +'&entId='+ entId + '&type=' + type + '&applyId=' + applyId ;
		window.open(newHref);
	}
}

function showTitle(index ,params){
	var title = new Array();
	if(params[0]=='Y'){
		title.push("<span style='color:blue;'>[置顶] </span>");
	}
	title.push("<span ");
	if(params[1] == 'RED'){
		title.push("style='color:red;'");
	}
	title.push(" title='"+params[2]+"'>"+params[2].substr(0,20));
	if(params[2].length>=20){
		title.push("...");
	}
	title.push("</span>");
	return title.join('');
}
