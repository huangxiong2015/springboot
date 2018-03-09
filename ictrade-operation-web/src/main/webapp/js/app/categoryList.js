var validate;
var vm= new Vue({
    el: "#manufacturer-list",                    //该ID标签的范围，必须包含实例中的方法和属性的所属标签，一般将改ID绑定到class=“content-wrapper”上
    data: {
    	categoryTypeId:'',
    	categoryId: '',
    	categoryName: '',
    	categoryImageUrlBig: '',
    	categoryImageUrlSmall: '',
    	fileBigName: '',
    	fileSmallName: '',
    	url: ykyUrl.database + '/v1/category/findPageList',  //访问数据接口
        queryParams: {                //搜索字段及分页字段
        	defaultStatus:  false,     //判断是否修改搜索条件
            size: 10,
            page: 1,
        },                            //请求接口参数
        gridColumns: [  
			{key: 'sequenceNum', name: '序号',  align:'center', width:'200px'},
			{key: 'categoryId', name: '分类ID', align:'center', default: '--'},
			{key: 'categoryTypeId', name: '分类类型ID',  align:'center'},
			{key: 'categoryName', name: '分类名称',  align:'center'},
			{key: 'operate', name: '操作', align:'center', items:[  
			    {
			        className : 'btn-edit',
			        text: '编辑',
			        show : true,
			        href : ykyUrl._this + '/category/edit.htm?id={categoryId}',
			        target: '_self'
			    },
			    {
			        className : 'btn-delete',
			        text: '删除',
			        show : true,
			        callback : {
						confirm : {
							title : '删除',
							content : '确认删除？'
						},
						action : 'deleteCategory',
						params : [ '{categoryId}' ]
					}
			    }
			 ]
			}
        ],        //此处内容必须参照返回数据来修改
        refresh: false,
        pageflag: true
    },
    methods: {

         /*搜索方法*/
        onSearch: function () {        //该方法表单提交时绑定，如：<form id="seachForm" @submit.prevent="onSearch">
            var that = this;
            if($.trim($("#searchCategoryName").val()) !== ''){
            	vm.queryParams.categoryName = $("#searchCategoryName").val();
            }else{
            	delete vm.queryParams.categoryName;
            }
            
            if($.trim($("#searchCateTypeId").val()) !== ''){
            	vm.queryParams.categoryTypeId = $("#searchCateTypeId").val();
            }else{
            	delete vm.queryParams.categoryTypeId;
            }
            vm.queryParams.page = 1;
            vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus;//判断是否修改搜索条件
        },
        addCategory: function(){
        	var that = this;
        	var postData = {};
        	layer.open({
				type: 1,
				title: '添加分类',
				area: '700px',
				offset: '300px',
				move: false,
				skin: "s_select_data",
				content: $("#addCategory"),
				btn: ['保存', '取消'],
				yes: function(index, layero) {
					$("#addCategory form").submit();
					if(!validate){
						return ;
					}
					postData.categoryId = vm.categoryId;
					postData.categoryName = vm.categoryName;
					postData.categoryTypeId = vm.categoryTypeId;
					postData.categoryImageUrlSmall = vm.categoryImageUrlSmall;
					postData.categoryImageUrlBig = vm.categoryImageUrlBig;
					syncData(ykyUrl.database+'/v1/category/add', 'POST', postData,function(res,err){
						if(!err){
							vm.categoryId = '';
							vm.categoryName='';
							vm.categoryTypeId='';
							vm.categoryImageUrl='';
							layer.closeAll();
							vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus;
						}
					})
				},
				cancel: function(index, layero) {
					vm.categoryId = '';
					vm.categoryName='';
					vm.categoryTypeId='';
					vm.categoryImageUrl='';
				}
			})
        }
    }
})

function deleteCategory(index, params){
	syncData(ykyUrl.database+'/v1/category/delete?categoryId=' + params[0], 'GET', null,function(res,err){
		if(!err){
			layer.msg('删除成功')
			vm.queryParams.defaultStatus = !vm.queryParams.defaultStatus;
		}
	})
}

$("#addCategory form").validate({ //jquery validate 校验
	rules: {
		categoryId: {
			required: true
		},
		categoryTypeId: {
			required: true
		},
		categoryName: {
			required: true
		}
	},
	messages: {
		categoryId: {
			required: '分类ID不能为空'
		},
		categoryTypeId: {
			required: '分类类型ID不能为空'
		},
		categoryName: {
			required: '分类名称不能为空'
		}
	},
	errorElement: "em",
	success: function(label) {
		$(label).parent().find('.tip').remove();
		$(label).removeClass("error icon-triangle-leftmin"); // speech-bubble speech-bubble-left

		$(label).addClass("icon-confirm");
		if ($(label).parent().find('input').val() === '') {
			$(label).removeClass("icon-confirm");
		}
	},
	errorPlacement: function(error, element) {
		var f = error[0].getAttribute("for");
		var ele = $("em[for=" + f + "]");
		if (ele.length > 0) {
			ele[0].remove();
		}
		element.parent().find('.tip').remove();
		error.appendTo(element.parent());
		$(error).addClass("error icon-triangle-leftmin");
	},
	submitHandler: function(form) { //表单提交是校验
		validate = true;
	}
});

var uploaderBig = createUploader({
	buttonId: "uploadBtnBig", 
	uploadType: "categoryUpload.categoryTypeUpload", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "jpg,png",//可允许上传文件的类型
	fileSize: "5mb", //最多允许上传文件的大小
	isImage: true, //是否是图片
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.fileBigName = file.name;
				vm.categoryImageUrlBig = _yetAnotherFileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploaderBig.init(); 
var uploaderSmall = createUploader({
	buttonId: "uploadBtnSmall", 
	uploadType: "categoryUpload.categoryTypeUpload", //上传后文件存放路径
	url:  ykyUrl.webres,
	types: "jpg,png",//可允许上传文件的类型
	fileSize: "5mb", //最多允许上传文件的大小
	isImage: true, //是否是图片
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.fileSmallName = file.name;
				vm.categoryImageUrlSmall = _yetAnotherFileUrl;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploaderSmall.init(); 