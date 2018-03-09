var vm = '';
$(function() {
	
	vm = new Vue(
		{
			el : '#customer-list',
			data : {
				url : ykyUrl.party + "/v1/customers", //访问数据接口 
				queryParams : { //请求接口参数 
					size : 10, //分页参数
					page : 1, //当前页
					status : '', //账号状态
					defaultStatus : true //监测参数变化标识
				},
				gridColumns : [ //表格列
                /*{
					key : 'id',
					name : 'userId',
					hide : 'true'
				}, */
				{
					key : 'mail',
					align : 'center',
					name : '邮箱',
					default : '-',
					cutstring: true
				}, 
				{
					key : 'name',
					align : 'center',
					name : '联系人',
					default : '-',
					cutstring: true
				}, 
				{
					key : 'tel',
					align : 'center',
					cutstring: true,
					default : '-',
					name : '手机号码'
				}, 
				{
					key : 'statusId',
					align : 'center',
					cutstring: true,
					name : '账号状态',
					text : {
						PARTY_ENABLED : {
							'value': '有效'
						},
						PARTY_DISABLED : {
							'value': '冻结'
						}
					}	
				},
				{
					key : 'regTime',
					align : 'center',
					cutstring: true,
					default : '-',
					name : '注册时间'
				}, 
				{
					key : 'lastLoginTime',
					align : 'center',
					cutstring: true,
					default : '-',
					name : '最后登录时间'
				},
				{
					key : 'operate',
					align : 'center',
					name : '操作',
					items: [ {
						className : 'btn-detail',
						text : '详情',
						show : true,
						target: '_blank',
						href : ykyUrl._this + '/enterprise/customers.htm?action=detail&id=' + '{partyId}'
					}, {
						className : 'btn-upgrade ml5',
						text : '升级企业',
						show : true,
						href : ykyUrl._this + '/enterprise/customers.htm?action=personUpEnt&id=' + '{partyId}'
					}, {
						className : 'btn-frozen ml5',
						show : "'{statusId}'=='PARTY_ENABLED'",
						text : '冻结',
						callback: {
							action : 'frozenAccount',
							params : [ '{partyId}', '{tel}','{statusId}' ]
						}
					}, {
						className : 'btn-cancelFrozen ml5',
						show : "'{statusId}'=='PARTY_DISABLED'",
						text : '取消冻结',
						callback: {
							action : 'frozenAccount',
							params : [ '{partyId}', '{tel}','{statusId}' ]
						}
					}]
				}],
				pageflag : true, //是否显示分页
				showTotal: true, //显示列表总数
				refresh : false //重载 
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
				},
				//导出表格
				exportExcels: function() {
					var date_url = ykyUrl._this + "/enterprise/customers/excel.htm?" + $('#seachForm').serialize();
					//var date_url = ykyUrl._this + "/customers/excel.htm?status=PARTY_ENABLED";
					$("input[name=Authorization]").val($('#pageToken').val());
				    var form=$("#exportForm");//定义一个form表单
				    form.attr("action",date_url);
				    form.submit();//表单提交
				}
			}
		});

	//从其他页面返回能保留搜索条件
	var queryParams  = LS.get("queryParams");
	if(!queryParams){
		var params = queryParams ? JSON.parse(queryParams) : {};
		vm.queryParams.size = params.size || 10;
		vm.queryParams.page = params.page || 1;
		vm.queryParams.mail = params.mail || "";
		vm.queryParams.name = params.name || "";
		vm.queryParams.status = params.status || "";
		vm.queryParams.registerStart = params.registerStart || "";
		vm.queryParams.registerEnd = params.registerEnd || "";
		vm.queryParams.lastLoginStart = params.lastLoginStart || "";
		vm.queryParams.lastLoginEnd = params.lastLoginEnd || "";	
	}
	
	//激活日期控件
	
	//注册时间
	$('#registerDate .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true
	});
	
	//最后登录时间
	$('#lastLoginDate .input-daterange').datepicker({
	    format: config.dateFormat,
	    autoclose: true 
	});
		
});


//冻结/取消冻结账号
function frozenAccount(index, params){
	var id = params[0];
	var tel = params[1];
	var partyStatus = params[2] == 'PARTY_ENABLED' ? 'PARTY_DISABLED' : 'PARTY_ENABLED';
	layer.confirm(partyStatus =='PARTY_ENABLED'?'确定取消冻结  '+ tel +'?' : '确定冻结  '+ tel +'?',{
		offset: "auto",
		closeBtn: false,
		btn: ['确      定','取      消'], //按钮
		title: " ",
		area: 'auto',			
		move: false,
		skin: "up_skin_class",
		yes:function(){
			$.aAjax({
				url:ykyUrl.party + '/v1/customers/frozen?accountId=' + id + '&partyStatus=' + partyStatus,
				type:'POST',
				success:function(data){
					location.reload();
				},
				error:function(res){
					layer.msg("网络异常，请稍后重试！");
				}
			})
		}
	})				
}
