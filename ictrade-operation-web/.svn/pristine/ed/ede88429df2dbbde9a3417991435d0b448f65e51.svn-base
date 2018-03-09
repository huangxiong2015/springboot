var vm = '';
$(function() {
	
	vm = new Vue(
			{
				el : '#enterprise-list',
				data : {
					url : ykyUrl.workflow + "/v2/apply", //访问数据接口 
					queryParams : { //请求接口参数 
						size : 10, //分页参数
						page : 1, //当前页
						processIdList : 'ORG_DATA_REVIEW,ORG_PROXY_REVIEW',//ORG_ACCOUNT_PERIOD_REVIEW', //申请类型
						status : '', //审核状态
						defaultStatus : true //监测参数变化标识
					},
					gridColumns : [ //表格列
	                /*{
						key : 'id',
						name : 'userId',
						hide : 'true'
					}, */
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
						/*text : {
							ORG_DATA_REVIEW : {
								'value': '企业资质'
							},
							ORG_PROXY_REVIEW : {
								'value': '子账号管理'
							},
							ORG_ACCOUNT_PERIOD_REVIEW : {
								'value': '账期'
							}
						}*/	
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
						name : '审核时间',
						default : '-'
					}, 
					{
						key : 'status',
						align : 'center',
						name : '审核状态',
						cutstring: true,
						text : {
							APPROVED : {
								'value': '通过'
							},
							REJECT : {
								'value': '不通过'
							},
							WAIT_APPROVE : {
								'value': '待审核'
							}
						}
					},  
					{
						key : 'reason',
						align : 'center',
						cutstring: true,
						name : '审核意见',
						render: function(h,params){
							var status = params.row.status,str;
							if(status == 'WAIT_APPROVE'){
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
							show : "'{status}'=='WAIT_APPROVE'",
							text : '审核',
							callback: {
								action: 'toExamine',
								params : [ '{applyContentJsonObj}', '{applyOrgId}', '{processId}', '{applyId}' ]  //applyOrgId 企业ID  processId 申请类型（企业资质、子账号管理） applyId 流程id
							}
						}]
					}],
					pageflag : true, //是否显示分页
					showTotal: true, //显示列表总数
					refresh : false
				//重载 

				},
				methods : {
					//搜索方法
					onSearch : function() {
						var that = this;
						var qModel = $('#seachForm').serializeObject();
						this.queryParams.size = 10;
						this.queryParams.page = 1;
						this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
						for ( var i in qModel) {
							that.queryParams[i] = qModel[i];
						}
						LS.set("queryParams",JSON.stringify(that.queryParams));
					}
				}
			});
	
		var queryParams  = LS.get("queryParams");
		if(!queryParams){
			var params = queryParams ? JSON.parse(queryParams) : {};
			vm.queryParams.size = params.size || 10;
			vm.queryParams.page = params.page || 1;
			vm.queryParams.applyId = params.applyId || "";
			vm.queryParams.name = params.name || "";
			vm.queryParams.status = params.status || "";
			vm.queryParams.processIdList = params.processIdList || "ORG_DATA_REVIEW,ORG_PROXY_REVIEW";//,ORG_ACCOUNT_PERIOD_REVIEW
			vm.queryParams.reviewUserName = params.reviewUserName || "";
			vm.queryParams.createDateStart = params.createDateStart || "";
			vm.queryParams.createDateEnd = params.createDateEnd || "";
			vm.queryParams.applyDateStart = params.applyDateStart || "";
			vm.queryParams.applyDateEnd = params.applyDateEnd || "";	
		}
		
		//申请时间
		$('#createDate .input-daterange').datepicker({
		    format: config.dateFormat,
		    autoclose: true
		});
		
		//截止时间
		$('#applyDate .input-daterange').datepicker({
		    format: config.dateFormat,
		    autoclose: true
		});
		
});

//操作-跳转审核页面
function toExamine(index, params) {
	var id = params[0].entUserId;
	var entId = params[1];
	var type = params[2];
	var applyId = params[3];
	if(type == 'ORG_DATA_REVIEW'){
		var newHref = ykyUrl._this + '/verify.htm?action=examineQualifi&id='+ id +'&entId=' + entId + '&type=' + type + '&applyId=' + applyId;
		window.open(newHref);
	}else if(type == 'ORG_PROXY_REVIEW'){
		var newHref = ykyUrl._this + '/verify.htm?action=examineSubAccount&id='+ id +'&entId='+ entId + '&type=' + type + '&applyId=' + applyId;
		window.open(newHref);
	}else if(type == 'ORG_ACCOUNT_PERIOD_REVIEW'){
		var newHref = ykyUrl._this + '/verify.htm?action=examineAccountPeriod&id='+ id +'&entId='+ entId + '&type=' + type + '&applyId=' + applyId;
		window.open(newHref);
	}
}

//操作-跳转详情页面
function toDetail(index, params) {
	var id = params[0].entUserId;
	var entId = params[1];
	var type = params[2];
	var applyId = params[3];
	if(type == 'ORG_DATA_REVIEW'){
		var newHref = ykyUrl._this + '/verify.htm?action=examineQualifiDetail&id='+ id +'&entId=' + entId + '&type=' + type + '&applyId=' + applyId;
		window.open(newHref);
	}else if(type == 'ORG_PROXY_REVIEW'){
		var newHref = ykyUrl._this + '/verify.htm?action=examineSubAccountDetail&id='+ id +'&entId=' + entId + '&type=' + type + '&applyId=' + applyId;
		window.open(newHref);
	}else if(type == 'ORG_ACCOUNT_PERIOD_REVIEW'){
		var newHref = ykyUrl._this + '/verify.htm?action=examineAccountPeriodDetail&id='+ id +'&entId='+ entId + '&type=' + type + '&applyId=' + applyId;
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
