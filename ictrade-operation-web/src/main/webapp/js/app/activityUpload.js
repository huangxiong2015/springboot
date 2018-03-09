/**
 * create by roy.he@yikuyi.com
 * time: 2017.6.8
 */
var vm = new Vue({
	el: '#activity-upload',
	data(){
		return {
			aperList: [],
			fileName: '',
			fileUrl: '',
			query: getQueryString('periodsId'),
			activityId: $("#activityId").val(),
			periodsId: '',
			getTableData: false,
		}
	},
	created: function(){
		syncData(ykyUrl.product + "/v1/activities/"+$("#activityId").val()+"/draft", 'GET', null, function(res,err) {
			if (!err && res !== '') {
				vm.aperList = res.periodsList;
				if(vm.query && vm.query !== ''){
					vm.periodsId = vm.query;
				}else{
					vm.periodsId = vm.aperList[0].periodsId;
				}
				getDataList();
			}
		})
	},
	methods:{
		addProduct: function(){
			layer.open({
				type: 1,
				title: '添加商品',
				area: '700px',
				offset: '300px',
				move: false,
				skin: "s_select_data",
				content: $("#file-upload"),
				btn: ['保存', '取消'],
				yes: function(index, layero) {
					if(vm.fileName === ''){
						layer.msg('请选择文件！')
						return false;
					}
					var index = layer.load(1, {
					  shade: [0.1,'#fff'] //0.1透明度的白色背景
					});
					
					$.aAjax({
						url: ykyUrl.product+'/v1/activities/'+vm.activityId+'/products/parse?periodsId='+vm.periodsId+'&oriFileName='+ vm.fileName+'&fileUrl='+vm.fileUrl,
						type: 'POST',
						success: function(res){
							layer.close(index);
							vm.fileName='';
							vm.fileUrl='';
							vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus;
							layer.closeAll();
							
							vm.$nextTick(function(){
								vm.$children[0].ickeck = [];
								vm.$children[0].isAll = false;
								$('#checkedIds').val('');
								$('.chk_al1').prop('checked', false)
							});
						},
						error: function(err){
							layer.close(index);
							var errorTop = layer.open({
								type: 1,
								title: '错误',
								area: ['700px', '300px'],
								offset: '300px',
								move: false,
								skin: "s_select_data",
								content: err.responseJSON.errMsg,
								btn: ['确定'],
								yes: function(index, layero){
									layer.close(errorTop);
								}
							});
						}
					});
				},
				cancel: function(index, layero) {
					vm.fileName='';
					vm.fileUrl='';
				}
			})
		},
		downloadSelect: function(){
			if($(".table-container tbody tr").length < 0) {
				layer.msg('没有数据可以下载')
				return false;
			}
			var ids = '', checkedItem;
			checkedItem = eval('('+$("#checkedIds").val()+')');
			if(checkedItem.length > 0){
				for(var i =0; i<checkedItem.length; i++){
					if(ids !== ''){
						ids += ','+checkedItem[i].activityProductId
					}else{
						ids = checkedItem[i].activityProductId;
					}
				}
			}else{
				layer.msg('请选择需要下载的数据')
				return false;
			}
			$("input[name=Authorization]").val($('#pageToken').val());
			$("input[name=ids]").val(ids);
			$("input[name=activityId]").val($("#activityId").val());
			$("input[name=periodsId]").val(vm.periodsId);
			$("input[name=status]").val('');
			var date_url = ykyUrl._this + "/activity/export.htm";
			var form=$("#exportForm");//定义一个form表单
		    form.attr("action",date_url);
		    form.submit();//表单提交
			
		},
		downloadError: function(){
			if($(".table-container tbody tr").length < 0) {
				layer.msg('没有数据可以下载')
				return false;
			}
			$("input[name=Authorization]").val($('#pageToken').val());
			$("input[name=ids]").val('');
			$("input[name=status]").val('UNABLE');
			$("input[name=activityId]").val($("#activityId").val());
			$("input[name=periodsId]").val(vm.periodsId);
			var date_url = ykyUrl._this + "/activity/export.htm";
			var form=$("#exportForm");//定义一个form表单
		    form.attr("action",date_url);
		    form.submit();//表单提交
		},
		changePeriods: function(id){
			location.href = location.origin + location.pathname+'?periodsId='+id;
		},
		backPrev: function(){
			location.href = ykyUrl._this + '/timepromotion/information/'+vm.activityId+'.htm?from=upload'
		},
		toNext: function(){
			syncData(ykyUrl.product + "/v1/activities/"+$("#activityId").val()+"/draft", 'GET', null, function(res,err) {
				if (!err && res !== '') {
					if(_.find(res.periodsList, ['status', 'ENABLE'])){
						location.href = ykyUrl._this + '/timepromotion/maintain/'+vm.activityId+'.htm';
					}else{
						layer.msg('请上传商品！')
					}
				}
			});
		},
		multipleDelete:function(){//批量删除
			var that = this;
			var ids = [], checkedItem;
			checkedItem = eval('('+$("#checkedIds").val()+')');												
			if(checkedItem.length > 0){
				for(var i =0; i<checkedItem.length; i++){
					ids.push(checkedItem[i].activityProductId)
				}
			}else{
				layer.msg('请选择需要删除的数据')
				return false;
			}
			var index = layer.confirm('确认删除？',{
				 btn: ['确认','取消'] //按钮
			},function(){
				multiDelete(ykyUrl.product + "/v1/activities/" + that.activityId + "/periods/" + that.periodsId + "/products", 'DELETE', ids,
						function(res, err) {//页面加载前调用方法
							that.queryParams.defaultStatus = !that.queryParams.defaultStatus;
						});
				layer.close(index);
			})
		}
	}
})

