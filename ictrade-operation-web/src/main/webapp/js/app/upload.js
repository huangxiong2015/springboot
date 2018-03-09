var	vm = new Vue({
        el: '#upload',
        data: {
        	isUploading:false,
        	isFinish:false,
        	isSave:false,
        	fileName:''
        },
        methods: {
        	saveData:function(){
        		var _this = this;
        		if(!_this.isFinish || _this.isSave){
    				return;
    			}
        		var data = {
        				fileUrl: _fileUrl,
    					oriFileName: _this.fileName 
        			};
        		syncData(ykyUrl.product + '/v1/basicmaterial/notification/upload', 'POST', data, function(data){ 
        			if(data != null){
        				_this.isSave = true;
        				layer.msg("物料资料已保存成功！");
    					setTimeout(function(){
    						location.href = ykyUrl._this + '/basicMaterial/history.htm';
    						},2000);
        			}
        		})
        	}
        },
     
    });

var uploader = createUploader({
	buttonId: "file", 
	uploadType: "productUpload.basicMaterialUpload", 
	url:  ykyUrl.webres,
	types: "csv,xlsx,xls",
	fileSize: "10mb",
	isImage: false, 
	init:{
		BeforeUpload : function(up, file) {
			vm.isUploading = true;
			//$(".init_tips").css("display","none");
			$("#fileName").text(file.name);
			//$("#progressBar").css("display","inline-block");
			//$(".doing_tips").css("display","inline-block");	
			$( "#progressBar" ).progressbar({
			      value: Number(0)
			    });
			$("#showPercent").text(0+'%')
		},
		UploadProgress : function(up, file) {
			$( "#progressBar" ).progressbar({
			      value: Number(file["percent"])
			    });
			$("#showPercent").text(file["percent"]+'%');
		},
		FileUploaded : function(up, file, info) { 
            layer.close(index);
			if (info.status == 200 || info.status == 203) {
				vm.isUploading = false;
				vm.isFinish = true;
	        	vm.fileName = file.name;
				//$(".finish_tips").prev().css("display","none");
				//$(".finish_tips").css("display","block");
				vm.btnStatus = true;
			} else {
				layer.msg("上传文件失败,请重新上传！",{icon:2,offset:120}); 
			}
			up.files=[];
		}
	}
}); 
uploader.init();