var vm = '';
$(function(){
	 
     vm = new Vue({
        el: '#recuit-list',
        data: { 
            url : ykyUrl.info + "/v1/news?categoryTypeIdStr=RECRUITMENT", //访问数据接口 
            queryParams : {           //请求接口参数 
            	pageSize: 10,        //分页参数
                page:1,              //当前页
                defaultStatus:true,  //监测参数变化标识
                status:'',
                categoryId:''
            },
            gridColumns: [          //表格列
                {key: 'index', name: '序号',width: '50px'},
                {key: 'categoryName', name: '类型', cutstring: true},
                {key: 'title', name: '职位',cutstring: true}, 
                {key: 'regionCategoryName', name: '工作地点',cutstring: true},
                {key: 'extend', name: '招聘人数',width: '80px',default : '--',},
                {key: 'lastUpdateDate', name: '更新时间',cutstring: true},
                {key: 'publishDate', name: '发布时间', align: 'center', cutstring: true,callback: 
	            	{ 
	            		action: 'getText',
	            		params: ['{status}', '{publishDate}']
	            	}
	            },
                {key: 'author', name: '创建人', width: '80px', cutstring: true},
                {
					key : 'status',
					align : 'center',
					name : '状态',
					align : 'center',
					width: '80px',
					text : {
						PUBLISHED : {
							'color' : 'green',
							'value' : '已发布'
						},
						DRAFT : {
							'color' : 'black',
							'value' : '草稿'
						},
						HOLD : {
							'color' : 'red',
							'value' : '停用'
						}
					}
				},
				{
					key : 'operate',
					name : '操作',
					align : 'center',
					width: '150px',
					items : [ {
						className : 'btn-detail',
						text : '预览',
						show : true,
						href : ykyUrl.portal + '/about.htm?action=recruit',
						target : '_blank'
					}, {
						className : 'btn-delete',
						show : "'{status}'=='PUBLISHED'",
						text : '停用',
						callback : {
							confirm : {
								title : '停用',
								content : '确认停用？'
							},
							action : 'doChangeStatus',
							params : [ '{newsId}', 'HOLD' ]
						}
					}, {
						className : 'btn-delete',
						show : "'{status}'!='PUBLISHED'",
						text : '发布',
						callback : {
							confirm : {
								title : '发布',
								content : '确认发布？'
							},
							action : 'doChangeStatus',
							params : [ '{newsId}', 'PUBLISHED' ]
						}
					}, {
						className : 'btn-edit',
						text : '编辑',
						show : " '{status}'!='PUBLISHED' ",
						href : ykyUrl._this+'/recruit/edit.htm?id={newsId}',
					}, {
						className : 'btn-delete',
						text : '删除',
						show : "'{status}'=='DRAFT' || '{status}'=='HOLD'",
						callback : {
							confirm : {
								title : '删除',
								content : '确认删除？'
							},
							action : 'delFunc',
							params : [ '{newsId}' ]
						}
					} ]
				}
            ], 
            pageflag : true,    //是否显示分页
            refresh: false ,    //重载 
           
            selectUrl :ykyUrl.database + '/v1/category/companylist?categoryTypeId=OCCUPATION_TYPE',    //数据接口，必须
            id : 'categoryId',        //设置控件ID ，必须
            name : 'categoryName',        //设置控件name ，必须
            inputClass : '',    //设置下拉选项的class，可选
            styleObject: ''    //设置style，可选

        }, 
        created(){
        	  var that = this;
        	  var qModel = parseUrlParam();
        	  for(var i in qModel){
                  that.queryParams[i] = qModel[i];
              } 
              this.queryParams.defaultStatus = !this.queryParams.defaultStatus;
              //this.queryParams.categoryId  =  !this.queryParams.categoryId;
        }
    }); 
     
     
     
     
});

function getText(index, params){ 
	var str='';
	if(params[0]=='HOLD' || params[0]=='DRAFT') 
		str='--'
	else
		str= params[1]
	return str;
}
function delFunc(index, params) {//删除方法
	syncData(ykyUrl.info + "/v1/news/" + params[0], 'DELETE', null,
			function(res, err) {//页面加载前调用方法
				console.log(res);
				window.setTimeout(function() {
					vm.refresh = !vm.refresh;//重载
				}.bind(vm), 400);
				layer.close(index);
			});
	console.log(params);
}

function doChangeStatus(index, params) {//发布
	syncData(ykyUrl.info + "/v1/news/" + params[0] + "/" + params[1], 'PUT',
			null, function(res, err) {//页面加载前调用方法
				console.log(res);
				window.setTimeout(function() {
					vm.refresh = !vm.refresh;//重载
				}.bind(vm), 400);
				layer.close(index);
			});
	console.log(params);
}