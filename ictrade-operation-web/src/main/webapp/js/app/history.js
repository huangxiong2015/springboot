var vm = '';
$(function() {	
	vm = new Vue(
			{
				el : '#history-list',
				data : {
					url : ykyUrl.product+"/v1/imports/history", //访问数据接口 
					queryParams : { //请求接口参数 
						size : 20, //分页参数
						page : 1, //当前页
						docTypeId : 'VENDOR_SPU_DOCUMENT',						
						defaultStatus : true //监测参数变化标识
					},
					gridColumns : [ //表格列
					{
						key : 'createdDate',
						align : 'center',
						name : '上传时间',
						default : '-',
						cutstring: true
					}, 
					{
						key : 'documentName',
						align : 'center',
						name : '文件名称',
						default : '-',
						cutstring: true,						
					}, 
					{
						key : 'statusId',
						align : 'center',
						cutstring: true,
						default : '-',
						name : '处理结果',
						text : {
							DOC_PRO_SUCCESS : {
								'value': '全部成功'
							},
							DOC_PRO_PART_SUCCESS : {
								'value': '部分成功'
							},
							DOC_PRO_FAILED : {
								'value': '全部失败'
							},
							DOC_CREATED : {
								'value': '正在处理'
							},
							DOC_IN_PROCESS : {
								'value': '正在处理'
							},
							DOC_LOCKED : {
								'value': '正在处理'
							}
						}
					},
					{
						key : 'comments',
						align : 'center',
						cutstring: true,
						name : '原因',
						default : '-'						
					}, 
					{
						key : 'operation',
						align : 'center',
						name : '操作',
						default : '-',
						render:function(h,params){
							var rowData = params.row,
								statusId = rowData.statusId,
								location = rowData.logLocation?rowData.logLocation:"",
								documentName = rowData.documentName,
								flag = rowData.comments?rowData.comments:"",
								str;
							if(statusId == 'DOC_PRO_PART_SUCCESS' || statusId == 'DOC_PRO_FAILED' && flag.indexOf("com.ykyframework.exception.SystemException") < 0){
								str = '下载错误文件';
								return h('a',{
									'class': {
										cutstring: true
	                                },
	                                style: {
	                                	cursor: 'pointer'
	                                },
	                                attrs: {
	                                    title: str,
	                                    href:location,
	                                    /*download:'Errorlog.csv'*/
	                                  },
	                                /*on: {
	                            	    click: function(){
	                            	    	return downloadErrorFile(rowData.id);
	                            	    }
	                            	  },*/
	                            },str)
							}else{
								str = '-'
								return h('span',{
									'class': {
										cutstring: true
										
	                                },
	                                attrs: {
	                                    title: str
	                                  }
	                            },str)
							}							
							
						}
					},
					{
						key : 'creator',
						align : 'center',
						cutstring: true,
						name : '操作人',
						default : '-'
					}],
					pageflag : true, //是否显示分页
					showTotal: true, //显示列表总数
					refresh : false
				//重载 

				},
				
			});

		
});

//下载错误文件
function downloadErrorFile(id) {
	$("input[name='Authorization']").val($("#pageToken").val());
	$("#docId").val(id);
	$("#exportForm").submit();
}



