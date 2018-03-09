/**
 * create by roy.he@yikuyi.com
 * 
 */

var vm = new Vue({
	el: '#categoryAlias',
	data: {
		fileUrl: '',
		fileName: '',
		errData: [],
	},
	methods: {
		download: function(){
			var date_url = ykyUrl._this + "/category/excel/down.htm";
			//var date_url = ykyUrl._this + "/customers/excel.htm?status=PARTY_ENABLED";
			$("input[name=Authorization]").val($('#pageToken').val());
		    var form=$("#exportForm");//定义一个form表单
		    form.attr("action",date_url);
		    form.submit();//表单提交
		},
		uploaderCategory: function(){
			var _this = this;
			if(_this.fileUrl === ''){
				layer.msg('请上传文件')
				return false;
			}
			$('.btn-danger').attr('disabled', true);
			syncData(ykyUrl.product + "/v1/products/categories/import?fileUrl="+this.fileUrl+"&oriName="+this.fileName, "POST", null, function (data, err) {
                if (!err) {
                	$('.btn-danger').removeAttr('disabled')
                    if(data.length > 0){
                    	_this.errData = data;
                    	layer.open({
    		        		type: 1,
    		        		title: '提示',
    		        		area: ['400px', '500px'],
    		        		offset: '150px',
    		        		move: false,
    		        		skin: "s_select_data",
    		        		content: $(".alias-layer")
    		        	})
                    }else{
                    	_this.fileUrl = '';
                    	_this.fileName = '';
                    	layer.msg('上传成功')
                    }
                }
            });
		}
	}
})

var uploader = createUploader({
	buttonId: "uploadBtn", 
	uploadType: "productUpload.materialDetectionUpload", 
	url:  ykyUrl.webres,
	types: "xls,xlsx,csv,pdf,zip,rar",
	fileSize: "5mb",
	isImage: true, 
	init:{
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.fileUrl = signatureData.accessUrl;
				vm.fileName = file.name;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploader.init();