var getDataList = function(){
	vm.getTableData = true;
	vm.url = ykyUrl.product + '/v1/activities/'+vm.activityId+'/products/draft',
	vm.queryParams = { //请求接口参数
            defaultStatus: true, //监测参数变化标识
            periodsId: vm.periodsId,
            size: 10,
            page: 1,
        };
	vm.gridColumns = [
	    {key :'selected', name: '',width: '50px'},
	    {key: 'index', name: '序号',width: '50px'},    
	    {key: 'manufacturer', name: '制造商', cutstring: true,textColor:{
	    	condition: "'{status}'=='UNABLE'",
	    	color: 'red'
	    }},
	    {key: 'manufacturerPartNumber', name: '型号', width: '180px', cutstring: true, textColor:{
	    	condition: "'{status}'=='UNABLE'",
	    	color: 'red'
	    }},
	    {key: 'vendorName', name: '分销商', cutstring: true,textColor:{
	    	condition: "'{status}'=='UNABLE'",
	    	color: 'red'
	    }},
	    {key: 'totalQty', name: '库存', width:'90px', cutstring: true},
	    {key: 'sourceName', name: '仓库', cutstring: true,textColor:{
	    	condition: "'{status}'=='UNABLE'",
	    	color: 'red'
	    }},
	    {key: 'currencyUomId', name: '币种', width: '50px', callback:{
			action : 'changeCurrency',
			params : ['{currencyUomId}']
		}},
	    {key: 'qtyBreak', name: '阶梯', endString: '+', type: 'array',width:'100px',textColor:{
	    	condition: "'{status}'=='UNABLE'",
	    	color: 'red'
	    }},
	    {key: 'priceBreak', name: '限时价', type:'array',textColor:{
	    	condition: "'{status}'=='UNABLE'",
	    	color: 'red'
	    }},
	    {key: 'desc', name: '备注', cutstring: true, textColor:{
	    	condition: "'{status}'=='UNABLE'",
	    	color: 'red'
	    }},
	    {
			key : 'operate',
			name : '操作',
			align : 'center',
			width: '60px',
			items : [ {
				className : 'btn-delete',
				text : '删除',
				show : true,
				callback : {
					confirm : {
						title : '删除',
						content : '确认删除？'
					},
					action : 'deleteItem',
					params : [ '{activityProductId}', vm.activityId, vm.periodsId]
				}
			} ]
		}
	    
	    ]
	vm.checkflag = true;
	vm.pageflag = false;
	vm.refresh = true;
};

var deleteItem = function(index, params){
	syncData(ykyUrl.product + "/v1/activities/"+params[0]+"/delete?activityId="+params[1]+"&periodsId="+params[2], 'DELETE', null, function(res,err) {
		if (!err) {
			layer.msg('删除成功');
			vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus;
		}
	})
};

function changeCurrency(index,e){
	if(e[0]){
		var upperStr = e[0].toUpperCase();
		
		if(upperStr === 'CNY'){
			upperStr = 'RMB';
		}
		return upperStr;
	}	
}

var uploader = createUploader({
	buttonId: "uploadBtn", 
	uploadType: "productUpload.activityProductsUpload", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "xls,xlsx,csv",//可允许上传文件的类型
	fileSize: "5mb", //最多允许上传文件的大小
	isImage: false, //是否是图片
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.fileName = file.name;
				vm.fileUrl = _fileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploader.init(); 
